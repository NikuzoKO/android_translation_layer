package android.atl;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stand-in for Android's MediaStore (authority "media").
 *
 * By default it serves the image/video/audio files found in the user's XDG media
 * directories, like a real MediaStore would. Apps query MediaStore in the background
 * (e.g. "recent media" suggestions), so opening a file chooser per query is not an option.
 * The old behaviour (every query opens a file chooser) is available with ATL_MEDIA_CHOOSER=1.
 */
public class ATLMediaContentProvider extends ContentProvider {

	private static final boolean USE_CHOOSER = System.getenv("ATL_MEDIA_CHOOSER") != null;
	private static final String[] DEFAULT_PROJECTION = {
		"_id", "_data", "_display_name", "title", "mime_type", "media_type", "_size",
		"date_added", "date_modified", "datetaken", "orientation", "width", "height", "duration",
		"bucket_id", "bucket_display_name", "relative_path"};
	private static final int MAX_DEPTH = 4;
	private static final long RESCAN_INTERVAL_MS = 5000;

	boolean waitingForFileChooser = false;
	File selectedFile = null;
	long timestamp = 0;

	private final Map<Long, File> filesById = new HashMap<>();
	private List<File> scanned = new ArrayList<>();
	private long lastScan = 0;

	// called from native
	void setSelectedFile(String selectedFile) {
		this.selectedFile = selectedFile == null ? null : new File(selectedFile);
		this.waitingForFileChooser = false;
		this.timestamp = System.currentTimeMillis();
		synchronized (this) {
			notifyAll();
		}
	}

	private void openFileChooser() {
		if (!waitingForFileChooser) {
			waitingForFileChooser = true;
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					native_open_media_folder();
				}
			});
		}
		synchronized (this) {
			try {
				while (waitingForFileChooser) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<File> mediaRoots() {
		String home = System.getProperty("user.home", System.getenv("HOME"));
		List<File> roots = new ArrayList<>();
		for (String name : new String[] {"Pictures", "Videos", "Music", "Downloads", "DCIM"}) {
			File dir = new File(home, name);
			if (dir.isDirectory())
				roots.add(dir);
		}
		return roots;
	}

	private static void scanDir(File dir, int depth, List<File> out) {
		File[] children = dir.listFiles();
		if (children == null)
			return;
		for (File f : children) {
			if (f.getName().startsWith("."))
				continue;
			if (f.isDirectory()) {
				if (depth < MAX_DEPTH)
					scanDir(f, depth + 1, out);
			} else if (mediaType(mimeOf(f)) != 0) {
				out.add(f);
			}
		}
	}

	private synchronized List<File> scan() {
		long now = System.currentTimeMillis();
		if (now - lastScan > RESCAN_INTERVAL_MS) {
			List<File> files = new ArrayList<>();
			for (File root : mediaRoots())
				scanDir(root, 0, files);
			// newest first, like the usual "date_modified DESC"
			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File a, File b) {
					return Long.compare(b.lastModified(), a.lastModified());
				}
			});
			filesById.clear();
			for (File f : files)
				filesById.put(idOf(f), f);
			scanned = files;
			lastScan = now;
		}
		return scanned;
	}

	private static long idOf(File f) {
		return f.getAbsolutePath().hashCode() & 0x7fffffffL;
	}

	private static String mimeOf(File f) {
		String mime = null;
		try {
			mime = Files.probeContentType(f.toPath());
		} catch (IOException e) {
		}
		if (mime != null)
			return mime;
		String name = f.getName().toLowerCase(Locale.ROOT);
		int dot = name.lastIndexOf('.');
		String ext = dot < 0 ? "" : name.substring(dot + 1);
		switch (ext) {
			case "jpg": case "jpeg": return "image/jpeg";
			case "png": return "image/png";
			case "gif": return "image/gif";
			case "webp": return "image/webp";
			case "heic": return "image/heic";
			case "mp4": return "video/mp4";
			case "webm": return "video/webm";
			case "mkv": return "video/x-matroska";
			case "3gp": return "video/3gpp";
			case "mp3": return "audio/mpeg";
			case "ogg": case "opus": return "audio/ogg";
			case "m4a": return "audio/mp4";
			case "flac": return "audio/flac";
			case "wav": return "audio/wav";
			default: return "application/octet-stream";
		}
	}

	/* MediaStore.Files.FileColumns.MEDIA_TYPE_* */
	private static int mediaType(String mime) {
		if (mime.startsWith("image/"))
			return 1;
		if (mime.startsWith("audio/"))
			return 2;
		if (mime.startsWith("video/"))
			return 3;
		return 0;
	}

	/* content://media/external/{images,video,audio,file}/media[/id] */
	private static int wantedMediaType(Uri uri) {
		for (String segment : uri.getPathSegments()) {
			switch (segment) {
				case "images": return 1;
				case "audio": return 2;
				case "video": return 3;
			}
		}
		return 0; // any
	}

	private static long idFromUri(Uri uri) {
		String last = uri.getLastPathSegment();
		if (last == null)
			return -1;
		try {
			return Long.parseLong(last);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private static int limitOf(Uri uri, String sortOrder) {
		String limit = uri.getQueryParameter("limit");
		if (limit == null && sortOrder != null) {
			Matcher m = Pattern.compile("(?i)\\blimit\\s+(\\d+)").matcher(sortOrder);
			if (m.find())
				limit = m.group(1);
		}
		try {
			return limit != null ? Integer.parseInt(limit.trim()) : Integer.MAX_VALUE;
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}

	private static Object column(String name, File f, String mime) {
		File parent = f.getParentFile();
		switch (name) {
			case "_id": return idOf(f);
			case "_data": return f.getAbsolutePath();
			case "_display_name": return f.getName();
			case "title": {
				String n = f.getName();
				int dot = n.lastIndexOf('.');
				return dot > 0 ? n.substring(0, dot) : n;
			}
			case "mime_type": return mime;
			case "media_type": return mediaType(mime);
			case "_size": return f.length();
			case "date_added":
			case "date_modified": return f.lastModified() / 1000; // seconds
			case "datetaken": return f.lastModified();           // milliseconds
			case "orientation":
			case "width":
			case "height":
			case "duration": return 0;
			case "bucket_id": return parent != null ? (long)(parent.getAbsolutePath().hashCode() & 0x7fffffff) : 0L;
			case "bucket_display_name": return parent != null ? parent.getName() : "";
			case "relative_path": return parent != null ? parent.getName() + "/" : "";
			case "count": return 1;
			default: return null;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		if (projection == null)
			projection = DEFAULT_PROJECTION;
		if (USE_CHOOSER)
			return chooserQuery(uri, projection, selectionArgs);

		List<File> files = new ArrayList<>();
		long id = idFromUri(uri);
		if (id >= 0) {
			scan();
			File f = filesById.get(id);
			if (f != null)
				files.add(f);
		} else if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0] != null
		           && selectionArgs[0].startsWith("/") && new File(selectionArgs[0]).isFile()) {
			files.add(new File(selectionArgs[0])); // lookup by _data path
		} else {
			int wanted = wantedMediaType(uri);
			for (File f : scan())
				if (wanted == 0 || mediaType(mimeOf(f)) == wanted)
					files.add(f);
		}

		MatrixCursor cursor = new MatrixCursor(projection);
		if (uri.getQueryParameter("distinct") != null) {
			// one row per bucket (directory)
			Map<File, File> buckets = new LinkedHashMap<>();
			for (File f : files)
				if (!buckets.containsKey(f.getParentFile()))
					buckets.put(f.getParentFile(), f);
			files = new ArrayList<>(buckets.values());
		}
		int limit = limitOf(uri, sortOrder);
		for (File f : files) {
			if (cursor.getCount() >= limit)
				break;
			String mime = mimeOf(f);
			Object[] row = new Object[projection.length];
			for (int i = 0; i < projection.length; i++)
				row[i] = column(projection[i], f, mime);
			cursor.addRow(row);
		}
		return cursor;
	}

	/* the original behaviour: every query opens a file chooser and returns the chosen file */
	private Cursor chooserQuery(Uri uri, String[] projection, String[] selectionArgs) {
		if (selectionArgs != null && selectionArgs.length > 0) {
			selectedFile = new File(selectionArgs[0]);
			timestamp = System.currentTimeMillis();
		}
		if (!"0".equals(uri.getLastPathSegment()) && timestamp + 1000 < System.currentTimeMillis()) {
			openFileChooser();
		}
		MatrixCursor cursor = new MatrixCursor(projection);
		// nothing chosen (chooser cancelled): no rows instead of rows full of nulls
		if (selectedFile == null)
			return cursor;
		String mime = mimeOf(selectedFile);
		Object[] row = new Object[projection.length];
		for (int i = 0; i < projection.length; i++)
			row[i] = "_id".equals(projection[i]) ? 0 : column(projection[i], selectedFile, mime);
		cursor.addRow(row);
		return cursor;
	}

	private File fileForUri(Uri uri) {
		long id = idFromUri(uri);
		if (id >= 0 && !USE_CHOOSER) {
			scan();
			File f = filesById.get(id);
			if (f == null) {
				lastScan = 0; // maybe new, rescan once
				scan();
				f = filesById.get(id);
			}
			if (f != null)
				return f;
		}
		return selectedFile;
	}

	@Override
	public String getType(Uri uri) {
		File f = fileForUri(uri);
		return f != null ? mimeOf(f) : "application/octet-stream";
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		File f = fileForUri(uri);
		if (f == null)
			throw new FileNotFoundException("no media file for " + uri);
		return ParcelFileDescriptor.open(f, ParcelFileDescriptor.parseMode(mode));
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'insert'");
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'update'");
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}

	private native void native_open_media_folder();

	@Override
	public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
		ParcelFileDescriptor pfd = openFile(uri, mode);
		return new AssetFileDescriptor(pfd, 0, AssetFileDescriptor.UNKNOWN_LENGTH);
	}
}

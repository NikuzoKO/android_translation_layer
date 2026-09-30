package android.atl;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Slog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Stand-in for Android's contacts provider (authority "com.android.contacts"), read-only.
 *
 * The address book is a plain text file, one "number<TAB>name" per line (the format of
 * sxmo's contacts.tsv): $ATL_CONTACTS_FILE, else $SXMO_CONTACTFILE, else
 * $XDG_CONFIG_HOME/sxmo/contacts.tsv. Lines sharing a name become one contact with several
 * numbers. The file is re-read when it changes.
 *
 * The rows live in an in-memory SQLite database with one wide table per ContactsContract
 * "view" (data, contacts, raw_contacts, phone_lookup), so whatever selection and sort
 * order an app sends runs as real SQL. Projection columns we don't have come back NULL.
 * Only reached when the app holds READ_CONTACTS (see PackageManager.checkPermission).
 */
public class ATLContactsContentProvider extends ContentProvider {
	private static final String TAG = "ATLContactsContentProvider";
	private static final boolean DEBUG = System.getenv("ATL_CONTACTS_DEBUG") != null;

	private static final String MIME_PHONE = "vnd.android.cursor.item/phone_v2";
	private static final String MIME_NAME = "vnd.android.cursor.item/name";
	private static final String ACCOUNT_TYPE = "atl.local";
	private static final String ACCOUNT_NAME = "Phone";

	/* every table has all of these, so one projection works against any of them */
	private static final String[] COLUMNS = {
		"_id INTEGER", "contact_id INTEGER", "raw_contact_id INTEGER", "name_raw_contact_id INTEGER",
		"lookup TEXT", "display_name TEXT", "display_name_alt TEXT", "display_name_source INTEGER",
		"sort_key TEXT", "sort_key_alt TEXT", "phonetic_name TEXT", "phonebook_label TEXT",
		"mimetype TEXT",
		"data1 TEXT", "data2 TEXT", "data3 TEXT", "data4 TEXT", "data5 TEXT", "data6 TEXT", "data7 TEXT",
		"data8 TEXT", "data9 TEXT", "data10 TEXT", "data11 TEXT", "data12 TEXT", "data13 TEXT",
		"data14 TEXT", "data15 BLOB",
		"data_version INTEGER", "is_primary INTEGER", "is_super_primary INTEGER",
		"has_phone_number INTEGER", "starred INTEGER", "pinned INTEGER", "times_contacted INTEGER",
		"last_time_contacted INTEGER", "times_used INTEGER", "last_time_used INTEGER",
		"custom_ringtone TEXT", "send_to_voicemail INTEGER",
		"photo_id INTEGER", "photo_file_id INTEGER", "photo_uri TEXT", "photo_thumb_uri TEXT",
		"in_visible_group INTEGER", "in_default_directory INTEGER",
		"is_user_profile INTEGER", "raw_contact_is_user_profile INTEGER",
		"account_type TEXT", "account_name TEXT", "data_set TEXT", "account_type_and_data_set TEXT",
		"sourceid TEXT", "version INTEGER", "dirty INTEGER", "deleted INTEGER",
		"contact_last_updated_timestamp INTEGER", "res_package TEXT", "carrier_presence INTEGER",
		"contact_presence INTEGER", "contact_chat_capability INTEGER", "contact_status TEXT",
		"contact_status_ts INTEGER", "contact_status_res_package TEXT", "contact_status_label INTEGER",
		"contact_status_icon INTEGER",
		"sync1 TEXT", "sync2 TEXT", "sync3 TEXT", "sync4 TEXT",
		"data_sync1 TEXT", "data_sync2 TEXT", "data_sync3 TEXT", "data_sync4 TEXT",
		/* phone_lookup */
		"number TEXT", "normalized_number TEXT", "type INTEGER", "label TEXT",
	};
	private static final String[] TABLES = {"data", "contacts", "raw_contacts", "phone_lookup"};
	private static final Set<String> COLUMN_NAMES = new HashSet<>();
	static {
		for (String column : COLUMNS)
			COLUMN_NAMES.add(column.substring(0, column.indexOf(' ')));
	}

	private SQLiteDatabase db;
	private File source;
	private long loadedStamp = -1;

	private static File sourceFile() {
		String path = System.getenv("ATL_CONTACTS_FILE");
		if (path == null)
			path = System.getenv("SXMO_CONTACTFILE");
		if (path == null) {
			String config = System.getenv("XDG_CONFIG_HOME");
			if (config == null)
				config = System.getProperty("user.home", System.getenv("HOME")) + "/.config";
			path = config + "/sxmo/contacts.tsv";
		}
		return new File(path);
	}

	private static String digits(String number) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < number.length(); i++) {
			char c = number.charAt(i);
			if (c >= '0' && c <= '9')
				out.append(c);
		}
		return out.toString();
	}

	/* E.164 if the number carries its country code, like android.telephony does; else null */
	private static String normalized(String number) {
		String trimmed = number.trim();
		return trimmed.startsWith("+") ? "+" + digits(trimmed) : null;
	}

	private synchronized SQLiteDatabase database() {
		if (db == null) {
			db = SQLiteDatabase.create(null);
			StringBuilder columns = new StringBuilder();
			for (String column : COLUMNS)
				columns.append(columns.length() == 0 ? "" : ", ").append(column);
			for (String table : TABLES)
				db.execSQL("CREATE TABLE " + table + " (" + columns + ")");
			source = sourceFile();
		}
		long stamp = source.isFile() ? source.lastModified() ^ source.length() : 0;
		if (stamp != loadedStamp) {
			loadedStamp = stamp;
			load();
		}
		return db;
	}

	private Map<String, List<String>> readSource() {
		Map<String, List<String>> contacts = new LinkedHashMap<>(); // name -> numbers
		if (!source.isFile())
			return contacts;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), "UTF-8"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				int tab = line.indexOf('\t');
				if (tab <= 0)
					continue;
				String number = line.substring(0, tab).trim();
				String name = line.substring(tab + 1).trim();
				if (number.isEmpty() || name.isEmpty())
					continue;
				List<String> numbers = contacts.get(name);
				if (numbers == null)
					contacts.put(name, numbers = new ArrayList<>());
				if (!numbers.contains(number))
					numbers.add(number);
			}
		} catch (Exception e) {
			Slog.e(TAG, "reading " + source, e);
		}
		return contacts;
	}

	private ContentValues common(long contactId, String name, long updated) {
		ContentValues v = new ContentValues();
		v.put("contact_id", contactId);
		v.put("raw_contact_id", contactId);
		v.put("name_raw_contact_id", contactId);
		// stable across reloads as long as the name is
		v.put("lookup", "atl" + Integer.toHexString(name.hashCode()));
		v.put("display_name", name);
		v.put("display_name_alt", name);
		v.put("display_name_source", 40 /* STRUCTURED_NAME */);
		v.put("sort_key", name);
		v.put("sort_key_alt", name);
		v.put("phonebook_label", name.substring(0, 1).toUpperCase());
		v.put("has_phone_number", 1);
		v.put("starred", 0);
		v.put("pinned", 0);
		v.put("times_contacted", 0);
		v.put("last_time_contacted", 0);
		v.put("times_used", 0);
		v.put("last_time_used", 0);
		v.put("send_to_voicemail", 0);
		v.put("in_visible_group", 1);
		v.put("in_default_directory", 1);
		v.put("is_user_profile", 0);
		v.put("raw_contact_is_user_profile", 0);
		v.put("account_type", ACCOUNT_TYPE);
		v.put("account_name", ACCOUNT_NAME);
		v.put("account_type_and_data_set", ACCOUNT_TYPE);
		v.put("version", 1);
		v.put("data_version", 1);
		v.put("dirty", 0);
		v.put("deleted", 0);
		v.put("contact_last_updated_timestamp", updated);
		v.put("carrier_presence", 0);
		return v;
	}

	private void load() {
		Map<String, List<String>> contacts = readSource();
		long updated = source.isFile() ? source.lastModified() : 0;
		db.beginTransaction();
		try {
			for (String table : TABLES)
				db.execSQL("DELETE FROM " + table);
			long contactId = 0, dataId = 0;
			for (Map.Entry<String, List<String>> contact : contacts.entrySet()) {
				contactId++;
				String name = contact.getKey();
				ContentValues base = common(contactId, name, updated);

				ContentValues row = new ContentValues(base);
				row.put("_id", contactId);
				db.insert("contacts", null, row);
				db.insert("raw_contacts", null, row);

				row = new ContentValues(base);
				row.put("_id", ++dataId);
				row.put("mimetype", MIME_NAME);
				row.put("data1", name);
				int space = name.lastIndexOf(' ');
				row.put("data2", space > 0 ? name.substring(0, space) : name); // given
				if (space > 0)
					row.put("data3", name.substring(space + 1)); // family
				row.put("is_primary", 1);
				row.put("is_super_primary", 1);
				db.insert("data", null, row);

				boolean first = true;
				for (String number : contact.getValue()) {
					row = new ContentValues(base);
					row.put("_id", ++dataId);
					row.put("mimetype", MIME_PHONE);
					row.put("data1", number);
					row.put("data2", 2 /* TYPE_MOBILE */);
					row.put("data4", normalized(number));
					row.put("is_primary", first ? 1 : 0);
					row.put("is_super_primary", first ? 1 : 0);
					db.insert("data", null, row);

					ContentValues lookup = new ContentValues(base);
					lookup.put("_id", contactId);
					lookup.put("number", number);
					lookup.put("normalized_number", normalized(number));
					lookup.put("type", 2);
					lookup.put("data1", digits(number)); // what phone_lookup matches on
					db.insert("phone_lookup", null, lookup);
					first = false;
				}
			}
			db.setTransactionSuccessful();
			Slog.i(TAG, contactId + " contacts from " + source);
		} finally {
			db.endTransaction();
		}
	}

	private static String and(String a, String b) {
		if (a == null || a.isEmpty())
			return b;
		if (b == null || b.isEmpty())
			return a;
		return "(" + a + ") AND (" + b + ")";
	}

	private static String[] concat(String[] first, String[] second) {
		if (first == null || first.length == 0)
			return second;
		if (second == null || second.length == 0)
			return first;
		String[] out = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, out, first.length, second.length);
		return out;
	}

	private static boolean isNumber(String s) {
		if (s == null || s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) < '0' || s.charAt(i) > '9')
				return false;
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		List<String> path = uri.getPathSegments();
		String table = null;
		String where = null;
		String[] whereArgs = null; // come before the caller's selectionArgs
		String first = path.isEmpty() ? "" : path.get(0);
		String second = path.size() > 1 ? path.get(1) : null;
		String third = path.size() > 2 ? path.get(2) : null;

		switch (first) {
			case "data":
				table = "data";
				if ("phones".equals(second) || "callables".equals(second)) {
					where = "mimetype='" + MIME_PHONE + "'";
					if ("filter".equals(third) && path.size() > 3) {
						String filter = path.get(3);
						String number = digits(filter);
						if (number.isEmpty()) {
							where = and(where, "display_name LIKE ?");
							whereArgs = new String[] {"%" + filter + "%"};
						} else {
							where = and(where, "(display_name LIKE ? OR replace(replace(data1,' ',''),'+','') LIKE ?)");
							whereArgs = new String[] {"%" + filter + "%", "%" + number + "%"};
						}
					} else if (isNumber(third)) {
						where = and(where, "_id=" + third);
					}
				} else if ("emails".equals(second) || "postals".equals(second)) {
					where = "0";
				} else if (isNumber(second)) {
					where = "_id=" + second;
				}
				break;
			case "contacts":
				table = "contacts";
				if ("lookup".equals(second) && third != null) {
					where = "lookup=?";
					whereArgs = new String[] {third};
					if (path.size() > 4 && "data".equals(path.get(4))) {
						table = "data";
					}
				} else if ("filter".equals(second)) {
					if (third != null) {
						where = "display_name LIKE ?";
						whereArgs = new String[] {"%" + third + "%"};
					}
				} else if (isNumber(second)) {
					if ("data".equals(third) || "entities".equals(third)) {
						table = "data";
						where = "contact_id=" + second;
					} else {
						where = "_id=" + second;
					}
				} else if (second != null) { // strequent, frequent, group, as_vcard...
					where = "0";
				}
				break;
			case "raw_contacts":
				table = "raw_contacts";
				if (isNumber(second)) {
					if ("data".equals(third) || "entity".equals(third)) {
						table = "data";
						where = "raw_contact_id=" + second;
					} else {
						where = "_id=" + second;
					}
				}
				break;
			case "raw_contact_entities":
				table = "data";
				break;
			case "phone_lookup":
			case "phone_lookup_enterprise":
				table = "phone_lookup";
				String wanted = second == null ? "" : digits(second);
				if (wanted.isEmpty()) {
					where = "0";
				} else {
					// like Android's loose matching: compare the trailing digits, so a number
					// with and without its country code or trunk prefix finds the same contact
					String tail = wanted.length() > 7 ? wanted.substring(wanted.length() - 7) : wanted;
					where = wanted.length() > 7 ? "data1 LIKE ?" : "data1=?";
					whereArgs = new String[] {wanted.length() > 7 ? "%" + tail : tail};
				}
				break;
			default: // profile, groups, settings, directories, status_updates...
				break;
		}

		if (DEBUG)
			Slog.i(TAG, "query " + uri + " projection=" + Arrays.toString(projection) + " selection=" + selection
			                + " args=" + Arrays.toString(selectionArgs) + " sort=" + sortOrder + " -> " + table + " where " + where);

		String[] names = projection;
		if (table == null) {
			if (names == null)
				names = new String[] {"_id"};
			return new MatrixCursor(names);
		}

		String columns = "*";
		if (projection != null && projection.length > 0) {
			StringBuilder list = new StringBuilder();
			for (String column : projection) {
				if (list.length() > 0)
					list.append(", ");
				// anything we don't carry reads as NULL instead of failing the whole query
				String plain = column.trim();
				if (COLUMN_NAMES.contains(plain) || plain.contains("(") || plain.toLowerCase().contains(" as "))
					list.append(plain);
				else
					list.append("NULL AS \"").append(plain.replace("\"", "")).append("\"");
			}
			columns = list.toString();
		}
		StringBuilder sql = new StringBuilder("SELECT ").append(columns).append(" FROM ").append(table);
		String fullWhere = and(where, selection);
		if (fullWhere != null && !fullWhere.isEmpty())
			sql.append(" WHERE ").append(fullWhere);
		if (sortOrder != null && !sortOrder.isEmpty())
			sql.append(" ORDER BY ").append(sortOrder);
		String limit = uri.getQueryParameter("limit");
		if (isNumber(limit))
			sql.append(" LIMIT ").append(limit);
		try {
			return database().rawQuery(sql.toString(), concat(whereArgs, selectionArgs));
		} catch (RuntimeException e) {
			Slog.e(TAG, "query failed: " + sql, e);
			return new MatrixCursor(names != null ? names : new String[] {"_id"});
		}
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public String getType(Uri uri) {
		List<String> path = uri.getPathSegments();
		String first = path.isEmpty() ? "" : path.get(0);
		boolean item = path.size() > 1 && isNumber(path.get(path.size() - 1));
		switch (first) {
			case "contacts":
				return item || path.contains("lookup") ? "vnd.android.cursor.item/contact" : "vnd.android.cursor.dir/contact";
			case "raw_contacts":
				return item ? "vnd.android.cursor.item/raw_contact" : "vnd.android.cursor.dir/raw_contact";
			case "data":
				if (path.size() > 1 && "phones".equals(path.get(1)))
					return item ? MIME_PHONE : "vnd.android.cursor.dir/phone_v2";
				return item ? "vnd.android.cursor.item/data" : "vnd.android.cursor.dir/data";
			case "phone_lookup":
				return "vnd.android.cursor.dir/phone_lookup";
			default:
				return null;
		}
	}

	/* read-only: the address book belongs to the host (sxmo edits contacts.tsv) */

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (DEBUG)
			Slog.i(TAG, "insert " + uri + " ignored");
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if (DEBUG)
			Slog.i(TAG, "update " + uri + " ignored");
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (DEBUG)
			Slog.i(TAG, "delete " + uri + " ignored");
		return 0;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		throw new FileNotFoundException("no contact photos: " + uri);
	}
}

package android.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public class MediaMetadataRetriever {

	private MediaPlayer mediaPlayer;

	public void release() {
		if (mediaPlayer != null)
			mediaPlayer.release();
	}

	public void setDataSource(Context context, Uri uri) {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(uri.getPath());
	}

	public void setDataSource(String path) {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(path);
	}

	public byte[] getEmbeddedPicture() {
		return null;
	}

	public String extractMetadata(int key) {
		switch (key) {
			case 9 /*METADATA_KEY_DURATION*/:
				return String.valueOf(mediaPlayer.getDuration());
			default:
				return null;
		}
	}

	public Bitmap getFrameAtTime(long time) {
		return null;
	}

	public Bitmap getFrameAtTime() {
		return null;
	}

	public android.graphics.Bitmap getFrameAtIndex(int arg0) { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public android.graphics.Bitmap getFrameAtTime(long arg0, int arg1) { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public android.graphics.Bitmap getScaledFrameAtTime(long arg0, int arg1, int arg2, int arg3) { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public void setDataSource(java.io.FileDescriptor arg0) {} // ATL-autostub
	public void setDataSource(java.io.FileDescriptor arg0, long arg1, long arg2) {} // ATL-autostub
	public void setDataSource(java.lang.String arg0, java.util.Map arg1) {} // ATL-autostub
}

package android.media;

import android.content.Context;
import android.net.Uri;
import java.io.FileDescriptor;
import java.util.Map;

public class MediaPlayer {
	private long gtk_media_stream;

	public interface OnCompletionListener {
		void onCompletion(MediaPlayer media_player);
	}
	public interface OnErrorListener {
	
	public default boolean onError(android.media.MediaPlayer arg0, int arg1, int arg2) { return false; } // ATL-autostub
}
	public interface OnPreparedListener {
	
	public default void onPrepared(android.media.MediaPlayer arg0) {} // ATL-autostub
}
	public interface OnBufferingUpdateListener {
	}
	public interface OnInfoListener {
	}
	public interface OnSeekCompleteListener {
	}
	public interface OnVideoSizeChangedListener {
	}
	public interface MediaPlayerControl {
	}

	public static MediaPlayer create(Context context, int dummy) { return new MediaPlayer(); }

	public void setDataSource(FileDescriptor src, long offset, long length) {}

	public void setDataSource(String path) {
		gtk_media_stream = native_setDataSource(path);
	}

	public void setDataSource(Context context, Uri uri) {
		System.out.println("setDataSource(" + uri + ") called");
		setDataSource(uri.getPath());
	}

	public void setDataSource(Context context, Uri uri, Map<String, String> headers) {
		System.out.println("setDataSource(" + uri + ") called");
		setDataSource(uri.getPath());
	}

	public void setLooping(boolean dummy) {}

	public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
		native_setOnCompletionListener(gtk_media_stream, listener);
	}

	public void setOnErrorListener(MediaPlayer.OnErrorListener dummy) {}
	public void setOnPreparedListener(MediaPlayer.OnPreparedListener dummy) {}
	public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener dummy) {}
	public void setOnInfoListener(MediaPlayer.OnInfoListener dummy) {}
	public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener dummy) {}
	public void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener dummy) {}
	public void setAudioAttributes(AudioAttributes attributes) {}
	public void setAudioStreamType(int dummy) {}

	public void start() {
		native_start(gtk_media_stream);
	}

	public void stop() {}
	public void pause() {}

	public void prepare() {
		native_prepare(gtk_media_stream);
	}

	public void prepareAsync() {}
	public void reset() {}
	public void release() {}

	public boolean isPlaying() { return false; }

	public void seekTo(int dummy) {}

	public void setVolume(float leftVolume, float rightVolume) {}

	public int getDuration() {
		return native_getDuration(gtk_media_stream);
	}

	public int getCurrentPosition() {
		return native_getCurrentPosition(gtk_media_stream);
	}

	public int getAudioSessionId() { return 0; }

	public void setWakeMode(Context context, int mode) {}

	public static native void native_prepare(long gtk_media_stream);
	public native long native_setDataSource(String path);
	public static native void native_setOnCompletionListener(long gtk_media_stream, MediaPlayer.OnCompletionListener listener);
	public static native void native_start(long gtk_media_stream);
	public static native int native_getDuration(long gtk_media_stream);
	public static native int native_getCurrentPosition(long gtk_media_stream);

	public int getVideoHeight() { return 0; } // ATL-autostub
	public int getVideoWidth() { return 0; } // ATL-autostub
	public void seekTo(long arg0, int arg1) {} // ATL-autostub
	public void setAudioSessionId(int arg0) {} // ATL-autostub
	public void setDataSource(java.io.FileDescriptor arg0) {} // ATL-autostub
	public void setDisplay(android.view.SurfaceHolder arg0) {} // ATL-autostub
	public void setScreenOnWhilePlaying(boolean arg0) {} // ATL-autostub
	public void setSurface(android.view.Surface arg0) {} // ATL-autostub
	public void setVideoScalingMode(int arg0) {} // ATL-autostub
}

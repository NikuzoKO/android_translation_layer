package android.media;

import android.atl.ATLLoadedApp;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SoundPool {

	private long nativePool;

	public interface OnLoadCompleteListener {
		/**
		 * Called when a sound has completed loading.
		 *
		 * @param soundPool SoundPool object from the load() method
		 * @param soundPool the sample ID of the sound loaded.
		 * @param status the status of the load operation (0 = success)
		 */
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status);
	}

	public SoundPool(int maxStreams, int streamType, int srcQuality) {
		nativePool = native_constructor();
	}

	public int load(AssetFileDescriptor afd, int priority) throws IOException {
		String packageCodePath = ATLLoadedApp.getPrimaryApplication().pkg.applicationInfo.sourceDir;
		AssetManager.extractFromAPK(packageCodePath, afd.fileName, afd.fileName);
		return nativeLoad(nativePool, Environment.getExternalStorageDirectory().getPath() + "/" + afd.fileName);
	}

	/* The resource's file inside the APK is not named after its entry name (WhatsApp's are
	 * res/<obfuscated>), so go through Resources to find it and copy it out once per package. */
	public int load(Context context, int resId, int priority) {
		File file = new File(context.getCacheDir(), "soundpool/" + Integer.toHexString(resId));
		try {
			if (!file.exists() || file.lastModified() < new File(context.getPackageCodePath()).lastModified()) {
				file.getParentFile().mkdirs();
				try (InputStream in = context.getResources().openRawResource(resId)) {
					Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			}
		} catch (Exception e) {
			System.err.println("SoundPool.load: resource 0x" + Integer.toHexString(resId) + " unavailable: " + e);
			return 0;
		}
		return nativeLoad(nativePool, file.getPath());
	}

	/**
	 * Sets the callback hook for the OnLoadCompleteListener.
	 */
	public void setOnLoadCompleteListener(OnLoadCompleteListener listener) {
		System.out.println("WARNING: SoundPool.setOnLoadCompleteListener not implemented yet");
	}

	public final int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) {
		return nativePlay(nativePool, soundID);
	}

	public void autoResume() {
		System.out.println("WARNING: SoundPool.autoResume not implemented yet");
	}

	public void autoPause() {
		System.out.println("WARNING: SoundPool.autoPause not implemented yet");
	}

	public void setVolume(int streamType, float leftVolume, float rightVolume) {
		System.out.println("WARNING: SoundPool.setVolume not implemented yet");
	}

	public class Builder {
		public Builder setAudioAttributes(AudioAttributes attributes) {
			return this;
		}

		public Builder setMaxStreams(int maxStreams) {
			return this;
		}

		public SoundPool build() {
			return new SoundPool(0, 0, 0); // FIXME
		}
	}

	public void release() {}

	private static native long native_constructor();
	private static native int nativeLoad(long nativePool, String path);
	public static native int nativePlay(long nativePool, int soundID);

	public void stop(int arg0) {} // ATL-autostub
}

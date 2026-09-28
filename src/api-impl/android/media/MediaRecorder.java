package android.media;

import android.content.Context;

public class MediaRecorder {
	public class AudioEncoder {
		public static final int AAC = 3;
		public static final int OPUS = 7;
		public static final int VORBIS = 7;
	}

	public class AudioSource {
		public static final int DEFAULT = 0;
		public static final int MIC = 1;
	}

	public class MetricsConstants {}
	public class OutputFormat {
		public static final int AAC_ADTS = 6;
		public static final int DEFAULT = 0;
		public static final int OGG = 11;
	}
	public class VideoEncoder {}
	public class VideoSource {}

	public interface OnErrorListener {}
	public interface OnInfoListener {}

	public MediaRecorder() {}
	public MediaRecorder(Context context) {}

	private int audioSource;
	private int outputFormat;
	private int audioEncoder;

	public void setAudioSource(int audioSource) {
		this.audioSource = audioSource;
	}

	public void setOutputFormat(int outputFormat) {
		this.outputFormat = outputFormat;
	}

	public void setAudioEncoder(int audioEncoder) {
		this.audioEncoder = audioEncoder;
	}

	public int getMaxAmplitude() {
		return 0;
	}

	public void setAudioEncodingBitRate(int audioEncodingBitrate) {}
	public void setAudioSamplingRate(int setAudioSamplingRate) {}
	public void setOutputFile(String filePath) {}
	public void prepare() {}
	public void start() {}
	public void stop() {}
	public void reset() {
		audioSource = 0;
		outputFormat = 0;
		audioEncoder = 0;
	}
	public void resume() {}
	public void pause() {}
	public void release() {}

	public android.view.Surface getSurface() { return ((Object)this) instanceof android.view.Surface ? (android.view.Surface)(Object)this : null; } // ATL-autostub
	public void setAudioChannels(int arg0) {} // ATL-autostub
	public void setCamera(android.hardware.Camera arg0) {} // ATL-autostub
	public void setOnErrorListener(android.media.MediaRecorder.OnErrorListener arg0) {} // ATL-autostub
	public void setOnInfoListener(android.media.MediaRecorder.OnInfoListener arg0) {} // ATL-autostub
	public void setOrientationHint(int arg0) {} // ATL-autostub
	public void setOutputFile(java.io.FileDescriptor arg0) {} // ATL-autostub
	public void setPreviewDisplay(android.view.Surface arg0) {} // ATL-autostub
	public void setVideoEncoder(int arg0) {} // ATL-autostub
	public void setVideoEncodingBitRate(int arg0) {} // ATL-autostub
	public void setVideoEncodingProfileLevel(int arg0, int arg1) {} // ATL-autostub
	public void setVideoFrameRate(int arg0) {} // ATL-autostub
	public void setVideoSize(int arg0, int arg1) {} // ATL-autostub
	public void setVideoSource(int arg0) {} // ATL-autostub
}

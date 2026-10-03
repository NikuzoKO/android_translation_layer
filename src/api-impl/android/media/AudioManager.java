package android.media;

import android.os.Handler;

public class AudioManager {
	public static final String PROPERTY_OUTPUT_FRAMES_PER_BUFFER = "android.media.property.OUTPUT_FRAMES_PER_BUFFER";
	public static final String PROPERTY_OUTPUT_SAMPLE_RATE = "android.media.property.OUTPUT_SAMPLE_RATE";

	public static final int STREAM_MUSIC = 0x3;

	private native void nativeSetStreamVolume(int volume);
	private native int nativeGetStreamVolume();

	public static final int RINGER_MODE_NORMAL = 2;

	public boolean isBluetoothA2dpOn() {
		return false;
	}

	public String getProperty(String name) {
		switch (name) {
			case PROPERTY_OUTPUT_FRAMES_PER_BUFFER:
				return "256"; // FIXME arbitrary
			case PROPERTY_OUTPUT_SAMPLE_RATE:
				return "44100"; // FIXME arbitrary
			default:
				System.out.println("AudioManager.getProperty: >" + name + "< not handled");
				return "";
		}
	}

	public interface OnAudioFocusChangeListener {
	
	public default void onAudioFocusChange(int arg0) {} // ATL-autostub
}

	public static final int GET_DEVICES_INPUTS = 0x0001;
	public static final int GET_DEVICES_OUTPUTS = 0x0002;
	public static final int GET_DEVICES_ALL = GET_DEVICES_OUTPUTS | GET_DEVICES_INPUTS;

	public int getRingerMode() {
		return RINGER_MODE_NORMAL; // 0 is RINGER_MODE_SILENT
	}

	// every stream reports the system (ALSA "Master") volume
	public int getStreamVolume(int streamType) {
		int volume = nativeGetStreamVolume();
		return volume < 0 ? getStreamMaxVolume(streamType) / 2 : volume;
	}

	public int getStreamMaxVolume(int streamType) {
		return 100;
	}

	public int requestAudioFocus(OnAudioFocusChangeListener listener, int streamType, int durationHint) {
		return /*AUDIOFOCUS_REQUEST_GRANTED*/ 1;
	}

	public int abandonAudioFocus(OnAudioFocusChangeListener listener) {
		return /*AUDIOFOCUS_REQUEST_GRANTED*/ 1;
	}

	public boolean isWiredHeadsetOn() {
		return false;
	}

	public void setStreamVolume(int streamType, int index, int flags) {
		nativeSetStreamVolume(index);
	}

	public boolean isStreamMute(int streamType) {
		return nativeGetStreamVolume() == 0;
	}

	public boolean isMusicActive() {
		return false;
	}

	/* Call routing. One state for the whole process (apps get several AudioManager instances).
	 * In a call (MODE_IN_CALL/MODE_IN_COMMUNICATION) with the speakerphone off, playback belongs
	 * on the earpiece; ATL can't switch hardware routes itself, so it runs
	 * $ATL_AUDIO_ROUTE_CMD earpiece|speaker whenever the wanted route changes. */
	public static final int MODE_NORMAL = 0;
	public static final int MODE_IN_CALL = 2;
	public static final int MODE_IN_COMMUNICATION = 3;
	private static final AudioDeviceInfo EARPIECE = new AudioDeviceInfo(AudioDeviceInfo.TYPE_BUILTIN_EARPIECE, 1, "Earpiece");
	private static final AudioDeviceInfo SPEAKER = new AudioDeviceInfo(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER, 2, "Speaker");
	private static int mode = MODE_NORMAL;
	private static boolean speakerphone = false;
	private static String route = "speaker"; // what the hardware is set to

	private static boolean inCall() {
		return mode == MODE_IN_CALL || mode == MODE_IN_COMMUNICATION;
	}

	private static synchronized void applyRoute() {
		String want = inCall() && !speakerphone ? "earpiece" : "speaker";
		if (want.equals(route))
			return;
		route = want;
		String cmd = System.getenv("ATL_AUDIO_ROUTE_CMD");
		System.out.println("AudioManager: call route -> " + want + (cmd == null ? " (no ATL_AUDIO_ROUTE_CMD, ignored)" : ""));
		if (cmd == null)
			return;
		try {
			new ProcessBuilder(cmd, want).inheritIO().start();
		} catch (Exception e) {
			System.err.println("AudioManager: " + cmd + " " + want + " failed: " + e);
		}
	}

	public void setSpeakerphoneOn(boolean on) {
		System.out.println("AudioManager.setSpeakerphoneOn(" + on + ")");
		speakerphone = on;
		applyRoute();
	}

	public boolean isSpeakerphoneOn() {
		return speakerphone;
	}

	public void setBluetoothScoOn(boolean on) {}

	public boolean isBluetoothScoOn() {
		return false;
	}

	public void stopBluetoothSco() {}

	public void setMode(int mode) {
		System.out.println("AudioManager.setMode(" + mode + ")");
		AudioManager.mode = mode;
		if (!inCall())
			speakerphone = false; // the next call starts on the earpiece, like on Android
		applyRoute();
	}

	public int getMode() {
		return mode;
	}

	public boolean isMicrophoneMute() {
		return false;
	}

	public void setMicrophoneMute(boolean on) {
		System.out.println("AudioManager.setMicrophoneMute(" + on + ")");
	}
	public void unloadSoundEffects() {}

	public int generateAudioSessionId() {
		return 0;
	}

	public AudioDeviceInfo[] getDevices(int flags) {
		return (flags & 2 /*GET_DEVICES_OUTPUTS*/) != 0 ? new AudioDeviceInfo[] {EARPIECE, SPEAKER} : new AudioDeviceInfo[0];
	}

	public void registerAudioDeviceCallback(AudioDeviceCallback callback, Handler handler) {}

	public void unregisterAudioDeviceCallback(AudioDeviceCallback callback) {}

	public AudioDeviceInfo getCommunicationDevice() {
		return speakerphone || !inCall() ? SPEAKER : EARPIECE;
	}

	public boolean setCommunicationDevice(AudioDeviceInfo device) {
		System.out.println("AudioManager.setCommunicationDevice(" + (device == null ? null : device.getProductName() + " type " + device.getType()) + ")");
		if (device == null)
			return false;
		speakerphone = device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER;
		applyRoute();
		return true;
	}

	public void clearCommunicationDevice() {
		System.out.println("AudioManager.clearCommunicationDevice()");
		speakerphone = false;
		applyRoute();
	}

	public java.util.List<AudioDeviceInfo> getAvailableCommunicationDevices() {
		return java.util.Arrays.asList(EARPIECE, SPEAKER);
	}
	public void adjustStreamVolume(int arg0, int arg1, int arg2) {} // ATL-autostub
	public void adjustSuggestedStreamVolume(int arg0, int arg1, int arg2) {} // ATL-autostub
	public void playSoundEffect(int arg0) {} // ATL-autostub
	public void startBluetoothSco() {} // ATL-autostub
}

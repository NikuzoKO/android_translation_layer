package android.os;

public class VibrationEffect {

	public static VibrationEffect createOneShot(long milliseconds, int amplitude) {
		return new VibrationEffect();
	}

	public static VibrationEffect createWaveform(long[] pattern, int repeat) {
		return new VibrationEffect();
	}

	public static android.os.VibrationEffect createPredefined(int arg0) { return null; } // ATL-autostub
	public static android.os.VibrationEffect createWaveform(long[] arg0, int[] arg1, int arg2) { return null; } // ATL-autostub
}

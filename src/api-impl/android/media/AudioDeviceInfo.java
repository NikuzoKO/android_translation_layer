package android.media;

public class AudioDeviceInfo {
	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_BUILTIN_EARPIECE = 1;
	public static final int TYPE_BUILTIN_SPEAKER = 2;

	private final int type;
	private final int id;
	private final String name;

	public AudioDeviceInfo() {
		this(TYPE_UNKNOWN, 0, null);
	}

	/* ATL: the phone's two built-in outputs, see AudioManager's call routing */
	AudioDeviceInfo(int type, int id, String name) {
		this.type = type;
		this.id = id;
		this.name = name;
	}

	public int getType() { return type; }
	public int getId() { return id; }
	public java.lang.CharSequence getProductName() { return name; }
	public java.lang.String getAddress() { return ""; }
	public boolean isSink() { return true; }
	public boolean isSource() { return false; }
	public int[] getSampleRates() { return new int[0]; }
	public int[] getChannelCounts() { return new int[0]; }
	public int[] getEncodings() { return new int[0]; }
}

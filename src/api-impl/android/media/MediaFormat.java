package android.media;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class MediaFormat {

	private Map<String, Object> map = new HashMap<>();

	public void setString(String key, String value) {
		map.put(key, value);
	}

	public void setInteger(String key, int value) {
		map.put(key, value);
	}

	public void setByteBuffer(String key, ByteBuffer value) {
		map.put(key, value);
	}

	public void setFloat(String key, float value) {
		map.put(key, value);
	}

	public ByteBuffer getByteBuffer(String name) {
		return (ByteBuffer)map.get(name);
	}

	public int getInteger(String name) {
		return (int)map.get(name);
	}

	public boolean containsKey(String name) {
		return map.containsKey(name);
	}

	public String toString() {
		return map.toString();
	}

	public String getString(String name) {
		return (String)map.get(name);
	}

	public long getLong(String name) {
		return (long)map.get(name);
	}

	public boolean getFeatureEnabled(java.lang.String arg0) { return false; } // ATL-autostub
	public float getFloat(java.lang.String arg0) { return 0f; } // ATL-autostub
	public float getFloat(java.lang.String arg0, float arg1) { return 0f; } // ATL-autostub
	public int getInteger(java.lang.String arg0, int arg1) { return 0; } // ATL-autostub
	public int getValueTypeForKey(java.lang.String arg0) { return 0; } // ATL-autostub
	public static android.media.MediaFormat createAudioFormat(java.lang.String arg0, int arg1, int arg2) { return null; } // ATL-autostub
	public static android.media.MediaFormat createVideoFormat(java.lang.String arg0, int arg1, int arg2) { return null; } // ATL-autostub
	public void removeKey(java.lang.String arg0) {} // ATL-autostub
	public void setFeatureEnabled(java.lang.String arg0, boolean arg1) {} // ATL-autostub
}

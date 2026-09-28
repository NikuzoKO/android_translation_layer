package android.net.wifi;

public class WifiManager {

	public static final int WIFI_STATE_DISABLED = 1;
	public static final int WIFI_STATE_DISABLING = 0;
	public static final int WIFI_STATE_ENABLED = 3;
	public static final int WIFI_STATE_ENABLING = 2;
	public static final int WIFI_STATE_UNKNOWN = 4;

	public class WifiLock {

		public void setReferenceCounted(boolean referenceCounted) {}

		public void release() {}

		public void acquire() {}

		public boolean isHeld() { return false; }
	}

	public WifiLock createWifiLock(int lockType, String tag) {
		return new WifiLock();
	}

	public WifiLock createWifiLock(String tag) {
		return new WifiLock();
	}

	public WifiInfo getConnectionInfo() {
		return new WifiInfo();
	}

	public int getWifiState() {
		return WIFI_STATE_UNKNOWN;
	}

	public boolean isWifiEnabled() {
		return false;
	}

	public boolean disconnect() { return false; } // ATL-autostub
	public boolean enableNetwork(int arg0, boolean arg1) { return false; } // ATL-autostub
	public boolean isP2pSupported() { return false; } // ATL-autostub
	public boolean reconnect() { return false; } // ATL-autostub
	public boolean removeNetwork(int arg0) { return false; } // ATL-autostub
	public boolean saveConfiguration() { return false; } // ATL-autostub
	public boolean setWifiEnabled(boolean arg0) { return false; } // ATL-autostub
	public java.util.List getScanResults() { return null; } // ATL-autostub
}

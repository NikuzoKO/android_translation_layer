package android.telephony;

import java.util.ArrayList;
import java.util.List;

public class TelephonyManager {
	public static TelephonyManager getDefault() {
		return new TelephonyManager();
	}

	// FIXME: can we return null instead of ""?
	public String getNetworkOperator() {
		return "";
	}

	public String getNetworkOperatorName() {
		return "";
	}

	public String getSimOperator() {
		return "";
	}

	public String getSubscriberId() {
		return "";
	}

	public int getPhoneType() {
		// PHONE_TYPE_GSM on a phone, PHONE_TYPE_NONE otherwise
		return System.getenv("ATL_HAS_TELEPHONY") != null ? 1 : 0;
	}

	public String getNetworkCountryIso() { return ""; }
	public String getSimCountryIso() { return ""; }

	public List getNeighboringCellInfo() {
		return new ArrayList(0);
	}

	public CellLocation getCellLocation() {
		return new CellLocation();
	}

	public boolean isNetworkRoaming() {
		return false;
	}

	public void listen(PhoneStateListener listener, int events) {}

	public int getNetworkType() {
		return 0; // NETWORK_TYPE_UNKNOWN
	}

	public String getLine1Number() {
		return null;
	}

	public int getSimState() {
		return 0; // SIM_STATE_UNKNOWN
	}

	public String getSimOperatorName() {
		return null;
	}

	public int getCallState() {
		return 0; // CALL_STATE_IDLE
	}

	public String getDeviceId() {
		return "";
	}
}

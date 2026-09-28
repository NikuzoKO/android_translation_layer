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

	public static final int SIM_STATE_UNKNOWN = 0;
	public static final int SIM_STATE_ABSENT = 1;

	/* No SIM is reachable from ATL apps. Saying so keeps apps (e.g. WhatsApp) from
	 * picking SIM-dependent flows like flash-call verification. */
	public int getSimState() {
		return SIM_STATE_ABSENT;
	}

	public int getSimState(int slotIndex) {
		return SIM_STATE_ABSENT;
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

	public android.telephony.TelephonyManager createForSubscriptionId(int arg0) { return ((Object)this) instanceof android.telephony.TelephonyManager ? (android.telephony.TelephonyManager)(Object)this : null; } // ATL-autostub
	public boolean isSmsCapable() { return false; } // ATL-autostub
	public int getCallStateForSubscription() { return 0; } // ATL-autostub
	public int getDataActivity() { return 0; } // ATL-autostub
	public int getDataNetworkType() { return 0; } // ATL-autostub
	public int getDataState() { return 0; } // ATL-autostub
	public int getPhoneCount() { return 0; } // ATL-autostub
	public java.lang.CharSequence getSimCarrierIdName() { return null; } // ATL-autostub
	public java.lang.String getSimSerialNumber() { return null; } // ATL-autostub
	public static java.lang.String EXTRA_STATE_RINGING; // ATL-autostub
}

package android.nfc;

import android.content.Context;

public class NfcAdapter {

	//@UnsupportedAppUsage
	public static NfcAdapter getDefaultAdapter() {
		return null;
	}

	public static NfcAdapter getDefaultAdapter(Context context) {
		return null;
	}

	public boolean isEnabled() { return false; } // ATL-autostub
	public void disableForegroundDispatch(android.app.Activity arg0) {} // ATL-autostub
	public void enableForegroundDispatch(android.app.Activity arg0, android.app.PendingIntent arg1, android.content.IntentFilter[] arg2, java.lang.String[][] arg3) {} // ATL-autostub
}

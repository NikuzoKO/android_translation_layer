package android.content;

public abstract class BroadcastReceiver {

	public static class PendingResult {

		public void setResultCode(int resultCode) {}

		public void finish() {}
	}

	public abstract void onReceive(Context context, Intent intent);

	public boolean isOrderedBroadcast() {
		return true;
	}

	public PendingResult goAsync() {
		return new PendingResult();
	}

	public android.os.Bundle getResultExtras(boolean arg0) { return ((Object)this) instanceof android.os.Bundle ? (android.os.Bundle)(Object)this : null; } // ATL-autostub
	public int getResultCode() { return 0; } // ATL-autostub
	public java.lang.String getResultData() { return null; } // ATL-autostub
	public java.lang.String getSentFromPackage() { return null; } // ATL-autostub
	public void abortBroadcast() {} // ATL-autostub
	public void setResult(int arg0, java.lang.String arg1, android.os.Bundle arg2) {} // ATL-autostub
	public void setResultCode(int arg0) {} // ATL-autostub
}

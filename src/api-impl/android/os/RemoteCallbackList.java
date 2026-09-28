package android.os;

public class RemoteCallbackList {

	public int beginBroadcast() {
		return 0;
	}

	public void finishBroadcast() {}

	public void kill() {}

	public android.os.IInterface getBroadcastItem(int arg0) { return ((Object)this) instanceof android.os.IInterface ? (android.os.IInterface)(Object)this : null; } // ATL-autostub
	public boolean register(android.os.IInterface arg0, java.lang.Object arg1) { return false; } // ATL-autostub
	public boolean unregister(android.os.IInterface arg0) { return false; } // ATL-autostub
	public java.lang.Object getBroadcastCookie(int arg0) { return null; } // ATL-autostub
}

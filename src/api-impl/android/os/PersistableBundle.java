package android.os;

public class PersistableBundle extends BaseBundle {

	@Override
	public synchronized String toString() {
		return "PersistableBundle[" + mMap.toString() + "]";
	}

	public android.os.PersistableBundle getPersistableBundle(java.lang.String arg0) { return ((Object)this) instanceof android.os.PersistableBundle ? (android.os.PersistableBundle)(Object)this : null; } // ATL-autostub
	public void putPersistableBundle(java.lang.String arg0, android.os.PersistableBundle arg1) {} // ATL-autostub
}

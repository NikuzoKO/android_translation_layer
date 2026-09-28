package android.os;

public interface IBinder {

	public interface DeathRecipient {}

	public IInterface queryLocalInterface(String descriptor);

	public boolean transact(int code, Parcel data, Parcel reply, int flags);

	public default boolean isBinderAlive() { return false; } // ATL-autostub
	public default boolean pingBinder() { return false; } // ATL-autostub
	public default boolean unlinkToDeath(android.os.IBinder.DeathRecipient arg0, int arg1) { return false; } // ATL-autostub
	public default java.lang.String getInterfaceDescriptor() { return null; } // ATL-autostub
	public default void dump(java.io.FileDescriptor arg0, java.lang.String[] arg1) {} // ATL-autostub
	public default void dumpAsync(java.io.FileDescriptor arg0, java.lang.String[] arg1) {} // ATL-autostub
	public default void linkToDeath(android.os.IBinder.DeathRecipient arg0, int arg1) {} // ATL-autostub
}

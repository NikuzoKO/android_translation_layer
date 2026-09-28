package android.os.storage;

import java.io.File;

public class StorageManager {
	public StorageVolume[] getVolumeList() {
		StorageVolume[] sVolumes = {new StorageVolume()};
		return sVolumes;
	}

	public StorageVolume getStorageVolume(File file) { return null; }

	public java.util.UUID getUuidForPath(java.io.File arg0) { return null; } // ATL-autostub
	public long getAllocatableBytes(java.util.UUID arg0) { return 0L; } // ATL-autostub
	public static java.util.UUID UUID_DEFAULT; // ATL-autostub
	public void allocateBytes(java.util.UUID arg0, long arg1) {} // ATL-autostub
}

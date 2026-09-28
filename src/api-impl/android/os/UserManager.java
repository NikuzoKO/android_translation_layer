package android.os;

public class UserManager {
	public boolean isUserUnlocked() {
		return true;
	}
	public static boolean supportsMultipleUsers() {
		return false;
	}

	public long getSerialNumberForUser(UserHandle user) {
		return user.getIdentifier();
	}

	public boolean isSystemUser() { return false; } // ATL-autostub
	public boolean isUserRunning(android.os.UserHandle arg0) { return false; } // ATL-autostub
}

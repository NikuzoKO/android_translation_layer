package android.app;

public class AppOpsManager {

	public static String permissionToOp(String permission) {
		return permission;
	}

	public int noteProxyOpNoThrow(String op, String pkg) {
		return 0;
	}

	public int checkOp(java.lang.String arg0, int arg1, java.lang.String arg2) { return 0; } // ATL-autostub
	public int checkOpNoThrow(java.lang.String arg0, int arg1, java.lang.String arg2) { return 0; } // ATL-autostub
	public void checkPackage(int arg0, java.lang.String arg1) {} // ATL-autostub
}

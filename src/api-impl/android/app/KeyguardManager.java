package android.app;

public class KeyguardManager {
	public boolean inKeyguardRestrictedInputMode() {
		return false;
	}

	public boolean isKeyguardLocked() {
		return false;
	}

	public boolean isKeyguardSecure() {
		return true;
	}

	public boolean isDeviceSecure() {
		return true;
	}

	public boolean isDeviceLocked() {
		return false;
	}

	public android.content.Intent createConfirmDeviceCredentialIntent(CharSequence title, CharSequence description) {
		return null;
	}

	public void requestDismissKeyguard(Activity activity, KeyguardDismissCallback callback) {
		if (callback != null)
			callback.onDismissSucceeded();
	}

	public static abstract class KeyguardDismissCallback {
		public void onDismissError() {}
		public void onDismissSucceeded() {}
		public void onDismissCancelled() {}
	}

	public KeyguardLock newKeyguardLock(String tag) {
		return new KeyguardLock();
	}

	public class KeyguardLock {
		public void disableKeyguard() {}
		public void reenableKeyguard() {}
	}

}

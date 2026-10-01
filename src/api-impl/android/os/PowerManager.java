package android.os;

public final class PowerManager {
	/* Android wakelocks map to kernel wakelocks (/sys/power/wake_lock, needs write access; silently off
	 * otherwise). They are never written without a timeout: a held lock is re-armed every 30 s for 60 s, so
	 * a crashed or killed app can keep the device awake for at most a minute. */
	private static final long KERNEL_LOCK_MS = 60000;
	private static boolean kernelLocksBroken = false;
	private static java.util.Timer timer;
	private static int nextLockId = 0;

	private static synchronized boolean writeSysPower(String file, String line) {
		if (kernelLocksBroken)
			return false;
		try {
			java.io.FileOutputStream out = new java.io.FileOutputStream("/sys/power/" + file);
			try {
				out.write(line.getBytes());
			} finally {
				out.close();
			}
			return true;
		} catch (java.io.FileNotFoundException e) { // missing or no permission
			System.err.println("PowerManager: kernel wakelocks unavailable: " + e);
			kernelLocksBroken = true;
		} catch (java.io.IOException e) { // e.g. unlocking a lock the kernel already dropped
		}
		return false;
	}

	/** hidden: hold the kernel wakelock `name` for `ms` milliseconds (re-arming extends it) */
	public static void kernelWakeLock(String name, long ms) {
		writeSysPower("wake_lock", name + " " + (ms * 1000000L));
	}

	/** hidden */
	public static void kernelWakeUnlock(String name) {
		writeSysPower("wake_unlock", name);
	}

	private static synchronized java.util.Timer timer() {
		if (timer == null)
			timer = new java.util.Timer("WakeLock", true);
		return timer;
	}

	public final class WakeLock {
		private final String kernelName;
		private final String tag;
		private boolean refCounted = true;
		private int count;
		private long deadline; // elapsedRealtime, Long.MAX_VALUE = until released
		private java.util.TimerTask task;

		WakeLock(String tag) {
			synchronized (PowerManager.class) {
				kernelName = "atl_" + Process.myPid() + "_" + (nextLockId++);
			}
			this.tag = tag;
			System.err.println("PowerManager: WakeLock " + kernelName + " = " + tag);
		}

		public void setReferenceCounted(boolean referenceCounted) {
			refCounted = referenceCounted;
		}

		public void acquire() {
			acquireUntil(Long.MAX_VALUE);
		}

		public void acquire(long timeout) {
			acquireUntil(SystemClock.elapsedRealtime() + timeout);
		}

		private synchronized void acquireUntil(long until) {
			if (refCounted || count == 0)
				count++;
			if (count == 1 || until > deadline)
				deadline = until;
			arm();
		}

		/* hold the kernel lock until the deadline (at most KERNEL_LOCK_MS ahead) and check again before it runs out */
		private synchronized void arm() {
			if (task != null)
				task.cancel();
			task = null;
			long left = deadline - SystemClock.elapsedRealtime();
			if (count == 0 || left <= 0) {
				count = 0;
				kernelWakeUnlock(kernelName);
				return;
			}
			kernelWakeLock(kernelName, Math.min(left, KERNEL_LOCK_MS));
			task = new java.util.TimerTask() {
				@Override
				public void run() {
					arm();
				}
			};
			timer().schedule(task, Math.min(left, KERNEL_LOCK_MS / 2));
		}

		public synchronized void release() {
			release(0);
		}

		public synchronized void release(int flags) {
			if (count == 0)
				return;
			count = refCounted ? count - 1 : 0;
			if (count == 0) {
				deadline = 0;
				arm();
			}
		}

		public synchronized boolean isHeld() {
			return count > 0;
		}

		public void setWorkSource(android.os.WorkSource arg0) {}
	}

	public WakeLock newWakeLock(int levelAndFlags, String tag) {
		return new WakeLock(tag);
	}

	public void goToSleep(long time) {
		// This requires DEVICE_POWER permission which is only possible for system processes. Any normal application gets a SecurityException.
		throw new SecurityException();
	}

	public void userActivity(long dummy, boolean dummy2) {}

	public static final int FULL_WAKE_LOCK = 0x1a;

	public boolean isPowerSaveMode() {
		return false;
	}

	public boolean isScreenOn() {
		return true;
	}

	public boolean isIgnoringBatteryOptimizations(String packageName) {
		return true;
	}

	public boolean isDeviceIdleMode() {
		return false;
	}

	public boolean isInteractive() {
		return true;
	}

	public int getCurrentThermalStatus() { return 0; } // ATL-autostub
}

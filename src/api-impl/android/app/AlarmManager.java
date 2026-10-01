package android.app;

import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Slog;

public class AlarmManager {
	private static final String TAG = "AlarmManager";

	public static final int RTC = 1;
	public static final int RTC_WAKEUP = 0;
	public static final int ELAPSED_REALTIME = 3;
	public static final int ELAPSED_REALTIME_WAKEUP = 2;

	/* Scheduled alarms by token (PendingIntent or OnAlarmListener): setting again replaces, cancel removes.
	 * Handler.postDelayed runs on CLOCK_MONOTONIC, which stops while the device is suspended, so the alarms
	 * are kept on SystemClock.elapsedRealtime() (CLOCK_BOOTTIME) and checked by a dispatcher thread that never
	 * sleeps more than a second: after a resume, due alarms go out within a second. The earliest *_WAKEUP
	 * alarm is published (see publishNextWakeup) so the system's suspend loop can set an RTC wakeup for it. */
	private static class Alarm {
		int type;
		long when; // elapsedRealtime
		long interval;
		Runnable action;
		Handler target;
	}
	private static final java.util.HashMap<Object, Alarm> alarms = new java.util.HashMap<Object, Alarm>();
	private static final Handler handler = new Handler(Looper.getMainLooper());
	private static Thread dispatcher;
	private static long publishedWakeup = -1;

	private static boolean isWakeup(int type) {
		return type == RTC_WAKEUP || type == ELAPSED_REALTIME_WAKEUP;
	}

	private static long delayFor(int type, long triggerTime) {
		long now = (type == ELAPSED_REALTIME || type == ELAPSED_REALTIME_WAKEUP) ? SystemClock.elapsedRealtime() : System.currentTimeMillis();
		return Math.max(0, triggerTime - now);
	}

	private static void schedule(int type, final Object token, final Runnable action, long delay, final long interval, Handler target) {
		Alarm alarm = new Alarm();
		alarm.type = type;
		alarm.when = SystemClock.elapsedRealtime() + delay;
		alarm.interval = interval;
		alarm.action = action;
		alarm.target = target != null ? target : handler;
		synchronized (alarms) {
			alarms.put(token, alarm);
			if (dispatcher == null) {
				dispatcher = new Thread(new Runnable() {
					@Override
					public void run() {
						dispatch();
					}
				}, "AlarmManager");
				dispatcher.setDaemon(true);
				dispatcher.start();
			}
			alarms.notifyAll();
		}
	}

	private static void dispatch() {
		java.util.ArrayList<Alarm> due = new java.util.ArrayList<Alarm>();
		while (true) {
			synchronized (alarms) {
				long now = SystemClock.elapsedRealtime();
				long next = Long.MAX_VALUE;
				long nextWakeup = Long.MAX_VALUE;
				java.util.Iterator<java.util.Map.Entry<Object, Alarm>> it = alarms.entrySet().iterator();
				while (it.hasNext()) {
					Alarm alarm = it.next().getValue();
					if (alarm.when <= now) {
						due.add(alarm);
						if (alarm.interval > 0)
							alarm.when = now + alarm.interval;
						else
							it.remove();
					}
					if (alarm.interval > 0 || alarm.when > now) {
						next = Math.min(next, alarm.when);
						if (isWakeup(alarm.type))
							nextWakeup = Math.min(nextWakeup, alarm.when);
					}
				}
				publishNextWakeup(nextWakeup, now);
				if (due.isEmpty()) {
					try {
						alarms.wait(Math.max(1, Math.min(1000, next - now)));
					} catch (InterruptedException e) {
					}
					continue;
				}
			}
			for (Alarm alarm : due) {
				// like the Android alarm manager, keep the device awake while the alarm is handled
				if (isWakeup(alarm.type))
					PowerManager.kernelWakeLock("atl_alarm_" + android.os.Process.myPid(), 5000);
				alarm.target.post(alarm.action);
			}
			due.clear();
		}
	}

	/* $XDG_RUNTIME_DIR/atl-alarms/<pid>: wall-clock time (seconds since the epoch) of the next wakeup alarm */
	private static void publishNextWakeup(long nextWakeup, long now) {
		if (nextWakeup == publishedWakeup)
			return;
		publishedWakeup = nextWakeup;
		String runtimeDir = System.getenv("XDG_RUNTIME_DIR");
		if (runtimeDir == null)
			return;
		java.io.File dir = new java.io.File(runtimeDir, "atl-alarms");
		java.io.File file = new java.io.File(dir, String.valueOf(android.os.Process.myPid()));
		try {
			if (nextWakeup == Long.MAX_VALUE) {
				file.delete();
				return;
			}
			dir.mkdirs();
			long wall = (System.currentTimeMillis() + (nextWakeup - now) + 999) / 1000;
			java.io.File tmp = new java.io.File(dir, "." + file.getName());
			java.io.FileOutputStream out = new java.io.FileOutputStream(tmp);
			out.write((wall + "\n").getBytes());
			out.close();
			tmp.renameTo(file);
		} catch (java.io.IOException e) {
			Slog.w(TAG, "can't publish the next wakeup: " + e);
		}
	}

	private static Runnable sender(final PendingIntent operation) {
		return new Runnable() {
			@Override
			public void run() {
				Slog.i(TAG, "delivering alarm: " + operation);
				operation.send();
			}
		};
	}

	static void cancelAlarms(Object token) {
		synchronized (alarms) {
			if (alarms.remove(token) != null)
				alarms.notifyAll();
		}
	}

	public void setRepeating(int type, long triggerTime, long interval, PendingIntent operation) {
		Slog.i(TAG, "setRepeating(" + type + ", " + triggerTime + ", " + interval + ", " + operation + ") called");
		setInexactRepeating(type, triggerTime, interval, operation);
	}

	public void cancel(PendingIntent operation) {
		Slog.i(TAG, "cancel(" + operation + ") called");
		cancelAlarms(operation);
	}

	public void cancel(OnAlarmListener listener) {
		Slog.i(TAG, "cancel(" + listener + ") called");
		cancelAlarms(listener);
	}

	public void setInexactRepeating(int type, long triggerTime, long interval, PendingIntent operation) {
		long delay = delayFor(type, triggerTime);
		Slog.i(TAG, "setInexactRepeating(" + type + ", " + operation + ") delay: " + DateUtils.formatElapsedTime(delay / 1000) + " interval: " + DateUtils.formatElapsedTime(interval / 1000));
		schedule(type, operation, sender(operation), delay, interval, null);
	}

	public void setExact(int type, long triggerTime, PendingIntent operation) {
		long delay = delayFor(type, triggerTime);
		Slog.i(TAG, "setExact(" + type + ", " + operation + ") delay: " + DateUtils.formatElapsedTime(delay / 1000));
		schedule(type, operation, sender(operation), delay, 0, null);
	}

	public void set(int type, long triggerTime, PendingIntent operation) {
		setExact(type, triggerTime, operation);
	}

	public void setExactAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {
		setExact(type, triggerAtMillis, operation);
	}

	public void setWindow(int type, long windowStartMillis, long windowLengthMillis, PendingIntent operation) {
		setExact(type, windowStartMillis, operation);
	}

	public void setExact(int type, long triggerAtMillis, String tag, final OnAlarmListener listener, Handler targetHandler) {
		schedule(type, listener, new Runnable() {
			@Override
			public void run() {
				listener.onAlarm();
			}
		}, delayFor(type, triggerAtMillis), 0, targetHandler);
	}

	public void set(int type, long triggerAtMillis, String tag, OnAlarmListener listener, Handler targetHandler) {
		setExact(type, triggerAtMillis, tag, listener, targetHandler);
	}

	public void setWindow(int type, long windowStartMillis, long windowLengthMillis, String tag, OnAlarmListener listener, Handler targetHandler) {
		setExact(type, windowStartMillis, tag, listener, targetHandler);
	}

	public void setAlarmClock(AlarmClockInfo info, PendingIntent operation) {
		setExact(RTC_WAKEUP, info.getTriggerTime(), operation);
	}

	public AlarmClockInfo getNextAlarmClock() {
		return null;
	}

	public void setAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {
		setExact(type, triggerAtMillis, operation);
	}

	public boolean canScheduleExactAlarms() {
		return true;
	}

	public static class AlarmClockInfo implements Parcelable {
		private long mTriggerTime;
		private PendingIntent mPendingIntent;
		public AlarmClockInfo(long triggerTime, PendingIntent showIntent) {
			mTriggerTime = triggerTime;
			mPendingIntent = showIntent;
		}

		public long getTriggerTime() {
			return mTriggerTime;
		}

		public PendingIntent getShowIntent() {
			return mPendingIntent;
		}
	}

	public static interface OnAlarmListener {
		void onAlarm();
	}

	public void setTime(long arg0) {} // ATL-autostub
	public void setTimeZone(java.lang.String arg0) {} // ATL-autostub
}

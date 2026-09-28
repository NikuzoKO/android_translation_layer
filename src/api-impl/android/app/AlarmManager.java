package android.app;

import android.os.Handler;
import android.os.Looper;
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

	/* scheduled alarms by token (PendingIntent or OnAlarmListener): setting again replaces, cancel removes */
	private static final java.util.HashMap<Object, Runnable> alarms = new java.util.HashMap<Object, Runnable>();
	private static final Handler handler = new Handler(Looper.getMainLooper());

	private static long delayFor(int type, long triggerTime) {
		long now = (type == ELAPSED_REALTIME || type == ELAPSED_REALTIME_WAKEUP) ? SystemClock.elapsedRealtime() : System.currentTimeMillis();
		return Math.max(0, triggerTime - now);
	}

	private static void schedule(final Object token, final Runnable action, long delay, final long interval, Handler target) {
		final Handler h = target != null ? target : handler;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				synchronized (alarms) {
					if (alarms.get(token) != this)
						return; // cancelled or replaced
					if (interval <= 0)
						alarms.remove(token);
				}
				action.run();
				if (interval > 0)
					h.postDelayed(this, interval);
			}
		};
		synchronized (alarms) {
			Runnable old = alarms.put(token, runnable);
			if (old != null)
				handler.removeCallbacks(old);
		}
		h.postDelayed(runnable, delay);
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
			Runnable old = alarms.remove(token);
			if (old != null)
				handler.removeCallbacks(old);
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
		schedule(operation, sender(operation), delay, interval, null);
	}

	public void setExact(int type, long triggerTime, PendingIntent operation) {
		long delay = delayFor(type, triggerTime);
		Slog.i(TAG, "setExact(" + type + ", " + operation + ") delay: " + DateUtils.formatElapsedTime(delay / 1000));
		schedule(operation, sender(operation), delay, 0, null);
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
		schedule(listener, new Runnable() {
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

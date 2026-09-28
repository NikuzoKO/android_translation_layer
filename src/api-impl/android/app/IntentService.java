package android.app;

import android.content.Intent;
import android.os.IBinder;

public abstract class IntentService extends Service {
	public IntentService(String name) {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.onHandleIntent(intent);
		return 0;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected abstract void onHandleIntent(Intent intent);

	public void onTimeout(int arg0, int arg1) {} // ATL-autostub
	public void startForeground(int arg0, android.app.Notification arg1, int arg2) {} // ATL-autostub
}

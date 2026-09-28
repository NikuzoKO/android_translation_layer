package android.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;

public class Toast {
	private final Context mContext;
	private String text;

	public Toast(Context context) {
		mContext = context;
	}

	public static Toast makeText(Context context, int resId, int duration) {
		return makeText(context, context.getString(resId), duration);
	}

	public static Toast makeText(Context context, CharSequence text, int duration) {
		Toast toast = new Toast(context);
		toast.text = String.valueOf(text);
		return toast;
	}

	public void show() {
		System.out.println("showing toast: " + text);
		Notification notification = new Notification.Builder(mContext).setContentText(text).build();
		NotificationManager manager = (NotificationManager)mContext.getSystemService("notification");
		int id = hashCode();
		manager.notify(id, notification);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				manager.cancel(id);
			}
		}, 2000);
	}

	public void setView(View view) {}

	public android.view.View getView() { return ((Object)this) instanceof android.view.View ? (android.view.View)(Object)this : null; } // ATL-autostub
	public void cancel() {} // ATL-autostub
	public void setDuration(int arg0) {} // ATL-autostub
	public void setGravity(int arg0, int arg1, int arg2) {} // ATL-autostub
	public void setText(int arg0) {} // ATL-autostub
}

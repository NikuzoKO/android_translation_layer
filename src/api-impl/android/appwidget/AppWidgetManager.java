package android.appwidget;

import android.content.ComponentName;
import android.content.Context;

public class AppWidgetManager {

	public static AppWidgetManager getInstance(Context context) {
		return new AppWidgetManager();
	}

	public int[] getAppWidgetIds(ComponentName provider) {
		return new int[0];
	}

	public void notifyAppWidgetViewDataChanged(int appWidgetId, int viewId) {}

	public void notifyAppWidgetViewDataChanged(int[] appWidgetIds, int viewId) {}

	public android.os.Bundle getAppWidgetOptions(int arg0) { return ((Object)this) instanceof android.os.Bundle ? (android.os.Bundle)(Object)this : null; } // ATL-autostub
	public boolean isRequestPinAppWidgetSupported() { return false; } // ATL-autostub
	public boolean requestPinAppWidget(android.content.ComponentName arg0, android.os.Bundle arg1, android.app.PendingIntent arg2) { return false; } // ATL-autostub
	public void updateAppWidget(int arg0, android.widget.RemoteViews arg1) {} // ATL-autostub
}

package android.appwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Slog;

public class AppWidgetProvider extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Slog.i("AppWidgetProvider", "onReceive(" + intent + ") called");
	}

	public void onAppWidgetOptionsChanged(android.content.Context arg0, android.appwidget.AppWidgetManager arg1, int arg2, android.os.Bundle arg3) {} // ATL-autostub
	public void onDisabled(android.content.Context arg0) {} // ATL-autostub
	public void onEnabled(android.content.Context arg0) {} // ATL-autostub
	public void onUpdate(android.content.Context arg0, android.appwidget.AppWidgetManager arg1, int[] arg2) {} // ATL-autostub
}

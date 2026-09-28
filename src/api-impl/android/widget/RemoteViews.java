package android.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

public class RemoteViews {

	public RemoteViews(String packageName, int layoutId) {}

	public View apply(Context context, ViewGroup parent) {
		return null;
	}

	public void setProgressBar(int viewId, int max, int progress, boolean indeterminate) {}

	public void setTextViewText(int viewId, CharSequence text) {}

	public void setImageViewResource(int viewId, int resId) {}

	public void setContentDescription(int viewId, CharSequence text) {}

	public void setOnClickPendingIntent(int viewId, PendingIntent pendingIntent) {}

	public void setViewVisibility(int viewId, int visibility) {}

	public void setImageViewBitmap(int viewId, Bitmap bitmap) {}

	public void removeAllViews(int viewId) {}

	public RemoteViews clone() {
		return new RemoteViews(null, 0);
	}

	public void addView(int viewId, RemoteViews child) {}

	public void setViewPadding(int viewId, int left, int top, int right, int bottom) {}

	public void setBoolean(int arg0, java.lang.String arg1, boolean arg2) {} // ATL-autostub
	public void setEmptyView(int arg0, int arg1) {} // ATL-autostub
	public void setFloat(int arg0, java.lang.String arg1, float arg2) {} // ATL-autostub
	public void setInt(int arg0, java.lang.String arg1, int arg2) {} // ATL-autostub
	public void setLong(int arg0, java.lang.String arg1, long arg2) {} // ATL-autostub
	public void setOnClickFillInIntent(int arg0, android.content.Intent arg1) {} // ATL-autostub
	public void setPendingIntentTemplate(int arg0, android.app.PendingIntent arg1) {} // ATL-autostub
	public void setRemoteAdapter(int arg0, int arg1, android.content.Intent arg2) {} // ATL-autostub
	public void setTextColor(int arg0, int arg1) {} // ATL-autostub
}

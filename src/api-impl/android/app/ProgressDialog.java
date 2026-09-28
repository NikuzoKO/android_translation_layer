package android.app;

import android.content.Context;

public class ProgressDialog extends AlertDialog {

	public ProgressDialog(Context context) {
		super(context, 0);
	}

	public ProgressDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	public void setIndeterminate(boolean indeterminate) {}

	public void setProgressStyle(int style) {}

	public void setProgress(int progress) {}

	public void setMax(int max) {}

	public static ProgressDialog show(Context context, CharSequence title, CharSequence message) {
		return show(context, title, message, false);
	}

	public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}

	public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable, null);
	}

	public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
		return new ProgressDialog(context);
	}

	public int getProgress() { return 0; } // ATL-autostub
	public void setProgressNumberFormat(java.lang.String arg0) {} // ATL-autostub
	public void setProgressPercentFormat(java.text.NumberFormat arg0) {} // ATL-autostub
}

package android.content;

public interface DialogInterface {
	/** The identifier for the positive button. */
	int BUTTON_POSITIVE = -1;
	/** The identifier for the negative button. */
	int BUTTON_NEGATIVE = -2;
	/** The identifier for the neutral button. */
	int BUTTON_NEUTRAL = -3;

	public void dismiss();

	public void cancel();

	public interface OnDismissListener {
		void onDismiss(DialogInterface dialog);
	}
	public interface OnClickListener {
		void onClick(DialogInterface dialog, int which);
	}
	public interface OnShowListener {
		void onShow(DialogInterface dialog);
	}
	public interface OnCancelListener {
	
	public default void onCancel(android.content.DialogInterface arg0) {} // ATL-autostub
}
	public interface OnMultiChoiceClickListener {
	
	public default void onClick(android.content.DialogInterface arg0, int arg1, boolean arg2) {} // ATL-autostub
}
	public interface OnKeyListener {
	
	public default boolean onKey(android.content.DialogInterface arg0, int arg1, android.view.KeyEvent arg2) { return false; } // ATL-autostub
}
}

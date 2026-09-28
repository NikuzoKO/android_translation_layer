package android.widget;

public interface ThemedSpinnerAdapter extends SpinnerAdapter {

	public default android.content.res.Resources.Theme getDropDownViewTheme() { return ((Object)this) instanceof android.content.res.Resources.Theme ? (android.content.res.Resources.Theme)(Object)this : null; } // ATL-autostub
	public default void setDropDownViewTheme(android.content.res.Resources.Theme arg0) {} // ATL-autostub
}

package android.view;

public class FocusFinder {

	public static FocusFinder getInstance() {
		return new FocusFinder();
	}

	public View findNextFocus(ViewGroup root, View focused, int direction) {
		return null;
	}

	public android.view.View findNextFocusFromRect(android.view.ViewGroup arg0, android.graphics.Rect arg1, int arg2) { return ((Object)this) instanceof android.view.View ? (android.view.View)(Object)this : null; } // ATL-autostub
}

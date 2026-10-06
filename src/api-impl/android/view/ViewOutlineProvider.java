package android.view;

import android.graphics.Outline;

public class ViewOutlineProvider {
	public static final ViewOutlineProvider BACKGROUND = new ViewOutlineProvider();
	public static final ViewOutlineProvider BOUNDS = new ViewOutlineProvider();
	public static final ViewOutlineProvider PADDED_BOUNDS = new ViewOutlineProvider();

	/** Apps override this; the built-in providers leave the outline empty (no rounding). */
	public void getOutline(View view, Outline outline) {}
}

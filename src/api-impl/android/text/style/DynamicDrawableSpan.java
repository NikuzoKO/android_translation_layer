package android.text.style;

public class DynamicDrawableSpan extends ReplacementSpan {

	public static final int ALIGN_BOTTOM = 0;
	public static final int ALIGN_BASELINE = 1;
	public static final int ALIGN_CENTER = 2;

	protected int mVerticalAlignment = ALIGN_BOTTOM;

	public DynamicDrawableSpan() {}

	protected DynamicDrawableSpan(int verticalAlignment) {
		mVerticalAlignment = verticalAlignment;
	}

	public int getVerticalAlignment() {
		return mVerticalAlignment;
	}

	public android.graphics.drawable.Drawable getDrawable() {
		return null;
	}
}

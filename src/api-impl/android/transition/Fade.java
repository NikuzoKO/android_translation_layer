package android.transition;

import android.content.Context;
import android.util.AttributeSet;

public class Fade extends Visibility {
	public static final int IN = Visibility.MODE_IN;
	public static final int OUT = Visibility.MODE_OUT;

	public Fade() {}

	public Fade(int fadingMode) {
		setMode(fadingMode);
	}

	public Fade(Context context, AttributeSet attrs) {}
}

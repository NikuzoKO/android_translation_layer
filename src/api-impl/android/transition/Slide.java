package android.transition;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

public class Slide extends Visibility {
	private int slide_edge = Gravity.BOTTOM;

	public Slide() {}

	public Slide(int slideEdge) {
		slide_edge = slideEdge;
	}

	public Slide(Context context, AttributeSet attrs) {}

	public void setSlideEdge(int slideEdge) {
		slide_edge = slideEdge;
	}

	public int getSlideEdge() {
		return slide_edge;
	}
}

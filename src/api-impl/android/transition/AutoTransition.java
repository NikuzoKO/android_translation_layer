package android.transition;

import android.content.Context;
import android.util.AttributeSet;

public class AutoTransition extends TransitionSet {
	public AutoTransition() {
		setOrdering(ORDERING_SEQUENTIAL);
		addTransition(new Fade(Fade.OUT)).addTransition(new ChangeBounds()).addTransition(new Fade(Fade.IN));
	}

	public AutoTransition(Context context, AttributeSet attrs) {
		this();
	}
}

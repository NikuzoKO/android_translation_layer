package android.transition;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public abstract class Visibility extends Transition {
	public static final int MODE_IN = 0x1;
	public static final int MODE_OUT = 0x2;

	private int mode = MODE_IN | MODE_OUT;

	public Visibility() {}

	public Visibility(Context context, AttributeSet attrs) {}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}

	public boolean isVisible(TransitionValues values) {
		return values != null && values.view != null && values.view.getVisibility() == View.VISIBLE;
	}

	public Animator onAppear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
		return null;
	}

	public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
		return null;
	}

	public Animator onDisappear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
		return null;
	}

	public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
		return null;
	}
}

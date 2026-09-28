package android.transition;

import android.content.Context;
import android.util.AttributeSet;

public class ChangeTransform extends Transition {
	private boolean reparent = true;
	private boolean reparent_with_overlay = true;

	public ChangeTransform() {}

	public ChangeTransform(Context context, AttributeSet attrs) {}

	public void setReparent(boolean reparent) {
		this.reparent = reparent;
	}

	public boolean getReparent() {
		return reparent;
	}

	public void setReparentWithOverlay(boolean reparentWithOverlay) {
		reparent_with_overlay = reparentWithOverlay;
	}

	public boolean getReparentWithOverlay() {
		return reparent_with_overlay;
	}
}

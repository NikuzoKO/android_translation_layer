package android.transition;

import android.content.Context;
import android.util.AttributeSet;

public class ChangeBounds extends Transition {
	private boolean resize_clip;
	private boolean reparent;

	public ChangeBounds() {}

	public ChangeBounds(Context context, AttributeSet attrs) {}

	public void setResizeClip(boolean resizeClip) {
		resize_clip = resizeClip;
	}

	public boolean getResizeClip() {
		return resize_clip;
	}

	public void setReparent(boolean reparent) {
		this.reparent = reparent;
	}
}

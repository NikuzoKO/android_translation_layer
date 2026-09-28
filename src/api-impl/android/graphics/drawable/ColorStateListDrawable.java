package android.graphics.drawable;

import android.graphics.Canvas;

public class ColorStateListDrawable extends Drawable implements Drawable.Callback {
	@Override
	public void draw(Canvas c) {
	}
	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
	}
	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
	}
	@Override
	public void invalidateDrawable(Drawable who) {
	}

	public android.content.res.ColorStateList getColorStateList() { return ((Object)this) instanceof android.content.res.ColorStateList ? (android.content.res.ColorStateList)(Object)this : null; } // ATL-autostub
}

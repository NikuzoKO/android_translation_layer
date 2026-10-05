package android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class CheckedTextView extends TextView {

	public CheckedTextView(Context context) {
		super(context);
	}

	public CheckedTextView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public void setChecked(boolean checked) {}

	public void setCheckMarkDrawable(Drawable d) {}

	public android.graphics.drawable.Drawable getCheckMarkDrawable() { return ((Object)this) instanceof android.graphics.drawable.Drawable ? (android.graphics.drawable.Drawable)(Object)this : null; } // ATL-autostub
	public void setCheckMarkTintList(android.content.res.ColorStateList arg0) {} // ATL-autostub
	public void setCheckMarkTintMode(android.graphics.PorterDuff.Mode arg0) {} // ATL-autostub
}

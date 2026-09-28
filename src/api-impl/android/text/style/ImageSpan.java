package android.text.style;

import android.graphics.drawable.Drawable;

public class ImageSpan extends DynamicDrawableSpan {

	private Drawable drawable;

	public ImageSpan(Drawable d) {
		drawable = d;
	}

	public ImageSpan(Drawable d, String source) {
		drawable = d;
	}

	public ImageSpan(Drawable d, int verticalAlignment) {
		drawable = d;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public ImageSpan(android.content.Context arg0, android.graphics.Bitmap arg1) {} // ATL-autostub
	public ImageSpan(android.content.Context arg0, android.graphics.Bitmap arg1, int arg2) {} // ATL-autostub
	public ImageSpan(android.content.Context arg0, int arg1) {} // ATL-autostub
}

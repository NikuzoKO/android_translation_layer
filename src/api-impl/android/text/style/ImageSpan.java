package android.text.style;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class ImageSpan extends DynamicDrawableSpan {

	private Drawable drawable;
	private Context context;
	private int resourceId;
	private String source;

	public ImageSpan(Drawable d) {
		this(d, ALIGN_BOTTOM);
	}

	public ImageSpan(Drawable d, String source) {
		this(d, source, ALIGN_BOTTOM);
	}

	public ImageSpan(Drawable d, int verticalAlignment) {
		mVerticalAlignment = verticalAlignment;
		drawable = d;
	}

	public ImageSpan(Drawable d, String source, int verticalAlignment) {
		mVerticalAlignment = verticalAlignment;
		drawable = d;
		this.source = source;
	}

	public ImageSpan(Context context, Bitmap bitmap) {
		this(context, bitmap, ALIGN_BOTTOM);
	}

	public ImageSpan(Context context, Bitmap bitmap, int verticalAlignment) {
		mVerticalAlignment = verticalAlignment;
		this.context = context;
		if (bitmap != null) {
			drawable = context != null ? new BitmapDrawable(context.getResources(), bitmap) : new BitmapDrawable(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		}
	}

	public ImageSpan(Context context, int resourceId) {
		this(context, resourceId, ALIGN_BOTTOM);
	}

	public ImageSpan(Context context, int resourceId, int verticalAlignment) {
		mVerticalAlignment = verticalAlignment;
		this.context = context;
		this.resourceId = resourceId;
	}

	/* like Android: a resource drawable is loaded on first use, with its intrinsic size as bounds */
	@Override
	public Drawable getDrawable() {
		if (drawable == null && context != null && resourceId != 0) {
			try {
				drawable = context.getDrawable(resourceId);
				if (drawable != null)
					drawable.setBounds(0, 0, Math.max(0, drawable.getIntrinsicWidth()), Math.max(0, drawable.getIntrinsicHeight()));
			} catch (Exception e) {
				android.util.Log.e("ImageSpan", "Unable to find resource: " + resourceId, e);
			}
		}
		return drawable;
	}

	public String getSource() {
		return source;
	}
}

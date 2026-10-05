package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ImageView extends View {

	private Bitmap bitmap = null;
	private ScaleType scaleType = ScaleType.FIT_CENTER;
	private Drawable drawable = null;
	/* like Android: an explicit color filter overrides the tint */
	private ColorFilter colorFilter = null;
	private ColorStateList tintList = null;
	private PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;
	private boolean filterApplied = false;

	public ImageView(Context context) {
		this(context, null);
	}

	public ImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public ImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

		haveCustomMeasure = false;
		TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ImageView, defStyleAttr, defStyleRes);
		if (a.hasValue(com.android.internal.R.styleable.ImageView_tint)) {
			tintList = a.getColorStateList(com.android.internal.R.styleable.ImageView_tint);
			tintMode = PorterDuff.Mode.values()[a.getInt(com.android.internal.R.styleable.ImageView_tintMode, PorterDuff.Mode.SRC_IN.nativeInt)];
		}
		Drawable src = a.getDrawable(com.android.internal.R.styleable.ImageView_src);
		if (src != null)
			setImageDrawable(src);
		if (a.hasValue(com.android.internal.R.styleable.ImageView_scaleType))
			setScaleType(scaletype_from_int[a.getInt(com.android.internal.R.styleable.ImageView_scaleType, 3 /*CENTER*/)]);
		a.recycle();
	}

	public void setImageResource(final int resid) {
		if (this.getResources().getString(resid).endsWith(".xml")) {
			setImageDrawable(getContext().getDrawable(resid));
			return;
		}
		bitmap = BitmapFactory.decodeResource(this.getResources(), resid);
		native_setDrawable(widget, bitmap != null ? bitmap.getTexture() : 0);
	}
	public void setAdjustViewBounds(boolean adjustViewBounds) {}

	public void setScaleType(ScaleType scaleType) {
		this.scaleType = scaleType;
		native_setScaleType(widget, scaleType.nativeInt);
	}

	public ScaleType getScaleType() {
		return scaleType;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	private ColorFilter effectiveColorFilter() {
		if (colorFilter != null)
			return colorFilter;
		if (tintList != null)
			return new PorterDuffColorFilter(tintList.getColorForState(getDrawableState(), tintList.getDefaultColor()), tintMode);
		return null;
	}

	private void applyColorFilter() {
		if (drawable == null)
			return;
		ColorFilter filter = effectiveColorFilter();
		if (filter == null && !filterApplied)
			return;
		drawable.setColorFilter(filter);
		filterApplied = filter != null;
		native_setDrawable(widget, drawable.paintable);
	}

	public void setImageDrawable(Drawable drawable) {
		filterApplied = false;
		if (drawable != null && effectiveColorFilter() != null)
			drawable = drawable.mutate();
		if (this.drawable != null) {
			this.drawable.setCallback(null);
		}
		this.drawable = drawable;
		if (drawable != null) {
			drawable.setCallback(this);
			if (drawable.isStateful())
				drawable.setState(getDrawableState());
		}
		native_setDrawable(widget, drawable != null ? drawable.paintable : 0);
		applyColorFilter();
	}

	public void setImageMatrix(Matrix matrix) {}

	/* like Android: wrap in a BitmapDrawable, so getDrawable() returns it and tint applies */
	public void setImageBitmap(Bitmap bitmap) {
		setImageDrawable(bitmap != null ? new BitmapDrawable(getResources(), bitmap) : null);
	}

	/**
	 * Options for scaling the bounds of an image to the bounds of this view.
	 */
	public enum ScaleType {
		/**
		 * Scale using the image matrix when drawing. The image matrix can be set using
		 * {@link ImageView#setImageMatrix(Matrix)}. From XML, use this syntax:
		 * <code>android:scaleType="matrix"</code>.
		 */
		MATRIX(0),
		/**
		 * Scale the image using {@link Matrix.ScaleToFit#FILL}.
		 * From XML, use this syntax: <code>android:scaleType="fitXY"</code>.
		 */
		FIT_XY(1),
		/**
		 * Scale the image using {@link Matrix.ScaleToFit#START}.
		 * From XML, use this syntax: <code>android:scaleType="fitStart"</code>.
		 */
		FIT_START(2),
		/**
		 * Scale the image using {@link Matrix.ScaleToFit#CENTER}.
		 * From XML, use this syntax:
		 * <code>android:scaleType="fitCenter"</code>.
		 */
		FIT_CENTER(3),
		/**
		 * Scale the image using {@link Matrix.ScaleToFit#END}.
		 * From XML, use this syntax: <code>android:scaleType="fitEnd"</code>.
		 */
		FIT_END(4),
		/**
		 * Center the image in the view, but perform no scaling.
		 * From XML, use this syntax: <code>android:scaleType="center"</code>.
		 */
		CENTER(5),
		/**
		 * Scale the image uniformly (maintain the image's aspect ratio) so
		 * that both dimensions (width and height) of the image will be equal
		 * to or larger than the corresponding dimension of the view
		 * (minus padding). The image is then centered in the view.
		 * From XML, use this syntax: <code>android:scaleType="centerCrop"</code>.
		 */
		CENTER_CROP(6),
		/**
		 * Scale the image uniformly (maintain the image's aspect ratio) so
		 * that both dimensions (width and height) of the image will be equal
		 * to or less than the corresponding dimension of the view
		 * (minus padding). The image is then centered in the view.
		 * From XML, use this syntax: <code>android:scaleType="centerInside"</code>.
		 */
		CENTER_INSIDE(7);

		ScaleType(int ni) {
			nativeInt = ni;
		}
		final int nativeInt;
	}
	private final ScaleType[] scaletype_from_int = {
		ScaleType.MATRIX,
		ScaleType.FIT_XY,
		ScaleType.FIT_START,
		ScaleType.FIT_CENTER,
		ScaleType.FIT_END,
		ScaleType.CENTER,
		ScaleType.CENTER_CROP,
		ScaleType.CENTER_INSIDE,
	};

	public final void setColorFilter(int color, PorterDuff.Mode mode) {
		setColorFilter(new PorterDuffColorFilter(color, mode));
	}

	public void setImageTintList(ColorStateList tint) {
		tintList = tint;
		if (drawable != null && effectiveColorFilter() != null)
			drawable = drawable.mutate();
		applyColorFilter();
	}

	public void setImageAlpha(int alpha) {}

	public void setMaxWidth(int width) {}

	public void setMaxHeight(int height) {}

	public void setImageState(int[] state, boolean merge) {}

	public ColorStateList getImageTintList() {
		return tintList;
	}

	public PorterDuff.Mode getImageTintMode() {
		return tintMode;
	}

	public void setImageTintMode(PorterDuff.Mode tintMode) {
		this.tintMode = tintMode != null ? tintMode : PorterDuff.Mode.SRC_IN;
		applyColorFilter();
	}

	public void setCropToPadding(boolean crop) {}

	public void setColorFilter(int color) {
		setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
	}

	public void setColorFilter(ColorFilter cf) {
		colorFilter = cf;
		if (drawable != null && cf != null)
			drawable = drawable.mutate();
		applyColorFilter();
	}

	public ColorFilter getColorFilter() {
		return colorFilter;
	}

	public Matrix getImageMatrix() {
		return Matrix.IDENTITY_MATRIX;
	}

	@Override
	protected native long native_constructor(Context context, AttributeSet attrs);
	protected native void native_setDrawable(long widget, long paintable);
	protected native void native_setScaleType(long widget, int scale_type);

	public boolean hasOverlappingRendering() { return false; } // ATL-autostub
	public boolean setFrame(int arg0, int arg1, int arg2, int arg3) { return false; } // ATL-autostub
	public boolean verifyDrawable(android.graphics.drawable.Drawable arg0) { return false; } // ATL-autostub
	public int getMaxWidth() { return 0; } // ATL-autostub
	public void animateTransform(android.graphics.Matrix arg0) {} // ATL-autostub
	public void clearColorFilter() {
		setColorFilter(null);
	}
	public void drawableHotspotChanged(float arg0, float arg1) {} // ATL-autostub
	public void drawableStateChanged() {
		super.drawableStateChanged();
		if (drawable != null && drawable.isStateful() && drawable.setState(getDrawableState()))
			invalidate();
		if (colorFilter == null && tintList != null && tintList.isStateful())
			applyColorFilter();
	}
	public void setImageLevel(int arg0) {} // ATL-autostub
	public void setImageURI(android.net.Uri arg0) {} // ATL-autostub
	public void setSystemGestureExclusionRects(java.util.List arg0) {} // ATL-autostub
}

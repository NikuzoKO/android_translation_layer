package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* Draws its drawable inset by the given amounts (pixels, or fractions of the bounds) and reports
   the insets as padding. Material uses it for 32dp chips/buttons inside 48dp touch targets. */
public class InsetDrawable extends DrawableWrapper {
	private float insetLeft, insetTop, insetRight, insetBottom;
	private boolean leftFraction, topFraction, rightFraction, bottomFraction;
	private final Rect tmpRect = new Rect();
	private static final boolean DEBUG = System.getenv("ATL_DEBUG_INSET") != null;

	public InsetDrawable(Drawable drawable, int insetLeft, int insetTop, int insetRight, int insetBottom) {
		super(drawable);
		this.insetLeft = insetLeft;
		this.insetTop = insetTop;
		this.insetRight = insetRight;
		this.insetBottom = insetBottom;
	}

	public InsetDrawable(Drawable drawable, int inset) {
		this(drawable, inset, inset, inset, inset);
	}

	public InsetDrawable(Drawable drawable, float insetFraction) {
		this(drawable, insetFraction, insetFraction, insetFraction, insetFraction);
	}

	public InsetDrawable(Drawable drawable, float insetLeftFraction, float insetTopFraction, float insetRightFraction, float insetBottomFraction) {
		super(drawable);
		insetLeft = insetLeftFraction;
		insetTop = insetTopFraction;
		insetRight = insetRightFraction;
		insetBottom = insetBottomFraction;
		leftFraction = topFraction = rightFraction = bottomFraction = true;
	}

	InsetDrawable() {
		super(new InsetState(null, null), null);
	}

	@Override
	public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
		TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.InsetDrawable);
		float[] all = readInset(a, R.styleable.InsetDrawable_inset, 0, false);
		float[] v;
		v = readInset(a, R.styleable.InsetDrawable_insetLeft, all[0], all[1] != 0);
		insetLeft = v[0];
		leftFraction = v[1] != 0;
		v = readInset(a, R.styleable.InsetDrawable_insetTop, all[0], all[1] != 0);
		insetTop = v[0];
		topFraction = v[1] != 0;
		v = readInset(a, R.styleable.InsetDrawable_insetRight, all[0], all[1] != 0);
		insetRight = v[0];
		rightFraction = v[1] != 0;
		v = readInset(a, R.styleable.InsetDrawable_insetBottom, all[0], all[1] != 0);
		insetBottom = v[0];
		bottomFraction = v[1] != 0;
		a.recycle();
		if (DEBUG)
			android.util.Slog.i("InsetDrawable", "ATL_DEBUG_INSET inflate l=" + insetLeft + (leftFraction ? "%" : "") + " t=" + insetTop + (topFraction ? "%" : "") + " r=" + insetRight + (rightFraction ? "%" : "") + " b=" + insetBottom + (bottomFraction ? "%" : ""));
		super.inflate(r, parser, attrs, theme);
	}

	/* {value, isFraction} */
	private static float[] readInset(TypedArray a, int index, float fallback, boolean fallbackFraction) {
		TypedValue tv = a.peekValue(index);
		if (tv == null)
			return new float[] {fallback, fallbackFraction ? 1 : 0};
		if (tv.type == TypedValue.TYPE_FRACTION)
			return new float[] {tv.getFraction(1.0f, 1.0f), 1};
		return new float[] {a.getDimensionPixelOffset(index, 0), 0};
	}

	private int px(float value, boolean fraction, int size) {
		return fraction ? (int)(value * size) : (int)value;
	}

	private void insets(Rect bounds, Rect out) {
		int w = bounds.width(), h = bounds.height();
		out.set(px(insetLeft, leftFraction, w), px(insetTop, topFraction, h), px(insetRight, rightFraction, w), px(insetBottom, bottomFraction, h));
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		insets(bounds, tmpRect);
		Drawable d = getDrawable();
		if (DEBUG)
			android.util.Slog.i("InsetDrawable", "ATL_DEBUG_INSET bounds " + bounds + " insets " + tmpRect + " child " + d);
		if (d != null)
			d.setBounds(bounds.left + tmpRect.left, bounds.top + tmpRect.top, bounds.right - tmpRect.right, bounds.bottom - tmpRect.bottom);
	}

	@Override
	public boolean getPadding(Rect padding) {
		boolean pad = super.getPadding(padding);
		if (!pad)
			padding.set(0, 0, 0, 0);
		insets(getBounds(), tmpRect);
		padding.left += tmpRect.left;
		padding.top += tmpRect.top;
		padding.right += tmpRect.right;
		padding.bottom += tmpRect.bottom;
		return pad || (tmpRect.left | tmpRect.top | tmpRect.right | tmpRect.bottom) != 0;
	}

	@Override
	public int getIntrinsicWidth() {
		int w = super.getIntrinsicWidth();
		return w < 0 || leftFraction || rightFraction ? w : w + (int)insetLeft + (int)insetRight;
	}

	@Override
	public int getIntrinsicHeight() {
		int h = super.getIntrinsicHeight();
		return h < 0 || topFraction || bottomFraction ? h : h + (int)insetTop + (int)insetBottom;
	}

	static final class InsetState extends DrawableWrapper.DrawableWrapperState {

		InsetState(DrawableWrapperState orig, Resources res) {
			super(orig, res);
		}

		@Override
		public Drawable newDrawable(Resources res) {
			throw new UnsupportedOperationException("Unimplemented method 'newDrawable'");
		}
	}
}

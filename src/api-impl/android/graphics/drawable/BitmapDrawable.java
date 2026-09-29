package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class BitmapDrawable extends Drawable {

	private Bitmap bitmap;
	/* the texture has no draw-time filter, so tint/color filter render a filtered copy */
	private ColorStateList tint = null;
	private PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;
	private ColorFilter colorFilter = null;
	private Bitmap filtered = null;
	private int filteredColor;
	private PorterDuff.Mode filteredMode;

	public BitmapDrawable() {
	}

	public BitmapDrawable(Resources res, Bitmap bitmap) {
		this.bitmap = bitmap;
		if (bitmap != null)
			setPaintable(bitmap.getTexture());
	}

	public BitmapDrawable(Bitmap bitmap) {
		this.bitmap = bitmap;
		if (bitmap != null)
			setPaintable(bitmap.getTexture());
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme)
	    throws XmlPullParserException, IOException {
		final TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.BitmapDrawable);
		if (a.hasValue(R.styleable.BitmapDrawable_src)) {
			BitmapDrawable dr = null;
			try {
				// TODO: getDrawable() might pick up another <bitmap> XML,
				// but we should reject anything other than image files.
				dr = (BitmapDrawable)a.getDrawable(R.styleable.BitmapDrawable_src);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
			if (dr != null)
				bitmap = dr.bitmap;
		}
		a.recycle();
		if (bitmap == null)
			throw new XmlPullParserException("<bitmap> needs a valid `src' attribute");
		setPaintable(bitmap.getTexture());
	}

	@Override
	public int getIntrinsicWidth() {
		if (bitmap == null)
			return 0;
		return bitmap.getWidth();
	}

	@Override
	public int getIntrinsicHeight() {
		if (bitmap == null)
			return 0;
		return bitmap.getHeight();
	}

	@Override
	public void setTintList(ColorStateList tint) {
		this.tint = tint;
		updateFilter();
	}

	@Override
	public void setTintMode(PorterDuff.Mode tintMode) {
		this.tintMode = tintMode != null ? tintMode : PorterDuff.Mode.SRC_IN;
		updateFilter();
	}

	@Override
	public void setColorFilter(ColorFilter filter) {
		colorFilter = filter;
		updateFilter();
	}

	@Override
	public ColorFilter getColorFilter() {
		return colorFilter;
	}

	private void updateFilter() {
		if (bitmap == null)
			return;
		int color;
		PorterDuff.Mode mode;
		if (colorFilter instanceof PorterDuffColorFilter) {
			color = ((PorterDuffColorFilter)colorFilter).getColor();
			mode = ((PorterDuffColorFilter)colorFilter).getMode();
		} else if (colorFilter == null && tint != null) {
			color = tint.getColorForState(getState(), tint.getDefaultColor());
			mode = tintMode;
		} else {
			if (colorFilter != null)
				System.out.println("BitmapDrawable: unsupported color filter " + colorFilter.getClass().getName());
			filtered = null;
			setPaintable(bitmap.getTexture());
			invalidateSelf();
			return;
		}
		if (filtered == null || filteredColor != color || filteredMode != mode) {
			filtered = filterBitmap(bitmap, color, mode);
			filteredColor = color;
			filteredMode = mode;
		}
		setPaintable(filtered.getTexture());
		invalidateSelf();
	}

	private static Bitmap filterBitmap(Bitmap src, int color, PorterDuff.Mode mode) {
		int w = src.getWidth(), h = src.getHeight();
		int[] px = new int[w * h];
		src.getPixels(px, 0, w, 0, 0, w, h);
		int ca = color >>> 24, cr = (color >> 16) & 0xff, cg = (color >> 8) & 0xff, cb = color & 0xff;
		for (int i = 0; i < px.length; i++) {
			int d = px[i];
			int da = d >>> 24, dr = (d >> 16) & 0xff, dg = (d >> 8) & 0xff, db = d & 0xff;
			int a, r, g, b;
			switch (mode) {
				case SRC_ATOP:
					a = da;
					r = (cr * ca + dr * (255 - ca)) / 255;
					g = (cg * ca + dg * (255 - ca)) / 255;
					b = (cb * ca + db * (255 - ca)) / 255;
					break;
				case MULTIPLY:
					a = ca * da / 255;
					r = cr * dr / 255;
					g = cg * dg / 255;
					b = cb * db / 255;
					break;
				default: // SRC_IN
					a = ca * da / 255;
					r = cr;
					g = cg;
					b = cb;
					break;
			}
			px[i] = (a << 24) | (r << 16) | (g << 8) | b;
		}
		Bitmap out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		out.setPixels(px, 0, w, 0, 0, w, h);
		return out;
	}

	public void setTileModeX(Shader.TileMode mode) {}

	public Paint getPaint() {
		return new Paint();
	}

	public void setGravity(int g) {}

	public void setAntiAlias(boolean arg0) {} // ATL-autostub
}

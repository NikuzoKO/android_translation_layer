package android.graphics;

import java.util.Locale;

public class Paint {
	public static final int ANTI_ALIAS_FLAG = (1 << 0);
	public static final int FILTER_BITMAP_FLAG = (1 << 1);
	public static final int DITHER_FLAG = (1 << 2);
	public static final int UNDERLINE_TEXT_FLAG = (1 << 3);
	public static final int STRIKE_THRU_TEXT_FLAG = (1 << 4);
	public static final int FAKE_BOLD_TEXT_FLAG = (1 << 5);
	public static final int LINEAR_TEXT_FLAG = (1 << 6);
	public static final int SUBPIXEL_TEXT_FLAG = (1 << 7);
	public static final int DEV_KERN_TEXT_FLAG = (1 << 8);
	public static final int LCD_RENDER_TEXT_FLAG = (1 << 9);
	public static final int EMBEDDED_BITMAP_TEXT_FLAG = (1 << 10);
	public static final int AUTO_HINTING_TEXT_FLAG = (1 << 11);
	public static final int VERTICAL_TEXT_FLAG = (1 << 12);

	public long paint; // native paint
	private Typeface typeface;
	private Xfermode xfermode;
	private Shader shader;
	private Align align = Align.CENTER;
	private ColorFilter color_filter;

	public Paint() {
		paint = native_create();
	}

	public Paint(int flags) {
		this();
		setFlags(flags);
	}

	public Paint(Paint paint) {
		this.paint = native_clone(paint.paint);
		this.typeface = paint.typeface;
		this.userColor = paint.userColor;
		this.shader = paint.shader;
	}

	private int userColor = 0xff000000;

	public void setColor(int color) {
		userColor = color;
		applyColor();
	}

	/* with a gradient shader the paint color only contributes its alpha (see Shader.atl_colors) */
	private void applyColor() {
		int avg = shader != null ? shader.atlAverageColor() : 0;
		if (shader != null && shader.atl_colors != null)
			native_set_color(paint, (avg & 0xffffff) | ((Color.alpha(avg) * Color.alpha(userColor) / 255) << 24));
		else
			native_set_color(paint, userColor);
	}

	public void setARGB(int a, int r, int g, int b) {
		setColor(Color.argb(a, r, g, b));
	}

	public int getColor() {
		return native_get_color(paint);
	}

	public void setAlpha(int a) {
		userColor = (userColor & 0xffffff) | ((a & 0xff) << 24);
		if (shader != null && shader.atl_colors != null)
			applyColor();
		else
			native_set_alpha(paint, a);
	}

	public int getAlpha() {
		return native_get_alpha(paint);
	}

	public void setAntiAlias(boolean aa) {
	}

	public boolean setFontVariationSettings(String fvs) {
		return true;
	}

	public void setStrokeWidth(float width) {
		native_set_stroke_width(paint, width);
	}
	public void setTextSize(float size) {
		native_set_text_size(paint, size);
	}

	public Typeface setTypeface(Typeface typeface) {
		this.typeface = typeface;
		Typeface tf = typeface != null ? typeface : Typeface.DEFAULT;
		native_set_typeface(paint, tf.getFamily(), tf.getWeight(), tf.isItalic());
		return typeface;
	}
	public void getTextBounds(String text, int start, int end, Rect bounds) {
		if (end > text.length())
			end = text.length();
		native_get_text_bounds(paint, text.substring(start, end), bounds);
	}
	public void getTextBounds(char[] text, int index, int count, Rect bounds) {
		native_get_text_bounds(paint, new String(text, index, count), bounds);
	}
	public int getTextWidths(String text, int start, int end, float[] widths) {
		Rect bounds = new Rect();
		native_get_text_bounds(paint, text.substring(start, end), bounds);
		return bounds.width();
	}
	public void setFilterBitmap(boolean filter) {}

	public void setFlags(int flags) {
		if ((flags & ANTI_ALIAS_FLAG) != 0)
			setAntiAlias(true);
	}

	public void setStyle(Style style) {
		native_set_style(paint, style.ordinal());
	}

	public float ascent() {
		float[] m = new float[5];
		native_get_font_metrics(paint, m);
		return m[1];
	}

	public float descent() {
		float[] m = new float[5];
		native_get_font_metrics(paint, m);
		return m[2];
	}

	public float measureText(char[] text, int index, int count) {
		return measureText(new String(text, index, count));
	}
	public float measureText(String text, int start, int end) {
		if (text == null || end <= start)
			return 0;
		return native_measure_text(paint, text.substring(start, end));
	}
	public float measureText(String text) {
		return measureText(text, 0, text.length());
	}
	public float measureText(CharSequence text, int start, int end) {
		return measureText(text.toString(), start, end);
	}

	public ColorFilter setColorFilter(ColorFilter colorFilter) {
		if (colorFilter instanceof PorterDuffColorFilter) {
			PorterDuffColorFilter porterDuff = (PorterDuffColorFilter)colorFilter;
			native_set_color_filter(paint, porterDuff.getMode().ordinal(), porterDuff.getColor());
		} else {
			native_set_color_filter(paint, -1, 0);
		}
		color_filter = colorFilter;
		return colorFilter;
	}

	public Object getColorFilter() {
		return color_filter;
	}

	public Shader setShader(Shader shader) {
		boolean was = this.shader != null && this.shader.atl_colors != null;
		this.shader = shader;
		if (was || (shader != null && shader.atl_colors != null))
			applyColor();
		return shader;
	}

	public enum Style {
		/**
		 * Geometry and text drawn with this style will be filled, ignoring all
		 * stroke-related settings in the paint.
		 */
		FILL,
		/**
		 * Geometry and text drawn with this style will be stroked, respecting
		 * the stroke-related fields on the paint.
		 */
		STROKE,
		/**
		 * Geometry and text drawn with this style will be both filled and
		 * stroked at the same time, respecting the stroke-related fields on
		 * the paint. This mode can give unexpected results if the geometry
		 * is oriented counter-clockwise. This restriction does not apply to
		 * either FILL or STROKE.
		 */
		FILL_AND_STROKE;

		public static final Style values[] = Style.values();
	}

	public static class FontMetrics {
		/**
		 * The maximum distance above the baseline for the tallest glyph in
		 * the font at a given text size.
		 */
		public float top;
		/**
		 * The recommended distance above the baseline for singled spaced text.
		 */
		public float ascent;
		/**
		 * The recommended distance below the baseline for singled spaced text.
		 */
		public float descent;
		/**
		 * The maximum distance below the baseline for the lowest glyph in
		 * the font at a given text size.
		 */
		public float bottom;
		/**
		 * The recommended additional space to add between lines of text.
		 */
		public float leading;
	}

	public static class FontMetricsInt {
		public int top;
		public int ascent;
		public int descent;
		public int bottom;
		public int leading;

		@Override
		public String toString() {
			return "FontMetricsInt: top=" + top + " ascent=" + ascent + " descent=" + descent + " bottom=" + bottom + " leading=" + leading;
		}
	}

	public /*native*/ int getFlags() { return 0; }

	public /*native*/ int getHinting() { return 0; }
	public /*native*/ void setHinting(int mode) {}

	public /*native*/ void setDither(boolean dither) {}
	public boolean isDither() {
		return false;
	}

	public /*native*/ void setLinearText(boolean linearText) {}
	public /*native*/ void setSubpixelText(boolean subpixelText) {}
	public /*native*/ void setUnderlineText(boolean underlineText) {}
	public /*native*/ void setStrikeThruText(boolean strikeThruText) {}
	public /*native*/ void setFakeBoldText(boolean fakeBoldText) {}

	public float getStrokeWidth() {
		return native_get_stroke_width(paint);
	}

	public /*native*/ float getStrokeMiter() { return 0; }
	public /*native*/ void setStrokeMiter(float miter) {}
	public /*native*/ float getTextSize() {
		return native_get_text_size(paint);
	}

	public /*native*/ float getTextScaleX() { return 0; }
	public /*native*/ void setTextScaleX(float scaleX) {}
	public /*native*/ float getTextSkewX() { return 0; }
	public /*native*/ void setTextSkewX(float skewX) {}

	/* {top, ascent, descent, bottom, leading}, Android signs (above the baseline is negative) */
	public float getFontMetrics(FontMetrics metrics) {
		float[] m = new float[5];
		native_get_font_metrics(paint, m);
		if (metrics != null) {
			metrics.top = m[0];
			metrics.ascent = m[1];
			metrics.descent = m[2];
			metrics.bottom = m[3];
			metrics.leading = m[4];
		}
		return m[2] - m[1] + m[4];
	}
	public int getFontMetricsInt(FontMetricsInt fmi) {
		float[] m = new float[5];
		native_get_font_metrics(paint, m);
		if (fmi != null) {
			fmi.top = (int)Math.floor(m[0]);
			fmi.ascent = Math.round(m[1]);
			fmi.descent = Math.round(m[2]);
			fmi.bottom = (int)Math.ceil(m[3]);
			fmi.leading = Math.round(m[4]);
		}
		return Math.round(m[2] - m[1] + m[4]);
	}

	public void setShadowLayer(float radius, float dx, float dy, int color) {}

	public Xfermode setXfermode(Xfermode xfermode) {
		this.xfermode = xfermode;
		return xfermode;
	}

	public Xfermode getXfermode() {
		return xfermode;
	}

	public void setBlendMode(BlendMode blendmode) {
		setXfermode(blendmode == null ? null : blendmode.getXfermode());
	}

	public BlendMode getBlendMode() {
		if (this.xfermode instanceof PorterDuffXfermode) {
			return BlendMode.fromValue(((PorterDuffXfermode)this.xfermode).porterDuffMode);
		}
		return null;
	}

	public void setLetterSpacing(float spacing) {}

	public enum Cap {
		/**
		 * The stroke ends with the path, and does not project beyond it.
		 */
		BUTT,
		/**
		 * The stroke projects out as a semicircle, with the center at the
		 * end of the path.
		 */
		ROUND,
		/**
		 * The stroke projects out as a square, with the center at the end
		 * of the path.
		 */
		SQUARE;

		public static final Cap values[] = Cap.values();
	}

	public enum Join {
		/**
		 * The outer edges of a join meet at a sharp angle
		 */
		MITER,
		/**
		 * The outer edges of a join meet in a circular arc.
		 */
		ROUND,
		/**
		 * The outer edges of a join meet with a straight line
		 */
		BEVEL;

		public static final Join values[] = Join.values();
	}

	public enum Align {
		LEFT,
		CENTER,
		RIGHT,
	}

	public void setStrokeCap(Cap cap) {
		native_set_stroke_cap(paint, cap.ordinal());
	}

	public void setStrokeJoin(Join join) {
		native_set_stroke_join(paint, join.ordinal());
	}

	public Typeface getTypeface() {
		return typeface;
	}

	public void setTextAlign(Align align) {
		this.align = align;
		native_set_text_align(paint, align.ordinal());
	}

	public Shader getShader() {
		return shader;
	}

	public PathEffect getPathEffect() {
		return new PathEffect();
	}

	public PathEffect setPathEffect(PathEffect effect) {
		return effect;
	}

	public int breakText(char[] text, int index, int count, float maxWidth, float[] measuredWidth) {
		return breakText(new String(text, index, count), true, maxWidth, measuredWidth);
	}
	public int breakText(String text, boolean measureForwards, float maxWidth, float[] measuredWidth) {
		return breakText(text, 0, text.length(), measureForwards, maxWidth, measuredWidth);
	}
	/* how many chars (from the start or, backwards, from the end) fit in maxWidth */
	public int breakText(CharSequence text, int start, int end, boolean measureForwards, float maxWidth, float[] measuredWidth) {
		String s = text.subSequence(start, end).toString();
		int lo = 0, hi = s.length();
		while (lo < hi) {
			int mid = (lo + hi + 1) / 2;
			String part = measureForwards ? s.substring(0, mid) : s.substring(s.length() - mid);
			if (measureText(part) <= maxWidth)
				lo = mid;
			else
				hi = mid - 1;
		}
		if (measuredWidth != null && measuredWidth.length > 0)
			measuredWidth[0] = lo == 0 ? 0 : measureText(measureForwards ? s.substring(0, lo) : s.substring(s.length() - lo));
		return lo;
	}

	public void clearShadowLayer() {}

	public boolean hasShadowLayer() {
		return false;
	}

	public FontMetrics getFontMetrics() {
		FontMetrics fm = new FontMetrics();
		getFontMetrics(fm);
		return fm;
	}

	public void setFontMetricsInt(FontMetricsInt fmi) {}

	public FontMetricsInt getFontMetricsInt() {
		FontMetricsInt fmi = new FontMetricsInt();
		getFontMetricsInt(fmi);
		return fmi;
	}

	public void set(Paint paint) {
		native_recycle(this.paint);
		this.paint = native_clone(paint.paint);
		this.typeface = paint.typeface;
		this.userColor = paint.userColor;
		this.shader = paint.shader;
	}

	public boolean isFilterBitmap() { return false; }

	public Cap getStrokeCap() {
		return Cap.values[native_get_stroke_cap(paint)];
	}

	public Join getStrokeJoin() {
		return Join.values[native_get_stroke_join(paint)];
	}

	public Locale getTextLocale() { return Locale.getDefault(); }

	public float getLetterSpacing() { return 1.0f; }

	public Style getStyle() {
		return Style.values[native_get_style(paint)];
	}

	public Align getTextAlign() {
		return align;
	}

	public boolean hasGlyph(String text) { return false; }

	public MaskFilter setMaskFilter(MaskFilter filter) { return filter; }

	public boolean isFakeBoldText() {
		return false;
	}

	public void reset() {
		native_recycle(paint);
		paint = native_create();
		xfermode = null;
		shader = null;
		align = Align.CENTER;
		color_filter = null;
	}

	private static native long native_create();
	private static native long native_clone(long paint);
	private static native void native_recycle(long paint);
	private static native void native_set_color(long paint, int color);
	private static native int native_get_color(long paint);
	private static native void native_set_alpha(long paint, int alpha);
	private static native int native_get_alpha(long paint);
	private static native void native_set_style(long paint, int style);
	private static native int native_get_style(long paint);
	private static native void native_set_stroke_width(long paint, float width);
	private static native float native_get_stroke_width(long paint);
	private static native void native_set_stroke_cap(long paint, int cap);
	private static native int native_get_stroke_cap(long paint);
	private static native void native_set_stroke_join(long paint, int join);
	private static native int native_get_stroke_join(long paint);
	private static native void native_set_text_size(long paint, float size);
	private static native float native_get_text_size(long paint);
	private static native void native_set_color_filter(long paint, int mode, int color);
	private static native void native_get_text_bounds(long paint, String text, Rect bounds);
	private static native void native_set_text_align(long paint, int align);

	public boolean equalsForTextMeasurement(android.graphics.Paint arg0) { return false; } // ATL-autostub
	public boolean isAntiAlias() { return false; } // ATL-autostub
	public boolean isElegantTextHeight() { return false; } // ATL-autostub
	public boolean isUnderlineText() { return false; } // ATL-autostub
	public float getUnderlinePosition() { return 0f; } // ATL-autostub
	public float getUnderlineThickness() { return 0f; } // ATL-autostub
	public int getOffsetForAdvance(java.lang.CharSequence arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, float arg6) { return 0; } // ATL-autostub
	public int getShadowLayerColor() { return 0; } // ATL-autostub
	public int getTextWidths(java.lang.CharSequence arg0, int arg1, int arg2, float[] arg3) { return 0; } // ATL-autostub
	public int getTextWidths(java.lang.String arg0, float[] arg1) { return 0; } // ATL-autostub
	public java.lang.String getFontFeatureSettings() { return null; } // ATL-autostub
	public void getTextBounds(java.lang.CharSequence arg0, int arg1, int arg2, android.graphics.Rect arg3) {} // ATL-autostub
	public void getTextPath(java.lang.String arg0, int arg1, int arg2, float arg3, float arg4, android.graphics.Path arg5) {} // ATL-autostub
	public void setFontFeatureSettings(java.lang.String arg0) {} // ATL-autostub
	public void setTextLocale(java.util.Locale arg0) {} // ATL-autostub
	private static native void native_set_typeface(long paint, String family, int weight, boolean italic);
	private static native float native_measure_text(long paint, String text);
	private static native void native_get_font_metrics(long paint, float[] metrics);
}

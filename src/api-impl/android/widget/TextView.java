package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.BaseMovementMethod;
import android.text.method.KeyListener;
import android.text.method.MovementMethod;
import android.text.method.TransformationMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

public class TextView extends View {
	private ColorStateList colors = new ColorStateList(new int[][] {new int[0]}, new int[1]);
	private CharSequence text = "";
	private TextPaint paint = new TextPaint();
	private boolean include_padding = false;
	private int break_strategy = 0 /*BREAK_STRATEGY_SIMPLE*/;
	private int hyphenation_frequency = 0 /*HYPHENATION_FREQUENCY_NONE*/;
	private int gravity = Gravity.CENTER;

	public TextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextView(Context context) {
		this(context, null);
	}

	public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public TextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

		TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.TextView, defStyleAttr, 0);
		try {
			if (a.hasValue(com.android.internal.R.styleable.TextView_text)) {
				setText(a.getText(com.android.internal.R.styleable.TextView_text));
			}
			if (a.hasValue(com.android.internal.R.styleable.TextView_hint)) {
				setHint(a.getText(com.android.internal.R.styleable.TextView_hint));
			}

			int ap = a.getResourceId(com.android.internal.R.styleable.TextView_textAppearance, -1);
			if (ap != -1) {
				TypedArray aa = context.obtainStyledAttributes(ap, com.android.internal.R.styleable.TextAppearance);
				if (aa.hasValue(com.android.internal.R.styleable.TextAppearance_textColor)) {
					setTextColor(aa.getColorStateList(com.android.internal.R.styleable.TextAppearance_textColor));
				}
				if (aa.hasValue(com.android.internal.R.styleable.TextAppearance_textSize)) {
					setTextSize(aa.getDimensionPixelSize(com.android.internal.R.styleable.TextAppearance_textSize, 10));
				}
				aa.recycle();
			}
			if (a.hasValue(com.android.internal.R.styleable.TextView_textColor)) {
				setTextColor(a.getColorStateList(com.android.internal.R.styleable.TextView_textColor));
			}
			if (a.hasValue(com.android.internal.R.styleable.TextView_textSize)) {
				setTextSize(a.getDimensionPixelSize(com.android.internal.R.styleable.TextView_textSize, 10));
			}

			if (a.hasValue(com.android.internal.R.styleable.TextView_textStyle)) {
				int textStyle = a.getInt(com.android.internal.R.styleable.TextView_textStyle, 0);
				setTypeface(getTypeface(), textStyle);
			}

			if (a.hasValue(com.android.internal.R.styleable.TextView_textAllCaps)) {
				boolean allCaps = a.getBoolean(com.android.internal.R.styleable.TextView_textAllCaps, false);
				setAllCaps(allCaps);
			}

			if (a.getBoolean(com.android.internal.R.styleable.TextView_singleLine, false))
				maxLines = 1;
			if (a.hasValue(com.android.internal.R.styleable.TextView_lines))
				maxLines = a.getInt(com.android.internal.R.styleable.TextView_lines, maxLines);
			if (a.hasValue(com.android.internal.R.styleable.TextView_maxLines))
				maxLines = a.getInt(com.android.internal.R.styleable.TextView_maxLines, maxLines);
			if (a.hasValue(com.android.internal.R.styleable.TextView_ellipsize)) {
				// attr values: none=0 start=1 middle=2 end=3 marquee=4
				int e = a.getInt(com.android.internal.R.styleable.TextView_ellipsize, 0);
				ellipsize = e == 1 ? TextUtils.TruncateAt.START : e == 2 ? TextUtils.TruncateAt.MIDDLE : e == 3 ? TextUtils.TruncateAt.END : e == 4 ? TextUtils.TruncateAt.MARQUEE : null;
			}
			updateLineLimit();
		} catch (java.lang.Exception e) {
			System.out.println("exception while inflating TextView:");
			e.printStackTrace();
		}

		a.recycle();
		haveCustomMeasure = false;
	}

	@Override
	protected native long native_constructor(Context context, AttributeSet attrs);

	public void setText(int resId) {
		setText(getContext().getResources().getText(resId));
	}

	public void setText(CharSequence text) {
		setText(text, BufferType.NORMAL);
	}

	public void setText(CharSequence text, BufferType type) {
		this.text = text == null ? "" : text;
		native_setText(text != null ? text.toString() : null);

		if (text instanceof android.text.Spanned)
			native_set_markup(1);
		if (!isLayoutRequested())
			requestLayout();
	}

	public void setText(char[] text, int start, int len) {
		setText(new String(text, start, len));
	}

	public final void append(CharSequence text) {
		append(text, 0, text.length());
	}

	public void append(CharSequence text, int start, int end) {
		setText(SpannableStringBuilder.valueOf(this.text).append(text, start, end));
	}

	private native final void native_set_markup(int bool);

	public native final void native_setText(String text);

	public void setTextSize(int unit, float size) {
		if (unit != TypedValue.COMPLEX_UNIT_SP)
			System.out.println("setTextSize called with non-SP unit (" + unit + "), we don't currently handle that");
		setTextSize(size);
	}
	public native void setTextSize(float size);

	public native final void native_setTextColor(int color);
	public void setTextColor(int color) {
		native_setTextColor(color);
	}
	public void setTextColor(ColorStateList colors) {
		if (colors != null) {
			this.colors = colors;
			native_setTextColor(colors.getDefaultColor()); // TODO: do this properly
		}
	}
	public void setTypeface(Typeface tf, int style) {
		String[] classesToRemove = {"ATL-font-bold", "ATL-font-italic"};
		native_removeClasses(widget, classesToRemove);

		switch (style) {
			case Typeface.BOLD:
				native_addClass(widget, "ATL-font-bold");
				break;
			case Typeface.ITALIC:
				native_addClass(widget, "ATL-font-italic");
				break;
			case Typeface.BOLD_ITALIC:
				native_addClass(widget, "ATL-font-bold");
				native_addClass(widget, "ATL-font-italic");
				break;
			default:
				break;
		}
	}
	public void setTypeface(Typeface tf) {}
	public void setLineSpacing(float add, float mult) {}
	public final void setLinksClickable(boolean whether) {}

	public void setInputType(int type) {}
	public void setFilters(InputFilter[] filters) {}
	public void setCursorVisible(boolean visible) {}
	public void setImeOptions(int imeOptions) {}

	public final ColorStateList getTextColors() { return colors; }
	public static ColorStateList getTextColors(Context context, TypedArray attrs) { return new ColorStateList(new int[][] {new int[0]}, new int[1]); }

	public TextPaint getPaint() {
		return paint;
	}

	public void addTextChangedListener(TextWatcher watcher) {}
	public void removeTextChangedListener(TextWatcher watcher) {}
	public void setOnEditorActionListener(TextView.OnEditorActionListener l) {}

	public TransformationMethod getTransformationMethod() {
		return null;
	}

	public void setHintTextColor(ColorStateList colorStateList) {}
	public void setHintTextColor(int i) {}
	public void setLinkTextColor(ColorStateList colorStateList) {}

	/* line limit and ellipsizing, done by the GtkLabel (lines + ellipsize) */
	private int maxLines = Integer.MAX_VALUE;
	private TextUtils.TruncateAt ellipsize = null;

	private void updateLineLimit() {
		int mode = 0; // PANGO_ELLIPSIZE_NONE
		if (ellipsize == TextUtils.TruncateAt.START)
			mode = 1;
		else if (ellipsize == TextUtils.TruncateAt.MIDDLE)
			mode = 2;
		else if (ellipsize != null)
			mode = 3; // END, MARQUEE
		native_setLineLimit(maxLines == Integer.MAX_VALUE ? -1 : maxLines, mode);
	}

	protected native void native_setLineLimit(int lines, int ellipsizeMode);

	public void setSingleLine() {
		setSingleLine(true);
	}
	public void setSelection(int i) {}
	public void setSelection(int i, int j) {}

	public int getSelectionStart() {
		return 0;
	}

	public int getSelectionEnd() {
		return 0;
	}

	public void setEllipsize(TextUtils.TruncateAt truncateAt) {
		ellipsize = truncateAt;
		updateLineLimit();
	}

	public void setTextAppearance(Context context, int appearance) {}

	public void setMaxLines(int maxLines) {
		this.maxLines = maxLines > 0 ? maxLines : Integer.MAX_VALUE;
		updateLineLimit();
	}

	public void setMinWidth(int minWidth) {}
	public void setMaxWidth(int maxWidth) {}

	public Typeface getTypeface() { return null; }

	public float getTextSize() { return 10; }

	public int getGravity() {
		return gravity;
	}

	public int getCompoundPaddingTop() { return 0; }
	public int getCompoundPaddingBottom() { return 0; }

	public CharSequence getText() {
		return text;
	};

	public void setCompoundDrawablePadding(int pad) {}

	protected native void native_setCompoundDrawables(long widget, long left, long top, long right, long bottom);

	// just to prevent garbage collection while native side uses it
	private Drawable drawableLeft = null;
	private Drawable drawableTop = null;
	private Drawable drawableRight = null;
	private Drawable drawableBottom = null;

	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		native_setCompoundDrawables(widget,
		                            left != null ? left.paintable : 0,
		                            top != null ? top.paintable : 0,
		                            right != null ? right.paintable : 0,
		                            bottom != null ? bottom.paintable : 0);
		drawableLeft = left;
		drawableTop = top;
		drawableRight = right;
		drawableBottom = bottom;
	}

	public void setAllCaps(boolean allCaps) {
		String[] classesToRemove = {"ATL-text-uppercase"};
		native_removeClasses(widget, classesToRemove);

		if (allCaps) {
			native_addClass(widget, "ATL-text-uppercase");
		}
	}

	public void setSaveEnabled(boolean enabled) {}

	public final void setAutoLinkMask(int mask) {}

	public void setEditableFactory(Editable.Factory factory) {}

	public KeyListener getKeyListener() { return null; }

	public int getInputType() { return 0; }

	public final void setTransformationMethod(TransformationMethod method) {}

	public InputFilter[] getFilters() { return new InputFilter[0]; }

	public int getMaxLines() { return maxLines == Integer.MAX_VALUE ? -1 : maxLines; }

	public void setCompoundDrawablesRelative(Drawable start, Drawable top, Drawable end, Drawable bottom) {
		setCompoundDrawables(start, top, end, bottom);
	}

	public int getLineCount() { return 1; }

	public URLSpan[] getUrls() { return new URLSpan[0]; }

	public void setMovementMethod(MovementMethod method) {}

	public void setTextIsSelectable(boolean selectable) {}

	public MovementMethod getMovementMethod() {
		return new BaseMovementMethod();
	}

	public CharSequence getHint() { return "HINT"; }

	public int getMinHeight() { return 0; }
	public int getMinWidth() { return 0; }
	public void setMinHeight(int minHeight) {}
	public void setMaxHeight(int maxHeight) {}

	public void setHorizontallyScrolling(boolean whether) {}
	public boolean getHorizontallyScrolling() {
		return false;
	}

	public static interface OnEditorActionListener {
		public abstract boolean onEditorAction(TextView v, int actionId, KeyEvent event);
	}

	public static enum BufferType {
		EDITABLE,
		NORMAL,
		SPANNABLE,
	}

	public Layout getLayout() {
		return new BoringLayout(getText(), getPaint(), getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, new BoringLayout.Metrics(), false);
	}

	public int getCurrentTextColor() {
		return Color.CYAN;
	}

	public void setSingleLine(boolean singleLine) {
		setMaxLines(singleLine ? 1 : Integer.MAX_VALUE);
	}

	public int getCompoundPaddingLeft() { return 0; }

	public int getCompoundPaddingRight() { return 0; }

	public void setHint(int resId) {
		setHint(getContext().getResources().getText(resId));
	}

	public float getLetterSpacing() { return 0.f; }

	public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {}

	public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom) {}

	public boolean getLinksClickable() { return true; }

	public boolean isTextSelectable() { return true; }

	public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {}

	public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {}

	public void setHint(CharSequence s) {}

	public Drawable[] getCompoundDrawablesRelative() { return new Drawable[4]; }

	public Drawable[] getCompoundDrawables() { return new Drawable[4]; }

	public void setTextAppearance(int dummy) {}

	public int length() {
		return getText().length();
	}

	public void setHighlightColor(int color) {}

	public Editable getEditableText() {
		return new SpannableStringBuilder(getText());
	}

	public int getMaxWidth() { return 1000; }

	public void nullLayouts() {}

	public void setLinkTextColor(int color) {}

	public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {}

	public int getExtendedPaddingTop() { return 0; }

	public void setRawInputType(int type) {}

	public TextUtils.TruncateAt getEllipsize() { return ellipsize; }

	public void setLines(int lines) {
		setMaxLines(lines);
	}

	public int getMinLines() {
		return -1;
	}

	public void setMinLines(int lines) {}

	public void setSelectAllOnFocus(boolean selectAllOnFocus) {}

	public int getCompoundDrawablePadding() { return 0; }

	public int getPaintFlags() { return 0; }

	public void setPaintFlags(int flags) {}

	public int getLineHeight() {
		return 10; // FIXME
	}

	public int getMaxHeight() {
		return -1;
	}

	public boolean isAllCaps() { return false; }

	public int getAutoSizeStepGranularity() {
		return -1;
	}

	public void setAutoSizeTextTypeUniformWithPresetSizes(int[] presetSizes, int unit) {}

	public void setCompoundDrawableTintList(ColorStateList tint) {}

	public int getHyphenationFrequency() {
		return hyphenation_frequency;
	}

	public void setHyphenationFrequency(int hyphenationFrequency) {
		hyphenation_frequency = hyphenationFrequency;
	}

	public boolean getIncludeFontPadding() { return include_padding; }

	public void setIncludeFontPadding(boolean includePadding) {
		include_padding = includePadding;
	}

	public float getLineSpacingExtra() { return 0.f; }

	public float getLineSpacingMultiplier() { return 1.f; }

	public TextDirectionHeuristic getTextDirectionHeuristic() {
		return TextDirectionHeuristics.LTR;
	}

	public Bundle getInputExtras(boolean key) {
		return new Bundle();
	}

	public void setError(CharSequence error) {
		System.out.println("ERROR: " + error);
	}

	public int getTotalPaddingLeft() { return 0; }

	public int getTotalPaddingTop() { return 0; }

	public int getTotalPaddingRight() { return 0; }

	public int getTotalPaddingBottom() { return 0; }

	public int getImeOptions() { return 0; }

	public void setShadowLayer(float radius, float dx, float dy, int color) {}

	public int getBreakStrategy() {
		return break_strategy;
	}

	public void setBreakStrategy(int strategy) {
		break_strategy = strategy;
	}

	public void clearComposingText() {}

	public void setKeyListener(KeyListener keyListener) {}

	public int getAutoLinkMask() { return 0; }

	public void setWidth(int width) {}
	public void setHeight(int height) {}

	public void setFreezesText(boolean freezesText) {}

	public void setLetterSpacing(float letterSpacing) {}

	public void setMarqueeRepeatLimit(int marqueeLimit) {}

	public void setShowSoftInputOnFocus(boolean showSoftInputOnFocus) {}

	public void setCustomInsertionActionModeCallback(ActionMode.Callback actionModeCallback) {}

	@UnsupportedAppUsage /* androidx ACTVAutoSizeHelper seems to love this */
	/* Copyright (C) 2006 The Android Open Source Project */
	private Layout.Alignment getLayoutAlignment() {
		Layout.Alignment alignment;
		switch (getTextAlignment()) {
			case TEXT_ALIGNMENT_GRAVITY:
				switch (gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
					case Gravity.START:
						alignment = Layout.Alignment.ALIGN_NORMAL;
						break;
					case Gravity.END:
						alignment = Layout.Alignment.ALIGN_OPPOSITE;
						break;
					case Gravity.LEFT:
						alignment = Layout.Alignment.ALIGN_LEFT;
						break;
					case Gravity.RIGHT:
						alignment = Layout.Alignment.ALIGN_RIGHT;
						break;
					case Gravity.CENTER_HORIZONTAL:
						alignment = Layout.Alignment.ALIGN_CENTER;
						break;
					default:
						alignment = Layout.Alignment.ALIGN_NORMAL;
						break;
				}
				break;
			case TEXT_ALIGNMENT_TEXT_START:
				alignment = Layout.Alignment.ALIGN_NORMAL;
				break;
			case TEXT_ALIGNMENT_TEXT_END:
				alignment = Layout.Alignment.ALIGN_OPPOSITE;
				break;
			case TEXT_ALIGNMENT_CENTER:
				alignment = Layout.Alignment.ALIGN_CENTER;
				break;
			case TEXT_ALIGNMENT_VIEW_START:
				alignment = (getLayoutDirection() == LAYOUT_DIRECTION_RTL) ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
				break;
			case TEXT_ALIGNMENT_VIEW_END:
				alignment = (getLayoutDirection() == LAYOUT_DIRECTION_RTL) ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
				break;
			case TEXT_ALIGNMENT_INHERIT:
				// This should never happen as we have already resolved the text alignment
				// but better safe than sorry so we just fall through
			default:
				alignment = Layout.Alignment.ALIGN_NORMAL;
				break;
		}
		return alignment;
	}

	public android.content.res.ColorStateList getHintTextColors() { return ((Object)this) instanceof android.content.res.ColorStateList ? (android.content.res.ColorStateList)(Object)this : null; } // ATL-autostub
	public android.graphics.drawable.Drawable getTextCursorDrawable() { return ((Object)this) instanceof android.graphics.drawable.Drawable ? (android.graphics.drawable.Drawable)(Object)this : null; } // ATL-autostub
	public android.os.Parcelable onSaveInstanceState() { return ((Object)this) instanceof android.os.Parcelable ? (android.os.Parcelable)(Object)this : null; } // ATL-autostub
	public android.view.ActionMode.Callback getCustomInsertionActionModeCallback() { return ((Object)this) instanceof android.view.ActionMode.Callback ? (android.view.ActionMode.Callback)(Object)this : null; } // ATL-autostub
	public android.view.ActionMode.Callback getCustomSelectionActionModeCallback() { return ((Object)this) instanceof android.view.ActionMode.Callback ? (android.view.ActionMode.Callback)(Object)this : null; } // ATL-autostub
	public boolean getShowSoftInputOnFocus() { return false; } // ATL-autostub
	public boolean isHorizontallyScrollable() { return false; } // ATL-autostub
	public boolean onKeyPreIme(int arg0, android.view.KeyEvent arg1) { return false; } // ATL-autostub
	public boolean onKeyUp(int arg0, android.view.KeyEvent arg1) { return false; } // ATL-autostub
	public boolean onPrivateIMECommand(java.lang.String arg0, android.os.Bundle arg1) { return false; } // ATL-autostub
	public boolean setFontVariationSettings(java.lang.String arg0) { return false; } // ATL-autostub
	public float getShadowDx() { return 0f; } // ATL-autostub
	public float getShadowDy() { return 0f; } // ATL-autostub
	public float getShadowRadius() { return 0f; } // ATL-autostub
	public float getTextScaleX() { return 0f; } // ATL-autostub
	public int getAutoSizeMaxTextSize() { return 0; } // ATL-autostub
	public int getAutoSizeMinTextSize() { return 0; } // ATL-autostub
	public int getAutoSizeTextType() { return 0; } // ATL-autostub
	public int getCurrentHintTextColor() { return 0; } // ATL-autostub
	public int getHighlightColor() { return 0; } // ATL-autostub
	public int getImeActionId() { return 0; } // ATL-autostub
	public int getLineBounds(int arg0, android.graphics.Rect arg1) { return 0; } // ATL-autostub
	public int getOffsetForPosition(float arg0, float arg1) { return 0; } // ATL-autostub
	public int getShadowColor() { return 0; } // ATL-autostub
	public int[] getAutoSizeTextAvailableSizes() { return null; } // ATL-autostub
	public void beginBatchEdit() {} // ATL-autostub
	public void drawableStateChanged() {} // ATL-autostub
	public void endBatchEdit() {} // ATL-autostub
	protected void onFocusChanged(boolean arg0, int arg1, android.graphics.Rect arg2) {} // ATL-autostub
	public void onRestoreInstanceState(android.os.Parcelable arg0) {} // ATL-autostub
	public void onRtlPropertiesChanged(int arg0) {} // ATL-autostub
	public void onSelectionChanged(int arg0, int arg1) {} // ATL-autostub
	public void onTextChanged(java.lang.CharSequence arg0, int arg1, int arg2, int arg3) {} // ATL-autostub
	public void setAutoSizeTextTypeUniformWithConfiguration(int arg0, int arg1, int arg2, int arg3) {} // ATL-autostub
	public void setAutoSizeTextTypeWithDefaults(int arg0) {} // ATL-autostub
	public void setCompoundDrawableTintMode(android.graphics.PorterDuff.Mode arg0) {} // ATL-autostub
	public void setFirstBaselineToTopHeight(int arg0) {} // ATL-autostub
	public void setImeActionLabel(java.lang.CharSequence arg0, int arg1) {} // ATL-autostub
	public void setImportantForContentCapture(int arg0) {} // ATL-autostub
	public void setLastBaselineToBottomHeight(int arg0) {} // ATL-autostub
	public void setLineHeight(int arg0) {} // ATL-autostub
	public void setLineHeight(int arg0, float arg1) {} // ATL-autostub
	public void setMaxEms(int arg0) {} // ATL-autostub
	public void setMinEms(int arg0) {} // ATL-autostub
	public void setOutlineAmbientShadowColor(int arg0) {} // ATL-autostub
	public void setOutlineSpotShadowColor(int arg0) {} // ATL-autostub
	public void setPrivateImeOptions(java.lang.String arg0) {} // ATL-autostub
	public void setText(int arg0, android.widget.TextView.BufferType arg1) {} // ATL-autostub
	public void setTextCursorDrawable(android.graphics.drawable.Drawable arg0) {} // ATL-autostub
	public void setTextLocale(java.util.Locale arg0) {} // ATL-autostub
	public void setTextLocales(android.os.LocaleList arg0) {} // ATL-autostub
	public void setTextScaleX(float arg0) {} // ATL-autostub
	public void setUseBoundsForWidth(boolean arg0) {} // ATL-autostub
}

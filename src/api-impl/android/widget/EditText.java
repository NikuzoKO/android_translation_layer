package android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class EditText extends TextView {
	public EditText(Context context) {
		super(context);
	}

	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditText(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
		super(context, attrs, defStyle, defStyleRes);
	}

	@Override
	protected long native_constructor(Context context, AttributeSet attrs) {
		return native_constructor_multiline(context, attrs, isMultiLine(context, attrs));
	}

	/* the native widget is chosen at construction (GtkText or a wrapping GtkTextView), so only the
	   XML attributes count: inputType with textMultiLine, or maxLines/lines/minLines > 1 without singleLine */
	private static boolean isMultiLine(Context context, AttributeSet attrs) {
		if (attrs == null)
			return false;
		android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.TextView, 0, 0);
		try {
			if (a.getBoolean(com.android.internal.R.styleable.TextView_singleLine, false))
				return false;
			if (a.hasValue(com.android.internal.R.styleable.TextView_inputType))
				return (a.getInt(com.android.internal.R.styleable.TextView_inputType, 0) & 0x20000 /* InputType.TYPE_TEXT_FLAG_MULTI_LINE */) != 0;
			return a.getInt(com.android.internal.R.styleable.TextView_maxLines, 1) > 1
			    || a.getInt(com.android.internal.R.styleable.TextView_lines, 1) > 1
			    || a.getInt(com.android.internal.R.styleable.TextView_minLines, 1) > 1;
		} catch (Exception e) {
			return false;
		} finally {
			a.recycle();
		}
	}

	protected native long native_constructor_multiline(Context context, AttributeSet attrs, boolean multiline);
	protected native String native_getText(long widget);
	protected native void native_addTextChangedListener(long widget, TextWatcher watcher);
	protected native void native_removeTextChangedListener(long widget, TextWatcher watcher);
	protected native void native_setOnEditorActionListener(long widget, OnEditorActionListener l);
	protected native void native_setText(long widget, String text);
	protected native void native_setHint(long widget, CharSequence s);
	protected native CharSequence native_getHint(long widget); // gtk_entry_set_placeholder_text

	public Editable getText() {
		return new SpannableStringBuilder(native_getText(widget));
	}

	public Editable getEditableText() {
		return new SpannableStringBuilder(native_getText(widget));
	}

	@Override
	public void setText(CharSequence text) {
		native_setText(widget, text == null ? "" : text.toString());
	}
	@Override
	protected void applyTextSize(float px) {
		native_setFontSizeCss(widget, px);
	}

	@Override
	public void removeTextChangedListener(TextWatcher watcher) {
		native_removeTextChangedListener(widget, watcher);
	}

	@Override
	public void addTextChangedListener(TextWatcher watcher) {
		native_addTextChangedListener(widget, watcher);
	}

	@Override
	public void setOnEditorActionListener(OnEditorActionListener l) {
		native_setOnEditorActionListener(widget, l);
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {}

	@Override
	public void setHint(CharSequence s) {
		if (System.getenv("ATL_DEBUG_HINT") != null)
			android.util.Slog.i("EditText", "ATL_DEBUG_HINT " + this + " setHint \"" + s + "\"", new Throwable());
		native_setHint(widget, s == null ? "" : s.toString());
	}

	@Override
	public CharSequence getHint() {
		return native_getHint(widget);
	}

	public void selectAll() {}

	public android.graphics.drawable.Drawable getTextCursorDrawable() { return ((Object)this) instanceof android.graphics.drawable.Drawable ? (android.graphics.drawable.Drawable)(Object)this : null; } // ATL-autostub
	public boolean isSingleLine() { return false; } // ATL-autostub
	public boolean onTextContextMenuItem(int arg0) { return false; } // ATL-autostub
	public int getAutofillType() { return 0; } // ATL-autostub
	public void setImeHintLocales(android.os.LocaleList arg0) {} // ATL-autostub
}

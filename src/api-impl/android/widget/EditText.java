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
	protected native long native_constructor(Context context, AttributeSet attrs);
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

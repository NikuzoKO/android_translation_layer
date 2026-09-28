package android.view.inputmethod;

import android.os.Bundle;
import android.os.LocaleList;

public class EditorInfo {
	public int actionId = 0;
	public CharSequence actionLabel = null;
	public Bundle extras = null;
	public int fieldId = 0;
	public String fieldName = null;
	public CharSequence hintText = null;
	public int imeOptions = 0x0;
	public int initialCapsMode = 0;
	public int initialSelStart = -1;
	public int initialSelEnd = -1;
	public int inputType = /*0x0*/ 0x00000001; /* TYPE_NULL */ /* TYPE_CLASS_TEXT */
	public CharSequence label = null;
	public String packageName = "com.example.FIXME";
	public String privateImeOptions = null;
	public LocaleList hintLocales = null;

	public java.lang.CharSequence getInitialSelectedText(int arg0) { return null; } // ATL-autostub
	public java.lang.CharSequence getInitialTextAfterCursor(int arg0, int arg1) { return null; } // ATL-autostub
	public java.lang.CharSequence getInitialTextBeforeCursor(int arg0, int arg1) { return null; } // ATL-autostub
	public java.lang.String[] contentMimeTypes; // ATL-autostub
	public void setInitialSurroundingSubText(java.lang.CharSequence arg0, int arg1) {} // ATL-autostub
	public void setSupportedHandwritingGesturePreviews(java.util.Set arg0) {} // ATL-autostub
	public void setSupportedHandwritingGestures(java.util.List arg0) {} // ATL-autostub
}

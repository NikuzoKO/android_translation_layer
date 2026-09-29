package android.text.method;

import android.graphics.Rect;
import android.view.View;

/**
 * Keeps newlines from breaking lines in a single-line TextView (they are shown as spaces).
 */
public class SingleLineTransformationMethod implements TransformationMethod {
	private static SingleLineTransformationMethod sInstance;

	public static SingleLineTransformationMethod getInstance() {
		if (sInstance == null)
			sInstance = new SingleLineTransformationMethod();
		return sInstance;
	}

	@Override
	public CharSequence getTransformation(CharSequence source, View view) {
		if (source == null)
			return null;
		String s = source.toString();
		if (s.indexOf('\n') < 0 && s.indexOf('\r') < 0)
			return source;
		return s.replace('\n', ' ').replace('\r', ' ');
	}

	@Override
	public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {}
}

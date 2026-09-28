package android.view.accessibility;

import java.util.Locale;

public class CaptioningManager {

	public boolean isEnabled() {
		return false;
	}

	public float getFontScale() {
		return 1.0f;
	}

	public static final class CaptionStyle {
	public android.graphics.Typeface getTypeface() { return ((Object)this) instanceof android.graphics.Typeface ? (android.graphics.Typeface)(Object)this : null; } // ATL-autostub
	public boolean hasBackgroundColor() { return false; } // ATL-autostub
	public boolean hasEdgeColor() { return false; } // ATL-autostub
	public boolean hasEdgeType() { return false; } // ATL-autostub
	public boolean hasForegroundColor() { return false; } // ATL-autostub
	public boolean hasWindowColor() { return false; } // ATL-autostub
	public int backgroundColor; // ATL-autostub
	public int edgeColor; // ATL-autostub
	public int edgeType; // ATL-autostub
	public int foregroundColor; // ATL-autostub
	public int windowColor; // ATL-autostub
}

	public static abstract class CaptioningChangeListener {
		public void onEnabledChanged(boolean enabled) {}
		public void onUserStyleChanged(CaptionStyle userStyle) {}
		public void onLocaleChanged(Locale locale) {}
		public void onFontScaleChanged(float fontScale) {}
		public void onSystemAudioCaptioningChanged(boolean enabled) {}
		public void onSystemAudioCaptioningUiChanged(boolean enabled) {}
	}

	public void addCaptioningChangeListener(CaptioningChangeListener listener) {}

	public android.view.accessibility.CaptioningManager.CaptionStyle getUserStyle() { return ((Object)this) instanceof android.view.accessibility.CaptioningManager.CaptionStyle ? (android.view.accessibility.CaptioningManager.CaptionStyle)(Object)this : null; } // ATL-autostub
}

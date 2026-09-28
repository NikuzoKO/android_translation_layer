package android.text;

public class BoringLayout extends Layout {

	public BoringLayout(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingMult, float spacingAdd, BoringLayout.Metrics metrics, boolean includePad) {
		super(source, paint, outerwidth, align, spacingMult, spacingAdd);
	}

	public static class Metrics {
	public int width; // ATL-autostub
};

	public static Metrics isBoring(CharSequence source, TextPaint paint, Metrics metrics) {
		return metrics;
	}

	public static Metrics isBoring(CharSequence source, TextPaint paint) {
		return null;
	}

	public boolean isFallbackLineSpacingEnabled() { return false; } // ATL-autostub
	public static android.text.BoringLayout make(java.lang.CharSequence arg0, android.text.TextPaint arg1, int arg2, android.text.Layout.Alignment arg3, float arg4, float arg5, android.text.BoringLayout.Metrics arg6, boolean arg7, android.text.TextUtils.TruncateAt arg8, int arg9) { return null; } // ATL-autostub
	public static android.text.BoringLayout.Metrics isBoring(java.lang.CharSequence arg0, android.text.TextPaint arg1, android.text.TextDirectionHeuristic arg2, boolean arg3, android.text.BoringLayout.Metrics arg4) { return null; } // ATL-autostub
}

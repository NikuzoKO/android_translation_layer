package android.atl;

import android.util.Slog;

/* ATL_NO_AI=1: app-side "AI" surfaces are rewritten or hidden at the text level.
 * ATL_NO_AI=log only logs every string that mentions Meta AI (to map the surfaces). */
public class ATLNoAi {
	private static final String MODE = System.getenv("ATL_NO_AI");
	public static final boolean ENABLED = MODE != null && !MODE.equals("log");
	private static final boolean LOG = MODE != null;

	public static boolean mentionsAi(CharSequence t) {
		if (MODE == null || t == null)
			return false;
		String s = t.toString();
		return s.contains("Meta AI") || s.contains("Meta AI");
	}

	/* the search pill's "Ask Meta AI or Search" becomes plain "Search" */
	public static String clean(String s) {
		if (!ENABLED || s == null)
			return s;
		return s.replace("Ask Meta AI or Search", "Search");
	}

	public static void log(String where, CharSequence t) {
		if (!LOG)
			return;
		try (java.io.FileWriter w = new java.io.FileWriter(System.getenv("XDG_RUNTIME_DIR") + "/atl-noai.log", true)) {
			w.write(where + ": " + t + "\n");
		} catch (java.io.IOException e) {
		}
	}
}

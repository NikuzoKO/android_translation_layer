package android.text;

public class TextDirectionHeuristics {

	public static final TextDirectionHeuristic LTR = new TextDirectionHeuristic() {
		@Override
		public boolean isRtl(CharSequence text, int start, int end) {
			return false;
		}
	};

	public static final TextDirectionHeuristic RTL = new TextDirectionHeuristic() {
		@Override
		public boolean isRtl(CharSequence text, int start, int end) {
			return true;
		}
	};

	public static final TextDirectionHeuristic FIRSTSTRONG_LTR = new TextDirectionHeuristic() {
		@Override
		public boolean isRtl(CharSequence text, int start, int end) {
			return false;
		}
	};

	public static android.text.TextDirectionHeuristic ANYRTL_LTR; // ATL-autostub
	public static android.text.TextDirectionHeuristic FIRSTSTRONG_RTL; // ATL-autostub
	public static android.text.TextDirectionHeuristic LOCALE; // ATL-autostub
}

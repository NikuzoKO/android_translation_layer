package android.graphics;

public class Shader {

	public enum TileMode {
		CLAMP,
		MIRROR,
		REPEAT
	}

	protected void init(long ni) {
	}

	/* ATL doesn't render shaders yet: gradients remember their colors and Paint draws with
	   their average (alpha-weighted) instead of the plain paint color */
	protected int[] atl_colors;

	int atlAverageColor() {
		if (atl_colors == null || atl_colors.length == 0)
			return 0;
		long a = 0, r = 0, g = 0, b = 0;
		for (int c : atl_colors) {
			int ca = Color.alpha(c);
			a += ca;
			r += Color.red(c) * ca;
			g += Color.green(c) * ca;
			b += Color.blue(c) * ca;
		}
		int n = atl_colors.length;
		if (a == 0)
			return 0;
		return Color.argb((int)(a / n), (int)(r / a), (int)(g / a), (int)(b / a));
	}

	public void setLocalMatrix(Matrix matrix) {}

	protected void copyLocalMatrix(Shader dest) {}

	public boolean getLocalMatrix(android.graphics.Matrix arg0) { return false; } // ATL-autostub
}

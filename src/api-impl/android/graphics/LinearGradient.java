package android.graphics;

public class LinearGradient extends Shader {

	public LinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, TileMode mode) {
		atl_colors = colors;
	}
	public LinearGradient(float x0, float y0, float x1, float y1, long[] colors, float[] positions, TileMode tile) {}
	public LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1, TileMode tile) {
		atl_colors = new int[] {color0, color1};
	}
}

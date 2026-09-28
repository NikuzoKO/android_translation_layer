package android.transition;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;

public abstract class PathMotion {
	static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
		@Override
		public Path getPath(float startX, float startY, float endX, float endY) {
			Path path = new Path();
			path.moveTo(startX, startY);
			path.lineTo(endX, endY);
			return path;
		}
	};

	public PathMotion() {}

	public PathMotion(Context context, AttributeSet attrs) {}

	public abstract Path getPath(float startX, float startY, float endX, float endY);
}

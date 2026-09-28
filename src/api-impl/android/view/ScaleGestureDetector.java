package android.view;

import android.content.Context;

public class ScaleGestureDetector {

	public interface OnScaleGestureListener {
		boolean onScale(ScaleGestureDetector detector);
		boolean onScaleBegin(ScaleGestureDetector detector);
		void onScaleEnd(ScaleGestureDetector detector);
	}

	public ScaleGestureDetector(Context context, OnScaleGestureListener listener) {}

	public void setQuickScaleEnabled(boolean enabled) {}
	public void setStylusScaleEnabled(boolean enabled) {}

	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	public boolean isInProgress() {
		return false;
	}

	public static class SimpleOnScaleGestureListener implements OnScaleGestureListener {
		public SimpleOnScaleGestureListener() {
		}

		public boolean onScale(ScaleGestureDetector detector) {
			return false;
		}
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}
		public void onScaleEnd(ScaleGestureDetector detector) {
		}
	}

	public ScaleGestureDetector(android.content.Context arg0, android.view.ScaleGestureDetector.OnScaleGestureListener arg1, android.os.Handler arg2) {} // ATL-autostub
	public float getCurrentSpan() { return 0f; } // ATL-autostub
	public float getFocusX() { return 0f; } // ATL-autostub
	public float getFocusY() { return 0f; } // ATL-autostub
	public float getScaleFactor() { return 0f; } // ATL-autostub
}

package android.view;

import android.content.Context;

/**
 * default values are mainly based on AOSPs defaults. Does not account for scaling yet.
 */
public class ViewConfiguration {

	public static ViewConfiguration get(Context context) {
		return new ViewConfiguration();
	}

	public int getScaledTouchSlop() {
		return 8;
	}

	public int getScaledMaximumFlingVelocity() {
		return 8000;
	}

	public int getScaledMinimumFlingVelocity() {
		return 50;
	}

	public static int getTapTimeout() {
		return 100;
	}

	public static int getLongPressTimeout() {
		return 400;
	}

	public int getScaledPagingTouchSlop() {
		return 16;
	}

	public boolean hasPermanentMenuKey() {
		return false;
	}

	public static int getDoubleTapTimeout() {
		return 300;
	}

	public int getScaledDoubleTapSlop() {
		return 100;
	}

	public static float getScrollFriction() {
		return 0.015f;
	}

	public int getScaledWindowTouchSlop() {
		return 8;
	}

	public float getScaledHorizontalScrollFactor() {
		return 1;
	}

	public float getScaledVerticalScrollFactor() {
		return 1;
	}

	public static int getScrollBarFadeDuration() {
		return 100;
	}

	public static int getScrollDefaultDelay() {
		return 16;
	}

	public boolean shouldShowMenuShortcutsWhenKeyboardPresent() {
		return true;
	}

	public static int getKeyRepeatTimeout() {
		return 1000;
	}

	public static int getKeyRepeatDelay() {
		return 100;
	}

	public int getScaledHandwritingGestureLineMargin() { return 0; } // ATL-autostub
	public int getScaledHandwritingSlop() { return 0; } // ATL-autostub
	public int getScaledHoverSlop() { return 0; } // ATL-autostub
	public int getScaledMaximumFlingVelocity(int arg0, int arg1, int arg2) { return 0; } // ATL-autostub
	public int getScaledMinimumFlingVelocity(int arg0, int arg1, int arg2) { return 0; } // ATL-autostub
}

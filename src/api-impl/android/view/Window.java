package android.view;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.transition.Transition;
import android.widget.FrameLayout;
import android.widget.Toolbar;

public class Window {
	public static final int FEATURE_OPTIONS_PANEL = 0;
	public static final int FEATURE_NO_TITLE = 1;

	public ViewTreeObserver view_tree_observer = null;
	private WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, 0);

	public static interface Callback {
		public void onContentChanged();

		public abstract boolean onCreatePanelMenu(int featureId, Menu menu);

		public View onCreatePanelView(int featureId);

		public boolean onPreparePanel(int featureId, View view, Menu menu);

		public boolean onMenuItemSelected(int featureId, MenuItem item);

		public void onPanelClosed(int featureId, Menu menu);

		public boolean onMenuOpened(int featureId, Menu menu);
	
	public default android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback arg0) { return ((Object)this) instanceof android.view.ActionMode ? (android.view.ActionMode)(Object)this : null; } // ATL-autostub
	public default android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback arg0, int arg1) { return ((Object)this) instanceof android.view.ActionMode ? (android.view.ActionMode)(Object)this : null; } // ATL-autostub
	public default boolean dispatchGenericMotionEvent(android.view.MotionEvent arg0) { return false; } // ATL-autostub
	public default boolean dispatchKeyEvent(android.view.KeyEvent arg0) { return false; } // ATL-autostub
	public default boolean dispatchKeyShortcutEvent(android.view.KeyEvent arg0) { return false; } // ATL-autostub
	public default boolean dispatchTouchEvent(android.view.MotionEvent arg0) { return false; } // ATL-autostub
	public default boolean dispatchTrackballEvent(android.view.MotionEvent arg0) { return false; } // ATL-autostub
	public default boolean onSearchRequested() { return false; } // ATL-autostub
	public default void onActionModeFinished(android.view.ActionMode arg0) {} // ATL-autostub
	public default void onActionModeStarted(android.view.ActionMode arg0) {} // ATL-autostub
	public default void onAttachedToWindow() {} // ATL-autostub
	public default void onDetachedFromWindow() {} // ATL-autostub
	public default void onPointerCaptureChanged(boolean arg0) {} // ATL-autostub
	public default void onProvideKeyboardShortcuts(java.util.List arg0, android.view.Menu arg1, int arg2) {} // ATL-autostub
	public default void onWindowAttributesChanged(android.view.WindowManager.LayoutParams arg0) {} // ATL-autostub
	public default void onWindowFocusChanged(boolean arg0) {} // ATL-autostub
}

	public static interface OnFrameMetricsAvailableListener {}

	public long native_window;
	private ViewGroup decorView;

	private Window.Callback callback;
	private Context context;

	public Window(Context context, Window.Callback callback) {
		this.callback = callback;
		this.context = context;
		decorView = new FrameLayout(context);
		decorView.setId(android.R.id.content);
	}

	public void set_native_window(long native_window) {
		this.native_window = native_window;
		set_jobject(native_window, this);
	}

	public void addFlags(int flags) {}
	public void setFlags(int flags, int mask) {}
	public void clearFlags(int flags) {}

	public final Callback getCallback() {
		return this.callback;
	}
	public void setCallback(Window.Callback callback) {
		this.callback = callback;
	}

	public void setContentView(View view) {
		if (decorView.getBackground() == null) {
			TypedArray ta = context.obtainStyledAttributes(new int[] {R.attr.windowBackground});
			if (ta.hasValue(0))
				setBackgroundDrawable(ta.getDrawable(0));
			ta.recycle();
		}
		decorView.removeAllViews();
		decorView.addView(view);
		if (view != null) {
			set_widget_as_root(native_window, decorView.widget);
		}
	}

	public View getDecorView() {
		return decorView;
	}

	public void takeInputQueue(InputQueue.Callback callback) {
		take_input_queue(native_window, callback, new InputQueue());
	}

	public boolean requestFeature(int featureId) {
		return false;
	}

	public View findViewById(int id) {
		if (id == com.android.internal.R.id.action_bar)
			return new Toolbar(context);
		return decorView.findViewById(id);
	}

	public View peekDecorView() {
		return null;
	}

	public WindowManager.LayoutParams getAttributes() {
		return params;
	}

	public void setBackgroundDrawable(Drawable drawable) {
		// HACK: disable transparent background for WhatsApp dialogs
		// For some unknown reason, the language picker in WhatsApp doesn't render the BottomSheet background currently.
		if (!"com.whatsapp".equals(context.getPackageName()))
			remove_gtk_background(native_window);
		decorView.setBackgroundDrawable(drawable);
	}

	public void setAttributes(WindowManager.LayoutParams params) {
		if (params.screenBrightness != -1)
			set_screen_brightness(params.screenBrightness);
		this.params = params;
		setLayout(params.width, params.height);
	}

	public void takeSurface(SurfaceHolder.Callback2 callback) {}

	public void setStatusBarColor(int color) {}

	public void setNavigationBarColor(int color) {}

	public void setFormat(int format) {}

	public void setLayout(int width, int height) {
		params.width = width;
		params.height = height;
		set_layout(native_window, width, height);
	}

	public WindowManager getWindowManager() {
		return new WindowManagerImpl();
	}

	public void setSoftInputMode(int dummy) {}

	public int getNavigationBarColor() {
		return 0xFF888888; // gray
	}

	public void setBackgroundDrawableResource(int resId) {
		setBackgroundDrawable(context.getDrawable(resId));
	}

	public int getStatusBarColor() { return 0xFFFF0000; }

	public Context getContext() {
		return context;
	}

	public boolean hasFeature(int featureId) {
		return false;
	}

	public void setTitle(CharSequence title) {
		set_title(native_window, title != null ? title.toString() : context.getPackageName());
	}

	public Transition getSharedElementEnterTransition() {
		return new Transition();
	}

	public void setSharedElementExitTransition(Transition transition) {}

	public void setSharedElementReenterTransition(Transition transition) {}

	public void setSharedElementReturnTransition(Transition transition) {}

	public Transition getSharedElementExitTransition() {
		return new Transition();
	}

	public Transition getSharedElementReenterTransition() {
		return new Transition();
	}

	public void setReturnTransition(Transition transition) {}

	public void setEnterTransition(Transition transition) {}

	public void setGravity(int gravity) {}

	public void setDecorFitsSystemWindows(boolean fits) {}

	public WindowInsetsController getInsetsController() {
		return new InsetsController();
	}

	public void setStatusBarContrastEnforced(boolean enforced) {}

	public void setNavigationBarContrastEnforced(boolean enforced) {}

	public native void set_widget_as_root(long native_window, long widget);
	private native void set_title(long native_window, String title);
	public native void take_input_queue(long native_window, InputQueue.Callback callback, InputQueue queue);
	public native void set_layout(long native_window, int width, int height);
	private static native void set_jobject(long ptr, Window obj);
	private native void remove_gtk_background(long native_window);
	private native void set_screen_brightness(float brightness);

	public void setExitTransition(Transition transition) {}
	public void setReenterTransition(Transition transition) {}
	public void setSharedElementEnterTransition(Transition transition) {}
	public void setAllowEnterTransitionOverlap(boolean allow) {}
	public void setAllowReturnTransitionOverlap(boolean allow) {}
	public boolean getAllowEnterTransitionOverlap() { return true; }
	public boolean getAllowReturnTransitionOverlap() { return true; }
	public void setTransitionBackgroundFadeDuration(long fadeDurationMillis) {}
	public long getTransitionBackgroundFadeDuration() { return 0; }
	public void setSharedElementsUseOverlay(boolean sharedElementsUseOverlay) {}
	public boolean getSharedElementsUseOverlay() { return true; }
	public Transition getEnterTransition() { return null; }
	public Transition getExitTransition() { return null; }
	public Transition getReturnTransition() { return null; }
	public Transition getReenterTransition() { return null; }
	public Transition getSharedElementReturnTransition() { return null; }

	public boolean isFloating() { return false; } // ATL-autostub
	public boolean superDispatchKeyEvent(android.view.KeyEvent arg0) { return false; } // ATL-autostub
	public void setDimAmount(float arg0) {} // ATL-autostub
	public void setWindowAnimations(int arg0) {} // ATL-autostub
}

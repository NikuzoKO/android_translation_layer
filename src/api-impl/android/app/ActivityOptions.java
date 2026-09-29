package android.app;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

public class ActivityOptions {

	public static ActivityOptions makeSceneTransitionAnimation(Activity activity, Pair<View, String>... pairs) {
		return new ActivityOptions();
	}

	public static ActivityOptions makeSceneTransitionAnimation(Activity activity, View view, String name) {
		return new ActivityOptions();
	}

	public Bundle toBundle() {
		return new Bundle();
	}

	public android.app.ActivityOptions setShareIdentityEnabled(boolean arg0) { return this; }
	public static android.app.ActivityOptions makeBasic() { return new ActivityOptions(); }
	public static android.app.ActivityOptions makeCustomAnimation(android.content.Context arg0, int arg1, int arg2) { return new ActivityOptions(); }
	public void setAllowPassThroughOnTouchOutside(boolean arg0) {}

	/* other factories apps use for transitions; ATL doesn't animate, so they are all "basic" */
	public static ActivityOptions makeScaleUpAnimation(View source, int startX, int startY, int width, int height) { return new ActivityOptions(); }
	public static ActivityOptions makeClipRevealAnimation(View source, int startX, int startY, int width, int height) { return new ActivityOptions(); }
	public static ActivityOptions makeThumbnailScaleUpAnimation(View source, android.graphics.Bitmap thumbnail, int startX, int startY) { return new ActivityOptions(); }
	public static ActivityOptions makeTaskLaunchBehind() { return new ActivityOptions(); }
}

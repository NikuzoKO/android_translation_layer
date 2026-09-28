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
		return null;
	}

	public android.app.ActivityOptions setShareIdentityEnabled(boolean arg0) { return ((Object)this) instanceof android.app.ActivityOptions ? (android.app.ActivityOptions)(Object)this : null; } // ATL-autostub
	public static android.app.ActivityOptions makeBasic() { return null; } // ATL-autostub
	public static android.app.ActivityOptions makeCustomAnimation(android.content.Context arg0, int arg1, int arg2) { return null; } // ATL-autostub
	public void setAllowPassThroughOnTouchOutside(boolean arg0) {} // ATL-autostub
}

package android.app;

import android.content.Context;
import android.content.res.Configuration;

public class UiModeManager {

	public int getCurrentModeType() {
		return Context.sys_config.uiMode & Configuration.UI_MODE_TYPE_MASK;
	}

	public static final int MODE_NIGHT_AUTO = 0;
	public static final int MODE_NIGHT_NO = 1;
	public static final int MODE_NIGHT_YES = 2;

	public int getNightMode() {
		switch (Context.sys_config.uiMode & Configuration.UI_MODE_NIGHT_MASK) {
			case Configuration.UI_MODE_NIGHT_YES:
				return MODE_NIGHT_YES;
			case Configuration.UI_MODE_NIGHT_NO:
				return MODE_NIGHT_NO;
			default:
				return MODE_NIGHT_AUTO;
		}
	}
}

package android.content.pm;

import java.util.Collections;
import java.util.List;

public class ShortcutManager {
	public void removeAllDynamicShortcuts() {
	}

	public List getShortcuts(int matchFlags) {
		return Collections.emptyList();
	}
	public void removeLongLivedShortcuts(List<String> shortcutIds) {
	}

	public boolean setDynamicShortcuts(List<ShortcutInfo> shortcutInfoList) {
		return true;
	}

	public android.content.Intent createShortcutResultIntent(android.content.pm.ShortcutInfo arg0) { return ((Object)this) instanceof android.content.Intent ? (android.content.Intent)(Object)this : null; } // ATL-autostub
	public boolean addDynamicShortcuts(java.util.List arg0) { return false; } // ATL-autostub
	public boolean isRateLimitingActive() { return false; } // ATL-autostub
	public boolean isRequestPinShortcutSupported() { return false; } // ATL-autostub
	public boolean requestPinShortcut(android.content.pm.ShortcutInfo arg0, android.content.IntentSender arg1) { return false; } // ATL-autostub
	public int getMaxShortcutCountPerActivity() { return 0; } // ATL-autostub
	public java.util.List getDynamicShortcuts() { return null; } // ATL-autostub
	public java.util.List getPinnedShortcuts() { return null; } // ATL-autostub
	public void disableShortcuts(java.util.List arg0) {} // ATL-autostub
	public void disableShortcuts(java.util.List arg0, java.lang.CharSequence arg1) {} // ATL-autostub
	public void pushDynamicShortcut(android.content.pm.ShortcutInfo arg0) {} // ATL-autostub
	public void removeDynamicShortcuts(java.util.List arg0) {} // ATL-autostub
	public void reportShortcutUsed(java.lang.String arg0) {} // ATL-autostub
}

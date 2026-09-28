package android.content.pm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Parcelable;

public class ShortcutInfo implements Parcelable {
	public static final Creator<ShortcutInfo> CREATOR = null;

	public static class Builder {
		public Builder(Context context, String id) {}

		public ShortcutInfo build() {
			return new ShortcutInfo();
		}

		public Builder setIcon(Icon icon) {
			return this;
		}

		public Builder setIntent(Intent intent) {
			return this;
		}

		public Builder setLongLabel(CharSequence longLabel) {
			return this;
		}

		public Builder setShortLabel(CharSequence shortLabel) {
			return this;
		}
	
	public android.content.pm.ShortcutInfo.Builder setActivity(android.content.ComponentName arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
	public android.content.pm.ShortcutInfo.Builder setCategories(java.util.Set arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
	public android.content.pm.ShortcutInfo.Builder setDisabledMessage(java.lang.CharSequence arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
	public android.content.pm.ShortcutInfo.Builder setExcludedFromSurfaces(int arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
	public android.content.pm.ShortcutInfo.Builder setExtras(android.os.PersistableBundle arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
	public android.content.pm.ShortcutInfo.Builder setIntents(android.content.Intent[] arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
	public android.content.pm.ShortcutInfo.Builder setLongLived(boolean arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
	public android.content.pm.ShortcutInfo.Builder setRank(int arg0) { return ((Object)this) instanceof android.content.pm.ShortcutInfo.Builder ? (android.content.pm.ShortcutInfo.Builder)(Object)this : null; } // ATL-autostub
}

	public android.content.ComponentName getActivity() { return ((Object)this) instanceof android.content.ComponentName ? (android.content.ComponentName)(Object)this : null; } // ATL-autostub
	public android.content.Intent[] getIntents() { return null; } // ATL-autostub
	public android.os.PersistableBundle getExtras() { return ((Object)this) instanceof android.os.PersistableBundle ? (android.os.PersistableBundle)(Object)this : null; } // ATL-autostub
	public android.os.UserHandle getUserHandle() { return ((Object)this) instanceof android.os.UserHandle ? (android.os.UserHandle)(Object)this : null; } // ATL-autostub
	public boolean hasKeyFieldsOnly() { return false; } // ATL-autostub
	public boolean isCached() { return false; } // ATL-autostub
	public boolean isDeclaredInManifest() { return false; } // ATL-autostub
	public boolean isDynamic() { return false; } // ATL-autostub
	public boolean isEnabled() { return false; } // ATL-autostub
	public boolean isImmutable() { return false; } // ATL-autostub
	public boolean isPinned() { return false; } // ATL-autostub
	public int getDisabledReason() { return 0; } // ATL-autostub
	public int getRank() { return 0; } // ATL-autostub
	public java.lang.CharSequence getDisabledMessage() { return null; } // ATL-autostub
	public java.lang.CharSequence getLongLabel() { return null; } // ATL-autostub
	public java.lang.CharSequence getShortLabel() { return null; } // ATL-autostub
	public java.lang.String getId() { return null; } // ATL-autostub
	public java.lang.String getPackage() { return null; } // ATL-autostub
	public java.util.Set getCategories() { return null; } // ATL-autostub
	public long getLastChangedTimestamp() { return 0L; } // ATL-autostub
}

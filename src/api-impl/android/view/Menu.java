package android.view;

import android.content.ComponentName;
import android.content.Intent;
import android.view.MenuItem;

public interface Menu {

	public static final int NONE = 0;

	public MenuItem add(int groupId, int itemId, int order, CharSequence title);

	public MenuItem add(int groupId, int itemId, int order, int titleRes);

	public MenuItem findItem(int id);

	public MenuItem getItem(int id);

	public void clear();

	public void removeGroup(int groupId);

	public SubMenu addSubMenu(int id);

	public MenuItem add(int id);

	public MenuItem add(CharSequence text);

	public void setGroupCheckable(int group, boolean checkable, boolean exclusive);

	public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title);

	public void setGroupVisible(int group, boolean visible);

	public void removeItem(int id);

	public int size();

	public boolean hasVisibleItems();

	public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes);

	public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems);

	public default android.view.SubMenu addSubMenu(java.lang.CharSequence arg0) { return ((Object)this) instanceof android.view.SubMenu ? (android.view.SubMenu)(Object)this : null; } // ATL-autostub
	public default boolean isShortcutKey(int arg0, android.view.KeyEvent arg1) { return false; } // ATL-autostub
	public default boolean performIdentifierAction(int arg0, int arg1) { return false; } // ATL-autostub
	public default boolean performShortcut(int arg0, android.view.KeyEvent arg1, int arg2) { return false; } // ATL-autostub
	public default void close() {} // ATL-autostub
	public default void setGroupDividerEnabled(boolean arg0) {} // ATL-autostub
	public default void setGroupEnabled(int arg0, boolean arg1) {} // ATL-autostub
	public default void setQwertyMode(boolean arg0) {} // ATL-autostub
}

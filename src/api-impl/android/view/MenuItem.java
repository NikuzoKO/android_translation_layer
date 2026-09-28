package android.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public interface MenuItem {

	public interface OnMenuItemClickListener {
		public boolean onMenuItemClick(MenuItem item);
	}

	public interface OnActionExpandListener {
		public boolean onMenuItemActionExpand(MenuItem item);
		public boolean onMenuItemActionCollapse(MenuItem item);
	}

	public MenuItem setIcon(int iconRes);

	public MenuItem setVisible(boolean visible);

	public MenuItem setChecked(boolean checked);

	public MenuItem setEnabled(boolean enabled);

	public MenuItem setCheckable(boolean checkable);

	public boolean isCheckable();

	public MenuItem setTitleCondensed(CharSequence titleCondensed);

	public MenuItem setTitle(CharSequence title);

	public MenuItem setActionView(View actionView);

	public void setShowAsAction(int action);

	public int getItemId();

	public int getGroupId();

	public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener listener);

	public MenuItem setTitle(int resId);

	public boolean isVisible();

	public Drawable getIcon();

	public SubMenu getSubMenu();

	public MenuItem setActionView(int resId);

	public View getActionView();

	public boolean hasSubMenu();

	public MenuItem setOnActionExpandListener(OnActionExpandListener listener);

	public MenuItem setIcon(Drawable icon);

	public boolean isChecked();

	public MenuItem setShowAsActionFlags(int action);

	public MenuItem setAlphabeticShortcut(char alphaChar);

	public MenuItem setShortcut(char numeric, char alpha);

	public int getOrder();

	public boolean isEnabled();

	public CharSequence getTitleCondensed();

	public CharSequence getTitle();

	public MenuItem setNumericShortcut(char numericChar);

	public boolean expandActionView();

	public boolean isActionViewExpanded();

	public MenuItem setIntent(Intent intent);

	public default android.content.Intent getIntent() { return ((Object)this) instanceof android.content.Intent ? (android.content.Intent)(Object)this : null; } // ATL-autostub
	public default android.content.res.ColorStateList getIconTintList() { return ((Object)this) instanceof android.content.res.ColorStateList ? (android.content.res.ColorStateList)(Object)this : null; } // ATL-autostub
	public default android.view.ContextMenu.ContextMenuInfo getMenuInfo() { return ((Object)this) instanceof android.view.ContextMenu.ContextMenuInfo ? (android.view.ContextMenu.ContextMenuInfo)(Object)this : null; } // ATL-autostub
	public default android.view.MenuItem setAlphabeticShortcut(char arg0, int arg1) { return ((Object)this) instanceof android.view.MenuItem ? (android.view.MenuItem)(Object)this : null; } // ATL-autostub
	public default android.view.MenuItem setContentDescription(java.lang.CharSequence arg0) { return ((Object)this) instanceof android.view.MenuItem ? (android.view.MenuItem)(Object)this : null; } // ATL-autostub
	public default android.view.MenuItem setIconTintList(android.content.res.ColorStateList arg0) { return ((Object)this) instanceof android.view.MenuItem ? (android.view.MenuItem)(Object)this : null; } // ATL-autostub
	public default android.view.MenuItem setIconTintMode(android.graphics.PorterDuff.Mode arg0) { return ((Object)this) instanceof android.view.MenuItem ? (android.view.MenuItem)(Object)this : null; } // ATL-autostub
	public default android.view.MenuItem setNumericShortcut(char arg0, int arg1) { return ((Object)this) instanceof android.view.MenuItem ? (android.view.MenuItem)(Object)this : null; } // ATL-autostub
	public default android.view.MenuItem setTooltipText(java.lang.CharSequence arg0) { return ((Object)this) instanceof android.view.MenuItem ? (android.view.MenuItem)(Object)this : null; } // ATL-autostub
	public default boolean collapseActionView() { return false; } // ATL-autostub
	public default char getAlphabeticShortcut() { return '\0'; } // ATL-autostub
	public default char getNumericShortcut() { return '\0'; } // ATL-autostub
}

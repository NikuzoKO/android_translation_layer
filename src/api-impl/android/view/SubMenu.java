package android.view;

import android.graphics.drawable.Drawable;

public interface SubMenu extends Menu {

	public MenuItem getItem();

	public void clearHeader();

	public SubMenu setIcon(Drawable icon);

	public default void setGroupDividerEnabled(boolean arg0) {} // ATL-autostub
}

package android.view.accessibility;

import java.lang.CharSequence;

public class AccessibilityNodeInfo {
	public static final class AccessibilityAction {
		public static final AccessibilityAction ACTION_CONTEXT_CLICK = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SET_PROGRESS = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SHOW_ON_SCREEN = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SCROLL_TO_POSITION = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SCROLL_UP = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SCROLL_LEFT = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SCROLL_RIGHT = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SCROLL_DOWN = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_PAGE_UP = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_PAGE_DOWN = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_PAGE_LEFT = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_PAGE_RIGHT = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_MOVE_WINDOW = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_SHOW_TOOLTIP = new AccessibilityAction(0, null);
		public static final AccessibilityAction ACTION_HIDE_TOOLTIP = new AccessibilityAction(0, null);

		public AccessibilityAction(int actionId, CharSequence label) {}

		public int getId() { return 0; }
	
	public java.lang.CharSequence getLabel() { return null; } // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_CLICK; // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_DRAG_CANCEL; // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_DRAG_DROP; // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_DRAG_START; // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_IME_ENTER; // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_PRESS_AND_HOLD; // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_IN_DIRECTION; // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction ACTION_SHOW_TEXT_SUGGESTIONS; // ATL-autostub
}

	public class RangeInfo {
		public static final int RANGE_TYPE_INT = 0;
		public static final int RANGE_TYPE_FLOAT = 1;
		public static final int RANGE_TYPE_PERCENT = 2;
	
}

	public static AccessibilityNodeInfo obtain() { return null; }

	public void setClassName(CharSequence className) {}

	public void addAction(int action) {}

	public void setCheckable(boolean checkable) {}

	public void setChecked(boolean checked) {}

	public void setClickable(boolean clickable) {}

	public void setContentDescription(CharSequence description) {}

	public void setEditable(boolean editable) {}

	public void setEnabled(boolean enabled) {}

	public void setFocusable(boolean focusable) {}

	public void setFocused(boolean focused) {}

	public void setScrollable(boolean scrollable) {}

	public void setVisibleToUser(boolean visible) {}

	public void setTextSelection(int start, int end) {}

	public void setRangeInfo(RangeInfo info) {}

	public android.os.Bundle getExtras() { return ((Object)this) instanceof android.os.Bundle ? (android.os.Bundle)(Object)this : null; } // ATL-autostub
	public boolean isAccessibilityDataSensitive() { return false; } // ATL-autostub
	public boolean isCheckable() { return false; } // ATL-autostub
	public boolean isChecked() { return false; } // ATL-autostub
	public boolean isClickable() { return false; } // ATL-autostub
	public boolean isContextClickable() { return false; } // ATL-autostub
	public boolean isEnabled() { return false; } // ATL-autostub
	public boolean isFocusable() { return false; } // ATL-autostub
	public boolean isFocused() { return false; } // ATL-autostub
	public boolean isImportantForAccessibility() { return false; } // ATL-autostub
	public boolean isLongClickable() { return false; } // ATL-autostub
	public boolean isPassword() { return false; } // ATL-autostub
	public boolean isScrollable() { return false; } // ATL-autostub
	public boolean isSelected() { return false; } // ATL-autostub
	public boolean isShowingHintText() { return false; } // ATL-autostub
	public boolean isTextSelectable() { return false; } // ATL-autostub
	public boolean isVisibleToUser() { return false; } // ATL-autostub
	public boolean performAction(int arg0, android.os.Bundle arg1) { return false; } // ATL-autostub
	public boolean removeAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction arg0) { return false; } // ATL-autostub
	public int getActions() { return 0; } // ATL-autostub
	public int getChildCount() { return 0; } // ATL-autostub
	public int getMaxTextLength() { return 0; } // ATL-autostub
	public int getMovementGranularities() { return 0; } // ATL-autostub
	public java.lang.CharSequence getClassName() { return null; } // ATL-autostub
	public java.lang.CharSequence getContainerTitle() { return null; } // ATL-autostub
	public java.lang.CharSequence getContentDescription() { return null; } // ATL-autostub
	public java.lang.CharSequence getError() { return null; } // ATL-autostub
	public java.lang.CharSequence getPackageName() { return null; } // ATL-autostub
	public java.lang.CharSequence getStateDescription() { return null; } // ATL-autostub
	public java.lang.CharSequence getText() { return null; } // ATL-autostub
	public java.lang.CharSequence getTooltipText() { return null; } // ATL-autostub
	public java.lang.String getUniqueId() { return null; } // ATL-autostub
	public java.lang.String getViewIdResourceName() { return null; } // ATL-autostub
	public java.util.List getActionList() { return null; } // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo obtain(android.view.View arg0) { return null; } // ATL-autostub
	public static android.view.accessibility.AccessibilityNodeInfo obtain(android.view.accessibility.AccessibilityNodeInfo arg0) { return null; } // ATL-autostub
	public void addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction arg0) {} // ATL-autostub
	public void addChild(android.view.View arg0) {} // ATL-autostub
	public void addChild(android.view.View arg0, int arg1) {} // ATL-autostub
	public void getBoundsInParent(android.graphics.Rect arg0) {} // ATL-autostub
	public void getBoundsInScreen(android.graphics.Rect arg0) {} // ATL-autostub
	public void getBoundsInWindow(android.graphics.Rect arg0) {} // ATL-autostub
	public void setAccessibilityFocused(boolean arg0) {} // ATL-autostub
	public void setAvailableExtraData(java.util.List arg0) {} // ATL-autostub
	public void setBoundsInParent(android.graphics.Rect arg0) {} // ATL-autostub
	public void setBoundsInScreen(android.graphics.Rect arg0) {} // ATL-autostub
	public void setContainerTitle(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setContentInvalid(boolean arg0) {} // ATL-autostub
	public void setDismissable(boolean arg0) {} // ATL-autostub
	public void setError(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setHeading(boolean arg0) {} // ATL-autostub
	public void setHintText(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setImportantForAccessibility(boolean arg0) {} // ATL-autostub
	public void setLabelFor(android.view.View arg0) {} // ATL-autostub
	public void setLabeledBy(android.view.View arg0) {} // ATL-autostub
	public void setLiveRegion(int arg0) {} // ATL-autostub
	public void setLongClickable(boolean arg0) {} // ATL-autostub
	public void setMaxTextLength(int arg0) {} // ATL-autostub
	public void setMinDurationBetweenContentChanges(java.time.Duration arg0) {} // ATL-autostub
	public void setMovementGranularities(int arg0) {} // ATL-autostub
	public void setMultiLine(boolean arg0) {} // ATL-autostub
	public void setPackageName(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setPaneTitle(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setParent(android.view.View arg0) {} // ATL-autostub
	public void setParent(android.view.View arg0, int arg1) {} // ATL-autostub
	public void setPassword(boolean arg0) {} // ATL-autostub
	public void setRequestInitialAccessibilityFocus(boolean arg0) {} // ATL-autostub
	public void setScreenReaderFocusable(boolean arg0) {} // ATL-autostub
	public void setSelected(boolean arg0) {} // ATL-autostub
	public void setShowingHintText(boolean arg0) {} // ATL-autostub
	public void setSource(android.view.View arg0, int arg1) {} // ATL-autostub
	public void setStateDescription(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setText(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setTextSelectable(boolean arg0) {} // ATL-autostub
	public void setTooltipText(java.lang.CharSequence arg0) {} // ATL-autostub
	public void setTraversalAfter(android.view.View arg0) {} // ATL-autostub
	public void setTraversalAfter(android.view.View arg0, int arg1) {} // ATL-autostub
	public void setTraversalBefore(android.view.View arg0) {} // ATL-autostub
	public void setTraversalBefore(android.view.View arg0, int arg1) {} // ATL-autostub
	public void setViewIdResourceName(java.lang.String arg0) {} // ATL-autostub
}

package android.text.method;

public interface KeyListener {

	public default boolean onKeyDown(android.view.View arg0, android.text.Editable arg1, int arg2, android.view.KeyEvent arg3) { return false; } // ATL-autostub
	public default boolean onKeyOther(android.view.View arg0, android.text.Editable arg1, android.view.KeyEvent arg2) { return false; } // ATL-autostub
	public default boolean onKeyUp(android.view.View arg0, android.text.Editable arg1, int arg2, android.view.KeyEvent arg3) { return false; } // ATL-autostub
	public default int getInputType() { return 0; } // ATL-autostub
	public default void clearMetaKeyState(android.view.View arg0, android.text.Editable arg1, int arg2) {} // ATL-autostub
}

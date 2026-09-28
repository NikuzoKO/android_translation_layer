package android.view.autofill;

public class AutofillManager {

	public static abstract class AutofillCallback {
	public void onAutofillEvent(android.view.View arg0, int arg1, int arg2) {} // ATL-autostub
}

	public interface AutofillClient {}

	public void registerCallback(AutofillCallback callback) {}

	public void unregisterCallback(AutofillCallback callback) {}

	public android.view.autofill.AutofillId getNextAutofillId() { return ((Object)this) instanceof android.view.autofill.AutofillId ? (android.view.autofill.AutofillId)(Object)this : null; } // ATL-autostub
	public void cancel() {} // ATL-autostub
	public void commit() {} // ATL-autostub
	public void notifyValueChanged(android.view.View arg0) {} // ATL-autostub
	public void notifyViewEntered(android.view.View arg0, int arg1, android.graphics.Rect arg2) {} // ATL-autostub
	public void notifyViewExited(android.view.View arg0, int arg1) {} // ATL-autostub
	public void notifyViewVisibilityChanged(android.view.View arg0, int arg1, boolean arg2) {} // ATL-autostub
	public void requestAutofill(android.view.View arg0, int arg1, android.graphics.Rect arg2) {} // ATL-autostub
}

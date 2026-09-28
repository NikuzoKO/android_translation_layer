package android.app;

import android.os.Bundle;

public class Fragment {

	Activity activity;

	public void onCreate(Bundle savedInstanceState) {
	}

	public void onStart() {
	}

	public void onResume() {
	}

	public void onPause() {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public Activity getActivity() {
		return activity;
	}

	public boolean isRemoving() { return false; } // ATL-autostub
	public void dump(java.lang.String arg0, java.io.FileDescriptor arg1, java.io.PrintWriter arg2, java.lang.String[] arg3) {} // ATL-autostub
	public void onActivityCreated(android.os.Bundle arg0) {} // ATL-autostub
	public void onActivityResult(int arg0, int arg1, android.content.Intent arg2) {} // ATL-autostub
	public void onSaveInstanceState(android.os.Bundle arg0) {} // ATL-autostub
}

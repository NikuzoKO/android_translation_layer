package android.app;

import android.os.Bundle;

public class RemoteInput {

	public static class Builder {

		public Builder(String resultKey) {}

		public Builder setLabel(CharSequence label) { return this; }

		public Builder setChoices(CharSequence[] choices) { return this; }

		public Builder setAllowFreeFormInput(boolean allowFreeFormInput) { return this; }

		public Builder addExtras(Bundle extras) { return this; }

		public RemoteInput build() {
			return new RemoteInput();
		}
	
	public android.app.RemoteInput.Builder setAllowDataType(java.lang.String arg0, boolean arg1) { return ((Object)this) instanceof android.app.RemoteInput.Builder ? (android.app.RemoteInput.Builder)(Object)this : null; } // ATL-autostub
	public android.app.RemoteInput.Builder setEditChoicesBeforeSending(int arg0) { return ((Object)this) instanceof android.app.RemoteInput.Builder ? (android.app.RemoteInput.Builder)(Object)this : null; } // ATL-autostub
}

	public static android.os.Bundle getResultsFromIntent(android.content.Intent arg0) { return null; } // ATL-autostub
}

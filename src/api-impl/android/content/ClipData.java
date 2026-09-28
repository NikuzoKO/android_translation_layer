package android.content;

import android.net.Uri;

public class ClipData {

	public static class Item {

		public Item(Uri uri) {}
	
	public Item(java.lang.CharSequence arg0, java.lang.String arg1, android.content.Intent arg2, android.net.Uri arg3) {} // ATL-autostub
	public android.net.Uri getUri() { return ((Object)this) instanceof android.net.Uri ? (android.net.Uri)(Object)this : null; } // ATL-autostub
	public java.lang.CharSequence coerceToStyledText(android.content.Context arg0) { return null; } // ATL-autostub
	public java.lang.CharSequence coerceToText(android.content.Context arg0) { return null; } // ATL-autostub
	public java.lang.CharSequence getText() { return null; } // ATL-autostub
	public java.lang.String getHtmlText() { return null; } // ATL-autostub
}

	String text;

	public ClipData(ClipDescription description, Item item) {}

	public static ClipData newPlainText(CharSequence label, CharSequence text) {
		ClipData clip = new ClipData(new ClipDescription(label, null), null);
		clip.text = text.toString();
		return clip;
	}

	public static ClipData newRawUri(CharSequence label, Uri uri) {
		ClipData clip = new ClipData(new ClipDescription(label, null), new Item(uri));
		clip.text = uri.toString();
		return clip;
	}

	public void addItem(ContentResolver resolver, Item item) {
	}

	public Item getItemAt(int index) {
		return null;
	}

	public int getItemCount() {
		return 0;
	}

	public ClipData(java.lang.CharSequence arg0, java.lang.String[] arg1, android.content.ClipData.Item arg2) {} // ATL-autostub
	public android.content.ClipDescription getDescription() { return ((Object)this) instanceof android.content.ClipDescription ? (android.content.ClipDescription)(Object)this : null; } // ATL-autostub
	public static android.content.ClipData newIntent(java.lang.CharSequence arg0, android.content.Intent arg1) { return null; } // ATL-autostub
	public static android.content.ClipData newUri(android.content.ContentResolver arg0, java.lang.CharSequence arg1, android.net.Uri arg2) { return null; } // ATL-autostub
	public void addItem(android.content.ClipData.Item arg0) {} // ATL-autostub
}

package android.media;

import android.content.Context;
import android.net.Uri;

public class RingtoneManager {
	public static Ringtone getRingtone(Context context, Uri uri) {
		return new Ringtone();
	}

	public static Uri getDefaultUri(int type) {
		return new Uri.Builder().build();
	}

	public RingtoneManager(android.content.Context arg0) {} // ATL-autostub
	public android.database.Cursor getCursor() { return ((Object)this) instanceof android.database.Cursor ? (android.database.Cursor)(Object)this : null; } // ATL-autostub
	public android.net.Uri getRingtoneUri(int arg0) { return ((Object)this) instanceof android.net.Uri ? (android.net.Uri)(Object)this : null; } // ATL-autostub
	public void setType(int arg0) {} // ATL-autostub
}

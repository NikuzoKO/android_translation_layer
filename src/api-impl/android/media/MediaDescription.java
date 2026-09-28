package android.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

public class MediaDescription {

	public Uri iconUri;
	public CharSequence title;
	public CharSequence subtitle;

	public static class Builder {

		MediaDescription description = new MediaDescription();

		public Builder setMediaId(String mediaId) { return this; }

		public Builder setTitle(CharSequence title) {
			description.title = title;
			return this;
		}

		public Builder setSubtitle(CharSequence subtitle) {
			description.subtitle = subtitle;
			return this;
		}

		public Builder setDescription(CharSequence description) { return this; }

		public Builder setIconBitmap(Bitmap iconBitmap) { return this; }

		public Builder setIconUri(Uri iconUri) {
			description.iconUri = iconUri;
			return this;
		}

		public Builder setExtras(Bundle extras) { return this; }

		public Builder setMediaUri(Uri mediaUri) { return this; }

		public MediaDescription build() {
			return description;
		}
	}

	public android.graphics.Bitmap getIconBitmap() { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public android.net.Uri getIconUri() { return ((Object)this) instanceof android.net.Uri ? (android.net.Uri)(Object)this : null; } // ATL-autostub
	public android.net.Uri getMediaUri() { return ((Object)this) instanceof android.net.Uri ? (android.net.Uri)(Object)this : null; } // ATL-autostub
	public android.os.Bundle getExtras() { return ((Object)this) instanceof android.os.Bundle ? (android.os.Bundle)(Object)this : null; } // ATL-autostub
	public java.lang.CharSequence getDescription() { return null; } // ATL-autostub
	public java.lang.CharSequence getSubtitle() { return null; } // ATL-autostub
	public java.lang.CharSequence getTitle() { return null; } // ATL-autostub
	public java.lang.String getMediaId() { return null; } // ATL-autostub
	public static android.os.Parcelable.Creator CREATOR; // ATL-autostub
	public void writeToParcel(android.os.Parcel arg0, int arg1) {} // ATL-autostub
}

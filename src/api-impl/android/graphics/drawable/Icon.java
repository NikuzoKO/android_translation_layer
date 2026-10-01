package android.graphics.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import java.io.InputStream;

/* Minimal Icon: keeps what it was created from and turns it into a Drawable.
 * loadDrawable never returns null: apps (androidx NotificationCompat action
 * buttons, WhatsApp's media notification) use the result unchecked, so an
 * icon that can't be loaded becomes a 1x1 transparent bitmap. */
public class Icon {
	public static final int TYPE_BITMAP = 1;
	public static final int TYPE_RESOURCE = 2;
	public static final int TYPE_DATA = 3;
	public static final int TYPE_URI = 4;
	public static final int TYPE_ADAPTIVE_BITMAP = 5;
	public static final int TYPE_URI_ADAPTIVE_BITMAP = 6;

	private final int type;
	private Bitmap bitmap;
	private String resPackage;
	private int resId;
	private byte[] data;
	private int dataOffset;
	private int dataLength;
	private String uri;
	private ColorStateList tintList;
	private PorterDuff.Mode tintMode;

	private Icon(int type) {
		this.type = type;
	}

	public static Icon createWithResource(String packageName, int resourceId) {
		Icon icon = new Icon(TYPE_RESOURCE);
		icon.resPackage = packageName;
		icon.resId = resourceId;
		return icon;
	}

	public static Icon createWithResource(Context context, int resourceId) {
		return createWithResource(context == null ? null : context.getPackageName(), resourceId);
	}

	public static Icon createWithBitmap(Bitmap bitmap) {
		Icon icon = new Icon(TYPE_BITMAP);
		icon.bitmap = bitmap;
		return icon;
	}

	public static Icon createWithAdaptiveBitmap(Bitmap bitmap) {
		Icon icon = new Icon(TYPE_ADAPTIVE_BITMAP);
		icon.bitmap = bitmap;
		return icon;
	}

	public static Icon createWithData(byte[] data, int offset, int length) {
		Icon icon = new Icon(TYPE_DATA);
		icon.data = data;
		icon.dataOffset = offset;
		icon.dataLength = length;
		return icon;
	}

	public static Icon createWithContentUri(String uri) {
		Icon icon = new Icon(TYPE_URI);
		icon.uri = uri;
		return icon;
	}

	public static Icon createWithContentUri(Uri uri) {
		return createWithContentUri(uri == null ? null : uri.toString());
	}

	public static Icon createWithAdaptiveBitmapContentUri(Uri uri) {
		Icon icon = new Icon(TYPE_URI_ADAPTIVE_BITMAP);
		icon.uri = uri == null ? null : uri.toString();
		return icon;
	}

	public Drawable loadDrawable(Context context) {
		Drawable drawable = null;
		try {
			drawable = load(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (drawable == null)
			drawable = new BitmapDrawable(context == null ? null : context.getResources(),
			                              Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
		if (tintList != null)
			drawable.setTintList(tintList);
		return drawable;
	}

	private Drawable load(Context context) throws Exception {
		Bitmap loaded = null;
		switch (type) {
			case TYPE_BITMAP:
			case TYPE_ADAPTIVE_BITMAP:
				loaded = bitmap;
				break;
			case TYPE_RESOURCE:
				return context == null || resId == 0 ? null : context.getResources().getDrawable(resId, context.getTheme());
			case TYPE_DATA:
				loaded = data == null ? null : BitmapFactory.decodeByteArray(data, dataOffset, dataLength);
				break;
			case TYPE_URI:
			case TYPE_URI_ADAPTIVE_BITMAP:
				if (context != null && uri != null) {
					try (InputStream stream = context.getContentResolver().openInputStream(Uri.parse(uri))) {
						loaded = BitmapFactory.decodeStream(stream);
					}
				}
				break;
		}
		return loaded == null ? null : new BitmapDrawable(context == null ? null : context.getResources(), loaded);
	}

	public Icon setTintList(ColorStateList tintList) {
		this.tintList = tintList;
		return this;
	}

	public Icon setTint(int tint) {
		return setTintList(ColorStateList.valueOf(tint));
	}

	public Icon setTintMode(PorterDuff.Mode mode) {
		this.tintMode = mode;
		return this;
	}

	public int getType() {
		return type;
	}

	public int getResId() {
		return resId;
	}

	public String getResPackage() {
		return resPackage;
	}

	public Uri getUri() {
		return uri == null ? null : Uri.parse(uri);
	}
}

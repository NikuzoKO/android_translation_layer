package android.media;

import android.graphics.Bitmap;

public class ThumbnailUtils {

	public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
		return Bitmap.createScaledBitmap(source, width, height, true);
	}

	public static android.graphics.Bitmap createImageThumbnail(java.io.File arg0, android.util.Size arg1, android.os.CancellationSignal arg2) { return null; } // ATL-autostub
	public static android.graphics.Bitmap createVideoThumbnail(java.io.File arg0, android.util.Size arg1, android.os.CancellationSignal arg2) { return null; } // ATL-autostub
	public static android.graphics.Bitmap createVideoThumbnail(java.lang.String arg0, int arg1) { return null; } // ATL-autostub
}

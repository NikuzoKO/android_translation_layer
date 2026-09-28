package android.view;

import android.content.Context;
import android.util.AttributeSet;

public class TextureView extends View {

	private SurfaceTextureListener surfaceTextureListener;

	public TextureView(Context context) {
		super(context);
	}

	public TextureView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public interface SurfaceTextureListener {
	public default boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture arg0) { return false; } // ATL-autostub
	public default void onSurfaceTextureAvailable(android.graphics.SurfaceTexture arg0, int arg1, int arg2) {} // ATL-autostub
	public default void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture arg0, int arg1, int arg2) {} // ATL-autostub
	public default void onSurfaceTextureUpdated(android.graphics.SurfaceTexture arg0) {} // ATL-autostub
}

	public void setSurfaceTextureListener(SurfaceTextureListener surfaceTextureListener) {
		this.surfaceTextureListener = surfaceTextureListener;
	}

	public void setOpaque(boolean opaque) {}

	public SurfaceTextureListener getSurfaceTextureListener() {
		return surfaceTextureListener;
	}

	public boolean isAvailable() {
		return false;
	}

	public android.graphics.Bitmap getBitmap() { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public android.graphics.Bitmap getBitmap(android.graphics.Bitmap arg0) { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public android.graphics.Bitmap getBitmap(int arg0, int arg1) { return ((Object)this) instanceof android.graphics.Bitmap ? (android.graphics.Bitmap)(Object)this : null; } // ATL-autostub
	public android.graphics.Matrix getTransform(android.graphics.Matrix arg0) { return ((Object)this) instanceof android.graphics.Matrix ? (android.graphics.Matrix)(Object)this : null; } // ATL-autostub
	public android.graphics.SurfaceTexture getSurfaceTexture() { return ((Object)this) instanceof android.graphics.SurfaceTexture ? (android.graphics.SurfaceTexture)(Object)this : null; } // ATL-autostub
	public void setTransform(android.graphics.Matrix arg0) {} // ATL-autostub
}

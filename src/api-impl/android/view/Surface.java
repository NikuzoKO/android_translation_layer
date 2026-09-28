package android.view;

import android.graphics.SurfaceTexture;

public class Surface {
	public long widget;

	public Surface() {}

	public Surface(SurfaceTexture surfaceTexture) {}

	public boolean isValid() {
		return widget != 0;
	}

	public void release() {}

	public android.graphics.Canvas lockCanvas(android.graphics.Rect arg0) { return ((Object)this) instanceof android.graphics.Canvas ? (android.graphics.Canvas)(Object)this : null; } // ATL-autostub
	public android.graphics.Canvas lockHardwareCanvas() { return ((Object)this) instanceof android.graphics.Canvas ? (android.graphics.Canvas)(Object)this : null; } // ATL-autostub
	public void setFrameRate(float arg0, int arg1) {} // ATL-autostub
	public void unlockCanvasAndPost(android.graphics.Canvas arg0) {} // ATL-autostub
}

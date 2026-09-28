package android.graphics;

public class SurfaceTexture {

	public static interface OnFrameAvailableListener {}

	public SurfaceTexture(int texName) {}

	public void setOnFrameAvailableListener(OnFrameAvailableListener listener) {}

	public void release() {}

	public SurfaceTexture(boolean arg0) {} // ATL-autostub
	public SurfaceTexture(int arg0, boolean arg1) {} // ATL-autostub
	public boolean isReleased() { return false; } // ATL-autostub
	public int getDataSpace() { return 0; } // ATL-autostub
	public long getTimestamp() { return 0L; } // ATL-autostub
	public void attachToGLContext(int arg0) {} // ATL-autostub
	public void detachFromGLContext() {} // ATL-autostub
	public void getTransformMatrix(float[] arg0) {} // ATL-autostub
	public void setDefaultBufferSize(int arg0, int arg1) {} // ATL-autostub
	public void setOnFrameAvailableListener(android.graphics.SurfaceTexture.OnFrameAvailableListener arg0, android.os.Handler arg1) {} // ATL-autostub
	public void updateTexImage() {} // ATL-autostub
}

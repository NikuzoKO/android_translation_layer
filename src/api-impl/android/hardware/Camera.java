package android.hardware;

public class Camera {

	public interface PreviewCallback {}

	public interface AutoFocusCallback {}

	public interface ErrorCallback {}

	public static int getNumberOfCameras() { return 0; }

	public static Camera open() {
		return null;
	}

	public static Camera open(int cameraId) {
		return null;
	}

	public void setErrorCallback(ErrorCallback callback) {}

	public void addCallbackBuffer(byte[] arg0) {} // ATL-autostub
	public void autoFocus(android.hardware.Camera.AutoFocusCallback arg0) {} // ATL-autostub
	public void cancelAutoFocus() {} // ATL-autostub
	public void lock() {} // ATL-autostub
	public void reconnect() {} // ATL-autostub
	public void release() {} // ATL-autostub
	public void setDisplayOrientation(int arg0) {} // ATL-autostub
	public void setOneShotPreviewCallback(android.hardware.Camera.PreviewCallback arg0) {} // ATL-autostub
	public void setPreviewCallback(android.hardware.Camera.PreviewCallback arg0) {} // ATL-autostub
	public void setPreviewCallbackWithBuffer(android.hardware.Camera.PreviewCallback arg0) {} // ATL-autostub
	public void setPreviewDisplay(android.view.SurfaceHolder arg0) {} // ATL-autostub
	public void setPreviewTexture(android.graphics.SurfaceTexture arg0) {} // ATL-autostub
	public void startPreview() {} // ATL-autostub
	public void startSmoothZoom(int arg0) {} // ATL-autostub
	public void stopPreview() {} // ATL-autostub
	public void stopSmoothZoom() {} // ATL-autostub
	public void unlock() {} // ATL-autostub
}

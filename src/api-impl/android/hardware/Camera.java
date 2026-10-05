package android.hardware;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/*
 * android.hardware.Camera for ATL. The types (CameraInfo, Parameters, the
 * callbacks, ...) are AOSP's (android14-release). Frames come from an external
 * helper: startPreview() runs `$ATL_CAMERA_CMD start <id> <width> <height>`,
 * which keeps writing NV21 preview frames to $ATL_CAMERA_FRAME (default
 * /run/lagos-cam/frame): a 32-byte header (u32 magic "LCAM", version, width,
 * height, format, seq, size, frames) and one frame, under a seqlock (seq is odd
 * while the frame is being written). stopPreview()/release() run `... stop`.
 * Without ATL_CAMERA_CMD there are no cameras, like before.
 * The preview is drawn into the SurfaceHolder given to setPreviewDisplay
 * (half resolution, ~10 fps); setPreviewTexture has no backend yet.
 */
public class Camera {
	private static final String TAG = "Camera";
	private static final String CMD = System.getenv("ATL_CAMERA_CMD");
	private static final String FRAME_PATH = System.getenv("ATL_CAMERA_FRAME") != null
		? System.getenv("ATL_CAMERA_FRAME") : "/run/lagos-cam/frame";
	private static final int MAGIC = 0x4d41434c; /* "LCAM" */
	private static final int HEADER = 32;
	private static final int PREVIEW_WIDTH = 640, PREVIEW_HEIGHT = 480;

	private final int cameraId;
	private Parameters parameters;
	private final Handler eventHandler;
	private final Handler mainHandler = new Handler(Looper.getMainLooper());
	private PreviewCallback previewCallback;
	private boolean withBuffer, oneShot;
	private final ArrayDeque<byte[]> callbackBuffers = new ArrayDeque<>();
	private SurfaceHolder holder;
	private int displayOrientation;
	private ErrorCallback errorCallback;
	private volatile Thread frameThread;
	private boolean released;

	public static int getNumberOfCameras() {
		return CMD != null ? 2 : 0;
	}

	public static void getCameraInfo(int cameraId, CameraInfo info) {
		/* camera 0 = rear GC50F6, 1 = front SC820CS (sensor ids of the stock HAL) */
		info.facing = cameraId == 0 ? CameraInfo.CAMERA_FACING_BACK : CameraInfo.CAMERA_FACING_FRONT;
		info.orientation = cameraId == 0 ? 90 : 270;
		info.canDisableShutterSound = true;
	}

	public static Camera open(int cameraId) {
		if (CMD == null || cameraId < 0 || cameraId >= getNumberOfCameras())
			throw new RuntimeException("Fail to connect to camera service");
		Log.i(TAG, "open(" + cameraId + ")");
		return new Camera(cameraId);
	}

	public static Camera open() {
		return CMD != null ? open(0) : null;
	}

	private Camera(int cameraId) {
		this.cameraId = cameraId;
		Looper looper = Looper.myLooper();
		eventHandler = new Handler(looper != null ? looper : Looper.getMainLooper());
		parameters = new Parameters();
		parameters.unflatten("preview-size=" + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT
			+ ";preview-size-values=" + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT
			+ ";preview-format=yuv420sp;preview-format-values=yuv420sp"
			+ ";preview-frame-rate=25;preview-frame-rate-values=25"
			+ ";preview-fps-range=10000,30000;preview-fps-range-values=(10000,30000)"
			+ ";picture-size=" + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT
			+ ";picture-size-values=" + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT
			+ ";picture-format=jpeg;picture-format-values=jpeg;jpeg-quality=90"
			+ ";jpeg-thumbnail-size-values=0x0;jpeg-thumbnail-width=0;jpeg-thumbnail-height=0"
			+ ";focus-mode=continuous-picture;focus-mode-values=auto,continuous-picture,continuous-video,fixed"
			+ ";focus-distances=0.1,1.2,Infinity;max-num-focus-areas=0;max-num-metering-areas=0"
			+ ";zoom-supported=false;smooth-zoom-supported=false;max-zoom=0;zoom=0;zoom-ratios=100"
			+ ";exposure-compensation=0;max-exposure-compensation=0;min-exposure-compensation=0;exposure-compensation-step=0"
			+ ";auto-exposure-lock-supported=false;auto-whitebalance-lock-supported=false"
			+ ";whitebalance=auto;whitebalance-values=auto;scene-mode=auto;scene-mode-values=auto"
			+ ";antibanding=auto;antibanding-values=auto;effect=none;effect-values=none"
			+ ";video-size=" + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT + ";video-size-values=" + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT
			+ ";preferred-preview-size-for-video=" + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT
			+ ";video-stabilization-supported=false;video-snapshot-supported=false"
			+ ";focal-length=3.5;horizontal-view-angle=66;vertical-view-angle=52"
			+ ";max-num-detected-faces-hw=0;max-num-detected-faces-sw=0");
	}

	public Parameters getParameters() {
		Parameters copy = new Parameters();
		copy.unflatten(parameters.flatten());
		return copy;
	}

	public void setParameters(Parameters params) {
		Size size = params.getPreviewSize();
		if (size != null && (size.width != PREVIEW_WIDTH || size.height != PREVIEW_HEIGHT))
			Log.w(TAG, "setParameters: preview size " + size.width + "x" + size.height + " not supported, keeping " + PREVIEW_WIDTH + "x" + PREVIEW_HEIGHT);
		parameters = new Parameters();
		parameters.unflatten(params.flatten());
		parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
		parameters.setPreviewFormat(17 /* ImageFormat.NV21 */);
	}

	public final void setPreviewDisplay(SurfaceHolder holder) {
		this.holder = holder;
	}

	public final void setPreviewTexture(SurfaceTexture surfaceTexture) {
		Log.w(TAG, "setPreviewTexture: no preview drawing for SurfaceTexture, frames still go to the preview callback");
	}

	public final void setDisplayOrientation(int degrees) {
		displayOrientation = degrees;
	}

	public final synchronized void setPreviewCallback(PreviewCallback cb) {
		previewCallback = cb;
		withBuffer = false;
		oneShot = false;
	}

	public final synchronized void setOneShotPreviewCallback(PreviewCallback cb) {
		previewCallback = cb;
		withBuffer = false;
		oneShot = true;
	}

	public final synchronized void setPreviewCallbackWithBuffer(PreviewCallback cb) {
		previewCallback = cb;
		withBuffer = true;
		oneShot = false;
	}

	public final synchronized void addCallbackBuffer(byte[] buffer) {
		if (buffer != null)
			callbackBuffers.add(buffer);
	}

	public final void setErrorCallback(ErrorCallback cb) {
		errorCallback = cb;
	}

	private void runCmd(String... args) {
		if (CMD == null)
			return;
		String[] command = new String[args.length + 1];
		command[0] = CMD;
		System.arraycopy(args, 0, command, 1, args.length);
		try {
			new ProcessBuilder(command).inheritIO().start().waitFor();
		} catch (Exception e) {
			Log.w(TAG, CMD + " " + args[0] + " failed: " + e);
		}
	}

	public final synchronized void startPreview() {
		if (frameThread != null || released)
			return;
		Log.i(TAG, "startPreview: camera " + cameraId);
		new File(FRAME_PATH).delete(); /* only fresh frames count; ignored if not ours */
		Thread thread = new Thread(new Runnable() {
			public void run() {
				frameLoop();
			}
		}, "ATL-CameraFrames");
		frameThread = thread;
		thread.start();
	}

	public final void stopPreview() {
		Thread thread;
		synchronized (this) {
			thread = frameThread;
			frameThread = null;
		}
		if (thread == null)
			return;
		Log.i(TAG, "stopPreview: camera " + cameraId);
		thread.interrupt();
		try {
			thread.join(2000);
		} catch (InterruptedException e) {
		}
		runCmd("stop");
	}

	public final void release() {
		stopPreview();
		released = true;
	}

	/* the frame thread: start the helper, then copy every new frame out of the shared file */
	private void frameLoop() {
		runCmd("start", Integer.toString(cameraId), Integer.toString(PREVIEW_WIDTH), Integer.toString(PREVIEW_HEIGHT));
		Thread self = Thread.currentThread();
		MappedByteBuffer map = null;
		int size = PREVIEW_WIDTH * PREVIEW_HEIGHT * 3 / 2;
		byte[] frame = new byte[size];
		int lastSeq = -1;
		long lastDraw = 0;
		long started = System.currentTimeMillis();
		while (frameThread == self) {
			try {
				if (map == null) {
					File file = new File(FRAME_PATH);
					if (file.length() >= HEADER + size) {
						try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
							map = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, HEADER + size);
							map.order(ByteOrder.LITTLE_ENDIAN);
						}
						if (map.getInt(0) != MAGIC || map.getInt(8) != PREVIEW_WIDTH || map.getInt(12) != PREVIEW_HEIGHT)
							map = null;
					}
					if (map == null) {
						if (System.currentTimeMillis() - started > 60000) {
							Log.w(TAG, "no frames from " + FRAME_PATH + " after 60 s");
							notifyError();
							return;
						}
						Thread.sleep(100);
						continue;
					}
				}
				int seq = map.getInt(20);
				if (seq == lastSeq || (seq & 1) != 0) {
					Thread.sleep(10);
					continue;
				}
				map.position(HEADER);
				map.get(frame, 0, size);
				if (map.getInt(20) != seq)
					continue; /* overwritten while copying */
				lastSeq = seq;
				deliver(frame);
				long now = System.currentTimeMillis();
				if (holder != null && now - lastDraw >= 100) {
					lastDraw = now;
					drawPreview(frame);
				}
			} catch (InterruptedException e) {
				return;
			} catch (Exception e) {
				Log.w(TAG, "frame loop: " + e);
				map = null;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e2) {
					return;
				}
			}
		}
	}

	private void notifyError() {
		final ErrorCallback cb = errorCallback;
		if (cb != null)
			eventHandler.post(new Runnable() {
				public void run() {
					cb.onError(1 /* CAMERA_ERROR_UNKNOWN */, Camera.this);
				}
			});
	}

	private void deliver(byte[] frame) {
		final PreviewCallback cb;
		final byte[] data;
		synchronized (this) {
			cb = previewCallback;
			if (cb == null)
				return;
			if (withBuffer) {
				byte[] buffer = callbackBuffers.poll();
				if (buffer == null)
					return; /* app hasn't returned a buffer yet: drop the frame */
				if (buffer.length < frame.length) {
					Log.w(TAG, "callback buffer too small: " + buffer.length);
					return;
				}
				data = buffer;
			} else {
				data = new byte[frame.length];
			}
			if (oneShot)
				previewCallback = null;
		}
		System.arraycopy(frame, 0, data, 0, frame.length);
		eventHandler.post(new Runnable() {
			public void run() {
				cb.onPreviewFrame(data, Camera.this);
			}
		});
	}

	/* NV21 -> ARGB at half resolution, rotated by the display orientation, center-cropped */
	private void drawPreview(byte[] nv21) {
		final int w = PREVIEW_WIDTH / 2, h = PREVIEW_HEIGHT / 2;
		final int[] argb = new int[w * h];
		final boolean mirror = cameraId == 1;
		for (int y = 0; y < h; y++) {
			int yRow = (y * 2) * PREVIEW_WIDTH;
			int uvRow = PREVIEW_WIDTH * PREVIEW_HEIGHT + y * PREVIEW_WIDTH;
			for (int x = 0; x < w; x++) {
				int Y = nv21[yRow + x * 2] & 0xff;
				int V = (nv21[uvRow + x * 2] & 0xff) - 128;
				int U = (nv21[uvRow + x * 2 + 1] & 0xff) - 128;
				int r = Y + ((1436 * V) >> 10);
				int g = Y - ((352 * U + 731 * V) >> 10);
				int b = Y + ((1815 * U) >> 10);
				r = r < 0 ? 0 : r > 255 ? 255 : r;
				g = g < 0 ? 0 : g > 255 ? 255 : g;
				b = b < 0 ? 0 : b > 255 ? 255 : b;
				argb[y * w + (mirror ? w - 1 - x : x)] = 0xff000000 | (r << 16) | (g << 8) | b;
			}
		}
		mainHandler.post(new Runnable() { public void run() {
			SurfaceHolder h2 = holder;
			if (h2 == null || frameThread == null)
				return;
			Rect frameRect = h2.getSurfaceFrame();
			int viewW = frameRect.width(), viewH = frameRect.height();
			if (viewW <= 0 || viewH <= 0)
				return;
			Bitmap bitmap = Bitmap.createBitmap(argb, w, h, Bitmap.Config.ARGB_8888);
			Canvas canvas = h2.lockCanvas();
			if (canvas == null)
				return;
			boolean sideways = displayOrientation % 180 != 0;
			float imageW = sideways ? h : w, imageH = sideways ? w : h;
			float scale = Math.max(viewW / imageW, viewH / imageH); /* fill, crop the rest */
			canvas.save();
			canvas.translate(viewW / 2f, viewH / 2f);
			canvas.rotate(displayOrientation);
			canvas.scale(scale, scale);
			canvas.drawBitmap(bitmap, null, new Rect(-w / 2, -h / 2, w - w / 2, h - h / 2), null);
			canvas.restore();
			h2.unlockCanvasAndPost(canvas);
		}});
	}

	public final void autoFocus(AutoFocusCallback cb) {
		/* the HAL runs continuous AF on its own */
		if (cb != null)
			eventHandler.post(new Runnable() {
				public void run() {
					cb.onAutoFocus(true, Camera.this);
				}
			});
	}

	public final void cancelAutoFocus() {}

	public void setAutoFocusMoveCallback(AutoFocusMoveCallback cb) {}

	public final void takePicture(ShutterCallback shutter, PictureCallback raw, PictureCallback jpeg) {
		takePicture(shutter, raw, null, jpeg);
	}

	public final void takePicture(ShutterCallback shutter, PictureCallback raw, PictureCallback postview, PictureCallback jpeg) {
		Log.w(TAG, "takePicture: not implemented, only the shutter callback fires");
		if (shutter != null)
			eventHandler.post(new Runnable() {
				public void run() {
					shutter.onShutter();
				}
			});
	}

	public final boolean enableShutterSound(boolean enabled) {
		return true;
	}

	public final void lock() {}
	public final void unlock() {}
	public final void reconnect() {}
	public final void startSmoothZoom(int value) {}
	public final void stopSmoothZoom() {}
	public final void setZoomChangeListener(OnZoomChangeListener listener) {}
	public final void setFaceDetectionListener(FaceDetectionListener listener) {}
	public final void startFaceDetection() {}
	public final void stopFaceDetection() {}

    public static class CameraInfo {
        
        public static final int CAMERA_FACING_BACK = 0;

        public static final int CAMERA_FACING_FRONT = 1;

        public int facing;

        public int orientation;

        public boolean canDisableShutterSound;
    };

    public interface PreviewCallback
    {
        
        void onPreviewFrame(byte[] data, Camera camera);
    };

    public interface AutoFocusCallback
    {
        
        void onAutoFocus(boolean success, Camera camera);
    }

    public interface AutoFocusMoveCallback
    {
        
        void onAutoFocusMoving(boolean start, Camera camera);
    }

    public interface ShutterCallback
    {
        
        void onShutter();
    }

    public interface PictureCallback {
        
        void onPictureTaken(byte[] data, Camera camera);
    };

    public interface OnZoomChangeListener
    {
        
        void onZoomChange(int zoomValue, boolean stopped, Camera camera);
    };

    public interface FaceDetectionListener
    {
        
        void onFaceDetection(Face[] faces, Camera camera);
    }

    public static class Face {
        
        public Face() {
        }

        public Rect rect;

        public int score;

        public int id = -1;

        public Point leftEye = null;

        public Point rightEye = null;

        public Point mouth = null;
    }

    public interface ErrorCallback
    {
        
        void onError(int error, Camera camera);
    };

    public class Size {
        
        public Size(int w, int h) {
            width = w;
            height = h;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Size)) {
                return false;
            }
            Size s = (Size) obj;
            return width == s.width && height == s.height;
        }
        @Override
        public int hashCode() {
            return width * 32713 + height;
        }
        
        public int width;
        
        public int height;
    };

    public static class Area {
        
        public Area(Rect rect, int weight) {
            this.rect = rect;
            this.weight = weight;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Area)) {
                return false;
            }
            Area a = (Area) obj;
            if (rect == null) {
                if (a.rect != null) return false;
            } else {
                if (!rect.equals(a.rect)) return false;
            }
            return weight == a.weight;
        }

        public Rect rect;

        public int weight;
    }

    public class Parameters {
        // Parameter keys to communicate with the camera driver.
        private static final String KEY_PREVIEW_SIZE = "preview-size";
        private static final String KEY_PREVIEW_FORMAT = "preview-format";
        private static final String KEY_PREVIEW_FRAME_RATE = "preview-frame-rate";
        private static final String KEY_PREVIEW_FPS_RANGE = "preview-fps-range";
        private static final String KEY_PICTURE_SIZE = "picture-size";
        private static final String KEY_PICTURE_FORMAT = "picture-format";
        private static final String KEY_JPEG_THUMBNAIL_SIZE = "jpeg-thumbnail-size";
        private static final String KEY_JPEG_THUMBNAIL_WIDTH = "jpeg-thumbnail-width";
        private static final String KEY_JPEG_THUMBNAIL_HEIGHT = "jpeg-thumbnail-height";
        private static final String KEY_JPEG_THUMBNAIL_QUALITY = "jpeg-thumbnail-quality";
        private static final String KEY_JPEG_QUALITY = "jpeg-quality";
        private static final String KEY_ROTATION = "rotation";
        private static final String KEY_GPS_LATITUDE = "gps-latitude";
        private static final String KEY_GPS_LONGITUDE = "gps-longitude";
        private static final String KEY_GPS_ALTITUDE = "gps-altitude";
        private static final String KEY_GPS_TIMESTAMP = "gps-timestamp";
        private static final String KEY_GPS_PROCESSING_METHOD = "gps-processing-method";
        private static final String KEY_WHITE_BALANCE = "whitebalance";
        private static final String KEY_EFFECT = "effect";
        private static final String KEY_ANTIBANDING = "antibanding";
        private static final String KEY_SCENE_MODE = "scene-mode";
        private static final String KEY_FLASH_MODE = "flash-mode";
        private static final String KEY_FOCUS_MODE = "focus-mode";
        private static final String KEY_FOCUS_AREAS = "focus-areas";
        private static final String KEY_MAX_NUM_FOCUS_AREAS = "max-num-focus-areas";
        private static final String KEY_FOCAL_LENGTH = "focal-length";
        private static final String KEY_HORIZONTAL_VIEW_ANGLE = "horizontal-view-angle";
        private static final String KEY_VERTICAL_VIEW_ANGLE = "vertical-view-angle";
        private static final String KEY_EXPOSURE_COMPENSATION = "exposure-compensation";
        private static final String KEY_MAX_EXPOSURE_COMPENSATION = "max-exposure-compensation";
        private static final String KEY_MIN_EXPOSURE_COMPENSATION = "min-exposure-compensation";
        private static final String KEY_EXPOSURE_COMPENSATION_STEP = "exposure-compensation-step";
        private static final String KEY_AUTO_EXPOSURE_LOCK = "auto-exposure-lock";
        private static final String KEY_AUTO_EXPOSURE_LOCK_SUPPORTED = "auto-exposure-lock-supported";
        private static final String KEY_AUTO_WHITEBALANCE_LOCK = "auto-whitebalance-lock";
        private static final String KEY_AUTO_WHITEBALANCE_LOCK_SUPPORTED = "auto-whitebalance-lock-supported";
        private static final String KEY_METERING_AREAS = "metering-areas";
        private static final String KEY_MAX_NUM_METERING_AREAS = "max-num-metering-areas";
        private static final String KEY_ZOOM = "zoom";
        private static final String KEY_MAX_ZOOM = "max-zoom";
        private static final String KEY_ZOOM_RATIOS = "zoom-ratios";
        private static final String KEY_ZOOM_SUPPORTED = "zoom-supported";
        private static final String KEY_SMOOTH_ZOOM_SUPPORTED = "smooth-zoom-supported";
        private static final String KEY_FOCUS_DISTANCES = "focus-distances";
        private static final String KEY_VIDEO_SIZE = "video-size";
        private static final String KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO =
                                            "preferred-preview-size-for-video";
        private static final String KEY_MAX_NUM_DETECTED_FACES_HW = "max-num-detected-faces-hw";
        private static final String KEY_MAX_NUM_DETECTED_FACES_SW = "max-num-detected-faces-sw";
        private static final String KEY_RECORDING_HINT = "recording-hint";
        private static final String KEY_VIDEO_SNAPSHOT_SUPPORTED = "video-snapshot-supported";
        private static final String KEY_VIDEO_STABILIZATION = "video-stabilization";
        private static final String KEY_VIDEO_STABILIZATION_SUPPORTED = "video-stabilization-supported";

        // Parameter key suffix for supported values.
        private static final String SUPPORTED_VALUES_SUFFIX = "-values";

        private static final String TRUE = "true";
        private static final String FALSE = "false";

        // Values for white balance settings.
        public static final String WHITE_BALANCE_AUTO = "auto";
        public static final String WHITE_BALANCE_INCANDESCENT = "incandescent";
        public static final String WHITE_BALANCE_FLUORESCENT = "fluorescent";
        public static final String WHITE_BALANCE_WARM_FLUORESCENT = "warm-fluorescent";
        public static final String WHITE_BALANCE_DAYLIGHT = "daylight";
        public static final String WHITE_BALANCE_CLOUDY_DAYLIGHT = "cloudy-daylight";
        public static final String WHITE_BALANCE_TWILIGHT = "twilight";
        public static final String WHITE_BALANCE_SHADE = "shade";

        // Values for color effect settings.
        public static final String EFFECT_NONE = "none";
        public static final String EFFECT_MONO = "mono";
        public static final String EFFECT_NEGATIVE = "negative";
        public static final String EFFECT_SOLARIZE = "solarize";
        public static final String EFFECT_SEPIA = "sepia";
        public static final String EFFECT_POSTERIZE = "posterize";
        public static final String EFFECT_WHITEBOARD = "whiteboard";
        public static final String EFFECT_BLACKBOARD = "blackboard";
        public static final String EFFECT_AQUA = "aqua";

        // Values for antibanding settings.
        public static final String ANTIBANDING_AUTO = "auto";
        public static final String ANTIBANDING_50HZ = "50hz";
        public static final String ANTIBANDING_60HZ = "60hz";
        public static final String ANTIBANDING_OFF = "off";

        // Values for flash mode settings.
        
        public static final String FLASH_MODE_OFF = "off";

        public static final String FLASH_MODE_AUTO = "auto";

        public static final String FLASH_MODE_ON = "on";

        public static final String FLASH_MODE_RED_EYE = "red-eye";

        public static final String FLASH_MODE_TORCH = "torch";

        public static final String SCENE_MODE_AUTO = "auto";

        public static final String SCENE_MODE_ACTION = "action";

        public static final String SCENE_MODE_PORTRAIT = "portrait";

        public static final String SCENE_MODE_LANDSCAPE = "landscape";

        public static final String SCENE_MODE_NIGHT = "night";

        public static final String SCENE_MODE_NIGHT_PORTRAIT = "night-portrait";

        public static final String SCENE_MODE_THEATRE = "theatre";

        public static final String SCENE_MODE_BEACH = "beach";

        public static final String SCENE_MODE_SNOW = "snow";

        public static final String SCENE_MODE_SUNSET = "sunset";

        public static final String SCENE_MODE_STEADYPHOTO = "steadyphoto";

        public static final String SCENE_MODE_FIREWORKS = "fireworks";

        public static final String SCENE_MODE_SPORTS = "sports";

        public static final String SCENE_MODE_PARTY = "party";

        public static final String SCENE_MODE_CANDLELIGHT = "candlelight";

        public static final String SCENE_MODE_BARCODE = "barcode";

        public static final String SCENE_MODE_HDR = "hdr";

        public static final String FOCUS_MODE_AUTO = "auto";

        public static final String FOCUS_MODE_INFINITY = "infinity";

        public static final String FOCUS_MODE_MACRO = "macro";

        public static final String FOCUS_MODE_FIXED = "fixed";

        public static final String FOCUS_MODE_EDOF = "edof";

        public static final String FOCUS_MODE_CONTINUOUS_VIDEO = "continuous-video";

        public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";

        // Indices for focus distance array.
        
        public static final int FOCUS_DISTANCE_NEAR_INDEX = 0;

        public static final int FOCUS_DISTANCE_OPTIMAL_INDEX = 1;

        public static final int FOCUS_DISTANCE_FAR_INDEX = 2;

        public static final int PREVIEW_FPS_MIN_INDEX = 0;

        public static final int PREVIEW_FPS_MAX_INDEX = 1;

        // Formats for setPreviewFormat and setPictureFormat.
        private static final String PIXEL_FORMAT_YUV422SP = "yuv422sp";
        private static final String PIXEL_FORMAT_YUV420SP = "yuv420sp";
        private static final String PIXEL_FORMAT_YUV422I = "yuv422i-yuyv";
        private static final String PIXEL_FORMAT_YUV420P = "yuv420p";
        private static final String PIXEL_FORMAT_RGB565 = "rgb565";
        private static final String PIXEL_FORMAT_JPEG = "jpeg";
        private static final String PIXEL_FORMAT_BAYER_RGGB = "bayer-rggb";

        private final LinkedHashMap<String, String> mMap;

        private Parameters() {
            mMap = new LinkedHashMap<String, String>(64);
        }

        public void copyFrom(Parameters other) {
            if (other == null) {
                throw new NullPointerException("other must not be null");
            }

            mMap.putAll(other.mMap);
        }

        private Camera getOuter() {
            return Camera.this;
        }

        public boolean same(Parameters other) {
            if (this == other) {
                return true;
            }
            return other != null && Parameters.this.mMap.equals(other.mMap);
        }

        @Deprecated
        public void dump() {
            Log.e(TAG, "dump: size=" + mMap.size());
            for (String k : mMap.keySet()) {
                Log.e(TAG, "dump: " + k + "=" + mMap.get(k));
            }
        }

        public String flatten() {
            StringBuilder flattened = new StringBuilder(128);
            for (String k : mMap.keySet()) {
                flattened.append(k);
                flattened.append("=");
                flattened.append(mMap.get(k));
                flattened.append(";");
            }
            // chop off the extra semicolon at the end
            flattened.deleteCharAt(flattened.length()-1);
            return flattened.toString();
        }

        public void unflatten(String flattened) {
            mMap.clear();

            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(';');
            splitter.setString(flattened);
            for (String kv : splitter) {
                int pos = kv.indexOf('=');
                if (pos == -1) {
                    continue;
                }
                String k = kv.substring(0, pos);
                String v = kv.substring(pos + 1);
                mMap.put(k, v);
            }
        }

        public void remove(String key) {
            mMap.remove(key);
        }

        public void set(String key, String value) {
            if (key.indexOf('=') != -1 || key.indexOf(';') != -1 || key.indexOf(0) != -1) {
                Log.e(TAG, "Key \"" + key + "\" contains invalid character (= or ; or \\0)");
                return;
            }
            if (value.indexOf('=') != -1 || value.indexOf(';') != -1 || value.indexOf(0) != -1) {
                Log.e(TAG, "Value \"" + value + "\" contains invalid character (= or ; or \\0)");
                return;
            }

            put(key, value);
        }

        public void set(String key, int value) {
            put(key, Integer.toString(value));
        }

        private void put(String key, String value) {
            
            mMap.remove(key);
            mMap.put(key, value);
        }

        private void set(String key, List<Area> areas) {
            if (areas == null) {
                set(key, "(0,0,0,0,0)");
            } else {
                StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < areas.size(); i++) {
                    Area area = areas.get(i);
                    Rect rect = area.rect;
                    buffer.append('(');
                    buffer.append(rect.left);
                    buffer.append(',');
                    buffer.append(rect.top);
                    buffer.append(',');
                    buffer.append(rect.right);
                    buffer.append(',');
                    buffer.append(rect.bottom);
                    buffer.append(',');
                    buffer.append(area.weight);
                    buffer.append(')');
                    if (i != areas.size() - 1) buffer.append(',');
                }
                set(key, buffer.toString());
            }
        }

        public String get(String key) {
            return mMap.get(key);
        }

        public int getInt(String key) {
            return Integer.parseInt(mMap.get(key));
        }

        public void setPreviewSize(int width, int height) {
            String v = Integer.toString(width) + "x" + Integer.toString(height);
            set(KEY_PREVIEW_SIZE, v);
        }

        public Size getPreviewSize() {
            String pair = get(KEY_PREVIEW_SIZE);
            return strToSize(pair);
        }

        public List<Size> getSupportedPreviewSizes() {
            String str = get(KEY_PREVIEW_SIZE + SUPPORTED_VALUES_SUFFIX);
            return splitSize(str);
        }

        public List<Size> getSupportedVideoSizes() {
            String str = get(KEY_VIDEO_SIZE + SUPPORTED_VALUES_SUFFIX);
            return splitSize(str);
        }

        public Size getPreferredPreviewSizeForVideo() {
            String pair = get(KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO);
            return strToSize(pair);
        }

        public void setJpegThumbnailSize(int width, int height) {
            set(KEY_JPEG_THUMBNAIL_WIDTH, width);
            set(KEY_JPEG_THUMBNAIL_HEIGHT, height);
        }

        public Size getJpegThumbnailSize() {
            return new Size(getInt(KEY_JPEG_THUMBNAIL_WIDTH),
                            getInt(KEY_JPEG_THUMBNAIL_HEIGHT));
        }

        public List<Size> getSupportedJpegThumbnailSizes() {
            String str = get(KEY_JPEG_THUMBNAIL_SIZE + SUPPORTED_VALUES_SUFFIX);
            return splitSize(str);
        }

        public void setJpegThumbnailQuality(int quality) {
            set(KEY_JPEG_THUMBNAIL_QUALITY, quality);
        }

        public int getJpegThumbnailQuality() {
            return getInt(KEY_JPEG_THUMBNAIL_QUALITY);
        }

        public void setJpegQuality(int quality) {
            set(KEY_JPEG_QUALITY, quality);
        }

        public int getJpegQuality() {
            return getInt(KEY_JPEG_QUALITY);
        }

        @Deprecated
        public void setPreviewFrameRate(int fps) {
            set(KEY_PREVIEW_FRAME_RATE, fps);
        }

        @Deprecated
        public int getPreviewFrameRate() {
            return getInt(KEY_PREVIEW_FRAME_RATE);
        }

        @Deprecated
        public List<Integer> getSupportedPreviewFrameRates() {
            String str = get(KEY_PREVIEW_FRAME_RATE + SUPPORTED_VALUES_SUFFIX);
            return splitInt(str);
        }

        public void setPreviewFpsRange(int min, int max) {
            set(KEY_PREVIEW_FPS_RANGE, "" + min + "," + max);
        }

        public void getPreviewFpsRange(int[] range) {
            if (range == null || range.length != 2) {
                throw new IllegalArgumentException(
                        "range must be an array with two elements.");
            }
            splitInt(get(KEY_PREVIEW_FPS_RANGE), range);
        }

        public List<int[]> getSupportedPreviewFpsRange() {
            String str = get(KEY_PREVIEW_FPS_RANGE + SUPPORTED_VALUES_SUFFIX);
            return splitRange(str);
        }

        public void setPreviewFormat(int pixel_format) {
            String s = cameraFormatForPixelFormat(pixel_format);
            if (s == null) {
                throw new IllegalArgumentException(
                        "Invalid pixel_format=" + pixel_format);
            }

            set(KEY_PREVIEW_FORMAT, s);
        }

        public int getPreviewFormat() {
            return pixelFormatForCameraFormat(get(KEY_PREVIEW_FORMAT));
        }

        public List<Integer> getSupportedPreviewFormats() {
            String str = get(KEY_PREVIEW_FORMAT + SUPPORTED_VALUES_SUFFIX);
            ArrayList<Integer> formats = new ArrayList<Integer>();
            for (String s : split(str)) {
                int f = pixelFormatForCameraFormat(s);
                if (f == ImageFormat.UNKNOWN) continue;
                formats.add(f);
            }
            return formats;
        }

        public void setPictureSize(int width, int height) {
            String v = Integer.toString(width) + "x" + Integer.toString(height);
            set(KEY_PICTURE_SIZE, v);
        }

        public Size getPictureSize() {
            String pair = get(KEY_PICTURE_SIZE);
            return strToSize(pair);
        }

        public List<Size> getSupportedPictureSizes() {
            String str = get(KEY_PICTURE_SIZE + SUPPORTED_VALUES_SUFFIX);
            return splitSize(str);
        }

        public void setPictureFormat(int pixel_format) {
            String s = cameraFormatForPixelFormat(pixel_format);
            if (s == null) {
                throw new IllegalArgumentException(
                        "Invalid pixel_format=" + pixel_format);
            }

            set(KEY_PICTURE_FORMAT, s);
        }

        public int getPictureFormat() {
            return pixelFormatForCameraFormat(get(KEY_PICTURE_FORMAT));
        }

        public List<Integer> getSupportedPictureFormats() {
            String str = get(KEY_PICTURE_FORMAT + SUPPORTED_VALUES_SUFFIX);
            ArrayList<Integer> formats = new ArrayList<Integer>();
            for (String s : split(str)) {
                int f = pixelFormatForCameraFormat(s);
                if (f == ImageFormat.UNKNOWN) continue;
                formats.add(f);
            }
            return formats;
        }

        private String cameraFormatForPixelFormat(int pixel_format) {
            switch(pixel_format) {
            case ImageFormat.NV16:      return PIXEL_FORMAT_YUV422SP;
            case ImageFormat.NV21:      return PIXEL_FORMAT_YUV420SP;
            case ImageFormat.YUY2:      return PIXEL_FORMAT_YUV422I;
            case ImageFormat.YV12:      return PIXEL_FORMAT_YUV420P;
            case ImageFormat.RGB_565:   return PIXEL_FORMAT_RGB565;
            case ImageFormat.JPEG:      return PIXEL_FORMAT_JPEG;
            default:                    return null;
            }
        }

        private int pixelFormatForCameraFormat(String format) {
            if (format == null)
                return ImageFormat.UNKNOWN;

            if (format.equals(PIXEL_FORMAT_YUV422SP))
                return ImageFormat.NV16;

            if (format.equals(PIXEL_FORMAT_YUV420SP))
                return ImageFormat.NV21;

            if (format.equals(PIXEL_FORMAT_YUV422I))
                return ImageFormat.YUY2;

            if (format.equals(PIXEL_FORMAT_YUV420P))
                return ImageFormat.YV12;

            if (format.equals(PIXEL_FORMAT_RGB565))
                return ImageFormat.RGB_565;

            if (format.equals(PIXEL_FORMAT_JPEG))
                return ImageFormat.JPEG;

            return ImageFormat.UNKNOWN;
        }

        public void setRotation(int rotation) {
            if (rotation == 0 || rotation == 90 || rotation == 180
                    || rotation == 270) {
                set(KEY_ROTATION, Integer.toString(rotation));
            } else {
                throw new IllegalArgumentException(
                        "Invalid rotation=" + rotation);
            }
        }

        public void setGpsLatitude(double latitude) {
            set(KEY_GPS_LATITUDE, Double.toString(latitude));
        }

        public void setGpsLongitude(double longitude) {
            set(KEY_GPS_LONGITUDE, Double.toString(longitude));
        }

        public void setGpsAltitude(double altitude) {
            set(KEY_GPS_ALTITUDE, Double.toString(altitude));
        }

        public void setGpsTimestamp(long timestamp) {
            set(KEY_GPS_TIMESTAMP, Long.toString(timestamp));
        }

        public void setGpsProcessingMethod(String processing_method) {
            set(KEY_GPS_PROCESSING_METHOD, processing_method);
        }

        public void removeGpsData() {
            remove(KEY_GPS_LATITUDE);
            remove(KEY_GPS_LONGITUDE);
            remove(KEY_GPS_ALTITUDE);
            remove(KEY_GPS_TIMESTAMP);
            remove(KEY_GPS_PROCESSING_METHOD);
        }

        public String getWhiteBalance() {
            return get(KEY_WHITE_BALANCE);
        }

        public void setWhiteBalance(String value) {
            String oldValue = get(KEY_WHITE_BALANCE);
            if (same(value, oldValue)) return;
            set(KEY_WHITE_BALANCE, value);
            set(KEY_AUTO_WHITEBALANCE_LOCK, FALSE);
        }

        public List<String> getSupportedWhiteBalance() {
            String str = get(KEY_WHITE_BALANCE + SUPPORTED_VALUES_SUFFIX);
            return split(str);
        }

        public String getColorEffect() {
            return get(KEY_EFFECT);
        }

        public void setColorEffect(String value) {
            set(KEY_EFFECT, value);
        }

        public List<String> getSupportedColorEffects() {
            String str = get(KEY_EFFECT + SUPPORTED_VALUES_SUFFIX);
            return split(str);
        }

        public String getAntibanding() {
            return get(KEY_ANTIBANDING);
        }

        public void setAntibanding(String antibanding) {
            set(KEY_ANTIBANDING, antibanding);
        }

        public List<String> getSupportedAntibanding() {
            String str = get(KEY_ANTIBANDING + SUPPORTED_VALUES_SUFFIX);
            return split(str);
        }

        public String getSceneMode() {
            return get(KEY_SCENE_MODE);
        }

        public void setSceneMode(String value) {
            set(KEY_SCENE_MODE, value);
        }

        public List<String> getSupportedSceneModes() {
            String str = get(KEY_SCENE_MODE + SUPPORTED_VALUES_SUFFIX);
            return split(str);
        }

        public String getFlashMode() {
            return get(KEY_FLASH_MODE);
        }

        public void setFlashMode(String value) {
            set(KEY_FLASH_MODE, value);
        }

        public List<String> getSupportedFlashModes() {
            String str = get(KEY_FLASH_MODE + SUPPORTED_VALUES_SUFFIX);
            return split(str);
        }

        public String getFocusMode() {
            return get(KEY_FOCUS_MODE);
        }

        public void setFocusMode(String value) {
            set(KEY_FOCUS_MODE, value);
        }

        public List<String> getSupportedFocusModes() {
            String str = get(KEY_FOCUS_MODE + SUPPORTED_VALUES_SUFFIX);
            return split(str);
        }

        public float getFocalLength() {
            return Float.parseFloat(get(KEY_FOCAL_LENGTH));
        }

        public float getHorizontalViewAngle() {
            return Float.parseFloat(get(KEY_HORIZONTAL_VIEW_ANGLE));
        }

        public float getVerticalViewAngle() {
            return Float.parseFloat(get(KEY_VERTICAL_VIEW_ANGLE));
        }

        public int getExposureCompensation() {
            return getInt(KEY_EXPOSURE_COMPENSATION, 0);
        }

        public void setExposureCompensation(int value) {
            set(KEY_EXPOSURE_COMPENSATION, value);
        }

        public int getMaxExposureCompensation() {
            return getInt(KEY_MAX_EXPOSURE_COMPENSATION, 0);
        }

        public int getMinExposureCompensation() {
            return getInt(KEY_MIN_EXPOSURE_COMPENSATION, 0);
        }

        public float getExposureCompensationStep() {
            return getFloat(KEY_EXPOSURE_COMPENSATION_STEP, 0);
        }

        public void setAutoExposureLock(boolean toggle) {
            set(KEY_AUTO_EXPOSURE_LOCK, toggle ? TRUE : FALSE);
        }

        public boolean getAutoExposureLock() {
            String str = get(KEY_AUTO_EXPOSURE_LOCK);
            return TRUE.equals(str);
        }

        public boolean isAutoExposureLockSupported() {
            String str = get(KEY_AUTO_EXPOSURE_LOCK_SUPPORTED);
            return TRUE.equals(str);
        }

        public void setAutoWhiteBalanceLock(boolean toggle) {
            set(KEY_AUTO_WHITEBALANCE_LOCK, toggle ? TRUE : FALSE);
        }

        public boolean getAutoWhiteBalanceLock() {
            String str = get(KEY_AUTO_WHITEBALANCE_LOCK);
            return TRUE.equals(str);
        }

        public boolean isAutoWhiteBalanceLockSupported() {
            String str = get(KEY_AUTO_WHITEBALANCE_LOCK_SUPPORTED);
            return TRUE.equals(str);
        }

        public int getZoom() {
            return getInt(KEY_ZOOM, 0);
        }

        public void setZoom(int value) {
            set(KEY_ZOOM, value);
        }

        public boolean isZoomSupported() {
            String str = get(KEY_ZOOM_SUPPORTED);
            return TRUE.equals(str);
        }

        public int getMaxZoom() {
            return getInt(KEY_MAX_ZOOM, 0);
        }

        public List<Integer> getZoomRatios() {
            return splitInt(get(KEY_ZOOM_RATIOS));
        }

        public boolean isSmoothZoomSupported() {
            String str = get(KEY_SMOOTH_ZOOM_SUPPORTED);
            return TRUE.equals(str);
        }

        public void getFocusDistances(float[] output) {
            if (output == null || output.length != 3) {
                throw new IllegalArgumentException(
                        "output must be a float array with three elements.");
            }
            splitFloat(get(KEY_FOCUS_DISTANCES), output);
        }

        public int getMaxNumFocusAreas() {
            return getInt(KEY_MAX_NUM_FOCUS_AREAS, 0);
        }

        public List<Area> getFocusAreas() {
            return splitArea(get(KEY_FOCUS_AREAS));
        }

        public void setFocusAreas(List<Area> focusAreas) {
            set(KEY_FOCUS_AREAS, focusAreas);
        }

        public int getMaxNumMeteringAreas() {
            return getInt(KEY_MAX_NUM_METERING_AREAS, 0);
        }

        public List<Area> getMeteringAreas() {
            return splitArea(get(KEY_METERING_AREAS));
        }

        public void setMeteringAreas(List<Area> meteringAreas) {
            set(KEY_METERING_AREAS, meteringAreas);
        }

        public int getMaxNumDetectedFaces() {
            return getInt(KEY_MAX_NUM_DETECTED_FACES_HW, 0);
        }

        public void setRecordingHint(boolean hint) {
            set(KEY_RECORDING_HINT, hint ? TRUE : FALSE);
        }

        public boolean isVideoSnapshotSupported() {
            String str = get(KEY_VIDEO_SNAPSHOT_SUPPORTED);
            return TRUE.equals(str);
        }

        public void setVideoStabilization(boolean toggle) {
            set(KEY_VIDEO_STABILIZATION, toggle ? TRUE : FALSE);
        }

        public boolean getVideoStabilization() {
            String str = get(KEY_VIDEO_STABILIZATION);
            return TRUE.equals(str);
        }

        public boolean isVideoStabilizationSupported() {
            String str = get(KEY_VIDEO_STABILIZATION_SUPPORTED);
            return TRUE.equals(str);
        }

        // Splits a comma delimited string to an ArrayList of String.
        // Return null if the passing string is null or the size is 0.
        private ArrayList<String> split(String str) {
            if (str == null) return null;

            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(',');
            splitter.setString(str);
            ArrayList<String> substrings = new ArrayList<String>();
            for (String s : splitter) {
                substrings.add(s);
            }
            return substrings;
        }

        // Splits a comma delimited string to an ArrayList of Integer.
        // Return null if the passing string is null or the size is 0.
        private ArrayList<Integer> splitInt(String str) {
            if (str == null) return null;

            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(',');
            splitter.setString(str);
            ArrayList<Integer> substrings = new ArrayList<Integer>();
            for (String s : splitter) {
                substrings.add(Integer.parseInt(s));
            }
            if (substrings.size() == 0) return null;
            return substrings;
        }

        private void splitInt(String str, int[] output) {
            if (str == null) return;

            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(',');
            splitter.setString(str);
            int index = 0;
            for (String s : splitter) {
                output[index++] = Integer.parseInt(s);
            }
        }

        // Splits a comma delimited string to an ArrayList of Float.
        private void splitFloat(String str, float[] output) {
            if (str == null) return;

            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(',');
            splitter.setString(str);
            int index = 0;
            for (String s : splitter) {
                output[index++] = Float.parseFloat(s);
            }
        }

        // Returns the value of a float parameter.
        private float getFloat(String key, float defaultValue) {
            try {
                return Float.parseFloat(mMap.get(key));
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }

        // Returns the value of a integer parameter.
        private int getInt(String key, int defaultValue) {
            try {
                return Integer.parseInt(mMap.get(key));
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }

        // Splits a comma delimited string to an ArrayList of Size.
        // Return null if the passing string is null or the size is 0.
        private ArrayList<Size> splitSize(String str) {
            if (str == null) return null;

            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(',');
            splitter.setString(str);
            ArrayList<Size> sizeList = new ArrayList<Size>();
            for (String s : splitter) {
                Size size = strToSize(s);
                if (size != null) sizeList.add(size);
            }
            if (sizeList.size() == 0) return null;
            return sizeList;
        }

        // Parses a string (ex: "480x320") to Size object.
        // Return null if the passing string is null.
        private Size strToSize(String str) {
            if (str == null) return null;

            int pos = str.indexOf('x');
            if (pos != -1) {
                String width = str.substring(0, pos);
                String height = str.substring(pos + 1);
                return new Size(Integer.parseInt(width),
                                Integer.parseInt(height));
            }
            Log.e(TAG, "Invalid size parameter string=" + str);
            return null;
        }

        // Splits a comma delimited string to an ArrayList of int array.
        // Example string: "(10000,26623),(10000,30000)". Return null if the
        // passing string is null or the size is 0.
        private ArrayList<int[]> splitRange(String str) {
            if (str == null || str.charAt(0) != '('
                    || str.charAt(str.length() - 1) != ')') {
                Log.e(TAG, "Invalid range list string=" + str);
                return null;
            }

            ArrayList<int[]> rangeList = new ArrayList<int[]>();
            int endIndex, fromIndex = 1;
            do {
                int[] range = new int[2];
                endIndex = str.indexOf("),(", fromIndex);
                if (endIndex == -1) endIndex = str.length() - 1;
                splitInt(str.substring(fromIndex, endIndex), range);
                rangeList.add(range);
                fromIndex = endIndex + 3;
            } while (endIndex != str.length() - 1);

            if (rangeList.size() == 0) return null;
            return rangeList;
        }

        // Splits a comma delimited string to an ArrayList of Area objects.
        // Example string: "(-10,-10,0,0,300),(0,0,10,10,700)". Return null if
        // the passing string is null or the size is 0 or (0,0,0,0,0).
        private ArrayList<Area> splitArea(String str) {
            if (str == null || str.charAt(0) != '('
                    || str.charAt(str.length() - 1) != ')') {
                Log.e(TAG, "Invalid area string=" + str);
                return null;
            }

            ArrayList<Area> result = new ArrayList<Area>();
            int endIndex, fromIndex = 1;
            int[] array = new int[5];
            do {
                endIndex = str.indexOf("),(", fromIndex);
                if (endIndex == -1) endIndex = str.length() - 1;
                splitInt(str.substring(fromIndex, endIndex), array);
                Rect rect = new Rect(array[0], array[1], array[2], array[3]);
                result.add(new Area(rect, array[4]));
                fromIndex = endIndex + 3;
            } while (endIndex != str.length() - 1);

            if (result.size() == 0) return null;

            if (result.size() == 1) {
                Area area = result.get(0);
                Rect rect = area.rect;
                if (rect.left == 0 && rect.top == 0 && rect.right == 0
                        && rect.bottom == 0 && area.weight == 0) {
                    return null;
                }
            }

            return result;
        }

        private boolean same(String s1, String s2) {
            if (s1 == null && s2 == null) return true;
            if (s1 != null && s1.equals(s2)) return true;
            return false;
        }
    };
}

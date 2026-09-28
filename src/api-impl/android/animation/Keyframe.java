package android.animation;

public class Keyframe {
	private float fraction;
	private Object value;

	public static Keyframe ofFloat(float fraction) {
		Keyframe kf = new Keyframe();
		kf.setFraction(fraction);
		return kf;
	}

	public static Keyframe ofFloat(float fraction, float value) {
		Keyframe kf = ofFloat(fraction);
		kf.setValue(value);
		return kf;
	}

	public float getFraction() {
		return fraction;
	}

	public void setFraction(float fraction) {
		this.fraction = fraction;
	}

	public boolean hasValue() {
		return value != null;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public java.lang.Class getType() { return null; } // ATL-autostub
	public static android.animation.Keyframe ofInt(float arg0) { return null; } // ATL-autostub
	public static android.animation.Keyframe ofInt(float arg0, int arg1) { return null; } // ATL-autostub
	public static android.animation.Keyframe ofObject(float arg0) { return null; } // ATL-autostub
	public void setInterpolator(android.animation.TimeInterpolator arg0) {} // ATL-autostub
}

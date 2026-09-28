package android.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/* Transitions are not animated in ATL: the scene change happens immediately. */
public class Transition implements Cloneable {
	public static final int MATCH_INSTANCE = 1;
	public static final int MATCH_NAME = 2;
	public static final int MATCH_ID = 3;
	public static final int MATCH_ITEM_ID = 4;

	private long duration = -1;
	private long start_delay = -1;
	private TimeInterpolator interpolator;
	private String name = getClass().getName();
	private EpicenterCallback epicenter_callback;
	private PathMotion path_motion = PathMotion.STRAIGHT_PATH_MOTION;
	private ArrayList<Integer> target_ids = new ArrayList<Integer>();
	private ArrayList<View> targets = new ArrayList<View>();
	private ArrayList<String> target_names = new ArrayList<String>();
	private ArrayList<Class> target_types = new ArrayList<Class>();

	public interface TransitionListener {
		default void onTransitionStart(Transition transition) {}
		default void onTransitionEnd(Transition transition) {}
		default void onTransitionCancel(Transition transition) {}
		default void onTransitionPause(Transition transition) {}
		default void onTransitionResume(Transition transition) {}
	}

	public static abstract class EpicenterCallback {
		public abstract Rect onGetEpicenter(Transition transition);
	}

	public Transition() {}

	public Transition(Context context, AttributeSet attrs) {}

	public void captureStartValues(TransitionValues transitionValues) {}

	public void captureEndValues(TransitionValues transitionValues) {}

	public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
		return null;
	}

	public String[] getTransitionProperties() {
		return null;
	}

	public boolean isTransitionRequired(TransitionValues startValues, TransitionValues endValues) {
		return false;
	}

	public TransitionValues getTransitionValues(View view, boolean start) {
		return null;
	}

	public Transition setDuration(long duration) {
		this.duration = duration;
		return this;
	}

	public long getDuration() {
		return duration;
	}

	public Transition setStartDelay(long startDelay) {
		this.start_delay = startDelay;
		return this;
	}

	public long getStartDelay() {
		return start_delay;
	}

	public Transition setInterpolator(TimeInterpolator interpolator) {
		this.interpolator = interpolator;
		return this;
	}

	public TimeInterpolator getInterpolator() {
		return interpolator;
	}

	public void setMatchOrder(int... matches) {}

	public Transition addListener(TransitionListener listener) {
		return this;
	}

	public Transition removeListener(TransitionListener listener) {
		return this;
	}

	public Transition addTarget(View target) {
		targets.add(target);
		return this;
	}

	public Transition addTarget(int targetId) {
		target_ids.add(targetId);
		return this;
	}

	public Transition addTarget(String targetName) {
		target_names.add(targetName);
		return this;
	}

	public Transition addTarget(Class targetType) {
		target_types.add(targetType);
		return this;
	}

	public Transition removeTarget(View target) {
		targets.remove(target);
		return this;
	}

	public Transition removeTarget(int targetId) {
		target_ids.remove((Integer)targetId);
		return this;
	}

	public Transition removeTarget(String targetName) {
		target_names.remove(targetName);
		return this;
	}

	public Transition removeTarget(Class target) {
		target_types.remove(target);
		return this;
	}

	public List<Integer> getTargetIds() {
		return target_ids;
	}

	public List<View> getTargets() {
		return targets;
	}

	public List<String> getTargetNames() {
		return target_names;
	}

	public List<Class> getTargetTypes() {
		return target_types;
	}

	public Transition excludeTarget(int targetId, boolean exclude) {
		return this;
	}

	public Transition excludeTarget(String targetName, boolean exclude) {
		return this;
	}

	public Transition excludeTarget(View target, boolean exclude) {
		return this;
	}

	public Transition excludeTarget(Class type, boolean exclude) {
		return this;
	}

	public Transition excludeChildren(int targetId, boolean exclude) {
		return this;
	}

	public Transition excludeChildren(View target, boolean exclude) {
		return this;
	}

	public Transition excludeChildren(Class type, boolean exclude) {
		return this;
	}

	public void setEpicenterCallback(EpicenterCallback epicenterCallback) {
		this.epicenter_callback = epicenterCallback;
	}

	public EpicenterCallback getEpicenterCallback() {
		return epicenter_callback;
	}

	public Rect getEpicenter() {
		return epicenter_callback != null ? epicenter_callback.onGetEpicenter(this) : null;
	}

	public void setPathMotion(PathMotion pathMotion) {
		this.path_motion = pathMotion != null ? pathMotion : PathMotion.STRAIGHT_PATH_MOTION;
	}

	public PathMotion getPathMotion() {
		return path_motion;
	}

	public void setPropagation(Object transitionPropagation) {}

	public String getName() {
		return name;
	}

	public boolean canRemoveViews() {
		return false;
	}

	@Override
	public Transition clone() {
		try {
			Transition clone = (Transition)super.clone();
			clone.target_ids = new ArrayList<Integer>(target_ids);
			clone.targets = new ArrayList<View>(targets);
			clone.target_names = new ArrayList<String>(target_names);
			clone.target_types = new ArrayList<Class>(target_types);
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}
}

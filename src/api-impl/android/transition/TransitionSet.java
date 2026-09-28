package android.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

public class TransitionSet extends Transition {
	public static final int ORDERING_TOGETHER = 0;
	public static final int ORDERING_SEQUENTIAL = 1;

	private ArrayList<Transition> transitions = new ArrayList<Transition>();
	private int ordering = ORDERING_TOGETHER;

	public TransitionSet() {}

	public TransitionSet(Context context, AttributeSet attrs) {}

	public TransitionSet setOrdering(int ordering) {
		this.ordering = ordering;
		return this;
	}

	public int getOrdering() {
		return ordering;
	}

	public TransitionSet addTransition(Transition transition) {
		if (transition != null)
			transitions.add(transition);
		return this;
	}

	public int getTransitionCount() {
		return transitions.size();
	}

	public Transition getTransitionAt(int index) {
		return (index < 0 || index >= transitions.size()) ? null : transitions.get(index);
	}

	public TransitionSet removeTransition(Transition transition) {
		transitions.remove(transition);
		return this;
	}

	@Override
	public TransitionSet setDuration(long duration) {
		super.setDuration(duration);
		return this;
	}

	@Override
	public TransitionSet setStartDelay(long startDelay) {
		super.setStartDelay(startDelay);
		return this;
	}

	@Override
	public TransitionSet setInterpolator(TimeInterpolator interpolator) {
		super.setInterpolator(interpolator);
		return this;
	}

	@Override
	public TransitionSet addListener(TransitionListener listener) {
		return this;
	}

	@Override
	public TransitionSet removeListener(TransitionListener listener) {
		return this;
	}

	@Override
	public TransitionSet addTarget(View target) {
		super.addTarget(target);
		return this;
	}

	@Override
	public TransitionSet addTarget(int targetId) {
		super.addTarget(targetId);
		return this;
	}

	@Override
	public TransitionSet addTarget(String targetName) {
		super.addTarget(targetName);
		return this;
	}

	@Override
	public TransitionSet addTarget(Class targetType) {
		super.addTarget(targetType);
		return this;
	}

	@Override
	public TransitionSet removeTarget(View target) {
		super.removeTarget(target);
		return this;
	}

	@Override
	public TransitionSet removeTarget(int targetId) {
		super.removeTarget(targetId);
		return this;
	}

	@Override
	public TransitionSet removeTarget(String targetName) {
		super.removeTarget(targetName);
		return this;
	}

	@Override
	public TransitionSet removeTarget(Class target) {
		super.removeTarget(target);
		return this;
	}

	@Override
	public TransitionSet clone() {
		TransitionSet clone = (TransitionSet)super.clone();
		clone.transitions = new ArrayList<Transition>();
		for (Transition transition : transitions)
			clone.transitions.add(transition.clone());
		return clone;
	}
}

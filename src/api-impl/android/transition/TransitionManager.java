package android.transition;

import android.view.ViewGroup;

/* No animations: changes made after beginDelayedTransition() just apply immediately. */
public class TransitionManager {
	public TransitionManager() {}

	public static void beginDelayedTransition(ViewGroup sceneRoot) {}

	public static void beginDelayedTransition(ViewGroup sceneRoot, Transition transition) {}

	public static void endTransitions(ViewGroup sceneRoot) {}
}

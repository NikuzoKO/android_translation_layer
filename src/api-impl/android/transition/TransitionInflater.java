package android.transition;

import android.content.Context;
import android.view.ViewGroup;

public class TransitionInflater {
	private Context context;

	private TransitionInflater(Context context) {
		this.context = context;
	}

	public static TransitionInflater from(Context context) {
		return new TransitionInflater(context);
	}

	/* transitions aren't animated, so the XML isn't parsed: any placeholder does */
	public Transition inflateTransition(int resource) {
		return new AutoTransition();
	}

	public TransitionManager inflateTransitionManager(int resource, ViewGroup sceneRoot) {
		return new TransitionManager();
	}
}

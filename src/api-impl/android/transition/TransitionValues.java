package android.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransitionValues {
	public View view;
	public final Map<String, Object> values = new HashMap<String, Object>();
	final ArrayList<Transition> targetedTransitions = new ArrayList<Transition>();

	public TransitionValues() {}

	public TransitionValues(View view) {
		this.view = view;
	}
}

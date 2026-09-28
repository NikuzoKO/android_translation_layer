package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class Toolbar extends View {

	public Toolbar(Context context) {
		super(context);
	}

	public Toolbar(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public void setNavigationIcon(int resId) {}

	public void setNavigationOnClickListener(OnClickListener listener) {}

	public int getTitleMarginBottom() { return 0; } // ATL-autostub
	public int getTitleMarginEnd() { return 0; } // ATL-autostub
	public int getTitleMarginStart() { return 0; } // ATL-autostub
	public int getTitleMarginTop() { return 0; } // ATL-autostub
	public java.lang.CharSequence getTitle() { return null; } // ATL-autostub
}

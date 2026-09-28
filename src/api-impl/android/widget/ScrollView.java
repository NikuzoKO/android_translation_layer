package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ScrollView extends ViewGroup {
	private boolean fill_viewport = false;

	public ScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ScrollView, 0, 0);
		fill_viewport = a.getBoolean(com.android.internal.R.styleable.ScrollView_fillViewport, false);
		a.recycle();
	}

	public ScrollView(Context context) {
		super(context);
	}

	@Override
	protected native long native_constructor(Context context, AttributeSet attrs);
	@Override
	protected native void native_addView(long widget, long child, int index, LayoutParams params);
	@Override
	protected native void native_removeView(long widget, long child);

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = 0;
		int height = 0;
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			LayoutParams lp = child.getLayoutParams();
			int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			width = child.getMeasuredWidth();
			height = child.getMeasuredHeight();
			// fillViewport: stretch content shorter than the viewport to its height,
			// so match_constraint/weighted content gets space (as in AOSP)
			if (fill_viewport && MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED) {
				int viewport = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
				if (height < viewport) {
					child.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(viewport, MeasureSpec.EXACTLY));
					width = child.getMeasuredWidth();
					height = child.getMeasuredHeight();
				}
			}
		}
		setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
	}

	public void setFillViewport(boolean fillViewport) {
		if (fill_viewport != fillViewport) {
			fill_viewport = fillViewport;
			requestLayout();
		}
	}

	public boolean isFillViewport() {
		return fill_viewport;
	}

	public boolean fullScroll(int direction) {
		return true;
	}

	public int getMaxScrollAmount() { return 0; } // ATL-autostub
	public void fling(int arg0) {} // ATL-autostub
	public void smoothScrollBy(int arg0, int arg1) {} // ATL-autostub
	public void smoothScrollTo(int arg0, int arg1) {} // ATL-autostub
}

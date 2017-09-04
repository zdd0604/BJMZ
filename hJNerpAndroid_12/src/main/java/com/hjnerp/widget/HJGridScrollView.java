package com.hjnerp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;

public class HJGridScrollView extends HorizontalScrollView {

	public HJGridScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJGridScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (getParent() == null)
			return super.onTouchEvent(ev);
		ViewParent view = getParent();
		while (view != null) {
			if (view instanceof HJGridLayout)
				break;
			view = view.getParent();
		}
		if (view instanceof HJGridLayout)
			((HJGridLayout) view).mTouchView = this;
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		if (getParent() == null)
			return;
		ViewParent view = getParent();
		while (view != null) {
			if (view instanceof HJGridLayout)
				break;
			view = view.getParent();
		}
		if (view instanceof HJGridLayout) {
			if (((HJGridLayout) view).mTouchView == this) {
				((HJGridLayout) view).onScrollChanged(l, t, oldl, oldt);
			} else {
				super.onScrollChanged(l, t, oldl, oldt);
			}
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

}

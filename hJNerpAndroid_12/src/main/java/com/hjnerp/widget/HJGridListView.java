package com.hjnerp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class HJGridListView extends ListView {

	public static final String TAG = "HJGridListView";

	public HJGridListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HJGridListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJGridListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	ScrollView parentScrollView;

	public ScrollView getParentScrollView() {
		return parentScrollView;
	}

	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			switch (HJGridLayout.listViewSize) {
			case ADAPTIVE:
				setParentScrollAble(true);
				break;
			case FULL_SCREEN:
			case SPECIFY:
				setParentScrollAble(false);
				break;
			}
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:

		case MotionEvent.ACTION_CANCEL:
			switch (HJGridLayout.listViewSize) {
			case ADAPTIVE:
				setParentScrollAble(false);
				break;
			case FULL_SCREEN:
			case SPECIFY:
				setParentScrollAble(true);
				break;
			}
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * @param flag
	 */
	private void setParentScrollAble(boolean flag) {

		parentScrollView.requestDisallowInterceptTouchEvent(!flag);
	}

}

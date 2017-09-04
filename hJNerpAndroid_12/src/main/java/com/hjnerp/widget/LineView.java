package com.hjnerp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class LineView extends View {

	private int x = -1;
	private int color;

	public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LineView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void layout(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (x >= 0) {
			super.layout(x, t, x + 5, b);
		} else {
			super.layout(l, t, r, b);
		}
		if(color != 0){
			setBackgroundColor(color);
		}
	}


	public void setX(int x) {
		this.x = x;
	}
	
	public void setColors(int color){
		this.color = color;
	}

}

package com.hjnerp.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

	private int x = 0;
	private int color;

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setLongClickable(true);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLongClickable(true);
	}

	public CircleImageView(Context context) {
		super(context);
		setLongClickable(true); // 保证imageview可以滑动
	}

	@Override
	public void layout(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (x > 0) {
			l = x;
			DisplayMetrics outMetrics = new DisplayMetrics();
			((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics );
			float dip = outMetrics.density;
			r = (int) (x+20*dip);
		} 
		super.layout(l, t, r, b);
		
		if (color != 0) {
			GradientDrawable drawable = (GradientDrawable) getBackground();
			drawable.setStroke(1, color);
		}
		//设置选择区域的范围
		 View  view = (View) getParent();
		 Rect bound = new Rect();
		 bound.top = t-getHeight();
		 bound.bottom = b+getHeight();
		 bound.left = l-getWidth();
		 bound.right =r+getWidth();
		 view.setTouchDelegate(new TouchDelegate(bound, this));
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setColors(int color) {
		// TODO Auto-generated method stub
		this.color = color;
	}

}

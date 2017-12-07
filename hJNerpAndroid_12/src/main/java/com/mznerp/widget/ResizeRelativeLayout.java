package com.mznerp.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ResizeRelativeLayout extends RelativeLayout
{
	private ArrayList<OnHeightSmallerListener> eventListeners = new ArrayList<OnHeightSmallerListener>();
	
	public ResizeRelativeLayout(Context context)
	{
		super(context);
	}
	
	public ResizeRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		int width = getWidth();
		if(width != 0 && width == w && width == oldw && h != 0 && oldh != 0)
		{
			for(OnHeightSmallerListener l : eventListeners)
			{
				l.onHeightSmaller(this, oldh, w);
			}
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void setOnHeightSmallerListener(OnHeightSmallerListener l)
	{
		eventListeners.add(l);
	}
	
	public void removeOnHeightSmallerListener(OnHeightSmallerListener l)
	{
		eventListeners.remove(l);
	}
	
	public interface OnHeightSmallerListener
	{
		public void onHeightSmaller(ResizeRelativeLayout layout, int oldh, int h);
	}
}

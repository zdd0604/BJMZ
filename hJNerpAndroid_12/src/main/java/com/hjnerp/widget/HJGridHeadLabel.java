package com.hjnerp.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.hjnerp.business.view.WidgetAttribute;
import com.hjnerpandroid.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class HJGridHeadLabel extends TextView   {
	private static String TAG = "HJGridHeadLabel";

	public HJGridHeadLabel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJGridHeadLabel(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJGridHeadLabel(Context context) {
		super(context);
		initView();
	}
	private void initView() {
		LayoutParams ll = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		setLayoutParams(ll);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public void setAttribute(WidgetAttribute item) {
		// 设置字体大小
		setHJLabelSize(item);
	 
		this.setGravity(Gravity.CENTER);
		// 设置加粗
		Paint paint = getPaint();
		paint.setFakeBoldText(true);
		
		// 设置宽度大小
		if (item.width > 0) {
			Point outSize = new Point();
			((Activity) getContext()).getWindowManager().getDefaultDisplay()
					.getSize(outSize);
			LayoutParams ll = getLayoutParams();
			ll.width = (int) (outSize.x * item.width);
		}
		this.setTextColor(  Color.WHITE );
	 

	} 
 
	// 设置字体大小
	public void setHJLabelSize(WidgetAttribute item){
		if (getContext().getResources().getString(R.string.text_size_large).equalsIgnoreCase(item.fontsize)) {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeLarge));
		} else if (getContext().getResources().getString(R.string.text_size_medium).equalsIgnoreCase(item.fontsize)) {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeMedium));
		} else {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeSmall));
		}
	}
	
	 

}

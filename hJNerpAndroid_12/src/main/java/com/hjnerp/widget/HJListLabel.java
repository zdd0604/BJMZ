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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.business.view.WidgetName;
import com.hjnerpandroid.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class HJListLabel extends TextView    {
	 
 
	public HJListLabel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJListLabel(Context context, AttributeSet attrs) {
		super(context, attrs); 
		initView();
	}

	public HJListLabel(Context context) {
		super(context); 
		initView();
	} 
	private void initView() {
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);  
        ll.setMargins(5, 0, 5, 0);
		setLayoutParams(ll);   
		
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public void setAttribute(WidgetClass item) {
		// 设置字体大小
		setHJLabelSize(item);
		// 设置居中
		setHJLabelGravity(item); 
		setHJLabelVisible(item);
		// 设置加粗
		Paint paint = getPaint();
		paint.setFakeBoldText(item.attribute.bold);
		// 设置宽度大小
		if (item.attribute.width > 0) {
			Point outSize = new Point();
			((Activity) getContext()).getWindowManager().getDefaultDisplay()
					.getSize(outSize);
			LayoutParams ll = getLayoutParams();
			ll.width = (int) (outSize.x * item.attribute.width);
		}
		// 设置是否换行
		setSingleLine(item.attribute.singleline);
		 
 		setHJLabelColor(item); 

	}
	
	//设置颜色
		public void setHJLabelVisible(WidgetClass item){
			if ( !item.attribute.visible   )
			{
			  this.setVisibility(GONE);
			}
		}
		
	//设置颜色
	public void setHJLabelColor(WidgetClass item){
		if ( item.attribute.textcolor != null && !"".equalsIgnoreCase(item.attribute.textcolor ))
		{
			setTextColor(Color.parseColor( item.attribute.textcolor ));
		}
	}
	
	public void setHJLabelColor(int color){
		setTextColor(color);
	}
	//设置位置
	public void setHJLabelGravity(WidgetClass item){
			if(WidgetName.HJ_GRID.equalsIgnoreCase(item.widgetType)){
				setGravity( Gravity.CENTER_VERTICAL|Gravity.CENTER);
			}else{
				setGravity( Gravity.CENTER_VERTICAL|Gravity.LEFT);
			}
	}
	
 
	// 设置字体大小
	public void setHJLabelSize(WidgetClass item){
		if (getContext().getResources().getString(R.string.text_size_large).equalsIgnoreCase(item.attribute.fontsize)) {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeLarge));
		} else if (getContext().getResources().getString(R.string.text_size_medium).equalsIgnoreCase(item.attribute.fontsize)) {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeMedium));
		} else {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeSmall));
		}
	}
	
	public void setDataResource(String s) {
		setText(s);
	}

}
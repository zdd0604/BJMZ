package com.hjnerp.widget;

import java.text.DecimalFormat;

import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View; 
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

//@SuppressLint("NewApi")
public class HJGridTextView extends TextView  {
	Ctlm1347 ctlm1347  = null;
	WidgetClass items ;
	String data;
	int[]position;
	public HJGridTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HJGridTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJGridTextView(Context context,int[]position) {
		super(context);
		this.position = position;
		initView();
	}
	public HJGridTextView(Context context){
		super(context);
		initView();
	}
	
	public int[] getPosition(){
		return position;
	}
	private void initView() {
		LayoutParams ll = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(ll);
	}

	public void setAttribute(WidgetClass item) {
		items =item;  
		
		// 设置字体大小
		if (getContext().getResources().getString(R.string.text_size_large).equalsIgnoreCase(item.attribute.fontsize)) {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeLarge));
		} else if (getContext().getResources().getString(R.string.text_size_medium).equalsIgnoreCase(item.attribute.fontsize)) {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeMedium));
		} else {
			setTextSize(getContext().getResources().getInteger(R.integer.TextSizeSmall));
		}
		// 设置居中
		if ("left".equalsIgnoreCase(item.attribute.alignment)    )
		{
			setGravity(Gravity.LEFT);
		}
		if ( "right".equalsIgnoreCase(item.attribute.alignment) )
		{
			setGravity(Gravity.RIGHT); 
		}
		if ( "center".equalsIgnoreCase(item.attribute.alignment) )
		{
			setGravity(Gravity.CENTER); 
		}
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
		/**
		 * 设置显示还是隐藏
		 * @author haijian
		 */
       
		if(item.attribute.visible){
			setVisibility(View.VISIBLE);
		}else{
			setVisibility(View.GONE);
		}
	 
	 
		
	}

 
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (StringUtil.isNullOrEmpty( msg) ) return ;
		String   format;
		String valuetype ;
		String formatted ;
		if ( items.attribute.valuetype != null)
		{ 
			valuetype = items.attribute.valuetype.toString();
		     format = items.attribute.format.toString();
		  
			if ( "decimal".equalsIgnoreCase(valuetype) && !StringUtil.isNullOrEmpty( format) ) 
			{  
				DecimalFormat dformat = new DecimalFormat(format);
				 formatted = dformat.format(  Double.parseDouble(msg )  );  
			}
			else
			{
				formatted = msg;
			}
			 this.setText( formatted );  
		}
		else {
		   this.setText( msg );  
		} 
	}

 

 
}

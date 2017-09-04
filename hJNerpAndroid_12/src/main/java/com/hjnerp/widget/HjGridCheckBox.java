package com.hjnerp.widget;

 
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;  
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

//@SuppressLint("NewApi")
public class HjGridCheckBox extends ImageView {

	Ctlm1347 ctlm1347 ;  //用于记录当前数据
	WidgetClass items ;
	String data;
	public HjGridCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HjGridCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HjGridCheckBox(Context context) {
		super(context);
		initView();
	}
	 
	 
	private void initView()
	{
		data = "N";
		LayoutParams ll = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(ll);
		setScaleType(ImageView.ScaleType.CENTER);
//		this.setBackground(getResources().getDrawable(
//				R.drawable.selecter_unselected_icon));
//		this.setImageDrawable(getResources().getDrawable(
//				R.drawable.selecter_unselected_icon));
		this.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("Y".equalsIgnoreCase(data))
				{
					data = "N";
//					setBackground(getResources().getDrawable(
//							R.drawable.selecter_unselected_icon));
					setImageDrawable(getResources().getDrawable(
							R.drawable.selecter_unselected_icon));
					changCtlm1347();
				}
				else
				{
					data = "Y";
//					setBackground(getResources().getDrawable(
//							R.drawable.selecter_selected_icon));
					setImageDrawable(getResources().getDrawable(
							R.drawable.selecter_selected_icon));
					changCtlm1347();
				}
			}
		});
		
	}

	public void setValue(String msg)
	{
		if ("Y".equalsIgnoreCase( msg))
		{ 
//			setBackground(getResources().getDrawable(R.drawable.selecter_selected_icon));
			setBackgroundDrawable(getResources().getDrawable(R.drawable.selecter_selected_icon));
			data = "Y";
			changCtlm1347();
		}
		else
		{ 
//			setBackground(getResources().getDrawable(R.drawable.selecter_unselected_icon));
			setBackgroundDrawable(getResources().getDrawable(R.drawable.selecter_unselected_icon));
			data= "N";
			changCtlm1347();
		}
		
	}
	
	public void setAttribute(WidgetClass item) {
		items =item;
	 
 
		// 设置宽度大小
		if (item.attribute.width > 0) {
			 
			Point outSize = new Point();
			((Activity) getContext()).getWindowManager().getDefaultDisplay()
					.getSize(outSize);
			LayoutParams ll = getLayoutParams(); 
			ll.width = (int) (outSize.x * item.attribute.width);
		}
		if(item.attribute.visible){
			setVisibility(View.VISIBLE);
		}else{
			setVisibility(View.GONE);
		}
	}
	
	public void changCtlm1347()
	{
		if (ctlm1347 == null) return ;
		
		String jsonvalues;
        jsonvalues = ctlm1347.getvar_Json();
        if ( !StringUtil.isNullOrEmpty(jsonvalues) ) 
        {   
        	Map<String, Object> map = null;  
		    Gson gson = new Gson(); 
		    map = (Map<String, Object>)gson.fromJson(jsonvalues, Object.class); 
			if (map != null && !map.isEmpty() )
			{       map.put(items.attribute.field , data); 
				    ctlm1347.setvar_Json(gson.toJson(map ));
			}  
			
        }
        ctlm1347.setVar_data(items.attribute.dbfield , data); 
        
	}
	
	@SuppressWarnings("unchecked")
	public void setCtlm1347(Ctlm1347 ctlm1347a)
	{
		ctlm1347 = ctlm1347a;
		
		String jsonvalues;
        jsonvalues = ctlm1347.getvar_Json();
        if ( !StringUtil.isNullOrEmpty(jsonvalues) ) 
        {   
        	Map<String, Object> map = null;  
		    Gson gson = new Gson(); 
		    map = (Map<String, Object>)gson.fromJson(jsonvalues, Object.class); 
			if (map != null && !map.isEmpty() )
			{   
				data =    (String) map.get( items.attribute.field );
				if ("Y".equalsIgnoreCase(data))
				{
					
					setImageDrawable(getResources().getDrawable(
							R.drawable.selecter_selected_icon));
				}
				else
				{
					this.setImageDrawable(getResources().getDrawable(
		    				R.drawable.selecter_unselected_icon));
				} 
			}  
			else
			{
				this.setImageDrawable(getResources().getDrawable(
	    				R.drawable.selecter_unselected_icon));
			}
        }
        else
        {
        	this.setImageDrawable(getResources().getDrawable(
    				R.drawable.selecter_unselected_icon));
        }
	}
 
}

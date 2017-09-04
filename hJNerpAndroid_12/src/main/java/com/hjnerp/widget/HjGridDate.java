package com.hjnerp.widget;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

public class HjGridDate extends TextView implements OnClickListener{
	Ctlm1347 ctlm1347  = null;
	WidgetClass items ;
	String data;
	int[]position;
	private final static int DATE_DIALOG = 0;
    private final static int TIME_DIALOG = 1;
    private Calendar c = null;
    private Context context;
	
	public HjGridDate(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}
	public HjGridDate(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public HjGridDate(Context context,int[]position) {
		super(context);
		this.context = context;
		initView();
	}
	public HjGridDate(Context context){
		super(context);
		this.context = context;
		initView();
	}
	
	
	
	
	
	public int[] getPosition(){
		return position;
	}
	private void initView() {
		LayoutParams ll = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(ll);
		setOnClickListener(this);
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
//			if (map != null && !map.isEmpty() )
//			{   
//				data =    (String) map.get( items.attribute.field );
//				if ("Y".equalsIgnoreCase(data))
//				{
//					
//					setImageDrawable(getResources().getDrawable(
//							R.drawable.selecter_selected_icon));
//				}
//				else
//				{
//					this.setImageDrawable(getResources().getDrawable(
//		    				R.drawable.selecter_unselected_icon));
//				} 
//			}  
//			else
//			{
//				this.setImageDrawable(getResources().getDrawable(
//	    				R.drawable.selecter_unselected_icon));
//			}
//        }
//        else
//        {
//        	this.setImageDrawable(getResources().getDrawable(
//    				R.drawable.selecter_unselected_icon));
        }
	}
	
	
	
	@Override
	public void onClick(View v) {
		//选择时间
		onCreateDialog(DATE_DIALOG).show();
	}
	 /**
     * 创建日期及时间选择对话框
     */
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
        case DATE_DIALOG:
            c = Calendar.getInstance();
            dialog = new DatePickerDialog(
                this.context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                    	if(month+1<9){
                    		setText(year + "-0" + (month+1) + "-" + dayOfMonth );
                    	}else{
                    		setText(year + "-" + (month+1) + "-" + dayOfMonth );
                    	}
                    }
                }, 
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
            );
            break;
        case TIME_DIALOG:
            c=Calendar.getInstance();
            dialog=new TimePickerDialog(
                this.context, 
                new TimePickerDialog.OnTimeSetListener(){
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setText("您选择了："+hourOfDay+"时"+minute+"分");
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
            );
            break;
        }
        return dialog;
    }
}

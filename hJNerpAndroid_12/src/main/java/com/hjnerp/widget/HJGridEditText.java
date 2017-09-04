package com.hjnerp.widget;

import java.text.DecimalFormat; 
import java.util.Map;
 
 

import com.google.gson.Gson;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;
 

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point; 
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet; 
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

//@SuppressLint("NewApi")
public class HJGridEditText extends EditText  {
	Ctlm1347 ctlm1347  = null;
	WidgetClass items ;
	String data;
	int[]position;
	public HJGridEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HJGridEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJGridEditText(Context context,int[]position) {
		super(context);
		this.position = position;
		initView();
	}
	public HJGridEditText(Context context){
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
//		this.setOnTouchListener(new OnTouchListener(){ 
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//			}
// 
//		} );
	}

	public void setAttribute(WidgetClass item) {
		items =item;
		int  lengthlimit =200 ; 
		String   format="";
		String valuetype="" ;
		
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
        if ( item.attribute.valuetype != null)
		{ 
          valuetype = item.attribute.valuetype.toString();
          if(item.attribute.format != null)
		  format = item.attribute.format.toString();
		}
		if(StringUtil.isNullOrEmpty(format)  ) format = "";
		if(StringUtil.isNullOrEmpty(valuetype)  ) valuetype = "";
		
//		if  (!item.attribute.singleline )
//		 {
//			
			if  (item.attribute.lengthlimit ==0) 
			{   if  (  "phoneNumber".equalsIgnoreCase(valuetype) ) lengthlimit = 11; 	 
			    if  ( "integer".equalsIgnoreCase(valuetype)  ) lengthlimit = 5;
			    if  ( "decimal".equalsIgnoreCase(valuetype)  ) lengthlimit = 15;  
			    if  ( !"decimal".equalsIgnoreCase(valuetype) &&  !"phoneNumber".equalsIgnoreCase(valuetype) && !"integer".equalsIgnoreCase(valuetype) ) lengthlimit = 500;   
		    }
			else
			{
				lengthlimit=item.attribute.lengthlimit ; 
			} 
//		 }
//		else
//		{
//			if  (item.attribute.lengthlimit ==0) 
//			{lengthlimit =500;			}
//			else
//			{lengthlimit=item.attribute.lengthlimit ;}
//		}
		
		 addTextChangedListener( new  HJEditTextWatcher( this ,lengthlimit ,valuetype, format, callHandler));
		 setOnFocusChangeListener(new OnFocusChangeListenerImp()) ;
		if ("string".equalsIgnoreCase(item.attribute.valuetype ))
		{
			 setInputType(InputType.TYPE_CLASS_TEXT);
		}
		if ("phonenumber".equalsIgnoreCase(item.attribute.valuetype ))
		{
			 setInputType(InputType.TYPE_CLASS_PHONE);
			
		}
		if ("integer".equalsIgnoreCase(item.attribute.valuetype ))
		{
			 setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);  
		}
		if ("password".equalsIgnoreCase(item.attribute.valuetype ))
		{
			 setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); 
		}
		if ("decimal".equalsIgnoreCase(item.attribute.valuetype ))
		{
			 setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);  
			
	 	}  
		if (item.attribute.textcolor != null && !"".equalsIgnoreCase(item.attribute.textcolor ))
		{
			setTextColor(Color.parseColor( item.attribute.textcolor ));
		} 
		/**
		 * @author haijian
		 * 增加显示隐藏功能
		 */
		if( item.attribute.visible  ){
			setVisibility(View.VISIBLE);
		}else{
			setVisibility(View.GONE);
		}
		
	}
	
	@Override
	public void setSelection(int index) { 
		super.setSelection(index); 
	}

	 
	public void setcurSelection( String str )
	{
		 this.setSelection(data.length());
	}

	public void setCtlm1347(Ctlm1347 ctlm1347a)
	{
		ctlm1347 = ctlm1347a;
	}
	
	@SuppressWarnings("unchecked")
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
				 if(Double.parseDouble(msg )<1){
						formatted = "0"+formatted;
					}
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
		changCtlm1347();
	}
 
	private class OnFocusChangeListenerImp implements OnFocusChangeListener{

		@Override
		public void onFocusChange(View arg0, boolean hasFocus) {
			// TODO Auto-generated method stub 
				data =getText().toString();
			
				   if(hasFocus) { 
					   setSelection(data.length()) ;  
					} else { 
						setValue(data);

					}

		}
		
		
	}

	private Handler callHandler = new Handler(){
		
	     public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1: 
					data =getText().toString();
					changCtlm1347();
					break;
				default: 
					break;
				}
				
			};
			
		};
}

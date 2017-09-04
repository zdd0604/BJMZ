package com.hjnerp.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.BusinessData;
import com.hjnerpandroid.R;

@SuppressLint("ResourceAsColor") @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class HJLabel extends LinearLayout   implements HJViewInterface{
	private static String TAG = "HJLabel";

	private Context context;
	private WidgetClass items;
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	private View view ;
	private  TextView content;
	public HJLabel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView( );
	}

	public HJLabel(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context  =context; 
		initView( );
	}

	public HJLabel(Context context) {
		super(context);
		this.context  =context;
	 
	}

	public HJLabel(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo){
		super(context);
		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo; 
	    
		initView( );
	}
	
	private void initView( ) {
	    view = LayoutInflater.from(getContext()).inflate(R.layout.layout_hjlabel, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		addView(view); 
	    content = (TextView) view.findViewById(R.id.hjlabel);
	    content.setText(items.name);  
	}
	  
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public void setAttribute(WidgetClass item) {
		// 设置字体大小
		setHJLabelSize(item);
		// 设置居中
		setHJLabelGravity(item); 
		// 设置加粗
		Paint paint = content.getPaint();
		paint.setFakeBoldText(item.attribute.bold); 
		// 设置是否换行
		content.setSingleLine(item.attribute.singleline); 
		setHJLabelBackground(item); 
		setHJLabelColor(item);
	}
	
	//设置颜色
	public void setHJLabelColor(WidgetClass item){
 
		if ( item.attribute.textcolor != null && !"".equalsIgnoreCase(item.attribute.textcolor ))
		{
			content.setTextColor(Color.parseColor( item.attribute.textcolor ));
		}
	}
	  
	//设置位置
	public void setHJLabelGravity(WidgetClass item){
			if(  "left".equalsIgnoreCase(  item.attribute.alignment)  ){
				content.setGravity( Gravity.CENTER_VERTICAL|Gravity.LEFT);
			} 
			if(  "center".equalsIgnoreCase(  item.attribute.alignment)  ){
				content.setGravity( Gravity.CENTER_VERTICAL|Gravity.CENTER);
			} 
			if(  "right".equalsIgnoreCase(  item.attribute.alignment)  ){
				content.setGravity( Gravity.CENTER_VERTICAL|Gravity.RIGHT);
			}  
	}
	
	//设置HJLabel背景
	public void setHJLabelBackground(WidgetClass item){ 
		
		if ( item.attribute.backgroundcolor != null && !"".equalsIgnoreCase(item.attribute.backgroundcolor ))
		{ 
			view.setBackgroundColor(Color.parseColor( item.attribute.backgroundcolor));
		}
		 
	}
	// 设置字体大小
	public void setHJLabelSize(WidgetClass item){
		
		if (getContext().getResources().getString(R.string.text_size_large).equalsIgnoreCase(item.attribute.fontsize)) {
			content.setTextSize(getContext().getResources().getInteger(R.integer.TextSizeLarge));
		} else if (getContext().getResources().getString(R.string.text_size_medium).equalsIgnoreCase(item.attribute.fontsize)) {
			content.setTextSize(getContext().getResources().getInteger(R.integer.TextSizeMedium));
		} else {
			content.setTextSize(getContext().getResources().getInteger(R.integer.TextSizeSmall));
		}
	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setJesonValue(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getEditable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return "";
	}

 

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataBuild( Boolean flag ,
			BusinessData ctlm1345List) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setLocation() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String setPhoto() {
		// TODO Auto-generated method stub
		 
		return "";
	}

	@Override
	public int saveData(Boolean required) {
		// TODO Auto-generated method stub
		return 0; 
	}

 
 
	@Override
	public void addItem(String billno,String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}

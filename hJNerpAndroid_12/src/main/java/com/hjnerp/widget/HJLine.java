package com.hjnerp.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.BusinessData;
import com.hjnerpandroid.R;

public class HJLine extends View implements HJViewInterface{
	public static final String TAG = "HJLine";
	private WidgetClass items;
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	

	public HJLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public HJLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJLine(Context context) {
		super(context);
		initView();
	}

	public HJLine(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo) {
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		initView();
	}

	private void initView() {
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(lp);
		setHeight(items.attribute.height);
		setBackgroundColor(getResources().getColor(R.color.business_bkg_grey));
	}
	
	public void setHeight(double height){
//		Log.e(TAG,"the height is "+height);
		LayoutParams lp = getLayoutParams();
		lp.height = (int) height;
	}
	
	public void setBackgroundColor(String color){
//		Log.e(TAG,"the background is "+color);
		int col = Color.parseColor(color);
		setBackgroundColor(col);
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
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return null;
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
	public void setDataBuild(Boolean flag ,
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
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}

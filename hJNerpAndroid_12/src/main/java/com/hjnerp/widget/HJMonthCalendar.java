package com.hjnerp.widget;

import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.BusinessData;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CalendarView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class HJMonthCalendar extends CalendarView  implements HJViewInterface{

	public HJMonthCalendar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
	}

	public HJMonthCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	public HJMonthCalendar(Context context) {
		super(context);
		initViews();
	}
	
	private void initViews(){
	}
	
	public void setAttribute(WidgetClass item){
		
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
	public void addItem(String billno,String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}


}

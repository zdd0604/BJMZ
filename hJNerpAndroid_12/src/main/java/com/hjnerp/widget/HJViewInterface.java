package com.hjnerp.widget;

import com.hjnerp.model.BusinessData;

public interface HJViewInterface {
//	public void setValue(ArrayList<Ctlm1347> ctlm1347List);
	public void setValue(String msg);
	public void setValueDefault();
	public void setValue(String row,String column,String value);
	public String getValue(String row,String column);
	public String getValue();
	public void setJesonValue(String values);
 
	public String setLocation();   ///设置定位
	public String setPhoto( ) ; //拍一次照片
	
	public String getDataSource();
	public String getID();
	public boolean getEditable();
	
	public String getRowCount();
	public String getCurrentRow();
	 
	public void  addItem(String  billno,String nodeid,String vlues);
	
	public void setDataSource(String Data);
	public void setDataBuild(Boolean flag, BusinessData ctlm1345List);
	
	public int saveData(Boolean required);
	
	
	public boolean validate();

}

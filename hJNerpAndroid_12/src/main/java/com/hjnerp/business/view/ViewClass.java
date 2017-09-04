package com.hjnerp.business.view;

import java.io.Serializable;
import java.util.List;

public class ViewClass implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String id;
	public boolean returnenable;//当前页面是否可以使用物理返回键
	public List<WidgetClass> list;
	public String dataset ;
	public String condition ;
	public String datasetmode ;
	public String onload;
	public String screendisply;
	public boolean presave ;//控件是否默认保存数据（默认值false），true/null--控件默认保存数据;false--默认不保存数据，需外界调用控件的保存方法才进行数据的保存
	public boolean backsave =false;  //界面返回时保存数据  
	public boolean backdel=false;   // 界面返回=fa时删除数据  
	@Override
	public String toString() {
		return "ViewClass [name=" + name + ", id=" + id + ", returnenable="
				+ returnenable + ", list=" + list + ", dataset=" + dataset
				+ ", condition=" + condition + ", datasetmode=" + datasetmode
				+ ", onload=" + onload + ", screendisply=" + screendisply
				+ ", presave=" + presave + ", backsave=" + backsave
				+ ", backdel=" + backdel + "]";
	}
	
}

package com.hjnerp.business.view;

import java.io.Serializable;
import java.util.List;

public class WidgetClass implements Serializable{
	public String widgetType;// 控件类型
	public String id;// 控件ID
	public String name;// 控件名称
	public List<WidgetClass> HJRadioButtonOption; //表格或者下拉框的属性
	public WidgetAttribute attribute;
	public String defaultValue;  //控件的默认值
	@Override
	public String toString() {
		return "WidgetClass [widgetType=" + widgetType + ", id=" + id
				+ ", name=" + name + ", HJRadioButtonOption="
				+ HJRadioButtonOption + ", attribute=" + attribute
				+ ", defaultValue=" + defaultValue + "]";
	}
}

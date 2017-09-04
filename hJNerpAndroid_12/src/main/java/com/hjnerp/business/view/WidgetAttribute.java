package com.hjnerp.business.view;

import java.io.Serializable;


public class WidgetAttribute implements Serializable{
	
	public boolean editable; //是否可编辑
	public boolean required; // 是否必填项
	public boolean visible; //是否可见
	public double width; //控件宽度
	public double height; //控件高度
	public String valuetype;// 值类型
	public String format;//值格式
	public Object defaultvalue; //默认值
	public int lengthlimit;// 值长度限制
	public boolean singleline = true;//是否多行,默认值不允许多行
	public String backgroundcolor; //背景颜色
	public boolean bold;// 是否加粗
	public String textcolor;//字体颜色
	public boolean visiblescrollbar;//是否显示滚动条
	public String nextview;//跳转界面
	public String fontsize;//字体大小
	public String alignment;//对齐方式
	public String field;
	public String datasource;
	public int locking;  //gridView锁定的列数
	public String backview; //是否能返回
	public boolean visiblename;//listView是否显示名字（true显示为终端名称：小吃店；false显示为小吃店）
	public String dbfield = "var_data1";//对应的存储的数据库的字段，默认放在var_data1
	public String viewparm;
	public boolean sumenable;  ////是否表格里合计
	
	public String columntype ;//表格里的列类型
	public String onclick;   ///点击事件 
	public String onitemchange;//内容变化
	
	public String layouttype;
	public String style;
	public boolean upload;  ////是否上传
	public boolean visibledisclosure = false;//listlayout是否显示右侧箭头
	
	public boolean delrow = false;
	//?????????
	public String colors; //????????????
	public String chartfield; //???id,???id????????
	public String valuefield;//y????
	public String xaxisname; //x????
	public String yaxisname;//y????
	public String xaxisfield;//x??????
	public int yaxismaxvalue;//y????
	public int yaxisminvalue;//y????
	public int yaxistick;//y?????
	public boolean visiblelegend;//??????
	public String sortfield; //??????
	@Override
	public String toString() {
		return "editable "+editable+" required "+required+" visible "+visible+" width "+width+" height "+height
				+" valuetype " +valuetype+" format "+format+" defaultvalue "+defaultvalue+" lengthlimit "+lengthlimit
				+" singleline "+singleline+" backagroundcolor "+backgroundcolor+" bold "+bold+" textcolor "+textcolor
				+" visiblescrollbar "+visiblescrollbar+" nextview "+nextview+" fontsize "+fontsize;
	}

}

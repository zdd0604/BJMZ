package com.hjnerp.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

import com.hjnerp.util.Log;
import com.hjnerp.util.myscom.ObjectUtils;

public class Ctlm1347 implements Serializable ,Cloneable{
	 
	private static final long serialVersionUID = 1L;
	
	 //<HJTextView id = "002002" name = "终端名称"   editable ="false"  required ="false" visible ="true" width ="0.7" valuetype="string" format="@@@@@@" singleline = "false" fontsize = "medium"  alignment="left" >成都小吃店</HJTextView>
	
	public static final String IDRECORDER = "id_recorder"; //录入人  Login userId  key
	public static final String IDCOM = "id_com";//单位代码 Login comId   key
	public static final String IDNODE = "id_node";//节点代码   id 自己生成 uuid   key
	public static final String NAMENODE = "name_node";//业务名称    终端代码
	public static final String VARBILLNO = "var_billno";//业务单号     客户端生成业务单号,时间+6位随机数  key
	public static final String IDSRCNODE = "id_srcnode";//原结点代码  002002   key
	public static final String FLAGUPLOAD = "flag_upload";//上传日期  data   N
	public static final String IDPNODE = "id_parentnode";//父结点代码有跳转的才有父节点，上级跳转结点   key
	public static final String DATEOPR = "date_opr";
	public static final String IDTABLE = "id_table";
	
	public static final String VARDATA1 = "var_data1";//   成都小吃店
	public static final String VARDATA2 = "var_data2";
	public static final String VARDATA3 = "var_data3";
	public static final String VARDATA4 = "var_data4";
	public static final String VARDATA5 = "var_data5";
	public static final String VARDATA6 = "var_data6";
	public static final String VARDATA7 = "var_data7";
	public static final String VARDATA8 = "var_data8";
	public static final String VARDATA9 = "var_data9";
	public static final String VARDATA10 = "var_data10";
	
	public static final String VARDATA11 = "var_data11";
	public static final String VARDATA12 = "var_data12";
	public static final String VARDATA13 = "var_data13";
	public static final String VARDATA14 = "var_data14";
	public static final String VARDATA15 = "var_data15";
	public static final String VARDATA16 = "var_data16";
	public static final String VARDATA17 = "var_data17";
	public static final String VARDATA18 = "var_data18";
	public static final String VARDATA19 = "var_data19";
	public static final String VARDATA20 = "var_data20";
	
	public static final String IDNODETYPE = "id_nodetype";//控件类型
	public static final String IDMODEL = "id_model";//业务类型      服务器传过来（或者model.xml得到）
	public static String VARVERSION = "var_version";
	
	public static final String IDVIEW = "id_view";
	public static String VARJSON = "var_json";
	public static final String INTLINE = "int_line";

	private String id_recorder = "";
	private String id_com = "";
	private String id_node = "";
	private String name_node = "";
	private String var_billno = "";
	private String id_srcnode="";
	private String flag_upload = "";
	private String id_parentnode = "";
	private String date_opr = "";
	private String id_table = "";
	
	private String var_data1 = "";
	private String var_data2 = "";
	private String var_data3 = "";
	private String var_data4 = "";
	private String var_data5 = "";
	private String var_data6 = "";
	private String var_data7 = "";
	private String var_data8 = "";
	private String var_data9 = "";
	private String var_data10 = "";
	
	private String var_data11 = "";
	
	public String getVar_data11() {
		return var_data11;
	}
	public void setVar_data11(String var_data11) {
		this.var_data11 = var_data11;
	}
	public String getVar_data12() {
		return var_data12;
	}
	public void setVar_data12(String var_data12) {
		this.var_data12 = var_data12;
	}
	public String getVar_data13() {
		return var_data13;
	}
	public void setVar_data13(String var_data13) {
		this.var_data13 = var_data13;
	}
	public String getVar_data14() {
		return var_data14;
	}
	public void setVar_data14(String var_data14) {
		this.var_data14 = var_data14;
	}
	public String getVar_data15() {
		return var_data15;
	}
	public void setVar_data15(String var_data15) {
		this.var_data15 = var_data15;
	}
	public String getVar_data16() {
		return var_data16;
	}
	public void setVar_data16(String var_data16) {
		this.var_data16 = var_data16;
	}
	public String getVar_data17() {
		return var_data17;
	}
	public void setVar_data17(String var_data17) {
		this.var_data17 = var_data17;
	}
	public String getVar_data18() {
		return var_data18;
	}
	public void setVar_data18(String var_data18) {
		this.var_data18 = var_data18;
	}
	public String getVar_data19() {
		return var_data19;
	}
	public void setVar_data19(String var_data19) {
		this.var_data19 = var_data19;
	}
	public String getVar_data20() {
		return var_data20;
	}
	public void setVar_data20(String var_data20) {
		this.var_data20 = var_data20;
	}
	private String var_data12 = "";
	private String var_data13 = "";
	private String var_data14 = "";
	private String var_data15 = "";
	private String var_data16 = "";
	private String var_data17 = "";
	private String var_data18 = "";
	private String var_data19 = "";
	private String var_data20 = "";
	
    private String id_nodetype = "";
	private String id_model = "";
	String var_version = "";
	
	private String id_view = "";
	String var_json = "";
	private int int_line = 0;
	
	public int getInt_line(){
		return int_line;
	}
	public void setInt_line(int line){
		this.int_line = line;
	}
	public String getId_table()
	{
		return id_table;
	}
	public void setId_table(String id_table)
	{
		this.id_table = id_table;
	}
	
	public String getVar_version()
	{
		return var_version;
	}
	public void setVar_version(String var_version)
	{
		this.var_version = var_version;
	}
	
	public String getId_recorder() {
		return id_recorder;
	}

	public void setId_recorder(String id_recorder) {
		this.id_recorder = id_recorder;
	}
	public String getId_com() {
		return id_com;
	}

	public void setId_com(String id_com) {
		this.id_com = id_com;
	}
	public String getId_node() {
		return id_node;
	}

	public void setId_node(String id_node) {
		this.id_node = id_node;
	}
	public String getName_node() {
		return name_node;
	}

	public void setName_node(String name_node) {
		this.name_node = name_node;
	}
	public String getVar_billno() {
		return var_billno;
	}

	public void setVar_billno(String var_billno) {
		this.var_billno = var_billno;
	}
	public String getId_model() {
		return id_model;
	}

	public void setId_model(String id_model) {
		this.id_model = id_model;
	}
	public String getId_srcnode() {
		return id_srcnode;
	}

	public void setId_srcnode(String id_srcnode) {
		this.id_srcnode = id_srcnode;
	}
	public String getFlag_upload() {
		return flag_upload;
	}

	public void setFlag_upload(String flag_upload) {
		this.flag_upload = flag_upload;
	}
	
	public String getId_parentnode() {
		return id_parentnode;
	}
	public void setId_parentnode(String id_parentnode) {
		this.id_parentnode = id_parentnode;
	}
	
	public String getId_nodetype() {
		return id_nodetype;
	}
	public void setId_nodetype(String id_nodetype) {
		this.id_nodetype = id_nodetype;
	}
	
	
	//TODO getdata
	public String getVar_data(String var_data){
		var_data = var_data.trim();
		if(var_data.equalsIgnoreCase(VARDATA1)){
			return getVar_data1();
		}
		if(var_data.equalsIgnoreCase(VARDATA2)){
			return getVar_data2();		}
		if(var_data.equalsIgnoreCase(VARDATA3)){
			return getVar_data3();		}
		if(var_data.equalsIgnoreCase(VARDATA4)){
			return getVar_data4();		}
		if(var_data.equalsIgnoreCase(VARDATA5)){
			return getVar_data5();		}
		if(var_data.equalsIgnoreCase(VARDATA6)){
			return getVar_data6();		}
		if(var_data.equalsIgnoreCase(VARDATA7)){
			return getVar_data7();		}
		if(var_data.equalsIgnoreCase(VARDATA8)){
			return getVar_data8();		}
		if(var_data.equalsIgnoreCase(VARDATA9)){
			return getVar_data9();		}
		if(var_data.equalsIgnoreCase(VARDATA10)){
			return getVar_data10();		}
		return null;
	}
	
	
	//TODO setdata
	
	public void setVar_data(String var_data,String data){
		var_data = var_data.trim();
		if(var_data.equalsIgnoreCase(VARDATA1)){
			setVar_data1(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA2)){
			setVar_data2(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA3)){
			setVar_data3(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA4)){
			setVar_data4(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA5)){
			setVar_data5(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA6)){
			setVar_data6(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA7)){
			setVar_data7(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA8)){
			setVar_data8(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA9)){
			setVar_data9(data);
		}
		if(var_data.equalsIgnoreCase(VARDATA10)){
			setVar_data10(data);
		}
	}
	
	public String getVar_data1() {
		return var_data1;
	}
	public void setVar_data1(String var_data1) {
		this.var_data1 = var_data1;
	}
	
	public String getVar_data2() {
		return var_data2;
	}
	public void setVar_data2(String var_data2) {
		this.var_data2 = var_data2;
	}
	public String getVar_data3() {
		return var_data3;
	}
	public void setVar_data3(String var_data3) {
		this.var_data3 = var_data3;
	}
	public String getVar_data4() {
		return var_data4;
	}
	public void setVar_data4(String var_data4) {
		this.var_data4 = var_data4;
	}
	public String getVar_data5() {
		return var_data5;
	}
	public void setVar_data5(String var_data5) {
		this.var_data5 = var_data5;
	}
	public String getVar_data6() {
		return var_data6;
	}
	public void setVar_data6(String var_data6) {
		this.var_data6 = var_data6;
	}
	public String getVar_data7() {
		return var_data7;
	}
	public void setVar_data7(String var_data7) {
		this.var_data7 = var_data7;
	}
	public String getVar_data8() {
		return var_data8;
	}
	public void setVar_data8(String var_data8) {
		this.var_data8 = var_data8;
	}
	public String getVar_data9() {
		return var_data9;
	}
	public void setVar_data9(String var_data9) {
		this.var_data9 = var_data9;
	}
	public String getVar_data10() {
		return var_data10;
	}
	public void setVar_data10(String var_data10) {
		this.var_data10 = var_data10;
	}
	
	public String getDate_opr() {
		return date_opr;
	}

	public void setDate_opr(String date_opr) {
		this.date_opr = date_opr;
	}
	
	public String getId_view()
	{
		return id_view;
	}
	public void setId_view(String id_view)
	{
		this.id_view = id_view;
	}
	
	public String getvar_Json()
	{
		return var_json;
	}
	public void setvar_Json(String var_json)
	{
		this.var_json = var_json;
	}
	
	
	public String getColumNames(){
		return null;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Field[] fields = Ctlm1347.class.getDeclaredFields();
		Arrays.sort(fields, new Comparator<Field>() {
			@Override
			public int compare(Field lhs, Field rhs) {
				// TODO Auto-generated method stub
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		for(Field field :fields)
		{
			int modifiers = field.getModifiers();
			if(!Modifier.isStatic(modifiers) && Modifier.isPrivate(modifiers))
			{
				field.setAccessible(true);
				try {
					sb.append("'").append(ObjectUtils.toString(field.get(this))).append("',");
				} catch (Exception e) {
					sb.append("'',");
					Log.e(e);
				}
			}
		}
		if(sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}

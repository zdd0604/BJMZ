package com.hjnerp.model;

import java.io.Serializable;

public class Ctlm1345 implements Serializable {
	public static final String IDTABLE="id_table";
	public static final String LINENO="line_no";
	public static final String VARVALNAME="var_valname";
	public static final String VARVALUE="var_value";
	public static final String IDRECORDER="id_recorder";
	public static final String IDCOM="id_com";
	public static final String IDCOLUMN="id_column";
	public static final String VARCONDITION="var_condition";
	public static final String NAMECOLUMN="name_column";
	public static final String VARLONGI="var_rlongi";
	public static final String VARLATI="var_rlati";
	public static final String FLAG_DOWNLOAD ="flag_download";
	public static final String VAR_LOCATION = "var_location";
	public static final String VAR_IMAGE = "var_image";

	private String id_table= "";
	private String line_no = "0";
	private String var_valname = "";
	private String var_value = "";
	private String var_condition= "";
	private String id_recorder="";
	private String id_com="";
	private String id_column="";
	private String name_column="";
	private String var_rlongi="";
	private String var_rlati="";
	private String flag_download="";
	private String var_location = "";
	private String var_image = "";
	public String getVar_location()
	{
		return var_location;
	}
	public void setVar_location(String var_location)
	{
		this.var_location = var_location;
	}
	public String getVar_image()
	{
		return var_image;
	}
	public void setVar_image(String var_image)
	{
		this.var_image = var_image;
	}
	public String getFlag_download() {
		return flag_download;
	}
	public void setFlag_download(String flag_download) {
		this.flag_download = flag_download;
	}
	public String getId_table() {
		return id_table;
	}
	public void setId_table(String id_table) {
		this.id_table = id_table;
	}
	public String getLine_no() {
		return line_no;
	}
	public void setLine_no(String line_no) {
		this.line_no = line_no;
	}
	public String getVar_valname() {
		return var_valname;
	}
	public void setVar_valname(String var_valname) {
		this.var_valname = var_valname;
	}
	public String getVar_value() {
		return var_value;
	}
	public void setVar_value(String var_value) {
		this.var_value = var_value;
	}
	public String getVar_condition() {
		return var_condition;
	}
	public void setVar_condition(String var_condition) {
		this.var_condition = var_condition;
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
	public String getId_column() {
		return id_column;
	}
	public void setId_column(String id_column) {
		this.id_column = id_column;
	}
	public String getName_column() {
		return name_column;
	}
	public void setName_column(String name_column) {
		this.name_column = name_column;
	}
	public String getVar_lati() {
		return var_rlati;
	}
	public void setVar_lati(String var_rlati) {
		this.var_rlati = var_rlati;
	}
	public String getVar_longi() {
		return var_rlongi;
	}
	public void setVar_longi(String var_rlongi) {
		this.var_rlongi = var_rlongi;
	}

	@Override
	public String toString() {
		return "Ctlm1345{" +
				"id_table='" + id_table + '\'' +
				", line_no='" + line_no + '\'' +
				", var_valname='" + var_valname + '\'' +
				", var_value='" + var_value + '\'' +
				", var_condition='" + var_condition + '\'' +
				", id_recorder='" + id_recorder + '\'' +
				", id_com='" + id_com + '\'' +
				", id_column='" + id_column + '\'' +
				", name_column='" + name_column + '\'' +
				", var_rlongi='" + var_rlongi + '\'' +
				", var_rlati='" + var_rlati + '\'' +
				", flag_download='" + flag_download + '\'' +
				", var_location='" + var_location + '\'' +
				", var_image='" + var_image + '\'' +
				'}';
	}

}

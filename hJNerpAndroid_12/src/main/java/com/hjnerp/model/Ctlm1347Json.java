package com.hjnerp.model;

/**
 * 
 * @author 巫志英 用来解析数据库1347中的json字符串
 * 
 */

public class Ctlm1347Json {

	private String id_terminal;
	private String name_terminal;
	private String name_tertype;
	private String var_ownername;
	private String var_tel;
	private String var_addr;
	private String name_spestype;
	private String name_corr;

	private String date_conbegin;
	private String date_conend;
	private String var_input;
	private String var_bfpc;
	private String var_bfcs;
	private String var_review;

	private String var_lati;
	private String var_longi;
	private String checked;

	// json字符串实例

	// local zddm = hju_getjsonvalues(sender.values,'id_terminal');
	// local zdmc = hju_getjsonvalues(sender.values,'name_terminal');
	// local zdlx = hju_getjsonvalues(sender.values,'name_tertype');
	// local zdlxr = hju_getjsonvalues(sender.values,'var_ownername');
	// local zdlxfs = hju_getjsonvalues(sender.values,'var_tel');
	// local zddz = hju_getjsonvalues(sender.values,'var_addr');
	// local xsfs = hju_getjsonvalues(sender.values,'name_spestype');
	// local jxs = hju_getjsonvalues(sender.values,'name_corr');
	// local xykz = hju_getjsonvalues(sender.values,'date_conbegin');
	// local xyjs = hju_getjsonvalues(sender.values,'date_conend');
	// local hstr = hju_getjsonvalues(sender.values,'var_input');
	// local bfpc = hju_getjsonvalues(sender.values,'var_bfpc');
	// local ybfcs = hju_getjsonvalues(sender.values,'var_bfcs');
	// local review = hju_getjsonvalues(sender.values,'var_review');
	// {"id_terminal":"0120101021466","name_terminal":"海洋百货商店","id_tertype":"A6",
	// "name_tertype":"小卖亭及其他","var_ownername":"马海洋","var_tel":"4993328","var_addr":"牟家庄","
	// id_spestype":"B","name_spestype":"混场","id_dept":"012170102","name_dept":"城关二区","
	// id_zone":"01180102","name_zone":"城关二区","id_corr":"0101032","name_corr":"李鹏华","
	// date_conbegin":"1900-01-01","date_conend":"1900-01-01","var_input":"","
	// var_bfpc":"","var_bfcs":"","checked":"N","id_salesman":"15009429331","
	// name_salesman":"息斌","var_contact":"马海洋","var_lati":"103.845305","var_longi":"36.041858",
	// "var_location":" ","checked":"N","var_review":""}

	public String getId_terminal() {
		return id_terminal;
	}

	public void setId_terminal(String id_terminal) {
		this.id_terminal = id_terminal;
	}

	public String getName_tertype() {
		return name_tertype;
	}

	public void setName_tertype(String name_tertype) {
		this.name_tertype = name_tertype;
	}

	public String getVar_ownername() {
		return var_ownername;
	}

	public void setVar_ownername(String var_ownername) {
		this.var_ownername = var_ownername;
	}

	public String getVar_tel() {
		return var_tel;
	}

	public void setVar_tel(String var_tel) {
		this.var_tel = var_tel;
	}

	public String getName_spestype() {
		return name_spestype;
	}

	public void setName_spestype(String name_spestype) {
		this.name_spestype = name_spestype;
	}

	public String getName_corr() {
		return name_corr;
	}

	public void setName_corr(String name_corr) {
		this.name_corr = name_corr;
	}

	public String getDate_conbegin() {
		return date_conbegin;
	}

	public void setDate_conbegin(String date_conbegin) {
		this.date_conbegin = date_conbegin;
	}

	public String getDate_conend() {
		return date_conend;
	}

	public void setDate_conend(String date_conend) {
		this.date_conend = date_conend;
	}

	public String getVar_input() {
		return var_input;
	}

	public void setVar_input(String var_input) {
		this.var_input = var_input;
	}

	public String getVar_bfpc() {
		return var_bfpc;
	}

	public void setVar_bfpc(String var_bfpc) {
		this.var_bfpc = var_bfpc;
	}

	public String getVar_bfcs() {
		return var_bfcs;
	}

	public void setVar_bfcs(String var_bfcs) {
		this.var_bfcs = var_bfcs;
	}

	public String getVar_review() {
		return var_review;
	}

	public void setVar_review(String var_review) {
		this.var_review = var_review;
	}

	public String getName_terminal() {
		return name_terminal;
	}

	public void setName_terminal(String name_terminal) {
		this.name_terminal = name_terminal;
	}

	public String getVar_addr() {
		return var_addr;
	}

	public void setVar_addr(String var_addr) {
		this.var_addr = var_addr;
	}

	public String getVar_lati() {
		return var_lati;
	}

	public void setVar_lati(String var_lati) {
		this.var_lati = var_lati;
	}

	public String getVar_longi() {
		return var_longi;
	}

	public void setVar_longi(String var_longi) {
		this.var_longi = var_longi;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}
}

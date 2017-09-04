package com.hjnerp.model;

import java.util.List;

public class BusinessData {
	public static final String IDTABLE="id_table";
	private String id_table= ""; 
	private String id_view= "";
	public String var_values ;
	public List<Ctlm1345> Ctlm1345 ;
	
	public String flag_tag = "";
	
	public String getFlag_tag() {
		return flag_tag;
	}
	public void setFlag_tag(String flag_tag) {
		this.flag_tag = flag_tag;
	}
	public String getId_view() {
		return id_view;
	}
	public void setId_view(String idview) {
		this.id_view = idview;
	}
	
	
	public String getId_table() {
		return id_table;
	}
	public void setId_table(String id_table) {
		this.id_table = id_table;
	}
	public String getVar_values() {
		return var_values;
	}
	public void setVar_values(String var_values) {
		this.var_values = var_values;
	}
	public List<Ctlm1345> getCtlm1345() {
		return Ctlm1345;
	}
	public void setCtlm1345(List<Ctlm1345> ctlm1345) {
		Ctlm1345 = ctlm1345;
	}
	
	
}

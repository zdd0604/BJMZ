package com.hjnerp.model;

import com.hjnerp.util.StringUtil;

public class WidgetData {

	public String id;
	public String dbfield ;
	public String field;
	public Object value;
	public String UUID;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDbfield() {
		return dbfield;
	}
	public void setDbfield(String dbfield) {
		this.dbfield = dbfield;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public void setUUID( ) {
		UUID =   StringUtil.getMyUUID();
	}
	
	
}

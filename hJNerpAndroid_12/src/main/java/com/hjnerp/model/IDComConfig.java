package com.hjnerp.model;

import java.io.Serializable;

public class IDComConfig  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String id_com;
	String name_com;
	String id_user;
	String url_http;
	String url_websocket;
	String flag_map;
	String var_worktable;

	public String getVar_worktable() {
		return var_worktable;
	}

	public void setVar_worktable(String var_worktable) {
		this.var_worktable = var_worktable;
	}

	public String getFlag_map() {
		return flag_map;
	}
	public void setFlag_map(String flag_map) {
		this.flag_map = flag_map;
	}
	public String getId_com() {
		return id_com;
	}
	public void setId_com(String id_com) {
		this.id_com = id_com;
	}
	public String getName_com() {
		return name_com;
	}
	public void setName_com(String name_com) {
		this.name_com = name_com;
	}
	public String getId_user() {
		return id_user;
	}
	public void setId_user(String id_user) {
		this.id_user = id_user;
	}
	public String getUrl_http() {
		return url_http;
	}
	public void setUrl_http(String url_http) {
		this.url_http = url_http;
	}
	public String getUrl_websocket() {
		return url_websocket;
	}
	public void setUrl_websocket(String url_websocket) {
		this.url_websocket = url_websocket;
	}
	

}

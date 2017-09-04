package com.hjnerp.model;

public class MenuInfo {

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String id; // 主键
	private String title; // 标题
	private String modelwindow; // 模板窗口
	private String parm; // 参数
	private String type;// 菜单类型

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getModelwindow() {
		return modelwindow;
	}

	public void setModelwindow(String modelwindow) {
		this.modelwindow = modelwindow;
	}

	public String getParm() {
		return parm;
	}

	public void setParm(String parm) {
		this.parm = parm;
	}

}

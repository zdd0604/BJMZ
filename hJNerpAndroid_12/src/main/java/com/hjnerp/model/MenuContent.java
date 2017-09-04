package com.hjnerp.model;

import java.io.Serializable;

/*
 * 记录BusinessFragment展示的业务类型
 */
public class MenuContent implements Serializable{
	
	private String idMenu;//菜单id
	private String nameMenu;//带单名称
	private String modelWindow;//此菜单服务器上最新的模板版本号
	private String varParm;//xml文件名
	private String picpath;//图片
	private String varParm1 = "";//分组

	public String getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(String idMenu) {
		this.idMenu = idMenu;
	}

	public String getNameMenu() {
		return nameMenu;
	}

	public void setNameMenu(String nameMenu) {
		this.nameMenu = nameMenu;
	}

	public String getModelWindow() {
		return modelWindow;
	}

	public void setModelWindow(String modelWindow) {
		this.modelWindow = modelWindow;
	}

	public String getVarParm() {
		return varParm;
	}

	public void setVarParm(String varParm) {
		this.varParm = varParm;
	}

	
	public String getPicpath() {
		return picpath;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public String getVarParm1() {
		return varParm1;
	}

	public void setVarParm1(String varParm1) {
		this.varParm1 = varParm1;
	}

	@Override
	public String toString() {
		return "MenuContent [idMenu=" + idMenu + ", nameMenu=" + nameMenu
				+ ", modelWindow=" + modelWindow + ", varParm=" + varParm
				+ ", picpath=" + picpath + ", varParm1=" + varParm1 + "]";
	}


}

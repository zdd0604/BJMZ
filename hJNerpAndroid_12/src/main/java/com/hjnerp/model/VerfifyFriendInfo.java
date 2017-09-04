package com.hjnerp.model;

import java.io.Serializable;

public class VerfifyFriendInfo extends TempContactInfo implements Serializable{
	protected String verfifyType;   //验证类型，我要加好友还是别人要加我为好友
	protected String verfifyResult;  //验证结果
	protected String verfifyNote;  //附加用户手动输入的验证信息
	protected String flag_read = "N";//用户是否已查看此申请

	
	public String getFlagRead(){
		return flag_read;
	}
	public void setFlagRead(String flag){
		this.flag_read = flag;
	}
	
	public String getVerfifyType() {
		return verfifyType;
	}

	public void setVerfifyType(String type) {
		this.verfifyType = type;
	}
	public String getVerfifyResult(){
		return verfifyResult;
	}
	public void setVerfifyResult(String result){
		this.verfifyResult = result;
	}
	public String getVerfifyNote(){
		return verfifyNote;
	}
	public void setVerfifyNote(String note){
		this.verfifyNote = note;
	}
}

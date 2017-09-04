package com.hjnerp.model;

import java.io.Serializable;

public class WorkFlowRecorderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	//	private String password;
//	private String userName;
//	private String comId;
//	private String clerkId;
//	private String flagDownload;
//	private String postId;
//	private String idUser;
//	private String postName;
//	private String comName;
//	private String idDept;
//	private String picUrl;
//	private String varMtel;
//	private String varEmail;
//	private String nameDept;
	private String opinion;//审批意见
	private String description;//审批意见描述
	private String date;//审批时间
	private String user_id;//审批人id
	private String user_name;//	审批人名字
	private String avatar;//审批人头像
	private String com_id;
	
	public String getOpinion(){
		return opinion;
	}
	public void setOpinion(String opinion){
		this.opinion = opinion;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date = date;
	}
	public String getUserId(){
		return user_id;
	}
	public void setUserId(String userId){
		this.user_id = userId;
	}
	
	public String getUserName(){
		return user_name;
	}
	public void setUserName(String userName){
		this.user_name = userName;
	}
	public String getAvatar(){
		return avatar;
	}
	public void setAvatar(String avatar){
		this.avatar = avatar;
	}
	
	public String getComID()
	{
		return com_id; 
	}
	
	public void setComID(String comID)
	{
		this.com_id = comID;
	}

	@Override
	public String toString() {
		return "WorkFlowRecorderInfo{" +
				"opinion='" + opinion + '\'' +
				", description='" + description + '\'' +
				", date='" + date + '\'' +
				", user_id='" + user_id + '\'' +
				", user_name='" + user_name + '\'' +
				", avatar='" + avatar + '\'' +
				", com_id='" + com_id + '\'' +
				'}';
	}
}

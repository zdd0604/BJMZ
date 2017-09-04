package com.hjnerp.model;

import java.io.Serializable;

/**
 * 
 * 最近联系人显示的与某个的聊天记录bean，包括 收到某个人的最后一条信息的全部内容，收到某人未读信息的数量总和
 * 
 * @author 李庆义
 */
public class ChatHisBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id=""; // 主键
	private String title=""; // 标题
	private String msgcontent=""; // 最后内容
	private String msgTime=""; // 最后通知时间
	private Integer msgSum=0;// 收到未读消息总数、
	private String msgType=""; // 聊天类型 chat chatgroup
	private String msgFrom=""; // 来源的人员信息
	private String msgGroup=""; // 来源消息组
	private String msgPhoto=""; // /头像
	private String msgTo="";
	private String msgIdType=""; //消息类型（IQ/MSG，默认MSG）
	private String msgIdFile="";//图片id/fileID,必须唯一
	private String msgTypeFile="";//pic/audio/doc(对应企信表sen)
	private String msgSendStatus="";//信息发送状态（发送中，发送失败，发送成功）
	private String msgIdRecorder="";
	private String msgFlagPaly="";
	private int type;   
	

	private boolean isShowTime = false;
	
	public boolean isShowTime() {
		return isShowTime;
	}

	public void setShowTime(boolean isShowTime) {
		this.isShowTime = isShowTime;
	}

	public String getMsgFlagPaly() {
		return msgFlagPaly;
	}

	public void setMsgFlagPaly(String msgFlagPaly) {
		this.msgFlagPaly = msgFlagPaly;
	}

	@Override
	public String toString() {
		return "ChatHisBean [id=" + id + ", title=" + title + ", msgcontent=" + msgcontent + ", msgTime=" + msgTime + ", msgSum=" + msgSum + ", msgType=" + msgType + ", msgFrom="
				+ msgFrom + ", msgGroup=" + msgGroup + ", msgPhoto=" + msgPhoto + ", msgTo=" + msgTo + ", msgIdType=" + msgIdType + ", msgIdFile=" + msgIdFile + ", msgTypeFile="
				+ msgTypeFile + ", msgSendStatus=" + msgSendStatus + ", msgIdRecorder=" + msgIdRecorder + ", type=" + type  + ", msgFlagPaly=" + msgFlagPaly + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(id != null)
		this.id = id;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getMsgIdRecorder() {
		return msgIdRecorder;
	}

	public void setMsgIdRecorder(String msgIdRecorder) {
		if(msgIdRecorder != null)
		this.msgIdRecorder = msgIdRecorder;
	}
	public String getMsgIdType() {
		return msgIdType;
		
	}

	public void setMsgIdType(String idtype) {
		if(idtype != null)
		this.msgIdType = idtype;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(title != null)
		this.title = title;
	}

	public String getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		if(msgcontent != null)
		this.msgcontent = msgcontent;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		if(msgTime != null)
		this.msgTime = msgTime;
	}

	public Integer getMsgSum() {
		return msgSum;
	}

	public void setMsgSum(Integer msgSum) {
		if(msgSum != null)
		this.msgSum = msgSum;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		if(msgType != null)
		this.msgType = msgType;
	}

	public String getMsgFrom() {
		return msgFrom;
	}

	public void setMsgFrom(String msgFrom) {
		if(msgFrom != null)
		this.msgFrom = msgFrom;
	}

	public String getMsgGroup() {
		return msgGroup;
	}

	public void setMsgGroup(String msgGroup) {
		if(msgGroup != null)
		this.msgGroup = msgGroup;
	}

	public String getMsgPhoto() {
		return msgPhoto;
	}

	public void setMsgPhoto(String msgPhoto) {
		if(msgPhoto != null)
		this.msgPhoto = msgPhoto;
	}
	
	public String getMsgTo() {
		return msgTo;
	}

	public void setMsgTo(String msgto) {
		if(msgto != null)
		this.msgTo = msgto;
	}
	public String getmsgIdFile() {
		return msgIdFile;
	}

	public void setmsgIdFile(String msgIdFile) {
		if(msgIdFile != null)
		this.msgIdFile = msgIdFile;
	}
	
	public String getmsgTypeFile() {
		return msgTypeFile;
	}

	public void setmsgTypeFile(String msgTypeFile) {
		if(msgTypeFile != null)
		this.msgTypeFile = msgTypeFile;
	}
	public String getmsgSendStatus() {
		return msgSendStatus;
	}

	public void setmsgSendStatus(String msgSendStatus) {
		if(msgSendStatus != null)
		this.msgSendStatus = msgSendStatus;
	}
}

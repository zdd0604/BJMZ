package com.hjnerp.activity.im;

public class NewChatMessage {
	private String msg_type;
	private String msg_from;
	private String msg_to;
	private int msg_count;
	
	public void setMsgType(String msgtype){
		this.msg_type = msgtype;
	}
	public String getMsgType(){
		return msg_type;
	}
	
	public void setMsgFrom(String msgfrom){
		this.msg_from = msgfrom;
	}
	public String getMsgFrom(){
		return msg_from;
	}
	
	public void setMsgTo(String msgto){
		this.msg_to = msgto;
	}
	public String getMsgTo(){
		return msg_to;
	}

	public void setMsgCount(int count){
		this.msg_count = count;
	}
	public int getMsgCount(){
		return msg_count;
	}
	
	public void CountAdd(){
		msg_count++;
	}
	
}

package com.hjnerp.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.hjnerp.common.Constant;
import com.hjnerp.util.DateUtil;

public class IMMessage implements Parcelable, Comparable<IMMessage> {
	public static final String IMMESSAGE_KEY = "immessage.key";
	public static final String KEY_TIME = "immessage.time";
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;

	private int type;
	private String msgcontent; // 发送文本
	private String msggroup; // 发送组
	private String msgfrom; // 存在本地，表示与谁聊天
	private String msgtime; // 时间
	private int msgstatus = 0;// 0:读与未读
	private int msgdirect = 0;
	private String msgHtml; // 发送HTML内容
	private String msgtype; // 消息类型 1.好友请求 2.系统消息
	private String msgid; // 消息ID
	private String msgto; // 发送TO

	public IMMessage() {
		this.type = SUCCESS;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}

	public String getMsggroup() {
		return msggroup;
	}

	public void setMsggroup(String msggroup) {
		this.msggroup = msggroup;
	}

	public String getMsgfrom() {
		return msgfrom;
	}

	public void setMsgfrom(String msgfrom) {
		this.msgfrom = msgfrom;
	}

	public String getMsgtime() {
		return msgtime;
	}

	public void setMsgtime(String msgtime) {
		this.msgtime = msgtime;
	}

	public int getMsgstatus() {
		return msgstatus;
	}

	public void setMsgstatus(int msgstatus) {
		this.msgstatus = msgstatus;
	}

	public int getMsgdirect() {
		return msgdirect;
	}

	public void setMsgdirect(int msgdirect) {
		this.msgdirect = msgdirect;
	}

	public String getMsgHtml() {
		return msgHtml;
	}

	public void setMsgHtml(String msgHtml) {
		this.msgHtml = msgHtml;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getMsgto() {
		return msgto;
	}

	public void setMsgto(String msgto) {
		this.msgto = msgto;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeString(msgcontent);
		dest.writeString(msggroup);
		dest.writeString(msgfrom);
		dest.writeString(msgtime);
		dest.writeInt(msgstatus);
		dest.writeInt(msgdirect);
		dest.writeString(msgHtml);
		dest.writeString(msgtype);
		dest.writeString(msgid);
		dest.writeString(msgto);

	}

	public static final Parcelable.Creator<IMMessage> CREATOR = new Parcelable.Creator<IMMessage>() {

		@Override
		public IMMessage createFromParcel(Parcel source) {
			IMMessage message = new IMMessage();
			message.setType(source.readInt());
			message.setMsgcontent(source.readString());
			message.setMsggroup(source.readString());
			message.setMsgfrom(source.readString());
			message.setMsgtime(source.readString());
			message.setMsgstatus(source.readInt());
			message.setMsgdirect(source.readInt());
			message.setMsgHtml(source.readString());
			message.setMsgtype(source.readString());
			message.setMsgid(source.readString());
			message.setMsgto(source.readString());

			return message;
		}

		@Override
		public IMMessage[] newArray(int size) {
			return new IMMessage[size];
		}

	};

	/**
	 * 新消息的构造方法.
	 * 
	 * @param content
	 * @param time
	 */
	public IMMessage(String content, String time, String withGroup,
			String withSb, String msgType, int msgDirect, int msgStatus,
			String msgHtml, String msgTo) {
		super();
		this.msgcontent = content;
		this.msgtime = time;
		this.msgtype = msgType;
		this.msgfrom = withSb;
		this.msgdirect = msgDirect;
		this.msggroup = withGroup;
		this.msgHtml = msgHtml;
		this.msgstatus = msgStatus;
		this.msgto = msgTo;

	}

	/**
	 * 李庆义 按时间降序排列
	 */
	@Override
	public int compareTo(IMMessage oth) {
		if (null == this.getMsgtime() || null == oth.getMsgtime()) {
			return 0;
		}
		String format = null;
		String time1 = "";
		String time2 = "";
		if (this.getMsgtime().length() == oth.getMsgtime().length()
				&& this.getMsgtime().length() == 23) {
			time1 = this.getMsgtime();
			time2 = oth.getMsgtime();
			format = Constant.MS_FORMART;
		} else {
			time1 = this.getMsgtime().substring(0, 19);
			time2 = oth.getMsgtime().substring(0, 19);
		}
		Date da1 = DateUtil.str2Date(time1, format);
		Date da2 = DateUtil.str2Date(time2, format);
		if (da1.before(da2)) {
			return -1;
		}
		if (da2.before(da1)) {
			return 1;
		}

		return 0;
	}
}

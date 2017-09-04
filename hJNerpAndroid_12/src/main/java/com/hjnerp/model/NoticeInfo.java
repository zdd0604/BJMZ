package com.hjnerp.model;

public class NoticeInfo {

	private String id; // 主键
	private String title; // 标题
	private String content; // 最后内容
	private Integer status; // 最后状态 0已读 1未读
	private String from; // 最后通知来源
	private String to; // 最后通知去想
	private String noticeTime; // 最后通知时间
	private Integer noticeType; // 消息类型 1.文本 ，2
	private String groupId;
	private String noticeSubhead;

	@Override
	public String toString() {
		return "NoticeInfo [id=" + id + ", title=" + title + ", content="
				+ content + ", status=" + status + ", from=" + from + ", to="
				+ to + ", noticeTime=" + noticeTime + ", noticeType="
				+ noticeType + ", groupId=" + groupId + ", noticeSubhead="
				+ noticeSubhead + "]";
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getNoticeSubhead() {
		return noticeSubhead;
	}

	public void setNoticeSubhead(String noticeSubhead) {
		this.noticeSubhead = noticeSubhead;
	}

	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}
}

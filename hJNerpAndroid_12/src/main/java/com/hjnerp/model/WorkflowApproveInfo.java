package com.hjnerp.model;

import java.io.Serializable;

public class WorkflowApproveInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** 审批时间*/
	private String deal_date;
	/** 审批意见:Y/N/*/
	private String opinion;
	/** 审批意见描述:"很好"*/
	private String suggest;
	private WorkFlowRecorderInfo handler_info;
	
	
	public String getTime() {
		return deal_date;
	}
	public void setTime(String time) {
		this.deal_date = time;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setsuggest(String suggest) {
		this.suggest = suggest;
	}
	public WorkFlowRecorderInfo getUser() {
		return handler_info;
	}

	public void setUser(WorkFlowRecorderInfo user) {
		this.handler_info = user;
	}

	public WorkFlowRecorderInfo getHandler_info() {
		return handler_info;
	}

	@Override
	public String toString() {
		return "WorkflowApproveInfo{" +
				"deal_date='" + deal_date + '\'' +
				", opinion='" + opinion + '\'' +
				", suggest='" + suggest + '\'' +
				", handler_info=" + handler_info.toString() +
				'}';
	}
}

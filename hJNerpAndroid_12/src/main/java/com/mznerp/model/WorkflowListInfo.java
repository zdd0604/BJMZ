package com.mznerp.model;

import java.io.Serializable;

/**
 * 
 * 最近联系人显示的与某个的聊天记录bean，包括 收到某个人的最后一条信息的全部内容，收到某人未读信息的数量总和
 * 
 * @author 李庆义
 */
public class WorkflowListInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int ADD_FRIEND = 1;// 好友请求
	public static final int SYS_MSG = 2; // 系统消息
	public static final int CHAT_MSG = 3;// 聊天消息

	public static final int READ = 0;
	public static final int UNREAD = 1;

//	private String context;//简内容
//	private String time;// 单据时间
//	private String table;// 单据类型
//	private String lineNo;//行号
//	private String compId;//完成时间?
//
//	private String billNo;//单号
//	private String stepNo;//步骤号
//	private List<String> docs;//附件
//	private List<WorkflowDetailInfo> detail;//详细内容
//	private String oprType;//操作类型
//	private String title;//标题
//	private String flagDeal;// 处理标志 Y为已处理 N为未处理
//	private WorkFlowRecorderInfo user;// 录入人
	
	private String title;	// 标题
	private String content;	// 简介内容
	private String attach;	// 附件名字，存在并长度不为空时，界面显示圆形针
	private String bill_type;	// 单据类型（请假单等）
	private String bill_no;	// 单据号，单据标识
	private String bill_date;	// 单据日期  
	private String flag_deal;	// 	处理标语，Y为已经处理，N为未处理
	private String opt_type;	//	操作类型，用于区分不同的审批类型的操作 R-审阅;A-会签;Y-审批
	private boolean isVisible;	//	是否授权使用该单据类型
	private WorkFlowRecorderInfo recoder_info;	//	单据创建人相关信息(仅包含user_id、user_name、avatar三个属性)
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getAttach(){
		return attach;
	}
	public void setAttach(String attach){
		this.attach = attach;
	}
	public String getBillType(){
		return bill_type;
	}
	public void setBillType(String bill_type){
		this.bill_type = bill_type;
	}
	public String getBillNo(){
		return bill_no;
	}
	public void setBillNo(String bill_no){
		this.bill_no = bill_no;
	}
	public String getDate(){
		return bill_date;
	}
	public void setDate(String date){
		this.bill_date = date;
	}
	public String getFlagDeal(){
		return flag_deal;
	}
	public void setFlagDeal(String flag_deal){
		this.flag_deal = flag_deal;
	}
	public String getOptType(){
		return opt_type;
	}
	public void setOptType(String opt_type){
		this.opt_type = opt_type;
	}
	
	public WorkFlowRecorderInfo getUser() {
		return recoder_info;
	}

	public void setUser(WorkFlowRecorderInfo user) {
		this.recoder_info = user;
	}

	@Override
	public String toString() {
		return "WorkflowListInfo{" +
				"title='" + title + '\'' +
				", content='" + content + '\'' +
				", attach='" + attach + '\'' +
				", bill_type='" + bill_type + '\'' +
				", bill_no='" + bill_no + '\'' +
				", bill_date='" + bill_date + '\'' +
				", flag_deal='" + flag_deal + '\'' +
				", opt_type='" + opt_type + '\'' +
				", recoder_info=" + recoder_info +
				'}';
	}
}

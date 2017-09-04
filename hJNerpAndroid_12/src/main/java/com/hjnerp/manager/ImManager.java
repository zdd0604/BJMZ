package com.hjnerp.manager;

import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.hjnerp.common.Constant;
import com.hjnerp.db.DBManager;
import com.hjnerp.db.SQLiteTemplate;
import com.hjnerp.db.SQLiteTemplate.RowMapper;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.model.IMMessage;
import com.hjnerp.model.NoticeInfo;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.StringUtil;

/**
 * 
 * 消息历史记录，
 * 
 * @author 李庆义
 */
public class ImManager {
	private static ImManager charManager = null;
	private static DBManager manager = null;

	private ImManager(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(
				Constant.LOGIN_SET, Context.MODE_PRIVATE);
		String databaseName = sharedPre.getString(Constant.USERNAME, null);
		manager = DBManager.getInstance(context, databaseName);
	}

	public static ImManager getInstance(Context context) {

		if (charManager == null) {
			charManager = new ImManager(context);
		}

		return charManager;
	}

	/**
	 * 
	 * 保存消息.
	 * 
	 * @param msg
	 * @author 李庆义
	 * @update 2012-5-16 下午3:23:15
	 */
	public long saveGroup(ChatHisBean group) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues(); 
//		contentValues.put("group_id", group.getId());
//		contentValues.put("group_from", group.getMsgFrom());
//		contentValues.put("opr_time", group.getOprTime());
//		contentValues.put("group_type", group.getGroupType());
//		contentValues.put("group_name", group.getTitle());
//		contentValues.put("opr_id", group.getOprId());
 
		return st.insert("im_group", contentValues);
	}

	/**
	 * 
	 * 保存消息.
	 * 
	 * @param msg
	 * @author 李庆义
	 * @update 2012-5-16 下午3:23:15
	 */
	public long saveIMMessage(IMMessage msg)
	{
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		String s = UUID.randomUUID().toString();
		contentValues.put("msgid" ,s);
		contentValues.put("msgcontent" ,msg.getMsgcontent());
		contentValues.put("msggroup", msg.getMsggroup());
		contentValues.put("msgfrom", msg.getMsgfrom()); 
		contentValues.put("msgdate", msg.getMsgtime());
		contentValues.put("msgstatus", msg.getMsgstatus());
		contentValues.put("msgdirect", msg.getMsgdirect());
		contentValues.put("msghtml",msg.getMsgHtml());  
		contentValues.put("msgtype",msg.getMsgtype());
		contentValues.put("msgto",msg.getMsgto());
		
		return st.insert("msg", contentValues);
		
	}
	
	
	public long savenotice(NoticeInfo notice) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
 		ContentValues contentValues = new ContentValues();
 		contentValues.put("notice_id" , notice.getId());
 		contentValues.put("notice_type", notice.getNoticeType());
 		contentValues.put("notice_content",notice.getContent());
 		contentValues.put("notice_from",notice.getFrom());
 		contentValues.put("notice_to", notice.getTo());
 		contentValues.put("notice_time", notice.getNoticeTime());
 		contentValues.put("notice_status", notice.getStatus());
 		contentValues.put("group_id", notice.getGroupId());
 		contentValues.put("notice_subhead", notice.getNoticeSubhead() ); 
		return st.insert("im_notice", contentValues);
	}
	
	
	/**
	 * 
	 * 更新状态.
	 * 
	 * @param status
	 * @author 李庆义
	 * @update 2012-5-16 下午3:22:44
	 */
	public void updateStatus(String id, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("notice_status", status);
        st.update("im_notice", contentValues, " id_group =? ", new String[]{ id});
		 
	}
	/**
	 * 
	 * 查找与某人的聊天记录聊天记录
	 * 
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            要查的记录条数
	 * @return
	 * @author 李庆义
	 * @update 2012-7-2 上午9:31:04
	 */
	public List<NoticeInfo> getNoticeListByFrom(String fromUser, int pageNum,
			int pageSize) {
		if (StringUtil.empty(fromUser)) {
			return null;
		}
		int fromIndex = (pageNum - 1) * pageSize;
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<NoticeInfo> list = st.queryForList(
				new RowMapper<NoticeInfo>() {
					@Override
					public NoticeInfo mapRow(Cursor cursor, int index) {
						NoticeInfo notice = new NoticeInfo();
						notice.setContent(cursor.getString(cursor
								.getColumnIndex("notice_content")));
						notice.setFrom( cursor.getString(cursor
								.getColumnIndex("notice_from")));
						notice.setTo( cursor.getString(cursor
								.getColumnIndex("notice_to")));
						String time =cursor.getString(cursor
								.getColumnIndex("notice_time")) ;
						time= DateUtil.formatTimeString(time); 
						notice.setNoticeTime( time);
						notice.setNoticeType( cursor.getInt(cursor
								.getColumnIndex("notice_type")));
						notice.setGroupId(cursor.getString(cursor
								.getColumnIndex("group_id")));
						notice.setId(cursor.getString(cursor
								.getColumnIndex("notice_id")));
						notice.setNoticeSubhead(cursor.getString(cursor
								.getColumnIndex("notice_subhead")));
						notice.setStatus(cursor.getInt(cursor
								.getColumnIndex("notice_status")));
						notice.setTitle(cursor.getString(cursor
								.getColumnIndex("notice_title")));
						return notice;
					}
				},
				"select   notice_id  ,   notice_type  ,   notice_content  ,  notice_from  ,  notice_to  ,  notice_time  ,   notice_status  ,  group_id ,   notice_subhead  ,   notice_title  from im_notice where group_id = ? order by notice_time ASC limit ? , ? ",
				new String[] { "" + fromUser, "" + fromIndex, "" + pageSize });
		return list;

	}
	 
	/**
	 * 
	 * 查找与组的聊天记录总数
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-7-2 上午9:31:04
	 */
	public int getChatCountWithSb(String groupId) {
		if (StringUtil.empty(groupId)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st
				.getCount(
						"select notice_id  from im_notice where group_id=?",
						new String[] { "" + groupId });

	}

	/**
	 * 清楚与组的聊天记录 author 李庆义
	 * 
	 * @param fromUser
	 */
	public int delChatHisWithSb(String fromUser) {
		if (StringUtil.empty(fromUser)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_notice", "group_id=?",
				new String[] { "" + fromUser });
	}

	 
	/**
	 * 
	 * 获取最近聊天人聊天最后一条消息和未读消息总数
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-5-16 下午3:22:53
	 */
	public List<ChatHisBean> getRecentContactsWithLastMsg() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<ChatHisBean> list = st
				.queryForList(
						new RowMapper<ChatHisBean>() { 
							@Override
							public ChatHisBean mapRow(Cursor cursor, int index) {
								ChatHisBean chatBean = new ChatHisBean(); 
								chatBean.setId(cursor.getString(cursor
										.getColumnIndex("msgid"))); 
								chatBean.setMsgFrom(cursor.getString(cursor
										.getColumnIndex("msgfrom")));
								chatBean.setMsgType (cursor.getString(cursor
										.getColumnIndex("msgtype"))); 
								String time =cursor.getString(cursor
										.getColumnIndex("msgdate")) ;
								time= DateUtil.formatTimeString(time); 
								chatBean.setMsgTime(time );
								chatBean.setMsgcontent(cursor.getString(cursor
										.getColumnIndex("msgcontent")));
								chatBean.setMsgGroup(cursor.getString(cursor
										.getColumnIndex("msggroup"))); 
								return chatBean;
							}
						},
						"select msg.msgid,  msg.msggroup , msg.msgfrom , msg.msgcontent , msg.msgtype , msg.msgdate , msg.msghtml  from msg , ( select  max(msg. msgdate ) msgdate ,msg.msggroup, msg.msgfrom,msg.msgto,msg.msgtype  from msg  group by  msg.msggroup, msg.msgfrom,msg.msgto,msg.msgtype   ) a   where  msg.msgdate = a.msgdate and msg.msgfrom = a.msgfrom and msg.msgto = a.msgto and msg.msgtype = a.msgtype and msg.msggroup = a.msggroup" ,
						 null);
		
		for (ChatHisBean b : list) { 
			 int count =  st.getCount(   "select    msgid   "+
                                  "  from   msg where msgstatus = 0  and msggroup = ? and msgfrom =? group by msgid  ",
                         new String[] { b.getMsgGroup(),b.getMsgFrom()  });
 			if (count > 0   )  
 			{
				b.setMsgSum (count) ;   
 			}
 			else
 			{
 				b.setMsgSum(0) ;    
 			}
		}
		return list;
	}
	
	 
	
	/**
	 * 
	 * 获取最近与某人的聊天最后一条消息和未读消息总数
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-5-16 下午3:22:53
	 */
	public List<IMMessage> getMessageListByFrom( String groupid,String friendid,int fromIndex,int pagesize) {
		
		if (StringUtil.empty(friendid)) {
			return null;
		}
		if (StringUtil.empty(groupid)) {
			groupid = "";
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<IMMessage> list = st
				.queryForList(
						new RowMapper<IMMessage>() { 
							@Override
							public IMMessage mapRow(Cursor cursor, int index) {
								
							    IMMessage msg = new IMMessage(); 
								msg.setMsgid(cursor.getString(cursor
										.getColumnIndex("msgid")));
								msg.setMsgcontent(cursor.getString(cursor
										.getColumnIndex("msgcontent")));
								msg.setMsggroup(cursor.getString(cursor
										.getColumnIndex("msggroup")));
								msg.setMsgfrom(cursor.getString(cursor
										.getColumnIndex("msgfrom")));
								msg.setMsgtime(cursor.getString(cursor
										.getColumnIndex("msgdate")));
								msg.setMsgstatus(cursor.getInt(cursor
										.getColumnIndex("msgstatus")));
								msg.setMsgdirect(cursor.getInt(cursor
										.getColumnIndex("msgdirect")));
								msg.setMsgHtml(cursor.getString(cursor
										.getColumnIndex("msghtml")));
								msg.setMsgtype(cursor.getString(cursor
										.getColumnIndex("msgtype")));
								msg.setMsgto(cursor.getString(cursor
										.getColumnIndex("msgto"))); 
								return msg;
							}
						},
						" select msgid , msgcontent  , msggroup  ,   msgfrom  ,   msgdate  , msgstatus  ,   msgdirect  ,   msghtml  ,  msgtype  ,  msgto   from msg   where  msggroup=? and msgfrom=?  order by msgdate  ",
						new String[]{ groupid,friendid});

		return list;
	}
	
}

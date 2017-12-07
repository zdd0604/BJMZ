package com.mznerp.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.db.Tables;
import com.mznerp.model.ChatHisBean;
import com.mznerp.model.FriendInfo;
import com.mznerp.model.GroupInfo;
import com.mznerp.model.UserInfo;
import com.mznerp.model.VerfifyFriendInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.util.DateUtil;
import com.mznerp.util.SharePreferenceUtil;

/**
 * 数据访问层, 并做SQLite数据库和model包下的适配
 * 
 * @author John Kenrinus Lee
 */
@SuppressLint("SimpleDateFormat")
public class QiXinBaseDao extends BaseDao {
	// TODO 联系人表 ctlm1355

	private static String TAG = "QiXinBaseDao";

	public static final void updateSingleChatSendFlag(String userID,
			String friendId) {
		// SQLiteDatabase database = beginDMLOnTransaction();
		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendfrom = Tables.EnterpriseInfoTable.COL_ID_SENDFROM;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();

		sql.append("update   ").append(ctlm1356)
				.append(" set flag_send = 'fail'");
		sql.append(" where ((").append(id_sendfrom).append("='").append(userID)
				.append("' and ").append(id_sendto).append("='")
				.append(friendId).append("')");
		sql.append(" or (").append(id_sendto).append("='").append(userID)
				.append("' and ").append(id_sendfrom).append("='")
				.append(friendId).append("'))");
		sql.append(" and ").append(var_contype).append("='chat'");
		sql.append(" and ").append(id_recorder).append("='").append(userID)
				.append("'");
		sql.append(" and flag_send = 'ing' ");
		updateSQL(sql.toString());
		StringBuffer sql2 = new StringBuffer();

		sql2.append("update   ").append(ctlm1356)
				.append(" set flag_send = 'file_send_fail'");
		sql2.append(" where ((").append(id_sendfrom).append("='")
				.append(userID).append("' and ").append(id_sendto).append("='")
				.append(friendId).append("')");
		sql2.append(" or (").append(id_sendto).append("='").append(userID)
				.append("' and ").append(id_sendfrom).append("='")
				.append(friendId).append("'))");
		sql2.append(" and ").append(var_contype).append("='chat'");
		sql2.append(" and ").append(id_recorder).append("='").append(userID)
				.append("'");
		sql2.append(" and flag_send = 'file_send_ing' ");
		updateSQL(sql2.toString());

	}

	public static final void updateGroupChatSendFlag(String groupID) {
		// SQLiteDatabase database = beginDMLOnTransaction();
		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();

		sql.append("update   ").append(ctlm1356)
				.append(" set flag_send = 'fail'");
		sql.append(" where ").append(id_sendto).append("='").append(groupID)
				.append("'");
		sql.append(" and ").append(var_contype).append("='groupchat'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");
		sql.append(" and flag_send = 'ing' ");

		updateSQL(sql.toString());
		StringBuffer sql2 = new StringBuffer();

		sql2.append("update   ").append(ctlm1356)
				.append(" set flag_send = 'file_send_fail'");
		sql2.append(" where ").append(id_sendto).append("='").append(groupID)
				.append("'");
		sql2.append(" and ").append(var_contype).append("='groupchat'");
		sql2.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");
		sql2.append(" and flag_send = 'file_send_ing' ");
		updateSQL(sql2.toString());

	}

	/** 插入或更新一批联系人信息 flag:'Y'/'N' */
	public static final void replaceFriendInfos(final List<FriendInfo> friends,
			final char flag) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			for (FriendInfo friend : friends) {
				values.clear();
				values.put(Tables.ContactTable.COL_ID_FRIEND,
						friend.getFriendid());
				values.put(Tables.ContactTable.COL_NAME_FRIEND,
						friend.getFriendname());
				values.put(Tables.ContactTable.COL_VAR_MTEL,
						friend.getFriendmtel());
				values.put(Tables.ContactTable.COL_ID_DEPT, friend.getDeptid());
				values.put(Tables.ContactTable.COL_NAME_DEPT,
						friend.getDeptname());
				values.put(Tables.ContactTable.COL_VAR_IMGFRIEND,
						friend.getFriendimage());
				values.put(Tables.ContactTable.COL_VAR_EMAIL,
						friend.getFriendmail());
				values.put(Tables.ContactTable.COL_FLAG, String.valueOf(flag)
						.toUpperCase(Locale.ENGLISH));
				database.replace(Tables.ContactTable.NAME, null, values);
			}
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 插入或更新一条联系人信息 */
	public static final void replaceFriendInfo(FriendInfo friend, char flag) {
		replaceFriendInfos(Arrays.asList(friend), flag);
	}

	/** 更改联系人表中指定用户非用户id字段的值 */
	public static final void updateFriendInfo(final String userID,
			final String columnName, final String columnValue) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(Tables.ContactTable.NAME).append(" set ");
		sql.append(columnName).append(" = '").append(columnValue).append("' ");
		sql.append("where ").append(Tables.ContactTable.COL_ID_FRIEND)
				.append(" = '").append(userID).append("' ");
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	// 查询一条真正好友信息
	public static final FriendInfo queryFriendInfo(String friendID) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.ContactTable.NAME)
				.append(" where ").append(Tables.ContactTable.COL_FLAG)
				.append(" = 'Y'").append(" and ")
				.append(Tables.ContactTable.COL_ID_FRIEND).append(" = ")
				.append(" '").append(friendID).append("' ");
		ArrayList<FriendInfo> friends = queryContacts(sql.toString());
		if (friends.isEmpty())
			return null;
		return friends.get(0);
	}

	// 查询一条好友信息
	public static final FriendInfo queryFriendInfoall(String friendID) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.ContactTable.NAME)
				.append(" where ")
				// .append(Tables.ContactTable.COL_FLAG).append(" = 'Y'")
				.append(Tables.ContactTable.COL_ID_FRIEND).append(" = ")
				.append(" '").append(friendID).append("' ");
		ArrayList<FriendInfo> friends = queryContacts(sql.toString());
		if (friends.isEmpty())
			return null;
		return friends.get(0);
	}

	// 查询一条好友信息
	public static final FriendInfo queryFriendInfoByPhoneNumber(
			String friendPhone) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.ContactTable.NAME)
				.append(" where ").append(Tables.ContactTable.COL_FLAG)
				.append(" = 'Y'").append(" and ")
				.append(Tables.ContactTable.COL_VAR_MTEL).append(" = ")
				.append(" '").append(friendPhone).append("' ");
		ArrayList<FriendInfo> friends = queryContacts(sql.toString());
		if (friends.isEmpty())
			return null;
		return friends.get(0);
	}

	/** 查询当前用户的所有联系人信息到FriendInfo, flag可以是"Y","N","YN"或者其他 */
	public static final ArrayList<FriendInfo> queryFriendInfos(String flags) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.ContactTable.NAME);
		if (!(flags.contains("Y") && flags.contains("N"))) {
			char flag = 'Y';
			if (flags.contains("N"))
				flag = 'N';
			sql.append(" where ").append(Tables.ContactTable.COL_FLAG)
					.append(" = '").append(flag).append("'");
		}
		sql.append(" order by ").append(Tables.ContactTable.COL_ID_FRIEND);
		return queryContacts(sql.toString());
	}

	/** 搜索功能专用:按关键字搜索联系人好友 */
	public static final ArrayList<FriendInfo> searchFriendInfo(String searchKey) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.ContactTable.NAME)
				.append(" where ");
		sql.append(Tables.ContactTable.COL_NAME_FRIEND).append(" like '%")
				.append(searchKey).append("%' or ");
		sql.append(Tables.ContactTable.COL_VAR_MTEL).append(" like '%")
				.append(searchKey).append("%' or ");
		sql.append(Tables.ContactTable.COL_NAME_DEPT).append(" like '%")
				.append(searchKey).append("%' or ");
		sql.append(Tables.ContactTable.COL_VAR_EMAIL).append(" like '%")
				.append(searchKey).append("%' ");
		return queryContacts(sql.toString());
	}

	// 实际SQL查询联系人表并返回ArrayList<FriendInfo>
	private static final ArrayList<FriendInfo> queryContacts(String sql) {
		ArrayList<FriendInfo> result = new ArrayList<FriendInfo>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				FriendInfo fi = new FriendInfo();
				fi.setDeptid(getColumnString(cursor,
						Tables.ContactTable.COL_ID_DEPT));
				fi.setDeptname(getColumnString(cursor,
						Tables.ContactTable.COL_NAME_DEPT));
				fi.setFriendid(getColumnString(cursor,
						Tables.ContactTable.COL_ID_FRIEND));
				fi.setFriendname(getColumnString(cursor,
						Tables.ContactTable.COL_NAME_FRIEND));
				fi.setFriendimage(getColumnString(cursor,
						Tables.ContactTable.COL_VAR_IMGFRIEND));
				fi.setFriendmail(getColumnString(cursor,
						Tables.ContactTable.COL_VAR_EMAIL));
				fi.setFriendmtel(getColumnString(cursor,
						Tables.ContactTable.COL_VAR_MTEL));
				result.add(fi);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	/** 通过联系人ID删除一条联系人 */
	public static final void deleteContactById(final String id) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(Tables.ContactTable.NAME)
					.append(" where ")
					.append(Tables.ContactTable.COL_ID_FRIEND).append(" = '")
					.append(id).append("' ");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 更改联系人为非好友 */
	public static final void updateContactAsNOFriendById(final String userID) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(Tables.ContactTable.NAME).append(" set ");
		sql.append(Tables.ContactTable.COL_FLAG).append(" = '").append("N")
				.append("' ");
		sql.append("where ").append(Tables.ContactTable.COL_ID_FRIEND)
				.append(" = '").append(userID).append("' ");
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	// TODO 用户表 ctlm1005

	/** 向用户表插入或更新一批用户信息 */
	public static final void replaceUserInfos(final List<UserInfo> users) {

		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.delete(Tables.UserTable.NAME, null, null);
			ContentValues values = new ContentValues();
			for (UserInfo user : users) {
				values.clear();
				values.put(Tables.UserTable.COL_FLAG_AUTOLOGIN,
						user.isAutoLogin);
				values.put(Tables.UserTable.COL_ID_COM, user.companyID);
				values.put(Tables.UserTable.COL_ID_DEPT, user.departmentID);
				values.put(Tables.UserTable.COL_ID_USER, user.userID);
				values.put(Tables.UserTable.COL_NAME_COM, user.companyName);
				values.put(Tables.UserTable.COL_NAME_DEPT, user.departmentName);
				values.put(Tables.UserTable.COL_NAME_USER, user.username);
				values.put(Tables.UserTable.COL_VAR_EMAIL, user.email);
				values.put(Tables.UserTable.COL_VAR_IMGUSER, user.userImage);
				values.put(Tables.UserTable.COL_VAR_MTEL, user.phoneNumber);
				values.put(Tables.UserTable.COL_VAR_SESSION, user.sessionID);
				values.put(Tables.UserTable.COL_DATE_LOGIN, user.lastLoginTime);
				database.replace(Tables.UserTable.NAME, null, values);
			}

		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 向用户表插入或更新一条用户信息 */
	public static final void replaceUserInfo(UserInfo user) {
		replaceUserInfos(Arrays.asList(user));
	}

	/** 更改用户表中指定用户非用户id字段的值 */
	public static final void updateUserInfo(final String userID,
			final String columnName, final String columnValue) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(Tables.UserTable.NAME).append(" set ");
		sql.append(columnName).append(" = '").append(columnValue).append("' ");
		sql.append("where ").append(Tables.UserTable.COL_ID_USER)
				.append(" = '").append(userID).append("' ");
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 查询所有登录过的用户信息到UserInfo */
	public static final ArrayList<UserInfo> queryUserInfos() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.UserTable.NAME)
				.append(" order by ").append(Tables.UserTable.COL_ID_USER);
		return queryUsers(sql.toString());
	}

	/** 查询最近登录的当前用户的信息到UserInfo */
	public static final UserInfo queryCurrentUserInfo() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.UserTable.NAME)
				.append(" order by ").append(Tables.UserTable.COL_DATE_LOGIN)
				.append(" desc limit 0,1");
		ArrayList<UserInfo> list = queryUsers(sql.toString());
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	// 实际SQL查询用户表并返回ArrayList<UserInfo>
	private static final ArrayList<UserInfo> queryUsers(String sql) {
		ArrayList<UserInfo> result = new ArrayList<UserInfo>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				UserInfo ui = new UserInfo();
				ui.companyID = getColumnString(cursor,
						Tables.UserTable.COL_ID_COM);
				ui.companyName = getColumnString(cursor,
						Tables.UserTable.COL_NAME_COM);
				ui.departmentID = getColumnString(cursor,
						Tables.UserTable.COL_ID_DEPT);
				ui.departmentName = getColumnString(cursor,
						Tables.UserTable.COL_NAME_DEPT);
				ui.email = getColumnString(cursor,
						Tables.UserTable.COL_VAR_EMAIL);
				ui.isAutoLogin = getColumnString(cursor,
						Tables.UserTable.COL_FLAG_AUTOLOGIN);
				ui.lastLoginTime = getColumnString(cursor,
						Tables.UserTable.COL_DATE_LOGIN);
				ui.phoneNumber = getColumnString(cursor,
						Tables.UserTable.COL_VAR_MTEL);
				ui.sessionID = getColumnString(cursor,
						Tables.UserTable.COL_VAR_SESSION);
				ui.userID = getColumnString(cursor,
						Tables.UserTable.COL_ID_USER);
				ui.userImage = getColumnString(cursor,
						Tables.UserTable.COL_VAR_IMGUSER);
				ui.username = getColumnString(cursor,
						Tables.UserTable.COL_NAME_USER);
				result.add(ui);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	// TODO 企信表 ctlm1356

	/** 插入一批历史聊天消息到企信表 */
	public static final int insertMsgsL(final List<ChatHisBean> chbs) {
		int effectCount = 0;
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			for (ChatHisBean chb : chbs) {
				StringBuffer sql = new StringBuffer();
				sql.append("REPLACE into ")
						.append(Tables.EnterpriseInfoTable.NAME).append(" ( ");
				sql.append(Tables.EnterpriseInfoTable.COL_DATE_SEND).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_DATE_OPR)
						.append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_GROUP)
						.append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_MESSAGE).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_SENDFROM).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_SENDTO).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_VAR_CONTENT).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_VAR_CONTYPE).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FILE_ID).append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_SCENE).append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_TYPE).append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FLAG_READ).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_RECORDER).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FLAG_SEND).append(
						") ");

				SimpleDateFormat df3 = new SimpleDateFormat("yyyyMMddHHmmssSS");
				Long time = Long.parseLong(chb.getMsgTime());
				String date1 = df3.format(new Date(time * 1000L));

				String date2 = DateUtil.getCurrentTime();
				int year1 = Integer.parseInt(date1.substring(0, 4));
				int year2 = Integer.parseInt(date2.substring(0, 4));
				year1 = year1 - (year2 - year1 + 1);
				date1 = String.valueOf(year1) + date1.substring(4);
				;

				StringBuffer args = new StringBuffer();

				args.append("'").append(chb.getMsgTime()).append("',");
				args.append("'").append(date1).append("',");
				args.append("'").append(chb.getMsgGroup()).append("',");
				args.append("'").append(chb.getId()).append("',");
				args.append("'").append(chb.getMsgFrom()).append("',");
				args.append("'").append(chb.getMsgTo()).append("',");
				args.append("'").append(chb.getMsgcontent()).append("',");
				args.append("'").append(chb.getMsgType()).append("',");
				args.append("'").append(chb.getmsgIdFile()).append("',");
				args.append("'").append(chb.getmsgTypeFile()).append("',");
				args.append("'").append(chb.getMsgIdType()).append("',");
				args.append("'").append("N").append("',");
				args.append("'").append(chb.getmsgSendStatus()).append("'");

				// boolean isGroupMsg =
				// ChatConstants.msg.TYPE_GROUPCHAT.equals(chb.getMsgType());
				// if(isGroupMsg)
				// {
				// sql.append(" select ").append(args.toString()).append(" where exists (select ");
				// sql.append(Tables.GroupInfoTable.COL_ID_GROUP).append(" from ").append(Tables.GroupInfoTable.NAME);
				// sql.append(" where ").append(Tables.GroupInfoTable.COL_ID_GROUP).append(" = '").append(chb.getMsgTo()).append("') ");
				// }else{
				// sql.append(" values (").append(args.toString()).append(") ");
				// }
				sql.append(" values (").append(args.toString()).append(") ");
				SQLiteStatement statement = database.compileStatement(sql
						.toString());
				if (statement.executeInsert() != -1)
					++effectCount;
			}
		} finally {
			endDMLOffTransaction(database);
		}
		return effectCount;
	}

	/** 插入一条聊天消息到企信表 */
	public static final int insertMsgN(ChatHisBean chb) {
		return insertMsgsN(Arrays.asList(chb));
	}

	/** 插入一批聊天消息到企信表 */
	public static final int insertMsgsN(final List<ChatHisBean> chbs) {
		int effectCount = 0;
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			for (ChatHisBean chb : chbs) {
				StringBuffer sql = new StringBuffer();
				sql.append("REPLACE into ")
						.append(Tables.EnterpriseInfoTable.NAME).append(" ( ");
				sql.append(Tables.EnterpriseInfoTable.COL_DATE_SEND).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_DATE_OPR)
						.append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_GROUP)
						.append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_MESSAGE).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_SENDFROM).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_SENDTO).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_VAR_CONTENT).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_VAR_CONTYPE).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FILE_ID).append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_SCENE).append(", ");// 聊天附件类型
				sql.append(Tables.EnterpriseInfoTable.COL_ID_TYPE).append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_RECORDER).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FLAG_READ).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FLAG_SEND).append(
						") ");// 发送状态

				StringBuffer args = new StringBuffer();
				args.append("'").append(chb.getMsgTime()).append("',");
				args.append("'").append(DateUtil.getCurrentTimeM())
						.append("',");
				args.append("'").append(chb.getMsgGroup()).append("',");
				args.append("'").append(chb.getId()).append("',");
				args.append("'").append(chb.getMsgFrom()).append("',");
				args.append("'").append(chb.getMsgTo()).append("',");
				args.append("'").append(chb.getMsgcontent()).append("',");
				args.append("'").append(chb.getMsgType()).append("',");
				args.append("'").append(chb.getmsgIdFile()).append("',");
				args.append("'").append(chb.getmsgTypeFile()).append("',");// 聊天附件类型
				args.append("'").append(chb.getMsgIdType()).append("',");
				args.append("'").append(chb.getMsgIdRecorder()).append("',");
				args.append("'").append("Y").append("',");
				args.append("'").append(chb.getmsgSendStatus()).append("'");// 发送状态
				sql.append(" values (").append(args.toString()).append(") ");
				SQLiteStatement statement = database.compileStatement(sql
						.toString());
				Log.v("show","消息的信息:"+sql.toString());
				if (statement.executeInsert() != -1)
					++effectCount;
			}
		} finally {
			endDMLOffTransaction(database);
		}
		return effectCount;
	}

	/** 插入一批聊天消息到企信表 */
	public static final int insertMsgs(final List<ChatHisBean> chbs) {
		int effectCount = 0;
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			for (ChatHisBean chb : chbs) {
				StringBuffer sql = new StringBuffer();
				sql.append("REPLACE into ")
						.append(Tables.EnterpriseInfoTable.NAME).append(" ( ");
				sql.append(Tables.EnterpriseInfoTable.COL_DATE_SEND).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_DATE_OPR)
						.append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_GROUP)
						.append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_MESSAGE).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_SENDFROM).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_SENDTO).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_VAR_CONTENT).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_VAR_CONTYPE).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FILE_ID).append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_SCENE).append(", ");// 聊天附件类型
				sql.append(Tables.EnterpriseInfoTable.COL_ID_TYPE).append(", ");
				sql.append(Tables.EnterpriseInfoTable.COL_ID_RECORDER).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FLAG_READ).append(
						", ");
				sql.append(Tables.EnterpriseInfoTable.COL_FLAG_SEND).append(
						", ");// 发送状态
				sql.append(Tables.EnterpriseInfoTable.COL_FLAG_PALY).append(
						") ");
				StringBuffer args = new StringBuffer();
				args.append("'").append(chb.getMsgTime()).append("',");
				args.append("'").append(DateUtil.getCurrentTimeM())
						.append("',");
				args.append("'").append(chb.getMsgGroup()).append("',");
				args.append("'").append(chb.getId()).append("',");
				args.append("'").append(chb.getMsgFrom()).append("',");
				args.append("'").append(chb.getMsgTo()).append("',");
				args.append("'").append(chb.getMsgcontent()).append("',");
				args.append("'").append(chb.getMsgType()).append("',");
				args.append("'").append(chb.getmsgIdFile()).append("',");
				args.append("'").append(chb.getmsgTypeFile()).append("',");// 聊天附件类型
				args.append("'").append(chb.getMsgIdType()).append("',");
				args.append("'").append(chb.getMsgIdRecorder()).append("',");
				args.append("'").append("N").append("',");
				args.append("'").append(chb.getmsgSendStatus()).append("',");// 发送状态
				args.append("'N'");// 发送状态
				sql.append(" values (").append(args.toString()).append(") ");
				SQLiteStatement statement = database.compileStatement(sql
						.toString());
				if (statement.executeInsert() != -1)
					++effectCount;
			}
		} catch (Exception e) {
			Log.i("info", "插入一批聊天消息到企信表异常：" + e.toString());
		} finally {
			endDMLOffTransaction(database);
		}
		return effectCount;
	}

	/** 插入一条聊天消息到企信表 */
	public static final int insertMsg(ChatHisBean chb) {
		return insertMsgs(Arrays.asList(chb));
	}

	/** 把单聊新信息改为已读 */
	public static final void updateSingleChatMsgFlagRead(final String userID) {
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(Tables.EnterpriseInfoTable.NAME)
				.append(" set ");
		sql.append(Tables.EnterpriseInfoTable.COL_FLAG_READ).append(" = '")
				.append("Y").append("' ");
		sql.append("where (")
				.append(Tables.EnterpriseInfoTable.COL_ID_SENDFROM)
				.append(" = '").append(userID).append("' ").append("or ")
				.append(Tables.EnterpriseInfoTable.COL_ID_SENDTO)
				.append(" = '").append(userID).append("' ");
		sql.append(" ) and ")
				.append(Tables.EnterpriseInfoTable.COL_VAR_CONTYPE)
				.append("='chat'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 把群聊新信息改为已读 */
	public static final void updateGroupChatMsgFlagRead(final String groupID) {
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(Tables.EnterpriseInfoTable.NAME)
				.append(" set ");
		sql.append(Tables.EnterpriseInfoTable.COL_FLAG_READ).append(" = '")
				.append("Y").append("' ");
		sql.append("where ( ").append(Tables.EnterpriseInfoTable.COL_ID_SENDTO)
				.append(" = '").append(groupID).append("' ");
		sql.append(" ) and ")
				.append(Tables.EnterpriseInfoTable.COL_VAR_CONTYPE)
				.append("='groupchat'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 查询指定用户ID的所有聊天记录 */
	public static final ArrayList<ChatHisBean> queryChatHisBeansByUserID(
			String userID) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.EnterpriseInfoTable.NAME)
				.append(" where ")
				.append(Tables.EnterpriseInfoTable.COL_ID_SENDFROM)
				.append("='").append(userID).append("' order by ")
				.append(Tables.EnterpriseInfoTable.COL_DATE_SEND);
		return queryEnterpriseInfos(sql.toString());
	}

	/** 查询单聊新消息数量 */
	public static final int queryChatNewChatMsgCounts() {
		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*)  from ").append(ctlm1356);
		sql.append(" where ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("' and flag_read <> 'Y' ");

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql.toString(), null);
			if (cursor.moveToFirst()) {
				return (cursor.getInt(0));
			} else {
				return 0;
			}
		} finally {
			endDQL(database, cursor);
		}
	}

	/** 查询单聊新消息数量 */
	public static final int querySingleChatNewChatMsgCounts(String userID,
			String friendId) {

		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendfrom = Tables.EnterpriseInfoTable.COL_ID_SENDFROM;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*)  from ").append(ctlm1356);
		sql.append(" where ((").append(id_sendfrom).append("='").append(userID)
				.append("' and ").append(id_sendto).append("='")
				.append(friendId).append("')");
		sql.append(" or (").append(id_sendto).append("='").append(userID)
				.append("' and ").append(id_sendfrom).append("='")
				.append(friendId).append("'))");
		sql.append(" and (").append(var_contype).append("='chat')");
		sql.append(" and (").append(Tables.EnterpriseInfoTable.COL_FLAG_READ)
				.append("='").append("N')");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql.toString(), null);
			if (cursor.moveToFirst()) {
				return (cursor.getInt(0));
			} else {
				return 0;
			}
		} finally {
			endDQL(database, cursor);
		}

	}

	/** 查询群聊新消息数量 */
	public static final int queryGroupChatNewChatMsgCounts(String groupID) {
		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*)  from ").append(ctlm1356);
		sql.append(" where ").append(id_sendto).append("='").append(groupID)
				.append("'");
		sql.append(" and ").append(var_contype).append("='groupchat'");
		sql.append(" and ").append(Tables.EnterpriseInfoTable.COL_FLAG_READ)
				.append("='").append("N'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql.toString(), null);
			if (cursor.moveToFirst()) {
				return (cursor.getInt(0));
			} else {
				return 0;
			}
		} finally {
			endDQL(database, cursor);
		}
	}

	/** 查询指定朋友ID和自己的所有未读的对话记录 */
	public static final ArrayList<ChatHisBean> queryChatHisBeansBySingleChatNoRead(
			String userID, String friendId) {
		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendfrom = Tables.EnterpriseInfoTable.COL_ID_SENDFROM;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String date_send = Tables.EnterpriseInfoTable.COL_DATE_SEND;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String var_flag_read = Tables.EnterpriseInfoTable.COL_FLAG_READ;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(ctlm1356);
		sql.append(" where (").append(id_sendfrom).append("='").append(userID)
				.append("' and ").append(id_sendto).append("='")
				.append(friendId).append("')");
		sql.append(" or (").append(id_sendto).append("='").append(userID)
				.append("' and ").append(id_sendfrom).append("='")
				.append(friendId).append("')");
		sql.append(" and ").append(var_contype).append("='chat'");
		sql.append(" and ").append(var_flag_read).append("='N'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");
		sql.append(" order by ").append(" date_opr").append(" asc ");
		return queryEnterpriseInfos(sql.toString());
	}

	/** 查询指定朋友ID和自己的所有对话记录 */
	public static final ArrayList<ChatHisBean> queryChatHisBeansBySingleChat(
			String userID, String friendId, int offSet) {
		int rownum = 0;
		int pagerownum = 10;
		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendfrom = Tables.EnterpriseInfoTable.COL_ID_SENDFROM;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String date_send = Tables.EnterpriseInfoTable.COL_DATE_OPR;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(ctlm1356);
		sql.append(" where ((").append(id_sendfrom).append("='").append(userID)
				.append("' and ").append(id_sendto).append("='")
				.append(friendId).append("')");
		sql.append(" or (").append(id_sendto).append("='").append(userID)
				.append("' and ").append(id_sendfrom).append("='")
				.append(friendId).append("'))");
		sql.append(" and ").append(var_contype).append("='chat'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");

		// 数据记录数为0时查询出表里最大记录数

		StringBuffer sql2 = new StringBuffer();
		sql2.append("select count(*) rownum from ").append(ctlm1356);
		sql2.append(" where ((").append(id_sendfrom).append("='")
				.append(userID).append("' and ").append(id_sendto).append("='")
				.append(friendId).append("')");
		sql2.append(" or (").append(id_sendto).append("='").append(userID)
				.append("' and ").append(id_sendfrom).append("='")
				.append(friendId).append("'))");
		sql2.append(" and ").append(var_contype).append("='chat'");
		sql2.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql2.toString(), null);
			while (cursor.moveToNext()) {
				rownum = cursor.getInt(cursor.getColumnIndex("rownum"));//
			}
		} finally {
			endDQL(database, cursor);
		}

		if (rownum == offSet)
			return null;

		if (rownum > 10) {
			if (pagerownum > rownum - offSet) {
				pagerownum = rownum - offSet;
			}
			if (offSet > 0) {
				rownum = rownum - offSet - 10;
			} else {

				rownum = rownum - 10;
			}
			sql.append(" order by ")
					.append(date_send)
					.append(" asc    limit " + String.valueOf(pagerownum)
							+ " offset    " + String.valueOf(rownum));
		} else {
			sql.append(" order by ").append(date_send).append(" asc   ");
		}
		return queryEnterpriseInfos(sql.toString());
	}

	/** 查询指定群的所有未读对话记录 */
	public static final ArrayList<ChatHisBean> queryChatHisBeansByGroupChatNoRead(
			String groupID) {
		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String date_send = Tables.EnterpriseInfoTable.COL_DATE_OPR;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String var_flag_read = Tables.EnterpriseInfoTable.COL_FLAG_READ;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(ctlm1356);
		sql.append(" where ").append(id_sendto).append("='").append(groupID)
				.append("'");
		sql.append(" and ").append(var_contype).append("='groupchat'");
		sql.append(" and ").append(var_flag_read).append("='N'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");
		sql.append(" order by ").append(date_send).append(" asc ");
		return queryEnterpriseInfos(sql.toString());
	}

	/** 查询指定群的所有对话记录 */

	public static final ArrayList<ChatHisBean> queryChatHisBeansByGroupChat(
			String groupID, int offSet) {
		int rownum = 0;
		int pagerownum = 10;

		String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
		String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
		String date_send = Tables.EnterpriseInfoTable.COL_DATE_OPR;
		String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(ctlm1356);
		sql.append(" where ").append(id_sendto).append("='").append(groupID)
				.append("'");
		sql.append(" and ").append(var_contype).append("='groupchat'");
		sql.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");

		// 数据记录数为0时查询出表里最大记录数

		StringBuffer sql2 = new StringBuffer();
		sql2.append("select count(*) rownum from ")
				.append(ctlm1356)
				.append(" where ")
				.append(id_sendto)
				.append("='")
				.append(groupID)
				.append("'")
				.append(" and ")
				.append(var_contype)
				.append("='groupchat'")
				.append(" and ")
				.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'");

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql2.toString(), null);
			while (cursor.moveToNext()) {
				rownum = cursor.getInt(cursor.getColumnIndex("rownum"));//
			}
		} finally {
			endDQL(database, cursor);
		}

		if (rownum == offSet)
			return null;

		if (rownum > 10) {
			if (pagerownum > rownum - offSet) {
				pagerownum = rownum - offSet;
			}
			if (offSet > 0) {
				rownum = rownum - offSet - 10;
			} else {

				rownum = rownum - 10;
			}
			sql.append(" order by ")
					.append(date_send)
					.append(" asc    limit " + String.valueOf(pagerownum)
							+ " offset    " + String.valueOf(rownum));
		} else {
			sql.append(" order by ").append(date_send).append(" asc ");
		}

		return queryEnterpriseInfos(sql.toString());
	}

	/** 从企信表中取出每个用户ID的最近一条聊天信息 */
	public static final ArrayList<ChatHisBean> queryLastChatHisBeans() {
		StringBuffer sql = new StringBuffer();
		String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
		sql.append("select ctlm1356.* ");
		sql.append(" from ctlm1356 ,  ");
		sql.append("( select max(date_opr) date_opr, id_sendto, id_sendfrom,var_contype from  ");
		sql.append("( select   date_opr ,case when   id_sendfrom = '"
				+ SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId()
				+ "' then id_sendto else id_sendfrom end id_sendfrom  "
				+ " ,case when id_sendto = '"
				+ SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId()
				+ "' then id_sendfrom else id_sendto end  id_sendto ,var_contype ");
		sql.append(" from  ctlm1356  where  var_contype ='chat'  and ");
		sql.append(id_recorder)
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("'  ) a ");
		sql.append(" group by    id_sendfrom ,var_contype,id_sendto ");
		sql.append(" )  a  ");
		sql.append(" where ctlm1356.var_contype = a.var_contype ");
		sql.append(" and  ctlm1356.date_opr  =a.date_opr ");
		sql.append(" and case when  ctlm1356.id_sendfrom   = '"
				+ SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId()
				+ "' then   ctlm1356.id_sendto  else ctlm1356.id_sendfrom end =  a.id_sendfrom ");
		sql.append(" and case when  ctlm1356.id_sendfrom   = '"
				+ SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId()
				+ "' then   ctlm1356.id_sendto  else ctlm1356.id_sendfrom end =  a.id_sendfrom ");
		sql.append(" and case when  ctlm1356.id_sendto   = '"
				+ SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId()
				+ "' then   ctlm1356.id_sendfrom  else ctlm1356.id_sendto end =  a.id_sendto ");
		sql.append(" and ctlm1356.id_recorder")
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("' ");

		sql.append(" union all select ctlm1356.*  from ctlm1356 , ");
		sql.append("  ( select  max(date_opr)  date_opr ,  id_sendto,var_contype ");
		sql.append("  from  ctlm1356    where var_contype = 'groupchat' ");
		sql.append("  group by    var_contype,id_sendto )  a  ");
		sql.append("  where ctlm1356.var_contype = a.var_contype ");
		sql.append("  and  ctlm1356.date_opr  =a.date_opr ");
		sql.append("  and ctlm1356.id_sendto =  a.id_sendto   ");
		sql.append(" and ctlm1356.id_recorder")
				.append("='")
				.append(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyUserId())
				.append("' ");

		return queryEnterpriseInfos(sql.toString());
	}

	// 实际SQL查询企信表并返回ArrayList<ChatHisBean>
	private static final ArrayList<ChatHisBean> queryEnterpriseInfos(String sql) {
		String showTime = "";
		String myID = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyUserId();
		ArrayList<ChatHisBean> result = new ArrayList<ChatHisBean>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ChatHisBean chb = new ChatHisBean();
				chb.setId(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_ID_MESSAGE));
				chb.setMsgcontent(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_VAR_CONTENT));
				chb.setMsgFrom(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_ID_SENDFROM));
				chb.setMsgGroup(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_ID_GROUP));
				chb.setMsgTime(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_DATE_SEND));
				chb.setMsgType(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_VAR_CONTYPE));
				chb.setMsgTo(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_ID_SENDTO));
				chb.setmsgIdFile(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_FILE_ID));
				chb.setmsgTypeFile(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_SCENE));
				chb.setMsgIdRecorder(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_ID_RECORDER));
				chb.setMsgIdType(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_ID_TYPE));
				chb.setmsgSendStatus(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_FLAG_SEND));
				chb.setMsgFlagPaly(getColumnString(cursor,
						Tables.EnterpriseInfoTable.COL_FLAG_PALY));

				if (ChatConstants.msg.TYPE_ID_IQ.equalsIgnoreCase(chb
						.getMsgIdType())) {
					chb.setType(ChatConstants.VALUE_NOTICE);// 提示
				} else if ((!myID.equalsIgnoreCase(chb.getMsgFrom()))
						&& (TextUtils.isEmpty(chb.getmsgIdFile()) || TextUtils
								.isEmpty(chb.getmsgIdFile().trim()))) {
					chb.setType(ChatConstants.VALUE_LEFT_TEXT);// 别人发的文本
				} else if (!myID.equalsIgnoreCase(chb.getMsgFrom())
						&& !TextUtils.isEmpty(chb.getmsgIdFile())
						&& chb.getmsgIdFile().length() > 2
						&& Constant.FILE_TYPE_PIC.equalsIgnoreCase(chb
								.getmsgTypeFile())) {
					chb.setType(ChatConstants.VALUE_LEFT_IMAGE);// 别人发的图片
				} else if (!myID.equalsIgnoreCase(chb.getMsgFrom())
						&& !TextUtils.isEmpty(chb.getmsgIdFile())
						&& chb.getmsgIdFile().length() > 2
						&& Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(chb
								.getmsgTypeFile())) {
					chb.setType(ChatConstants.VALUE_LEFT_AUDIO);// 别人发的语音
				} else if (myID.equalsIgnoreCase(chb.getMsgFrom())
						&& (TextUtils.isEmpty(chb.getmsgIdFile()) || TextUtils
								.isEmpty(chb.getmsgIdFile().trim()))) {
					chb.setType(ChatConstants.VALUE_RIGHT_TEXT);// 我发送的文本
				} else if (myID.equalsIgnoreCase(chb.getMsgFrom())
						&& !TextUtils.isEmpty(chb.getmsgIdFile())
						&& chb.getmsgIdFile().length() > 2
						&& Constant.FILE_TYPE_PIC.equalsIgnoreCase(chb
								.getmsgTypeFile())) {
					chb.setType(ChatConstants.VALUE_RIGHT_IMAGE);// 我发的图片
				} else if (myID.equalsIgnoreCase(chb.getMsgFrom())
						&& !TextUtils.isEmpty(chb.getmsgIdFile())
						&& chb.getmsgIdFile().length() > 2
						&& Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(chb
								.getmsgTypeFile())) {
					chb.setType(ChatConstants.VALUE_RIGHT_AUDIO);// 我发的语音
				} else if (myID.equalsIgnoreCase(chb.getMsgFrom())//我发的位置
						&& !TextUtils.isEmpty(chb.getmsgIdFile())
						&& chb.getmsgIdFile().length() > 2
						&& Constant.FILE_TYPE_LOCATION.equalsIgnoreCase(chb
								.getmsgTypeFile())) {
					chb.setType(ChatConstants.VALUE_RIGHT_LOCATION);// 
				} else if (!myID.equalsIgnoreCase(chb.getMsgFrom())//别人发的位置
						&& !TextUtils.isEmpty(chb.getmsgIdFile())
						&& chb.getmsgIdFile().length() > 2
						&& Constant.FILE_TYPE_LOCATION.equalsIgnoreCase(chb
								.getmsgTypeFile())) {
					chb.setType(ChatConstants.VALUE_LEFT_LOCATION);//
				}

				if ("".equalsIgnoreCase(showTime)) {
					chb.setShowTime(true);
				} else {
					long lasttime = Long.valueOf(showTime);
					long currenttime = Long.valueOf(chb.getMsgTime().substring(
							0, 13));
					if (currenttime - lasttime < 80000) {
						chb.setShowTime(false);
					} else {
						chb.setShowTime(true);
					}
				}
				showTime = chb.getMsgTime().substring(0, 13);
				result.add(chb);
			}
		}catch(Exception e){
			Log.i("info", "查询异常："+e.toString());
		}
		finally {
			endDQL(database, cursor);
		}
		return result;
	}

	/** 按照用户id和好友id删除单人聊天记录 */
	public static final void deleteSingleMsgById(final String userID,
			final String friendId) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
			String id_sendfrom = Tables.EnterpriseInfoTable.COL_ID_SENDFROM;
			String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
			String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
			String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;

			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(ctlm1356).append(" where ((")
					.append(id_sendfrom).append(" = '").append(userID)
					.append("' and ").append(id_sendto).append(" = '")
					.append(friendId).append("') or (").append(id_sendfrom)
					.append(" = '").append(friendId).append("' and ")
					.append(id_sendto).append(" = '").append(userID)
					.append("')) and ").append(var_contype).append(" = 'chat'");
			sql.append(" and ")
					.append(id_recorder)
					.append("='")
					.append(SharePreferenceUtil.getInstance(
							EapApplication.getApplication()
									.getApplicationContext()).getMyUserId())
					.append("'");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 删除指定群的所有聊天记录 */
	public static final void deleteGroupMsgById(final String groupID) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			String ctlm1356 = Tables.EnterpriseInfoTable.NAME;
			String id_sendto = Tables.EnterpriseInfoTable.COL_ID_SENDTO;
			String var_contype = Tables.EnterpriseInfoTable.COL_VAR_CONTYPE;
			String id_recorder = Tables.EnterpriseInfoTable.COL_ID_RECORDER;
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(ctlm1356).append(" where ")
					.append(id_sendto).append(" = '").append(groupID)
					.append("' and ").append(var_contype)
					.append(" = 'groupchat'");
			sql.append(" and ")
					.append(id_recorder)
					.append("='")
					.append(SharePreferenceUtil.getInstance(
							EapApplication.getApplication()
									.getApplicationContext()).getMyUserId())
					.append("'");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static void updateMsgSendStatue(String msgId, String sendStatue) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("update ").append(Tables.EnterpriseInfoTable.NAME)
					.append(" set ")
					.append(Tables.EnterpriseInfoTable.COL_FLAG_SEND)
					.append(" = '").append(sendStatue).append("' ")
					.append(" where ")
					.append(Tables.EnterpriseInfoTable.COL_ID_MESSAGE)
					.append(" = '").append(msgId).append("'");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static void updateMsgPaly(String msgId, String sendStatue) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("update ").append(Tables.EnterpriseInfoTable.NAME)
					.append(" set ")
					.append(Tables.EnterpriseInfoTable.COL_FLAG_PALY)
					.append(" = '").append(sendStatue).append("' ")
					.append(" where ")
					.append(Tables.EnterpriseInfoTable.COL_ID_MESSAGE)
					.append(" = '").append(msgId).append("'");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	// // 设置图片属性由发送中变为真正的属性
	// public static final void updatePicMsgSendStatue(
	// String id, String sendStatus) {
	// SQLiteDatabase database = beginDMLOnTransaction();
	// try {
	// StringBuffer sql = new StringBuffer();
	// sql.append("update ").append(Tables.EnterpriseInfoTable.NAME)
	// .append(" set ")
	// .append(Tables.EnterpriseInfoTable.COL_FLAG_SEND)
	// .append(" = '").append(sendStatus).append("' ")
	// .append(" where ")
	// .append(Tables.EnterpriseInfoTable.COL_ID_MESSAGE)
	// .append(" = '").append(id).append("'");
	// database.execSQL(sql.toString());
	// } finally {
	// endDMLOffTransaction(database);
	// }
	// }

	// 聊天图片发送失败
	public static final void updatePicMsgStatueSendFail(final String time,
			String picID) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("update ").append(Tables.EnterpriseInfoTable.NAME)
					.append(" set ")
					.append(Tables.EnterpriseInfoTable.COL_SCENE)
					.append(" = '").append("fail").append("' ")
					.append(" where ")
					.append(Tables.EnterpriseInfoTable.COL_DATE_SEND)
					.append(" = '").append(time).append("'").append(" and ")
					.append(Tables.EnterpriseInfoTable.COL_FILE_ID)
					.append(" = '").append(picID).append("'");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 删除指定的所有聊天记录 */
	public static final void deleteMsgByTime(final String time) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			String ctlm1356 = Tables.EnterpriseInfoTable.NAME;

			String var_time = Tables.EnterpriseInfoTable.COL_DATE_SEND;
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(ctlm1356).append(" where ")
					.append(var_time).append(" = '").append(time).append("'");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	// TODO 群组信息表 ctlm1357

	/** 插入一批群组信息到群组信息表 */
	public static final void replaceGroupInfos(final List<GroupInfo> gis) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			for (GroupInfo gi : gis) {
				values.clear();
				values.put(Tables.GroupInfoTable.COL_ID_GROUP, gi.groupID);
				values.put(Tables.GroupInfoTable.COL_NAME_GROUP, gi.groupName);
				values.put(Tables.GroupInfoTable.COL_ID_GROUPTYPE, gi.groupType);
				values.put(Tables.GroupInfoTable.COL_VAR_GROUPIMAGE,
						gi.groupImage);
				values.put(Tables.GroupInfoTable.COL_ID_RECORDER, gi.groupLord);
				database.replace(Tables.GroupInfoTable.NAME, null, values);
			}
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 插入一条群组信息到群组信息表 */
	public static final void replaceGroupInfo(GroupInfo gi) {
		replaceGroupInfos(Arrays.asList(gi));
	}

	/** 删除一个群信息 */
	public static final void deleteGroupInfoById(final String groupID) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(Tables.EnterpriseInfoTable.COL_ID_GROUP).append(" = ? ");
			database.delete(Tables.GroupInfoTable.NAME, sql.toString(),
					new String[] { groupID });
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 查询所有可用的组信息,按照组名称排序返回 */
	public static final ArrayList<GroupInfo> queryAllGroupInfo() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.GroupInfoTable.NAME).append(" where ").append(Tables.GroupInfoTable.COL_ID_GROUP).append(" != 'sys'");
		sql.append(" order by ").append(Tables.GroupInfoTable.COL_NAME_GROUP);
		return queryGroupInfos(sql.toString());
	}

	// 实际SQL查询群组信息表并返回ArrayList<GroupInfo>
	private static final ArrayList<GroupInfo> queryGroupInfos(String sql) {
		ArrayList<GroupInfo> result = new ArrayList<GroupInfo>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				GroupInfo gi = new GroupInfo();
				gi.groupID = getColumnString(cursor,
						Tables.GroupInfoTable.COL_ID_GROUP);
				gi.groupName = getColumnString(cursor,
						Tables.GroupInfoTable.COL_NAME_GROUP);
				gi.groupType = getColumnString(cursor,
						Tables.GroupInfoTable.COL_ID_GROUPTYPE);
				gi.groupImage = getColumnString(cursor,
						Tables.GroupInfoTable.COL_VAR_GROUPIMAGE);
				gi.groupLord = getColumnString(cursor,
						Tables.GroupInfoTable.COL_ID_RECORDER);
				result.add(gi);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	// 查询一条群组信息
	public static final GroupInfo queryGroupInfo(String groupID) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.GroupInfoTable.NAME)
				.append(" where ").append(Tables.GroupInfoTable.COL_ID_GROUP)
				.append(" = '").append(groupID).append("' ");
		ArrayList<GroupInfo> groups = queryGroupInfos(sb.toString());
		if (groups.isEmpty())
			return null;
		return groups.get(0);
	}

	// TODO 群组关系表 ctlm1358

	/** 插入一条群组关系到群组关系表 */
	public static final void insertGroupRelation(final String groupID,
			final String[] userIDs) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			for (String user : userIDs) {
				values.clear();
				values.put(Tables.GroupRelationTable.COL_ID_GROUP, groupID);
				values.put(Tables.GroupRelationTable.COL_ID_USER, user);
				database.replace(Tables.GroupRelationTable.NAME, null, values);
				// database.insertWithOnConflict(Tables.GroupRelationTable.NAME,
				// null, values, SQLiteDatabase.CONFLICT_IGNORE);
			}
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 插入一批群组关系到群组关系表 */
	public static final void insertGroupRelations(
			final List<Map<String, String>> items) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			for (Map<String, String> map : items) {
				values.clear();
				values.put(Tables.GroupRelationTable.COL_ID_GROUP,
						map.get(ChatConstants.iq.DATA_KEY_GROUP_ID));
				values.put(Tables.GroupRelationTable.COL_ID_USER,
						map.get(ChatConstants.iq.DATA_KEY_USER_ID));
				database.insertWithOnConflict(Tables.GroupRelationTable.NAME,
						null, values, SQLiteDatabase.CONFLICT_IGNORE);
			}
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 删除群关系 */
	public static final void deleteGroupRelations(final String groupID,
			final String... userIDs) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(Tables.GroupRelationTable.COL_ID_GROUP).append(" = ? ")
					.append(" and ")
					.append(Tables.GroupRelationTable.COL_ID_USER)
					.append(" = ? ");
			String where = sql.toString();
			for (String userID : userIDs)
				database.delete(Tables.GroupRelationTable.NAME, where,
						new String[] { groupID, userID });
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 按照群组id查询所有组成员 */
	public static final String[] queryGroupRelations(String groupID) {
		ArrayList<String> result = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(Tables.GroupRelationTable.COL_ID_USER)
				.append(" from ").append(Tables.GroupRelationTable.NAME)
				.append(" where ")
				.append(Tables.GroupRelationTable.COL_ID_GROUP).append("='")
				.append(groupID).append("'");
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext())
				result.add(getColumnString(cursor,
						Tables.GroupRelationTable.COL_ID_USER));
		} finally {
			endDQL(database, cursor);
		}
		String[] strs = new String[result.size()];
		return result.toArray(strs);
	}

	// TODO ctlm13550 临时联系人表

	/** 插入一条临时联系人 */
	public static final void replaceTempContactInfo(final VerfifyFriendInfo tci) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			values.put(Tables.TempContactTable.COL_ID_COM, tci.getComid());
			values.put(Tables.TempContactTable.COL_ID_DEPT, tci.getDeptid());
			values.put(Tables.TempContactTable.COL_NAME_DEPT, tci.getDeptname());
			values.put(Tables.TempContactTable.COL_ID_USER, tci.getFriendid());
			values.put(Tables.TempContactTable.COL_VAR_IMGUSER,
					tci.getFriendimage());
			values.put(Tables.TempContactTable.COL_VAR_EMAIL,
					tci.getFriendmail());
			values.put(Tables.TempContactTable.COL_VAR_EQUTYPE,
					tci.getFriendOS());
			values.put(Tables.TempContactTable.COL_NAME_USER,
					tci.getFriendname());
			values.put(Tables.TempContactTable.COL_VAR_MTEL,
					tci.getFriendmtel());
			values.put(Tables.TempContactTable.COL_VERFIFY_NOTE,
					tci.getVerfifyNote());
			values.put(Tables.TempContactTable.COL_VERFIFY_TYPE,
					tci.getVerfifyType());
			values.put(Tables.TempContactTable.COL_VERFIFY_RESULT,
					tci.getVerfifyResult());
			values.put(Tables.TempContactTable.COL_FLAG_READ, "N");
			database.replace(Tables.TempContactTable.NAME, null, values);
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 插入一条临时联系人 */
	public static final void replaceTempContactInfoN(final VerfifyFriendInfo tci) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			values.put(Tables.TempContactTable.COL_ID_COM, tci.getComid());
			values.put(Tables.TempContactTable.COL_ID_DEPT, tci.getDeptid());
			values.put(Tables.TempContactTable.COL_NAME_DEPT, tci.getDeptname());
			values.put(Tables.TempContactTable.COL_ID_USER, tci.getFriendid());
			values.put(Tables.TempContactTable.COL_VAR_IMGUSER,
					tci.getFriendimage());
			values.put(Tables.TempContactTable.COL_VAR_EMAIL,
					tci.getFriendmail());
			values.put(Tables.TempContactTable.COL_VAR_EQUTYPE,
					tci.getFriendOS());
			values.put(Tables.TempContactTable.COL_NAME_USER,
					tci.getFriendname());
			values.put(Tables.TempContactTable.COL_VAR_MTEL,
					tci.getFriendmtel());
			values.put(Tables.TempContactTable.COL_VERFIFY_NOTE,
					tci.getVerfifyNote());
			values.put(Tables.TempContactTable.COL_VERFIFY_TYPE,
					tci.getVerfifyType());
			values.put(Tables.TempContactTable.COL_VERFIFY_RESULT,
					tci.getVerfifyResult());
			values.put(Tables.TempContactTable.COL_FLAG_READ, "Y");
			database.replace(Tables.TempContactTable.NAME, null, values);
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 删除一条临时联系人 */
	public static final void deleteTempContactInfoById(final String userID) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.delete(Tables.TempContactTable.NAME,
					Tables.TempContactTable.COL_ID_USER + " = ? ",
					new String[] { userID });
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 将指定ID的临时联系人转入正式联系人表 */
	public static final void shiftTempContactInfoById(String userID) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("replace into ").append(Tables.ContactTable.NAME)
					.append(" ( ").append(Tables.ContactTable.COL_ID_DEPT)
					.append(", ").append(Tables.ContactTable.COL_ID_FRIEND)
					.append(", ").append(Tables.ContactTable.COL_NAME_DEPT)
					.append(", ").append(Tables.ContactTable.COL_NAME_FRIEND)
					.append(", ").append(Tables.ContactTable.COL_VAR_EMAIL)
					.append(", ").append(Tables.ContactTable.COL_FLAG)
					.append(", ").append(Tables.ContactTable.COL_VAR_IMGFRIEND)
					.append(", ").append(Tables.ContactTable.COL_VAR_MTEL)
					.append(" ) ").append("select distinct ")
					.append(Tables.TempContactTable.COL_ID_DEPT).append(", ")
					.append(Tables.TempContactTable.COL_ID_USER).append(", ")
					.append(Tables.TempContactTable.COL_NAME_DEPT).append(", ")
					.append(Tables.TempContactTable.COL_NAME_USER).append(", ")
					.append(Tables.TempContactTable.COL_VAR_EMAIL).append(", ")
					.append("'Y'").append(", ")
					.append(Tables.TempContactTable.COL_VAR_IMGUSER)
					.append(", ").append(Tables.TempContactTable.COL_VAR_MTEL)
					.append(" from ").append(Tables.TempContactTable.NAME);
			database.execSQL(sql.toString());
			// database.delete(Tables.TempContactTable.NAME,
			// Tables.TempContactTable.COL_ID_USER + "=?", new
			// String[]{userID});
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 传null查询所有临时联系人,否则查询指定用户ID的记录 */
	public static final ArrayList<VerfifyFriendInfo> queryTempContactInfos(
			String userID) {
		ArrayList<VerfifyFriendInfo> result = new ArrayList<VerfifyFriendInfo>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ").append(Tables.TempContactTable.NAME);
			if (userID != null)
				sql.append(" where ")
						.append(Tables.TempContactTable.COL_ID_USER)
						.append(" = '").append(userID).append("' ");
			cursor = database.rawQuery(sql.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				VerfifyFriendInfo tci = new VerfifyFriendInfo();
				tci.setComid(getColumnString(cursor,
						Tables.TempContactTable.COL_ID_COM));
				tci.setDeptid(getColumnString(cursor,
						Tables.TempContactTable.COL_ID_DEPT));
				tci.setDeptname(getColumnString(cursor,
						Tables.TempContactTable.COL_NAME_DEPT));
				tci.setFriendid(getColumnString(cursor,
						Tables.TempContactTable.COL_ID_USER));
				tci.setFriendimage(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_IMGUSER));
				tci.setFriendmail(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_EMAIL));
				tci.setFriendOS(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_EQUTYPE));
				tci.setFriendname(getColumnString(cursor,
						Tables.TempContactTable.COL_NAME_USER));
				tci.setFriendmtel(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_MTEL));
				tci.setVerfifyNote(getColumnString(cursor,
						Tables.TempContactTable.COL_VERFIFY_NOTE));
				tci.setVerfifyResult(getColumnString(cursor,
						Tables.TempContactTable.COL_VERFIFY_RESULT));
				tci.setVerfifyType(getColumnString(cursor,
						Tables.TempContactTable.COL_VERFIFY_TYPE));
				tci.setFlagRead(getColumnString(cursor,
						Tables.TempContactTable.COL_FLAG_READ));
				result.add(tci);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	/** 查询未查看的临时联系人数量 */
	public static final int queryNewTempContactInfosCounts() {
		ArrayList<VerfifyFriendInfo> result = new ArrayList<VerfifyFriendInfo>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ").append(Tables.TempContactTable.NAME);
			sql.append(" where ").append(Tables.TempContactTable.COL_FLAG_READ)
					.append(" = '").append("N").append("' ");
			cursor = database.rawQuery(sql.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				VerfifyFriendInfo tci = new VerfifyFriendInfo();
				tci.setComid(getColumnString(cursor,
						Tables.TempContactTable.COL_ID_COM));
				tci.setDeptid(getColumnString(cursor,
						Tables.TempContactTable.COL_ID_DEPT));
				tci.setDeptname(getColumnString(cursor,
						Tables.TempContactTable.COL_NAME_DEPT));
				tci.setFriendid(getColumnString(cursor,
						Tables.TempContactTable.COL_ID_USER));
				tci.setFriendimage(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_IMGUSER));
				tci.setFriendmail(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_EMAIL));
				tci.setFriendOS(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_EQUTYPE));
				tci.setFriendname(getColumnString(cursor,
						Tables.TempContactTable.COL_NAME_USER));
				tci.setFriendmtel(getColumnString(cursor,
						Tables.TempContactTable.COL_VAR_MTEL));
				tci.setVerfifyNote(getColumnString(cursor,
						Tables.TempContactTable.COL_VERFIFY_NOTE));
				tci.setVerfifyResult(getColumnString(cursor,
						Tables.TempContactTable.COL_VERFIFY_RESULT));
				tci.setVerfifyType(getColumnString(cursor,
						Tables.TempContactTable.COL_VERFIFY_TYPE));
				tci.setFlagRead(getColumnString(cursor,
						Tables.TempContactTable.COL_FLAG_READ));
				result.add(tci);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result.size();
	}

	/** 更新临时联系人状态 */
	public static final void updateTempContactStatus(final String userID,
			final String status) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("update ").append(Tables.TempContactTable.NAME)
					.append(" set ")
					.append(Tables.TempContactTable.COL_VERFIFY_RESULT)
					.append(" = '").append(status).append("' ")
					.append(" where ")
					.append(Tables.TempContactTable.COL_ID_USER).append(" = '")
					.append(userID).append("'");
			database.execSQL(sql.toString());
			Log.i("info", "sql==" + sql);
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 更新临时联系人是否已读状态 */
	public static final void updateTempContactFlagRead() {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("update ").append(Tables.TempContactTable.NAME)
					.append(" set ")
					.append(Tables.TempContactTable.COL_FLAG_READ)
					.append(" = '").append("Y").append("' ");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final void updateSQL(String sql) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}
}

package com.mznerp.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.FriendInfo;
import com.mznerp.model.GroupInfo;
import com.mznerp.model.TempContactInfo;
import com.mznerp.model.UserInfo;
import com.mznerp.util.DateUtil;
import com.mznerp.util.SharePreferenceUtil;

/**
 * the helper which wrap the part of the chat packet creation procedure
 * 
 * @note most of methods in this class has not check the connection or session
 *       or null
 * @author John Kenrinus Lee
 */
public class ChatPacketHelper {
	private static final HashMap<String, String> map = new HashMap<String, String>();
	static {
		map.put(ChatConstants.error_code.ERROR_CODE_DB_CONN,
				ChatConstants.error_string.ERROR_STRING_DB_CONN);
		map.put(ChatConstants.error_code.ERROR_CODE_DB_SQL,
				ChatConstants.error_string.ERROR_STRING_DB_SQL);
		map.put(ChatConstants.error_code.ERROR_CODE_ILLEGAL_CAST,
				ChatConstants.error_string.ERROR_STRING_ILLEGAL_CAST);
		map.put(ChatConstants.error_code.ERROR_CODE_SERVER_UNREACH,
				ChatConstants.error_string.ERROR_STRING_SERVER_UNREACH);
		map.put(ChatConstants.error_code.ERROR_CODE_JSON_PARSE,
				ChatConstants.error_string.ERROR_STRING_JSON_PARSE);
		map.put(ChatConstants.error_code.ERROR_CODE_DB_OP,
				ChatConstants.error_string.ERROR_STRING_DB_OP);
		map.put(ChatConstants.error_code.ERROR_CODE_ILLEGAL_ARGUMENT,
				ChatConstants.error_string.ERROR_STRING_ILLEGAL_ARGUMENT);
		map.put(ChatConstants.error_code.ERROR_CODE_USERNAME_PASSWORD,
				ChatConstants.error_string.ERROR_STRING_USERNAME_PASSWORD);
		map.put(ChatConstants.error_code.ERROR_CODE_BAD_AUTH,
				ChatConstants.error_string.ERROR_STRING_BAD_AUTH);
		map.put(ChatConstants.error_code.ERROR_CODE_SESSION_INVALID,
				ChatConstants.error_string.ERROR_STRING_SESSION_INVALID);
		map.put(ChatConstants.error_code.ERROR_CODE_REPEAT_LOGIN,
				ChatConstants.error_string.ERROR_STRING_REPEAT_LOGIN);
		map.put(ChatConstants.error_code.ERROR_CODE_NO_PERMISSION,
				ChatConstants.error_string.ERROR_STRING_NO_PERMISSION);
		map.put(ChatConstants.error_code.ERROR_CODE_REGIST_VERIFY,
				ChatConstants.error_string.ERROR_STRING_REGIST_VERIFY);
		map.put(ChatConstants.error_code.ERROR_CODE_CONTACT_NOT_FOUND,
				ChatConstants.error_string.ERROR_STRING_CONTACT_NOT_FOUND);
	}

	/**
	 * if the type property of iq isn't error, then return null, else return the
	 * human readable error detail string
	 */
	@SuppressWarnings("unchecked")
	public static final String parseErrorCode(IQ iq) {
		if (iq == null)
			return Constant.ERROR_SERVER_NO_RESPONDSE;
		if (!ChatConstants.iq.TYPE_ERROR.equals(iq.type))
			return null;
		Map<String, Object> dataMap = (Map<String, Object>) iq.data;

		String error_code = (String) dataMap
				.get(ChatConstants.iq.DATA_KEY_ERROR_CODE);
		if (error_code != null)
			return map.get(error_code);

		String error_msg = (String) dataMap.get(ChatConstants.iq.DATA_KEY_MSG);
		if (error_msg != null)
			return error_msg;
		return "Unknown Error!";
	}

	/** 　return the error code description which human can readable */
	public static final String parseErrorCode(String error_code) {
		return map.get(error_code);
	}

	/** create a random UUID for id property of IQ packet */
	public static final String getGeneralUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * time in Msg is like "1234559433.445", and the millisecond is
	 * 1234559433445, this method make 1234559433445 to "1234559433.445"
	 */
	public static final String fromMillsec(long millsec) {
		String s = String.valueOf(millsec);
		return s.substring(0, s.length() - 3) + "."
				+ s.substring(s.length() - 3);
	}

	/**
	 * time in Msg is like "1234559433.445", and the millisecond is
	 * 1234559433445, this method make "1234559433.445" to 1234559433445
	 */
	public static final long toMillsec(String millsec) {
		String s = millsec.replaceAll("\\.", "");
		return Long.valueOf(s);
	}

	public static final GroupInfo getMapGroupInfo(Map<String, Object> item)
	{
		GroupInfo gi = new GroupInfo();
		gi.groupID = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_ID);
		gi.groupName = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_NAME);
		gi.groupType = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_TYPE);
		gi.groupImage = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_ICON);
		gi.groupLord = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_LORD);
		return gi;
	}
	/** insert group info union group relation with atomic */
	@SuppressWarnings("unchecked")
	public static final void insertGroup(Map<String, Object> item) {
		String groupId = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_ID);
		GroupInfo gi = new GroupInfo();
		gi.groupID = groupId;
		gi.groupName = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_NAME);
		gi.groupType = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_TYPE);
		gi.groupImage = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_ICON);
		gi.groupLord = (String) item.get(ChatConstants.iq.DATA_KEY_GROUP_LORD);
		QiXinBaseDao.replaceGroupInfo(gi); 
		String userss = (String) item
				.get(ChatConstants.iq.DATA_KEY_GROUP_USER_IDS);
		List<String> users = new ArrayList<String>();
		for (String t : userss.split("@")) {
			users.add(t);
		}

		String selfId = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		if (selfId != null && !users.contains(selfId))
			users.add(selfId);
		if (!users.contains(gi.groupLord))
			users.add(gi.groupLord);
		String[] strs = new String[users.size()];
		QiXinBaseDao.insertGroupRelation(groupId, users.toArray(strs));
	}

	/**
	 * request change password, the URL like
	 * "http://localhost:8080/nerp/hjpda?actionType=mobileUpdatePassWord&user_id=lixiang&password=123&npassword=1234&session_id=xxx"
	 */
	public static final String requestChangePasswd(char[] oldPassword,
			char[] newPassword) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				ActivitySupport.sputil.getMySessionId());
		map.put(ChatConstants.iq.DATA_KEY_ACTION_TYPE,
				ChatConstants.iq.DATA_VALUE_ACTION_MODIFYPASSWORD);
		map.put(ChatConstants.iq.DATA_KEY_USER_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
		map.put(ChatConstants.iq.DATA_KEY_PASSWORD, String.valueOf(oldPassword));
		map.put(ChatConstants.iq.DATA_KEY_NEW_PASSWORD,
				String.valueOf(newPassword));
		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_CHANGE_PASSWD, map,
				HttpUtils.ENCODE_UTF8);
		return responseContent;
	}

	/** build image like avatar request URL */
	public static final String buildImageRequestURL1(String imageName,
			String type) {
		StringBuffer sb = new StringBuffer();
		sb.append(EapApplication.URL_SERVER_HOST_HTTP)
				.append(ChatConstants.http.URLPATH_PULL_AVATAR).append("?")
				.append(ChatConstants.iq.DATA_KEY_FILE_NAME).append("=")
				.append(imageName).append("&")
				.append(ChatConstants.iq.DATA_KEY_SESSION_ID).append("=")
				.append((String) ActivitySupport.sputil.getMySessionId())
				.append("&").append(ChatConstants.iq.DATA_KEY_TYPE).append("=")
				.append(type);
		return sb.toString();
	}

	/** build image like avatar request URL */
	public static final String buildImageRequestURL(String imageName,
			String type) {
		StringBuffer sb = new StringBuffer();
		sb.append(EapApplication.URL_SERVER_HOST_HTTP)
				.append(ChatConstants.http.URLPATH_PULL_AVATAR)
				.append("?")
				.append(ChatConstants.iq.DATA_KEY_FILE_NAME)
				.append("=")
				.append(imageName)
				// .append("&")
				// .append(ChatConstants.iq.DATA_KEY_SESSION_ID)
				// .append("=")
				// .append((String)
				// EapApplication.getApplication().getExtra(EapApplication.EXTRA_SESSION_ID))
				.append("&").append(ChatConstants.iq.DATA_KEY_TYPE).append("=")
				.append(type);
		return sb.toString();
	}

	public static final UserInfo createUserInfo(Map<String, String> item) {
		UserInfo user = new UserInfo();
		user.sessionID = item.get(ChatConstants.iq.DATA_KEY_SESSION_ID);
		user.userID = item.get(ChatConstants.iq.DATA_KEY_USER_ID);
		user.username = item.get(ChatConstants.iq.DATA_KEY_USER_NAME);
		user.departmentID = item.get(ChatConstants.iq.DATA_KEY_DEPT_ID);
		user.departmentName = item.get(ChatConstants.iq.DATA_KEY_DEPT_NAME);
		user.userImage = item.get(ChatConstants.iq.DATA_KEY_AVATAR);
		user.email = item.get(ChatConstants.iq.DATA_KEY_EMAIL);
		user.phoneNumber = item.get(ChatConstants.iq.DATA_KEY_MOBILE_PHONE);
		user.companyID = item.get(ChatConstants.iq.DATA_KEY_COM_ID);
		user.companyName = item.get(ChatConstants.iq.DATA_KEY_COM_NAME);
		user.isAutoLogin = "Y";
		user.lastLoginTime = DateUtil.yyyy_MM_dd__HHimm.format(new Date());
		return user;
	}

	public static final FriendInfo createFriendInfo(Map<String, String> item) {

		FriendInfo friend = new FriendInfo();
		friend.setDeptid(item.get(ChatConstants.iq.DATA_KEY_DEPT_ID));
		friend.setDeptname(item.get(ChatConstants.iq.DATA_KEY_DEPT_NAME));
		friend.setFriendid(item.get(ChatConstants.iq.DATA_KEY_USER_ID));
		friend.setFriendimage(item.get(ChatConstants.iq.DATA_KEY_AVATAR));
		friend.setFriendmail(item.get(ChatConstants.iq.DATA_KEY_EMAIL));
		friend.setFriendmtel(item.get(ChatConstants.iq.DATA_KEY_MOBILE_PHONE));
		friend.setFriendname(item.get(ChatConstants.iq.DATA_KEY_USER_NAME));
		return friend;
	}

	public static final GroupInfo createGroupInfo(Map<String, String> item) {
		GroupInfo gi = new GroupInfo();
		gi.groupID = item.get(ChatConstants.iq.DATA_KEY_GROUP_ID);
		gi.groupName = item.get(ChatConstants.iq.DATA_KEY_GROUP_NAME);
		gi.groupType = item.get(ChatConstants.iq.DATA_KEY_GROUP_TYPE);
		gi.groupImage = item.get(ChatConstants.iq.DATA_KEY_GROUP_ICON);
		gi.groupLord = item.get(ChatConstants.iq.DATA_KEY_GROUP_LORD);
		return gi;
	}

	public static final TempContactInfo parseContactInIQ(
			Map<String, String> infoMap) {
		TempContactInfo tci = new TempContactInfo();
		tci.setFriendid(infoMap.get(ChatConstants.iq.DATA_KEY_USER_ID));
		tci.setFriendname(infoMap.get(ChatConstants.iq.DATA_KEY_USER_NAME));
		tci.setComid(infoMap.get(ChatConstants.iq.DATA_KEY_COM_ID));
		tci.setDeptid(infoMap.get(ChatConstants.iq.DATA_KEY_DEPT_ID));
		tci.setFriendimage(infoMap.get(ChatConstants.iq.DATA_KEY_AVATAR));
		tci.setFriendmtel(infoMap.get(ChatConstants.iq.DATA_KEY_MOBILE_PHONE));
		tci.setFriendmail(infoMap.get(ChatConstants.iq.DATA_KEY_EMAIL));
		tci.setFriendOS(infoMap.get(ChatConstants.iq.DATA_KEY_SYS_TYPE));
		tci.setDeptname(infoMap.get(ChatConstants.iq.DATA_KEY_DEPT_NAME));
		// QiXinBaseDao.insertTempContactInfo(tci);
		return tci;
	}

	public static final void processContactAddResult(String userId,
			boolean agree) {
		if (agree)
			QiXinBaseDao.shiftTempContactInfoById(userId);
		else
			QiXinBaseDao.deleteTempContactInfoById(userId);
	}

	@SuppressWarnings("unchecked")
	public static final ArrayList<FriendInfo> processSearchedUserInfo(IQ iq) {
		ArrayList<FriendInfo> result = new ArrayList<FriendInfo>();
		Map<String, Object> map = (Map<String, Object>) iq.data;
		List<Map<String, String>> items = (List<Map<String, String>>) map
				.get(ChatConstants.iq.DATA_KEY_ITEMS);
		for (Map<String, String> item : items) {
			result.add(createFriendInfo(item));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static final ArrayList<FriendInfo> processUnFriendsUsersInGroup(IQ iq) {
		ArrayList<FriendInfo> result = new ArrayList<FriendInfo>();
		Map<String, Object> map = (Map<String, Object>) iq.data;
		List<Map<String, String>> items = (List<Map<String, String>>) map
				.get(ChatConstants.iq.DATA_KEY_INFO);
		if (items != null) {
			for (Map<String, String> item : items) {
				result.add(createFriendInfo(item));
			}
		}
		return result;
	}

}

package com.hjnerp.manager;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.Tables;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.HttpUtils;
import com.hjnerp.net.IQ;
import com.hjnerp.net.Msg;
import com.hjnerp.net.Presence;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;

public class HJWebSocketManager {

	private static HJWebSocketManager hjWebSocketManager;
	protected Gson gson;

	private HJWebSocketManager() {
		gson = new Gson();

	}

	public static HJWebSocketManager getInstance() {
		if (hjWebSocketManager == null) {
			hjWebSocketManager = new HJWebSocketManager();
		}
		return hjWebSocketManager;
	}

	// public HJWebSocketClient connect(String Url) throws Exception {
	//
	// connection = new HJWebSocketClient(new URI(Url));
	//
	// String KEYSTORE = "hjandroidclient.bks";
	// String STOREPASSWORD = "HJA0326";
	// // String KEYPASSWORD = "keypassword";
	// KeyStore ks = KeyStore.getInstance("BKS");
	// InputStream keyfile = this.getClass().getResourceAsStream(KEYSTORE);
	// ks.load(keyfile, STOREPASSWORD.toCharArray());
	//
	// KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("X509");
	// keyMgrFactory.init(ks, STOREPASSWORD.toCharArray());
	// KeyManager[] keyMgrs = keyMgrFactory.getKeyManagers();
	//
	// TrustManagerFactory trustMgrFactory = TrustManagerFactory
	// .getInstance("X509");
	// trustMgrFactory.init(ks);
	// TrustManager[] trustMgrs = trustMgrFactory.getTrustManagers();
	//
	// SSLContext sslContext = SSLContext.getInstance("TLS" /* "SSL" */);
	// sslContext.init(keyMgrs, trustMgrs, null);
	//
	// SSLSocketFactory factory = sslContext.getSocketFactory();
	// connection.setSocket(factory.createSocket());
	//
	// if (!connection.connectBlocking()) {
	// connection = null;
	// throw new Exception("连接服务失败" + Url);
	// }
	// return connection;
	// }
	//
	// public void disconnect() {
	// if (connection != null) {
	// connection.close();
	// connection = null;
	// }
	// }

	public IQ login(String comid, String userid, String password) {

		IQ iq = new IQ();
		iq.from = userid;
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_LOGIN;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;
		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_USER_ID, userid);
		map.put(ChatConstants.iq.DATA_KEY_PASSWORD, String.valueOf(password));
		map.put(ChatConstants.iq.DATA_KEY_COM_ID, comid);
		map.put(ChatConstants.iq.DATA_KEY_EQU_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getDeviceId());
		map.put(ChatConstants.iq.DATA_KEY_SYS_TYPE,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getOsType());
		map.put(ChatConstants.iq.DATA_KEY_APP_VERSION,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getAppVersion());
		iq.data = map;

		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		/**/
		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		Log.i("info", "登陆地址："+EapApplication.URL_SERVER_HOST_HTTP
				+ ChatConstants.http.URLPATH_HJMOBILESERVLET);
		Log.i("info", "登陆返回："+responseContent);
		if (responseContent == null)
			return null;
		IQ response = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);

			if (jsonObj != null && jsonObj.has("iq")) {
				response = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i("info", "登陆异常："+e.toString());
		}
		return response;

	}

	public long sendFileChatMsg(String id,String to, String fileid, String fileType,
			String fileSize,String msgType ,String errorText) {

		Msg msg = new Msg();
		msg.from = ActivitySupport.sputil.getMyId();
		msg.to = to;
		if (Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(fileType)) {
			msg.body =  fileSize;
		} else if (Constant.FILE_TYPE_PIC.equalsIgnoreCase(fileType)) {
			msg.body = "[图片]";
		} else if (Constant.FILE_TYPE_MIE.equalsIgnoreCase(fileType)) {
			msg.body = "[视频]";
		} else if (Constant.FILE_TYPE_DOC.equalsIgnoreCase(fileType)) {
			msg.body = "[文档]";
		}else if (Constant.FILE_TYPE_LOCATION.equalsIgnoreCase(fileType)) {
			msg.body = fileSize;
		}
		msg.type = msgType;
		long millsec = System.currentTimeMillis(); 
		msg.time = String.valueOf(millsec);
		msg.encode = ChatConstants.ENCODE_GZIP;
		msg.file.id = fileid;
		msg.file.scene = fileType;
		msg.id = id;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA,
				"{\"msg\":" + gson.toJson(msg) + "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);

		if (responseContent == null)
			return -1;
		IQ response = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);

			if (jsonObj != null && jsonObj.has("iq")) {
				response = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response != null && "result".equalsIgnoreCase(response.type)) {
			return 0;
		} else {  
			errorText =  ChatPacketHelper.parseErrorCode(response);
//			Looper.prepare() ;
//			ToastUtil.ShowShort( EapApplication.getApplication().getApplicationContext(), ChatPacketHelper.parseErrorCode(response));
			return -1;
		}
	}

	public boolean sendMsg(String groupId, String to, String body,
			String msgType,String id ,String errorText) {

		body = body.replaceAll("\\\\", "\\\\\\\\");
		Msg msg = new Msg();
		msg.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		msg.to = to;
		msg.body = body;
		msg.type = msgType;
		long millsec = System.currentTimeMillis();
		msg.time = String.valueOf(millsec);
		msg.encode = ChatConstants.ENCODE_GZIP;
		msg.id = id;
		
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA,
				"{\"msg\":" + gson.toJson(msg) + "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);

		if (responseContent == null)
			return false;
		IQ response = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);

			if (jsonObj != null && jsonObj.has("iq")) {
				response = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response != null && "result".equalsIgnoreCase(response.type)) {
			return true;
		} else { 
			errorText =  ChatPacketHelper.parseErrorCode(response);
 		 	return false;
		}

	}

	public boolean sendMsgResponses(final String mssageId) {

		new Thread() {
			@Override
			public void run() {
				Msg msg = new Msg();
				msg.id = mssageId;
				msg.from = SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId();
				msg.to = "system";
				msg.body = "";
				msg.type = "responses";
				long millsec = System.currentTimeMillis();
				msg.time = String.valueOf(millsec);
				msg.encode = ChatConstants.ENCODE_GZIP;

				LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();

				mapp.put(ChatConstants.httpParm.HTTPDATA,
						"{\"msg\":" + gson.toJson(msg) + "}");
				HttpUtils.post(EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
						HttpUtils.ENCODE_UTF8);
			};
		}.start();
		return true;
	}

	public boolean sendIqResponses(final String iqId)
	{
		new Thread() {
			@Override
			public void run() {
				IQ iq = new IQ();
				iq.from = SharePreferenceUtil.getInstance(
						EapApplication.getApplication().getApplicationContext())
						.getMyId();
				iq.to = ChatConstants.iq.USERID_SYSTEM;
				iq.feature = "9999";
				iq.type = ChatConstants.iq.TYPE_GET;
				iq.id = iqId;
				iq.encode = ChatConstants.ENCODE_GZIP; 
				LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>(); 
				map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
						SharePreferenceUtil
								.getInstance(
										EapApplication.getApplication()
												.getApplicationContext())
								.getMySessionId());
				map.put(ChatConstants.iq.DATA_KEY_EQU_ID,
						SharePreferenceUtil
								.getInstance(
										EapApplication.getApplication()
												.getApplicationContext()).getDeviceId());
				map.put(ChatConstants.iq.DATA_KEY_SYS_TYPE,
						SharePreferenceUtil
								.getInstance(
										EapApplication.getApplication()
												.getApplicationContext()).getOsType());
				map.put(ChatConstants.iq.DATA_KEY_USER_ID,
						SharePreferenceUtil
								.getInstance(
										EapApplication.getApplication()
												.getApplicationContext()).getMyId());
				iq.data = map;

				LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
				mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
						+ "}");
				HttpUtils.post(
						EapApplication.URL_SERVER_HOST_HTTP
								+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
						HttpUtils.ENCODE_UTF8);
			};
		}.start();
		
		return true;
	}
	
    public IQ requestIQ(String iq_feature) {
		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = iq_feature;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;
		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		// map.put(ChatConstants.iq.DATA_KEY_SESSION_ID, (String)
		// EapApplication.getApplication().getExtra(EapApplication.EXTRA_SESSION_ID));
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		map.put(ChatConstants.iq.DATA_KEY_EQU_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getDeviceId());
		map.put(ChatConstants.iq.DATA_KEY_SYS_TYPE,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getOsType());
		
		iq.data = map;

		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);

		if (responseContent == null)
			return null;
		if ("".equalsIgnoreCase(responseContent.trim()))
			return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	// 查询历史聊天jil
	public String requestHistoryChatMsg(char[] requst) {
		HashMap<String, String> map = new HashMap<String, String>();
		// map.put(ChatConstants.iq.DATA_KEY_SESSION_ID, (String)
		// EapApplication.getApplication().getExtra(EapApplication.EXTRA_SESSION_ID));
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		map.put(ChatConstants.iq.DATA_KEY_ACTION_TYPE,
				ChatConstants.iq.DATA_VALUE_ACTION_QUERYHISHORRYMESSAGE);
		map.put(ChatConstants.iq.DATA_KEY_USER_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
		map.put("chat_query_key", String.valueOf(requst));
		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_CHANGE_PASSWD, map,
				HttpUtils.ENCODE_UTF8);
		return responseContent;
	}

	public String requestChangePasswd(char[] oldPassword, char[] newPassword) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
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

	public String requestChangeEmail(String newEmal) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		map.put(ChatConstants.iq.DATA_KEY_ACTION_TYPE,
				ChatConstants.iq.DATA_VALUE_ACTION_MODIFYCONTENT);
		map.put(ChatConstants.iq.DATA_KEY_CONTENTTYPE,
				ChatConstants.iq.DATA_KEY_MAILBOX);
		map.put(ChatConstants.iq.DATA_KEY_USER_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
		map.put(ChatConstants.iq.DATA_KEY_CONTENT,  newEmal );
		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_CHANGE_PASSWD, map,
				HttpUtils.ENCODE_UTF8);
		return responseContent;
	}

	public String requestChangePhone(char[] newPhone) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		map.put(ChatConstants.iq.DATA_KEY_ACTION_TYPE,
				ChatConstants.iq.DATA_VALUE_ACTION_MODIFYCONTENT);
		map.put(ChatConstants.iq.DATA_KEY_CONTENTTYPE,
				ChatConstants.iq.DATA_KEY_PHONE);
		map.put(ChatConstants.iq.DATA_KEY_USER_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
		map.put(ChatConstants.iq.DATA_KEY_CONTENT, String.valueOf(newPhone));
		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_CHANGE_PASSWD, map,
				HttpUtils.ENCODE_UTF8);
		return responseContent;
	}

	public String fromMillsec(long millsec) {
		String s = String.valueOf(millsec);
		return s.substring(0, s.length() - 3) + "."
				+ s.substring(s.length() - 3);
	}

	public IQ logout() {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_LOGOUT;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP; 
		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
	 
		iq.data = map;

		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		HttpUtils.post(EapApplication.URL_SERVER_HOST_HTTP
				+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);

		return null;
	}

	public String autoLogin() {

		String errorText;

		IQ iq = HJWebSocketManager.getInstance().requestIQ(
				ChatConstants.iq.FEATURE_AUTO_LOGIN);
		if (iq == null) {
			return "服务端没有返回";
		}
		if (ChatConstants.iq.TYPE_ERROR.equals(iq.type)) {
			errorText = ChatPacketHelper.parseErrorCode(iq);
			return errorText;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) iq.data;
		String sessionId = (String) dataMap
				.get(ChatConstants.iq.DATA_KEY_SESSION_ID);
		SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.setMySessionId(sessionId);
		// EapApplication.getApplication().putExtra(
		// EapApplication.EXTRA_SESSION_ID, sessionId);
		// ActivitySupport.sputil.setMySessionId(sessionId);

		String userID = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();

		QiXinBaseDao.updateUserInfo(userID, Tables.UserTable.COL_VAR_SESSION,
				sessionId);
		return "";
	}

	public IQ createGroupChat(String chatTitle, List<String> userIDs) {

		StringBuffer sb = new StringBuffer();
		for (String userID : userIDs) {
			sb.append(userID).append("@");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		LinkedTreeMap<String, String> dataMap = new LinkedTreeMap<String, String>();
		dataMap.put(ChatConstants.iq.DATA_KEY_GROUP_NAME, chatTitle);
		dataMap.put(ChatConstants.iq.DATA_KEY_GROUP_USER_IDS, sb.toString());
		dataMap.put(
				ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		 

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_CREATE_GROUP_CHAT;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;
		iq.data = dataMap;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);

		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ answerAddContact(String contactID, boolean agree) {

		String iq_feature;
		iq_feature = agree ? ChatConstants.iq.FEATURE_CONTACT_AGREE
				: ChatConstants.iq.FEATURE_CONTACT_REFUSE;
		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = contactID;
		iq.feature = iq_feature;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;
		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId()); 
		iq.data = map;

		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;

	}

	public IQ addContact(String contactID, String note) {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = contactID;
		iq.feature = ChatConstants.iq.FEATURE_ADD_CONTACT;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;
		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		map.put(ChatConstants.iq.DATA_KEY_MSG, note); 
		iq.data = map;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ removeContact(String contactID) {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = contactID;
		iq.feature = ChatConstants.iq.FEATURE_REMOVE_CONTACT;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;
		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		map.put(ChatConstants.iq.DATA_KEY_MSG, ""); 
		iq.data = map;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ searchUser(String id) {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_SEARCH_USER;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;

		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SCOPE,
				ChatConstants.iq.DATA_VALUE_SCOPE_MORE);
		map.put(ChatConstants.iq.DATA_KEY_ID, id);
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId()); 
		
		iq.data = map;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ searchUserInfo(String id) {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_SEARCH_USERINFO;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;

		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SCOPE,
				ChatConstants.iq.DATA_VALUE_SCOPE_MORE);
		map.put(ChatConstants.iq.DATA_KEY_ID, id);
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId()); 
		iq.data = map;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ searchGroup(String id) {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_SEARCH_GROUP;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;

		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SCOPE,
				ChatConstants.iq.DATA_VALUE_SCOPE_MORE);
		map.put(ChatConstants.iq.DATA_KEY_ID, id);
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId()); 
		iq.data = map;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ doGroupOpt(String groupID, List<String> userIDs, String flag) {

		StringBuffer sb = new StringBuffer();
		for (String userID : userIDs) {
			sb.append(userID).append("@");
		}
		sb.deleteCharAt(sb.length() - 1); 

		LinkedTreeMap<String, String> dataMap = new LinkedTreeMap<String, String>();
		dataMap.put(ChatConstants.iq.DATA_KEY_GROUP_USER_IDS, sb.toString());
		dataMap.put(ChatConstants.iq.DATA_KEY_GROUP_ID, groupID);
		dataMap.put(ChatConstants.iq.DATA_KEY_TYPE, flag);
		dataMap.put(
				ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());  
	
		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_OP_GROUP_CHAT;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;
		iq.data = dataMap;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ modifyGroupName(String groupID, String newName) {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_MODIFY_GROUP_INFO;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;

		LinkedTreeMap<String, String> dataMap = new LinkedTreeMap<String, String>();
		dataMap.put(ChatConstants.iq.DATA_KEY_GROUP_ID, groupID);
		dataMap.put(ChatConstants.iq.DATA_KEY_INFO, newName);
		dataMap.put(ChatConstants.iq.DATA_KEY_TYPE,
				ChatConstants.iq.DATA_VALUE_MODIFY_NAME);
		dataMap.put(
				ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId()); 

		iq.data = dataMap;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public IQ changeAvartar(String vartarName) {

		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_CHANGE_AVARTAR;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;

		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_AVATAR, vartarName);
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId()); 
		iq.data = map;

		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);
		if (responseContent == null ) return null;
		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;
	}

	public void readyPresence() {
		Presence presence = new Presence(SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId(), "", "Y");

		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>(); 
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId());
		 

		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA,
				"{\"presence\":" + gson.toJson(presence) + "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);

		// IQ requestiq = null;
		// JSONObject jsonObj;
		// try {
		// jsonObj = new JSONObject(responseContent);
		// if (jsonObj.has("iq")) {
		// requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// presence.from =
		// SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId();
		//
		// presence.status="Y";

		// getConnection().sendPresence(presence);

	}

	public IQ readyWorkFlow() {
		IQ iq = new IQ();
		iq.from = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();
		iq.to = ChatConstants.iq.USERID_SYSTEM;
		iq.feature = ChatConstants.iq.FEATURE_WORK_FLOW;
		iq.type = ChatConstants.iq.TYPE_GET;
		iq.id = StringUtil.getMyUUID();
		iq.encode = ChatConstants.ENCODE_GZIP;

		LinkedTreeMap<String, String> map = new LinkedTreeMap<String, String>();
		map.put(ChatConstants.iq.DATA_KEY_SESSION_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext())
						.getMySessionId()); 
		iq.data = map;
		LinkedTreeMap<String, String> mapp = new LinkedTreeMap<String, String>();
		mapp.put(ChatConstants.httpParm.HTTPDATA, "{\"iq\":" + gson.toJson(iq)
				+ "}");

		String responseContent = HttpUtils.post(
				EapApplication.URL_SERVER_HOST_HTTP
						+ ChatConstants.http.URLPATH_HJMOBILESERVLET, mapp,
				HttpUtils.ENCODE_UTF8);

		if (responseContent == null)
			return null;

		IQ requestiq = null;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(responseContent);
			if (jsonObj.has("iq")) {
				requestiq = gson.fromJson(jsonObj.getString("iq"), IQ.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestiq;

	}
}

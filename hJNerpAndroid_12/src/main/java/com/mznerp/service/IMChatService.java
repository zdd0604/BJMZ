package com.mznerp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mznerp.activity.MainActivity;
import com.mznerp.activity.contact.ContactConstants;
import com.mznerp.activity.im.ChatActivity;
import com.mznerp.business.ContactBusiness;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.manager.HJWebSocketManager;
import com.mznerp.manager.SoundPoolPlay;
import com.mznerp.model.FriendInfo;
import com.mznerp.model.GroupInfo;
import com.mznerp.model.TempContactInfo;
import com.mznerp.model.VerfifyFriendInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.net.IQ;
import com.mznerp.net.Msg;
import com.mznerp.util.PollingUtils;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.util.StringUtil;
import com.mznerp.websocket.HjHTTPNotificationManager;
import com.mznerp.websocket.OnNotificationListener;
import com.mznerp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class IMChatService extends Service implements OnNotificationListener {

	private final int WHAT_NOTIFICATION_SUCCESS = 1;
	private final int WHAT_NOTIFICATION_FAILURE = 2;
	protected Gson gson;
	private Context context;
	private NotificationManager notificationManager;
	private int notificationNumber = 0;

	private NetworkInfo info;
	public static final String ACTION = "com.hjnerp.service.websocket.action.reconnect";
	private ConnectivityManager connectivityManager;

	@Override
	public void onCreate() {
		context = this;
		super.onCreate();
		gson = new Gson();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		initChatManager();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);// 网络变化事件
		mFilter.addAction(Intent.ACTION_SCREEN_ON);// 屏幕解锁事件
		mFilter.addAction(ACTION);// 屏幕解锁事件
		registerReceiver(reConnectionBroadcastReceiver, mFilter);
		PollingUtils.startPollingService(context, 1);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		if (notificationManager != null) {
			HjHTTPNotificationManager.getInstance().stop();
			PollingUtils.stopPollingService(context);
		}
		/**
		 * @author haijian 注销广播
		 */
		super.onDestroy();
		unregisterReceiver(reConnectionBroadcastReceiver);
		Log.i("info", "停止操作执行了");
	}

	private void initChatManager() {

		if (SharePreferenceUtil.getInstance(context).isForceExit())

		{
			notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			if (HjHTTPNotificationManager.getInstance().isStoped()) {
				// 先登录，看能不能自动登录

				HjHTTPNotificationManager
						.getInstance()
						.setURL(EapApplication.URL_SERVER_HOST_HTTP
								+ ChatConstants.http.URLPATH_HJMOBILEPUSHSERVLET);
				HjHTTPNotificationManager.getInstance()
						.setOnNotificationListener(this);
				HjHTTPNotificationManager.getInstance().start();
			}
		}
	}

	public void processPacketMsg(ArrayList<Msg> messages, Boolean notiFlag) {
		// TODO Auto-generated method stub
		for (Msg message : messages) {
			if (message.time.length() > 13) {
				message.time = message.time.substring(0, 14);
			}
			String sTitle = "";
			FriendInfo friendInfo = null;
			GroupInfo groupInfo = null;
			int ret = 0;
			if (message != null) {

				if (ChatConstants.msg.TYPE_GROUPCHAT.equals(message.type)) {
					groupInfo = QiXinBaseDao.queryGroupInfo(message.group);
					if (groupInfo != null) {
						sTitle = groupInfo.groupName;
						ret = ContactBusiness.saveGroupChatMsgIndb(message,
								"msg");
						if ("sys".equalsIgnoreCase(groupInfo.groupType)) {
							notiFlag = false;
						}
						EapApplication.getApplication();
						if (EapApplication.EXTRA_CURRENT_CHAT_FRIENDORGROUP_ID
								.equalsIgnoreCase(groupInfo.groupID)) {
							notiFlag = false;
						}
					}
				} else if (ChatConstants.msg.TYPE_CHAT.equals(message.type)) {
					friendInfo = QiXinBaseDao.queryFriendInfo(message.from);
					if (friendInfo != null) {
						sTitle = friendInfo.getFriendname();
						ret = ContactBusiness.saveSingleChatMsgIndb(message,
								"msg");
						EapApplication.getApplication();
						if (EapApplication.EXTRA_CURRENT_CHAT_FRIENDORGROUP_ID
								.equalsIgnoreCase(friendInfo.getFriendid())) {
							notiFlag = false;
						}
					}
				} else {
				}
				Intent intent = new Intent(
						"com.hjnerp.service.websocket.action.MSG");
				intent.putExtra("data", message);
				sendBroadcast(intent);
				SoundPoolPlay.getInstance(context).playSound(1);
				// 生成通知
				if (ret != 0 && notiFlag) {
					setNotiTypeMsg(R.drawable.icon2, sTitle, message.body,
							ChatActivity.class, message.from, message.isplay,
							groupInfo, friendInfo);
				}

				HJWebSocketManager.getInstance().sendMsgResponses(message.id);
			}

		}

	}

	/**
	 *
	 * 发出Notification的method.
	 *
	 * @param iconId
	 *            图标
	 * @param contentTitle
	 *            标题
	 * @param contentText
	 *            你内容
	 * @param activity
	 * @author 李庆义
	 * @update 2012-5-14 下午12:01:55
	 */
	private void setNotiTypeMsg(int iconId, String contentTitle,
			String contentText, Class<ChatActivity> activity, String from,
			String ifplay, GroupInfo groupInfo, FriendInfo friendInfo) {

		int badgenumber = 0;
		notificationNumber++;
		String sContent = contentText;
		if (sContent.indexOf("[语音]") != -1) {
			sContent = "[语音]";
		}

		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		Intent notifyIntent = new Intent(this, activity);
		if (groupInfo != null) {
			Bundle mBundle = new Bundle();
			mBundle.putSerializable(Constant.IM_GOUP_NEWS,
					(Serializable) groupInfo);
			notifyIntent.putExtras(mBundle);
			// notifyIntent.putExtra(Constant.IM_GOUP_NEWS, (Serializable)
			// groupInfo);
			badgenumber = QiXinBaseDao
					.queryGroupChatNewChatMsgCounts(groupInfo.groupID);

		}
		if (friendInfo != null) {
			Bundle mBundle = new Bundle();
			mBundle.putSerializable(Constant.IM_NEWS, (Serializable) friendInfo);
			notifyIntent.putExtras(mBundle);

			badgenumber = QiXinBaseDao.querySingleChatNewChatMsgCounts(
					SharePreferenceUtil.getInstance(
							EapApplication.getApplication()
									.getApplicationContext()).getMyId(),
					friendInfo.getFriendid());
		}
		/* 创建PendingIntent作为设置递延运行的Activity */
		PendingIntent appIntent = PendingIntent.getActivity(this,
				notificationNumber, notifyIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		/* 创建Notication，并设置相关参数 */
//		Notification myNotify = new Notification();
//		// 点击自动消失
//		myNotify.flags = Notification.FLAG_AUTO_CANCEL;
//		/* 设置statusbar显示的icon */
//		myNotify.icon = iconId;
//		/* 设置statusbar显示的文字信息 */
//		myNotify.tickerText = contentTitle;
//		myNotify.number = badgenumber;
//		/* 设置Notification留言条的参数 */
//		myNotify.when = System.currentTimeMillis();
//		/* 设置Notification留言条的参数 */
//		myNotify.setLatestEventInfo(this, contentTitle, sContent, appIntent);
		Notification myNotify = new Notification.Builder(this)
				.setAutoCancel(true)
				.setContentTitle(contentTitle)
				.setContentText(sContent)
				.setContentIntent(appIntent)
				.setSmallIcon(iconId)
				.setTicker(contentTitle)
				.setNumber(badgenumber)
				.setWhen(System.currentTimeMillis())
				.setOngoing(true).build();
		/* 送出Notification */
		notificationManager.notify(123, myNotify);
	}

	private void setNotiTypeIQ(int iconId, String contentTitle,
			String contentText, int activityType) {

		notificationNumber++;
		Intent notifyIntent;
		if (activityType == 0) {
			// notifyIntent = new Intent(this, ChatActivity.class);
			// /添加参数
			notifyIntent = new Intent(this, MainActivity.class);
		} else {
			notifyIntent = new Intent(this, MainActivity.class);
		}
		/* 创建PendingIntent作为设置递延运行的Activity */
		PendingIntent appIntent = PendingIntent.getActivity(this, 0,
				notifyIntent, 0);
		/* 创建Notication，并设置相关参数 */
//		Notification myNotify = new Notification();
//		// 点击自动消失
//		myNotify.flags = Notification.FLAG_AUTO_CANCEL;
//		/* 设置statusbar显示的icon */
//		myNotify.icon = iconId;
//		/* 设置statusbar显示的文字信息 */
//		myNotify.tickerText = contentTitle;
//		/* 设置notification发生时同时发出默认声音 */
//		// myNotify.defaults = Notification.DEFAULT_SOUND;
//		/* 设置Notification留言条的参数 */
//		myNotify.number = notificationNumber;
//		myNotify.when = System.currentTimeMillis();
//		myNotify.setLatestEventInfo(this, contentTitle, contentText, appIntent);
		Notification myNotify = new Notification.Builder(this)
				.setAutoCancel(true)
				.setContentTitle(contentTitle)
				.setContentText(contentText)
				.setContentIntent(appIntent)
				.setDefaults(Notification.DEFAULT_SOUND)
				.setSmallIcon(iconId)
				.setTicker(contentTitle)
				.setNumber(notificationNumber)
				.setWhen(System.currentTimeMillis())
				.setOngoing(true).build();
		/* 送出Notification */
		notificationManager.notify(234, myNotify);

	}

	@Override
	public void onNotification(String msg) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(msg))
			return;
		Message message = handler.obtainMessage();
		message.obj = msg;
		message.what = WHAT_NOTIFICATION_SUCCESS;
		handler.sendMessage(message);

	}

	public void processPacketIQ(IQ iq) {
		int ret = 0; // 是否需要广播
		Boolean flag_notify = false;
		String sTtile = "", sContent = "";
		if (iq == null)
			return;
		Msg message = new Msg();
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) iq.data;

		if (ChatConstants.iq.FEATURE_CLINET_OFFLINE.equals(iq.feature)) {// 被踢下线
			Intent intent = new Intent(
					"com.hjnerp.service.websocket.action.IQ");
			intent.putExtra("data", iq);
			sendBroadcast(intent);
			// /添加操作
			// context.unbindService( );
			SharePreferenceUtil.getInstance(context).setForceExit(false);
			SharePreferenceUtil.getInstance(context).setMySessionId("");
			Intent chatServer = new Intent(context, IMChatService.class);
			this.stopService(chatServer);
			// Log.i("info", "userids=="+iq);
			return;

		} else if (ChatConstants.iq.FEATURE_CREATE_GROUP_CHAT
				.equals(iq.feature)) {// 创建群时邀请我加入群
			ArrayList<FriendInfo> mUserList = ChatPacketHelper
					.processUnFriendsUsersInGroup(iq);

			String lord = (String) dataMap
					.get(ChatConstants.iq.DATA_KEY_GROUP_LORD);
			if (lord.equalsIgnoreCase(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId()))
				return;

			ChatPacketHelper.insertGroup(dataMap);
			QiXinBaseDao.replaceFriendInfos(mUserList, 'N');

			flag_notify = true;
			FriendInfo friendInfo = QiXinBaseDao.queryFriendInfo(lord);
			if (friendInfo != null) {
				sTtile = friendInfo.getFriendname();
			}
			sContent = sTtile
					+ "邀请你加入"
					+ (String) dataMap
							.get(ChatConstants.iq.DATA_KEY_GROUP_NAME) + "群！";

			message.id = StringUtil.getMyUUID();
			message.group = (String) dataMap
					.get(ChatConstants.iq.DATA_KEY_GROUP_ID);
			message.from = lord;
			message.to = SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId();
			message.time = String.valueOf(System.currentTimeMillis());
			message.body = sContent;
			ContactBusiness.saveGroupChatMsgIndb(message, "iq");
		}
		/************************** 别人请求加我为好友 ******************************/
		else if (ChatConstants.iq.FEATURE_ADD_CONTACT.equals(iq.feature)) {// 别人请求添加我为好友
			VerfifyFriendInfo verfifyFiendInfo = getWantMeFriendUserInfo(iq,
					dataMap);
			QiXinBaseDao.replaceTempContactInfo(verfifyFiendInfo);

			flag_notify = true;
			sTtile = verfifyFiendInfo.getFriendname();
			sContent = "请求加你好友！";
		}
		/************************** 别人同意加我为好友 ******************************/
		else if (ChatConstants.iq.FEATURE_CONTACT_AGREE.equals(iq.feature)) {// 别人同意加我为好友,根据临时表是否有此人存在进行相应业务逻辑

			ArrayList<VerfifyFriendInfo> tempFriendsInfo = QiXinBaseDao
					.queryTempContactInfos(iq.from);

			if (tempFriendsInfo.size() > 0) {
				QiXinBaseDao.shiftTempContactInfoById(tempFriendsInfo.get(0)
						.getFriendid());
				QiXinBaseDao.updateTempContactStatus(tempFriendsInfo.get(0)
						.getFriendid(), ContactConstants.VERFIFY_PERMISSION);

				flag_notify = true;
				sTtile = tempFriendsInfo.get(0).getFriendname();
				sContent = "已添加你为好友，现在可以聊天了。";

				// 写入一条聊天记录
				message.id = StringUtil.getMyUUID();
				message.from = iq.from;
				message.to = SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId();
				message.time = String.valueOf(System.currentTimeMillis());
				message.body = sTtile + sContent;
				ContactBusiness.saveSingleChatMsgIndb(message, "iq");
			}
		}
		/************************** 别人拒绝加我为好友 ******************************/
		else if (ChatConstants.iq.FEATURE_CONTACT_REFUSE.equals(iq.feature)) {// 别人拒绝加我为好友
			ArrayList<VerfifyFriendInfo> mfriend = QiXinBaseDao
					.queryTempContactInfos(iq.from);
			if (mfriend.size() > 0) {

				flag_notify = true;
				sTtile = mfriend.get(0).getFriendname();
				sContent = "拒绝了你的好友申请";
			}

		}
		/************************** 有人解除和我的好友关系 ******************************/
		else if (ChatConstants.iq.FEATURE_REMOVE_CONTACT.equals(iq.feature)) {// 这是有人解除和你的好友关系的推送
			String userId = iq.from; // 这个人把你删了
			if (userId.equalsIgnoreCase(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId())) {
				return;// 我把别人删除了
			}
			ContactBusiness.setContactFlag(userId, 'N');// 好友标志置位'N'
			// 删除和他的聊天记录
			QiXinBaseDao
					.deleteSingleMsgById(
							SharePreferenceUtil.getInstance(
									EapApplication.getApplication()
											.getApplicationContext()).getMyId(),
							userId);
			flag_notify = false;
		}
		/************************** 群相关操作 ******************************/
		else if (ChatConstants.iq.FEATURE_OP_GROUP_CHAT.equals(iq.feature)) {// 群相关操作
			String type = (String) dataMap.get(ChatConstants.iq.DATA_KEY_TYPE);
			String groupID = (String) dataMap
					.get(ChatConstants.iq.DATA_KEY_GROUP_ID); // 群ID
			String userIDs = (String) dataMap
					.get(ChatConstants.iq.DATA_KEY_GROUP_USER_IDS);
			/**
			 * @author haijian 判断userIDs是否为空
			 */
			Log.i("info", "userids==" + userIDs);
			if (userIDs == null) {
				return;
			}
			String[] users = userIDs.split("@");
			GroupInfo groupInfo = ChatPacketHelper.getMapGroupInfo(dataMap);
			//
			String fromName = "";
			String usersfrind = "";
			FriendInfo friendInfo = QiXinBaseDao.queryFriendInfo(iq.from);
			if (friendInfo != null) {
				fromName = friendInfo.getFriendname();
			}
			usersfrind = getFriendName(users);
			// //////////////////////////////增加群成员////////////////////////////////////////
			if (ChatConstants.iq.DATA_VALUE_ADD.equals(type)) {
				// 检查群是否存在，如果存在，增加群成员，如果不存在直接
				if (ContactBusiness.checkGroup(groupID)) {
					QiXinBaseDao.insertGroupRelation(groupID, users);
				} else {
					ChatPacketHelper.insertGroup(dataMap);
				}
				if (EapApplication.EXTRA_CURRENT_CHAT_FRIENDORGROUP_ID
						.equalsIgnoreCase((String) dataMap
								.get(ChatConstants.iq.DATA_KEY_GROUP_ID))) {
					flag_notify = false;
				} else {
					flag_notify = true;
				}
				// 保存群成员信息
				ArrayList<FriendInfo> mUserList = ChatPacketHelper
						.processUnFriendsUsersInGroup(iq);
				QiXinBaseDao.replaceFriendInfos(mUserList, 'N');

				sTtile = groupInfo.groupName;
				sContent = fromName + "邀" + usersfrind + "请加入了"
						+ groupInfo.groupName + "群";
				message.id = StringUtil.getMyUUID();
				message.group = (String) dataMap
						.get(ChatConstants.iq.DATA_KEY_GROUP_ID);
				message.from = iq.from;
				message.to = SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId();
				message.time = String.valueOf(System.currentTimeMillis());
				message.body = sContent;
				message.type = "iq";
				ContactBusiness.saveGroupChatMsgIndb(message, "iq");

			} else if (ChatConstants.iq.DATA_VALUE_DROP.equals(type)) {// 解散群
				ContactBusiness.DropGroup(groupID, users);
				flag_notify = true;
				sTtile = groupInfo.groupName;
				sContent = fromName + "解散" + groupInfo.groupName + "群";

			} else if (ChatConstants.iq.DATA_VALUE_EXIT.equals(type)
					|| ChatConstants.iq.DATA_VALUE_REMOVE.equals(type)) {
				sTtile = groupInfo.groupName;
				if (ChatConstants.iq.DATA_VALUE_EXIT.equals(type)) {
					sContent = fromName + "退出了" + groupInfo.groupName + "群";
				} else {
					sContent = fromName + "把" + usersfrind + "移出了"
							+ groupInfo.groupName + "群";
				}

				if (StringUtil.isStringArrayContains(
						users,
						SharePreferenceUtil.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId())) {
					ContactBusiness.DropGroup(groupID, users);
					flag_notify = true;
					sContent = fromName + "把你移出了" + groupInfo.groupName + "群";

				} else {
					QiXinBaseDao.deleteGroupRelations(groupID, users);// 删除群成员关系
					ContactBusiness.saveBeDeleteGroup(iq, false, users);
					message.id = StringUtil.getMyUUID();
					message.group = (String) dataMap
							.get(ChatConstants.iq.DATA_KEY_GROUP_ID);
					message.from = iq.from;
					message.to = SharePreferenceUtil.getInstance(
							EapApplication.getApplication()
									.getApplicationContext()).getMyId();
					message.time = String.valueOf(System.currentTimeMillis());
					message.body = sContent;
					message.type = "iq";
					ContactBusiness.saveGroupChatMsgIndb(message, "iq");

					if (EapApplication.EXTRA_CURRENT_CHAT_FRIENDORGROUP_ID
							.equalsIgnoreCase((String) dataMap
									.get(ChatConstants.iq.DATA_KEY_GROUP_ID))) {
						flag_notify = false;
					} else {
						flag_notify = true;
					}
				}
			}
		}
		/************************************
		 * 6003推送工作流 @author haijian
		 * ****************************************************/
		else if (ChatConstants.iq.FEATURE_WORK_PUSH.equals(iq.feature)) {// 这是有人解除和你的好友关系的推送
			sTtile = "工作流推送";
			sContent = (String) dataMap.get("msg");// 消息发送到通知栏 关联到主界面
			MainActivity.WORK_COUNT = (Integer) dataMap.get("number");// 工作流数量
			flag_notify = true;
		}

		// 回复IQ

		if (ret == 0) {
			Intent intent = new Intent(
					"com.hjnerp.service.websocket.action.IQ");
			intent.putExtra("data", iq);
			intent.putExtra("msg", message);
			sendBroadcast(intent);
			if (flag_notify) {
				SoundPoolPlay.getInstance(context).playSound(1);
				setNotiTypeIQ(R.drawable.icon2, sTtile, sContent, 1);
			}
		}
		HJWebSocketManager.getInstance().sendIqResponses(iq.id);
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		Message message = handler.obtainMessage();
		message.obj = msg;
		message.what = WHAT_NOTIFICATION_FAILURE;
		handler.sendMessage(message);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			String hjMsg = msg.obj.toString();
			ArrayList<Msg> msgList = new ArrayList<Msg>();
			if (hjMsg == null || "".equalsIgnoreCase(hjMsg.trim()))
				return;
			switch (msg.what) {
			case WHAT_NOTIFICATION_SUCCESS:
				try {
					Log.e("XXXXXXXXXX", "------------>" + hjMsg);
					JSONObject jsonObj = new JSONObject(hjMsg);

					if (jsonObj.has("msg")) {

						Msg msg2 = gson.fromJson(jsonObj.getString("msg"),
								Msg.class);
						msgList.add(msg2);
						processPacketMsg(msgList, true);
					} else if (jsonObj.has("iq")) {
						String iqs = jsonObj.getString("iq").toString();
						IQ iq = gson.fromJson(iqs, IQ.class);
						processPacketIQ(iq);
					} else if (jsonObj.has("iqs")) {
						JSONArray jsonArray = jsonObj.getJSONArray("iqs");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jo = jsonArray.getJSONObject(i);
							// Msg msg2 = gson.fromJson(jo.getString("msg"),
							// Msg.class);
							IQ iq = gson.fromJson(jo.getString("iq"), IQ.class);
							processPacketIQ(iq);
							// msgList.add( msg2);
						}
						// String iqs = jsonObj.getString("iq").toString();
						// IQ iq = gson.fromJson(iqs, IQ.class);
						// processPacketIQ(iq);
					} else if (jsonObj.has("msgs")) {
						JSONArray jsonArray = jsonObj.getJSONArray("msgs");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jo = jsonArray.getJSONObject(i);
							Msg msg2 = gson.fromJson(jo.getString("msg"),
									Msg.class);
							msgList.add(msg2);
						}
						processPacketMsg(msgList, false);
					}

				} catch (JSONException e) {
					throw new RuntimeException("json parse error" + hjMsg);
				}

				break;
			case WHAT_NOTIFICATION_FAILURE:
				break;
			default:
				break;
			}

		};
	};

	public VerfifyFriendInfo getWantMeFriendUserInfo(IQ iq,
			Map<String, Object> dataMap) {
		String extraNote = (String) dataMap.get(ChatConstants.iq.DATA_KEY_MSG);
		@SuppressWarnings("unchecked")
		Map<String, String> infoMap = (Map<String, String>) dataMap
				.get(ChatConstants.iq.DATA_KEY_INFO);
		TempContactInfo info = ChatPacketHelper.parseContactInIQ(infoMap);
		VerfifyFriendInfo verfifyFiendInfo = new VerfifyFriendInfo();
		verfifyFiendInfo.setVerfifyNote((String) dataMap
				.get(ChatConstants.iq.DATA_KEY_MSG));
		verfifyFiendInfo.setDeptid(info.getDeptid());
		verfifyFiendInfo.setFriendid(info.getFriendid());
		verfifyFiendInfo.setFriendimage(info.getFriendimage());
		verfifyFiendInfo.setFriendname(info.getFriendname());
		verfifyFiendInfo.setDeptname(info.getDeptname());
		verfifyFiendInfo.setFrienddescribe(info.getFrienddescribe());
		verfifyFiendInfo.setFriendmail(info.getFriendmail());
		verfifyFiendInfo.setFriendmtel(info.getFriendmtel());
		verfifyFiendInfo.setVerfifyNote(extraNote);

		verfifyFiendInfo.setVerfifyResult(ContactConstants.VERFIFY_ONGO);
		verfifyFiendInfo
				.setVerfifyType(ContactConstants.THIS_FRIEND_WANT_ADD_ME);
		return verfifyFiendInfo;
	}

	private String getFriendName(String[] users) {
		String userNmae = "";
		for (String user : users) {
			if (user.equals(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId())) {
				if ("".equalsIgnoreCase(userNmae)) {
					userNmae = "你 ,";
				} else {
					userNmae = "你 ," + userNmae + ",";
				}
			} else {
				FriendInfo friend = QiXinBaseDao.queryFriendInfoall(user);
				if (friend != null) {
					userNmae = userNmae + friend.getFriendname() + ",";
				}
			}
		}
		/**
		 * @author haijian
		 * 增加userNmae.length()>0验证
		 */
		if (userNmae != null && !"".equalsIgnoreCase(userNmae)&&userNmae.length()>0) {
			userNmae = userNmae.substring(0, userNmae.length() - 1);
		}
		return userNmae;
	}

	/**
	 * @author haijian 接收重连的广播
	 */
	BroadcastReceiver reConnectionBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
					|| action.equalsIgnoreCase(Intent.ACTION_SCREEN_ON)
					|| action
							.equals("com.hjnerp.service.websocket.action.reconnect")) {
				Log.i("info", "重连：" + action);
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				Log.i("info", "111111");
				if (info != null && info.isAvailable()) {
					reConnect();
				} else {
					// sendInentAndPre(Constant.RECONNECT_STATE_FAIL);
					Toast.makeText(context, "网络断开,用户已离线!", Toast.LENGTH_SHORT)
							.show();
				}
			}

		}

	};

	/**
	 *
	 * 递归重连，直连上为止.
	 *
	 * @author 李庆义
	 * @update 2012-7-10 下午2:12:25
	 */
	public void reConnect() {
		//
		if (HjHTTPNotificationManager.getInstance().isStoped()) {
			HjHTTPNotificationManager.getInstance().start();
		}

	}
}

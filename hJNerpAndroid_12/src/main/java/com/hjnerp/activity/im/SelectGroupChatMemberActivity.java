package com.hjnerp.activity.im;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.activity.im.adapter.FriendsListAdapter;
import com.hjnerp.activity.im.adapter.HorizontalListViewAdapter;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.util.CharacterParser;
import com.hjnerp.util.FriendSortLetterComparator;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.HorizontalListView;
import com.hjnerp.widget.SideBar;
import com.hjnerp.widget.SideBar.OnTouchingLetterChangedListener;
import com.hjnerp.widget.WaitDialog;
import com.hjnerpandroid.R;

public class SelectGroupChatMemberActivity extends ActivitySupport {
	String TAG = "SelectGroupChatMemberActivity";
	private Dialog setGroupNameDialog;
	public ArrayList<FriendInfo> friendList;
	public ArrayList<FriendInfo> selectedFriendInfoList; //选中的好友
	private RelativeLayout rl_back;
	private SideBar sideBar;
	private Button confirm_btn;
	private TextView message_tv;
	private RelativeLayout rl_setgroupname_cancel, rl_setgroupname_confirm;
	private ListView friendList_lv;
	private EditText et_groupname;

	private FriendsListAdapter friendsListAdapter;
	public HorizontalListViewAdapter horizontalAdapter;
	private HorizontalListView horizontalListView;
	private CharacterParser characterParser;
	public FriendSortLetterComparator pinyinComparator;
//	private List<String> addFriendsIdList;
	private Thread creatGroupThread;
	private Thread addGroupMemberThread;
	private WaitDialog waitDialog;
	private Dialog noticeDialog;
	private IntentFilter filter;
	public static String CREATE_GROUP_SUCCESS = "success";
	public static String CREATE_GROUP_ERROR = "error";
	public static String CREATE_GROUP_UNKNOWNERROE = "unknownerror";
	public static String ACTION_SINGLE_CHAT = "single_chat";
	public static String ACTION_GROUP_CHAT = "group_chat";
	public static String ACTION_CREATE_ERROR = "creategroup_error";
	public static String ACTION_ADD_GROUPMEMBER_SUCCESS = "add_groupmember_success";
	public static String ACTION_ADD_GROUPMEMBER_ERROR = "add_groupmember_error";

	public static String EXTRA_GROUPID = "extra_groupid";
	/* 新建群聊跳转至此时groupId为null,增加群成员跳转至此时groupId不为空 */
	private String groupId = null;
	private GroupInfo groupInfo;
//	private String[] responds = new String[3];
	private String groupName;
	private boolean isSelected = false;
	private final int max_groupmembers_counts = 40;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();

		groupId = getIntent().getStringExtra(EXTRA_GROUPID);

		setContentView(R.layout.groupchatselectmember);
		initMyActionBar();
		initWidget();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void dropGroupChat(String groupId) {
		if(groupId.equals(this.groupId)){
		Log.i(TAG,"群已解散");
		
		if(waitDialog != null){
			waitDialog.dismiss();
		}
		if(setGroupNameDialog != null){
			setGroupNameDialog.dismiss();
		}
		showNoticeDialog("群已经被解散");
		}
	}
	@Override
	public void refalshContent(String groupId) {
		if(groupId.equals(this.groupId)){
		Log.i(TAG,"群已解散");
		
		if(waitDialog != null){
			waitDialog.dismiss();
		}
		if(setGroupNameDialog != null){
			setGroupNameDialog.dismiss();
		}
		showNoticeDialog("你被群主移除出群");
		}
	}
	
	private void initMyActionBar() {
		rl_back = (RelativeLayout) findViewById(R.id.actionbar_back_rl);
		rl_back.setOnClickListener(onClickListener);

		confirm_btn = (Button) findViewById(R.id.actionbar_countmember_iv);
		confirm_btn.setOnClickListener(onClickListener);

	}

	private void initWidget() {
		waitDialog = new WaitDialog(this);
		sideBar = (SideBar) findViewById(R.id.myfriends_slidebar);
		message_tv = (TextView) findViewById(R.id.myfriends_dialog_tv);
		sideBar.setTextView(message_tv);

		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// Get current selected position.
				int position = friendsListAdapter.getPositionForSection(s
						.charAt(0));
				// TODO 需要增加向上箭头的处理
				if (position != -1) {
					friendList_lv.setSelection(position);
				}

			}
		});

		friendList_lv = (ListView) findViewById(R.id.myfriends_lv);

		characterParser = CharacterParser.getInstance();
		pinyinComparator = new FriendSortLetterComparator();
		getFriendList();

		if (null == friendList || friendList.isEmpty()) {
			// TBD
			finish();
			return;
		}

		// sort friend list by the a-Z order
		Collections.sort(friendList, pinyinComparator);

		// 强制在position 0 加上一个数据以便在liseview的position 0 显示“选择一个群”
		FriendInfo zero0 = new FriendInfo();
		ArrayList<FriendInfo> templist = new ArrayList<FriendInfo>();
		templist.add(zero0);
		for (int i = 0; i < friendList.size(); i++) {
			templist.add(friendList.get(i));
		}
		friendList = templist;
		friendsListAdapter = new FriendsListAdapter(this, friendList,
				isSelected);
		friendList_lv.setAdapter(friendsListAdapter);

		friendList_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ImageView selected_iv = (ImageView) view
				// .findViewById(R.id.member_select_iv);
				// selected_iv.callOnClick();

			}
		});

		// 初始化HorizontalListView
		selectedFriendInfoList = new ArrayList<FriendInfo>();
		horizontalListView = (HorizontalListView) findViewById(R.id.horizontallistview1);
		horizontalAdapter = new HorizontalListViewAdapter(this, selectedFriendInfoList);
		horizontalAdapter.notifyDataSetChanged();
		horizontalListView.setAdapter(horizontalAdapter);
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(TAG, "position is " + position+" friends id is "+selectedFriendInfoList.get(position).getFriendid());

				friendsListAdapter.selectedFriends.put(
						selectedFriendInfoList.get(position), false);
				friendsListAdapter.updateListView(friendList,
						friendsListAdapter.selectedFriends);

				selectedFriendInfoList.remove(position);
				horizontalAdapter.updateListView(selectedFriendInfoList);
				

			}
		});

	}

	private void getFriendList() {
		isSelected = false;
		/* 获取全部好友数据 */
		friendList = QiXinBaseDao.queryFriendInfos("Y");

		for(int i =0;i<friendList.size();i++){
			Log.e(TAG,friendList.get(i).getFriendid() + " " + friendList.get(i).getFriendimage());
		}
		/* 去掉自己 */
		removeOneItem(sputil.getMyId());

		/* 如果是新增群成员，去掉已经是群成员的部分 */
		if (groupId != null) {
			removeRepeat();
		}
		/* 设置名称首字母 */
		handleFriendListData();
	}

	private void removeOneItem(String id) {
		for (int i = 0; i < friendList.size(); i++) {
			if (id.equals(friendList.get(i).getFriendid())) {
				friendList.remove(i);
			}
		}

	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	// 去掉已经是群成员的部分
	private void removeRepeat() {
		// TODO 根据groupId获取群成员
		// ArrayList<FriendInfo> fList = BaseDao.queryFriendInfos();
		String[] fList = QiXinBaseDao.queryGroupRelations(groupId);
		for (int i = 0; i < fList.length; i++) {
			removeOneItem(fList[i]);
		}
	}

	/**
	 * Initialize user's sort letter.
	 */
	private void handleFriendListData() {
		if (null == friendList || friendList.isEmpty()) {
			return;
		}

		Iterator<FriendInfo> ite = friendList.iterator();

		while (ite.hasNext()) {
			FriendInfo friend = ite.next();
			String pinyin = characterParser.getSelling(friend.getFriendname());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				friend.setSortLetter(sortString.toUpperCase().charAt(0));
			} else {
				friend.setSortLetter('#');
			}

		}
	}

	// TODO
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.actionbar_back_rl:
				finish();
				break;
			case R.id.actionbar_countmember_iv:
				
				handleClick();

				break;
			case R.id.dialog_group_confirm_rl:
				
				groupName = et_groupname.getText().toString();
				if (!StringUtil.isNullOrEmpty(groupName)) {
					setGroupNameDialog.dismiss();
					waitDialog.show();
					createGroup();
				}

				break;
			case R.id.dialog_group_cancel_rl:
				setGroupNameDialog.dismiss();
				break;
			default:
				break;
			}

		}
	};

	public void setConfirmBtnText(int count) {
		String confirmStr = getResources().getString(R.string.confirm);
		if (count == 0) {
			confirm_btn.setText(confirmStr);
			confirm_btn.setTextColor(getResources().getColor(
					R.color.creategroup_normal_white));
			confirm_btn.setEnabled(false);
		} else {
			confirm_btn.setText(confirmStr + "(" + count + ")");
			confirm_btn.setTextColor(getResources().getColor(R.color.white));
			confirm_btn.setEnabled(true);
		}
	}


	private void handleClick() {

		if(groupId == null){// 创建群
			if (selectedFriendInfoList.size() == 1) {//进入单人聊天

				sendToHandler(ACTION_SINGLE_CHAT);
				return;
			} 
//			else if(selectedFriendInfoList.size() >= max_groupmembers_counts){
//				ToastUtil.ShowLong(this, "群成员人数超过限制");
//
//			}
			else{
				showSetGroupNameDialog();
			}
		}else{//增加群成员/
			String[] fList = QiXinBaseDao.queryGroupRelations(groupId);
//			if(selectedFriendInfoList.size() + fList.length >= max_groupmembers_counts){
//				ToastUtil.ShowLong(this, "群成员人数超过限制");
//			}else{
				addGroupMember();
//			}
		}
	}

	private void createGroup() {
		creatGroupThread = new Thread() {
			@Override
			public void run() {
				handleCreateGroup();

			}
		};
		creatGroupThread.start();
	}
	private void addGroupMember() {
		addGroupMemberThread = new Thread() {
			@Override
			public void run() {
				handleAddGroupMember();

			}
		};
		addGroupMemberThread.start();
	}
	
	
	private void handleCreateGroup() {

		String[] ids = new String[selectedFriendInfoList.size()];
		List<String> templist = new ArrayList<String>();
		for(int i = 0;i<selectedFriendInfoList.size();i++){
			String id = new String();
			id = selectedFriendInfoList.get(i).getFriendid();
			templist.add(id);
		}
		
		try {
			// run this in thread return 3string
			// "success"+groupId+"msg"-success;
			// "error"+"error code"+"msg"-error;"unknown"
			// String groupName = "天地会"+StringUtil.randomString(10);

			// TODO dialog to create group name
			/*mService.createGroupChat(groupName, templist, new WebSocketClient.ResponseCallback() {
				
				@Override
				public void onReceive(DataPacket packet) {
					IQ iq = (IQ)packet;
					if(ChatPacketHelper.parseErrorCode(iq) == null)
					{
						final Map<String, Object> data = (Map<String, Object>) iq.data;
						if(!data.containsKey(ChatConstants.iq.DATA_KEY_GROUP_LORD))
							data.put(ChatConstants.iq.DATA_KEY_GROUP_LORD, (String) EapApplication.getApplication()
									.getExtra(EapApplication.EXTRA_USER_ID));
						final String groupID = (String)data.get(ChatConstants.iq.DATA_KEY_GROUP_ID);
						final String msg = (String)data.get(ChatConstants.iq.DATA_KEY_MSG);
						worker.postDML(new SQLiteWorker.AbstractSQLable()
						{
							@Override
							public void onCompleted(Object event)
							{
								if(!(event instanceof Throwable))
								{
									groupInfo = QiXinBaseDao.queryGroupInfo(groupID);
								}
							}
							@Override
							public Object doAysncSQL()
							{
								ChatPacketHelper.insertGroup(data);
								return null;
							}
						});
						
//						groupInfo = new GroupInfo();
//						groupInfo.groupID = groupID;
//						groupInfo.groupName = groupName;
//						groupInfo.groupType = ChatConstants.iq.TYPE_CHAT;

						final ChatHisBean cBean = new ChatHisBean();
						cBean.setMsgTo(groupID);
						cBean.setId(ChatPacketHelper.getGeneralUUID());
						cBean.setMsgFrom(sputil.getMyId());
						cBean.setMsgcontent("你创建了一个群");
						cBean.setMsgTime(String.valueOf(System.currentTimeMillis()));
						cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);
						worker.postDML(new SQLiteWorker.AbstractSQLable()
						{
							@Override
							public void onCompleted(Object event)
							{
								if(!(event instanceof Throwable))
								{
									sendToHandler(ACTION_GROUP_CHAT);
								}
							}
							@Override
							public Object doAysncSQL()
							{
								QiXinBaseDao.insertMsg(cBean);
								return null;
							}
						});
						
					}else{
						//error
						sendToHandler(ACTION_CREATE_ERROR);
					}
				}
			});
			*/
			
//			if (responds != null) {
//				if (CREATE_GROUP_SUCCESS.equals(responds[0])) {
//					groupInfo = new GroupInfo();
//					groupInfo.groupID = responds[1];
//					groupInfo.groupName = groupName;
//					groupInfo.groupType = ChatConstants.iq.TYPE_CHAT;
//					Log.i(TAG, ">>>>>>>>>>>>>>>" + responds[2]);
//
//					ChatHisBean cBean = new ChatHisBean();
//					cBean.setMsgTo(responds[1]);
//					cBean.setId(ChatPacketHelper.getGeneralUUID());
//					cBean.setMsgFrom(sputil.getMyId());
//					cBean.setMsgcontent("你创建了一个群");
//					cBean.setMsgTime(String.valueOf(System.currentTimeMillis()));
//					cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);
//
//					QiXinBaseDao.insertMsg(cBean);
//
//					sendToHandler(ACTION_GROUP_CHAT);
//				} else if (CREATE_GROUP_ERROR.equals(responds[0])) {
//					Log.i(TAG, ">>>>>>>>>>>>>>>" + responds[2]);
//					sendToHandler(ACTION_CREATE_ERROR);
//				} else {
//					Log.i(TAG, responds[1]);
//					sendToHandler(ACTION_CREATE_ERROR);
//				}
//			}
		} catch (Exception e) {
			sendToHandler(ACTION_CREATE_ERROR);
			Log.e(TAG, "---------------", e);
		}

	}

	private void handleAddGroupMember(){
		List<String> templist = new ArrayList<String>();
		for(int i = 0;i<selectedFriendInfoList.size();i++){
			String id = new String();
			id = selectedFriendInfoList.get(i).getFriendid();
			templist.add(id);
		}
		
		/*
			mService.doGroupOpt(groupId, templist, ChatConstants.iq.DATA_VALUE_ADD, new ResponseCallback() {
				
				@Override
				public void onReceive(DataPacket packet) {
					IQ iq = (IQ)packet;
					String errorMsg = ChatPacketHelper.parseErrorCode(iq);
					Log.i(TAG,"drop group respond is " + errorMsg);
					if(errorMsg != null)
					{
						sendToHandler(ACTION_ADD_GROUPMEMBER_ERROR);
						
					}else{
						//TODO 在db中增加群成员
						Log.i(TAG,"add groupmember ok");
						
						sendToHandler(ACTION_ADD_GROUPMEMBER_SUCCESS);
						
					}
					
				}
			});
			*/
	
	}
	
	private void sendToHandler(String msg) {

		Message Msg = new Message();
		Bundle b = new Bundle();
		b.putString("flag", msg);
		Msg.setData(b);

		myHandler.sendMessage(Msg);
	}

	final Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			waitDialog.dismiss();
			Bundle b = msg.getData();
			String mmsg = b.getString("flag");
			Log.i(TAG, "******* handler receive mmsg is " + mmsg);
			if (mmsg.equals(ACTION_SINGLE_CHAT)) {
				FriendInfo friendinfo = new FriendInfo();

				friendinfo = selectedFriendInfoList
						.get(0);

				Intent intent = new Intent(SelectGroupChatMemberActivity.this,
						ChatActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Constant.IM_NEWS,
						(Serializable) friendinfo);
				intent.putExtras(mBundle);
				startActivity(intent);
				finish();
			} else if (mmsg.equals(ACTION_GROUP_CHAT)) {

				Log.i(TAG, "jump to group chat ");
				Intent intent = new Intent();
				intent.setClass(SelectGroupChatMemberActivity.this,
						ChatActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Constant.IM_GOUP_NEWS,
						(Serializable) groupInfo);
				intent.putExtras(mBundle);

				startActivity(intent);
				finish();

			} else if (mmsg.equals(ACTION_CREATE_ERROR)) {
				ToastUtil.ShowLong(SelectGroupChatMemberActivity.this,
						"创建失败，请重试");
			}else if(mmsg.equals(ACTION_ADD_GROUPMEMBER_SUCCESS)){
				final String[] ids = new String[selectedFriendInfoList.size()];
				for(int i = 0;i<selectedFriendInfoList.size();i++){
					String id = new String();
					id = selectedFriendInfoList.get(i).getFriendid();
					ids[i] = id;
				}
				worker.postDML(new SQLiteWorker.AbstractSQLable()
				{
					@Override
					public void onCompleted(Object event)
					{
						if(!(event instanceof Throwable))
						{
							finish();
						}
					}
					@Override
					public Object doAysncSQL()
					{
						QiXinBaseDao.insertGroupRelation(groupId,ids);
						return null;
					}
				});
				
			}else if(mmsg.equals(ACTION_ADD_GROUPMEMBER_ERROR)){
				ToastUtil.ShowLong(SelectGroupChatMemberActivity.this, "系统繁忙，请重试");
			}
		};
	};

	public List<String> countMember(HashMap<String, Boolean> selectedValues,
			boolean isAdd) {
		List<String> userIdList = new ArrayList<String>();
		Set<String> setKeys = selectedValues.keySet();
		Iterator<String> ite = setKeys.iterator();
		while (ite.hasNext()) {
			String key = ite.next();
			if (selectedValues.get(key) == isAdd) {
				userIdList.add(key);
			}

		}
		return userIdList;
	}

	private void showSetGroupNameDialog() {
		setGroupNameDialog = new Dialog(this, R.style.input_dialog);
		setGroupNameDialog.setContentView(R.layout.dialog_renamegroup);
		et_groupname = (EditText) setGroupNameDialog
				.findViewById(R.id.dialog_goup_rename_et);
		rl_setgroupname_confirm = (RelativeLayout) setGroupNameDialog
				.findViewById(R.id.dialog_group_confirm_rl);
		rl_setgroupname_cancel = (RelativeLayout) setGroupNameDialog
				.findViewById(R.id.dialog_group_cancel_rl);
		rl_setgroupname_confirm.setOnClickListener(onClickListener);
		rl_setgroupname_cancel.setOnClickListener(onClickListener);

		setGroupNameDialog.show();
	}
	/*提示用户群已解散*/
	private void showNoticeDialog(String msg) {
		noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
		noticeDialog.setContentView(R.layout.dialog_notice_nocancel);
		noticeDialog.setCancelable(false);
		noticeDialog.setCanceledOnTouchOutside(false);
		RelativeLayout dialog_confirm_rl;
		TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
		notice.setText(msg);
		dialog_confirm_rl = (RelativeLayout) noticeDialog.findViewById(R.id.dialog_nc_confirm_rl);
		dialog_confirm_rl.setOnClickListener(dialogOnClickListener);		
		
		noticeDialog.show();
	}
	
	private OnClickListener dialogOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dialog_nc_confirm_rl:
				noticeDialog.dismiss();
				jumpToMain(SelectGroupChatMemberActivity.this);
				break;
			default:
				break;
			}

		}
	};

}

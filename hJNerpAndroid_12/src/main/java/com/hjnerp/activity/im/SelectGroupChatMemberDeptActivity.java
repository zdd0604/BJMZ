package com.hjnerp.activity.im;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.activity.im.adapter.FriendsListAdapterDept;
import com.hjnerp.activity.im.adapter.HorizontalListViewAdapter;
import com.hjnerp.business.ContactBusiness;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.model.DeptInfo;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.util.CharacterParser;
import com.hjnerp.util.FriendSortLetterComparator;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.HorizontalListView;
import com.hjnerp.widget.WaitDialog;
import com.hjnerpandroid.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectGroupChatMemberDeptActivity extends ActivitySupport {
	String TAG = "SelectGroupChatMemberActivity";
	private Dialog setGroupNameDialog; 
	public ArrayList<FriendInfo> selectedFriendInfoList = new ArrayList<FriendInfo>() ; //选中的好友
	private RelativeLayout rl_back;
	private Button confirm_btn;
//	private RelativeLayout rl_setgroupname_cancel, rl_setgroupname_confirm;
	private EditText et_groupname;

	private ExpandableListView exListView = null;
	private FriendsListAdapterDept friendListAdapterDept;
	
	public HorizontalListViewAdapter horizontalAdapter;
	private HorizontalListView horizontalListView;
	private CharacterParser characterParser;
	public FriendSortLetterComparator pinyinComparator;
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
//	private boolean isSelected = false;
	private final int max_groupmembers_counts = 40;
	private ArrayList<DeptInfo> deptInfoList;
	@BindView(R.id.dialog_group_cancel_tv)
	TextView dialog_group_cancel_tv;
	@BindView(R.id.dialog_group_confirm_tv)
	TextView dialog_group_confirm_tv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		groupId = getIntent().getStringExtra(EXTRA_GROUPID); 
		setContentView(R.layout.groupchatselectmember_dpet);
		ButterKnife.bind(this);
		initMyActionBar();
		initWidget();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	//增加群成员过程中群已解散
	@Override
	public void dropGroupChat(String groupId) {
		if(groupId.equals(this.groupId)){
		 
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
		rl_back = (RelativeLayout) findViewById(R.id.actionbar_back_rl_dept);
		rl_back.setOnClickListener(onClickListener);

		confirm_btn = (Button) findViewById(R.id.actionbar_countmember_iv_dept);
		confirm_btn.setOnClickListener(onClickListener);

	}

	private void initWidget() {
		waitDialog = new WaitDialog(this);


		exListView = (ExpandableListView) findViewById(R.id.expand_list_selectgroupmember_dept);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new FriendSortLetterComparator();
		getFriendList();

		if (null == deptInfoList || deptInfoList.isEmpty()) {
			finish();
			return;
		}

		friendListAdapterDept = new FriendsListAdapterDept(this, deptInfoList);
		exListView.setAdapter(friendListAdapterDept);
		
		
		exListView.setGroupIndicator(null);
		keepExpand(exListView);

		exListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				 
				if(groupPosition == 0){
					Intent intent1 = new Intent(SelectGroupChatMemberDeptActivity.this,
							ShowGroupActivity.class);
					startActivity(intent1);
				}
				
				// return true to keep expand
				return true;
			}
		});
		// 初始化HorizontalListView
		horizontalListView = (HorizontalListView) findViewById(R.id.horizontallistview1_dept);
		horizontalAdapter = new HorizontalListViewAdapter(this, selectedFriendInfoList);
		horizontalAdapter.notifyDataSetChanged();
		horizontalListView.setAdapter(horizontalAdapter);
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Log.i(TAG, "position is " + position+" friends id is "+selectedFriendInfoList.get(position).getFriendid());

//				friendsListAdapter.selectedFriends.put(
//						selectedFriendInfoList.get(position), false);
//				friendsListAdapter.updateListView(friendList,
//						friendsListAdapter.selectedFriends);

//				selectedFriendInfoList.remove(position);
//				horizontalAdapter.updateListView(selectedFriendInfoList);
				

			}
		});

	}

	/* 获取全部好友数据 */
	private void getFriendList() {
		//deptInfoList 第一个数据是选择群租
		deptInfoList = new ArrayList<DeptInfo>();
		DeptInfo addfriend = new DeptInfo();
		addfriend.setDeptName("选择群组");
		addfriend.setDeptChild(null);
		deptInfoList.add(addfriend);
		
		//查找好友并加入deptInfoList
		ArrayList<DeptInfo> temp = new ArrayList<DeptInfo>();
		if(groupId != null){
			temp  = ContactBusiness.getDepInfos(sputil.getMyId(),QiXinBaseDao.queryGroupRelations(groupId));
		}else{
			temp = ContactBusiness.getDepInfos(sputil.getMyId(),null);
		}
		deptInfoList.addAll(temp);
		
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.actionbar_back_rl_dept:
				finish();
				break;
			case R.id.actionbar_countmember_iv_dept:
				
				handleClick();

				break;
			case R.id.dialog_group_confirm_tv:
				groupName = et_groupname.getText().toString();
				if (!StringUtil.isNullOrEmpty(groupName)) {
					setGroupNameDialog.dismiss();
					waitDialog.show();
					createGroup();
				}

				break;
			case R.id.dialog_group_cancel_tv:
				setGroupNameDialog.dismiss();
				break;
			default:
				break;
			}

		}
	};


	public void setConfirmBtnText() {
		int count = selectedFriendInfoList.size();
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
	

	public void addSelectedFriends(FriendInfo friendInfo){
		selectedFriendInfoList.add(friendInfo);
	}
	public void deleteSelectedFriends(FriendInfo friendInfo){

		for (int i = 0; i < selectedFriendInfoList.size(); i++) {
			if (friendInfo.getFriendid().equals(selectedFriendInfoList.get(i).getFriendid())) {
				selectedFriendInfoList.remove(i);
			}
		}	
	}
	
	public boolean ifIsSelected(FriendInfo friendInfo){
		boolean isSelected = false;
		for (int i = 0; i < selectedFriendInfoList.size(); i++) {
			if (friendInfo.getFriendid().equals(selectedFriendInfoList.get(i).getFriendid())) {
				isSelected = true;
				return true;
			}
		}
		
		return isSelected;
	}

	private void handleClick() {

		if(groupId == null){// 创建群
			if (selectedFriendInfoList.size() == 1) {//进入单人聊天

				sendToHandler(ACTION_SINGLE_CHAT);
				return;
			} 
			else if(selectedFriendInfoList.size() >= max_groupmembers_counts){
				ToastUtil.ShowLong(this, "群成员数量超过客户端建群的最大成员数量：40人！");

			}
			else{
				showSetGroupNameDialog();
			}
		}else{//增加群成员/
			String[] fList = QiXinBaseDao.queryGroupRelations(groupId);
			if(selectedFriendInfoList.size() + fList.length >= max_groupmembers_counts){
				ToastUtil.ShowLong(this, "群成员数量超过客户端建群的最大成员数量：40人！");
			}else{
				addGroupMember();
			}
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
		//	Log.e(TAG,"create group selectedFriendInfoList " + i + " " + selectedFriendInfoList.get(i).getFriendname() +  " " + selectedFriendInfoList.get(i).getFriendid());
			String id = new String();
			id = selectedFriendInfoList.get(i).getFriendid();
			templist.add(id);
		}
		
		try {


			IQ iq = HJWebSocketManager.getInstance().createGroupChat(groupName, templist);
			String errorCode = ChatPacketHelper.parseErrorCode(iq);
			if(errorCode == null){//成功
				final Map<String, Object> data = (Map<String, Object>) iq.data;
				if(!data.containsKey(ChatConstants.iq.DATA_KEY_GROUP_LORD))
					data.put(ChatConstants.iq.DATA_KEY_GROUP_LORD, sputil.getMyId());
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

				final ChatHisBean cBean = new ChatHisBean();
				cBean.setMsgTo(groupID);
				cBean.setId(ChatPacketHelper.getGeneralUUID());
				cBean.setMsgFrom(sputil.getMyId());
				cBean.setMsgcontent("你创建了一个群,现在可以聊天了");
				cBean.setmsgSendStatus(Constant.MSG_SEND_STATUS_SUCCESS );
				cBean.setMsgIdRecorder(sputil.getMyId());
				cBean.setMsgTime(String.valueOf(System.currentTimeMillis()));
				cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);
				cBean.setMsgIdType(  ChatConstants.msg.TYPE_ID_IQ);
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
			}else{//不成功
				ToastUtil.ShowShort(SelectGroupChatMemberDeptActivity.this, errorCode);
				
			}
					

		} catch (Exception e) {
			sendToHandler(ACTION_CREATE_ERROR); 
		}

	}

	private void handleAddGroupMember(){
		List<String> templist = new ArrayList<String>();
		for(int i = 0;i<selectedFriendInfoList.size();i++){
//			Log.e(TAG,"add groupMember " + i + " " + selectedFriendInfoList.get(i).getFriendname() + selectedFriendInfoList.get(i).getFriendid());
			String id = new String();
			id = selectedFriendInfoList.get(i).getFriendid();
			templist.add(id);
		}
		
		for(int i =0;i<templist.size();i++){
//			Log.e(TAG," templist " + i + " " + templist.get(i));
		}
		IQ iq = HJWebSocketManager.getInstance().doGroupOpt(groupId, templist, ChatConstants.iq.DATA_VALUE_ADD);
		String errorMsg = ChatPacketHelper.parseErrorCode(iq);
		
		if(errorMsg != null)
		{
			sendToHandler(ACTION_ADD_GROUPMEMBER_ERROR);
			
		}else{
			sendToHandler(ACTION_ADD_GROUPMEMBER_SUCCESS);
			
		}
		
//			mService.doGroupOpt(groupId, templist, ChatConstants.iq.DATA_VALUE_ADD, new ResponseCallback() {
//				
//				@Override
//				public void onReceive(DataPacket packet) {
//					IQ iq = (IQ)packet;
//					String errorMsg = ChatPacketHelper.parseErrorCode(iq);
//					Log.i(TAG,"drop group respond is " + errorMsg);
//					if(errorMsg != null)
//					{
//						sendToHandler(ACTION_ADD_GROUPMEMBER_ERROR);
//						
//					}else{
//						
//						Log.i(TAG,"add groupmember ok");
//						
//						sendToHandler(ACTION_ADD_GROUPMEMBER_SUCCESS);
//						
//					}
//					
//				}
//			});
	
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
//			Log.i(TAG, "******* handler receive mmsg is " + mmsg);
			if (mmsg.equals(ACTION_SINGLE_CHAT)) {
				FriendInfo friendinfo = new FriendInfo();

				friendinfo = selectedFriendInfoList
						.get(0);

				Intent intent = new Intent(SelectGroupChatMemberDeptActivity.this,
						ChatActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Constant.IM_NEWS,
						(Serializable) friendinfo);
				intent.putExtras(mBundle);
				startActivity(intent);
				finish();
			} else if (mmsg.equals(ACTION_GROUP_CHAT)) {

//				Log.i(TAG, "jump to group chat ");
				Intent intent = new Intent();
				intent.setClass(SelectGroupChatMemberDeptActivity.this,
						ChatActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Constant.IM_GOUP_NEWS,
						(Serializable) groupInfo);
				intent.putExtras(mBundle);

				startActivity(intent);
				finish();

			} else if (mmsg.equals(ACTION_CREATE_ERROR)) {
				ToastUtil.ShowLong(SelectGroupChatMemberDeptActivity.this,
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
				ToastUtil.ShowLong(SelectGroupChatMemberDeptActivity.this, "系统繁忙，请重试");
			}
		};
	};


	private void showSetGroupNameDialog() {
//		Log.e(TAG,"showSetGroupNameDialog");
		setGroupNameDialog = new Dialog(this, R.style.input_dialog);
		setGroupNameDialog.setContentView(R.layout.dialog_renamegroup);
		et_groupname = (EditText) setGroupNameDialog.findViewById(R.id.dialog_goup_rename_et);
		dialog_group_confirm_tv.setOnClickListener(onClickListener);
		dialog_group_cancel_tv.setOnClickListener(onClickListener);
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
				jumpToMain(SelectGroupChatMemberDeptActivity.this);
				break;
			default:
				break;
			}

		}
	};
	// expand全部展开
	private void keepExpand(ExpandableListView mexpand) {

		if (deptInfoList.size() != 0) {
			for (int i = 0; i < deptInfoList.size(); i++) {
				mexpand.expandGroup(i);
			}
		}
	}
}

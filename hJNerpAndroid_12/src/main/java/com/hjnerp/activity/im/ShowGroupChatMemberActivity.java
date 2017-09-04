package com.hjnerp.activity.im;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.activity.MainActivity;
import com.hjnerp.activity.im.adapter.ShowGroupChatMemberGridAdapter;
import com.hjnerp.business.ContactBusiness;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.WaitDialog;
import com.hjnerpandroid.R;

/**
 * 有些群内成员不是好友，需要从服务器获取这些用户的信息；目前没有存储，只能每次都从服务器获取
 * Activity首次创立是在onWebSocketServiceReady（）方法中加载数据，其余时刻在onResume中刷新数据
 * 
 */
public class ShowGroupChatMemberActivity extends ActivitySupport {
	private String TAG = "ShowGroupChatMemberActivity";
	private GridView mGridView;
	private RelativeLayout group_name_rl, group_maxnumber_rl,
			delete_chathistory_rl, search_chathistory_rl;
	private Button delete_group_btn;
	private TextView groupName_tv,groupMemberCounts;
	private int group_member_counts;
	private ShowGroupChatMemberGridAdapter mAdapter;
	private ArrayList<FriendInfo> mFriendList;
	private FriendInfo deleteFriendInfo;
	public GroupInfo group;
	private Thread mThread;
	private WaitDialog waitDialog;
	private Dialog setGroupNameDialog;
	private Dialog noticeDialog = null;

	private String groupname = "";
	private TextView tv_setgroupname_cancel, tv_setgroupname_confirm;
	private EditText et_groupname;
	public String myId;
	public String lordId;
	private String deleteFriendId;// 最近一次删除的群成员的id
	private String groupNewName;
	private FriendInfo tempFriendInfo;
	private int notFriendsCounts = 0;
	/* 群解散或自己被群主移除或user解除了和自己的好友关系的标志 */
	public Boolean ifGroupExit = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		group = (GroupInfo) getIntent().getSerializableExtra(
				Constant.GROUP_READ);

		lordId = group.groupLord;
	 
		myId = sputil.getMyId();
		if (myId == null || lordId == null || group == null) {
			finish();
		}

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.show_groupchat_member, null);
		this.setContentView(view);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle(group.groupName + "群成员");

		getView();

		mFriendList = new ArrayList<FriendInfo>();
		mAdapter = new ShowGroupChatMemberGridAdapter(this, mFriendList);
		mGridView.setAdapter(mAdapter);
		waitDialog = new WaitDialog(this);
		// waitDialog.show();// 等待数据加载完成
	}

	@Override
	public void addUserFromGroup(String groupId) {
		Log.i(TAG, "add new member " + groupId);
		getData();
	}

	@Override
	public void quitUserFromGroup(String groupId) {
		getData();
	}

	@Override
	public void dropGroupChat(String groupId) {
		if (groupId.equals(group.groupID)) {
			ifGroupExit = false;
			showNoticeDialog("群已经被解散");
		}
	}

	@Override
	public void refalshContent(String groupId) {
		if (groupId.equals(group.groupID)) {
			ifGroupExit = false;
			showNoticeDialog("你被群主移除出群");
		}
	}

	@Override
	public void refreshContact() {
		ifGroupExit = ContactBusiness.checkGroup(group.groupID);
		if (ifGroupExit) {
			getData();
		} else {
			showNoticeDialog("群已经被解散");
		}
	}

	private void getView() {
		mGridView = (GridView) findViewById(R.id.gridView_friend);
		delete_group_btn = (Button) findViewById(R.id.btn_deletegroup);
		group_name_rl = (RelativeLayout) findViewById(R.id.rl_groupname);
		group_maxnumber_rl = (RelativeLayout) findViewById(R.id.rl_maxnumber);
		delete_chathistory_rl = (RelativeLayout) findViewById(R.id.rl_deletechat);
		search_chathistory_rl = (RelativeLayout) findViewById(R.id.rl_searchchat);
		groupName_tv = (TextView) findViewById(R.id.tv_groupname);
		groupMemberCounts = (TextView) findViewById(R.id.tv_maxnumber);
		
		groupName_tv.setText(group.groupName);
		

		delete_group_btn.setOnClickListener(onClickListener);
		group_name_rl.setOnClickListener(onClickListener);
		group_maxnumber_rl.setOnClickListener(onClickListener);
		delete_chathistory_rl.setOnClickListener(onClickListener);
		search_chathistory_rl.setOnClickListener(onClickListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getData();
		groupMemberCounts.setText( group_member_counts + " 人 ");
	}

	private void getData() {
		// 根据groupId获取群成员
		mFriendList = new ArrayList<FriendInfo>();
		String[] friendsId = QiXinBaseDao.queryGroupRelations(group.groupID); 
		for (int i = 0; i < friendsId.length; i++) {
			FriendInfo friendInfo = new FriendInfo();
			friendInfo = ContactBusiness.queryUserInfo(friendsId[i]);
			if (friendInfo.getFriendid() == null) { 
				finish();
			} else { 
				mFriendList.add(friendInfo);
			}
		}
		group_member_counts = mFriendList.size();
		
		mFriendList = handleDate(mFriendList);
		mAdapter = new ShowGroupChatMemberGridAdapter(this, mFriendList);
		mGridView.setAdapter(mAdapter);
	}

	/**
	 * list最后强制加上一组自定义数据，用以显示“+” list最后强制加上一组数据,用以显示“-”（如果群主是自己的话）
	 * 
	 * @param mList
	 * @return
	 */
	private ArrayList<FriendInfo> handleDate(ArrayList<FriendInfo> mList) {
		FriendInfo mFriendInfo = new FriendInfo();
		mFriendInfo.setFriendid("");
		mList.add(mFriendInfo);
		if (group.groupLord.equals(sputil.getMyId())) {
			mFriendInfo = new FriendInfo();
			mFriendInfo.setFriendid("");
			mList.add(mFriendInfo);
		}
		return mList;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_groupname: // 更改群名称
				showSetGroupNameDialog();
				break;

			case R.id.rl_maxnumber: // 更改群最大成员数量

				break;
			case R.id.rl_deletechat: // 删除群聊记录

				break;
			case R.id.rl_searchchat: // 查找群聊记录

				break;
			case R.id.btn_deletegroup: // 删除并推出群
				if (ifIamLord()) {// 解散群
					dropGroup();
				} else {// 退出群
					quitGroup();
				}
				break;
			default:
				break;
			}

		}
	};
	

	private Thread dropGroupThread;
	public void dropGroup() {
		dropGroupThread = new Thread() {
			@Override
			public void run() {
				ArrayList<String> ids = new ArrayList<String>();
				ids.add(myId);
				IQ iq = HJWebSocketManager.getInstance().doGroupOpt(group.groupID, ids,ChatConstants.iq.DATA_VALUE_DROP);
				String errorMsg = ChatPacketHelper.parseErrorCode(iq);
				if (errorMsg != null) {

				} else {
					// TODO 在db中删除该群和聊天记录并推出当前页面
					final String[] friendsId = QiXinBaseDao
							.queryGroupRelations(group.groupID);
					worker.postDML(new SQLiteWorker.AbstractSQLable()
					{
						@Override
						public void onCompleted(Object event)
						{
							if(!(event instanceof Throwable))
							{
								sendToHandler(0);
							}
						}
						@Override
						public Object doAysncSQL()
						{
							ContactBusiness.DropGroup(group.groupID, friendsId);
							return null;
						}
					});
				}
			}
		};
		dropGroupThread.start();
	}
	
	
//	private void dropGroup1() {
//		ArrayList<String> ids = new ArrayList<String>();
//		ids.add(myId);
//		Log.i(TAG,"myId is " + ids.get(0));
//		mService.doGroupOpt(group.groupID, ids,
//				ChatConstants.iq.DATA_VALUE_DROP, new ResponseCallback() {
//
//					@Override
//					public void onReceive(DataPacket packet) {
//						IQ iq = (IQ) packet;
//						String errorMsg = ChatPacketHelper.parseErrorCode(iq);
//						Log.i(TAG, "drop group respond is " + errorMsg);
//						if (errorMsg != null) {
//
//						} else {
//							// TODO 在db中删除该群和聊天记录并推出当前页面
//							Log.i(TAG, "drop group ok");
//							final String[] friendsId = QiXinBaseDao
//									.queryGroupRelations(group.groupID);
//							worker.postDML(new SQLiteWorker.AbstractSQLable()
//							{
//								@Override
//								public void onCompleted(Object event)
//								{
//									if(!(event instanceof Throwable))
//									{
//										sendToHandler(0);
//									}
//								}
//								@Override
//								public Object doAysncSQL()
//								{
//									ContactBusiness.DropGroup(group.groupID, friendsId);
//									return null;
//								}
//							});
//						}
//
//					}
//				});
//	}

	
	private Thread quitGroupThread;
	public void quitGroup() {
		quitGroupThread = new Thread() {
			@Override
			public void run() {
				ArrayList<String> ids = new ArrayList<String>();
				ids.add(myId);
				IQ iq = HJWebSocketManager.getInstance().doGroupOpt(group.groupID, ids,ChatConstants.iq.DATA_VALUE_EXIT);
				String errorMsg = ChatPacketHelper.parseErrorCode(iq);
				if (errorMsg != null) {

				} else {
					// TODO 在db中删除该群和聊天记录并推出当前页面 
					final String[] friendsId = QiXinBaseDao
							.queryGroupRelations(group.groupID);
					worker.postDML(new SQLiteWorker.AbstractSQLable()
					{
						@Override
						public void onCompleted(Object event)
						{
							if(!(event instanceof Throwable))
							{
								sendToHandler(0);
							}
						}
						@Override
						public Object doAysncSQL()
						{
							ContactBusiness.DropGroup(group.groupID, friendsId);
							return null;
						}
					});

				}
			}
		};
		quitGroupThread.start();
	}
	
//	private void quitGroup1() {
//		ArrayList<String> ids = new ArrayList<String>();
//		ids.add(myId);
//		Log.i(TAG,"myId is " + ids.get(0));
//		mService.doGroupOpt(group.groupID, ids,
//				ChatConstants.iq.DATA_VALUE_EXIT, new ResponseCallback() {
//
//					@Override
//					public void onReceive(DataPacket packet) {
//						IQ iq = (IQ) packet;
//						String errorMsg = ChatPacketHelper.parseErrorCode(iq);
//						Log.i(TAG, "delete myself group respond is " + errorMsg);
//						if (errorMsg != null) {
//
//						} else {
//							// TODO 在db中删除该群和聊天记录并推出当前页面
//							Log.i(TAG, "delete group myself ok");
//							final String[] friendsId = QiXinBaseDao
//									.queryGroupRelations(group.groupID);
//							worker.postDML(new SQLiteWorker.AbstractSQLable()
//							{
//								@Override
//								public void onCompleted(Object event)
//								{
//									if(!(event instanceof Throwable))
//									{
//										sendToHandler(0);
//									}
//								}
//								@Override
//								public Object doAysncSQL()
//								{
//									ContactBusiness.DropGroup(group.groupID, friendsId);
//									return null;
//								}
//							});
//
//						}
//
//					}
//				});
//	}

	public Boolean ifIamLord() {
		if (myId.equals(lordId)) {
			return true;
		} else {
			return false;
		}
	}

	private Thread deleMemberThread;
	public void deleteMember(final FriendInfo deleteFriendInfo) {
		waitDialog.show();
		deleMemberThread = new Thread() {
			@Override
			public void run() {
				deleteFriendId = deleteFriendInfo.getFriendid();
				ArrayList<String> ids = new ArrayList<String>();
				ids.add(deleteFriendId);
				
				IQ iq = HJWebSocketManager.getInstance().doGroupOpt(group.groupID, ids, ChatConstants.iq.DATA_VALUE_REMOVE);
				String errorMsg = ChatPacketHelper.parseErrorCode(iq);
				if (errorMsg != null) {

				} else {
					sendToHandler(99);

				}
			}
		};
		deleMemberThread.start();
	}
	
	
	
//	public void deleteMember1(FriendInfo deleteFriendInfo) {
//		waitDialog.show();
//		deleteFriendId = deleteFriendInfo.getFriendid();
//		ArrayList<String> ids = new ArrayList<String>();
//		ids.add(deleteFriendId);
//		
//		
//		
//		
//		((ShowGroupChatMemberActivity) context).mService.doGroupOpt(
//				group.groupID, ids, ChatConstants.iq.DATA_VALUE_REMOVE,
//				new ResponseCallback() {
//
//					@Override
//					public void onReceive(DataPacket packet) {
//						IQ iq = (IQ) packet;
//						String errorMsg = ChatPacketHelper.parseErrorCode(iq);
//						Log.i(TAG, "delete member group respond is " + errorMsg);
//						if (errorMsg != null) {
//
//						} else {
//							Log.i(TAG, "delete group member ok");
//							sendToHandler(99);
//
//						}
//
//					}
//				});
//	}

	private void jumpToMain() {
		Intent intent = new Intent();
		intent.setClass(ShowGroupChatMemberActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/* 更改群名称 */
	private void showSetGroupNameDialog() {
		setGroupNameDialog = new Dialog(this, R.style.input_dialog);
		setGroupNameDialog.setContentView(R.layout.dialog_renamegroup);
		et_groupname = (EditText) setGroupNameDialog
				.findViewById(R.id.dialog_goup_rename_et);
		tv_setgroupname_confirm = (TextView) setGroupNameDialog
				.findViewById(R.id.dialog_group_confirm_tv);
		tv_setgroupname_cancel = (TextView) setGroupNameDialog
				.findViewById(R.id.dialog_group_cancel_tv);

		et_groupname.setText(group.groupName);
		tv_setgroupname_confirm.setOnClickListener(dialogOnClickListener);
		tv_setgroupname_cancel.setOnClickListener(dialogOnClickListener);

		setGroupNameDialog.show();
	}

	/* 提示用户群已解散 */
	private void showNoticeDialog(String msg) {
		noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
		noticeDialog.setContentView(R.layout.dialog_notice_nocancel);
		noticeDialog.setCancelable(false);
		noticeDialog.setCanceledOnTouchOutside(false);
		RelativeLayout dialog_confirm_rl;
		TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
		notice.setText(msg);
		dialog_confirm_rl = (RelativeLayout) noticeDialog
				.findViewById(R.id.dialog_nc_confirm_rl);
		dialog_confirm_rl.setOnClickListener(dialogOnClickListener);

		noticeDialog.show();
	}

	private OnClickListener dialogOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dialog_nc_confirm_rl:
				noticeDialog.dismiss();
				jumpToMain(ShowGroupChatMemberActivity.this);
				break;
			case R.id.dialog_group_confirm_tv:
				// TODO 修改群名称
				setGroupNameDialog.dismiss();
				groupNewName = et_groupname.getText().toString();
				if (StringUtil.isNullOrEmpty(groupNewName)) {
					ToastUtil
							.ShowLong(ShowGroupChatMemberActivity.this, "不能为空");
				} else {
					changeGroupName(group.groupID, groupNewName);
//					mService.modifyGroupName(group.groupID, groupNewName,
//							new ResponseCallback() {
//
//								@Override
//								public void onReceive(DataPacket packet) {
//									IQ iq = (IQ) packet;
//									if (ChatPacketHelper.parseErrorCode(iq) == null) {
//
//										group.groupName = groupNewName;
//										QiXinBaseDao.replaceGroupInfo(group);
//										groupName_tv.setText(group.groupName);
//									} else {
//										ToastUtil
//												.ShowLong(
//														ShowGroupChatMemberActivity.this,
//														"系统繁忙，请稍后重试");
//									}
//
//								}
//							});
				}
				break;

			case R.id.dialog_group_cancel_tv:
				// 取消修改群名称
				setGroupNameDialog.dismiss();
				break;
			default:
				break;
			}

		}
	};

	private Thread changeGroupNameThread;
	public void changeGroupName(final String groupID,final String newName) {
		changeGroupNameThread = new Thread() {
			@Override
			public void run() {
				IQ iq = HJWebSocketManager.getInstance().modifyGroupName(groupID, newName);
				if (ChatPacketHelper.parseErrorCode(iq) == null) {

					group.groupName = groupNewName;
					groupname = groupNewName;
					QiXinBaseDao.replaceGroupInfo(group); 
					sendToHandler(1000); 
				} else {
					Looper.prepare();
					ToastUtil
							.ShowLong(
									ShowGroupChatMemberActivity.this,
									"系统繁忙，请稍后重试");
					Looper.loop();
				}
			}
		};
		changeGroupNameThread.start();
	}
	
	
	private void sendToHandler(int counts) {
		Message Msg = new Message();
		Bundle b = new Bundle();
		b.putInt("flag", counts);
		Msg.setData(b);

		myHandler.sendMessage(Msg);
	}

	final Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			waitDialog.dismiss();
			Bundle b = msg.getData();
			int number = b.getInt("flag");
		 
			switch (number) {
			case 0:
				jumpToMain();
				break;
			case 99:
				Log.i(TAG, "删除成员成功");
				// TODO updata db and mFriendList
				worker.postDML(new SQLiteWorker.AbstractSQLable()
				{
					@Override
					public void onCompleted(Object event)
					{
						if(!(event instanceof Throwable))
						{
							getData();
						}
					}
					@Override
					public Object doAysncSQL()
					{
						QiXinBaseDao
						.deleteGroupRelations(group.groupID, deleteFriendId);// 删除群成员关系
						return null;
					}
				});
				break;
			case  1000: 
				groupName_tv.setText(groupname);
			default:
				break;
			}
		};
	};
}

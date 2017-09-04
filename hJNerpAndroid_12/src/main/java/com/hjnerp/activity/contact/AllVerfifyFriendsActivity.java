package com.hjnerp.activity.contact;


import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.activity.contact.adapter.AllVerfifyFriendsListAdapter;
import com.hjnerp.business.ContactBusiness;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.VerfifyFriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.net.Msg;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerpandroid.R;

public class AllVerfifyFriendsActivity extends ActivitySupport{
	private String TAG = "AllVerfifyFriendsActivity";
	private AllVerfifyFriendsListAdapter adapter;
	private ArrayList<VerfifyFriendInfo> friendsInfoList = new ArrayList<VerfifyFriendInfo>();
	private VerfifyFriendInfo vfriendInfo;
	private Dialog noticeDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.friendsverfify);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("联系人申请");
		ListView listview = (ListView) findViewById(R.id.listview);
		//friendsInfoList = sputil.getVerfifyFriends();
		//TODO 从db获取待新的好友信息
		friendsInfoList = QiXinBaseDao.queryTempContactInfos(null);
		if(friendsInfoList == null){
			// 	Log.e(TAG,"is null *********************>");
			friendsInfoList = new ArrayList<VerfifyFriendInfo>();
		}else{
			for(int i =0;i<friendsInfoList.size();i++){
				Log.i(TAG," ***** name is " + friendsInfoList.get(i).getFriendname());
			}
		}
		
		
		adapter = new AllVerfifyFriendsListAdapter(this,friendsInfoList);
			
		 //添加并且显示
		 listview.setAdapter(adapter);
		 
		 //长按弹出对话框删除item
		 listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				vfriendInfo = friendsInfoList.get(arg2);
				Log.i(TAG,"click position is " + arg2 + " temp friend id is " + vfriendInfo.getFriendid());
				if(!vfriendInfo.getVerfifyType().equals(ContactConstants.I_WANT_ADD_THIS_FRIEND)){
					showNoticeDialog(friendsInfoList.get(arg2).getFriendname(),"删除");
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		QiXinBaseDao.updateTempContactFlagRead();
		
	}
	
	private Thread mThread;
	public void addFriend(final VerfifyFriendInfo tempfriendinf) {
		mThread = new Thread() {
			@Override
			public void run() {
				IQ iq = HJWebSocketManager.getInstance().answerAddContact(tempfriendinf.getFriendid(), true);
				String errorMsg = ChatPacketHelper.parseErrorCode(iq);
				if(errorMsg != null)
				{
					
					String error_code = (String)((Map<String,Object>)iq.data).get(ChatConstants.iq.DATA_KEY_ERROR_CODE);
					//Log.e(TAG,"erroe code : " + error_code);
					//错误类型：重复发送了同意请求
					if(ChatConstants.error_code.ERROR_CODE_NO_PERMISSION.equals(error_code))
					{
						QiXinBaseDao.updateTempContactStatus(tempfriendinf.getFriendid(), ContactConstants.VERFIFY_PERMISSION);
						worker.postDML(new SQLiteWorker.AbstractSQLable()
						{
							@Override
							public void onCompleted(Object event)
							{
								if(!(event instanceof Throwable))
								{
									//刷新ui
									sendToHandler("refresh");
								}
							}
							@Override
							public Object doAysncSQL()
							{
								QiXinBaseDao.shiftTempContactInfoById(tempfriendinf.getFriendid());
								//写入一条聊天记录
								
								return null;
							}
						});
					}
 
					sendToHandler("add_user_error");
				}else{ 
					worker.postDML(new SQLiteWorker.AbstractSQLable()
					{
						@Override
						public void onCompleted(Object event)
						{
							if(!(event instanceof Throwable))
							{
								//刷新ui
								Log.i("info", "添加成功刷新ui");
								sendToHandler("refresh");
							}
						}
						@Override
						public Object doAysncSQL()
						{
							QiXinBaseDao.updateTempContactStatus(tempfriendinf.getFriendid(), ContactConstants.VERFIFY_PERMISSION);
							QiXinBaseDao.shiftTempContactInfoById(tempfriendinf.getFriendid());
						   //写入一条聊天记录
							Msg message = new Msg();
							// 写入一条聊天记录
							message.id = StringUtil.getMyUUID();
							message.from = tempfriendinf.getFriendid();
							message.to = SharePreferenceUtil
									.getInstance(
											EapApplication.getApplication()
													.getApplicationContext()).getMyId();
							message.time = String.valueOf(System.currentTimeMillis());
							message.body = "你已添" + tempfriendinf.getFriendname() +"为好友，现在可以聊天了。";
							message.type="chat";
							ContactBusiness.saveSingleChatMsgIndb(message, "iq");
							
							return null;
						}
					});
				}
			}
		};
		mThread.start();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			noticeDialog.dismiss();
			switch (v.getId()) {

			case R.id.dialog_simple__nc_confirm_rl:
				worker.postDML(new SQLiteWorker.AbstractSQLable()
				{
					@Override
					public void onCompleted(Object event)
					{
						if(!(event instanceof Throwable))
						{
							sendToHandler("refresh");
						}
					}
					@Override
					public Object doAysncSQL()
					{
						QiXinBaseDao.deleteTempContactInfoById(vfriendInfo.getFriendid());
						return null;
					}
				});
				break;
			
			default:
				break;

			}
		}
	};
	
	private void reFreshList(){
		adapter.refreshList(QiXinBaseDao.queryTempContactInfos(null));
	}
	private void sendToHandler(String msg) {

		Message Msg = new Message();
		Bundle b = new Bundle();
		b.putString("flag", msg);
		Msg.setData(b);

		myHandler.sendMessage(Msg);
	}
	final Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message Msg) {
			Bundle b = Msg.getData();
			String msg = b.getString("flag");
			if (msg.equals("refresh")) {
				
				//增加新联系人提示数量
//				addNewContactCount();
				//清除新联系人提醒
//				ActivitySupport.cleanNewContactCount();
				
				reFreshList();
			}else{
				ToastUtil.ShowShort(AllVerfifyFriendsActivity.this, "添加好友错误，请重试！");
			}
			
		};
	};

	/*提示用户是否删除*/
	private void showNoticeDialog(String title,String confirmtv) {
		noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
		noticeDialog.setContentView(R.layout.dialog_notice_simple);

		RelativeLayout dialog_confirm_rl;
		TextView notice = (TextView) noticeDialog.findViewById(R.id.dialog_simple_title);
		notice.setText(title);
		TextView confirm = (TextView) noticeDialog.findViewById(R.id.dialog_simple_confirm_tv);
		confirm.setText(confirmtv);
		dialog_confirm_rl = (RelativeLayout) noticeDialog.findViewById(R.id.dialog_simple__nc_confirm_rl);
		dialog_confirm_rl.setOnClickListener(onClickListener);		
		
		noticeDialog.show();
	}

	
}

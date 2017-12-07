package com.mznerp.activity.contact;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.activity.im.ChatActivity;
import com.mznerp.business.ContactBusiness;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.db.SQLiteWorker;
import com.mznerp.manager.HJWebSocketManager;
import com.mznerp.model.FriendInfo;
import com.mznerp.model.VerfifyFriendInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.net.IQ;
import com.mznerp.util.ImageLoaderHelper;
import com.mznerp.util.StringUtil;
import com.mznerp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;



public class AddingFriendActivity extends ActivitySupport {
	String TAG = "AddingFriendActivity";
	private String sFriendId;
	private FriendInfo friend;
	private Button sendBtn;
	private TextView firstnameEdit, nicknameEdit, orgunitEdit;
	private ImageView photo;
	private Dialog noticeDialog,setNoteDialog;
	private TextView tv_setnote_cancel, tv_setnote_confirm;
	private EditText et_note;
	private String extra_note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar = getSupportActionBar();
		friend = (FriendInfo) getIntent().getSerializableExtra(
				Constant.FRIEND_READ);
		Log.i(TAG, friend.getFriendid() + friend.getFriendname());
		setContentView(R.layout.adding_friend);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("详细资料");// 设置左上角标题
		init();
	}

	protected void init() {
		photo = (ImageView) findViewById(R.id.user_head_avatar);
		firstnameEdit = (TextView) findViewById(R.id.user_head_name);
		nicknameEdit = (TextView) findViewById(R.id.user_head_content);
		orgunitEdit = (TextView) findViewById(R.id.orgunit);
		sendBtn = (Button) findViewById(R.id.ui_addfriend_btn);
		sendBtn.setOnClickListener(listener);

		setFriendView();

		closeInput();

	}

	Button.OnClickListener listener = new Button.OnClickListener() {// 创建监听对象

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.dialog_group_confirm_tv:
				setNoteDialog.dismiss();
				sendBtn.setEnabled(false);
				extra_note = et_note.getText().toString();
				

					//TODO add friend 稍后需要加入手动输入验证信息
					//如果临时联系人表中有对方请求添加自己的信息，则直接发送同意添加，否则发送请求添加
					VerfifyFriendInfo vFriendInfo = null;
					ArrayList<VerfifyFriendInfo> tempFriendsInfo = QiXinBaseDao
							.queryTempContactInfos(friend.getFriendid());
				//	Log.e(TAG,"tempFriendsInfo size is " + tempFriendsInfo.size());

					if (tempFriendsInfo.size() > 0) {
						vFriendInfo = new VerfifyFriendInfo();
						vFriendInfo = tempFriendsInfo.get(0);
					}
					
					if(vFriendInfo != null && vFriendInfo.getVerfifyType() .equals(ContactConstants.THIS_FRIEND_WANT_ADD_ME)){//发送同意添加请求
						IQ iq =   HJWebSocketManager.getInstance().answerAddContact(friend.getFriendid(),true);
						  String errorMsg = ChatPacketHelper.parseErrorCode(iq);
								if(errorMsg != null)
								{ 
									showNoticeDialog(errorMsg);
								}else{
									// 			Log.e(TAG,"add friend succ");
									QiXinBaseDao.updateTempContactStatus(friend.getFriendid(), ContactConstants.VERFIFY_PERMISSION);
									worker.postDML(new SQLiteWorker.AbstractSQLable()
									{
										@Override
										public void onCompleted(Object event)
										{
											if(!(event instanceof Throwable))
											{
												showNoticeDialog("添加成功!");
											}
										}
										@Override
										public Object doAysncSQL()
										{
											QiXinBaseDao.shiftTempContactInfoById(friend.getFriendid());
											return null;
										}
									});
								} 
					}else{//发送添加请求
						
						IQ iq =   HJWebSocketManager.getInstance().addContact(friend.getFriendid(),extra_note);
					    String errorMsg = ChatPacketHelper.parseErrorCode(iq);
						if(errorMsg != null){ 
							String error_code = (String)((Map<String,Object>)iq.data).get(ChatConstants.iq.DATA_KEY_ERROR_CODE);
							//错误类型：已经是好友
							if(ChatConstants.error_code.ERROR_CODE_CONTACT_HAD_ADDED.equals(error_code))
							{

								final VerfifyFriendInfo mfriend = ContactBusiness.makeSaveDate(friend, ContactConstants.I_WANT_ADD_THIS_FRIEND, ContactConstants.VERFIFY_PERMISSION);
								worker.postDML(new SQLiteWorker.AbstractSQLable()
								{
									@Override
									public void onCompleted(Object event)
									{
										if(!(event instanceof Throwable))
										{
											sendToMyHandler("add_success");
										}
									}
									@Override
									public Object doAysncSQL()
									{
										QiXinBaseDao.replaceTempContactInfo(mfriend);
										QiXinBaseDao.replaceFriendInfo(friend, 'Y');
										return null;
									}
								});
								
							}
						}else{
							final VerfifyFriendInfo mfriend = ContactBusiness.makeSaveDate(friend, ContactConstants.I_WANT_ADD_THIS_FRIEND, ContactConstants.VERFIFY_ONGO);
							worker.postDML(new SQLiteWorker.AbstractSQLable()
							{
								@Override
								public void onCompleted(Object event)
								{
									if(!(event instanceof Throwable))
									{
										//((Activity) context).finish();
										showNoticeDialog("添加成功，等待验证");
									}
								}
								@Override
								public Object doAysncSQL()
								{
									QiXinBaseDao.replaceTempContactInfo(mfriend);
									return null;
								}
							});
							
						} 
					
				}
				break;
			case R.id.dialog_group_cancel_tv:
				setNoteDialog.dismiss();
				break;
			case R.id.ui_addfriend_btn:
				showsetNoteDialog();
				break;
			case R.id.dialog_nc_confirm_rl:
				noticeDialog.dismiss();
				((Activity) context).finish();
				break;

			default:
				break;
			}

		}

	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
		}
		return false;
	}

	private void setFriendView() {
		String url = friend.getFriendimage();
		if (!StringUtil.isNullOrEmpty(url)) {
			ImageLoaderHelper.displayImage(ChatPacketHelper.buildImageRequestURL(url, ChatConstants.iq.DATA_VALUE_RES_TYPE_IM), photo);
		}

		firstnameEdit.setText(friend.getFriendname());
		nicknameEdit.setText("企信ID:" + friend.getFriendid());
		orgunitEdit.setText(friend.getDeptname());


	}
	
	private void showNoticeDialog(String msg) {
		noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
		noticeDialog.setContentView(R.layout.dialog_notice_nocancel);
		noticeDialog.setCancelable(true);
		noticeDialog.setCanceledOnTouchOutside(true);
		RelativeLayout dialog_confirm_rl;
		TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
		notice.setText(msg);
		dialog_confirm_rl = (RelativeLayout) noticeDialog.findViewById(R.id.dialog_nc_confirm_rl);
		dialog_confirm_rl.setOnClickListener(listener);		
		
		noticeDialog.show();
	}
	
	/*添加验证信息*/
	private void showsetNoteDialog() {
		setNoteDialog = new Dialog(this, R.style.input_dialog);
		setNoteDialog.setContentView(R.layout.dialog_renamegroup);
		TextView title = (TextView) setNoteDialog.findViewById(R.id.et_groupname_title);
		title.setText("验证信息");
		
		et_note = (EditText) setNoteDialog
				.findViewById(R.id.dialog_goup_rename_et);
		tv_setnote_confirm = (TextView) setNoteDialog
				.findViewById(R.id.dialog_group_confirm_tv);
		tv_setnote_cancel = (TextView) setNoteDialog
				.findViewById(R.id.dialog_group_cancel_tv);
		String text = "我是";
		et_note.setText(text);
		et_note.setSelection(text.length());
		tv_setnote_confirm.setOnClickListener(listener);
		tv_setnote_cancel.setOnClickListener(listener);

		setNoteDialog.show();
	}
	private void sendToMyHandler(String msg) {
		Message Msg = new Message();
		Bundle b = new Bundle();
		b.putString("flag", msg);
		Msg.setData(b);

		myHandler.sendMessage(Msg);
	}

	final Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle b = msg.getData();
			String mmsg = b.getString("flag");
			Log.i(TAG, "mmsg is " + mmsg);
			if (mmsg.equals("add_success")) {
				//jumpToMain(AddingFriendActivity.this);
				Intent intent = new Intent(context, ChatActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Constant.IM_NEWS,
						(Serializable) friend);
				intent.putExtras(mBundle);
				startActivity(intent);
				((Activity) context).finish();
				
			} 

		};
	};

}

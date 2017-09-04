package com.hjnerp.activity.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.activity.im.ChatActivity;
import com.hjnerp.business.ContactBusiness;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.VerfifyFriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerpandroid.R;

public class SearchQiXinFriendsretActivity extends ActivitySupport {
	private String TAG = "SearchQiXinFriendsretActivity";
	private ArrayList<FriendInfo> listFriendInfo;
	private Dialog noticeDialog, setNoteDialog;
	private TextView tv_setnote_cancel, tv_setnote_confirm;
	private EditText et_note;
	private String extra_note;
	private FriendInfo wantAddFriendInfo;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listFriendInfo = (ArrayList<FriendInfo>) getIntent()
				.getSerializableExtra(Constant.FRIEND_READ);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setTitle("搜索联系人");

		setContentView(R.layout.addfriendsret);
		ListView listview = (ListView) findViewById(R.id.addfriends_list);
//		// /去掉自己
//		FriendInfo findFrien = null;
//		for (FriendInfo find : listFriendInfo) {
//			if (find != null
//					&& find.getFriendid().equalsIgnoreCase(sputil.getMyId())) {
//				findFrien = find;
//			}
//
//		}
//		if (findFrien != null) {
//			listFriendInfo.remove(findFrien);
//
//		}
		 
		addFriendsListAdapter adapter = new addFriendsListAdapter(
				listFriendInfo);
		listview.setAdapter(adapter);
	}

	Button.OnClickListener listener = new Button.OnClickListener() {// 创建监听对象

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.dialog_group_confirm_tv:
				setNoteDialog.dismiss();
				extra_note = et_note.getText().toString();

				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {

						// TODO add friend 稍后需要加入手动输入验证信息
						// 如果临时联系人表中有对方请求添加自己的信息，则直接发送同意添加，否则发送请求添加
						VerfifyFriendInfo vFriendInfo = null;
						ArrayList<VerfifyFriendInfo> tempFriendsInfo = QiXinBaseDao
								.queryTempContactInfos(wantAddFriendInfo
										.getFriendid());
						// Log.e(TAG,"tempFriendsInfo size is " +
						// tempFriendsInfo.size());

						if (tempFriendsInfo.size() > 0) {
							vFriendInfo = new VerfifyFriendInfo();
							vFriendInfo = tempFriendsInfo.get(0);
						}

						if (vFriendInfo != null
								&& vFriendInfo
										.getVerfifyType()
										.equals(ContactConstants.THIS_FRIEND_WANT_ADD_ME)) {// 发送同意添加请求
							IQ iq = HJWebSocketManager.getInstance()
									.answerAddContact(
											wantAddFriendInfo.getFriendid(),
											true);
							String errorMsg = ChatPacketHelper
									.parseErrorCode(iq);
							if (errorMsg != null) {
								// Log.e(TAG,errorMsg);
								showNoticeDialog("服务器繁忙，请重试!");
							} else {
								// Log.e(TAG,"add friend succ");
								QiXinBaseDao.updateTempContactStatus(
										wantAddFriendInfo.getFriendid(),
										ContactConstants.VERFIFY_PERMISSION);
								worker.postDML(new SQLiteWorker.AbstractSQLable() {
									@Override
									public void onCompleted(Object event) {
										if (!(event instanceof Throwable)) {
											showNoticeDialog("添加成功!");
										}
									}

									@Override
									public Object doAysncSQL() {
										QiXinBaseDao
												.shiftTempContactInfoById(wantAddFriendInfo
														.getFriendid());
										return null;
									}
								});
							}
						} else {// 发送添加请求

							IQ iq = HJWebSocketManager.getInstance()
									.addContact(
											wantAddFriendInfo.getFriendid(),
											extra_note);
							String errorMsg = ChatPacketHelper
									.parseErrorCode(iq);
							if (errorMsg != null) {
								String error_code = (String) ((Map<String, Object>) iq.data)
										.get(ChatConstants.iq.DATA_KEY_ERROR_CODE);
								// 错误类型：已经是好友
								if (ChatConstants.error_code.ERROR_CODE_CONTACT_HAD_ADDED
										.equals(error_code)) {

									final VerfifyFriendInfo mfriend = ContactBusiness
											.makeSaveDate(
													wantAddFriendInfo,
													ContactConstants.I_WANT_ADD_THIS_FRIEND,
													ContactConstants.VERFIFY_PERMISSION);
									worker.postDML(new SQLiteWorker.AbstractSQLable() {
										@Override
										public void onCompleted(Object event) {
											if (!(event instanceof Throwable)) {
												Intent intent = new Intent(
														context,
														ChatActivity.class);
												Bundle mBundle = new Bundle();
												mBundle.putSerializable(
														Constant.IM_NEWS,
														(Serializable) wantAddFriendInfo);
												intent.putExtras(mBundle);
												startActivity(intent);
												((Activity) context).finish();
											}
										}

										@Override
										public Object doAysncSQL() {
											QiXinBaseDao
													.replaceTempContactInfo(mfriend);
											QiXinBaseDao.replaceFriendInfo(
													wantAddFriendInfo, 'Y');
											return null;
										}
									});

								}
							} else {
								final VerfifyFriendInfo mfriend = ContactBusiness
										.makeSaveDate(
												wantAddFriendInfo,
												ContactConstants.I_WANT_ADD_THIS_FRIEND,
												ContactConstants.VERFIFY_ONGO);
								worker.postDML(new SQLiteWorker.AbstractSQLable() {
									@Override
									public void onCompleted(Object event) {
										if (!(event instanceof Throwable)) {
											// ((Activity) context).finish();
											showNoticeDialog("添加成功，等待验证");
										}
									}

									@Override
									public Object doAysncSQL() {
										QiXinBaseDao
												.replaceTempContactInfoN(mfriend);
										return null;
									}
								});

							}

						}

					}
				});
				thread.start();

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

	public class addFriendsListAdapter extends BaseAdapter {

		private ArrayList<FriendInfo> list = new ArrayList<FriendInfo>();

		public addFriendsListAdapter(ArrayList<FriendInfo> list) {
			this.list = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			FriendInfo friendInfo = list.get(position);
			addh h = null;
			if (view == null) {
				h = new addh();
				view = LayoutInflater.from(context).inflate(
						R.layout.addfriendsret_item, parent, false);
				h.pic = (ImageView) view.findViewById(R.id.addfriendphoto);
				h.title = (TextView) view.findViewById(R.id.addfriend_title);
				h.dept = (TextView) view.findViewById(R.id.addfriend_dept);
				h.add = (Button) view.findViewById(R.id.add_friend_btn);
				view.setTag(h);
			} else {
				h = (addh) view.getTag();
			}

			h.add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button btn = (Button) v;
					FriendInfo mFriendInfo = (FriendInfo) btn.getTag();
					wantAddFriendInfo = mFriendInfo;
					showsetNoteDialog();
				}
			});

			h.add.setTag(friendInfo);
			h.title.setText(friendInfo.getFriendname());
			h.dept.setText(friendInfo.getDeptname());

			String url = ChatPacketHelper.buildImageRequestURL(
					friendInfo.getFriendimage(),
					ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
			ImageLoaderHelper.displayImage(url, h.pic);
			return view;
		}

		class addh {
			ImageView pic;
			TextView title;
			TextView dept;
			Button add;
		}

	}

	/* 添加验证信息 */
	private void showsetNoteDialog() {
		setNoteDialog = new Dialog(this, R.style.input_dialog);
		setNoteDialog.setContentView(R.layout.dialog_renamegroup);
		TextView title = (TextView) setNoteDialog
				.findViewById(R.id.et_groupname_title);
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

	private void showNoticeDialog(String msg) {
		noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
		noticeDialog.setContentView(R.layout.dialog_notice_nocancel);
		noticeDialog.setCancelable(true);
		noticeDialog.setCanceledOnTouchOutside(true);
		RelativeLayout dialog_confirm_rl;
		TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
		notice.setText(msg);
		dialog_confirm_rl = (RelativeLayout) noticeDialog
				.findViewById(R.id.dialog_nc_confirm_rl);
		dialog_confirm_rl.setOnClickListener(listener);

		noticeDialog.show();
	}
}

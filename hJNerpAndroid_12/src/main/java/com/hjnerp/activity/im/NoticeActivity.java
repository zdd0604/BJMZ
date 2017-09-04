package com.hjnerp.activity.im;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hjnerp.activity.MainActivity;
import com.hjnerp.activity.im.adapter.NoticeListAdapter;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.model.GroupInfo;
import com.hjnerpandroid.R;

public class NoticeActivity extends ActivitySupport {
	private String TAG = "NoticeActivity";
	private PullToRefreshListView listview;
	private int pageSize = 10;
	private int pageNum = 1;
	private GroupInfo mGroup;
	private NoticeListAdapter noticelistadapter;
	private ArrayList<ChatHisBean> message_pool = null;
	private final int NET_GET_HISTORY_CHAT_MSG_OK = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGroup = (GroupInfo) getIntent().getSerializableExtra(
				Constant.IM_GOUP_NEWS);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 设置左上角的home按钮是否有效
		getSupportActionBar().setTitle(mGroup.groupName);// 设置左上角标题
		setSupportProgressBarIndeterminateVisibility(true); // 加载进度隐藏

		setContentView(R.layout.notice);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 设置左上角的home按钮是否有效

		listview = (PullToRefreshListView) findViewById(R.id.listview_notice);
		noticelistadapter = new NoticeListAdapter(this, getNoticeList(),
				listview);
		listview.setAdapter(noticelistadapter);

		// //上下拉刷新
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new Thread("Listview-Refresh-ChatActivity") {
					public void run() {
						try {
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						getRefreshMessage();
						listview.post(new Runnable() {
							@Override
							public void run() {
								// pullRrefresMessage(message_pool_show);
								listview.onRefreshComplete();
							}
						});
					}
				}.start();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Log.i(TAG, "上拉刷新");

			}

		});
	}

	@Override
	protected void onPause() {

		QiXinBaseDao.updateGroupChatMsgFlagRead(mGroup.groupID);// 在企信表中把聊天消息标记为已读
		super.onPause();
	}

	// 下拉刷新获取应显示的信息
	private void getRefreshMessage() {
		int mes_pool_size = message_pool.size();
		//
		List<ChatHisBean> message_pooltemp = QiXinBaseDao
				.queryChatHisBeansByGroupChat(mGroup.groupID, mes_pool_size);
		if (message_pooltemp != null) {
			message_pool.addAll(0, message_pooltemp);
		}
	}

	private void sendToHandler(int msg) {
		Message Msg = new Message();
		Bundle b = new Bundle();
		b.putInt("flag", msg);
		Msg.setData(b);

		myHandler.sendMessage(Msg);
	}

	final Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Bundle b = msg.getData();
			int mmsg = b.getInt("flag");
			switch (mmsg) {

			case NET_GET_HISTORY_CHAT_MSG_OK:

				pullRrefresMessage(message_pool);
				break;
			default:
				break;
			}

		};
	};

	protected void pullRrefresMessage(ArrayList<ChatHisBean> messages) {
		noticelistadapter.pullRrefreshList(messages);
	}

	// /从本地取得聊天消息列表
	private ArrayList<ChatHisBean> getNoticeList() {
		message_pool = QiXinBaseDao.queryChatHisBeansByGroupChat(
				mGroup.groupID, 0);

		return message_pool;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent();
			intent.setClass(NoticeActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();

		}
		return false;
	}
}

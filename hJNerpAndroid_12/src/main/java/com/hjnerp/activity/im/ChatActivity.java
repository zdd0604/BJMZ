package com.hjnerp.activity.im;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hjnerp.activity.MainActivity;
import com.hjnerp.activity.contact.FriendsActivity;
import com.hjnerp.activity.im.adapter.ChatListTypeAdapter;
import com.hjnerp.activity.im.adapter.ExtendGridAdapter;
import com.hjnerp.activity.im.adapter.ExtendGridAdapter.ExtendGridDataItem;
import com.hjnerp.activity.im.adapter.RecordPlayManager;
import com.hjnerp.business.ContactBusiness;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.net.Msg;
import com.hjnerp.service.WebSocketService;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.Log;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.audio.BmobRecordManager;
import com.hjnerp.util.audio.OnRecordChangeListener;
import com.hjnerp.util.imageCompressUtil;
import com.hjnerp.widget.ResizeRelativeLayout;
import com.hjnerpandroid.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Actity 聊天类 单人和群聊公用此类（通过上级跳转页面携带的对象来判断是单聊还是群聊）
 * 通过广播接收消息（群解散、群成员添加、群成员减少），做出相应反应
 * Activity首次创立是在onWebSocketServiceReady（）方法中加载数据，其余时刻在onResume中刷新数据
 * 
 */

public class ChatActivity extends ActivitySupport {
	public String TAG = "ChatActivity";
	public static String myid;
	private List<ChatHisBean> message_pool = new ArrayList<ChatHisBean>();
	private List<FriendInfo> mFriendList;
	private PullToRefreshListView listview;
	private FriendInfo friend = null;
	private GroupInfo group = null;
	private ChatListTypeAdapter chatListTypeAdapter;
	private EditText messageInput;
	private GridView appGrid;
	private ExtendGridAdapter appGridAdapter;
	private ImageButton chatModeBtn;
	private ImageButton attachBtn;
	private Button voiceRecordBtn;
	private Button sendBtn;
	private String latitude,lonlongtitude,address;

	private Menu mMenu;
	public String strImage;
	private File out;
	private Uri uri;
	BmobRecordManager recordManager;
	private FriendInfo tempFriendInfo;
	/* 群解散或自己被群主移除或user解除了和自己的好友关系的标志 */
	public Boolean ifGroupOrFriendExit = true;

	private long time;
	private ContacterReceiver contacterReceiver;
	private IntentFilter contacterFilter;
	private boolean btn_vocie = false;
	private View rcChat_popup;
	private LinearLayout voice_rcd_hint_rcding, voice_rcd_hint_tooshort;
	private int flag = 1;
	private Handler mHandler = new Handler();
	private ImageView iv_record;
	private boolean isShosrt = false;
	private String voiceName;

	private TextView voiceNotice;

	private Drawable[] drawable_Anims;// 话筒动画

	public static Context chat_contest;
	private Dialog noticeDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			mActionBar = getSupportActionBar();
			chat_contest = this;
			myid = sputil.getMyId();
			friend = (FriendInfo) getIntent()
					.getSerializableExtra(Constant.IM_NEWS);
			group = (GroupInfo) getIntent().getSerializableExtra(
					Constant.IM_GOUP_NEWS);
			if (friend != null) {
				initIfFriendChat();
			} else if (group != null) {
				initIfGroupChat();
			}

			contacterReceiver = new ContacterReceiver();
			contacterFilter = new IntentFilter();
			contacterFilter.addAction(WebSocketService.ACTION_ON_MSG);
			contacterFilter.addAction(WebSocketService.ACTION_ON_IQ);
			initRecordManager();
			initVoiceAnimRes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			android.util.Log.i("info", "chat oncreate异常："+e.toString());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		friend = (FriendInfo) getIntent()
				.getSerializableExtra(Constant.IM_NEWS);
		group = (GroupInfo) getIntent().getSerializableExtra(
				Constant.IM_GOUP_NEWS);

		if (friend != null) {
			initIfFriendChat();
		} else if (group != null) {
			initIfGroupChat();
		}

		contacterReceiver = new ContacterReceiver();
		contacterFilter = new IntentFilter();
		contacterFilter.addAction(WebSocketService.ACTION_ON_MSG);
		onCreateOptionsMenu(mMenu);
		initVoiceAnimRes();

	}

	private void initVoiceAnimRes() {
		drawable_Anims = new Drawable[] {
				getResources().getDrawable(R.drawable.amp1),
				getResources().getDrawable(R.drawable.amp2),
				getResources().getDrawable(R.drawable.amp3),
				getResources().getDrawable(R.drawable.amp4),
				getResources().getDrawable(R.drawable.amp5),
				getResources().getDrawable(R.drawable.amp6),
				getResources().getDrawable(R.drawable.amp7) };
	}

	private void getSingleChatFriendData() {
		mFriendList = new ArrayList<FriendInfo>();
		mFriendList.add(friend);
		FriendInfo myFriendInfo = QiXinBaseDao
				.queryFriendInfo(sputil.getMyId());
		mFriendList.add(myFriendInfo);
	}

	private void getGroupUsersData() {
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

	}

	/* 当联系人发生变化时，检查是否还可以继续聊天 */
	@Override
	public void removeMeAsFriend(String friendId) {
		super.removeMeAsFriend(friendId);
		if (friend != null && friendId.equals(friend.getFriendid())) {
			// ifGroupOrFriendExit = ContactBusiness.checkFriends(friend
			// .getFriendid());
			ifGroupOrFriendExit = false;
			Log.e(TAG, "ifGroupOrFriendExit is " + ifGroupOrFriendExit);
			if (!ifGroupOrFriendExit) {
				ChatHisBean cBean = new ChatHisBean();
				cBean.setTitle("对方解除了好友关系");
				message_pool.add(cBean);
				refreshMessage(message_pool);
			}
		}

	}

	// 群解散
	@Override
	public void dropGroupChat(String groupId) {
		if (group != null) {
			if (groupId.equals(group.groupID)) {
				ifGroupOrFriendExit = false;
				ChatHisBean cBean = new ChatHisBean();
				cBean.setTitle("群已被解散");
				message_pool.add(cBean);
				refreshMessage(message_pool);
			}
		}
	}

	@Override
	public void refalshContent(String msg) {
		if (friend != null) {

			List<ChatHisBean> message_pool_notread = QiXinBaseDao
					.queryChatHisBeansBySingleChatNoRead(sputil.getMyId(),
							friend.getFriendid());
			message_pool.addAll(message_pool_notread);
			refreshMessage(message_pool);
		} else if (group != null) {

			if (!ContactBusiness.checkGroup(group.groupID)) {
				finish();
				return;
			}
			List<ChatHisBean> message_pool_notread = QiXinBaseDao
					.queryChatHisBeansByGroupChatNoRead(group.groupID);
			message_pool.addAll(message_pool_notread);
			refreshMessage(message_pool);
			QiXinBaseDao.updateGroupChatMsgFlagRead(group.groupID);// 在企信表中把聊天消息标记为已读
		}
	}

	private void initIfFriendChat() {
		mActionBar.setTitle(friend.getFriendname());// 设置左上角标题
		setCurrentChatFriendOrGroupId(friend.getFriendid());
		QiXinBaseDao.updateSingleChatSendFlag(sputil.getMyId(),
				friend.getFriendid());
		List<ChatHisBean> message_pool_temp = QiXinBaseDao
				.queryChatHisBeansBySingleChat(sputil.getMyId(),
						friend.getFriendid(), 0);
		if (message_pool_temp != null && message_pool_temp.size() > 0) {
			message_pool = message_pool_temp;
		}

	}

	private void initIfGroupChat() {
		mActionBar.setTitle(group.groupName);// 设置左上角标题
		setCurrentChatFriendOrGroupId(group.groupID);
		QiXinBaseDao.updateGroupChatSendFlag(group.groupID);
		List<ChatHisBean> message_pool_temp = QiXinBaseDao
				.queryChatHisBeansByGroupChat(group.groupID, 0);
		if (message_pool_temp != null && message_pool_temp.size() > 0) {
			message_pool = message_pool_temp;
		}
	}

	private void init() {
		mActionBar.setDisplayHomeAsUpEnabled(true);// 设置左上角的home按钮是否有效
		setSupportProgressBarIndeterminateVisibility(true); // 加载进度隐藏
		setContentView(R.layout.chat);

		voiceRecordBtn = (Button) findViewById(R.id.chatting_voice_record_btn);
		rcChat_popup = this.findViewById(R.id.rcChat_popup);
		voice_rcd_hint_rcding = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_tooshort = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_tooshort);

		iv_record = (ImageView) this.findViewById(R.id.volume);

		chatModeBtn = (ImageButton) findViewById(R.id.chatting_mode_btn);
		attachBtn = (ImageButton) findViewById(R.id.chatting_attach_btn);
		sendBtn = (Button) findViewById(R.id.chatting_send_btn);
		sendBtn.setOnClickListener(listener);
		messageInput = (EditText) findViewById(R.id.chatting_sendmessage_et);
		listview = (PullToRefreshListView) findViewById(R.id.chatting_list_view);
		appGrid = (GridView) findViewById(R.id.chatting_attach_extend);
		appGridAdapter = new ExtendGridAdapter(this);
		appGridAdapter.addItem(getImageTransferExtend());
		appGridAdapter.addItem(getCameraExtend());
		/**
		 * @author haijian 增加位置、文件的item
		 */
		appGridAdapter.addItem(getLocationExtend());
//		appGridAdapter.addItem(getFileExtend());
		appGrid.setAdapter(appGridAdapter);
		voiceNotice = (TextView) findViewById(R.id.voice_rcd_hint_notice);

		chatListTypeAdapter = new ChatListTypeAdapter(this, message_pool);
		listview.setAdapter(chatListTypeAdapter);

		// //发
		voiceRecordBtn.setOnTouchListener(new OnTouchListener() {
			// TODO
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				// 按下语音录制按钮时返回false执行父类OnTouch
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(ChatActivity.this, "No SDCard",
							Toast.LENGTH_LONG).show();
					return false;
				}
				if (btn_vocie) {
					int[] location = new int[2];
					voiceRecordBtn.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
					int btn_rc_Y = location[1];
					int btn_rc_X = location[0];

					if (event.getAction() == MotionEvent.ACTION_DOWN
							&& flag == 1) {
						if (!Environment.getExternalStorageDirectory().exists()) {
							Toast.makeText(ChatActivity.this, "No SDCard",
									Toast.LENGTH_LONG).show();
							return false;
						}

						if (event.getRawY() > btn_rc_Y
								&& event.getRawX() > btn_rc_X) {// 判断手势按下的位置是否是语音录制按钮的范围内
							// 触摸到录音按钮，开始录音
							voiceRecordBtn
									.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
							rcChat_popup.setVisibility(View.VISIBLE);
							voice_rcd_hint_rcding.setVisibility(View.GONE);
							voice_rcd_hint_tooshort.setVisibility(View.GONE);
							mHandler.postDelayed(new Runnable() {
								public void run() {
									if (!isShosrt) {
										voice_rcd_hint_rcding
												.setVisibility(View.VISIBLE);
									}
								}
							}, 300);

							voiceName = makeAudioFileName();
							time = System.currentTimeMillis();
							try {
								recordManager.startRecording(voiceName);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Log.i("info", "录音异常：" + e.toString());
							}
							flag = 2;
						}
					} else if (event.getAction() == MotionEvent.ACTION_UP
							&& flag == 2) {// 松开手势时执行录制完成

						voiceRecordBtn
								.setBackgroundResource(R.drawable.voice_rcd_btn_nor);

						voice_rcd_hint_rcding.setVisibility(View.GONE);
						flag = 1;
						/**
						 * @author haijian 将取消录音放在前面
						 */
						int timelong = recordManager.stopRecording();
						if (event.getRawY() > btn_rc_Y) {
							if (timelong < 1) {// 录音时间太短

								isShosrt = true;
								voice_rcd_hint_rcding.setVisibility(View.GONE);
								voice_rcd_hint_tooshort
										.setVisibility(View.VISIBLE);
								mHandler.postDelayed(new Runnable() {
									public void run() {
										voice_rcd_hint_tooshort
												.setVisibility(View.GONE);
										rcChat_popup.setVisibility(View.GONE);
										isShosrt = false;
									}
								}, 500);
								/**
								 * @author haijian 解决无网络时发送音频文件闪退问题
								 *         问题原因：处理完ontouch的手势抬起事件后应该返回true
								 *         ，不然会继续传递点击事件
								 */
								return true;
							} else {

								sendVoiceMessage(timelong);
							}
						}

						rcChat_popup.setVisibility(View.GONE);
						/**
						 * @author haijian 解决无网络时发送音频文件闪退问题
						 *         问题原因：处理完ontouch的手势抬起事件后应该返回true，不然会继续传递点击事件
						 */
						return true;

					}
					if (event.getRawY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内

						voiceNotice.setText("松手取消");

					} else {
						voiceNotice.setText("松手发送");
					}
				}
				return false;
			}
		});

		ResizeRelativeLayout rrl = (ResizeRelativeLayout) findViewById(R.id.chatting_room_rt_relative_layout);
		rrl.setOnHeightSmallerListener(new ResizeRelativeLayout.OnHeightSmallerListener() {
			@Override
			public void onHeightSmaller(ResizeRelativeLayout layout, int oldh,
					int h) {
				if (oldh < h) { // 可能是输入法缩回
				} else { // 可能是输入法勃起
					listview.getRefreshableView().postDelayed(new Runnable() {
						public void run() {
							listview.getRefreshableView()
									.setSelection(
											(listview.getRefreshableView())
													.getBottom());
						}
					}, 100);
				}
			}
		});
		messageInput.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				if (s.toString().isEmpty()) {
					attachBtn.setVisibility(View.VISIBLE);
					sendBtn.setVisibility(View.GONE);
				} else {
					sendBtn.setVisibility(View.VISIBLE);
					attachBtn.setVisibility(View.GONE);
				}
			}
		});
		messageInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				animGone(appGrid, null, null);
			}
		});
		messageInput.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				animGone(appGrid, null, null);
			}
		});
		chatModeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chatModeBtn
						.setImageResource(R.drawable.chatting_setmode_keyboard_btn);
				animGone(appGrid, null, null);
				if (messageInput.isShown()) {
					chatModeBtn
							.setImageResource(R.drawable.chatting_setmode_keyboard_btn);
					closeInputMethodOnce(ChatActivity.this);
					btn_vocie = true;
					Log.e(TAG, "btn_voice is true");
					animSwitch2(messageInput, voiceRecordBtn, null);
				} else {
					chatModeBtn
							.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_vocie = false;
					animSwitch2(voiceRecordBtn, messageInput, new Runnable() {
						@Override
						public void run() {
							openInputMethodOnce(messageInput);
						}
					});
				}
			}
		});
		attachBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (appGrid.isShown()) {
					animGone(appGrid, new Runnable() {
						public void run() {
							openInputMethodOnce(messageInput);
						}
					}, null);
				} else {
					try {
						if (!messageInput.isShown()) {
							chatModeBtn
									.setImageResource(R.drawable.chatting_setmode_voice_btn);
							animSwitch2(voiceRecordBtn, messageInput, null);
						}
						animShow(appGrid, new Runnable() {
							public void run() {
								closeInputMethodOnce(ChatActivity.this);
							}
						}, null);
						listview.post(new Runnable() {
							@Override
							public void run() {
								// listview.setSelection(listview.getAdapter()
								// .getCount() - 1);
								listview.getRefreshableView().setSelection(
										(listview.getRefreshableView())
												.getAdapter().getCount() - 1);
								android.util.Log.i("info", "select:"+((listview.getRefreshableView())
										.getAdapter().getCount() - 1));
							}
						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						android.util.Log.i("info", "刷新异常："+e.toString());;
					}
				}
			}
		});

		chatListTypeAdapter = new ChatListTypeAdapter(this, message_pool);

		listview.setAdapter(chatListTypeAdapter);

		// //TODO 上下拉刷新
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
								// refreshMessage(message_pool_show);
								pullRrefresMessage(message_pool);
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

		listview.post(new Runnable() {
			@Override
			public void run() {
				// listview.setSelection(listview.getAdapter().getCount() - 1);
				listview.getRefreshableView()
						.setSelection(
								(listview.getRefreshableView()).getAdapter()
										.getCount() - 1);
			}
		});

		closeInput();

		if (group != null && group.groupType.equals("sys")) {
			((LinearLayout) findViewById(R.id.chatting_ll_bottom))
					.setVisibility(View.GONE);
		}

		// /添加重发按钮事件
		chatListTypeAdapter.setOnInViewClickListener(
				R.id.chat_right_item_pic_sendfail_rightaudio,
				new ChatListTypeAdapter.onInternalClickListener() {

					@Override
					public void OnClickListener(View parentV, View v,
							Integer position, Object values) {
						// 重发消息
						showResendDialog(parentV, v, values);
					}
				});

		// /添加重发按钮事件
		chatListTypeAdapter.setOnInViewClickListener(
				R.id.chat_right_item_pic_sendfail_rightpic,
				new ChatListTypeAdapter.onInternalClickListener() {

					@Override
					public void OnClickListener(View parentV, View v,
							Integer position, Object values) {
						// 重发消息
						showResendDialog(parentV, v, values);
					}
				});

		// /添加重发按钮事件
		chatListTypeAdapter.setOnInViewClickListener(
				R.id.chat_right_item_text_sendfail_righttext,
				new ChatListTypeAdapter.onInternalClickListener() {

					@Override
					public void OnClickListener(View parentV, View v,
							Integer position, Object values) {
						// 重发消息
						showResendDialog(parentV, v, values);
					}
				});

		// /左边播放声音
		chatListTypeAdapter.setOnInViewClickListener(R.id.layout_left_voice,
				new ChatListTypeAdapter.onInternalClickListener() {

					@Override
					public void OnClickListener(View parentV, View v,
							Integer position, Object values) {
						RecordPlayManager
								.getInstance(context)
								.recordPaly(
										(ChatHisBean) values,
										(ImageView) parentV
												.findViewById(R.id.chat_left_item_audio_leftaudio),
										(ImageView) parentV
												.findViewById(R.id.chat_left_item_pic_sendfail_leftaudio));
					}
				});

		// 右边播放声音
		chatListTypeAdapter.setOnInViewClickListener(R.id.layout_right_voice,
				new ChatListTypeAdapter.onInternalClickListener() {

					@Override
					public void OnClickListener(View parentV, View v,
							Integer position, Object values) {
						RecordPlayManager
								.getInstance(context)
								.recordPaly(
										(ChatHisBean) values,
										(ImageView) parentV
												.findViewById(R.id.chat_right_item_pic_rightaudio),
										null);
					}
				});

	}

	public void animSwitch2(final View gone, final View visible,
			final Runnable run) {
		AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.0f);
		aa1.setDuration(800);
		gone.startAnimation(aa1);
		gone.setVisibility(View.GONE);

		TranslateAnimation ta = new TranslateAnimation(-200, 0, 0, 0);
		ta.setDuration(900);
		ta.setInterpolator(new DecelerateInterpolator());
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(800);
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ta);
		as.addAnimation(aa);
		as.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (run != null)
					run.run();
			}
		});
		visible.setAnimation(as);
		visible.setVisibility(View.VISIBLE);
	}

	public void animSwitch(final View gone, final View visible,
			final Runnable run) {
		AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.0f);
		aa1.setDuration(400);
		gone.setAnimation(aa1);
		gone.setVisibility(View.GONE);
		AlphaAnimation aa2 = new AlphaAnimation(0.0f, 1.0f);
		aa2.setDuration(400);
		visible.setAnimation(aa2);
		visible.setVisibility(View.VISIBLE);
	}

	public void animGone(final View view, final Runnable start,
			final Runnable end) {
		AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
		aa.setDuration(500);
		aa.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
				if (start != null)
					start.run();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.GONE);
				if (end != null)
					end.run();
			}
		});
		view.startAnimation(aa);
	}

	public void animShow(final View view, final Runnable start,
			final Runnable end) {
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(500);
		aa.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);
				if (start != null)
					start.run();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (end != null)
					end.run();
			}
		});
		view.startAnimation(aa);
	}

	// 下拉刷新获取应显示的信息
	private void getRefreshMessage() {

		int mes_pool_size = message_pool.size();

		if (group != null) {
			List<ChatHisBean> message_pooltemp = QiXinBaseDao
					.queryChatHisBeansByGroupChat(group.groupID, mes_pool_size);
			if (message_pooltemp != null) {
				message_pool.addAll(0, message_pooltemp);
			}
		} else if (friend != null) {

			List<ChatHisBean> message_pooltemp = QiXinBaseDao
					.queryChatHisBeansBySingleChat(sputil.getMyId(),
							friend.getFriendid(), mes_pool_size);
			if (message_pooltemp != null) {
				message_pool.addAll(0, message_pooltemp);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		mMenu = menu;
		if (friend != null) {
			getMenuInflater().inflate(R.menu.chat, menu);
		} else if (group != null) {
			getMenuInflater().inflate(R.menu.groupchat, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_chat:
			if (ifGroupOrFriendExit
					&& !StringUtil.isNullOrEmpty(friend.getFriendid())) {
				Intent intent = new Intent(this, FriendsActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Constant.FRIEND_READ,
						(Serializable) friend);
				intent.putExtras(mBundle);
				startActivity(intent);
			}
			break;
		case R.id.menu_groupchat:
			if (ifGroupOrFriendExit && !StringUtil.isNullOrEmpty(group.groupID)) {
				Intent intent1 = new Intent(this,
						ShowGroupChatMemberActivity.class);
				Bundle mBundle1 = new Bundle();
				mBundle1.putSerializable(Constant.GROUP_READ,
						(Serializable) group);
				intent1.putExtras(mBundle1);
				startActivity(intent1);
			}
			break;

		case android.R.id.home:
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		}

		return false;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	Button.OnClickListener listener = new Button.OnClickListener() {// 创建监听对象

		@Override
		public void onClick(View arg0) {

			switch (arg0.getId()) {
			case R.id.chatting_send_btn:

				messageInput.setFocusable(true); // EditText重新获取焦点，保持键盘弹出
				messageInput.requestFocus();

				if (ifGroupOrFriendExit) {
					String message = messageInput.getText().toString();
					if ("".equals(message)) {
						ToastUtil.ShowLong(ChatActivity.this, "不能为空");
					} else {
						sendMsg(message);
						messageInput.setText("");
					}
				}
				break;

			default:
				break;
			}

		}
	};

	public void sendMsg(final String content) {

		String time = String.valueOf(System.currentTimeMillis());
		final ChatHisBean cBean = new ChatHisBean();
		cBean.setId(ChatPacketHelper.getGeneralUUID());
		cBean.setMsgFrom(myid);
		cBean.setMsgIdRecorder(myid);
		cBean.setMsgcontent(content);
		cBean.setMsgTime(time);

		if (friend != null) {
			cBean.setMsgTo(friend.getFriendid());
			int msgType = msgType(cBean);
			cBean.setType(msgType);
			cBean.setMsgType(ChatConstants.msg.TYPE_CHAT);
		} else if (group != null) {

			cBean.setMsgTo(group.groupID);
			cBean.setMsgGroup(group.groupID);
			cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);
			int msgType = msgType(cBean);
			cBean.setType(msgType);

		}
		cBean.setmsgSendStatus(Constant.MSG_SEND_STATUS_ING);

		if (message_pool.size() > 0) {
			long lasttime = Long
					.valueOf(message_pool.get(message_pool.size() - 1)
							.getMsgTime().substring(0, 13));
			long currenttime = Long.valueOf(time.substring(0, 13));
			if (currenttime - lasttime < 30000) {
				cBean.setShowTime(false);
			} else {
				cBean.setShowTime(true);
			}
		} else {
			cBean.setShowTime(true);
		}
		message_pool.add(cBean);
		QiXinBaseDao.insertMsgN(cBean);
		refreshMessage(message_pool);
	}

	private void sendVoiceMessage(int timelong) {
		time = System.currentTimeMillis();
		ChatHisBean cb = newChatHisBean(voiceName, Constant.FILE_TYPE_AUDIO,
				Constant.MSG_SEND_STATUS_ING, String.valueOf(time), "[语音]:"
						+ String.valueOf(timelong));
		message_pool.add(cb);
		QiXinBaseDao.insertMsgN(cb);
		refreshMessage(message_pool);
	}

	private void sendPicMessage(final String strImage) {
		ChatHisBean cb;
		time = System.currentTimeMillis();
		cb = newChatHisBean(strImage, Constant.FILE_TYPE_PIC,
				Constant.MSG_SEND_STATUS_ING, String.valueOf(time), "[图片]");
		message_pool.add(cb);
		QiXinBaseDao.insertMsgN(cb);
		refreshMessage(message_pool);
	}

	private void sendLocationMessage(final String strImage) {
		ChatHisBean cb;
		time = System.currentTimeMillis();
		cb = newChatHisBean(strImage, Constant.FILE_TYPE_LOCATION,
				Constant.MSG_SEND_STATUS_ING, String.valueOf(time), "[位置],"
						+ latitude + ","
						+ lonlongtitude + "," + address);
		message_pool.add(cb);
		QiXinBaseDao.insertMsgN(cb);
		refreshMessage(message_pool);
	}

	private ChatHisBean newChatHisBean(String fileId, String filetype,
			String sendStatus, String time, String msgContent) {
		ChatHisBean cBean = new ChatHisBean();
		if (friend != null) {
			cBean.setMsgTo(friend.getFriendid());
			cBean.setMsgType(ChatConstants.msg.TYPE_CHAT);
		} else if (group != null) {
			cBean.setMsgTo(group.groupID);
			cBean.setMsgGroup(group.groupID);
			cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);
		}
		cBean.setId(ChatPacketHelper.getGeneralUUID());
		cBean.setMsgFrom(myid);
		cBean.setMsgcontent(msgContent);
		cBean.setMsgIdRecorder(myid);
		cBean.setmsgIdFile(fileId);
		cBean.setmsgTypeFile(filetype);// 附件类型
		cBean.setmsgSendStatus(sendStatus);// 发送状态
		cBean.setMsgTime(time);
		int msgType = msgType(cBean);
		cBean.setType(msgType);
		return cBean;
	}

	@Override
	protected void onResume() {
		// ChatActivity.this.isClose = false;
		registerReceiver(contacterReceiver, contacterFilter);

		if (group != null) {
			getGroupUsersData();
			QiXinBaseDao.updateGroupChatMsgFlagRead(group.groupID);// 在企信表中把聊天消息标记为已读

			List<ChatHisBean> message_pooltemp = QiXinBaseDao
					.queryChatHisBeansByGroupChat(group.groupID, 0);
			if (message_pooltemp != null && message_pooltemp.size() != 0) {
				message_pool = message_pooltemp;
				// getFirstShowMessage();
			}

		} else if (friend != null) {
			getSingleChatFriendData();
			QiXinBaseDao.updateSingleChatMsgFlagRead(friend.getFriendid());// 在企信表中把聊天消息标记为已读

			List<ChatHisBean> message_pooltemp = QiXinBaseDao
					.queryChatHisBeansBySingleChat(sputil.getMyId(),
							friend.getFriendid(), 0);
			if (message_pooltemp != null && message_pooltemp.size() != 0) {
				message_pool = message_pooltemp;

			}

		}
		init();
		super.onResume();

	}

	@Override
	protected void onPause() {
		unregisterReceiver(contacterReceiver);
		setCurrentChatFriendOrGroupId("");
		if (group != null) {
			QiXinBaseDao.updateGroupChatMsgFlagRead(group.groupID);// 在企信表中把聊天消息标记为已读
		} else if (friend != null) {
			QiXinBaseDao.updateSingleChatMsgFlagRead(friend.getFriendid());// 在企信表中把聊天消息标记为已读
		}
		RecordPlayManager.getInstance(context).stopPlayRecord();
		super.onPause();
	}

	protected void refreshMessage(List<ChatHisBean> messages) {
		chatListTypeAdapter.refreshList(messages);
		listview.post(new Runnable() {
			@Override
			public void run() {
				// listview.setSelection(listview.getAdapter().getCount() - 1);
				listview.getRefreshableView()
						.setSelection(
								(listview.getRefreshableView()).getAdapter()
										.getCount() - 1);
			}
		});
	}

	protected void refreshMessage0(List<ChatHisBean> messages) {
		chatListTypeAdapter.refreshList(messages);

	}

	protected void pullRrefresMessage(List<ChatHisBean> messages) {

		chatListTypeAdapter.pullRrefreshList(messages);
	}

	private int msgType(ChatHisBean chb) {
		int msgType = 0;
		if (ChatConstants.msg.TYPE_ID_IQ.equalsIgnoreCase(chb.getMsgIdType())) {
			msgType = ChatConstants.VALUE_NOTICE;// 提示
		} else if (!sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& TextUtils.isEmpty(chb.getmsgIdFile())) {
			msgType = ChatConstants.VALUE_LEFT_TEXT;// 别人发的文本
		} else if (!sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& !TextUtils.isEmpty(chb.getmsgIdFile())
				&& Constant.FILE_TYPE_PIC
						.equalsIgnoreCase(chb.getmsgTypeFile())) {
			msgType = ChatConstants.VALUE_LEFT_IMAGE;// 别人发的图片
		} else if (!sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& !TextUtils.isEmpty(chb.getmsgIdFile())
				&& Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(chb
						.getmsgTypeFile())) {
			msgType = ChatConstants.VALUE_LEFT_AUDIO;// 别人发的语音
		} else if (sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& TextUtils.isEmpty(chb.getmsgIdFile())) {
			msgType = ChatConstants.VALUE_RIGHT_TEXT;// 我发送的文本
		} else if (sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& !TextUtils.isEmpty(chb.getmsgIdFile())
				&& Constant.FILE_TYPE_PIC
						.equalsIgnoreCase(chb.getmsgTypeFile())) {
			msgType = ChatConstants.VALUE_RIGHT_IMAGE;// 我发的图片
		} else if (sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& !TextUtils.isEmpty(chb.getmsgIdFile())
				&& Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(chb
						.getmsgTypeFile())) {
			msgType = ChatConstants.VALUE_RIGHT_AUDIO;// 我发的语音
			
		} else if (sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& !TextUtils.isEmpty(chb.getmsgIdFile())
				&& Constant.FILE_TYPE_LOCATION.equalsIgnoreCase(chb
						.getmsgTypeFile())) {
			msgType = ChatConstants.VALUE_RIGHT_LOCATION;// 我发的位置
		} else if (!sputil.getMyId().equalsIgnoreCase(chb.getMsgFrom())
				&& !TextUtils.isEmpty(chb.getmsgIdFile())
				&& Constant.FILE_TYPE_LOCATION.equalsIgnoreCase(chb
						.getmsgTypeFile())) {
			msgType = ChatConstants.VALUE_LEFT_LOCATION;// 别人发的位置
		}
		return msgType;
	}

	private class ContacterReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (WebSocketService.ACTION_ON_MSG.equals(action)) { // 新的聊天信息
				// 判断data类型和来源，当消息来自于正在聊天的对象,则进行处理，来自于其他好友或群组，ActivitySupport会进行处理
				Msg mMsg = (Msg) intent.getSerializableExtra("data");

				if (friend != null) {
					if (mMsg.from.trim().equals(friend.getFriendid().trim())
							&& "chat".equalsIgnoreCase(mMsg.type)) {
						final ChatHisBean cBean = new ChatHisBean();
						cBean.setId(mMsg.id);
						cBean.setMsgTo(mMsg.to);
						cBean.setMsgFrom(mMsg.from);
						cBean.setMsgcontent(mMsg.body);
						cBean.setMsgTime(String.valueOf(ChatPacketHelper
								.toMillsec(mMsg.time)));
						cBean.setMsgType(mMsg.type);
						cBean.setmsgIdFile(mMsg.file.id);
						cBean.setmsgTypeFile(mMsg.file.scene);
						int msgType = msgType(cBean);
						cBean.setType(msgType);
						message_pool.add(cBean);
						refreshMessage(message_pool);
					}

				}
				if (group != null) {
					if (mMsg.group.trim().equals(group.groupID)
							&& "groupchat".equalsIgnoreCase(mMsg.type)) {

						final ChatHisBean cBean = new ChatHisBean();
						cBean.setId(mMsg.id);
						cBean.setMsgTo(mMsg.group);
						cBean.setMsgFrom(mMsg.from);
						cBean.setMsgcontent(mMsg.body);
						cBean.setMsgTime(String.valueOf(ChatPacketHelper
								.toMillsec(mMsg.time)));
						cBean.setMsgType(mMsg.type);
						cBean.setmsgIdFile(mMsg.file.id);
						cBean.setmsgTypeFile(mMsg.file.scene);
						int msgType = msgType(cBean);
						cBean.setType(msgType);
						message_pool.add(cBean);
						refreshMessage(message_pool);
					}
				}
			}
			if (WebSocketService.ACTION_ON_IQ.equals(action)) {
				IQ iq = (IQ) intent.getSerializableExtra("data");
				Msg mIqMsg = (Msg) intent.getSerializableExtra("msg");

				if (ChatConstants.iq.FEATURE_REMOVE_CONTACT.equals(iq.feature)) {// 这是有人解除和你的好友关系的推送
					String userId = iq.from; // 这个人把你删了
					if (!StringUtil.isNullOrEmpty(userId) && friend != null
							&& friend.getFriendid().equalsIgnoreCase(userId)) {
						finish();
					}
				} else if (ChatConstants.iq.FEATURE_OP_GROUP_CHAT
						.equals(iq.feature)) {// 群相关操作
					@SuppressWarnings("unchecked")
					Map<String, Object> dataMap = (Map<String, Object>) iq.data;
					String type = (String) dataMap
							.get(ChatConstants.iq.DATA_KEY_TYPE);

					String groupID = (String) dataMap
							.get(ChatConstants.iq.DATA_KEY_GROUP_ID); // 群ID
					String userIDs = (String) dataMap
							.get(ChatConstants.iq.DATA_KEY_GROUP_USER_IDS); // 被删除的人，
					// "@"分割ID
					String[] users = userIDs.split("@");
					//
					// 如果是添加联系人等信息可以直接显示
					if (group != null
							&& groupID.equalsIgnoreCase(group.groupID)) {
						// 解散群
						if (ChatConstants.iq.DATA_VALUE_DROP.equals(type)) {
							finish();
						}
						// 添加人
						if (ChatConstants.iq.DATA_VALUE_ADD.equals(type)) {
							// final ChatHisBean cBean = new ChatHisBean();
							// cBean.setId(mIqMsg.id);
							// cBean.setMsgTo(mIqMsg.group);
							// cBean.setMsgFrom(mIqMsg.from);
							// cBean.setMsgcontent(mIqMsg.body);
							// cBean.setMsgTime(String.valueOf(ChatPacketHelper
							// .toMillsec(mIqMsg.time)));
							// cBean.setMsgType(mIqMsg.type);
							// cBean.setmsgIdFile(mIqMsg.file.id);
							// cBean.setmsgTypeFile(mIqMsg.file.scene);
							// int msgType = msgType(cBean);
							// cBean.setType(msgType);
							// message_pool.add(cBean);
							// refreshMessage(message_pool);
						}
						// 删除人
						if (ChatConstants.iq.DATA_VALUE_REMOVE.equals(type)) {
							// finish();
							// 如果删除了自己直接返回就可以了
							// 如果删除了别人，直接添加消息
							if (StringUtil.isStringArrayContains(users,
									sputil.getMyId())) {
								finish();

							} else {
								// final ChatHisBean cBean = new ChatHisBean();
								// cBean.setId(mIqMsg.id);
								// cBean.setMsgTo(mIqMsg.group);
								// cBean.setMsgFrom(mIqMsg.from);
								// cBean.setMsgcontent(mIqMsg.body);
								// cBean.setMsgTime(String
								// .valueOf(ChatPacketHelper
								// .toMillsec(mIqMsg.time)));
								// cBean.setMsgType(mIqMsg.type);
								// cBean.setmsgIdFile(mIqMsg.file.id);
								// cBean.setmsgTypeFile(mIqMsg.file.scene);
								// int msgType = msgType(cBean);
								// cBean.setType(msgType);
								// message_pool.add(cBean);
								// refreshMessage(message_pool);
							}
						}
					}
				}
			}

		}

	}

	// 从缓存中获取FriendInfo
	public FriendInfo getFriendInfo(String userId) {
		tempFriendInfo = new FriendInfo();
		for (int i = 0; i < mFriendList.size(); i++) {
			if (userId.equals(mFriendList.get(i).getFriendid())) {
				tempFriendInfo = mFriendList.get(i);
				return tempFriendInfo;
			}
		}
		return tempFriendInfo;
	}

	// 从相册选取图片
	public ExtendGridDataItem getImageTransferExtend() {
		ExtendGridDataItem item = new ExtendGridDataItem();
		item.tipResId = R.string.app_pane_pic_str;
		item.iconResId = R.drawable.getgallery_btn;
		item.action = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 调用相册
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 2);
			}
		};
		return item;
	}

	// 发送位置
	public ExtendGridDataItem getLocationExtend() {
		ExtendGridDataItem item = new ExtendGridDataItem();
		item.tipResId = R.string.app_pane_location_str;
		item.iconResId = R.drawable.getlocation_btn;
		item.action = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取位置
				Intent intent = new Intent(ChatActivity.this,
						LocationActivity.class);
				intent.putExtra("type", "select");
				startActivityForResult(intent, 0x000003);
			}
		};
		return item;
	}

	// 发送文件
		public ExtendGridDataItem getFileExtend() {
			ExtendGridDataItem item = new ExtendGridDataItem();
			item.tipResId = R.string.app_pane_file_str;
			item.iconResId = R.drawable.takepfile_btn;
			item.action = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(ChatActivity.this,
							FileListActivity.class);
//					intent.putExtra("type", "select");
					startActivity(intent);
					Toast.makeText(ChatActivity.this, "文件", Toast.LENGTH_LONG).show();
				}
			};
			return item;
		}
	// 拍照选取图片
	public ExtendGridDataItem getCameraExtend() {
		ExtendGridDataItem item = new ExtendGridDataItem();
		item.tipResId = R.string.app_pane_camera_str;
		item.iconResId = R.drawable.takephoto_btn;
		item.action = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean sdState = Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED);
				if (!sdState) {
					ToastUtil.ShowLong(getContext(), "sd卡不存在");
					return;
				}
				startPhoto();
			}
		};
		return item;
	}

	private String makePicFileName() {
		return Constant.FILE_TYPE_PIC + sputil.getMyId()
				+ DateUtil.getCurrentTime() + ".jpg";
	}

	private String makeAudioFileName() {
		return Constant.FILE_TYPE_AUDIO + sputil.getMyId()
				+ DateUtil.getCurrentTime() + ".aac";
	}

	private void startPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String fileName = makePicFileName();
		strImage = fileName;

		out = new File(Constant.CHAT_CACHE_DIR);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(Constant.CHAT_CACHE_DIR, fileName);
		uri = FileProvider.getUriForFile(this, "com.hjnerp.takephoto.fileprovider", out);//通过FileProvider创建一个content类型的Uri
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//		uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		checkPath(intent, Constant.CHAT_CACHE_DIR + fileName);
		((Activity) getContext()).startActivityForResult(intent, 1);
	}

	private void checkPath(Intent intent, String picpath) {
		if (intent != null) {
			Uri uri_DCIM = null;
			if (intent.getData() != null) {
				uri_DCIM = intent.getData();
			} else {
				uri_DCIM = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			}
			String DCIMPath = "";
			Cursor cr = getContext().getContentResolver().query(uri_DCIM,
					new String[] { MediaStore.Images.Media.DATA }, null, null,
					MediaStore.Images.Media.DATE_MODIFIED + " desc");
			if (cr.moveToNext()) {
				DCIMPath = cr.getString(cr
						.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cr.close();
			if (DCIMPath.equals(picpath)) {
				ToastUtil.ShowLong(getContext(), "");
			}
		}
	}

	// TODO
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {

				String filePath = Constant.CHAT_CACHE_DIR + strImage;
				// //压缩成功后直接显示到聊天记录中
				Bitmap bitmap = imageCompressUtil.getCompressImage(filePath);
				if (bitmap != null) {
					try {

						imageCompressUtil.saveImage(bitmap, filePath);
						System.gc();
						sendPicMessage(strImage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("发送图片异常：" + e.toString());
					}
				}

			}
			break;
		case 2:
			if (data == null) {
				break;
			}
			Uri uri = data.getData();
			if (uri != null) {

				String strImage = uri.getPath();
				String fileName = makePicFileName();
				strImage = fileName;

				// //压缩成功后直接显示到聊天记录中
				Bitmap bitmapuri = imageCompressUtil.getCompressUri(
						this.getContext(), uri);

				if (bitmapuri != null) {
					try {
						imageCompressUtil.saveImage(bitmapuri,
								Constant.CHAT_CACHE_DIR + strImage);
						System.gc();
						sendPicMessage(strImage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("发送图片异常2：" + e.toString());
					}

				} else {
					return;
				}
			}
			break;
		case 0x000003:
			String path = data.getStringExtra("path");
			latitude = data.getDoubleExtra("y", 0)+"";//("x");
			lonlongtitude = data.getDoubleExtra("x", 0)+"";
			address = data.getStringExtra("address");
			try {
				sendLocationMessage(path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				android.util.Log.i("info", "发送位置异常："+e.toString());
			}
			break;
		default:
			break;

		}

	}

	private void initRecordManager() {
		// 语音相关管理器
		recordManager = BmobRecordManager.getInstance(this);
		// 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
		recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

			@Override
			public void onVolumnChanged(int value) {
				iv_record.setImageDrawable(drawable_Anims[value]);
			}

			@Override
			public void onTimeChanged(int recordTime, String localPath) {
				// TODO Auto-generated method stub

				if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
					// // 需要重置按钮
					voiceRecordBtn.setPressed(false);
					voiceRecordBtn.setClickable(false);
					voiceRecordBtn
							.setBackgroundResource(R.drawable.voice_rcd_btn_nor);

					voice_rcd_hint_rcding.setVisibility(View.GONE);
					rcChat_popup.setVisibility(View.GONE);
					int timelong = recordManager.stopRecording();
					sendVoiceMessage(timelong);
				} else {

				}
			}
		});
	}

	/**
	 * 显示重发按钮 showResendDialog
	 * 
	 * @Title: showResendDialog
	 * @Description: TODO
	 * @param @param recent
	 * @return void
	 * @throws
	 */
	public void showResendDialog(final View parentV, View v, final Object values) {
		noticeDialog = new Dialog(context, R.style.noticeDialogStyle);
		noticeDialog.setContentView(R.layout.dialog_notice_withcancel);

		TextView dialog_confirm_rl;
		TextView dialog_cc_cancel_rl;
		TextView notice = (TextView) noticeDialog
				.findViewById(R.id.dialog_notice_tv);
		notice.setText("确定重发该消息");

		TextView confirm = (TextView) noticeDialog
				.findViewById(R.id.dialog_cancel_tv);
		confirm.setText("重发");

		dialog_confirm_rl = (TextView) noticeDialog
				.findViewById(R.id.dialog_confirm_tv);
		dialog_confirm_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				noticeDialog.dismiss();
				ChatHisBean chatBean = (ChatHisBean) values;
				// TODO Auto-generated method stub
				int type = chatBean.getType();
				switch (type) {
				case ChatConstants.VALUE_RIGHT_TEXT:
					ProgressBar pb;
					ImageView fail;
					pb = (ProgressBar) parentV
							.findViewById(R.id.chat_right_item_pb_righttext);
					fail = (ImageView) parentV
							.findViewById(R.id.chat_right_item_text_sendfail_righttext);
					chatBean.setmsgSendStatus(Constant.MSG_SEND_STATUS_ING);
					reSendMsg sendTxtMsg = new reSendMsg(fail, pb, chatBean);
					sendTxtMsg.execute();
					break;
				case ChatConstants.VALUE_RIGHT_IMAGE:
					ProgressBar pb1;
					ImageView fail1;
					pb1 = (ProgressBar) parentV
							.findViewById(R.id.chat_right_item_pb_rightpic);
					fail1 = (ImageView) parentV
							.findViewById(R.id.chat_right_item_pic_sendfail_rightpic);

					if (Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(chatBean
							.getmsgSendStatus())) {
						chatBean.setmsgSendStatus(Constant.MSG_SEND_STATUS_ING);
					}
					if (Constant.FILE_SEND_STATUS_FILE_FAIL
							.equalsIgnoreCase(chatBean.getmsgSendStatus())) {
						chatBean.setmsgSendStatus(Constant.FILE_SEND_STATUS_FILE_ING);
					}
					reSendMsg sendPicMsg = new reSendMsg(fail1, pb1, chatBean);
					sendPicMsg.execute();
					break;
				case ChatConstants.VALUE_RIGHT_AUDIO:
					ProgressBar pb2;
					ImageView fail2;
					pb2 = (ProgressBar) parentV
							.findViewById(R.id.chat_right_item_pb_rightaudio);
					fail2 = (ImageView) parentV
							.findViewById(R.id.chat_right_item_pic_sendfail_rightaudio);

					if (Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(chatBean
							.getmsgSendStatus())) {
						chatBean.setmsgSendStatus(Constant.MSG_SEND_STATUS_ING);
					}
					if (Constant.FILE_SEND_STATUS_FILE_FAIL
							.equalsIgnoreCase(chatBean.getmsgSendStatus())) {
						chatBean.setmsgSendStatus(Constant.FILE_SEND_STATUS_FILE_ING);
					}
					reSendMsg sendAudioMsg = new reSendMsg(fail2, pb2, chatBean);
					sendAudioMsg.execute();
					break;
				default:
					break;
				}

			}
		});
		dialog_cc_cancel_rl = (TextView) noticeDialog
				.findViewById(R.id.dialog_cancel_tv);
		dialog_cc_cancel_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				noticeDialog.dismiss();
			}
		});
		noticeDialog.show();

	}

	public ChatHisBean getChatHisBean(String filname) {
		for (int i = 0; i < message_pool.size(); i++) {
			if (filname.equalsIgnoreCase(message_pool.get(i).getmsgIdFile())) {
				return message_pool.get(i);
			}
		}

		return null;
	}

}

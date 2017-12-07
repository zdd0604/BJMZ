package com.mznerp.activity.im.adapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mznerp.activity.contact.FriendsActivity;
import com.mznerp.activity.im.ChatActivity;
import com.mznerp.activity.im.ShowChatPictureActivity;
import com.mznerp.common.Constant;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.ChatHisBean;
import com.mznerp.model.FriendInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.util.AttachFileProcessor;
import com.mznerp.util.AttachFileProcessor.OnProcessResultListener;
import com.mznerp.util.DateUtil;
import com.mznerp.util.ImageLoaderHelper;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.util.myscom.FileUtils;
import com.mznerp.util.myscom.StringUtils;
import com.mznerp.R;
 
//@SuppressLint("NewApi")
public class ChatListAdapter extends BaseAdapter {
	String TAG = "ChatListAdapter";
	private Context context;
	private List<ChatHisBean> list = null;
	private PullToRefreshListView adapterList;
	private SharePreferenceUtil sputil;
	private String myid;
	private FriendInfo myinfo;
	private FriendInfo friendinfo;
	private Bitmap default_bitmap;
	AnimationDrawable anim;
	ImageView IMAGEVIEW;
	String FILENAME;

	public ChatListAdapter(Context context, List<ChatHisBean> list,
			PullToRefreshListView adapterList) {
		this.context = context;
		this.list = list;
		this.adapterList = adapterList;
		sputil = SharePreferenceUtil.getInstance(context);
		myid = sputil.getMyId();
		myinfo = QiXinBaseDao.queryFriendInfo(myid);
		default_bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.v5_0_1_profile_headphoto);
	}

	public void refreshList(List<ChatHisBean> list) {
		this.list = list;
		this.notifyDataSetChanged();
//		adapterList.setSelection(list.size() - 1);
		adapterList.getRefreshableView().setSelection((adapterList.getRefreshableView()).getAdapter().getCount() - 1);
	}

	public void pullRrefreshList(List<ChatHisBean> list) {
		this.list = list;
		this.notifyDataSetChanged();
//		adapterList.setSelection(0);
		adapterList.getRefreshableView().setSelection(0);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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
		ChatHisBean message = list.get(position);
		ViewHolder vHolder = null;

		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.chat_item,
					parent, false);
			vHolder = new ViewHolder();
			vHolder.chatLeftHeadphoto = (ImageView) view
					.findViewById(R.id.chat_left_item_headphoto);
			vHolder.chatRightHeadphoto = (ImageView) view
					.findViewById(R.id.chat_right_item_headphoto);
			vHolder.chatLeftContent = (TextView) view
					.findViewById(R.id.chat_left_item_content);
			vHolder.chatRightContent = (TextView) view
					.findViewById(R.id.chat_right_item_content);
			vHolder.chatTime = (TextView) view.findViewById(R.id.chat_sendtime);
			vHolder.chatTitle = (TextView) view
					.findViewById(R.id.chat_sendtitle);
			vHolder.chatLeftPic = (ImageView) view
					.findViewById(R.id.chat_left_item_pic);
			vHolder.chatRightPic = (ImageView) view
					.findViewById(R.id.chat_right_item_pic);
			vHolder.resendPic = (ImageView) view
					.findViewById(R.id.chat_right_item_pic_sendfail);
			view.setTag(vHolder);

		} else {
			vHolder = (ViewHolder) view.getTag();
		}
		
		if (ChatConstants.msg.TYPE_ID_IQ.equalsIgnoreCase(message.getMsgIdType())) {
			// 设置title
			sureVisibility(vHolder, View.GONE, View.GONE);
			vHolder.chatTime.setVisibility(View.GONE);
			vHolder.chatTitle.setVisibility(View.VISIBLE);
			vHolder.chatTitle.setText(message.getMsgcontent());

		} else {
			
			vHolder.chatTitle.setVisibility(View.GONE);
			if (position > 0) {
				long lasttime = Long.valueOf(list.get(position - 1)
						.getMsgTime());
				long currenttime = Long.valueOf(message.getMsgTime());

				if (currenttime - lasttime > 30000) {
					vHolder.chatTime.setVisibility(View.VISIBLE);
					vHolder.chatTime.setText(DateUtil
							.msgToHumanReadableTime(message.getMsgTime()));
				} else {
					vHolder.chatTime.setVisibility(View.GONE);
				}
			} else {
				vHolder.chatTime.setVisibility(View.VISIBLE);
				vHolder.chatTime.setText(DateUtil
						.msgToHumanReadableTime(message.getMsgTime()));
			}

			// 设置聊天内容
			if (message.getMsgFrom().equals(myid)) {// 我发送的消息
				sureVisibility(vHolder, View.GONE, View.VISIBLE);
				
				// 设置头像
				// if (StringUtil.isNullOrEmpty(myinfo.getFriendimage())) {
				if (StringUtils.isBlank(myinfo.getFriendimage())) {
					vHolder.chatRightHeadphoto.setImageBitmap(default_bitmap);
				} else {
					String url = ChatPacketHelper.buildImageRequestURL(myinfo
							.getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
					ImageLoaderHelper.displayImage(url, vHolder.chatRightHeadphoto);
				}
				vHolder.chatRightHeadphoto.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i(TAG,">>>>>>>>> 点击我的头像" );
						Intent intent = new Intent();
						intent.setClass(context, FriendsActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putSerializable(Constant.FRIEND_READ,
								(Serializable) myinfo);
						intent.putExtras(mBundle);

						((ChatActivity)context).startActivity(intent);
						
					}
				});
				vHolder.chatRightPic.setTag(message);
				vHolder.chatRightPic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final ImageView myview  = (ImageView)v;
						Log.i(TAG,">>>>>>>>>> 点击我发送的附件" );
						final ChatHisBean messageTemp = (ChatHisBean)v.getTag();
						if(!TextUtils.isEmpty(messageTemp.getmsgIdFile()) && Constant.FILE_TYPE_PIC.equalsIgnoreCase(messageTemp.getmsgTypeFile())){
							Intent intent = new Intent();
							intent.setClass(context, ShowChatPictureActivity.class);
							intent.putExtra("picname", messageTemp.getmsgIdFile());
							((ChatActivity)context).startActivity(intent);
						}else if(!TextUtils.isEmpty(messageTemp.getmsgIdFile()) && Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(messageTemp.getmsgTypeFile())){
							IMAGEVIEW = myview;
							FILENAME = messageTemp.getmsgIdFile();
							if(!FileUtils.isFileExit(Constant.CHATAUDIO_DIR + messageTemp.getmsgIdFile() )){//文件存在
								ToastUtil.ShowShort(context, "下载中，请稍后重试");
								AttachFileProcessor.requestImAudioAttach(messageTemp.getmsgIdFile(), new OnProcessResultListener() {
									
									@Override
									public void onProcessResult(boolean success, String msg) {
										Log.e(TAG,"down audio result is " + success + "  " + msg);
										Log.e(TAG,"down audio result is >>>>>>>>>>>>>>>>>");
										if(success){
											//TODO
											sendToHandler("right");
											
										}else{
										}
									}
								});
								return;
							}
							
//							playaudio(myview,messageTemp.getmsgIdFile());
							sendToHandler("right");
							
//							MediaPlayer mplayer = new MediaPlayer();
//							myview.setBackgroundResource(R.anim.audioplayframe_right);  
//							anim = (AnimationDrawable) myview.getBackground();  
//							anim.start();  
////							myview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_right_playing));
//			                     try {
//									mplayer.setDataSource(Constant.CHATAUDIO_DIR + messageTemp.getmsgIdFile());
//									mplayer.prepare();
//								} catch (IllegalArgumentException e) {
//									e.printStackTrace();
//								} catch (SecurityException e) {
//									e.printStackTrace();
//								} catch (IllegalStateException e) {
//									e.printStackTrace();
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
//			                     mplayer.start();
//			                     
//			                     mplayer.setOnCompletionListener(new OnCompletionListener() {
//									
//									@Override
//									public void onCompletion(MediaPlayer mp) {
//										Log.e(TAG,"oncompletion");
//										anim.stop();
//										myview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_right));
//									}
//								});
			                 
						}
						
					}
				});
				
				
				
				// 设置我发送的图片
				// if(!StringUtil.isNullOrEmpty(message.getmsgIdFile())){
				if (StringUtils.isNotBlank(message.getmsgIdFile()) && Constant.FILE_TYPE_PIC.equalsIgnoreCase(message.getmsgTypeFile())) {
					Log.i(TAG, "pic name is " + message.getmsgIdFile());
					
					vHolder.chatRightPic.setVisibility(View.VISIBLE);
					
					if(Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(message.getmsgSendStatus())){
						vHolder.chatRightPic.setImageDrawable(context.getResources().getDrawable(R.drawable.set_wrapdata));
						vHolder.resendPic.setVisibility(view.GONE);
					}else if(Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(message.getmsgSendStatus())){
					
						vHolder.chatRightPic.setImageDrawable(context.getResources().getDrawable(R.drawable.set_wrapdata));
						vHolder.resendPic.setVisibility(view.VISIBLE);
						vHolder.resendPic.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								ChatHisBean messageTemp = (ChatHisBean)v.getTag();
								v.setVisibility(View.GONE);
								try {
//									long time = System.currentTimeMillis();
//									((ChatActivity)context).sendPic(messageTemp.getmsgIdFile(),"pic",String.valueOf(time));
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						});
					}else{
						vHolder.resendPic.setVisibility(view.GONE);
						String url = ChatPacketHelper
								.buildImageRequestURL(message.getmsgIdFile(), ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);				
						com.mznerp.util.Log.i("info", "我发送的图片地址："+url);
						ImageLoaderHelper.displayImage(url,
								vHolder.chatRightPic);
					}
					
					vHolder.chatRightContent.setVisibility(View.GONE);
				}else if(StringUtils.isNotBlank(message.getmsgIdFile()) && Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(message.getmsgTypeFile())){
					//TODO　我发送的语音
					vHolder.chatRightContent.setVisibility(View.GONE);
					vHolder.chatRightPic.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_right));
					
					
				}else {
					vHolder.chatRightContent.setVisibility(View.VISIBLE);
					vHolder.chatRightContent.setText(message.getMsgcontent());
					
					vHolder.chatRightPic.setVisibility(View.GONE);
				}

			} else {// 别人发送的消息，本地获取或网络获取
				sureVisibility(vHolder, View.VISIBLE, View.GONE);
				
				// 设置头像
				/**
				 * @author haijian
				 * 这里出现头像错乱现象
				 */
				friendinfo = ((ChatActivity) context).getFriendInfo(message.getMsgFrom());
				vHolder.chatLeftHeadphoto.setTag(friendinfo);
				vHolder.chatLeftPic.setTag(message);
				if (StringUtils.isBlank(friendinfo.getFriendimage())) {
					vHolder.chatLeftHeadphoto.setImageBitmap(default_bitmap);
				} else {
					String url = ChatPacketHelper
							.buildImageRequestURL(friendinfo.getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);

					ImageLoaderHelper.displayImage(url, vHolder.chatLeftHeadphoto);
				}
				
				vHolder.chatLeftHeadphoto.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FriendInfo friendInfoTemp = (FriendInfo)v.getTag();
						Log.e(TAG,">>>>>> 点击别人发送的头像 " + friendinfo.toString());
						if (friendInfoTemp == null)
						{
							return ;
						}
						Intent intent = new Intent();
						intent.setClass(context, FriendsActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putSerializable(Constant.FRIEND_READ,
								(Serializable) friendInfoTemp);
						intent.putExtras(mBundle);

						((ChatActivity)context).startActivity(intent);
					}
				});
				
				vHolder.chatLeftPic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final ImageView myview  = (ImageView)v;
						Log.i(TAG,">>>>>>>>>> 点击别人发送的附件" );
						final ChatHisBean messageTemp = (ChatHisBean)v.getTag();
						if(!TextUtils.isEmpty(messageTemp.getmsgIdFile()) && Constant.FILE_TYPE_PIC.equalsIgnoreCase(messageTemp.getmsgTypeFile())){
							Intent intent = new Intent();
							intent.setClass(context, ShowChatPictureActivity.class);
							intent.putExtra("picname", messageTemp.getmsgIdFile());
							((ChatActivity)context).startActivity(intent);
						}else if(!TextUtils.isEmpty(messageTemp.getmsgIdFile()) && Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(messageTemp.getmsgTypeFile())){
							IMAGEVIEW = myview;
							FILENAME = messageTemp.getmsgIdFile();
							//TODO
							if(!FileUtils.isFileExit(Constant.CHATAUDIO_DIR + messageTemp.getmsgIdFile() )){//文件存在
								ToastUtil.ShowShort(context, "下载中，请稍后重试");
								AttachFileProcessor.requestImAudioAttach(messageTemp.getmsgIdFile(), new OnProcessResultListener() {
									@Override
									public void onProcessResult(boolean success, String msg) {
										Log.e(TAG,"down audio result is " + success + "  " + msg);
										if(success){
											sendToHandler("left");
										}else{
										}
										
									}
								});
								return;
							}
							
							sendToHandler("left");
//							MediaPlayer mplayer = new MediaPlayer();
//							myview.setBackgroundResource(R.anim.audioplayframe_left);  
//							anim = (AnimationDrawable) myview.getBackground();  
//							anim.start();  
//
////							myview.setImageDrawable(context.getResources().getDrawable(R.drawable.audio_left_playing));
//			                     try {
//									mplayer.setDataSource(Constant.CHATAUDIO_DIR + messageTemp.getmsgIdFile());
//									mplayer.prepare();
//								} catch (IllegalArgumentException e) {
//									e.printStackTrace();
//								} catch (SecurityException e) {
//									e.printStackTrace();
//								} catch (IllegalStateException e) {
//									e.printStackTrace();
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
//			                     mplayer.start();
//		                     
//			                     mplayer.setOnCompletionListener(new OnCompletionListener() {
//									
//									@Override
//									public void onCompletion(MediaPlayer mp) {
//										Log.e(TAG,"oncompletion");
//										anim.stop();
//										myview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_left));
//									}
//								});
			                 
						}
						
						
						
						
						
						
//						ChatHisBean bean = (ChatHisBean)v.getTag();
//						FriendInfo friendInfoTemp = ((ChatActivity) context).getFriendInfo(bean.getMsgFrom());;
//						Log.e(TAG,">>>>>> 点击别人发送的内容 " + friendInfoTemp.getFriendname());
//						if(!TextUtils.isEmpty(bean.getmsgIdFile())){
//						Intent intent = new Intent();
//						intent.setClass(context, ShowChatPictureActivity.class);
//						intent.putExtra("picname", bean.getmsgIdFile());
//						((ChatActivity)context).startActivity(intent);
//						}
						
					}
				});
				// 设置聊天图片

				if (StringUtils.isNotBlank(message.getmsgIdFile()) && Constant.FILE_TYPE_PIC.equalsIgnoreCase(message.getmsgTypeFile())) {
					Log.i(TAG,"position " + position+ "chat pic id  is >>>>>>>>>>>>>>>> "+ message.getmsgIdFile());
					vHolder.chatLeftPic.setVisibility(View.VISIBLE);
					
					String url = ChatPacketHelper
							.buildImageRequestURL(message.getmsgIdFile(), ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);				
					
					ImageLoaderHelper.displayImage(url,
							vHolder.chatLeftPic);
					vHolder.chatLeftContent.setVisibility(View.GONE);
				} else if(StringUtils.isNotBlank(message.getmsgIdFile()) && Constant.FILE_TYPE_AUDIO.equalsIgnoreCase(message.getmsgTypeFile())){
					//TODO 别人发送的语音
					vHolder.chatLeftContent.setVisibility(View.GONE);
					vHolder.chatLeftPic.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_left));
				}else {
					vHolder.chatLeftContent.setVisibility(View.VISIBLE);
					vHolder.chatLeftContent.setText(message.getMsgcontent());
					vHolder.chatLeftPic.setVisibility(View.GONE);
				}
//				vHolder.chatLeftContent.setVisibility(View.GONE);

			}
		}
//		vHolder.chatLeftContent.setVisibility(View.GONE);
		return view;
	}

	/*
	 * 列表布局的各个控件
	 */
	public static class ViewHolder {
		ImageView chatLeftHeadphoto;// 对方的头像
		ImageView chatRightHeadphoto;// 当前用户的头像
		ImageView chatRightPic;// 当前用户的头像
		ImageView chatLeftPic;// 当前用户的头像
		ImageView resendPic;// 

		TextView chatLeftContent;// 对方内容
		TextView chatRightContent;// 当前用户的内容
		TextView chatTime; // 聊天信息发送时间
		TextView chatTitle;// 聊天系统公告
	}

	private void sureVisibility(ViewHolder holder, int viewLeft, int viewRight) {
		holder.chatLeftContent.setVisibility(viewLeft);
		holder.chatLeftHeadphoto.setVisibility(viewLeft);
		holder.chatLeftPic.setVisibility(viewLeft);

		holder.chatRightContent.setVisibility(viewRight);
		holder.chatRightHeadphoto.setVisibility(viewRight);
		holder.chatRightPic.setVisibility(viewRight);
	}

	public static Bitmap MakeCameraPreview(View convertView, Bitmap bm) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		Log.e(">>>>>>>>>", "pic width is " + width + " height is " + height);
		// 设置想要的大小
		NinePatchDrawable npd = (NinePatchDrawable) convertView.getResources()
				.getDrawable(R.drawable.chat_to_bg_normal1);
		// npd.setBounds(0,0,0,0);

		// Bitmap bitmap = Bitmap
		// .createBitmap(
		// npd.getIntrinsicWidth(),
		// npd.getIntrinsicHeight(),
		// npd.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
		// : Bitmap.Config.RGB_565);

		// Canvas canvas = new Canvas(bitmap);
		// npd.setBounds(0, 0, npd.getIntrinsicWidth(),
		// npd.getIntrinsicHeight());
		// npd.draw(canvas);
		//
		// canvas.drawBitmap(bm, 0, 0, null);// 叠加bm
		// canvas.save(Canvas.ALL_SAVE_FLAG);
		// Log.e("aaaa","width is " + bitmap.getWidth() + " height is " +
		// bitmap.getHeight());

		Bitmap bkg = BitmapFactory.decodeResource(convertView.getResources(),
				R.drawable.chat_to_bg_normal).copy(Bitmap.Config.ARGB_8888,
				true);
		// Bitmap bkg =
		// BitmapFactory.decodeResource(convertView.getResources(),R.drawable.start_bg).copy(Bitmap.Config.ARGB_8888,
		// true);
		// int newWidth = 320;
		// int newHeight = 240;
		// int bkgWidth = bkg.getWidth();
		// int bkgHeight = bkg.getHeight();
		// Log.e("aaaa","bkg width is "+bkgWidth+" width is "+bkgHeight);

		// float scaleBkgWidth = (width+100)/bkgWidth;
		// float scaleBkgHeight = (height + 100)/bkgHeight;
		// Matrix bkgMatrix = new Matrix();
		// bkgMatrix.postScale(scaleBkgWidth, scaleBkgHeight);
		// Bitmap.createBitmap(bkg,0,0,bkg.getWidth(),bkg.getHeight(),bkgMatrix,true);
		// Log.e("aaaa","bkgNew width is "+bkgNew.getWidth()+" width is "+bkgNew.getHeight());

		Paint paint = new Paint();
		paint.setAlpha(153);// 透明度60%
		
		
		Bitmap b = Bitmap.createBitmap(100, 500, Bitmap.Config.ARGB_8888);
		Log.e("aaaa","b width is " + b.getWidth() + " b height is " + b.getHeight());
		Canvas c = new Canvas(b);
		// Canvas canvas1 = new Canvas(bkg);
		c.drawBitmap(bm, 0, 0, null);
//		npd.setBounds(0, 0, 50, 200);
		npd.draw(c);
		
		
		
		// c.drawBitmap(bm, 0, 0, paint1);// 叠加bm
		// canvas1.save(Canvas.ALL_SAVE_FLAG);

		// return bkgNew;
		Log.e("aaaa","new b width is " + b.getWidth() + " b height is " + b.getHeight());
		return b;
	}

	
	private void sendToHandler(String flag) {
		Message Msg = new Message();
		Bundle b = new Bundle();
		b.putString("flag", flag);
		Msg.setData(b);

		myHandler.sendMessage(Msg);
	}

	final Handler myHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {

			Bundle b = msg.getData();
			String mmsg = b.getString("flag");
			Log.e(TAG,"handler receive is >>>>>>>>>>  " + mmsg);
			if("left".equalsIgnoreCase(mmsg)){
				playaudio("left");
			}else{
				playaudio("right");
			}
		};
	};
	private void playaudio(final String leftorright){
		MediaPlayer mplayer = new MediaPlayer();
		if("left".equalsIgnoreCase(leftorright)){
			IMAGEVIEW.setBackgroundResource(R.anim.audioplayframe_left);  
		}else{
			IMAGEVIEW.setBackgroundResource(R.anim.audioplayframe_right);  
		}
		anim = (AnimationDrawable) IMAGEVIEW.getBackground();  
		anim.start();  
//		myview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_right_playing));
             try {
				mplayer.setDataSource(Constant.CHATAUDIO_DIR + FILENAME);
				mplayer.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
             mplayer.start();
             
             mplayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					Log.e(TAG,"oncompletion");
					anim.stop();
					
					if("left".equalsIgnoreCase(leftorright)){
						IMAGEVIEW.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_left));
					}else{
						IMAGEVIEW.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.audio_right));
					}
					
				}
			});
	}
	

}

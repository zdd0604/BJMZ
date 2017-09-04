package com.hjnerp.activity.im;

import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.util.AttachFileProcessor;
import com.hjnerp.util.AttachFileProcessor.OnProcessResultListener;
import com.hjnerp.util.ToastUtil;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class reSendMsg extends AsyncTask<String, Integer, Integer> {

	private ImageView imageView;
	private ProgressBar progressBar;
	private ChatHisBean chatHisBean; 
	private String errorText =""; 
	private int fileSend = 10;

	public reSendMsg(ImageView imageView, ProgressBar progressBar,
			ChatHisBean chatHisBean) {
		this.imageView = imageView;
		this.progressBar = progressBar;
		this.chatHisBean = chatHisBean;
	}

	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		return sendMsg();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(Integer result) {
		switch (result) {
		case -1:
			imageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case 0: //
			imageView.setVisibility(View.GONE);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case 10:
			imageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		}
		if (!"".equalsIgnoreCase( errorText.trim()))
		{
			ToastUtil.ShowShort( EapApplication.getApplication().getApplicationContext(), errorText);
			
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		imageView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	private int sendMsg() {
		int ret = 0;
		int type = chatHisBean.getType();
		switch (type) {
		case ChatConstants.VALUE_RIGHT_TEXT:
			ret = reSendTextMsg();
			break;
		case ChatConstants.VALUE_RIGHT_IMAGE:
			ret = reSendPicMsg();
			break;
		case ChatConstants.VALUE_RIGHT_AUDIO:
			ret = reSendAudioMsg();
			break;
		case ChatConstants.VALUE_RIGHT_LOCATION:
			ret = reSendLocMsg();
			break;
		default:
			break;
		}

		return ret;
	}

	private int reSendTextMsg() {

		if (Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(chatHisBean
				.getmsgSendStatus())) {

			boolean sendflag = false;
			sendflag = HJWebSocketManager.getInstance().sendMsg(
					chatHisBean.getMsgGroup(), chatHisBean.getMsgTo(),
					chatHisBean.getMsgcontent(), chatHisBean.getMsgType(),
					chatHisBean.getId(),errorText );
			if (sendflag) {
				saveData(Constant.MSG_SEND_STATUS_SUCCESS);
			} else {
				saveData(Constant.MSG_SEND_STATUS_FAIL);
				return -1;
			}
		}
		return 0;
	}

	private int reSendPicMsg() {

		int intret = 0;
		if (Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(chatHisBean
				.getmsgSendStatus())) {
			long resualt = 0;

			resualt = HJWebSocketManager.getInstance().sendFileChatMsg(chatHisBean.getId(),
					chatHisBean.getMsgTo(), chatHisBean.getmsgIdFile(),
					Constant.FILE_TYPE_PIC, chatHisBean.getMsgcontent(), chatHisBean.getMsgType(),errorText);
			if (0 == resualt) {
				saveData(Constant.FILE_SEND_STATUS_FILE_ING);
			} else {
				saveData(Constant.MSG_SEND_STATUS_FAIL);
				return -1;
			}
		}
		if (Constant.FILE_SEND_STATUS_FILE_ING.equalsIgnoreCase(chatHisBean
				.getmsgSendStatus())) {
			sendPic();
			do {

			} while (fileSend == 10);
			intret = fileSend;
		}

		return intret;
	}
	/**
	 * @author haijian
	 * 发送位置
	 * @return
	 */
	private int reSendLocMsg() {

		int intret = 0;
		if (Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(chatHisBean
				.getmsgSendStatus())) {
			long resualt = 0;

			resualt = HJWebSocketManager.getInstance().sendFileChatMsg(chatHisBean.getId(),
					chatHisBean.getMsgTo(), chatHisBean.getmsgIdFile(),
					Constant.FILE_TYPE_LOCATION, chatHisBean.getMsgcontent(), chatHisBean.getMsgType(),errorText);
			if (0 == resualt) {
				saveData(Constant.FILE_SEND_STATUS_FILE_ING);
			} else {
				saveData(Constant.MSG_SEND_STATUS_FAIL);
				return -1;
			}
		}
		if (Constant.FILE_SEND_STATUS_FILE_ING.equalsIgnoreCase(chatHisBean
				.getmsgSendStatus())) {
			sendPic();
			do {

			} while (fileSend == 10);
			intret = fileSend;
		}

		return intret;
	}
	

	private int reSendAudioMsg() {
		long resualt = 0;

		if (Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(chatHisBean
				.getmsgSendStatus())) {

			resualt = HJWebSocketManager.getInstance().sendFileChatMsg(chatHisBean.getId(),
					chatHisBean.getMsgTo(), chatHisBean.getmsgIdFile(),
					Constant.FILE_TYPE_AUDIO, chatHisBean.getMsgcontent(), chatHisBean.getMsgType(),errorText);
			if (0 == resualt) {
				saveData(Constant.FILE_SEND_STATUS_FILE_ING);
			} else {
				saveData(Constant.MSG_SEND_STATUS_FAIL);
				return -1;
			}

		}

		if (Constant.FILE_SEND_STATUS_FILE_ING.equalsIgnoreCase(chatHisBean
				.getmsgSendStatus())) {
			sendAudio();
			do {

			} while (fileSend == 10);
		}
		resualt = fileSend;
		return fileSend;
	}

	// TODO http异步发送图片
	public void sendPic() {
		// 发送图片
		AttachFileProcessor.responseImAttach(chatHisBean.getmsgIdFile(),
				new OnProcessResultListener() {
					@Override
					public void onProcessResult(boolean success, String msg) {
						// TODO Auto-generated method stub

						if (success) {// 图片上传成功
							fileSend = 0;
							saveData(Constant.FILE_SEND_STATUS_FILE_SUCCESS);

						} else {// 图片上传失败
							fileSend = -1;
							saveData(Constant.FILE_SEND_STATUS_FILE_FAIL);
						}
					}
				});
	}

	public void sendAudio() { 
		//发送语音
		AttachFileProcessor.responseImAudioAttach(chatHisBean.getmsgIdFile(),
				new OnProcessResultListener() {
					@Override
					public void onProcessResult(boolean success, String msg) {
						// TODO Auto-generated method stub
						if (success) {
							fileSend = 0;
							saveData(Constant.FILE_SEND_STATUS_FILE_SUCCESS);
						} else {
							fileSend = -1;
							saveData(Constant.FILE_SEND_STATUS_FILE_FAIL);
						}
					}
				}); 
	}

	public void saveData(String flag_send) { 
		chatHisBean.setmsgSendStatus(flag_send);
		QiXinBaseDao.updateMsgSendStatue(chatHisBean.getId(), flag_send);
	}

}

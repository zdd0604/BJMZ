package com.hjnerp.activity.im.adapter;

import java.io.File;
import java.io.FileInputStream;

import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.myscom.FileUtils;
import com.hjnerpandroid.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.widget.ImageView;

/**
 * 播放录音文件
 * 
 * @ClassName: RecordPlayClickListener
 * @Description: TODO
 * @author 李庆义
 */

public class RecordPlayManager {
	ChatHisBean message;
	ImageView iv_voice;
	ImageView iv_play;
	private AnimationDrawable anim = null;
	static Context context;
	MediaPlayer mediaPlayer = null;
	public boolean isPlaying = false;
	private static Object INSTANCE_LOCK = new Object();
	private static volatile RecordPlayManager instance;
	boolean isFrom; // 如果为真 是左边 否则 为右边
 
	public static RecordPlayManager getInstance(Context paramContext) {
		if (instance == null)
			synchronized (INSTANCE_LOCK) {
				if (instance == null)
					instance = new RecordPlayManager();
				
				context = paramContext;
			}
		return instance;
	}

	public void recordPaly(ChatHisBean msg, ImageView voice,ImageView play ) {
		if (isPlaying) {
			stopPlayRecord();
		}
		this.iv_voice = voice;
		this.message = msg;
		if (play != null)
		{
		  this.isFrom = true;
		  this.iv_play = play;
		}
		String localPath = Constant.CHATAUDIO_DIR + message.getmsgIdFile();
		if (!FileUtils.isFileExit(localPath)) {// 文件不存在
			ToastUtil.ShowShort(context, "文件不存在！");
			return;
		} else {
			startPlayRecord(localPath, true);
		}

	}

	/**
	 * 播放语音
	 * 
	 * @Title: 李庆义
	 * @Description: TODO
	 * @param @param filePath
	 * @param @param isUseSpeaker
	 * @return void
	 * @throws
	 */

	@SuppressWarnings("resource")
	private void startPlayRecord(String filePath, boolean isUseSpeaker) {
		if (!(new File(filePath).exists())) {
			return;
		}
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = new MediaPlayer();
		if (isUseSpeaker) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		}
		try {
			mediaPlayer.reset();
			// 单独使用此方法会报错播放错误:setDataSourceFD failed.: status=0x80000000
			// mediaPlayer.setDataSource(filePath);
			// 因此采用此方式会避免这种错误
			FileInputStream fis = new FileInputStream(new File(filePath));
			mediaPlayer.setDataSource(fis.getFD());
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					isPlaying = true;
					arg0.start();
					QiXinBaseDao.updateMsgPaly(message.getId(),"Y");
					startRecordAnimation();
				}
			});
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							stopPlayRecord();
						}

					});

		} catch (Exception e) {

		}
	}

	/**
	 * 停止播放
	 * 
	 * @Title: stopPlayRecord
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void stopPlayRecord() {
		if (isPlaying) {
			stopRecordAnimation();
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			isPlaying = false;
		}
	}

	/**
	 * 开启播放动画
	 * 
	 * @Title: 李庆义
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void startRecordAnimation() {
		if (isFrom) {
			iv_voice.setImageResource(R.anim.audioplayframe_left);
			if (iv_play != null){
			iv_play.setVisibility(View.GONE);
			}
		} else {
			iv_voice.setImageResource(R.anim.audioplayframe_right);
		}
		anim = (AnimationDrawable) iv_voice.getDrawable();
		anim.start();
	}

	/**
	 * 停止播放动画
	 * 
	 * @Title: 李庆义
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void stopRecordAnimation() {

		if (isFrom) {
			iv_voice.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			iv_voice.setImageResource(R.drawable.chatto_voice_playing);
		}
		if (anim != null) {
			anim.stop();
		}
	}

	// public void Play() {
	// if (isPlaying) {
	// stopPlayRecord();
	// if (currentMsg != null
	// && currentMsg.hashCode() == message.hashCode()) {
	// currentMsg = null;
	// return;
	// }
	// }
	//
	// String localPath = Constant.CHATAUDIO_DIR + message.getmsgIdFile();
	// if (!FileUtils.isFileExit(localPath)) {// 文件不存在
	// ToastUtil.ShowShort(context, "文件不存在！");
	// return;
	// } else {
	// startPlayRecord(localPath, true);
	// }
	//
	// }

}

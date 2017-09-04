package com.hjnerp.util;

import java.io.FileDescriptor;
import java.util.Timer;
import java.util.TimerTask;

import com.hjnerp.common.Settings;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

/**
 * 媒体播放录制辅助器类(不包括AudioTrack,AudioRecord和AudioManager的辅助)
 * @author John Kenrinus Lee 
 * @date 2014年6月10日
 */
public class MediaUtils
{
	private static final String TAG = "MediaUtils";
	private static final String DEFAULT_DIDI_PATH = "shake_match.mp3";
	
	private static volatile boolean isSleeped = false;
	private static final long sleepMillsec = 1000;
	private static final Timer timer = new Timer("MediaSleep-Timer");
	
	private static MediaPlayer mp;
	private static SoundPool sp;
	private static MediaRecorder mr;
	
	/**
	 * 震动
	 * @param context
	 */
	public static final void dongdong(Context context)
	{
		Settings settings = Settings.getSharedInstance();
		if(settings.isNewMessageNotify() && settings.isNewMessageVibrate())
		{
			Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			if(vibrator != null)
				vibrator.vibrate(new long[]{75,300,75,150}, -1);
		}
	}
	
	/**
	 * 是否获得了音频流焦点并且处在可播放音频的情景模式中
	 * @param context
	 * @return
	 */
	public static final boolean canPlayAudio(Context context)
	{
		AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		boolean normalMode = am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
		int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		boolean focused = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
		Settings settings = Settings.getSharedInstance();
		boolean settingsAllowed = settings.isNewMessageNotify()&&settings.isNewMessageMusic();
		return normalMode && focused && settingsAllowed;
	}
	
	/**
	 * 播放提示音
	 * @param context
	 * @param path 提示音文件位置,类型可以是AssetFileDescriptor或者String,其他情况包括null值,都播放默认提示音
	 * @throws Exception
	 */
	public static final void didi(final Context context, Object path) throws Exception
	{
		if(canPlayAudio(context) && !isSleeped)
		{
			sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			sp.setOnLoadCompleteListener(new OnLoadCompleteListener()
			{
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
				{
					if(status != 0)
						return;
					AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
					final float currentVolume = ((float)am.getStreamVolume(AudioManager.STREAM_MUSIC)) /
												((float)am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
						if(soundPool.play(sampleId, currentVolume, currentVolume, 1, 0, 1.0f) == 0)
							Log.e(TAG, "Failed to start sound");
						sleep();
				}
			});
			if(path instanceof AssetFileDescriptor)
			{
				AssetFileDescriptor afd = (AssetFileDescriptor)path;
				sp.load(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength(), 1);
				afd.close();
			}
			else if(path instanceof String)
			{
				sp.load((String)path, 1);
			}
			else
			{
				AssetFileDescriptor afd = context.getAssets().openFd(DEFAULT_DIDI_PATH);
				sp.load(afd, 1);
				afd.close();
			}
		}
	}
	
	/**
	 * 停止提示音,释放资源
	 */
	public static final void undidi()
	{
		if(sp != null)
		{
			sp.release();
			sp = null;
		}
	}
	
	/**
	 * 播放音频
	 * @param context
	 * @param path 音频文件位置,不可以为null,类型可以是AssetFileDescriptor或String或Uri
	 * @throws Exception
	 */
	public static final void play(Context context, Object path) throws Exception
	{
		if(canPlayAudio(context))
		{
			mp = new MediaPlayer();
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setVolume(0.6f, 0.6f);
			mp.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
			if(path instanceof AssetFileDescriptor)
			{
				AssetFileDescriptor afd = (AssetFileDescriptor)path;
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				afd.close();
			}
			else if(path instanceof String)
			{
				mp.setDataSource((String)path);
			}
			else if(path instanceof Uri)
			{
				mp.setDataSource(context, (Uri)path);
			}
			else
			{
				throw new IllegalArgumentException("the type of \"path\" is illegal");
			}
			mp.setOnPreparedListener(new OnPreparedListener()
			{
				@Override
				public void onPrepared(MediaPlayer mp)
				{
					mp.seekTo(0);
					mp.start();
				}
			});
			mp.prepareAsync();
			mp.setOnCompletionListener(new OnCompletionListener()
			{
				@Override
				public void onCompletion(MediaPlayer mp)
				{
					stop();
				}
			});
		}
	}
	
	/**
	 * 停止播放音频并释放资源
	 */
	public static final void stop()
	{
		if(mp != null)
		{
			mp.release();
			mp = null;
		}
	}
	
	/**
	 * 录制音频
	 * @param context
	 * @param path 录制后音频文件的输出路径,类型可以是String或者FileDescriptor,不能为null
	 * @throws Exception
	 */
	public static final void record(Context context, Object path) throws Exception
	{
		mr = new MediaRecorder();
		mr.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION/*MediaRecorder.AudioSource.MIC*/);
		mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		if(path instanceof String)
		{
			mr.setOutputFile((String)path);
		}
		else if(path instanceof FileDescriptor)
		{
			mr.setOutputFile((FileDescriptor)path);
		}
		else
		{
			throw new IllegalArgumentException("the type of \"path\" is illegal");
		}
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC/*MediaRecorder.AudioEncoder.AMR_NB*/);
		mr.setMaxDuration(60000);
		mr.setMaxFileSize(1024*1024*2);
		mr.prepare();
		mr.start();
	}
	
	/**
	 * 停止录制音频并释放资源
	 * @param context
	 */
	public static final void unrecord(Context context)
	{
		if(mr != null)
		{
			mr.stop();
			mr.release();
			mr = null;
		}
	}
	
	private static final synchronized void sleep()
	{
		if(isSleeped) return;
		isSleeped = true;
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				isSleeped = false;
			}
		}, sleepMillsec);
	}
}

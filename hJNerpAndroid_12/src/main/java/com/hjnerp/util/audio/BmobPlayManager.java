package com.hjnerp.util.audio;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class BmobPlayManager implements MediaPlayer.OnCompletionListener,
		PlayControlListener {

	private MediaPlayer A;
	private Context context;
	private static volatile BmobPlayManager E;
	private static Object INSTANCE_LOCK = new Object();
	private boolean G = false;
	private OnPlayChangeListener H;

	public static BmobPlayManager getInstance(Context paramContext) {
		if (E == null)
			synchronized (INSTANCE_LOCK) {
				if (E == null)
					E = new BmobPlayManager();
				E.init(paramContext);
			}
		return E;
	}

	public void init(Context paramContext) {
		this.context = paramContext;
		// BmobUserManager.getInstance(paramContext);
	}

	@Override
	public void playRecording(String paramString, boolean paramBoolean) {
		// TODO Auto-generated method stub

		if (!(new File(paramString)).exists())
			return;
		if (this.A == null) {
			this.A = new MediaPlayer();
			this.A.setOnErrorListener(new PlayerErrorListener(this));
		} else {
			this.A.stop();
			this.A.reset();
		}
		AudioManager audioManager = (AudioManager) this.context
				.getSystemService(Context.AUDIO_SERVICE);
		if (paramBoolean) {
			((AudioManager) audioManager).setMode(0);
			((AudioManager) audioManager).setSpeakerphoneOn(true);
			this.A.setAudioStreamType(2);
		} else {
			((AudioManager) audioManager).setSpeakerphoneOn(false);
			((AudioManager) audioManager).setMode(2);
			this.A.setAudioStreamType(0);
		}
		try {
			this.A.setDataSource(paramString);
			this.A.prepare();
			this.A.setOnCompletionListener(this);
			this.A.seekTo(0);
			this.A.start();
			this.G = true;
			if (this.H != null) {
				this.H.onPlayStart();
				return;
			}
		} catch (IOException localIOException) {
			this.A.release();
			this.A = null;
		}
	}

	@Override
	public void stopPlayback() {
		// TODO Auto-generated method stub
		if (this.A != null)
			onStop();
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return this.G;
	}

	@Override
	public int getPlaybackDuration() {
		// TODO Auto-generated method stub
		int i = 0;
		if ((this.A != null) && (this.A.isPlaying()))
			i = this.A.getDuration();
		return i;
	}

	@Override
	public MediaPlayer getMediaPlayer() {
		// TODO Auto-generated method stub
		return this.A;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		onStop();
	}

	private void onStop() {
		if (this.H != null)
			this.H.onPlayStop();
		this.A.stop();
		this.A.release();
		this.A = null;
		this.G = false;
	}
}

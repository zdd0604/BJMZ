package com.hjnerp.manager;

import com.hjnerpandroid.R;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

public class BeepManager {

	// 甯搁噺
	private final float BEEP_VOLUME = 0.3f;
	private final long VIBRATE_DURATION = 200L;

	// 鍙橀噺
	private boolean playBeep = false;
	private boolean vibrate = false;

	// 鎺у埗锟??
	private Context mContext;
	// private MediaPlayer mMediaPlayer;
	private int loadId1;
	private SoundPool mSoundPool;
	private Vibrator mVibrator;

	public BeepManager(Context context, boolean playBeep, boolean vibrate) {
		super();
		this.mContext = context;
		this.playBeep = playBeep;
		this.vibrate = vibrate;

		initial();
	}

	public boolean isPlayBeep() {
		return playBeep;
	}

	public void setPlayBeep(boolean playBeep) {
		this.playBeep = playBeep;
	}

	public boolean isVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

	private void initial() {
		if (null == mSoundPool) {
			mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		}
		loadId1 = mSoundPool.load(mContext, R.raw.beep, 1);

		// initialVibrator
		mVibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public void play() {
		// playMusic
		// if (playBeep && !km.inKeyguardRestrictedInputMode()) {
		// mMediaPlayer.start();
		// }
		if (playBeep) {
			// mMediaPlayer.start();
			// 鍙傛暟1锛氭挱鏀剧壒鏁堝姞杞藉悗鐨処D锟??
			// 鍙傛暟2锛氬乏澹伴亾闊抽噺澶у皬(range = 0.0 to 1.0)
			// 鍙傛暟3锛氬彸澹伴亾闊抽噺澶у皬(range = 0.0 to 1.0)
			// 鍙傛暟4锛氱壒鏁堥煶涔愭挱鏀剧殑浼樺厛绾э紝鍥犱负鍙互鍚屾椂鎾斁澶氫釜鐗规晥闊充箰
			// 鍙傛暟5锛氭槸鍚﹀惊鐜挱鏀撅紝0鍙挱鏀句竴锟??0 = no loop, -1 = loop forever)
			// 鍙傛暟6锛氱壒鏁堥煶涔愭挱鏀剧殑閫熷害锟??F涓烘甯告挱鏀撅紝鑼冨洿 0.5 锟??2.0
			mSoundPool.play(loadId1, 0.5f, 0.5f, 1, 0, 1f);
		}
		// vibrate
		if (vibrate) {
			mVibrator.vibrate(VIBRATE_DURATION);
		}

	}

}

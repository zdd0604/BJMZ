package com.hjnerp.manager;

import java.util.HashMap; 
import com.hjnerpandroid.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPoolPlay { 
	private static SoundPoolPlay hjSoundPoolPlay;

	@SuppressLint("UseSparseArrays")
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

	private SoundPoolPlay() {
	}

	SoundPool mSoundPool = null;

	public static SoundPoolPlay getInstance(Context context) {
		if (hjSoundPoolPlay == null) {
			hjSoundPoolPlay = new SoundPoolPlay();
			hjSoundPoolPlay.init( context);
			
		}

		return hjSoundPoolPlay;
	}

	public void init(Context paramContext) {
		if (mSoundPool == null) {
			mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
			soundMap.put(1, mSoundPool.load(paramContext, R.raw.smsreceived1, 1));
			soundMap.put(2, mSoundPool.load(paramContext, R.raw.smsreceived1, 1));
			soundMap.put(3, mSoundPool.load(paramContext, R.raw.smsreceived1, 1));

		}
	}
	
	public void playSound(int  indexSound) {
		if (mSoundPool!=null && indexSound >=0 && indexSound <3  ) { 
			   mSoundPool.play(soundMap.get(indexSound), 1, 1, 0, 0, 1);
		}
		
	}
}

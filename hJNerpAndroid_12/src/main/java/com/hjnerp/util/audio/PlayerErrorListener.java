package com.hjnerp.util.audio;

import android.media.MediaPlayer;

public class PlayerErrorListener implements MediaPlayer.OnErrorListener {

	public PlayerErrorListener(BmobPlayManager paramBmobPlayManager) {
	}

	@Override
	public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1,
 		int paramInt2) {
//		switch (paramInt1) {
//		case 1:
//			paramMediaPlayer = "MEDIA_ERROR_UNKNOWN";
//			break;
//		case 100:
//			paramMediaPlayer = "MEDIA_ERROR_SERVER_DIED";
//			break;
//		default:
//			paramMediaPlayer = Integer.toString(paramInt1);
//		}
		return false;
	}

}

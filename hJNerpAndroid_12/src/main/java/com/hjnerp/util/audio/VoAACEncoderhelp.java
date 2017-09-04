package com.hjnerp.util.audio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import com.hjnerp.common.Constant;
import com.sinaapp.bashell.VoAACEncoder;

public class VoAACEncoderhelp {

	private AudioRecord recordInstance;
	private boolean isStartRecord;
	private FileOutputStream fos;
	private int SAMPLERATE = 8000;
	private VoAACEncoder vo;
	public void startRecord(String mRecordFileName) {
		
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		
		try {
			fos = new FileOutputStream(Constant.CHATAUDIO_DIR+ mRecordFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				  vo = new VoAACEncoder();
				vo.Init(SAMPLERATE, 16000, (short) 1, (short) 1);// 采样率:16000,bitRate:32k,声道数:1，编码:0.raw
				// 1.ADTS
				int min = AudioRecord.getMinBufferSize(SAMPLERATE,
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				if (min < 2048) {
					min = 2048;
				}
				byte[] tempBuffer = new byte[2048];
				recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC,
						SAMPLERATE, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT, min);
				recordInstance.startRecording();
				isStartRecord = true;
				while (isStartRecord) {
					int bufferRead = recordInstance.read(tempBuffer, 0, 2048);
					byte[] ret = vo.Enc(tempBuffer);
					if (bufferRead > 0) {
					 
						try {
							fos.write(ret);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
			
		}).start();
	}
	
	public void stopRecord() {
		isStartRecord = false;
		recordInstance.stop();
		recordInstance.release();
		recordInstance = null; 
	 
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
}

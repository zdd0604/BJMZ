package com.hjnerp.util.audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.hjnerp.common.Constant;
import com.sinaapp.bashell.VoAACEncoder;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler; 
import android.util.Log;

public class BmobRecordManager implements RecordControlListener {

	// 音频获取源
	private int audioSource = MediaRecorder.AudioSource.MIC;
	// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	private static int sampleRateInHz = 44100;
	// 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
	private static int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	// 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

	private AudioRecord recordInstance;
	private VoAACEncoder vo;
	private int SAMPLERATE = 16000;
	public static int MAX_RECORD_TIME = 60;
	public static int MIN_RECORD_TIME = 1;
	public long startTime;
	private static volatile BmobRecordManager N;
	private static Object INSTANCE_LOCK = new Object();
	OnRecordChangeListener O;
	final Handler R = new Handler(new RecordMeter(this));
	private boolean isRecord = false;// 设置正在录制的状态
	private String recordFileName = "";
	private ExecutorService Q;
	Object mLock;
	int recBufSize=0;
	 int readMean = 0 ;

	public static BmobRecordManager getInstance(Context paramContext) {
		if (N == null)
			synchronized (INSTANCE_LOCK) {
				if (N == null)
					N = new BmobRecordManager();
				N.init(paramContext);
			}
		return N;
	}

	public void init(Context paramContext) {
		this.Q = Executors.newCachedThreadPool();
		mLock = new Object();
	}

	public void setOnRecordChangeListener(
			OnRecordChangeListener paramOnRecordChangeListener) {
		this.O = paramOnRecordChangeListener;
	}

	@Override
	public void startRecording(String paramString) {
		// TODO Auto-generated method stub
		readMean = 0 ;
		recordFileName = paramString;
		vo = new VoAACEncoder();
		vo.Init(sampleRateInHz, 44100, (short) 1, (short) 1);// 采样率:16000,bitRate:32k,声道数:1，编码:0.raw
		// 1.ADTS
		recBufSize = AudioRecord.getMinBufferSize(sampleRateInHz,
				channelConfig, audioFormat);
		if (recBufSize < 2048) {
			recBufSize = 2048;
		} 
		recordInstance = new AudioRecord(audioSource,
				sampleRateInHz, channelConfig,
				audioFormat,recBufSize);
		recordInstance.startRecording();
		isRecord = true;
		// 开启音频文件写入线程
		new Thread(new AudioRecordThread()).start();
		this.startTime = new Date().getTime();
		this.Q.execute(new What(this));

	}

	@Override
	public void cancelRecording() {
		// TODO Auto-generated method stub

	}

	@Override
	public int stopRecording() {
		// TODO Auto-generated method stub
		if (isRecord) {
			isRecord = false;// 停止文件写入
			recordInstance.stop();
			recordInstance.release();
			recordInstance = null;
			vo.Uninit();
			return (int) (new Date().getTime() - this.startTime) / 1000;
		}
		return 0;
	}

	@Override
	public boolean isRecording() {
		// TODO Auto-generated method stub
		return isRecord;
	}

	@Override
	public String getRecordFilePath(String paramString) {
		// TODO Auto-generated method stub
		return Constant.CHATAUDIO_DIR + recordFileName;
	}

	public String getRecordFileName() {
		return recordFileName;
	}

	public String getRecordFilePath() {
		return Constant.CHATAUDIO_DIR + recordFileName;
	}

	public void setRecordFileName(String fileName) {
		recordFileName = fileName;
	}

	/**
	 * 这里将数据写入文件，但是并不能播放，因为AudioRecord获得的音频是原始的裸音频，
	 * 如果需要播放就必须加入一些格式或者编码的头信息。但是这样的好处就是你可以对音频的 裸数据进行处理，比如你要做一个爱说话的TOM
	 * 猫在这里就进行音频的处理，然后重新封装 所以说这样得到的音频比较容易做一些音频的处理。
	 */

	private void writeDateTOFile() {
		// new一个byte数组用来存一些字节数据，大小为缓冲区大小
		byte[] audiodata = new byte[recBufSize];
		FileOutputStream fos = null; 
		try {
			/**
			 * @author haijian
			 * 解决点击录音崩溃问题
			 * 崩溃原因：没有提前建立音频文件的文件夹，直接建立的音频文件
			 *          导致创建没有成功
			 * 解决办法，提前建立文件夹
			 */
			File files = new File(Constant.CHATAUDIO_DIR);
			if(!files.exists()){
				files.mkdirs();
			}
			File file = new File(getRecordFilePath());
			Log.i("info", "录音文件路径："+getRecordFilePath());
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);// 建立一个可存取字节的文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (isRecord == true) {
			int bufferRead = recordInstance.read(audiodata, 0, recBufSize);
			byte[] ret = vo.Enc(audiodata);
			if (bufferRead > 0) { 
				try {
					if(ret==null){
						Log.i("system.out", "录音文件为空！！");
						return;
					}
					fos.write(ret);
					
					  long v = 0;  
					  for (int i = 0; i < audiodata.length; i++) 
					  {
						  v += Math.abs(audiodata[i]);//取绝对值，因为可
//						   v += audiodata[i] * audiodata[i];  
					  }
					    readMean =(int) ( v / (double) bufferRead)/7;  
					 Log.i("dddddd","声音分贝 :" +readMean );
					
				} catch (IOException e) {
					e.printStackTrace();
					com.hjnerp.util.Log.i("writeDateTOFile异常："+e.toString());
				}
			 
			}
		}
		try {
			fos.close();// 关闭写入流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class AudioRecordThread implements Runnable {
		@Override
		public void run() {
			writeDateTOFile();// 往文件中写入裸数据
		}
	}
	public int getMaxAmplitude()
	{
		return readMean;
	}
}

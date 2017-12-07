package com.mznerp.business.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.jiebao.barcode.BarcodeManager;
import android.jiebao.barcode.BarcodeManager.Callback;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mznerp.common.EapApplication;
import com.mznerp.manager.BeepManager;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.IQ;
import com.mznerp.service.WebSocketService;
import com.mznerp.util.myscom.FileUtils;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.LOG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class BussinessHtmlActivity extends CordovaActivity {
	// private WebView wb;
	private String htmlFileName;
	private String htmlContent;
	private File index_file;
	private BarcodeManager barcodeManager;
	private BeepManager beepManager;
	private long nowTime = 0;
	private long lastTime = 0;
	private byte[] codeBuffer;
	private String codeId;

	private Boolean isSan =  false;
	public static final int START_SCAN = 1;
	public static final int END_SCAN = 2;
	public static final int SHOWTIME = 1000*10;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_SCAN:
//				initF4Key();
//				isSan =  true;
//				nowTime = System.currentTimeMillis();
//				Log.i("info", "START_SCAN");
//				barcodeManager.Barcode_Stop();
//				// 按键时间不低于200ms
//				if (nowTime - lastTime > 200) {
//					System.out.println("scan(0)");
//					if (null != barcodeManager) {
//						barcodeManager.Barcode_Start();
//					}
//					lastTime = nowTime;
//				}
				break;
			case 5:
				Toast.makeText(BussinessHtmlActivity.this, "执行了吗？？", Toast.LENGTH_LONG).show();
				break;
			case 6:
				Toast.makeText(BussinessHtmlActivity.this, "读数成功", Toast.LENGTH_LONG).show();
				break;
			case END_SCAN:
				// 获取扫码结果
				if (null != codeBuffer) {
					String code = new String(codeBuffer);
					Toast.makeText(BussinessHtmlActivity.this, code, Toast.LENGTH_LONG).show();
					beepManager.play();
					appView.loadUrlIntoView("javascript:f4qrcode(" + code + ")",false);
					//loadUrl("javascript:f4qrcode(" + code + ")");
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setupView();
		super.loadUrl("file:///"
				+ EapApplication.getApplication().getFilesDir() + "/index.html");//
		
//		 super.loadUrl("file:///android_asset/www/dsaordhtml.html");
	}

	private void setupView() {

		htmlFileName = getIntent().getStringExtra("id_model");
		try {
			// htmlContent = praseXMLModel(htmlFileName);
			// htmlContent = htmlContent.replaceAll("Framework7/",
			// "file:///android_asset/www/Framework7/");
			index_file = new File(EapApplication.getApplication().getFilesDir()
					+ "/index.html");
			if (index_file.exists()) {// 如果文件存在就删除
				index_file.delete();
			}
			index_file.createNewFile();
			/**
			 * @author haijian 按行读文件 然后替换
			 */
			String read;
			String read2;
			FileReader fileread = new FileReader(new File(EapApplication
					.getApplication().getFilesDir()
					+ "/"
					+ htmlFileName
					+ ".xml"));
			BufferedReader bufread = new BufferedReader(fileread);
			bufread = new BufferedReader(fileread);
			FileWriter newFile = new FileWriter(index_file);
			BufferedWriter bw = new BufferedWriter(newFile);
			while ((read = bufread.readLine()) != null) {
				read2 = read.replace("Framework7/",
						"file:///android_asset/www/Framework7/");

				System.out.println(read2);
				bw.write(read2);
				bw.newLine();
				bw.flush();

				// FileOutputStream fout = new FileOutputStream(index_file);
				// fout.write(htmlContent.getBytes());
				// fout.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.i("info", "读写文件异常" + e.toString());
		}

	}

	MyReciever reciever;

	private void initF4Key() {
		if (barcodeManager == null) {
			barcodeManager = BarcodeManager.getInstance();
		}
		barcodeManager.Barcode_Open(this, dataReceived);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		beepManager = new BeepManager(this, true, false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		reciever = new MyReciever();
		IntentFilter filter = new IntentFilter();
		filter.addAction("backhome");
		filter.addAction("com.jb.action.F4key");
		filter.addAction("startF4key");
		filter.addAction(WebSocketService.ACTION_ON_IQ);
		registerReceiver(reciever, filter);
		if (isSan) 
		{
			initF4Key();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(reciever);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (null != barcodeManager) {
			barcodeManager.Barcode_Close();
			barcodeManager.Barcode_Stop();
		}
		super.onStop();
	}
	
	Callback dataReceived = new Callback() {

		@Override
		public void Barcode_Read(byte[] buffer, String codeId, int errorCode) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(6);
			if (null != buffer) {
				codeBuffer = buffer;
				BussinessHtmlActivity.this.codeId = codeId;
				handler.sendEmptyMessage(END_SCAN);
				barcodeManager.Barcode_Stop();
				
			}
		}
	};

	class MyReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if ("backhome".equals(action)) {
				finish();
			}
			if (WebSocketService.ACTION_ON_IQ.equals(action)) {
				IQ iq = (IQ) intent.getSerializableExtra("data");
				Map<String, Object> dataMap = (Map<String, Object>) iq.data;

				if (ChatConstants.iq.FEATURE_CLINET_OFFLINE.equals(iq.feature)) {// 被踢下线
					// isForcedExit("您的账号在另一台设备登陆！");
				}
				if (ChatConstants.iq.FEATURE_OP_GROUP_CHAT.equals(iq.feature)) {
					String type = (String) dataMap
							.get(ChatConstants.iq.DATA_KEY_TYPE);
					if (ChatConstants.iq.DATA_VALUE_ADD.equals(type)
							|| ChatConstants.iq.DATA_VALUE_EXIT.equals(type)
							|| ChatConstants.iq.DATA_VALUE_REMOVE.equals(type)) {// 有成员退出

						// refalshContent("");
					}

				}

			}
			/**
			 * @author haijian 开始扫码
			 */
			if ("startF4key".equals(action)) {
				handler.sendEmptyMessage(START_SCAN);
			}
			/**
			 * @author haijian 增加扫码
			 */
			if (intent.hasExtra("F4key")) {
				if (intent.getStringExtra("F4key").equals("down")) {
					Log.e("trig", "key down");
//					handler.sendEmptyMessage(5);
//					 isContines = true;
					if (null != barcodeManager) {
						nowTime = System.currentTimeMillis();

						if (nowTime - lastTime > 200) {
							barcodeManager.Barcode_Stop();
							lastTime = nowTime;
							if (null != barcodeManager) {
								barcodeManager.Barcode_Start();
							}
						}
					}
				} else if (intent.getStringExtra("F4key").equals("up")) {
					Log.e("trig", "key up");
				}
			}
		}

	}

	private String praseXMLModel(String xmlName) throws IOException {
		// String praseResult = "";
		StringBuilder buffer = new StringBuilder();
		InputStream fileInputStream = FileUtils.openInputStream(new File(
				EapApplication.getApplication().getFilesDir() + "/" + xmlName
						+ ".xml"));
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				fileInputStream, "UTF-8"));
		String str;

		while ((str = bufferReader.readLine()) != null) {
			buffer.append(str);
		}
		fileInputStream.close();

		return buffer.toString();
		// return praseResult;
	}
}

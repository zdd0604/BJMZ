package com.hjnerp.widget;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

//@SuppressLint("NewApi") 
public class HJQrcode extends LinearLayout implements HJViewInterface,
		Callback {
 
	private WidgetClass items;
	private Context context;
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	private BusinessParam businessParam;

    private String Data;
 
    private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
    private Button cancelScan ;
    private Button cancelIput ;
	public HJQrcode(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HJQrcode(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJQrcode(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,
			BusinessParam param) {
		super(context);

		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;
		initView();

	}

	private void initView() {

		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjqrcode, null);

		WindowManager windowManager = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		int height = windowManager.getDefaultDisplay().getHeight();

		 
		
		int statusBarHeight = StringUtil.getStatusHeight((Activity) getContext());// 手机状态栏高度
		int actionbar_height = (int) getResources().getDimension(
				R.dimen.abc_action_bar_default_height) ;// 自定义actionbar高度
//		int actionbar_height  =  getResources().getInteger(R.integer.abc_action_bar_default_height);
		LayoutParams ll = new LayoutParams(LayoutParams.MATCH_PARENT, height
				- actionbar_height - statusBarHeight);
		view.setLayoutParams(ll);

		
		CameraManager.init(getContext());
		viewfinderView = (ViewfinderView) view.findViewById(R.id.viewfinder_view);
		cancelScan  = (Button) view.findViewById(R.id.btn_cancel_scan);
		cancelIput = (Button) view.findViewById(R.id.btn_cancel_input);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		
	 
		SurfaceView surfaceView = (SurfaceView) view.findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
		//quit the scan view
		cancelScan.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
			//	CaptureActivity.this.finish();
				
				if (  "开启闪光灯".equalsIgnoreCase((String) cancelScan.getText()  ))
				{
					CameraManager.get().openLight();
			        cancelScan.setText( "关闭闪光灯");
				}
				else
				{
					CameraManager.get().offLight();
					cancelScan.setText( "开启闪光灯");
				}
				
			}
		});
		
		cancelIput.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
			//	CaptureActivity.this.finish();
				final Dialog noticeDialog = new Dialog(context, R.style.noticeDialogStyle); 
				noticeDialog.setContentView(R.layout.dialog_notice_qrcode); 
				
				RelativeLayout dialog_confirm_rl;
				RelativeLayout dialog_cc_cancel_rl;
				final EditText  dialog_textview;
				
				dialog_textview = (EditText) noticeDialog
				.findViewById(R.id.dialog_edittext_qrcpde);
				dialog_confirm_rl = (RelativeLayout) noticeDialog
						.findViewById(R.id.dialog_qrcode_confirm_rl);
				dialog_cc_cancel_rl = (RelativeLayout) noticeDialog
						.findViewById(R.id.dialog_qrcode_cancel_rl);
				
				dialog_cc_cancel_rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						noticeDialog.dismiss();
					}
				});
				
				dialog_confirm_rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String resultString = dialog_textview.getText().toString().trim();
						noticeDialog.dismiss();
						//FIXME
						if (!resultString.equals("") && resultString != null)  
						{
							Data = resultString;
							if (!StringUtil.isNullOrEmpty(items.attribute.nextview)) {
								((com.hjnerp.business.activity.BusinessActivity) context)
										.setNextView(items.attribute.nextview, "", "",
												"");
							}
						}
					}
				});
				
				noticeDialog.show();
			}
		});
		
		addView(view);
	}

	 
	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return Data;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return Data;
	}

	@Override
	public void setJesonValue(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return items.attribute.datasource;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return items.id;
	}

	@Override
	public boolean getEditable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataBuild( Boolean flag ,
			BusinessData ctlm1345List) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setLocation() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String setPhoto() {
		// TODO Auto-generated method stub

		return "";
	}

	@Override
	public int saveData(Boolean required) {
		// TODO Auto-generated method stub
		return 0;
	}
 

	@Override
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub

	}

	 
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (!hasSurface) {
		 
			hasSurface = true;
			initCamera(holder);
			initinactivityTimer();
		}
		
	}

	public void initinactivityTimer()
	{
		inactivityTimer = new InactivityTimer(this);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		hasSurface = false;
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		
		inactivityTimer.shutdown();
	}
	
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText().trim();
		//FIXME
		if (!resultString.equals(""))  
		{
			Data = resultString;
			if (!StringUtil.isNullOrEmpty(items.attribute.nextview)) {
				((com.hjnerp.business.activity.BusinessActivity) context)
						.setNextView(items.attribute.nextview, "", "",
								"");
			}
		}
 
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}
	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}
	
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			((Activity) getContext()).setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}
	
	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
}

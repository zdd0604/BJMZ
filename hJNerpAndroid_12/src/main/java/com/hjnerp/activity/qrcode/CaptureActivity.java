package com.hjnerp.activity.qrcode;
 
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner; 
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import android.content.Intent;
import android.content.pm.ActivityInfo; 
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size; 
import android.os.Bundle;
import android.os.Handler; 
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hjnerp.common.ActivitySupport;
import com.hjnerpandroid.R;
 
   
public class CaptureActivity  extends ActivitySupport implements SurfaceHolder.Callback  
 
{ 
	private static final long VIBRATE_DURATION = 200L;
 
    @SuppressWarnings("deprecation")
	private Camera mCamera;
	private SurfaceHolder mHolder;
	private SurfaceView surface_view;
	private ImageScanner scanner;
	private Handler autoFocusHandler; 

	private Button openLight;

	private Boolean status = false;
	
    static {
        System.loadLibrary("iconv");
    } 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar = getSupportActionBar(); 
		mActionBar.setDisplayHomeAsUpEnabled(true);
  		  setContentView(R.layout.activity_qrcode);
  		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  		mActionBar.setTitle("扫一扫");
  		  init();
  		// 初始化声音 
	} 
	
	private void init() {
		surface_view = (SurfaceView) findViewById(R.id.qrcodesurface_view);
		mHolder = surface_view.getHolder();
		mHolder.addCallback(this);
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);
		autoFocusHandler = new Handler(); 
		openLight = (Button) findViewById(R.id.capture_flashin);
		openLight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!status) {
					openLight.setText("关闭闪光灯");
					status = true;
					@SuppressWarnings("deprecation")
					Parameters params = mCamera.getParameters();
					params.setFlashMode(Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(params);
					// mCamera.startPreview(); // 开始亮灯

				} else {
					status = false;
					openLight.setText("开启闪光灯");
					Parameters parameter = mCamera.getParameters(); 
					parameter.setFlashMode(Parameters.FLASH_MODE_OFF); 
					mCamera.setParameters(parameter);
				}
			}
		});
	}

	public void onPause() {
        super.onPause();
        releaseCamera();
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (mHolder.getSurface() == null) {
			return;
		}
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
		}
		try {
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(previewCb);
			mCamera.startPreview(); 
			mCamera.autoFocus(autoFocusCallback);
		} catch (Exception e) {
		 
		}
	}

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                if (result != 0) { 
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {  
                    		  playBeepSoundAndVibrate();
                          	 Intent resultintent =new Intent();  
                        	 resultintent.putExtra("result",  sym.getData());     
                             //请求代码可以自己设置，这里设置成20   
                             setResult(1001, resultintent);  
                             //关闭掉这个Activity   
                             finish();   
                    }
                }
            }
      };

	  /** A safe way to get an instance of the Camera object. */
	    public   Camera getCameraInstance(){
	        Camera c = null;
	        try {
	            c = Camera.open();
	        } catch (Exception e){
	        }
	        return c;
	    } 
	    
	    private void releaseCamera() {
	        if (mCamera != null) {  
	            mCamera.setPreviewCallback(null);   
	            mCamera.release();
	            mCamera = null;
	        }
	    }

	    @Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera = Camera.open();
			} catch (Exception e) {
				mCamera = null;
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (mCamera != null) {
				mCamera.setPreviewCallback(null);
				mCamera.release();
				mCamera = null;
			}
		}


	    /**
		 * 自动对焦回调
		 */
		AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
			public void onAutoFocus(boolean success, Camera camera) {
				autoFocusHandler.postDelayed(doAutoFocus, 1500);
			}
		};

		//自动对焦
		private Runnable doAutoFocus = new Runnable() {
			public void run() {
				if (null == mCamera || null == autoFocusCallback) {
					return;
				}
				mCamera.autoFocus(autoFocusCallback);
			}
		};
		 
		/**
		 * 播放声音和震动
		 */
		private void playBeepSoundAndVibrate() {
			// 打开震动
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}

	 
}
	 
 
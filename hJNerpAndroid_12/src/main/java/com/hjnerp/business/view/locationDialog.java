package com.hjnerp.business.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hjnerp.business.activity.BusinessActivity;
import com.hjnerp.common.EapApplication;
import com.hjnerpandroid.R;

/*
 *  定位业务逻辑
 * */
public class locationDialog extends Dialog {
	String dialogResult;
	Handler mHandler;
	String billNo = "";
	BusinessActivity aActivity;
	private static String dBLocation = "";
	public static int SCANNER_TIME = 5000; // 扫描周期
	public static String BAIDU_COORTYPE = "bd09ll";// 百度经纬度

	public static Boolean islocation = false;
	public static Boolean isFist = false;
	private static LocationClient mClient;
	private static LocationClientOption opts = new LocationClientOption();
	private static MyHandler myHandler;
	private static GeoCoder mSearch;

	private TextView tv_waitdialog_text;
	public static final int LOCATION_FAILED = 3;
	public static final int LOCATION_SUCCESS = 4;

	private static BDLocation location;

	private static LocationThread timerThread;
	private static Thread timeOutThread;

	public locationDialog(BusinessActivity bcontext, String string) {

		super(bcontext, R.style.noticeDialogStyle);
		aActivity = bcontext;
		setOwnerActivity(bcontext);
		onCreate();

	}

	public String getDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(String dialogResult) {
		this.dialogResult = dialogResult;
	}

	/** Called when the activity is first created. */

	public void onCreate() {
		setContentView(R.layout.dialog_wait_withtext2);
		tv_waitdialog_text = (TextView) findViewById(R.id.tv_waitdialog2_notice);
		tv_waitdialog_text.setText("定位中...");
		myHandler = new MyHandler();
		getLocation(aActivity);
	}

	public void endDialog(String result) {
		timerThread = null;

		dismiss();
		setDialogResult(result);
		dBLocation = result;
		Message m = mHandler.obtainMessage();
		m.obj = result;
		mHandler.sendMessage(m);
	}

	public String showDialog() {
		islocation = false;
		isFist = false;
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message mesg) {
				// process incoming messages here
				// super.handleMessage(msg);
				throw new RuntimeException();
			}
		};
		super.show();
		try {
			Looper.getMainLooper().loop();
		} catch (RuntimeException e2) {
		}
		return dBLocation;
	}

	class MyHandler extends Handler {
		public MyHandler() {
		}

		public MyHandler(Looper L) {
			super(L);
		}

		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String dLocation = msg.obj.toString();
			endDialog(dLocation);

		}
	}

	public static void getLocation(Context context) {
		mClient = new LocationClient(context);
		opts.setLocationMode(LocationMode.Hight_Accuracy);
		opts.setOpenGps(true);
		opts.setCoorType(BAIDU_COORTYPE);
		opts.setScanSpan(SCANNER_TIME);
		opts.setIsNeedAddress(true);
		opts.setNeedDeviceDirect(true);
		mClient.setLocOption(opts);

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(
							EapApplication.getApplication().getBaseContext(),
							"抱歉，未能找到结果", Toast.LENGTH_LONG).show();
					return;
				}
				islocation = true;
				if (timerThread == null)
					return;
				String peoName = "";
				if (result.getPoiList() != null
						&& result.getPoiList().size() != 0) {

					peoName = result.getPoiList().get(0).name;
				}
				String province = result.getAddressDetail().province;
				String city = result.getAddressDetail().city;
				String district = result.getAddressDetail().district;
				String street = result.getAddressDetail().street;
				String streetNumber = result.getAddressDetail().streetNumber;
				String address = result.getAddress();
				Message msg = myHandler.obtainMessage(LOCATION_SUCCESS);
				msg.obj = location.getLatitude() + ":"
						+ location.getLongitude() + ":" + address + ":"
						+ peoName;

				dBLocation = msg.obj.toString();
				msg.what = LOCATION_SUCCESS;
				myHandler.sendMessage(msg);

			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {

			}
		});

		mClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				String dlocation = "";
				if (timerThread == null) {
					mClient.unRegisterLocationListener(this);
					mClient.stop();
					return;
				}
				;
				if (location != null
						&& !"4.9E-324".equalsIgnoreCase(location.getLatitude()
								+ "")) {
					dlocation = location.getLatitude() + ":"
							+ location.getLongitude();
					LatLng ptCenter = new LatLng(location.getLatitude(),
							location.getLongitude());

					// 反Geo搜索
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
							.location(ptCenter));
				} else {
					return;
				}

				mClient.unRegisterLocationListener(this);
				mClient.stop();
				locationDialog.location = location;

				// Message msg = myHandler.obtainMessage(LOCATION_SUCCESS);
				// msg.obj = dlocation;
				// myHandler.sendMessage(msg);
			}

		});
		// 开始进行定位
		mClient.start();
		mClient.requestLocation();
		timerThread = new LocationThread(true);
		timeOutThread = new Thread(timerThread);
		timeOutThread.start();

	}

	private static class LocationThread implements Runnable {
		private long beginTime;
		private long endTime;
		private boolean flag;

		public LocationThread(boolean flag) {
			this.flag = flag;
			beginTime = System.currentTimeMillis();
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		@Override
		public void run() {
			try {
				endTime = System.currentTimeMillis();
				while (flag && endTime - beginTime < 30 * 1000) {
					// 更新对话框的界面

					endTime = System.currentTimeMillis();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (islocation)
				return;
			if (endTime - beginTime >= 30 * 1000) {
				Message msg = myHandler.obtainMessage(LOCATION_SUCCESS);
				msg.obj = "";
				dBLocation = msg.obj.toString();

				// mClient.unRegisterLocationListener(this);
				mClient.stop();
				if (timerThread != null)
					timerThread.setFlag(false);
				timerThread = null;

				msg.what = LOCATION_SUCCESS;
				myHandler.sendMessage(msg);
				// 超时了，就结束线程

			}

		}

	}
}
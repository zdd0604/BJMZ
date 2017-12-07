package com.mznerp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.mznerp.common.EapApplication;
import com.mznerp.model.BaseNear;
import com.mznerp.model.NearBuild;

public class BDLocationUtilII {

	public static final String TAG = "BDLocationUtil";
	public static final ExecutorService executors = Executors
			.newFixedThreadPool(3, new NameableThreadFactory(
					"BDLocation-Thread"));
	public static final int DIALOG_DISMISS = 1;
	public static final int DIALOG_REFRESH = 2;
	public static final int LOCATION_FAILED = 3;
	public static final int LOCATION_SUCCESS = 4;
	public static final int SCANNER_TIME = 5000; // 扫描周期
	public static final String BAIDU_COORTYPE = "bd09ll";// 百度经纬度
	// private static WaitDialog dialog;
	private static LocationClient mClient;
	private static LocationClientOption opts = new LocationClientOption();
	private static LocationThread locThread = null;

	private static Handler activityHandler;
	private static BDLocation location;

	static GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	public static int nointernet = -1;
	public static int wifi = 2;
	public static int gprs = 3;
	public static int state;

	private static Thread timeOutThread;
	private static Gson gson;
	private static BaseNear near = null;
	private static NearBuild bill;
	private static boolean isClose = false;

	private static final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOCATION_SUCCESS:// 获取省市区
				// dialog.setTextView("当前正在定位..."+msg.arg1);
				location = (BDLocation) msg.obj;
				LatLng ptCenter = new LatLng(location.getLatitude(),
						location.getLongitude());
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
				break;
			case DIALOG_DISMISS:
				// dialog.dismiss();
				// 停止定位
				mClient.stop();
				locThread = null;
				break;
			default:
				break;
			}
		};
	};

	public static void getLocation(Context context, final boolean isGeo) {
		mClient = new LocationClient(context);
		// dialog = new WaitDialog(context);
		// dialog.show();
		// dialog.setTextView("当前正在定位....");
		locThread = new LocationThread(true);
		timeOutThread = new Thread(locThread);
		executors.submit(locThread);
		// opts.setLocationMode(LocationMode.Hight_Accuracy);
		opts.setOpenGps(true);
		opts.setCoorType(BAIDU_COORTYPE);
		opts.setScanSpan(SCANNER_TIME);
		opts.setTimeOut(5000);
		opts.setScanSpan(1000);
		gson = new Gson();
		// opts.setIsNeedAddress(true);
		// opts.setNeedDeviceDirect(true);
		mClient.setLocOption(opts);
		// 初始化搜索模块，注册事件监听
		timeOutThread.start();
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
				if (locThread != null) {

					locThread.setFlag(false);
					// dialog.dismiss();
					// }

					locThread = null;
				}
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
				Message msg = new Message();
				msg.obj = "{\"lat\":\"" + location.getLatitude()
						+ "\",\"lng\":\"" + location.getLongitude()
						+ "\",\"province\":\"" + province + "\",\"city\":\""
						+ city + "\",\"district\":\"" + district
						+ "\",\"strent\":\"" + street
						+ "\",\"strentNumber\":\"" + streetNumber
						+ "\",\"address\":\"" + address + "\",\"peoName\":\""
						+ peoName + "\"}";
				msg.what = LOCATION_SUCCESS;
				activityHandler.sendMessage(msg);

			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {

			}
		});
		mClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(final BDLocation location) {
				// if(dialog.isShowing()) {
				if (location == null)
					return;
				Message msg = new Message();
				msg.what = LOCATION_SUCCESS;
				msg.obj = location;
				handler.sendMessage(msg);
				mClient.unRegisterLocationListener(this);
				mClient.stop();

			}

		});
		// 开始进行定位
		mClient.start();
		mClient.requestLocation();
	}

	public static void setActivityHandler(Handler handler) {
		isClose = false;
		activityHandler = handler;
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
				while (flag && endTime - beginTime < 10 * 1000) {
					// 更新对话框的界面
					Message msg = handler.obtainMessage(DIALOG_REFRESH);
					msg.arg1 = (int) (10 - (endTime - beginTime) / 1000);
					handler.sendMessage(msg);
					Thread.sleep(1000);
					endTime = System.currentTimeMillis();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (endTime - beginTime >= 10 * 1000) {
				handler.sendEmptyMessage(DIALOG_DISMISS);
				activityHandler.sendEmptyMessage(LOCATION_FAILED);
				isClose = true;

			}

		}

	}

	/*
	 * @author haijian 获取网络状态
	 */

	public static int getInternetState(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			state = nointernet;
			return state;
		}
		if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			state = gprs;

		}
		if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			state = wifi;
		}
		return state;
	}

}

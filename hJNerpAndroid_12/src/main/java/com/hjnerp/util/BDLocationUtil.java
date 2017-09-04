package com.hjnerp.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.hjnerp.common.EapApplication;
import com.hjnerp.widget.WaitDialog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import_location com.baidu.location.LocationClientOption.LocationMode;

public class BDLocationUtil {
	
	public static final String TAG = "BDLocationUtil";
	public static final ExecutorService executors = Executors.newFixedThreadPool(3, new NameableThreadFactory("BDLocation-Thread"));
	public static final int DIALOG_DISMISS = 1;
	public static final int DIALOG_REFRESH = 2;
	public static final int LOCATION_FAILED = 3;
	public static final int LOCATION_SUCCESS = 4;
	public static final int SCANNER_TIME = 5000; //扫描周期
	public static final String BAIDU_COORTYPE="bd09ll";//百度经纬度
	private static WaitDialog dialog;
	private static LocationClient mClient;
	private static LocationClientOption opts = new LocationClientOption();
	private static LocationThread locThread = null;
	
	private static Handler activityHandler;
	private static GeoCoder mSearch;
	private static BDLocation location;
	
	private static final Handler handler = new Handler(){

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DIALOG_REFRESH:
				dialog.setTextView("当前正在定位..."+msg.arg1);
				
				break;
			case DIALOG_DISMISS:
				dialog.dismiss();
				locThread = null;
				break;
			case LOCATION_SUCCESS:
				location = (BDLocation) msg.obj;
				LatLng ptCenter = new LatLng(location.getLatitude(),
						location.getLongitude());
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
				break;
			default:
				break;
			}
		};
	};

	public static void getLocation(Context context) {
	    mClient = new LocationClient(context);
	    dialog = new WaitDialog(context);
	    dialog.show();
	    dialog.setTextView("当前正在定位....");
	    locThread = new LocationThread(true);
	    executors.submit(locThread);
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
				msg.obj =  location.getLatitude()+
						":" + location.getLongitude()
						+ ":" +  address + ":"
						+ peoName;
				msg.what = 1;
				activityHandler.sendMessage(msg);

			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {

			}
		});
		
		mClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				 if(dialog.isShowing()) {
					 if (locThread!=null) {
						 locThread.setFlag(false);
					}
					dialog.dismiss();
				 } 
				    locThread = null;
					Message msg = handler.obtainMessage(LOCATION_SUCCESS);
					msg.obj = location;
					handler.sendMessage(msg);   
				    mClient.unRegisterLocationListener(this);
					mClient.stop(); 
			}

			 
		});
		//开始进行定位
		mClient.start();
		mClient.requestLocation();
			
	}
	

	public static void setActivityHandler(Handler handler) {
		activityHandler = handler;
	}

	private static class LocationThread implements Runnable{
		private long beginTime;
		private long endTime;
		private boolean flag;
		public LocationThread(boolean flag) {
			this.flag = flag;
			beginTime = System.currentTimeMillis();
		}
		
		public void setFlag(boolean flag){
			this.flag = flag;
		}

		@Override
		public void run() {
			try {
				endTime = System.currentTimeMillis();
				while(flag&&endTime-beginTime<30*1000){
					//更新对话框的界面
					Message msg = handler.obtainMessage(DIALOG_REFRESH);
					msg.arg1 = (int) (30 -(endTime-beginTime)/1000);
					handler.sendMessage(msg);
					Thread.sleep(1000);
					endTime = System.currentTimeMillis();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(endTime-beginTime>=30*1000) {
				handler.sendEmptyMessage(DIALOG_DISMISS);
				activityHandler.sendEmptyMessage(LOCATION_FAILED);
				
				
			}
			
		}
		
	}

}

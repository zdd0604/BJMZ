package com.hjnerp.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hjnerp.business.activity.AttendanceInfoActivity;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.BaseNear;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.NameValue;
import com.hjnerp.model.NearBuild;
import com.hjnerp.model.NearResult;
import com.hjnerp.net.NetUtils;
import com.hjnerp.util.Log;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/*
 * 地图签到控件
 * */
public class HJAttendance extends LinearLayout implements HJViewInterface {

	private WidgetClass items;
	private Context context;
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	private BusinessParam businessParam;
	private InfoWindow mInfoWindow;
	private OnInfoWindowClickListener listener = null;
	LocationClient mLocClient;
	// 定位相关
	// LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	BaiduMap mBaiduMap;

	private TextView textViewInfo;
	boolean isFirstLoc = true;// 是否首次定位
	int locCount = 0;

	/**
	 * @author haijian 定义gson
	 */
	private Gson gson;
	private ArrayList<NearBuild> lists;
	private BaseNear near = null;
	private View viewinfo;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(lists.size()>0){
				isFirstLoc = false;
			LatLng ll = (LatLng) msg.obj;
			textViewInfo.setText(lists.get(0).name);
			mInfoWindow = new InfoWindow(
					BitmapDescriptorFactory.fromView(viewinfo), ll, 0, listener);
			mBaiduMap.showInfoWindow(mInfoWindow);
			}
//			else{
//				isFirstLoc = true;
//			}
		};
	};

	public HJAttendance(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJAttendance(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJAttendance(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HJAttendance(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,
			BusinessParam param) {
		super(context);
		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;
		this.businessParam.setBillNo(StringUtil.getMyUUID());
		initView();
		lists = new ArrayList<NearBuild>();

	}

	@SuppressLint("InflateParams")
	private void initView() {

		// View view = LayoutInflater.from(getContext()).inflate(
		// R.layout.layout_hjattendance, null);
		WindowManager windowManager = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);

		int height = windowManager.getDefaultDisplay().getHeight();
		int statusBarHeight = StringUtil
				.getStatusHeight((Activity) getContext());// 手机状态栏高度
		int actionbar_height = (int) getResources().getDimension(
				R.dimen.abc_action_bar_default_height);// 自定义actionbar高度

		LayoutParams ll = new LayoutParams(LayoutParams.MATCH_PARENT, height
				- actionbar_height - statusBarHeight);
		BaiduMapOptions mapOptions = new BaiduMapOptions();
		mapOptions.scaleControlEnabled(false); // 隐藏比例尺控件
		mapOptions.zoomControlsEnabled(false);// 隐藏缩放按钮
		mMapView = new MapView(context, mapOptions);
		mMapView.setLayoutParams(ll);

		addView(mMapView);
		mBaiduMap = mMapView.getMap();
		mCurrentMode = LocationMode.FOLLOWING;
		// // 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mMapView.removeViewAt(1);
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(20);
		mBaiduMap.setMapStatus(u);

		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));

		listener = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				// 弹了到另外一个窗口
				Intent intent = new Intent(context,
						AttendanceInfoActivity.class);
				intent.putExtra("pois", lists);
				intent.putExtra("params", businessParam);
				if (lists != null && lists.size() != 0) {
					((Activity) getContext()).startActivityForResult(intent, 0);
				} else {
					Toast.makeText(getContext(), "没有位置信息！", Toast.LENGTH_LONG)
							.show();
				}
			}
		};
		// 定位初始化
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000 * 10);
		option.setOpenGps(true); // 是否打开GPRS
		gson = new Gson();
		viewinfo = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjattendance_info, null);
		textViewInfo = (TextView) viewinfo
				.findViewById(R.id.hj_map_textView_info);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	// /**
	// * 定位SDK监听函数
	// */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(final BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder().accuracy(15)
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(0).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			mBaiduMap.setMyLocationData(locData);
			locCount = locCount + 1;

			final LatLng ll = new LatLng(location.getLatitude() + 0.000080,
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);

			/**
			 * @author haijian 加请求，获取附近地址信息
			 */
			if (isFirstLoc) {

				new Thread() {
					public void run() {
						// Map<String, String> maps = new LinkedHashMap<String,
						// String>();
						// maps.put("ak", "Dv3KtjWM4lZSGneppyMZ6wOG");
						// maps.put("mcode",
						// "B2:69:83:A2:A4:83:9C:8C:2E:95:73:79:C4:B2:3A:86:FD:88:81:62;com.hjnerpandroid");
						// maps.put("location", location.getLatitude() + ","
						// + location.getLongitude());
						// maps.put("output", "json");
						// maps.put("pois", "1");
						//
						// String result =
						// HttpUtils.post("http://api.map.baidu.com/geocoder/v2/",
						// maps, HttpUtils.ENCODE_UTF8);
						// android.util.Log.i("info", "地址返回的结果："+result);
						// try {
						// BaseNear near = gson.fromJson(result,
						// BaseNear.class);
						// if(near.statue==0){//返回有结果
						// NearResult rs = near.result;
						// Log.i("info",
						// "formatted_address:"+rs.formatted_address);
						// Log.i("info", "长度:"+rs.builds.size());
						// }
						// } catch (JsonSyntaxException e) {
						// // TODO Auto-generated catch block
						
						// Log.i("info","获取地址异常："+ e.toString());
						// }
						ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new NameValue("ak",
								"Dv3KtjWM4lZSGneppyMZ6wOG"));
						params.add(new NameValue("mcode",
								"B2:69:83:A2:A4:83:9C:8C:2E:95:73:79:C4:B2:3A:86:FD:88:81:62;com.hjnerpandroid"));
						params.add(new NameValue("location", location
								.getLatitude() + "," + location.getLongitude()));
						params.add(new NameValue("output", "json"));
						params.add(new NameValue("pois", "1"));
						try {
							String result = NetUtils.getResultStr(
									"http://api.map.baidu.com/geocoder/v2/",
									params, NetUtils.METHOD_GET, 5000);
							Log.i("info", "" + result);
							try {
								near = gson.fromJson(result, BaseNear.class);
								if (near.statue == 0) {// 返回有结果
									NearResult rs = near.result;
									lists = rs.pois;
									Message msg = new Message();
									msg.what = 0;
									msg.obj = ll;
									handler.sendMessage(msg);
								}
							} catch (JsonSyntaxException e) {
								// TODO Auto-generated catch block
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.i("info", "获取地址异常：" + e.toString());
							e.printStackTrace();
						}
					};
				}.start();
			}
			
			// textViewInfo = (TextView) viewinfo
			// .findViewById(R.id.hj_map_textView_info);
			// if(near!=null){
			//
			// // textViewInfo.setText(lists.get(0).name);
			// // mInfoWindow = new InfoWindow(
			// // BitmapDescriptorFactory.fromView(viewinfo), ll, 0, listener);
			// // mBaiduMap.showInfoWindow(mInfoWindow);
			// }else{
			// textViewInfo.setText("没有信息，请稍后。。。");
			// // textViewInfo.setText("北京和佳软件技术有限公司");
			// }

		}
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
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJesonValue(String values) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setPhoto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
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
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataBuild(Boolean flag, BusinessData ctlm1345List) {
		// TODO Auto-generated method stub

	}

	@Override
	public int saveData(Boolean required) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}

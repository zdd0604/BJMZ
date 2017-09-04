package com.hjnerp.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.hjnerp.common.ActivitySupport;
import com.hjnerpandroid.R;
import com.hjnerpandroid.R.layout;

import java.util.ArrayList;

public class OfflineMap extends ActivitySupport implements MKOfflineMapListener {

	private MKOfflineMap mOffline = null;
//	private ListViewForScrollView nowlist;
	private ListViewForScrollView allCityList;
	private ListViewForScrollView localMapListView;
	private ArrayList<MKOLSearchRecord> nowCities;
	private ArrayList<MKOLSearchRecord> allCities;
	private ArrayList<MKOLUpdateElement> nowCitiesd;
	private LocalMapAdapter lAdapter;
	private int cityid;// 当前正在下载的id
	private String cityNameNow;
	private ArrayList<MKOLUpdateElement> localMapList = null;// 已下载的地图列表

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_offline);
		getSupportActionBar().show();
		getSupportActionBar().setTitle("离线地图管理");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		initView();

	}

	private void initView() {
//		nowlist = (ListViewForScrollView) findViewById(R.id.nowlist_offlineMap);
		Intent intent = getIntent();
		cityNameNow = intent.getStringExtra("cityName");

		allCityList = (ListViewForScrollView) findViewById(R.id.undownloadedCityList);
		allCities = mOffline.getOfflineCityList();
		lAdapter = new LocalMapAdapter(3);
		allCityList.setAdapter(lAdapter);
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		initReload();
		localMapListView = (ListViewForScrollView) findViewById(R.id.downloadedCityList);
		lAdapter = new LocalMapAdapter(2);
		localMapListView.setAdapter(lAdapter);
		initLocationCity();
	}

	private void initReload() {
		for (MKOLUpdateElement element:localMapList){
			 if (element.ratio!=100){
				mOffline.start(element.cityID);
			}
		}

	}


	private void initLocationCity() {
		// TODO Auto-generated method stub
		TextView cityNamen=(TextView)findViewById(R.id.cityNameForMapNow);
		TextView updaten=(TextView)findViewById(R.id.updateForMapNow);
		TextView ration=(TextView)findViewById(R.id.ratioForMapNow);
		ImageView downloadStaten=(ImageView)findViewById(R.id.iv_download_offlineNow);

		if (cityNameNow != null && cityNameNow != "") {
			nowCities = mOffline.searchCity(cityNameNow);
			if (nowCities == null || nowCities.size() != 1) {
				nowCities=new ArrayList<MKOLSearchRecord>();
			}
			for (int i = 0; i < localMapList.size(); i++) {
				if (localMapList.get(i).cityID == nowCities.get(0).cityID) {
					final MKOLUpdateElement e=localMapList.get(i);
					cityNamen.setText(e.cityName);
					ration.setText(e.ratio + "%");
					if (e.update) {
						updaten.setText("可更新");
					} else {
						updaten.setText("最新");
					}
					downloadStaten.setImageResource(R.drawable.cancel);
					downloadStaten.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mOffline.remove(e.cityID);
							Toast.makeText(getContext(),
									"已删除离线地图. 城市名: " + e.cityName,
									Toast.LENGTH_SHORT).show();
							updateView();
							initLocationCity();
						}
					});
					return;
				}
			}

			cityNamen.setText(nowCities.get(0).cityName);
			updaten.setText(formatDataSize(nowCities.get(0).size));
			ration.setText("");
			downloadStaten.setImageResource(R.drawable.download);
			downloadStaten.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mOffline.start(nowCities.get(0).cityID);
					cityid = nowCities.get(0).cityID;
					Toast.makeText(getContext(),
							"开始下载离线地图. 省份名: " + nowCities.get(0).cityName,
							Toast.LENGTH_SHORT).show();
					updateView();
					initLocationCity();
				}
			});
		}
	}

	@Override
	protected void onPause() {
		mOffline.pause(cityid);

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

	@Override
	public void onDestroy() {
		/**
		 * 退出时，销毁离线地图模块
		 */
		mOffline.destroy();
		super.onDestroy();
	}

	/**
	 * 更新状态显示
	 */
	public void updateView() {
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		initLocationCity();
		lAdapter.notifyDataSetChanged();
	}

	/**
	 * 离线地图列表适配器
	 */
	public class LocalMapAdapter extends BaseAdapter {
		private int status;

		public LocalMapAdapter(int status) {
			this.status = status;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int count = 0;
			switch (status) {
			case 2:
				count = localMapList.size();
				break;
			case 3:
				count = allCities.size();
				break;
			}

			return count;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			switch (status) {
//			case 0:
//				return nowCitiesd.get(position);
//			case 1:
//				return nowCities.get(position);
			case 2:
				return localMapList.get(position);
			case 3:
				return allCities.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			switch (status) {
//			case 0:
			case 2:
				MKOLUpdateElement e = (MKOLUpdateElement) getItem(position);
				convertView = View.inflate(OfflineMap.this,
						R.layout.offline_localmap_list, null);
				initViewItem(convertView, e, null);
				break;
//			case 1:
			case 3:
				MKOLSearchRecord r = (MKOLSearchRecord) getItem(position);
				convertView = View.inflate(OfflineMap.this,
						R.layout.offline_localmap_list, null);
				initViewItem(convertView, null, r);
				break;
			}

			return convertView;
		}

		private void initViewItem(View convertView, final MKOLUpdateElement e,
				final MKOLSearchRecord r) {
			// TODO Auto-generated method stub

			TextView cityName = (TextView) convertView
					.findViewById(R.id.cityNameForMap);
			TextView update = (TextView) convertView
					.findViewById(R.id.updateForMap);
			TextView ratio = (TextView) convertView
					.findViewById(R.id.ratioForMap);
			TextView state = (TextView) convertView
					.findViewById(R.id.state);
			ImageView downSign = (ImageView) convertView
					.findViewById(R.id.iv_download_offline);
			switch (status) {
//			case 0:
			case 2:
				ratio.setText(e.ratio + "%");
				cityName.setText(e.cityName);
				if (e.update) {
					update.setText("可更新");
				} else {
					update.setText("最新");
				}
				if (e.ratio!=100){
					state.setText("正在下载");

				}else {
					state.setText("下载完成");
				}

				downSign.setImageResource(R.drawable.cancel);
				downSign.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mOffline.remove(e.cityID);
						Toast.makeText(getContext(),
								"已删除离线地图. 城市名: " + e.cityName,
								Toast.LENGTH_SHORT).show();
						updateView();
					}
				});
				break;
//			case 1:
			case 3:
				state.setText("");
				cityName.setText(r.cityName);
				update.setText(formatDataSize(r.size));
				ratio.setText("");
				downSign.setImageResource(R.drawable.download);
				downSign.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mOffline.start(r.cityID);
						cityid = r.cityID;
						Toast.makeText(getContext(),
								"开始下载离线地图. 省份名: " + r.cityName,
								Toast.LENGTH_SHORT).show();
						updateView();
					}
				});
				break;
			}

		}

	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			// 处理下载进度更新提示
			if (update != null) {

				updateView();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			// MKOLUpdateElement e = mOffline.getUpdateInfo(state);

			break;
		}
	}
}
package com.hjnerp.business.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.hjnerp.activity.SetActivity;
import com.hjnerp.business.businessutils.MyOrientationListener;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.manager.HjActivityManager;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.Ctlm1347Json;
import com.hjnerp.model.DisCardFageDataClass;
import com.hjnerp.net.HttpClientBuilder;
import com.hjnerp.net.HttpClientBuilder.HttpClientParam;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.net.HttpClientManager.HttpResponseHandler;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.myscom.StringUtils;
import com.hjnerpandroid.R;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * @author 巫志英
 */

@SuppressLint("ResourceAsColor")
public class DisCardFage extends ActivitySupport implements OnClickListener, MyOrientationListener.OnOrientationListener {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private RelativeLayout mMarkerInfoLy;
    private BitmapDescriptor bitmap;
    private TextView tableMode;
    private TextView test;
    private int colorRes;
    //    private WaitDialog dialog;
    private View discard_menuView;
    private static ArrayList<Ctlm1347> ctlm1347List;
    private DisCardFageDataClass disCardFageDataClass;
    public static final String JSON_VALUE = "values";
    private List<String> terminal_ids;
    public BDLocationListener myListener = new MyLocationListener();
    private LocationManager locationManager;
    // 定位的功能
    private LocationClient mLocationClient;
    // 定位的设置
    private MyLocationListener myLocationListener;
    private boolean isFirstIn = true;
    private double mLatitude;
    private double mLongtitude;
    private BitmapDescriptor mIconLocation;
    public static final Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    private ImageView backToMyLocation;
    private HttpResponseHandler responseHandler = new NsyncDataHandler();
    // 模式
    private MyLocationConfiguration.LocationMode locationMode;
    private String mCityName;
    private boolean scatterMode;
    private boolean hasOutNoVisit = false;
    private boolean isDownload = false;
    private GetAllPoints task;
    private Boolean hasCanceled = false;
    private Boolean finishOutValue = false;
    private List<MapInfo> allInfos;
    private TextView title;
    private RelativeLayout rl_actionbar_back;
    private LinearLayout businessmenu_btn;
    private PopupWindow mPopupWindow;
    private TextView dataupload;
    private TextView outlinemap;
    private static final int REQUEST_CODE_WRITE_SETTINGS = 2;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;//方向

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.discard_baidumap);
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        initLocation();
        initView();
        initData();
        initMarkerClickEvent();

    }

    private void initData() {
        // TODO Auto-generated method stub
        SharedPreferences sharedPref = this.getSharedPreferences("mapMode",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isScatterMode", false);
        editor.commit();
        ctlm1347List = new ArrayList<Ctlm1347>();
        ctlm1347List = BusinessBaseDao.getCtlm1347List("001", null, null, null,
                "001002");
        boolean isLoadMode = false;

        if (ctlm1347List.size() > 0) {
            lable4:
            for (int i = 0; i < ctlm1347List.size(); i++) {
                if (ctlm1347List.get(i).getFlag_upload().equalsIgnoreCase("N")
                        && !ctlm1347List.get(i).getId_table()
                        .equalsIgnoreCase("ctlm4101map")) {
                    isLoadMode = true;
                    break lable4;
                }
            }
        }
        if (!isLoadMode) {
            ctlm1347List = new ArrayList<Ctlm1347>();
            ctlm1347List = BusinessBaseDao.getCtlm1347List(null, null, null,
                    null, "001002");
            lable5:
            for (int i = 0; i < ctlm1347List.size(); i++) {
                if (ctlm1347List.get(i).getFlag_upload().equalsIgnoreCase("N")
                        && ctlm1347List.get(i).getId_table()
                        .equalsIgnoreCase("ctlm4101map")) {
                    isLoadMode = false;
                    SharedPreferences sharedPref2 = this.getSharedPreferences(
                            "mapMode", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPref2.edit();
                    editor.putBoolean("isScatterMode", true);
                    editor.commit();
                    break lable5;
                }
            }

        }
        SharedPreferences sharedPref3 = this.getSharedPreferences("mapMode",
                Context.MODE_PRIVATE);
        boolean isScatterMode1 = sharedPref3.getBoolean("isScatterMode", false);
        if (isScatterMode1) {
            scatterMode = true;
            hasOutNoVisit = true;
            doUseOutOfLine();
            // changeMode.setVisibility(View.GONE);
        } else {
            scatterMode = false;
            initDataRoad();
        }
    }

    @Override
    public void onOrientationChanged(float x) {

    }

    private class NsyncDataHandler extends HttpResponseHandler {

        @Override
        public void onResponse(HttpResponse resp) {
            // TODO Auto-generated method stub
            try {
                String contentType = resp.getHeaders("Content-Type")[0]
                        .getValue();
                // if ("application/octet-stream".equals(contentType) ) {
                if (contentType.indexOf("application/octet-stream") != -1) {
                    String contentDiscreption = resp
                            .getHeaders("Content-Disposition")[0].getValue();
                    String fileName = contentDiscreption
                            .substring(contentDiscreption.indexOf("=") + 1);
                    FileOutputStream fos = new FileOutputStream(new File(
                            getExternalCacheDir(), fileName));
                    resp.getEntity().writeTo(fos);
                    fos.close();
                    String json = processBusinessCompress(fileName);
                    JSONObject jsonObject = new JSONObject(json);
                    String value = jsonObject.getString(JSON_VALUE);
                    Log.d("value", value);
                    processJsonValue(value);
                } else {

                }

            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onException(Exception e) {
            // TODO Auto-generated method stub

        }
    }

    private String processBusinessCompress(String fileName) {
        // TODO Auto-generated method stub
        ZipInputStream zis = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            File f = new File(getExternalCacheDir(), fileName);
            FileInputStream fis = new FileInputStream(f);
            zis = new ZipInputStream(fis);
            ZipEntry zip = zis.getNextEntry();
            int len = 0;
            while ((len = zis.read(data)) != -1) {
                baos.write(data, 0, len);
            }
            String json = new String(baos.toByteArray(), HTTP.UTF_8);
            return json;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (zis != null) {
                    zis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public void processJsonValue(String value) throws JSONException {
        // TODO Auto-generated method stub

        if (value.equalsIgnoreCase("[]") || value.equalsIgnoreCase(null)) {
            waitDialogRectangle.dismiss();
            return;
        }
        JSONArray jsonArray = new JSONArray(value);

        for (int i = 0; i < jsonArray.length(); i++) {
            Ctlm1345 ctlm1345 = new Ctlm1345();
            // 添加表头
            String temp = jsonArray.getString(i);

            Matcher m = p.matcher(temp);
            temp = m.replaceAll("");
            String subValue = temp.substring(temp.indexOf("{"),
                    temp.indexOf("}") + 1);
            // 2.存入1347
            Gson gson = new Gson();
            Ctlm1347Json ctlm1347Json = gson.fromJson(subValue,
                    Ctlm1347Json.class);

            saveInto1347(subValue, i, ctlm1347Json.getName_terminal(),
                    ctlm1347Json.getVar_addr());

        }
        task = new GetAllPoints();
        task.execute("");
        waitDialogRectangle.dismiss();

    }

    private void saveInto1347(String subValue, int i, String name, String addr) {
        // TODO Auto-generated method stub
        ArrayList<Ctlm1347> ctlm1347result = new ArrayList<Ctlm1347>();
        ctlm1347result.clear();
        Ctlm1347 ctlm1347 = new Ctlm1347();
        ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(
                EapApplication.getApplication().getApplicationContext())
                .getMyId());
        ctlm1347.setId_com(SharePreferenceUtil.getInstance(
                EapApplication.getApplication().getApplicationContext())
                .getComid());
        ctlm1347.setId_node(StringUtil.getMyUUID());
        ctlm1347.setName_node("路线");
        ctlm1347.setVar_billno(StringUtil.getMyUUID());
        // ctlm1347.setId_model("001");
        ctlm1347.setId_srcnode("001002");
        ctlm1347.setFlag_upload("N");
        ctlm1347.setId_parentnode("");
        ctlm1347.setId_nodetype("HJList");
        ctlm1347.setVar_data1("N");
        ctlm1347.setVar_data2(name);
        ctlm1347.setVar_data3(addr);
        ctlm1347.setDate_opr(DateUtil.getCurDateStr("yyyy-MM-dd HH:mm:ss"));
        ctlm1347.setId_view("001");
        String s1 = subValue.substring(0, subValue.length() - 1);
        String s2 = s1 + ",\"checked\":\"N\"}";
        ctlm1347.setvar_Json(s2);
        ctlm1347.setInt_line(i + 1);
        ctlm1347.setId_table("ctlm4101map");// 根据是不是4101map判断地图颜色是不是蓝色，即为计划外线路
        ctlm1347result.add(ctlm1347);
        BusinessBaseDao.replaceCTLM1347s(ctlm1347result);
    }

    /**
     * 定位配置
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

        // 初始化图标
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.position0);
        //定位成功后的回调监听方向监听
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            MyLocationData myLocationData = new MyLocationData.Builder()//
                    .direction(mCurrentX)// 设置定位图标的方向
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();

            mBaiduMap.setMyLocationData(myLocationData);

            // 设置自定义图标
            MyLocationConfiguration configuration = new MyLocationConfiguration(
                    locationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(configuration);

            mLatitude = bdLocation.getLatitude();
            mLongtitude = bdLocation.getLongitude();

            if (isFirstIn) {

                LatLng latLng = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MyMapStatusUpdate(latLng);

                isFirstIn = false;

            }
            mCityName = bdLocation.getCity();

        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.tv_actionbar_title);
        title.setText("地图拜访");
        rl_actionbar_back = (RelativeLayout) findViewById(R.id.rl_actionbar_back);
        rl_actionbar_back.setOnClickListener(this);
        businessmenu_btn = (LinearLayout) findViewById(R.id.businessmenu_btn);

        businessmenu_btn.setVisibility(View.VISIBLE);
        ImageView img = new ImageView(this);

        img.setImageResource(R.drawable.ic_actbar_more);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        businessmenu_btn.addView(img);
//        businessmenu_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_actbar_more));
//        businessmenu_btn.setBackgroundResource(R.drawable.ic_actbar_more);
//        businessmenu_btn.setScaleX();
        businessmenu_btn.setOnClickListener(this);
        View popupView = getLayoutInflater().inflate(
                R.layout.popupwindowformap, null);
        dataupload = (TextView) popupView.findViewById(R.id.dataupload);
        dataupload.setOnClickListener(this);
        outlinemap = (TextView) popupView.findViewById(R.id.outlinemap);
        outlinemap.setOnClickListener(this);
        mPopupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
                (Bitmap) null));
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMarkerInfoLy = (RelativeLayout) findViewById(R.id.id_marker_info);
        discard_menuView = LayoutInflater.from(this).inflate(
                R.layout.discard_bdmap_menu, null);
        mMarkerInfoLy.addView(discard_menuView);
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_red);
        colorRes = 0xAAFF0000;
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);// 地图放大级别
        mBaiduMap.setMapStatus(msu);
        LatLng latLng = new LatLng(mLatitude, mLongtitude);
        MyMapStatusUpdate(latLng);
        tableMode = (TextView) findViewById(R.id.p2);
        test = (TextView) findViewById(R.id.p11);
        backToMyLocation = (ImageView) findViewById(R.id.returnMyLocation);
        tableMode.setOnClickListener(this);
        test.setOnClickListener(this);
        backToMyLocation.setOnClickListener(this);
        // changeMode = (LinearLayout) findViewById(R.id.changeMode);

        mBaiduMap
                .setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

                    @Override
                    public void onMapStatusChangeStart(MapStatus arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onMapStatusChangeFinish(MapStatus arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onMapStatusChange(MapStatus mapStatus) {
                        // TODO Auto-generated method stub
                        if (finishOutValue) {
                            double lati = mapStatus.target.latitude;
                            double longi = mapStatus.target.longitude;
                            AddMap task1 = new AddMap();
                            task1.execute(lati + "", longi + "");
                        }

                    }
                });

    }

    private void initDataRoad() {
        // TODO Auto-generated method stub

        List<MapInfo> infos = new ArrayList<MapInfo>();
        terminal_ids = new ArrayList<String>();
        // 从数据库中读取数据并解析
        ctlm1347List = new ArrayList<Ctlm1347>();
        ctlm1347List = BusinessBaseDao.getCtlm1347List("001", null, null, null,
                "001002");
        if (ctlm1347List.size() > 0) {
            int c = 0;// 用来判断是否只剩下拜访完毕的终端,如果是,则c=0.c的值正好是未拜访的数量
            lable1:
            for (int i = 0; i < ctlm1347List.size(); i++) {
                String json = ctlm1347List.get(i).getvar_Json();
                String billno = ctlm1347List.get(i).getVar_billno();
                String nodeid = ctlm1347List.get(i).getId_node();
                String viewid2 = ctlm1347List.get(i).getId_view();

                Gson gson = new Gson();
                Ctlm1347Json ctlm1347Json = gson.fromJson(json,
                        Ctlm1347Json.class);
                double a;
                boolean b;
                double longi;
                double lati;
                if (ctlm1347Json.getVar_longi().equals("")
                        || ctlm1347Json.getVar_lati().equals("")) {
                    if (ctlm1347List.get(i).getFlag_upload()
                            .equalsIgnoreCase("Y")) {
                        b = true;
                        terminal_ids.add(ctlm1347Json.getId_terminal());
                        String location = BusinessBaseDao
                                .getCtlm1347List(null, null, nodeid, billno,
                                        "002060").get(0).getVar_data1();
                        if (location != null) {
                            String[] locats = location.split(":");
                            longi = Double.valueOf(locats[0]).doubleValue();
                            lati = Double.valueOf(locats[1]).doubleValue();
                            Log.v("location is ", "经纬度为:" + longi + "," + lati);
                            infos.add(new MapInfo(longi, lati, ctlm1347Json
                                    .getName_terminal(), ctlm1347Json
                                    .getVar_addr(), b, "002", billno, nodeid,
                                    json, i));
                        }
                    } else {
                        c++;
                    }
                } else {
                    try {
                        longi = Double.valueOf(ctlm1347Json.getVar_longi())
                                .doubleValue();
                        lati = Double.valueOf(ctlm1347Json.getVar_lati())
                                .doubleValue();
                        if (longi > lati) {
                            // 如果数据库的经纬度出错 则交换数值,有些数据是错误的,经纬度反了
                            a = longi;
                            longi = lati;
                            lati = a;
                        }
                        if (ctlm1347List.get(i).getFlag_upload()
                                .equalsIgnoreCase("Y")) {
                            b = true;
                            terminal_ids.add(ctlm1347Json.getId_terminal());
                        } else {
                            b = false;
                            c++;
                        }
                        infos.add(new MapInfo(longi, lati, ctlm1347Json
                                .getName_terminal(),
                                ctlm1347Json.getVar_addr(), b, "002", billno,
                                nodeid, json, i));
                    } catch (Exception e) {

                    }

                }
            }

            if (c == 0) {
                doUseOutOfLine();
            } else {
                LatLng latLng = null;
                OverlayOptions overlayOptions = null;
                OverlayOptions textOptions = null;
                Marker marker = null;
                List<LatLng> points = new ArrayList<LatLng>();
                for (MapInfo info : infos) {
                    latLng = new LatLng(info.getLatitude(), info.getLongitude());
                    points.add(latLng);
                    Bitmap bitmap1;
                    if (info.isChecked()) {
                        bitmap1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.location_green);// 如果已经拜访就变成绿色的

                    } else {
                        bitmap1 = BitmapFactory.decodeResource(getResources(),
                                R.drawable.location_red);// 如果未拜访就变成红色的

                    }

                    bitmap = BitmapDescriptorFactory.fromBitmap(bitmap1);
                    overlayOptions = new MarkerOptions().position(latLng).icon(
                            bitmap);

                    marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", info);
                    marker.setExtraInfo(bundle);

                }
            }

        } else {
            doUseOutOfLine();
        }

    }

    private void doUseOutOfLine() {
        // TODO Auto-generated method stub
        if (hasOutNoVisit) {
            if (finishOutValue) {
                double lati = mBaiduMap.getMapStatus().target.latitude;
                double longi = mBaiduMap.getMapStatus().target.longitude;
                AddMap task = new AddMap();
                task.execute(lati + "", longi + "");
            } else {
                task = new GetAllPoints();
                task.execute("");
            }
        } else if (scatterMode) {
            useOutOfLine();
        } else {
            findOutLine();
        }
    }

    private void findOutLine() {
        // TODO Auto-generated method stub
        final Dialog noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);
        RelativeLayout dialog_cancel_rl, dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog
                .findViewById(R.id.dialog_notice_tv);
        notice.setText("尚未检测到任何路线，请选择手动下载计划内线路，或者进入散点拜访模式");
        dialog_cancel_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_cancel_rl);
        TextView dialog_cancel_tv = (TextView) noticeDialog
                .findViewById(R.id.dialog_cancel_tv);
        dialog_cancel_tv.setText("计划下载");
        dialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_confirm_rl);
        TextView dialog_confirm_tv = (TextView) noticeDialog
                .findViewById(R.id.dialog_confirm_tv);
        dialog_confirm_tv.setText("散点模式");
        dialog_cancel_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
                Intent intent1 = new Intent(getContext(), SetActivity.class);
                startActivity(intent1);
                // finish();
                isDownload = true;
            }
        });
        dialog_confirm_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
                useOutOfLine();
                scatterMode = true;
                // changeMode.setVisibility(View.GONE);
            }
        });

        noticeDialog.show();

    }

    /**
     * 散点模式拜访
     */
    private void useOutOfLine() {
        // TODO Auto-generated method stub
        // 1.解析传回来的字符串
        SharedPreferences sharedPref = this.getSharedPreferences("mapMode",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isScatterMode", true);
        editor.commit();
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在加载...");
        try {
            HttpClientParam param = HttpClientBuilder
                    .createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
            param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                    .addKeyValue("id_table", StringUtils.join("ctlm4101map:"))
                    .addKeyValue("condition", "id_table = 'ctlm4101map'");

            HttpClientManager.addTask(responseHandler, param.getHttpPost());
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initMarkerClickEvent() {
        // TODO Auto-generated method stub
        // 对Marker的点击
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                // 获得marker中的数据
                MapInfo info = (MapInfo) marker.getExtraInfo().get("info");
                InfoWindow mInfoWindow;
                // 生成一个TextView用户在地图中显示InfoWindow
                TextView location = new TextView(getApplicationContext());
                location.setBackgroundResource(R.drawable.location_tips);
                location.setPadding(30, 20, 30, 50);
                location.setText(info.getName());
                location.setTextColor(Color.rgb(255, 255, 255));
                // 将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                // p.y -= 47;
                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                // 为弹出的InfoWindow添加点击事件
                mInfoWindow = new InfoWindow(location, llInfo, -47);
                // 显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                // 设置详细信息布局为可见
                mMarkerInfoLy.setVisibility(View.VISIBLE);
                // 根据商家信息为详细信息布局设置信息
                popupInfo(mMarkerInfoLy, info);
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                mBaiduMap.hideInfoWindow();
                mMarkerInfoLy.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 根据info为布局上的控件设置信息
     *
     * @param mMarkerLy
     * @param info
     */
    public void popupInfo(RelativeLayout mMarkerLy, final MapInfo info) {
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null) {
            viewHolder = new ViewHolder();
            viewHolder.infoName = (TextView) mMarkerLy
                    .findViewById(R.id.info_name);
            viewHolder.infoChecked = (TextView) mMarkerLy
                    .findViewById(R.id.info_checked);
            viewHolder.biness_layout = (LinearLayout) mMarkerLy
                    .findViewById(R.id.biness_layout);
            viewHolder.buiness_time = (TextView) mMarkerLy
                    .findViewById(R.id.buiness_time);
            viewHolder.info_visit = (Button) mMarkerLy
                    .findViewById(R.id.info_visit);
            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();

        // viewHolder.infoDistance.setText(info.getDistance());
        viewHolder.infoName.setText(info.getName());
        viewHolder.buiness_time.setText("");

        if (info.isChecked()) {
            viewHolder.infoChecked.setText("已拜访");
            viewHolder.infoChecked.setTextColor(getResources().getColor(R.color.darkseagreen));
            viewHolder.biness_layout.setVisibility(View.VISIBLE);
            viewHolder.info_visit.setVisibility(View.GONE);
            String time = BusinessBaseDao
                    .getCtlm1347List(null, null, info.getNodeid(),
                            info.getBillno(), "002055").get(0).getVar_data1();
            if (time != null) {
                viewHolder.buiness_time.setText(time);
                viewHolder.buiness_time.setTextColor(getResources().getColor(R.color.darkseagreen));
            }

        } else {
            viewHolder.infoChecked.setText("未拜访");
            viewHolder.info_visit.setVisibility(View.VISIBLE);
            viewHolder.biness_layout.setVisibility(View.GONE);
        }
        viewHolder.info_visit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disCardFageDataClass = new DisCardFageDataClass();
                disCardFageDataClass.setBillno(info.getBillno());
                disCardFageDataClass.setValue(info.getValues());
                disCardFageDataClass.setNodeid(info.getNodeid());
                disCardFageDataClass.setViewid2(info.getViewid2());
                /**
                 * 更新数据库，使计划外数据变为可拜访数据
                 */
                ctlm1347List = BusinessBaseDao.getCtlm1347List(null, null,
                        null, null, "001002");
                if (ctlm1347List.size() > 0) {

                    for (int i = 0; i < ctlm1347List.size(); i++) {
                        String nodeid = ctlm1347List.get(i).getId_node();
                        if (ctlm1347List.get(i).getId_table()
                                .equalsIgnoreCase("ctlm4101map")
                                && info.getNodeid().equalsIgnoreCase(
                                ctlm1347List.get(i).getId_node())) {
                            ctlm1347List.get(i).setId_model("001");

                        }
                    }
                    BusinessBaseDao.replaceCTLM1347s(ctlm1347List);
                }
                setDatas();
            }
        });
    }

    /**
     * 设置数据
     */
    private void setDatas() {
        Intent intent = getIntent();
        intent.putExtra("DisCardData", disCardFageDataClass);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 复用弹出面板mMarkerLy的控件
     *
     * @author wzy
     */
    private class ViewHolder {
        TextView infoName;
        // TextView infoDistance;
        TextView infoChecked;
        TextView buiness_time;
        LinearLayout biness_layout;
        Button info_visit;
    }

    /**
     * 地图定位
     */
    private void MyMapStatusUpdate(LatLng myLatlng) {
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(myLatlng);
        mBaiduMap.animateMapStatus(msu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
        hasCanceled = true;
        SharedPreferences sharedPref = this.getSharedPreferences("mapMode",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isScatterMode", false);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        ctlm1347List = BusinessBaseDao.getCtlm1347List("001", null, null, null,
                "001002");
        boolean hasLine = false;
        if (isDownload && !scatterMode) {
            if (ctlm1347List.size() > 0) {
                lable5:
                for (int i = 0; i < ctlm1347List.size(); i++) {
                    if (ctlm1347List.get(i).getFlag_upload()
                            .equalsIgnoreCase("N")) {
                        initDataRoad();
                        hasLine = true;
                        isDownload = false;
                        return;
                    }
                }

            }
        }
        if (isDownload && !hasLine) {
            final Dialog noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
            noticeDialog.setContentView(R.layout.dialog_notice_withcancel);
            RelativeLayout dialog_cancel_rl, dialog_confirm_rl;
            TextView notice = (TextView) noticeDialog
                    .findViewById(R.id.dialog_notice_tv);
            notice.setText("没有检测到数据，请确保已经下载数据。要重新下载数据还是直接进入散点模式？");
            dialog_cancel_rl = (RelativeLayout) noticeDialog
                    .findViewById(R.id.dialog_cc_cancel_rl);
            TextView dialog_cancel_tv = (TextView) noticeDialog
                    .findViewById(R.id.dialog_cancel_tv);
            dialog_cancel_tv.setText("重新下载");
            dialog_confirm_rl = (RelativeLayout) noticeDialog
                    .findViewById(R.id.dialog_cc_confirm_rl);
            TextView dialog_confirm_tv = (TextView) noticeDialog
                    .findViewById(R.id.dialog_confirm_tv);
            dialog_confirm_tv.setText("散点模式");
            dialog_cancel_rl.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    noticeDialog.dismiss();
                    Intent intent1 = new Intent(getContext(), SetActivity.class);
                    startActivity(intent1);
                    // finish();
                    isDownload = true;
                }
            });
            dialog_confirm_rl.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    noticeDialog.dismiss();
                    SharedPreferences sharedPref = getContext().getSharedPreferences("mapMode",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isScatterMode", true);
                    editor.commit();
                    scatterMode = true;
                    doUseOutOfLine();
                    // changeMode.setVisibility(View.GONE);
                    isDownload = false;
                }
            });

            noticeDialog.show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 开启定位的允许
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }

        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();

        //停止方向传感器
        myOrientationListener.stop();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.p2:
                finish();
                break;
            case R.id.returnMyLocation:
                MapStatusUpdate msu1 = MapStatusUpdateFactory.zoomTo(17.0f);
                mBaiduMap.setMapStatus(msu1);
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MyMapStatusUpdate(latLng);
                break;
            case R.id.p11:

                break;
            case R.id.rl_actionbar_back:
                finish();
                break;
            case R.id.businessmenu_btn:
                mPopupWindow.showAsDropDown(v);
                break;
            case R.id.outlinemap:
                Intent intent = new Intent();
                intent.setClass(this, OfflineMap.class);
                intent.putExtra("cityName", mCityName);
                startActivity(intent);
                break;
            case R.id.dataupload:
                HjActivityManager.getInstance().hjc_controluplod("001", "001002");
                break;
        }
    }

    private class AddMap extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            Log.d("progress", values + "");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            LatLng latLng = null;
            OverlayOptions overlayOptions = null;
            OverlayOptions textOptions = null;
            Marker marker = null;
            List<MapInfo> infos = new ArrayList<MapInfo>();
            List<LatLng> points = new ArrayList<LatLng>();
            for (MapInfo info : allInfos) {
                if ((info.getLatitude() < Double.valueOf(params[0]) + 0.01)
                        && (info.getLatitude() > Double.valueOf(params[0]) - 0.01)
                        && (info.getLongitude() < Double.valueOf(params[1]) + 0.01)
                        && (info.getLongitude() > Double.valueOf(params[1]) - 0.01)) {
                    infos.add(info);
                }
            }
            for (MapInfo info : infos) {
                if (hasCanceled) {
                    infos.clear();
                    points.clear();
                    ctlm1347List.clear();
                    return false;
                }
                latLng = new LatLng(info.getLatitude(), info.getLongitude());
                points.add(latLng);
                Bitmap bitmap1;
                if (!info.isChecked()) {
                    bitmap1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.location_blue);// 如果已经拜访就变成蓝色的

                } else {
                    bitmap1 = BitmapFactory.decodeResource(getResources(),
                            R.drawable.location_green);// 如果未拜访就变成绿色的

                }

                bitmap = BitmapDescriptorFactory.fromBitmap(bitmap1);
                overlayOptions = new MarkerOptions().position(latLng).icon(
                        bitmap);

                marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

                Bundle bundle = new Bundle();
                bundle.putSerializable("info", info);
                marker.setExtraInfo(bundle);

            }
            return true;
        }

    }

    private class GetAllPoints extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            Log.d("progress0", values + "");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            finishOutValue = false;
            if (hasCanceled) {
                return false;
            } else {
                allInfos = new ArrayList<MapInfo>();
                ctlm1347List.clear();
                ctlm1347List = BusinessBaseDao.getCtlm1347List(null, null,
                        null, null, "001002");
                if (ctlm1347List.size() > 0) {
                    lable2:
                    for (int i = 0; i < ctlm1347List.size(); i++) {
                        if (hasCanceled) {
                            allInfos.clear();
                            ctlm1347List.clear();
                            return false;
                        }
                        if (ctlm1347List.get(i).getId_table()
                                .equalsIgnoreCase("ctlm4101map")) {
                            String json = ctlm1347List.get(i).getvar_Json();
                            String billno = ctlm1347List.get(i).getVar_billno();
                            String nodeid = ctlm1347List.get(i).getId_node();
                            String viewid2 = ctlm1347List.get(i).getId_view();
                            Gson gson = new Gson();
                            Ctlm1347Json ctlm1347Json = gson.fromJson(json,
                                    Ctlm1347Json.class);
                            double a1;
                            boolean b1 = false;
                            double longi1;
                            double lati1;
                            if (ctlm1347List.get(i).getFlag_upload()
                                    .equalsIgnoreCase("Y")) {
                                b1 = true;
                            } else {
                                if (!scatterMode
                                        && terminal_ids.contains(ctlm1347Json
                                        .getId_terminal())) {
                                    continue lable2;
                                }
                                b1 = false;
                            }
                            if (ctlm1347Json.getVar_longi().equals("")
                                    || ctlm1347Json.getVar_lati().equals("")) {
                                continue lable2;
                            } else {
                                try {
                                    longi1 = Double.valueOf(
                                            ctlm1347Json.getVar_longi())
                                            .doubleValue();
                                    lati1 = Double.valueOf(
                                            ctlm1347Json.getVar_lati())
                                            .doubleValue();
                                    if (longi1 > lati1) {
                                        // 如果数据库的经纬度出错 则交换数值,有些数据是错误的,经纬度反了
                                        a1 = longi1;
                                        longi1 = lati1;
                                        lati1 = a1;
                                    }

                                    allInfos.add(new MapInfo(longi1, lati1,
                                            ctlm1347Json.getName_terminal(),
                                            ctlm1347Json.getVar_addr(), b1,
                                            "002", billno, nodeid, json, i));
                                } catch (Exception e) {

                                }

                            }

                        }

                    }
                }
                finishOutValue = true;
                double lati = mBaiduMap.getMapStatus().target.latitude;
                double longi = mBaiduMap.getMapStatus().target.longitude;
                AddMap task = new AddMap();
                task.execute(lati + "", longi + "");

                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

}

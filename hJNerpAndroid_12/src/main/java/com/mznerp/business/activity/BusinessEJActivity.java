package com.mznerp.business.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mznerp.business.adapter.BusinessDdispLocathAdapter;
import com.mznerp.business.adapter.BusinessSginImageViewAdapter;
import com.mznerp.business.businessutils.BusinessTimeUtils;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.Ej1345;
import com.mznerp.model.UserInfo;
import com.mznerp.net.HttpClientManager;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.util.ZipUtils;
import com.mznerp.util.myscom.FileUtils;
import com.mznerp.widget.HorizontalListView;
import com.mznerp.widget.WaitDialogRectangle;
import com.mznerp.R;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 考勤签到之前的版本
 */
public class BusinessEJActivity extends ActivitySupport implements
        View.OnClickListener {
    private TextView title;// 设置标题
    private RadioButton locath_sign_in;// 签到
    private RadioButton locath_sign_out;// 签退
    private ListView ddlicath_info;// 个人信息
    private List<String> list_title = new ArrayList<String>(); // 标题
    private List<String> list_content = new ArrayList<String>();// 内容
    private List<Integer> lsit_icon = new ArrayList<>();//图标
    // 默认情况 下的title 、 content 内容
//    private String title_userID = "		签到人：";
//    private String content_userID = "签到人";
//
//    private String title_userTime = "签到时间：";
//    private String content_userTime = "签到时间";
//
//    private String title_userAdress = "签到地址：";
//    private String content_userAdress = "签到地址";
    private String title_userID = "姓       名：";
    private String content_userID = "姓名ID";

    private String title_userDept = "部       门：";
    private String content_userDept = "部门名称";

    private String title_planSginlTime = "计划上班：";
    private String content_planSginTime = "计划上班时间";

    private String title_practicaSginlTime = "实际上班：";
    private String content_practicalSginTime = "实际上班时间";

    private String title_planOutlTime = "计划下班：";
    private String content_planOutlTime = "计划下班时间时间";

    private String title_practicaOutlTime = "实际下班：";
    private String content_practicaOutlTime = "实际下班时间";


    protected WaitDialogRectangle waitDialogRectangle;
    private LocationClient mLocationClient = null;
    // 定位的设置
    private BusinessEJActivity.MyLocationListener myLocationListener;
    // 经纬度
    private double sginLatitude = 0.00;
    private double sginLongitude = 0.00;

    private UserInfo myInfo; // 最近登陆账户的个人信息
    private String sgin_userID = null; // 签到人
    private String sgin_data = null; // 签到时间
    private String sgin_address = null;// 签到地址
    private String sginPhotoName = "";// 照片名称(照片保存在SD卡机的要加反斜杠)
    private TextView sginlocath_btn;
    private String uuid = null;// 随机编码不重复

    private File sginSaveFile = null;// 签到文件的保存路径
    private File sginCreateFile = null;// 签到文件生成后的的text的文件对象
    private String sginCreateFilePath = null;// 签到文件生成的路径
    private File sginCreateFileZIP = null;// 签到文件生成后的的ZIP的文件对象
    private String sginCreateFileZIPPath = null;// 签到文件ZIP生成的路径
    private File sginSavePhotoFile = null; // 图片文件对象
    private String sginPhotoFile = null;// 签到照片存放地址

    private File sginZipFile = null;// 签到文件压缩后的的地址文件对象
    private static final String DATASET_KEY = "key";
    private static final String DATASET_VALUE = "value";
    public static final int SGIN_INTYPE = 0;// 签到
    public static final int SGIN_OUTTYPE = 1;// 签退
    public static final int SGIN_DEFAULTDATA = 2;// 修改页面信息
    public static final int SGIN_ZIPPHOTO = 3;// 压缩照片
    public static final int UP_ZIPPHOTO = 4;// 上传照片
    public static final int DIALOG_SHOW = 5;// 提示框开启
    public static final int DIALOG_DISMISS = 6;// 提示框关闭
    public static final int UPLOAD_INFO = 7;// 上传失败提示
    // private LinearLayout lay_sginlocath;// 加载图片的控件
    private HorizontalListView sgin_horizon_listview;
    private BusinessSginImageViewAdapter businessSginImageViewAdapter;
    private List<Bitmap> sginlocathList_Bitmap = new ArrayList<>();// 存储照片的集合
    private List<String> sginPhotoList = new ArrayList<>();// 保存图片路径
    private List<String> sginPhotoNameList = new ArrayList<>();// 图片名称
    private List<File> siginPhotoList = new ArrayList<>();
    private String sginphotoZIPPath = null;
    private File sginPhotoZIP = null;
    private boolean sgin_out = true;
    private File sgin_photoFile = null;

    public static String sgin_Type;
    private TextView must_ddisp;
    private Ej1345 ej;
    private Boolean isLate;
    private Boolean isRetreat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        setContentView(R.layout.activity_business_ddisplocath);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("E佳考勤");
        waitDialogRectangle = new WaitDialogRectangle(this);
        uuid = StringUtil.getMyUUID();
        initBdLocath();
        initView();
    }

    /**
     * 初始化定位
     */
    private void initBdLocath() {
        setHandlerInfoNoDialog(DIALOG_SHOW, "正在定位");
        mLocationClient = new LocationClient(this);
        myLocationListener = new BusinessEJActivity.MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    private void initView() {
        locath_sign_in = (RadioButton) findViewById(R.id.locath_sign_in);
        locath_sign_in.setText("上班");
        locath_sign_in.setOnClickListener(this);
        locath_sign_out = (RadioButton) findViewById(R.id.locath_sign_out);
        locath_sign_out.setText("下班");
        locath_sign_out.setOnClickListener(this);
        ddlicath_info = (ListView) findViewById(R.id.ddisplocath_infolist);
        // lay_sginlocath = (LinearLayout) findViewById(R.id.lay_sginlocath);
        sgin_horizon_listview = (HorizontalListView) findViewById(R.id.sgin_horizon_listview);

        sginlocath_btn = (TextView) findViewById(R.id.sginlocath_btn);
        must_ddisp = (TextView) findViewById(R.id.must_ddisp);
        must_ddisp.setVisibility(View.GONE);
        sginlocath_btn.setOnClickListener(this);
        // 查询个人信息
        myInfo = QiXinBaseDao.queryCurrentUserInfo();

        sgin_userID = myInfo.username;

        ej = Constant.ej1345;

        if (ej != null) {
            // 设置默认的显示值
            initListData(ej.getName_user(), ej.getName_dept(), ej.getVar_on(), content_practicalSginTime, ej.getVar_off(), content_practicaOutlTime);
        } else {
            // 设置默认的显示值
            initListData(content_userID, content_userDept, content_practicalSginTime, content_planSginTime, content_practicaOutlTime, content_practicaOutlTime);
        }

        setSginImage();

    }

    /**
     * @param useridentityId    姓名
     * @param userDept          公司
     * @param userplanSginTime  计划上班时间
     * @param practicalSginTime 实际上班时间
     * @param userplanOutlTime  计划下班时间
     * @param practicaOutlTime  实际下班时间
     */
    private void initListData(String useridentityId, String userDept,
                              String userplanSginTime, String practicalSginTime,
                              String userplanOutlTime, String practicaOutlTime) {
        list_title.clear();
        list_content.clear();
        lsit_icon.clear();

        list_title.add(title_userID);
        list_content.add(useridentityId);
        lsit_icon.add(R.drawable.ddisp_user_icon);

        list_title.add(title_userDept);
        list_content.add(userDept);
        lsit_icon.add(R.drawable.location_icon);

        list_title.add(title_planSginlTime);
        list_content.add(userplanSginTime);
        lsit_icon.add(R.drawable.contact_time_icon);

        list_title.add(title_practicaSginlTime);
        list_content.add(practicalSginTime);
        lsit_icon.add(R.drawable.contact_time_icon);

        list_title.add(title_planOutlTime);
        list_content.add(userplanOutlTime);
        lsit_icon.add(R.drawable.contact_time_icon);


        list_title.add(title_practicaOutlTime);
        list_content.add(practicaOutlTime);
        lsit_icon.add(R.drawable.contact_time_icon);


        BusinessDdispLocathAdapter businessDdispLocathAdapter = new BusinessDdispLocathAdapter(list_title, list_content, lsit_icon);
        businessDdispLocathAdapter.notifyDataSetChanged();
        ddlicath_info.setAdapter(businessDdispLocathAdapter);
        locath_sign_in.setClickable(true);
        locath_sign_out.setClickable(true);
    }

    /**
     * 设置签到界面的照片
     */
    private void setSginImage() {
        sginlocathList_Bitmap.clear();
        InputStream is = getResources().openRawResource(R.drawable.chat_normal_ddisp);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        sginlocathList_Bitmap.add(mBitmap);

        businessSginImageViewAdapter = new BusinessSginImageViewAdapter(
                sginlocathList_Bitmap, this);
        businessSginImageViewAdapter.notifyDataSetChanged();
        sgin_horizon_listview.setAdapter(businessSginImageViewAdapter);
        sgin_horizon_listview.setOnItemClickListener(clickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sginlocath_btn:
                // ShowPickDialog();
                break;
            case R.id.locath_sign_out:
                if (isNetworkConnected(this)) {
                    sgin_out = false;
                    setLocathInfo(SGIN_OUTTYPE);
                } else {
                    ToastUtil.ShowLong(this,
                            getResources().getString(R.string.net_connect_error));
                }
                break;
            case R.id.locath_sign_in:
                if (isNetworkConnected(this)) {
                    sgin_out = true;
                    setLocathInfo(SGIN_INTYPE);
                } else {
                    ToastUtil.ShowLong(this,
                            getResources().getString(R.string.net_connect_error));
                }
                break;
            default:
                break;
        }

    }

    /**
     * 判断网络是否链接
     *
     * @param context
     * @return
     */
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 设置签到, 页面的内容
     */
    private void setLocathInfo(int sginType) {
        // 发送消息
        Message msg = new Message();
        msg.what = sginType;
        mHandler.sendMessage(msg);
    }

    /**
     * 消息发送
     */
    private void setHandlerInfo(int sginType, Object content) {
        // 发送消息
        Message msg = new Message();
        msg.what = sginType;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }

    /**
     * 消息发送
     */
    private void setHandlerInfoNoDialog(int sginType, Object content) {
        // 发送消息
        Message msg = new Message();
        msg.what = sginType;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }

    /**
     * 弹框、上传、提示
     */
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SGIN_INTYPE:
                    sendMessageSginLocath("Y");
                    break;
                case SGIN_OUTTYPE:
                    sendMessageSginLocath("N");
                    break;
                case SGIN_ZIPPHOTO:
                    List<Bitmap> sginlocathListBp = (List<Bitmap>) msg.obj;
                    addImageView(sginlocathListBp);
                    break;
                case UP_ZIPPHOTO:
//                    sendPhotoFile();
                    break;
                case SGIN_DEFAULTDATA:
                    if (ej != null) {
                        // 设置默认的显示值
                        if (sgin_Type.equals("Y")) {
                            initListData(ej.getName_user(), ej.getName_dept(), ej.getVar_on(), sgin_data, ej.getVar_off(), content_practicaOutlTime);
                        } else {
                            initListData(ej.getName_user(), ej.getName_dept(), ej.getVar_on(), content_practicalSginTime, ej.getVar_off(), sgin_data);
                        }
                    } else {
                        // 设置默认的显示值
                        initListData(content_userID, content_userDept, content_practicalSginTime, content_planSginTime, content_practicaOutlTime, content_practicaOutlTime);
                    }

                    setSginImage();
                case DIALOG_SHOW:
                    waitDialogRectangle.show();
                    String show_content = (String) msg.obj;
                    if (StringUtil.isStrTrue(show_content)) {
                        waitDialogRectangle.setText(show_content);
                    }
                    break;
                case DIALOG_DISMISS:
                    String dismiss_content = (String) msg.obj;
                    if (StringUtil.isStrTrue(dismiss_content)) {
                        waitDialogRectangle.setText(dismiss_content);
                    }
                    if (waitDialogRectangle != null) {
                        waitDialogRectangle.dismiss();
                    }
                    break;
                case UPLOAD_INFO:
                    String erro_info = (String) msg.obj;
                    if (StringUtil.isStrTrue(erro_info)) {
                        ToastUtil.ShowLong(getApplicationContext(), erro_info);
                    }
                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 添加多张照片
     *
     * @param sginList
     */
    private void addImageView(List<Bitmap> sginList) {
        businessSginImageViewAdapter = new BusinessSginImageViewAdapter(
                sginList, this);
        businessSginImageViewAdapter.notifyDataSetChanged();
        sgin_horizon_listview.setAdapter(businessSginImageViewAdapter);
        sgin_horizon_listview.setOnItemClickListener(clickListener);
    }

    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position == sginlocathList_Bitmap.size() - 1) {
                ShowPickDialog();
            } else {
                ShowSginPhotoFeleteDialog(position);
            }
        }

    };

    /**
     * 提示是否删除照片
     */
    private void ShowSginPhotoFeleteDialog(final int position) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("是否删除当前照片?")

                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sginlocathList_Bitmap.remove(position);
                        businessSginImageViewAdapter.notifyDataSetChanged();
                        String sginPath = sginPhotoList.get(position);
                        File file = new File(sginPath);
                        if (file.exists()) {
                            if (file.delete()) {
                                sginPhotoList.remove(position);
                            }
                        }
                        sginPhotoNameList.remove(position);
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        }).show();
    }

    /**
     * 上传图片
     */
//    private void sendPhotoFile() {
//
//        List<File> files = new ArrayList<File>();
//        // 文件夹压缩的类型
//        sginphotoZIPPath = Constant.SGIN_SAVE_DIR + "/" + uuid + ".zip";
//        sginPhotoZIP = new File(sginphotoZIPPath);
//
//        // 要压缩的文件目录
//        File sgin_path = new File(Constant.SGIN_SAVE_DIR + "/" + uuid);
//        File[] fileName = sgin_path.listFiles();
//        if (fileName.length > 0) {
//            for (int i = 0; i < fileName.length; i++) {
//                files.add(fileName[i]);
//            }
//        }
//        try {
//            ZipUtils.zipPhotoFiles(files, sginPhotoZIP);
//            new Thread() {
//
//                @Override
//                public void run() {
//                    super.run();
//                    sendZipFile(sginphotoZIPPath);
//                }
//
//            }.start();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 获取相关数据并加载
     */
    @SuppressLint("SimpleDateFormat")
    private void sendMessageSginLocath(String isSdin) {
        locath_sign_in.setClickable(false);
        locath_sign_out.setClickable(false);
        // 获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat(Constant.SGIN_FORMART);
        Date curDate = new Date(System.currentTimeMillis());
        sgin_data = formatter.format(curDate);

        if (!StringUtil.isStrTrue(ej.getId_user())) {
            ToastUtil.ShowLong(this, "个人信息获取失败,请到设置下载基础数据");
            // 设置默认的显示值
            initListData(ej.getName_user(), ej.getName_dept(), ej.getVar_on(), content_practicalSginTime, ej.getVar_off(), content_practicaOutlTime);
            waitDialogRectangle.dismiss();
            return;
        }

        if (!StringUtil.isStrTrue(sgin_data)) {
            ToastUtil.ShowLong(this, "时间获取失败");
            initListData(ej.getName_user(), ej.getName_dept(), ej.getVar_on(), content_practicalSginTime, ej.getVar_off(), content_practicaOutlTime);
            waitDialogRectangle.dismiss();
            return;
        }
        setSginDate(isSdin);
    }

    private void setSginDate(String isSdin) {
        BusinessTimeUtils.getOutCompareAddTime(sgin_data);
        String photoName = setPhotoName();
        // ToastUtil.ShowShort(this, photoName);
        sgin_Type = isSdin;
        if (isSdin.equals("Y")) {
            initListData(ej.getName_user(), ej.getName_dept(), ej.getVar_on(), sgin_data, ej.getVar_off(), content_practicaOutlTime);
            if (StringUtil.isStrTrue(ej.getVar_on())) {
                isLate = BusinessTimeUtils.getSginCompare(sgin_data, ej.getVar_on());
            } else {
                ToastUtil.ShowLong(this, "计划上班时间获取失败");
            }
        } else {
            initListData(ej.getName_user(), ej.getName_dept(), ej.getVar_on(), content_practicalSginTime, ej.getVar_off(), sgin_data);
            if (StringUtil.isStrTrue(ej.getVar_on())) {
                isRetreat = BusinessTimeUtils.getOutCompare(sgin_data, ej.getVar_off());
            } else {
                ToastUtil.ShowLong(this, "计划下班时间获取失败");
            }
        }

        sginNewFile(ej.getId_user(), ej.getId_com(), sgin_data, ej.getId_clerk(), sgin_data, ej.getId_user(), sgin_address, isSdin);
    }


    /**
     * @param id_user
     * @param id_com
     * @param date_checkin
     * @param id_clerk
     * @param date_opr
     * @param id_recorder
     * @param var_equno
     * @param card_no
     */
    private void sginNewFile(String id_user, String id_com, String date_checkin, String id_clerk, String date_opr, String id_recorder, String var_equno, String card_no) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[{key:{\"tableid\":\"ctlm7162\",\"tabletype\":\"B\",\"opr\":\"SS\",\"no\":\"id_clerk\",\"userid\":\"" + id_user + "\",\"comid\":\"" + id_com + "\",");
            stringBuffer.append("\"data\":[{\"table\": \"ctlm7162\",\"where\":\" \",");
            stringBuffer.append("\"data\": [{\"column\":\"flag_checkin\",\"value\":\"" + String.valueOf(4) + "\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"date_checkin\",\"value\":\"" + date_checkin + "\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + id_clerk + "\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + date_opr + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_recorder + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_location\",\"value\":\"" + var_equno + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"flag_signin\",\"value\":\"" + card_no + "\",\"datatype\":\"varchar\"}]}]},value:\"\"}]");

            String str = stringBuffer.toString();
            JSONArray jsonArray = new JSONArray(str);
            onhjupload(jsonArray);

            Log.v("show", stringBuffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String setPhotoName() {
        try {
            if (sginPhotoNameList.size() >= 10) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < 10; i++) {
                    if (i == (sginPhotoNameList.size() - 1)) {
                        stringBuffer.append(sginPhotoNameList.get(i) + ".jpg");
                    } else {
                        stringBuffer.append(sginPhotoNameList.get(i) + ".jpg;");
                    }
                }
                String str = stringBuffer.toString();
                return str;
            } else if (sginPhotoNameList.size() > 0
                    && sginPhotoNameList.size() < 10) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < sginPhotoNameList.size(); i++) {
                    if (i == (sginPhotoNameList.size() - 1)) {
                        stringBuffer.append(sginPhotoNameList.get(i) + ".jpg");
                    } else {
                        stringBuffer.append(sginPhotoNameList.get(i) + ".jpg;");
                    }
                }
                String str = stringBuffer.toString();
                return str;
            }
        } catch (IndexOutOfBoundsException e) {
            Log.v("show", "数组下标越界");
        }
        return null;
    }

    /**
     * 上传表单
     *
     * @param args
     */
    public void onhjupload(JSONArray args) {
        waitDialogRectangle.show();
        // 上传的代码
        JSONObject obj = args.optJSONObject(0);
        if (obj != null) {
            try {
                List<File> files = new ArrayList<>();
                String json = obj.getString(DATASET_KEY);
                String file_list = obj.getString(DATASET_VALUE);
                sginCreateFilePath = Constant.SGIN_SAVE_DIR + "/" + uuid + "/" + uuid + ".txt";
                sginCreateFile = new File(sginCreateFilePath);
                if (!"".equals(file_list)) {
                    for (String name : file_list.split(",")) {
                        files.add(new File("file://" + Constant.SGIN_SAVE_DIR + "/" + uuid + name));
                    }
                }

                files.add(sginCreateFile);
                if (siginPhotoList.size() > 0) {
                    for (int i = 0; i < siginPhotoList.size(); i++) {
                        files.add(siginPhotoList.get(i));
                    }
                }
                try {
                    // 将签到文件写入到txt
                    FileUtils.writeStringToFile(sginCreateFile, "business" + json, "utf-8");
                    // 签到文件压缩地址
                    sginCreateFileZIPPath = Constant.SGIN_SAVE_DIR + "/" + uuid + ".zip";
                    sginCreateFileZIP = new File(sginCreateFileZIPPath);
                    setHandlerInfoNoDialog(DIALOG_SHOW, "开始上传数据");
                    if (ZipUtils.zipPhotoFiles(files, sginCreateFileZIP)) {
                        sendZipFile(sginCreateFileZIPPath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 地理位置获取失败 提示是否选择拍照代替
     */
    private void ShowSginPhotoDialog() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("地址获取失败")

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setHandlerInfo(DIALOG_DISMISS, "");
//				ShowPickDialog();
                    }
                }).setPositiveButton("继续定位", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                setHandlerInfo(DIALOG_DISMISS, "");
                if (sgin_out) {
                    setLocathInfo(SGIN_INTYPE);
                } else {
                    setLocathInfo(SGIN_OUTTYPE);
                }
            }
        }).show();
    }

    /**
     * 选择照片类型提示对话框
     */
    @SuppressLint("SimpleDateFormat")
    private void ShowPickDialog() {
        // 获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat(
                Constant.SGIN_PHOTONAME);
        Date curDate = new Date(System.currentTimeMillis());
        sginPhotoName = "ph" + formatter.format(curDate) + myInfo.userID;
        sginPhotoFile = Constant.SGIN_SAVE_DIR + "/" + uuid + "/" + sginPhotoName + ".jpg";

        sginSaveFile = new File(Constant.SGIN_SAVE_DIR + "/" + uuid);
        if (!sginSaveFile.exists()) {
            sginSaveFile.mkdirs();
        }

        // 跳转相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        sginSavePhotoFile = new File(sginPhotoFile);
        siginPhotoList.add(sginSavePhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(sginSavePhotoFile));
        startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            ToastUtil.ShowLong(this, "获取图片失败,请重试");
            sginlocathList_Bitmap.clear();
            InputStream is = getResources().openRawResource(R.drawable.chat_normal_ddisp);
            Bitmap mBitmap = BitmapFactory.decodeStream(is);
            sginlocathList_Bitmap.add(mBitmap);
            return;
        }
        switch (requestCode) {
            case Activity.DEFAULT_KEYS_DIALER: {
                try {
                    sginPhotoList.add(sginPhotoFile);
                    sginPhotoNameList.add(sginPhotoName);

                    Bitmap decodeBitmap = decodeBitmap(sginSavePhotoFile);
                    decodeBitmap.compress(Bitmap.CompressFormat.JPEG, 60,
                            new FileOutputStream(sginSavePhotoFile));
                    sginlocathList_Bitmap.add(sginlocathList_Bitmap.size() - 1,
                            decodeBitmap);
                    setHandlerInfo(SGIN_ZIPPHOTO, sginlocathList_Bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public Bitmap decodeBitmap(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        // 缩放比
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 800);
        if (scale <= 0) {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        return bitmap;
    }

    /**
     * 上传签到压缩文件
     *
     * @param name
     */
    private void sendZipFile(final String name) {
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("download", new FileBody(new File(name)));
        HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP
                + Constant.BUSINESS_SERVICE_ADDRESS);
        Log.v("show", "上传服务地址：" + EapApplication.URL_SERVER_HOST_HTTP
                + Constant.BUSINESS_SERVICE_ADDRESS + entity.toString());
        httpPost.setEntity(entity);
        HttpClientManager.addTask(new HttpClientManager.HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {
                    String msga = HttpClientManager.toStringContent(resp);
                    try {
                        JSONObject jsonObject = new JSONObject(msga);
                        String result = jsonObject.getString("flag");
                        Log.v("show", "上传返回的数据：" + jsonObject.toString());
                        if ("ok".equalsIgnoreCase(result)) {
                            // 上传成功设置默认的显示值
                            if (sgin_out) {
                                if (isLate) {
                                    succeedInfomation("新的一天开始了，要努力工作哦！");
                                } else {
                                    succeedInfomation("本次打卡异常");
                                }
                            } else {
                                if (isRetreat) {
                                    succeedInfomation("工作辛苦了，再见！");
                                } else {
                                    succeedInfomation("打卡成功，不要早退哦！");
                                }
                            }
                        } else {
                            // 上传失败
                            erroInfoMation();
                            Log.v("show", "上传失败");
                        }
                    } catch (JSONException e) {
                        erroInfoMation();
                        Log.v("show", " JSONException 抛出异常 上传失败");
                    }
                } catch (IOException e) {
                    erroInfoMation();
                    Log.v("show", " IOException 抛出异常 上传失败");
                }
            }

            public void onException(Exception e) {
                erroInfoMation();
                Log.v("show", "访问异常");
            }
        }, httpPost);
    }

    /**
     * 上传成功
     */

    private void succeedInfomation(String content) {
        setHandlerInfoNoDialog(DIALOG_SHOW, content);
        setHandlerInfoNoDialog(UPLOAD_INFO, content);
        setHandlerInfo(DIALOG_DISMISS, "");
        deleteSaveFile();
    }

    /**
     * 上传错误的信息
     */
    private void erroInfoMation() {
        setHandlerInfoNoDialog(DIALOG_DISMISS, "上传失败");
        deleteSaveFile();
    }

    public void deletePhotoFiles(String ph_path) {
        File file = new File(ph_path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 上传失败或者成功的时候清理压缩的文件
     */
    private void deleteSaveFile() {
        // 删除本地照片，清空路径
        if (StringUtil.isStrTrue(sginPhotoFile)) {
            sginSavePhotoFile.delete();
            sginPhotoFile = null;
            sginPhotoName = null;
        }
        if (StringUtil.isStrTrue(sginCreateFilePath)) {
            sginCreateFile.delete();
            sginCreateFilePath = null;
        }
        if (StringUtil.isStrTrue(sginCreateFileZIPPath)) {
            sginCreateFileZIP.delete();
            sginCreateFileZIPPath = null;
        }

        if (StringUtil.isStrTrue(sginphotoZIPPath)) {
            sginPhotoZIP.delete();
            sginphotoZIPPath = null;
        }

        if (sginPhotoNameList.size() > 0) {
            sginPhotoNameList.clear();
        }

        if (siginPhotoList.size() > 0) {
            sginSaveFile.delete();
            sginSaveFile = null;
            siginPhotoList.clear();
        }


        if (sginPhotoList.size() > 0) {
            for (int i = 0; i < sginPhotoList.size(); i++) {
                File file = new File(sginPhotoList.get(i));
                file.delete();
            }
            sginPhotoList.clear();
        }
        sginlocathList_Bitmap.clear();

        setLocathInfo(SGIN_DEFAULTDATA);
        setHandlerInfo(DIALOG_DISMISS, "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开启定位的允许
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭定位
        mLocationClient.stop();
    }

    /**
     * 销毁文件及照片
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        sgin_photoFile = new File(Constant.SGIN_SAVE_DIR + "/" + uuid);
        if (sgin_photoFile.exists()) {
            String[] strings = sgin_photoFile.list();
            for (int i = 0; i < strings.length; i++) {
                File delFile = new File(Constant.SGIN_SAVE_DIR + "/" + uuid + "/" + strings[i]);
                delFile.delete();
            }
        }
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 地理位置信息返回结果
            sgin_address = bdLocation.getAddrStr();
            sginLatitude = bdLocation.getLatitude();
            sginLongitude = bdLocation.getLongitude();
//            Log.v("show",
//                    bdLocation.getAddrStr() + "," + bdLocation.getLatitude()
//                            + "," + bdLocation.getLongitude());
            if (StringUtil.isStrTrue(sgin_address)) {
                setHandlerInfo(DIALOG_DISMISS, "定位成功");
            } else {
                setHandlerInfo(DIALOG_DISMISS, "定位失败");
            }
        }

    }
}

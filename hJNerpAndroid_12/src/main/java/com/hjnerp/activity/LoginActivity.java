package com.hjnerp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BaseDao;
import com.hjnerp.dao.OtherBaseDao;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.Tables;
import com.hjnerp.model.BaseData;
import com.hjnerp.model.IDComConfig;
import com.hjnerp.model.LoginConfig;
import com.hjnerp.model.UserInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.myscom.StringUtils;
import com.hjnerp.widget.SelectPopupWindow;
import com.hjnerp.widget.SelectText;
import com.hjnerpandroid.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.Date;
import java.util.List;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登录.
 *
 * @author 李庆义 使用google官方库appcompat_v7支持Actionbar
 */

public class LoginActivity extends ActivitySupport {

    private EditText edt_username, edt_pwd;
    private SelectText edt_company;
    private Button btn_login = null;
    private TextView tv_register;
    private String companyname = "";
    private String username = "";
    private Drawable mIconEdittextClear, mIconUser, mIconPWd;// /搜索文本框默认图标
    private List<IDComConfig> listConfig = null;// new ArrayList<IDComConfig>();
    private LoginConfig loginConfig;
    private int type;
    Toast mToast;
    private ImageView myImageView;
    Handler mHandler;
    private UserInfo myinfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .request();
        isReceiver = false;
        mIconEdittextClear = getResources().getDrawable(R.drawable.reader_news_fontcancel_pressed);
        mIconUser = getResources().getDrawable(R.drawable.creat_username_slt);
        mIconPWd = getResources().getDrawable(R.drawable.icon_register_password);
        loginConfig = new LoginConfig();
        mHandler = new Handler();
        // 应用程序崩溃报告 开发测时可以关掉
//        TestinAgent.init(context, "3bc83bfdc90d487405c53d2f9ed011ae", "");
        if (!sputil.isForceExit()
                || StringUtils.isBlank(sputil.getMySessionId())) {
            init();
            regist();
        } else {
            setContentView(R.layout.splash);
            setServerURL(sputil.getComid());
            // 判断是几号
//            boolean isConfir = DateUtil.getCurrentDay() == 1
//                    || DateUtil.getCurrentDay() == 10
//                    || DateUtil.getCurrentDay() == 20;
            Boolean isConfir = true;//去掉日期判断，仅限美正版本
//            Log.d("isconfir", isConfir + "");
            if (isConfir) {
                /**
                 * 进行验证看时间是否过期
                 */
//				waitDialogRectangle.show();
                regist();
                type = 0;
            } else {
                AutoLoginTask loginTask = new AutoLoginTask(LoginActivity.this, sputil);
                loginTask.execute();
            }

        }
    }

    private void prepareComData() {
        listConfig = OtherBaseDao.queryAllRegInfos();
        IDComConfig idconfig = OtherBaseDao.queryReginfo(sputil.getComid());
        if (idconfig != null) {
            edt_company.setText(idconfig.getName_com());
        }
        edt_company.setPopup(new SelectPopupWindow(LoginActivity.this,
                listConfig, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                sputil.setComid(listConfig.get(position).getId_com());
                edt_company.setText(listConfig.get(position).getName_com());
                edt_company.getPopup().dismiss();
            }
        }));
        // }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 校验SD卡
        checkMemoryCard();
        // 检测网络和版本
        validateInternet();
        // registerReceiver();

    }

    /**
     * 初始化.
     *
     * @author 李庆义
     * @update 2012-5-16 上午9:13:01
     */
    protected void init() {
        // mActionBar.show();
        setContentView(R.layout.login);
        // ActionBar mActionBar = getSupportActionBar();
        // mActionBar.setHomeButtonEnabled(true);
        // mActionBar.setIcon(R.drawable.icon2); //manifest已经设置android:logo

        edt_company = (SelectText) findViewById(R.id.ui_company_input);
        edt_username = (EditText) findViewById(R.id.ui_username_input);
        edt_pwd = (EditText) findViewById(R.id.ui_password_input);
        btn_login = (Button) findViewById(R.id.ui_login_btn);
        tv_register = (TextView) findViewById(R.id.ui_register_tv);

        btn_login.setOnClickListener(onClickListener);
        tv_register.setOnClickListener(onClickListener);
        myImageView = (ImageView) findViewById(R.id.myphoto);
        edt_company.addTextChangedListener(textWatcher);
        edt_username.addTextChangedListener(textWatcher);
        edt_pwd.addTextChangedListener(textWatcher);

        edt_username.setOnTouchListener(editTextOnTouch);
        edt_pwd.setOnTouchListener(editTextOnTouch);

        edt_username.setOnFocusChangeListener(mFocusChangeListener);
        edt_pwd.setOnFocusChangeListener(mFocusChangeListener);

        myinfo = QiXinBaseDao.queryCurrentUserInfo();

        if (myinfo != null) {
            String url = myinfo.userImage;
            if (!StringUtil.isNullOrEmpty(url)) {
                ImageLoaderHelper.displayImage(ChatPacketHelper.buildImageRequestURL(url, ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),
                        myImageView);
            }
        }

        prepareComData();

        if (!StringUtil.isNullOrEmpty(sputil.getMyUserId())) {
            edt_username.setText(sputil.getMyUserId());
            edt_username.setTextColor(getResources().getColor(R.color.black));
        }

        closeInput();
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ui_login_btn:
//                    handlLogin();
                    boolean isConfir = DateUtil.getCurrentDay() == 1
                            || DateUtil.getCurrentDay() == 10
                            || DateUtil.getCurrentDay() == 20;
//                    isConfir = true;
                    Log.d("isconfir", isConfir + "");
                    if (isConfir) {
                        /**
                         * 进行验证看时间是否过期
                         */
                        waitDialogRectangle.show();
                        waitDialogRectangle.setText("正在验证...");
//                        regist();
                        handlLogin();
                        type = 1;
                    } else {
                        // 改到登录成功时清楚数据
                        handleData();
                        setServerURL(sputil.getComid());
                        loginConfig.setPassword(edt_pwd.getText().toString());
                        loginConfig.setUsername(username);
                        loginConfig.setComid(sputil.getComid());
                        LoginTask loginTask = new LoginTask(LoginActivity.this, loginConfig, sputil);
                        loginTask.execute();
                    }
                    break;
                case R.id.ui_register_tv:
                    if (validateInternet()) {
                        forwardRegister();
                    } else {
                        ToastUtil.ShowLong(LoginActivity.this, getResources()
                                .getString(R.string.net_connect_error));
                    }
                    break;

                default:
                    break;
            }

        }

    };

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            companyname = edt_company.getText().toString();
            username = edt_username.getText().toString();
            username = StringUtil.lowerCaseString(username);

            if (companyname.length() > 0) {
                edt_company.setTextColor(getResources().getColor(R.color.login_input_normal));
            } else {
                edt_company.setTextColor(getResources().getColor(R.color.login_input_notice));
            }
            if (username.length() > 0 && edt_username.hasFocus()) {
                edt_username.setTextColor(getResources().getColor(R.color.login_input_normal));
                edt_username.setCompoundDrawablesWithIntrinsicBounds(mIconUser,
                        null, mIconEdittextClear, null);
            } else if (username.length() == 0) {
                edt_username.setTextColor(getResources().getColor(
                        R.color.login_input_notice));
                edt_username.setCompoundDrawablesWithIntrinsicBounds(mIconUser,
                        null, null, null);
            }
            String password = edt_pwd.getText().toString();
            if (password.length() > 0 && edt_pwd.hasFocus()) {
                edt_pwd.setTextColor(getResources().getColor(
                        R.color.login_input_normal));
                edt_pwd.setCompoundDrawablesWithIntrinsicBounds(mIconPWd, null,
                        mIconEdittextClear, null);
            } else if (password.length() == 0) {
                edt_pwd.setTextColor(getResources().getColor(
                        R.color.login_input_notice));
                edt_pwd.setCompoundDrawablesWithIntrinsicBounds(mIconPWd, null,
                        null, null);
            }

            if (companyname.length() > 0 && username.length() > 0
                    && password.length() > 0) {
                btn_login.setEnabled(true);
            } else {
                btn_login.setEnabled(false);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private OnTouchListener editTextOnTouch = new OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            EditText met = (EditText) findViewById(v.getId());
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (met.hasFocus()) {
                        int curX = (int) event.getX();
                        if (curX > v.getWidth()
                                - mIconEdittextClear.getIntrinsicWidth() - 10
                                && !TextUtils.isEmpty(met.getText())) {
                            met.setText("");
                            int cacheInputType = met.getInputType();// backup the
                            // input
                            // type
                            met.setInputType(InputType.TYPE_NULL);// disable soft
                            // input
                            met.onTouchEvent(event);// call native handler
                            met.setInputType(cacheInputType);// restore input type
                            return true;// consume touch even
                        }
                    }
                    break;
            }
            return false;
        }
    };

    private OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {

            switch (v.getId()) {
                // edt_username, edt_pwd;
                case R.id.ui_username_input:

                    if (username.length() > 0 && hasFocus) {
                        edt_username.setCompoundDrawablesWithIntrinsicBounds(
                                mIconUser, null, mIconEdittextClear, null);
                    } else {
                        edt_username.setCompoundDrawablesWithIntrinsicBounds(
                                mIconUser, null, null, null);
                    }

                    break;

                case R.id.ui_password_input:
                    String password = edt_pwd.getText().toString();
                    if (password != null && password.length() > 0 && hasFocus) {
                        edt_pwd.setCompoundDrawablesWithIntrinsicBounds(mIconPWd,
                                null, mIconEdittextClear, null);
                    } else {
                        edt_pwd.setCompoundDrawablesWithIntrinsicBounds(mIconPWd,
                                null, null, null);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // 判断登陆前是否清除数据，如果用户重新登陆的账号和上次的账号一样，则不删除
    private void handleData() {
        UserInfo mInfo = QiXinBaseDao.queryCurrentUserInfo();
        if (mInfo != null && username.equalsIgnoreCase(mInfo.userID.trim())) {
            // Log.i(TAG,"不删除数据*****************8");
        } else {
            // Log.i(TAG,"删除数据******************");

            QiXinBaseDao.updateUserInfo(sputil.getMyUserId(),
                    Tables.UserTable.COL_VAR_SESSION, "");
            // 清楚除用户表以外的db数据
            BaseDao.clearToBase();
            // 设置sp
            // sputil.setWorkListBillType(null);
            // sputil.clearSharePreference();
        }

    }

    void forwardRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0: {
                switch (resultCode) {
                    case RESULT_OK: {
                        listConfig = OtherBaseDao.queryAllRegInfos();
                        if (listConfig != null) {
                            edt_company.setText(listConfig.get(0).getName_com());
                            sputil.setComid(listConfig.get(0).getId_com());
                            edt_company.setPopup(new SelectPopupWindow(
                                    LoginActivity.this, listConfig,
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            sputil.setComid(listConfig.get(position).getId_com());
                                            edt_company.setText(listConfig.get(position).getName_com());
                                            edt_company.getPopup().dismiss();
                                        }
                                    }));
                        }

                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
                break;
        }
    }

    public static final void setServerURL(String comID) {
        IDComConfig idconfig = OtherBaseDao.queryReginfo(comID);
        if (idconfig != null) {
            EapApplication.URL_SERVER_HOST_HTTP = idconfig.getUrl_http();
//            EapApplication.URL_SERVER_HOST_HTTP = "http://172.16.12.243:8080/hjmerp";
            // 测试地址
            // EapApplication.URL_SERVER_HOST_HTTP =
            // "http://192.168.0.114:8080/hjmerp";
            Log.i("info", "LoginActivity登陆地址：" + EapApplication.URL_SERVER_HOST_HTTP);
            // EapApplication.URL_SERVER_HOST_HTTP =
            // "http://pda.hqbeer.com:1013/hjmerp";

            // EapApplication.URL_SERVER_HOST_HTTP =
            // "http://192.168.0.54:8080/hjmerp";
        }
    }

    /**
     * 时间验证
     * 查询软件是否过期
     *
     * @author haijian
     */
    private void regist() {
//        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
//        parameters.add(new BasicNameValuePair("phoneId", sputil.getRegistId()));
//        parameters
//                .add(new BasicNameValuePair("valiadId", sputil.getRegistNub()));
//        parameters.add(new BasicNameValuePair("actionType", "mobileInit"));
//        HttpPost request = new
//                HttpPost("http://register.hejia.cn:8090/nerp/hjMobile");
////        Log.i("regist", "phoneId：" + sputil.getRegistId()+"valiadId：" + sputil.getRegistNub());
////		HttpPost request = new HttpPost(
////				"http://172.16.12.26:8095/nerp/hjMobile");
//        try {
//            request.setEntity(new UrlEncodedFormEntity(parameters));
//        } catch (UnsupportedEncodingException e) {
//            Log.e(null, "", e);
//        }
//        HttpClientManager.open();
//        HttpClientManager.addTask(new RegisterResponseHandler(), request);
        OkGo.getInstance().setConnectTimeout(5000).setRetryCount(0);
        OkGo.post("http://register.hejia.cn:8090/nerp/hjMobile")
//        OkGo.post("http://172.16.12.27:8090/nerp/hjMobile")
                .params("phoneId", sputil.getRegistId())
                .params("valiadId", sputil.getRegistNub())
                .params("actionType", "mobileInit")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("okgo", "s:" + s + ",response:" + response);
                        Gson gson = new Gson();
                        BaseData data = gson.fromJson(s, BaseData.class);
                        if (data.isSuccess()) {
                            /**
                             * 检查当前时间是什么状态 正常 警告 停止
                             */
                            List<Map<String, Object>> dataList = data.getDataList();
                            // 获取停止使用时间
                            String dateStopUse = dataList.get(0).get("dateStopUse")
                                    .toString();
                            String dateWarning = dataList.get(0).get("dateWarning")
                                    .toString();
                            long stopReg = DateUtil.StrToDate(dateStopUse).getTime();
                            long warn = DateUtil.StrToDate(dateWarning).getTime();
                            long current = new Date().getTime();
                            if (current > stopReg) {
                                showToast("当前软件已过期，请联系软件提供商！");
                                if (type == 0) {
                                    handlInit();
                                }
                            } else {
                                if (current > warn) {
                                    showToast("当前软件即将过期，请及时联系软件提供商！");
                                }
                                if (!sputil.isForceExit()
                                        || StringUtils.isBlank(sputil.getMySessionId())) {
                                    OtherBaseDao.replaceRegInfo(dataList);//验证后，将新数据保存到数据库
                                } else {
                                    // 正常登陆
                                    if (type == 0) {
                                        // 自动登陆
                                        OtherBaseDao.replaceRegInfo(dataList);//验证后，将新数据保存到数据库
                                        handlAutoLogin();
                                    } else {
                                        OtherBaseDao.replaceRegInfo(dataList);//验证后，将新数据保存到数据库
                                        handlLogin();
                                    }
                                }

                            }
                        } else {
                            if (type == 0) {
                                handlInit();
                            }
                            showToast(data.message);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (type == 0) {
                            handlInit();
                        }
                    }
                });
    }

//    class RegisterResponseHandler extends HttpClientManager.HttpResponseHandler {
//        @Override
//        public void onResponse(HttpResponse resp) {
//            try {
//                // eRegPhone));
//                // parameters.add(new BasicNameValuePair("valiadId", eRegCode)
//                dialogCancel();
//                String json = HttpClientManager.toStringContent(resp);
//                Gson gson = new Gson();
//                BaseData data = gson.fromJson(json, BaseData.class);
//                if (data.isSuccess()) {
//                    /**
//                     * 检查当前时间是什么状态 正常 警告 停止
//                     */
//                    List<Map<String, Object>> dataList = data.getDataList();
//                    // 获取停止使用时间
//                    String dateStopUse = dataList.get(0).get("dateStopUse")
//                            .toString();
//                    String dateWarning = dataList.get(0).get("dateWarning")
//                            .toString();
//                    long stopReg = DateUtil.StrToDate(dateStopUse).getTime();
//                    long warn = DateUtil.StrToDate(dateWarning).getTime();
//                    long current = new Date().getTime();
//                    if (current > stopReg) {
//                        showToast("当前软件已过期，请联系软件提供商！");
//                        if (type == 0) {
//                            handlInit();
//                        }
//                    } else {
//                        if (current > warn) {
//                            showToast("当前软件即将过期，请及时联系软件提供商！");
//                        }
//                        // 正常登陆
//
//                        if (type == 0) {
//                            // 自动登陆
//                            handlAutoLogin();
//                        } else {
//                            handlLogin();
//                        }
//                    }
//
//                } else {
//                    if (type == 0) {
//                        handlInit();
//                    }
//                    showToast(data.message);
//                }
//            } catch (IOException e) {
//                onException(e);
//                if (type == 0) {
//                    handlInit();
//                }
//            }
//        }
//
//
//        @Override
//        public void onException(Exception e) {
//            // 连接服务器失败
////			showToast("服务器没有反馈");
////			dialogCancel();
//            if (type == 0) {
//                handlInit();
//            }
//        }
//    }

    private void handlInit() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });

    }

    private void handlAutoLogin() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AutoLoginTask loginTask = new AutoLoginTask(
                        LoginActivity.this, sputil);
                loginTask.execute();
            }
        });
    }

    private void handlLogin() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handleData();
                setServerURL(sputil.getComid());
                loginConfig.setPassword(edt_pwd.getText().toString());
                loginConfig.setUsername(username);
                loginConfig.setComid(sputil.getComid());
                LoginTask loginTask = new LoginTask(
                        LoginActivity.this, loginConfig, sputil);
                loginTask.execute();

            }
        });

    }

    public void dialogCancel() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                waitDialogRectangle.cancel();
            }
        });
    }

    void showToast(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(msg);
                }
                mToast.show();
            }
        });
    }

    @Override
    public void saveLoginConfig(LoginConfig loginConfig) {
        // sputil.setMyPWD(loginConfig.getPassword());// 密码存储到sp
        sputil.setMyUserId(loginConfig.getUsername());
        sputil.setComid(loginConfig.getComid());
        sputil.setForceExit(false);
    }
}

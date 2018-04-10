package com.mznerp.business.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mznerp.business.BusinessJsonCallBack.BFlagCallBack;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.Ctlm1345;
import com.mznerp.model.Ej1345;
import com.mznerp.model.LeaveType;
import com.mznerp.model.PerformanceDatas;
import com.mznerp.model.businessFlag;
import com.mznerp.net.HttpClientManager;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.util.ZipUtils;
import com.mznerp.util.myscom.FileUtils;
import com.mznerp.widget.WaitDialogRectangle;
import com.mznerp.R;
import com.lidroid.xutils.util.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class LeaveBusiness extends ActivitySupport implements View.OnClickListener {

    private TextView nofocus_leave;
    private EditText leave_name;
    private EditText leave_part;
    private EditText leave_time_begin;
    private EditText leave_time_end;
    private Spinner leave_type;
    private EditText leave_days;
    private EditText leave_reason;
    private Button submit_leave;
    private Calendar c = Calendar.getInstance();
    private String name_user;
    private String id_user;
    private String name_dept;
    private String id_com;
    private String id_dept;
    private List<Ctlm1345> users;
    private String id_clerk;
    private String id_linem;
    private String id_auditlvl;
    private ArrayAdapter<String> stringArrayAdapter;
    //    private List<Ctlm1345> typesAll;
    private List<Ctlm1345> types;
    private List<LeaveType> typevalues;
    private String[] typeNames;
    private String[] typeId;
    private String[] decNo;
    private TextView have_days;
    private static final int UPLOAD_INFO = 10000;// 上传结果提示
    private WaitDialogRectangle waitDialogRectangle;
    private EditText var_rejust_name_leave;
    private LinearLayout rejust_list_leave;
    private PerformanceDatas pds;
    private PerformanceDatas.MainBean mainBean;
    private int mark_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_business);
        initData();
        initView();
    }

    private void initData() {
        users = new ArrayList<>();
        users = BusinessBaseDao.getCTLM1345ByIdTable("user");
        if (users.size() == 0) {
            ToastUtil.ShowShort(this, "请先下载基础数据");
            finish();
            return;
        }
        String userinfos = users.get(0).getVar_value();
        Gson gson1 = new Gson();
        Ej1345 ej1345 = gson1.fromJson(userinfos, Ej1345.class);
        id_clerk = ej1345.getId_clerk();
        id_user = ej1345.getId_user();
        name_user = ej1345.getName_user();
        id_dept = ej1345.getId_dept();
        name_dept = ej1345.getName_dept();
        id_com = ej1345.getId_com();
        id_linem = ej1345.getId_linem();
        id_auditlvl = ej1345.getId_auditlvl();
        types = new ArrayList<>();
        typevalues = new ArrayList<>();
        types = BusinessBaseDao.getCTLM1345ByIdTable("dgtdvat");
        typeNames = new String[types.size() + 1];
        typeNames[0] = "";
        decNo = new String[types.size() + 1];
        decNo[0] = "";
        typeId = new String[types.size() + 1];
        typeId[0] = "";
        for (int i = 0; i < types.size(); i++) {
            String typeValues = types.get(i).getVar_value();
            Gson gson = new Gson();
            LeaveType leaveType = gson.fromJson(typeValues, LeaveType.class);
            typevalues.add(leaveType);
        }
        Collections.sort(typevalues);
        for (int i = 0; i < typevalues.size(); i++) {
            typeNames[i + 1] = typevalues.get(i).getName_atttype();
            typeId[i + 1] = typevalues.get(i).getId_atttype();
            decNo[i + 1] = typevalues.get(i).getDec_no();
        }

        stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, typeNames);
        stringArrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
    }

    private void initView() {
        getSupportActionBar().show();
        getSupportActionBar().setTitle("休假申请单");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        leave_name = (EditText) findViewById(R.id.leave_name);
        leave_part = (EditText) findViewById(R.id.leave_part);
        leave_time_begin = (EditText) findViewById(R.id.leave_time_begin);
        leave_time_end = (EditText) findViewById(R.id.leave_time_end);
        leave_type = (Spinner) findViewById(R.id.leave_type);
        leave_days = (EditText) findViewById(R.id.leave_days);
        leave_reason = (EditText) findViewById(R.id.leave_reason);
        submit_leave = (Button) findViewById(R.id.submit_leave);
        have_days = (TextView) findViewById(R.id.have_days);
        submit_leave.setOnClickListener(this);
        leave_time_begin.setOnClickListener(this);
        leave_time_end.setOnClickListener(this);
        if (users.size() == 0) {
            ToastUtil.ShowShort(this, "请先下载基础数据");
            finish();
            return;
        }
        leave_name.setText(name_user.trim());
        leave_part.setText(name_dept.trim());
        leave_type.setAdapter(stringArrayAdapter);
        leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                have_days.setText(decNo[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        var_rejust_name_leave = (EditText) findViewById(R.id.var_rejust_name_leave);
        rejust_list_leave = (LinearLayout) findViewById(R.id.rejust_list_leave);
        if (!Constant.JUDGE_TYPE) {
            rejust_list_leave.setVisibility(View.VISIBLE);
            setRejustContext();
        }
    }

    private void setRejustContext() {
        pds = Constant.performanceDatas;
        mainBean = pds.getMain();
        var_rejust_name_leave.setText(mainBean.getVar_rejust());
        leave_time_begin.setText(mainBean.getDate_begin());
        leave_time_end.setText(mainBean.getDate_end());
        leave_days.setText(mainBean.getDec_leave());
        leave_reason.setText(mainBean.getVar_remark());
        for (int i = 0; i < typeId.length; i++) {
            if (typeId[i].equalsIgnoreCase(mainBean.getId_atttype())) {
                leave_type.setSelection(i, true);
                mark_type = i;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_leave:
                submit();
                break;
            case R.id.leave_time_begin:
                showtime(leave_time_begin);
                break;
            case R.id.leave_time_end:
                showtime(leave_time_end);
                break;
        }
    }

    private void showtime(final EditText editText) {
//        c = Calendar.getInstance();
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        if (month < 10 && dayOfMonth < 10) {
                            editText.setText(year + "-0" + month
                                    + "-0" + dayOfMonth);
                        } else if (month < 10 && dayOfMonth >= 10) {
                            editText.setText(year + "-0" + month
                                    + "-" + dayOfMonth);
                        } else if (month >= 10 && dayOfMonth < 10) {
                            editText.setText(year + "-" + month
                                    + "-0" + dayOfMonth);
                        } else {
                            editText.setText(year + "-" + month
                                    + "-" + dayOfMonth);
                        }

                    }
                }
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();
    }

    private void submit() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String a = f.format(c.getTime());
        String name = leave_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "休假人员不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String part = leave_part.getText().toString().trim();
        if (TextUtils.isEmpty(part)) {
            Toast.makeText(this, "部门不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String begin = leave_time_begin.getText().toString().trim();
        if (TextUtils.isEmpty(begin)) {
            Toast.makeText(this, "请选择开始休假的日期", Toast.LENGTH_SHORT).show();
            return;
        }

        String end = leave_time_end.getText().toString().trim();
        if (TextUtils.isEmpty(end)) {
            Toast.makeText(this, "请选择结束休假的日期", Toast.LENGTH_SHORT).show();
            return;
        }
        String id_type_leave = typeId[leave_type.getSelectedItemPosition()];
        String type_leave = typeNames[leave_type.getSelectedItemPosition()];
        String dec_no = decNo[leave_type.getSelectedItemPosition()];
        if (TextUtils.isEmpty(id_type_leave)) {
            Toast.makeText(this, "请选择休假类型", Toast.LENGTH_SHORT).show();
            return;
        }

        String days = leave_days.getText().toString().trim();
        if (TextUtils.isEmpty(days)) {
            Toast.makeText(this, "请输入休假天数", Toast.LENGTH_SHORT).show();
            return;
        }

        String reason = leave_reason.getText().toString().trim();
        HjUpload hjUpload = new HjUpload(this);
        try {
            if (hjUpload.compare(end, begin)) {
                ToastUtil.ShowShort(this, "开始时间不能大于结束时间。");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // TODO validate success, do something
        StringBuffer stringBuffer = new StringBuffer();
        if (!Constant.JUDGE_TYPE) {
            stringBuffer.append("{\"tableid\":\"dgtdvat\",\"opr\":\"SS\",\"no\":\"" + mainBean.getDgtdvat_no() + "\",\"userid\":\"" + id_user + "\",\"comid\":\"" + id_com + "\",");
            stringBuffer.append("\"menuid\":\"002040\",\"dealtype\":\"save\",\"data\":[");
            stringBuffer.append("{\"table\": \"dgtdvat_03\",\"oprdetail\":\"U\",\"where\":\" \",\"data\":[");
            stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_user + "\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + a + "\",\"datatype\":\"datetime\"}, ");
            stringBuffer.append("{\"column\":\"flag_psts\",\"value\":\"\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_table\",\"value\":\"\",\"datatype\":\"varchar\"},");

        }else{
            stringBuffer.append("[{key:{\"tableid\":\"dgtdvat\",\"opr\":\"SS\",\"no\":\"\",\"userid\":\"" + id_user + "\",\"comid\":\"" + id_com + "\",\"data\":[");
            stringBuffer.append("{\"table\": \"dgtdvat_03\",\"where\":\" \",\"data\":[");
        }

        stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"L\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"FBgtd\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"line_no\",\"value\":\"1\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + id_dept + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"dec_no\",\"value\":\"" + dec_no + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + id_clerk + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"id_atttype\",\"value\":\"" + id_type_leave + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"dec_leave\",\"value\":\"" + days + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"date_begin\",\"value\":\"" + begin + "\",\"datatype\":\"datetime\"},");
        stringBuffer.append("{\"column\":\"date_end\",\"value\":\"" + end + "\",\"datatype\":\"datetime\"},");
        stringBuffer.append("{\"column\":\"id_linem\",\"value\":\"" + id_linem + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_auditlvl\",\"value\":\"" + id_auditlvl + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"var_title\",\"value\":\"" + name_user + " " + type_leave + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + reason + "\",\"datatype\":\"varchar\"}]}]}");
        if (!Constant.JUDGE_TYPE) {
            String str = stringBuffer.toString();
            Log.d("str1", str);
            getBusinessList(str, "save");
            String save = "\"menuid\":\"002040\",\"dealtype\":\"save\",\"data\":[";
            String send = "\"menuid\":\"002040\",\"dealtype\":\"send\",\"data\":[";
            String finalstring = str.replace(save, send);
            Log.d("str2", finalstring);
            getBusinessList(finalstring, "send");
        } else {
            stringBuffer.append(",value:\"\"}]");
            String str = stringBuffer.toString();
            try {
                JSONArray jsonArray = new JSONArray(str);
                onhjuploada(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void onhjuploada(JSONArray args) {
//        int resultint = 0;
        // 上传的代码

        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
            waitDialogRectangle.dismiss();
        }
        waitDialogRectangle = new WaitDialogRectangle(context);
        waitDialogRectangle.setCanceledOnTouchOutside(false);
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在提交");
        JSONObject obj = args.optJSONObject(0);
        if (obj != null) {
            try {
                List<File> files = new ArrayList<>();
                String json = obj.getString("key");
                String file_list = obj.getString("value");
                String uuid = StringUtil.getMyUUID();
                File file = new File(Constant.TEMP_DIR, uuid + ".txt");
                if (!"".equals(file_list)) {
                    for (String name : file_list.split(",")) {
                        files.add(new File("file://" + Constant.HJPHOTO_CACHE_DIR + name));
                    }
                }
                files.add(file);
                try {
                    FileUtils.writeStringToFile(file, "business" + json, "utf-8");
                    File f = new File(Constant.TEMP_DIR + uuid + ".zip");
                    ZipUtils.zipFiles(files, f);
                    sendZipFile(Constant.TEMP_DIR + uuid + ".zip");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // for (Ctlm1347 ctlm1347 : ctlm1345ListFromCtlm1347) {
        // if (CHECKED.equals(ctlm1347.getFlag_upload())) {
        // billNoList.add(ctlm1347.getVar_billno());
        // idnodeList.add(ctlm1347.getId_node());
        // }
        // }
        // BusinessLua.uploadData(billNoList);
    }

    /**
     * @param name
     */
    private void sendZipFile(String name) {
//        final int[] resultint = {0};
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("download", new FileBody(new File(name)));
        HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP
                + Constant.BUSINESS_SERVICE_ADDRESS);

        LogUtils.i("上传服务地址：" + EapApplication.URL_SERVER_HOST_HTTP
                + Constant.BUSINESS_SERVICE_ADDRESS);

        httpPost.setEntity(entity);
        HttpClientManager.addTask(new HttpClientManager.HttpResponseHandler() {

            @Override
            public void onResponse(HttpResponse resp) {

                try {
                    String msga = HttpClientManager.toStringContent(resp);

                    try {
                        JSONObject jsonObject = new JSONObject(msga);
                        String result = jsonObject.getString("flag");
                        Log.v("show", "上传返回的数据:" + jsonObject.toString());
                        if ("ok".equalsIgnoreCase(result)) {
                            // 上传成功
//                            ToastUtil.ShowShort(context, "上传成功：" + result);
                            setHandlerInfoNoDialog(UPLOAD_INFO, "提交成功");
//                            resetAll();
                        } else {
                            String message = jsonObject.getString("message");
                            if (message.contains("$")) {
                                int index = message.indexOf("$");
                                String messageSbf = message.substring(0, index);
                                // 上传失败
                                Log.d(context.getPackageName(), "上传失败：" + result
                                        + " 消息：" + messageSbf);
                                setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败:" + messageSbf);

                            } else {
                                // 上传失败
                                Log.d(context.getPackageName(), "上传失败：" + result
                                        + " 消息：" + message);
                                setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + message);

                            }

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Log.d(context.getPackageName(), "上传失败：" + e.toString());
                        setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + e.toString());

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d(context.getPackageName(), "上传失败：" + e.toString());
                    setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + e.toString());

                }

            }

            @Override
            public void onException(Exception e) {
                // 上传失败
                Log.d(context.getPackageName(), "上传失败：" + e.toString());
                setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + e.toString());

            }
        }, httpPost);
    }

    private void setHandlerInfoNoDialog(int sginType, Object content) {
        Message msg = new Message();
        msg.what = sginType;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_INFO:
                    String info = (String) msg.obj;
                    if (StringUtil.isStrTrue(info)) {
                        ToastUtil.ShowLong(context, info);
                        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                            waitDialogRectangle.dismiss();
                        }
                    }
                    if (info.equalsIgnoreCase("提交成功")) {
                        resetAll();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void resetAll() {
        leave_time_begin.setText("");
        leave_time_end.setText("");
        leave_type.setSelection(0);
        leave_days.setText("");
        leave_reason.setText("");
        c = Calendar.getInstance();
        users.clear();
        types.clear();
        typevalues.clear();
        initData();
        initView();

    }
    /**
     * 提交数据
     *
     * @param datas
     */
    private void getBusinessList(String datas, final String dealtype) {
        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
            waitDialogRectangle.dismiss();
        }
        waitDialogRectangle = new WaitDialogRectangle(context);
        waitDialogRectangle.setCanceledOnTouchOutside(false);
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在提交");
//        OkGo.post("http://172.16.12.243:8085/hjmerp/servlet/DataUpdateServlet")
        OkGo.post(EapApplication.URL_SERVER_HOST_HTTP + "/servlet/DataUpdateServlet")
                .isMultipart(true)
                .params("datas", datas)
                .execute(new BFlagCallBack<businessFlag>() {
                    @Override
                    public void onSuccess(businessFlag businessFlag, Call call, Response response) {
                        String content = businessFlag.getMessage();
                        Constant.billsNo = businessFlag.getNo();
                        if (businessFlag.getFlag().equals("Y")) {
                            if (dealtype.equalsIgnoreCase("send")) {
                                ToastUtil.ShowShort(getContext(), "上传成功");
//                                resetAll();
                                setResult(22);
                                finish();
                            }

                        } else {
                            ToastUtil.ShowShort(getContext(), "上传失败");
                        }
                        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                            waitDialogRectangle.dismiss();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (e instanceof OkGoException) {
                            ToastUtil.ShowShort(getContext(), "网络错误");

                        } else {
                            ToastUtil.ShowShort(getContext(), e.getMessage());

                        }
                        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                            waitDialogRectangle.dismiss();
                        }
                    }
                });
    }


}

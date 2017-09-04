package com.hjnerp.business.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjnerp.business.BusinessJsonCallBack.BFlagCallBack;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Ej1345;
import com.hjnerp.model.PerformanceDatas;
import com.hjnerp.model.businessFlag;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class OverBusiness extends ActivitySupport implements View.OnClickListener {

    private EditText over_name;
    private EditText over_part;
    private EditText over_time_begin;
    private EditText over_days;
    private EditText over_reason;
    private Button submit_over;
    private Calendar c = Calendar.getInstance();
    private String name_user;
    private String id_user;
    private String name_dept;
    private String id_com;
    private String id_dept;
    private List<Ctlm1345> users;
    private String id_clerk;
    private String id_linem;
    private EditText var_rejust_name_over;
    private LinearLayout rejust_list_over;
    private PerformanceDatas pds;
    private PerformanceDatas.MainBean mainBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_business);
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
    }

    private void initView() {
        getSupportActionBar().show();
        getSupportActionBar().setTitle("加班申请单");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        over_name = (EditText) findViewById(R.id.over_name);
        over_part = (EditText) findViewById(R.id.over_part);
        over_time_begin = (EditText) findViewById(R.id.over_time_begin);
        over_days = (EditText) findViewById(R.id.over_days);
        over_reason = (EditText) findViewById(R.id.over_reason);
        submit_over = (Button) findViewById(R.id.submit_over);
        over_time_begin.setOnClickListener(this);
        submit_over.setOnClickListener(this);
        if (users.size() == 0) {
            ToastUtil.ShowShort(this, "请先下载基础数据");
            finish();
            return;
        }
        over_name.setText(name_user.trim());
        over_part.setText(name_dept.trim());
        var_rejust_name_over = (EditText) findViewById(R.id.var_rejust_name_over);
        rejust_list_over = (LinearLayout) findViewById(R.id.rejust_list_over);
        if (!Constant.JUDGE_TYPE) {
            rejust_list_over.setVisibility(View.VISIBLE);
            setRejustContext();
        }
    }

    private void setRejustContext() {
        pds = Constant.performanceDatas;
        mainBean = pds.getMain();
        var_rejust_name_over.setText(mainBean.getVar_rejust());
        over_time_begin.setText(mainBean.getDate_time());
        over_days.setText(mainBean.getDec_ot());
        over_reason.setText(mainBean.getRemark());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_over:
                submit();
                break;
            case R.id.over_time_begin:
                showtime(over_time_begin);
                break;
        }
    }

    private void showtime(final EditText editText) {
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
        String name = over_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "员工姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String part = over_part.getText().toString().trim();
        if (TextUtils.isEmpty(part)) {
            Toast.makeText(this, "部门不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String begin = over_time_begin.getText().toString().trim();
        if (TextUtils.isEmpty(begin)) {
            Toast.makeText(this, "请选择开始加班的日期", Toast.LENGTH_SHORT).show();
            return;
        }

        String days = over_days.getText().toString().trim();
        if (TextUtils.isEmpty(days)) {
            Toast.makeText(this, "请输入加班天数", Toast.LENGTH_SHORT).show();
            return;
        }

        String reason = over_reason.getText().toString().trim();
        if (TextUtils.isEmpty(reason)) {
            Toast.makeText(this, "请填写加班原因及内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something

        StringBuffer stringBuffer = new StringBuffer();
        if (!Constant.JUDGE_TYPE) {
            stringBuffer.append("{\"tableid\":\"dgtdot\",\"opr\":\"SS\",\"no\":\"" + mainBean.getDgtdot_no() + "\",\"userid\":\"" + id_user + "\",\"comid\":\"" + id_com + "\",");
            stringBuffer.append("\"menuid\":\"002090\",\"dealtype\":\"save\",\"data\":[");
            stringBuffer.append("{\"table\": \"dgtdot_03\",\"oprdetail\":\"U\",\"where\":\" \",\"data\":[");
            stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_user + "\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + a + "\",\"datatype\":\"datetime\"}, ");
            stringBuffer.append("{\"column\":\"flag_psts\",\"value\":\"\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_table\",\"value\":\"\",\"datatype\":\"varchar\"},");

        } else {
            stringBuffer.append("[{key:{\"tableid\":\"dgtdot\",\"opr\":\"SS\",\"no\":\"\",\"userid\":\"" + id_user + "\",\"comid\":\"" + id_com + "\",\"data\":[");
            stringBuffer.append("{\"table\": \"dgtdot_03\",\"where\":\" \",\"data\":[");
        }
        stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"L\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"FBgtd\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"line_no\",\"value\":\"1\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + id_dept + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + id_clerk + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"dec_ot\",\"value\":\"" + days + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"date_time\",\"value\":\"" + begin + "\",\"datatype\":\"datetime\"},");
        stringBuffer.append("{\"column\":\"id_linem\",\"value\":\"" + id_linem + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"var_title\",\"value\":\"" + name_user + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"remark\",\"value\":\"" + reason + "\",\"datatype\":\"varchar\"}]}]}");
        if (!Constant.JUDGE_TYPE) {
            String str = stringBuffer.toString();
            Log.d("str1", str);
            getBusinessList(str, "save");
            String save = "\"menuid\":\"002090\",\"dealtype\":\"save\",\"data\":[";
            String send = "\"menuid\":\"002090\",\"dealtype\":\"send\",\"data\":[";
            String finalstring = str.replace(save, send);
            Log.d("str2", finalstring);
            getBusinessList(finalstring, "send");
        } else {
            stringBuffer.append(",value:\"\"}]");
            String str = stringBuffer.toString();
            try {
                HjUpload hjUpload = new HjUpload(this);
                JSONArray jsonArray = new JSONArray(str);
                hjUpload.onhjupload(jsonArray);
                resetAll();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void resetAll() {
        over_time_begin.setText("");
        over_days.setText("");
        over_reason.setText("");
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

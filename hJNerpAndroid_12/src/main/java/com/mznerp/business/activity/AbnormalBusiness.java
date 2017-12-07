package com.mznerp.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mznerp.business.BusinessJsonCallBack.BFlagCallBack;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.AbnormalDetailModel;
import com.mznerp.model.Ctlm1345;
import com.mznerp.model.Ej1345;
import com.mznerp.model.PerformanceBean;
import com.mznerp.model.PerformanceDatas;
import com.mznerp.model.businessFlag;
import com.mznerp.util.ToastUtil;
import com.mznerp.widget.WaitDialogRectangle;
import com.mznerp.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class AbnormalBusiness extends ActivitySupport implements View.OnClickListener {

    private EditText abnormal_name;
    private EditText abnormal_part;
    private LinearLayout add_layout_abnormal;
    private TextView add_abnormal;
    private Button submit_abnormal;
    private String name_user;
    private String id_user;
    private String name_dept;
    private String id_com;
    private String id_dept;
    private List<Ctlm1345> users;
    private String id_clerk;
    private String id_linem;
    private List<AbnormalDetailModel> data;
    private List<View> listaddview;
    private EditText var_rejust_name_abnormal;
    private LinearLayout rejust_list_abnormal;
    private PerformanceDatas pds;
    private PerformanceDatas.MainBean mainBean;
    private List<PerformanceBean> details;
    private int countDetail = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abnormal_business);
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
        data = new ArrayList<>();
        data = getIntent().getParcelableArrayListExtra("data");
        listaddview = new ArrayList<>();
    }

    private void initView() {
        getSupportActionBar().show();
        getSupportActionBar().setTitle("考勤异常");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        abnormal_name = (EditText) findViewById(R.id.abnormal_name);
        abnormal_part = (EditText) findViewById(R.id.abnormal_part);
        add_layout_abnormal = (LinearLayout) findViewById(R.id.add_layout_abnormal);
        add_abnormal = (TextView) findViewById(R.id.add_abnormal);
        submit_abnormal = (Button) findViewById(R.id.submit_abnormal);
        submit_abnormal.setOnClickListener(this);
        add_abnormal.setOnClickListener(this);
        if (users.size() == 0) {
            ToastUtil.ShowShort(this, "请先下载基础数据");
            finish();
            return;
        }
        abnormal_name.setText(name_user.trim());
        abnormal_part.setText(name_dept.trim());
        if (data != null && data.size() > 0) {
            add_abnormal.setText("重新选择");
            for (int i = 0; i < data.size(); i++) {
                addlayout(i);
            }

        }
        var_rejust_name_abnormal = (EditText) findViewById(R.id.var_rejust_name_abnormal);
        rejust_list_abnormal = (LinearLayout) findViewById(R.id.rejust_list_abnormal);
        if (!Constant.JUDGE_TYPE) {
            rejust_list_abnormal.setVisibility(View.VISIBLE);
            setRejustContext();
        }
    }

    private void setRejustContext() {
        pds = Constant.performanceDatas;
        mainBean = pds.getMain();
        details = pds.getDetails();
        var_rejust_name_abnormal.setText(mainBean.getVar_rejust());
        add_abnormal.setText("重新选择");
        countDetail = details.size();
        if (data == null || data.size() == 0) {
            for (int i = 0; i < details.size(); i++) {
                addlayoutforrejust(i);
            }
        }

    }

    private void addlayout(int i) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.abnormal_detail, add_layout_abnormal, false);
        add_layout_abnormal.addView(view);
        listaddview.add(view);
        EditText abnormal_date = (EditText) view.findViewById(R.id.abnormal_date);
        EditText abnormal_type = (EditText) view.findViewById(R.id.abnormal_type);
        EditText abnormal_time = (EditText) view.findViewById(R.id.abnormal_time);
        abnormal_date.setText(data.get(i).getVar_date());
        abnormal_type.setText(data.get(i).getVar_on());
        abnormal_time.setText(data.get(i).getVar_off());
    }

    private void addlayoutforrejust(int i) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.abnormal_detail, add_layout_abnormal, false);
        add_layout_abnormal.addView(view);
        listaddview.add(view);
        EditText abnormal_date = (EditText) view.findViewById(R.id.abnormal_date);
        EditText abnormal_type = (EditText) view.findViewById(R.id.abnormal_type);
        EditText abnormal_time = (EditText) view.findViewById(R.id.abnormal_time);
        EditText abnormal_remark = (EditText) view.findViewById(R.id.abnormal_remark);

        abnormal_date.setText(details.get(i).getVar_date());
        abnormal_type.setText(details.get(i).getVar_on());
        abnormal_time.setText(details.get(i).getVar_off());
        abnormal_remark.setText(details.get(i).getVar_remark());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_abnormal:
                submit();
                break;
            case R.id.add_abnormal:
                gotoDetail();
                break;
        }
    }

    private void gotoDetail() {
        Intent intent = new Intent(this, AbnormalDetail.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void submit() {
        // validate
        String name = abnormal_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "名字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String part = abnormal_part.getText().toString().trim();
        if (TextUtils.isEmpty(part)) {
            Toast.makeText(this, "部门不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listaddview.size() == 0) {
            Toast.makeText(this, "请添加明细", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO
        for (int i = 0; i < listaddview.size(); i++) {
            View view3 = listaddview.get(i);
            EditText abnormal_remark = (EditText) view3.findViewById(R.id.abnormal_remark);
            String remark = abnormal_remark.getText().toString().trim();
            if (TextUtils.isEmpty(remark)) {
                Toast.makeText(this, "异常说明不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a = sdftime.format(dt);
        String date_today = sdf.format(dt);
        HjUpload hjUpload = new HjUpload(this);
        StringBuffer stringBuffer = new StringBuffer();

        if (!Constant.JUDGE_TYPE) {
            stringBuffer.append("{\"tableid\":\"dgtdabn\",\"opr\":\"SS\",\"no\":\"" + mainBean.getDgtdabn_no() + "\",\"userid\":\"" + id_user + "\",\"comid\":\"" + id_com + "\",");
            stringBuffer.append("\"menuid\":\"002095\",\"dealtype\":\"save\",\"data\":[");
        } else {
            stringBuffer.append("[{key:{\"tableid\":\"dgtdabn\",\"opr\":\"SS\",\"no\":\"\",\"userid\":\"" + id_user + "\",\"comid\":\"" + id_com + "\",\"data\":[");
        }
        int gap = 0;
        int cycleTime = 0;
        if (Constant.JUDGE_TYPE) {
            gap = 0;
            cycleTime = listaddview.size();

        } else {
            gap = listaddview.size() - countDetail;
            if (gap >= 0) {
                cycleTime = listaddview.size();
            } else {
                cycleTime = countDetail;
            }
        }
        for (int i = 0; i < cycleTime; i++) {
            if (!Constant.JUDGE_TYPE) {
                if (i >= countDetail) {
                    stringBuffer.append("{\"table\": \"dgtdabn_03\",\"oprdetail\":\"N\",\"where\":\" \",\"data\":[");
                } else if (i >= countDetail + gap) {
                    stringBuffer.append("{\"table\": \"dgtdabn_03\",\"oprdetail\":\"D\",\"where\":\" \",\"data\":[");
                } else {
                    stringBuffer.append("{\"table\": \"dgtdabn_03\",\"oprdetail\":\"U\",\"where\":\" \",\"data\":[");
                }
            } else {
                stringBuffer.append("{\"table\": \"dgtdabn_03\",\"where\":\" \",\"data\":[");
            }
            stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"L\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"FBgtd\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + id_dept + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"line_no\",\"value\":\"" + (i + 1) + "\",\"datatype\":\"int\"}, ");
            if (!Constant.JUDGE_TYPE) {
                stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_user + "\",\"datatype\":\"varchar\"},");
                stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + a + "\",\"datatype\":\"datetime\"}, ");
                stringBuffer.append("{\"column\":\"flag_psts\",\"value\":\"\",\"datatype\":\"varchar\"},");
                stringBuffer.append("{\"column\":\"id_table\",\"value\":\"\",\"datatype\":\"varchar\"},");

            }
            String date = "";
            String type = "";
            String time = "";
            String remark = "";
            if (i < listaddview.size()) {
                View view3 = listaddview.get(i);
                EditText abnormal_date = (EditText) view3.findViewById(R.id.abnormal_date);
                EditText abnormal_type = (EditText) view3.findViewById(R.id.abnormal_type);
                EditText abnormal_time = (EditText) view3.findViewById(R.id.abnormal_time);
                EditText abnormal_remark = (EditText) view3.findViewById(R.id.abnormal_remark);
                date = abnormal_date.getText().toString().trim();
                type = abnormal_type.getText().toString().trim();
                time = abnormal_time.getText().toString().trim();
                remark = abnormal_remark.getText().toString().trim();
            }
            stringBuffer.append("{\"column\":\"var_date\",\"value\":\"" + date + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_off\",\"value\":\"" + time + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_on\",\"value\":\"" + type + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + remark + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + id_clerk + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"date_today\",\"value\":\"" + date_today + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_linem\",\"value\":\"" + id_linem + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_title\",\"value\":\"" + name + "\",\"datatype\":\"varchar\"}]}");
            if (i != cycleTime - 1) {
                stringBuffer.append(",");
            }
        }
        if (!Constant.JUDGE_TYPE) {
            stringBuffer.append("]}");
            String str = stringBuffer.toString();
            Log.d("str1", str);
            getBusinessList(str, "save");
            String save = "\"menuid\":\"002095\",\"dealtype\":\"save\",\"data\":[";
            String send = "\"menuid\":\"002095\",\"dealtype\":\"send\",\"data\":[";
            String finalstring = str.replace(save, send);
            Log.d("str2", finalstring);
            getBusinessList(finalstring, "send");
        } else {
            stringBuffer.append("]},value:\"\"}]");
            String str = stringBuffer.toString();
            try {
                JSONArray jsonArray = new JSONArray(str);
                hjUpload.onhjupload(jsonArray);
                resetAll();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void resetAll() {
        data.clear();
        add_layout_abnormal.removeAllViews();
        listaddview.clear();
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
                                TravelActivityNew.travelActivityNew.refresh();
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

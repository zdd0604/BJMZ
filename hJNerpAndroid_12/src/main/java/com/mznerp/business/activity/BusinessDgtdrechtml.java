package com.mznerp.business.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mznerp.business.BusinessAdapter.popupAdapter;
import com.mznerp.business.BusinessJsonCallBack.BFlagCallBack;
import com.mznerp.business.BusinessQueryDao.BusinessQueryDao;
import com.mznerp.business.BusinessStringBuffer.BusinessEJBuffer;
import com.mznerp.business.businessutils.BusinessTimeUtils;
import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.model.BusinessOneLine;
import com.mznerp.model.EjWType1345;
import com.mznerp.model.EjWadd1345;
import com.mznerp.model.businessFlag;
import com.mznerp.util.Log;
import com.mznerp.util.StringUtil;
import com.mznerp.widget.ClearEditText;
import com.mznerp.R;
import com.lzy.okgo.OkGo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static com.mznerp.common.Constant.buss_key;
import static com.mznerp.common.Constant.buss_value;
import static com.mznerp.common.Constant.datas;
import static com.mznerp.common.Constant.dsaordbaseJsons_new;

/**
 * 工作日志
 */
public class BusinessDgtdrechtml extends ActionBarWidgetActivity implements View.OnClickListener {
    //界面ID
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.log_date_task)
    TextView log_date_task;
    @BindView(R.id.log_id_wproj)
    EditText log_id_wproj;
    @BindView(R.id.log_var_tel)
    EditText log_var_tel;
    @BindView(R.id.log_dec_wtime)
    ClearEditText log_dec_wtime;
    @BindView(R.id.log_var_wtitle)
    ClearEditText log_var_wtitle;
    @BindView(R.id.log_id_corr)
    ClearEditText log_id_corr;
    @BindView(R.id.log_var_remark)
    ClearEditText log_var_remark;
    @BindView(R.id.log_flag_wadd)
    TextView log_flag_wadd;
    @BindView(R.id.log_id_wtype)
    TextView log_id_wtype;
    @BindView(R.id.dgtdrechtml_submit)
    Button dgtdrechtml_submit;
    @BindView(R.id.pro_text)
    TextView pro_text;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.log_id_cantact)
    TextView log_id_cantact;
    @BindView(R.id.log_id_duty)
    TextView log_id_duty;
    private List<EjWadd1345> ejWaddList;
    private List<EjWType1345> ejtpe1345List;

    private String Log_id_wproj;
    private String date_task;
    private String flag_wadd;
    private String id_wtype;
    private String dec_wtime;
    private String var_wtitle;
    private String var_remark;
    private PopupWindow mPopupWindow;
    private List<String> idSiteList = new ArrayList<>();
    private List<String> nameSiteList = new ArrayList<>();
    private List<String> idWTypeList = new ArrayList<>();
    private List<String> nameWTypeList = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.HANDLERTYPE_0:
                    log_id_wproj.setText(dsaordbaseJsons_new.get(0).getName_corr());
                    log_id_corr.setText(dsaordbaseJsons_new.get(0).getVar_conplace());
                    log_id_cantact.setText(dsaordbaseJsons_new.get(0).getVar_contact());
                    log_id_duty.setText(dsaordbaseJsons_new.get(0).getVar_contactduty());
                    log_var_tel.setText(dsaordbaseJsons_new.get(0).getVar_tel());
                    break;
                case Constant.HANDLERTYPE_1:
                    id_wtype = buss_key;
                    log_id_wtype.setText(buss_value);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dgtdrechtml);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        actionCenterTv.setText(getString(R.string.work_Tile_Activity));
        actionRightTv.setText(getString(R.string.action_right_content_commit));
        actionRightTv.setOnClickListener(this);
        actionLeftTv.setOnClickListener(this);
        log_date_task.setOnClickListener(this);
        log_id_wproj.setOnClickListener(this);
        dgtdrechtml_submit.setOnClickListener(this);
        log_flag_wadd.setOnClickListener(this);
        log_id_wtype.setOnClickListener(this);
        log_date_task.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date()));//默认显示当前时间
        //方便后期判断进行版本设计
//        if (QiXinBaseDao.queryCurrentUserInfo().companyID.equalsIgnoreCase("CM1101-0001")){
//            pro_text.setText("客户名称");
//            textView7.setText("地    址");
//        }
        querySite();
        queryWType();
    }

    /**
     * 查询公司地点
     */
    private void querySite() {
        ejWaddList = BusinessQueryDao.getMyWadd(mContext);
        for (int i = 0; i < ejWaddList.size(); i++) {
            nameSiteList.add(ejWaddList.get(i).getName_wadd());
            idSiteList.add(ejWaddList.get(i).getFlag_wadd());
        }
    }

    /**
     * 查询工作类型
     */
    private void queryWType() {
        ejtpe1345List = BusinessQueryDao.getMyWType(mContext);
        for (int i = 0; i < ejtpe1345List.size(); i++) {
            idWTypeList.add(ejtpe1345List.get(i).getId_wtype());
            nameWTypeList.add(ejtpe1345List.get(i).getName_projcate());
            Log.d(ejtpe1345List.get(i).getName_projcate() + "," + ejtpe1345List.get(i).getId_wtype());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_date_task:
                showCalendar(log_date_task);
                break;
            case R.id.log_id_wproj:
                queryWorkType();
                break;
//            case R.id.dgtdrechtml_submit:
//                submitData();
//                break;
            case R.id.log_flag_wadd:
                showPopupWindow(log_flag_wadd, nameSiteList, "site");//以前的POPUPWINDOW形式
                break;
            case R.id.log_id_wtype:
                datas = new ArrayList<>();
                for (int i = 0; i < idWTypeList.size(); i++) {
                    BusinessOneLine businessOneLine = new BusinessOneLine();
                    businessOneLine.setKey(idWTypeList.get(i));
                    businessOneLine.setValue(nameWTypeList.get(i));
                    datas.add(businessOneLine);
                }
                Intent intent = new Intent(getContext(), BusinessSearchOneLine.class);
                startActivityForResult(intent, 33);
//                showPopupWindow(log_id_wtype, nameWTypeList, "wtype");//以前的POPUPWINDOW形式
                break;
            case R.id.action_left_tv:
                finish();
                break;
            case R.id.action_right_tv:
                submitData();
                break;
        }
    }

    private void submitData() {
        waitDialog.show();
        Log_id_wproj = log_id_wproj.getText().toString().trim();
        date_task = log_date_task.getText().toString().trim();
        dec_wtime = log_dec_wtime.getText().toString().trim();
        var_wtitle = log_var_wtitle.getText().toString().trim();
        var_remark = log_var_remark.getText().toString().trim();

        if (!StringUtil.isStrTrue(date_task)) {
            showFailToast("任务时间不能为空");
            waitDialog.dismiss();
            return;
        }
//        if (!StringUtil.isStrTrue(flag_wadd)) {
//            showFailToast("工作地点不能为空");
//            waitDialog.dismiss();
//            return;
//        }
        if (!StringUtil.isStrTrue(id_wtype)) {
            showFailToast("工作类型不能为空");
            waitDialog.dismiss();
            return;
        }
        if (!StringUtil.isStrTrue(Log_id_wproj)) {
            showFailToast("工作项目不能为空");
            waitDialog.dismiss();
            return;
        }
        //美正去掉工时
//        if (!StringUtil.isStrTrue(dec_wtime)) {
//            showFailToast("工时不能为空");
//            waitDialog.dismiss();
//            return;
//        }
        if (!StringUtil.isStrTrue(var_wtitle)) {
            showFailToast("主题不能为空");
            waitDialog.dismiss();
            return;
        }
//美正不需要新客户
//        if (id_wtype.equalsIgnoreCase("030")) {
//            Constant.id_wproj = "0";
//            Constant.item_peoject = log_id_wproj.getText().toString().trim();
//            Constant.id_corr = log_id_corr.getText().toString().trim();
//        }
        if (TextUtils.isEmpty(dec_wtime)) {
            dec_wtime = "8";
        }
        if (TextUtils.isEmpty(flag_wadd)) {
            flag_wadd = "A";
        }
        String data = BusinessEJBuffer.getDgtdrecBuffer(
                Constant.ID_MENU,
                Constant.ej1345.getId_user(),
                Constant.MYUSERINFO.userID,
                Constant.MYUSERINFO.companyID,
                date_task,
                Constant.MYUSERINFO.departmentID,
                flag_wadd,
                id_wtype,
                Constant.id_wproj,
                dec_wtime,
                var_wtitle,
                var_remark,
                Constant.item_peoject,
                BusinessTimeUtils.getCurrentTime(Constant.NOWTIME),
                Integer.valueOf(BusinessTimeUtils.getCurrentTime(Constant.kpiperiod_MM)),
                Integer.valueOf(BusinessTimeUtils.getCurrentTime(Constant.Int_year_YYYY)),
                Constant.id_corr,
                Constant.var_tel,
                log_id_cantact.getText().toString().trim(),
                log_id_duty.getText().toString().trim());
        getBusinessList(data);
    }

    /**
     * 提交数据
     *
     * @param datas
     */
    private void getBusinessList(String datas) {
        LogShow("工作日志："+datas);
        OkGo.post(EapApplication.URL_SERVER_HOST_HTTP + "/servlet/DataUpdateServlet")
                .isMultipart(true)
                .params("datas", datas)
                .cacheKey(Constant.ID_MENU)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .execute(new BFlagCallBack<businessFlag>() {
                    @Override
                    public void onSuccess(businessFlag businessFlag, Call call, Response response) {
                        String content = businessFlag.getMessage();
                        showFailToast("提交成功！");
                        removeData();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showFailToast("提交失败！");
                        removeData();
                    }
                });
    }

    private void queryWorkType() {
        if (StringUtil.isStrTrue(id_wtype)) {
            Constant.id_wtype = id_wtype;
            if (!id_wtype.equalsIgnoreCase("030")) {
                Constant.project_type = 5;
                intentActivity(BusinessSearch.class);
            }

        } else {
            showFailToast("请先选择工作类型");
//            removeData();
        }
    }

    public void intentActivity(Class c) {
        Intent intent = new Intent(this, c);
        Constant.travel = true;
        startActivityForResult(intent, 11);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // //界面返回值
        /**
         * @author haijian
         * 收到返回的值判断是否成功，如果同意就将数据移除刷新列表
         */
        if (requestCode == 11 && resultCode == 22) {
            handler.sendEmptyMessage(Constant.HANDLERTYPE_0);
        } else if (requestCode == 33 && resultCode == 22) {
            handler.sendEmptyMessage(Constant.HANDLERTYPE_1);
        }
    }

    private void showPopupWindow(View anchorView, List<String> content, String btnType) {
        View contentView = getPopupWindowContentView(content, btnType);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int xOffset = anchorView.getWidth() / 2 - contentView.getMeasuredWidth() / 2;
//        mPopupWindow.showAsDropDown(anchorView, xOffset, 0);    //
        // 设置好参数之后再show
        mPopupWindow.showAsDropDown(anchorView);  // 默认在mButton2的左下角显示
    }

    private View getPopupWindowContentView(final List<String> content, final String btnType) {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        final View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        ListView dgtdrech_list = (ListView) contentView.findViewById(R.id.dgtdrech_list);
        popupAdapter popupAdapter = new popupAdapter(BusinessDgtdrechtml.this, content);

        dgtdrech_list.setAdapter(popupAdapter);
        dgtdrech_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (btnType.equals("site")) {
                    log_flag_wadd.setText(content.get(position));
                    flag_wadd = idSiteList.get(position);
                } else {
                    id_wtype = idWTypeList.get(position);
                    log_id_wtype.setText(content.get(position));
                    if (id_wtype.equalsIgnoreCase("030")) {
                        log_id_wproj.setFocusable(true);
                        log_id_wproj.setFocusableInTouchMode(true);
                        log_id_wproj.setHint("请输入新客户");
                        log_id_wproj.setText("");
                        log_id_corr.setFocusable(true);
                        log_id_corr.setFocusableInTouchMode(true);
                        log_id_corr.setText("");
                    } else {
                        log_id_wproj.setFocusable(false);
                        log_id_wproj.setFocusableInTouchMode(false);
                        log_id_wproj.setHint("点击选择客户");
                        log_id_wproj.setText("");
                        log_id_corr.setFocusable(false);
                        log_id_corr.setFocusableInTouchMode(false);
                        log_id_corr.setText("");
                    }
                    Constant.id_wproj = "";
                    Constant.item_peoject = "";
                    Constant.id_corr = "";
                }
                if (mPopupWindow != null)
                    mPopupWindow.dismiss();
            }
        });
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Constant.project_type = 0;
        Constant.travel = false;
        dsaordbaseJsons_new = new ArrayList<>();
        Constant.id_wproj = "";
        Constant.item_peoject = "";
        Constant.id_corr = "";
        buss_key = "";
        buss_value = "";
    }

    private void removeData() {
        waitDialog.dismiss();
        log_id_wtype.setText("");
        log_id_wproj.setText("");
        log_dec_wtime.setText("");
        log_var_wtitle.setText("");
        log_id_corr.setText("");
        log_id_cantact.setText("");
        log_id_duty.setText("");
        log_var_tel.setText("");
        log_var_remark.setText("");
    }
}

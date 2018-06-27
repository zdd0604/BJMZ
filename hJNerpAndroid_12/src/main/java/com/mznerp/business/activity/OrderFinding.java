package com.mznerp.business.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.common.Constant;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.BusinessOneLine;
import com.mznerp.model.Ctlm1345;
import com.mznerp.model.DsaordQueryJson;
import com.mznerp.model.DsaordQueryJsonAccoutage;
import com.mznerp.model.DsaordQueryJsonAccoutage2;
import com.mznerp.model.DsaordQueryJsonD;
import com.mznerp.model.Dsaordtype;
import com.mznerp.model.ProductDetail;
import com.mznerp.net.HttpClientBuilder;
import com.mznerp.net.HttpClientManager;
import com.mznerp.util.Log;
import com.mznerp.util.StringUtil;
import com.mznerp.util.myscom.StringUtils;
import com.mznerp.R;
import com.mznerp.widget.LockTableView2;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.jiebao.utils.Tools.getToday;
import static com.mznerp.common.Constant.buss_key;
import static com.mznerp.common.Constant.buss_value;
import static com.mznerp.common.Constant.datas;
import static com.mznerp.common.Constant.dsaordbaseJsons_new;
import static com.mznerp.common.Constant.user_myid;

/**
 * 单据查询
 */
public class OrderFinding extends ActionBarWidgetActivity implements View.OnClickListener,
        ActionBarWidgetActivity.NsyncDataConnector {
    private List<Ctlm1345> myauthuser = new ArrayList<>();
    private List<String> users_id = new ArrayList<String>();
    private List<String> users_name = new ArrayList<String>();
    private List<DsaordQueryJson> dsaordqueryJsons = new ArrayList<DsaordQueryJson>();
    private List<DsaordQueryJsonAccoutage> dsaordbaseJsonsAccount = new ArrayList<DsaordQueryJsonAccoutage>();
    private List<DsaordQueryJsonAccoutage2> dsaordbaseJsonsAccount2 = new ArrayList<DsaordQueryJsonAccoutage2>();
    private List<DsaordQueryJsonD> dsaordbaseJsonsD = new ArrayList<DsaordQueryJsonD>();
    private ArrayAdapter<String> adapter;
    public static final String JSON_VALUE = "values";
    public static final Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    private ArrayList<ArrayList<String>> mTableDatas;

    private String tab_name;
    private Calendar c = Calendar.getInstance();
    private String pattern = "MM-dd-yyyy";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
    private double allPrice = 0.0;//总价
    private double firstPrice = 0.0;//总价
    private double secondPrice = 0.0;//总价
    private LockTableView2 mLockTableView;
    private EditText all_goneprice_textview;
    private LinearLayout all_goneprice_layout;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;

    @BindView(R.id.object_name)
    TextView object_name;
    @BindView(R.id.object_person)
    TextView object_person;
    @BindView(R.id.linear_list)
    LinearLayout linear_list;
    @BindView(R.id.table_begin_time)
    TextView table_begin_time;
    @BindView(R.id.table_date_from)
    LinearLayout table_date_from;
    @BindView(R.id.table_end_time)
    TextView table_end_time;
    @BindView(R.id.table_date_to)
    LinearLayout table_date_to;
    @BindView(R.id.all_price_textview)
    TextView all_price_textview;
    @BindView(R.id.all_price_layout)
    LinearLayout all_price_layout;
    private TextView first_price_textview;
    private LinearLayout first_price_layout;
    private TextView second_price_textview;
    private LinearLayout second_price_layout;
    private boolean changeRow = false;//是否有需要改变列为左上对齐的，默认为没有，工作日志内容查询专用
    private int changeRowNo;//需要改变对齐方式的列，仅限一列，如果是两列需要重新修改源码,定义为列数减2，如内容为第九列，该值为7

    //异步任务
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getTab();
                    break;
                case 1:
                    setTxt();
                    break;
                case 5:
                    object_person.setText(buss_value);
                    break;
                case 2:
                    waitDialog.dismiss();
                    showFailToast("结果为空!");
                    linear_list.removeAllViews();
                    all_price_textview.setText("0.00");
                    break;
                case 3:
                    //设置表格数据
                    SetTableTask setTableTask = new SetTableTask();
                    setTableTask.execute((Void) null);

                    break;
                case 4:
                    dsaordqueryJsons.clear();
                    dsaordbaseJsonsAccount.clear();
                    dsaordbaseJsonsAccount2.clear();
                    dsaordbaseJsonsD.clear();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finding);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    //加载页面
    private void initView() {
        actionRightTv.setText(getString(R.string.orderFinding_Title_RightTvSearch));
        actionRightTv1.setVisibility(View.VISIBLE);
        actionRightTv1.setText(getString(R.string.orderFinding_Title_RightTvReset));
        table_begin_time.setOnClickListener(this);
        table_end_time.setText(getToday());
        table_end_time.setOnClickListener(this);
        object_name.setOnClickListener(this);
        actionRightTv.setOnClickListener(this);
        actionRightTv1.setOnClickListener(this);
        actionLeftTv.setOnClickListener(this);
        table_begin_time.setOnClickListener(this);
        ActionBarWidgetActivity.setNsyncDataConnector(this);
        table_date_from = (LinearLayout) findViewById(R.id.table_date_from);
        table_end_time = (TextView) findViewById(R.id.table_end_time);
        table_end_time.setText(getToday());
        table_end_time.setOnClickListener(this);
        table_date_to = (LinearLayout) findViewById(R.id.table_date_to);
        all_price_textview = (TextView) findViewById(R.id.all_price_textview);
        all_price_layout = (LinearLayout) findViewById(R.id.all_price_layout);
        all_goneprice_textview = (EditText) findViewById(R.id.all_goneprice_textview);
        all_goneprice_layout = (LinearLayout) findViewById(R.id.all_goneprice_layout);
        //此段代码为开始时间提前一个月
        try {
            Calendar cal = Calendar.getInstance();
            Date date = format.parse(getToday());
            cal.setTime(date);
            cal.add(Calendar.DATE, -30);//为了和ios一致，改为减30天
//            cal.add(Calendar.MONTH, -1);
            Date fDate = cal.getTime();
            table_begin_time.setText(format.format(fDate));
        } catch (ParseException e) {
            e.printStackTrace();
            table_begin_time.setText(getToday());
        }
        first_price_textview = (TextView) findViewById(R.id.first_price_textview);
        first_price_layout = (LinearLayout) findViewById(R.id.first_price_layout);
        second_price_textview = (TextView) findViewById(R.id.second_price_textview);
        second_price_layout = (LinearLayout) findViewById(R.id.second_price_layout);
    }


    //加载数据，必要的页面数据
    private void initData() {
        //不同表格的不同数据
        switch (Constant.tab_type) {
            case 0:
                changeRow = false;
                actionCenterTv.setText("订单查询");
                tab_name = "dsaordquery";
                table_date_from.setVisibility(View.VISIBLE);
                table_date_to.setVisibility(View.VISIBLE);
                all_price_layout.setVisibility(View.VISIBLE);
                table_begin_time.setText(getToday());
                all_goneprice_layout.setVisibility(View.GONE);
                break;
            case 1:
                changeRow = false;
                actionCenterTv.setText("应收账龄");
                tab_name = "accountage";
                table_date_from.setVisibility(View.GONE);
                table_date_to.setVisibility(View.GONE);
                all_price_layout.setVisibility(View.VISIBLE);
                all_goneprice_layout.setVisibility(View.GONE);
                break;
            case 2:
                actionCenterTv.setText("日志查询");
                tab_name = "dgtdrec";
                table_date_from.setVisibility(View.VISIBLE);
                table_date_to.setVisibility(View.VISIBLE);
                all_goneprice_layout.setVisibility(View.GONE);
                changeRow = true;//设置左对齐
                changeRowNo = 9;//第九行
                break;
            case 3:
                changeRow = false;
                actionCenterTv.setText("订单明细");
                tab_name = "dsaordquery";
                table_date_from.setVisibility(View.VISIBLE);
                table_date_to.setVisibility(View.VISIBLE);
                all_price_layout.setVisibility(View.VISIBLE);
                table_begin_time.setText(getToday());
                all_goneprice_layout.setVisibility(View.GONE);
                break;
            case 4:
                changeRow = false;
                actionCenterTv.setText("客户应收");
                tab_name = "accountage2";
                table_date_from.setVisibility(View.GONE);
                table_date_to.setVisibility(View.GONE);
                all_goneprice_layout.setVisibility(View.VISIBLE);
                first_price_layout.setVisibility(View.VISIBLE);
                second_price_layout.setVisibility(View.VISIBLE);
                break;

        }
//        users_id.add("");
//        users_name.add("请选择业务员");
        myauthuser = BusinessBaseDao.getCTLM1345ByIdTable("sauser");
        if (myauthuser.size() == 0) {
//            ToastUtil.ShowShort(this, "请先下载基础数据");
//            finish();
//            return;
            users_name.add(QiXinBaseDao.queryCurrentUserInfo().username);
            users_id.add(QiXinBaseDao.queryCurrentUserInfo().userID);

        } else {
            users_name.add(QiXinBaseDao.queryCurrentUserInfo().username);
            users_id.add(QiXinBaseDao.queryCurrentUserInfo().userID);
            datas = new ArrayList<>();
            for (int i = 0; i < myauthuser.size(); i++) {
                Dsaordtype dsaordtype = mGson.fromJson(myauthuser.get(i).getVar_value(), Dsaordtype.class);
                if (!users_id.contains(dsaordtype.getId_user())) {

                    users_name.add(dsaordtype.getName_user());
                    users_id.add(dsaordtype.getId_user());

                }

            }

        }

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, users_name);
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        object_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BusinessSearchOneLine.class);
                datas = new ArrayList<BusinessOneLine>();
                for (int i = 0; i < users_id.size(); i++) {
                    BusinessOneLine businessOneLine = new BusinessOneLine();
                    businessOneLine.setKey(users_id.get(i));
                    businessOneLine.setValue(users_name.get(i));
                    datas.add(businessOneLine);
                }
                startActivityForResult(intent, 33);
            }
        });
//        object_person.setAdapter(adapter);
    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.object_name:
                Intent intent = new Intent(getApplicationContext(), ProductSearch.class);
                if (buss_key.equalsIgnoreCase("")) {
                    String a = "";
                    for (int i = 0; i < users_id.size(); i++) {
                        a = a + users_id.get(i) + ",";
                    }
                    user_myid = a;
                } else {
                    user_myid = buss_key + ",";
                }
                Constant.project_type = 2;
                Constant.travel = true;
                startActivityForResult(intent, 44);
                break;
            case R.id.action_right_tv:
                submit();
                break;
            case R.id.action_right_tv1:
                reset();
                showFailToast("查询条件已重置!");
                break;
            case R.id.table_begin_time:
                showCalendar(table_begin_time);
                break;
            case R.id.table_end_time:
                showCalendar(table_end_time);
                break;
            case R.id.action_left_tv:
                finish();
                break;

        }
    }


    //设置数据
    private void setTxt() {
        //旧版的搜索注销既可以使用
//        if (dsaordbaseJsons_new != null) {
//            object_name.setText(dsaordbaseJsons_new.get(0).getName_corr());
//        }

        if (StringUtil.isStrTrue(Constant.name_searCorr)) {
            object_name.setText(Constant.name_searCorr);
        }
    }


    //提交查询数据
    private void submit() {
        // validate
        linear_list.removeAllViews();
        //旧版的搜索注销既可以使用
//        String id_terminal;
//        if (dsaordbaseJsons_new == null || dsaordbaseJsons_new.size() == 0) {
//            id_terminal = "";
//        } else {
//            id_terminal = dsaordbaseJsons_new.get(0).getName_corr();
//        }
//        LogShow("------"+id_terminal);
        String all_goneprice = all_goneprice_textview.getText().toString().trim();
        String id_user = buss_key;
//        String id_user = users_id.get(object_person.getSelectedItemPosition());
        String time_begin = table_begin_time.getText().toString().isEmpty() ? "1800-00-00" : table_begin_time.getText().toString();
        String time_end = table_end_time.getText().toString().isEmpty() ? "2999-12-31" : table_end_time.getText().toString();

        waitDialog.show();
        try {
            LogShow("开始获取数据。。。");
            HttpClientBuilder.HttpClientParam param = HttpClientBuilder
                    .createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
            String condition = "";
            if (StringUtils.isEmpty(id_user)) {
                String cons = "1=1 and var_value like '%" + Constant.id_searCorr + "%' and (";
                for (int i = 0; i < users_id.size(); i++) {
                    if (i == users_id.size() - 1) {
                        cons = cons + "id_column='" + users_id.get(i) + "')";
                    } else {
                        cons = cons + "id_column='" + users_id.get(i) + "' or ";

                    }
                }
                if (Constant.tab_type == 0 || Constant.tab_type == 3 || Constant.tab_type == 2) {
                    cons = cons + " and name_column>='" + time_begin + "' and name_column<='" + time_end + "'";
                } else if (Constant.tab_type == 4 && (!TextUtils.isEmpty(all_goneprice))) {
                    cons = cons + " and name_column>" + all_goneprice + "";
                }
                condition = cons;
            } else {
                condition = "1=1 and id_column='" + id_user + "' and var_value like '%" + Constant.id_searCorr + "%'";
                if (Constant.tab_type == 0 || Constant.tab_type == 3 || Constant.tab_type == 2) {
                    condition = condition + " and name_column>='" + time_begin + "' and name_column<='" + time_end + "'";
                } else if (Constant.tab_type == 4 && (!TextUtils.isEmpty(all_goneprice))) {
                    condition = condition + " and name_column>" + all_goneprice + "";
                }
            }
            param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                    .addKeyValue("id_table", StringUtils.join(tab_name))
                    .addKeyValue("condition", condition);

            HttpClientManager.addTask(responseHandler, param.getHttpPost());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置表格数据
     */
    private void setTable(){

        long start = System.currentTimeMillis();

        mTableDatas = new ArrayList<>();
        ArrayList<String> mfristData = new ArrayList<>();
        //表头，不同表格不同表头
        LogShow("开始绘制表头。。。");
        switch (Constant.tab_type) {
            case 0:
                //订单查询
                mfristData.add("客户名称");
                mfristData.add("终端名称");
                mfristData.add("业务员");
                mfristData.add("订单日期");
                mfristData.add("产品编码");
                mfristData.add("产品");
                mfristData.add("规格");
                mfristData.add("型号");
                mfristData.add("性能参数");
                mfristData.add("单价");
                mfristData.add("数量");
                mfristData.add("金额");
                mfristData.add("快递单号（货）");
                mfristData.add("快递单号（票）");
                mfristData.add("回款情况");
                mfristData.add("订单号");
                mfristData.add("行号");
                break;
            case 1:
                //应收账龄
                mfristData.add("客户名称");
                mfristData.add("业务员");
                mfristData.add("区域");
                mfristData.add("省区");
                mfristData.add("01至30天");
                mfristData.add("30至60天");
                mfristData.add("60至90天");
                mfristData.add("90至180天");
                mfristData.add("180至270天");
                mfristData.add("270至365天");
                mfristData.add("365至730天");
                mfristData.add("730至1095天");
                mfristData.add("1095天以上");
                mfristData.add("总计");
                break;
            case 2:
                //日志查询
                mfristData.add("客户");
                mfristData.add("部门");
                mfristData.add("人员");
                mfristData.add("日期");
                mfristData.add("工作类型");
                mfristData.add("联系人");
                mfristData.add("电话");
                mfristData.add("地址");
                mfristData.add("职务");
                mfristData.add("主题");
                mfristData.add("内容");
                break;
            case 3:
                mfristData.add("结算客户");
                mfristData.add("终端");
                mfristData.add("业务员");
                mfristData.add("订单日期");
                mfristData.add("物料名称");
                mfristData.add("销售类型");
                mfristData.add("规格");
                mfristData.add("型号");
                mfristData.add("数量");
                mfristData.add("单价");
                mfristData.add("金额");
                break;
            case 4:
                mfristData.add("客户名称");
//                            mfristData.add("客户代码");
                mfristData.add("业务员");
                mfristData.add("180至365天");
                mfristData.add("365天以上");
                mfristData.add("总欠款额");
                break;
        }
        allPrice = 0.0;
        firstPrice = 0.0;
        secondPrice = 0.0;
        String time_begin = table_begin_time.getText().toString().isEmpty() ? "1800-00-00" : table_begin_time.getText().toString();
        String time_end = table_end_time.getText().toString().isEmpty() ? "2999-12-31" : table_end_time.getText().toString();
        DecimalFormat formatNum = new DecimalFormat(",##0.00");
        DecimalFormat formatNo = new DecimalFormat("#0.00");
        LogShow("开始排列数据。。。");
        int circleValues = 0;
        switch (Constant.tab_type) {
            case 0:
            case 3:
                Collections.sort(dsaordqueryJsons);
                circleValues = dsaordqueryJsons.size();
                break;
            case 1:
                circleValues = dsaordbaseJsonsAccount.size();
                break;
            case 2:
                Collections.sort(dsaordbaseJsonsD);
                circleValues = dsaordbaseJsonsD.size();
                break;
            case 4:
                circleValues = dsaordbaseJsonsAccount2.size();
                break;
        }

        mTableDatas.add(mfristData);
        ArrayList<String> contents = new ArrayList<String>();
        LogShow("开始绘制表格。。。");
        try {
            for (int i = 0; i < circleValues; i++) {
                ArrayList<String> mRowDatas = new ArrayList<String>();
                switch (Constant.tab_type) {
                    case 0:
                        DsaordQueryJson dsaordbaseJson = dsaordqueryJsons.get(i);
                        if (!contents.contains(dsaordbaseJson.getDsaord_no() + dsaordbaseJson.getLine_no())) {
                            contents.add(dsaordbaseJson.getDsaord_no() + dsaordbaseJson.getLine_no());

                            mRowDatas.add(dsaordbaseJson.getName_corr());
                            mRowDatas.add(dsaordbaseJson.getName_terminal());
                            mRowDatas.add(dsaordbaseJson.getName_seller());
                            mRowDatas.add(dsaordbaseJson.getDate_opr());
                            mRowDatas.add(dsaordbaseJson.getId_item());
                            mRowDatas.add(dsaordbaseJson.getName_item());
                            mRowDatas.add(dsaordbaseJson.getVar_ispec().trim());
                            mRowDatas.add(dsaordbaseJson.getVar_pattern().trim());
                            mRowDatas.add(dsaordbaseJson.getVar_chkparm().trim());
                            mRowDatas.add(formatNum.format(Double.valueOf(dsaordbaseJson.getDec_price())));
                            mRowDatas.add(formatNo.format(Double.valueOf(dsaordbaseJson.getDec_ordqty())));
                            double dec_amt = Double.valueOf(dsaordbaseJson.getDec_ordamt());
                            allPrice += dec_amt;
                            mRowDatas.add(formatNum.format(dec_amt));
                            mRowDatas.add(dsaordbaseJson.getVar_epsno().trim());
                            mRowDatas.add(dsaordbaseJson.getInv_epsno().trim());
                            mRowDatas.add(formatNum.format(Double.valueOf(TextUtils.isEmpty(dsaordbaseJson.getDec_setamt().trim()) ? "0.00" : dsaordbaseJson.getDec_setamt())));
                            mRowDatas.add(dsaordbaseJson.getDsaord_no());
                            mRowDatas.add(dsaordbaseJson.getLine_no());
                            mTableDatas.add(mRowDatas);

                        }
                        break;
                    case 1:
                        DsaordQueryJsonAccoutage dsaordQueryJsonAccoutage = dsaordbaseJsonsAccount.get(i);
                        if (!contents.contains(dsaordQueryJsonAccoutage.getName_corr() + dsaordQueryJsonAccoutage.getName_seller() + dsaordQueryJsonAccoutage.getName_area() + dsaordQueryJsonAccoutage.getName_zone())) {
                            contents.add(dsaordQueryJsonAccoutage.getName_corr() + dsaordQueryJsonAccoutage.getName_seller() + dsaordQueryJsonAccoutage.getName_area() + dsaordQueryJsonAccoutage.getName_zone());

                            mRowDatas.add(dsaordQueryJsonAccoutage.getName_corr());
                            mRowDatas.add(dsaordQueryJsonAccoutage.getName_seller());
                            mRowDatas.add(dsaordQueryJsonAccoutage.getName_area());
                            mRowDatas.add(dsaordQueryJsonAccoutage.getName_zone());
                            double _$130 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$130天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$130天());
                            double _$3060 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$3060天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$3060天());
                            double _$6090 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$6090天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$6090天());
                            double _$90180 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$90180天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$90180天());
                            double _$180270 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$180270天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$180270天());
                            double _$270365 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$270365天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$270365天());
                            double _$365730 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$365730天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$365730天());
                            double _$7301095 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$7301095天().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$7301095天());
                            double _$1095up = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage.get_$1095天以上().trim()) ? "0.00" : dsaordQueryJsonAccoutage.get_$1095天以上());
                            double all_acc = _$130 + _$3060 + _$6090 + _$90180 + _$180270 + _$270365 + _$365730 + _$7301095 + _$1095up;
                            mRowDatas.add(formatNum.format(_$130));
                            mRowDatas.add(formatNum.format(_$3060));
                            mRowDatas.add(formatNum.format(_$6090));
                            mRowDatas.add(formatNum.format(_$90180));
                            mRowDatas.add(formatNum.format(_$180270));
                            mRowDatas.add(formatNum.format(_$270365));
                            mRowDatas.add(formatNum.format(_$365730));
                            mRowDatas.add(formatNum.format(_$7301095));
                            mRowDatas.add(formatNum.format(_$1095up));
                            mRowDatas.add(formatNum.format(all_acc));
                            allPrice += all_acc;
                            mTableDatas.add(mRowDatas);
                        }

                        break;
                    case 2:
                        //日志查询
                        DsaordQueryJsonD dsaordQueryJsonD = dsaordbaseJsonsD.get(i);
                        mRowDatas.add(dsaordQueryJsonD.getName_wproj());
                        mRowDatas.add(dsaordQueryJsonD.getName_dept());
                        mRowDatas.add(dsaordQueryJsonD.getName_user());
                        mRowDatas.add(dsaordQueryJsonD.getDate_task());
                        mRowDatas.add(dsaordQueryJsonD.getName_type());
                        mRowDatas.add(dsaordQueryJsonD.getVar_contact());
                        mRowDatas.add(dsaordQueryJsonD.getVar_tel());
                        mRowDatas.add(dsaordQueryJsonD.getId_corr());
                        mRowDatas.add(dsaordQueryJsonD.getVar_contactduty());
                        mRowDatas.add(dsaordQueryJsonD.getVar_wtitle());
                        mRowDatas.add(dsaordQueryJsonD.getVar_remark());
                        mTableDatas.add(mRowDatas);
                        break;
                    case 3:
                        DsaordQueryJson dsaordbaseJson3 = dsaordqueryJsons.get(i);
                        if (!contents.contains(dsaordbaseJson3.getDsaord_no() + dsaordbaseJson3.getLine_no())) {
                            contents.add(dsaordbaseJson3.getDsaord_no() + dsaordbaseJson3.getLine_no());
                            mRowDatas.add(dsaordbaseJson3.getName_corr());
                            mRowDatas.add(dsaordbaseJson3.getName_terminal());
                            mRowDatas.add(dsaordbaseJson3.getName_seller());
                            mRowDatas.add(dsaordbaseJson3.getDate_opr());
                            mRowDatas.add(dsaordbaseJson3.getName_item());
                            mRowDatas.add(dsaordbaseJson3.getName_samth());
                            mRowDatas.add(dsaordbaseJson3.getVar_ispec().trim());
                            mRowDatas.add(dsaordbaseJson3.getVar_pattern().trim());
                            mRowDatas.add(formatNo.format(Double.valueOf(dsaordbaseJson3.getDec_ordqty())));
                            mRowDatas.add(formatNum.format(Double.valueOf(dsaordbaseJson3.getDec_price())));
                            double dec_amt1 = Double.valueOf(dsaordbaseJson3.getDec_ordamt());
                            allPrice += dec_amt1;
                            mRowDatas.add(formatNum.format(dec_amt1));
                            mTableDatas.add(mRowDatas);
                        }
                        break;
                    case 4:
                        DsaordQueryJsonAccoutage2 dsaordQueryJsonAccoutage2 = dsaordbaseJsonsAccount2.get(i);
                        if (!contents.contains(dsaordQueryJsonAccoutage2.getName_corr() + dsaordQueryJsonAccoutage2.getName_seller() + dsaordQueryJsonAccoutage2.get总欠款额().trim())) {
                            contents.add(dsaordQueryJsonAccoutage2.getName_corr() + dsaordQueryJsonAccoutage2.getName_seller() + dsaordQueryJsonAccoutage2.get总欠款额().trim());

                            mRowDatas.add(dsaordQueryJsonAccoutage2.getName_corr());
//                                      mRowDatas.add(dsaordbaseJson.getId_corr());
                            mRowDatas.add(dsaordQueryJsonAccoutage2.getName_seller());
                            double _$180365 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage2.get_$180365天().trim()) ? "0.00" : dsaordQueryJsonAccoutage2.get_$180365天());
                            double _$365up = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage2.get_$365天以上().trim()) ? "0.00" : dsaordQueryJsonAccoutage2.get_$365天以上());
                            double all_acc2 = Double.valueOf(TextUtils.isEmpty(dsaordQueryJsonAccoutage2.get总欠款额().trim()) ? "0.00" : dsaordQueryJsonAccoutage2.get总欠款额());
                            mRowDatas.add(formatNum.format(_$180365));
                            mRowDatas.add(formatNum.format(_$365up));
                            mRowDatas.add(formatNum.format(all_acc2));
                            firstPrice += _$180365;
                            secondPrice += _$365up;
                            mTableDatas.add(mRowDatas);
                        }
                        break;
                }
            }
            if (mTableDatas.size() == 1) {//如果只有表头
                mHandler.sendEmptyMessage(2);
            } else {
                mHandler.sendEmptyMessage(0);
            }
            LogShow("计算时间长：" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            LogShow("OrderFinding:数据设置异常");
        }
    }

    @Override
    public void processJsonValue(String value) {
        LogShow("回调数据:" +value);
        try {
            value = value.trim();
            if (value.equalsIgnoreCase("[]") || TextUtils.isEmpty(value)) {
                waitDialog.dismiss();
                mHandler.sendEmptyMessage(2);
                return;
            }
            JSONArray jsonArray = new JSONArray(value);
            //DsaordQueryJsonAccoutage
            switch (Constant.tab_type) {
                case 0:
                case 3:
                    dsaordqueryJsons.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String temp = jsonArray.getString(i);
                        Matcher m = p.matcher(temp);
                        String subValue = temp.substring(temp.indexOf("{"), temp.indexOf("}") + 1);
                        DsaordQueryJson dsaordbaseJson = mGson.fromJson(subValue, DsaordQueryJson.class);
                        dsaordqueryJsons.add(dsaordbaseJson);
                    }
                    break;
                case 2:
                    dsaordbaseJsonsD.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String temp = jsonArray.getString(i);
                        Matcher m = p.matcher(temp);
                        String subValue = temp.substring(temp.indexOf("{"), temp.indexOf("}") + 1);
                        DsaordQueryJsonD dsaordbaseJson = mGson.fromJson(subValue, DsaordQueryJsonD.class);
                        dsaordbaseJsonsD.add(dsaordbaseJson);
                    }
                    break;
                case 1:
                    dsaordbaseJsonsAccount.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String temp = jsonArray.getString(i);
                        Matcher m = p.matcher(temp);
                        String subValue = temp.substring(temp.indexOf("{"), temp.indexOf("}") + 1);
                        DsaordQueryJsonAccoutage dsaordbaseJson = mGson.fromJson(subValue, DsaordQueryJsonAccoutage.class);
                        dsaordbaseJsonsAccount.add(dsaordbaseJson);
                    }
                    break;
                case 4:
                    dsaordbaseJsonsAccount2.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String temp = jsonArray.getString(i);
                        Matcher m = p.matcher(temp);
                        String subValue = temp.substring(temp.indexOf("{"), temp.indexOf("}") + 1);
                        DsaordQueryJsonAccoutage2 dsaordbaseJson = mGson.fromJson(subValue, DsaordQueryJsonAccoutage2.class);
                        dsaordbaseJsonsAccount2.add(dsaordbaseJson);
                    }
                    break;
            }

            mHandler.sendEmptyMessage(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置表格
     */
    public class SetTableTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setTable();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }


    //获取表格
    private void getTab() {
        LogShow("开始形成表格。。。");
        long start = System.currentTimeMillis();
        mLockTableView = new LockTableView2(this, linear_list, mTableDatas);
        int max_weight;
        int max_height;
        if (Constant.tab_type == 2) {//工作日志加高加宽
            max_weight = 200;
            max_height = 10000;
            mLockTableView.setOntColumnWidth(true, 100);//是否单独更改第一列值的宽度
        } else {
            max_weight = 120;
            max_height = 200;
        }
        DecimalFormat formatNum = new DecimalFormat(",##0.00");
        all_price_textview.setText(formatNum.format(allPrice));
        first_price_textview.setText(formatNum.format(firstPrice));
        second_price_textview.setText(formatNum.format(secondPrice));

        if (changeRow) {
            mLockTableView.changeGravity(changeRowNo);
        }
        mLockTableView.setLockFristColumn(true) //是否锁定第一列
                .setLockFristRow(true) //是否锁定第一行
                .setMaxColumnWidth(max_weight) //列最大宽度
                .setMinColumnWidth(10) //列最小宽度
                .setMinRowHeight(20)//行最小高度
                .setMaxRowHeight(max_height)//行最大高度
                .setTextViewSize(14) //单元格字体大小
                .setFristRowBackGroudColor(R.color.item_table_title_color)//表头背景色
                .setTableHeadTextColor(R.color.white)//表头字体颜色
                .setTableHeadTextSize(15)
                .setTableContentTextColor(R.color.black)//单元格字体颜色
                .setNullableString("--") //空值替换值
                .show(); //显示表格,此方法必须调用

        LogShow("计算时间长：" + (System.currentTimeMillis() - start));
        waitDialog.dismiss();
        LogShow("表格显示完毕。。。");
        mHandler.sendEmptyMessage(4);
    }

    //选择后的返回事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 44 && resultCode == 22) {
            mHandler.sendEmptyMessage(1);
        } else if (requestCode == 33 && resultCode == 22) {
            mHandler.sendEmptyMessage(5);
        }

        ActionBarWidgetActivity.setNsyncDataConnector(this);
    }


    /**
     * 重置查询条件
     */
    private void reset() {
        user_myid = "";
        buss_key = "";
        buss_value = "";
        if (dsaordbaseJsons_new != null && dsaordbaseJsons_new.size() > 0) {
            dsaordbaseJsons_new.clear();
        }
        object_name.setText("");
        object_person.setText("");
        Constant.id_corr = "";
        Constant.name_searCorr = "";
        //后来决定时间不再重置，如果需要重置，解开以下代码
//        table_end_time.setText(getToday());
//        try {
//            Calendar cal = Calendar.getInstance();
//            Date date = format.parse(getToday());
//            cal.setTime(date);
//            cal.add(Calendar.DATE, -30);//为了和ios一致，改为减30天
////            cal.add(Calendar.MONTH, -1);
//            Date fDate = cal.getTime();
//            table_begin_time.setText(format.format(fDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            table_begin_time.setText(getToday());
//        }
////        mHandler.sendEmptyMessage(1);
////        object_name.setText("");
//
//        switch (Constant.tab_type) {
//            case 0:
//            case 3:
//                table_begin_time.setText(getToday());
//                break;
//
//
//        }
    }

    //页面关闭时，数据清零
    @Override
    public void onDestroy() {
        super.onDestroy();
        Constant.project_type = 0;
        Constant.travel = false;
        reset();
    }
}
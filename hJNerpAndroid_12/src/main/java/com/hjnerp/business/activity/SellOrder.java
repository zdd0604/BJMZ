package com.hjnerp.business.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.google.gson.Gson;
import com.hjnerp.business.BusinessAdapter.SellOrderAdapter;
import com.hjnerp.business.BusinessJsonCallBack.BFlagCallBack;
import com.hjnerp.common.ActionBarWidgetActivity;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Dsaordtype;
import com.hjnerp.model.Ej1345;
import com.hjnerp.model.businessFlag;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.MyListView;
import com.hjnerp.widget.MyToast;
import com.hjnerp.widget.MyToast2;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static android.jiebao.utils.Tools.getToday;
import static com.hjnerp.common.Constant.dsaordbaseJsons_new;
import static com.hjnerp.common.Constant.id_terminal_for_item;
import static com.hjnerp.common.Constant.id_wproj;
import static com.hjnerp.common.Constant.item_client;
import static com.hjnerp.common.Constant.item_peoject;
import static com.hjnerp.common.Constant.performanceDatas;
import static com.hjnerp.common.Constant.project_type;
import static com.hjnerp.common.Constant.sellDetails;
import static com.hjnerp.common.Constant.travel;
import static com.hjnerpandroid.R.id.action_right_tv1;


public class SellOrder extends ActionBarWidgetActivity implements View.OnClickListener,
        ActionSheet.ActionSheetListener {
    private RelativeLayout sell_actionbar_back;
    private TextView sell_tv_actionbar_title;
    private TextView var_title_code;
    private LinearLayout rejust_list;
    private TextView sell_order_recorder;
    private TextView sell_order_date_record;
    private Spinner sell_order_type;
    private TextView sell_order_terminal;
    private TextView sell_order_send;
    private Calendar c = Calendar.getInstance();
    private TextView sell_order_address;
    private Spinner sell_order_ticket_type;
    private TextView sell_order_ticket_time;
    private Spinner sell_order_space;
    private TextView sell_order_addess_send;
    private EditText sell_order_more;
    private MyListView order_list_view;
    private List<String> orderType = new ArrayList<String>();
    private List<String> orderType_id = new ArrayList<String>();
    private List<String> orderTicketType = new ArrayList<String>();
    private List<String> orderExpress = new ArrayList<String>();
    private List<String> orderTicketType_id = new ArrayList<String>();
    private List<String> orderExpress_id = new ArrayList<String>();
    private List<String> orderSpace = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private SellOrderAdapter sellOrderAdapter;
    private List<Ctlm1345> users;
    private List<Ctlm1345> sell = new ArrayList<>();
    private List<Ctlm1345> tax = new ArrayList<>();
    private List<Ctlm1345> express = new ArrayList<>();
    private String username;
    private String userID;
    private String companyID;
    private String id_dept;
    private String var_rcvcorr;
    private String id_terminal;
    private String name_terminal;
    private String var_tel;
    private String var_contact;
    private String name_corr;
    private String id_corr;
    private String dec_acclimit;
    private String a;
    private String b;
    private WaitDialogRectangle waitDialogRectangle;
    private TextView sell_over_money;

    private int no;
    private WaitDialogRectangle waitDialog;
    private int countDetail;
    private Spinner sell_order_express;

    //    @BindView(R.id.action_left_img)
//    ImageView actionLeftImg;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    //    @BindView(R.id.action_back_layout)
//    LinearLayout actionBackLayout;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.add_sell_order_detail)
    LinearLayout add_sell_order_detail;
    private String old_var_invaddr;
    private String old_var_tel;
    private String old_var_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        actionCenterTv.setText(getString(R.string.action_center_content_sellTitle));
        actionRightTv.setText(getString(R.string.action_right_content_send));
        actionRightTv1.setText(getString(R.string.action_right_content_save));
        actionRightTv1.setVisibility(View.VISIBLE);
        actionLeftTv.setOnClickListener(this);
        actionRightTv.setOnClickListener(this);
        actionRightTv1.setOnClickListener(this);

        var_title_code = (TextView) findViewById(R.id.var_title_code);
        sell_order_recorder = (TextView) findViewById(R.id.sell_order_recorder);
        sell_order_date_record = (TextView) findViewById(R.id.sell_order_date_record);
        sell_order_type = (Spinner) findViewById(R.id.sell_order_type_1);
        sell_order_terminal = (TextView) findViewById(R.id.sell_order_terminal);
        sell_order_terminal.setOnClickListener(this);
        sell_order_send = (TextView) findViewById(R.id.sell_order_send);
//        sell_order_num = (EditText) findViewById(sell_order_num);
        sell_order_address = (TextView) findViewById(R.id.sell_order_address);
        sell_order_address.setOnClickListener(this);
        sell_order_ticket_type = (Spinner) findViewById(R.id.sell_order_ticket_type);
        sell_order_ticket_time = (TextView) findViewById(R.id.sell_order_ticket_time);
        sell_order_ticket_time.setOnClickListener(this);
        sell_order_space = (Spinner) findViewById(R.id.sell_order_space);
        sell_order_addess_send = (TextView) findViewById(R.id.sell_order_addess_send);
        sell_order_addess_send.setOnClickListener(this);
        sell_order_more = (EditText) findViewById(R.id.sell_order_more);
        order_list_view = (MyListView) findViewById(R.id.order_list_view);
        sellDetails = new ArrayList<>();
        sell_over_money = (TextView) findViewById(R.id.sell_over_money);
        sellOrderAdapter = new SellOrderAdapter(sellDetails, this, R.layout.item_sell_order, this);
        order_list_view.setAdapter(sellOrderAdapter);
        add_sell_order_detail.setOnClickListener(this);
        sell_order_express = (Spinner) findViewById(R.id.sell_order_express);
    }

    private void initData() {
//        Constant.dsaordbaseJsons_new = new ArrayList<>();
        users = new ArrayList<>();
        users = BusinessBaseDao.getCTLM1345ByIdTable("user");
        sell = BusinessBaseDao.getCTLM1345ByIdTable("dsaordtype");
        tax = BusinessBaseDao.getCTLM1345ByIdTable("invtype");
        express = BusinessBaseDao.getCTLM1345ByIdTable("express");
        if (users.size() == 0) {
            ToastUtil.ShowShort(this, "请先下载基础数据");
            finish();
            return;
        }

        String userinfos = users.get(0).getVar_value();
//        id_clerk =
        Gson gson1 = new Gson();
        Ej1345 ej1345 = gson1.fromJson(userinfos, Ej1345.class);
        int selected_no = 0;
        for (int i = 0; i < sell.size(); i++) {
            Dsaordtype dsaordtype = gson1.fromJson(sell.get(i).getVar_value(), Dsaordtype.class);
            orderType.add(dsaordtype.getName_ordtype());
            orderType_id.add(dsaordtype.getId_ordtype());
            if (orderType_id.get(i).equalsIgnoreCase("P")) {
                selected_no = i;
            }
        }
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, orderType);
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        sell_order_type.setAdapter(adapter);

        sell_order_type.setSelection(selected_no);
        sell_order_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sellOrderAdapter.SetOrderType(orderType_id.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        int selected_express_no = 0;
        for (int i = 0; i < express.size(); i++) {
            Dsaordtype dsaordtype = gson1.fromJson(express.get(i).getVar_value(), Dsaordtype.class);
            orderExpress.add(dsaordtype.getName_express());
            orderExpress_id.add(dsaordtype.getId_express());
            if (orderExpress_id.get(i).equalsIgnoreCase("106")) {
                selected_express_no = i;
            }
        }
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, orderExpress);
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        sell_order_express.setAdapter(adapter);
        sell_order_express.setSelection(selected_express_no);
        for (int i = 0; i < tax.size(); i++) {
            Dsaordtype dsaordtype = gson1.fromJson(tax.get(i).getVar_value(), Dsaordtype.class);
            orderTicketType.add(dsaordtype.getName_invtype());
            orderTicketType_id.add(dsaordtype.getId_invtype());
        }
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, orderTicketType);
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        sell_order_ticket_type.setAdapter(adapter);
        orderSpace.add("是");
        orderSpace.add("否");
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, orderSpace);
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        sell_order_space.setAdapter(adapter);
        sell_order_space.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    sell_order_addess_send.setClickable(true);
                    sell_order_addess_send.setTextColor(mContext.getResources().getColor(R.color.black));
                } else {
                    sell_order_addess_send.setClickable(false);
                    sell_order_addess_send.setText(sell_order_address.getText());
                    sell_order_addess_send.setTextColor(mContext.getResources().getColor(R.color.title_hint));

//                    if (dsaordbaseJsons_new != null && dsaordbaseJsons_new.size() > 0) {
//                        sell_order_addess_send.setSelection(sell_order_address.getSelectedItemPosition());
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        username = ej1345.getName_user();
        userID = ej1345.getId_user();
        companyID = ej1345.getId_com();
        id_dept = ej1345.getId_dept();
        sell_order_recorder.setText(username);
        sell_order_date_record.setText(getToday());
        sell_order_ticket_time.setText(getToday());
        if (Constant.JUDGE_TYPE) {
            Constant.billsNo = "";
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");
            a = f.format(c.getTime());
            b = f2.format(c.getTime());
            no = 0;
            SellOrderModel sellOrderModel = new SellOrderModel();
            sellOrderModel.setOrder_first(true);
            sellOrderModel.setOrder_no(no);
//        sellOrderModel.setOrder_delete(false);
            sellDetails.add(sellOrderModel);
            no++;
        } else {
            Constant.billsNo = performanceDatas.getMain().getDsaord_no();
            var_title_code.setText(Constant.billsNo);

//            orderAddress.add(performanceDatas.getMain().getVar_addr());
//            adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, orderAddress);
//            adapter.setDropDownViewResource(R.layout.spinner_item_hint);
//            sell_order_address.setAdapter(adapter);
//            sell_order_addess_send.setAdapter(adapter);
            sell_order_address.setText(performanceDatas.getMain().getVar_addr());
            sell_order_addess_send.setText(performanceDatas.getMain().getVar_invaddr());
            old_var_invaddr = performanceDatas.getMain().getVar_invaddr();
            var_rcvcorr = performanceDatas.getMain().getVar_rcvcorr();
            id_terminal = performanceDatas.getMain().getId_terminal();
            name_terminal = performanceDatas.getMain().getName_terminal();
            dec_acclimit = performanceDatas.getMain().getDec_acclimit();
            var_tel = performanceDatas.getMain().getVar_tel();
            old_var_tel = performanceDatas.getMain().getVar_tel();
            var_contact = performanceDatas.getMain().getVar_contact();
            old_var_contact = performanceDatas.getMain().getVar_contact();
//            id_seller = performanceDatas.getMain().getId_seller();
            id_corr = performanceDatas.getMain().getId_corr();
            name_corr = performanceDatas.getMain().getName_corr();
            sell_order_terminal.setText(name_terminal);
            DecimalFormat format = new DecimalFormat(",##0.00");
            sell_over_money.setText(format.format(Double.valueOf(performanceDatas.getMain().getDec_acaramt())));
            sell_order_send.setText(name_corr);
            id_terminal_for_item = id_corr;
            Constant.id_terminal_for_address = id_terminal;
//            orderAddress = new ArrayList<>();
//            DsaordbaseJson dsaordbaseJson = new DsaordbaseJson();
//            dsaordbaseJson.setId_corr(performanceDatas.getMain().getId_corr());
            a = performanceDatas.getMain().getDate_opr();
            b = performanceDatas.getMain().getDate_sign();
            for (int i = 0; i < orderType_id.size(); i++) {
                if (performanceDatas.getMain().getId_ordtype().equalsIgnoreCase(orderType_id.get(i))) {
                    sell_order_type.setSelection(i);
                }
            }
            for (int i = 0; i < orderTicketType_id.size(); i++) {
                if (performanceDatas.getMain().getId_invtype().equalsIgnoreCase(orderTicketType_id.get(i))) {
                    sell_order_ticket_type.setSelection(i);
                }
            }
            for (int i = 0; i < orderExpress_id.size(); i++) {
                if (performanceDatas.getMain().getId_express().equalsIgnoreCase(orderExpress_id.get(i))) {
                    sell_order_express.setSelection(i);
                }
            }
            if (performanceDatas.getMain().getFlag_itemwith().equalsIgnoreCase("Y")) {
                sell_order_space.setSelection(0);
            } else {
                sell_order_space.setSelection(1);

            }
            sell_order_ticket_time.setText(TextUtils.isEmpty(performanceDatas.getMain().getDate_demand().trim()) ? getToday() : performanceDatas.getMain().getDate_demand().trim());
            sell_order_more.setText(performanceDatas.getMain().getVar_remark());
            no = 0;
            countDetail = performanceDatas.getDetails().size();
            for (int i = 0; i < performanceDatas.getDetails().size(); i++) {
                SellOrderModel sellOrderModel = new SellOrderModel();
                sellOrderModel.setOrder_first(i == 0);
                sellOrderModel.setOrder_no(no);
                sellOrderModel.setName_item(performanceDatas.getDetails().get(i).getName_item());
                sellOrderModel.setId_tax(performanceDatas.getDetails().get(i).getId_tax());
                sellOrderModel.setOrder_num(Double.valueOf(performanceDatas.getDetails().get(i).getDec_qty()));
                sellOrderModel.setOrder_price(Double.valueOf(performanceDatas.getDetails().get(i).getDec_oriamt()));
                sellOrderModel.setPer_price(Double.valueOf(performanceDatas.getDetails().get(i).getDec_price()));
                sellOrderModel.setId_item(performanceDatas.getDetails().get(i).getId_item());
                sellOrderModel.setId_sell(performanceDatas.getDetails().get(i).getId_satype());
                sellDetails.add(sellOrderModel);
                no++;
            }
        }
        sellOrderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        project_type = 0;
        travel = false;
        dsaordbaseJsons_new = new ArrayList<>();
        id_terminal_for_item = "";
        Constant.id_terminal_for_address = "";
        sellDetails.clear();
        setResult(33);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case action_right_tv1:
                caculatePrice();
                saveAndSend("save");
                break;
            case R.id.action_right_tv:
                saveAndSend("send");
                break;
            case R.id.action_left_tv:
                finish();
                break;
//            case R.id.action_right_tv:
//                popupWindow();
//                break;
            case R.id.sell_order_terminal:

                Intent intent = new Intent(getApplicationContext(), BusinessSearch.class);
                project_type = 1;
                travel = true;
                startActivityForResult(intent, 33);
                break;
            case R.id.sell_order_ticket_time:
                showCalendar(sell_order_ticket_time);
                break;
            case R.id.add_sell_order_detail:
//                sellDetails.clear();
//                sellDetails = sellOrderAdapter.getListDeatils();
                Constant.JUDGE_TYPE1 = true;
                SellOrderModel sellOrderModel = new SellOrderModel();
                sellOrderModel.setOrder_first(false);
                sellOrderModel.setOrder_no(no);
                sellDetails.add(sellOrderModel);
                sellOrderAdapter.notifyDataSetChanged();
                no++;
                break;
            case R.id.sell_order_addess_send:
                Intent intent1 = new Intent(this, BusinessSearch.class);
                Constant.project_type = 3;
                Constant.travel = true;
                startActivityForResult(intent1, 66);
                break;
            case R.id.sell_order_address:
                name_terminal = sell_order_terminal.getText().toString().trim();
                if (TextUtils.isEmpty(name_terminal)) {
//            Toast.makeText(this, "请先选择所属终端", Toast.LENGTH_SHORT).show();
                    new MyToast2(SellOrder.this, "请先选择所属终端!");

                    return;
                }
                Intent intent2 = new Intent(this, BusinessSearch.class);
                Constant.project_type = 3;
                Constant.travel = true;
                startActivityForResult(intent2, 55);
                break;
        }
    }

    /**
     * 底部popupwindow弹框
     */
    private void popupWindow() {
        setTheme(R.style.ActionSheetStyleiOS7);
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("保存", "送审")
                .setCancelableOnTouchOutside(true)
                .setListener(this).show();
    }

    /**
     * item点击事件
     *
     * @param actionSheet
     * @param index
     */
    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        dateCommit(index);
    }

    /**
     * 取消事件
     *
     * @param actionSheet
     * @param isCancel
     */
    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    /**
     * 判断点击的是什么
     *
     * @param index
     */
    private void dateCommit(int index) {
        switch (index) {
            case Constant.HANDLERTYPE_0:
                caculatePrice();
                saveAndSend("save");
                break;
            case Constant.HANDLERTYPE_1:
                saveAndSend("send");
                break;
        }
    }

    private void saveAndSend(String save) {
        name_terminal = sell_order_terminal.getText().toString().trim();
        if (TextUtils.isEmpty(name_terminal)) {
//            Toast.makeText(this, "请先选择所属终端", Toast.LENGTH_SHORT).show();
            new MyToast2(SellOrder.this, "请先选择所属终端!");

            return;
        }
        name_corr = sell_order_send.getText().toString().trim();
        if (TextUtils.isEmpty(name_corr)) {
//            Toast.makeText(this, "客户名不能为空", Toast.LENGTH_SHORT).show();
            new MyToast2(SellOrder.this, "客户名不能为空!");

            return;
        }
        String time_p = sell_order_ticket_time.getText().toString().trim();
        if (TextUtils.isEmpty(time_p)) {
//            Toast.makeText(this, "请先选择指定发货日期", Toast.LENGTH_SHORT).show();
            new MyToast2(SellOrder.this, "请先选择发货日期!");

            return;
        }
        //取消预计开票时间，设置为0000-00-00
        String time = getToday();
        String more = sell_order_more.getText().toString().trim();
        String address = sell_order_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
//            Toast.makeText(this, "请先选择送货地址", Toast.LENGTH_SHORT).show();
            new MyToast2(SellOrder.this, "请先选择送货地址!");

            return;
        }
        String var_invaddr = sell_order_addess_send.getText().toString().trim();
        if (TextUtils.isEmpty(var_invaddr)) {
//            Toast.makeText(this, "请先选择发票邮寄地址", Toast.LENGTH_SHORT).show();
            new MyToast2(SellOrder.this, "请先选择发票地址!");

            return;
        }
//        if (TextUtils.isEmpty(dec_acclimit)) {
//            dec_acclimit = "";
//        }
        double allprice = 0;
        if (sellDetails.size() > 0)
            for (int i = 0; i < sellDetails.size(); i++) {
                allprice = allprice + sellDetails.get(i).getOrder_price();
//            ToastUtil.ShowShort(this, String.valueOf(allprice));
            }

        StringBuffer stringBuffer = new StringBuffer();
        companyID = "CM1101-0002";
        stringBuffer.append("{\"tableid\":\"dsaord\",\"opr\":\"SS\",\"no\":\"" + Constant.billsNo + "\",\"userid\":\"" + userID + "\",\"comid\":\"" + companyID + "\",");
        stringBuffer.append("\"menuid\":\"002020\",\"dealtype\":\"" + save + "\",\"data\":[");
        int gap = 0;
        int cycleTime = 0;
        if (Constant.JUDGE_TYPE) {
            gap = 0;
            cycleTime = sellDetails.size();

        } else {
            gap = sellDetails.size() - countDetail;
            if (gap >= 0) {
                cycleTime = sellDetails.size();
            } else {
                cycleTime = countDetail;
            }
        }
        for (int i = 0; i < cycleTime; i++) {
            if (!Constant.JUDGE_TYPE) {
                if (i >= countDetail) {
                    stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"N\",\"where\":\" \",\"data\":[");
                    stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + a + "\",\"datatype\":\"datetime\"}, ");
                } else if (i >= countDetail + gap) {
                    stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"D\",\"where\":\" \",\"data\":[");

                } else {
                    stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"U\",\"where\":\" \",\"data\":[");

                }
            } else {
                stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"N\",\"where\":\" \",\"data\":[");

            }
            stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"L\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"flag_prerec\",\"value\":\"N\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"flag_temp\",\"value\":\"N\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_channel\",\"value\":\"10\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_delivery\",\"value\":\"101\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"dec_srate\",\"value\":\"1\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_saprt\",\"value\":\"P01\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + userID + "\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + id_dept + "\",\"datatype\":\"varchar\"},");

            stringBuffer.append("{\"column\":\"date_sign\",\"value\":\"" + b + "\",\"datatype\":\"datetime\"}, ");
            stringBuffer.append("{\"column\":\"flag_psts\",\"value\":\"\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_table\",\"value\":\"\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"FBsa\",\"datatype\":\"varchar\"},");
            stringBuffer.append("{\"column\":\"line_no\",\"value\":\"" + (i + 1) + "\",\"datatype\":\"int\"}, ");
            stringBuffer.append("{\"column\":\"id_ordsource\",\"value\":\"001\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_curr\",\"value\":\"CNY\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_contact\",\"value\":\"" + var_contact + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_corr\",\"value\":\"" + id_corr + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"name_corr\",\"value\":\"" + name_corr + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_seller\",\"value\":\"" + userID + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"dec_acaramt\",\"value\":\"" + sell_over_money.getText().toString().replaceAll(",", "") + "\",\"datatype\":\"double\"}, ");
            stringBuffer.append("{\"column\":\"dec_acclimit\",\"value\":\"" + dec_acclimit.replaceAll(",", "") + "\",\"datatype\":\"double\"}, ");
//            stringBuffer.append("{\"column\":\"var_invcontact\",\"value\":\"" + var_contact + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_tel\",\"value\":\"" + var_tel + "\",\"datatype\":\"varchar\"}, ");
//            stringBuffer.append("{\"column\":\"var_invtel\",\"value\":\"" + var_tel + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_terminal\",\"value\":\"" + id_terminal + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"name_terminal\",\"value\":\"" + name_terminal + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_ordtype\",\"value\":\"" + orderType_id.get(sell_order_type.getSelectedItemPosition()) + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_invtype\",\"value\":\"" + orderTicketType_id.get(sell_order_ticket_type.getSelectedItemPosition()) + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + more + "\",\"datatype\":\"varchar\"}, ");
            if (!Constant.JUDGE_TYPE && (i >= countDetail + gap)) {
                stringBuffer.append("{\"column\":\"id_satype\",\"value\":\"" + sellDetails.get(0).getId_sell() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_item\",\"value\":\"" + sellDetails.get(0).getId_item() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"name_item\",\"value\":\"" + sellDetails.get(0).getName_item() + "\",\"datatype\":\"varchar\"}, ");
//                stringBuffer.append("{\"column\":\"var_chkparm\",\"value\":\"" + sellDetails.get(0).getVar_chkparm() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_uom\",\"value\":\"" + sellDetails.get(0).getId_uom() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"dec_qty\",\"value\":\"" + String.valueOf(sellDetails.get(0).getOrder_num()) + "\",\"datatype\":\"double\"}, ");
                stringBuffer.append("{\"column\":\"dec_oriprice\",\"value\":\"" + String.valueOf(sellDetails.get(0).getPer_price()) + "\",\"datatype\":\"double\"}, ");
                stringBuffer.append("{\"column\":\"dec_oriamt\",\"value\":\"" + String.valueOf(sellDetails.get(0).getOrder_price()) + "\",\"datatype\":\"double\"}, ");
                stringBuffer.append("{\"column\":\"id_tax\",\"value\":\"" + sellDetails.get(0).getId_tax() + "\",\"datatype\":\"varchar\"}, ");

            } else {
                String item = sellDetails.get(i).getName_item();
                if (TextUtils.isEmpty(item)) {
//                    Toast.makeText(this, "请选择产品", Toast.LENGTH_SHORT).show();
                    new MyToast2(SellOrder.this, "明细(" + (i + 1) + ")请选择产品!");
                    return;
                }
                if (sellDetails.get(i).getOrder_num() == 0) {
//                    Toast.makeText(this, "请选择产品", Toast.LENGTH_SHORT).show();
                    new MyToast2(SellOrder.this, "明细(" + (i + 1) + ")数量不能为0!");
                    return;
                }
                stringBuffer.append("{\"column\":\"id_tax\",\"value\":\"" + sellDetails.get(i).getId_tax() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_satype\",\"value\":\"" + sellDetails.get(i).getId_sell() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_item\",\"value\":\"" + sellDetails.get(i).getId_item() + "\",\"datatype\":\"varchar\"}, ");
//                stringBuffer.append("{\"column\":\"var_chkparm\",\"value\":\"" + sellDetails.get(i).getVar_chkparm() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"name_item\",\"value\":\"" + sellDetails.get(i).getName_item() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_uom\",\"value\":\"" + sellDetails.get(i).getId_uom() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"dec_qty\",\"value\":\"" + String.valueOf(sellDetails.get(i).getOrder_num()) + "\",\"datatype\":\"double\"}, ");
                stringBuffer.append("{\"column\":\"dec_oriprice\",\"value\":\"" + String.valueOf(sellDetails.get(i).getPer_price()) + "\",\"datatype\":\"double\"}, ");
                stringBuffer.append("{\"column\":\"dec_oriamt\",\"value\":\"" + String.valueOf(sellDetails.get(i).getOrder_price()) + "\",\"datatype\":\"double\"}, ");

            }
            //票货同行单拿出来
            if (sell_order_space.getSelectedItemPosition() == 0) {//选择是
                stringBuffer.append("{\"column\":\"flag_itemwith\",\"value\":\"Y\",\"datatype\":\"char\"}, ");
                stringBuffer.append("{\"column\":\"var_invaddr\",\"value\":\"" + old_var_invaddr + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invtel\",\"value\":\"" + old_var_tel + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invcontact\",\"value\":\"" + old_var_contact + "\",\"datatype\":\"varchar\"}, ");
//                stringBuffer.append("{\"column\":\"id_invexpress\",\"value\":\"" + old_var_contact + "\",\"datatype\":\"varchar\"}, ");

            } else {//选择否
                stringBuffer.append("{\"column\":\"flag_itemwith\",\"value\":\"N\",\"datatype\":\"char\"}, ");
                stringBuffer.append("{\"column\":\"var_invaddr\",\"value\":\"" + var_invaddr + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invtel\",\"value\":\"" + var_tel + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invcontact\",\"value\":\"" + var_contact + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_invexpress\",\"value\":\"" + orderExpress_id.get(sell_order_express.getSelectedItemPosition()) + "\",\"datatype\":\"varchar\"}, ");
//                stringBuffer.append("{\"column\":\"var_invaddr\",\"value\":\"" + old_var_invaddr + "\",\"datatype\":\"varchar\"}, ");
//                stringBuffer.append("{\"column\":\"var_invtel\",\"value\":\"" + old_var_tel + "\",\"datatype\":\"varchar\"}, ");
//                stringBuffer.append("{\"column\":\"var_invcontact\",\"value\":\"" + old_var_contact + "\",\"datatype\":\"varchar\"}, ");

            }
            stringBuffer.append("{\"column\":\"dec_orisamt\",\"value\":\"" + String.valueOf(allprice) + "\",\"datatype\":\"double\"}, ");
            stringBuffer.append("{\"column\":\"var_rcvcorr\",\"value\":\"" + var_rcvcorr + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_addr\",\"value\":\"" + address + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_express\",\"value\":\"" + orderExpress_id.get(sell_order_express.getSelectedItemPosition()) + "\",\"datatype\":\"varchar\"}, ");
//            stringBuffer.append("{\"column\":\"var_invaddr\",\"value\":\"" + var_invaddr + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"date_planinv\",\"value\":\"" + time + "\",\"datatype\":\"datetime\"}, ");
            stringBuffer.append("{\"column\":\"date_demand\",\"value\":\"" + time_p + "\",\"datatype\":\"datetime\"}]}");
            if (i != cycleTime - 1) {
                stringBuffer.append(",");
            }

        }
        stringBuffer.append("]}");
        String str = stringBuffer.toString();
        Log.e("str1", str);
        getBusinessList(str, save);
    }

    private void getBusinessList(String datas, final String dealtype) {
        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
            waitDialogRectangle.dismiss();
        }
        waitDialogRectangle = new WaitDialogRectangle(this);
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
                        String flag = businessFlag.getFlag();
                        if (dealtype.equals(Constant.SAVE_DEALTYPE)) {
                            Constant.billsNo = businessFlag.getNo();
//                            ToastUtil.ShowShort(getApplicationContext(), content);
                            if (flag.equalsIgnoreCase("Y")) {
                                new MyToast(SellOrder.this, "成功!");
                                setResult(33);
                            } else {
                                new MyToast2(SellOrder.this, "失败!");
                            }
                            var_title_code.setText(Constant.billsNo);
                            Constant.JUDGE_TYPE = false;
                            countDetail = sellDetails.size();
                        } else {
                            Constant.billsNo = businessFlag.getNo();
//                            ToastUtil.ShowShort(getApplicationContext(), content);
                            if (flag.equalsIgnoreCase("Y")) {
                                new MyToast(SellOrder.this, "成功!");
                                setResult(22);
                                finish();
                            } else {
                                new MyToast2(SellOrder.this, "失败!");
                            }
//                            setResult(22);
//                            finish();
                        }
                        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                            waitDialogRectangle.dismiss();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (e instanceof OkGoException) {
//                            ToastUtil.ShowShort(getApplicationContext(), "网络错误");
                            new MyToast2(SellOrder.this, "网络错误!");
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
//                                ToastUtil.ShowShort(getApplicationContext(), "提交失败");
                                new MyToast2(SellOrder.this, "提交失败!");

                            } else {
//                                ToastUtil.ShowShort(getApplicationContext(), e.getMessage());
                                new MyToast2(SellOrder.this, e.getMessage());
                            }


                        }
                        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                            waitDialogRectangle.dismiss();
                        }
                    }
                });
    }


    private void caculatePrice() {
//        if (dsaordbaseJsons_new == null || dsaordbaseJsons_new.size() == 0) {
//            ToastUtil.ShowShort(this, "请先选择所属终端");
//            return;
//        }
        add_sell_order_detail.requestFocusFromTouch();
        for (int i = 0; i < sellDetails.size(); i++) {
            sellDetails.get(i).setOrder_price(sellDetails.get(i).getPer_price()
                    * sellDetails.get(i).getOrder_num());
//            for (int j = 0; j < dsaordbaseJsons_new.size(); j++) {
//                if (sellDetails.get(i).getId_item().equalsIgnoreCase(dsaordbaseJsons_new.get(j).getId_item())) {
//                    sellDetails.get(i).setOrder_price(Double.parseDouble(dsaordbaseJsons_new.get(j).getDec_price())
//                            * sellDetails.get(i).getOrder_num());
//                }
//            }

        }
        sellOrderAdapter.notifyDataSetChanged();
    }

    private void showCalendar(final TextView editText) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 33 && resultCode == 22) {
            mHandler.sendEmptyMessage(0);
        } else if (requestCode == 44 && resultCode == 22) {
            mHandler.sendEmptyMessage(1);

        } else if (requestCode == 55 && resultCode == 22) {
            mHandler.sendEmptyMessage(2);

        } else if (requestCode == 66 && resultCode == 22) {
            mHandler.sendEmptyMessage(3);

        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getItem();
                    break;
                case 1:
                    sellOrderAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    sell_order_address.setText(item_peoject);
                    var_tel = id_wproj;
                    var_contact = item_client;
                    if (sell_order_space.getSelectedItemPosition() == 0) {
                        sell_order_addess_send.setText(item_peoject);
                    }
                    break;
                case 3:
                    sell_order_addess_send.setText(item_peoject);
                    break;

            }
        }
    };

    private void getItem() {
        sell_order_terminal.setText(dsaordbaseJsons_new.get(0).getName_terminal());
        sell_order_send.setText(dsaordbaseJsons_new.get(0).getName_corr());
        DecimalFormat format = new DecimalFormat(",##0.00");
        sell_over_money.setText(format.format(Double.valueOf(dsaordbaseJsons_new.get(0).getDec_acaramt())));
        dec_acclimit = dsaordbaseJsons_new.get(0).getDec_acclimit();
        var_rcvcorr = dsaordbaseJsons_new.get(0).getVar_rcvcorr();
        id_terminal = dsaordbaseJsons_new.get(0).getId_terminal();
        id_corr = dsaordbaseJsons_new.get(0).getId_corr();
        id_terminal_for_item = id_corr;
        Constant.id_terminal_for_address = id_terminal;
        for (int i = 0; i < orderTicketType_id.size(); i++) {
            if (dsaordbaseJsons_new.get(0).getId_invtype().equalsIgnoreCase(orderTicketType_id.get(i))) {
                sell_order_ticket_type.setSelection(i);
            }
        }
        sell_order_address.setText("");
        sell_order_addess_send.setText("");
        for (int i = 0; i < sellDetails.size(); i++) {
            sellDetails.get(i).setName_item("");
            sellDetails.get(i).setId_item("");
            sellDetails.get(i).setOrder_num(0);
            sellDetails.get(i).setPer_price(0);
            sellDetails.get(i).setOrder_price(0);
        }
        sellOrderAdapter.notifyDataSetChanged();
    }


}



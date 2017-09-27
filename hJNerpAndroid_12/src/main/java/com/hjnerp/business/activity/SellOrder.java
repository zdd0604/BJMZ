package com.hjnerp.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.hjnerp.model.BusinessChange;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Dsaordtype;
import com.hjnerp.model.Ej1345;
import com.hjnerp.model.SellOrderModel;
import com.hjnerp.model.businessFlag;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.MyListView;
import com.hjnerp.widget.MyToast2;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.OkGoException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private List<String> orderType = new ArrayList<String>();//订单类型具体名称
    private List<String> orderType_id = new ArrayList<String>();//订单类型id
    private List<String> orderTicketType = new ArrayList<String>();//发票类型名称
    private List<String> orderExpress = new ArrayList<String>();//邮寄方式
    private List<String> orderTicketType_id = new ArrayList<String>();//发票类型id
    private List<String> orderExpress_id = new ArrayList<String>();//邮寄方式的id
    private List<String> orderSpace = new ArrayList<String>();//票货同行选项
    private ArrayAdapter<String> adapter;//spinner的adapter
    private ArrayAdapter<String> adapter_express;//邮寄方式的adapter(因为有的要隐藏)
    private ArrayAdapter<String> adapter_grey;//邮寄方式的灰色adapter，当票货同行选择否的时候用到
    private SellOrderAdapter sellOrderAdapter;//订单明细的adapter
    private List<Ctlm1345> users;//用户信息
    private List<Ctlm1345> sell = new ArrayList<>();//订单类型，从1345获取，下同
    private List<Ctlm1345> tax = new ArrayList<>();//发票税种，同上
    private List<Ctlm1345> express = new ArrayList<>();//邮寄方式，同上
    private String username;//用户名
    private String userID;//用户id
    private String companyID;//公司id
    private String id_dept;//部门id
    private String var_rcvcorr;//结算客户
    private String id_terminal;//终端id
    private String name_terminal;//终端名称
    private String var_tel;//电话
    private String var_inv_tel;//发票邮寄电话
    private String var_contact;//联系人
    private String var_inv_contact;//发票邮寄联系人
    private String name_corr;//客户名称
    private String id_corr;//客户id
    private String dec_acclimit;//账期
    private String orderTicket_id;//发票类型id
    private String a;//操作时间
    private String b;//订单日期
    private int no;//订单明细号
    private int countDetail;//列表返回的明细行数

    private Spinner sell_order_invexpress;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.add_sell_order_detail)
    LinearLayout add_sell_order_detail;

    @BindView(R.id.var_title_code)
    TextView var_title_code;
    @BindView(R.id.sell_order_recorder)
    TextView sell_order_recorder;
    @BindView(R.id.sell_order_date_record)
    TextView sell_order_date_record;
    @BindView(R.id.sell_order_type_1)
    Spinner sell_order_type;
    @BindView(R.id.sell_order_terminal)
    TextView sell_order_terminal;
    @BindView(R.id.sell_order_send)
    TextView sell_order_send;
    @BindView(R.id.sell_order_address)
    TextView sell_order_address;
    @BindView(R.id.sell_order_ticket_type)
    TextView sell_order_ticket_type;
    @BindView(R.id.sell_order_ticket_time)
    TextView sell_order_ticket_time;
    @BindView(R.id.sell_order_space)
    Spinner sell_order_space;
    @BindView(R.id.sell_order_addess_send)
    TextView sell_order_addess_send;
    @BindView(R.id.sell_order_more)
    EditText sell_order_more;
    @BindView(R.id.order_list_view)
    MyListView order_list_view;
    @BindView(R.id.sell_over_money)
    TextView sell_over_money;
    @BindView(R.id.sell_order_express)
    Spinner sell_order_express;
    @BindView(R.id.sellord_scroll)
    ScrollView sellord_scroll;
    private String id_zone;//区域
    private String id_area;//地区
    private String id_corrtype;//客户类型
    private String id_industry;//行业

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    //开始进行界面设置
    private void initView() {
        actionCenterTv.setText(getString(R.string.sellOrder_Tile_NewActivity));
        actionRightTv.setText(getString(R.string.action_right_content_send));
        actionRightTv1.setText(getString(R.string.action_right_content_save));
        actionRightTv1.setVisibility(View.VISIBLE);
        sellDetails = new ArrayList<>();

        sellOrderAdapter = new SellOrderAdapter(sellDetails, this, R.layout.item_sell_order, this);//明细数据适配
        order_list_view.setAdapter(sellOrderAdapter);

        add_sell_order_detail.setOnClickListener(this);//添加明细
        actionLeftTv.setOnClickListener(this);
        actionRightTv.setOnClickListener(this);
        actionRightTv1.setOnClickListener(this);
        sell_order_addess_send.setOnClickListener(this);
        sell_order_ticket_time.setOnClickListener(this);
        sell_order_terminal.setOnClickListener(this);
        sell_order_address.setOnClickListener(this);
        sell_order_ticket_type = (TextView) findViewById(R.id.sell_order_ticket_type);
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
        sell_order_invexpress = (Spinner) findViewById(R.id.sell_order_invexpress);
    }

    //数据的获取
    private void initData() {
        users = new ArrayList<>();
        //从1345获取以下信息
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
        Gson gson1 = new Gson();
        //数据转换为1345
        Ej1345 ej1345 = gson1.fromJson(userinfos, Ej1345.class);
        int selected_no = 0;//默认选择第一项
        for (int i = 0; i < sell.size(); i++) {
            Dsaordtype dsaordtype = gson1.fromJson(sell.get(i).getVar_value(), Dsaordtype.class);//遍历订单类型和id
            orderType.add(dsaordtype.getName_ordtype());
            orderType_id.add(dsaordtype.getId_ordtype());
            if (orderType_id.get(i).equalsIgnoreCase("P")) {//订单类型id为P时，即为正常销售，也就是默认选中的项目
                selected_no = i;
            }
        }
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, orderType);//为订单类型设置adapter
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        sell_order_type.setAdapter(adapter);

        sell_order_type.setSelection(selected_no);//设置其默认选项
        sell_order_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sellOrderAdapter.SetOrderType(orderType_id.get(i));//当选择订单类型后，适配器去操作相应的事件，因为选择免费订单，会有一些明细变化
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        int selected_express_no = 0;//相应的邮寄方式的默认选项
        int selected_invexpress_no = 0;//相应的邮寄方式的默认选项
        for (int i = 0; i < express.size(); i++) {
            Dsaordtype dsaordtype = gson1.fromJson(express.get(i).getVar_value(), Dsaordtype.class);
            orderExpress.add(dsaordtype.getName_express());
            orderExpress_id.add(dsaordtype.getId_express());
            if (orderExpress_id.get(i).equalsIgnoreCase("106")) {//默认选择为顺丰快递
                selected_express_no = i;
                selected_invexpress_no = i;
            }
        }
        adapter_express = new ArrayAdapter<String>(this, R.layout.spinner_item, orderExpress);//分别设置正常adapter和灰色不可选adapter，当票货同行为是，发票邮寄类型为灰色不可选择
        adapter_grey = new ArrayAdapter<String>(this, R.layout.spinner_item_grey, orderExpress);
        adapter_express.setDropDownViewResource(R.layout.spinner_item_hint);
        adapter_grey.setDropDownViewResource(R.layout.spinner_item_hint);
        sell_order_express.setAdapter(adapter_express);
        sell_order_invexpress.setAdapter(adapter_express);
        sell_order_express.setSelection(selected_express_no);//邮寄方式的默认选项
        sell_order_invexpress.setSelection(selected_invexpress_no);
        for (int i = 0; i < tax.size(); i++) {//发票类型遍历
            Dsaordtype dsaordtype = gson1.fromJson(tax.get(i).getVar_value(), Dsaordtype.class);
            orderTicketType.add(dsaordtype.getName_invtype());
            orderTicketType_id.add(dsaordtype.getId_invtype());
        }

        orderSpace.add("是");//票货同行选项
        orderSpace.add("否");
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, orderSpace);
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        sell_order_space.setAdapter(adapter);
        //从1345获取的用户信息
        username = ej1345.getName_user();
        userID = ej1345.getId_user();
        companyID = ej1345.getId_com();
        id_dept = ej1345.getId_dept();
        sell_order_recorder.setText(username);//设置录入人为本人
        sell_order_date_record.setText(getToday());//录入时间为今天
        sell_order_ticket_time.setText(getToday());//订单日期为今天
        if (Constant.JUDGE_TYPE) {//如果不是列表获取数据，即新增数据
            Constant.billsNo = "";//订单号为空
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");
            if (TextUtils.isEmpty(a)) {
                a = f.format(calendar.getTime());//设置操作时间

            }
            b = f2.format(calendar.getTime());//设置订单日期
            no = 0;//设置第一行明细为0
            SellOrderModel sellOrderModel = new SellOrderModel();//建立新的明细数据
            sellOrderModel.setOrder_first(true);//设置为首行（不能删除）
            sellOrderModel.setOrder_no(no);//首行行号为0，即明细（1）
            sellDetails.add(sellOrderModel);//添加明细到集合
            no++;//明细号加1，添加新明细使用这个区分编号
        } else {
            //从列表获取的数据
            Constant.billsNo = performanceDatas.getMain().getDsaord_no();//订单号
            var_title_code.setText(Constant.billsNo);//设置订单号
            sell_order_address.setText(performanceDatas.getMain().getVar_addr());//邮寄地址
            sell_order_addess_send.setText(performanceDatas.getMain().getVar_invaddr());//发票地址
            id_zone = performanceDatas.getMain().getId_zone();//区域
            id_area = performanceDatas.getMain().getId_area();//省区
            id_corrtype = performanceDatas.getMain().getId_corrtype();//客户类型
            id_industry = performanceDatas.getMain().getId_industry();//行业
            var_rcvcorr = performanceDatas.getMain().getVar_rcvcorr();//结算客户
            id_terminal = performanceDatas.getMain().getId_terminal();//终端id
            name_terminal = performanceDatas.getMain().getName_terminal();//终端名称
            dec_acclimit = performanceDatas.getMain().getDec_acclimit();//账期
            var_tel = performanceDatas.getMain().getVar_tel();//电话
            var_inv_tel = performanceDatas.getMain().getVar_invtel();//发票联系电话
            var_inv_contact = performanceDatas.getMain().getVar_invcontact();//发票联系人
            var_contact = performanceDatas.getMain().getVar_contact();//联系人
            id_corr = performanceDatas.getMain().getId_corr();//客户id
            name_corr = performanceDatas.getMain().getName_corr();//客户名称
            sell_order_terminal.setText(name_terminal);//终端名称
            DecimalFormat format = new DecimalFormat(",##0.00");//保留两位小数，使用千分符
            sell_over_money.setText(format.format(Double.valueOf(performanceDatas.getMain().getDec_acaramt())));//超期金额
            sell_order_send.setText(name_corr);//客户名称
            id_terminal_for_item = id_corr;//客户名称保存
            Constant.id_terminal_for_address = id_terminal;//终端id保存
            a = performanceDatas.getMain().getDate_opr();//操作日期获取
            b = performanceDatas.getMain().getDate_sign();//订单日期
            for (int i = 0; i < orderType_id.size(); i++) {//订单类型遍历，看看列表使用的订单类型应该选择哪个，下同
                if (performanceDatas.getMain().getId_ordtype().equalsIgnoreCase(orderType_id.get(i))) {
                    sell_order_type.setSelection(i);
                }
            }
            for (int i = 0; i < orderTicketType_id.size(); i++) {//发票类型遍历，看看列表使用的发票类型应该选择哪个，下同
                if (performanceDatas.getMain().getId_invtype().equalsIgnoreCase(orderTicketType_id.get(i))) {
                    sell_order_ticket_type.setText(orderTicketType.get(i));
                    orderTicket_id = orderTicketType_id.get(i);
                }
            }
            for (int i = 0; i < orderExpress_id.size(); i++) {//邮寄类型遍历，看看列表使用的邮寄类型应该选择哪个，下同
                if (performanceDatas.getMain().getId_express().equalsIgnoreCase(orderExpress_id.get(i))) {
                    sell_order_express.setSelection(i);
                }
                if (performanceDatas.getMain().getId_invexpress().equalsIgnoreCase(orderExpress_id.get(i))) {
//                    new MyToast(this, orderExpress_id.get(i));
                    selected_invexpress_no = i;
                    sell_order_invexpress.setSelection(selected_invexpress_no);
                }
            }
            if (performanceDatas.getMain().getFlag_itemwith().equalsIgnoreCase("Y")) {//票货同行类型遍历，看看列表使用的票货同行类型应该选择哪个，下同
                sell_order_space.setSelection(0);
            } else {
                sell_order_space.setSelection(1);

            }
            sell_order_ticket_time.setText(TextUtils.isEmpty(performanceDatas.getMain().getDate_demand().trim()) ? getToday() : performanceDatas.getMain().getDate_demand().trim());//订单日期，如果列表为空，就用今天
            sell_order_more.setText(performanceDatas.getMain().getVar_remark());//备注
            no = 0;//订单明细从0开始
            countDetail = performanceDatas.getDetails().size();//设置返回来的订单明细个数，提交时使用
            for (int i = 0; i < performanceDatas.getDetails().size(); i++) {//列表返回订单明细遍历
                SellOrderModel sellOrderModel = new SellOrderModel();//每一个明细都建立一个类装入集合
                sellOrderModel.setOrder_first(i == 0);//是不是第一行明细，如果是就不能删除
                sellOrderModel.setOrder_no(no);//设置明细行号
                sellOrderModel.setName_item(performanceDatas.getDetails().get(i).getName_item());//产品名称
                sellOrderModel.setId_tax(performanceDatas.getDetails().get(i).getId_tax());//税种
                sellOrderModel.setDec_taxrate(performanceDatas.getDetails().get(i).getDec_taxrate());//税率
                sellOrderModel.setOrder_num(Double.valueOf(performanceDatas.getDetails().get(i).getDec_qty()));//数量
                sellOrderModel.setOrder_price(Double.valueOf(performanceDatas.getDetails().get(i).getDec_oriamt()));//金额
                sellOrderModel.setPer_price(Double.valueOf(performanceDatas.getDetails().get(i).getDec_price()));//单价
                sellOrderModel.setId_item(performanceDatas.getDetails().get(i).getId_item());//产品id
                sellOrderModel.setId_sell(performanceDatas.getDetails().get(i).getId_satype());//销售类型
                sellOrderModel.setId_stocktype(performanceDatas.getDetails().get(i).getId_stocktype());//库存大类
                sellOrderModel.setId_itemcate(performanceDatas.getDetails().get(i).getId_itemcate());//物料大类
                sellOrderModel.setId_stockstyle(performanceDatas.getDetails().get(i).getId_stockstyle());//二级分类
                sellOrderModel.setDec_oriprice(performanceDatas.getDetails().get(i).getDec_jxprice());//原价
                sellDetails.add(sellOrderModel);
                no++;
            }
        }
        final int finalSelected_invexpress_no = selected_invexpress_no;//设置选择的邮寄类型序号
        sell_order_space.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {//票货同行选择为否，发票地址，发票邮寄类型都可以选择，颜色为黑色
                    sell_order_addess_send.setClickable(true);
                    sell_order_invexpress.setEnabled(true);
                    sell_order_invexpress.setAdapter(adapter_express);
                    sell_order_addess_send.setTextColor(mContext.getResources().getColor(R.color.black));
                    sell_order_invexpress.setSelection(finalSelected_invexpress_no);
                } else {//票货同行选择为否，发票地址，发票邮寄类型都不可以选择，颜色为灰色
                    sell_order_addess_send.setClickable(false);
                    sell_order_invexpress.setAdapter(adapter_grey);
                    sell_order_addess_send.setText(sell_order_address.getText());
                    sell_order_addess_send.setTextColor(mContext.getResources().getColor(R.color.title_hint));
                    sell_order_invexpress.setSelection(sell_order_express.getSelectedItemPosition());
                    sell_order_invexpress.setEnabled(false);
//                    if (dsaordbaseJsons_new != null && dsaordbaseJsons_new.size() > 0) {
//                        sell_order_addess_send.setSelection(sell_order_address.getSelectedItemPosition());
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sellOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {//退出时，需要清理数据
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
                caculatePrice();//保存时需要计算金额
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
                //终端搜索
                Intent intent = new Intent(getApplicationContext(), BusinessSearch.class);
                project_type = 1;//1为终端搜索
                travel = true;//区别出差外出
                startActivityForResult(intent, 33);
                break;
            case R.id.sell_order_ticket_time:
                showCalendar(sell_order_ticket_time);//调用日历控件
                break;
            case R.id.add_sell_order_detail://添加明细
                Constant.JUDGE_TYPE1 = true;
                SellOrderModel sellOrderModel = new SellOrderModel();
                sellOrderModel.setOrder_first(false);
                sellOrderModel.setOrder_no(no);
                sellDetails.add(sellOrderModel);
                sellOrderAdapter.notifyDataSetChanged();
                no++;
                break;
            case R.id.sell_order_addess_send://选择发票邮寄地址
                Intent intent1 = new Intent(this, BusinessSearch.class);
                Constant.project_type = 3;//地址类的是3
                Constant.travel = true;
                startActivityForResult(intent1, 66);//66为发票邮寄地址
                break;
            case R.id.sell_order_address://选择发货地址
                name_terminal = sell_order_terminal.getText().toString().trim();
                if (TextUtils.isEmpty(name_terminal)) {
                    new MyToast2(SellOrder.this, "请先选择所属终端!");
                    return;
                }
                Intent intent2 = new Intent(this, BusinessSearch.class);
                Constant.project_type = 3;
                Constant.travel = true;
                startActivityForResult(intent2, 55);//55是发货地址
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
            case Constant.HANDLERTYPE_0://保存
                caculatePrice();
                saveAndSend("save");
                break;
            case Constant.HANDLERTYPE_1://送审
                saveAndSend("send");
                break;
        }
    }

    /**
     * 保存和送审方法
     *
     * @param save 区分保存还是送审
     */
    private void saveAndSend(String save) {
        name_terminal = sell_order_terminal.getText().toString().trim();
        if (TextUtils.isEmpty(name_terminal)) {
            showFailToast("请先选择所属终端!");
            return;
        }
        name_corr = sell_order_send.getText().toString().trim();
        if (TextUtils.isEmpty(name_corr)) {
            showFailToast("客户名不能为空!");
            return;
        }
        String time_p = sell_order_ticket_time.getText().toString().trim();
        if (TextUtils.isEmpty(time_p)) {
            showFailToast("请先选择发货日期!");
            return;
        }
        //预计开票时间，设置今天
        String time = getToday();
        String more = sell_order_more.getText().toString().trim();//备注
        String address = sell_order_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            showFailToast("请先选择送货地址!");
            return;
        }
        String var_invaddr = sell_order_addess_send.getText().toString().trim();
        if (TextUtils.isEmpty(var_invaddr)) {
            showFailToast("请先选择发票地址!");
            return;
        }

        double allprice = 0;//总价
        if (sellDetails.size() > 0)//订单明细有行号可以计算总价
            for (int i = 0; i < sellDetails.size(); i++) {
                allprice = allprice + sellDetails.get(i).getOrder_price();
            }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"dsaord\",\"opr\":\"SS\",\"no\":\"" + Constant.billsNo + "\",\"userid\":\"" + userID + "\",\"comid\":\"" + companyID + "\",");
        stringBuffer.append("\"menuid\":\"002020\",\"dealtype\":\"" + save + "\",\"data\":[");
        int gap = 0;//增加的明细，如果是正，就为新增，负数为删除个数
        int cycleTime = 0;//提交明细的次数
        if (Constant.JUDGE_TYPE) {
            gap = 0;
            cycleTime = sellDetails.size();//总单据行数

        } else {
            gap = sellDetails.size() - countDetail;//新增加的单据行数
            if (gap >= 0) {//有新增单据
                cycleTime = sellDetails.size();
            } else {//有删除单据
                cycleTime = countDetail;
            }
        }
        BusinessChange businessChange = new BusinessChange();//新接口提交,和旧接口写到了一起,这样更好的比较和赋值
        businessChange.setTable_name("dsaord_03");
        businessChange.setTable_no_name("dsaord_no");
        businessChange.setTable_no(Constant.billsNo);
        List<BusinessChange.ValuesBean> valuesBeen = new ArrayList<>();
        DecimalFormat format = new DecimalFormat("#0.00");//保留两位小数
        DecimalFormat format4 = new DecimalFormat("#0.0000");//保留四位小数

        for (int i = 0; i < cycleTime; i++) {
            BusinessChange.ValuesBean valuesBean = new BusinessChange.ValuesBean();//新接口的明细提交
            if (!Constant.JUDGE_TYPE) {
                valuesBean.setId_com(companyID);
                if (i >= countDetail) {//新增的明细
                    valuesBean.setDealtype("N");
                    valuesBean.setDate_opr(a);
                    stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"N\",\"where\":\" \",\"data\":[");
                    stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + a + "\",\"datatype\":\"datetime\"}, ");
                } else if (i >= countDetail + gap) {//删除的明细
                    valuesBean.setDealtype("D");
                    stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"D\",\"where\":\" \",\"data\":[");

                } else {//修改的明细
                    valuesBean.setDealtype("U");
                    stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"U\",\"where\":\" \",\"data\":[");

                }

//                continue;
            } else {//第一次新单据
                stringBuffer.append("{\"table\": \"dsaord_03\",\"oprdetail\":\"N\",\"where\":\" \",\"data\":[");
                stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + a + "\",\"datatype\":\"datetime\"}, ");

            }
            valuesBean.setFlag_sts("L");
            valuesBean.setFlag_prerec("N");
            valuesBean.setFlag_temp("N");
            valuesBean.setId_channel("10");
            valuesBean.setId_delivery("101");
            valuesBean.setDec_srate("1");
            valuesBean.setId_saprt("P01");
            valuesBean.setId_recorder(userID);
            valuesBean.setId_dept(id_dept);
            valuesBean.setDate_sign(b);
            valuesBean.setId_flow("FBsa");
            valuesBean.setDec_scorramt(String.valueOf(allprice));
            valuesBean.setId_zone(id_zone);
            valuesBean.setId_area(id_area);
            valuesBean.setId_corrtype(id_corrtype);
            valuesBean.setId_industry(id_industry);
            valuesBean.setLine_no((String.valueOf(i + 1)));
            valuesBean.setId_ordsource("001");
            valuesBean.setId_curr("CNY");
            valuesBean.setDec_exchangerate("1");
            valuesBean.setVar_contact(var_contact);
            valuesBean.setId_corr(id_corr);
            valuesBean.setName_corr(name_corr);
            valuesBean.setId_seller(userID);
            valuesBean.setDec_acaramt(sell_over_money.getText().toString().replaceAll(",", ""));
            valuesBean.setDec_acclimit(dec_acclimit.replaceAll(",", ""));
            valuesBean.setVar_tel(var_tel);
            valuesBean.setId_terminal(id_terminal);
            valuesBean.setName_terminal(name_terminal);
            valuesBean.setId_ordtype(orderType_id.get(sell_order_type.getSelectedItemPosition()));
            valuesBean.setId_invtype(orderTicket_id);
            valuesBean.setVar_remark(more);

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
            stringBuffer.append("{\"column\":\"id_invtype\",\"value\":\"" + orderTicket_id + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + more + "\",\"datatype\":\"varchar\"}, ");
            if (!Constant.JUDGE_TYPE && (i >= countDetail + gap)) {
                valuesBean.setId_satype(sellDetails.get(0).getId_sell());
                valuesBean.setId_item(sellDetails.get(0).getId_item());
                valuesBean.setName_item(sellDetails.get(0).getName_item());
                valuesBean.setId_uom(sellDetails.get(0).getId_uom());
                valuesBean.setDec_qty(String.valueOf(sellDetails.get(0).getOrder_num()));
                valuesBean.setDec_oriprice(String.valueOf(sellDetails.get(0).getPer_price()));
                valuesBean.setDec_oriamt(String.valueOf(sellDetails.get(0).getOrder_price()));
                valuesBean.setId_tax(sellDetails.get(0).getId_tax());
                valuesBean.setDec_taxrate(sellDetails.get(0).getDec_taxrate());
                valuesBean.setId_stockstyle(sellDetails.get(0).getId_stockstyle());
                valuesBean.setId_stocktype(sellDetails.get(0).getId_stocktype());
                valuesBean.setId_itemcate(sellDetails.get(0).getId_itemcate());
                valuesBean.setFlag_gift(sellDetails.get(0).getFlag_gift());
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
                    new MyToast2(SellOrder.this, "明细(" + (i + 1) + ")请选择产品!");
                    return;
                }
                if (sellDetails.get(i).getOrder_num() == 0) {
                    new MyToast2(SellOrder.this, "明细(" + (i + 1) + ")数量不能为0!");
                    return;
                }
                valuesBean.setId_satype(sellDetails.get(i).getId_sell());
                valuesBean.setId_item(sellDetails.get(i).getId_item());
                valuesBean.setName_item(sellDetails.get(i).getName_item());
                valuesBean.setId_uom(sellDetails.get(i).getId_uom());
                valuesBean.setDec_qty(String.valueOf(sellDetails.get(i).getOrder_num()));
                valuesBean.setDec_ordqty(String.valueOf(sellDetails.get(i).getOrder_num()));
                valuesBean.setDec_oriprice(String.valueOf(sellDetails.get(i).getPer_price()));
                valuesBean.setDec_price(String.valueOf(sellDetails.get(i).getPer_price()));
                valuesBean.setDec_oriamt(String.valueOf(sellDetails.get(i).getOrder_price()));
                valuesBean.setDec_amt(String.valueOf(sellDetails.get(i).getOrder_price()));
                valuesBean.setId_tax(sellDetails.get(i).getId_tax());
                valuesBean.setDec_taxrate(sellDetails.get(i).getDec_taxrate());
                double dec_noamt = sellDetails.get(i).getOrder_price() / (1 + Double.valueOf(sellDetails.get(i).getDec_taxrate()));
                valuesBean.setDec_noamt(format.format(dec_noamt));
                double dec_noprice = sellDetails.get(i).getPer_price() / (1 + Double.valueOf(sellDetails.get(i).getDec_taxrate()));
                valuesBean.setDec_noprice(format4.format(dec_noprice));
                double dec_taxamt = sellDetails.get(i).getOrder_price() - dec_noamt;
                valuesBean.setDec_taxamt(format.format(dec_taxamt));
                valuesBean.setId_stockstyle(sellDetails.get(i).getId_stockstyle());
                valuesBean.setId_stocktype(sellDetails.get(i).getId_stocktype());
                valuesBean.setId_itemcate(sellDetails.get(i).getId_itemcate());
                valuesBean.setFlag_gift(sellDetails.get(i).getFlag_gift());
                valuesBean.setDec_discrate("1.00");
                valuesBean.setDec_jxprice(sellDetails.get(i).getDec_oriprice());
                valuesBean.setDec_discprice(sellDetails.get(i).getDec_oriprice());
                valuesBean.setDec_rpprice(sellDetails.get(i).getDec_oriprice());
                if (sellDetails.get(i).getPer_price() == 0) {//如果最终价格为0，则返利金额提交该行总金额
                    valuesBean.setDec_rpamt(String.valueOf(Double.valueOf(sellDetails.get(i).getDec_oriprice()) * sellDetails.get(i).getOrder_num()));
                } else {
                    valuesBean.setDec_rpamt("0.00");
                }
                valuesBean.setDec_corrprice(String.valueOf(sellDetails.get(i).getPer_price()));
                valuesBean.setDec_corramt(String.valueOf(sellDetails.get(i).getOrder_price()));
                stringBuffer.append("{\"column\":\"id_tax\",\"value\":\"" + sellDetails.get(i).getId_tax() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_satype\",\"value\":\"" + sellDetails.get(i).getId_sell() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_item\",\"value\":\"" + sellDetails.get(i).getId_item() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"dec_discrate\",\"value\":\"1.00\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"dec_rpprice\",\"value\":\"" + sellDetails.get(i).getDec_oriprice() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"dec_jxprice\",\"value\":\"" + sellDetails.get(i).getDec_oriprice() + "\",\"datatype\":\"varchar\"}, ");
//                stringBuffer.append("{\"column\":\"var_chkparm\",\"value\":\"" + sellDetails.get(i).getVar_chkparm() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"name_item\",\"value\":\"" + sellDetails.get(i).getName_item() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_uom\",\"value\":\"" + sellDetails.get(i).getId_uom() + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"dec_qty\",\"value\":\"" + String.valueOf(sellDetails.get(i).getOrder_num()) + "\",\"datatype\":\"double\"}, ");
                stringBuffer.append("{\"column\":\"dec_oriprice\",\"value\":\"" + String.valueOf(sellDetails.get(i).getPer_price()) + "\",\"datatype\":\"double\"}, ");
                stringBuffer.append("{\"column\":\"dec_oriamt\",\"value\":\"" + String.valueOf(sellDetails.get(i).getOrder_price()) + "\",\"datatype\":\"double\"}, ");

            }
            //票货同行单拿出来
            if (sell_order_space.getSelectedItemPosition() == 0) {//选择是
                valuesBean.setFlag_itemwith("Y");
                valuesBean.setVar_invaddr(var_invaddr);
                valuesBean.setVar_invtel(var_tel);
                valuesBean.setVar_invcontact(var_contact);
                valuesBean.setId_invexpress(orderExpress_id.get(sell_order_invexpress.getSelectedItemPosition()));
                stringBuffer.append("{\"column\":\"flag_itemwith\",\"value\":\"Y\",\"datatype\":\"char\"}, ");
                stringBuffer.append("{\"column\":\"var_invaddr\",\"value\":\"" + var_invaddr + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invtel\",\"value\":\"" + var_tel + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invcontact\",\"value\":\"" + var_contact + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_invexpress\",\"value\":\"" + orderExpress_id.get(sell_order_invexpress.getSelectedItemPosition()) + "\",\"datatype\":\"varchar\"}, ");

            } else {//选择否
                valuesBean.setFlag_itemwith("N");
                valuesBean.setVar_invaddr(var_invaddr);
                valuesBean.setVar_invtel(var_inv_tel);
                valuesBean.setVar_invcontact(var_inv_contact);
                valuesBean.setId_invexpress(orderExpress_id.get(sell_order_invexpress.getSelectedItemPosition()));
                stringBuffer.append("{\"column\":\"flag_itemwith\",\"value\":\"N\",\"datatype\":\"char\"}, ");
                stringBuffer.append("{\"column\":\"var_invaddr\",\"value\":\"" + var_invaddr + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invtel\",\"value\":\"" + var_inv_tel + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"var_invcontact\",\"value\":\"" + var_inv_contact + "\",\"datatype\":\"varchar\"}, ");
                stringBuffer.append("{\"column\":\"id_invexpress\",\"value\":\"" + orderExpress_id.get(sell_order_invexpress.getSelectedItemPosition()) + "\",\"datatype\":\"varchar\"}, ");
            }
            valuesBean.setDec_orisamt(String.valueOf(allprice));
            valuesBean.setDec_samt(String.valueOf(allprice));
            valuesBean.setVar_rcvcorr(var_rcvcorr);
            valuesBean.setVar_addr(address);
            valuesBean.setId_express(orderExpress_id.get(sell_order_express.getSelectedItemPosition()));
            valuesBean.setDate_planinv(time);
            valuesBean.setDate_demand(time_p);
            stringBuffer.append("{\"column\":\"dec_orisamt\",\"value\":\"" + String.valueOf(allprice) + "\",\"datatype\":\"double\"}, ");
            stringBuffer.append("{\"column\":\"var_rcvcorr\",\"value\":\"" + var_rcvcorr + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"var_addr\",\"value\":\"" + address + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"id_express\",\"value\":\"" + orderExpress_id.get(sell_order_express.getSelectedItemPosition()) + "\",\"datatype\":\"varchar\"}, ");
            stringBuffer.append("{\"column\":\"date_planinv\",\"value\":\"" + time + "\",\"datatype\":\"datetime\"}, ");
            stringBuffer.append("{\"column\":\"date_demand\",\"value\":\"" + time_p + "\",\"datatype\":\"datetime\"}]}");
            if (i != cycleTime - 1) {
                stringBuffer.append(",");
            }
            valuesBeen.add(valuesBean);
        }
        businessChange.setValues(valuesBeen);
        stringBuffer.append("]}");
        String str = stringBuffer.toString();
        Log.e("str1", str);
        getBusinessList(str, save, new Gson().toJson(businessChange));
    }

    /**
     * 提交单据
     *
     * @param datas        旧方法的数据
     * @param dealtype     是保存还是送审
     * @param businessJson 新方法的送审数据
     */
    private void getBusinessList(String datas, final String dealtype, String businessJson) {
        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
            waitDialogRectangle.dismiss();
        }
        waitDialogRectangle = new WaitDialogRectangle(this);
        waitDialogRectangle.setCanceledOnTouchOutside(false);
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在提交");
        boolean isMultipart;//是否使用新接口
        String url_serv;
        String datas_add;
        if ((!Constant.JUDGE_TYPE) && dealtype.equals(Constant.SAVE_DEALTYPE)) {//从列表进来并且是保存,则用新方法
            isMultipart = false;
            url_serv = "/servlet/BusinessMobileSqlUpdate";
            datas_add = businessJson;
        } else {
            isMultipart = true;
            url_serv = "/servlet/DataUpdateServlet";
            datas_add = datas;
        }

        OkGo.post(EapApplication.URL_SERVER_HOST_HTTP + url_serv)
                .isMultipart(isMultipart)
                .params("datas", datas_add)
                .execute(new BFlagCallBack<businessFlag>() {
                    @Override
                    public void onSuccess(businessFlag businessFlag, Call call, Response response) {
                        String content = businessFlag.getMessage();
                        String flag = businessFlag.getFlag();
                        if (dealtype.equals(Constant.SAVE_DEALTYPE)) {
                            Constant.billsNo = businessFlag.getNo();
//                            ToastUtil.ShowShort(getApplicationContext(), content);
                            if (flag.equalsIgnoreCase("Y")) {
                                showFailToast("保存成功!");
                                setResult(33);
                            } else {
                                showFailToast("保存失败!");
                            }
                            var_title_code.setText(Constant.billsNo);
                            Constant.JUDGE_TYPE = false;
                            countDetail = sellDetails.size();
                        } else {
                            Constant.billsNo = businessFlag.getNo();
//                            ToastUtil.ShowShort(getApplicationContext(), content);
                            if (flag.equalsIgnoreCase("Y")) {
                                showFailToast("送审成功!");
                                setResult(22);
                                finish();
                            } else {
                                showFailToast("送审失败!");
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
                            showFailToast("网络错误!");
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
//                                ToastUtil.ShowShort(getApplicationContext(), "提交失败");
                                showFailToast("提交失败!");
                            } else {
//                                ToastUtil.ShowShort(getApplicationContext(), e.getMessage());
                                showFailToast(e.getMessage());
                            }


                        }
                        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                            waitDialogRectangle.dismiss();
                        }
                    }
                });
    }

    /**
     * 计算单行金额
     */
    private void caculatePrice() {
        add_sell_order_detail.requestFocusFromTouch();
        for (int i = 0; i < sellDetails.size(); i++) {
            sellDetails.get(i).setOrder_price(sellDetails.get(i).getPer_price()
                    * sellDetails.get(i).getOrder_num());
        }
        sellOrderAdapter.notifyDataSetChanged();
    }

    /**
     * 点击搜索后的返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                    getItem();//终端搜索
                    break;
                case 1:
                    sellOrderAdapter.notifyDataSetChanged();//产品搜索
                    break;
                case 2://地址搜索
                    sell_order_address.setText(item_peoject);
                    var_tel = id_wproj;
                    var_contact = item_client;
                    var_rcvcorr = Constant.id_corr;
                    if (sell_order_space.getSelectedItemPosition() == 0) {
                        sell_order_addess_send.setText(item_peoject);
                    }
                    var_inv_tel = id_wproj;
                    var_inv_contact = item_client;
                    break;
                case 3://发票地址搜索
                    sell_order_addess_send.setText(item_peoject);
                    var_inv_tel = id_wproj;
                    var_inv_contact = item_client;
                    break;

            }
        }
    };

    /**
     * 搜索完终端后进行数据处理
     */
    private void getItem() {
        sell_order_terminal.setText(dsaordbaseJsons_new.get(0).getName_terminal());
        sell_order_send.setText(dsaordbaseJsons_new.get(0).getName_corr());
        sell_order_more.setText(dsaordbaseJsons_new.get(0).getVar_rmark());
        DecimalFormat format = new DecimalFormat(",##0.00");
        sell_over_money.setText(format.format(Double.valueOf(dsaordbaseJsons_new.get(0).getDec_acaramt())));
        dec_acclimit = dsaordbaseJsons_new.get(0).getDec_acclimit();
        id_zone = dsaordbaseJsons_new.get(0).getId_zone();
        id_area = dsaordbaseJsons_new.get(0).getId_area();
        id_corrtype = dsaordbaseJsons_new.get(0).getId_corrtype();
        id_industry = dsaordbaseJsons_new.get(0).getId_industry();
        id_terminal = dsaordbaseJsons_new.get(0).getId_terminal();
        id_corr = dsaordbaseJsons_new.get(0).getId_corr();
        id_terminal_for_item = id_corr;
        Constant.id_terminal_for_address = id_terminal;
        for (int i = 0; i < orderTicketType_id.size(); i++) {
            if (dsaordbaseJsons_new.get(0).getId_invtype().equalsIgnoreCase(orderTicketType_id.get(i))) {
                sell_order_ticket_type.setText(orderTicketType.get(i));
                orderTicket_id = dsaordbaseJsons_new.get(0).getId_invtype();
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
            sellDetails.get(i).setFlag_gift("N");

        }
        sellOrderAdapter.notifyDataSetChanged();
    }


}



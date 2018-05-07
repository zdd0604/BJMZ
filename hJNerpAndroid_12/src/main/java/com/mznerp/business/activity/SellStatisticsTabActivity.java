package com.mznerp.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.mznerp.R;
import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.common.ActivityBaseHeader;
import com.mznerp.common.Constant;
import com.mznerp.model.SellStatisticsModule;
import com.mznerp.net.HttpClientBuilder;
import com.mznerp.net.HttpClientManager;
import com.mznerp.util.Log;
import com.mznerp.util.StringUtil;
import com.mznerp.util.myscom.StringUtils;
import com.mznerp.widget.CountUtils;
import com.mznerp.widget.WaitDialogRectangle;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author zhangdongdong
 *         季度统计表
 */
public class SellStatisticsTabActivity extends ActivityBaseHeader implements
        ActionBarWidgetActivity.NsyncDataConnector {
    private String tabName = "dsaordquery3";
    private ArrayList<ArrayList<String>> mTableDatas;
    private LockTableView2 mLockTableView;
    public static final Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");

    //本年度汇总
    List<Double> dec_amtList = new ArrayList<>();
    //上年度汇总
    List<Double> dec_lastamtList = new ArrayList<>();

    @BindView(R.id.statistics_ll)
    LinearLayout statistics_ll;

    //后台返回数据
    private List<SellStatisticsModule> gsonList = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.HANDLERTYPE_0:
                    showMyToast("");
                    break;
                case Constant.HANDLERTYPE_1:
                    setTabView();
                    waitDialogRectangle.cancel();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_statistics_tab);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        //网络获取回调方法
        ActionBarWidgetActivity.setNsyncDataConnector(this);

        setBaseCenterTv(Constant.ModuleName);

        try {
            getTabDatas();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LogShow("SellStatisticsTabActivity 获取数据异常");
        }
    }

    /**
     * 获取table的内容
     */
    private void getTabDatas() throws UnsupportedEncodingException {
        waitDialogRectangle.show();
        LogShow("开始获取数据");
        HttpClientBuilder.HttpClientParam param = HttpClientBuilder
                .createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
        param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                .addKeyValue("id_table", StringUtils.join(tabName))
                .addKeyValue("condition", " 1=1 ");

        HttpClientManager.addTask(responseHandler, param.getHttpPost());
    }


    /**
     * 后台返回数据
     *
     * @param value
     */
    @Override
    public void processJsonValue(String value) {
        value = value.trim();
        if (value.equalsIgnoreCase("[]") || TextUtils.isEmpty(value)) {
            waitDialog.dismiss();
            mHandler.sendEmptyMessage(0);
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++) {
                String temp = jsonArray.getString(i);
                Matcher m = pattern.matcher(temp);
                String subValue = temp.substring(temp.indexOf("{"), temp.indexOf("}") + 1);
                LogShow(subValue);
                SellStatisticsModule statisticsModule = mGson.fromJson(subValue, SellStatisticsModule.class);

                dec_amtList.add(Double.parseDouble(statisticsModule.getDec_amt()));
                dec_lastamtList.add(Double.parseDouble(statisticsModule.getDec_lastamt()));

                gsonList.add(statisticsModule);
            }


            mHandler.sendEmptyMessage(Constant.HANDLERTYPE_1);
        } catch (JSONException e) {
            e.printStackTrace();
            waitDialogRectangle.cancel();
            LogShow("SellStatisticsTabActivity 后台返回数据解析错误");
        }
    }

    /**
     * 设置数据
     */
    private void setTabView() {
        mTableDatas = new ArrayList<>();
        //表头
        ArrayList<String> mfristData = new ArrayList<>();
        mfristData.add("月份");
        mfristData.add("本年度汇总金额");
        mfristData.add("上年度汇总金额");
        mfristData.add("同期增长率");
        mTableDatas.add(mfristData);

        //全年汇总
        ArrayList<String> yearCount = new ArrayList<>();
        yearCount.add("汇总");
        yearCount.add(getList(dec_amtList));
        yearCount.add(getList(dec_lastamtList));
        yearCount.add(countScale(getList(dec_amtList), getList(dec_lastamtList)));

        LogShow("增长率："+countScale(getList(dec_amtList), getList(dec_lastamtList)));

        mTableDatas.add(yearCount);

        Collections.sort(gsonList);

        //添加表的明细
        for (int i = 0; i < gsonList.size(); i++) {
            ArrayList<String> mRowDatas = new ArrayList<>();
            mRowDatas.add(gsonList.get(i).getFiscal_period());
            mRowDatas.add(gsonList.get(i).getDec_amt());
            mRowDatas.add(gsonList.get(i).getDec_lastamt());
            if (StringUtil.isStrTrue(gsonList.get(i).getDec_amt()) &&
                    StringUtil.isStrTrue(gsonList.get(i).getDec_lastamt())) {
                if (Double.parseDouble(gsonList.get(i).getDec_amt()) > 0.00
                        && Double.parseDouble(gsonList.get(i).getDec_lastamt()) > 0.00)
                    mRowDatas.add(countScale(gsonList.get(i).getDec_amt(),
                            gsonList.get(i).getDec_lastamt()));
            }
            mTableDatas.add(mRowDatas);
        }

        int max_weight = 120;
        int max_height = 200;
        mLockTableView = new LockTableView2(this, statistics_ll, mTableDatas);
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

    }


    /**
     * @param dec_amt     本年度
     * @param dec_lastamt 上年度
     *                    增长率计算公式
     *                    （本年度-上年度）/上年度=同期增长率*100%
     *                    <p>
     *                    计算后的double转换百分比计算方法
     *                    <p>
     *                    "百分比："+CountUtils.countNumberPercentage(0.12345,2);
     * @return
     */
    private String countScale(String dec_amt, String dec_lastamt) {
        double d1 = Double.parseDouble(dec_amt);
        double d2 = Double.parseDouble(dec_lastamt);
        double number = (d1 - d2) / d1;
        return CountUtils.countNumberPercentage(number, 2);
    }

    private String getList(List<Double> numberList) {
        double nmb = 0.0;
        for (int i = 0; i < numberList.size(); i++) {
            nmb += numberList.get(i);
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return  nf.format(nmb);
    }
}

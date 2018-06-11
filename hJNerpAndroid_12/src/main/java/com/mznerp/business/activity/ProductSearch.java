package com.mznerp.business.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mznerp.R;
import com.mznerp.business.BusinessQueryDao.BusinessQueryDao;
import com.mznerp.business.adapter.PdSearchAdapter;
import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.common.ActivityBaseHeader;
import com.mznerp.common.Constant;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.EjMyWProj1345;
import com.mznerp.model.ProductDetail;
import com.mznerp.util.StringUtil;
import com.mznerp.widget.ClearEditText;
import com.mznerp.widget.WaitDialogRectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mznerp.common.Constant.id_corr;
import static com.mznerp.common.Constant.user_myid;


/**
 * 产品搜索
 * 查询本地数据
 */
public class ProductSearch extends ActivityBaseHeader{
    @BindView(R.id.pdSh_Ed)
    ClearEditText pdSh_Ed;
    @BindView(R.id.pdSh_Rl)
    RecyclerView pdSh_Rl;
    @BindView(R.id.pdSh_error)
    LinearLayout pdSh_error;
    @BindView(R.id.pdSh_corr)
    TextView pdSh_corr;

    //适配器
    PdSearchAdapter pdSearchAdapter;
    private GetCoorTask getCoorTask;

    //客户名称
    private Map<String, String> repetition = new HashMap<>();
    //去除重复后的数据
    private List<ProductDetail> repList = new ArrayList<>();

    //客户列表数据
    private List<ProductDetail> pDetails = null;

    //客户列表数据
    private List<ProductDetail> listDetails = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            switch (msg.what) {
                case Constant.HANDLERTYPE_0:
                    //无数据
                    pdSh_Rl.setVisibility(View.GONE);
                    pdSh_error.setVerticalGravity(View.VISIBLE);
                    pdSh_corr.setText(mContext.getString(R.string.pdSearch_ErrorTitle1));
                    break;
                case Constant.HANDLERTYPE_1:
                    //设置数据
                    getWProject(content);
                    break;
                case Constant.HANDLERTYPE_2:
                    //搜索数据

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_prodctsearch);
        initView();
    }

    public void initView() {
        ButterKnife.bind(this);
        setBaseCenterTv(getString(R.string.tlbar_Title_productSearch));

        pdSh_Rl.setLayoutManager(new LinearLayoutManager(this));

        getCoorTask = new GetCoorTask();
        getCoorTask.execute((Void) null);

        pdSh_Ed.addTextChangedListener(textWatcher);
    }


    /**
     * 搜索输入框监听事件
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setHandlerMsg(mHandler, Constant.HANDLERTYPE_1, getEdVaule(pdSh_Ed));
        }
    };


    /**
     * 设置数据
     *
     * @param content
     */
    public void getWProject(final String content) {

        listDetails = getpDetails(content);

        pdSearchAdapter = new PdSearchAdapter(mContext, listDetails);
        pdSearchAdapter.notifyDataSetChanged();
        pdSh_Rl.setAdapter(pdSearchAdapter);

        pdSearchAdapter.setmOnItemClickLitener(new PdSearchAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(String id_corr, String name_corr, int position) {

                Constant.id_searCorr = id_corr;
                Constant.name_searCorr = name_corr;

                setResult(22);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


    /**
     * 根据用户的模糊搜索进行获取数据
     *
     * @param content
     * @return
     */
    private List<ProductDetail> getpDetails(String content) {

        //如果没有查询调节直接返回全部
        if (!StringUtil.isStrTrue(Constant.buss_key)&&!StringUtil.isStrTrue(content)){
            return repList;
        }

        LogShow("getProject:" + content);
        //根据用户筛选的数据
        List<ProductDetail> listUserDetail = new ArrayList<>();

        //根据用户以及模糊字段搜索后的数据筛选完成后的数据
        List<ProductDetail> listDetail = new ArrayList<>();

        //业务员查询条件
        if (StringUtil.isStrTrue(Constant.buss_key)) {
            for (int i = 0; i < repList.size(); i++) {
                if (repList.get(i).getId_seller().contains(Constant.buss_key)) {
                    listUserDetail.add(repList.get(i));
                }
            }

        }

        //客户搜索框输入的条件
        if (StringUtil.isStrTrue(content)) {
            if (listUserDetail.size()>0){
                for (int i = 0; i < listUserDetail.size(); i++) {
                    if (listUserDetail.get(i).getId_corr().contains(content)
                            || listUserDetail.get(i).getName_corr().contains(content)) {
                        listDetail.add(listUserDetail.get(i));
                    }
                }
                return listDetail;
            }else{
                for (int i = 0; i < repList.size(); i++) {
                    if (repList.get(i).getId_corr().contains(content)
                            || repList.get(i).getName_corr().contains(content)) {
                        listDetail.add(repList.get(i));
                    }
                }
                return listDetail;
            }
        }

        return listUserDetail;
    }

    /**
     * 获取数据
     */
    public class GetCoorTask extends AsyncTask<Void, Void, List<ProductDetail>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitDialogRectangle.show();
            waitDialogRectangle.setText("正在查询数据...");
        }

        @Override
        protected List<ProductDetail> doInBackground(Void... voids) {

            pDetails = BusinessQueryDao.get1345Corr("corr");
            if (pDetails.size() > 0)
                for (int i = 0; i < pDetails.size(); i++) {
                    if (repetition.size() > 0) {
                        if (!repetition.get(pDetails.get(i).getName_corr())
                                .equals(pDetails.get(i).getName_corr())) {
                            repList.add(pDetails.get(i));
                        }
                    } else {
                        repList.add(pDetails.get(i));
                    }
                }
            return repList;
        }

        @Override
        protected void onPostExecute(List<ProductDetail> productDetails) {
            super.onPostExecute(productDetails);
            waitDialogRectangle.dismiss();
            //没有数据
            if (productDetails.size() == 0) {
                mHandler.sendEmptyMessage(Constant.HANDLERTYPE_0);
                return;
            }
            mHandler.sendEmptyMessage(Constant.HANDLERTYPE_1);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            waitDialogRectangle.dismiss();
        }
    }

}
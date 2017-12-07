package com.mznerp.business.BusinessAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mznerp.adapter.WZYBaseAdapter;
import com.mznerp.business.activity.BusinessSearch;
import com.mznerp.model.SellOrderModel;
import com.mznerp.common.Constant;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.Ctlm1345;
import com.mznerp.model.Dsaordtype;
import com.mznerp.widget.MySpinner;
import com.mznerp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mznerp.common.Constant.sellDetailsPosition;

/**
 * Created by zy on 2017/6/12.
 */

public class SellOrderAdapter extends WZYBaseAdapter<SellOrderModel> {
    private List<Ctlm1345> sellType = new ArrayList<>();//销售类型
    private List<String> sellTypes_id = new ArrayList<String>();//销售类型的id
    private List<String> sellTypesZ = new ArrayList<String>();//
    private List<String> sellTypesP = new ArrayList<String>();
    private List<String> sellTypes_idZ = new ArrayList<String>();
    private List<String> sellTypes_idP = new ArrayList<String>();
    private List<String> productTypes_id = new ArrayList<String>();
    private List<String> id_uom = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private List<SellOrderModel> data;
    private Context context;
    private Activity activity;
    private int type_no;
    private boolean typeZ = false;


    public void SetOrderType(String type) {
        if (type.equalsIgnoreCase("P")) {
            typeZ = false;
        } else if (type.equalsIgnoreCase("Z")) {
            typeZ = true;
        }
        notifyDataSetChanged();
    }

    public SellOrderAdapter(List<SellOrderModel> data, Context context, int layoutRes, Activity activity) {
        super(data, context, layoutRes);
        this.data = data;
        this.context = context;
        this.activity = activity;
        sellType = BusinessBaseDao.getCTLM1345ByIdTable("satype");
        Gson gson1 = new Gson();
        for (int i = 0; i < sellType.size(); i++) {
            Dsaordtype dsaordtype = gson1.fromJson(sellType.get(i).getVar_value(), Dsaordtype.class);
            sellTypesP.add(dsaordtype.getName_type());
            sellTypes_idP.add(dsaordtype.getId_satype());
            if (sellTypes_idP.get(i).equalsIgnoreCase("20")) {
//                type_z = i;
                sellTypesZ.add(dsaordtype.getName_type());
                sellTypes_idZ.add(dsaordtype.getId_satype());
            }
            if (sellTypes_idP.get(i).equalsIgnoreCase("10")) {
                type_no = i;
            }

        }
        adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, sellTypesP);
        adapter.setDropDownViewResource(R.layout.spinner_item_hint);
        adapter2 = new ArrayAdapter<String>(context, R.layout.spinner_item, sellTypesZ);
        adapter2.setDropDownViewResource(R.layout.spinner_item_hint);
    }

    @Override
    public void bindData(ViewHolder holder, final SellOrderModel sellOrderModel, final int position) {
        Log.e("show",sellOrderModel.toString());
        MySpinner sell_order_type = (MySpinner) holder.getView(R.id.sell_order_type);
        TextView sell_order_products = (TextView) holder.getView(R.id.sell_order_products);
        sell_order_products.setText(sellOrderModel.getName_item());
        sell_order_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BusinessSearch.class);
                sellDetailsPosition = sellOrderModel.getOrder_no();
                Constant.project_type = 4;
                Constant.travel = true;
                activity.startActivityForResult(intent, 44);
            }
        });
        final EditText sell_order_num = (EditText) holder.getView(R.id.sell_order_num);
        sell_order_num.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        final TextView sell_order_type_sell = (TextView) holder.getView(R.id.sell_order_type_sell);
        sell_order_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!sell_order_num.getText().toString().isEmpty()) {
                        DecimalFormat format = new DecimalFormat("#0.00");
                        sellOrderModel.setOrder_num(Double.valueOf(format.format(Double.valueOf(sell_order_num.getText().toString()))));

                    } else {
                        sellOrderModel.setOrder_num(0);

                    }
                }
            }
        });

        final MySpinner finalSell_order_type = sell_order_type;
        final TextView finalSell_order_products = sell_order_products;

        Log.i("type_no", String.valueOf(type_no));
        if (typeZ) {
            sellTypes_id.clear();
            sellTypes_id.addAll(sellTypes_idZ);
            finalSell_order_type.setAdapter(adapter2);
            finalSell_order_type.setSelection(0);
            finalSell_order_type.setEnabled(false);
        } else {
            sellTypes_id.clear();
            sellTypes_id.addAll(sellTypes_idP);
            finalSell_order_type.setAdapter(adapter);
            finalSell_order_type.setSelection(type_no);
            finalSell_order_type.setEnabled(true);
        }
//        sell_order_products.setAdapter(adapter2);

        sell_order_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(sellOrderModel.getId_sell())) {
//                    if ((sellTypes_id.get(position).equalsIgnoreCase("10") || sellTypes_id.get(position).equalsIgnoreCase("60")) && (!sellOrderModel.getId_sell().equalsIgnoreCase("10")) && (!sellOrderModel.getId_sell().equalsIgnoreCase("60"))) {
//                        setEmpty();
//                    } else if ((!(sellTypes_id.get(position).equalsIgnoreCase("10") || sellTypes_id.get(position).equalsIgnoreCase("60"))) && ((sellOrderModel.getId_sell().equalsIgnoreCase("10")) || (sellOrderModel.getId_sell().equalsIgnoreCase("60")))) {
//                        setEmpty();
//                    }
                    if (!sellTypes_id.get(position).equalsIgnoreCase(sellOrderModel.getId_sell())){
                        setEmpty();
                    }

                }
                sellOrderModel.setOrder_type(position);
                sellOrderModel.setId_sell(sellTypes_id.get(position));
                if (!(sellTypes_id.get(position).equalsIgnoreCase("10") || sellTypes_id.get(position).equalsIgnoreCase("60"))) {
                    sellOrderModel.setPer_price(0.00);
                    sellOrderModel.setFlag_gift("Y");
                }else {
                    sellOrderModel.setFlag_gift("N");
                }
            }

            private void setEmpty() {
                sellOrderModel.setId_item("");
                sellOrderModel.setName_item("");
                sellOrderModel.setOrder_num(0);
                sellOrderModel.setPer_price(0);
                sellOrderModel.setOrder_price(0);
                sellOrderModel.setFlag_gift("N");
                notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sellOrderModel.setOrder_type(0);
                sellOrderModel.setId_sell(sellTypes_id.get(0));
            }
        });


        TextView order_num = (TextView) holder.getView(R.id.order_num);
        order_num.setText("订单明细(" + String.valueOf(sellOrderModel.getOrder_no() + 1) + ")");
        TextView order_delete = (TextView) holder.getView(R.id.order_delete);
        order_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(sellOrderModel);
                notifyDataSetChanged();
            }
        });
        if (sellOrderModel.isOrder_first()) {
            order_delete.setVisibility(View.GONE);
        } else {
            order_delete.setVisibility(View.VISIBLE);

        }
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//            String content = (String) msg.obj;
                switch (msg.what) {
                    case 0:
                        finalSell_order_type.setSelection(sellOrderModel.getOrder_type());
                        finalSell_order_products.setText(sellOrderModel.getName_item());
                        sell_order_num.setText(String.valueOf(sellOrderModel.getOrder_num() == 0 ? "" : sellOrderModel.getOrder_num()));
                        DecimalFormat format = new DecimalFormat(",##0.00");
                        sell_order_type_sell.setText(format.format(sellOrderModel.getOrder_price()));
                        break;

                }
            }
        };
        if (!Constant.JUDGE_TYPE && !Constant.JUDGE_TYPE1) {//已录入的数据
            for (int i = 0; i < sellTypes_id.size(); i++) {
                if (sellOrderModel.getId_sell().equalsIgnoreCase(sellTypes_id.get(i))) {
                    sellOrderModel.setOrder_type(i);
                }
            }
            for (int i = 0; i < productTypes_id.size(); i++) {
                if (sellOrderModel.getId_item().equalsIgnoreCase(productTypes_id.get(i))) {
                    sellOrderModel.setOrder_product(i);
                }
            }
        }
        mHandler.sendEmptyMessage(0);
    }
}

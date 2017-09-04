package com.hjnerp.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.AbnormalDetailModel;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.util.ToastUtil;
import com.hjnerpandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbnormalDetail extends ActivitySupport implements View.OnClickListener {

    private CheckBox checkbox_all;
    private ListView abnormal_list_view;
    private Button submit_over;
    private MyListAdapter adapter;
    private List<AbnormalDetailModel> data;
    private List<AbnormalDetailModel> data2;
    private List<Ctlm1345> abnormals;
    private List<Integer> listItemID = new ArrayList<Integer>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abnormal_detail);
        initData();
        initView();
    }

    private void initData() {
        data = new ArrayList<>();
        abnormals = new ArrayList<>();
        abnormals = BusinessBaseDao.getCTLM1345ByIdTable("dgtdabn");
        if (abnormals.size() == 0) {
            ToastUtil.ShowShort(this, "没有考勤异常记录");
            finish();
            return;
        }
        for (int i = 0; i < abnormals.size(); i++) {
            String abstrings = abnormals.get(i).getVar_value();
            Gson gson1 = new Gson();
            AbnormalDetailModel abnormalDetailModel = gson1.fromJson(abstrings, AbnormalDetailModel.class);
            data.add(abnormalDetailModel);
        }
        Collections.sort(data);

    }

    private void initView() {
        getSupportActionBar().show();
        getSupportActionBar().setTitle("考勤异常明细");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkbox_all = (CheckBox) findViewById(R.id.checkbox_all);
        abnormal_list_view = (ListView) findViewById(R.id.abnormal_list_view);
        submit_over = (Button) findViewById(R.id.submit_over);
        submit_over.setOnClickListener(this);
        adapter = new MyListAdapter(data);
        abnormal_list_view.setAdapter(adapter);
        mContext = getApplicationContext();
        checkbox_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb= (CheckBox) v;
                if (cb.isChecked()){
                    for (int i=0;i<adapter.mChecked2.size();i++){
                        adapter.mChecked2.set(i,true);
                    }
                }else {
                    for (int i=0;i<adapter.mChecked2.size();i++){
                        adapter.mChecked2.set(i,false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_over:
                submit();
                break;
        }
    }

    private void submit() {
        listItemID.clear();
        data2 = new ArrayList<>();

        for (int i=0;i<adapter.mChecked2.size();i++){
            if (adapter.mChecked2.get(i)){
                listItemID.add(i);

            }
        }
        if (listItemID.size()==0){
            ToastUtil.ShowShort(this,"没有选择任何记录");
        }else {
            for (int i=0;i<listItemID.size();i++){
                data2.add(data.get(listItemID.get(i)));
            }
        }

        if (data2.size() > 0) {
            Intent intent = new Intent(this, AbnormalBusiness.class);
            ArrayList<AbnormalDetailModel> mlist=new ArrayList<>();
            mlist.addAll(data2);
            intent.putParcelableArrayListExtra("data", mlist);
            this.startActivity(intent);
            this.finish();
        }
    }
    class MyListAdapter extends BaseAdapter {
        List<Boolean> mChecked2;
        List<AbnormalDetailModel> listAbnormal;

        MyListAdapter(List<AbnormalDetailModel> data) {
            listAbnormal=new ArrayList<AbnormalDetailModel>();
            listAbnormal=data;
            mChecked2=new ArrayList<Boolean>();
            for (int i = 0; i < data.size(); i++) {
                mChecked2.add(false);
            }
        }

        @Override
        public int getCount() {
            return listAbnormal.size();
        }

        @Override
        public AbnormalDetailModel getItem(int position) {
            return listAbnormal.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                Log.e("AbnormalDetailAdapter","position1 = "+position);
                LayoutInflater mInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.abnormal_detail_item, null);
                holder.date = (TextView) convertView.findViewById(R.id.date_abnormal_item);
                holder.type = (TextView) convertView.findViewById(R.id.type_abnormal_item);
                holder.onoff = (TextView) convertView.findViewById(R.id.startoff_abnormal_item);
                holder.time = (TextView) convertView.findViewById(R.id.time_abnormal_item);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.abnormal_detail_checkbox);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.abnormal_detail_layout);
                convertView.setTag(holder);
            } else {
                Log.e("AbnormalDetailAdapter","position2 = "+position);
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkBox.setChecked(mChecked2.get(position));

            holder.date.setText("日期：" + listAbnormal.get(position).getVar_date());

            switch (listAbnormal.get(position).getId_schtype()) {
                case "A":
                    holder.type.setText("类型：管理班");
                    break;
                case "B":
                    holder.type.setText("类型：正常班");
                    break;
                case "C":
                    holder.type.setText("类型：休息");
                    break;
                case "D":
                    holder.type.setText("类型：加班");
                    break;
                case "E":
                    holder.type.setText("类型：特殊班（保洁）");
                    break;
                case "F":
                    break;
                case "G":
                    holder.type.setText("类型：特殊班（哺乳期）");
                    break;
                default:
                    holder.type.setText("类型：特殊班");
                    break;
            }
            holder.onoff.setText("上/下班：" + listAbnormal.get(position).getVar_on());
            holder.time.setText("时间：" + listAbnormal.get(position).getVar_off());
            final int p=position;
            final ViewHolder finalHolder = holder;
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CheckBox checkBox= (CheckBox) v;
                    if (mChecked2.get(p)){
                        mChecked2.set(p, false);
                    }else {
                        mChecked2.set(p, true);
                    }
                    finalHolder.checkBox.setChecked(mChecked2.get(p));
                }
            });
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CheckBox checkBox= (CheckBox) v;
                    if (mChecked2.get(p)){
                        mChecked2.set(p, false);
                    }else {
                        mChecked2.set(p, true);
                    }
                    finalHolder.checkBox.setChecked(mChecked2.get(p));
                }
            });
            holder.checkBox.setChecked(mChecked2.get(p));
            return convertView;
        }

        class ViewHolder {

            TextView date;
            TextView type;
            TextView onoff;
            TextView time;
            CheckBox checkBox;
            LinearLayout layout;
        }
    }
}

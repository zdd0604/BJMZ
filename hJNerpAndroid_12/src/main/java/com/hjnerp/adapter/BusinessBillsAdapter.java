package com.hjnerp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hjnerp.model.PerformanceDatas;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/12/19.
 */

public class BusinessBillsAdapter extends BaseAdapter {
    private Context context;
    private List<PerformanceDatas> datas;
    List<Integer> list = new ArrayList<>();

    public BusinessBillsAdapter(Context context, List<PerformanceDatas> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refreshList(List<PerformanceDatas> list) {
        this.datas = list;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.businessbills_item, parent, false);
            viewHolder.line_id = (TextView) convertView.findViewById(R.id.line_id);
            viewHolder.bs_name_user = (TextView) convertView.findViewById(R.id.bs_name_user);
            viewHolder.bs_date_opr = (TextView) convertView.findViewById(R.id.bs_date_opr);
            viewHolder.rejust_type = (TextView) convertView.findViewById(R.id.rejust_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PerformanceDatas.MainBean info = datas.get(position).getMain();
        if (StringUtil.isStrTrue(info.getDgtdot_no())){
            viewHolder.bs_name_user.setText(info.getName_clerk());
        }else {
            viewHolder.bs_name_user.setText(info.getName_user());

        }

        String time = info.getDate_opr().substring(0, 10);
        viewHolder.bs_date_opr.setText(time);
        viewHolder.line_id.setText("" + (position + 1));
        if (StringUtil.isStrTrue(info.getVar_rejust())) {
            list.add(position);

        }else if (datas.get(position).getDetails()!=null){
            if(StringUtil.isStrTrue(datas.get(position).getDetails().get(0).getVar_rejust())){
                list.add(position);
            }
        }

        //在结尾的时候重记录的列表中对页面效果进行处理
        if (list.indexOf(position) > -1) {
            viewHolder.rejust_type.setVisibility(View.VISIBLE);
            Log.d("listposition",list.indexOf(position)+"");
        }else{
            Log.d("listposition",list.indexOf(position)+"");
            viewHolder.rejust_type.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView line_id;
        TextView bs_name_user;
        TextView bs_date_opr;
        TextView rejust_type;
    }
}

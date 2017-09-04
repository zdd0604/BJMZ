package com.hjnerp.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hjnerp.model.HJAboutBean;
import com.hjnerpandroid.R;

import java.util.List;

/**
 * Created by Admin on 2017/2/20.
 */

public class HjABoutAdapter extends BaseAdapter{
    private Context mContext;
    private List<HJAboutBean> list;

    public HjABoutAdapter(Context mContext, List<HJAboutBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hj_about_item,parent,false);
            viewHolder.title_one = (TextView) convertView.findViewById(R.id.title_one);
            viewHolder.title_two = (TextView) convertView.findViewById(R.id.title_two);
            viewHolder.content_hj = (TextView) convertView.findViewById(R.id.content_hj);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title_one.setText(list.get(position).getTitle_one());
        viewHolder.title_two.setText(list.get(position).getTitle_two());
        viewHolder.content_hj.setText(list.get(position).getContent_hj());
        return convertView;
    }

    class ViewHolder{
        private TextView title_one;
        private TextView title_two;
        private TextView content_hj;
    }
}

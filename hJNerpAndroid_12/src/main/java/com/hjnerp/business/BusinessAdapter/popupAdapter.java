package com.hjnerp.business.BusinessAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hjnerpandroid.R;

import java.util.List;

/**
 * Created by Admin on 2017/2/7.
 */

public class popupAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;

    public popupAdapter(Context context, List<String> datas) {
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dgtdrec_item, parent, false);
            viewHolder.dgtdrec_item_content = (TextView) convertView.findViewById(R.id.dgtdrec_item_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.dgtdrec_item_content.setText(datas.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView dgtdrec_item_content;
    }
}

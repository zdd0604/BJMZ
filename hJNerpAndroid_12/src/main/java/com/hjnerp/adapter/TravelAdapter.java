package com.hjnerp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hjnerp.model.Ctlm7502Json;
import com.hjnerpandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/12/1.
 */

public class TravelAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private List<Ctlm7502Json> mList;
    private Context context;
    private ArrayList<Ctlm7502Json> mUnfilteredData;

    public TravelAdapter(List<Ctlm7502Json> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Ctlm7502Json getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(context, R.layout.travel_item, null);
            holder = new ViewHolder();
            holder.name_project = (TextView) view.findViewById(R.id.name_project);
            holder.name_client = (TextView) view.findViewById(R.id.name_client);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Ctlm7502Json pc = mList.get(position);
        holder.name_project.setText("项目：" + pc.getName_proj());
        holder.name_client.setText("客户：" + pc.getName_corr());
        return view;
    }

    static class ViewHolder {
        public TextView name_project;
        public TextView name_client;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();

        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<Ctlm7502Json>(mList);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<Ctlm7502Json> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<Ctlm7502Json> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<Ctlm7502Json> newValues = new ArrayList<Ctlm7502Json>(count);

                for (int i = 0; i < count; i++) {
                    Ctlm7502Json pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if (pc.getName_proj() != null && pc.getName_proj().contains(prefixString)) {

                            newValues.add(pc);
                        } else if (pc.getName_corr() != null && pc.getName_corr().contains(prefixString)) {

                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            mList = (List<Ctlm7502Json>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}

package com.hjnerp.business.BusinessAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hjnerp.model.BusinessOneLine;
import com.hjnerpandroid.R;

import java.util.List;


/**
 * Created by Admin on 2017/1/17.
 * 适用于单行的适配器,具体方法同多行的
 */

public class BusinessSearchAdapterOne extends RecyclerView.Adapter<BusinessSearchAdapterOne.MyViewHolder> {
    private Context mContext;
    private List<BusinessOneLine> businessOneLines;

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public BusinessSearchAdapterOne(Context mContext, List<BusinessOneLine> businessOneLines) {
        this.mContext = mContext;
        this.businessOneLines = businessOneLines;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.bus_search_one_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.id_name_bus_item.setText(businessOneLines.get(position).getValue());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(businessOneLines.get(position).getKey(), businessOneLines.get(position).getValue());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return businessOneLines.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_name_bus_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            id_name_bus_item = (TextView) itemView.findViewById(R.id.id_name_bus_item);

        }
    }

    public interface OnItemClickLitener {
        void onItemClick(String item_key, String item_value);

        void onItemLongClick(View view, int position);
    }

}

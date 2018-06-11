package com.mznerp.business.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mznerp.R;
import com.mznerp.model.ProductDetail;

import java.util.List;


/**
 * 客户搜索界面
 */
public class PdSearchAdapter extends RecyclerView.Adapter<PdSearchAdapter.MyViewHolder> {
    private Context mContext;
    private List<ProductDetail> details;
    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public PdSearchAdapter(Context mContext, List<ProductDetail> details) {
        this.mContext = mContext;
        this.details = details;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.bus_search_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.pro_text.setText(mContext.getString(R.string.sellOrder_Tile_Client));
        holder.text_address.setText(mContext.getString(R.string.sellOrder_Tile_Address));
        holder.item_client.setTextColor(Color.parseColor("#888888"));
        holder.pro_text.setTextColor(Color.parseColor("#000000"));

        holder.item_peoject.setText(details.get(position).getName_corr());
        holder.item_client.setText(details.get(position).getVar_addr());

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(details.get(position).getId_corr(),
                            details.get(position).getName_corr(),
                            position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearlayout_overclient;
        LinearLayout linearlayout_besearch;
        LinearLayout linear_layout_id;
        LinearLayout linear_layout_chkparm;
        //电话
        LinearLayout linear_layout_varTel;
        //代码
        TextView text_id_item;
        TextView item_id_item;
        //客户
        TextView pro_text;
        TextView item_peoject;
        //地址
        TextView text_address;
        TextView item_client;
        //性能
        TextView text_chkparm;
        TextView item_chkparm;
        //结算
        TextView text_overclient;
        TextView item_overclient;
        //电话
        TextView item_varTel;
        TextView text_varTel;

        public MyViewHolder(View itemView) {
            super(itemView);
            //结算
            linearlayout_overclient = (LinearLayout) itemView.findViewById(R.id.linearlayout_overclient);
            //地址
            linearlayout_besearch = (LinearLayout) itemView.findViewById(R.id.linearlayout_besearch);
            //代码
            linear_layout_id = (LinearLayout) itemView.findViewById(R.id.linear_layout_id);
            //性能
            linear_layout_chkparm = (LinearLayout) itemView.findViewById(R.id.linear_layout_chkparm);
            //电话
            linear_layout_varTel = (LinearLayout) itemView.findViewById(R.id.linear_layout_varTel);

            //代码
            text_id_item = (TextView) itemView.findViewById(R.id.text_id_item);
            item_id_item = (TextView) itemView.findViewById(R.id.item_id_item);
            //客户
            pro_text = (TextView) itemView.findViewById(R.id.pro_text);
            item_peoject = (TextView) itemView.findViewById(R.id.item_peoject);
            //地址
            text_address = (TextView) itemView.findViewById(R.id.text_address);
            item_client = (TextView) itemView.findViewById(R.id.item_client);
            //性能
            text_chkparm = (TextView) itemView.findViewById(R.id.text_chkparm);
            item_chkparm = (TextView) itemView.findViewById(R.id.item_chkparm);
            //结算
            item_overclient = (TextView) itemView.findViewById(R.id.item_overclient);
            text_overclient = (TextView) itemView.findViewById(R.id.text_overclient);
            //电话
            text_varTel = (TextView) itemView.findViewById(R.id.text_varTel);
            item_varTel = (TextView) itemView.findViewById(R.id.item_varTel);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(String id_corr,
                         String name_corr,
                         int position);

        void onItemLongClick(View view, int position);
    }
}

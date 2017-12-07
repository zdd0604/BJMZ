package com.mznerp.business.BusinessAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mznerp.common.Constant;
import com.mznerp.model.EjMyWProj1345;
import com.mznerp.R;

import java.util.List;

/**
 * Created by Admin on 2017/1/17.
 */

public class BusinessSearchAdapter extends RecyclerView.Adapter<BusinessSearchAdapter.MyViewHolder> {
    private Context mContext;
    private List<EjMyWProj1345> list;

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public BusinessSearchAdapter(Context mContext, List<EjMyWProj1345> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate( R.layout.bus_search_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        switch (Constant.project_type) {
            case 0:
                break;
            case 1:
                holder.pro_text.setText(mContext.getString(R.string.sellOrder_Tile_Terminal));
                holder.text_address.setText(mContext.getString(R.string.sellOrder_Tile_Client));
                holder.item_client.setTextColor(Color.parseColor("#888888"));
                holder.pro_text.setTextColor(Color.parseColor("#000000"));
                break;
            case 2:
                holder.pro_text.setText(mContext.getString(R.string.sellOrder_Tile_Client));
                holder.text_address.setText(mContext.getString(R.string.sellOrder_Tile_Address));
                holder.item_client.setTextColor(Color.parseColor("#888888"));
                holder.pro_text.setTextColor(Color.parseColor("#000000"));
                break;
            case 3:
                holder.linearlayout_overclient.setVisibility(View.VISIBLE);
                holder.pro_text.setText(mContext.getString(R.string.sellOrder_Tile_Address));
                holder.text_address.setText(mContext.getString(R.string.sellOrder_Tile_Relation));
                holder.text_overclient.setText(mContext.getString(R.string.sellOrder_Tile_rcvcorr));

                holder.item_overclient.setText(list.get(position).getId_corr());
                holder.pro_text.setTextColor(Color.parseColor("#000000"));
                holder.text_address.setTextColor(Color.parseColor("#888888"));
                holder.item_client.setTextColor(Color.parseColor("#888888"));
                holder.text_overclient.setTextColor(Color.parseColor("#888888"));
                holder.item_overclient.setTextColor(Color.parseColor("#888888"));
                break;
            case 4:
                holder.linearlayout_overclient.setVisibility(View.VISIBLE);
                holder.linear_layout_id.setVisibility(View.VISIBLE);
                holder.linear_layout_chkparm.setVisibility(View.VISIBLE);
                holder.pro_text.setText(mContext.getString(R.string.sellOrder_Tile_Product));
                holder.text_address.setText(mContext.getString(R.string.sellOrder_Tile_Specification));
                holder.text_overclient.setText(mContext.getString(R.string.sellOrder_Tile_Type));
                holder.text_id_item.setText(mContext.getString(R.string.sellOrder_Tile_Code));
                holder.text_chkparm.setText(mContext.getString(R.string.sellOrder_Tile_Performance));
                holder.item_client.setTextColor(Color.parseColor("#888888"));
                holder.item_overclient.setTextColor(Color.parseColor("#888888"));
                holder.item_chkparm.setTextColor(Color.parseColor("#888888"));
                holder.pro_text.setTextColor(Color.parseColor("#000000"));
                holder.text_id_item.setTextColor(Color.parseColor("#000000"));
                holder.item_overclient.setText(list.get(position).getName_terminal());
                holder.item_id_item.setText(list.get(position).getId_wproj());
                String chkparm = list.get(position).getVar_chkparm().trim();
                holder.item_chkparm.setText(chkparm.replaceAll("\r|\n*", ""));
                break;
            case 5:
                holder.linear_layout_chkparm.setVisibility(View.VISIBLE);
                holder.pro_text.setText(mContext.getString(R.string.sellOrder_Tile_Client));
                holder.text_address.setText(mContext.getString(R.string.sellOrder_Tile_Address));
                holder.text_chkparm.setText(mContext.getString(R.string.sellOrder_Tile_Relation));
                holder.item_chkparm.setText(list.get(position).getDec_acaramt());
                holder.item_client.setTextColor(Color.parseColor("#888888"));
                holder.pro_text.setTextColor(Color.parseColor("#000000"));
                holder.text_chkparm.setTextColor(Color.parseColor("#888888"));
                holder.item_chkparm.setTextColor(Color.parseColor("#888888"));
                break;
        }

        holder.item_peoject.setText(list.get(position).getName_wproj());
        holder.item_client.setText(list.get(position).getName_corr());
//        holder.item_line_id.setText(String.valueOf((position + 1)));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(
                            list.get(position).getName_wproj(),
                            list.get(position).getName_corr(),
                            list.get(position).getId_wproj(),
                            list.get(position).getId_corr(),
                            list.get(position).getId_terminal(),
                            list.get(position).getDec_acaramt(),
                            list.get(position).getVar_chkparm(),
                            list.get(position).getDec_taxrate(),
                            position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item_peoject;
        TextView item_client;
        //        TextView item_line_id;
        TextView pro_text;
        TextView text_address;
        TextView text_overclient;
        TextView text_id_item;
        TextView item_id_item;
        TextView text_chkparm;
        TextView item_chkparm;
        LinearLayout linearlayout_overclient;
        LinearLayout linearlayout_besearch;
        LinearLayout linear_layout_id;
        LinearLayout linear_layout_chkparm;
        TextView item_overclient;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_peoject = (TextView) itemView.findViewById(R.id.item_peoject);
            pro_text = (TextView) itemView.findViewById(R.id.pro_text);
            text_address = (TextView) itemView.findViewById(R.id.text_address);
            item_client = (TextView) itemView.findViewById(R.id.item_client);
//            item_line_id = (TextView) itemView.findViewById(R.id.item_line_id);
            text_id_item = (TextView) itemView.findViewById(R.id.text_id_item);
            item_id_item = (TextView) itemView.findViewById(R.id.item_id_item);
            text_chkparm = (TextView) itemView.findViewById(R.id.text_chkparm);
            item_chkparm = (TextView) itemView.findViewById(R.id.item_chkparm);
            linearlayout_overclient = (LinearLayout) itemView.findViewById(R.id.linearlayout_overclient);
            linearlayout_besearch = (LinearLayout) itemView.findViewById(R.id.linearlayout_besearch);
            linear_layout_id = (LinearLayout) itemView.findViewById(R.id.linear_layout_id);
            linear_layout_chkparm = (LinearLayout) itemView.findViewById(R.id.linear_layout_chkparm);
            item_overclient = (TextView) itemView.findViewById(R.id.item_overclient);
            text_overclient = (TextView) itemView.findViewById(R.id.text_overclient);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(String item_peoject,
                         String item_client,
                         String id_wproj,
                         String id_corr,
                         String id_terminal,
                         String dec_acaramt,
                         String var_chkparm,
                         String dec_taxrate,
                         int position);

        void onItemLongClick(View view, int position);
    }

}

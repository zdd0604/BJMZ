package com.mznerp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.MenuContent;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.util.ImageLoaderHelper;
import com.mznerp.util.StringUtil;
import com.mznerp.util.myscom.StringUtils;
import com.mznerp.R;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by zy on 2016/11/4.
 */

public class BusinessGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MenuContent> menulist;// 传递过来未处理的总数据

    @Override
    public int getCount() {
        return menulist != null ? menulist.size() : 0;
    }

    @Override
    public MenuContent getItem(int position) {
        return menulist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        if (StringUtils.isEmpty(menulist.get(position).getIdMenu())) {
            return false;
        } else {
            return true;
        }
    }

    public BusinessGridViewAdapter(Context context, ArrayList<MenuContent> menulist) {
        this.context = context;
        if (menulist == null)
            menulist = new ArrayList<MenuContent>();
        this.menulist = menulist;
    }

    public void refreshList() {
        if (menulist == null) {
            menulist = new ArrayList<MenuContent>();
            notifyDataSetChanged();
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (menulist.size() <= 0) {
            return null;
        } else {
            MenuContent menuContent = menulist.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater
                        .inflate(R.layout.businessmenugrid_item, null);

                viewHolder.tvName = (TextView) convertView
                        .findViewById(R.id.tv_menutitlegrid);//
                viewHolder.paopao = (View) convertView
                        .findViewById(R.id.tv_paopao_businessmenuitemgrid);// 更新红圈提醒
                viewHolder.photo = (RoundedImageView) convertView
                        .findViewById(R.id.imagegrid);//

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 设置图片，没有的话不显示
            if (!StringUtil.isNullOrEmpty(menuContent.getPicpath().trim())) {

                String url = ChatPacketHelper.buildImageRequestURL(
                        menuContent.getPicpath(),
                        ChatConstants.iq.DATA_VALUE_RES_TYPE_MENU);
                ImageLoaderHelper.displayImage(url, viewHolder.photo);
//                Glide.with(context).load(url).transform(new GlideRoundTransform(context, 11)).into(viewHolder.photo);
            } else {
                viewHolder.photo.setVisibility(View.INVISIBLE);
            }


            viewHolder.tvName.setText(menuContent.getNameMenu());

            // 检查客户端当前菜单模板（ctlm1346）与服务器最新模板（menu）是否一致，不一致红点提醒用户更新
            String serverVersion = menuContent.getModelWindow();
            String localVersion = BusinessBaseDao.getTemlateVersion(menuContent.getVarParm());
            if (serverVersion != null && serverVersion.equalsIgnoreCase(localVersion) || serverVersion.equalsIgnoreCase("0")) {
                viewHolder.paopao.setVisibility(View.GONE);
            } else {
                viewHolder.paopao.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    private final static class ViewHolder {
        TextView tvName;
        View paopao;
        RoundedImageView photo;
    }
}

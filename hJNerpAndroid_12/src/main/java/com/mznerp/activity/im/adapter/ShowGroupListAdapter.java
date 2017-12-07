package com.mznerp.activity.im.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mznerp.model.GroupInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.util.ImageLoaderHelper;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.util.myscom.StringUtils;
import com.mznerp.R;

public class ShowGroupListAdapter extends BaseAdapter {
	String TAG = "ImListAdapter";
	private Context context;
	protected SharePreferenceUtil sputil;
	private Bitmap default_group_pic;
	private GroupInfo groupinfo = new GroupInfo();
	private ArrayList<GroupInfo> groupInfoList;

	public ShowGroupListAdapter(Context context,
			ArrayList<GroupInfo> groupInfoList) {
		this.context = context;
		this.groupInfoList = groupInfoList;
		sputil = SharePreferenceUtil.getInstance(context);

		default_group_pic = BitmapFactory.decodeResource(
				context.getResources(),
				R.drawable.nearby_bind_mobile_headerview_icon);
	}

	@Override
	public int getCount() {
		return groupInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return groupInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void refreshList(ArrayList<GroupInfo> items) {
		this.groupInfoList = items;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		groupinfo = groupInfoList.get(position);
		H h = null;
		if (view == null) {
			h = new H();
			view = LayoutInflater.from(context).inflate(
					R.layout.show_groupinfo_item, parent, false);
			h.pic = (ImageView) view.findViewById(R.id.photo_showgroup);
			h.title = (TextView) view.findViewById(R.id.name_showgroup);

			view.setTag(h);
		} else {
			h = (H) view.getTag();
		}

		h.title.setText(groupinfo.groupName);

		if (StringUtils.isNotBlank(groupinfo.groupImage)) {
			String url = ChatPacketHelper.buildImageRequestURL(
					groupinfo.groupImage,
					ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
			ImageLoaderHelper.displayImage(url, h.pic);
		} else {
			h.pic.setImageBitmap(default_group_pic);
		}

		return view;
	}

	/**
	 * 使用static hodler初次加载相对耗时，但是其后占用内存较少
	 * 
	 * @author kang
	 * 
	 */
	static class H {
		ImageView pic;
		TextView title;

	}
}

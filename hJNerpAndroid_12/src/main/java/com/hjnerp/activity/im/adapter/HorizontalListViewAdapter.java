package com.hjnerp.activity.im.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hjnerp.model.FriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

public class HorizontalListViewAdapter extends BaseAdapter{
	String TAG = "HorizontalListViewAdapter";
	//private List<String> friendsIdlist = null;
	private ArrayList<FriendInfo> friendInfoList = null;
	private Context mContext;
	private Bitmap default_user_pic;
	
	public HorizontalListViewAdapter(Context context,ArrayList<FriendInfo> list){
		this.mContext = context;
		this.friendInfoList = list;
		mInflater=LayoutInflater.from(context);
		default_user_pic = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.v5_0_1_profile_headphoto);
	}
	@Override
	public int getCount() {
		return friendInfoList.size();
	}
	private LayoutInflater mInflater;
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void updateListView(ArrayList<FriendInfo> list) {
		this.friendInfoList = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		FriendInfo friendInfo = friendInfoList.get(position);
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontallistview_item, null);
			viewHolder.im=(ImageView)convertView.findViewById(R.id.iv_pic);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		
		if (!StringUtil.isNullOrEmpty(friendInfo.getFriendimage())) {
			/**
			 * @author haijian
			 *添加群成员底下显示头像问题
			 */
			String url = ChatPacketHelper.buildImageRequestURL(friendInfo.getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
			ImageLoaderHelper.displayImage(url, viewHolder.im);
		}else{
			viewHolder.im.setImageBitmap(default_user_pic);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {;
		private ImageView im;
	}
}
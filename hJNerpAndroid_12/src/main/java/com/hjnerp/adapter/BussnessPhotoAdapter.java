package com.hjnerp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BussnessPhotoAdapter extends BaseAdapter{
	String TAG = "HorizontalListViewAdapter";
	//private List<String> friendsIdlist = null;
	private ArrayList<String> friendInfoList = null;
	private Context mContext;
	private Bitmap default_user_pic;
	
	public BussnessPhotoAdapter(Context context,ArrayList<String> list){
		this.mContext = context;
		if(list == null)
			list = new ArrayList<String>();
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
	
	public void updateListView(ArrayList<String> list) {
		if(list == null)
			list = new ArrayList<String>();
		this.friendInfoList = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		String friendInfo = friendInfoList.get(position);
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.bussness_horizontallistview_item, null);
			viewHolder.im=(ImageView)convertView.findViewById(R.id.iv_pic);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		
		if (!StringUtil.isNullOrEmpty(friendInfo)) {
			/**
			 * @author haijian
			 *添加群成员底下显示头像问题
			 */
//			String urllocal = "file://" + imageLocalUri + imageName;
			ImageLoader.getInstance().displayImage("file://"+friendInfo, viewHolder.im);
//			ImageLoaderHelper.displayImage(friendInfo, viewHolder.im);
		}else{
			viewHolder.im.setImageBitmap(default_user_pic);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {;
		private ImageView im;
	}
}
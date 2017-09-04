package com.hjnerp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hjnerpandroid.R;

public class BusinessSginImageViewAdapter extends BaseAdapter {
	private List<Bitmap> imgList = new ArrayList<Bitmap>();
	private Context context;
	
	public void  addBitmap(Bitmap bitmap) {
		imgList.add(bitmap);
		notify();
	}
	
	public void addBitmap (int positon,Bitmap bitmap) {
		imgList.add(positon,bitmap);
		notify();
	}

	public BusinessSginImageViewAdapter(List<Bitmap> imgList, Context context) {
		super();
		this.imgList = imgList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_sgin_imageview, null);
			holder.mImage = (ImageView) convertView.findViewById(R.id.sgin_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mImage.setImageBitmap(imgList.get(position));
		return convertView;
	}

	private static class ViewHolder {
		private ImageView mImage;
	}
}

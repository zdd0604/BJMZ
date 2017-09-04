package com.hjnerp.activity.im.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hjnerp.model.LocationInfo;
import com.hjnerpandroid.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationAdatper extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<LocationInfo> lists;
	
	public LocationAdatper(Context context,List<LocationInfo> lists){
		if(lists==null)
			lists = new ArrayList<LocationInfo>();
		this.lists = lists;
		inflater = LayoutInflater.from(context);
	}
	public void setData(List<LocationInfo> lists){
		if(lists==null)
			lists = new ArrayList<LocationInfo>();
		this.lists = lists;
		notifyDataSetChanged();
	}
	
	public void setSelected(int position){
		for(int i=0;i<lists.size();i++){
			lists.get(i).setSelected(false);
		}
		lists.get(position).setSelected(true);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public LocationInfo getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder;
		if(v== null){
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.location_item, null);
			holder.tvPosition = (TextView) v.findViewById(R.id.tv_position);
			holder.img = (ImageView) v.findViewById(R.id.selected);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		if(getItem(position).isSelected()){
			holder.tvPosition.setTextColor(Color.rgb(34,72,221));//2248DD
			holder.img.setVisibility(View.VISIBLE);
		}else{
			holder.tvPosition.setTextColor(Color.BLACK);
			holder.img.setVisibility(View.GONE);
		}
		holder.tvPosition.setText(getItem(position).getInfo().name);
		return v;
	}
	
	static class ViewHolder{
		private TextView tvPosition;
		private ImageView img;
	}

}

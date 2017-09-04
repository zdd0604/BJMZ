package com.hjnerp.activity.im.adapter;

import java.util.ArrayList;

import com.hjnerpandroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ExtendGridAdapter extends BaseAdapter
{
	ArrayList<ExtendGridDataItem> list = new ArrayList<ExtendGridDataItem>();
	Context context;
	
	public ExtendGridAdapter(Context context)
	{
		this.context = context;
	}
	
	public ArrayList<ExtendGridDataItem> getList()
	{
		return list;
	}
	
	public void addItem(ExtendGridDataItem item)
	{
		list.add(item);
	}
	
	public void removeItem(ExtendGridDataItem item)
	{
		list.remove(item);
	}
	
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public ExtendGridDataItem getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ExtendGridDataItem item = getItem(position);
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.app_item, null);
			holder = new ViewHolder();
			holder.attach_icon = (ImageButton)convertView.findViewById(R.id.app_attach_icon);
			holder.attach_tip = (TextView)convertView.findViewById(R.id.app_attach_tip);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.attach_tip.setText(item.tipResId);
		holder.attach_icon.setImageResource(item.iconResId);
		holder.attach_icon.setOnClickListener(item.action);
		return convertView;
	}
	
	class ViewHolder
	{
		ImageButton attach_icon;
		TextView attach_tip;
	}
	
	public static class ExtendGridDataItem
	{
		public int iconResId;
		public int tipResId;
		public View.OnClickListener action;
	}
}

package com.hjnerp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hjnerpandroid.R;
import com.hjnerp.model.MenuContent;

public class BusinessListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MenuContent> list = new ArrayList<MenuContent>();

	public BusinessListAdapter(Context context, ArrayList<MenuContent> list) {
		this.context = context;
		if(list==null)
			list = new ArrayList<MenuContent>();
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void refreshList(ArrayList<MenuContent> list) {
		if(list == null)
			list = new ArrayList<MenuContent>();
		this.list = list;
		this.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		MenuContent menuContent = list.get(position);
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.businessmenu_item, parent, false);
			viewHolder.title = (TextView) view.findViewById(R.id.tv_menutitle);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.title.setText(menuContent.getNameMenu());
		
		return view;
	}

	public static class ViewHolder {
		//ImageView pic;
		TextView title;
//		TextView modelwindow;
//		TextView parm;
//		TextView type;
	}
}

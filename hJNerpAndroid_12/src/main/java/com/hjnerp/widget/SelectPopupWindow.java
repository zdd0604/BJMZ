package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hjnerp.model.IDComConfig;
import com.hjnerpandroid.R;
import com.hjnerpandroid.R.color;

public class SelectPopupWindow extends PopupWindow 
{
	private View mMenuView;
	private ListView itemView;
	private SelectPopupListAdapter adapter;

	@SuppressLint("InflateParams")
	public SelectPopupWindow(Activity context,List<IDComConfig> items, AdapterView.OnItemClickListener listener) 
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.select_popup_window, null);
		itemView = (ListView)mMenuView.findViewById(R.id.sc_lv);
		adapter = new SelectPopupListAdapter(context, items);
		itemView.setAdapter(adapter);
		itemView.setOnItemClickListener(listener);
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
//		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(color.white);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		
		this.setAnimationStyle(R.style.SelectPopupWindowAnimation);
		this.setInputMethodMode(INPUT_METHOD_NOT_NEEDED);
		this.setOutsideTouchable(true);
	}
	
	public ListView getListView()
	{
		return itemView;
	}
	
	public boolean canShow()
	{
		return !adapter.isEmpty();
	}
	
	public static class SelectPopupListAdapter extends BaseAdapter
	{
		private List<IDComConfig> itemsAdapter =  new ArrayList<IDComConfig>();
		private Context context;
		
		public SelectPopupListAdapter(Context context, List<IDComConfig> items)
		{
			this.context = context;
			setItems(items);
		}



		public void setItems(List<IDComConfig> items)
		{
			this.itemsAdapter = items;
		}
		
		public List<IDComConfig>  getItems()
		{
			return itemsAdapter;
		}
		
		@Override
		public int getCount()
		{
			return itemsAdapter.size();
		}

		@Override
		public String getItem(int position)
		{
			return itemsAdapter.get(position ).getName_com();
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder viewHolder;
			if(context == null)
				return null;
			if(convertView == null)
			{
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.login_com_select_item, null);
				viewHolder.select_item = (TextView) convertView.findViewById(R.id.select_item);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.select_item.setText(getItem(position));
			return convertView;
		}

		private class ViewHolder {
			private TextView select_item;
		}
	}
}

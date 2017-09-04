package com.hjnerp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjnerpandroid.R;

import java.util.List;

/**
 * 签到页面适配器
 * @author zdd
 */
public class BusinessDdispLocathAdapter extends BaseAdapter {
	private List<String> list_locath_title;
	private List<String> list_locath_content;
	private List<Integer> list_locath_cion;

	public BusinessDdispLocathAdapter(List<String> list_locath_title, List<String> list_locath_content
			, List<Integer> list_locath_cion) {
		this.list_locath_title = list_locath_title;
		this.list_locath_content = list_locath_content;
		this.list_locath_cion = list_locath_cion;
	}

	@Override
	public int getCount() {
		return list_locath_title.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.businessddisplocath_item, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.ddisplocath_title);
			viewHolder.content = (TextView) convertView.findViewById(R.id.ddisplocath_content);
			viewHolder.ddisplocath_cion = (ImageView) convertView.findViewById(R.id.ddisplocath_cion);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title.setText(list_locath_title.get(position));
		viewHolder.content.setText(list_locath_content.get(position));
		viewHolder.ddisplocath_cion.setImageResource(list_locath_cion.get(position));
		return convertView;
	}

	private class ViewHolder {
		private TextView title, content;
		private ImageView ddisplocath_cion;
	}
}

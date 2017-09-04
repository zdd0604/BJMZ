package com.hjnerp.activity.im.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hjnerp.model.FileItem;
import com.hjnerpandroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<FileItem> lists;
	private Context context;

	public FileListAdapter(Context context, List<FileItem> lists) {
		if (lists == null)
			lists = new ArrayList<FileItem>();
		this.lists = lists;
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public void refresh(List<FileItem> lists) {
		if (lists == null) {
			lists = new ArrayList<FileItem>();
		}
		this.lists = lists;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public FileItem getItem(int position) {
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
		// TODO Auto-generated method stub
		ViewHolder holder;
		FileItem item = getItem(position);
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.filelist_item, null);
			holder.img = (ImageView) v.findViewById(R.id.file_img);
			holder.sel = (ImageView) v.findViewById(R.id.file_select);
			holder.tv = (TextView) v.findViewById(R.id.tv_file_name);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		String s = item.getFile().getPath();
		File f = new File(s);// 获得文件对象
		char c[] = s.toCharArray();
		int i = s.length();
		if (item.getFile().isDirectory())// 存在分支
		{
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.file));// 设置图片
			holder.sel.setVisibility(View.GONE);
		} else if (c[i - 1] == 't' && c[i - 2] == 'x' && c[i - 3] == 't') {
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.sl_txt));
			holder.sel.setVisibility(View.VISIBLE);

		} else if (c[i - 1] == 'k' && c[i - 2] == 'p' && c[i - 3] == 'a') {
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.apk));
			holder.sel.setVisibility(View.VISIBLE);

		} else if ((c[i - 1] == 'g' && c[i - 2] == 'n' && c[i - 3] == 'p')
				|| (c[i - 1] == 'g' && c[i - 2] == 'p' && c[i - 3] == 'j')) {
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.picture));
			holder.sel.setVisibility(View.VISIBLE);

		} else if (c[i - 1] == '3' && c[i - 2] == 'p' && c[i - 3] == 'm') {
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.music));
			holder.sel.setVisibility(View.VISIBLE);

		} else if (c[i - 1] == 'b' && c[i - 2] == 'd') {
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.database_icon));
			holder.sel.setVisibility(View.VISIBLE);

		} else if ((c[i - 1] == 'p' && c[i - 2] == 'i' && c[i - 3] == 'z')
				|| (c[i - 1] == 'r' && c[i - 2] == 'a' && c[i - 3] == 'r')) {
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.yasuo));
			holder.sel.setVisibility(View.VISIBLE);

		} else {
			holder.img.setImageDrawable(context.getResources().getDrawable(
					R.drawable.sl_else));
			holder.sel.setVisibility(View.VISIBLE);
		}
		if (item.isSelect()) {
			holder.sel.setImageResource(R.drawable.file_selected);
		} else {
			holder.sel.setImageResource(R.drawable.file_un_select);
		}
		holder.tv.setText(item.getFile().getName());
		return v;
	}

	static class ViewHolder {
		private ImageView img, sel;
		private TextView tv;
	}
}

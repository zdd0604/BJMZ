package com.mznerp.activity.im.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mznerp.R;
import com.mznerp.model.ChatHisBean;
import com.mznerp.util.StringUtil;

public class NoticeListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ChatHisBean> noticeList = new ArrayList<ChatHisBean>();
	private PullToRefreshListView adapterList;

	public NoticeListAdapter(Context context, ArrayList<ChatHisBean> list,PullToRefreshListView adapterList) {
		this.context = context;
		if(list==null)
			list = new ArrayList<ChatHisBean>();
		this.noticeList = list;
		this.adapterList = adapterList;
	}

	@Override
	public int getCount() {
		return noticeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void pullRrefreshList(ArrayList<ChatHisBean> list) {
		if(list==null)
			list = new ArrayList<ChatHisBean>();
		this.noticeList = list;
		this.notifyDataSetChanged();
//		adapterList.setSelection(0);
		adapterList.getRefreshableView().setSelection(0);
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ChatHisBean notice = noticeList.get(position);
		ViewHolder h = null;
		if (view == null) {
			h = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.notice_list,
					parent, false);
			h.NoticeContent = (TextView) view.findViewById(R.id.notice_content);
			h.NoticeTitle = (TextView) view.findViewById(R.id.notice_title);
			h.NoticeTime = (TextView) view.findViewById(R.id.notice_time);
			h.LinearAttach = (LinearLayout) view.findViewById(R.id.ll_attach_notice);

			view.setTag(h);
		} else {
			h = (ViewHolder) view.getTag();
		}
		
		String [] msg = notice.getMsgcontent().split("`");
		if(msg.length == 2){
			h.NoticeTitle.setText(msg[0]);
			h.NoticeContent.setText(msg[1]);
		}
		
		long currentTime = Long.valueOf(notice.getMsgTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(currentTime);
		System.out.println(formatter.format(date));

		String timetemp = formatter.format(date);
		
		h.NoticeTime.setText(timetemp);
		
		if(false){//附件
		if(!StringUtil.isNullOrEmpty(notice.getmsgIdFile())){
		View view1 = (View) LayoutInflater.from(context).inflate(
				R.layout.textview_workflow_attach, null);
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		h.LinearAttach.addView(view1, params);
		TextView textView = (TextView) view1
				.findViewById(R.id.tv_attach);
		textView.setText(notice.getmsgIdFile());

		LinearLayout ll_attach = (LinearLayout) view1
				.findViewById(R.id.linear_attach);
		ll_attach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {


			}
		});
		}
		}
		return view;
	}

	/*
	 * 列表布局的各个控件
	 */
	public class ViewHolder {
		TextView NoticeContent;// 对方内容
		TextView NoticeTitle;// 当前用户的内容
		TextView NoticeTime;// 聊天信息发送时间
		LinearLayout LinearAttach;//附件
	}

}

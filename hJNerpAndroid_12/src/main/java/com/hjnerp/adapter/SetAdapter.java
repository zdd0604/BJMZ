package com.hjnerp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjnerp.model.CommonSetInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

public class SetAdapter extends BaseAdapter {
	String TAG = "SetAdapter";
	private Context context;
//	private ArrayList<ChatHisBean> list = new ArrayList<ChatHisBean>();
	ArrayList<CommonSetInfo> commonSetInfoList = new ArrayList<CommonSetInfo>();
	protected SharePreferenceUtil sputil;

	public SetAdapter(Context context, ArrayList<CommonSetInfo> commonSetInfoList) {
		this.context = context;
		if(commonSetInfoList == null)
			commonSetInfoList = new ArrayList<CommonSetInfo>();
		this.commonSetInfoList = commonSetInfoList;
		sputil = SharePreferenceUtil.getInstance(context);
		
	}

	@Override
	public int getCount() {
		return commonSetInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commonSetInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void refreshList(ArrayList<CommonSetInfo> items) {
		if(items == null)
			items = new ArrayList<CommonSetInfo>();
		this.commonSetInfoList = items;
		this.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		CommonSetInfo commonInfo = commonSetInfoList.get(position);
		H h = null;
		if (view == null) {
			h = new H();
			view = LayoutInflater.from(context).inflate(
					R.layout.commonset_item, parent, false);
			h.pic = (ImageView) view.findViewById(R.id.commonset_item_iv);
			h.title = (TextView) view.findViewById(R.id.commonset_item_tv);
			view.setTag(h);
		} else {
			h = (H) view.getTag();
		}
		//单聊
		h.title.setText(commonInfo.name);
		if (StringUtil.isNullOrEmpty(commonInfo.pic)) {
//			Log.e(TAG,"name is " + commonInfo.name);
			if(context.getResources().getString(R.string.commonset_wrapdata).equals(commonInfo.name)){
//				Log.e(TAG,"1");
				h.pic.setImageResource(R.drawable.set_wrapcache);
			}
			if(context.getResources().getString(R.string.commonset_wrapcache).equals(commonInfo.name)){
//				Log.e(TAG,"2");
				h.pic.setImageResource(R.drawable.set_wrapdata);
			}
			if(context.getResources().getString(R.string.commonset_versioncheck).equals(commonInfo.name)){
//				Log.e(TAG,"3");
				h.pic.setImageResource(R.drawable.set_updata);
			}
			if(context.getResources().getString(R.string.commonset_updata).equals(commonInfo.name)){
//				Log.e(TAG,"4");
				h.pic.setImageResource(R.drawable.set_set);
			}
			if(context.getResources().getString(R.string.commonset_updatadate).equals(commonInfo.name)){
//				Log.e(TAG,"5");
				h.pic.setImageResource(R.drawable.set_updatadata);
			}
			
		} else {
			String url = ChatPacketHelper.buildImageRequestURL(commonInfo.pic, ChatConstants.iq.DATA_VALUE_RES_TYPE_MENU);
			ImageLoaderHelper.displayImage(url, h.pic);
		}
		
		return view;
	}
	
	static class H {
		ImageView pic;
		TextView title;
	}
}

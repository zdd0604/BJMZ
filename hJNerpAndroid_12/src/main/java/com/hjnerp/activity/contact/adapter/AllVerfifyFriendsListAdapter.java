package com.hjnerp.activity.contact.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjnerp.activity.contact.AllVerfifyFriendsActivity;
import com.hjnerp.activity.contact.ContactConstants;
import com.hjnerp.model.VerfifyFriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import java.util.ArrayList;

public class AllVerfifyFriendsListAdapter extends BaseAdapter {
	String TAG = "AllVerfifyFriendsListAdapter";
	private Context context;
	private ArrayList<VerfifyFriendInfo> list = new ArrayList<VerfifyFriendInfo>();
	protected SharePreferenceUtil sputil;
//	private VerfifyFriendInfo friendinfo;

	public AllVerfifyFriendsListAdapter(Context context, ArrayList<VerfifyFriendInfo> list) {
		this.context = context;
		this.list = list;
		sputil = SharePreferenceUtil.getInstance(context);
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
	public void refreshList(ArrayList<VerfifyFriendInfo> items) {
		this.list = items;
		this.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final VerfifyFriendInfo friendinfo = list.get(position);
		H h = null;
		if (view == null) {
			h = new H();
			view = LayoutInflater.from(context).inflate(
					R.layout.allfriendsverfify_item, parent, false);
			h.pic = (ImageView) view.findViewById(R.id.pic);
			h.result = (TextView) view.findViewById(R.id.result_tv);
			h.usermsg = (TextView) view.findViewById(R.id.msg_tv);
			h.username = (TextView) view.findViewById(R.id.username_tv);
			h.addbtn = (TextView) view.findViewById(R.id.add_btn);
			view.setTag(h);
		} else {
			h = (H) view.getTag();
		}
		h.addbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//	Log.e(TAG,"onClick " + friendinfo.getFriendid());
				Log.i("info", ""+friendinfo.getFriendid()+"");
				// TODO Auto-generated method stub
				addFriend(friendinfo);
				
			}
		});

		h.username.setText(friendinfo.getFriendname());
		
		//设置头像
		if (StringUtil.isNullOrEmpty(friendinfo.getFriendimage())) {
			h.pic.setImageResource(R.drawable.v5_0_1_profile_headphoto);
		} else {
			String url = ChatPacketHelper.buildImageRequestURL(friendinfo.getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
			ImageLoaderHelper.displayImage(url, h.pic);
		}
		
		//Log.e(TAG,"<type> is " + friendinfo.getVerfifyType() + " <result> is " + friendinfo.getVerfifyResult());
		//如果是我主动添加好友
		if(friendinfo.getVerfifyType().equals(ContactConstants.I_WANT_ADD_THIS_FRIEND)){
			setVisibility(h,View.GONE,View.VISIBLE);
			if(friendinfo.getVerfifyResult().equals(ContactConstants.VERFIFY_ONGO)){
				h.result.setText("等待验证");
			}else if(friendinfo.getVerfifyResult().equals(ContactConstants.VERFIFY_PERMISSION)){
				h.result.setText("通过对方验证");
			}else if(friendinfo.getVerfifyResult().equals(ContactConstants.VERFIFY_REFUSE)){
				h.result.setText("对方拒绝添加");
			}
		}
		
		if(friendinfo.getVerfifyType().equals(ContactConstants.THIS_FRIEND_WANT_ADD_ME)){
			h.usermsg.setText(friendinfo.getVerfifyNote());
			if(friendinfo.getVerfifyResult().equals(ContactConstants.VERFIFY_ONGO)){
				h.result.setVisibility(View.GONE);
				setVisibility(h,View.VISIBLE,View.GONE);
				
			}else if(friendinfo.getVerfifyResult().equals(ContactConstants.VERFIFY_PERMISSION)){
				setVisibility(h,View.GONE,View.VISIBLE);
				h.result.setText("我同意添加");
			}else if(friendinfo.getVerfifyResult().equals(ContactConstants.VERFIFY_REFUSE)){
				setVisibility(h,View.GONE,View.VISIBLE);
				h.result.setText("我拒绝添加");
			}
		}
		
		return view;
	}

	public void addFriend(VerfifyFriendInfo friendinfo){
		if(friendinfo != null){
			((AllVerfifyFriendsActivity)context).addFriend(friendinfo);
		}
	}
	
	/**
	 * 使用static hodler初次加载相对耗时，但是其后占用内存较少
	 * @author kang
	 *
	 */
	static class H {
		ImageView pic;
		TextView username;
		TextView usermsg;
		TextView result;
		TextView addbtn;
	}
	
	private void setVisibility(H holder, int btn, int textview) {
		holder.addbtn.setVisibility(btn);
		holder.result.setVisibility(textview);
	}
}


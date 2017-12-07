package com.mznerp.activity.im.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mznerp.activity.contact.FriendsActivity;
import com.mznerp.activity.im.SelectGroupChatMemberActivity;
import com.mznerp.activity.im.SelectGroupChatMemberDeptActivity;
import com.mznerp.activity.im.ShowGroupChatMemberActivity;
import com.mznerp.common.Constant;
import com.mznerp.model.FriendInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.util.ImageLoaderHelper;
import com.mznerp.util.StringUtil;
import com.mznerp.R;

public class ShowGroupChatMemberGridAdapter extends BaseAdapter {
	String TAG = "ShowGroupChatMemberGridAdapter";
	private LayoutInflater inflater = null;
	private ArrayList<FriendInfo> list;
	private Context context;
	private String myId, lordId;
	private Boolean showDelete = false;
	private Bitmap add_bitmap,remove_bitmap,default_bitmap;

	public ShowGroupChatMemberGridAdapter(Context context,
			ArrayList<FriendInfo> list) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		myId = ((ShowGroupChatMemberActivity) context).myId;
		lordId = ((ShowGroupChatMemberActivity) context).lordId;
		add_bitmap = BitmapFactory.decodeResource(
				context.getResources(),R.drawable.avatar_dotline_add_bg);
		remove_bitmap = BitmapFactory.decodeResource(
				context.getResources(),
				R.drawable.avatar_dotline_minus_bg);
		default_bitmap = BitmapFactory.decodeResource(
				context.getResources(),
				R.drawable.v5_0_1_profile_headphoto);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateView(ArrayList<FriendInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int a = position;
		ViewHolder vHolder = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.groupchatmember_item, null);
			vHolder = new ViewHolder();
			vHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
			vHolder.nickname = (TextView) convertView
					.findViewById(R.id.nickname);
			vHolder.delete = (TextView) convertView.findViewById(R.id.delete_tv);

		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		if (myId.equals(lordId)) { //如果我是群主，末行显示“+”、“-”
			if (position == (list.size() - 1)) {
				Log.i(TAG, "remove---------------->");
				vHolder.nickname.setText("");
				vHolder.photo.setImageBitmap(remove_bitmap);

			} else if (position == (list.size() - 2)) {
				Log.i(TAG, "add---------------->");

				vHolder.nickname.setText("");
				vHolder.photo.setImageBitmap(add_bitmap);
			} else {
				vHolder.nickname.setText((String) list.get(a).getFriendname());

				if (StringUtil.isNullOrEmpty(list.get(a).getFriendimage())) {
					vHolder.photo.setImageBitmap(default_bitmap);
					
				}else{
					/**
					 * @author haijian
					 * 头像显示不出bug调试
					 * 原因：经检查，能显示头像的ChatPacketHelper.buildImageRequestURL(list
							.get(a).getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
							这个方法中的第二个参数是ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH
							而原来的是ChatConstants.iq.DATA_VALUE_RES_TYPE_IM，现在先这样修改了，
							我感觉应该是传错了，
							当然也有可能是服务端做了图片区分了，不同位置显示的图片大小不一样
					 */
//					String url = ChatPacketHelper.buildImageRequestURL(list
//							.get(a).getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
					String url = ChatPacketHelper.buildImageRequestURL(list
							.get(a).getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
					ImageLoaderHelper.displayImage(url, vHolder.photo);
				}
			}
		} else {//如果我不是群主，末行显示“+”
			if (position != (list.size() - 1)) {
				vHolder.nickname.setText((String) list.get(a).getFriendname());
				if (StringUtil.isNullOrEmpty(list.get(a).getFriendimage())) {
					vHolder.photo.setImageBitmap(default_bitmap);
				} else {
					String url = ChatPacketHelper.buildImageRequestURL(list
							.get(a).getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
					ImageLoaderHelper.displayImage(url, vHolder.photo);
				}

			} else {

				vHolder.nickname.setText("");
				vHolder.photo.setImageBitmap(add_bitmap);
			}
		
		}

		if(showDelete && !list.get(a).getFriendid().equals("") && !myId.equals(list.get(a).getFriendid())){
			
			vHolder.delete.setVisibility(View.VISIBLE);
		}else{
			vHolder.delete.setVisibility(View.GONE);
		}
		
		vHolder.photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG,"click photo " + list.get(a).getFriendid());
				if(ifIamLord()){
					if(a == list.size() - 1){//减
						if(showDelete){
							showDelete = false;
						}else{
							showDelete = true;
						}
						updateView(list);
					}else if(a == list.size() - 2){//加
						// TODO 增加新成员
						showDelete = false;
						jumpToAddMember();
					}else{
						jumpToFriendDetil(list.get(a));
					}
				}else{
					if(a == list.size() - 1){//加
						jumpToAddMember();
					}else{
						jumpToFriendDetil(list.get(a));
					}
				}
				
			}
		});
		vHolder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG,"delete " + list.get(a).getFriendid());
				((ShowGroupChatMemberActivity)context).deleteMember(list.get(a));
				
			}
		});
		convertView.setTag(vHolder);

		return convertView;
	}

	static class ViewHolder {
		TextView nickname,delete;
		ImageView photo;

	}
	public Boolean ifIamLord(){
		if(myId.equals(lordId)){
			return true;
		}else{
			return false;
		}
	}
	

	public void jumpToAddMember(){
		Intent intent = new Intent();
		intent.setClass(context, SelectGroupChatMemberDeptActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(SelectGroupChatMemberActivity.EXTRA_GROUPID,
				((ShowGroupChatMemberActivity)context).group.groupID);
		intent.putExtras(mBundle);

		context.startActivity(intent);
	}
	public void jumpToFriendDetil(FriendInfo friendInfo){
		Intent intent = new Intent();
		intent.setClass(context, FriendsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Constant.FRIEND_READ,
				(Serializable) friendInfo);
		intent.putExtras(mBundle);

		context.startActivity(intent);
	}
}

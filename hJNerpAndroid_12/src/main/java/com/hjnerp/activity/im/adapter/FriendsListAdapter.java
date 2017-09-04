package com.hjnerp.activity.im.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.activity.im.SelectGroupChatMemberActivity;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.FriendSortLetterComparator;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

public class FriendsListAdapter extends BaseAdapter {
	String TAG = "FriendsListAdapter";
	private List<FriendInfo> list = null;
	private Context mContext;

	public HashMap<FriendInfo, Boolean> selectedFriends;
	public FriendSortLetterComparator pinyinComparator;
	private Bitmap default_user_pic;

	// True: remove member from group; False: add member into group
	private boolean isSelected;

	private String filterStr;

	public FriendsListAdapter(Context mContext, List<FriendInfo> list,
			boolean isSelected) {
		pinyinComparator = new FriendSortLetterComparator();
		this.mContext = mContext;
		this.list = list;
		this.isSelected = isSelected;
		default_user_pic = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.v5_0_1_profile_headphoto);

		selectedFriends = new HashMap<FriendInfo, Boolean>();
		for (int i = 0; i < list.size(); i++) {
			selectedFriends.put(list.get(i), isSelected);
		}
	}

	public void updateListView(List<FriendInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void updateListView(List<FriendInfo> list,
			HashMap<FriendInfo, Boolean> selectedMember) {
		this.list = list;
		this.selectedFriends = selectedMember;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.friendinfo_item, null);
			viewHolder.tvTitle = (TextView) view
					.findViewById(R.id.member_item_title_tv);
			viewHolder.tvSearch = (TextView) view.findViewById(R.id.tv_search);
			viewHolder.imPhoto = (ImageView) view
					.findViewById(R.id.member_item_photo_iv);
			viewHolder.tvLetter = (TextView) view
					.findViewById(R.id.member_itemt_catalog);
			viewHolder.selected_iv = (ImageView) view
					.findViewById(R.id.member_select_iv);
			viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
			viewHolder.rl_title = (RelativeLayout) view
					.findViewById(R.id.rl_title);
			viewHolder.ll_content = (LinearLayout) view
					.findViewById(R.id.ll_content);

			viewHolder.selected_iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ImageView selected_iv = (ImageView) v;
					FriendInfo friendInfo = (FriendInfo) selected_iv.getTag();
					boolean isSelected = selectedFriends.get(friendInfo);
					if (isSelected) {
						selected_iv.setImageDrawable(mContext.getResources()
								.getDrawable(R.drawable.member_normal));

					} else {
						selected_iv.setImageDrawable(mContext.getResources()
								.getDrawable(R.drawable.member_pressed));
					}
					selectedFriends.put(friendInfo, !isSelected);
					updateConfirmBtnText();
					updateHorizontalListView();
				}
			});

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		if (position == 0) {
			viewHolder.tvSearch.setVisibility(View.VISIBLE);
			viewHolder.ll_content.setVisibility(View.GONE);
		} else {
			viewHolder.tvSearch.setVisibility(View.GONE);
			viewHolder.ll_content.setVisibility(View.VISIBLE);
			FriendInfo friendInfo = this.list.get(position);
			int section = getSectionForPosition(position);

			if (position == getPositionForSection(section)) {
				viewHolder.rl_title.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(String.valueOf(friendInfo
						.getSortLetter()));
			} else {
				viewHolder.rl_title.setVisibility(View.GONE);
			}
			viewHolder.tvTitle.setText(friendInfo.getFriendname());
			boolean isSelected = selectedFriends.get(friendInfo);
			if (isSelected) {
				viewHolder.selected_iv.setImageDrawable(mContext.getResources()
						.getDrawable(R.drawable.member_pressed));

			} else {
				viewHolder.selected_iv.setImageDrawable(mContext.getResources()
						.getDrawable(R.drawable.member_normal));
			}

			viewHolder.selected_iv.setTag(friendInfo);
			if (!StringUtil.isNullOrEmpty(friendInfo.getFriendimage())) {
				String url = ChatPacketHelper.buildImageRequestURL(friendInfo
						.getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
				ImageLoaderHelper.displayImage(url, viewHolder.imPhoto);
			}else{
				viewHolder.imPhoto.setImageBitmap(default_user_pic);
			}
		}

		return view;

	}

	private void updateConfirmBtnText() {

		((SelectGroupChatMemberActivity) mContext)
				.setConfirmBtnText(countSelectedFriends());
	}

	private void updateHorizontalListView() {
		((SelectGroupChatMemberActivity) mContext).horizontalAdapter
				.updateListView(((SelectGroupChatMemberActivity) mContext).selectedFriendInfoList);
	}

	public int countSelectedFriends() {
		((SelectGroupChatMemberActivity) mContext).selectedFriendInfoList = new ArrayList<FriendInfo>();
		Set<FriendInfo> setKeys = selectedFriends.keySet();
		Iterator<FriendInfo> ite = setKeys.iterator();
		if (isSelected) {

			while (ite.hasNext()) {
				FriendInfo key = ite.next();
				// when selected value is false. we need remove this member from
				// group.
				if (!selectedFriends.get(key)) {
					((SelectGroupChatMemberActivity) mContext).selectedFriendInfoList
							.add(key);
				}

			}

		} else {
			while (ite.hasNext()) {
				FriendInfo key = ite.next();
				if (selectedFriends.get(key)) {
					Log.i(TAG, "key is " + key);
					((SelectGroupChatMemberActivity) mContext).selectedFriendInfoList
							.add(key);
				}

			}
		}
		Collections
				.sort(((SelectGroupChatMemberActivity) mContext).selectedFriendInfoList,
						pinyinComparator);
		return ((SelectGroupChatMemberActivity) mContext).selectedFriendInfoList
				.size();
	}

	private boolean checkFilterStrValue() {

		if (StringUtil.isNullOrEmpty(filterStr)) {
			return true;
		}
		return false;
	}

	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetter();
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			char firstChar = list.get(i).getSortLetter();
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView tvSearch; // 首行“选择一个群”
		ImageView imPhoto;
		ImageView selected_iv;
		LinearLayout ll_item;
		LinearLayout ll_content; // 正常内容部分（包括字母部分）

		RelativeLayout rl_title;

	}

}
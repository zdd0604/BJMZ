package com.hjnerp.activity.im.adapter;

//package com.husoft.eap.adapter;
//
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerp.activity.im.SelectGroupChatMemberDeptActivity;
import com.hjnerp.model.DeptInfo;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.myscom.StringUtils;
import com.hjnerpandroid.R;

/**
* 自定义ExpandableListView的适配器
* 
* @author way
* 
*/

public class FriendsListAdapterDept extends BaseExpandableListAdapter {
	String TAG = "ContactExpandableListAdapter";
	private Context context;
	private List<DeptInfo> deptList;// 传递过来的经过处理的总数据
	private Bitmap default_user_pic;
	private Boolean selected = true;
	private Boolean unselected = false;
	private Map<String, Object> tagMap;
	
	DeptInfo deptBean;
	int clickChildPosition;

	public FriendsListAdapterDept(Context context, List<DeptInfo> dept) {
		super();
		this.context = context;
		this.deptList = dept;
		default_user_pic = BitmapFactory.decodeResource(context.getResources(),R.drawable.v5_0_1_profile_headphoto);
	}
	public void refreshList(List<DeptInfo> dept) {
		this.deptList = dept;
		this.notifyDataSetChanged();
	}
	// 得到大组成员的view
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		DeptInfo deptBean = deptList.get(groupPosition);
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.fragment_contacter_group_dept,null);
		}

		LinearLayout ll_group = (LinearLayout) convertView
				.findViewById(R.id.ll_group_dept);
		ImageView image_group = (ImageView) convertView
				.findViewById(R.id.groupImage_dept);
		TextView deptname = (TextView) convertView.findViewById(R.id.groupName_dept);
		

		if (groupPosition == 0) {
			image_group.setVisibility(View.VISIBLE);
//			image_group.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.default_chatroom));
			ll_group.setBackgroundColor(context.getResources().getColor(R.color.white));
		}  else{
			image_group.setVisibility(View.GONE);
			ll_group.setBackgroundColor(context.getResources().getColor(R.color.contact_bkg_white));
		}
		if(StringUtil.isNullOrEmpty(deptBean.getDeptName())){
			deptname.setText("");
		}else{
			deptname.setText(deptBean.getDeptName());// 设置大组成员名称
		}

		return convertView;
	}

	// 得到大组成员的id
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 得到大组成员名称
	public Object getGroupName(int groupPosition) {
		return deptList.get(groupPosition).getDeptName();
	}

	// 得到大组成员名称
	public Object getGroupID(int groupPosition) {
		return deptList.get(groupPosition).getDeptId();
	}

	// 得到大组成员总数
	public int getGroupCount() {
		return deptList.size();
	}

	// 得到小组成员的view

	
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		deptBean = deptList.get(groupPosition);
		clickChildPosition = childPosition;
		FriendInfo mFriendInfo = deptBean.getFriendInfo(childPosition);
		
		ChildViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ChildViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.friendinfo_item_dept,
					null);

			viewHolder.tvchildName = (TextView) convertView
					.findViewById(R.id.member_item_title_tv_dept);// 显示用户名
			viewHolder.photo = (ImageView) convertView
					.findViewById(R.id.member_item_photo_iv_dept);//
			viewHolder.button = (ImageView) convertView.findViewById(R.id.member_select_iv_dept);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ChildViewHolder) convertView.getTag();
		}

	

		viewHolder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageView selected_iv = (ImageView) v;
				FriendInfo friendInfoTemp = (FriendInfo) selected_iv.getTag();

				if(((SelectGroupChatMemberDeptActivity)context).ifIsSelected(friendInfoTemp)){//之前被选中
					
					selected_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.member_normal));
					((SelectGroupChatMemberDeptActivity)context).deleteSelectedFriends(friendInfoTemp);
					((SelectGroupChatMemberDeptActivity)context).setConfirmBtnText();
				}else{
					
					selected_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.member_pressed));
					((SelectGroupChatMemberDeptActivity)context).addSelectedFriends(friendInfoTemp);
					((SelectGroupChatMemberDeptActivity)context).setConfirmBtnText();
				}
				updateHorizontalListView();
			}
		});
		

		viewHolder.button.setTag(mFriendInfo);
		
		if(((SelectGroupChatMemberDeptActivity)context).ifIsSelected(mFriendInfo)){//之前被选中
			
			viewHolder.button.setImageDrawable(context.getResources().getDrawable(R.drawable.member_pressed));
		}else{
			
			viewHolder.button.setImageDrawable(context.getResources().getDrawable(R.drawable.member_normal));
		}
		
		
		
		
		if(StringUtils.isNotBlank(deptBean.getFriendInfo(childPosition).getFriendimage())){
			String url = ChatPacketHelper.buildImageRequestURL(deptBean
					.getFriendInfo(childPosition).getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
			
			ImageLoaderHelper.displayImage(url, viewHolder.photo);
		}else{
			viewHolder.photo.setImageBitmap(default_user_pic);
		}
		viewHolder.tvchildName.setText(deptBean.getFriendInfo(childPosition)
				.getFriendname());


		return convertView;
	}

	final static class ChildViewHolder {
		TextView tvchildName;
		ImageView photo;
		ImageView button;
	}

	// 得到小组成员id
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 得到小组成员的名称
	public Object getChild(int groupPosition, int childPosition) {
		return deptList.get(groupPosition).getFriendInfo(childPosition);
	}

	// 得到小组成员的数量
	public int getChildrenCount(int groupPosition) {
		return deptList.get(groupPosition).getChildCount();

	}

	/**
	 * Indicates whether the child and group IDs are stable across changes to
	 * the underlying data. 表明大組和小组id是否稳定的更改底层数据。
	 * 
	 * @return whether or not the same ID always refers to the same object
	 * @see Adapter#hasStableIds()
	 */
	public boolean hasStableIds() {
		return true;
	}

	// 得到小组成员是否被选择
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 这个方法是我自定义的，用于下拉刷新好友的方法
	 * 
	 * @param group
	 *            传递进来的新数据
	 */
	public void updata(List<DeptInfo> group) {
		this.deptList = null;
		this.deptList = group;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return deptList.get(groupPosition);
	}
	private void updateHorizontalListView() {
		((SelectGroupChatMemberDeptActivity) context).horizontalAdapter
				.updateListView(((SelectGroupChatMemberDeptActivity) context).selectedFriendInfoList);
	}
}

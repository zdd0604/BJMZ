package com.hjnerp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjnerpandroid.R;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;

import java.util.ArrayList;

public class ImListAdapter extends BaseAdapter {
	String TAG = "ImListAdapter";
	private Context context;
	private ArrayList<ChatHisBean> list = new ArrayList<ChatHisBean>();
	protected SharePreferenceUtil sputil;
	private Bitmap default_group_pic, default_user_pic;
	private FriendInfo friendinfo = new FriendInfo();
	private GroupInfo groupinfo = new GroupInfo();

	public ImListAdapter(Context context, ArrayList<ChatHisBean> list) {
		this.context = context;
		if(list==null)
			list = new ArrayList<ChatHisBean>();
		this.list = list;
		sputil = SharePreferenceUtil.getInstance(context);

		default_group_pic = BitmapFactory.decodeResource(
				context.getResources(),
				R.drawable.nearby_bind_mobile_headerview_icon);
		default_user_pic = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.v5_0_1_profile_headphoto);
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

	public void refreshList(ArrayList<ChatHisBean> items) {
		if(items==null)
			items = new ArrayList<ChatHisBean>();
		this.list = items;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ChatHisBean chatbean = list.get(position);
		Integer NCount = chatbean.getMsgSum();
		H h = null;
		if (view == null) {
			h = new H();
			view = LayoutInflater.from(context).inflate(
					R.layout.fragment_im_list, parent, false);
			h.pic = (ImageView) view.findViewById(R.id.photo);
			h.title = (TextView) view.findViewById(R.id.new_title);
			h.time = (TextView) view.findViewById(R.id.new_date);
			h.lastmsg = (TextView) view.findViewById(R.id.new_content);
			h.paopao = (TextView) view.findViewById(R.id.paopao);
			view.setTag(h);
		} else {
			h = (H) view.getTag();
		}
		// 单聊
		if (ChatConstants.msg.TYPE_CHAT.equals(chatbean.getMsgType())) {

			// 最后一条信息室我发的
			if (chatbean.getMsgFrom().equals(sputil.getMyId())) {

				friendinfo = QiXinBaseDao.queryFriendInfo(chatbean.getMsgTo());
				if (friendinfo != null) {
					h.title.setText(friendinfo.getFriendname());
				}

				// 最后一条信息是好友发的
			} else {
				friendinfo = QiXinBaseDao
						.queryFriendInfo(chatbean.getMsgFrom());
				if (friendinfo != null) {
					h.title.setText(friendinfo.getFriendname());
				}
			}

			// 设置头像
			if (friendinfo == null
					|| StringUtil.isNullOrEmpty(friendinfo.getFriendimage())
					|| friendinfo.getFriendimage().length() < 5) {
				h.pic.setImageBitmap(default_user_pic);
			} else {
				String url = ChatPacketHelper.buildImageRequestURL(
						friendinfo.getFriendimage(),
						ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
				ImageLoaderHelper.displayImage(url, h.pic);
			}

		}

		// 群聊
		if (ChatConstants.msg.TYPE_GROUPCHAT.equals(chatbean.getMsgType())) {
			// Log.i(TAG,"group id is "+chatbean.getMsgTo());
			groupinfo = QiXinBaseDao.queryGroupInfo(chatbean.getMsgTo());
			if (groupinfo != null) {
				h.title.setText(groupinfo.groupName);
			}
			// 设置头像
			h.pic.setImageBitmap(default_group_pic);

		}

		h.time.setText(DateUtil.msgToHumanReadableTime(chatbean.getMsgTime()));

		if (chatbean.getMsgcontent().indexOf("[语音]") != -1) {
			h.lastmsg.setText("[语音]");
		}else if(chatbean.getMsgcontent().indexOf("[位置]") != -1){
			h.lastmsg.setText(chatbean.getMsgcontent().split(",")[chatbean.getMsgcontent().split(",").length-1]);
		}
		else {
			h.lastmsg.setText(TextUtils.isEmpty(chatbean.getMsgcontent()) ? ""
					: chatbean.getMsgcontent().split("`")[0]);
		}

		if (NCount != null && NCount > 0) {
			h.paopao.setText(NCount.toString() + "");
			h.paopao.setVisibility(View.VISIBLE);
		} else {
			h.paopao.setVisibility(View.GONE);
		}

		return view;
	}

	/**
	 * 使用static hodler初次加载相对耗时，但是其后占用内存较少
	 * 
	 * @author kang
	 * 
	 */
	static class H {
		ImageView pic;
		TextView title;
		TextView time;
		TextView lastmsg;
		TextView noticesum;
		TextView paopao;
	}
}

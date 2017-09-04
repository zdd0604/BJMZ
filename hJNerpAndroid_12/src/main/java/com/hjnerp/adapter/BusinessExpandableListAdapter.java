package com.hjnerp.adapter;

//package com.husoft.eap.adapter;
//
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.MenuContent;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class BusinessExpandableListAdapter extends BaseExpandableListAdapter {
	String TAG = "ContactExpandableListAdapter";
	private int iHeight = 0;
	private Context context;
	private ArrayList<MenuContent> menulist;// 传递过来未处理的总数据

	public ArrayList<ArrayList<MenuContent>> groupMenuList = new ArrayList<ArrayList<MenuContent>>();// 进行分组处理后的数据

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, ArrayList> sort(ArrayList<MenuContent> list) {
		TreeMap tm = new TreeMap();
		for (int i = 0; i < list.size(); i++) {
			MenuContent s = list.get(i);
			if (tm.containsKey(s.getVarParm1())) {//
				ArrayList<MenuContent> l11 = (ArrayList<MenuContent>) tm.get(s
						.getVarParm1());
				l11.add(s);
			} else {
				ArrayList<MenuContent> tem = new ArrayList<MenuContent>();
				tem.add(s);
				tm.put(s.getVarParm1(), tem);
			}

		}
		return tm;
	}

	// 把原始数据menulist转变成需要的数据groupMenuList
	private void makeData() {
		Map<String, ArrayList> ss = sort(menulist);
		Iterator it = ss.keySet().iterator();
		groupMenuList = new ArrayList<ArrayList<MenuContent>>();
		while (it.hasNext()) {

			String key = (String) it.next();
			Log.d("key",key);
			ArrayList<MenuContent> list1 = ss.get(key);
			groupMenuList.add(list1);
		}

	}

	public BusinessExpandableListAdapter(Context context,
			ArrayList<MenuContent> dept) {
		super();
		this.context = context;
		if(dept==null)
			dept = new ArrayList<MenuContent>();
		this.menulist = dept;
		makeData();
	}

	public void refreshList(ArrayList<MenuContent> menulist) {
		if(menulist==null)
			menulist = new ArrayList<MenuContent>();
		this.menulist = menulist;
		this.notifyDataSetChanged();
		makeData();
	}

	// 组
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// MenuContent menuContent = menulist.get(groupPosition);
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(
					R.layout.fragment_business_group_item, null);
		}

		LinearLayout ll_group = (LinearLayout) convertView
				.findViewById(R.id.ll_business_group);

		ll_group.setBackgroundColor(context.getResources().getColor(
				R.color.contact_bkg_white));

		return convertView;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public int getGroupCount() {
		return groupMenuList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return menulist.get(groupPosition);
	}

	// 组内成员
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (groupMenuList.size() <= 0)
			return null;
		if (groupMenuList.get(groupPosition).size() > childPosition) {

			MenuContent menuContent = groupMenuList.get(groupPosition).get(
					childPosition);
			ChildViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ChildViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater
						.inflate(R.layout.businessmenu_item, null);

				viewHolder.tvchildName = (TextView) convertView
						.findViewById(R.id.tv_menutitle);//
				viewHolder.paopao = (TextView) convertView
						.findViewById(R.id.tv_paopao_businessmenuitem);// 更新红圈提醒
				viewHolder.photo = (ImageView) convertView
						.findViewById(R.id.image);//
				viewHolder.line = (ImageView) convertView
						.findViewById(R.id.iv_line);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ChildViewHolder) convertView.getTag();
			}

			// 设置图片，没有的话不显示
			if (!StringUtil.isNullOrEmpty(menuContent.getPicpath().trim())) {

				String url = ChatPacketHelper.buildImageRequestURL(
						menuContent.getPicpath(),
						ChatConstants.iq.DATA_VALUE_RES_TYPE_MENU);
				ImageLoaderHelper.displayImage(url, viewHolder.photo);
			} else {
				viewHolder.photo.setVisibility(View.INVISIBLE);

			}

			if (childPosition < groupMenuList.get(groupPosition).size() - 1) {
				viewHolder.line.setVisibility(View.VISIBLE);
			} else {
				viewHolder.line.setVisibility(View.INVISIBLE);
			}

			viewHolder.tvchildName.setText(menuContent.getNameMenu());

			// 检查客户端当前菜单模板（ctlm1346）与服务器最新模板（menu）是否一致，不一致红点提醒用户更新
			String serverVersion = menuContent.getModelWindow();
			String localVersion = BusinessBaseDao.getTemlateVersion(menuContent
					.getVarParm());

			if (serverVersion != null
					&& serverVersion.equalsIgnoreCase(localVersion)) {
				viewHolder.paopao.setVisibility(View.GONE);
			} else {
				viewHolder.paopao.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	final static class ChildViewHolder {
		TextView tvchildName;
		TextView paopao;
		ImageView photo;
		ImageView line;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return groupMenuList.get(groupPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return groupMenuList.get(groupPosition).size();

	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}

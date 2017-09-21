package com.hjnerp.adapter;

//package com.husoft.eap.adapter;
//

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.fragment.ContactFragment;
import com.hjnerp.model.DeptInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.Log;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.myscom.StringUtils;
import com.hjnerpandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义ExpandableListView的适配器
 *
 * @author way
 */

public class ContactExpandableListAdapter extends BaseExpandableListAdapter {
    String TAG = "ContactExpandableListAdapter";
    private int iHeight = 0;
    private Context context;
    private List<DeptInfo> dept;// 传递过来的经过处理的总数据
    private Bitmap default_user_pic;
    TextView deptname;

    public ContactExpandableListAdapter(Context context, List<DeptInfo> dept) {
        super();
        this.context = context;
        if (dept == null)
            dept = new ArrayList<DeptInfo>();
        this.dept = dept;
        default_user_pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.v5_0_1_profile_headphoto);
    }

    public void refreshList(List<DeptInfo> dept) {
        if (dept == null)
            dept = new ArrayList<DeptInfo>();
        this.dept = dept;
        this.notifyDataSetChanged();
    }

    // 得到大组成员的view
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DeptInfo deptBean = dept.get(groupPosition);

        Log.e("show", deptBean.toString());
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.fragment_contacter_group, null);
        }

        LinearLayout ll_group = (LinearLayout) convertView.findViewById(R.id.ll_group);
        View view1 = convertView.findViewById(R.id.view1);
        View view2 = convertView.findViewById(R.id.view2);
        View view3 = convertView.findViewById(R.id.view3);
        RelativeLayout grouprl = (RelativeLayout) convertView.findViewById(R.id.grouprl);
        ImageView image_group = (ImageView) convertView.findViewById(R.id.groupImage);
        deptname = (TextView) convertView.findViewById(R.id.groupName);
        TextView paopao = (TextView) convertView.findViewById(R.id.tv_newcontact_paopao);
        paopao.setVisibility(View.GONE);

        if (groupPosition == 0) {//添加联系人
            image_group.setVisibility(View.VISIBLE);
            image_group.setImageResource(R.drawable.default_fmessage);
            ll_group.setBackgroundColor(Color.parseColor("#FFFFFF"));
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
        } else if (groupPosition == 1) {//联系人申请
            if (ContactFragment.ALL_NEW_MSG_COUNTS != 0) {
                paopao.setVisibility(View.VISIBLE);
                paopao.setText(String.valueOf(ContactFragment.ALL_NEW_MSG_COUNTS));
            }
            image_group.setVisibility(View.VISIBLE);
            image_group.setImageResource(R.drawable.userguide_nearfirends_icon);
            ll_group.setBackgroundColor(Color.parseColor("#FFFFFF"));
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
        } else if (groupPosition == 2) {//选择群组
            image_group.setVisibility(View.VISIBLE);
            image_group.setImageResource(R.drawable.default_chatroom);
            ll_group.setBackgroundColor(Color.parseColor("#FFFFFF"));
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
        } else {
            grouprl.setVisibility(View.GONE);
            deptname.setCompoundDrawables(null, null, null, null);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
        }
        if (StringUtil.isNullOrEmpty(deptBean.getDeptName())) {
            deptname.setText("");
        } else {
            deptname.setText(deptBean.getDeptName());// 设置大组成员名称
        }
        android.util.Log.e("show", deptBean.toString());
        return convertView;
    }


    // 得到大组成员的id
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 得到大组成员名称
    public Object getGroupName(int groupPosition) {
        return dept.get(groupPosition).getDeptName();
    }

    // 得到大组成员名称
    public Object getGroupID(int groupPosition) {
        return dept.get(groupPosition).getDeptId();
    }

    // 得到大组成员总数
    public int getGroupCount() {
        return dept.size();
    }

    // 得到小组成员的view 联系人
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        DeptInfo deptBean = dept.get(groupPosition);
        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ChildViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.fragment_contacter_child, null);

            viewHolder.tvchildName = (TextView) convertView
                    .findViewById(R.id.fc_ct_name);// 显示用户名
            viewHolder.view_top = convertView.findViewById(R.id.view_top);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.fc_ct_photo);//
            viewHolder.child_view_btom = convertView.findViewById(R.id.child_view_btom);//
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        if (StringUtils.isNotBlank(deptBean.getFriendInfo(childPosition).getFriendimage())) {
            String url = ChatPacketHelper.buildImageRequestURL(deptBean
                    .getFriendInfo(childPosition).getFriendimage(), ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
            ImageLoaderHelper.displayImage(url, viewHolder.photo);
        } else {
            viewHolder.photo.setImageBitmap(default_user_pic);
        }
        if (childPosition == 0) {
            viewHolder.view_top.setVisibility(View.GONE);

        } else {
            viewHolder.view_top.setVisibility(View.VISIBLE);

        }

        if (childPosition!=deptBean.getDeptFriendInfoList().size()-1){
            viewHolder.child_view_btom.setVisibility(View.GONE);
        }else{
            viewHolder.child_view_btom.setVisibility(View.VISIBLE);
        }

        viewHolder.tvchildName.setText(deptBean.getFriendInfo(childPosition).getFriendname());
        deptname.setText(deptBean.getFriendInfo(childPosition).getDeptname());
        return convertView;
    }

    final static class ChildViewHolder {
        TextView tvchildName;
        ImageView photo;
        View view_top;
        View child_view_btom;
    }

    // 得到小组成员id
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 得到小组成员的名称
    public Object getChild(int groupPosition, int childPosition) {
        return dept.get(groupPosition).getFriendInfo(childPosition);
    }

    // 得到小组成员的数量
    public int getChildrenCount(int groupPosition) {
        return dept.get(groupPosition).getChildCount();

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
     * @param group 传递进来的新数据
     */
    public void updata(List<DeptInfo> group) {
        this.dept = null;
        this.dept = group;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return dept.get(groupPosition);
    }
}

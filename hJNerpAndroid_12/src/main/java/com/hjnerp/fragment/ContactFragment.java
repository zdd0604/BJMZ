package com.hjnerp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.hjnerp.activity.contact.AllVerfifyFriendsActivity;
import com.hjnerp.activity.contact.FriendsActivity;
import com.hjnerp.activity.contact.SearchQiXinFriendsActivity;
import com.hjnerp.activity.im.ShowGroupActivity;
import com.hjnerp.adapter.ContactExpandableListAdapter;
import com.hjnerp.business.ContactBusiness;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.DeptInfo;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerpandroid.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 联系人界面
 */
public class ContactFragment extends Fragment {
    private String TAG = "ContactFragment";

    private List<DeptInfo> dept = new ArrayList<DeptInfo>();// 传递过来的经过处理的总数据

    private ExpandableListView exListView = null;
    private ContactExpandableListAdapter adapter;
    public static int ALL_NEW_MSG_COUNTS = 0;// 所有的新消息数量总和，Actionbar 通讯录tab红圈显示
    //	private ImageView addTextView;
//	private ImageView refresh;
    public static ContactFragment contactFragment = null;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            getData();
            refreshList(dept);
            keepExpand(exListView);
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_contacter,
                container, false);
//		addTextView = (ImageView) contextView
//				.findViewById(R.id.fragment_contact_add);
        exListView = (ExpandableListView) contextView
                .findViewById(R.id.qq_list);
        contactFragment = this;
//		refresh = (ImageView) contextView
//				.findViewById(R.id.fragment_contact_refresh);
        getData();
        adapter = new ContactExpandableListAdapter(this.getActivity(), dept);
        exListView.setAdapter(adapter);
        exListView.setGroupIndicator(null);

        keepExpand(exListView);

        exListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), FriendsActivity.class);
                // intent.setClass(getActivity(), ContactActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(
                        Constant.FRIEND_READ,
                        (Serializable) dept.get(groupPosition).getFriendInfo(
                                childPosition));
                intent.putExtras(mBundle);

                startActivity(intent);
                return false;
            }
        });

        exListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        exListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Log.i(TAG, "OnGroupClickListener() groupposition is "
                        + groupPosition);

                if (groupPosition == 0) {
                    Intent intent1 = new Intent(getActivity(),
                            SearchQiXinFriendsActivity.class);
                    startActivity(intent1);
                } else if (groupPosition == 1) {
                    /**
                     * @author haijian 将消息数置为0 刷新界面
                     */
                    ALL_NEW_MSG_COUNTS = 0;
                    refreshList();
                    Intent intent2 = new Intent(getActivity(),
                            AllVerfifyFriendsActivity.class);
                    startActivity(intent2);

                } else if (groupPosition == 2) {
                    Intent intent3 = new Intent(getActivity(),
                            ShowGroupActivity.class);
                    startActivity(intent3);
                }

                // return true to keep expand
                return true;
            }
        });
        exListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.e(TAG, "OnGroupCollapseListener()");
                // ViewGroup.LayoutParams params = exListView.getLayoutParams();
                // Log.e(TAG,">>>>> setOnGroupCollapseListener is " + height);
                // height = height - dept.get(groupPosition).getChildCount()
                // * GROUP_HEIGHT - 2;
                // params.height = height;
                // Log.e(TAG,"setOnGroupCollapseListener is >>>> " + height);
                // exListView.setLayoutParams(params);
            }
        });
//		addTextView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				onAddClick();
//			}
//
//		});

//		refresh.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				getHttpData();
//			}
//		});
        return contextView;

    }

    public void onAddClick() {
        Intent intent = new Intent(this.getActivity(),
                SearchQiXinFriendsActivity.class);
        startActivity(intent);
    }

    // 初始化数据
    public void getData() {
        dept = new ArrayList<DeptInfo>();
        List<DeptInfo> mdept = ContactBusiness.getDepInfos();

        DeptInfo addfriend = new DeptInfo();
        addfriend.setDeptName("添加联系人");
        addfriend.setDeptChild(null);
        dept.add(addfriend);
        addfriend = new DeptInfo();
        addfriend.setDeptName("联系人申请");
        addfriend.setDeptChild(null);
        dept.add(addfriend);

        addfriend = new DeptInfo();
        addfriend.setDeptName("选择群组");
        addfriend.setDeptChild(null);
        dept.add(addfriend);

        if (mdept != null) {
            for (int i = 0; i < mdept.size(); i++) {
                dept.add(mdept.get(i));
            }
        }
    }

    @Override
    public void onResume() {
        getData();
        refreshList(dept);
        keepExpand(exListView);
        getHttpData();
        super.onResume();
    }

    protected void refreshList(List<DeptInfo> dept) {
        adapter.refreshList(dept);
    }

    public void refreshList() {
        if (adapter == null)
            return;
        getData();
        adapter.refreshList(dept);
    }

    // expand全部展开
    private void keepExpand(ExpandableListView mexpand) {

        if (dept.size() != 0) {
            for (int i = 0; i < dept.size(); i++) {
                mexpand.expandGroup(i);
            }
        }
    }

    /**
     * 联网获取数据
     */
    public void getHttpData() {
        new Thread() {
            public void run() {
                // 取联系人
                IQ iqs = HJWebSocketManager.getInstance().requestIQ(
                        ChatConstants.iq.FEATURE_REQUEST_CONTACT);
                if (iqs != null) {
                    if (ChatConstants.iq.TYPE_SUCCESS.equals(iqs.type)) {
                        Map<String, Object> dataMap = (Map<String, Object>) iqs.data;
                        List<Map<String, String>> items = (List<Map<String, String>>) dataMap
                                .get(ChatConstants.iq.DATA_KEY_ITEMS);
                        ArrayList<FriendInfo> list = new ArrayList<FriendInfo>();
                        for (Map<String, String> item : items) {
                            list.add(ChatPacketHelper.createFriendInfo(item));
                        }
                        // 清除联系人
                        QiXinBaseDao.replaceFriendInfos(list, 'Y');
                    }
                }
                // /群组信息
                IQ iqgroupinfo = HJWebSocketManager.getInstance().requestIQ(
                        ChatConstants.iq.FEATURE_REQUEST_GROUP_INFO);
                if (iqgroupinfo != null) {
                    if (ChatConstants.iq.TYPE_SUCCESS.equals(iqgroupinfo.type)) {

                        Map<String, Object> dataMap = (Map<String, Object>) iqgroupinfo.data;
                        Object obj = dataMap
                                .get(ChatConstants.iq.DATA_KEY_ITEMS);

                        if (obj instanceof List) {
                            List<Map<String, String>> items = (List<Map<String, String>>) obj;
                            ArrayList<GroupInfo> list = new ArrayList<GroupInfo>();
                            for (Map<String, String> item : items)
                                list.add(ChatPacketHelper.createGroupInfo(item));
                            QiXinBaseDao.replaceGroupInfos(list);
                        }
                    }
                }
                // 群组成员信息
                IQ iqgrouprelation = HJWebSocketManager
                        .getInstance()
                        .requestIQ(
                                ChatConstants.iq.FEATURE_REQUEST_GROUP_RELATION);
                if (iqgrouprelation != null) {
                    if (ChatConstants.iq.TYPE_SUCCESS
                            .equals(iqgrouprelation.type)) {
                        Map<String, Object> dataMap = (Map<String, Object>) iqgrouprelation.data;
                        Object obj = dataMap
                                .get(ChatConstants.iq.DATA_KEY_ITEMS);
                        if (obj instanceof List) {
                            List<Map<String, String>> items = (List<Map<String, String>>) obj;
                            QiXinBaseDao.insertGroupRelations(items);
                        }
                    }

                }

                IQ iqgrouprelations = HJWebSocketManager
                        .getInstance()
                        .requestIQ(
                                ChatConstants.iq.FEATURE_REQUEST_GROUP_RELATION);
                /**
                 * 修改原来是
                 * iqgrouprelation
                 * 修改为
                 * iqgrouprelations
                 */
                if (iqgrouprelations != null) {
                    Map<String, Object> dataMap = (Map<String, Object>) iqgrouprelations.data;
                    Object obj = dataMap.get(ChatConstants.iq.DATA_KEY_ITEMS);
                    if (obj instanceof List) {
                        List<Map<String, String>> items = (List<Map<String, String>>) obj;
                        QiXinBaseDao.insertGroupRelations(items);
                    }

                    ArrayList<FriendInfo> mUserList = ChatPacketHelper
                            .processUnFriendsUsersInGroup(iqgrouprelations);
                    QiXinBaseDao.replaceFriendInfos(mUserList, 'N');

                }
                handler.sendEmptyMessage(0);
            }

            ;
        }.start();

    }
}

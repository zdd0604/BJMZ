package com.mznerp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.activity.im.ChatActivity;
import com.mznerp.activity.im.NoticeActivity;
import com.mznerp.adapter.ImListAdapter;
import com.mznerp.common.Constant;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.ChatHisBean;
import com.mznerp.model.FriendInfo;
import com.mznerp.model.GroupInfo;
import com.mznerp.model.UserInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ImFragment extends Fragment {
    String TAG = "ImFragment";

    private ArrayList<ChatHisBean> chatHisBeanList = new ArrayList<ChatHisBean>();
    private Dialog noticeDialog;
    private ImListAdapter listItemAdapter = null;
    public SharePreferenceUtil sputil;
    private int longClickPosition;
    private OnImListener mListener;
    //    private ImageView addTextView;
//    private ImageView callFragment;
//    private ImageView addFriend;
    private UserInfo myInfo;
    public static int ALL_NEW_MSG_COUNTS = 0;// 所有的新消息数量总和，Actionbar 企信tab红圈显示
//    private ImageView myImageView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.fragment_im, container,
                false);
        myInfo = QiXinBaseDao.queryCurrentUserInfo();
        sputil = SharePreferenceUtil.getInstance(getActivity());
        ListView listview = (ListView) contextView.findViewById(R.id.listview);

//        addTextView = (ImageView) contextView.findViewById(R.id.fragment_im_add);
//        callFragment = (ImageView) contextView.findViewById(R.id.fragment_call);
//        addFriend = (ImageView) contextView.findViewById(R.id.fragment_friend_add);

        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new ImListAdapter(this.getActivity(), chatHisBeanList);
//        myImageView = (ImageView) contextView
//                .findViewById(R.id.fragment_im_iamager);
        // 添加并且显示
        listview.setAdapter(listItemAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                ChatHisBean charhis = chatHisBeanList.get(arg2);
                Intent intent = null;
                if (ChatConstants.msg.TYPE_GROUPCHAT.equals(charhis
                        .getMsgType())) {
                    GroupInfo groupInfo = QiXinBaseDao.queryGroupInfo(charhis
                            .getMsgTo());

                    if (groupInfo != null
                            && "chat".equalsIgnoreCase(groupInfo.groupType)) {// 群聊
                        intent = new Intent(getActivity(), ChatActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(Constant.IM_GOUP_NEWS,
                                (Serializable) groupInfo);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    } else if (groupInfo != null
                            && "sys".equalsIgnoreCase(groupInfo.groupType)) {// 公告
                        intent = new Intent(getActivity(), NoticeActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(Constant.IM_GOUP_NEWS,
                                (Serializable) groupInfo);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                } else if (ChatConstants.msg.TYPE_CHAT.equals(charhis
                        .getMsgType())) {// 单聊

                    FriendInfo friendinfo = new FriendInfo();

                    if (charhis.getMsgFrom().equals(sputil.getMyId())) {
                        friendinfo = QiXinBaseDao.queryFriendInfo(charhis
                                .getMsgTo());
                    } else {
                        friendinfo = QiXinBaseDao.queryFriendInfo(charhis
                                .getMsgFrom());
                    }
                    if (friendinfo != null) {
                        intent = new Intent(getActivity(), ChatActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(Constant.IM_NEWS,
                                (Serializable) friendinfo);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                } else if (ChatConstants.msg.TYPE_SYS.equals(charhis
                        .getMsgType())) {// 公告
                    GroupInfo groupInfo = QiXinBaseDao.queryGroupInfo(charhis
                            .getMsgTo());

                    if (groupInfo != null) {
                        intent = new Intent(getActivity(), NoticeActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(Constant.IM_GOUP_NEWS,
                                (Serializable) groupInfo);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                }
            }

        });

        // 长按弹出对话框删除item
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                longClickPosition = arg2;
                showNoticeDialog("操作", "确定删除");
                return true;
            }
        });

//        addTextView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                onAddClick();
//            }
//
//        });
//        callFragment.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                if (isAvilible("com.google.android.contacts")) {
//                    intent.setClassName("com.google.android.contacts",
//                            "com.android.contacts.activities.PeopleActivity");
//                } else {
//                    intent.setClassName("com.android.contacts",
//                            "com.android.contacts.activities.PeopleActivity");
//                }
//
//                startActivity(intent);
//            }
//
//        });
//        addFriend.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),
//                        SearchQiXinFriendsActivity.class);
//                startActivity(intent);
//            }
//        });


//        UserInfo myinfo = QiXinBaseDao.queryCurrentUserInfo();

//        if (myinfo != null) {
//            String url = myinfo.userImage;
//            if (!StringUtil.isNullOrEmpty(url)) {
//                ImageLoaderHelper.displayImage(ChatPacketHelper
//                                .buildImageRequestURL(url,
//                                        ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),
//                        myImageView);
//            }
//        }
//        myImageView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onImReflash("LEFT");
//            }
//        });
        refreshMessage();
        return contextView;
    }


//    public void onAddClick() {
//        Intent intent = new Intent(this.getActivity(),
//                SelectGroupChatMemberDeptActivity.class);
//        startActivity(intent);
//    }

    public interface OnImListener {
        public void onImReflash(String msg);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnImListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        UserInfo myinfo = QiXinBaseDao.queryCurrentUserInfo();
//
//        if (myinfo != null && myImageView != null) {
//            String url = myinfo.userImage;
//            if (!StringUtil.isNullOrEmpty(url)) {
//                ImageLoaderHelper.displayImage(ChatPacketHelper
//                                .buildImageRequestURL(url,
//                                        ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),
//                        myImageView);
//            }
//        }
    }

    ;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // ///取得聊天消息列表
    private ArrayList<ChatHisBean> getChartList() {
        // TODO
        ALL_NEW_MSG_COUNTS = 0;
        ArrayList<ChatHisBean> baseCBeanList = QiXinBaseDao
                .queryLastChatHisBeans();
        if (myInfo == null) return new ArrayList<ChatHisBean>();
        //myInfo = QiXinBaseDao.queryCurrentUserInfo();
        if (myInfo == null)
            return null;
        for (int i = 0; i < baseCBeanList.size(); i++) {
            int newmsgcounts = 0;
            if (ChatConstants.msg.TYPE_CHAT.equals(baseCBeanList.get(i)
                    .getMsgType())) {// 单聊，设置此条单聊新消息数量
                String userIdTemp = baseCBeanList.get(i).getMsgFrom();
                if (userIdTemp.equalsIgnoreCase(myInfo.userID)) {
                    userIdTemp = baseCBeanList.get(i).getMsgTo();
                }
                newmsgcounts = QiXinBaseDao.querySingleChatNewChatMsgCounts(
                        myInfo.userID, userIdTemp);
                baseCBeanList.get(i).setMsgSum(newmsgcounts);
                ALL_NEW_MSG_COUNTS = ALL_NEW_MSG_COUNTS + newmsgcounts;

            }
            if (ChatConstants.msg.TYPE_GROUPCHAT.equals(baseCBeanList.get(i)
                    .getMsgType())) {// 群聊，设置此条群聊新消息数量
                newmsgcounts = QiXinBaseDao
                        .queryGroupChatNewChatMsgCounts(baseCBeanList.get(i)
                                .getMsgTo());
                baseCBeanList.get(i).setMsgSum(newmsgcounts);
                ALL_NEW_MSG_COUNTS = ALL_NEW_MSG_COUNTS + newmsgcounts;
            }
        }
        if (ALL_NEW_MSG_COUNTS == 0) {
            mListener.onImReflash("");
        } else {
            mListener.onImReflash(String.valueOf(ALL_NEW_MSG_COUNTS));
        }

        return baseCBeanList;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            noticeDialog.dismiss();
            switch (v.getId()) {
                // TODO 刪除一組單聊或者一組群聊記錄
                case R.id.dialog_simple__nc_confirm_rl:
                    ChatHisBean mChatHisBean = chatHisBeanList
                            .get(longClickPosition);
                    if (ChatConstants.msg.TYPE_GROUPCHAT.equals(mChatHisBean
                            .getMsgType())) {
                        String groupId = chatHisBeanList.get(longClickPosition)
                                .getMsgTo();
                        QiXinBaseDao.deleteGroupMsgById(groupId);
                        // removeGroupChatPaoPao(groupId);
                        chatHisBeanList = getChartList();
                        refreshMessage(chatHisBeanList);

                    } else if (ChatConstants.msg.TYPE_CHAT.equals(mChatHisBean
                            .getMsgType())) {
                        String friendId;
                        if (mChatHisBean.getMsgFrom().equals(sputil.getMyId())) {
                            friendId = mChatHisBean.getMsgTo();
                        } else {
                            friendId = mChatHisBean.getMsgFrom();
                        }
                        QiXinBaseDao
                                .deleteSingleMsgById(sputil.getMyId(), friendId);
                        chatHisBeanList = getChartList();
                        refreshMessage(chatHisBeanList);

                    }

                    break;

                default:
                    break;

            }
        }
    };

    public void refreshMessage(ArrayList<ChatHisBean> messages) {
        if (listItemAdapter == null)
            return;
        listItemAdapter.refreshList(messages);
    }

    public void refreshMessage() {
        chatHisBeanList = getChartList();
        if (chatHisBeanList != null) {
            refreshMessage(chatHisBeanList);
        }
    }

    /* 提示用户是否删除 */
    private void showNoticeDialog(String title, String confirmtv) {
        noticeDialog = new Dialog(getActivity(), R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_simple);

        RelativeLayout dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog
                .findViewById(R.id.dialog_simple_title);
        notice.setText(title);
        TextView confirm = (TextView) noticeDialog
                .findViewById(R.id.dialog_simple_confirm_tv);
        confirm.setText(confirmtv);
        dialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_simple__nc_confirm_rl);
        dialog_confirm_rl.setOnClickListener(onClickListener);

        noticeDialog.show();
    }

}
package com.hjnerp.activity.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hjnerp.activity.contact.FriendsActivity;
import com.hjnerp.activity.im.ChatActivity;
import com.hjnerp.activity.im.LocationActivity;
import com.hjnerp.activity.im.ShowChatPictureActivity;
import com.hjnerp.activity.im.reSendMsg;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.AttachFileProcessor;
import com.hjnerp.util.AttachFileProcessor.OnProcessResultListener;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.myscom.FileUtils;
import com.hjnerp.util.myscom.StringUtils;
import com.hjnerpandroid.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@SuppressLint("NewApi")
public class ChatListTypeAdapter extends BaseListAdapter<ChatHisBean> {
    private Context context;

    private SharePreferenceUtil sputil;
    private String myid;
    private FriendInfo myinfo;
    private FriendInfo friendinfo;
    private LayoutInflater mInflater;

    public ChatListTypeAdapter(Context context, List<ChatHisBean> list) {

        super(context, list);
        this.context = context;
        sputil = SharePreferenceUtil.getInstance(context);
        myid = sputil.getMyId();
        myinfo = QiXinBaseDao.queryFriendInfo(myid);
        if (myinfo == null) {
            myinfo = new FriendInfo();
        }
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshList(List<ChatHisBean> listbean) {
        if (listbean.size() == 0)
            return;
        this.list = listbean;
        this.notifyDataSetChanged();

    }

    public void pullRrefreshList(List<ChatHisBean> listbean) {
        if (listbean.size() == 0)
            return;
        this.list = listbean;
        this.notifyDataSetChanged();
        // adapterList.getRefreshableView().setSelection(0);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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

    @Override
    public int getItemViewType(int position) {

        ChatHisBean msg = list.get(position);
        int type = msg.getType();
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 9;
    }

    @SuppressLint("InflateParams")
    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {

        final ChatHisBean msg = list.get(position);
        int type = getItemViewType(position);
        ViewHolderNotice holderNotice = null;
        ViewHolderRightText holderRightText = null;
        ViewHolderRightImg holderRightImg = null;
        ViewHolderRightAudio holderRightAudio = null;
        ViewHolderLeftText holderLeftText = null;
        ViewHolderLeftImg holderLeftImg = null;
        ViewHolderLeftAudio holderLeftAudio = null;
        ViewHolderLeftLocation holderLeftLocation = null;
        ViewHolderRightLocation holderRightLocation = null;
        ViewHolderLeftFile holderLeftFile = null;
        ViewHolderRightFile holderRightFile = null;

        if (convertView == null) {
            switch (type) {
                case ChatConstants.VALUE_NOTICE:
                    holderNotice = new ViewHolderNotice();
                    convertView = mInflater.inflate(R.layout.chat_notice, null);
                    holderNotice.tvNotice = (TextView) convertView
                            .findViewById(R.id.chat_sendtitle);
                    holderNotice.tvTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_notice);
                    convertView.setTag(holderNotice);
                    break;
                // TODO 左边
                case ChatConstants.VALUE_LEFT_TEXT:
                    holderLeftText = new ViewHolderLeftText();
                    convertView = mInflater.inflate(R.layout.chat_left_text, null);
                    holderLeftText.ivLeftIcon = (ImageView) convertView
                            .findViewById(R.id.chat_left_item_headphoto_lefttext);
                    holderLeftText.tvLeftText = (TextView) convertView
                            .findViewById(R.id.chat_left_item_content_lefttext);
                    holderLeftText.tvLeftTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_lefttext);
                    holderLeftText.tvLeftName = (TextView) convertView
                            .findViewById(R.id.chat_Left_item_text_name);
                    convertView.setTag(holderLeftText);
                    break;

                case ChatConstants.VALUE_LEFT_IMAGE:
                    // Log.e(TAG,"convertView == null case ChatConstants.VALUE_LEFT_IMAGE");
                    holderLeftImg = new ViewHolderLeftImg();
                    convertView = mInflater.inflate(R.layout.chat_left_pic, null);
                    holderLeftImg.ivLeftIcon = (ImageView) convertView
                            .findViewById(R.id.chat_left_item_headphoto_leftpic);
                    holderLeftImg.ivLeftImage = (ImageView) convertView
                            .findViewById(R.id.chat_left_item_pic_leftpic);
                    holderLeftImg.tvLeftTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_leftpic);
                    holderLeftImg.pbLeftProgressBar = (ProgressBar) convertView
                            .findViewById(R.id.chat_left_item_pb_rightpic);
                    holderLeftImg.tvLeftName = (TextView) convertView
                            .findViewById(R.id.chat_Left_item_pic_name);
                    convertView.setTag(holderLeftImg);
                    break;

                case ChatConstants.VALUE_LEFT_AUDIO:
                    // Log.e(TAG,"convertView == null case ChatConstants.VALUE_LEFT_AUDIO");
                    holderLeftAudio = new ViewHolderLeftAudio();
                    convertView = mInflater.inflate(R.layout.chat_left_audio, null);
                    holderLeftAudio.ivLeftIcon = (ImageView) convertView
                            .findViewById(R.id.chat_left_item_headphoto_leftaudio);
                    holderLeftAudio.tvLeftTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_leftaudio);
                    holderLeftAudio.ivLeftWaitting = (ProgressBar) convertView
                            .findViewById(R.id.chat_left_item_pb_leftaudio);
                    holderLeftAudio.ivLeftAudioSendFail = (ImageView) convertView
                            .findViewById(R.id.chat_left_item_pic_sendfail_leftaudio);
                    holderLeftAudio.tvLeftAudioTimeLong = (TextView) convertView
                            .findViewById(R.id.tv_left_voice_long);
                    holderLeftAudio.tvLeftName = (TextView) convertView
                            .findViewById(R.id.chat_Left_item_audio_name);
                    convertView.setTag(holderLeftAudio);
                    break;
                // 右边
                case ChatConstants.VALUE_RIGHT_TEXT:
                    // Log.e(TAG,"convertView == null case ChatConstants.VALUE_RIGHT_TEXT");
                    holderRightText = new ViewHolderRightText();
                    convertView = mInflater.inflate(R.layout.chat_right_text, null);
                    holderRightText.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_headphoto_righttext);

                    holderRightText.tvRightTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_righttext);
                    holderRightText.ivRightTextSendFail = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_text_sendfail_righttext);
                    holderRightText.pbRightTextProgressBar = (ProgressBar) convertView
                            .findViewById(R.id.chat_right_item_pb_righttext);
                    holderRightText.tvRightText = (TextView) convertView
                            .findViewById(R.id.chat_right_item_content_righttext);
                    convertView.setTag(holderRightText);
                    break;

                case ChatConstants.VALUE_RIGHT_IMAGE:
                    holderRightImg = new ViewHolderRightImg();
                    convertView = mInflater.inflate(R.layout.chat_right_pic, null);
                    convertView.setLayoutParams(new AbsListView.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    holderRightImg.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_headphoto_rightpic);
                    holderRightImg.ivRightImage = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_pic_rightpic);
                    holderRightImg.tvRightTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_righttext_rightpic);
                    holderRightImg.ivRightImageSendFail = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_pic_sendfail_rightpic);
                    holderRightImg.pbRightProgressBar = (ProgressBar) convertView
                            .findViewById(R.id.chat_right_item_pb_rightpic);
                    convertView.setTag(holderRightImg);
                    break;

                case ChatConstants.VALUE_RIGHT_AUDIO:
                    // Log.e(TAG,"convertView == null case ChatConstants.VALUE_RIGHT_AUDIO");
                    holderRightAudio = new ViewHolderRightAudio();
                    convertView = mInflater
                            .inflate(R.layout.chat_right_audio, null);
                    holderRightAudio.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_headphoto_rightaudio);
                    holderRightAudio.ivRightAudio = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_pic_rightaudio);
                    holderRightAudio.ivRightAudioSendFail = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_pic_sendfail_rightaudio);
                    holderRightAudio.tvRightAudioTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_righttext_rightaudio);
                    holderRightAudio.ivRightWaitting = (ProgressBar) convertView
                            .findViewById(R.id.chat_right_item_pb_rightaudio);
                    holderRightAudio.tvRightAudioTimeLong = (TextView) convertView
                            .findViewById(R.id.tv_right_voice_long);
                    convertView.setTag(holderRightAudio);
                    break;
                case ChatConstants.VALUE_LEFT_LOCATION:// 左侧位置
                    holderLeftLocation = new ViewHolderLeftLocation();
                    convertView = mInflater.inflate(R.layout.chat_left_location,
                            null);
                    convertView.setLayoutParams(new AbsListView.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    holderLeftLocation.ivLeftIcon = (ImageView) convertView
                            .findViewById(R.id.chat_left_item_headphoto_leftpic);
                    holderLeftLocation.ivLeftLocationPic = (ImageView) convertView
                            .findViewById(R.id.chat_left_item_pic_leftpic);
                    holderLeftLocation.ivLeftWaitting = (ProgressBar) convertView
                            .findViewById(R.id.chat_left_item_pb_rightpic);
                    holderLeftLocation.tvLeftTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_leftpic);
                    holderLeftLocation.tvLeftName = (TextView) convertView
                            .findViewById(R.id.chat_Left_item_pic_name);
                    holderLeftLocation.tvLeftLocation = (TextView) convertView
                            .findViewById(R.id.tv_location);
                    convertView.setTag(holderLeftLocation);
                    break;
                case ChatConstants.VALUE_RIGHT_LOCATION:// 右侧位置
                    holderRightLocation = new ViewHolderRightLocation();
                    convertView = mInflater.inflate(R.layout.chat_right_location,
                            null);
                    convertView.setLayoutParams(new AbsListView.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    holderRightLocation.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_headphoto_rightpic);
                    holderRightLocation.ivRightLocationPic = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_pic_rightpic);
                    holderRightLocation.ivRightLocationSendFail = (ImageView) convertView
                            .findViewById(R.id.chat_right_item_pic_sendfail_rightpic);
                    holderRightLocation.ivRightWaitting = (ProgressBar) convertView
                            .findViewById(R.id.chat_right_item_pb_rightpic);
                    holderRightLocation.tvRightTime = (TextView) convertView
                            .findViewById(R.id.chat_sendtime_righttext_rightpic);
                    holderRightLocation.tvRightLocation = (TextView) convertView
                            .findViewById(R.id.adress);
                    convertView.setTag(holderRightLocation);
                    break;
                default:
                    break;
            }

        } else {
            // Log.d("baseAdapter", "Adapter_:" + (convertView == null));
            switch (type) {
                case ChatConstants.VALUE_NOTICE:
                    holderNotice = (ViewHolderNotice) convertView.getTag();
                    break;
                case ChatConstants.VALUE_LEFT_TEXT:
                    holderLeftText = (ViewHolderLeftText) convertView.getTag();
                    break;
                case ChatConstants.VALUE_LEFT_IMAGE:
                    holderLeftImg = (ViewHolderLeftImg) convertView.getTag();
                    break;
                case ChatConstants.VALUE_LEFT_AUDIO:
                    holderLeftAudio = (ViewHolderLeftAudio) convertView.getTag();
                    break;
                case ChatConstants.VALUE_RIGHT_TEXT:
                    holderRightText = (ViewHolderRightText) convertView.getTag();
                    break;
                case ChatConstants.VALUE_RIGHT_IMAGE:
                    holderRightImg = (ViewHolderRightImg) convertView.getTag();
                    break;
                case ChatConstants.VALUE_RIGHT_AUDIO:
                    holderRightAudio = (ViewHolderRightAudio) convertView.getTag();
                    break;
                case ChatConstants.VALUE_LEFT_LOCATION:// 左侧位置
                    holderLeftLocation = (ViewHolderLeftLocation) convertView
                            .getTag();
                    break;
                case ChatConstants.VALUE_RIGHT_LOCATION:// 右侧位置
                    holderRightLocation = (ViewHolderRightLocation) convertView
                            .getTag();
                    break;
                default:
                    break;
            }

        }

        switch (type) {
            case ChatConstants.VALUE_NOTICE:
                // holderNotice.tvNotice.setTag(msg.getMsgcontent());
                holderNotice.tvNotice.setText(msg.getMsgcontent());
                holderNotice.tvTime.setText(DateUtil.msgToHumanReadableTime(msg
                        .getMsgTime()));
                if (msg.isShowTime()) {
                    holderNotice.tvTime.setVisibility(View.VISIBLE);
                } else {
                    holderNotice.tvTime.setVisibility(View.GONE);
                }
                break;
            case ChatConstants.VALUE_LEFT_TEXT:

                friendinfo = QiXinBaseDao.queryFriendInfoall(msg.getMsgFrom());// ((ChatActivity)
                // context).getFriendInfo(msg
                // .getMsgFrom());

                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderLeftText.tvLeftTime.setText(DateUtil.msgToHumanReadableTime(msg.getMsgTime()));
                if (msg.isShowTime()) {
                    holderLeftText.tvLeftTime.setVisibility(View.VISIBLE);
                } else {
                    holderLeftText.tvLeftTime.setVisibility(View.GONE);
                }

                if (StringUtil.isStrTrue(friendinfo.getFriendname()))
                    holderLeftText.tvLeftName.setText(friendinfo.getFriendname());

                if (ChatConstants.msg.TYPE_GROUPCHAT.equalsIgnoreCase(msg
                        .getMsgType())) {
                    holderLeftText.tvLeftName.setVisibility(View.VISIBLE);
                } else {
                    holderLeftText.tvLeftName.setVisibility(View.GONE);
                }
                holderLeftText.ivLeftIcon.setTag(friendinfo);
                /**
                 * @author haijian 如果头像为空，并且是复用的item这个时候就会导致头像错位 修改方法加一个else
                 */
                holderLeftText.ivLeftIcon
                        .setImageResource(R.drawable.v5_0_1_profile_headphoto);
                if (!StringUtils.isBlank(friendinfo.getFriendimage().trim())
                        && (FriendInfo) holderLeftText.ivLeftIcon.getTag() != null
                        && (FriendInfo) holderLeftText.ivLeftIcon.getTag() == friendinfo) {
                    String url = ChatPacketHelper.buildImageRequestURL(
                            friendinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper.displayImage(url, holderLeftText.ivLeftIcon);
                }

                holderLeftText.ivLeftIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendInfo friendInfoTemp = (FriendInfo) v.getTag();
                        if (friendInfoTemp == null) {
                            return;
                        }
                        FriendInfoBrower(friendInfoTemp);
                    }
                });
                if (StringUtil.isStrTrue(msg.getMsgcontent()))
                    holderLeftText.tvLeftText.setText(msg.getMsgcontent());
                break;
            case ChatConstants.VALUE_LEFT_IMAGE:

                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderLeftImg.tvLeftTime.setText(DateUtil.msgToHumanReadableTime(msg.getMsgTime()));
                // 设置时间
                if (msg.isShowTime()) {
                    holderLeftImg.tvLeftTime.setVisibility(View.VISIBLE);
                } else {
                    holderLeftImg.tvLeftTime.setVisibility(View.GONE);
                }
                // 设置头像
                friendinfo = QiXinBaseDao.queryFriendInfoall(msg.getMsgFrom());// ((ChatActivity)
                // context).getFriendInfo(msg
                // .getMsgFrom());
                if (StringUtil.isStrTrue(friendinfo.getFriendname()))
                    holderLeftImg.tvLeftName.setText(friendinfo.getFriendname());
                if (ChatConstants.msg.TYPE_GROUPCHAT.equalsIgnoreCase(msg
                        .getMsgType())) {
                    holderLeftImg.tvLeftName.setVisibility(View.VISIBLE);
                } else {
                    holderLeftImg.tvLeftName.setVisibility(View.GONE);
                }

                holderLeftImg.ivLeftIcon.setTag(friendinfo);
                holderLeftImg.ivLeftIcon
                        .setImageResource(R.drawable.v5_0_1_profile_headphoto);
                if (!StringUtils.isBlank(friendinfo.getFriendimage().trim())
                        && (FriendInfo) holderLeftImg.ivLeftIcon.getTag() != null
                        && (FriendInfo) holderLeftImg.ivLeftIcon.getTag() == friendinfo) {

                    String url = ChatPacketHelper.buildImageRequestURL(
                            friendinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper.displayImage(url, holderLeftImg.ivLeftIcon);
                }
                holderLeftImg.ivLeftIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendInfo friendInfoTemp = (FriendInfo) v.getTag();
                        if (friendInfoTemp == null) {
                            return;
                        }
                        FriendInfoBrower(friendInfoTemp);
                    }
                });
                holderLeftImg.ivLeftImage.setTag(msg);
                /**
                 * @author haijian 设置默认图片
                 */
                holderLeftImg.ivLeftImage
                        .setImageResource(R.drawable.voicesearch_btnbg_pressed);
                holderLeftImg.pbLeftProgressBar.setVisibility(View.VISIBLE);
                // 设置图片
                if (StringUtils.isNotBlank(msg.getmsgIdFile())
                        && (ChatHisBean) holderLeftImg.ivLeftImage.getTag() != null
                        && (ChatHisBean) holderLeftImg.ivLeftImage.getTag() == msg) {
                    String url = ChatPacketHelper.buildImageRequestURL(
                            msg.getmsgIdFile(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
                    ImageLoaderHelper.displayImage(url, holderLeftImg.ivLeftImage,
                            holderLeftImg.pbLeftProgressBar);
                }
                holderLeftImg.ivLeftImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatHisBean messageTemp = (ChatHisBean) v.getTag();
                        if (!TextUtils.isEmpty(messageTemp.getmsgIdFile())
                                && Constant.FILE_TYPE_PIC
                                .equalsIgnoreCase(messageTemp
                                        .getmsgTypeFile())) {
                            imageBrower(messageTemp);
                        }
                    }
                });
                break;
            // 对方发送的语音
            case ChatConstants.VALUE_LEFT_AUDIO:
                friendinfo = QiXinBaseDao.queryFriendInfoall(msg.getMsgFrom());// ((ChatActivity)
                // context).getFriendInfo(msg
                // .getMsgFrom());
                holderLeftAudio.ivLeftIcon.setTag(friendinfo);
                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderLeftAudio.tvLeftTime.setText(DateUtil
                            .msgToHumanReadableTime(msg.getMsgTime()));

                if (msg.isShowTime()) {
                    holderLeftAudio.tvLeftTime.setVisibility(View.VISIBLE);
                } else {
                    holderLeftAudio.tvLeftTime.setVisibility(View.GONE);
                }

                if (StringUtil.isStrTrue(friendinfo.getFriendname()))
                    holderLeftAudio.tvLeftName.setText(friendinfo.getFriendname());
                if (ChatConstants.msg.TYPE_GROUPCHAT.equalsIgnoreCase(msg
                        .getMsgType())) {
                    holderLeftAudio.tvLeftName.setVisibility(View.VISIBLE);
                } else {
                    holderLeftAudio.tvLeftName.setVisibility(View.GONE);
                }

                holderLeftAudio.ivLeftIcon
                        .setImageResource(R.drawable.v5_0_1_profile_headphoto);
                if (!StringUtils.isBlank(friendinfo.getFriendimage())
                        && holderLeftAudio.ivLeftIcon.getTag() != null
                        && holderLeftAudio.ivLeftIcon.getTag() == friendinfo) {
                    String url = ChatPacketHelper.buildImageRequestURL(
                            friendinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper.displayImage(url, holderLeftAudio.ivLeftIcon);
                }

                holderLeftAudio.ivLeftIcon
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FriendInfo friendInfoTemp = (FriendInfo) v.getTag();
                                if (friendInfoTemp == null) {
                                    return;
                                }
                                FriendInfoBrower(friendInfoTemp);
                            }
                        });
                // 设置显示时长

                String msgLeftbody = msg.getMsgcontent();
                String msgLeftBody[] = msgLeftbody.split(":");
                holderLeftAudio.tvLeftAudioTimeLong.setTag(msg.getMsgcontent());
                if (msgLeftBody.length > 1) {
                    if (holderLeftAudio.tvLeftAudioTimeLong.getTag().equals(
                            msg.getMsgcontent())) {
                        holderLeftAudio.tvLeftAudioTimeLong.setText(msgLeftBody[1]
                                + "\"");
                    }
                }
                if ("Y".equalsIgnoreCase(msg.getMsgFlagPaly())) {
                    holderLeftAudio.ivLeftAudioSendFail.setVisibility(View.GONE);
                } else {
                    holderLeftAudio.ivLeftAudioSendFail.setVisibility(View.VISIBLE);
                }
                // //开始下载

                if (!FileUtils.isFileExit(Constant.CHATAUDIO_DIR
                        + msg.getmsgIdFile())) {// 文件存在
                    downloadAudioFile(msg, holderLeftAudio.ivLeftWaitting);
                } else {
                    holderLeftAudio.ivLeftWaitting.setVisibility(View.INVISIBLE);
                }

                break;
            case ChatConstants.VALUE_RIGHT_TEXT:

                holderRightText.ivRightIcon.setTag(myinfo);
                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderRightText.tvRightTime.setText(DateUtil
                            .msgToHumanReadableTime(msg.getMsgTime()));
                if (msg.isShowTime()) {
                    holderRightText.tvRightTime.setVisibility(View.VISIBLE);
                } else {
                    holderRightText.tvRightTime.setVisibility(View.GONE);
                }

                if (!StringUtils.isBlank(myinfo.getFriendimage())
                        && holderRightText.ivRightIcon.getTag() != null
                        && holderRightText.ivRightIcon.getTag() == myinfo) {

                    String url = ChatPacketHelper.buildImageRequestURL(
                            myinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper
                            .displayImage(url, holderRightText.ivRightIcon);
                }

                holderRightText.ivRightIcon
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(context, FriendsActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable(Constant.FRIEND_READ,
                                        (Serializable) myinfo);
                                intent.putExtras(mBundle);
                                ((ChatActivity) context).startActivity(intent);

                            }
                        });
                if (StringUtil.isStrTrue(msg.getMsgcontent()))
                    holderRightText.tvRightText.setText(msg.getMsgcontent());

                // 设置发送失败标示
                if (Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(msg
                        .getmsgSendStatus())) {
                    holderRightText.ivRightTextSendFail.setVisibility(View.VISIBLE);
                    holderRightText.pbRightTextProgressBar.setVisibility(View.GONE);
                } else if (Constant.MSG_SEND_STATUS_SUCCESS.equalsIgnoreCase(msg
                        .getmsgSendStatus())) {
                    holderRightText.ivRightTextSendFail.setVisibility(View.GONE);
                    holderRightText.pbRightTextProgressBar.setVisibility(View.GONE);
                } else {
                    holderRightText.ivRightTextSendFail.setVisibility(View.GONE);
                    holderRightText.pbRightTextProgressBar
                            .setVisibility(View.VISIBLE);
                    reSendMsg sendTxtMsg = new reSendMsg(
                            holderRightText.ivRightTextSendFail,
                            holderRightText.pbRightTextProgressBar, msg);
                    sendTxtMsg.execute();
                }

                break;
            case ChatConstants.VALUE_RIGHT_IMAGE:

                holderRightImg.ivRightIcon.setTag(myinfo);
                holderRightImg.ivRightImage.setTag(msg);
                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderRightImg.tvRightTime.setText(DateUtil
                            .msgToHumanReadableTime(msg.getMsgTime()));
                // 设置时间
                if (msg.isShowTime()) {
                    holderRightImg.tvRightTime.setVisibility(View.VISIBLE);
                } else {
                    holderRightImg.tvRightTime.setVisibility(View.GONE);
                }

                // 设置头像
                if (!StringUtils.isBlank(myinfo.getFriendimage())
                        && holderRightImg.ivRightIcon.getTag() != null
                        && holderRightImg.ivRightIcon.getTag() == myinfo) {
                    String url = ChatPacketHelper.buildImageRequestURL(
                            myinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper.displayImage(url, holderRightImg.ivRightIcon);
                }

                // /发送中 发送失败 发送成功
                if (StringUtils.isNotBlank(msg.getmsgIdFile())
                        && Constant.FILE_TYPE_PIC.equalsIgnoreCase(msg
                        .getmsgTypeFile())
                        && holderRightImg.ivRightImage.getTag() == msg) {

                    if (Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(msg
                            .getmsgSendStatus())
                            || Constant.FILE_SEND_STATUS_FILE_ING
                            .equalsIgnoreCase(msg.getmsgSendStatus())) {
                        // 发送中
                        holderRightImg.ivRightImageSendFail
                                .setVisibility(View.GONE);
                        holderRightImg.pbRightProgressBar
                                .setVisibility(View.VISIBLE);
                        reSendMsg sendTxtMsg = new reSendMsg(
                                holderRightImg.ivRightImageSendFail,
                                holderRightImg.pbRightProgressBar, msg);
                        sendTxtMsg.execute();

                    } else if (Constant.FILE_SEND_STATUS_FILE_FAIL
                            .equalsIgnoreCase(msg.getmsgSendStatus())
                            || Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(msg
                            .getmsgSendStatus())) {
                        // 发送失败
                        holderRightImg.pbRightProgressBar.setVisibility(View.GONE);
                        holderRightImg.ivRightImageSendFail
                                .setVisibility(View.VISIBLE);

                    } else {// 发送成功
                        holderRightImg.pbRightProgressBar.setVisibility(View.GONE);
                        holderRightImg.ivRightImageSendFail
                                .setVisibility(View.GONE);
                    }

                    if (holderRightImg.ivRightImage.getTag() != null
                            && holderRightImg.ivRightImage.getTag() == msg) {
                        String url = ChatPacketHelper.buildImageRequestURL(
                                msg.getmsgIdFile(),
                                ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
                        holderRightImg.ivRightImage
                                .setImageResource(R.drawable.voicesearch_btnbg_pressed);
                        ImageLoaderHelper.displayImage(msg.getmsgIdFile(),
                                Constant.CHAT_CACHE_DIR, url,
                                holderRightImg.ivRightImage);
                    }
                }

                holderRightImg.ivRightImage
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChatHisBean messageTemp = (ChatHisBean) v.getTag();
                                if (!TextUtils.isEmpty(messageTemp.getmsgIdFile())
                                        && Constant.FILE_TYPE_PIC
                                        .equalsIgnoreCase(messageTemp
                                                .getmsgTypeFile())) {
                                    imageBrower(messageTemp);
                                }
                            }
                        });

                holderRightImg.ivRightIcon
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(context, FriendsActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable(Constant.FRIEND_READ,
                                        (Serializable) myinfo);
                                intent.putExtras(mBundle);
                                ((ChatActivity) context).startActivity(intent);

                            }
                        });

                break;
            case ChatConstants.VALUE_RIGHT_AUDIO:

                // 设置时间
                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderRightAudio.tvRightAudioTime.setText(DateUtil
                            .msgToHumanReadableTime(msg.getMsgTime()));
                if (msg.isShowTime()) {
                    holderRightAudio.tvRightAudioTime.setVisibility(View.VISIBLE);
                } else {
                    holderRightAudio.tvRightAudioTime.setVisibility(View.GONE);
                }

                holderRightAudio.ivRightIcon.setTag(myinfo);
                // 设置头像
                if (!StringUtils.isBlank(myinfo.getFriendimage())
                        && holderRightAudio.ivRightIcon.getTag() != null
                        && holderRightAudio.ivRightIcon.getTag() == myinfo) {
                    String url = ChatPacketHelper.buildImageRequestURL(
                            myinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper.displayImage(url,
                            holderRightAudio.ivRightIcon);
                }

                holderRightAudio.ivRightIcon
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(context, FriendsActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable(Constant.FRIEND_READ,
                                        (Serializable) myinfo);
                                intent.putExtras(mBundle);
                                ((ChatActivity) context).startActivity(intent);

                            }
                        });

                // 设置显示时长
                String msgrightBody[] = msg.getMsgcontent().split(":");
                holderRightAudio.tvRightAudioTimeLong.setTag(msg.getMsgcontent());
                if (msgrightBody.length > 1) {
                    if (holderRightAudio.tvRightAudioTimeLong.getTag().equals(
                            msg.getMsgcontent())) {
                        holderRightAudio.tvRightAudioTimeLong
                                .setText(msgrightBody[1] + "\"");
                    }
                }

                // /发送中 发送失败 发送成功
                if (Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(msg
                        .getmsgSendStatus())
                        || Constant.FILE_SEND_STATUS_FILE_ING.equalsIgnoreCase(msg
                        .getmsgSendStatus())) {
                    holderRightAudio.ivRightAudioSendFail.setVisibility(View.GONE);
                    holderRightAudio.ivRightWaitting.setVisibility(View.VISIBLE);
                    // 加入发送成功对象
                    reSendMsg sendTxtMsg = new reSendMsg(
                            holderRightAudio.ivRightAudioSendFail,
                            holderRightAudio.ivRightWaitting, msg);
                    sendTxtMsg.execute();

                } else if (Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(msg
                        .getmsgSendStatus())
                        || Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(msg
                        .getmsgSendStatus())) {
                    holderRightAudio.ivRightAudioSendFail
                            .setVisibility(View.VISIBLE);
                    holderRightAudio.ivRightWaitting.setVisibility(View.INVISIBLE);
                } else {
                    holderRightAudio.ivRightAudioSendFail.setVisibility(View.GONE);
                    holderRightAudio.ivRightWaitting.setVisibility(View.INVISIBLE);
                }

                // 文件不存在直接下载！
                if (!FileUtils.isFileExit(Constant.CHATAUDIO_DIR
                        + msg.getmsgIdFile())
                        && Constant.FILE_SEND_STATUS_FILE_SUCCESS
                        .equalsIgnoreCase(msg.getmsgSendStatus())) {// 文件存在
                    downloadAudioFile(msg, holderLeftAudio.ivLeftWaitting);
                } else {
                    holderRightAudio.ivRightWaitting.setVisibility(View.INVISIBLE);
                }
                break;
            case ChatConstants.VALUE_RIGHT_LOCATION:// 右侧位置
                // 发送位置信息
                holderRightLocation.ivRightIcon.setTag(myinfo);
                holderRightLocation.ivRightLocationPic.setTag(msg);
                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderRightLocation.tvRightTime.setText(DateUtil
                            .msgToHumanReadableTime(msg.getMsgTime()));
                // 设置时间
                if (msg.isShowTime()) {
                    holderRightLocation.tvRightTime.setVisibility(View.VISIBLE);
                } else {
                    holderRightLocation.tvRightTime.setVisibility(View.GONE);
                }
                // 设置位置信息
                if (!StringUtils.isBlank(msg.getMsgcontent())) {
                    String text = msg.getMsgcontent().split(",")[msg
                            .getMsgcontent().split(",").length - 1];
                    holderRightLocation.tvRightLocation.setText(text);
                }
                // holderRightLocation.tvRightLocation.setText(text)
                // 设置头像
                if (!StringUtils.isBlank(myinfo.getFriendimage())
                        && holderRightLocation.ivRightIcon.getTag() != null
                        && holderRightLocation.ivRightIcon.getTag() == myinfo) {
                    String url = ChatPacketHelper.buildImageRequestURL(
                            myinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper.displayImage(url, holderRightLocation.ivRightIcon);
                }

                // /发送中 发送失败 发送成功
                if (StringUtils.isNotBlank(msg.getmsgIdFile())
                        && Constant.FILE_TYPE_LOCATION.equalsIgnoreCase(msg
                        .getmsgTypeFile())
                        && holderRightLocation.ivRightLocationPic.getTag() == msg) {

                    if (Constant.MSG_SEND_STATUS_ING.equalsIgnoreCase(msg
                            .getmsgSendStatus())
                            || Constant.FILE_SEND_STATUS_FILE_ING
                            .equalsIgnoreCase(msg.getmsgSendStatus())) {
                        // 发送中
                        holderRightLocation.ivRightLocationSendFail
                                .setVisibility(View.GONE);
                        holderRightLocation.ivRightWaitting
                                .setVisibility(View.VISIBLE);
                        reSendMsg sendTxtMsg = new reSendMsg(
                                holderRightLocation.ivRightLocationSendFail,
                                holderRightLocation.ivRightWaitting, msg);
                        sendTxtMsg.execute();

                    } else if (Constant.FILE_SEND_STATUS_FILE_FAIL
                            .equalsIgnoreCase(msg.getmsgSendStatus())
                            || Constant.MSG_SEND_STATUS_FAIL.equalsIgnoreCase(msg
                            .getmsgSendStatus())) {
                        // 发送失败
                        holderRightLocation.ivRightWaitting
                                .setVisibility(View.GONE);
                        holderRightLocation.ivRightLocationSendFail
                                .setVisibility(View.VISIBLE);

                    } else {// 发送成功
                        holderRightLocation.ivRightWaitting
                                .setVisibility(View.GONE);
                        holderRightLocation.ivRightLocationSendFail
                                .setVisibility(View.GONE);
                    }

                    if (holderRightLocation.ivRightLocationPic.getTag() != null
                            && holderRightLocation.ivRightLocationPic.getTag() == msg) {
                        String url = ChatPacketHelper.buildImageRequestURL(
                                msg.getmsgIdFile(),
                                ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
                        holderRightLocation.ivRightLocationPic
                                .setImageResource(R.drawable.voicesearch_btnbg_pressed);
                        ImageLoaderHelper.displayImage(msg.getmsgIdFile(),
                                Constant.CHAT_CACHE_DIR, url,
                                holderRightLocation.ivRightLocationPic);
                    }
                }

                holderRightLocation.ivRightLocationPic
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String[] text = msg.getMsgcontent().split(",");
                                Intent intent = new Intent((ChatActivity) context, LocationActivity.class);
                                intent.putExtra("type", "look");
                                intent.putExtra("latitude", Double.parseDouble(text[1]));
                                intent.putExtra("longtitude", Double.parseDouble(text[2]));
                                ((ChatActivity) context).startActivity(intent);
                            }
                        });

                holderRightLocation.ivRightIcon
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(context, FriendsActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable(Constant.FRIEND_READ,
                                        (Serializable) myinfo);
                                intent.putExtras(mBundle);
                                ((ChatActivity) context).startActivity(intent);

                            }
                        });

                break;
            case ChatConstants.VALUE_LEFT_LOCATION:// 左侧位置
                // 接收位置信息
                if (StringUtil.isStrTrue(DateUtil.msgToHumanReadableTime(msg.getMsgTime())))
                    holderLeftLocation.tvLeftTime.setText(DateUtil
                            .msgToHumanReadableTime(msg.getMsgTime()));
                // 设置时间
                if (msg.isShowTime()) {
                    holderLeftLocation.tvLeftTime.setVisibility(View.VISIBLE);
                } else {
                    holderLeftLocation.tvLeftTime.setVisibility(View.GONE);
                }
                // 设置头像
                friendinfo = QiXinBaseDao.queryFriendInfoall(msg.getMsgFrom());// ((ChatActivity)
                // context).getFriendInfo(msg
                // .getMsgFrom());
                if (StringUtil.isStrTrue(friendinfo.getFriendname()))
                    holderLeftLocation.tvLeftName.setText(friendinfo.getFriendname());
                if (ChatConstants.msg.TYPE_GROUPCHAT.equalsIgnoreCase(msg
                        .getMsgType())) {
                    holderLeftLocation.tvLeftName.setVisibility(View.VISIBLE);
                } else {
                    holderLeftLocation.tvLeftName.setVisibility(View.GONE);
                }

                holderLeftLocation.ivLeftIcon.setTag(friendinfo);
                holderLeftLocation.ivLeftIcon
                        .setImageResource(R.drawable.v5_0_1_profile_headphoto);
                if (!StringUtils.isBlank(friendinfo.getFriendimage().trim())
                        && (FriendInfo) holderLeftLocation.ivLeftIcon.getTag() != null
                        && (FriendInfo) holderLeftLocation.ivLeftIcon.getTag() == friendinfo) {

                    String url = ChatPacketHelper.buildImageRequestURL(
                            friendinfo.getFriendimage(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);
                    ImageLoaderHelper.displayImage(url, holderLeftLocation.ivLeftIcon);
                }
                if (!StringUtils.isBlank(msg.getMsgcontent())) {
                    String text = msg.getMsgcontent().split(",")[msg
                            .getMsgcontent().split(",").length - 1];
                    holderLeftLocation.tvLeftLocation.setText(text);
                }
                holderLeftLocation.ivLeftIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendInfo friendInfoTemp = (FriendInfo) v.getTag();
                        if (friendInfoTemp == null) {
                            return;
                        }
                        FriendInfoBrower(friendInfoTemp);
                    }
                });
                holderLeftLocation.ivLeftLocationPic.setTag(msg);
                /**
                 * @author haijian 设置默认图片
                 */
                holderLeftLocation.ivLeftLocationPic
                        .setImageResource(R.drawable.voicesearch_btnbg_pressed);
                holderLeftLocation.ivLeftWaitting.setVisibility(View.VISIBLE);
                // 设置图片
                if (StringUtils.isNotBlank(msg.getmsgIdFile())
                        && (ChatHisBean) holderLeftLocation.ivLeftLocationPic.getTag() != null
                        && (ChatHisBean) holderLeftLocation.ivLeftLocationPic.getTag() == msg) {
                    String url = ChatPacketHelper.buildImageRequestURL(
                            msg.getmsgIdFile(),
                            ChatConstants.iq.DATA_VALUE_RES_TYPE_IM);
                    ImageLoaderHelper.displayImage(url, holderLeftLocation.ivLeftLocationPic,
                            holderLeftLocation.ivLeftWaitting);
                }
                holderLeftLocation.ivLeftLocationPic.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatHisBean messageTemp = (ChatHisBean) v.getTag();
                        if (!TextUtils.isEmpty(messageTemp.getmsgIdFile())
                                && Constant.FILE_TYPE_LOCATION
                                .equalsIgnoreCase(messageTemp
                                        .getmsgTypeFile())) {
//						imageBrower(messageTemp);
                            //跳转到地图界面
                            String[] text = msg.getMsgcontent().split(",");
                            Intent intent = new Intent((ChatActivity) context, LocationActivity.class);
                            intent.putExtra("type", "look");
                            intent.putExtra("latitude", Double.parseDouble(text[1]));
                            intent.putExtra("longtitude", Double.parseDouble(text[2]));
                            ((ChatActivity) context).startActivity(intent);
                        }
                    }
                });
                break;

            default:
                break;
        }

        return convertView;

    }

    static class ViewHolderNotice {
        private TextView tvNotice;//
        private TextView tvTime;//
    }

    static class ViewHolderRightText {
        private ImageView ivRightIcon;// 右边的头像
        private TextView tvRightText;// 右边的文本
        private TextView tvRightTime;// 右边的文本
        private ImageView ivRightTextSendFail;// 文本发送失败
        private ProgressBar pbRightTextProgressBar; // 上传进度
    }

    static class ViewHolderRightImg {
        private ImageView ivRightIcon;// 右边的头像
        private ImageView ivRightImage;// 右边的图像
        private ImageView ivRightImageSendFail;// resend
        private TextView tvRightTime;// 右边的文本
        private ProgressBar pbRightProgressBar; // 上传进度
    }

    static class ViewHolderRightAudio {
        private ImageView ivRightIcon;// 右边的头像
        private ImageView ivRightAudio;// 右边的声音
        private ImageView ivRightAudioSendFail;// 右边的声音
        private ProgressBar ivRightWaitting;// 下载中
        private TextView tvRightAudioTime;// 右边的声音时间
        private TextView tvRightAudioTimeLong; // 时长
    }

    static class ViewHolderLeftText {
        private ImageView ivLeftIcon;// 左边的头像
        private TextView tvLeftText;// 左边的文本
        private TextView tvLeftTime;// 左边的文本
        private TextView tvLeftName;
    }

    static class ViewHolderLeftImg {
        private ImageView ivLeftIcon;// 左边的头像
        private ImageView ivLeftImage;// 左边的图像
        private TextView tvLeftTime;// 左边的文本
        private ProgressBar pbLeftProgressBar;
        private TextView tvLeftName;
    }

    static class ViewHolderLeftAudio {
        private ImageView ivLeftIcon;// 左边的头像
        private ProgressBar ivLeftWaitting;// 下载中
        private TextView tvLeftTime;// 左边的声音时间
        private ImageView ivLeftAudioSendFail; //
        private TextView tvLeftAudioTimeLong; // 时长
        private TextView tvLeftName;
    }

    static class ViewHolderLeftLocation {
        private ImageView ivLeftIcon;// 左边的头像
        private ProgressBar ivLeftWaitting;// 下载中
        private TextView tvLeftTime;// 左边的声音时间
        private ImageView ivLeftLocationPic; // 左边的地图图片
        private TextView tvLeftLocation;// 左边的地址
        private TextView tvLeftName;//左边的用户名
    }

    static class ViewHolderRightLocation {
        private ImageView ivRightIcon;// 右边的头像
        private TextView tvRightTime;// 左边的声音时间
        private ImageView ivRightLocationPic;// 右边的地图图片
        private ProgressBar ivRightWaitting;// 下载中
        private TextView tvRightLocation;// 右边的地址
        private ImageView ivRightLocationSendFail;//
    }

    //发送文件布局
    static class ViewHolderLeftFile {
        private ImageView ivLeftIcon;// 左边的头像
        private ProgressBar ivLeftWaitting;// 下载中
        private TextView tvLeftTime;// 左边的声音时间
        private ImageView ivLeftLocationPic; // 左边的地图图片
        private TextView tvLeftLocation;// 左边的地址
        private TextView tvLeftName;//左边的用户名
    }

    static class ViewHolderRightFile {
        private ImageView ivRightIcon;// 右边的头像
        private TextView tvRightTime;// 左边的声音时间
        private ImageView ivRightLocationPic;// 右边的地图图片
        private ProgressBar ivRightWaitting;// 下载中
        private TextView tvRightLocation;// 右边的地址
        private ImageView ivRightLocationSendFail;//
    }

    // /下载声音文件
    private void downloadAudioFile(final ChatHisBean messageTemp,
                                   final ProgressBar pb) {
        // pb.setVisibility(View.VISIBLE);
        AttachFileProcessor.requestImAudioAttach(messageTemp.getmsgIdFile(),
                new OnProcessResultListener() {
                    @Override
                    public void onProcessResult(boolean success, String msg) {
                        // pb.setVisibility(View.INVISIBLE);
                    }
                });
    }

    protected void FriendInfoBrower(FriendInfo friendInfoTemp) {
        Intent intent = new Intent();
        intent.setClass(context, FriendsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Constant.FRIEND_READ,
                (Serializable) friendInfoTemp);
        intent.putExtras(mBundle);
        ((ChatActivity) context).startActivity(intent);

    }

    protected void imageBrower(ChatHisBean messageTemp) {

        ArrayList<String> urls2 = new ArrayList<String>();
        ChatHisBean tmpBean;
        for (int i = 0; i < list.size(); i++) {
            switch (getItemViewType(i)) {

                case ChatConstants.VALUE_LEFT_IMAGE:
                    tmpBean = list.get(i);
                    if (!TextUtils.isEmpty(tmpBean.getmsgIdFile())
                            && Constant.FILE_TYPE_PIC.equalsIgnoreCase(tmpBean
                            .getmsgTypeFile())) {

                        urls2.add(tmpBean.getmsgIdFile());
                    }
                    break;
                case ChatConstants.VALUE_RIGHT_IMAGE:
                    tmpBean = list.get(i);
                    if (!TextUtils.isEmpty(tmpBean.getmsgIdFile())
                            && Constant.FILE_TYPE_PIC.equalsIgnoreCase(tmpBean
                            .getmsgTypeFile())) {
                        urls2.add(tmpBean.getmsgIdFile());
                    }

                    break;
                default:
                    break;
            }
        }

        if (urls2.size() == 0)
            return;
        int p = 0;

        for (int i = 0; i < urls2.size(); i++) {
            if (urls2.get(i).toString()
                    .equalsIgnoreCase(messageTemp.getmsgIdFile())) {
                p = i;
                break;
            }
        }
        Intent intent = new Intent(context, ShowChatPictureActivity.class);
        intent.putExtra(ShowChatPictureActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ShowChatPictureActivity.EXTRA_IMAGE_INDEX, p);
        ((ChatActivity) context).startActivity(intent);
    }

}

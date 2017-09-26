package com.hjnerp.activity.contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.hjnerp.activity.im.ChatActivity;
import com.hjnerp.activity.myinfo.ShowPortraitActivity;
import com.hjnerp.business.ContactBusiness;
import com.hjnerp.common.ActionBarWidgetActivity;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.db.Tables;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.VerfifyFriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.util.AttachFileProcessor;
import com.hjnerp.util.AttachFileProcessor.OnProcessResultListener;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;
import com.sdyy.utils.XPermissions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsActivity extends ActionBarWidgetActivity implements OnClickListener,
        ActionSheet.ActionSheetListener {

    String sFriendId;
    private FriendInfo friend;
    private Button sendBtn, callBtn;
    private TextView firstnameEdit, nicknameEdit, orgunitEdit, mobileEdit,
            emailhomeEdit, discEdit;
    //    private ImageView email_edit, phone_edit;
//    private RelativeLayout dialog_confirm_rl, dialog_cancel_rl, rl_gally, rl_camera;
    private RelativeLayout rl_phone, rl_email;
    private ImageView photo, qrcode;
    private FriendPopupWindow menuSet = null;
    private Dialog noticeDialog = null;
    private Dialog setNoteDialog;
    private EditText et_note;
    private String extra_note;
    private TextView tv_setnote_cancel, tv_setnote_confirm;
    public static final int REQUEST_CODE＿IMAGE_CAPTURE = 0x31;
    public static final int REQUEST_CODE_IMAGE_CROP = 0x32;
    public static final int REQUEST_CODE_GALLY = 0x33;
    public static final int REQUEST_CODE_GALLY_CROP = 0x34;

    public static final int ADD_FRIEND = 0x20;

    public static final int SET_EMAIL = 0x10;
    public static final int SET_PHONE = 0x11;
    public static final int SET_REMARK = 0x12;
    public int flag_setwhat = 0;

    private File tempFile;

    // TODO 检查联系人表有没有此人（自己是自己的朋友），如果不是自己的好友，按钮点击动作应为加为好友
    private boolean ifIsMyFriends = true;

    private String photoUrl;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.user_head_layout)
    RelativeLayout user_head_layout;
    //判断是否是我自己的手机号
    private boolean isMyPhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friend = (FriendInfo) getIntent().getSerializableExtra(Constant.FRIEND_READ);
        ifIsMyFriends = ContactBusiness.checkFriends(friend.getFriendid());
        setContentView(R.layout.friendinfo);
        ButterKnife.bind(this);
        actionRightTv.setVisibility(View.GONE);
        actionLeftTv.setOnClickListener(this);
        actionCenterTv.setText("详细资料");

        initView();

        // 有网络则更新用户信息
        if (hasInternetConnected() && ifIsMyFriends) {
            waitDialogRectangle.show();
            sendCheckUserInfo();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (noticeDialog != null) {
            noticeDialog.dismiss();
        }
        if (menuSet != null) {
            menuSet.dismiss();
        }
    }

    // TODO 从通讯录进入好友详情，当对方解除好友关系后进行提示；从查看群成员信息进入时，进行同样的提示
    @Override
    public void removeMeAsFriend(String friendId) {
        super.removeMeAsFriend(friendId);
        if (friendId.equals(friend.getFriendid())) {
            sendBtn.setEnabled(false);
            callBtn.setEnabled(false);
            showNoticeDialog("对方解除了好友关系");
        }
    }

    /**
     * 初始化.
     *
     * @author 李庆义
     * @update 2012-5-16 上午9:13:01
     */
    // @SuppressLint("NewApi")
    protected void initView() {
        photo = (ImageView) findViewById(R.id.user_head_avatar);
        firstnameEdit = (TextView) findViewById(R.id.user_head_name);
        nicknameEdit = (TextView) findViewById(R.id.user_head_content);
        orgunitEdit = (TextView) findViewById(R.id.orgunit);
        mobileEdit = (TextView) findViewById(R.id.mobile);
        emailhomeEdit = (TextView) findViewById(R.id.emailhome);
        qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        sendBtn = (Button) findViewById(R.id.ui_send_btn);

        rl_email = (RelativeLayout) findViewById(R.id.rl_email);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_email.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        user_head_layout.setOnClickListener(this);
        photo.setOnClickListener(this);

        setFriendView();

        closeInput();

//        callBtn = (Button) findViewById(R.id.ui_call_btn);
//        user_head_layout = (RelativeLayout) findViewById(R.id.user_head_layout);
//        rl_remark = (RelativeLayout) findViewById(R.id.rl_remark);
//        email_edit = (ImageView) findViewById(R.id.ui_edit_iv_email);
//        phone_edit = (ImageView) findViewById(R.id.ui_edit_iv_phone);
//        rl_remark.setOnClickListener(listener);
//        callBtn.setOnClickListener(listener);

        // 二维码名片
        // try {
        // // Bitmap mQrcode = Create2DCode(friend.getFriendid());
        // Bitmap mQrcode = createQRCode(friend.getFriendid(),300);
        // qrcode.setBackground(new BitmapDrawable(mQrcode));
        // } catch (WriterException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

    private void sendCheckUserInfo() {
        mThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                IQ iq = HJWebSocketManager.getInstance().searchUserInfo(
                        friend.getFriendid());
                if (iq == null) {
                    sendToMyHandler("check_friendinfo_error");
                } else if (ChatPacketHelper.parseErrorCode(iq) != null) {
                    sendToMyHandler("check_friendinfo_error");
                } else {
                    ArrayList<FriendInfo> friendInfoList = ChatPacketHelper
                            .processSearchedUserInfo(iq);
                    if (friendInfoList.size() > 0) {

                        friend = friendInfoList.get(0);
                        if (ifIsMyFriends) {
                            QiXinBaseDao.replaceFriendInfo(friend, 'Y');
                        } else {
                            QiXinBaseDao.replaceFriendInfo(friend, 'N');
                        }
                        sendToMyHandler("check_friendinfo_ok");
                    } else {
                        sendToMyHandler("check_friendinfo_error");
                    }
                }
            }
        };
        mThread.start();
    }

    private OnClickListener setOnClick = new OnClickListener() {
        public void onClick(View v) {
            menuSet.dismiss();
            switch (v.getId()) {
                case R.id.linear_delete:
                    // show dialog if delete friend
                    showNoticeDialog();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 邮箱设置
     */
    private void setEmai() {
        if (!isEmail(extra_note)) {
            showFailToast(mContext.getString(R.string.toast_Title_EmailFail));
            return;
        }
        mThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                showWaitDialog("请稍等...");
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String respond = HJWebSocketManager.getInstance().requestChangeEmail(extra_note);
                if (!StringUtil.isNullOrEmpty(respond) && respond.contains("result")) {
                    sendToMyHandler("setemail_ok");
                } else if (!StringUtil.isNullOrEmpty(respond)
                        && respond.contains("session")) {
                    sendToMyHandler("session_error");
                } else {
                    sendToMyHandler("setemail_error");
                }
            }
        };
        mThread.start();
    }

    /**
     * 修改手机号码
     */
    private void setPhone() {
        if (!isMobileNO(extra_note)) {
            showFailToast(mContext.getString(R.string.toast_Title_PhoneFail));
            return;
        }
        mThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                showWaitDialog("请稍等...");
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String respond = HJWebSocketManager.getInstance().requestChangePhone(extra_note.toCharArray());
                if (!StringUtil.isNullOrEmpty(respond)
                        && respond.contains("result")) {
                    sendToMyHandler("setphone_ok");
                } else if (!StringUtil.isNullOrEmpty(respond)
                        && respond.contains("session")) {
                    sendToMyHandler("session_error");
                } else {
                    sendToMyHandler("setphone_error");
                }
            }
        };
        mThread.start();
    }

    private void addFriend() {
        sendBtn.setEnabled(false);
        showWaitDialog("请稍等...");
        new Thread() {
            @Override
            public void run() {
//
//				try {
//					sleep(200);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                // TODO add friend 稍后需要加入手动输入验证信息
                // 如果临时联系人表中有对方请求添加自己的信息，则直接发送同意添加，否则发送请求添加
                VerfifyFriendInfo vFriendInfo = null;
                ArrayList<VerfifyFriendInfo> tempFriendsInfo = QiXinBaseDao
                        .queryTempContactInfos(friend.getFriendid());

                if (tempFriendsInfo.size() > 0) {
                    vFriendInfo = new VerfifyFriendInfo();
                    vFriendInfo = tempFriendsInfo.get(0);
                }

                if (vFriendInfo != null
                        && vFriendInfo.getVerfifyType().equals(
                        ContactConstants.THIS_FRIEND_WANT_ADD_ME)) {// 发送同意添加请求
                    IQ iq = HJWebSocketManager.getInstance().answerAddContact(
                            friend.getFriendid(), true);
                    String errorMsg = ChatPacketHelper.parseErrorCode(iq);
                    if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                        waitDialogRectangle.dismiss();
                    }
                    if (errorMsg != null) {
                        showNoticeDialog(errorMsg);
                    } else {
                        // Log.e(TAG,"add friend succ");
                        QiXinBaseDao.updateTempContactStatus(
                                friend.getFriendid(),
                                ContactConstants.VERFIFY_PERMISSION);
                        worker.postDML(new SQLiteWorker.AbstractSQLable() {
                            @Override
                            public void onCompleted(Object event) {
                                if (!(event instanceof Throwable)) {
                                    showNoticeDialog("添加成功!");
                                }
                            }

                            @Override
                            public Object doAysncSQL() {
                                QiXinBaseDao.shiftTempContactInfoById(friend
                                        .getFriendid());
                                return null;
                            }
                        });
                    }
                } else {// 发送添加请求

                    IQ iq = HJWebSocketManager.getInstance().addContact(
                            friend.getFriendid(), extra_note);
                    String errorMsg = ChatPacketHelper.parseErrorCode(iq);
                    if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                        waitDialogRectangle.dismiss();
                    }
                    if (errorMsg != null) {
                        @SuppressWarnings("unchecked")
                        String error_code = (String) ((Map<String, Object>) iq.data)
                                .get(ChatConstants.iq.DATA_KEY_ERROR_CODE);
                        // 错误类型：已经是好友
                        if (ChatConstants.error_code.ERROR_CODE_CONTACT_HAD_ADDED
                                .equals(error_code)) {

                            final VerfifyFriendInfo mfriend = ContactBusiness
                                    .makeSaveDate(
                                            friend,
                                            ContactConstants.I_WANT_ADD_THIS_FRIEND,
                                            ContactConstants.VERFIFY_PERMISSION);
                            worker.postDML(new SQLiteWorker.AbstractSQLable() {
                                @Override
                                public void onCompleted(Object event) {
                                    if (!(event instanceof Throwable)) {
                                        sendToMyHandler("add_success");
                                    }
                                }

                                @Override
                                public Object doAysncSQL() {
                                    QiXinBaseDao
                                            .replaceTempContactInfo(mfriend);
                                    QiXinBaseDao.replaceFriendInfo(friend, 'Y');
                                    return null;
                                }
                            });

                        } else {
//							ToastUtil.ShowShort(FriendsActivity.this, errorMsg);
                        }
                    } else {
                        final VerfifyFriendInfo mfriend = ContactBusiness
                                .makeSaveDate(
                                        friend,
                                        ContactConstants.I_WANT_ADD_THIS_FRIEND,
                                        ContactConstants.VERFIFY_ONGO);
                        worker.postDML(new SQLiteWorker.AbstractSQLable() {
                            @Override
                            public void onCompleted(Object event) {
                                if (!(event instanceof Throwable)) {
                                    // ((Activity) context).finish();
                                    showNoticeDialog("添加成功，等待验证");
                                }
                            }

                            @Override
                            public Object doAysncSQL() {
                                QiXinBaseDao.replaceTempContactInfoN(mfriend);
                                return null;
                            }
                        });

                    }

                }
            }
        }.start();
    }

    private Thread deleteFriendThread;

    private void removeFriend(final String friendID) {

        deleteFriendThread = new Thread() {
            @Override
            public void run() {

                Looper.prepare();
                showWaitDialog("请稍等...");
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                IQ iq = HJWebSocketManager.getInstance()
                        .removeContact(friendID);

                String errorMsg = ChatPacketHelper.parseErrorCode(iq);
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
                if (errorMsg != null) {

                    // 如果错误类型是：好友已经被删除，则处理本地数据库
                    Map<String, Object> dataMap = (Map<String, Object>) iq.data;
                    String str = (String) dataMap
                            .get(ChatConstants.iq.DATA_KEY_ERROR_CODE);
                    if (ChatConstants.error_code.ERROR_CODE_CONTACT_NOT_FOUND
                            .equals(str)) {
                        worker.postDML(new SQLiteWorker.AbstractSQLable() {
                            @Override
                            public void onCompleted(Object event) {
                                if (!(event instanceof Throwable)) {

                                    // 添加提示框
                                    showFailToast("删除成功");
                                    jumpToMain(FriendsActivity.this);
                                }
                            }

                            @Override
                            public Object doAysncSQL() {
                                ContactBusiness.deleFriendById(
                                        sputil.getMyId(), friend.getFriendid());
                                return null;
                            }
                        });

                    } else {
                        showFailToast(errorMsg);
                    }

                } else {
                    // 删除成功，删除此人的本地联系人表和新消息
                    worker.postDML(new SQLiteWorker.AbstractSQLable() {
                        @Override
                        public void onCompleted(Object event) {
                            if (!(event instanceof Throwable)) {
                                // deleteOneSingleChatNewMsg(friend.getFriendid());
                                // 添加提示框
                                showFailToast("删除成功");
                                jumpToMain(FriendsActivity.this);
                            }
                        }

                        @Override
                        public Object doAysncSQL() {
                            ContactBusiness.deleFriendById(sputil.getMyId(),
                                    friend.getFriendid());
                            QiXinBaseDao.deleteTempContactInfoById(friend
                                    .getFriendid());
                            return null;
                        }
                    });

                }

            }
        };
        deleteFriendThread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!friend.getFriendid().equals(sputil.getMyId()) && ifIsMyFriends) {
            getMenuInflater().inflate(R.menu.friendinfo, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_more:
                menuSet = new FriendPopupWindow(FriendsActivity.this, setOnClick);
                View viewSet = this.findViewById(R.id.menu_more);
                menuSet.showAsDropDown(viewSet, 0, 0);
                break;
            default:
                break;
        }
        return false;
    }

    private void setFriendView() {
        photoUrl = friend.getFriendimage();
        /**
         * @author haijian
         * 获取群成员详情中的头像地址
         */
        Log.i("info", "群成员详情头像地址：" + photoUrl);
        if (!StringUtil.isNullOrEmpty(photoUrl)) {
            ImageLoaderHelper.displayImage(ChatPacketHelper.buildImageRequestURL(photoUrl,
                    ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH), photo);
        }
        firstnameEdit.setText(friend.getFriendname());
        nicknameEdit.setText(friend.getFriendid());
        orgunitEdit.setText(friend.getDeptname());

        emailhomeEdit.setText(friend.getFriendmail());

        // 是否显示相机团和发送消息按钮
        if (friend.getFriendid().equals(sputil.getMyId())) {// 我自己的详情
            sendBtn.setVisibility(View.GONE);
//            callBtn.setVisibility(View.INVISIBLE);
//            user_head_layout.setOnClickListener(cameraClickListener);
//            phone_edit.setVisibility(View.VISIBLE);
//            email_edit.setVisibility(View.VISIBLE);
            mobileEdit.setText(friend.getFriendmtel());
        } else {// 好友的详情
            sendBtn.setVisibility(View.VISIBLE);
            if (StringUtil.isNullOrEmpty(friend.getFriendmtel().trim())) {
//                callBtn.setVisibility(View.INVISIBLE);
                mobileEdit.setText(friend.getFriendmtel());
            } else {
//                callBtn.setVisibility(View.VISIBLE);
                mobileEdit.setText(friend.getFriendmtel());
            }
        }

        if (!ifIsMyFriends) {
            // sendBtn.setEnabled(false);
            sendBtn.setText("加为好友");
        }

    }

    /* 提示用户是否删除 */
    private void showNoticeDialog() {
        noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);

        TextView dialog_cancel_rl, dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog
                .findViewById(R.id.dialog_notice_tv);
        notice.setText("删除好友后，聊天记录和好友关系都将被删除，是否继续");
        dialog_cancel_rl = (TextView) noticeDialog
                .findViewById(R.id.dialog_cancel_tv);
        dialog_confirm_rl = (TextView) noticeDialog
                .findViewById(R.id.dialog_confirm_tv);
        dialog_cancel_rl.setOnClickListener(this);
        dialog_confirm_rl.setOnClickListener(this);
        noticeDialog.show();
    }

    private void showNoticeDialog(String msg) {
        if (noticeDialog != null) {
            noticeDialog.dismiss();
        }
        noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_nocancel);
        noticeDialog.setCancelable(false);
        noticeDialog.setCanceledOnTouchOutside(false);
        RelativeLayout noticedialog_confirm_rl;
        TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
        notice.setText(msg);
        noticedialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_nc_confirm_rl);
        noticedialog_confirm_rl.setOnClickListener(this);

        noticeDialog.show();
    }

    private void showWaitDialog(String msg) {
        if (noticeDialog != null) {
            noticeDialog.dismiss();
        }
        noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_wait);
        TextView rl_gally;
        rl_gally = (TextView) noticeDialog.findViewById(R.id.tv);
        rl_gally.setText(msg);
        noticeDialog.show();
    }

    /**
     * 旧版本的设置头像弹框
     */
    private void showSelectPicDialog() {
        noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_select_photo);
        RelativeLayout rl_gally, rl_camera, rl_cancel;
        rl_gally = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_rl_gally);
        rl_camera = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_rl_camera);
        rl_cancel = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_rl_cancel);

        rl_gally.setOnClickListener(this);
        rl_camera.setOnClickListener(this);
        rl_cancel.setOnClickListener(this);
        noticeDialog.show();
    }

    private Thread mThread;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GALLY:// 相册返回
            {
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
                    intent.setDataAndType(data.getData(), "image/*");
                    intent.putExtra("crop", false);// 进行修剪
                    // aspectX aspectY 是宽高的比例
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    // outputX outputY 是裁剪图片宽高
                    intent.putExtra("outputX", 320);
                    intent.putExtra("outputY", 320);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQUEST_CODE_GALLY_CROP);
                }
            }
            break;
            case REQUEST_CODE＿IMAGE_CAPTURE:// 拍照返回
            {
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
//                    Uri imageUri=FileProvider.getUriForFile(context, "com.hjnerp.takephoto.fileprovider", new File("/storage/emulated/0/temp/friends_photo.jpg"));//通过FileProvider创建一个content类型的Uri
//                    Uri outputUri = FileProvider.getUriForFile(this, "com.hjnerp.takephoto.fileprovider",tempFile);
//                    Log.d("tskephoto",tempFile.getAbsolutePath());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(getImageContentUri(this, new File(tempFile.getAbsolutePath())), "image/*");
                    intent.putExtra("return-data", true);
                    intent.putExtra("crop", false);
                    intent.putExtra("scaleUpIfNeeded", true);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 320);
                    intent.putExtra("outputY", 320);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                    startActivityForResult(intent, REQUEST_CODE_GALLY_CROP);
                } else {
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.delete();
                    }
                }
            }
            break;

            case REQUEST_CODE_GALLY_CROP: {
                LogShow(resultCode + "");
                if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                    try {
                        waitDialogRectangle.show();
//                        Uri outputUri = FileProvider.getUriForFile(context, "com.hjnerp.takephoto.fileprovider",tempFile);
                        Bitmap avartar = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            avartar = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                    data.getData());
                        } else {
                            avartar = data.getExtras().getParcelable("data");
                        }

                        String fileName = "avartar_" + UUID.randomUUID() + "_320x320.jpg";
                        File file = new File(Constant.CHAT_CACHE_DIR, fileName);
                        BufferedOutputStream bos = new BufferedOutputStream(
                                new FileOutputStream(file));
                        avartar.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.close();
                        AttachFileProcessor.responseAttach(fileName,
                                new OnProcessResultListener() {
                                    @Override
                                    public void onProcessResult(
                                            final boolean success, final String msg/* fileID */) {
                                        if (success) {
                                            sendChangePhoto(msg);
                                        } else {
                                            com.hjnerp.util.Log.w(msg);
                                            waitDialogRectangle.dismiss();
                                            showFailToast(mContext.getString(R.string.toast_Title_PortraitFail));
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        // com.hjnerp.util.Log.e(e);
                        waitDialogRectangle.dismiss();
                        showFailToast(mContext.getString(R.string.toast_Title_PortraitFail));
                    }
                }
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
            break;

            default:
                break;
        }
    }

    /**
     * Gets the content:// URI  from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 上传头像
     *
     * @param content
     */
    private void sendChangePhoto(final String content) {
        LogShow(content);
        mThread = new Thread() {
            @Override
            public void run() {
                final IQ iq = HJWebSocketManager.getInstance().changeAvartar(content);
                ((Handler) EapApplication.getApplication().getExtra(
                        EapApplication.EXTRA_MAIN_HANDLER))
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                String message;
                                if ((message = ChatPacketHelper.parseErrorCode(iq)) == null) {
                                    SQLiteWorker.getSharedInstance().postDML(
                                            new SQLiteWorker.AbstractSQLable() {
                                                @Override
                                                public void onCompleted(Object event) {
                                                    if (!(event instanceof Throwable)) {
                                                        photo.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ImageLoaderHelper
                                                                        .displayImage(
                                                                                ChatPacketHelper.buildImageRequestURL(
                                                                                        content,
                                                                                        ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),
                                                                                photo);
                                                                showFailToast(mContext.getString(R.string.dialog_Message_saveSucc));
                                                            }
                                                        });
                                                    }
                                                    waitDialogRectangle.dismiss();
                                                }

                                                @Override
                                                public Object doAysncSQL() {
                                                    String userID = sputil
                                                            .getMyId();
                                                    QiXinBaseDao
                                                            .updateUserInfo(
                                                                    userID,
                                                                    Tables.UserTable.COL_VAR_IMGUSER,
                                                                    content);
                                                    QiXinBaseDao
                                                            .updateFriendInfo(
                                                                    userID,
                                                                    Tables.ContactTable.COL_VAR_IMGFRIEND,
                                                                    content);
                                                    friend = QiXinBaseDao.queryFriendInfo(
                                                            SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext())
                                                                    .getMyUserId());

                                                    setFriendView();
                                                    return null;
                                                }
                                            });
                                } else {
                                    com.hjnerp.util.Log.w(message);
                                    if (waitDialogRectangle != null
                                            && waitDialogRectangle.isShowing())
                                        waitDialogRectangle.dismiss();
                                    showFailToast(mContext.getString(R.string.toast_Title_PortraitFail));
                                }

                            }
                        });

            }
        };
        mThread.start();
    }

    private void sendToMyHandler(String msg) {
        Message Msg = new Message();
        Bundle b = new Bundle();
        b.putString("flag", msg);
        Msg.setData(b);

        myHandler.sendMessage(Msg);
    }

    final Handler myHandler = new Handler() {

        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String mmsg = b.getString("flag");
            if (mmsg.equals("add_success")) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Constant.IM_NEWS, (Serializable) friend);
                intent.putExtras(mBundle);
                startActivity(intent);
                ((Activity) context).finish();
            }
            if ("check_friendinfo_ok".equalsIgnoreCase(mmsg)) {
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
                initView();
            }
            if ("check_friendinfo_error".equalsIgnoreCase(mmsg)) {
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
            }
            if ("setemail_ok".equalsIgnoreCase(mmsg)) {
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
                emailhomeEdit.setText(extra_note);
                // 添加提示框
                showFailToast(mContext.getString(R.string.toast_Title_ChangeSucc));
            }
            if ("setphone_ok".equalsIgnoreCase(mmsg)) {
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
                mobileEdit.setText(extra_note);
                showFailToast(mContext.getString(R.string.toast_Title_ChangeSucc));
            }
            if ("setemail_error".equalsIgnoreCase(mmsg)) {
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
                showFailToast(mContext.getString(R.string.toast_Title_ChangeFail));
            }
            if ("setphone_error".equalsIgnoreCase(mmsg)) {
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
                showFailToast(mContext.getString(R.string.toast_Title_ChangeFail));
            }
            if ("session_error".equalsIgnoreCase(mmsg)) {
                if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                    waitDialogRectangle.dismiss();
                }
                isForcedExit(ChatConstants.error_string.ERROR_STRING_SESSION_INVALID);
            }
        }
    };

    /* 添加验证信息 */
    private void showsetNoteDialog() {
        setNoteDialog = new Dialog(this, R.style.input_dialog);
        setNoteDialog.setContentView(R.layout.dialog_renamegroup);
        TextView title = (TextView) setNoteDialog
                .findViewById(R.id.et_groupname_title);

        et_note = (EditText) setNoteDialog
                .findViewById(R.id.dialog_goup_rename_et);
        tv_setnote_confirm = (TextView) setNoteDialog
                .findViewById(R.id.dialog_group_confirm_tv);
        tv_setnote_cancel = (TextView) setNoteDialog
                .findViewById(R.id.dialog_group_cancel_tv);
        switch (flag_setwhat) {
            case ADD_FRIEND:// 添加好友
                title.setText(mContext.getString(R.string.toast_Title_CheckMsg));
                String text = "我是";
                et_note.setText(text);
                et_note.setSelection(text.length());
                break;
            case SET_EMAIL:
                title.setText(mContext.getString(R.string.toast_Title_ChangeEmail));
                break;
            case SET_PHONE:
                title.setText(mContext.getString(R.string.toast_Title_ChangePhone));
                break;
            case SET_REMARK:
                title.setText(mContext.getString(R.string.toast_Title_ChangeRemark));
                break;
            default:
                break;
        }
        tv_setnote_confirm.setOnClickListener(this);
        tv_setnote_cancel.setOnClickListener(this);
        setNoteDialog.show();
    }

    /**
     * 用字符串生成二维码
     */
    private final int BLACK = 0xff000000;

    // public Bitmap createQRCode(String str,int widthAndHeight) throws
    // WriterException {
    // Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType,
    // String>();
    // hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
    // BitMatrix matrix = new MultiFormatWriter().encode(str,
    // BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
    // int width = matrix.getWidth();
    // int height = matrix.getHeight();
    // int[] pixels = new int[width * height];
    //
    // for (int y = 0; y < height; y++) {
    // for (int x = 0; x < width; x++) {
    // if (matrix.get(x, y)) {
    // pixels[y * width + x] = BLACK;
    // }
    // }
    // }
    // Bitmap bitmap = Bitmap.createBitmap(width, height,
    // Bitmap.Config.ARGB_8888);
    // bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    // return bitmap;
    // }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_left_tv:
                finish();
                break;
            case R.id.ui_send_btn:
                if (ifIsMyFriends) {// 进入聊天
                    Intent intent = new Intent(context, ChatActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(Constant.IM_NEWS,
                            (Serializable) friend);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    ((Activity) context).finish();
                } else {// 加为好友
                    flag_setwhat = ADD_FRIEND;
                    showsetNoteDialog();
                }
                break;
//                case R.id.ui_call_btn:
//                    String inputStr = friend.getFriendmtel();
//                    Intent phoneIntent = new Intent("android.intent.action.CALL",
//                            Uri.parse("tel:" + inputStr));
//                    startActivity(phoneIntent);
//                    break;
            case R.id.dialog_group_confirm_tv:
                extra_note = et_note.getText().toString();
                if (StringUtil.isNullOrEmpty(extra_note))
                    return;
                setNoteDialog.dismiss();
                switch (flag_setwhat) {
                    case ADD_FRIEND:// 添加好友
                        addFriend();
                        break;
                    case SET_PHONE:
                        setPhone();
                        break;
                    case SET_EMAIL:
                        setEmai();
                        break;
                    case SET_REMARK:
                        break;
                    default:
                        break;

                }
                break;
            case R.id.dialog_group_cancel_tv:
                setNoteDialog.dismiss();
                break;
            case R.id.rl_phone:
                if (friend.getFriendid().equals(sputil.getMyId())) {
                    flag_setwhat = SET_PHONE;
                    showsetNoteDialog();
                } else {
                    if (!isPermissions(new String[]{XPermissions.CALL_PHONE})) {
                        showFailToast("拨打电话未授权！");
                        return;
                    }
                    String phone = friend.getFriendmtel();
                    if (!StringUtil.isStrTrue(phone)) {
                        showFailToast("手机号不能为空！");
                        return;
                    }
                    showDialog(phone);
                }
                break;
            case R.id.rl_email:
                if (friend.getFriendid().equals(sputil.getMyId())) {
                    flag_setwhat = SET_EMAIL;
                    showsetNoteDialog();
                }
                break;
//                case R.id.rl_remark:
//                    // if(friend.getFriendid().equals(sputil.getMyId())){
//                    // flag_setwhat = SET_REMARK;
//                    // showsetNoteDialog();
//                    // }
//                    break;
            case R.id.dialog_cancel_tv:
                noticeDialog.dismiss();
                break;
            case R.id.dialog_rl_cancel:
                noticeDialog.dismiss();
                break;
            case R.id.dialog_confirm_tv:
                noticeDialog.dismiss();
                removeFriend(friend.getFriendid());
                break;
            case R.id.dialog_nc_confirm_rl:
                // jumpToMain(FriendsActivity.this);
                finish();
                break;
            //跳转相册
//            case R.id.dialog_rl_gally:// TODO
//                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//                intent1.setDataAndType(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent1, REQUEST_CODE_GALLY);
//                break;

//            case R.id.dialog_rl_camera:
//                tempFile = new File(Constant.CHAT_CACHE_DIR,
//                        "avartar-" + UUID.randomUUID()
//                                + ".photo");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    Uri imageUri = FileProvider.getUriForFile(context, "com.hjnerp.takephoto.fileprovider", tempFile);//通过FileProvider创建一个content类型的Uri
//                    Intent intent = new Intent();
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
//                    startActivityForResult(intent, REQUEST_CODE＿IMAGE_CAPTURE);
//                } else {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
//                    startActivityForResult(intent, REQUEST_CODE＿IMAGE_CAPTURE);
//                }
//                break;
            //点击更换头像
            case R.id.user_head_layout:
//                showSelectPicDialog();
                if (friend.getFriendid().equals(sputil.getMyId())) // 我自己的详情
                    popupWindow();
                break;
            //点击头像查看照片
            case R.id.user_head_avatar:
                Bundle bundle = new Bundle();
                bundle.putString("photoUrl", photoUrl);
                intentActivity(ShowPortraitActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    /**
     * 判断点击的是什么
     *
     * @param index
     */
    private void dateCommit(int index) {
        switch (index) {
            case Constant.HANDLERTYPE_0:
                //跳转相机
                tempFile = new File(Constant.CHAT_CACHE_DIR,
                        "avartar-" + UUID.randomUUID()
                                + ".photo");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri imageUri = FileProvider.getUriForFile(context, "com.hjnerp.takephoto.fileprovider", tempFile);//通过FileProvider创建一个content类型的Uri
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                    startActivityForResult(intent, REQUEST_CODE＿IMAGE_CAPTURE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                    startActivityForResult(intent, REQUEST_CODE＿IMAGE_CAPTURE);
                }
                break;
            case Constant.HANDLERTYPE_1:
                //跳转相册
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, REQUEST_CODE_GALLY);
                break;
        }
    }

    /**
     * 拨打电话弹框
     *
     * @param phone
     */
    private void showDialog(final String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(phone);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                startActivity(phoneIntent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 底部popupwindow弹框
     */
    private void popupWindow() {
        setTheme(R.style.ActionSheetStyleiOS7);
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍照", "从手机相册选择")
                .setCancelableOnTouchOutside(true)
                .setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        dateCommit(index);
    }
}

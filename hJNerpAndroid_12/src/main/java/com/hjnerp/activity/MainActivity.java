package com.hjnerp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjnerp.activity.im.SelectGroupChatMemberDeptActivity;
import com.hjnerp.adapter.TabPagerAdapter;
import com.hjnerp.business.Ctlm1345Update;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.fragment.BusinessFragment;
import com.hjnerp.fragment.ContactFragment;
import com.hjnerp.fragment.ImFragment;
import com.hjnerp.fragment.ImFragment.OnImListener;
import com.hjnerp.fragment.MyInforMation;
import com.hjnerp.fragment.WorkFragment;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.UserInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.IQ;
import com.hjnerp.service.WebSocketService;
import com.hjnerp.service.WorkService;
import com.hjnerp.util.Command;
import com.hjnerp.util.Log;
import com.hjnerp.util.VersionManager;
import com.hjnerp.widget.ChangeColorIconWithTextView;
import com.hjnerpandroid.R;

import org.apache.cordova.LOG;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("RtlHardcoded")
public class MainActivity extends ActivitySupport implements
        OnPageChangeListener, OnImListener, OnClickListener {
    private static Toast mToast = null;
    protected UserInfo myInfo;// 我的用户表信息
//    public static DrawerLayout mDrawerLayout;
    /**
     * ViewPager对象的引用
     */
    private ViewPager mViewPager;
    /**
     * 装载Fragment的容器，我们的每一个界面都是一个Fragment
     */
    private List<Fragment> mFragmentList;
    // private AddPopupWindow menuAdd; // 弹出框
    // private QueryPopupWindow menuQuery;
    private TextView tv_tab_qixin, tv_tab_work, tv_tab_business,
            tv_tab_contact, main_tab_my_unread;

    private ImFragment imFragment;
    private ContactFragment contactFragment;
    private BusinessFragment businessFragment;
    private MyInforMation myInforMation;

    private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
    private ContacterReceiver receiver = null;

    public static boolean need_fresh_businessmenu = false;

    private FrameLayout frameLayout_work;
    private FrameLayout frameLayout_im;
    private FrameLayout frameLayout_contact;
    public static int WORK_COUNT;

    private TextView main_title_text;
    //    private ImageView main_emp_icon;
    private ImageView main_search_icon;
    //    private ImageView main_phone_icon;
//    private ImageView main_group_icon;
    private ImageView main_find_icon;
    private ImageView main_refresh_icon;
    private List<Integer> title_list = new ArrayList<>();
    public static MainActivity newMain = null;
    private WorkReceiver workReceiver;
    private SharedPreferences sharedPref2;
    private SharedPreferences.Editor editor2;
    private NotificationManager nm;
    private Thread mThread;
    private Intent workServer;
    private boolean isNotMeizheng = false;//如果不是美正,在美正项目中默认为false，为了隐藏一些菜单
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    reflashWorkMenu();
                    break;
                case 1:
                    int number = (int) msg.obj;

                    if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                        sendToXiaoMi(number);
                    } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                        sendToSony(number);
                    } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
                        sendToSamsumg(number);
                    } else {
                        sendToOther(number);
                    }

                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void sendToOther(int number) {
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound)
                .setContentTitle("您有" + number + "条未读消息")
                .setTicker("您有" + number + "条未读消息")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon2)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent); // 关联PendingIntent
        notification = builder.build();
        nm.notify(101010, notification);
//        editor2.putInt("isNoti", number);
//        editor2.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        newMain = this;
        initView();
    }

    private void initView() {
        Log.v("show", "initView。。。。。。。。。。。。。。");
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        myInfo = QiXinBaseDao.queryCurrentUserInfo();
        // 从资源文件在获取Tab的title
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.clear();
        // //将Fragment加入到List中，并将Tab的title传递给Fragment
        if (isNotMeizheng) {//如果不是美正，就新建这些单据
            // 企信
            imFragment = new ImFragment();
            mFragmentList.add(imFragment);
            // 通讯录
            contactFragment = new ContactFragment();
            mFragmentList.add(contactFragment);
        }


        // 工作流
        if (sputil.isWorkFlow()) {
            mFragmentList.add(new WorkFragment());
        }
        // 功能
        businessFragment = new BusinessFragment();
        mFragmentList.add(businessFragment);


        // 我的
        myInforMation = new MyInforMation();
        mFragmentList.add(myInforMation);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        // 设置Adapter
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), mFragmentList));
        // 设置监听
        mViewPager.setOnPageChangeListener(this);
        // 设置预加载数为3
        mViewPager.setOffscreenPageLimit(3);
        frameLayout_work = (FrameLayout) findViewById(R.id.id_frame_workflow);
        frameLayout_im = (FrameLayout) findViewById(R.id.id_frame_im);
        frameLayout_contact = (FrameLayout) findViewById(R.id.id_frame_contact);
        tv_tab_qixin = (TextView) findViewById(R.id.main_tab_im_unread);
        tv_tab_work = (TextView) findViewById(R.id.main_tab_work_unread);
        tv_tab_business = (TextView) findViewById(R.id.main_tab_business_unread);
        tv_tab_contact = (TextView) findViewById(R.id.main_tab_contact_unread);
        main_tab_my_unread = (TextView) findViewById(R.id.main_tab_my_unread);


//        main_emp_icon = (ImageView) findViewById(R.id.main_emp_icon);
//        main_group_icon = (ImageView) findViewById(R.id.main_group_icon);
//        main_phone_icon = (ImageView) findViewById(R.id.main_phone_icon);
        main_search_icon = (ImageView) findViewById(R.id.main_search_icon);
        main_find_icon = (ImageView) findViewById(R.id.main_find_icon);
        main_title_text = (TextView) findViewById(R.id.main_title_text);
        main_refresh_icon = (ImageView) findViewById(R.id.main_refresh_icon);

        upload1345();

        if (isNotMeizheng) {
            title_list.add(R.string.main_tab_im);
            title_list.add(R.string.main_tab_contact);
        }

        if (sputil.isWorkFlow()) {
            title_list.add(R.string.main_tab_workflow);
        }
        title_list.add(R.string.main_tab_business);

        title_list.add(R.string.main_tab_my);

        sharedPref2 = this.getSharedPreferences(
                "main", Context.MODE_PRIVATE);
        editor2 = sharedPref2.edit();
        // 获取Action实例我们使用getSupportActionBar()方法
        initTabIndicator();
//        initEvents();

        // 启动服务
        registerMReceiver();
        startService();
        registerWork();
        checkNew();

    }

    private void checkNew() {
        VersionManager vm = VersionManager.getSharedInstance();
        vm.checkVersionUpgrade(true, this, new VersionManager.OnUpgradeResultListener() {
            @Override
            public void onUpgradeResult(boolean success, String msg) {

            }
        });
    }

    private void registerWork() {
        workReceiver = new WorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hjnerp.service.Work");
        registerReceiver(workReceiver, filter);
        if (workServer == null) {
            workServer = new Intent(context, WorkService.class);
            context.startService(workServer);
        }
    }

    //下载基础数据
    private void upload1345() {
        mThread = new Thread() {
            @Override
            public void run() {//new Ctlm1345Update(new String[]{"ctlm4101"}, new String[]{"1=1"}, null).action();
                new Ctlm1345Update(new String[]{"ctlm1345"}, new String[]{"2=2"}, new Command.OnResultListener() {
                    @Override
                    public void onResult(boolean success) {
                        if (success) {
//                            ToastUtil.ShowShort(context, "用户信息加载完成");
                        } else {
//                            ToastUtil.ShowShort(context, "用户信息加载失败");
                        }
                    }
                }).action();
            }
        };
        mThread.start();
    }

    private void initTabIndicator() {
        Log.v("show", "initTabIndicator。。。。。。。。。。。。。。");
        ChangeColorIconWithTextView buss = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_business);
        ChangeColorIconWithTextView my = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_my);

        if (sputil.isWorkFlow()) {
            ChangeColorIconWithTextView workflowview = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_workflow);
            mTabIndicator.add(workflowview);
            workflowview.setOnClickListener(this);
        } else {
            frameLayout_work.setVisibility(View.GONE);
        }
        if (isNotMeizheng) {
            ChangeColorIconWithTextView contact = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_contact);
            ChangeColorIconWithTextView im = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_im);

            mTabIndicator.add(im);
            mTabIndicator.add(contact);
            im.setOnClickListener(this);
            contact.setOnClickListener(this);
            im.setIconAlpha(1.0f);


        } else {
            frameLayout_im.setVisibility(View.GONE);
            frameLayout_contact.setVisibility(View.GONE);
        }
        mTabIndicator.add(buss);
        mTabIndicator.add(my);
        mViewPager.setCurrentItem(1);
        buss.setOnClickListener(this);
        my.setOnClickListener(this);
    }


    private void registerMReceiver() {
        Log.v("show", "registerMReceiver。。。。。。。。。。。。。。");
        // 初始化广播
        receiver = new ContacterReceiver();
        // // // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        // // // 好友请求
        filter.addAction(WebSocketService.ACTION_ON_MSG);// 全部msg
        filter.addAction(WebSocketService.ACTION_ON_IQ);// 全部iq
        // //
        registerReceiver(receiver, filter);
    }

    // TODO 刷新聊天新消息气泡
    @Override
    public void refreshIm() {
        if (imFragment != null) {
            imFragment.refreshMessage();
        }

    }

    // 刷新通讯录新联系人提醒气泡
    @Override
    public void refreshContact() {
        if (contactFragment != null) {
            contactFragment.refreshList();
            int nCount = QiXinBaseDao.queryNewTempContactInfosCounts();
            if (nCount == 0) {
                tv_tab_contact.setVisibility(View.GONE);
            } else {
                ContactFragment.ALL_NEW_MSG_COUNTS = 0;
                ContactFragment.ALL_NEW_MSG_COUNTS = nCount;

                tv_tab_contact.setText(String.valueOf(nCount));
                tv_tab_contact.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onImReflash(String msg) {
        if ("LEFT".equalsIgnoreCase(msg)) {
//            mDrawerLayout.openDrawer(Gravity.LEFT);
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
//                    Gravity.LEFT);

        } else {
            int nCount = QiXinBaseDao.queryChatNewChatMsgCounts();

            if (nCount == 0) {
                tv_tab_qixin.setVisibility(View.GONE);
            } else {
                tv_tab_qixin.setText(String.valueOf(msg));
                tv_tab_qixin.setVisibility(View.VISIBLE);
            }
        }
    }

    // 刷新业务菜单
    public void readWorkMenu() {
        if (sputil.isWorkFlow()) {
            new Thread() {
                public void run() {
                    try {
                        IQ iq = HJWebSocketManager.getInstance().requestIQ(
                                ChatConstants.iq.FEATURE_WORK_NUMBER);
                        Log.i("info", "IQ==" + iq);
                        Map<String, Object> data = (Map<String, Object>) iq.data;
                        WORK_COUNT = (int) Float.parseFloat(data.get("content")
                                .toString());

                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        //
                        Log.i("info", "获取workcount异常：" + e.toString());
                    }
                }

                ;
            }.start();
        }

    }

    public void reflashWorkMenu() {

        try {
            if (WORK_COUNT > 0) {
                tv_tab_work.setText(WORK_COUNT + "");
                tv_tab_work.setVisibility(View.VISIBLE);
//                ToastUtil.ShowShort(this, System.currentTimeMillis() + "");
                int numb = sharedPref2.getInt("isNoti", 0);
                long time = sharedPref2.getLong("time", 0);
                if (System.currentTimeMillis() > time && numb < WORK_COUNT) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = WORK_COUNT - numb;
                    WorkFragment.workFragment.listview.setRefreshing();
                    editor2.putInt("isNoti", WORK_COUNT);
                    editor2.putLong("time", System.currentTimeMillis());
                    editor2.commit();
                    handler.sendMessage(message);
                } else {
                    editor2.putInt("isNoti", WORK_COUNT);
                    editor2.putLong("time", System.currentTimeMillis());
                    editor2.commit();
                }
            } else {
                tv_tab_work.setVisibility(View.GONE);
                editor2.putLong("time", System.currentTimeMillis());
                editor2.putInt("isNoti", 0);
                editor2.commit();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.i("info", e.toString());
        }
    }

    // 刷work菜单
    public void reflashBusinessMenu() {
        if (businessFragment != null) {
            businessFragment.refreshList();
        }
        if (BusinessFragment.isPoPoVisible) {
            tv_tab_work.setVisibility(View.VISIBLE);
        } else {
            tv_tab_work.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

//    public void unabledrefresh() {
//        main_search_icon.setVisibility(View.GONE);
//    }
//
//    public void enabledrefresh() {
//        main_search_icon.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIconWithTextView left = mTabIndicator.get(position);
            ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
        main_title_text.setText(title_list.get(position));

        if (isNotMeizheng && position == 0) {
            main_title_text.setVisibility(View.VISIBLE);
            main_search_icon.setVisibility(View.GONE);
            main_find_icon.setVisibility(View.VISIBLE);
            main_refresh_icon.setVisibility(View.GONE);
            if (mToast != null) {
                mToast.cancel();

            }

        }
        if ((!isNotMeizheng && position == 0) || (isNotMeizheng && sputil.isWorkFlow() && position == 1)) {
            main_title_text.setVisibility(View.VISIBLE);
            main_search_icon.setVisibility(View.VISIBLE);
            main_find_icon.setVisibility(View.GONE);
            main_refresh_icon.setVisibility(View.GONE);
            main_search_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkFragment.workFragment.search(main_search_icon);
                }
            });
            main_refresh_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkFragment.workFragment.listview.setRefreshing();
                }
            });
            nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancelAll();
            if (mToast != null) {
                mToast.cancel();

            }
        }
        if ((!isNotMeizheng && position == 1) || (isNotMeizheng && sputil.isWorkFlow() && position == 2) || (isNotMeizheng && !sputil.isWorkFlow() && position == 1)) {
//            main_emp_icon.setVisibility(View.GONE);
            main_title_text.setVisibility(View.VISIBLE);
            main_search_icon.setVisibility(View.GONE);
//            main_phone_icon.setVisibility(View.GONE);
            main_find_icon.setVisibility(View.GONE);
//            main_title_text.setTextColor(0xff27a4e3);
//            main_group_icon.setVisibility(View.GONE);
            main_refresh_icon.setVisibility(View.VISIBLE);
            main_refresh_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusinessFragment.businessFragment.refreshList();
                }
            });
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (mToast != null) {
                    mToast.setText("未取得手机存储权限");
                    mToast.setDuration(Toast.LENGTH_SHORT);
                } else {
                    mToast = Toast.makeText(this, "未取得手机存储权限", Toast.LENGTH_SHORT);

                }
                mToast.show();

            }
        }
        if ((isNotMeizheng && sputil.isWorkFlow() && position == 3) || (isNotMeizheng && !sputil.isWorkFlow() && position == 2)) {
//            main_emp_icon.setVisibility(View.GONE);
            main_title_text.setVisibility(View.VISIBLE);
            main_search_icon.setVisibility(View.GONE);
//            main_phone_icon.setVisibility(View.GONE);
            main_find_icon.setVisibility(View.GONE);
//            main_group_icon.setVisibility(View.GONE);
//            main_title_text.setTextColor(0xff27a4e3);
            main_refresh_icon.setVisibility(View.GONE);
            main_refresh_icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactFragment.contactFragment.getHttpData();
                }
            });
            if (mToast != null) {
                mToast.cancel();

            }

        }
        if ((!isNotMeizheng && position == 2) || (isNotMeizheng && sputil.isWorkFlow() && position == 4) || (isNotMeizheng && !sputil.isWorkFlow() && position == 3)) {
//            main_emp_icon.setVisibility(View.GONE);
            main_title_text.setVisibility(View.VISIBLE);
            main_search_icon.setVisibility(View.GONE);
//            main_phone_icon.setVisibility(View.VISIBLE);
            main_find_icon.setVisibility(View.GONE);
//            main_title_text.setTextColor(0xff27a4e3);
//            main_group_icon.setVisibility(View.VISIBLE);
            main_refresh_icon.setVisibility(View.GONE);
            if (mToast != null) {
                mToast.cancel();

            }

        }
//        main_find_icon.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),
//                        SearchQiXinFriendsActivity.class);
//                startActivity(intent);
//            }
//        });
//        main_phone_icon.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
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
//        });
        main_find_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),
                        SelectGroupChatMemberDeptActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isAvilible(String packageName) {
        //获取packagemanager
        final PackageManager packageManager = this.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        unregisterReceiver(workReceiver);
        receiver = null;
        workReceiver = null;
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        if (receiver == null) {
            registerMReceiver();
        }
        if (workReceiver == null) {
            registerWork();
        }
        refreshIm();
        // refreshImActionbarTab();
        refreshContact();
        readWorkMenu();
//		 reflashBusinessMenu();

        // refreshContactActionbarTab();
        // if(need_fresh_businessmenu){
        // reflashBusinessMenu();
        // need_fresh_businessmenu = false;
        // }
        Log.v("show", "onResume。。。。。。。。。。。");
        super.onResume();
    }


    public void LogOut(Context context) {
//        mDrawerLayout.closeDrawer(Gravity.LEFT);
        final Dialog noticeDialog = new Dialog(context, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);

        TextView dialog_cancel_rl, dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog.findViewById(R.id.dialog_notice_tv);
        notice.setText("确认要退出和佳ERP吗?");
        dialog_cancel_rl = (TextView) noticeDialog
                .findViewById(R.id.dialog_cancel_tv);
        dialog_confirm_rl = (TextView) noticeDialog
                .findViewById(R.id.dialog_confirm_tv);
        dialog_cancel_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
            }
        });
        dialog_confirm_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
                isExit();
            }
        });

        noticeDialog.show();
    }

    @Override
    public void isExit() {
        LogoutTask logoutTask = new LogoutTask(this);
        logoutTask.execute();
    }

    private class ContacterReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshIm();
            refreshContact();
            readWorkMenu();
        }
    }

    private class WorkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            readWorkMenu();
        }
    }

    private void sendToXiaoMi(int number) {
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        boolean isMiUIV6 = true;
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound)
                    .setContentTitle("您有" + number + "条未读消息")
                    .setTicker("您有" + number + "条未读消息")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.icon2)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setContentIntent(pendingIntent); // 关联PendingIntent
            notification = builder.build();

            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, number);// 设置信息数
            field = notification.getClass().getField("extraNotification");
            field.setAccessible(true);
//            editor2.putInt("isNoti", number);
//            editor2.commit();
            field.set(notification, miuiNotification);
        } catch (Exception e) {
            e.printStackTrace();
            //miui 6之前的版本
            isMiUIV6 = false;
            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra("android.intent.extra.update_application_component_name", getPackageName());
            localIntent.putExtra("android.intent.extra.update_application_message_text", number);
//            editor2.putInt("isNoti", number);
//            editor2.commit();
            sendBroadcast(localIntent);
        } finally {
            if (notification != null && isMiUIV6) {
                //miui6以上版本需要使用通知发送
                nm.notify(101010, notification);
            }
        }

    }

    private void sendToSony(int number) {
        boolean isShow = true;
        if ("0".equals(number)) {
            isShow = false;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        Intent localIntent = new Intent();
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", pendingIntent);//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", getPackageName());//包名
        sendBroadcast(localIntent);

//        editor2.putInt("isNoti", number);
//        editor2.commit();
    }

    private void sendToSamsumg(int number) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        localIntent.putExtra("badge_count", number);//数字
        localIntent.putExtra("badge_count_package_name", getPackageName());//包名
        localIntent.putExtra("badge_count_class_name", pendingIntent); //启动页
        sendBroadcast(localIntent);

//        editor2.putInt("isNoti", number);
//        editor2.commit();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_im:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_workflow:
                if (isNotMeizheng) {
                    mTabIndicator.get(1).setIconAlpha(1.0f);
                    mViewPager.setCurrentItem(1, false);
                } else {
                    mTabIndicator.get(0).setIconAlpha(1.0f);
                    mViewPager.setCurrentItem(0, false);
                }

                break;
            case R.id.id_indicator_business:
                if (isNotMeizheng) {
                    if (sputil.isWorkFlow()) {
                        mTabIndicator.get(2).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(2, false);
                    } else {
                        mTabIndicator.get(1).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(1, false);
                    }
                } else {
                    if (sputil.isWorkFlow()) {
                        mTabIndicator.get(1).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(1, false);
                    } else {
                        mTabIndicator.get(0).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(0, false);
                    }
                }


                break;
            case R.id.id_indicator_contact:
                if (sputil.isWorkFlow()) {
                    mTabIndicator.get(3).setIconAlpha(1.0f);
                    mViewPager.setCurrentItem(3, false);
                } else {
                    mTabIndicator.get(2).setIconAlpha(1.0f);
                    mViewPager.setCurrentItem(2, false);
                }

                break;
            case R.id.id_indicator_my:
                if (isNotMeizheng) {
                    if (sputil.isWorkFlow()) {
                        mTabIndicator.get(4).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(4, false);
                    } else {
                        mTabIndicator.get(3).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(3, false);
                    }
                } else {
                    if (sputil.isWorkFlow()) {
                        mTabIndicator.get(2).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(2, false);
                    } else {
                        mTabIndicator.get(1).setIconAlpha(1.0f);
                        mViewPager.setCurrentItem(1, false);
                    }
                }

                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("status", "onstop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor2.putInt("isNoti", 0);
        editor2.commit();
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();

        context.stopService(workServer);
        finish();
    }

    //    private void initEvents() {
//        Log.v("show", "initEvents。。。。。。。。。。。。。。");
//        mDrawerLayout.setDrawerListener(new DrawerListener() {
//            @Override
//            public void onDrawerStateChanged(int newState) {
//            }
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//
//                View mContent = mDrawerLayout.getChildAt(0);
//                View mMenu = drawerView;
//                float scale = 1 - slideOffset;
//                float rightScale = 0.8f + scale * 0.2f;
//
//                if (drawerView.getTag().equals("LEFT")) {
//
//                    float leftScale = 1 - 0.3f * scale;
//
//                    ViewHelper.setScaleX(mMenu, leftScale);
//                    ViewHelper.setScaleY(mMenu, leftScale);
//                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
//                    ViewHelper.setTranslationX(mContent,
//                            mMenu.getMeasuredWidth() * (1 - scale));
//                    ViewHelper.setPivotX(mContent, 0);
//                    ViewHelper.setPivotY(mContent,
//                            mContent.getMeasuredHeight() / 2);
//                    mContent.invalidate();
//                    ViewHelper.setScaleX(mContent, rightScale);
//                    ViewHelper.setScaleY(mContent, rightScale);
//                } else {
//                    ViewHelper.setTranslationX(mContent,
//                            -mMenu.getMeasuredWidth() * slideOffset);
//                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
//                    ViewHelper.setPivotY(mContent,
//                            mContent.getMeasuredHeight() / 2);
//                    mContent.invalidate();
//                    ViewHelper.setScaleX(mContent, rightScale);
//                    ViewHelper.setScaleY(mContent, rightScale);
//                }
//
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//
//                mDrawerLayout.setDrawerLockMode(
//                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
//            }
//        });
//    }

    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

//    public void OpenLeftMenu() {
//        mDrawerLayout.openDrawer(Gravity.LEFT);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
//                Gravity.LEFT);
//
//    }

}

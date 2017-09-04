package com.hjnerp.common;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.FragmentActivity;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.hjnerp.activity.LoginActivity;
import com.hjnerp.db.DataBaseHelper;
import com.hjnerp.model.NearBuild;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.util.ImageLoaderHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

import static com.hjnerp.business.view.WidgetMap.context;

/**
 * 完整的退出应用.
 *
 * @author 李庆义
 */
public class EapApplication extends Application {
    /**
     * 服务器主机HTTP地址
     */
    public static String URL_SERVER_HOST_HTTP = "";
    /**
     * 公用数据库名
     */
    public static final String DATABASE_NAME = "hjnerp.db";
    /**
     * 公用数据库版本
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * 主UI线程全局Handler对象
     */
    public static final String EXTRA_MAIN_HANDLER = "main_handler";

    //表格控件中表示是TextView控件
    public static final String TEXT_TYPE = "textView";
    //表格控件中表示是EditText控件
    public static final String EDIT_TYPE = "editView";
    private static EapApplication instance;

    /**
     * ActivitySupport据此变量判断新聊天消息是否需要在Actionbar红圈提醒
     */
    public static String EXTRA_CURRENT_CHAT_FRIENDORGROUP_ID = "";


    private DataBaseHelper dataBaseHelper;
    private Stack<FragmentActivity> activityStack;
    private HashMap<String, Object> map = new HashMap<String, Object>();

    /**
     * @author haijian
     * 保存当前签到位置
     */
    public NearBuild near;

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);
//	    CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());  
        Stetho.initializeWithDefaults(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
//        Stetho.initializeWithDefaults(this);
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(new DumperPluginsProvider() {
//                    @Override
//                    public Iterable<DumperPlugin> get() {
//                        return new Stetho.DefaultDumperPluginsBuilder(getApplicationContext())
//                                .finish();
//                    }
//                })
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
//        new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();
        //初始化地址
        HttpClientManager.open();
        ImageLoader.getInstance().init(ImageLoaderHelper.getImageLoaderConfiguration());

        SDKInitializer.initialize(this.getApplicationContext()); // baidu地图API初始化
        dataBaseHelper = new DataBaseHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        map.put(EXTRA_MAIN_HANDLER, new Handler());

        //必须调用初始化
        OkGo.init(this);
        //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
        OkGo.getInstance()

                // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                .debug("HJNerpAndroid", Level.INFO, true)

                //如果使用默认的 60秒,以下三行也不需要传
                .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                .setCacheMode(CacheMode.NO_CACHE)

                //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE);
    }


    public void exitAPP() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
                restartIntent);
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public DataBaseHelper getDataBaseHelper() {
        return dataBaseHelper;
    }


    /**
     * 单一实例
     */
    public static final EapApplication getApplication() {
        return instance;
    }

    private static final void setApplication(EapApplication eap) {
        if (instance == null)
            instance = eap;
    }

    /**
     * 获取程序实例级全局的变量, key请使用EXTRA_开头的常量
     */
    public Object getExtra(String key) {
        return map.get(key);
    }

    /**
     * 设置程序实例级全局的变量, key请使用EXTRA_开头的常量
     */
    public Object putExtra(String key, Object value) {
        return map.put(key, value);
    }

    /**
     * 移除程序实例级全局的变量, key请使用EXTRA_开头的常量
     */
    public Object removeExtra(String key) {
        return map.remove(key);
    }

    // 添加Activity到容器中
    public void addActivity(FragmentActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<FragmentActivity>();
        }

        activityStack.add(activity);
    }

    /*
     *  获取所有Activity
     * */
    public Stack<FragmentActivity> allActivity() {

        return activityStack;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public FragmentActivity currentActivity() {
        FragmentActivity activity = activityStack.lastElement();
        return activity;
    }

    public void removeActivity(FragmentActivity activity) {
        if (activity != null) {

            activityStack.remove(activity);
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        FragmentActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(FragmentActivity activity) {
        if (activity != null) {

            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Stack<FragmentActivity> activitys = new Stack<FragmentActivity>();
        for (FragmentActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activitys.add(activity);
            }
        }

        for (FragmentActivity activity : activitys) {
            finishActivity(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public boolean hasNoAnyActivityStack() {
        return activityStack == null || activityStack.isEmpty();
    }

    // 遍历所有Activity并finish
    public void exit() {
        try {
            finishAllActivity();
            int pid = Process.myPid();
            Process.killProcess(pid);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

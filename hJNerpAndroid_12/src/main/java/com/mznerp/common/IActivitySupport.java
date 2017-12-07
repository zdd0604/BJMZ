package com.mznerp.common;

import android.app.ProgressDialog;
import android.content.Context;

import com.mznerp.model.LoginConfig;
import com.mznerp.widget.WaitDialogRectangle;

/**
 * Activity帮助支持类接口.
 * 
 * @author 李庆义
 */
public interface IActivitySupport {
	
	
	/**
	 * 
	 * 获取EimApplication.
	 * 
	 * @author 李庆义
	 * @update 2012-7-6 上午9:05:51
	 */
	public abstract EapApplication getEapApplication();

	/**
	 * 
	 * 终止服务.
	 * 
	 * @author 李庆义
	 * @update 2012-7-6 上午9:05:51
	 */
	public abstract void stopService();

	/**
	 * 
	 * 开启服务.
	 * 
	 * @author 李庆义
	 * @update 2012-7-6 上午9:05:44
	 */
	public abstract void startService();

	/**
	 * 
	 * 校验网络-如果没有网络就弹出设置,并返回true.
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-7-6 上午9:03:56
	 */
	public abstract boolean validateInternet();

	/**
	 * 
	 * 校验网络-如果没有网络就返回true.
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-7-6 上午9:05:15
	 */
	public abstract boolean hasInternetConnected();

	/**
	 * 
	 * 退出应用.
	 * 
	 * @author 李庆义
	 * @update 2012-7-6 上午9:06:42
	 */
	public abstract void isExit();

	/**
	 * 
	 * 判断GPS是否已经开启.
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-7-6 上午9:04:07
	 */
	public abstract boolean hasLocationGPS();

	/**
	 * 
	 * 判断基站是否已经开启.
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-7-6 上午9:07:34
	 */
	public abstract boolean hasLocationNetWork();

	/**
	 * 
	 * 检查内存卡.
	 * 
	 * @author 李庆义
	 * @update 2012-7-6 上午9:07:51
	 */
	public abstract void checkMemoryCard();

	/**
	 * 
	 * 获取进度条.
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-7-6 上午9:14:38
	 */
	public abstract ProgressDialog getProgressDialog();

	
	public abstract WaitDialogRectangle getWaitDialogRectangle();
	/**
	 * 
	 * 返回当前Activity上下文.
	 * 
	 * @return
	 * @author 李庆义
	 * @update 2012-7-6 上午9:19:54
	 */
	public abstract Context getContext();

	 

	/**
	 * 
	 * 保存用户配置.
	 * 
	 * @param loginConfig
	 * @author 李庆义
	 * @update 2012-7-6 上午9:58:31
	 */
	public void saveLoginConfig(LoginConfig loginConfig);

	/**
	 * 
	 * 获取用户配置.
	 * 
	 * @param loginConfig
	 * @author 李庆义
	 * @update 2012-7-6 上午9:59:49
	 */
	public LoginConfig getLoginConfig();

	 

	/**
	 * 
	 * 发出Notification的method.
	 * 
	 * @param iconId
	 *            图标
	 * @param contentTitle
	 *            标题
	 * @param contentText
	 *            你内容
	 * @param activity
	 * @author 李庆义
	 * @update 2012-5-14 下午12:01:55
	 */
	public void setNotiType(int iconId, String contentTitle,
			String contentText, Class activity, String from);
	
	
	
}

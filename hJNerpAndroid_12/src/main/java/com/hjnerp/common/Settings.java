package com.hjnerp.common;

import android.content.Context;

/**
 * 设置功能类
 * @author John Kenrinus Lee 
 * @date 2014年6月12日
 */
public class Settings
{
	private Settings()
	{
	}
	
	private static final class SettingsHolder
	{
		static final Settings instance = new Settings();
	}
	
	public static final Settings getSharedInstance()
	{
		return SettingsHolder.instance;
	}
	
	private boolean newMessageNotify = true;	//新消息是否提醒
	private boolean newMessageVibrate = true;		//新消息震动提醒
	private boolean newMessageMusic = true;	//新消息音乐提醒
	
	public boolean isNewMessageNotify()
	{
		return newMessageNotify;
	}
	public void setNewMessageNotify(Context context, boolean newMessageNotify)
	{
		this.newMessageNotify = newMessageNotify;
	}
	public boolean isNewMessageVibrate()
	{
		return newMessageVibrate;
	}
	public void setNewMessageVibrate(Context context, boolean newMessageVibrate)
	{
		this.newMessageVibrate = newMessageVibrate;
	}
	public boolean isNewMessageMusic()
	{
		return newMessageMusic;
	}
	public void setNewMessageMusic(Context context, boolean newMessageMusic)
	{
		this.newMessageMusic = newMessageMusic;
	}
}

package com.hjnerp.service;
 
/**
 * WebSocket后台服务类
 * @author John Kenrinus Lee
 */
public class WebSocketService 
{
	/** 当websocket成功连接到服务器后发送此广播*/
	public static final String ACTION_CONNECTED = "com.hjnerp.service.websocket.action.CONNECTED";
	/** 当websocket连接中断后发送此广播*/
	public static final String ACTION_DISCONNECTED = "com.hjnerp.service.websocket.action.DISCONNECTED";
	/** 当websocket连接与交互中发生任何非运行时异常都可能发送此广播,监听此Action的广播接收器取得的Intent对象的getSerializableExtra("data")是Throwable类型*/
	public static final String ACTION_ERROR = "com.hjnerp.service.websocket.action.EXCEPTION";
	/** 当服务收到Msg结构的消息时发出此广播,监听此Action的广播接收器取得的Intent对象的getSerializableExtra("data")是Msg类型*/
	public static final String ACTION_ON_MSG = "com.hjnerp.service.websocket.action.MSG";
	/** 当服务收到Presence结构的消息时发出此广播,监听此Action的广播接收器取得的Intent对象的getSerializableExtra("data")是Presence类型*/
	public static final String ACTION_ON_PRESENCE = "com.hjnerp.service.websocket.action.PRESENCE";
	/** 
	 * 当服务收到IQ结构的消息时发出此广播,监听此Action的广播接收器取得的Intent对象的getSerializableExtra("data")是Msg类型<br/>
	 * 注:如果sendIQ时bind了ResponseCallback将由ResponseCallback处理响应回的IQ,不会触发此广播;
	 * 注:可能需要处理的至少包括客户端被迫下线通知和新版本下载通知的推送
	 */
	public static final String ACTION_ON_IQ = "com.hjnerp.service.websocket.action.IQ";
	/** 当调用服务中的某些方法而连接没有建立时发出此广播以在前端引导用户开启网络,在非正常状态下执行的相关操作应保存,以在状态恢复正常时补执行*/
	public static final String ACTION_NO_CONNECT = "com.hjnerp.service.websocket.action.NO_CONNECT";
	/** 当调用服务中的某些方法而session没有设置时发出此广播以做登录操作并全局存储session_id,在非正常状态下执行的相关操作应保存,以在状态恢复正常时补执行*/
	public static final String ACTION_NO_SESSION = "com.hjnerp.service.websocket.action.NO_SESSION";
	
 

	 
}

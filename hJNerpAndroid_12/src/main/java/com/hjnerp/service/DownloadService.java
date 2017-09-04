package com.hjnerp.service;

import android.app.Service;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.NameableThreadFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <br>下载支持服务类 <br>
 * 注:本类所有接口方法都可传入Handler对象,对于下载的状态(可参考此类常量),如果传入,则使用此Handler对象发送消息,否则将发送广播;
 *    不论是收到Message还是Intent对象,都可查看其中的Bundle,里面可使用getString方法;
 *    getString中如果传入"file"则获得当初请求下载的文件名,如果传入"content"则获得此次下载的描述,格式为"SimpleClassName.MethodName:中文描述".
 * @author John Kenrinus Lee
 */
public class DownloadService extends Service
{
	/** 图像资源下载在SDCARD上的基础路径*/
	public static final String IMAGES_BASE_PATH = "/images_hejia/";
	/** 当下载失败时, 如果广播,那么Intent中action为此字符串*/
	public static final String ACTION_DOWNLOAD_FAIL = "com.hjnerp.service.download.action.DOWNLOAD_FAIL";
	/** 当下载成功时, 如果广播,那么Intent中action为此字符串*/
	public static final String ACTION_DOWNLOAD_SUCCESS = "com.hjnerp.service.download.action.DOWNLOAD_SUCCESS";
	/** 当下载失败时, 如果发送消息,那么Message中what属性为此值*/
	public static final int WHAT_DOWNLOAD_FAIL = -1;
	/** 当下载成功时, 如果发送消息,那么Message中what属性为此值*/
	public static final int WHAT_DOWNLOAD_SUCCESS = 0;
	
	DownloadBinder binder;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		binder = new DownloadBinder();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(binder != null)
		{
			binder.close();
			binder = null;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return binder;
	}
	
	@Override
	public boolean onUnbind(Intent intent)
	{
		return true;
	}
	
	void scheduleSilentGetImage(String uri, String fileName, Handler handler)
	{
		String path = IMAGES_BASE_PATH + fileName;
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("download-service");
		try
		{
			HttpGet hg = new HttpGet(uri);
			HttpResponse httpResponse = null;
			try
			{
				httpResponse = httpClient.execute(hg);
			}
			catch (ClientProtocolException e)
			{
				sendFail(path, "DownloadService.scheduleSilentGetImage:协议异常,请求失败", handler);
				httpClient.close();
				return;
			}
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == statusCode)
			{
				String status = httpResponse.getFirstHeader("Downloadable-Status").getValue();
				if ("0".equals(status))
				{//成功
					File file = new File(Environment.getExternalStorageDirectory(),
							IMAGES_BASE_PATH + fileName);
					HttpEntity httpEntity = httpResponse.getEntity();
					try
					{
						httpEntity.writeTo(new FileOutputStream(file));
					}
					catch (FileNotFoundException e)
					{
						sendFail(path, "DownloadService.scheduleSilentGetImage:下载文件保存路径无效", handler);
						httpClient.close();
						return;
					}
					sendSuccess(path, "DownloadService.scheduleSilentGetImage:请求的文件已成功下载", handler);
					httpClient.close();
					return;
				}
				else
				{//业务失败
					sendFail(path, "DownloadService.scheduleSilentGetImage:" + ChatPacketHelper.parseErrorCode(status), handler);
					httpClient.close();
					return;
				}
			}
			else
			{//请求失败
				sendFail(path, "DownloadService.scheduleSilentGetImage: HTTP " + statusCode, handler);
				httpClient.close();
				return;
			}
		}
		catch (IOException e)
		{
			sendFail(path, "DownloadService.scheduleSilentGetImage:网络连接或文件传输过程可能出现了错误", handler);
			httpClient.close();
			return;
		}
	}
	
	private void sendSuccess(String file, String content, Handler handler)
	{
		Bundle bundle = new Bundle();
		bundle.putString("file", file);
		bundle.putString("content", content);
		if(handler != null)
		{
			Message msg = Message.obtain();
			msg.what = WHAT_DOWNLOAD_SUCCESS;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}else{
			Intent intent = new Intent();
			intent.setAction(ACTION_DOWNLOAD_SUCCESS);
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}
	}
	
	private void sendFail(String file, String content, Handler handler)
	{
		Bundle bundle = new Bundle();
		bundle.putString("file", file);
		bundle.putString("content", content);
		if(handler != null)
		{
			Message msg = Message.obtain();
			msg.what = WHAT_DOWNLOAD_SUCCESS;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}else{
			Intent intent = new Intent();
			intent.setAction(ACTION_DOWNLOAD_FAIL);
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}
	}
	
	private class DownloadBinder extends Binder implements IDownloadService
	{
		ExecutorService es;
		
		public DownloadBinder()
		{
			es = Executors.newCachedThreadPool(new NameableThreadFactory("DownloadService-Thread"));
		}
		
		public void scheduleSilentGetImage(final String uri, final String fileName, final Handler handler)
		{
			es.execute(new Runnable()
			{
				@Override
				public void run()
				{
					DownloadService.this.scheduleSilentGetImage(uri, fileName, handler);
				}
			});
		}
		
		public void close()
		{
			es.shutdown();
		}
	}
}

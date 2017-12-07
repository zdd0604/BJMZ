package com.mznerp.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mznerp.common.EapApplication;
import com.mznerp.util.myscom.IOUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

/**
 * the delegate of http request.
 * you should call open() to start service, and call close() on exit application
 * the core method of this class is addTask.
 * @author John Kenrinus Lee
 * @date Jun 3, 2014
 */
public class HttpClientManager
{
	private static final int SOCKET_OPERATION_TIMEOUT = 100 * 1000;
	private static BlockingQueue<HttpUriRequestThread> queue;
	private static DefaultHttpClient httpClient;
	private static boolean isOpen;
	
	/** before use service, you should call this to init*/
	public static synchronized void open()
	{
		isOpen = true;
		queue = new LinkedBlockingQueue<HttpUriRequestThread>();
		initHttpClient();
		new Thread("HttpClientManager-Thread")
		{
			public void run()
			{
				while(isOpen)
				{
					try
					{
						HttpUriRequestThread requestThread = queue.take();
						if (requestThread != null && isInternetConnected())
						{
							requestThread.start();
						}
					}
					catch (Exception e)
					{ 
						break;
					}
				}
			}
		}.start();
	}

	private static void initHttpClient()
	{
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

//		HttpProtocolParams.setUserAgent(params,
//						"Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

		ConnManagerParams.setTimeout(params, SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_OPERATION_TIMEOUT);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		httpClient = new DefaultHttpClient(conMgr, params);

	}
	
	private static boolean isInternetConnected() 
	{
		ConnectivityManager manager = (ConnectivityManager) EapApplication.getApplication()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) 
		{
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) 
			{
				return true;
			}
		}
		return false;
	}

	/** the current task on execute maybe can't cancel, but the following will be canceled*/
	public static final synchronized void cancelRemains()
	{
		queue.clear();
	}
	
	/** when you won't use the service any more, you should call this to destroy service*/
	public static final synchronized void close()
	{
		isOpen = false;
		httpClient = null;
		queue = null;
	}
	
	/**
	 * post a http request task asynchronous
	 * @param responseHandler once the request had response, the onResponse method of responseHandler would be callback.
	 * @param request one or more http request
	 */
	public static final void addTask(HttpResponseHandler responseHandler, HttpUriRequest ...request)
	{
		queue.offer(new HttpUriRequestThread(request, responseHandler));
	}
	
	/** if the response is text, you can call this method for string*/
	public static final String toStringContent(HttpResponse resp) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(resp != null)
		{
			HttpEntity entity = resp.getEntity();
			if(entity != null)
			{
				entity.writeTo(baos);
			}
		}
		byte[] bytes = baos.toByteArray();
		if(resp.containsHeader("Content-Encoding") && "gzip".equalsIgnoreCase(resp.getFirstHeader("Content-Encoding").getValue()))
		{
			GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(bytes));
			bytes = IOUtils.toByteArray(gzip);
		}
		return new String(bytes, "UTF-8");
	}
	
	private static final class HttpUriRequestThread extends Thread
	{
		HttpUriRequest[] requests;
		HttpResponseHandler responseHandler;
		
		public HttpUriRequestThread(HttpUriRequest[] requests, HttpResponseHandler responseHandler)
		{
			super("HttpClientWorker-Thread");
			this.requests = requests;
			this.responseHandler = responseHandler;
		}
		
		@Override
		public void run()
		{
			try
			{
				for(HttpUriRequest request : requests)
					httpClient.execute(request, responseHandler);
			}
			catch (Exception e)
			{
				responseHandler.onException(e); 
			}finally{   
				
             } 
		}
	}
	
	/**
	 * @author John Kenrinus Lee 
	 * @date Jun 3, 2014
	 */
	public static abstract class HttpResponseHandler implements ResponseHandler<Void>
	{
		/** when the request execute, if exception throwed will call this method*/
		public abstract void onException(Exception e);
		/** when the request had repsonse, this method will be called*/
		public abstract void onResponse(HttpResponse resp);
		
		public final Void handleResponse(HttpResponse response)
		{
			try
			{
				onResponse(response);
			}
			catch (Exception e)
			{
				onException(e);
				
		 }

			return null;
		}
	}
}

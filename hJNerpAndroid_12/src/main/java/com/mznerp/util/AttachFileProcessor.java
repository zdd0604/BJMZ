package com.mznerp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.HttpClientBuilder;
import com.mznerp.net.HttpClientManager;
import com.mznerp.util.myscom.FileUtils;


public class AttachFileProcessor
{
	public static final String PATH_ATTACHMENT_DOWNLOAD = "/servlet/imageResourceServlet";
	public static final String PATH_ATTACHMENT_UPLOAD = "/servlet/attachmentUploadServlet";
	public static final String LOCAL_FILE_PATH = Constant.CHAT_CACHE_DIR;
	
	public static final void requestImAudioAttach(String idFile, OnProcessResultListener l)
	{
		try
		{
			HttpPost httpPost = HttpClientBuilder.createParamAudio(PATH_ATTACHMENT_DOWNLOAD,idFile).getHttpPost();
			HttpClientManager.addTask(new AttachDownHandler(idFile, l), httpPost);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e(e);
		}
	}
	
	public static final void responseAttach(String idFile, OnProcessResultListener l)
	{
		HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP   + PATH_ATTACHMENT_UPLOAD);
		try
		{
			EapApplication eap = EapApplication.getApplication();
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(ChatConstants.iq.DATA_KEY_FILE_ID, new StringBody(idFile));
			entity.addPart(ChatConstants.iq.DATA_KEY_TYPE, new StringBody("attach"));
			entity.addPart(ChatConstants.iq.DATA_KEY_SESSION_ID,
					new StringBody(ActivitySupport.sputil.getMySessionId()));
			entity.addPart(ChatConstants.iq.DATA_KEY_COM_ID, 
					new StringBody( SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid()));
			entity.addPart(ChatConstants.iq.DATA_KEY_USER_ID, 
					new StringBody( SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId()));
			entity.addPart(idFile, new FileBody(new File(LOCAL_FILE_PATH, idFile)));
				httpPost.setEntity(entity);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e(null, "", e);
		}
		HttpClientManager.addTask(new AttachUpHandler(idFile, l), httpPost);
	}
	
	public static final void responseImAttach(String idFile, OnProcessResultListener l)
	{
		HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP + PATH_ATTACHMENT_UPLOAD);
		try
		{
			EapApplication eap = EapApplication.getApplication();
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(ChatConstants.iq.DATA_KEY_FILE_ID, new StringBody(idFile));
			entity.addPart(ChatConstants.iq.DATA_KEY_TYPE, new StringBody("im"));
			entity.addPart(ChatConstants.iq.DATA_KEY_SESSION_ID,
					new StringBody(ActivitySupport.sputil.getMySessionId()));
			entity.addPart(ChatConstants.iq.DATA_KEY_COM_ID, 
					new StringBody(ActivitySupport.sputil.getComid()));
			entity.addPart(ChatConstants.iq.DATA_KEY_USER_ID, 
					new StringBody(ActivitySupport.sputil.getMyId()));
			entity.addPart(idFile, new FileBody(new File(LOCAL_FILE_PATH, idFile)));
				httpPost.setEntity(entity);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e(null, "", e);
		}
		HttpClientManager.addTask(new AttachUpHandler(idFile, l), httpPost);
	}
	
	public static final void responseImAudioAttach(String idFile, OnProcessResultListener l)
	{
		HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP   + PATH_ATTACHMENT_UPLOAD);
		try
		{
			EapApplication eap = EapApplication.getApplication();
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(ChatConstants.iq.DATA_KEY_FILE_ID, new StringBody(idFile));
			entity.addPart(ChatConstants.iq.DATA_KEY_TYPE, new StringBody("voices"));
			entity.addPart(ChatConstants.iq.DATA_KEY_SESSION_ID,
					new StringBody(ActivitySupport.sputil.getMySessionId()));
			entity.addPart(ChatConstants.iq.DATA_KEY_COM_ID, 
					new StringBody(ActivitySupport.sputil.getComid()));
			entity.addPart(ChatConstants.iq.DATA_KEY_USER_ID, 
					new StringBody(ActivitySupport.sputil.getMyId()));
			entity.addPart(idFile, new FileBody(new File(Constant.CHATAUDIO_DIR, idFile)));
				httpPost.setEntity(entity);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e(null, "", e);
		}
		HttpClientManager.addTask(new AttachUpHandler(idFile, l), httpPost);
	}
	
	
	public static final class AttachDownHandler extends HttpClientManager.HttpResponseHandler
	{
		private String fileID;
		private OnProcessResultListener l;
		public AttachDownHandler(String fileID, OnProcessResultListener l)
		{
			this.fileID = fileID;
			this.l = l;
		}
		@Override
		public void onResponse(HttpResponse resp)
		{
			String contentType = resp.getHeaders("Content-Type")[0].getValue();
			 if("application/octet-stream".equals(contentType))
			 {
				try
				{
					 FileUtils.checkOrMakeFileExit(Constant.CHATAUDIO_DIR);
					 FileOutputStream fos = new FileOutputStream(new File(Constant.CHATAUDIO_DIR, fileID));
					 resp.getEntity().writeTo(fos);
					 fos.close();
					 l.onProcessResult(true, fileID);
				}
				catch (Exception e)
				{
					onException(e);
				}
			 }
			 else
			 {
				try
				{
					l.onProcessResult(false, HttpClientManager.toStringContent(resp));
				}
				catch (IOException e)
				{
					onException(e);
				}
			 }
		}
		@Override
		public void onException(Exception e)
		{ 
			l.onProcessResult(false, e.getMessage());
		}
	}
	
	public static final class AttachUpHandler extends HttpClientManager.HttpResponseHandler
	{
		private String fileID;
		private OnProcessResultListener l;
		public AttachUpHandler(String fileID, OnProcessResultListener l)
		{
			this.fileID = fileID;
			this.l = l;
		}
		@Override
		public void onResponse(HttpResponse resp)
		{
			try
			{
				String json = HttpClientManager.toStringContent(resp);
				if(json.contains("error"))
				{ 
					l.onProcessResult(false, json);
				}else{
					l.onProcessResult(true, fileID);
				}
			}
			catch (IOException e)
			{
				onException(e);
			}
		}
		@Override
		public void onException(Exception e)
		{ 
			l.onProcessResult(false, e.getMessage());
		}
	}
	
	public interface OnProcessResultListener
	{
		public void onProcessResult(boolean success, String msg);
	}
}

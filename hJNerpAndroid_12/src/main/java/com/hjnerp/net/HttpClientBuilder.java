package com.hjnerp.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.EapApplication;

public class HttpClientBuilder
{
	public static final HttpClientParam createParam(String lastSegment)
	{
		HttpClientParam param = new HttpClientParam();
		param.post = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP   + lastSegment);
		param.parameters = new ArrayList<BasicNameValuePair>();
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_USER_ID,
				ActivitySupport.sputil.getMyId()));
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_COM_ID, 
				ActivitySupport.sputil.getComid()));
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_SESSION_ID,
				ActivitySupport.sputil.getMySessionId()));
		return param;
	}
	
	public static final HttpClientParam createParamAudio(String lastSegment,String filename)
	{
		HttpClientParam param = new HttpClientParam();
		param.post = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP  + lastSegment);
		param.parameters = new ArrayList<BasicNameValuePair>();
		EapApplication eap = EapApplication.getApplication();
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_USER_ID,
				ActivitySupport.sputil.getMyId()));
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_COM_ID, 
				ActivitySupport.sputil.getComid()));
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_SESSION_ID,
				ActivitySupport.sputil.getMySessionId()));
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_TYPE,"voices"));
		param.parameters.add(new BasicNameValuePair(ChatConstants.iq.DATA_KEY_FILE_NAME,filename));
		return param;
	}
	public static final class HttpClientParam
	{
	    private HttpPost post;
	    private List<BasicNameValuePair> parameters;
	    
	    public final HttpClientParam addKeyValue(String key, String value)
		{
			parameters.add(new BasicNameValuePair(key, value));
			return this;
		}
		
		public final HttpPost getHttpPost() throws UnsupportedEncodingException 
		{
			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			return post;
		}
	}
}

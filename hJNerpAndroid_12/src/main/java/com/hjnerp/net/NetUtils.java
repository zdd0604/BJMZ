package com.hjnerp.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtils {
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;
	public static final int NETCLOSE = -1;
	public static final int NETWIFI = 3;
	public static final int NETCMWAP = 4;
	public static final int NETCMNET = 5;

	public static String getResultStr(String uri, List<NameValuePair> params,
			int method, int timeOut) throws Exception {
		HttpEntity entity = getResultEntity(uri, params, method, timeOut);
		if (entity == null)
			return null;
		String resultStr = EntityUtils.toString(entity, "utf-8");
		return resultStr;
	}

	public static HttpEntity getResultEntity(String uri,
			List<NameValuePair> params, int method, int timeOut)
			throws Exception {
		HttpEntity entity = null;
		HttpClient client = new DefaultHttpClient();
		if (timeOut > 0) {
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					timeOut);
		}
		HttpUriRequest request = null;
		switch (method) {
		case METHOD_GET:
			StringBuilder sb = new StringBuilder(uri);
			if (params != null) {
				String param = URLEncodedUtils.format(params, "utf-8");
				param = param.replace("%3A", ":");
				param = param.replace("%3B", ";");
				sb.append("?").append(param);
			}
			Log.i("info requst", sb.toString());
			request = new HttpGet(sb.toString());
			request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			break;
		case METHOD_POST:
			request = new HttpPost(uri);
			if (params != null && !params.isEmpty()) {
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(params,
						"utf-8"));
			}
			break;
		}
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			entity = response.getEntity();
		}
		return entity;
	}

	public static long getLength(HttpEntity entity) {
		if (entity != null) {
			return entity.getContentLength();
		}
		return -1;
	}

	public static InputStream getStream(HttpEntity entity) throws IOException {
		if (entity != null) {
			return entity.getContent();
		}
		return null;
	}

	public static int getNetStat(Context context) {
		int netType = NETCLOSE;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = NETCMNET;
			} else {
				netType = NETCMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETWIFI;
		}
		return netType;
	}
}

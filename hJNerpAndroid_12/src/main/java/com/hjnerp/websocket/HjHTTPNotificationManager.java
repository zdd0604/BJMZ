package com.hjnerp.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.hjnerp.common.EapApplication;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.util.SharePreferenceUtil;

import android.annotation.SuppressLint;
import android.util.Log;

public final class HjHTTPNotificationManager {
	private static final String TAG = "NOTIFICATION_HTTP";

	private HttpParams httpParams = new BasicHttpParams();
	private HttpClient httpClient;

	private Thread HttpThread;
	private OnNotificationListener onNotificationListener;

	private static HjHTTPNotificationManager hjHTTPNotification;
	private String url;
	private boolean stoped = true;

	public HjHTTPNotificationManager() {
	}

	public void setURL(String url) {
		this.url = url;
	}

	public static HjHTTPNotificationManager getInstance() {
		if (hjHTTPNotification == null) {
			hjHTTPNotification = new HjHTTPNotificationManager();
		}

		return hjHTTPNotification;
	}

	public void start() {
		if (!stoped) {
			Log.v(TAG, "## connection is normal, no need create again ##");
			return;
		}
		HttpThread = 	new Thread(new Runnable() {
			public void run() {
				if (SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).isForceExit()) {
					initHttpClient();
					if (httpClient != null) {
						connectServer();
					} else {
						Log.e(TAG, "## http client init error ##");
					}
				}
			}
		});
		HttpThread.start();
	}

	@SuppressLint("TrulyRandom")
	private void initHttpClient() {
		HttpConnectionParams.setConnectionTimeout(httpParams, 300 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, Integer.MAX_VALUE);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpClientParams.setRedirecting(httpParams, true);
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		MySSLSocketFactory sFactory = null;
		try {
			SSLContext sc = SSLContext.getInstance("TLS");

			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HttpsURLConnection
					.setDefaultHostnameVerifier(new MyHostnameVerifier());

			sFactory = new MySSLSocketFactory(sc,
					new TrustManager[] { new TrustAnyTrustManager() },
					new SecureRandom(), null);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));

			sFactory.setHostnameVerifier(new MyHostnameVerifier());

			schReg.register(new Scheme("https", sFactory, 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					httpParams, schReg);

			httpClient = new DefaultHttpClient(conMgr, httpParams);
		} catch (Exception e) {
			Log.e(TAG, "set http client param error: ", e);
		}
	}

	private void connectServer() {
		stoped = false;
		BufferedReader in = null;
		Log.v(TAG, "## start listen notification ##");
		Map<String, String> params = new HashMap<String, String>();
		params.put(
				ChatConstants.iq.DATA_KEY_USER_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
		params.put(
				ChatConstants.iq.DATA_KEY_EQU_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getDeviceId());
		HttpPost httpRequest = new HttpPost(changeToGetUrl(url, params));
		String strResult = null;
		try {
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream is = httpEntity.getContent();

				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String msg = "";

				byte[] buf = new byte[1024 * 200];
				int len = 0;
				ByteBuffer buffer = ByteBuffer.allocate(1024 * 200);
				while ((len = is.read(buf)) != -1) {
					if (stoped) {
						Log.v(TAG, "## stoped");
						break;
					}
					buffer.clear();
					buffer.put(buf, 0, len);

					msg = msg + new String(buffer.array(), 0, len);
					if (StringUtilHttp.isEmpty(msg)) {
						continue;
					}
					if ("logout".equals(msg)) {
						break;
					}
					msg = msg.replace("$^E", "");
					if (new JsonValidator().validate(msg)) {
						if (onNotificationListener != null && !stoped) {
							onNotificationListener.onNotification(msg);
						}
						msg = "";
					}

				}
				is.close();
			} else {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				errorResponse(strResult);
			}
		} catch (Exception e) {
			strResult = e.getMessage().toString();
			errorResponse(strResult);
			Log.i("info", "长连接异常："+e.toString());
		}
		stoped = true;
	}

	private void errorResponse(String msg) {
		if (onNotificationListener != null && !this.stoped) {
			onNotificationListener.onError(msg);
		}
		stop();
	}

	public void setOnNotificationListener(
			OnNotificationListener onNotificationListener) {
		this.onNotificationListener = onNotificationListener;
	}

	public void stop() {
		// NOTE: need send logout message to server side?
//		 logout();
		this.stoped = true;
//		HttpThread.stop();
		
	}

	public boolean isStoped() {
		return stoped;
	}

	private void logout() {
		// new Thread(new Runnable() {
		// public void run() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "logout");
		params.put(
				ChatConstants.iq.DATA_KEY_USER_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
		params.put(
				ChatConstants.iq.DATA_KEY_EQU_ID,
				SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getDeviceId());
		try {
			HttpGet httpRequest = new HttpGet(changeToGetUrl(url, params));
			httpClient.execute(httpRequest);
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			Log.e(TAG, "logout error: ", e);
		}
		// }
		// }).start();
	}

	private String changeToGetUrl(String url, Map<String, String> params) {
		if (StringUtilHttp.isEmpty(url)) {
			return url;
		}
		if (params == null || params.size() <= 0) {
			params = new HashMap<String, String>();
		}
		params.put("_current_time_", new Date().getTime() + "");
		int index = url.indexOf("?");
		StringBuilder sb = new StringBuilder(100);
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (StringUtilHttp.isEmpty(entry.getKey())) {
				continue;
			}
			String val = entry.getValue();
			val = StringUtilHttp.isEmpty(val) ? "" : val;
			try {
				sb.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(val, HTTP.UTF_8)).append("&");
				// sb.append(entry.getKey()).append("=").append(val).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		if (index == -1) {
			url += "?" + sb.toString();
		} else {
			url += "&" + sb.toString();
		}
		// System.out.println("url: " + url);
		return url;
	}
}

class MyHostnameVerifier implements X509HostnameVerifier {

	public boolean verify(String hostname, SSLSession session) {
		return true;

	}

	public void verify(String host, SSLSocket ssl) throws IOException {

	}

	public void verify(String host, X509Certificate cert) throws SSLException {
	}

	public void verify(String host, String[] cns, String[] subjectAlts)
			throws SSLException {

	}

}

class TrustAnyTrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}

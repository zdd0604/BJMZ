package com.mznerp.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
 
import com.mznerp.net.ChatConstants;

import android.annotation.SuppressLint; 

public class HjHttpConnection extends HjConnection {

	protected HttpParams httpParams = new BasicHttpParams();
	protected HttpClient httpClient;
	HttpEntity httpEntity;
	private String user = null;
	private String equ = null; // 设置号
	private boolean connected = false;

	private String url=null;
	
	private volatile boolean socketClosed = false;

	PacketWriter packetWriter;
	PacketReader packetReader;

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return connected;
	}

	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		// if (!isAuthenticated()) {
		// return null;
		// }
		return user;
	}

	@Override
	public String getEqu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub

		Map<String, String> params = new HashMap<String, String>();
		params.put(ChatConstants.iq.DATA_KEY_USER_ID,  this.getUser());
		params.put(ChatConstants.iq.DATA_KEY_EQU_ID, this.getEqu());
		HttpPost httpRequest = new HttpPost(changeToGetUrl(url, params));
		String strResult = null;
		
		
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		httpEntity =httpResponse.getEntity();
		initReaderAndWriter();
		
		
		
	}

	@Override
	public void login(String username, String Equ) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPacket(String packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (packetReader == null || packetWriter == null) {
			return;
		}

		if (!isConnected()) {
			return;
		}
		shutdown();
		// packetWriter.cleanup();
		// packetReader.cleanup();
	}

	private void shutdown() {

	}

	//使用用户配置
	@SuppressLint("TrulyRandom")
	public void connectUsingConfiguration()
	{
		HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
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
			//Log.e(TAG, "set http client param error: ", e);
		}
	}
	//初始始化连接
	public void initConnection() { 
		 boolean isFirstInitialization = packetReader == null || packetWriter == null;
		 
		  connectUsingConfiguration();
		 
//		  if (isFirstInitialization) {
//			  
//		  }
//		  else {
////              packetWriter.init();
////              packetReader.init();
//          }
		
	}
	
	
	@SuppressWarnings("unused")
	private void initReaderAndWriter() throws UnsupportedEncodingException, IllegalStateException, IOException
	{
		 reader =
                 new BufferedReader(new InputStreamReader(httpEntity.getContent(),"UFT-8" ) );
//         writer = new BufferedWriter(
//                 new OutputStreamWriter(httpEntity.getContent(), "UTF-8"));
	}

	@Override
	public String url() {
		// TODO Auto-generated method stub
		return url;
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
		return url;
	}
}

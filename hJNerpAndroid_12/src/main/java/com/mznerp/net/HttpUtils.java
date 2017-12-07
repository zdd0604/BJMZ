package com.mznerp.net;

import android.util.Log;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.Map;

public class HttpUtils {
	public static final int TIMEOUT_CONN = 10000;
	public static final int TIMEOUT_READ = 10000;

	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	public static final String ENCODE_UTF8 = "UTF-8";
	public static final String ENCODE_GBK = "GBK";

	public static final String SP = " ";
	public static final String CRLF = "\r\n";

	public static final String post(String urlString,
			Map<String, String> parameters, String encode) {
		return requestTextResponse(urlString, METHOD_POST, parameters, encode);
	}

	public static final String requestTextResponse(String urlString,
			String method, Map<String, String> parameters, String encode) {
		
//		if(TimeKey.isPasttime()){
//			return null;
//		}
		/**
		 * @author haijian 
		 * 联网操作换成xutils
		 */
		String responseContent = "";
		try {
		RequestParams params = new RequestParams();
		for (String key : parameters.keySet()) {
			params.addQueryStringParameter(key, parameters.get(key));
		}
		
		

		com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		
			ResponseStream responseStream = http.sendSync(HttpRequest.HttpMethod.POST, urlString, params);
			responseContent = responseStream.readString();
		} catch (Exception e) {
			Log.i("info", "xutils异常："+e.toString());// TODO: handle exception
		}

		///////////////////////////////////////////////////////////////////////
//		HttpURLConnection conn = null;
//		byte[] bytes = null;
//		try {
//			if (parameters != null) {
//				StringBuffer sb = new StringBuffer();
//				for (String key : parameters.keySet()) {
//					sb.append(key);
//					sb.append("=");
//					sb.append(URLEncoder.encode(parameters.get(key), encode));
//					sb.append("&");
//				}
//				if (sb.length() > 0)
//					sb = sb.deleteCharAt(sb.length() - 1);
//				bytes = sb.toString().getBytes(encode);
//			}
//
//			URL url = new URL(urlString);
//			Log.i("info", "地址==：" + urlString);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setConnectTimeout(TIMEOUT_CONN);
//			conn.setReadTimeout(TIMEOUT_READ);
//			conn.setRequestMethod("POST");
//			conn.setDoInput(true); // 允许输入流，即允许下载
//			conn.setDoOutput(true); // 允许输出流，即允许上传
//			conn.setUseCaches(false); // 不使用缓冲
//			conn.setChunkedStreamingMode(0);
//
//			// conn.connect();
//
//			if (bytes != null) {
//				OutputStream os = conn.getOutputStream();
//				os.write(bytes, 0, bytes.length);
//				os.flush();
//				os.close();
//			}
//			if (conn.getResponseCode() == 200) {
//				InputStream is = conn.getInputStream();
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//
//				String readLine = null;
//				while ((readLine = br.readLine()) != null) {
//					// response = br.readLine();
//					responseContent = responseContent + readLine;
//				}
//				is.close();
//				br.close();

				// BufferedInputStream bis = new
				// BufferedInputStream(conn.getInputStream());
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// bytes = new byte[bis.available()];
				// int len = 0;
				// while((len = bis.read(bytes)) > 0)
				// baos.write(bytes, 0, len);
				// responseContent = new String(baos.toByteArray(), encode);
				// bis.close();
				// baos.close();
//			}
//		} catch (IOException e) {
//			Log.i("info", "登陆连接异常：" + e.toString());
//		} finally {
//			if (conn != null)
//				conn.disconnect();
//		}
		//////////////////////////////////////////////////////////////////////////
		if ("".equalsIgnoreCase(responseContent))
			return null;
		
		return responseContent;
	}

}

package com.hjnerp.business;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.db.SQLiteWorker.AbstractSQLable;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.NBusinessTableCreateModel;
import com.hjnerp.net.HttpClientBuilder;
import com.hjnerp.net.HttpClientBuilder.HttpClientParam;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.net.HttpClientManager.HttpResponseHandler;
import com.hjnerp.util.Command;
import com.hjnerp.util.Log;
import com.hjnerp.util.myscom.StringUtils;

public class Ctlm1345Update implements Command {
	String TAG = "Ctlm1345Update";
	NsyncDataHandler handler;
	OnResultListener onResultListener;
	String[] idTables;
	String[] conditions;
	public static String ctlm1345UpdateResultJson;

	public Ctlm1345Update(String[] idTables, String[] conditions,
			OnResultListener onResultListener) {
		this.idTables = idTables;
		this.conditions = conditions;
		this.onResultListener = onResultListener;
		handler = new NsyncDataHandler();
	}

	@Override
	public void action() {
		try {
			if (idTables != null && idTables.length > 0) {
				getNBCtlm1345(idTables);
			} else {
				getNBCtlm1345(getNBCtlm1345IDTABLE());
			}
		} catch (Exception e) {
			com.hjnerp.util.Log.e("", e);
		}
	}

	String[] getNBCtlm1345IDTABLE() {
		// List<Ctlm4203> list = BusinessBaseDao.queryCtlm4203sByFlag("A");
		// int size = list.size();
		String[] idTables = new String[1];
		// for (int i = 0; i < size; ++i)
		// {
		// idTables[i] = list.get(i).id_table;
		// }
		// idTables[0] = "ctlm1345";
		idTables[0] = "ctlm4101";
		return idTables;
	}

	String getNBCtlm1345ACKCODE(String[] idTables) {
		List<Ctlm1345> list = BusinessBaseDao
				.queryAllCtlm1345s("where id_table in ("
						+ StringUtils.join(idTables, ",") + ") order by line_no ASC");
		StringBuffer sb = new StringBuffer();
		for (Ctlm1345 ctlm : list) {
			android.util.Log.i("info", "行号== "+ctlm.getLine_no());
			sb.append(ctlm.getId_table()).append("#&#")
					.append(ctlm.getLine_no()).append("#&#")
					.append(ctlm.getId_column()).append("&#&");
		}
		sb.delete(sb.lastIndexOf("&#&"), sb.length());
		return sb.toString();
	}

	// 下载同步数据到ctlm1345
	void getNBCtlm1345(String[] idTables) throws UnsupportedEncodingException {
		HttpClientParam param = HttpClientBuilder
				.createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
		param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
				.addKeyValue("id_table", StringUtils.join(idTables, ":"))
				.addKeyValue(
						"condition",
						conditions == null ? "" : StringUtils.join(conditions,
								":"));
		HttpClientManager.addTask(handler, param.getHttpPost());
	}

	// 确认下载的情况给服务器
	void ackCtlm1345DownFlag(final String[] idTables) {
		HttpClientParam param = null;
		try {
			param = HttpClientBuilder
					.createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
			param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileAckSyncDownload")
					.addKeyValue("id_table", StringUtils.join(idTables, ":"));
			HttpClientManager.addTask(new HttpResponseHandler() {
				@Override
				public void onResponse(HttpResponse resp) {
					String contentType = resp.getHeaders("Content-Type")[0]
							.getValue();
					if (contentType.contains("json")) {
						SQLiteWorker.getSharedInstance().postDML(
								new AbstractSQLable() {
									@Override
									public void onCompleted(Object event) {
										if (!(event instanceof Throwable)) {
											if (onResultListener != null)
												onResultListener.onResult(true);
										} else {
											Log.e((Throwable) event);
											if (onResultListener != null)
												onResultListener
														.onResult(false);
										}
									}

									@Override
									public Object doAysncSQL() {
										BusinessBaseDao
												.updateCtlm1345DownloadFlag(idTables);
										return null;
									}
								});
					} else {
						try {
							Log.e(HttpClientManager.toStringContent(resp));
							if (onResultListener != null)
								onResultListener.onResult(true);
						} catch (IOException e) {
							onException(e);
						}
					}
				}

				@Override
				public void onException(Exception e) {
					Log.e(e);
					if (onResultListener != null)
						onResultListener.onResult(true);
				}
			}, param.getHttpPost());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

	String processBusinessCompress(String fileName) throws Exception {// 解压缩下载的同步数据
		File file = new File(EapApplication.getApplication()
				.getExternalCacheDir(), fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipInputStream zis = new ZipInputStream(fis);
		ZipEntry entry = zis.getNextEntry();
		if (entry == null) {
			Log.e("数据文件已损坏");
		}
		int len = -1;
		byte[] bytes = new byte[512];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = zis.read(bytes, 0, bytes.length)) > 0) {
			baos.write(bytes, 0, len);
		}
		String json = new String(baos.toByteArray(), HTTP.UTF_8);
		zis.close();
		return json;
	}

	void procCompressJson(String json) {// 处理压缩包中的文本为json
		 
		Gson gson = new Gson();
		final NBusinessTableCreateModel model = gson.fromJson(json,
				NBusinessTableCreateModel.class);
		model.create();

		SQLiteWorker.getSharedInstance().postDML(new AbstractSQLable() {
			public Object doAysncSQL() {
				BusinessBaseDao.opNBusinessTableCreateModels2(model);
				return null;
			}

			public void onCompleted(Object event) {
				if (idTables != null) {
					if (!(event instanceof Throwable)) {
						if (onResultListener != null) {

							onResultListener.onResult(true);
						}
					} else {
						if (onResultListener != null) {
							onResultListener.onResult(false);
						}
					}
				}
			}
		});
	}

	class NsyncDataHandler extends HttpClientManager.HttpResponseHandler {
		@Override
		public void onResponse(HttpResponse resp) {
			String contentType = resp.getHeaders("Content-Type")[0].getValue();
			if (contentType.indexOf("application/octet-stream") != -1) {
				try {
					String contentDiscreption = resp
							.getHeaders("Content-Disposition")[0].getValue();
					String fileName = contentDiscreption
							.substring(contentDiscreption.indexOf("=") + 1);
					FileOutputStream fos = new FileOutputStream(new File(
							EapApplication.getApplication()
									.getExternalCacheDir(), fileName));
					resp.getEntity().writeTo(fos);
					fos.close();
					ctlm1345UpdateResultJson = processBusinessCompress(fileName);
//					Log.i("Ctlm1345Update", " *********************json is "
//							+ ctlm1345UpdateResultJson);
					procCompressJson(ctlm1345UpdateResultJson);
					// if(idTables == null)
					// {
					// ackCtlm1345DownFlag(getNBCtlm1345IDTABLE());
					// }
				} catch (Exception e) {
					onException(e);
				}
			} else /* if("text/json".equals(contentType)) */
			{
				try {
					Log.w(Ctlm1345Update.class.getSimpleName(),
							HttpClientManager.toStringContent(resp));
					if (onResultListener != null)
						onResultListener.onResult(false);
				} catch (IOException e) {
					onException(e);
				}
			}
		}

		@Override
		public void onException(Exception e) {
			Log.e(e);
			if (onResultListener != null)
				onResultListener.onResult(false);
		}
	}
}

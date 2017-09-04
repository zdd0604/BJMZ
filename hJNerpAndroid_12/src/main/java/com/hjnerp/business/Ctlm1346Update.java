package com.hjnerp.business;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.MenuContent;
import com.hjnerp.net.HttpClientBuilder;
import com.hjnerp.net.HttpClientBuilder.HttpClientParam;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.util.Command;
import com.hjnerp.util.Log;
import com.hjnerp.util.NameableThreadFactory;

public class Ctlm1346Update implements Command {
	private ExecutorService es = Executors.newFixedThreadPool(4,
			new NameableThreadFactory("Ctlm1346Update-Thread"));
	private ArrayList<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
	private OnMultiResultListener l;

	public Ctlm1346Update(OnMultiResultListener l) {
		this.l = l;
	}

	@Override
	public void action() {
		ArrayList<MenuContent> list = BusinessBaseDao.queryBusinessMenus();
		for (MenuContent mc : list) {
			String templateName = mc.getVarParm();
			String version;
			if (!new File(EapApplication.getApplication().getFilesDir(), (templateName + ".xml")).exists()) {
				version = "0";
			} else {
				version = BusinessBaseDao.getTemlateVersion(templateName);
			}
			tasks.add(new DownCall(templateName, version));
		}
		try {
			List<Future<Boolean>> result = es.invokeAll(tasks);
			ArrayList<Boolean> bools = new ArrayList<Boolean>();
			for (Future<Boolean> future : result) {
				try {
					bools.add(future.get());
				} catch (ExecutionException e) {
					Log.e(e);
				}
			}
			es.shutdownNow();
			if (l != null)
				l.onResult(bools);
		} catch (InterruptedException e1) {
			Log.e(e1);
		}
	}

	class DownCall implements Callable<Boolean> {
		private String templateName;
		private String version;

		public DownCall(String templateName, String version) {
			this.templateName = templateName;
			this.version = version;
		}

		@Override
		public Boolean call() {
			Boolean result = downloadTemplate(templateName, version);
			if (Boolean.TRUE.equals(result)) {
				BusinessBaseDao.updateCtlm1346TemplateVersion(templateName,
						version);
			}
			return result;
		}

		private Boolean downloadTemplate(String templateName, String version) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpClientParam param = HttpClientBuilder
						.createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
				param.addKeyValue(Constant.BM_ACTION_TYPE,
						Constant.BMTYPE_DOWNLOAD_TEMPLATE)
						.addKeyValue(Constant.BM_ID_NODE, templateName)
						.addKeyValue(Constant.BM_INT_VERSION, version);
				HttpResponse resp = client.execute(param.getHttpPost());
				if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = resp.getEntity();
					String contentType = entity.getContentType().getValue();
					if (contentType != null
							&& contentType.contains("application/octet-stream")) {
						this.version = resp.getFirstHeader(
								Constant.BM_INT_VERSION).getValue();
						// TODO
						File file = new File(EapApplication.getApplication()
								.getFilesDir(), templateName + ".xml");
						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(file));
						entity.writeTo(bos);
						bos.close();
						return Boolean.TRUE;
					} else {
						String resultJson = HttpClientManager
								.toStringContent(resp);
						if (resultJson.contains("error")) {
							com.hjnerp.util.Log.w(null, resultJson);
							return Boolean.FALSE;
						} else { // 已经是最新版本的模板
							return null;
						}
					}
				} else {
					com.hjnerp.util.Log.w(HttpClientManager
							.toStringContent(resp));
					return Boolean.FALSE;
				}
			} catch (Exception e) {
				com.hjnerp.util.Log.e("", e);
				return Boolean.FALSE;
			}
		}
	}
}
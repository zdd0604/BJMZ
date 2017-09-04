package com.hjnerp.business.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.db.Tables;
import com.hjnerp.model.BusinessTableCreateModel;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.net.HttpClientManager.HttpResponseHandler;
import com.hjnerp.util.Log;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ZipUtils;
import com.hjnerp.util.myscom.FileUtils;
import com.hjnerpandroid.R;

public class uploadDialog extends Dialog {
	protected static final int LOCATION_SUCCESS = 0;
	private static String TAG = "uploadDialog";

	private int dialogResult;
	private static MyHandler myHandler;
	Handler mHandler;
	private String billNo = "";
	private List<String> parentnode;
	private TextView tv_waitdialog_text;

	public uploadDialog(Activity context, List<String> billno) {

		super(context, R.style.noticeDialogStyle);

		setOwnerActivity(context);
		parentnode = billno;
		onCreate();

	}

	public int getDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(int dialogResult) {
		this.dialogResult = dialogResult;
	}

	/** Called when the activity is first created. */

	public void onCreate() {
		setContentView(R.layout.dialog_wait_withtext2);
		tv_waitdialog_text = (TextView) findViewById(R.id.tv_waitdialog2_notice);
		tv_waitdialog_text.setText("数据上传中...");
		myHandler = new MyHandler();
	}

	public void endDialog(int result) {
		dismiss();
		setDialogResult(result);
		dialogResult = result;
		Message m = mHandler.obtainMessage();
		m.obj = result;
		mHandler.sendMessage(m);
	}

	class MyHandler extends Handler {
		public MyHandler() {
		}

		public MyHandler(Looper L) {
			super(L);
		}

		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			super.handleMessage(msg);
			// 此处可以更新UI
			// Bundle b = msg.getData();
			int dLocation = (Integer) msg.obj;
			endDialog(dialogResult);

		}
	}

	public int showDialog() {

		// //这时开始上传数据

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message mesg) {
				// process incoming messages here
				// super.handleMessage(msg);
				throw new RuntimeException();
			}
		};
		super.show();

		uploaddata();

		try {
			Looper.getMainLooper().loop();
		} catch (RuntimeException e2) {
		}
		return dialogResult;
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	// private void compressFiles(File f) {
	// FileOutputStream out = null;
	// try {
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inSampleSize = 8;
	// FileInputStream file = new FileInputStream(f);
	// Bitmap bitmap = BitmapFactory.decodeStream(file, null, options);
	// int opt = 80;
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// bitmap.compress(CompressFormat.JPEG, 100, baos);
	// file.close();
	// if (baos.toByteArray().length / 1024 > 40) {
	// opt = 40000 / (baos.toByteArray().length / 1024);
	// baos.reset();
	// bitmap.compress(CompressFormat.JPEG, opt, baos);
	// }
	//
	// out = new FileOutputStream(f);
	// out.write(baos.toByteArray(), 0, baos.toByteArray().length);
	// out.flush();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (out != null) {
	// out.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	private void uploaddata() {

		if (!isNetworkConnected()) {
			Message msg = myHandler.obtainMessage(LOCATION_SUCCESS);
			msg.obj = 1;
			myHandler.sendMessage(msg);
			return;
		}

		for (String billno : parentnode) {
			billNo = billNo + "," + billno;
		}

		List<Ctlm1347> list = BusinessBaseDao.getCTLM1347Bybillno(parentnode);

		BusinessTableCreateModel businessTableCreateModel = new BusinessTableCreateModel();
		businessTableCreateModel.table = Tables.BusinessCtlm1347.NAME;
		businessTableCreateModel.param = billNo;
		StringBuffer sb = new StringBuffer();

		String[] fields = { "var_billno", "id_parentnode", "date_opr",
				"id_com", "id_model", "id_node", "id_nodetype", "id_recorder",
				"id_srcnode", "id_table", "id_view", "int_line", "name_node",
				"var_data1", "var_data2", "var_data3", "var_data4",
				"var_data5", "var_data6", "var_data7", "var_data8",
				"var_data9", "var_data10", "var_data11", "var_data12",
				"var_data13", "var_data14", "var_data15", "var_data16",
				"var_data17", "var_data18", "var_data19", "var_data20" };

		List<File> files = new ArrayList<File>();
		businessTableCreateModel.cols = "var_billno, id_parentnode, date_opr, id_com, id_model, id_node, id_nodetype, id_recorder, id_srcnode, id_table, id_view, int_line, name_node, var_data1, var_data2, var_data3, var_data4, var_data5, var_data6, var_data7, var_data8, var_data9, var_data10, var_data11, var_data12, var_data13, var_data14, var_data15, var_data16, var_data17, var_data18, var_data19, var_data20";

		sb = new StringBuffer();
		for (Ctlm1347 ctlm1347 : list) {
			// sb.append(ctlm1347).append(BusinessTableCreateModel.DIVIDER_STRING);

			sb.append("'").append(ctlm1347.getVar_billno()).append("',");
			sb.append("'").append(ctlm1347.getId_parentnode()).append("',");
			sb.append("'").append(ctlm1347.getDate_opr()).append("',");

			sb.append("'").append(ctlm1347.getId_com()).append("',");
			sb.append("'").append(ctlm1347.getId_model()).append("',");
			sb.append("'").append(ctlm1347.getId_node()).append("',");
			sb.append("'").append(ctlm1347.getId_nodetype()).append("',");
			sb.append("'").append(ctlm1347.getId_recorder()).append("',");
			sb.append("'").append(ctlm1347.getId_srcnode()).append("',");
			sb.append("'").append(ctlm1347.getId_table()).append("',");
			sb.append("'").append(ctlm1347.getId_view()).append("',");
			sb.append("'").append(ctlm1347.getInt_line()).append("',");
			sb.append("'").append(ctlm1347.getName_node()).append("',");
			sb.append("'").append(ctlm1347.getVar_data1()).append("',");
			sb.append("'").append(ctlm1347.getVar_data2()).append("',");
			sb.append("'").append(ctlm1347.getVar_data3()).append("',");
			sb.append("'").append(ctlm1347.getVar_data4()).append("',");
			sb.append("'").append(ctlm1347.getVar_data5()).append("',");
			sb.append("'").append(ctlm1347.getVar_data6()).append("',");
			sb.append("'").append(ctlm1347.getVar_data7()).append("',");
			sb.append("'").append(ctlm1347.getVar_data8()).append("',");
			sb.append("'").append(ctlm1347.getVar_data9()).append("',");
			sb.append("'").append(ctlm1347.getVar_data10()).append("',");
			sb.append("'").append(ctlm1347.getVar_data11()).append("',");
			sb.append("'").append(ctlm1347.getVar_data12()).append("',");
			sb.append("'").append(ctlm1347.getVar_data13()).append("',");
			sb.append("'").append(ctlm1347.getVar_data14()).append("',");
			sb.append("'").append(ctlm1347.getVar_data15()).append("',");
			sb.append("'").append(ctlm1347.getVar_data16()).append("',");
			sb.append("'").append(ctlm1347.getVar_data17()).append("',");
			sb.append("'").append(ctlm1347.getVar_data18()).append("',");
			sb.append("'").append(ctlm1347.getVar_data19()).append("',");
			sb.append("'").append(ctlm1347.getVar_data20()).append("'");

			sb.append(BusinessTableCreateModel.DIVIDER_STRING);

			Log.v("show","拜访上传数据："+sb.toString());
			if (WidgetName.HJ_PHOTOVIEW.equalsIgnoreCase(ctlm1347
					.getId_nodetype().trim())) {
				String names = ctlm1347.getVar_data1();
				if (!TextUtils.isEmpty(names)) {

					String[] name = names.split(";");
					for (String s : name) {

						// File f = new File(Constant.HJPHOTO_CACHE_DIR +
						// ss[2]);//图片命名包含地址
						File f = new File(Constant.HJPHOTO_CACHE_DIR + s);// 图片命名不包含地址
						// 对文件进行压缩
						// compressFiles(f);

						files.add(f);
					}
				}
			}

			if (WidgetName.HJ_ATTENDANCE.equalsIgnoreCase(ctlm1347
					.getId_nodetype().trim())) {

				String names = ctlm1347.getVar_data4();
				if (!TextUtils.isEmpty(names)) {

					String[] name = names.split(",");
					for (String s : name) {

						// File f = new File(Constant.HJPHOTO_CACHE_DIR + "/" +
						// ss[2]);//图片命名包含地址
						File f = new File(Constant.CHAT_CACHE_DIR + s);// 图片命名不包含地址
						Log.v("show", "图片地址："+Constant.CHAT_CACHE_DIR + s);
						// 对文件进行压缩
						// compressFiles(f);
						files.add(f);
					}
				}
			}

		}
		sb.substring(0, sb.lastIndexOf(BusinessTableCreateModel.DIVIDER_STRING));

		businessTableCreateModel.values = sb.toString();

		Gson gson = new Gson();
		String json = gson.toJson(businessTableCreateModel);
		File filedir = new File(Constant.TEMP_DIR);
		if (!filedir.exists()) {
			filedir.mkdir();
		}
		String uuid = StringUtil.getMyUUID();
		// File file = new File(Constant.TEMP_DIR, "temp.txt");
		File file = new File(Constant.TEMP_DIR, uuid + ".txt");
		files.add(file);
		try {
			FileUtils.writeStringToFile(file, json, "utf-8");
			File f = new File(Constant.TEMP_DIR + uuid + ".zip");
			ZipUtils.zipFiles(files, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendZipFileFromBusinessActivity(Constant.TEMP_DIR + uuid + ".zip", list);
		Log.v("show","sendZipFileFromBusinessActivity集合的数据 :" + list.toString());
	}

	private static void sendZipFileFromBusinessActivity(String name,
			final List<Ctlm1347> list1347) {
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("download", new FileBody(new File(name)));
		HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP

		+ Constant.BUSINESS_SERVICE_ADDRESS);
		httpPost.setEntity(entity);

		HttpClientManager.addTask(new HttpResponseHandler() {

			@Override
			public void onResponse(HttpResponse resp) {
				// 修改数据库中的值
				// /得到返回值看是否成功

				try {
					String msga = HttpClientManager.toStringContent(resp);

					try {
						JSONObject jsonObject = new JSONObject(msga);
						String result = jsonObject.getString("flag");
						if ("ok".equalsIgnoreCase(result)) {
							BusinessBaseDao.updateCtlm1347s(list1347, "Y");
							Message msg = myHandler
									.obtainMessage(LOCATION_SUCCESS);
							msg.obj = 1;
							myHandler.sendMessage(msg);

						} else {

							Message msg = myHandler
									.obtainMessage(LOCATION_SUCCESS);
							msg.obj = 1;
							myHandler.sendMessage(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onException(Exception e) {

				Message msg = myHandler.obtainMessage(LOCATION_SUCCESS);
				msg.obj = 0;
				myHandler.sendMessage(msg);

			}
		}, httpPost);

	}
}
package com.hjnerp.business.htmlutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.hjnerp.business.Ctlm1345Update;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.net.HttpClientManager.HttpResponseHandler;
import com.hjnerp.util.BDLocationUtilII;
import com.hjnerp.util.Command.OnResultListener;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.ImageFileHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.ZipUtils;
import com.hjnerp.util.imageCompressUtil;
import com.hjnerp.util.myscom.FileUtils;
import com.lidroid.xutils.util.LogUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/*
 * @author haijian
 * html插件调用java代码
 */
public class HjBusiness extends CordovaPlugin {

	public static final String CHECKED = "Y";
	public static final String UNCHECKED = "N";

	private static final String GET_ONE = "getoneaction";
	private static final String GET_GROUP = "getgroupaction";
	private static final String F4KEY_SCAN = "f4keyscanaction";
	private static final String SAVE = "saveaction";
	private static final String CAMERA = "cameraaction";// 拍照
	private static final String PIC_CAMERA = "piccameraaction";// 相册
	private static final String SCAN = "scanaction";// 扫描二维码
	private static final String FIEL_UPLOAD = "fieluploadaction";
	private static final String BILLNO_UPLOAD = "billnouploadaction";
	private static final String GETLOCATION = "getlocationaction";// 获取位置
	private static final String GET_DATA = "getdataaction";// 获取地理位置
	private static final String BACK_HOME = "backhomeaction";// 返回主页
	private static final String DELETE_1347 = "delete1347action";// 删除1347
	private static final String UPDATE_1347 = "update1347action";// 修改1347

	private static final String GETPICPATH = "getpicpath";

	private static final int PHOTO_GRAPH = 1;// 拍照
	private static final int PHOTO_XIANGCE = 2;// 相册
	private static final int REQUEST_CODE_SCAN = 1001;// 扫描二维码

	private static final String DATASET_KEY = "key";
	private static final String DATASET_VALUE = "value";

	private static final String DATASETMODO = "datasetmode";

	private CallbackContext callbackContext;

	private String strImage;// 文件名称
	private File out;
	private Uri uri;
	public Boolean flag_curr = false;

	private final int GET_LOCATION = 4;// 获取位置
	private final int GET_GEO = 1;// 获取地理位置

	private boolean isGeo = false;

	private String DATEMODEL_LOCAL = "local";
	private String DATEMODEL_NET = "net";

	private String[] keys = new String[1];
	private String[] values = new String[1];

	/**
	 * @author haijian 定义gson
	 */
	private Gson gson;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_GEO:
				BDLocation location = (BDLocation) msg.obj;
				String locationString = location.getLatitude() + ","
						+ location.getLongitude();
				if (!isGeo) {
					if (callbackContext != null)
						callbackContext.success(locationString);
				}
				break;

			case GET_LOCATION:
				try {
					String address = (String) msg.obj;
					if (callbackContext != null)
						callbackContext.success(address);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				break;
			case 3:
				if (callbackContext != null)
					callbackContext.success("定位超时");
				break;

			default:
				break;
			}

		};
	};

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
		// 处理与js交互的事件
		// args js传过来的值
		if (FIEL_UPLOAD.equals(action)) {// 上传调用上传的方法
			onhjupload(args);
			return true;
		} else if (GET_ONE.equals(action)) {// 返回一条记录
			onhjreadctlm1347(args);
			return true;
		} else if (GET_GROUP.equals(action)) {// 返回一组记录
			onhjreadctlm1347(args);
			return true;
		} else if (SAVE.equals(action)) {// 保存记录 参数是数组
			onhjsavectlm1347(args);
			return true;
		} else if (CAMERA.equals(action)) {// 拍照
			onhjcapture(this.callbackContext);
			return true;
		} else if (SCAN.equals(action)) {// 二维码
			onhjscancode(this.callbackContext);
			return true;
		} else if (F4KEY_SCAN.equals(action)) {// f4key扫描二维码
			onf4scancode(this.callbackContext);
			return true;
		} else if (BILLNO_UPLOAD.equals(action)) {// 数据上传单据号
			onhjuploadctlm1347(args);
			return true;
		} else if (GETLOCATION.equals(action)) {// 获取位置
			getLocation();
			return true;
		} else if (GET_DATA.equals(action)) {// 联网获取数据
			hjloaddata(args);
			Log.v("show","工作日志args:" + args);
			return true;
		} else if (BACK_HOME.equals(action)) {// 返回
			onhjbackmain();
			return true;
		} else if (GETPICPATH.equals(action)) {// 获取图片路径
			imgfilepath(args);
			return true;
		} else if (PIC_CAMERA.equals(action)) {// 相册
			imgalbum();
			return true;
		} else if (UPDATE_1347.equals(action)) {// 修改1347
			update1347(args);
			return true;
		} else if (DELETE_1347.equals(action)) {// 删除1347
			delete1347(args);
			return true;
		}
		return false;
	}

	private void onf4scancode(CallbackContext callbackContext2) {
		/**
		 * 向主线程发消息开始扫描
		 */
		// callbackContext2.
		Intent intent = new Intent();
		intent.setAction("startF4key");
		Log.i("info", "f4扫描执行");
		EapApplication.getApplication().sendBroadcast(intent);
	}

	private void delete1347(JSONArray args) {
		JSONObject obj = args.optJSONObject(0);
		if (obj != null) {
			String key = obj.optString(DATASET_KEY);
			try {
				BusinessBaseDao.updateCtlm1347(key);
				if (this.callbackContext != null)
					this.callbackContext.success("操作成功！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (this.callbackContext != null)
					this.callbackContext.error(e.toString());
			}
		}
	}

	private void update1347(JSONArray args) {
		JSONObject obj = args.optJSONObject(0);
		if (obj != null) {
			try {
				String key = obj.optString(DATASET_KEY);
				BusinessBaseDao.deletectlm1347(key);
				if (this.callbackContext != null)
					this.callbackContext.success("操作成功！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (this.callbackContext != null)
					this.callbackContext.error(e.toString());
			}
		}
	}

	private void imgalbum() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		cordova.startActivityForResult(HjBusiness.this, intent, PHOTO_XIANGCE);
	}

	private void imgfilepath(JSONArray args) {
		JSONObject obj = args.optJSONObject(0);
		String name;
		try {
			name = obj.getString("pic");
			if (this.callbackContext != null)
				this.callbackContext.success("file://"
						+ Constant.HJPHOTO_CACHE_DIR + name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (this.callbackContext != null)
				this.callbackContext.error("错误：" + e.toString());
		}
	}

	private void onhjbackmain() {
		Intent intent = new Intent();
		intent.setAction("backhome");
		EapApplication.getApplication().sendBroadcast(intent);
	}

	private void hjloaddata(JSONArray args) {
		// 联网获取数据
		JSONObject obj = args.optJSONObject(0);
		if (obj != null) {
			String key = obj.optString(DATASET_KEY);
			keys[0] = key;
			String value = obj.optString(DATASET_VALUE).replace("``", "%");
			values[0] = value;
			String datasetmodel = obj.optString(DATASETMODO);
			if (DATEMODEL_LOCAL.equalsIgnoreCase(datasetmodel)) {
				getLocalBusinessData(key, value);
			} else {
				// 先清除数据
				if (isNetworkConnected()) {
					// /先删除数据
					for (int i = 0; i < keys.length; i++) {
						BusinessBaseDao.deleteBusinessData(keys[i], values[i]);
					}
					// /等待网络完成
					MySSListener ll = new MySSListener();
					Ctlm1345Update ctlm1345 = new Ctlm1345Update(keys, values,ll);
					ctlm1345.action();
				} else {
					getLocalBusinessData(key, value);
				}
			}
		}
		// }
		// this.callbackContext.success("获取数据通道成功");
	}

	public void getLocation() {
		// 获取位置
		isGeo = true;
		BDLocationUtilII.setActivityHandler(handler);
		BDLocationUtilII.getLocation(EapApplication.getApplication()
				.getBaseContext(), isGeo);
	}

	public void onhjuploadctlm1347(JSONArray json) {
		// 表单数据上传
		if (this.callbackContext != null)
			this.callbackContext.success("上传表单通过");
	}

	public void onhjscancode(CallbackContext callbackContext2) {
		// 二维码
		Intent intentScan = new Intent(
				"com.hjnerp.activity.qrcode.CaptureActivity");
		intentScan.addCategory(Intent.CATEGORY_DEFAULT);
		try {
			this.cordova.startActivityForResult((CordovaPlugin) this,
					intentScan, REQUEST_CODE_SCAN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("info", "跳转异常：" + e.toString());
		}
	}

	public void onhjcapture(CallbackContext callbackContext2) {
		// 拍照
		startPhoto();
	}

	// 拍照的方法
	@SuppressLint("SimpleDateFormat")
	public void startPhoto() {
		Random random = new Random();
		String rand = String.valueOf(random.nextInt());
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String fileName = SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId()
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ rand + ".jpg";

		strImage = fileName;
		out = new File(Constant.HJPHOTO_CACHE_DIR);
		if (!out.exists()) {
			out.mkdirs();
		}
		ImageFileHelper.getInstance().setFileName(fileName);
		out = new File(Constant.HJPHOTO_CACHE_DIR, fileName);
		uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		checkPath(intent, Constant.HJPHOTO_CACHE_DIR + fileName);
		flag_curr = true;
		// BusinessActivityFragment bfragment = (BusinessActivityFragment)
		// HjActivityManager
		// .getInstance().peek().getSupportFragmentManager()
		// .findFragmentByTag(businessParam.getViewId());
		// if (bfragment != null) {
		this.cordova.startActivityForResult(HjBusiness.this, intent,
				PHOTO_GRAPH);
		// }
		// ((Activity) getContext()).startActivityForResult(intent, 1001);

	}

	private void checkPath(Intent intent, String picpath) {
		if (intent != null) {
			Uri uri_DCIM = null;
			if (intent.getData() != null) {
				uri_DCIM = intent.getData();
			} else {
				uri_DCIM = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			}
			String DCIMPath = "";
			Cursor cr = EapApplication
					.getApplication()
					.getBaseContext()
					.getContentResolver()
					.query(uri_DCIM,
							new String[] { MediaStore.Images.Media.DATA },
							null, null,
							MediaStore.Images.Media.DATE_MODIFIED + " desc");
			if (cr.moveToNext()) {
				DCIMPath = cr.getString(cr
						.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cr.close();
			if (DCIMPath.equals(picpath)) {
				ToastUtil.ShowLong(EapApplication.getApplication()
						.getBaseContext(), "路径错误！");
			}
		}
	}

	public void onhjsavectlm1347(JSONArray ctlm13472) {
		// 保存
		gson = new Gson();
		try {
			for (int i = 0; i < ctlm13472.length(); i++) {
				Object obj = ctlm13472.get(i);
				Ctlm1347 ctlm1347 = gson.fromJson(obj.toString(),
						Ctlm1347.class);
				ctlm1347.setId_recorder(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
				ctlm1347.setId_com(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getComid());
				ctlm1347.setFlag_upload("N");
				ctlm1347.setDate_opr(DateUtil.getCurDateStr());
				BusinessBaseDao.replaceCTLM1347(ctlm1347);
			}
		} catch (Exception e) {
			com.hjnerp.util.Log.i("info", "保存数据异常：" + e.toString());
		}
		if (this.callbackContext != null)
			this.callbackContext.success("保存成功");

	}

	private void onhjreadctlm1347(JSONArray args) {
		// 返回一条或一组记录
		// 根据arg 获取id等参数 根据id查询数据库
		JSONObject obj = args.optJSONObject(0);
		String result = "";
		if (obj != null) {
			String key = obj.optString(DATASET_KEY);
			result = BusinessBaseDao.getCtlm1347Values(key);
		}
		if (this.callbackContext != null)
			this.callbackContext.success(result);

	}

	public void onhjupload(JSONArray args) {
		// 上传的代码
		JSONObject obj = args.optJSONObject(0);
		if (obj != null) {
			try {
				List<File> files = new ArrayList<>();
				String json = obj.getString(DATASET_KEY);
				String file_list = obj.getString(DATASET_VALUE);
				String uuid = StringUtil.getMyUUID();
				File file = new File(Constant.TEMP_DIR, uuid + ".txt");
				if (!"".equals(file_list)) {
					for (String name : file_list.split(",")) {
						files.add(new File("file://"
								+ Constant.HJPHOTO_CACHE_DIR + name));
					}
				}
				files.add(file);
				try {
					FileUtils.writeStringToFile(file, "business" + json,"utf-8");
					File f = new File(Constant.TEMP_DIR + uuid + ".zip");
					ZipUtils.zipFiles(files, f);
					sendZipFile(Constant.TEMP_DIR + uuid + ".zip");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// for (Ctlm1347 ctlm1347 : ctlm1345ListFromCtlm1347) {
		// if (CHECKED.equals(ctlm1347.getFlag_upload())) {
		// billNoList.add(ctlm1347.getVar_billno());
		// idnodeList.add(ctlm1347.getId_node());
		// }
		// }
		// BusinessLua.uploadData(billNoList);
	}

	private void sendZipFile(String name) {

		MultipartEntity entity = new MultipartEntity();
		entity.addPart("download", new FileBody(new File(name)));
		HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP
				+ Constant.BUSINESS_SERVICE_ADDRESS);

		LogUtils.i("上传服务地址：" + EapApplication.URL_SERVER_HOST_HTTP
				+ Constant.BUSINESS_SERVICE_ADDRESS);

		httpPost.setEntity(entity);
		HttpClientManager.addTask(new HttpResponseHandler() {

			@Override
			public void onResponse(HttpResponse resp) {

				try {
					String msga = HttpClientManager.toStringContent(resp);

					try {
						JSONObject jsonObject = new JSONObject(msga);
						String result = jsonObject.getString("flag");
						Log.v("show", "上传返回的数据:" + jsonObject.toString());
						if ("ok".equalsIgnoreCase(result)) {
							// 上传成功
							callbackContext.success("上传成功：" + result);
						} else {
							String message = jsonObject.getString("message");
							if (message.contains("$")) {
								int index = message.indexOf("$");
								String messageSbf = message.substring(0, index);
								// 上传失败
								callbackContext.success("上传失败：" + result
										+ " 消息：" + messageSbf);
							} else {
								// 上传失败
								callbackContext.success("上传失败：" + result
										+ " 消息：" + message);
							}

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						callbackContext.success("上传失败：" + e.toString());
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					callbackContext.success("上传失败：" + e.toString());
				}

			}

			@Override
			public void onException(Exception e) {
				// 上传失败
				callbackContext.success("上传失败：" + e.toString());

			}
		}, httpPost);

	}

	private String makePicFileName() {
		Random random = new Random();
		String rand = String.valueOf(random.nextInt());
		return SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId()
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ rand + ".jpg";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		if (requestCode == PHOTO_GRAPH) {// 拍照
			this.callbackContext.success("file://" + Constant.HJPHOTO_CACHE_DIR
					+ strImage);
		} else if (requestCode == REQUEST_CODE_SCAN) {// 扫描二维码
			if (resultCode == REQUEST_CODE_SCAN) {
				JSONObject obj = new JSONObject();
				try {
					this.callbackContext.success(intent
							.getStringExtra("result"));
				} catch (Exception e) {
					Log.d("info", "This should never happen");
					this.callbackContext.error("Unexpected error");
				}
				// this.success(new PluginResult(PluginResult.Status.OK, obj),
				// this.callback);

			} else if (resultCode == Activity.RESULT_CANCELED) {

				this.callbackContext.success("取消了");
			} else {
				// this.error(new PluginResult(PluginResult.Status.ERROR),
				// this.callback);
				this.callbackContext.error("Unexpected error");
			}
		} else if (requestCode == PHOTO_XIANGCE) {
			if (intent == null) {
				this.callbackContext.error("相册错误");
				return;
			}
			Uri uri = intent.getData();
			if (uri != null) {

				String picPath = uri.getPath();
				String fileName = makePicFileName();
				picPath = fileName;

				// //压缩成功后直接显示到聊天记录中
				Bitmap bitmapuri = imageCompressUtil.getCompressUri(
						EapApplication.getApplication(), uri);

				if (bitmapuri != null) {
					try {
						imageCompressUtil.saveImage(bitmapuri,
								Constant.HJPHOTO_CACHE_DIR + picPath);
						this.callbackContext.success("file://"
								+ Constant.HJPHOTO_CACHE_DIR + picPath);

						System.gc();
						// sendPicMessage(strImage);
					} catch (Exception e) {
						LOG.i("info", "相册异常：" + e.toString());
						this.callbackContext.error("相册错误");
					}

				} else {
					return;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	private void getLocalBusinessData(String dataSource, String condition) {

		BusinessData busData = new BusinessData();
		busData = BusinessBaseDao.getBusinessData(dataSource, condition);
		Log.v("show","部门信息："+busData.var_values);
		if ((busData.var_values != null)) {
			callbackContext.success(busData.var_values);
		} else {
			callbackContext.error("没有数据");
		}
		return;
	}

	class MySSListener implements OnResultListener {

		@Override
		public void onResult(boolean success) {

			// getLocalBusinessData(idtables, conditions);
			// }

			BusinessData busData = new BusinessData();
			busData = BusinessBaseDao.getBusinessData(keys[0], values[0]);
			if (busData != null && (busData.var_values != null)) {
				callbackContext.success(busData.var_values);
				Log.i("info", "搜索返回：" + busData.var_values);
			} else {
				callbackContext.error("没有数据");
			}
			return;
		}

	}

	// //界面初始化保存
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) EapApplication
				.getApplication()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
}

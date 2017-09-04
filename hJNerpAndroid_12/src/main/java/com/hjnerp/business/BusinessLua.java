package com.hjnerp.business;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.hjnerp.business.activity.BusinessActivity;
import com.hjnerp.business.activity.BussinessUploadActivity;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.db.Tables;
import com.hjnerp.manager.HjActivityManager;
import com.hjnerp.model.BusinessTableCreateModel;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.net.HttpClientManager.HttpResponseHandler;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.Log;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ZipUtils;
import com.hjnerp.util.myscom.FileUtils;

public class BusinessLua {

	public static final String TAG = "info";
	public static final String USER_ID_FLAG = "userId:";

	private static final Stack<String> tempidnode_down = new Stack<String>();
	private static final Stack<String> idnode_down = new Stack<String>();
	private static final Stack<String> parentnode = new Stack<String>();
	private static final Stack<String> tempparentnode = new Stack<String>();

	/**
	 * 用于根据IDnode取数据库中某一单的记录的值
	 * 
	 * @param billno
	 *            业务单据号
	 * @param nodeid
	 *            结点代码
	 * @param field
	 *            字段
	 * @return 返回取得值
	 */
	public static String getdb_nodevalue(String billno, String nodeid,
			String field) {
		return BusinessBaseDao.getNodeValue(billno, nodeid, field);
	}

	/**
	 * 用于根据IDnode设置数据里某一个值
	 * 
	 * @param billno
	 * @param nodeid
	 * @param field
	 * @param values
	 */

	public static String jhju_getjsonvalue(String jsontxt, String filed) {
		Map<String, Object> map = null;
		Gson gson = new Gson();
		String text = null;
		map = (Map<String, Object>) gson.fromJson(jsontxt, Object.class);
		if (map != null && !map.isEmpty()) {
			text = (String) map.get(filed);
		}
		if (text == null) {
			text = "";
		}
		return text;
	}

	/**
	 * 用于根据IDnode设置数据里某一个值
	 * 
	 * @param billno
	 * @param nodeid
	 * @param field
	 * @param values
	 */
	public static void setdb_nodevalue(String billno, String nodeid,
			String field, String values) {
		BusinessBaseDao.setNodeValue(billno, nodeid, field, values);

	}

	/**
	 * 用于根据界面上控件ID数据里某一个值
	 * 
	 * @param billno
	 * @param nodeid
	 * @param field
	 * @param values
	 */

	// public String getdb_controlvalue(String billno, String Controlid, String
	// pnodeid,String field )
	// {
	// return BusinessBaseDao.getNodeValue(billno ,nodeid,field) ;
	// }

	/**
	 * 用于根据IDnode设置数据里某一个值
	 * 
	 * @param billno
	 * @param nodeid
	 * @param field
	 * @param values
	 */
	public static void setdb_controlvalue() {

	}

	/**
	 * 
	 * @param billNoList
	 *            单号
	 * @param idnodeList
	 *            节点号
	 * 
	 */
	// TODO 由子节点查找当前单据(单据完成后上传)
	public static void jhjdb_nodeuplod(String billNo) {

		BusinessActivity dc = HjActivityManager.getInstance().peek();
		dc.showUploadDialog(billNo);
	}

	public static String getDatediff(String fdate, String tdate) {
		return DateUtil.getDatediff(fdate, tdate);

	}

	public String getDatetime() {
		return DateUtil.getCurDateStr();
	}

	public static void uploadData(List<String> billNoList) {
		// 需要上传的数据 table:表名 condition:条件 cols：数据库列名 values：列值
		if (billNoList.size() < 1) {
			return;
		}
		tempidnode_down.clear();
		idnode_down.clear();
		// for (int i = 0; i < billNoList.size(); i++) {
		// // 每个单据
		// String billNo = billNoList.get(i);
		// // String idnode = idnodeList.get(i);
		// searchIdNodeByParent(billNo);
		// }
		searchIdNodeByParent(billNoList);
		// sendZipFile(Constant.TEMP_DIR+"temp.zip");
	}

	// TODO 查询照片
	public static List<Ctlm1347> searchPictureByParent(String billNo,
			String idnode) {
		tempidnode_down.clear();
		idnode_down.clear();
		idnode_down.add(idnode);
		// BusinessBaseDao.getCTLM1347IdNode(idnode, billNo, tempidnode_down);
		BusinessBaseDao.getCTLM1347ParentNode(idnode, billNo, tempidnode_down);
		while (tempidnode_down.size() > 0) {
			String id = tempidnode_down.pop();
			idnode_down.add(id);
			// BusinessBaseDao.getCTLM1347IdNode(id, null, tempidnode_down);
			BusinessBaseDao.getCTLM1347ParentNode(id, null, tempidnode_down);
		}
		List<Ctlm1347> list = BusinessBaseDao.getCTLM1347ByIdnodes(idnode_down);
		list = BusinessBaseDao.getCTLM1347ByParentnodes(idnode_down);
		List<Ctlm1347> list1347 = new ArrayList<Ctlm1347>();
		if (list == null) {
			return null;
		}
		// 返回与之相关的父节点
		// for(Ctlm1347 ctlm1347:list){
		// if(!WidgetName.HJ_PHOTOVIEW.equalsIgnoreCase(ctlm1347.getId_nodetype())){
		// list.remove(ctlm1347);
		// }
		// }

		for (Ctlm1347 ctlm1347 : list) {
			if (WidgetName.HJ_PHOTOVIEW.equalsIgnoreCase(ctlm1347
					.getId_nodetype())) {
				list1347.add(ctlm1347);
			}
		}
		Log.e(TAG, " list sieze " + list1347.size());
		return list1347;
	}

	// TODO
	private static void searchIdNodeByParent(List<String> billNo) {

		List<Ctlm1347> list = BusinessBaseDao.getCTLM1347Bybillno(billNo);

		String aa = new String();
		for (int i = 0; i < billNo.size(); i++) {
			aa = aa + billNo.get(i) + ",";
		}

		BusinessTableCreateModel businessTableCreateModel = new BusinessTableCreateModel();
		businessTableCreateModel.table = Tables.BusinessCtlm1347.NAME;
		businessTableCreateModel.param = aa;
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

			// String names = ctlm1347.getVar_data1();
			if (WidgetName.HJ_PHOTOVIEW.equalsIgnoreCase(ctlm1347
					.getId_nodetype().trim())) {

				String names = ctlm1347.getVar_data1();
				if (!TextUtils.isEmpty(names)) {

					String[] name = names.split(";");
					for (String s : name) {

						// File f = new File(Constant.HJPHOTO_CACHE_DIR + "/" +
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
						// 对文件进行压缩
						// compressFiles(f);
						files.add(f);
					}
				}
			}
			// String names = ctlm1347.getVar_data1();
			//
			// if(!TextUtils.isEmpty(names)&&WidgetName.HJ_PHOTOVIEW.equalsIgnoreCase(ctlm1347.getId_nodetype().trim())){
			// String []name = names.split(";");
			// for(String s:name){
			//
			// // File f = new File(Constant.HJPHOTO_CACHE_DIR + "/" +
			// ss[2]);//图片命名包含地址
			// File f = new File(Constant.HJPHOTO_CACHE_DIR + s);//图片命名不包含地址
			// // 对文件进行压缩
			// // compressFiles(f);
			// files.add(f);
			// }
			// }

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
		// sendZipFileFromBusinessActivity(Constant.TEMP_DIR+ uuid+".zip",list);
		sendZipFile(Constant.TEMP_DIR + uuid + ".zip", list);

	}

	private static void compressFiles(File f) {
		FileOutputStream out = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			FileInputStream file = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(file, null, options);
			int opt = 80;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, baos);
			file.close();
			if (baos.toByteArray().length / 1024 > 40) {
				opt = 40000 / (baos.toByteArray().length / 1024);
				baos.reset();
				bitmap.compress(CompressFormat.JPEG, opt, baos);
			}

			out = new FileOutputStream(f);
			out.write(baos.toByteArray(), 0, baos.toByteArray().length);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void sendZipFile(String name, final List<Ctlm1347> list1347) {

		MultipartEntity entity = new MultipartEntity();
		entity.addPart("download", new FileBody(new File(name)));
		HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP
				+ Constant.BUSINESS_SERVICE_ADDRESS);
		httpPost.setEntity(entity);
		Log.i("info", "上传服务地址：" + EapApplication.URL_SERVER_HOST_HTTP
				+ Constant.BUSINESS_SERVICE_ADDRESS);
		Message msg = BussinessUploadActivity.handler
				.obtainMessage(Constant.MSG_UPDATE_TEXT);
		String text = "正在 上传中....";
		msg.obj = text;
		BussinessUploadActivity.handler.sendMessage(msg);

		Log.i(TAG, ">>>>>>>>>>>>> " + "正在上传中");
		HttpClientManager.addTask(new HttpResponseHandler() {

			@Override
			public void onResponse(HttpResponse resp) {

				try {
					String msga = HttpClientManager.toStringContent(resp);

					try {
						JSONObject jsonObject = new JSONObject(msga);
						Log.i(TAG, ">>>>>>>>>>>>>> " + "上传成功,结果是  " + msga);
						String result = jsonObject.getString("flag");
						if ("ok".equalsIgnoreCase(result)) {
							// 修改数据库中的值
							BusinessBaseDao.updateCtlm1347s(list1347, "Y");
							Message msg = BussinessUploadActivity.handler
									.obtainMessage(Constant.MSG_UPLOAD_SUCCESS);
							String text = "上传成功!!!!";
							msg.obj = text;
							BussinessUploadActivity.handler.sendMessage(msg);
						} else {
							Log.i(TAG, ">>>>>>>>>>>>>>>>>>> " + " 上传失败");
							BusinessBaseDao.updateCtlm1347s(list1347, "N");
							Message msg = BussinessUploadActivity.handler
									.obtainMessage(Constant.MSG_UPLOAD_FAILED);
							String text = "上传失败....";
							msg.obj = text;
							BussinessUploadActivity.handler.sendMessage(msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onException(Exception e) {
				Log.i(TAG, ">>>>>>>>>>>>>>>>>>> " + " 上传失败");
				BusinessBaseDao.updateCtlm1347s(list1347, "N");
				Message msg = BussinessUploadActivity.handler
						.obtainMessage(Constant.MSG_UPLOAD_FAILED);
				String text = "上传失败....";
				msg.obj = text;
				BussinessUploadActivity.handler.sendMessage(msg);

			}
		}, httpPost);

	}

	private static void sendZipFileFromBusinessActivity(String name,
			final List<Ctlm1347> list1347) {
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("download", new FileBody(new File(name)));
		HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP
				+ Constant.BUSINESS_SERVICE_ADDRESS);
		httpPost.setEntity(entity);

		Message msg = BusinessActivity.handler
				.obtainMessage(Constant.MSG_UPDATE_TEXT);
		String text = "正在 上传中....";
		msg.obj = text;
		BusinessActivity.handler.sendMessage(msg);
		// ((Handler)
		// EapApplication.getApplication().getExtra(EapApplication.EXTRA_MAIN_HANDLER)).post(new
		// Runnable() {
		// @Override
		// public void run() {
		// WaitDialog waitDialog = new
		// WaitDialog(EapApplication.getApplication());
		// waitDialog.show();
		// }
		// });

		// Log.i(TAG,">>>>>>>>>>>>> " + "正在上传中");
		HttpClientManager.addTask(new HttpResponseHandler() {

			@Override
			public void onResponse(HttpResponse resp) {
				Log.i(TAG, ">>>>>>>>>>>>>> " + "上传成功");
				// 修改数据库中的值
				BusinessBaseDao.updateCtlm1347s(list1347, "Y");

				// Message msg =
				// BusinessActivity.handler.obtainMessage(Constant.MSG_UPLOAD_SUCCESS);
				// String text = "上传成功!!!!";
				// msg.obj = text;
				// BusinessActivity.handler.sendMessage(msg);

			}

			@Override
			public void onException(Exception e) {
				Log.i(TAG, ">>>>>>>>>>>>>>>>>>> " + " 上传失败");
				BusinessBaseDao.updateCtlm1347s(list1347, "N");
				Message msg = BusinessActivity.handler
						.obtainMessage(Constant.MSG_UPLOAD_FAILED);
				String text = "上传失败....";
				msg.obj = text;
				BusinessActivity.handler.sendMessage(msg);

			}
		}, httpPost);

	}

	/**
	 * 计算一对经纬度坐标之间的距离
	 * 
	 * @param s_oLat
	 *            原点纬度
	 * @param s_oLng
	 *            原点经度
	 * @param s_dLat
	 *            终点纬度
	 * @param s_dLng
	 *            终点经度
	 * @return 两点距离，单位为： 米, 转换错误时返回"-1".
	 */
	public String getbmap_distance(String s_oLat, String s_oLng, String s_dLat,
			String s_dLng) {
		try {
			double d_oLat = Double.parseDouble(s_oLat);
			double d_oLng = Double.parseDouble(s_oLng);
			double d_dLat = Double.parseDouble(s_dLat);
			double d_dLng = Double.parseDouble(s_dLng);
			LatLng origin = new LatLng(d_oLat, d_oLng);
			LatLng destination = new LatLng(d_dLat, d_dLng);
			double distance = DistanceUtil.getDistance(origin, destination);
			return String.valueOf(distance);
		} catch (Exception e) {
			Log.e(e);
			return "-1";
		}
	}

	static {
		System.loadLibrary("BaiduMapSDK_base_v4_0_0");
		System.loadLibrary("locSDK7");
	}

	// //设置单据号
	public static String jhjv_getbillno(String viewid) {
		return HjActivityManager.getInstance().hjv_getbillno(viewid);
	}

	// //设置单据号
	public static void jhjv_setbillno(String viewid, String billno) {
		HjActivityManager.getInstance().hjv_setbillno(viewid, billno);
	}

	// //设置单据号
	public static String jhjv_savedata(String viewid) {
		return String.valueOf(HjActivityManager.getInstance().hjv_savedata(
				viewid));
	}

	public static String jhjv_getparentnode(String viewid) {
		return HjActivityManager.getInstance().hjv_getparentnode(viewid);
	}

	public static void jhjc_setbackview(String viewid) {
		HjActivityManager.getInstance().hjc_setbackview(viewid);
	}

	public void hjc_setfinishview(String viewid) {
		HjActivityManager.getInstance().hjc_setfinishview(viewid);
	}

	public static void setcontorl_visible(String viewid, String controlid,
			String visible) {
		HjActivityManager.getInstance().setcontorl_visible(viewid, controlid,
				visible);
	}

	public static void jhjc_setvalue(String viewid, String controlid,
			String values) {
		HjActivityManager.getInstance().hjc_setvalue(viewid, controlid, values);
		Log.d("jhjc_setvalue", "viewid:" + viewid + "controlid:" + controlid
				+ "values:" + values);
	}

	public static void jhjc_setvalue(String viewid, String controlid,
			String row, String column, String values) {
		HjActivityManager.getInstance().hjc_setvalue(viewid, controlid, row,
				column, values);
		Log.d("jhjc_setvalue", "viewid:" + viewid + "controlid:" + controlid
				+ "values:" + values + "row:" + row + "column:" + column);
	}

	public static String jhjc_getvalue(String viewid, String controlid) {
		return HjActivityManager.getInstance().hjc_getvalue(viewid, controlid);
	}

	public static String jhjc_getvalue(String viewid, String controlid,
			String row, String column) {
		return HjActivityManager.getInstance().hjc_getvalue(viewid, controlid,
				row, column);
	}

	public static void jdts_additem(String viewid, String dataset,
			String condition) {
		HjActivityManager.getInstance().dts_additem(viewid, dataset, condition);
	}

	public static void jhjdts_setcondition(String viewid, String key,
			String condition) {
		HjActivityManager.getInstance()
				.dts_setcondition(viewid, key, condition);
	}

	public static void jhjds_search(String viewid, String datasource,
			String condition) {
		HjActivityManager.getInstance().hjds_search(viewid, datasource,
				condition);
	}

	public static void jhjc_setnextview(String viewid, String controlid,
			String row, String viewid2, String billno, String nodeid,
			String vlaues) {
		HjActivityManager.getInstance().hjc_setnextview(viewid, controlid, row,
				viewid2, billno, nodeid, vlaues);
	}

	public static void jhjc_controluplod(String viewid, String controlid) {
		HjActivityManager.getInstance().hjc_controluplod(viewid, controlid);
	}

	public static void jhjc_datarefresh(String viewid, String controlid,
			String datasource) {
		HjActivityManager.getInstance().hjc_datarefresh(viewid, controlid,
				datasource);
	}

	public static void jhjc_browsephoto(String viewid, String billno,
			String nodeid) {
		HjActivityManager.getInstance().hjc_browsephoto(viewid, billno, nodeid);
	}

	public static void jhjc_additem(String viewid, String controlid,
			String billno, String nodeid, String value) {
		HjActivityManager.getInstance().jhjc_setvalue(viewid, controlid,
				billno, nodeid, value);

	}

	public static String jhjc_Location(String viewid, String controlid) {
		return HjActivityManager.getInstance().hjc_Location(viewid, controlid);

	}

	public static void jhjc_setphoto(String viewid, String controlid) {
		HjActivityManager.getInstance().hjc_setphoto(viewid, controlid);
	}

	public static String jhju_getuuid() {
		return StringUtil.getMyUUID();
	}

	public static String jhju_getuser() {
		return SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId();

		// EapApplication.getApplication().getExtra(EapApplication.EXTRA_USER_ID).toString();
	}

	public static void jhjb_setddiscard(String viewid, String locationid,
			String photoid, String nextview, String billno, String nodeid) {
		HjActivityManager.getInstance().hjb_setddiscard(viewid, locationid,
				photoid, nextview, billno, nodeid);
	}

	public static int jhjc_getrowcount(String viewid, String controlid) {
		return Integer.parseInt(HjActivityManager.getInstance()
				.hjc_getrowcount(viewid, controlid));
	}

	public static void jhjc_setmaketext(String viewid, String msg) {
		HjActivityManager.getInstance().hjc_setmakeText(viewid, msg);
	}

	public static void jhju_sendsms(String viewid, String phone, String msg) {
		HjActivityManager.getInstance().hju_sendsms(viewid, phone, msg);
	}

	public static void jhjc_setenabled(String viewid, String controlid,
			String enabled) {
		HjActivityManager.getInstance().hjc_setenabled(viewid, controlid,
				enabled);
	}

	public static String jhjc_getuuid() {
		return HjActivityManager.getInstance().hju_getuuid();
	}

}

package com.mznerp.business.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mznerp.business.view.StartViewInfo;
import com.mznerp.business.view.ViewClass;
import com.mznerp.business.view.WidgetClass;
import com.mznerp.business.view.WidgetName;
import com.mznerp.business.view.uploadDialog;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.OtherBaseDao;
import com.mznerp.manager.HjActivityManager;
import com.mznerp.model.BusinessParam;
import com.mznerp.model.DisCardFageDataClass;
import com.mznerp.model.HJSender;
import com.mznerp.model.IDComConfig;
import com.mznerp.util.DateUtil;
import com.mznerp.util.DomXmlParse;
import com.mznerp.util.LuaLoadScript;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.util.myscom.FileUtils;
import com.mznerp.widget.WaitDialogRectangle;
import com.mznerp.R;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class BusinessActivity extends ActivitySupport implements
		View.OnClickListener {
	private static String TAG = "BusinessActivity";
	private String disViewID = null;
	private String disBillNo = null;
	private String disNodeId = null;
	private String disValues = null;
	private String onload = null;
	// private Stack<BusinessParam> id_set = new Stack<BusinessParam>(); //
	// 存储历史记录

	public Boolean isBack = true;

	String viewID;

	private List<ViewClass> list; // 当前整个功能的界面，<HJModel下面的全部View的集合
	protected static Context context;
	public static ViewClass currentViewClass; // 当前显示的View，<HJModel下面的某一个View
	public StartViewInfo startViewInfo; // model.xml 头信息
	public BusinessParam currentBusParam; // 当前界面上参数
	private static String id_model;// 当前的业务类型
	protected static WaitDialogRectangle waitDialogRectangle;
	private TextView mapMode;
	private static LinearLayout mMarkForDiscard;
	private DisCardFage disCardFage;
	private boolean isScatterMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.businessactivity);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.refreshMapData");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		mapMode = (TextView) findViewById(R.id.p1);
		mMarkForDiscard = (LinearLayout) findViewById(R.id.mark_for_discard);
		mapMode.setOnClickListener(this);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		id_model = getIntent().getStringExtra("id_model");// 得到要展示的xml模板名称
		// /如果只传递过来来一个模板的时候
		if (id_model != null) {
			String praseResult = praseXMLModel(id_model);

			if ("error".equalsIgnoreCase(praseResult)) {
				ToastUtil.ShowShort(this, "解析文件出错，请确认模板是否定义正确。");
				this.finish();
				return;
			}
			canUseMap();
			currentBusParam = new BusinessParam();
			currentBusParam.setModelId(startViewInfo.id);
			currentBusParam.setViewId(startViewInfo.start);
			currentBusParam.setIdParentNode("");
			currentBusParam.setBillNo("");
			currentBusParam.setDataOpr(DateUtil.getCurDateStr());
			HjActivityManager.getInstance().setViewClassList(list);
			HjActivityManager.getInstance().push(this);
			BusinessActivityFragment firstFragment = new BusinessActivityFragment();
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("currentBusParam", currentBusParam);
			currentViewClass = getViewByViewId(list);
			mBundle.putSerializable("currentViewClass", currentViewClass);
			mBundle.putSerializable("startViewInfo", startViewInfo);
			firstFragment.setArguments(mBundle);
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.business_fragment, firstFragment,
							startViewInfo.start).commit();
			Log.v("show", "模板解析后的结果:" + currentViewClass+"启动页面"+startViewInfo.start);
		}
		this.closeInput();
	}

	public static void canUseMap() {
		// TODO Auto-generated method stub
		IDComConfig idconfig = OtherBaseDao.queryReginfo(sputil.getComid());
		if (idconfig != null && idconfig.getFlag_map().startsWith("Y")) {
			showMarkForDiscard();
		} else {
			closeMarkForDiscard();
		}
	}

	private ViewClass getViewByViewId(List<ViewClass> list) {
		for (ViewClass items : list) {
			if (currentBusParam.getViewId().equalsIgnoreCase(items.id)) {
				return items;
			}
		}
		return null;
	}

	private String praseXMLModel(String xmlName) {
		String praseResult = "ok";
		try {
			// InputStream in = getAssets().open("discard.xml");//discard.xml
			InputStream in = FileUtils.openInputStream(new File(EapApplication
					.getApplication().getFilesDir() + "/" + xmlName + ".xml"));
			startViewInfo = DomXmlParse.getStartViewInfo(in);
			list = DomXmlParse.getViews(in);
			Log.v("show", "模板解析后list的结果:" + list.toString());
		} catch (IOException e) {
			praseResult = "error";
			e.printStackTrace();
		} catch (SAXException e) {
			praseResult = "error";
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			praseResult = "error";
			e.printStackTrace();
		}
		return praseResult;
	}

	@Override
	public void onBackPressed() {
		if (isBack) {
			List<Fragment> lisFragment = getSupportFragmentManager()
					.getFragments();

			if (lisFragment.size() > 1) {
				for (int i = lisFragment.size() - 1; i > 1; i--) {
					BusinessActivityFragment frangt = (BusinessActivityFragment) lisFragment
							.get(i);
					if (frangt != null) {
						if (frangt.currentViewClass != null
								&& !frangt.currentViewClass.returnenable) {
							return;
						}
						break;
					}
				}

			}
		}
		super.onBackPressed();
	}

	public void setNextView(String viewid2, String billno, String nodeid,
			String values) {
		BusinessParam bparam = new BusinessParam();
		bparam.setBillNo(billno);
		// //在这里要调成本身的ID_node
		bparam.setIdParentNode(nodeid);
		bparam.setModelId(currentBusParam.getModelId());
		bparam.setViewId(viewid2);
		bparam.setDataOpr(currentBusParam.getDataOpr());
		bparam.setTable("transfer_dts");
		bparam.setVarJosn(values);

		BusinessActivityFragment firstFragment = new BusinessActivityFragment();
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("currentBusParam", bparam);
		for (ViewClass items : list) {
			if (viewid2.equalsIgnoreCase(items.id)) {
				currentViewClass = items;
				break;
			}
		}
		mBundle.putSerializable("currentViewClass", currentViewClass);
		mBundle.putSerializable("startViewInfo", startViewInfo);
		firstFragment.setArguments(mBundle);

		FragmentTransaction ft2 = getSupportFragmentManager()
				.beginTransaction();
		ft2.setCustomAnimations(R.animator.rotateup_right_in,
				R.animator.rotateup_right_out);
		ft2.add(R.id.business_fragment, firstFragment, viewid2);
		ft2.addToBackStack(null);
		ft2.commit();
		closeMarkForDiscard();
	}

	/**
	 * 控制底部菜单的显示
	 */
	public static void showMarkForDiscard() {
		if (StringUtil.isStrTrue(id_model) && id_model.equals("discard")) {
			mMarkForDiscard.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏底部菜单按钮
	 */
	public static void closeMarkForDiscard() {
		if (StringUtil.isStrTrue(id_model) && id_model.equals("discard")) {
			mMarkForDiscard.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("resultCode", requestCode + "" + resultCode);
		if (requestCode == 1002 && resultCode == 1002) {
			Bundle bundle = data.getExtras();
			String str = bundle.getString("result");
			String datasource = bundle.getString("id_table");
			if (!"".equalsIgnoreCase(viewID)) {
				BusinessActivityFragment bfragment = (BusinessActivityFragment) getSupportFragmentManager()
						.findFragmentByTag(viewID);
				bfragment.add_search(str, datasource);
			}
		} else if ((requestCode == 1003 || requestCode == 132075)
				&& resultCode == RESULT_OK) {
			DisCardFageDataClass dataClass = data
					.getParcelableExtra("DisCardData");
			disViewID = dataClass.getViewid2();
			disBillNo = dataClass.getBillno();
			disNodeId = dataClass.getNodeid();
			disValues = dataClass.getValue();

			setDataForMap(disBillNo, disNodeId, disValues);
		}
	}

	private void setDataForMap(String disBillNo, String disNodeId,
			String disValues) {

		// TODO Auto-generated method stub
		try {

			List<WidgetClass> items = currentViewClass.list;
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).widgetType
						.equalsIgnoreCase(WidgetName.HJ_LIST)) {

					onload = items.get(i).attribute.onclick;
				}
			}

			Map<String, String> mMap = new HashMap<String, String>();
			mMap.put(HJSender.COL, "");
			mMap.put(HJSender.COLID, "");
			mMap.put(HJSender.ROW, "");
			mMap.put(HJSender.VALUES, disValues);
			mMap.put(HJSender.BILLNO, disBillNo);
			mMap.put(HJSender.NODEID, disNodeId);

			LuaLoadScript.LoadScript();
			LuaLoadScript.runScript(onload, mMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /弹出选择窗口
	public void datastorce_search(String viewid, String datasource,
			String condition) {
		viewID = viewid;

		Intent intent = new Intent();
		intent.setClass(context, AddRouteActivity.class);
		intent.putExtra("id_model", id_model);
		intent.putExtra(AddRouteActivity.DATA_RESOURCE, datasource);
		intent.putExtra(AddRouteActivity.SERACH_BY_CONDITION, condition);
		startActivityForResult(intent, 1002);
	}

	public void startShowPictures(String billNo, String idnode) {
		Intent intent = new Intent();
		intent.putExtra("billNo", billNo);
		intent.putExtra("idnode", idnode);
		intent.setClass(this, SearchBussinessPicture.class);
		startActivity(intent);
	}

	public void showUploadDialog(String billno) {
		List<String> parentnode = new ArrayList<String>();
		parentnode.add(billno);
		uploadDialog dlg = new uploadDialog(this, parentnode);
		dlg.showDialog();

	}

	public void setmakeText(String msg) {
		ToastUtil.ShowShort(getContext(), msg);
	}

	// 发送短信
	public void sendsms(String phone, String message) {
		Intent sendIntent;
		Uri uri = null;
		if (StringUtil.isNullOrEmpty(phone)) {
			uri = Uri.parse("smsto:" + phone);
		}

		sendIntent = new Intent(Intent.ACTION_VIEW, uri);
		sendIntent.putExtra("sms_body", message);
		startActivity(sendIntent);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	public Handler disHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				setNextView(disViewID, disBillNo, disNodeId, disValues);
				break;

			default:
				break;
			}
		}

	};

	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (context == null) {
				return;
			}
			String text = (String) msg.obj;
			switch (msg.what) {
			case Constant.MSG_SHOW:
				break;
			case Constant.MSG_UPLOAD_SUCCESS:

				if (waitDialogRectangle.isShowing()) {
					waitDialogRectangle.dismiss();
					ToastUtil.ShowShort(context, text);
				}
				break;
			case Constant.MSG_UPLOAD_FAILED:

				if (waitDialogRectangle.isShowing()) {
					waitDialogRectangle.dismiss();
					ToastUtil.ShowShort(context, text);
				}
				break;
			case Constant.MSG_UPDATE_TEXT:
				waitDialogRectangle = new WaitDialogRectangle(context);
				waitDialogRectangle.show();
				waitDialogRectangle.setText(text);
				break;
			default:
				break;
			}

		};
	};

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.refreshMapData")) {
				initView();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}

	// 跳转到地图
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.p1:
			Intent intent = new Intent();
			intent.setClass(this, DisCardFage.class);
			startActivityForResult(intent, 1003);
			break;
		}
	}

}

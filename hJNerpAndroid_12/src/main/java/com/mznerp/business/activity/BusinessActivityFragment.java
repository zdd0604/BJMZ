package com.mznerp.business.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mznerp.business.BusinessTask;
import com.mznerp.business.Ctlm1345Update;
import com.mznerp.business.view.StartViewInfo;
import com.mznerp.business.view.ViewClass;
import com.mznerp.business.view.WidgetClass;
import com.mznerp.business.view.locationDialog;
import com.mznerp.business.view.uploadDialog;
import com.mznerp.common.Constant;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.BusinessData;
import com.mznerp.model.BusinessParam;
import com.mznerp.model.Ctlm1347;
import com.mznerp.util.Command.OnResultListener;
import com.mznerp.util.HJDialog;
import com.mznerp.util.ImageFileHelper;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.util.WidgetFactoryUtils;
import com.mznerp.util.imageCompressUtil;
import com.mznerp.widget.HJListLayout;
import com.mznerp.widget.HJPhotoView;
import com.mznerp.widget.HJToolBar;
import com.mznerp.widget.HJViewInterface;
import com.mznerp.widget.HJViewMenu;
import com.mznerp.widget.WaitDialogRectangle;
import com.mznerp.R;

import java.util.ArrayList;
import java.util.List;

public class BusinessActivityFragment extends Fragment {

	public LinearLayout linearLayout;// 自定义控件的父layout(不包含TAB等底部和头部自定义控件)
	public ScrollView scrollView;
	public LinearLayout stub;// 底部不随页面滚动TAB控件
	public LinearLayout viewMenu;// 右上角更多控件
	private RelativeLayout rl_actionbar_back;

	public RelativeLayout rl_loading;
	private TextView title;
	private List<ViewClass> list; // 当前整个功能的界面，<HJModel下面的全部View的集合
	protected static Context context;
	protected static WaitDialogRectangle waitDialogRectangle;

	public ViewClass currentViewClass; // 当前显示的View，<HJModel下面的某一个View
	public StartViewInfo startViewInfo; // model.xml 头信息

	public BusinessParam currentBusParam; // 当前界面上参数

	public View contextView;

	public ArrayList<Ctlm1347> ctlm1347List;
	public List<BusinessData> ctlm1345List = new ArrayList<BusinessData>();

	private String id_model;// 当前的业务类型

	private Boolean isDdiscard = false;
	private String ddiscardbillno, ddiscardview, ddiscardnodeid;

	public static BusinessActivityFragment newInstance(BusinessParam imageUrl) {
		final BusinessActivityFragment f = new BusinessActivityFragment();

		final Bundle args = new Bundle();
		args.getSerializable("currentBusParam");
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.currentBusParam = (BusinessParam) (getArguments() != null ? getArguments()
				.getSerializable("currentBusParam") : null);
		this.currentViewClass = (ViewClass) (getArguments() != null ? getArguments()
				.getSerializable("currentViewClass") : null);
		this.startViewInfo = (StartViewInfo) (getArguments() != null ? getArguments()
				.getSerializable("startViewInfo") : null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contextView = inflater.inflate(R.layout.businessactivity_fragment,
				container, false);
		displayViewWithoutData();
		prepareDate();
		return contextView;
	}

	@Override
	public void onDestroyView() {
		// FragmentManager mFragmentMgr = this.getActivity()
		// .getSupportFragmentManager();
		// FragmentTransaction mTransaction = mFragmentMgr.beginTransaction();
		// Fragment childFragment = mFragmentMgr
		// .findFragmentByTag(currentViewClass.id);
		// if (childFragment != null) {
		// mTransaction.remove(childFragment);
		// mTransaction.commit();
		// }
		super.onDestroyView();

	}

	// 显示控件，不加载数据
	private void displayViewWithoutData() {

		linearLayout = (LinearLayout) contextView
				.findViewById(R.id.linear_main);
		rl_loading = (RelativeLayout) contextView.findViewById(R.id.rl_loading);
		rl_loading.setVisibility(View.VISIBLE);
		scrollView = (ScrollView) contextView.findViewById(R.id.scroll);
		stub = (LinearLayout) contextView.findViewById(R.id.tool_bar);
		viewMenu = (LinearLayout) contextView
				.findViewById(R.id.businessmenu_btn);

		title = (TextView) contextView.findViewById(R.id.tv_actionbar_title);
		rl_actionbar_back = (RelativeLayout) contextView
				.findViewById(R.id.rl_actionbar_back);

		stub.setVisibility(View.GONE);// 底部不随页面滚动TAB控件
		viewMenu.setVisibility(View.GONE);// 右上角更多控件

		// 设置标题
		title.setText(currentViewClass.name);

		// 点击title返回
		rl_actionbar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backa();
			}
		});
		// 开始显示布局，控件只显示不设置数据
		List<WidgetClass> items = currentViewClass.list;
		for (int i = 0; i < items.size(); i++) {
			View view = WidgetFactoryUtils.createView(items.get(i),
					this.getActivity(), currentViewClass, startViewInfo,
					currentBusParam, scrollView, contextView);
			if (view != null) {
				if (view instanceof HJToolBar) {
					stub.setVisibility(View.VISIBLE);
					stub.addView(view);
				} else if (view instanceof HJViewMenu) {
					viewMenu.setVisibility(View.VISIBLE);
					viewMenu.addView(view);
				} else {
					linearLayout.addView(view);
				}
			}
		}
	}

	public void backa() {
		if (this.getActivity().getSupportFragmentManager().getFragments()
				.size() > 1
				&& currentViewClass != null && !currentViewClass.returnenable) {
			return;
		}

		if (currentViewClass.backdel) {
			BusinessBaseDao.deleteViewData(currentBusParam);
		}
		if (currentViewClass.backsave) {
			saveData();
		}
		this.getActivity().onBackPressed();
	}

	public void prepareDate() {
		rl_loading.setVisibility(View.VISIBLE);
		String dataset;
		dataset = currentViewClass.dataset;

		if (StringUtil.isNullOrEmpty(dataset))
			dataset = "";

		BusinessTask businesstask = new BusinessTask(this);
		businesstask.execute(dataset, "0");

	}

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

	// /取得窗口空间
	public HJViewInterface getControl(String controlid) {
		int count = linearLayout.getChildCount();
		HJViewInterface view = null;
		Boolean find = false;
		for (int i = 0; i < count; i++) {
			view = (HJViewInterface) linearLayout.getChildAt(i);
			if (controlid.equalsIgnoreCase(view.getID())) {
				find = true;
				return view;
			}
		}
		// /工具栏
		if (!find) {
			count = stub.getChildCount();
			for (int i = 0; i < count; i++) {
				view = (HJViewInterface) linearLayout.getChildAt(i);
				if (controlid.equalsIgnoreCase(view.getID())) {
					find = true;
					return view;
				}
			}
		}
		// /
		// ///菜单
		// if (!find )
		// {
		// count= viewMenu.get
		// for (int i = 0; i < count; i++) {
		// view= (HJViewInterface) linearLayout.getChildAt(i);
		// if (controlid.equalsIgnoreCase(view.getID())) {
		// find = true;
		// return view;
		// }
		// }
		// }
		return null;
	}

	// /弹出选择窗口
	public void datastorce_search(String viewid, String datasource,
			String condition) {
		Intent intent = new Intent();
		intent.setClass(this.getActivity(), AddRouteActivity.class);
		intent.putExtra("id_model", id_model);
		intent.putExtra(AddRouteActivity.DATA_RESOURCE, datasource);
		intent.putExtra(AddRouteActivity.SERACH_BY_CONDITION, condition);
		startActivityForResult(intent, 1002);
	}

	// /弹出选择窗口
	public void add_search(String values, String idTable) {
		int count2 = linearLayout.getChildCount();
		HJViewInterface view = null;
		for (int i = 0; i < count2; i++) {
			view = (HJViewInterface) linearLayout.getChildAt(i);
			if (idTable.equalsIgnoreCase(view.getDataSource())) {
				view.setJesonValue(values);
				view.saveData(true);
			}
		}
	}

	public void setcontorl_visible(String controlid, String visible) {
		int count = linearLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) linearLayout.getChildAt(i);
			if (controlid.equalsIgnoreCase(view.getID())) {
				if ("0".equalsIgnoreCase(visible)) {
					linearLayout.getChildAt(i).setVisibility(View.GONE);
				} else {
					linearLayout.getChildAt(i).setVisibility(View.VISIBLE);
				}

			}
		}
	}

	public String getViewId() {
		return currentBusParam.getViewId();
	}

	public String getBillno() {
		return currentBusParam.getBillNo();
	}

	public String getParentNode() {
		return currentBusParam.getIdParentNode();
	}

	// /保存数据
	public int saveData() {
		int count = linearLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) linearLayout.getChildAt(i);
			if (!view.validate())
				return 0;

		}
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) linearLayout.getChildAt(i);
			view.saveData(true);
		}

		return 1;
	}

	// private void setsaveData() {
	// int count = linearLayout.getChildCount();
	//
	// for (int i = 0; i < count; i++) {
	// HJViewInterface view = (HJViewInterface) linearLayout.getChildAt(i);
	// view.saveData(false);
	// }
	//
	// // //保存工具栏
	// count = stub.getChildCount();
	// for (int i = 0; i < count; i++) {
	// HJViewInterface view = (HJViewInterface) stub.getChildAt(i);
	// view.saveData(false);
	// }
	// // //保存菜单
	// count = viewMenu.getChildCount();
	// for (int i = 0; i < count; i++) {
	// HJViewInterface view = (HJViewInterface) viewMenu.getChildAt(i);
	// view.saveData(false);
	// }
	//
	// }

	public void startShowPictures(String billNo, String idnode) {
		Intent intent = new Intent();
		intent.putExtra("billNo", billNo);
		intent.putExtra("idnode", idnode);
		intent.setClass(this.getActivity(), SearchBussinessPicture.class);
		startActivity(intent);
	}

	public void startDataUplod(String controlId) {
		HJViewInterface view = getControl(controlId);
		if (view == null)
			return;
		WidgetClass items = null;
		if (view instanceof HJListLayout) {
			items = ((HJListLayout) view).getitems();
		}
		Intent intent = new Intent(this.getActivity(),
				BussinessUploadActivity.class);
		intent.putExtra("control_id", controlId);
		intent.putExtra("items", items);
		intent.putExtra("data", currentBusParam);
		startActivity(intent);
	}

	// 刷新控件数据源
	public void set_datarefresh(String controlid, String datasource) {
		Log.d("set_datarefresh", controlid + " " + datasource);
		int count = linearLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) linearLayout.getChildAt(i);
			if (controlid.equalsIgnoreCase(view.getID())) {
				view.setDataSource(datasource);

				for (int j = 0; j < ctlm1345List.size(); j++) {
					if (datasource.equalsIgnoreCase(ctlm1345List.get(j)
							.getId_table())) {
						view.setDataBuild(null, ctlm1345List.get(j));
					}
				}
			}

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		int count = linearLayout.getChildCount();
		if (resultCode == Activity.RESULT_OK && requestCode == 1001) {// HJPhotoView控件拍照返回
			Bitmap bitmap = imageCompressUtil
					.setCompressImage(Constant.HJPHOTO_CACHE_DIR
							+ ImageFileHelper.getInstance().getFileName());
			if (bitmap != null) {
				try {
					imageCompressUtil.saveImagebus(bitmap,
							Constant.HJPHOTO_CACHE_DIR
									+ ImageFileHelper.getInstance()
											.getFileName());
					System.gc();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return;
			}
			for (int i = 0; i < count; i++) {
				final View view = linearLayout.getChildAt(i);
				if (view instanceof HJPhotoView) {
					if (((HJPhotoView) view).flag_curr) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								((HJPhotoView) view).flag_curr = false;
								((HJPhotoView) view).array.add(ImageFileHelper
										.getInstance().getFileName());
								((HJPhotoView) view).activityRefreshGallery();
							}
						}, 100);
					}
				}
			}
			// //如果是定位的数据则加在下一个界面
			if (isDdiscard) {
				((BusinessActivity) this.getActivity()).setNextView(
						ddiscardview, ddiscardbillno, ddiscardnodeid, "");
			}
		}

		if (resultCode == Activity.RESULT_CANCELED && requestCode == 1001) {
			// // /拍照取消
			for (int i = 0; i < count; i++) {
				final View view = linearLayout.getChildAt(i);
				if (view instanceof HJPhotoView) {
					((HJPhotoView) view).flag_curr = false;
				}
			}
		}

	}

	// TODO -- 修改数据集的条件
	public void dataset_setcondition(String key, String condition) {
		// dataset_additem(viewid, key, condition);
		String dataSet = currentViewClass.dataset;
		if (StringUtil.isNullOrEmpty(dataSet))
			return;
		String execdataset = null;
		String ds[] = dataSet.split(";");

		String[] idtables = new String[ds.length];
		String[] conditions = new String[ds.length];

		for (int i = 0; i < ds.length; i++) {
			try {
				String[] params = ds[i].split(":");
				idtables[i] = params[0];
				if (key.equalsIgnoreCase(idtables[i])) {
					execdataset = params[0] + ":" + condition;
					conditions[i] = condition;
				} else {
					conditions[i] = params[1];
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dataSet = "";
		for (int i = 0; i < ds.length; i++) {
			dataSet = dataSet + ";" + idtables[i] + ":" + conditions[i];
		}
		dataSet = dataSet.substring(1);
		currentViewClass.dataset = dataSet;
		// BusinessTask businesstask = new BusinessTask(this);
		// businesstask.execute(execdataset, "1");
	}

	// "tk:1=1"-- 添加一项数据集
	public void dataset_additem(String datasetid, String condition) {
		String dataSet = currentViewClass.dataset;
		if (StringUtil.isNullOrEmpty(dataSet)) {
			currentViewClass.dataset = datasetid + ":" + condition + "";
		} else {
			currentViewClass.dataset = currentViewClass.dataset + ";"
					+ datasetid + ":" + condition;
		}
		// //直接用数据下载
		rl_loading.setVisibility(View.VISIBLE);
		MySSListener ll = new MySSListener();
		ll.setMySSListener(datasetid, condition);
		Ctlm1345Update ctlm1345 = new Ctlm1345Update(
				new String[] { datasetid }, new String[] { condition }, ll);
		ctlm1345.action();

	}

	// Task在另外的线程执行，不能直接在Task中更新UI，因此创建了Handler
	private Handler handlers = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			rl_loading.setVisibility(View.VISIBLE);
			String m = (String) msg.obj;
			String sDataSource;
			// //更新界面数数据
			if (!"".equalsIgnoreCase(m)) {
				int count = linearLayout.getChildCount();
				for (int i = 0; i < count; i++) {
					HJViewInterface view = (HJViewInterface) linearLayout
							.getChildAt(i);
					sDataSource = view.getDataSource();
					if (sDataSource.equalsIgnoreCase(m)
							&& !StringUtil.isNullOrEmpty(sDataSource)
							&& sDataSource.equalsIgnoreCase(m)) {

						for (BusinessData bd : ctlm1345List) {
							if (sDataSource.equalsIgnoreCase(bd.getId_table())
									&& sDataSource.equalsIgnoreCase(m)) {

								view.setDataBuild(false, bd);
							}
						}

					}

				}
			}
			rl_loading.setVisibility(View.GONE);
		}
	};

	class MySSListener implements OnResultListener {

		String condition;
		String id_table;

		public void setMySSListener(String id_table, String condition) {
			this.condition = condition;
			this.id_table = id_table;
		}

		@Override
		public void onResult(boolean success) {
			if (success) {
				BusinessData busData = new BusinessData();
				busData = BusinessBaseDao.getBusinessData(id_table, condition);
				if (busData != null) {
					busData.setId_view(currentViewClass.id);
					busData.setId_table(id_table);
					ctlm1345List.add(busData);
				}

				Message mg = Message.obtain();
				mg.obj = id_table;
				handlers.sendMessage(mg);

			} else {
				Message mg = Message.obtain();
				mg.obj = "";
				handlers.sendMessage(mg);
			}

		}

	}

	public void showUploadDialog(String billno) {
		List<String> parentnode = new ArrayList<String>();
		parentnode.add(billno);
		uploadDialog dlg = new uploadDialog(this.getActivity(), parentnode);
		dlg.showDialog();

	}

	public String setLocation(String controlid) {
		int dlgret = 0;
		String dLocation = "";
		do {
			dlgret = 0;
			locationDialog dlg = new locationDialog((BusinessActivity) this.getActivity(), "");
			dLocation = dlg.showDialog();
			//
			if (!StringUtil.isNullOrEmpty(dLocation)) {
				HJViewInterface control = getControl(controlid);
				if (controlid != null) {
					control.setValue(dLocation);
					control.saveData(true);
				}
			}
			// Log.i(TAG, dLocation);
			if (StringUtil.isNullOrEmpty(dLocation)) {
				HJDialog hjdlg = new HJDialog(
						(BusinessActivity) this.getActivity(), "定位不成功！是否继续定位?",
						true);
				dlgret = hjdlg.showDialog();
				dLocation = "";
			}
		} while (dlgret == 1);
		return dLocation;

	}

	public void setb_Ddiscard(String photoid, String nextview, String billno,
			String nodeid) {
		int count = linearLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) linearLayout.getChildAt(i);
			// //拍照
			if (photoid.equalsIgnoreCase(view.getID())) {
				isDdiscard = true;
				ddiscardbillno = billno;
				ddiscardview = nextview;
				ddiscardnodeid = nodeid;
				view.setPhoto();
			}
		}

	}

	public void setPhoto(String controlid) {
		HJViewInterface control = getControl(controlid);
		if (controlid != null) {
			control.setPhoto();
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void setcontorl_enabled(String controlid, String enabled) {
		// HJViewInterface control = getControl(controlid);
		int count = linearLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) linearLayout.getChildAt(i);
			if (controlid.equalsIgnoreCase(view.getID())) {
				if ("0".equalsIgnoreCase(enabled)) {
					linearLayout.getChildAt(i).setEnabled(false);
				} else {
					linearLayout.getChildAt(i).setEnabled(true);
				}
				return;
			}
		}

		// 添加工具栏

		count = stub.getChildCount();
		for (int i = 0; i < count; i++) {
			Button view = (Button) linearLayout.getChildAt(i);
			if (controlid.equalsIgnoreCase((String) view.getTag())) {
				if ("0".equalsIgnoreCase(enabled)) {
					linearLayout.getChildAt(i).setEnabled(false);
				} else {
					linearLayout.getChildAt(i).setEnabled(true);
				}
				return;
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("currentViewClass.id",currentViewClass.id);
		if (currentViewClass != null && startViewInfo != null
				&& currentViewClass.id.equalsIgnoreCase("002")
				&& startViewInfo.id.equalsIgnoreCase("001")) {
			BusinessActivity.canUseMap();
			// HjActivityManager.getInstance().hjc_datarefresh("001", "001001",
			// "ctlm4101");
			Intent intent1=new Intent();
			intent1.setAction("action.refreshMapData");
			this.getActivity().sendBroadcast(intent1);
			SharedPreferences sharedPref = getActivity().getSharedPreferences(
					"mapMode", Context.MODE_PRIVATE);
			boolean isScatterMode = sharedPref.getBoolean("isScatterMode",
					false);
			if (isScatterMode) {
				Intent intent3;
				intent3 = new Intent(getActivity(), DisCardFage.class);
				// intent3.putExtra("scatterMode", "ok");
				startActivityForResult(intent3, 1003);
			}

		}

	}

}

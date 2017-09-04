package com.hjnerp.business;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;

import com.hjnerp.business.activity.BusinessActivityFragment;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessData;
import com.hjnerp.util.Command.OnResultListener;
import com.hjnerp.util.LuaLoadScript;
import com.hjnerp.util.StringUtil;
import com.hjnerp.widget.HJViewInterface;

public class BusinessTask extends AsyncTask<String, Integer, Integer> {

	private BusinessActivityFragment activitySupport;
	private Context context;
	private Boolean netCompleted = false;
	private String DATEMODEL_NET = "net";
	private String DATEMODEL_NETLOCAL = "net-local";
	private String DATEMODEL_LOCAL = "local";
	private String DATEMODEL_LOCALNET = "local-net";
	private String DATASETMODE = "";
	private Boolean isonload = false;

	public BusinessTask(BusinessActivityFragment activitySupport) {

		this.activitySupport = activitySupport;
		this.context = activitySupport.getActivity();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... params) {
		return LoadData(params[0], params[1]);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		activitySupport.rl_loading.setVisibility(View.VISIBLE);// 去掉加载提示
	}

	@Override
	protected void onPostExecute(Integer result) {
		activitySupport.rl_loading.setVisibility(View.VISIBLE);// 去掉加载提示
		int count = activitySupport.linearLayout.getChildCount();
		String sDataSource;
		switch (result) {
		case 0:

		case 1: // 更新界面数据从ctlm1347中
			// ////读取数据源 规则
			for (int i = 0; i < count; i++) {
				HJViewInterface view = (HJViewInterface) activitySupport.linearLayout
						.getChildAt(i);
				view.setDataBuild(true, null);
			}
			// scrollView默认滚动到顶部
			activitySupport.scrollView.smoothScrollTo(0, 0);
			break;
		case 2: // 更新界面默认数据与传过来数据
			for (int i = 0; i < count; i++) {
				HJViewInterface view = (HJViewInterface) activitySupport.linearLayout
						.getChildAt(i);
				sDataSource = view.getDataSource();
				if ("transfer_dts".equalsIgnoreCase(sDataSource)) {
					view.setJesonValue(activitySupport.currentBusParam
							.getVarJosn());
				} else {
					// 加载默认值
					view.setValueDefault();
					// 加载ctlm1345值
					// view.setDataBuild(null,
					// activitySupport.ctlm1345List.get(location));
					if (StringUtil.isNullOrEmpty(sDataSource))
						sDataSource = "";
					if (!"".equalsIgnoreCase(sDataSource)) {
						for (BusinessData bd : activitySupport.ctlm1345List) {
							if (sDataSource.equalsIgnoreCase(bd.getId_table())) {
								view.setDataBuild(false, bd);
							}
						}
					}
				}
			}
			break;
		case 3: // /只更新默认值
			for (int i = 0; i < count; i++) {
				HJViewInterface view = (HJViewInterface) activitySupport.linearLayout
						.getChildAt(i);
				sDataSource = view.getDataSource();
				if ("transfer_dts".equalsIgnoreCase(sDataSource)) {
					view.setJesonValue(activitySupport.currentBusParam
							.getVarJosn());
				} else {
					// 加载默认值
					view.setValueDefault();
				}
			}

			break;
		}

		// // /加载数据库 onload事件
		if (!StringUtil.isNullOrEmpty(activitySupport.currentViewClass.onload)
				&& isonload) {
			try {
				LuaLoadScript.LoadScript();
				LuaLoadScript.runScript(activitySupport.currentViewClass.onload);
			} catch (Exception e) {
				e.printStackTrace();
				// TestinAgent.leaveBreadcrumb("User tapped a button");
			}

		}

		if (isonload
				&& activitySupport.currentViewClass.presave
				&& !"query"
						.equalsIgnoreCase(activitySupport.startViewInfo.type)) {
			setsaveData();
		}
		//
		activitySupport.rl_loading.setVisibility(View.GONE);// 去掉加载提示
		activitySupport.scrollView.smoothScrollTo(0, 0);
	}
	

	// /加载数据
	private Integer LoadData(String dataSet, String flag) {
		publishProgress(1);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if ("0".equalsIgnoreCase(flag)) {
			isonload = true;
		}
		if ("0".equalsIgnoreCase(flag)
				&& !"query"
						.equalsIgnoreCase(activitySupport.startViewInfo.type)) {

			int rowcount = BusinessBaseDao.getCtlm1347ListByBillCount(
					activitySupport.currentBusParam.getIdParentNode(),
					activitySupport.currentBusParam.getBillNo(),
					activitySupport.currentBusParam.getModelId(),
					activitySupport.currentBusParam.getViewId());

			// ///当前数据不存在的时候加载默认数据与设置
			if (rowcount > 0)
				return 1;
		}

		if (StringUtil
				.isNullOrEmpty(activitySupport.currentViewClass.datasetmode
						.trim())) {
			DATASETMODE = DATEMODEL_NET;

		} else {
			DATASETMODE = activitySupport.currentViewClass.datasetmode.trim();
		}

		// //如果数据集为空则加载默认值
		if (StringUtil.isNullOrEmpty(dataSet))
			return 3;

		// 解释dataset
		String ds[] = dataSet.split(";");
		String[] idtables = new String[ds.length];
		String[] conditions = new String[ds.length];

		for (int i = 0; i < ds.length; i++) {

			try {
				String[] params = ds[i].split(":");
				if ("0".equalsIgnoreCase(flag)) {
					String conditionslua = "";
					LuaLoadScript.LoadScript();
					conditionslua = LuaLoadScript.runScriptreturn(params[1]);
					if (StringUtil.isNullOrEmpty(conditionslua)) {
						conditionslua = "1=1";
					}
					conditions[i] = conditionslua;
				} else {
					conditions[i] = params[1];
				}

				idtables[i] = params[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		publishProgress(1);
		// 读取本数据
		if (DATEMODEL_LOCAL.equalsIgnoreCase(DATASETMODE)
				|| DATEMODEL_LOCALNET.equalsIgnoreCase(DATASETMODE)) {
			// /
			getLocalBusinessData(idtables, conditions);
			// /如果只是本地时，直接返回。
			if (DATEMODEL_LOCAL.equalsIgnoreCase(DATASETMODE))
				return 2;
		}
		// /如果从网络或网络到本地时， 先执行网络 、执行过程为网络、本地
		if (DATEMODEL_NETLOCAL.equalsIgnoreCase(DATASETMODE)
				|| DATEMODEL_NET.equalsIgnoreCase(DATASETMODE)) {
			// /从网络取数据

			getNetBusinessData(idtables, conditions);
			// /等待网络完成
			do {

			} while (!netCompleted);

			getLocalBusinessData(idtables, conditions);
			return 2;
		}

		return 0;
	}

	private void getNetBusinessData(String[] dataSource, String[] condition) {
		// /如果网络不通直接返回
		if (isNetworkConnected()) {
			// /先删除数据
			for (int i = 0; i < dataSource.length; i++) {
				BusinessBaseDao.deleteBusinessData(dataSource[i], condition[i]);
			}

			MySSListener ll = new MySSListener();
			Ctlm1345Update ctlm1345 = new Ctlm1345Update(dataSource, condition,
					ll);
			ctlm1345.action();
		} else {
			netCompleted = true;
		}
	}

	private void getLocalBusinessData(String[] dataSource, String[] condition) {

		for (int i = 0; i < dataSource.length; i++) {
			BusinessData busData = new BusinessData();
			busData = BusinessBaseDao.getBusinessData(dataSource[i],
					condition[i]);
			if (busData != null) {
				busData.setId_view(activitySupport.currentViewClass.id);
				busData.setId_table(dataSource[i]);
				activitySupport.ctlm1345List.add(busData);
			}
		}
		return;
	}

	// //界面初始化保存
	public boolean isNetworkConnected() {
		NetworkInfo ni = null;
		try {
			ConnectivityManager cm = (ConnectivityManager) EapApplication
					.getApplication().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			ni = cm.getActiveNetworkInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ni != null && ni.isConnectedOrConnecting();
	}

	private void setsaveData() {
		int count = activitySupport.linearLayout.getChildCount();

		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) activitySupport.linearLayout
					.getChildAt(i);
			view.saveData(false);
		}

		// //保存工具栏
		count = activitySupport.stub.getChildCount();
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) activitySupport.stub
					.getChildAt(i);
			view.saveData(false);
		}
		// //保存菜单
		count = activitySupport.viewMenu.getChildCount();
		for (int i = 0; i < count; i++) {
			HJViewInterface view = (HJViewInterface) activitySupport.viewMenu
					.getChildAt(i);
			view.saveData(false);
		}

	}

	class MySSListener implements OnResultListener {

		@Override
		public void onResult(boolean success) {
			netCompleted = true;
		}

	}

}

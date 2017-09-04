package com.hjnerp.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hjnerp.business.activity.BusinessActivity;
import com.hjnerp.business.activity.BusinessActivityFragment;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.util.StringUtil;
import com.hjnerp.widget.HJViewInterface;

import java.util.List;

public class HjActivityManager {
	private static HjActivityManager hjActivityManager;

	private BusinessActivity nBusinessParam;

	private HjActivityManager() {
	}

	private StartViewInfo startViewInfo;
	private List<ViewClass> listViewClass; // 当前整个功能的界面，<HJModel下面的全部View的集合

	public static HjActivityManager getInstance() {
		if (hjActivityManager == null) {
			hjActivityManager = new HjActivityManager();
		}

		return hjActivityManager;
	}

	public void setStartViewInfo(StartViewInfo nstartViewInfo) {
		this.startViewInfo = nstartViewInfo;
	}

	public StartViewInfo getStartViewInfo() {
		return this.startViewInfo;
	}

	public void setViewClassList(List<ViewClass> nlistViewClass) {
		this.listViewClass = nlistViewClass;
	}

	public List<ViewClass> getViewClassList() {
		return this.listViewClass;
	}

	public void remove(String viewid) {
		if (nBusinessParam == null)
			return;
	}

	public void removeall() {
		// nBusinessParam.clear();
	}

	public BusinessActivity peek() {
		return nBusinessParam;
	}

	// public BusinessActivity peek(String viewid) {
	// if (nBusinessParam == null)
	// return null;
	// BusinessActivity lbusinessParam = null;
	// for (BusinessActivity businessParam : nBusinessParam) {
	// if (viewid.equalsIgnoreCase(businessParam.getViewId())) {
	// lbusinessParam = businessParam;
	// break;
	// }
	// }
	// return lbusinessParam;
	// }

	// public void modify( BusinessActivity businessActivity)
	// {
	// if (nBusinessParam == null)
	// {
	// nBusinessParam = new ArrayList<BusinessActivity>();
	// }
	// String viewid ;
	// int i = 0 ;
	// viewid = businessActivity.getViewId();
	// for(BusinessActivity businessParam : nBusinessParam)
	// {
	// if (viewid.equalsIgnoreCase( businessParam.getViewId()))
	// {
	// nBusinessParam.set(i, businessActivity);
	// }
	// i++;
	// }
	// }
	public void push(BusinessActivity businessActivity) {
		nBusinessParam = businessActivity;
	}

	public void hjv_setbillno(String viewid, String billno) {
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			bfragment.currentBusParam.setBillNo(billno);
		}
	}

	public int hjv_savedata(String viewid) {
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			return bfragment.saveData();

		}

		return 1;
	}

	public String hjv_getbillno(String viewid) {
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			return bfragment.getBillno();

		}
		return "";
	}

	public String hjv_getparentnode(String viewid) {

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			return bfragment.getParentNode();

		}
		return "";
	}

	public void hjc_setbackview(String viewid) {

		List<Fragment> Fragments = nBusinessParam.getSupportFragmentManager()
				.getFragments();

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			
			for (int i = Fragments.size() - 1; i >= 0; i--) {
				Fragment fment = Fragments.get(i);
				if (fment == null ) {
					continue;
				}
				if (!viewid.equalsIgnoreCase(fment.getTag())) { 
					 FragmentTransaction ft2 = nBusinessParam
					 .getSupportFragmentManager().beginTransaction();
					 ft2.remove(fment);
					 ft2.commit(); 
					 fment = null;
					 nBusinessParam.getSupportFragmentManager().popBackStack(); 
				} else {
					break;
				}
			}
			
			bfragment.prepareDate();
			nBusinessParam.isBack = true;
		}
		else
		{
		
		}
	 
		

	
	}

	public void hjc_setfinishview(String viewid) {
		// BusinessActivityFragment bfragment = (BusinessActivityFragment)
		// nBusinessParam
		// .getSupportFragmentManager().findFragmentByTag(viewid);
		// if (bfragment != null)
		// {
		// nBusinessParam
		// .getSupportFragmentManager().
		//
		//
		//
		// bfragment.setcontorl_visible(controlid, visible);
		//
		// }

		// for (int i = nBusinessParam.size() - 1; i >= 0; i--) {
		// if (viewid.equalsIgnoreCase(nBusinessParam.get(i).getViewId())) {
		// BusinessActivity ba = nBusinessParam.get(i);
		// nBusinessParam.remove(i);
		// ba.finish();
		// ba = null;
		// break;
		// }
		// }
	}

	public void setcontorl_visible(String viewid, String controlid,
			String visible) {
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			bfragment.setcontorl_visible(controlid, visible); 
		}
	}

	public void hjc_setvalue(String viewid, String controlid, String values) {
		if (StringUtil.isNullOrEmpty(values))
			values = "";
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			HJViewInterface view = bfragment.getControl(controlid);
			if (view != null) {
				view.setValue(values);
			}

		}
	}

	public void hjc_setvalue(String viewid, String controlid, String row,
			String column, String values) {
		if (StringUtil.isNullOrEmpty(values)) {
			values = "";
		}

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			HJViewInterface view = bfragment.getControl(controlid);
			if (view != null) {
				view.setValue(row, column, values);
			}

		}
	}

	public String hjc_getvalue(String viewid, String controlid) {
		String value = "";

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			HJViewInterface view = bfragment.getControl(controlid);
			if (view != null) {
				value = view.getValue();
				if (StringUtil.isNullOrEmpty(value)) {
					value = "";
				}
				return value;

			}

		}

		return value;
	}

	public String hjc_getvalue(String viewid, String controlid, String row,
			String column) {
		Log.d("hjc_getvalue2","go-------------");
		String value = "";
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			HJViewInterface view = bfragment.getControl(controlid);
			if (view != null) {
				value = view.getValue(row, column);
				if (StringUtil.isNullOrEmpty(value)) {
					value = "";
				}
				return value;

			}

		}
		return value;
	}

	public void dts_additem(String viewid, String dataset, String condition) {

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			bfragment.dataset_additem(dataset, condition);
		}
	}

	public void dts_setcondition(String viewid, String dataset, String condition) {

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			bfragment.dataset_setcondition(dataset, condition);
		}

	}

	public void hjds_search(String viewid, String datasource, String condition) {

		// BusinessActivityFragment bfragment = (BusinessActivityFragment)
		// nBusinessParam
		// .getSupportFragmentManager().findFragmentByTag(viewid);
		// if (bfragment != null) {
		// bfragment.datastorce_search( viewid, datasource, condition);
		// }

		nBusinessParam.datastorce_search(viewid, datasource, condition);
	}

	public void hjc_setnextview(String viewid, String controlid, String row,
			String viewid2, String billno, String nodeid, String values) {
		nBusinessParam.setNextView(viewid2, billno, nodeid, values);
	}

	public void hjc_controluplod(String viewid, String controlid) {

		// nBusinessParam.startDataUplod(viewid, controlid);
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			bfragment.startDataUplod(controlid);
		}

	}

	public void hjc_datarefresh(String viewid, String controlid,
			String datasource) {

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		Log.d("set_datarefresh", viewid+" "+controlid+" "+datasource);
		if (bfragment != null) {
			bfragment.set_datarefresh(controlid, datasource);
		}
	}

	public void hjc_browsephoto(String viewid, String billno, String nodeid) {
		nBusinessParam.startShowPictures(billno, nodeid);
	}

	public void jhjc_setvalue(String viewid, String controlid, String billno,
			String nodeid, String value) {
		if (StringUtil.isNullOrEmpty(value)) {
			value = "";
		}
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			HJViewInterface view = bfragment.getControl(controlid);
			if (view != null) {
				view.addItem(billno, nodeid, value);
			}
		}
	}

	public String hjc_Location(String viewid, String controlid) {

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			return bfragment.setLocation(controlid);
		}

		return "";
	}

	public void hjc_setphoto(String viewid, String controlid) {

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);
		if (bfragment != null) {
			bfragment.setPhoto(controlid);
		}

	}

	public void hjb_setddiscard(String viewid, String locationid,
			String photoid, String nextview, String billno, String nodeid) {

		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);

		String locvalue  ;
		String imagervalue  ;
		//取下等位值
		
		
		if (bfragment != null) {
				locvalue = 	hjc_getvalue (viewid, locationid) ;
				if (StringUtil.isNullOrEmpty(locvalue)) {
					locvalue = "";
				} 
				else
				{
					nBusinessParam.setNextView(nextview, billno, nodeid, "");
					return ;
				} 
				imagervalue = 	hjc_getvalue (viewid, photoid) ;
				if (StringUtil.isNullOrEmpty(imagervalue)) {
					imagervalue = "";
				} 
				else
				{
					nBusinessParam.setNextView(nextview, billno, nodeid, "");
					return ;
				} 
		
			
			String location = bfragment.setLocation(locationid);
			if (StringUtil.isNullOrEmpty(location)) {

				bfragment.setb_Ddiscard(photoid, nextview, billno, nodeid);

			} else {
				nBusinessParam.setNextView(nextview, billno, nodeid, "");
			}
		}
	}

	public String hjc_getrowcount(String viewid, String controlid) {
		Log.d("hjc_getrowcount","go-------------");
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);

		if (bfragment != null) {
			HJViewInterface view = bfragment.getControl(controlid);
			if (view != null) {
				return view.getRowCount();
			}
		}

		return "0";
	}

	public void hjc_setmakeText(String viewid, String msg) {
		nBusinessParam.setmakeText(msg);
	}

	public void hju_sendsms(String viewid, String phone, String msg) {
		nBusinessParam.sendsms(phone, msg);
	}

	public void hjc_setenabled(String viewid, String controlid, String enabled) {
		// TODO Auto-generated method stub
		BusinessActivityFragment bfragment = (BusinessActivityFragment) nBusinessParam
				.getSupportFragmentManager().findFragmentByTag(viewid);

		if (bfragment != null) {
//			HJViewInterface view = bfragment.getControl(controlid);
//			if (view != null) {
//			   if ("0".equalsIgnoreCase(enabled)  )
//			   {
//				  
//			   }	 
//			}
			bfragment.setcontorl_enabled(  controlid , enabled);
		}
	}
	
	public String hju_getuuid()
	{
		return StringUtil.getMyUUID();
	}
}

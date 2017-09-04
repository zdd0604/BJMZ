package com.hjnerp.dao;

import android.content.Context;
import android.util.Log;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.UserInfo;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.StringUtil;

public class BusinessDataSaveHelper {
	private static String TAG = "BusinessDataSaveHelper";
	private static UserInfo userInfo = QiXinBaseDao.queryCurrentUserInfo();
	
	public static void createOneBusiness(Context context,StartViewInfo startViewInfo){
		//组织ctlm1347
		Ctlm1347 ctlm1347 = new Ctlm1347();
		ctlm1347.setId_recorder("userInfo.userID");
		ctlm1347.setId_com("userInfo.companyID");
		ctlm1347.setId_node(StringUtil.getMyUUID());  //自己生成id_node,当前时间+随机数
		ctlm1347.setId_model(startViewInfo.id);
		ctlm1347.setDate_opr(DateUtil.getCurrentTime());
		
		for(int i = 0;i<10;i++){
			Log.e(TAG,StringUtil.getMyUUID());
		}

		BusinessBaseDao.replaceCTLM1347(ctlm1347);
	}
	
	 
	
	
	
	public static void saveCTLM1347(WidgetClass items,String[] billNoArray,StartViewInfo startViewInfo,String layoutType,String uploadFlag) {
		
		for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {

//			Map<String, String> mmap = dataList.get(i);
			Ctlm1347 ctlm1347 = new Ctlm1347();

			ctlm1347.setId_recorder("userInfo.userID");
			ctlm1347.setId_com("userInfo.companyID");
			ctlm1347.setId_node(billNoArray[i]); // 自己生成id_node,当前时间+随机数
			ctlm1347.setName_node(items.name);
		//	ctlm1347.setVar_billno(BusinessActivity.BillNo);
			ctlm1347.setId_model(startViewInfo.id);
			ctlm1347.setId_nodetype(layoutType);
			
//			ctlm1347.setFlag_upload("N");
			ctlm1347.setFlag_upload(uploadFlag);
			
			
//			if(BusinessActivity.billInfoListMap.size() > 0){
//				HashMap<String, String> fatherMap = BusinessActivity.billInfoListMap.get(BusinessActivity.currentPageNumber - 2);
//				String parentnode = fatherMap.get("id_node");
//				ctlm1347.setId_parentnode(parentnode);
//
//			}else{
//				ctlm1347.setId_parentnode(null);
//			}
//
////			for (int j = 0; j < items.HJRadioButtonOption.size(); j++) {
////				WidgetClass widget = items.HJRadioButtonOption.get(j);
////				ctlm1347.setVar_data(widget.attribute.dbfield, mmap.get(widget.attribute.field));
////			}
//
//			ctlm1347.setId_srcnode(items.HJRadioButtonOption.get(i).id);
//			
//			ctlm1347.setDate_opr(DateUtil.getCurrentTime());
//
//			BusinessBaseDao.replaceCTLM1347(ctlm1347);
		}

	}
	

}

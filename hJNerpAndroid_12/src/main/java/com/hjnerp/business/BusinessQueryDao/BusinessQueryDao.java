package com.hjnerp.business.BusinessQueryDao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.Ctlm7161;
import com.hjnerp.model.DdisplocatBean;
import com.hjnerp.model.Ej1345;
import com.hjnerp.model.EjMyWProj1345;
import com.hjnerp.model.EjWType1345;
import com.hjnerp.model.EjWadd1345;
import com.hjnerp.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zdd on 2017/1/16.
 * 只要用于工作流查询数据库
 */

public class BusinessQueryDao {

    /**
     * @param context
     * @return 获取基础数据
     */
    public static boolean getUserInfo(Context context) {
        Constant.ctlm1345List = BusinessBaseDao.getCTLM1345ByIdTable("user");
        if (Constant.ctlm1345List.size() > 0) {
            for (int i = 0; i < Constant.ctlm1345List.size(); i++) {
                String json = Constant.ctlm1345List.get(i).getVar_value();
                Constant.ej1345 = new Gson().fromJson(json, new TypeToken<Ej1345>() {
                }.getType());
            }
            Constant.MYUSERINFO = QiXinBaseDao.queryCurrentUserInfo();
            return true;
        } else {
            ToastUtil.ShowLong(context, "请先下载基础数据");
            return false;
        }
    }


    /**
     * @param context
     * @return 获取工作地点
     */
    public static List<EjWadd1345> getMyWadd(Context context) {
        List<EjWadd1345> list = new ArrayList<>();
        Constant.ctlm1345List = BusinessBaseDao.getCTLM1345ByIdTable("mywadd");
        if (Constant.ctlm1345List.size() > 0) {
            for (int i = 0; i < Constant.ctlm1345List.size(); i++) {
                String json = Constant.ctlm1345List.get(i).getVar_value();
                EjWadd1345 ejWadd1345 = new Gson().fromJson(json, new TypeToken<EjWadd1345>() {
                }.getType());
                list.add(ejWadd1345);
            }
            return list;
        } else {
            return list;
        }
    }

    /**
     * @param context
     * @return 获取工作类型
     */
    public static List<EjWType1345> getMyWType(Context context) {
        List<EjWType1345> list = new ArrayList<>();
        Constant.ctlm1345List = BusinessBaseDao.getCTLM1345ByIdTable("mywtype");
        if (Constant.ctlm1345List.size() > 0) {
            for (int i = 0; i < Constant.ctlm1345List.size(); i++) {
                String json = Constant.ctlm1345List.get(i).getVar_value();
                EjWType1345 ejWType1345 = new Gson().fromJson(json, new TypeToken<EjWType1345>() {
                }.getType());
                list.add(ejWType1345);
            }
            return list;
        } else {
            return list;
        }
    }

    /**
     * @return 获取工作项目
     */
    public static List<EjMyWProj1345> getMyProj(String content, String id_column, String id_recorder) {
        List<EjMyWProj1345> list = new ArrayList<>();
        Constant.ctlm1345List = BusinessBaseDao.getCTLM1345NameColumn("mywproj", id_column, content, id_recorder);
        if (Constant.ctlm1345List.size() > 0) {
            for (int i = 0; i < Constant.ctlm1345List.size(); i++) {
                String json = Constant.ctlm1345List.get(i).getVar_value();
                EjMyWProj1345 ejMyWProj1345 = new Gson().fromJson(json, new TypeToken<EjMyWProj1345>() {
                }.getType());
                list.add(ejMyWProj1345);
            }
            return list;
        } else {
            return list;
        }
    }

    /**
     * @return 后台设定的地理位置信息
     */
    public static void getDdisplocat_location(String idTable) {
        Constant.ctlm1345List = BusinessBaseDao.getCTLM1345ByIdTable(idTable);
        if (Constant.ctlm1345List.size() > 0) {
            for (int i = 0; i < Constant.ctlm1345List.size(); i++) {
                String json = Constant.ctlm1345List.get(i).getVar_value();
                Constant.mDdisplocatBean = new Gson().fromJson(json, new TypeToken<DdisplocatBean>() {
                }.getType());
                Log.v("show",Constant.mDdisplocatBean.toString());
            }
        }
    }

    /**
     * @return 后台设定的地理位置信息
     */
    public static void getSgin_Section(String idTable) {
        Constant.ctlm1345List = BusinessBaseDao.getCTLM1345ByIdTable(idTable);
        if (Constant.ctlm1345List.size() > 0) {
            for (int i = 0; i < Constant.ctlm1345List.size(); i++) {
                String json = Constant.ctlm1345List.get(i).getVar_value();
                Constant.ctlm7161 = new Gson().fromJson(json, new TypeToken<Ctlm7161>() {
                }.getType());
            }
            Constant.ctlm7161Is = true;
        }else{
            Constant.ctlm7161Is = false;
        }
    }
}

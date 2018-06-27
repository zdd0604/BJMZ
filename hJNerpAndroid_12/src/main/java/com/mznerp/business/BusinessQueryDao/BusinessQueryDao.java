package com.mznerp.business.BusinessQueryDao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mznerp.common.Constant;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.Ctlm1345;
import com.mznerp.model.Ctlm7161;
import com.mznerp.model.DdisplocatBean;
import com.mznerp.model.Ej1345;
import com.mznerp.model.EjMyWProj1345;
import com.mznerp.model.EjWType1345;
import com.mznerp.model.EjWadd1345;
import com.mznerp.model.ProductDetail;
import com.mznerp.util.ToastUtil;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zdd on 2017/1/16.
 * 只要用于工作流查询数据库
 */

public class BusinessQueryDao {

    public static final Gson gson =new Gson();

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
                Log.v("show", Constant.mDdisplocatBean.toString());
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
        } else {
            Constant.ctlm7161Is = false;
        }
    }

    /**
     * 获取1345表中的客户信息列表
     *
     * @param idTable
     */
    public static List<ProductDetail> get1345Corr(String idTable) {

        List<ProductDetail> list = new ArrayList<>();
        Constant.ctlm1345List = BusinessBaseDao.getCTLM1345ByIdTable(idTable);

        int allSize = Constant.ctlm1345List.size();
        int size = (int) (Math.random()*4+4);
        Log.e("mz",""+size);
        ExecutorService pool = Executors.newFixedThreadPool(size);
        List<Future<List<ProductDetail>>> futures = new ArrayList();
        int avg = allSize/size;
        for (int i = 0; i < size; i++) {
            int start = i*avg;
            int end = (i==size-1)?allSize:(i+1)*avg;
            Callable<List<ProductDetail>> callable  = new MyCallable(start,end);
            futures.add(pool.submit(callable));
        }
        boolean flag = true;
        while (flag){
            flag = false ;
            for (Future future : futures) {
                if(!future.isDone()){
                    flag = true ;
                }
            }
        }
        for (Future future : futures) {
            try {
                list.addAll((List<ProductDetail>) future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
//        if (Constant.ctlm1345List.size() > 0)
//            for (int i = 0; i < Constant.ctlm1345List.size(); i++) {
//                String json = Constant.ctlm1345List.get(i).getVar_value();
//                ProductDetail productDetail = gson.fromJson(json, ProductDetail.class);
//                list.add(productDetail);
//            }
        return list;
    }


    static class MyCallable implements Callable<List<ProductDetail>> {
        private int start;
        private int end;

        public MyCallable(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public List<ProductDetail> call() throws Exception {
            List list = new ArrayList<ProductDetail>();
            for (int i = start; i < end; i++) {
                String json = Constant.ctlm1345List.get(i).getVar_value();
                try {
                    list.add(gson.fromJson(json, ProductDetail.class));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return list;
        }
    }

}

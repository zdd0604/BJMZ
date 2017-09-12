package com.hjnerp.business.activity;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by zy on 2017/6/13.
 */

public class DsaordQueryJsonD implements Comparable<DsaordQueryJsonD> {


    /**
     * id_user : 100003
     * name_user : 何厚勇
     * id_dept : BM07
     * name_dept : 北京美正-销售部
     * date_task : 2017-07-19
     * id_wtype : 030
     * name_type : 新客户
     * name_wproj : 宁夏回族自治区粮食局
     * var_wtitle : 沟通四川之行
     * var_remark : 去四川
     */

    private String id_user;
    private String name_user;
    private String id_dept;
    private String name_dept;
    private String date_task;
    private String id_wtype;
    private String name_type;
    private String name_wproj;
    private String var_wtitle;
    private String var_remark;

    @Override
    public int compareTo(DsaordQueryJsonD dsaordbaseJson) {
        int compare = 0;
       if (!TextUtils.isEmpty(dsaordbaseJson.date_task)) {
            try {
                long a = new SimpleDateFormat("yyyy.MM.dd").parse(dsaordbaseJson.date_task).getTime() - new SimpleDateFormat("yyyy.MM.dd").parse(this.date_task).getTime();
                if (a > 0) {
                    compare = 1;
                } else if (a == 0) {
                    compare = 0;
                } else {
                    compare = -1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return compare;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getId_dept() {
        return id_dept;
    }

    public void setId_dept(String id_dept) {
        this.id_dept = id_dept;
    }

    public String getName_dept() {
        return name_dept;
    }

    public void setName_dept(String name_dept) {
        this.name_dept = name_dept;
    }

    public String getDate_task() {
        return date_task;
    }

    public void setDate_task(String date_task) {
        this.date_task = date_task;
    }

    public String getId_wtype() {
        return id_wtype;
    }

    public void setId_wtype(String id_wtype) {
        this.id_wtype = id_wtype;
    }

    public String getName_type() {
        return name_type;
    }

    public void setName_type(String name_type) {
        this.name_type = name_type;
    }

    public String getName_wproj() {
        return name_wproj;
    }

    public void setName_wproj(String name_wproj) {
        this.name_wproj = name_wproj;
    }

    public String getVar_wtitle() {
        return var_wtitle;
    }

    public void setVar_wtitle(String var_wtitle) {
        this.var_wtitle = var_wtitle;
    }

    public String getVar_remark() {
        return var_remark;
    }

    public void setVar_remark(String var_remark) {
        this.var_remark = var_remark;
    }
}

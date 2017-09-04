package com.hjnerp.model;

import java.util.List;

/**
 * Created by Admin on 2016/12/14.
 */

public class BusinessPerformanceInfo {

    private String tableid;
    private String opr;
    private String no;
    private String userid;
    private String comid;
    private List<BusinessPerformanceData> data;

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getOpr() {
        return opr;
    }

    public void setOpr(String opr) {
        this.opr = opr;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public List<BusinessPerformanceData> getData() {
        return data;
    }

    public void setData(List<BusinessPerformanceData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BusinessPerformanceInfo{" +
                "tableid='" + tableid + '\'' +
                ", opr='" + opr + '\'' +
                ", no='" + no + '\'' +
                ", userid='" + userid + '\'' +
                ", comid='" + comid + '\'' +
                ", data=" + data +
                '}';
    }
}

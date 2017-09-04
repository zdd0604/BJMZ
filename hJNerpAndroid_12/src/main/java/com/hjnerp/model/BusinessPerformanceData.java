package com.hjnerp.model;

import java.util.List;

/**
 * Created by Admin on 2016/12/14.
 */

public class BusinessPerformanceData {
    private String table;
    private String detail;
    private String where;
    private List<BusinessPerformanceColumn> data;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public List<BusinessPerformanceColumn> getData() {
        return data;
    }

    public void setData(List<BusinessPerformanceColumn> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BusinessPerformanceData{" +
                "table='" + table + '\'' +
                ", detail='" + detail + '\'' +
                ", where='" + where + '\'' +
                ", data=" + data +
                '}';
    }
}

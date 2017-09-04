package com.hjnerp.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/12/27.
 */

public class businessFlag implements Serializable {

    private static final long serialVersionUID = 6349078604702824627L;
    /**
     * flag : Y
     * message : 处理成功！
     * no : KPI201611000509
     */

    private String flag;
    private String message;
    private String no;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "businessFlag{" +
                "flag='" + flag + '\'' +
                ", message='" + message + '\'' +
                ", no='" + no + '\'' +
                '}';
    }
}

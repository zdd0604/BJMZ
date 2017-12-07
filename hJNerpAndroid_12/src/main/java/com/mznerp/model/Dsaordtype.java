package com.mznerp.model;

/**
 * Created by zy on 2017/6/13.
 */

public class Dsaordtype {

    /**
     * id_user : 100001
     * name_user : 柳家鹏
     * id_ordtype : H
     * name_ordtype : 合同订单
     */

    private String id_user;
    private String id_authuser;
    private String name_user;
    private String id_ordtype;
    private String name_ordtype;
    private String id_invtype;
    private String name_invtype;
    private String id_satype;
    private String name_type;
    private String id_express;
    private String name_express;

    public String getId_express() {
        return id_express;
    }

    public void setId_express(String id_express) {
        this.id_express = id_express;
    }

    public String getName_express() {
        return name_express;
    }

    public void setName_express(String name_express) {
        this.name_express = name_express;
    }

    public String getId_authuser() {
        return id_authuser;
    }

    public void setId_authuser(String id_authuser) {
        this.id_authuser = id_authuser;
    }

    public String getId_invtype() {
        return id_invtype;
    }

    public void setId_invtype(String id_invtype) {
        this.id_invtype = id_invtype;
    }

    public String getName_invtype() {
        return name_invtype;
    }

    public void setName_invtype(String name_invtype) {
        this.name_invtype = name_invtype;
    }

    public String getId_satype() {
        return id_satype;
    }

    public void setId_satype(String id_satype) {
        this.id_satype = id_satype;
    }

    public String getName_type() {
        return name_type;
    }

    public void setName_type(String name_type) {
        this.name_type = name_type;
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

    public String getId_ordtype() {
        return id_ordtype;
    }

    public void setId_ordtype(String id_ordtype) {
        this.id_ordtype = id_ordtype;
    }

    public String getName_ordtype() {
        return name_ordtype;
    }

    public void setName_ordtype(String name_ordtype) {
        this.name_ordtype = name_ordtype;
    }
}

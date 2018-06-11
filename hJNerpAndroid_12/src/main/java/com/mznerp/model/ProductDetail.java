package com.mznerp.model;

import java.io.Serializable;


/**
 * 本地客户列表
 */
public class ProductDetail implements Serializable {

    private static final long serialVersionUID = 6178628626842502606L;


    /**
     * id_corr : 2000028
     * name_corr : 安阳市汇丰源茂粮业有限责任公司
     * var_addr :
     * var_tel :
     * var_contact :
     * id_seller : 100047
     * name_seller : 王显
     */

    private String id_corr;
    private String name_corr;
    private String var_addr;
    private String var_tel;
    private String var_contact;
    private String id_seller;
    private String name_seller;

    public String getId_corr() {
        return id_corr;
    }

    public void setId_corr(String id_corr) {
        this.id_corr = id_corr;
    }

    public String getName_corr() {
        return name_corr;
    }

    public void setName_corr(String name_corr) {
        this.name_corr = name_corr;
    }

    public String getVar_addr() {
        return var_addr;
    }

    public void setVar_addr(String var_addr) {
        this.var_addr = var_addr;
    }

    public String getVar_tel() {
        return var_tel;
    }

    public void setVar_tel(String var_tel) {
        this.var_tel = var_tel;
    }

    public String getVar_contact() {
        return var_contact;
    }

    public void setVar_contact(String var_contact) {
        this.var_contact = var_contact;
    }

    public String getId_seller() {
        return id_seller;
    }

    public void setId_seller(String id_seller) {
        this.id_seller = id_seller;
    }

    public String getName_seller() {
        return name_seller;
    }

    public void setName_seller(String name_seller) {
        this.name_seller = name_seller;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "id_corr='" + id_corr + '\'' +
                ", name_corr='" + name_corr + '\'' +
                ", var_addr='" + var_addr + '\'' +
                ", var_tel='" + var_tel + '\'' +
                ", var_contact='" + var_contact + '\'' +
                ", id_seller='" + id_seller + '\'' +
                ", name_seller='" + name_seller + '\'' +
                '}';
    }
}

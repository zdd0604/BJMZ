package com.hjnerp.business.activity;

/**
 * Created by zy on 2017/6/13.
 */

public class DsaordbaseJson3 {

    /**
     * id_corr : 2000595
     * name_corr : 黑龙江美正生物科技有限公司
     * id_item : JC0033
     * var_desc : 黄曲霉毒素M1快速检测试纸条
     * var_spec : 96条/盒
     * var_pattern : 8T
     * var_chkparm : 【0.1μg/kg】
     * id_uom : 111
     * name_uom : 条
     * dec_price : 40.00000000000
     * id_tax : 03
     * name_tax : 3增值税
     * dec_taxrate : 0.0300000
     */

    private String id_corr;
    private String name_corr;
    private String id_item;
    private String var_desc;
    private String var_spec;
    private String var_pattern;
    private String var_chkparm;
    private String id_uom;
    private String name_uom;
    private String dec_price;
    private String id_tax;
    private String name_tax;
    private String dec_taxrate;
    private String id_stocktype;
    private String id_itemcate;
    private String id_stockstyle;
    private String dec_oriprice;

    public String getDec_oriprice() {
        return dec_oriprice;
    }

    public void setDec_oriprice(String dec_oriprice) {
        this.dec_oriprice = dec_oriprice;
    }

    public String getId_stocktype() {
        return id_stocktype;
    }

    public void setId_stocktype(String id_stocktype) {
        this.id_stocktype = id_stocktype;
    }

    public String getId_itemcate() {
        return id_itemcate;
    }

    public void setId_itemcate(String id_itemcate) {
        this.id_itemcate = id_itemcate;
    }

    public String getId_stockstyle() {
        return id_stockstyle;
    }

    public void setId_stockstyle(String id_stockstyle) {
        this.id_stockstyle = id_stockstyle;
    }

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

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getVar_desc() {
        return var_desc;
    }

    public void setVar_desc(String var_desc) {
        this.var_desc = var_desc;
    }

    public String getVar_spec() {
        return var_spec;
    }

    public void setVar_spec(String var_spec) {
        this.var_spec = var_spec;
    }

    public String getVar_pattern() {
        return var_pattern;
    }

    public void setVar_pattern(String var_pattern) {
        this.var_pattern = var_pattern;
    }

    public String getVar_chkparm() {
        return var_chkparm;
    }

    public void setVar_chkparm(String var_chkparm) {
        this.var_chkparm = var_chkparm;
    }

    public String getId_uom() {
        return id_uom;
    }

    public void setId_uom(String id_uom) {
        this.id_uom = id_uom;
    }

    public String getName_uom() {
        return name_uom;
    }

    public void setName_uom(String name_uom) {
        this.name_uom = name_uom;
    }

    public String getDec_price() {
        return dec_price;
    }

    public void setDec_price(String dec_price) {
        this.dec_price = dec_price;
    }

    public String getId_tax() {
        return id_tax;
    }

    public void setId_tax(String id_tax) {
        this.id_tax = id_tax;
    }

    public String getName_tax() {
        return name_tax;
    }

    public void setName_tax(String name_tax) {
        this.name_tax = name_tax;
    }

    public String getDec_taxrate() {
        return dec_taxrate;
    }

    public void setDec_taxrate(String dec_taxrate) {
        this.dec_taxrate = dec_taxrate;
    }

    @Override
    public String toString() {
        return "DsaordbaseJson3{" +
                "id_corr='" + id_corr + '\'' +
                ", name_corr='" + name_corr + '\'' +
                ", id_item='" + id_item + '\'' +
                ", var_desc='" + var_desc + '\'' +
                ", var_spec='" + var_spec + '\'' +
                ", var_pattern='" + var_pattern + '\'' +
                ", var_chkparm='" + var_chkparm + '\'' +
                ", id_uom='" + id_uom + '\'' +
                ", name_uom='" + name_uom + '\'' +
                ", dec_price='" + dec_price + '\'' +
                ", id_tax='" + id_tax + '\'' +
                ", name_tax='" + name_tax + '\'' +
                ", dec_taxrate='" + dec_taxrate + '\'' +
                ", id_stocktype='" + id_stocktype + '\'' +
                ", id_itemcate='" + id_itemcate + '\'' +
                ", id_stockstyle='" + id_stockstyle + '\'' +
                ", dec_oriprice='" + dec_oriprice + '\'' +
                '}';
    }
}

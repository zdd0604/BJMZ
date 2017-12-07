package com.mznerp.model;

/**
 * Created by zy on 2017/6/12.
 */

public class SellOrderModel {
    private int order_no;//明细号
    private boolean order_first;//是否是第一条明细
    private boolean order_delete;//是否已经删除了明细
    private int order_type;//明细的销售类型选择位置
    private int order_product;//明细的产品类型选择位置
    private int order_add;
    private double order_num;//数量
    private int order_tax;//税
    private double order_price;//总价
    private double per_price;//每个产品的价格
    private String id_item;//产品id
    private String name_item;//产品名称
    private String id_sell;//销售类型id
    private String id_uom;//单位id
    private String id_tax;//增值税id
    private String var_chkparm;//性能参数
    private String dec_taxrate;//税率
    private String flag_gift;
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

    public String getFlag_gift() {
        return flag_gift;
    }

    public void setFlag_gift(String flag_gift) {
        this.flag_gift = flag_gift;
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

    public String getDec_taxrate() {
        return dec_taxrate;
    }

    public void setDec_taxrate(String dec_taxrate) {
        this.dec_taxrate = dec_taxrate;
    }

    public String getVar_chkparm() {
        return var_chkparm;
    }

    public void setVar_chkparm(String var_chkparm) {
        this.var_chkparm = var_chkparm;
    }

    public String getId_tax() {
        return id_tax;
    }

    public void setId_tax(String id_tax) {
        this.id_tax = id_tax;
    }

    public String getName_item() {
        return name_item;
    }

    public void setName_item(String name_item) {
        this.name_item = name_item;
    }

    public String getId_uom() {
        return id_uom;
    }

    public void setId_uom(String id_uom) {
        this.id_uom = id_uom;
    }

    public double getPer_price() {
        return per_price;
    }

    public void setPer_price(double per_price) {
        this.per_price = per_price;
    }

    public String getId_sell() {
        return id_sell;
    }

    public void setId_sell(String id_sell) {
        this.id_sell = id_sell;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public boolean isOrder_first() {
        return order_first;
    }

    public void setOrder_first(boolean order_first) {
        this.order_first = order_first;
    }

    public boolean isOrder_delete() {
        return order_delete;
    }

    public void setOrder_delete(boolean order_delete) {
        this.order_delete = order_delete;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getOrder_product() {
        return order_product;
    }

    public void setOrder_product(int order_product) {
        this.order_product = order_product;
    }

    public int getOrder_add() {
        return order_add;
    }

    public void setOrder_add(int order_add) {
        this.order_add = order_add;
    }

    public double getOrder_num() {
        return order_num;
    }

    public void setOrder_num(double order_num) {
        this.order_num = order_num;
    }

    public int getOrder_tax() {
        return order_tax;
    }

    public void setOrder_tax(int order_tax) {
        this.order_tax = order_tax;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    @Override
    public String toString() {
        return "SellOrderModel{" +
                "order_no=" + order_no +
                ", order_first=" + order_first +
                ", order_delete=" + order_delete +
                ", order_type=" + order_type +
                ", order_product=" + order_product +
                ", order_add=" + order_add +
                ", order_num=" + order_num +
                ", order_tax=" + order_tax +
                ", order_price=" + order_price +
                ", per_price=" + per_price +
                ", id_item='" + id_item + '\'' +
                ", name_item='" + name_item + '\'' +
                ", id_sell='" + id_sell + '\'' +
                ", id_uom='" + id_uom + '\'' +
                ", id_tax='" + id_tax + '\'' +
                ", var_chkparm='" + var_chkparm + '\'' +
                ", dec_taxrate='" + dec_taxrate + '\'' +
                ", flag_gift='" + flag_gift + '\'' +
                ", id_stocktype='" + id_stocktype + '\'' +
                ", id_itemcate='" + id_itemcate + '\'' +
                ", id_stockstyle='" + id_stockstyle + '\'' +
                ", dec_oriprice='" + dec_oriprice + '\'' +
                '}';
    }
}

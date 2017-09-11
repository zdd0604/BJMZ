package com.hjnerp.model;

import java.io.Serializable;

public class PerformanceBean implements Serializable {
    private static final long serialVersionUID = -3241825882337860989L;

    @Override
    public String toString() {
        return "PerformanceBean{" +
                "dec_import='" + dec_import + '\'' +
                ", dec_stdmark='" + dec_stdmark + '\'' +
                ", flag_psts='" + flag_psts + '\'' +
                ", id_table='" + id_table + '\'' +
                ", ori_line_no='" + ori_line_no + '\'' +
                ", ori_no='" + ori_no + '\'' +
                ", line_no='" + line_no + '\'' +
                ", id_kpicate='" + id_kpicate + '\'' +
                ", name_kpicate='" + name_kpicate + '\'' +
                ", id_kpipost='" + id_kpipost + '\'' +
                ", dec_scale='" + dec_scale + '\'' +
                ", var_evastd='" + var_evastd + '\'' +
                ", var_comp='" + var_comp + '\'' +
                ", dec_mark='" + dec_mark + '\'' +
                ", var_dremark='" + var_dremark + '\'' +
                ", var_rejust='" + var_rejust + '\'' +
                ", var_start='" + var_start + '\'' +
                ", var_end='" + var_end + '\'' +
                ", var_time='" + var_time + '\'' +
                ", id_fee='" + id_fee + '\'' +
                ", name_fee='" + name_fee + '\'' +
                ", dec_amt='" + dec_amt + '\'' +
                ", var_remark='" + var_remark + '\'' +
                '}';
    }

    /**
     * dec_import : 0
     * dec_stdmark : 0
     * flag_psts :
     * id_table :
     * ori_line_no : 0
     * ori_no :
     * line_no : 1
     * id_kpicate : 002
     * name_kpicate : 工作目标(GS)
     * id_kpipost : 看看咯
     * dec_scale : 0.8
     * var_evastd : 兔兔
     * var_comp : 呜呜呜呜
     * dec_mark : 0
     * var_dremark :
     */

    private String dec_import;
    private String dec_stdmark;
    private String flag_psts;
    private String id_table;
    private String ori_line_no;
    private String ori_no;
    private String line_no;
    private String id_kpicate;
    private String name_kpicate;
    private String id_kpipost;
    private String dec_scale;
    private String var_evastd;
    private String var_comp;
    private String dec_mark;
    private String var_dremark;
    //出差外出单新增
    private String var_rejust;
    private String var_start;
    private String var_end;
    private String var_time;
    private String id_fee;
    private String name_fee;
    private String dec_amt;
    private String var_remark;
    //考勤异常新增
    private String date_chinkin;
    private String id_linem;
    private String id_schtype;
    private String var_date;
    private String var_on;
    private String var_off;
    private String name_schtype;
    //销售订单录入
    private String id_satype;
    private String id_item;
    private String name_item;
    private String dec_qty;
    private String dec_oriamt;
    private String dec_price;
    private String id_tax;
    private String dec_taxrate;

    public String getDec_taxrate() {
        return dec_taxrate;
    }

    public void setDec_taxrate(String dec_taxrate) {
        this.dec_taxrate = dec_taxrate;
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

    public String getDec_price() {
        return dec_price;
    }

    public void setDec_price(String dec_price) {
        this.dec_price = dec_price;
    }

    public String getDec_oriamt() {
        return dec_oriamt;
    }

    public void setDec_oriamt(String dec_oriamt) {
        this.dec_oriamt = dec_oriamt;
    }

    public String getId_satype() {
        return id_satype;
    }

    public void setId_satype(String id_satype) {
        this.id_satype = id_satype;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getDec_qty() {
        return dec_qty;
    }

    public void setDec_qty(String dec_qty) {
        this.dec_qty = dec_qty;
    }

    public String getDate_chinkin() {
        return date_chinkin;
    }

    public void setDate_chinkin(String date_chinkin) {
        this.date_chinkin = date_chinkin;
    }

    public String getId_linem() {
        return id_linem;
    }

    public void setId_linem(String id_linem) {
        this.id_linem = id_linem;
    }

    public String getId_schtype() {
        return id_schtype;
    }

    public void setId_schtype(String id_schtype) {
        this.id_schtype = id_schtype;
    }

    public String getVar_date() {
        return var_date;
    }

    public void setVar_date(String var_date) {
        this.var_date = var_date;
    }

    public String getVar_on() {
        return var_on;
    }

    public void setVar_on(String var_on) {
        this.var_on = var_on;
    }

    public String getVar_off() {
        return var_off;
    }

    public void setVar_off(String var_off) {
        this.var_off = var_off;
    }

    public String getName_schtype() {
        return name_schtype;
    }

    public void setName_schtype(String name_schtype) {
        this.name_schtype = name_schtype;
    }

    public String getDate_today() {
        return date_today;
    }

    public void setDate_today(String date_today) {
        this.date_today = date_today;
    }

    private String date_today;


    public String getVar_rejust() {
        return var_rejust;
    }

    public void setVar_rejust(String var_rejust) {
        this.var_rejust = var_rejust;
    }

    public String getVar_start() {
        return var_start;
    }

    public void setVar_start(String var_start) {
        this.var_start = var_start;
    }

    public String getVar_end() {
        return var_end;
    }

    public void setVar_end(String var_end) {
        this.var_end = var_end;
    }

    public String getVar_time() {
        return var_time;
    }

    public void setVar_time(String var_time) {
        this.var_time = var_time;
    }

    public String getId_fee() {
        return id_fee;
    }

    public void setId_fee(String id_fee) {
        this.id_fee = id_fee;
    }

    public String getName_fee() {
        return name_fee;
    }

    public void setName_fee(String name_fee) {
        this.name_fee = name_fee;
    }

    public String getDec_amt() {
        return dec_amt;
    }

    public void setDec_amt(String dec_amt) {
        this.dec_amt = dec_amt;
    }

    public String getVar_remark() {
        return var_remark;
    }

    public void setVar_remark(String var_remark) {
        this.var_remark = var_remark;
    }


    public String getDec_import() {
        return dec_import;
    }

    public void setDec_import(String dec_import) {
        this.dec_import = dec_import;
    }

    public String getDec_stdmark() {
        return dec_stdmark;
    }

    public void setDec_stdmark(String dec_stdmark) {
        this.dec_stdmark = dec_stdmark;
    }

    public String getFlag_psts() {
        return flag_psts;
    }

    public void setFlag_psts(String flag_psts) {
        this.flag_psts = flag_psts;
    }

    public String getId_table() {
        return id_table;
    }

    public void setId_table(String id_table) {
        this.id_table = id_table;
    }

    public String getOri_line_no() {
        return ori_line_no;
    }

    public void setOri_line_no(String ori_line_no) {
        this.ori_line_no = ori_line_no;
    }

    public String getOri_no() {
        return ori_no;
    }

    public void setOri_no(String ori_no) {
        this.ori_no = ori_no;
    }

    public String getLine_no() {
        return line_no;
    }

    public void setLine_no(String line_no) {
        this.line_no = line_no;
    }

    public String getId_kpicate() {
        return id_kpicate;
    }

    public void setId_kpicate(String id_kpicate) {
        this.id_kpicate = id_kpicate;
    }

    public String getName_kpicate() {
        return name_kpicate;
    }

    public void setName_kpicate(String name_kpicate) {
        this.name_kpicate = name_kpicate;
    }

    public String getId_kpipost() {
        return id_kpipost;
    }

    public void setId_kpipost(String id_kpipost) {
        this.id_kpipost = id_kpipost;
    }

    public String getDec_scale() {
        return dec_scale;
    }

    public void setDec_scale(String dec_scale) {
        this.dec_scale = dec_scale;
    }

    public String getVar_evastd() {
        return var_evastd;
    }

    public void setVar_evastd(String var_evastd) {
        this.var_evastd = var_evastd;
    }

    public String getVar_comp() {
        return var_comp;
    }

    public void setVar_comp(String var_comp) {
        this.var_comp = var_comp;
    }

    public String getDec_mark() {
        return dec_mark;
    }

    public void setDec_mark(String dec_mark) {
        this.dec_mark = dec_mark;
    }

    public String getVar_dremark() {
        return var_dremark;
    }

    public void setVar_dremark(String var_dremark) {
        this.var_dremark = var_dremark;
    }
}
package com.mznerp.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by zy on 2017/6/13.
 */

public class DsaordbaseJson implements Comparable<DsaordbaseJson> {

//{"id_terminal":"2001106","name_terminal":"宁夏博瑞饲料有限公司","dsaord_no":"SO201705000140","line_no":"2","id_item":"SC0067","name_item":"三聚氰胺ELISA检测试剂盒","var_spec":"","var_pattern":"8孔","dec_qty":"77.0000000"}    /**
    /**
     * id_seller : 100003
     * name_seller : 何厚勇
     * id_terminal : 2000336
     * name_terminal : 甘肃中商食品质量检验检测有限公司
     * var_contact : 钱滢文
     * var_tel : 15002564678.0931-8490586
     * var_conplace : 甘肃省兰州市城关区雁南路449号（联创广场C座）16楼
     * var_rcvcorr : 甘肃中商食品质量检验检测有限公司
     * id_corr : 2000336
     * name_corr : 甘肃中商食品质量检验检测有限公司
     * id_item : SC0015
     * name_item : 赭曲霉毒素ELISA检测试剂盒
     * var_spec :
     * var_pattern : 8孔
     * id_uom : 117
     * name_uom : 盒
     * dec_price : 1960.00000000000
     * id_tax : 03
     * name_tax : 3增值税
     * dec_taxrate : 0.0300000
     */

    private String id_seller;
    private String name_seller;
    private String id_terminal;
    private String name_terminal;
    private String var_contact;
    private String var_tel;
    private String var_conplace;
    private String var_rcvcorr;
    private String id_corr;
    private String name_corr;
    private String id_item;
    private String name_item;
    private String var_desc;
    private String var_spec;
    private String var_pattern;
    private String id_uom;
    private String name_uom;
    private String dec_price;
    private String dec_amt;
    private String id_tax;
    private String name_tax;
    private String dec_taxrate;
    private String dsaord_no;
    private String line_no;
    private String dec_qty;
    private String name_area;
    private String name_zone;
    private String date_begin;
    private String date_end;
    private String date_opr;
    private String dec_samt;
    private String name_user;
    private String name_dept;
    private String date_task;
    private String name_type;
    private String name_wproj;
    private String var_wtitle;
    private String var_remark;
    private String dec_acaramt;
    private String dec_acclimit;
    private String var_chkparm;
    private String id_invtype;
    private String name_invtype;
    private String id_ordtype;
    private String name_ordtype;
    private String id_satype;
    private String name_samth;
    private String id_recorder;
    private String var_ispec;
    private String name_uomm;
    private String var_epsno;
    private String dec_ordqty;
    private String dec_ordamt;
    private String id_express;
    private String name_express;
    private String inv_epsno;
    private String dec_rcvamt;
    private String dec_setamt;
    /**
     * id_zone :
     * id_area : 21
     * 1-30天 : 0.00
     * 30-60天 : 0.00
     * 60-90天 : 0.00
     * 90-180天 : 35500.00
     * 180-270天 : 0.00
     * 270-365天 : 0.00
     * 365-730天 : 0.00
     * 730-1095天 : 0.00
     * 1095天以上 : 0.00
     */

    private String id_zone;
    private String id_area;
    @SerializedName("1-30天")
    private String _$130天;
    @SerializedName("30-60天")
    private String _$3060天;
    @SerializedName("60-90天")
    private String _$6090天;
    @SerializedName("90-180天")
    private String _$90180天;
    @SerializedName("180-270天")
    private String _$180270天;
    @SerializedName("270-365天")
    private String _$270365天;
    @SerializedName("365-730天")
    private String _$365730天;
    @SerializedName("730-1095天")
    private String _$7301095天;
    @SerializedName("1095天以上")
    private String _$1095天以上;
    @SerializedName("180-365天")
    private String _$180365天;
    @SerializedName("365天以上")
    private String _$365天以上;
    @SerializedName("总欠款额")
    private String _$总欠款额;

    public String get_$总欠款额() {
        return _$总欠款额;
    }

    public void set_$总欠款额(String _$总欠款额) {
        this._$总欠款额 = _$总欠款额;
    }

    public String get_$180365天() {
        return _$180365天;
    }

    public void set_$180365天(String _$180365天) {
        this._$180365天 = _$180365天;
    }

    public String get_$365天以上() {
        return _$365天以上;
    }

    public void set_$365天以上(String _$365天以上) {
        this._$365天以上 = _$365天以上;
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

    public String getId_satype() {
        return id_satype;
    }

    public void setId_satype(String id_satype) {
        this.id_satype = id_satype;
    }

    public String getName_samth() {
        return name_samth;
    }

    public void setName_samth(String name_samth) {
        this.name_samth = name_samth;
    }

    public String getId_recorder() {
        return id_recorder;
    }

    public void setId_recorder(String id_recorder) {
        this.id_recorder = id_recorder;
    }

    public String getVar_ispec() {
        return var_ispec;
    }

    public void setVar_ispec(String var_ispec) {
        this.var_ispec = var_ispec;
    }

    public String getName_uomm() {
        return name_uomm;
    }

    public void setName_uomm(String name_uomm) {
        this.name_uomm = name_uomm;
    }

    public String getVar_epsno() {
        return var_epsno;
    }

    public void setVar_epsno(String var_epsno) {
        this.var_epsno = var_epsno;
    }

    public String getDec_ordqty() {
        return dec_ordqty;
    }

    public void setDec_ordqty(String dec_ordqty) {
        this.dec_ordqty = dec_ordqty;
    }

    public String getDec_ordamt() {
        return dec_ordamt;
    }

    public void setDec_ordamt(String dec_ordamt) {
        this.dec_ordamt = dec_ordamt;
    }

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

    public String getInv_epsno() {
        return inv_epsno;
    }

    public void setInv_epsno(String inv_epsno) {
        this.inv_epsno = inv_epsno;
    }

    public String getDec_rcvamt() {
        return dec_rcvamt;
    }

    public void setDec_rcvamt(String dec_rcvamt) {
        this.dec_rcvamt = dec_rcvamt;
    }

    public String getDec_setamt() {
        return dec_setamt;
    }

    public void setDec_setamt(String dec_setamt) {
        this.dec_setamt = dec_setamt;
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

    public String getVar_chkparm() {
        return var_chkparm;
    }

    public void setVar_chkparm(String var_chkparm) {
        this.var_chkparm = var_chkparm;
    }

    public String getDec_amt() {
        return dec_amt;
    }

    public void setDec_amt(String dec_amt) {
        this.dec_amt = dec_amt;
    }

    public String getDate_opr() {
        return date_opr;
    }

    public void setDate_opr(String date_opr) {
        this.date_opr = date_opr;
    }

    public String getDec_acclimit() {
        return dec_acclimit;
    }

    public void setDec_acclimit(String dec_acclimit) {
        this.dec_acclimit = dec_acclimit;
    }

    public String getDec_acaramt() {
        return dec_acaramt;
    }

    public void setDec_acaramt(String dec_acaramt) {
        this.dec_acaramt = dec_acaramt;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
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

    public String getName_area() {
        return name_area;
    }

    public void setName_area(String name_area) {
        this.name_area = name_area;
    }

    public String getName_zone() {
        return name_zone;
    }

    public void setName_zone(String name_zone) {
        this.name_zone = name_zone;
    }

    public String getDate_begin() {
        return date_begin;
    }

    public void setDate_begin(String date_begin) {
        this.date_begin = date_begin;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getDec_samt() {
        return dec_samt;
    }

    public void setDec_samt(String dec_samt) {
        this.dec_samt = dec_samt;
    }

    public String getVar_desc() {
        return var_desc;
    }

    public void setVar_desc(String var_desc) {
        this.var_desc = var_desc;
    }

    public String getDsaord_no() {
        return dsaord_no;
    }

    public void setDsaord_no(String dsaord_no) {
        this.dsaord_no = dsaord_no;
    }

    public String getLine_no() {
        return line_no;
    }

    public void setLine_no(String line_no) {
        this.line_no = line_no;
    }

    public String getDec_qty() {
        return dec_qty;
    }

    public void setDec_qty(String dec_qty) {
        this.dec_qty = dec_qty;
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

    public String getId_terminal() {
        return id_terminal;
    }

    public void setId_terminal(String id_terminal) {
        this.id_terminal = id_terminal;
    }

    public String getName_terminal() {
        return name_terminal;
    }

    public void setName_terminal(String name_terminal) {
        this.name_terminal = name_terminal;
    }

    public String getVar_contact() {
        return var_contact;
    }

    public void setVar_contact(String var_contact) {
        this.var_contact = var_contact;
    }

    public String getVar_tel() {
        return var_tel;
    }

    public void setVar_tel(String var_tel) {
        this.var_tel = var_tel;
    }

    public String getVar_conplace() {
        return var_conplace;
    }

    public void setVar_conplace(String var_conplace) {
        this.var_conplace = var_conplace;
    }

    public String getVar_rcvcorr() {
        return var_rcvcorr;
    }

    public void setVar_rcvcorr(String var_rcvcorr) {
        this.var_rcvcorr = var_rcvcorr;
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

    public String getName_item() {
        return name_item;
    }

    public void setName_item(String name_item) {
        this.name_item = name_item;
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
        return "DsaordbaseJson{" +
                "id_seller='" + id_seller + '\'' +
                ", name_seller='" + name_seller + '\'' +
                ", id_terminal='" + id_terminal + '\'' +
                ", name_terminal='" + name_terminal + '\'' +
                ", var_contact='" + var_contact + '\'' +
                ", var_tel='" + var_tel + '\'' +
                ", var_conplace='" + var_conplace + '\'' +
                ", var_rcvcorr='" + var_rcvcorr + '\'' +
                ", id_corr='" + id_corr + '\'' +
                ", name_corr='" + name_corr + '\'' +
                ", id_item='" + id_item + '\'' +
                ", name_item='" + name_item + '\'' +
                ", var_spec='" + var_spec + '\'' +
                ", var_pattern='" + var_pattern + '\'' +
                ", id_uom='" + id_uom + '\'' +
                ", name_uom='" + name_uom + '\'' +
                ", dec_price='" + dec_price + '\'' +
                ", id_tax='" + id_tax + '\'' +
                ", name_tax='" + name_tax + '\'' +
                ", dec_taxrate='" + dec_taxrate + '\'' +
                ", dsaord_no='" + dsaord_no + '\'' +
                ", line_no='" + line_no + '\'' +
                ", dec_qty='" + dec_qty + '\'' +
                '}';
    }

    @Override
    public int compareTo(DsaordbaseJson dsaordbaseJson) {
        int compare = 0;
        if (!TextUtils.isEmpty(dsaordbaseJson.dsaord_no)) {
            compare = dsaordbaseJson.dsaord_no.compareTo(this.dsaord_no);
            if (compare == 0) {
                compare = Integer.valueOf(this.line_no) - Integer.valueOf(dsaordbaseJson.line_no);
            }

        } else if (!TextUtils.isEmpty(dsaordbaseJson.date_begin)) {
            try {
                long a = new SimpleDateFormat("MM-dd-yyyy").parse(dsaordbaseJson.date_begin).getTime() - new SimpleDateFormat("MM-dd-yyyy").parse(this.date_begin).getTime();
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
            if (compare == 0) {
                compare = this.id_seller.compareTo(dsaordbaseJson.id_seller);
                if (compare == 0) {
                    compare = this.id_terminal.compareTo(dsaordbaseJson.id_terminal);

                }
            }
        } else if (!TextUtils.isEmpty(dsaordbaseJson.date_task)) {
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

    public String getId_zone() {
        return id_zone;
    }

    public void setId_zone(String id_zone) {
        this.id_zone = id_zone;
    }

    public String getId_area() {
        return id_area;
    }

    public void setId_area(String id_area) {
        this.id_area = id_area;
    }

    public String get_$130天() {
        return _$130天;
    }

    public void set_$130天(String _$130天) {
        this._$130天 = _$130天;
    }

    public String get_$3060天() {
        return _$3060天;
    }

    public void set_$3060天(String _$3060天) {
        this._$3060天 = _$3060天;
    }

    public String get_$6090天() {
        return _$6090天;
    }

    public void set_$6090天(String _$6090天) {
        this._$6090天 = _$6090天;
    }

    public String get_$90180天() {
        return _$90180天;
    }

    public void set_$90180天(String _$90180天) {
        this._$90180天 = _$90180天;
    }

    public String get_$180270天() {
        return _$180270天;
    }

    public void set_$180270天(String _$180270天) {
        this._$180270天 = _$180270天;
    }

    public String get_$270365天() {
        return _$270365天;
    }

    public void set_$270365天(String _$270365天) {
        this._$270365天 = _$270365天;
    }

    public String get_$365730天() {
        return _$365730天;
    }

    public void set_$365730天(String _$365730天) {
        this._$365730天 = _$365730天;
    }

    public String get_$7301095天() {
        return _$7301095天;
    }

    public void set_$7301095天(String _$7301095天) {
        this._$7301095天 = _$7301095天;
    }

    public String get_$1095天以上() {
        return _$1095天以上;
    }

    public void set_$1095天以上(String _$1095天以上) {
        this._$1095天以上 = _$1095天以上;
    }
}

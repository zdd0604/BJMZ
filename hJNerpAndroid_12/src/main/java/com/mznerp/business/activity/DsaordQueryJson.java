package com.mznerp.business.activity;

import android.text.TextUtils;

/**
 * Created by zy on 2017/6/13.
 */

public class DsaordQueryJson implements Comparable<DsaordQueryJson> {


    /**
     * dsaord_no : SO201708000314
     * line_no : 1
     * date_opr : 09-07-2017
     * id_ordtype : P
     * name_ordtype : 普通订单
     * id_satype : 10
     * name_samth : 正常销售
     * id_recorder : 100001
     * name_user : 柳家鹏
     * id_corr : 2002142
     * name_corr : 渭南大北农农牧科技有限公司
     * id_terminal : 2002142
     * name_terminal : 渭南大北农农牧科技有限公司
     * id_seller : 100001
     * name_seller : 柳家鹏
     * id_item : JC0010
     * name_item : 玉米赤霉烯酮快速检测试纸条
     * var_ispec : 96条/盒
     * var_pattern : 8T
     * var_chkparm : 【60-1000μg/kg】
     * id_uom : 111
     * name_uomm : 条
     * dec_price : 0.0000000
     * dec_ordqty : 9.0000000
     * dec_ordamt : 0.00
     * var_epsno :
     * id_express :
     * name_express :
     * inv_epsno :
     * dec_rcvamt :
     * dec_setamt :
     */

    private String dsaord_no;
    private String line_no;
    private String date_opr;
    private String id_ordtype;
    private String name_ordtype;
    private String id_satype;
    private String name_samth;
    private String id_recorder;
    private String name_user;
    private String id_corr;
    private String name_corr;
    private String id_terminal;
    private String name_terminal;
    private String id_seller;
    private String name_seller;
    private String id_item;
    private String name_item;
    private String var_ispec;
    private String var_pattern;
    private String var_chkparm;
    private String id_uom;
    private String name_uomm;
    private String dec_price;
    private String dec_ordqty;
    private String dec_ordamt;
    private String var_epsno;
    private String id_express;
    private String name_express;
    private String inv_epsno;
    private String dec_rcvamt;
    private String dec_setamt;

    @Override
    public int compareTo(DsaordQueryJson dsaordbaseJson) {
        int compare = 0;
        if (!TextUtils.isEmpty(dsaordbaseJson.dsaord_no)) {
            compare = dsaordbaseJson.dsaord_no.compareTo(this.dsaord_no);
            if (compare == 0) {
                compare = Integer.valueOf(this.line_no) - Integer.valueOf(dsaordbaseJson.line_no);
            }

//        } else if (!TextUtils.isEmpty(dsaordbaseJson.date_begin)) {
//            try {
//                long a = new SimpleDateFormat("MM-dd-yyyy").parse(dsaordbaseJson.date_begin).getTime() - new SimpleDateFormat("MM-dd-yyyy").parse(this.date_begin).getTime();
//                if (a > 0) {
//                    compare = 1;
//                } else if (a == 0) {
//                    compare = 0;
//                } else {
//                    compare = -1;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (compare == 0) {
//                compare = this.id_seller.compareTo(dsaordbaseJson.id_seller);
//                if (compare == 0) {
//                    compare = this.id_terminal.compareTo(dsaordbaseJson.id_terminal);
//
//                }
//            }
//        } else if (!TextUtils.isEmpty(dsaordbaseJson.date_task)) {
//            try {
//                long a = new SimpleDateFormat("yyyy.MM.dd").parse(dsaordbaseJson.date_task).getTime() - new SimpleDateFormat("yyyy.MM.dd").parse(this.date_task).getTime();
//                if (a > 0) {
//                    compare = 1;
//                } else if (a == 0) {
//                    compare = 0;
//                } else {
//                    compare = -1;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }

        return compare;
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

    public String getDate_opr() {
        return date_opr;
    }

    public void setDate_opr(String date_opr) {
        this.date_opr = date_opr;
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

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
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

    public String getVar_ispec() {
        return var_ispec;
    }

    public void setVar_ispec(String var_ispec) {
        this.var_ispec = var_ispec;
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

    public String getName_uomm() {
        return name_uomm;
    }

    public void setName_uomm(String name_uomm) {
        this.name_uomm = name_uomm;
    }

    public String getDec_price() {
        return dec_price;
    }

    public void setDec_price(String dec_price) {
        this.dec_price = dec_price;
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

    public String getVar_epsno() {
        return var_epsno;
    }

    public void setVar_epsno(String var_epsno) {
        this.var_epsno = var_epsno;
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
}

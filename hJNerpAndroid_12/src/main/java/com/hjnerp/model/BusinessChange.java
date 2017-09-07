package com.hjnerp.model;

import java.util.List;

/**
 * Created by zy on 2017/9/6.
 */

public class BusinessChange {
    /**
     * table_name : dsaord_03
     * table_no : SO201708000295
     * table_no_name : dsaord_no
     * values : [{"dealtype":"U","line_no":"1","name_corr":"123456"},{"dealtype":"N","line_no":"2","name_corr":"123456"},{"dealtype":"N","line_no":"4","name_corr":"123456"},{"dealtype":"N","line_no":"3","name_corr":"123456"}]
     */

    private String table_name;//表名
    private String table_no;//表单号
    private String table_no_name;//表单号的名称
    private List<ValuesBean> values;//提交数据

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getTable_no_name() {
        return table_no_name;
    }

    public void setTable_no_name(String table_no_name) {
        this.table_no_name = table_no_name;
    }

    public List<ValuesBean> getValues() {
        return values;
    }

    public void setValues(List<ValuesBean> values) {
        this.values = values;
    }

    public static class ValuesBean {

        /**
         * date_audit : 审核时间
         * date_pause : 中止时间
         * dec_feerate : 费率
         * dec_import : 已导入值
         * dec_invqty : 库存量
         * dec_jxprice : 镜像单价
         * dec_noamt : 不含税金额
         * dec_noprice : 不含税单价
         * dec_ordqty : 订单执行数量
         * dec_baseprice : 指导价
         * dec_conprice : 报价
         * dec_corramt : 经销商金额
         * dec_corrprice : 经销商价
         * dec_discprice : 折扣单价
         * dec_discrate : 折扣率
         * dec_pqty : 数量
         * dec_price : 单价
         * dec_scorramt : 经销商总金额
         * dec_srate : 总比例
         * dec_taxamt : 税额
         * dec_utprice : 人份单价
         * dec_utqty : 人份数量
         * dfeepol_no : 返利单号
         * fiscal_period : 核算月
         * fiscal_year : 核算年
         * flag_custom : 是否报关
         * flag_dsainv : 开票标志
         * var_name : 方案
         * dec_rpamt : 返利金额
         * dec_rpprice : 返利单价
         * flag_gift : 赠品
         * flag_lotcheck : 药品批批检
         * flag_prerec : 预收标志
         * flag_pscheme : 方案
         * flag_psts : 中止标志
         * flag_quagspcom : 公司资质单
         * flag_quagspitem : 产品资质单
         * flag_qualcheck : 出厂合格报告
         * flag_sts : 状态
         * name_flagsts : 状态
         * flag_temp : 温度单
         * id_audit : 审核人
         * name_user : 录入人
         * id_bflow : 前流程
         * id_channel : 渠道类型
         * name_channel : 渠道类型
         * id_chargepay : 运费付款方
         * name_corr : 结算客户
         * id_com : 账套
         * name_com : 账套
         * id_corrtype : 客户类型
         * name_corrtype : 客户类型
         * id_flow : 流程代码
         * id_loc : 货位
         * name_loc : 货位
         * id_lot : 批号
         * id_ordsource : 订单来源
         * name_ordsource : 订单来源
         * id_oriitem : 来源产品
         * id_pause : 中止人
         * id_pitem : 生产物料代码
         * id_delivery : 交货方式
         * name_delivery : 交货方式
         * id_dept : 部门
         * name_dept : 部门
         * id_rtndealtype : 退货处理方式
         * name_rtndealtype : 退货处理方式
         * id_saprt : 价格类型
         * name_saprt : 价格类型
         * id_setmth : 结算方式
         * name_setmth : 结算方式
         * id_single : 序列号
         * id_supcorr : 经销商
         * id_table : 业务名
         * id_transport : 运输方式
         * name_transport : 运输方式
         * id_wh : 仓库
         * name_wh : 仓库
         * name_item : 物料名称
         * var_conno : 合同编号
         * var_rejust : 驳回意见
         * var_stoprsn : 中止原因
         * flag_ercv : 是否收货
         * flag_einv : 是否收票
         * dsaord_no : 单据号
         * date_sign : 订单日期
         * id_ordtype : 订单类型
         * name_ordtype : 订单类型
         * int_saconnum : 签订合同数
         * id_corr : 客户代码
         * id_seller : 业务员
         * name_seller : 业务员
         * id_terminal : 所属终端
         * name_terminal : 终端名称
         * dec_acclimit : 账期(月）
         * id_area : 区域
         * name_area : 区域
         * id_zone : 省区
         * name_zone : 省区
         * date_demand : 发货日期
         * id_express : 快递公司
         * name_express : 发票快递公司
         * var_addr : 送货地址
         * var_rcvcorr : 收货单位
         * var_contact : 联系人
         * var_tel : 联系方式
         * id_invexpress : 发票快递公司
         * id_invtype : 发票类型
         * name_invtype : 发票类型
         * date_planinv : 预计开票日期
         * flag_itemwith : 票货同行
         * dec_acaramt : 超期金额
         * var_invaddr : 发票邮寄地址
         * var_invcontact : 邮寄联系人
         * var_invtel : 联系人电话
         * dec_samt : 总金额
         * dec_orisamt : 总金额(原币)
         * var_remark : 备注
         * id_recorder : 录入人
         * date_opr : 录入时间
         * var_append : 附件上传
         * line_no : 行号
         * id_satype : 销售类型
         * name_samth : 销售类型
         * id_item : 产品代码
         * id_uom : 单位
         * name_uom : 单位
         * dec_qty : 数量
         * dec_oriprice : 单价(原币)
         * dec_amt : 金额
         * dec_taxrate : 税率
         * id_accitem : 纯赠品
         * dec_oriamt : 金额(原币)
         * id_curr : 币种
         * name_curr : 币种
         * dec_exchangerate : 汇率
         * id_tax : 税种
         * name_tax : 税种
         * var_dremark : 明细备注
         * ori_no : 原始单号
         * ori_line_no : 原始单行号
         */

        private String date_audit;
        private String date_pause;
        private String dec_feerate;
        private String dec_import;
        private String dec_invqty;
        private String dec_jxprice;
        private String dec_noamt;
        private String dec_noprice;
        private String dec_ordqty;
        private String dec_baseprice;
        private String dec_conprice;
        private String dec_corramt;
        private String dec_corrprice;
        private String dec_discprice;
        private String dec_discrate;
        private String dec_pqty;
        private String dec_price;
        private String dec_scorramt;
        private String dec_srate;
        private String dec_taxamt;
        private String dec_utprice;
        private String dec_utqty;
        private String dfeepol_no;
        private String fiscal_period;
        private String fiscal_year;
        private String flag_custom;
        private String flag_dsainv;
        private String var_name;
        private String dec_rpamt;
        private String dec_rpprice;
        private String flag_gift;
        private String flag_lotcheck;
        private String flag_prerec;
        private String flag_pscheme;
        private String flag_psts;
        private String flag_quagspcom;
        private String flag_quagspitem;
        private String flag_qualcheck;
        private String flag_sts;
        private String name_flagsts;
        private String flag_temp;
        private String id_audit;
        private String name_user;
        private String id_bflow;
        private String id_channel;
        private String name_channel;
        private String id_chargepay;
        private String name_corr;
        private String id_com;
        private String name_com;
        private String id_corrtype;
        private String name_corrtype;
        private String id_flow;
        private String id_loc;
        private String name_loc;
        private String id_lot;
        private String id_ordsource;
        private String name_ordsource;
        private String id_oriitem;
        private String id_pause;
        private String id_pitem;
        private String id_delivery;
        private String name_delivery;
        private String id_dept;
        private String name_dept;
        private String id_rtndealtype;
        private String name_rtndealtype;
        private String id_saprt;
        private String name_saprt;
        private String id_setmth;
        private String name_setmth;
        private String id_single;
        private String id_supcorr;
        private String id_table;
        private String id_transport;
        private String name_transport;
        private String id_wh;
        private String name_wh;
        private String name_item;
        private String var_conno;
        private String var_rejust;
        private String var_stoprsn;
        private String flag_ercv;
        private String flag_einv;
        private String dsaord_no;
        private String date_sign;
        private String id_ordtype;
        private String name_ordtype;
        private String int_saconnum;
        private String id_corr;
        private String id_seller;
        private String name_seller;
        private String id_terminal;
        private String name_terminal;
        private String dec_acclimit;
        private String id_area;
        private String name_area;
        private String id_zone;
        private String name_zone;
        private String date_demand;
        private String id_express;
        private String name_express;
        private String var_addr;
        private String var_rcvcorr;
        private String var_contact;
        private String var_tel;
        private String id_invexpress;
        private String id_invtype;
        private String name_invtype;
        private String date_planinv;
        private String flag_itemwith;
        private String dec_acaramt;
        private String var_invaddr;
        private String var_invcontact;
        private String var_invtel;
        private String dec_samt;
        private String dec_orisamt;
        private String var_remark;
        private String id_recorder;
        private String date_opr;
        private String var_append;
        private String line_no;
        private String id_satype;
        private String name_samth;
        private String id_item;
        private String id_uom;
        private String name_uom;
        private String dec_qty;
        private String dec_oriprice;
        private String dec_amt;
        private String dec_taxrate;
        private String id_accitem;
        private String dec_oriamt;
        private String id_curr;
        private String name_curr;
        private String dec_exchangerate;
        private String id_tax;
        private String name_tax;
        private String var_dremark;
        private String ori_no;
        private String ori_line_no;
        private String dealtype;

        public String getDealtype() {
            return dealtype;
        }

        public void setDealtype(String dealtype) {
            this.dealtype = dealtype;
        }

        public String getDate_audit() {
            return date_audit;
        }

        public void setDate_audit(String date_audit) {
            this.date_audit = date_audit;
        }

        public String getDate_pause() {
            return date_pause;
        }

        public void setDate_pause(String date_pause) {
            this.date_pause = date_pause;
        }

        public String getDec_feerate() {
            return dec_feerate;
        }

        public void setDec_feerate(String dec_feerate) {
            this.dec_feerate = dec_feerate;
        }

        public String getDec_import() {
            return dec_import;
        }

        public void setDec_import(String dec_import) {
            this.dec_import = dec_import;
        }

        public String getDec_invqty() {
            return dec_invqty;
        }

        public void setDec_invqty(String dec_invqty) {
            this.dec_invqty = dec_invqty;
        }

        public String getDec_jxprice() {
            return dec_jxprice;
        }

        public void setDec_jxprice(String dec_jxprice) {
            this.dec_jxprice = dec_jxprice;
        }

        public String getDec_noamt() {
            return dec_noamt;
        }

        public void setDec_noamt(String dec_noamt) {
            this.dec_noamt = dec_noamt;
        }

        public String getDec_noprice() {
            return dec_noprice;
        }

        public void setDec_noprice(String dec_noprice) {
            this.dec_noprice = dec_noprice;
        }

        public String getDec_ordqty() {
            return dec_ordqty;
        }

        public void setDec_ordqty(String dec_ordqty) {
            this.dec_ordqty = dec_ordqty;
        }

        public String getDec_baseprice() {
            return dec_baseprice;
        }

        public void setDec_baseprice(String dec_baseprice) {
            this.dec_baseprice = dec_baseprice;
        }

        public String getDec_conprice() {
            return dec_conprice;
        }

        public void setDec_conprice(String dec_conprice) {
            this.dec_conprice = dec_conprice;
        }

        public String getDec_corramt() {
            return dec_corramt;
        }

        public void setDec_corramt(String dec_corramt) {
            this.dec_corramt = dec_corramt;
        }

        public String getDec_corrprice() {
            return dec_corrprice;
        }

        public void setDec_corrprice(String dec_corrprice) {
            this.dec_corrprice = dec_corrprice;
        }

        public String getDec_discprice() {
            return dec_discprice;
        }

        public void setDec_discprice(String dec_discprice) {
            this.dec_discprice = dec_discprice;
        }

        public String getDec_discrate() {
            return dec_discrate;
        }

        public void setDec_discrate(String dec_discrate) {
            this.dec_discrate = dec_discrate;
        }

        public String getDec_pqty() {
            return dec_pqty;
        }

        public void setDec_pqty(String dec_pqty) {
            this.dec_pqty = dec_pqty;
        }

        public String getDec_price() {
            return dec_price;
        }

        public void setDec_price(String dec_price) {
            this.dec_price = dec_price;
        }

        public String getDec_scorramt() {
            return dec_scorramt;
        }

        public void setDec_scorramt(String dec_scorramt) {
            this.dec_scorramt = dec_scorramt;
        }

        public String getDec_srate() {
            return dec_srate;
        }

        public void setDec_srate(String dec_srate) {
            this.dec_srate = dec_srate;
        }

        public String getDec_taxamt() {
            return dec_taxamt;
        }

        public void setDec_taxamt(String dec_taxamt) {
            this.dec_taxamt = dec_taxamt;
        }

        public String getDec_utprice() {
            return dec_utprice;
        }

        public void setDec_utprice(String dec_utprice) {
            this.dec_utprice = dec_utprice;
        }

        public String getDec_utqty() {
            return dec_utqty;
        }

        public void setDec_utqty(String dec_utqty) {
            this.dec_utqty = dec_utqty;
        }

        public String getDfeepol_no() {
            return dfeepol_no;
        }

        public void setDfeepol_no(String dfeepol_no) {
            this.dfeepol_no = dfeepol_no;
        }

        public String getFiscal_period() {
            return fiscal_period;
        }

        public void setFiscal_period(String fiscal_period) {
            this.fiscal_period = fiscal_period;
        }

        public String getFiscal_year() {
            return fiscal_year;
        }

        public void setFiscal_year(String fiscal_year) {
            this.fiscal_year = fiscal_year;
        }

        public String getFlag_custom() {
            return flag_custom;
        }

        public void setFlag_custom(String flag_custom) {
            this.flag_custom = flag_custom;
        }

        public String getFlag_dsainv() {
            return flag_dsainv;
        }

        public void setFlag_dsainv(String flag_dsainv) {
            this.flag_dsainv = flag_dsainv;
        }

        public String getVar_name() {
            return var_name;
        }

        public void setVar_name(String var_name) {
            this.var_name = var_name;
        }

        public String getDec_rpamt() {
            return dec_rpamt;
        }

        public void setDec_rpamt(String dec_rpamt) {
            this.dec_rpamt = dec_rpamt;
        }

        public String getDec_rpprice() {
            return dec_rpprice;
        }

        public void setDec_rpprice(String dec_rpprice) {
            this.dec_rpprice = dec_rpprice;
        }

        public String getFlag_gift() {
            return flag_gift;
        }

        public void setFlag_gift(String flag_gift) {
            this.flag_gift = flag_gift;
        }

        public String getFlag_lotcheck() {
            return flag_lotcheck;
        }

        public void setFlag_lotcheck(String flag_lotcheck) {
            this.flag_lotcheck = flag_lotcheck;
        }

        public String getFlag_prerec() {
            return flag_prerec;
        }

        public void setFlag_prerec(String flag_prerec) {
            this.flag_prerec = flag_prerec;
        }

        public String getFlag_pscheme() {
            return flag_pscheme;
        }

        public void setFlag_pscheme(String flag_pscheme) {
            this.flag_pscheme = flag_pscheme;
        }

        public String getFlag_psts() {
            return flag_psts;
        }

        public void setFlag_psts(String flag_psts) {
            this.flag_psts = flag_psts;
        }

        public String getFlag_quagspcom() {
            return flag_quagspcom;
        }

        public void setFlag_quagspcom(String flag_quagspcom) {
            this.flag_quagspcom = flag_quagspcom;
        }

        public String getFlag_quagspitem() {
            return flag_quagspitem;
        }

        public void setFlag_quagspitem(String flag_quagspitem) {
            this.flag_quagspitem = flag_quagspitem;
        }

        public String getFlag_qualcheck() {
            return flag_qualcheck;
        }

        public void setFlag_qualcheck(String flag_qualcheck) {
            this.flag_qualcheck = flag_qualcheck;
        }

        public String getFlag_sts() {
            return flag_sts;
        }

        public void setFlag_sts(String flag_sts) {
            this.flag_sts = flag_sts;
        }

        public String getName_flagsts() {
            return name_flagsts;
        }

        public void setName_flagsts(String name_flagsts) {
            this.name_flagsts = name_flagsts;
        }

        public String getFlag_temp() {
            return flag_temp;
        }

        public void setFlag_temp(String flag_temp) {
            this.flag_temp = flag_temp;
        }

        public String getId_audit() {
            return id_audit;
        }

        public void setId_audit(String id_audit) {
            this.id_audit = id_audit;
        }

        public String getName_user() {
            return name_user;
        }

        public void setName_user(String name_user) {
            this.name_user = name_user;
        }

        public String getId_bflow() {
            return id_bflow;
        }

        public void setId_bflow(String id_bflow) {
            this.id_bflow = id_bflow;
        }

        public String getId_channel() {
            return id_channel;
        }

        public void setId_channel(String id_channel) {
            this.id_channel = id_channel;
        }

        public String getName_channel() {
            return name_channel;
        }

        public void setName_channel(String name_channel) {
            this.name_channel = name_channel;
        }

        public String getId_chargepay() {
            return id_chargepay;
        }

        public void setId_chargepay(String id_chargepay) {
            this.id_chargepay = id_chargepay;
        }

        public String getName_corr() {
            return name_corr;
        }

        public void setName_corr(String name_corr) {
            this.name_corr = name_corr;
        }

        public String getId_com() {
            return id_com;
        }

        public void setId_com(String id_com) {
            this.id_com = id_com;
        }

        public String getName_com() {
            return name_com;
        }

        public void setName_com(String name_com) {
            this.name_com = name_com;
        }

        public String getId_corrtype() {
            return id_corrtype;
        }

        public void setId_corrtype(String id_corrtype) {
            this.id_corrtype = id_corrtype;
        }

        public String getName_corrtype() {
            return name_corrtype;
        }

        public void setName_corrtype(String name_corrtype) {
            this.name_corrtype = name_corrtype;
        }

        public String getId_flow() {
            return id_flow;
        }

        public void setId_flow(String id_flow) {
            this.id_flow = id_flow;
        }

        public String getId_loc() {
            return id_loc;
        }

        public void setId_loc(String id_loc) {
            this.id_loc = id_loc;
        }

        public String getName_loc() {
            return name_loc;
        }

        public void setName_loc(String name_loc) {
            this.name_loc = name_loc;
        }

        public String getId_lot() {
            return id_lot;
        }

        public void setId_lot(String id_lot) {
            this.id_lot = id_lot;
        }

        public String getId_ordsource() {
            return id_ordsource;
        }

        public void setId_ordsource(String id_ordsource) {
            this.id_ordsource = id_ordsource;
        }

        public String getName_ordsource() {
            return name_ordsource;
        }

        public void setName_ordsource(String name_ordsource) {
            this.name_ordsource = name_ordsource;
        }

        public String getId_oriitem() {
            return id_oriitem;
        }

        public void setId_oriitem(String id_oriitem) {
            this.id_oriitem = id_oriitem;
        }

        public String getId_pause() {
            return id_pause;
        }

        public void setId_pause(String id_pause) {
            this.id_pause = id_pause;
        }

        public String getId_pitem() {
            return id_pitem;
        }

        public void setId_pitem(String id_pitem) {
            this.id_pitem = id_pitem;
        }

        public String getId_delivery() {
            return id_delivery;
        }

        public void setId_delivery(String id_delivery) {
            this.id_delivery = id_delivery;
        }

        public String getName_delivery() {
            return name_delivery;
        }

        public void setName_delivery(String name_delivery) {
            this.name_delivery = name_delivery;
        }

        public String getId_dept() {
            return id_dept;
        }

        public void setId_dept(String id_dept) {
            this.id_dept = id_dept;
        }

        public String getName_dept() {
            return name_dept;
        }

        public void setName_dept(String name_dept) {
            this.name_dept = name_dept;
        }

        public String getId_rtndealtype() {
            return id_rtndealtype;
        }

        public void setId_rtndealtype(String id_rtndealtype) {
            this.id_rtndealtype = id_rtndealtype;
        }

        public String getName_rtndealtype() {
            return name_rtndealtype;
        }

        public void setName_rtndealtype(String name_rtndealtype) {
            this.name_rtndealtype = name_rtndealtype;
        }

        public String getId_saprt() {
            return id_saprt;
        }

        public void setId_saprt(String id_saprt) {
            this.id_saprt = id_saprt;
        }

        public String getName_saprt() {
            return name_saprt;
        }

        public void setName_saprt(String name_saprt) {
            this.name_saprt = name_saprt;
        }

        public String getId_setmth() {
            return id_setmth;
        }

        public void setId_setmth(String id_setmth) {
            this.id_setmth = id_setmth;
        }

        public String getName_setmth() {
            return name_setmth;
        }

        public void setName_setmth(String name_setmth) {
            this.name_setmth = name_setmth;
        }

        public String getId_single() {
            return id_single;
        }

        public void setId_single(String id_single) {
            this.id_single = id_single;
        }

        public String getId_supcorr() {
            return id_supcorr;
        }

        public void setId_supcorr(String id_supcorr) {
            this.id_supcorr = id_supcorr;
        }

        public String getId_table() {
            return id_table;
        }

        public void setId_table(String id_table) {
            this.id_table = id_table;
        }

        public String getId_transport() {
            return id_transport;
        }

        public void setId_transport(String id_transport) {
            this.id_transport = id_transport;
        }

        public String getName_transport() {
            return name_transport;
        }

        public void setName_transport(String name_transport) {
            this.name_transport = name_transport;
        }

        public String getId_wh() {
            return id_wh;
        }

        public void setId_wh(String id_wh) {
            this.id_wh = id_wh;
        }

        public String getName_wh() {
            return name_wh;
        }

        public void setName_wh(String name_wh) {
            this.name_wh = name_wh;
        }

        public String getName_item() {
            return name_item;
        }

        public void setName_item(String name_item) {
            this.name_item = name_item;
        }

        public String getVar_conno() {
            return var_conno;
        }

        public void setVar_conno(String var_conno) {
            this.var_conno = var_conno;
        }

        public String getVar_rejust() {
            return var_rejust;
        }

        public void setVar_rejust(String var_rejust) {
            this.var_rejust = var_rejust;
        }

        public String getVar_stoprsn() {
            return var_stoprsn;
        }

        public void setVar_stoprsn(String var_stoprsn) {
            this.var_stoprsn = var_stoprsn;
        }

        public String getFlag_ercv() {
            return flag_ercv;
        }

        public void setFlag_ercv(String flag_ercv) {
            this.flag_ercv = flag_ercv;
        }

        public String getFlag_einv() {
            return flag_einv;
        }

        public void setFlag_einv(String flag_einv) {
            this.flag_einv = flag_einv;
        }

        public String getDsaord_no() {
            return dsaord_no;
        }

        public void setDsaord_no(String dsaord_no) {
            this.dsaord_no = dsaord_no;
        }

        public String getDate_sign() {
            return date_sign;
        }

        public void setDate_sign(String date_sign) {
            this.date_sign = date_sign;
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

        public String getInt_saconnum() {
            return int_saconnum;
        }

        public void setInt_saconnum(String int_saconnum) {
            this.int_saconnum = int_saconnum;
        }

        public String getId_corr() {
            return id_corr;
        }

        public void setId_corr(String id_corr) {
            this.id_corr = id_corr;
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

        public String getDec_acclimit() {
            return dec_acclimit;
        }

        public void setDec_acclimit(String dec_acclimit) {
            this.dec_acclimit = dec_acclimit;
        }

        public String getId_area() {
            return id_area;
        }

        public void setId_area(String id_area) {
            this.id_area = id_area;
        }

        public String getName_area() {
            return name_area;
        }

        public void setName_area(String name_area) {
            this.name_area = name_area;
        }

        public String getId_zone() {
            return id_zone;
        }

        public void setId_zone(String id_zone) {
            this.id_zone = id_zone;
        }

        public String getName_zone() {
            return name_zone;
        }

        public void setName_zone(String name_zone) {
            this.name_zone = name_zone;
        }

        public String getDate_demand() {
            return date_demand;
        }

        public void setDate_demand(String date_demand) {
            this.date_demand = date_demand;
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

        public String getVar_addr() {
            return var_addr;
        }

        public void setVar_addr(String var_addr) {
            this.var_addr = var_addr;
        }

        public String getVar_rcvcorr() {
            return var_rcvcorr;
        }

        public void setVar_rcvcorr(String var_rcvcorr) {
            this.var_rcvcorr = var_rcvcorr;
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

        public String getId_invexpress() {
            return id_invexpress;
        }

        public void setId_invexpress(String id_invexpress) {
            this.id_invexpress = id_invexpress;
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

        public String getDate_planinv() {
            return date_planinv;
        }

        public void setDate_planinv(String date_planinv) {
            this.date_planinv = date_planinv;
        }

        public String getFlag_itemwith() {
            return flag_itemwith;
        }

        public void setFlag_itemwith(String flag_itemwith) {
            this.flag_itemwith = flag_itemwith;
        }

        public String getDec_acaramt() {
            return dec_acaramt;
        }

        public void setDec_acaramt(String dec_acaramt) {
            this.dec_acaramt = dec_acaramt;
        }

        public String getVar_invaddr() {
            return var_invaddr;
        }

        public void setVar_invaddr(String var_invaddr) {
            this.var_invaddr = var_invaddr;
        }

        public String getVar_invcontact() {
            return var_invcontact;
        }

        public void setVar_invcontact(String var_invcontact) {
            this.var_invcontact = var_invcontact;
        }

        public String getVar_invtel() {
            return var_invtel;
        }

        public void setVar_invtel(String var_invtel) {
            this.var_invtel = var_invtel;
        }

        public String getDec_samt() {
            return dec_samt;
        }

        public void setDec_samt(String dec_samt) {
            this.dec_samt = dec_samt;
        }

        public String getDec_orisamt() {
            return dec_orisamt;
        }

        public void setDec_orisamt(String dec_orisamt) {
            this.dec_orisamt = dec_orisamt;
        }

        public String getVar_remark() {
            return var_remark;
        }

        public void setVar_remark(String var_remark) {
            this.var_remark = var_remark;
        }

        public String getId_recorder() {
            return id_recorder;
        }

        public void setId_recorder(String id_recorder) {
            this.id_recorder = id_recorder;
        }

        public String getDate_opr() {
            return date_opr;
        }

        public void setDate_opr(String date_opr) {
            this.date_opr = date_opr;
        }

        public String getVar_append() {
            return var_append;
        }

        public void setVar_append(String var_append) {
            this.var_append = var_append;
        }

        public String getLine_no() {
            return line_no;
        }

        public void setLine_no(String line_no) {
            this.line_no = line_no;
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

        public String getId_item() {
            return id_item;
        }

        public void setId_item(String id_item) {
            this.id_item = id_item;
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

        public String getDec_qty() {
            return dec_qty;
        }

        public void setDec_qty(String dec_qty) {
            this.dec_qty = dec_qty;
        }

        public String getDec_oriprice() {
            return dec_oriprice;
        }

        public void setDec_oriprice(String dec_oriprice) {
            this.dec_oriprice = dec_oriprice;
        }

        public String getDec_amt() {
            return dec_amt;
        }

        public void setDec_amt(String dec_amt) {
            this.dec_amt = dec_amt;
        }

        public String getDec_taxrate() {
            return dec_taxrate;
        }

        public void setDec_taxrate(String dec_taxrate) {
            this.dec_taxrate = dec_taxrate;
        }

        public String getId_accitem() {
            return id_accitem;
        }

        public void setId_accitem(String id_accitem) {
            this.id_accitem = id_accitem;
        }

        public String getDec_oriamt() {
            return dec_oriamt;
        }

        public void setDec_oriamt(String dec_oriamt) {
            this.dec_oriamt = dec_oriamt;
        }

        public String getId_curr() {
            return id_curr;
        }

        public void setId_curr(String id_curr) {
            this.id_curr = id_curr;
        }

        public String getName_curr() {
            return name_curr;
        }

        public void setName_curr(String name_curr) {
            this.name_curr = name_curr;
        }

        public String getDec_exchangerate() {
            return dec_exchangerate;
        }

        public void setDec_exchangerate(String dec_exchangerate) {
            this.dec_exchangerate = dec_exchangerate;
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

        public String getVar_dremark() {
            return var_dremark;
        }

        public void setVar_dremark(String var_dremark) {
            this.var_dremark = var_dremark;
        }

        public String getOri_no() {
            return ori_no;
        }

        public void setOri_no(String ori_no) {
            this.ori_no = ori_no;
        }

        public String getOri_line_no() {
            return ori_line_no;
        }

        public void setOri_line_no(String ori_line_no) {
            this.ori_line_no = ori_line_no;
        }
    }
}

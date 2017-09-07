package com.hjnerp.model;

import java.io.Serializable;
import java.util.List;

public class PerformanceDatas implements Serializable {
    private static final long serialVersionUID = -6367326250274309223L;

    /**
     * main : {"date_audit":"","date_eva":"2016-12-13 14:48:41","dec_cmark":"0","fiscal_period":"11","fiscal_year":"2016","id_audit":"","name_user":"叶嗣银","id_bflow":" ","id_com":"CM1092-0003","id_evapsn":" ","name_clerk":"王双田","id_flow":"385","id_kpigrade":"","var_evares":" ","var_remark":" ","dkpipost_no":"KPI201611000245","id_cgroup":"2300 ","name_cgroup":"移动事业部","id_clerk":"011040","id_dept":"020","name_dept":"移动事业部","id_post":"04109","name_post":"ERP实施顾问","int_year":"2016","id_kpiperiod":"12","name_kpiperiod":"十二月份","id_kpitype":"001","name_kpitype":"员工","dec_emark":"0","dec_dmark":"0","dec_smark":"0","var_rejust":" ","id_supclerk":"016063","date_plan":"2016-12-06 14:48:41","id_kpiphase":" ","name_kpiphase":"","flag_sts":"S","name_flagsts":"送审","id_recorder":"yesiyin","date_opr":"2016-12-13 14:48:27"}
     * details : [{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"1","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"看看咯","dec_scale":"0.8","var_evastd":"兔兔","var_comp":"呜呜呜呜","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"2","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"学以致用","dec_scale":"0.2","var_evastd":"YY我","var_comp":"我有我的","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"3","id_kpicate":"001","name_kpicate":"关键绩效指标(KPI)","id_kpipost":"呜呜呜呜","dec_scale":"0.6","var_evastd":"唐我木","var_comp":"呜呜呜呜","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"4","id_kpicate":"001","name_kpicate":"关键绩效指标(KPI)","id_kpipost":"头像9都","dec_scale":"0.4","var_evastd":"YY我","var_comp":"林业厅","dec_mark":"0","var_dremark":" "}]
     */

    private MainBean main;
    private List<PerformanceBean> details;

    public MainBean getMain() {
        return main;
    }

    public void setMain(MainBean main) {
        this.main = main;
    }

    public List<PerformanceBean> getDetails() {
        return details;
    }

    public void setDetails(List<PerformanceBean> details) {
        this.details = details;
    }

    public static class MainBean implements Serializable {

        private static final long serialVersionUID = -4044199846825991920L;
        /**
         * date_audit :
         * date_eva : 2016-12-13 14:48:41
         * dec_cmark : 0
         * fiscal_period : 11
         * fiscal_year : 2016
         * id_audit :
         * name_user : 叶嗣银
         * id_bflow :
         * id_com : CM1092-0003
         * id_evapsn :
         * name_clerk : 王双田
         * id_flow : 385
         * id_kpigrade :
         * var_evares :
         * var_remark :
         * dkpipost_no : KPI201611000245
         * id_cgroup : 2300
         * name_cgroup : 移动事业部
         * id_clerk : 011040
         * id_dept : 020
         * name_dept : 移动事业部
         * id_post : 04109
         * name_post : ERP实施顾问
         * int_year : 2016
         * id_kpiperiod : 12
         * name_kpiperiod : 十二月份
         * id_kpitype : 001
         * name_kpitype : 员工
         * dec_emark : 0
         * dec_dmark : 0
         * dec_smark : 0
         * var_rejust :
         * id_supclerk : 016063
         * date_plan : 2016-12-06 14:48:41
         * id_kpiphase :
         * name_kpiphase :
         * flag_sts : S
         * name_flagsts : 送审
         * id_recorder : yesiyin
         * date_opr : 2016-12-13 14:48:27
         */

        private String date_audit;
        private String date_eva;
        private String dec_cmark;
        private String fiscal_period;
        private String fiscal_year;
        private String id_audit;
        private String name_user;
        private String id_bflow;
        private String id_com;
        private String id_evapsn;
        private String name_clerk;
        private String id_flow;
        private String id_kpigrade;
        private String var_evares;
        private String var_remark;
        private String dkpipost_no;
        private String id_cgroup;
        private String name_cgroup;
        private String id_clerk;
        private String id_dept;
        private String name_dept;
        private String id_post;
        private String name_post;
        private String int_year;
        private String id_kpiperiod;
        private String name_kpiperiod;
        private String id_kpitype;
        private String name_kpitype;
        private String dec_emark;
        private String dec_dmark;
        private String dec_smark;
        private String var_rejust;
        private String id_supclerk;
        private String date_plan;
        private String id_kpiphase;
        private String name_kpiphase;
        private String flag_sts;
        private String name_flagsts;
        private String id_recorder;
        private String date_opr;
        private Boolean if_show;
        //出差申请新增字段
        private String dgtdout_no;
        private String remark;
        private String id_proj;
        private String name_proj;
        private String id_corr;
        private String name_corr;
        private String date_begin;
        private String date_end;
        private String date_fend;
        private String dec_samt;
        private String dec_pamt;

        //加班申请新增字段
        private String var_title;
        private String dgtdot_no;
        private String date_time;
        private String dec_ot;
        private String id_linem;
        //休假申请新增字段
        private String int_leave;
        private String int_off;
        private String dgtdvat_no;
        private String id_auditlvl;
        private String name_auditlvl;
        private String id_atttype;
        private String name_atttype;
        private String dec_leave;
        private String dec_no;
        //考勤异常新增字段
        private String dgtdabn_no;
        //销售订单录入新增字段
        private String dsaord_no;
        private String date_sign;
        private String id_ordtype;
        private String id_terminal;
        private String name_terminal;
        private String id_invtype;
        private String flag_itemwith;
        private String date_planinv;
        private String var_rcvcorr;
        private String var_addr;
        private String var_invaddr;
        private String var_tel;
        private String var_invtel;
        private String var_invcontact;
        private String var_contact;
        private String id_tax;
        private String id_seller;
        private String date_demand;
        private String id_express;
        private String dec_acaramt;
        private String dec_acclimit;

        public String getVar_invtel() {
            return var_invtel;
        }

        public void setVar_invtel(String var_invtel) {
            this.var_invtel = var_invtel;
        }

        public String getVar_invcontact() {
            return var_invcontact;
        }

        public void setVar_invcontact(String var_invcontact) {
            this.var_invcontact = var_invcontact;
        }

        public String getDec_acclimit() {
            return dec_acclimit;
        }

        public void setDec_acclimit(String dec_acclimit) {
            this.dec_acclimit = dec_acclimit;
        }

        public String getVar_invaddr() {
            return var_invaddr;
        }

        public void setVar_invaddr(String var_invaddr) {
            this.var_invaddr = var_invaddr;
        }

        public String getName_terminal() {
            return name_terminal;
        }

        public void setName_terminal(String name_terminal) {
            this.name_terminal = name_terminal;
        }

        public String getDec_acaramt() {
            return dec_acaramt;
        }

        public void setDec_acaramt(String dec_acaramt) {
            this.dec_acaramt = dec_acaramt;
        }

        public String getId_express() {
            return id_express;
        }

        public void setId_express(String id_express) {
            this.id_express = id_express;
        }

        public String getDate_demand() {
            return date_demand;
        }

        public void setDate_demand(String date_demand) {
            this.date_demand = date_demand;
        }

        public String getVar_contact() {
            return var_contact;
        }

        public void setVar_contact(String var_contact) {
            this.var_contact = var_contact;
        }

        public String getId_tax() {
            return id_tax;
        }

        public void setId_tax(String id_tax) {
            this.id_tax = id_tax;
        }

        public String getId_seller() {
            return id_seller;
        }

        public void setId_seller(String id_seller) {
            this.id_seller = id_seller;
        }

        public String getVar_tel() {
            return var_tel;
        }

        public void setVar_tel(String var_tel) {
            this.var_tel = var_tel;
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

        public String getId_invtype() {
            return id_invtype;
        }

        public void setId_invtype(String id_invtype) {
            this.id_invtype = id_invtype;
        }

        public String getId_terminal() {
            return id_terminal;
        }

        public void setId_terminal(String id_terminal) {
            this.id_terminal = id_terminal;
        }

        public String getId_ordtype() {
            return id_ordtype;
        }

        public void setId_ordtype(String id_ordtype) {
            this.id_ordtype = id_ordtype;
        }

        public String getDate_sign() {
            return date_sign;
        }

        public void setDate_sign(String date_sign) {
            this.date_sign = date_sign;
        }

        public String getDsaord_no() {
            return dsaord_no;
        }

        public void setDsaord_no(String dsaord_no) {
            this.dsaord_no = dsaord_no;
        }

        public String getDgtdabn_no() {
            return dgtdabn_no;
        }

        public void setDgtdabn_no(String dgtdabn_no) {
            this.dgtdabn_no = dgtdabn_no;
        }

        public String getInt_leave() {
            return int_leave;
        }

        public void setInt_leave(String int_leave) {
            this.int_leave = int_leave;
        }

        public String getInt_off() {
            return int_off;
        }

        public void setInt_off(String int_off) {
            this.int_off = int_off;
        }

        public String getDgtdvat_no() {
            return dgtdvat_no;
        }

        public void setDgtdvat_no(String dgtdvat_no) {
            this.dgtdvat_no = dgtdvat_no;
        }

        public String getId_auditlvl() {
            return id_auditlvl;
        }

        public void setId_auditlvl(String id_auditlvl) {
            this.id_auditlvl = id_auditlvl;
        }

        public String getName_auditlvl() {
            return name_auditlvl;
        }

        public void setName_auditlvl(String name_auditlvl) {
            this.name_auditlvl = name_auditlvl;
        }

        public String getId_atttype() {
            return id_atttype;
        }

        public void setId_atttype(String id_atttype) {
            this.id_atttype = id_atttype;
        }

        public String getName_atttype() {
            return name_atttype;
        }

        public void setName_atttype(String name_atttype) {
            this.name_atttype = name_atttype;
        }

        public String getDec_leave() {
            return dec_leave;
        }

        public void setDec_leave(String dec_leave) {
            this.dec_leave = dec_leave;
        }

        public String getDec_no() {
            return dec_no;
        }

        public void setDec_no(String dec_no) {
            this.dec_no = dec_no;
        }


        public String getVar_clerk() {
            return var_clerk;
        }

        public void setVar_clerk(String var_clerk) {
            this.var_clerk = var_clerk;
        }

        private String var_clerk;

        public String getDgtdout_no() {
            return dgtdout_no;
        }

        public void setDgtdout_no(String dgtdout_no) {
            this.dgtdout_no = dgtdout_no;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getId_proj() {
            return id_proj;
        }

        public void setId_proj(String id_proj) {
            this.id_proj = id_proj;
        }

        public String getName_proj() {
            return name_proj;
        }

        public void setName_proj(String name_proj) {
            this.name_proj = name_proj;
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

        public String getDate_fend() {
            return date_fend;
        }

        public void setDate_fend(String date_fend) {
            this.date_fend = date_fend;
        }

        public String getDec_samt() {
            return dec_samt;
        }

        public void setDec_samt(String dec_samt) {
            this.dec_samt = dec_samt;
        }

        public String getDec_pamt() {
            return dec_pamt;
        }

        public void setDec_pamt(String dec_pamt) {
            this.dec_pamt = dec_pamt;
        }


        public Boolean getIf_show() {
            return if_show;
        }

        public void setIf_show(Boolean if_show) {
            this.if_show = if_show;
        }

        public String getDate_audit() {
            return date_audit;
        }

        public void setDate_audit(String date_audit) {
            this.date_audit = date_audit;
        }

        public String getDate_eva() {
            return date_eva;
        }

        public void setDate_eva(String date_eva) {
            this.date_eva = date_eva;
        }

        public String getDec_cmark() {
            return dec_cmark;
        }

        public void setDec_cmark(String dec_cmark) {
            this.dec_cmark = dec_cmark;
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

        public String getId_com() {
            return id_com;
        }

        public void setId_com(String id_com) {
            this.id_com = id_com;
        }

        public String getId_evapsn() {
            return id_evapsn;
        }

        public void setId_evapsn(String id_evapsn) {
            this.id_evapsn = id_evapsn;
        }

        public String getName_clerk() {
            return name_clerk;
        }

        public void setName_clerk(String name_clerk) {
            this.name_clerk = name_clerk;
        }

        public String getId_flow() {
            return id_flow;
        }

        public void setId_flow(String id_flow) {
            this.id_flow = id_flow;
        }

        public String getId_kpigrade() {
            return id_kpigrade;
        }

        public void setId_kpigrade(String id_kpigrade) {
            this.id_kpigrade = id_kpigrade;
        }

        public String getVar_evares() {
            return var_evares;
        }

        public void setVar_evares(String var_evares) {
            this.var_evares = var_evares;
        }

        public String getVar_remark() {
            return var_remark;
        }

        public void setVar_remark(String var_remark) {
            this.var_remark = var_remark;
        }

        public String getDkpipost_no() {
            return dkpipost_no;
        }

        public void setDkpipost_no(String dkpipost_no) {
            this.dkpipost_no = dkpipost_no;
        }

        public String getId_cgroup() {
            return id_cgroup;
        }

        public void setId_cgroup(String id_cgroup) {
            this.id_cgroup = id_cgroup;
        }

        public String getName_cgroup() {
            return name_cgroup;
        }

        public void setName_cgroup(String name_cgroup) {
            this.name_cgroup = name_cgroup;
        }

        public String getId_clerk() {
            return id_clerk;
        }

        public void setId_clerk(String id_clerk) {
            this.id_clerk = id_clerk;
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

        public String getId_post() {
            return id_post;
        }

        public void setId_post(String id_post) {
            this.id_post = id_post;
        }

        public String getName_post() {
            return name_post;
        }

        public void setName_post(String name_post) {
            this.name_post = name_post;
        }

        public String getInt_year() {
            return int_year;
        }

        public void setInt_year(String int_year) {
            this.int_year = int_year;
        }

        public String getId_kpiperiod() {
            return id_kpiperiod;
        }

        public void setId_kpiperiod(String id_kpiperiod) {
            this.id_kpiperiod = id_kpiperiod;
        }

        public String getName_kpiperiod() {
            return name_kpiperiod;
        }

        public void setName_kpiperiod(String name_kpiperiod) {
            this.name_kpiperiod = name_kpiperiod;
        }

        public String getId_kpitype() {
            return id_kpitype;
        }

        public void setId_kpitype(String id_kpitype) {
            this.id_kpitype = id_kpitype;
        }

        public String getName_kpitype() {
            return name_kpitype;
        }

        public void setName_kpitype(String name_kpitype) {
            this.name_kpitype = name_kpitype;
        }

        public String getDec_emark() {
            return dec_emark;
        }

        public void setDec_emark(String dec_emark) {
            this.dec_emark = dec_emark;
        }

        public String getDec_dmark() {
            return dec_dmark;
        }

        public void setDec_dmark(String dec_dmark) {
            this.dec_dmark = dec_dmark;
        }

        public String getDec_smark() {
            return dec_smark;
        }

        public void setDec_smark(String dec_smark) {
            this.dec_smark = dec_smark;
        }

        public String getVar_rejust() {
            return var_rejust;
        }

        public void setVar_rejust(String var_rejust) {
            this.var_rejust = var_rejust;
        }

        public String getId_supclerk() {
            return id_supclerk;
        }

        public void setId_supclerk(String id_supclerk) {
            this.id_supclerk = id_supclerk;
        }

        public String getDate_plan() {
            return date_plan;
        }

        public void setDate_plan(String date_plan) {
            this.date_plan = date_plan;
        }

        public String getId_kpiphase() {
            return id_kpiphase;
        }

        public void setId_kpiphase(String id_kpiphase) {
            this.id_kpiphase = id_kpiphase;
        }

        public String getName_kpiphase() {
            return name_kpiphase;
        }

        public void setName_kpiphase(String name_kpiphase) {
            this.name_kpiphase = name_kpiphase;
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

        public String getId_recorder() {
            return id_recorder;
        }

        public String getVar_title() {
            return var_title;
        }

        public void setVar_title(String var_title) {
            this.var_title = var_title;
        }

        public String getDgtdot_no() {
            return dgtdot_no;
        }

        public void setDgtdot_no(String dgtdot_no) {
            this.dgtdot_no = dgtdot_no;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public String getDec_ot() {
            return dec_ot;
        }

        public void setDec_ot(String dec_ot) {
            this.dec_ot = dec_ot;
        }

        public String getId_linem() {
            return id_linem;
        }

        public void setId_linem(String id_linem) {
            this.id_linem = id_linem;
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
    }

    @Override
    public String toString() {
        return "PerformanceDatas{" +
                "main=" + main +
                ", details=" + details +
                '}';
    }
}
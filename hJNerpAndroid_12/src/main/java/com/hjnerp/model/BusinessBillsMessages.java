package com.hjnerp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2016/12/16.
 */

public class BusinessBillsMessages implements Serializable{

    private static final long serialVersionUID = -7774292126283715865L;
    /**
     * flag : Y
     * message : 成功
     * columns : {"date_audit":"审核时间","date_eva":"评价日期",
     * "dec_cmark":"综合得分","dec_import":"已导入值","dec_stdmark":"标准分值","fiscal_period":"核算月","fiscal_year":"核算年","flag_psts":"中止标志","id_audit":"审核人","name_user":"录入人","id_bflow":"前流程","id_com":"单位代码","id_evapsn":"评价人","name_clerk":"直接主管","id_flow":"流程代码","id_kpigrade":"评价等级","id_table":"业务名","ori_line_no":"原始单行号","ori_no":"原始单号","var_evares":"评价意见","var_remark":"备注","dkpipost_no":"单据号","id_cgroup":"绩效组","name_cgroup":"绩效组","id_clerk":"被评人","id_dept":"部门","name_dept":"部门","id_post":"岗位","name_post":"岗位","int_year":"考核年","id_kpiperiod":"考评周期","name_kpiperiod":"考评周期","id_kpitype":"考核类型","name_kpitype":"考核类型","dec_emark":"个人得分","dec_dmark":"扣分","line_no":"行号","id_kpicate":"指标分类","name_kpicate":"指标分类","id_kpipost":"指标","dec_smark":"个人总分","dec_scale":"权重","var_rejust":"驳回意见","id_supclerk":"直接主管","date_plan":"计划签订日期","id_kpiphase":"考评阶段","name_kpiphase":"考评阶段","flag_sts":"审核标志","name_flagsts":"审核标志","var_evastd":"评价标准","id_recorder":"录入人","date_opr":"操作时间","var_comp":"完成情况","dec_mark":"得分","var_dremark":"备注"}
     * datas : [{"main":{"date_audit":"","date_eva":"2016-12-13 14:48:41","dec_cmark":"0","fiscal_period":"11","fiscal_year":"2016","id_audit":"","name_user":"叶嗣银","id_bflow":" ","id_com":"CM1092-0003","id_evapsn":" ","name_clerk":"王双田","id_flow":"385","id_kpigrade":"","var_evares":" ","var_remark":" ","dkpipost_no":"KPI201611000245","id_cgroup":"2300 ","name_cgroup":"移动事业部","id_clerk":"011040","id_dept":"020","name_dept":"移动事业部","id_post":"04109","name_post":"ERP实施顾问","int_year":"2016","id_kpiperiod":"12","name_kpiperiod":"十二月份","id_kpitype":"001","name_kpitype":"员工","dec_emark":"0","dec_dmark":"0","dec_smark":"0","var_rejust":" ","id_supclerk":"016063","date_plan":"2016-12-06 14:48:41","id_kpiphase":" ","name_kpiphase":"","flag_sts":"S","name_flagsts":"送审","id_recorder":"yesiyin","date_opr":"2016-12-13 14:48:27"},"details":[{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"1","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"看看咯","dec_scale":"0.8","var_evastd":"兔兔","var_comp":"呜呜呜呜","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"2","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"学以致用","dec_scale":"0.2","var_evastd":"YY我","var_comp":"我有我的","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"3","id_kpicate":"001","name_kpicate":"关键绩效指标(KPI)","id_kpipost":"呜呜呜呜","dec_scale":"0.6","var_evastd":"唐我木","var_comp":"呜呜呜呜","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"4","id_kpicate":"001","name_kpicate":"关键绩效指标(KPI)","id_kpipost":"头像9都","dec_scale":"0.4","var_evastd":"YY我","var_comp":"林业厅","dec_mark":"0","var_dremark":" "}]},{"main":{"date_audit":"","date_eva":"2016-12-13 14:57:21","dec_cmark":"0","fiscal_period":"11","fiscal_year":"2016","id_audit":"","name_user":"叶嗣银","id_bflow":" ","id_com":"CM1092-0003","id_evapsn":" ","name_clerk":"王双田","id_flow":"385","id_kpigrade":"","var_evares":" ","var_remark":" ","dkpipost_no":"KPI201611000246","id_cgroup":"2300 ","name_cgroup":"移动事业部","id_clerk":"011040","id_dept":"020","name_dept":"移动事业部","id_post":"04109","name_post":"ERP实施顾问","int_year":"2016","id_kpiperiod":"12","name_kpiperiod":"十二月份","id_kpitype":"001","name_kpitype":"员工","dec_emark":"0","dec_dmark":"0","dec_smark":"0","var_rejust":" ","id_supclerk":"016063","date_plan":"2016-12-13 14:57:21","id_kpiphase":" ","name_kpiphase":"","flag_sts":"S","name_flagsts":"送审","id_recorder":"yesiyin","date_opr":"2016-12-13 14:57:07"},"details":[{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"1","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"修改界面","dec_scale":"0.8","var_evastd":"界面是否修改完成","var_comp":"唱不日出","dec_mark":"0","var_dremark":"你说什么"},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"2","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"总体","dec_scale":"0.2","var_evastd":"相信我","var_comp":"我的","dec_mark":"0","var_dremark":"123"},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"3","id_kpicate":"001","name_kpicate":"关键绩效指标(KPI)","id_kpipost":"沃恩","dec_scale":"1","var_evastd":"你永远","var_comp":"8门呀","dec_mark":"0","var_dremark":" "}]},{"main":{"date_audit":"","date_eva":"2016-12-14 09:24:30","dec_cmark":"0","fiscal_period":"11","fiscal_year":"2016","id_audit":"","name_user":"叶嗣银","id_bflow":" ","id_com":"CM1092-0003","id_evapsn":" ","name_clerk":"王双田","id_flow":"385","id_kpigrade":"","var_evares":" ","var_remark":" ","dkpipost_no":"KPI201611000247","id_cgroup":"2300 ","name_cgroup":"移动事业部","id_clerk":"011040","id_dept":"020","name_dept":"移动事业部","id_post":"04109","name_post":"ERP实施顾问","int_year":"2016","id_kpiperiod":"12","name_kpiperiod":"十二月份","id_kpitype":"001","name_kpitype":"员工","dec_emark":"0","dec_dmark":"0","dec_smark":"0","var_rejust":" ","id_supclerk":"016063","date_plan":"2016-12-14 09:24:30","id_kpiphase":" ","name_kpiphase":"","flag_sts":"S","name_flagsts":"送审","id_recorder":"yesiyin","date_opr":"2016-12-14 09:24:15"},"details":[{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"1","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"数据文件呃呃保存","dec_scale":"0.5","var_evastd":"确保数据可以保存添加","var_comp":"可以保存绩效","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"2","id_kpicate":"002","name_kpicate":"工作目标(GS)","id_kpipost":"今天不知道可不可以完成","dec_scale":"0.5","var_evastd":"大致可以把实体类写一下","var_comp":"然后读取文件的内容","dec_mark":"0","var_dremark":" "},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"3","id_kpicate":"001","name_kpicate":"关键绩效指标(KPI)","id_kpipost":"希望了","dec_scale":"0.4","var_evastd":"妹子黄","var_comp":"黄你妹","dec_mark":"0","var_dremark":"有啥啊"},{"dec_import":"0","dec_stdmark":"0","flag_psts":" ","id_table":" ","ori_line_no":"0","ori_no":"            ","line_no":"4","id_kpicate":"001","name_kpicate":"关键绩效指标(KPI)","id_kpipost":"你想干嘛","dec_scale":"0.6","var_evastd":"不知道","var_comp":"什么轻快","dec_mark":"0","var_dremark":" "}]}]
     */

    private String flag;
    private String message;
    private ColumnsBean columns;
    private List<PerformanceDatas> datas;

    @Override
    public String toString() {
        return "BusinessBillsMessages{" +
                "flag='" + flag + '\'' +
                ", message='" + message + '\'' +
                ", columns=" + columns +
                ", datas=" + datas +
                '}';
    }

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

    public ColumnsBean getColumns() {
        return columns;
    }

    public void setColumns(ColumnsBean columns) {
        this.columns = columns;
    }

    public List<PerformanceDatas> getDatas() {
        return datas;
    }

    public void setDatas(List<PerformanceDatas> datas) {
        this.datas = datas;
    }

    public static class ColumnsBean  implements Serializable{
        private static final long serialVersionUID = -5393588099324914562L;
        /**
         * date_audit : 审核时间
         * date_eva : 评价日期
         * dec_cmark : 综合得分
         * dec_import : 已导入值
         * dec_stdmark : 标准分值
         * fiscal_period : 核算月
         * fiscal_year : 核算年
         * flag_psts : 中止标志
         * id_audit : 审核人
         * name_user : 录入人
         * id_bflow : 前流程
         * id_com : 单位代码
         * id_evapsn : 评价人
         * name_clerk : 直接主管
         * id_flow : 流程代码
         * id_kpigrade : 评价等级
         * id_table : 业务名
         * ori_line_no : 原始单行号
         * ori_no : 原始单号
         * var_evares : 评价意见
         * var_remark : 备注
         * dkpipost_no : 单据号
         * id_cgroup : 绩效组
         * name_cgroup : 绩效组
         * id_clerk : 被评人
         * id_dept : 部门
         * name_dept : 部门
         * id_post : 岗位
         * name_post : 岗位
         * int_year : 考核年
         * id_kpiperiod : 考评周期
         * name_kpiperiod : 考评周期
         * id_kpitype : 考核类型
         * name_kpitype : 考核类型
         * dec_emark : 个人得分
         * dec_dmark : 扣分
         * line_no : 行号
         * id_kpicate : 指标分类
         * name_kpicate : 指标分类
         * id_kpipost : 指标
         * dec_smark : 个人总分
         * dec_scale : 权重
         * var_rejust : 驳回意见
         * id_supclerk : 直接主管
         * date_plan : 计划签订日期
         * id_kpiphase : 考评阶段
         * name_kpiphase : 考评阶段
         * flag_sts : 审核标志
         * name_flagsts : 审核标志
         * var_evastd : 评价标准
         * id_recorder : 录入人
         * date_opr : 操作时间
         * var_comp : 完成情况
         * dec_mark : 得分
         * var_dremark : 备注
         */

        private String date_audit;
        private String date_eva;
        private String dec_cmark;
        private String dec_import;
        private String dec_stdmark;
        private String fiscal_period;
        private String fiscal_year;
        private String flag_psts;
        private String id_audit;
        private String name_user;
        private String id_bflow;
        private String id_com;
        private String id_evapsn;
        private String name_clerk;
        private String id_flow;
        private String id_kpigrade;
        private String id_table;
        private String ori_line_no;
        private String ori_no;
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
        private String line_no;
        private String id_kpicate;
        private String name_kpicate;
        private String id_kpipost;
        private String dec_smark;
        private String dec_scale;
        private String var_rejust;
        private String id_supclerk;
        private String date_plan;
        private String id_kpiphase;
        private String name_kpiphase;
        private String flag_sts;
        private String name_flagsts;
        private String var_evastd;
        private String id_recorder;
        private String date_opr;
        private String var_comp;
        private String dec_mark;
        private String var_dremark;

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

        public String getFlag_psts() {
            return flag_psts;
        }

        public void setFlag_psts(String flag_psts) {
            this.flag_psts = flag_psts;
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

        public String getDec_smark() {
            return dec_smark;
        }

        public void setDec_smark(String dec_smark) {
            this.dec_smark = dec_smark;
        }

        public String getDec_scale() {
            return dec_scale;
        }

        public void setDec_scale(String dec_scale) {
            this.dec_scale = dec_scale;
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

        public String getVar_evastd() {
            return var_evastd;
        }

        public void setVar_evastd(String var_evastd) {
            this.var_evastd = var_evastd;
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
}

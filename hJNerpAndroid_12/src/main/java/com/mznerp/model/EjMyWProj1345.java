package com.mznerp.model;

/**
 * Created by Admin on 2017/1/16.
 */

public class EjMyWProj1345 {


    /**
     * id_wproj : 20140305
     * name_wproj : 北京燕京啤酒股份有限公司MERP系统项目
     * id_projcate : 160
     * var_conno : HJBJ201403MERP01
     * id_corr : BJ07200
     * name_corr : 北京燕京啤酒股份有限公司
     * id_terminal : 2000336
     * name_terminal : 甘肃中商食品质量检验检测有限公司
     */

    private String id_wproj;
    private String name_wproj;
    private String id_projcate;
    private String var_conno;
    private String id_corr;
    private String name_corr;
    private String id_terminal;
    private String name_terminal;
    private String dec_acaramt;
    private String var_chkparm;
    private String dec_taxrate;
    //联系电话
    private String var_tel;

    public String getVar_tel() {
        return var_tel;
    }

    public void setVar_tel(String var_tel) {
        this.var_tel = var_tel;
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

    public String getDec_acaramt() {
        return dec_acaramt;
    }

    public void setDec_acaramt(String dec_acaramt) {
        this.dec_acaramt = dec_acaramt;
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

    public String getId_wproj() {
        return id_wproj;
    }

    public void setId_wproj(String id_wproj) {
        this.id_wproj = id_wproj;
    }

    public String getName_wproj() {
        return name_wproj;
    }

    public void setName_wproj(String name_wproj) {
        this.name_wproj = name_wproj;
    }

    public String getId_projcate() {
        return id_projcate;
    }

    public void setId_projcate(String id_projcate) {
        this.id_projcate = id_projcate;
    }

    public String getVar_conno() {
        return var_conno;
    }

    public void setVar_conno(String var_conno) {
        this.var_conno = var_conno;
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

    @Override
    public String toString() {
        return "EjMyWProj1345{" +
                "id_wproj='" + id_wproj + '\'' +
                ", name_wproj='" + name_wproj + '\'' +
                ", id_projcate='" + id_projcate + '\'' +
                ", var_conno='" + var_conno + '\'' +
                ", id_corr='" + id_corr + '\'' +
                ", name_corr='" + name_corr + '\'' +
                '}';
    }
}

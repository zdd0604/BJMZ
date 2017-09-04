package com.hjnerp.business.BusinessStringBuffer;

import com.hjnerp.common.Constant;

/**
 * Created by Admin on 2017/1/12.
 * 生成的字符串都存放在这里
 */

public class BusinessEJBuffer {


//    模板中数据上传到基础表的设置，必须要有如下红色字体的设置（其中id_clerk处为基础表在和交谈了中的主键字段）
//    var jss='{"tableid":"ctlm7162",
// "tabletype":"B",
// "opr":"SS",
// "no":
// "id_clerk",
// "userid":"'+formData.id_user+'",
// "comid":"'+formData.id_com+'",
// "data":[{"table": "ctlm7162", "where": "", "data": [';
//    jss += '{"column":"flag_checkin","value":"4","datatype":"varchar"},' ;
//    jss += '{"column":"date_checkin","value":"'+formData.date_location+'","datatype":"varchar"},' ;
//    jss += '{"column":"date_opr","value":"'+formData.date_location+'","datatype":"varchar"},' ;
//    jss += '{"column":"id_recorder","value":"'+formData.id_user+'","datatype":"varchar"},' ;
//    jss += '{"column":"var_equno","value":"'+formData.var_location+'","datatype":"varchar"},' ;
//    jss += '{"column":"card_no","value":"'+var_signin+'","datatype":"varchar"}' ;
//    jss += ']  }  ] }';


    public static String getSginBuffer(String userID, String companyID, String date_location,
                                       String flag_sginin, String id_clerk, String id_com, String id_recorder,
                                       String var_equno, String var_location, String name_photo) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"ctlm7162\",\"opr\":\"SS\",\"no\":\"\",\"userid\":\"" + userID + "\",\"menuid\":\"" +
                Constant.ID_MENU + "\",\"dealtype\":\"save\",\"comid\":\"" + companyID + "\",");
        stringBuffer.append("\"data\":[{\"table\": \"ctlm7162\",\"oprdetail\":\"N\",\"where\":\" \",");
        stringBuffer.append("\"data\":[{\"column\":\"card_no\",\"value\":\"\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"date_checkin\",\"value\":\"" + date_location + "\",\"datatype\":\"datetime\"}, ");
        stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + date_location + "\",\"datatype\":\"datetime\"}, ");
        stringBuffer.append("{\"column\":\"flag_checkin\",\"value\":\"" + 4 + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"flag_signin\",\"value\":\"" + flag_sginin + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"id_clerk\",\"value\":\"" + id_clerk + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"id_com\",\"value\":\"" + id_com + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_recorder + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"var_equno\",\"value\":\"" + var_equno + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"var_location\",\"value\":\"" + var_location + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append("{\"column\":\"name_photo\",\"value\":\"" + name_photo + "\",\"datatype\":\"varchar\"}]}]}");
        String str = stringBuffer.toString().trim();
        return str;
    }

    /**
     * @param userID
     * @param companyID
     * @param date_task
     * @param id_dept
     * @param flag_wadd
     * @param id_wtype
     * @param id_wproj
     * @param dec_wtime
     * @param var_wtitle
     * @param var_remark
     * @return 工作日志
     */
    public static String getDgtdrecBuffer(String menuid, String id_recorder, String userID,
                                          String companyID, String date_task,
                                          String id_dept, String flag_wadd, String id_wtype,
                                          String id_wproj, String dec_wtime, String var_wtitle,
                                          String var_remark, String name_wproj,
                                          String date_opr, int fiscal_period, int fiscal_year,
                                          String id_corr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"tableid\":\"dgtdrec\",\"opr\":\"SS\",\"no\": \"\",\"menuid\":\"" + menuid + "\",\"dealtype\":\"save\",\"userid\":\"" + userID + "\",\"comid\":\"" + companyID + "\"," +
                "\"data\":[{\"table\": \"dgtdrec_03\",\"oprdetail\":\"N\",\"where\": \"\", \"data\": [");
        stringBuffer.append("{\"column\":\"flag_sts\",\"value\":\"L\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_flow\",\"value\":\"FBdis\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"line_no\",\"value\":\"1\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"date_task\",\"value\":\"" + date_task + "\",\"datatype\":\"datetime\"},");
        stringBuffer.append("{\"column\":\"id_dept\",\"value\":\"" + id_dept + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"flag_wadd\",\"value\":\"" + flag_wadd + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append(" {\"column\":\"id_wtype\",\"value\":\"" + id_wtype + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"id_wproj\",\"value\":\"" + id_wproj + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"dec_wtime\",\"value\":\"" + dec_wtime + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"id_recorder\",\"value\":\"" + id_recorder + "\",\"datatype\":\"varchar\"}, ");
        stringBuffer.append(" {\"column\":\"var_wtitle\",\"value\":\"" + var_wtitle + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"var_remark\",\"value\":\"" + var_remark + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"date_opr\",\"value\":\"" + date_opr + "\",\"datatype\":\"datetime\"},");
        stringBuffer.append("{\"column\":\"fiscal_period\",\"value\":\"" + fiscal_period + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"fiscal_year\",\"value\":\"" + fiscal_year + "\",\"datatype\":\"int\"},");
        stringBuffer.append("{\"column\":\"id_corr\",\"value\":\"" + id_corr + "\",\"datatype\":\"varchar\"},");
        stringBuffer.append("{\"column\":\"name_wproj\",\"value\":\"" + name_wproj + "\",\"datatype\":\"varchar\"}");
        stringBuffer.append("]}]}");
        String str = stringBuffer.toString().trim();
        return str;
    }


}

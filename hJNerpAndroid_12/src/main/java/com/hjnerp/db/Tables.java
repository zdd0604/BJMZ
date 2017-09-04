package com.hjnerp.db;

import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Ctlm1346;
import com.hjnerp.model.Ctlm1347;

/**
 * constants of table name and column name which in sqlite
 *
 * @author John Kenrinus Lee
 */
public class Tables {

    /**
     * 注册信息配置表
     */
    public static final class ConfigTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_COM).append(" NVARCHAR NOT NULL PRIMARY KEY, ");
            sb.append(COL_NAME_COM).append(" NVARCHAR NOT NULL, ");
            sb.append(COL_ID_USER).append(" NVARCHAR, ");
            sb.append(COL_URL_HTTP).append(" NVARCHAR NOT NULL, ");
            sb.append(COL_URL_WEBSOCKET).append(" NVARCHAR NOT NULL, ");
            sb.append(COL_FLAG_MAP).append(" NVARCHAR NOT NULL, ");
            sb.append(COL_VAR_WORKTABLE).append(" NVARCHAR NOT NULL ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "config";
        /**
         * 组织代码
         */
        public static final String COL_ID_COM = "id_com";
        /**
         * 组织名称
         */
        public static final String COL_NAME_COM = "name_com";
        /**
         * 用户ID
         */
        public static final String COL_ID_USER = "id_user";
        /**
         * 服务器HTTP地址
         */
        public static final String COL_URL_HTTP = "url_http";
        /**
         * 服务器WebSocket地址
         */
        public static final String COL_URL_WEBSOCKET = "url_websocket";
        /**
         * 是否可以使用地图
         */
        public static final String COL_FLAG_MAP = "flag_map";
        /**
         * 工作流权限
         */
        public static final String COL_VAR_WORKTABLE = "var_worktable";
    }

    /**
     * 用户表
     */
    public static final class UserTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_USER)
                    .append(" VARCHAR(20) NOT NULL PRIMARY KEY, ");
            sb.append(COL_VAR_IMGUSER).append(" VARCHAR(255), ");
            sb.append(COL_VAR_MTEL).append(" VARCHAR(20), ");
            sb.append(COL_VAR_EMAIL).append(" VARCHAR(20), ");
            sb.append(COL_NAME_USER).append(" VARCHAR(60), ");
            sb.append(COL_VAR_PASSWORD).append(" VARCHAR(20), ");
            sb.append(COL_ID_COM).append(" VARCHAR(20), ");
            sb.append(COL_NAME_COM).append(" VARCHAR(60), ");
            sb.append(COL_ID_DEPT).append(" VARCHAR(20), ");
            sb.append(COL_NAME_DEPT).append(" VARCHAR(60), ");
            sb.append(COL_DATE_LOGIN)
                    .append(" DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')), ");
            sb.append(COL_VAR_SESSION).append(" VARCHAR(60), ");
            sb.append(COL_FLAG_AUTOLOGIN).append(" CHAR(1) ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "ctlm1005";
        /**
         * 用户代码
         */
        public static final String COL_ID_USER = "id_user";
        /**
         * 用户图片
         */
        public static final String COL_VAR_IMGUSER = "var_imguser";
        /**
         * 手机号
         */
        public static final String COL_VAR_MTEL = "var_mtel";
        /**
         * 用户邮箱
         */
        public static final String COL_VAR_EMAIL = "var_email";
        /**
         * 用户名称
         */
        public static final String COL_NAME_USER = "name_user";
        /**
         * 用户密码
         */
        public static final String COL_VAR_PASSWORD = "var_password";
        /**
         * 单位代码
         */
        public static final String COL_ID_COM = "id_com";
        /**
         * 单位名称
         */
        public static final String COL_NAME_COM = "name_com";
        /**
         * 部门代码
         */
        public static final String COL_ID_DEPT = "id_dept";
        /**
         * 部门名称
         */
        public static final String COL_NAME_DEPT = "name_dept";
        /** 在线状态 */
        // public static final String COL_ID_ONLINE = "id_online";
        /** 用户是否有效 */
        // public static final String COL_FLAG_INVALID = "flag_invalid";
        /** 设备编号 */
        // public static final String COL_VAR_EQUNO = "var_equno";
        /** 设备类型 */
        // public static final String COL_VAR_EQUTYPE = "var_equtype";
        /**
         * 最后登录时间
         */
        public static final String COL_DATE_LOGIN = "date_login";
        /**
         * 会话代码
         */
        public static final String COL_VAR_SESSION = "var_session";
        /**
         * 是否自动登录
         */
        public static final String COL_FLAG_AUTOLOGIN = "flag_autologin";
    }

    /**
     * 联系人表
     */
    public static final class ContactTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_FRIEND).append(
                    " VARCHAR(20) NOT NULL PRIMARY KEY, ");
            sb.append(COL_NAME_FRIEND).append(" VARCHAR(60), ");
            sb.append(COL_VAR_MTEL).append(" VARCHAR(20), ");
            sb.append(COL_ID_DEPT).append(" VARCHAR(20), ");
            sb.append(COL_NAME_DEPT).append(" VARCHAR(60), ");
            sb.append(COL_VAR_IMGFRIEND).append(" VARCHAR(255), ");
            sb.append(COL_VAR_EMAIL).append(" VARCHAR(100), ");
            sb.append(COL_FLAG).append(" CHAR(1) ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "ctlm1355";
        /** 联系人关联的用户代码 */
        // public static final String COL_ID_USER = "id_user";
        /**
         * 联系人代码
         */
        public static final String COL_ID_FRIEND = "id_friend";
        /**
         * 联系人名称
         */
        public static final String COL_NAME_FRIEND = "name_friend";
        /**
         * 联系人手机号码
         */
        public static final String COL_VAR_MTEL = "var_mtel";
        /** 联系人单位代码 */
        // public static final String COL_ID_COM = "id_com";
        /** 联系人单位名称 */
        // public static final String COL_NAME_COM = "name_com";
        /**
         * 联系人部门代码
         */
        public static final String COL_ID_DEPT = "id_dept";
        /**
         * 联系人部门名称
         */
        public static final String COL_NAME_DEPT = "name_dept";
        /**
         * 联系人头像
         */
        public static final String COL_VAR_IMGFRIEND = "var_imgfriend";
        /** 联系人在线状态 */
        // public static final String COL_ID_ONLINE = "id_online";
        /**
         * 联系人Email
         */
        public static final String COL_VAR_EMAIL = "var_email";
        /**
         * 'Y'为好友,'N'为非好友群成员
         */
        public static final String COL_FLAG = "flag";
    }
//TODO

    /**
     * 企业信表(企信明细表)
     */
    public static final class EnterpriseInfoTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_MESSAGE).append(
                    " VARCHAR(20) NOT NULL PRIMARY KEY, ");
            sb.append(COL_ID_GROUP).append(" VARCHAR(20) NOT NULL, ");
            sb.append(COL_ID_SENDFROM).append(" VARCHAR(20) NOT NULL, ");
            sb.append(COL_ID_SENDTO).append(" VARCHAR(20) NOT NULL, ");
            sb.append(COL_VAR_CONTENT).append(" TEXT, ");
            sb.append(COL_VAR_CONTYPE).append(" VARCHAR(20), ");
            sb.append(COL_FLAG_SEND).append(" CHAR(1), ");
            sb.append(COL_FLAG_READ).append(" CHAR(1), ");
            sb.append(COL_DATE_SEND).append(" VARCHAR(30), ");
            sb.append(COL_DATE_OPR).append(" VARCHAR(30), ");
            sb.append(COL_FILE_ID).append(" VARCHAR(200), ");
            sb.append(COL_ID_TYPE).append(" VARCHAR(20), ");
            sb.append(COL_ID_RECORDER).append(" VARCHAR(20), ");
            sb.append(COL_SCENE).append(" VARCHAR(20), ");
            sb.append(COL_FLAG_PALY).append(" CHAR(1) ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "ctlm1356";
        /**
         * 消息代码
         */
        public static final String COL_ID_MESSAGE = "id_message";
        /**
         * 消息分组代码
         */
        public static final String COL_ID_GROUP = "id_group";
        /**
         * 发送人代码
         */
        public static final String COL_ID_SENDFROM = "id_sendfrom";
        /**
         * 接收人代码
         */
        public static final String COL_ID_SENDTO = "id_sendto";
        /**
         * 消息内容
         */
        public static final String COL_VAR_CONTENT = "var_content";
        /**
         * 消息内容类型 GCHAT/CHAT
         */
        public static final String COL_VAR_CONTYPE = "var_contype";
        /**
         * 消息状态  发送中-ing；发送失败-fail；发送成功-success
         */
        public static final String COL_FLAG_SEND = "flag_send";
        /**
         * 消息发送时间
         */
        public static final String COL_DATE_SEND = "date_send";
        /**
         * 消息操作时间
         */
        public static final String COL_DATE_OPR = "date_opr";
        /**
         * 附件id
         */
        public static final String COL_FILE_ID = "id_file";
        /**
         * 场景   普通文本null  图片-pic;语音-audio;视频-media;文档-doc
         */
        public static final String COL_SCENE = "scene";
        /**
         * 消息是否已读
         */
        public static final String COL_FLAG_READ = "flag_read";
        /**
         * 消息内容类型 IQ/MSG  默认MSG
         */
        public static final String COL_ID_TYPE = "id_type";
        /**
         * 消息所属用户
         */
        public static final String COL_ID_RECORDER = "id_recorder";
        /**
         * 消息是否打开
         */
        public static final String COL_FLAG_PALY = "flag_paly";

    }

    /**
     * 群组信息表
     */
    public static final class GroupInfoTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_GROUP).append(
                    " VARCHAR(20) NOT NULL PRIMARY KEY, ");
            sb.append(COL_NAME_GROUP).append(" VARCHAR(60), ");
            sb.append(COL_VAR_GROUPIMAGE).append(" VARCHAR(255), ");
            sb.append(COL_ID_RECORDER).append(" VARCHAR(20), ");
            sb.append(COL_ID_GROUPTYPE).append(" VARCHAR(20) ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "ctlm1357";
        /**
         * 会话组代码
         */
        public static final String COL_ID_GROUP = "id_group";
        /**
         * 会话组名称
         */
        public static final String COL_NAME_GROUP = "name_group";
        /**
         * 会话组图片
         */
        public static final String COL_VAR_GROUPIMAGE = "var_groupimage";
        /**
         * 会话组类型
         */
        public static final String COL_ID_GROUPTYPE = "id_grouptype";
        /**
         * 会话组创建人
         */
        public static final String COL_ID_RECORDER = "id_recorder";
    }

    /**
     * 群组关系表
     */
    public static final class GroupRelationTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_GROUP).append(" VARCHAR(20) NOT NULL, ");
            sb.append(COL_ID_USER).append(" VARCHAR(255) NOT NULL, ");
            sb.append("PRIMARY KEY (").append(COL_ID_GROUP).append(", ")
                    .append(COL_ID_USER).append(") ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "ctlm1358";
        /**
         * 分组代码
         */
        public static final String COL_ID_GROUP = "id_group";
        /**
         * 参与会话人代码
         */
        public static final String COL_ID_USER = "id_user";
    }

    /**
     * @author haijian
     * 创建IQ表
     */

    /**
     * 群组关系表
     */
    public static final class IQTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(ID_IQ).append(" VARCHAR(20) NOT NULL, ");
            sb.append(VAR_VALUE).append(" VARCHAR(255) NOT NULL, ");
            sb.append(VAR_FLAG).append(" VARCHAR(20) NOT NULL, ");
            sb.append("PRIMARY KEY (").append(ID_IQ).append(", ")
                    .append(VAR_VALUE).append(") ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "HJ_T_IQ";
        /**
         * ID
         */
        public static final String ID_IQ = "id_IQ";
        /**
         * iq值
         */
        public static final String VAR_VALUE = "var_value";
        /**
         * 标识
         */
        public static final String VAR_FLAG = "var_flag";
    }
//TODO

    /**
     * 临时联系人表
     */
    public static final class TempContactTable {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_USER)
                    .append(" VARCHAR(20) NOT NULL PRIMARY KEY, ");
            sb.append(COL_VAR_IMGUSER).append(" VARCHAR(255), ");
            sb.append(COL_VAR_MTEL).append(" VARCHAR(20), ");
            sb.append(COL_VAR_EMAIL).append(" VARCHAR(20), ");
            sb.append(COL_NAME_USER).append(" VARCHAR(60), ");
            sb.append(COL_ID_COM).append(" VARCHAR(20), ");
            sb.append(COL_ID_DEPT).append(" VARCHAR(20), ");
            sb.append(COL_NAME_DEPT).append(" VARCHAR(60), ");
            sb.append(COL_VAR_EQUTYPE).append(" VARCHAR(20), ");
            sb.append(COL_VERFIFY_TYPE).append(" VARCHAR(20), ");
            sb.append(COL_VERFIFY_RESULT).append(" VARCHAR(20), ");
            sb.append(COL_FLAG_READ).append(" CHAR(1), ");
            sb.append(COL_VERFIFY_NOTE).append(" VARCHAR(60) ");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "ctlm13550";
        /**
         * 用户代码
         */
        public static final String COL_ID_USER = "id_user";
        /**
         * 用户图片
         */
        public static final String COL_VAR_IMGUSER = "var_imguser";
        /**
         * 手机号
         */
        public static final String COL_VAR_MTEL = "var_mtel";
        /**
         * 用户邮箱
         */
        public static final String COL_VAR_EMAIL = "var_email";
        /**
         * 用户名称
         */
        public static final String COL_NAME_USER = "name_user";
        /**
         * 单位代码
         */
        public static final String COL_ID_COM = "id_com";
        /**
         * 部门代码
         */
        public static final String COL_ID_DEPT = "id_dept";
        /**
         * 部门名称
         */
        public static final String COL_NAME_DEPT = "name_dept";
        /**
         * 设备类型
         */
        public static final String COL_VAR_EQUTYPE = "var_equtype";
        /**
         * 验证类型
         */
        public static final String COL_VERFIFY_TYPE = "verfify_type";
        /**
         * 验证结果
         */
        public static final String COL_VERFIFY_RESULT = "verfify_result";
        /**
         * 验证信息
         */
        public static final String COL_VERFIFY_NOTE = "verfify_note";
        /**
         * 临时联系人是否已被查看
         */
        public static final String COL_FLAG_READ = "flag_read";
    }

    // TODO 工作流列表
    public static final class WorkFlowList {
        public static final String getCreatedSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(WORK_TITLE).append(" NVARCHAR, ");
            sb.append(WORK_CONTENT).append(" NVARCHAR, ");
            sb.append(WORK_ATTACH).append(" NVARCHAR, ");
            sb.append(WORK_BILLTYPE).append(" NVARCHAR, ");
            sb.append(WORK_BILLNO).append(" NVARCHAR, ");
            sb.append(WORK_DATE).append(" NVARCHAR, ");
            sb.append(WORK_FLAGDEAL).append(" NVARCHAR, ");
            sb.append(WORK_OPTTYPE).append(" NVARCHAR, ");
            sb.append(WORK_USER).append(" NVARCHAR, ");
            sb.append("PRIMARY KEY (").append(WORK_BILLTYPE).append(", ")
                    .append(WORK_BILLNO).append(")");
            sb.append(") ");

            return sb.toString();
        }

        /**
         * work_list表名
         */
        public static final String NAME = "work_list";
        /**
         * 标头
         */
        public static final String WORK_TITLE = "work_title";
        /**
         * 简内容
         */
        public static final String WORK_CONTENT = "work_content";
        /**
         * 附件
         */
        public static final String WORK_ATTACH = "work_attach";
        /**
         * 单据类型
         */
        public static final String WORK_BILLTYPE = "work_billtype";
        /**
         * 单据号
         */
        public static final String WORK_BILLNO = "work_billno";
        /**
         * 单据时间
         */
        public static final String WORK_DATE = "work_date";
        /**
         * 已處理/未處理
         **/
        public static final String WORK_FLAGDEAL = "work_flagdeal";
        /**
         * 操作类型
         */
        public static final String WORK_OPTTYPE = "work_opttype";
        /**
         * 用户ID
         */
        public static final String WORK_USER = "work_user";
    }

    // TODO 业务菜单表
    public static final class BusinessMenu {
        public static final String getCreatedSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(IDMENU).append(" NVARCHAR NOT NULL PRIMARY KEY, ");
            sb.append(NAMEMENU).append(" NVARCHAR, ");
            sb.append(MODELWINDOW).append(" NVARCHAR, ");
            sb.append(VARPARM).append(" NVARCHAR, ");
            sb.append(VARPARM1).append(" NVARCHAR, ");
            sb.append(PICPATH).append(" NVARCHAR");
            sb.append(") ");

            return sb.toString();
        }

        /**
         * 表名
         */
        public static final String NAME = "menu";

        public static String IDMENU = "id_menu";
        public static String NAMEMENU = "name_menu";//汉字名称
        public static String MODELWINDOW = "model_window"; //服务器上此项菜单的最新版本号
        public static String VARPARM = "var_parm";//此菜单使用的模板文件名
        public static String VARPARM1 = "var_parm1";//分组
        public static String PICPATH = "pic_path";//图片url

    }

    //TODO 业务表——ctlm1345
    public static final class BusinessCtlm1345 {
        public static final String getCreatedSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(Ctlm1345.FLAG_DOWNLOAD).append(" NVARCHAR , ");
            sb.append(Ctlm1345.IDCOLUMN).append(" NVARCHAR, ");
            sb.append(Ctlm1345.IDCOM).append(" NVARCHAR, ");
            sb.append(Ctlm1345.IDRECORDER).append(" NVARCHAR, ");
            sb.append(Ctlm1345.LINENO).append(" NVARCHAR, ");
            sb.append(Ctlm1345.NAMECOLUMN).append(" NVARCHAR , ");
            sb.append(Ctlm1345.VARCONDITION).append(" NVARCHAR, ");
            sb.append(Ctlm1345.VARLATI).append(" NVARCHAR, ");
            sb.append(Ctlm1345.VARLONGI).append(" NVARCHAR, ");
//			sb.append(Ctlm1345.VAR_LOCATION).append(" NVARCHAR, ");
            sb.append(Ctlm1345.VARVALNAME).append(" NVARCHAR, ");
            sb.append(Ctlm1345.VARVALUE).append(" NVARCHAR, ");
            sb.append(Ctlm1345.IDTABLE).append(" NVARCHAR,");
            sb.append(Ctlm1345.VAR_IMAGE).append(" NVARCHAR,");
            sb.append("PRIMARY KEY (").append(Ctlm1345.IDTABLE).append(", ")
                    .append(Ctlm1345.LINENO).append(")");
            sb.append(") ");

            return sb.toString();
        }

        /**
         * 表名
         */
        public static final String NAME = "ctlm1345";
    }

    public static final class BusinessCtlm1346 {
        public static final String getCreatedSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(Ctlm1346.COL_ID_NODE).append(" NVARCHAR PRIMARY KEY, ");
            sb.append(Ctlm1346.COL_NAME_NODE).append(" NVARCHAR , ");
            sb.append(Ctlm1346.COL_ID_COM).append(" NVARCHAR , ");
            sb.append(Ctlm1346.COL_ID_RECORDER).append(" NVARCHAR , ");
            sb.append(Ctlm1346.COL_VAR_REMARK).append(" NVARCHAR , ");
            sb.append(Ctlm1346.COL_VAR_PATHS).append(" NVARCHAR , ");
            sb.append(Ctlm1346.COL_INT_VERSION).append(" NVARCHAR  ");
            sb.append(") ");
            return sb.toString();
        }

        /**
         * 表名
         */
        public static final String NAME = "ctlm1346";
    }

    //TODO 业务表——ctlm1347
    public static final class BusinessCtlm1347 {
        public static final String getCreatedSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");

            sb.append(Ctlm1347.IDRECORDER).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDCOM).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDNODE).append(" NVARCHAR , ");
            sb.append(Ctlm1347.NAMENODE).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARBILLNO).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDMODEL).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDSRCNODE).append(" NVARCHAR , ");
            sb.append(Ctlm1347.FLAGUPLOAD).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDPNODE).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDNODETYPE).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA1).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA2).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA3).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA4).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA5).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA6).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA7).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA8).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA9).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA10).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA11).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA12).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA13).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA14).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA15).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA16).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA17).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA18).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA19).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARDATA20).append(" NVARCHAR , ");
            sb.append(Ctlm1347.DATEOPR).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDVIEW).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARJSON).append(" NVARCHAR , ");
            sb.append(Ctlm1347.VARVERSION).append(" NVARCHAR , ");
            sb.append(Ctlm1347.IDTABLE).append(" NVARCHAR , ");
            sb.append(Ctlm1347.INTLINE).append(" INTEGER  ,");


            sb.append("PRIMARY KEY (").append(Ctlm1347.IDNODE).append(", ")
                    .append(Ctlm1347.IDCOM).append(", ")
                    .append(Ctlm1347.VARBILLNO)
            ;
            sb.append(") ");
            sb.append(") ");

            return sb.toString();
        }

        /**
         * 表名
         */
        public static final String NAME = "ctlm1347";
    }

    /**
     * Ctlm4203表, 作为业务模块数据同步时机的依据
     *
     * @author John Kenrinus Lee
     * @date 2014-9-5
     */
    public static final class BusinessCtlm4203 {
        public static final String getCreateSQLString() {
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(NAME).append(" ");
            sb.append("( ");
            sb.append(COL_ID_TABLE).append(
                    " NVARCHAR NOT NULL PRIMARY KEY, ");
            sb.append(COL_FLAG_DOWNLOAD_TYPE).append(" NVARCHAR, ");
            sb.append(COL_VAR_WHERE).append(" NVARCHAR");
            sb.append(") ");
            return sb.toString();
        }

        public static final String NAME = "ctlm4203";
        /**
         * 类型
         */
        public static final String COL_ID_TABLE = "id_table";
        /**
         * 下载类型标志
         */
        public static final String COL_FLAG_DOWNLOAD_TYPE = "flag_downtype";
        /**
         * 查询条件
         */
        public static final String COL_VAR_WHERE = "var_where";
    }

}

package com.hjnerp.common;

import android.os.Environment;

import com.hjnerp.business.activity.DsaordbaseJson;
import com.hjnerp.business.activity.SellOrderModel;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Ctlm7161;
import com.hjnerp.model.DdisplocatBean;
import com.hjnerp.model.Ej1345;
import com.hjnerp.model.EjWadd1345;
import com.hjnerp.model.HJAboutBean;
import com.hjnerp.model.PerformanceDatas;
import com.hjnerp.model.UserInfo;

import java.util.List;

public class Constant {

    public static final String ABOUT_HJ_URL = "http://183.81.182.6:8090/nerp/remark.html";

    /**
     * 所有的action的监听的必须要以"ACTION_"开头
     *
     */

    /**
     * 花名册有删除的ACTION和KEY
     */
    public static final String ROSTER_DELETED = "roster.deleted";
    public static final String ROSTER_DELETED_KEY = "roster.deleted.key";

    /**
     * 花名册有更新的ACTION和KEY
     */
    public static final String ROSTER_UPDATED = "roster.updated";
    public static final String ROSTER_UPDATED_KEY = "roster.updated.key";

    /**
     * 花名册有增加的ACTION和KEY
     */
    public static final String ROSTER_ADDED = "roster.added";
    public static final String ROSTER_ADDED_KEY = "roster.added.key";

    /**
     * 花名册中成员状态有改变的ACTION和KEY
     */
    public static final String ROSTER_PRESENCE_CHANGED = "roster.presence.changed";
    public static final String ROSTER_PRESENCE_CHANGED_KEY = "roster.presence.changed.key";

    /**
     * 收到好友邀请请求
     */
    public static final String ROSTER_SUBSCRIPTION = "roster.subscribe";
    public static final String ROSTER_SUB_FROM = "roster.subscribe.from";
    public static final String NOTICE_ID = "notice.id";

    public static final String NEW_MESSAGE_ACTION = "roster.newmessage";

    /**
     * 我的消息
     */
    public static final String MY_NEWS = "my.news";
    public static final String MY_NEWS_DATE = "my.news.date";

    /**
     * 企信
     */
    public static final String IM_NEWS = "im.news";
    public static final String IM_NEWS_DATE = "im.news.date";
    public static final String IM_GOUP_NEWS = "im.group.news";
    public static final String IM_GROUP_NEWS_DATE = "im.group.news.date";

    public static final String ERROR_SERVER_NO_RESPONDSE = "服务器未响应！";
    /**
     * 工作流
     */
    public static final String WORK_NEWS = "my.news";
    public static final String WORK_NEWS_DATE = "my.news.date";

    /**
     * 朋友及个人信息
     */
    public static final String FRIEND_READ = "my.friend";
    public static final String GROUP_READ = "my.group";
    /**
     * 服务器的配置
     */
    public static final String LOGIN_SET = "eap_login_set";// 登录设置
    public static final String USERNAME = "username";// 账户
    public static final String PASSWORD = "password";// 密码
    public static final String XMPP_HOST = "xmpp_host";// 地址
    public static final String XMPP_PORT = "xmpp_port";// 端口
    public static final String XMPP_SEIVICE_NAME = "xmpp_service_name";// 服务名
    public static final String IS_AUTOLOGIN = "isAutoLogin";// 是否自动登录
    public static final String IS_NOVISIBLE = "isNovisible";// 是否隐身
    public static final String IS_REMEMBER = "isRemember";// 是否记住账户密码
    public static final String IS_FIRSTSTART = "isFirstStart";// 是否首次启动
    /**
     * 登录提示
     */
    public static final int LOGIN_SECCESS = 0;// 成功
    public static final int HAS_NEW_VERSION = 1;// 发现新版本
    public static final int IS_NEW_VERSION = 2;// 当前版本为最新
    public static final int LOGIN_ERROR_ACCOUNT_PASS = 3;// 账号或者密码错误
    public static final int SERVER_UNAVAILABLE = 4;// 无法连接到服务器
    public static final int LOGIN_ERROR = 5;// 连接失败

    public static final int LOGIN_ANONYMOUS = 6;// 连接失败

    public static final String XMPP_CONNECTION_CLOSED = "xmpp_connection_closed";// 连接中断

    public static final String LOGIN = "login"; // 登录
    public static final String RELOGIN = "relogin"; // 重新登录

    /**
     * 好友列表 组名
     */
    public static final String ALL_FRIEND = "所有好友";// 所有好友
    public static final String NO_GROUP_FRIEND = "未分组好友";// 所有好友
    /**
     * 系统消息
     */
    public static final String ACTION_SYS_MSG = "action_sys_msg";// 消息类型关键字
    public static final String MSG_TYPE = "broadcast";// 消息类型关键字
    public static final String SYS_MSG = "sysMsg";// 系统消息关键字
    public static final String SYS_MSG_DIS = "系统消息";// 系统消息
    public static final String ADD_FRIEND_QEQUEST = "好友请求";// 系统消息关键字
    /**
     * 请求某个操作返回的状态值
     */
    public static final int SUCCESS = 0;// 存在
    public static final int FAIL = 1;// 不存在
    public static final int UNKNOWERROR = 2;// 出现莫名的错误.
    public static final int NETWORKERROR = 3;// 网络错误
    /***
     * 企业通讯录根据用户ｉｄ和用户名去查找人员中的请求ｘｍｌ是否包含自组织
     */
    public static final int containsZz = 0;
    /***
     * 创建请求分组联系人列表xml分页参数
     */
    public static final String currentpage = "1";// 当前第几页
    public static final String pagesize = "1000";// 当前页的条数

    /***
     * 创建请求xml操作类型
     */
    public static final String add = "00";// 增加
    public static final String rename = "01";// 增加
    public static final String remove = "02";// 增加

    /**
     * 重连接
     */
    /**
     * 重连接状态acttion
     */
    public static final String ACTION_RECONNECT_STATE = "action_reconnect_state";
    /**
     * 描述冲连接状态的关机子，寄放的intent的关键字
     */
    public static final String RECONNECT_STATE = "reconnect_state";
    /**
     * 描述冲连接，
     */
    public static final boolean RECONNECT_STATE_SUCCESS = true;
    public static final boolean RECONNECT_STATE_FAIL = false;
    /**
     * 是否在线的SharedPreferences名称
     */
    public static final String PREFENCE_USER_STATE = "prefence_user_state";
    public static final String IS_ONLINE = "is_online";
    /**
     * 精确到毫秒
     */
    public static final String MS_FORMART = "yyyy-MM-dd HH:mm:ss SSS";
    /**
     * 签到时间
     */
    public static final String SGIN_FORMART = "yyyy-MM-dd HH:mm:ss";

    public static final String SGIN_PHOTONAME = "yyyyMMddHHmmss";

    public static final String TODAY = "yyyy/MM/dd";

    public static final String NOWTIME = "yyyy-MM-dd HH:mm:ss";
    public static final String NOWTIME2 = " HH:mm:ss";


    /*广播，通知imfrgmeng更新ui*/
    public static final String ACTION_HAVENEWMSG_REFLASHUI = "action_havenewmsg_reflashui";
    /*群已被解散*/
    public static final String ACTION_DROP_GROUP = "action_drop_group";
    /*增加群成员*/
    public static final String ACTION_ADD_GROUP_MEMBER = "action_add_group_member";
    /*删除群成员*/
    public static final String ACTION_DELETE_GROUP_MEMBER = "action_delete_group_member";
    /*被群主删除*/
    public static final String ACTION_REMOVED_GROUP_MEMBER = "action_removed_group_member";
    /*被好友解除好友关系*/
    public static final String ACTION_FRIEND_DELETE_ME = "action_friend_delete_me";
    /*有新联系人*/
    public static final String ACTION_NEW_CONTACT_REFRESH = "action_new_contact_refresh";

    public static final String WF_TYPE_LIST = "list";//工作流单据
    public static final String WF_TYPE_DETAIL = "detail"; //单据详情
    public static final String WF_TYPE_PROCEDURE = "procedure"; //审批流程
    public static final String WF_TYPE_OPERATE = "operate"; //发送操作
    public static final String WF_TYPE_CATEGORY = "category"; //工单类型
    public static final String WF_TYPE_ATTACH = "download_attachment";//下载附件
    /**
     * 通过
     */
    public static final String WF_OP_AGREE = "send";
    /**
     * 驳回上一步
     */
    public static final String WF_OP_REJEST_ONE = "rejest";
    /**
     * 驳回新建人
     */
    public static final String WF_OP_REJEST_OWNER = "rejestFirst";
    /**
     * 收回
     */
    public static final String WF_OP_REVOKE = "revoke";

    public static final String BUSINESS_SERVICE_ADDRESS = "/servlet/businessMobileServlet";
    public static final String NBUSINESS_SERVICE_ADDRESS = "/servlet/nbusinessMobileServlet";

    public static final String BM_ACTION_TYPE = "action_type";
    public static final String BM_ID_TABLE = "id_table";
    public static final String BM_IS_BASE_TABLE = "isBaseTable";
    public static final String BM_CONDITION = "condition";
    public static final String BM_NODE_TYPE = "node_type";
    public static final String BM_ROOT_NODE = "root_node";
    public static final String BM_BILL_NO = "bill_no";
    public static final String BM_ID_NODE = "id_node";
    public static final String BM_CURR_PAGE = "curr_page";
    public static final String BM_INT_VERSION = "int_version";
    public static final String BM_PAGE_SIZE = "page_size";

    /**
     * 移动端业务模块一进去tile界面
     */
    public static final String BMTYPE_BUSINESS_MENU = "MobileBusinessMenu";
    /**
     * 移动端数据同步中下载按钮点击动作
     */
    public static final String BMTYPE_SYNC_DATA_DOWNLOAD = "MobileSyncDataDownload";
    /**
     * 移动端下拉刷新做单据压缩请求,准备下载
     */
    public static final String BMTYPE_INPUT_DATA_DOWNLOAD = "MobiledInputDataDownload";
    /**
     * 移动端业务搜索功能
     */
    public static final String BMTYPE_DATA_QUERY = "MobiledDataQuery";
    /**
     * 移动端业务模板更新
     */
    public static final String BMTYPE_DOWNLOAD_TEMPLATE = "MobileDownloadTemplate";
    /**
     * 移动端ctlm1347下载
     */
    public static final String BMTYPE_CTML1347_DOWNLOAD = "MobileCtlm1347Download";
    /**
     * 移动端ctlm1347下载，手动下载应用数据
     */
    public static final String BMTYPE_CTML1347_DOWNLOAD_Again = "MobileCtlm1347DownloadAgain";

    /**
     * 业务数据上传状态
     */
    public static final int MSG_SHOW = 1;
    public static final int MSG_UPLOAD_SUCCESS = MSG_SHOW + 1;
    public static final int MSG_UPLOAD_FAILED = MSG_SHOW + 2;
    public static final int MSG_UPDATE_TEXT = MSG_SHOW + 3;

    /**
     * 聊天中发送的文件类型
     */
    public static final String FILE_TYPE_AUDIO = "aud";//语音
    public static final String FILE_TYPE_PIC = "pic";//图片
    public static final String FILE_TYPE_DOC = "doc";//文档
    public static final String FILE_TYPE_MIE = "mie";//视频
    public static final String FILE_TYPE_LOCATION = "loc";//位置

    /**
     * 聊天文本发送状态
     */
    public static final String MSG_SEND_STATUS_ING = "ing";
    public static final String MSG_SEND_STATUS_SUCCESS = "success";
    public static final String MSG_SEND_STATUS_FAIL = "fail";
    /**
     * 聊天附件发送状态
     */
    public static final String FILE_SEND_STATUS_FILE_SUCCESS = "file_send_success";
    public static final String FILE_SEND_STATUS_FILE_FAIL = "file_send_fail";
    public static final String FILE_SEND_STATUS_FILE_ING = "file_send_ing";

    /**
     * 业务拍照，照片保存路径
     */
    public static final String HJPHOTO_CACHE_DIR = Environment
            .getExternalStorageDirectory().toString() + "/HJNerpPicCache/pictures/";
    /**
     * 企信聊天，图片保存路径
     */
    public static final String CHAT_CACHE_DIR = Environment
            .getExternalStorageDirectory().toString() + "/HJNerpPicCache/chatpictures/";

    /**签到图片、文档保存地址*/
//	public static final String SGIN_SAVE_DIR = Environment
//			.getExternalStorageDirectory().toString() + "/HJNerpPicCache/sginInfo";
    /**
     * 签到图片、文档保存地址
     */
    public static final String SGIN_SAVE_DIR = Environment
            .getExternalStorageDirectory().toString() + "/HJNerpPicCache/sginInfo";

    public static final String TEMP_DIR = Environment
            .getExternalStorageDirectory().toString() + "/HJNerpPicCache/temp/";
    public static final String CHATAUDIO_DIR = Environment
            .getExternalStorageDirectory().toString() + "/HJNerpPicCache/chataudio/";
    public static final String SGIN_TODAY = "yyyy" + "年" + "MM" + "月" + "dd" + "日";
    public static final String SGIN_TIME = "HH:mm";
    public static final String kpiperiod_MM = "MM";
    public static final String Int_year_YYYY = "yyyy";


    public static Ej1345 ej1345;
    public static EjWadd1345 ejWadd1345;
    public static String ID_MENU;
    public static PerformanceDatas performanceDatas;
    public static List<Ctlm1345> ctlm1345List;
    public static String billsNo;
    public static UserInfo MYUSERINFO; // 最近登陆账户的个人信息
    //判断是新建还是保存 false保存数据中进去的  true是新建的数据
    public static Boolean JUDGE_TYPE = false;
    public static Boolean JUDGE_TYPE1 = false;//销售订单专用，检查是不是新的条目

    //判断按钮的状态
    public static String SEND_DEALTYPE = "send";
    public static String SAVE_DEALTYPE = "save";
    public static String REJUST_DEALTYPE = "rejust";

    //工作日志  的工作类型
    public static String id_wtype;
    public static String item_peoject;
    public static String item_client;
    public static String id_wproj;
    public static String id_corr;
    public static String dec_acaramt;

    /**
     * 考勤签到
     */
    public static DdisplocatBean mDdisplocatBean = null;
    public static Ctlm7161 ctlm7161;
    public static int sginType;
    public static int outType;
    public static boolean ctlm7161Is = false;

    /**
     * 出差外出
     */
    public static boolean travel;
    public static int project_type = 0;
    public static String user_myid = "";
    public static List<DsaordbaseJson> dsaordbaseJsons_new;
    public static List<SellOrderModel> sellDetails;
    public static int sellDetailsPosition;
    public static String id_terminal_for_item;//靠这个查询产品
    public static String id_terminal_for_address;//靠这个查询地址
    //    public static int selectAddressPosition;//地址位置
//    public static List<String> orderAddress;
    //    public static DsaordbaseJson dsaordbaseJson;//产品搜索
    //判断表格类型
    public static int tab_type = 0;

    //模板的名称
    //1、东兴堂及黄河的签到文件
    public static final String ddisplocatphohtml = "ddisplocatphohtml";
    //2、绩效计划录入
    public static final String dkpipostinputhtml = "dkpipostinputhtml";
    //3/EJ的考勤签到
    public static final String ddisplocatEJhtml = "ddisplocatEJhtml";
    //4、绩效计划审核
    public static final String dkpipostreviewhtml = "dkpipostreviewhtml";
    //5、绩效计划确认
    public static final String dkpipostconfimhtml = "dkpipostconfimhtml";
    //6、绩效完成情况自述
    public static final String dkpipostreadmehtml = "dkpipostreadmehtml";
    //7、绩效完成情况评价
    public static final String dkpipostratehtml = "dkpipostratehtml";
    //8、绩效完成情况自述
    public static final String dkpipostidentificatehtml = "dkpipostidentificatehtml";
    //9、工作日志
    public static final String dgtdrechtml = "dgtdrechtml";
    //10、出差外出
    public static final String dgtdouthtml = "dgtdouthtml";
    //11、休假申请
    public static final String dgtdvathtml = "dgtdvathtml";
    //12、加班申请
    public static final String dgtdothtml = "dgtdothtml";
    //13、考勤异常
    public static final String dgtdabnhtml = "dgtdabnhtml";
    //14、订单录入
    public static final String dsaordhtml = "dsaordhtml";
    //15、订单查询
    public static final String dsaordqueryhtml = "dsaordqueryhtml";
    //16、应收账龄
    public static final String accountagehtml = "accountagehtml";
    //17、日志查询
    public static final String dgtdrecvhtml = "dgtdrecvhtml";
    //18、订单明细
    public static final String dsaordquery2html = "dsaordquery2html";
    //19、客户应收
    public static final String accountage2html = "accountage2html";

    /**
     * 关于
     */
    public static List<HJAboutBean> HJbean;


    //Handler传递
    public static final int HANDLERTYPE_0 = 0;
    public static final int HANDLERTYPE_1 = 1;
    public static final int HANDLERTYPE_2 = 2;
    public static final int HANDLERTYPE_3 = 3;
    public static final int HANDLERTYPE_4 = 4;
    public static final int HANDLERTYPE_5 = 5;
    public static final int HANDLERTYPE_6 = 6;
    public static final int HANDLERTYPE_7 = 7;
    public static final int HANDLERTYPE_8 = 8;

    //bundle的key
    public static final String BUNDLE_TYPE = "BUNDLE_TYPE";
    public static final String BUNDLE_TYPE_0 = "0";
    public static final String BUNDLE_TYPE_1 = "1";
    public static final String BUNDLE_TYPE_2 = "2";
    public static final String BUNDLE_TYPE_3 = "3";
    public static final String BUNDLE_TYPE_4 = "4";
    public static final String BUNDLE_TYPE_5 = "5";
    public static final String BUNDLE_TYPE_6 = "6";


}

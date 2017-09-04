package com.hjnerp.net;

/**
 * 聊天功能常量
 * 
 * @author John Kenrinus Lee
 */
public class ChatConstants
{
	
	public static final int VALUE_NOTICE = 0;
	public static final int VALUE_LEFT_TEXT = 1;
	public static final int VALUE_LEFT_IMAGE = 2;
	public static final int VALUE_LEFT_AUDIO = 3;
	public static final int VALUE_RIGHT_TEXT = 4;
	public static final int VALUE_RIGHT_IMAGE = 5;
	public static final int VALUE_RIGHT_AUDIO = 6;
	public static final int VALUE_RIGHT_LOCATION = 7;//右边的位置
	public static final int VALUE_LEFT_LOCATION = 8;
	public static final int VALUE_RIGHT_FILE = 9;//右边的位置
	public static final int VALUE_LEFT_FILE = 10;
	
	/** 使用gzip压缩*/
	public static final String ENCODE_GZIP = "gzip";

	/**
	 * 
	 * 消息第一层属性为群聊/单聊，确定了第一层属性，群聊消息又有一个二层属性IQ/MSG，msg是普通的聊天消息，IQ是一些系统提示，例如：有人退出群。
	 *
	 */
	public static final class msg
	{
		/** 消息类型:单聊聊天, Msg的to为对方ID */
		public static final String TYPE_CHAT = "chat";
		/** 消息类型:群聊聊天, Msg的to为群ID */
		public static final String TYPE_GROUPCHAT = "groupchat";
		/** 消息类型:公告 */
		public static final String TYPE_SYS = "sys";
		
		/** 消息类型:IQ */
		public static final String TYPE_ID_IQ = "iq";
		/** 消息类型: MSG*/
		public static final String TYPE_ID_MSG = "msg";
	}

	public static final class iq
	{
		/** 系统所用的用户ID */
		public static final String USERID_SYSTEM = "system";
		/** 指令类型:请求 */
		public static final String TYPE_GET = "get";
		/** 指令类型:更改 */
		public static final String TYPE_SET = "set";
		/** 指令类型:执行成功 */
		public static final String TYPE_SUCCESS = "result";
		/** 指令类型:执行失败 */
		public static final String TYPE_ERROR = "error";
		/** 指令类型:收到聊天请求*/
		public static final String TYPE_CHAT = "chat";
 		/** 指令功能:登录 */
		public static final String FEATURE_LOGIN = "1001";
		/** 指令功能:退出 */
		public static final String FEATURE_LOGOUT = "1002";
		/** 指令功能:自动登录 */
		public static final String FEATURE_AUTO_LOGIN = "1003";
		/** 指令功能:客户端下线 */
		public static final String FEATURE_CLINET_OFFLINE = "1004";
		/** 指令功能:修改头像 */
		public static final String FEATURE_CHANGE_AVARTAR = "1005";
		/** 指令功能:请求联系人 */
		public static final String FEATURE_REQUEST_CONTACT = "1011";
		/** 指令功能:请求分组信息 */
		public static final String FEATURE_REQUEST_GROUP_INFO = "1012";
		/** 指令功能:请求分组关系 */
		public static final String FEATURE_REQUEST_GROUP_RELATION = "1013";
		/** 指令功能:请求未读信息 */
		public static final String FEATURE_REQUEST_UNREAD_MSG = "1014";
		/** 指令功能:推送 */
		public static final String FEATURE_PUSH = "2001";
		/** 指令功能:新版本推送 */
		public static final String FEATURE_PUSH_NEW_VERSION = "2002";
		/** 指令功能:创建群聊天室*/
		public static final String FEATURE_CREATE_GROUP_CHAT = "3001";
		/** 指令功能:代表操作:增删群成员,删除群/退出群*/
		public static final String FEATURE_OP_GROUP_CHAT = "3002";
		/** 指令功能:群信息修改*/
		public static final String FEATURE_MODIFY_GROUP_INFO = "3003";
		
		/**工作流权限设置**/
		public static final String FEATURE_WORK_FLOW = "6001";
		
		/**工作流读数**/
		public static final String FEATURE_WORK_NUMBER = "6002";
		/**工作流推送**/
		public static final String FEATURE_WORK_PUSH = "6003";
		/** 指令功能:添加联系人*/
		public static final String FEATURE_ADD_CONTACT = "8001";
		/** 指令功能:删除联系人*/
		public static final String FEATURE_REMOVE_CONTACT = "8004";
		/** 指令功能:被邀请的联系人同意添加联系人*/
		public static final String FEATURE_CONTACT_AGREE = "8002";
		/** 指令功能:被邀请的联系人拒绝添加联系人*/
		public static final String FEATURE_CONTACT_REFUSE = "8003";
		/** 指令功能:查询公司用户信息*/
		public static final String FEATURE_SEARCH_USER = "9001";
		/** 指令功能:查询任意群信息和关系*/
		public static final String FEATURE_SEARCH_GROUP = "9002";
		/** 指令功能:查询用户信息*/
		public static final String FEATURE_SEARCH_USERINFO = "9003";
		/** 推送分类:新版本下载 */
		public static final String MODULE_CODE_NEW_VERSION = "001";
		/** 推送分类:新工作单数量 */
		public static final String MODULE_CODE_WORKSHEET_COUNT = "002";
		/** 推送分类:业务数据推送 */
		public static final String MODULE_CODE_BUSINESS_DATA = "003";
		/** 推送分类:新联系人 */
		public static final String MODULE_CODE_NEW_CONTACT = "004";
		/** 键名:用户ID */
		public static final String DATA_KEY_USER_ID = "user_id";
		/** 键名:用户名 */
		public static final String DATA_KEY_USER_NAME = "user_name";
		/** 键名:密码 */
		public static final String DATA_KEY_PASSWORD = "password";
		/** 键名:组织ID */
		public static final String DATA_KEY_COM_ID = "com_id";
		/** 键名:组织名称 */
		public static final String DATA_KEY_COM_NAME = "com_name";
		/** 键名:设备ID */
		public static final String DATA_KEY_EQU_ID = "equ_id";
		/** 键名:系统类型 */
		public static final String DATA_KEY_SYS_TYPE = "sys_type";
		/** 键名:应用版本 */
		public static final String DATA_KEY_APP_VERSION = "app_version";
		/** 键名:会话ID */
		public static final String DATA_KEY_SESSION_ID = "session_id";
		/** 键名:错误代码 */
		public static final String DATA_KEY_ERROR_CODE = "error_code";
		/** 键名:消息 */
		public static final String DATA_KEY_MSG = "msg";
		/** 键名:项目 */
		public static final String DATA_KEY_ITEMS = "items";
		/** 键名:朋友ID */
		public static final String DATA_KEY_FRIEND_ID = "friend_id";
		/** 键名:朋友名称 */
		public static final String DATA_KEY_FRIEND_NAME = "friend_name";
		/** 键名:手机号 */
		public static final String DATA_KEY_MOBILE_PHONE = "mobile_phone";
		/** 键名:部门ID */
		public static final String DATA_KEY_DEPT_ID = "dept_id";
		/** 键名:部门名称 */
		public static final String DATA_KEY_DEPT_NAME = "dept_name";
		/** 键名:图标路径 */
		public static final String DATA_KEY_AVATAR = "avatar";
		/** 键名:邮箱 */
		public static final String DATA_KEY_EMAIL = "email";
		/** 键名:群ID */
		public static final String DATA_KEY_GROUP_ID = "group_id";
		/** 键名:群名称 */
		public static final String DATA_KEY_GROUP_NAME = "group_name";
		/** 键名:群图标 */
		public static final String DATA_KEY_GROUP_ICON = "group_icon";
		/** 键名:群类型 */
		public static final String DATA_KEY_GROUP_TYPE = "group_type";
		/** 键名:群主*/
		public static final String DATA_KEY_GROUP_LORD = "group_lord";
		/** 键名:模块代码 */
		public static final String DATA_KEY_MODULE_CODE = "module_code";
		/** 键名:信息 */
		public static final String DATA_KEY_INFO = "info";
		/** 键名:ID*/
		public static final String DATA_KEY_ID = "id";
		/** 键名:版本URL */
		public static final String DATA_KEY_VERSION_URL = "version_url";
		/** 键名:群组所有成员ID*/
		public static final String DATA_KEY_GROUP_USER_IDS = "users";
		/** 键名:指令功能*/
		public static final String DATA_KEY_FEATURE = "feature";
		/** 键名:类型*/
		public static final String DATA_KEY_TYPE = "type";
		/** 键名:文件ID*/
		public static final String DATA_KEY_FILE_ID = "id_file";
		/** 键名:文件名*/
		public static final String DATA_KEY_FILE_NAME = "file_name";
		/** 键名:重置的新密码*/
		public static final String DATA_KEY_NEW_PASSWORD = "npassword";
		/** 键名:action类型*/
		public static final String DATA_KEY_ACTION_TYPE = "action_type";
		/** 键值:action类型*/
		public static final String DATA_VALUE_ACTION_MODIFYPASSWORD = "modify_password";
		public static final String DATA_VALUE_ACTION_MODIFYCONTENT= "modify_content";
		
		public static final String DATA_VALUE_ACTION_QUERYHISHORRYMESSAGE = "query_history_message";

		public static final String DATA_KEY_CONTENTTYPE = "content_type";//
		public static final String DATA_KEY_CONTENT = "content";
		public static final String DATA_KEY_PHONE = "phone";
		public static final String DATA_KEY_MAILBOX = "mailbox";


		/** 键名:范围*/
		public static final String DATA_KEY_SCOPE = "scope";
		/** 键值:小范围信息*/
		public static final String DATA_VALUE_SCOPE_BASE = "base";
		/** 键名:大范围信息(可能需要更多权限)*/
		public static final String DATA_VALUE_SCOPE_MORE = "more";
		/** 头字段名:下载状态*/
		public static final String HEAD_KEY_DOWNLOADABLE_STATUS = "Downloadable-Status";
		/** 头字段名:文件名*/
		public static final String HEAD_KEY_FILE_NAME = "File-Name";
		/** 键值:添加成员*/
		public static final String DATA_VALUE_ADD = "add";
		/** 键值:删除群*/
		public static final String DATA_VALUE_DROP = "drop";
		/** 键值:删除成员*/
		public static final String DATA_VALUE_REMOVE = "remove";
		/** 键值:退出群*/
		public static final String DATA_VALUE_EXIT = "exit";
		/** 键值:聊天图片*/
		public static final String DATA_VALUE_RES_TYPE_IM = "im";
		/** 键值:头像图片*/
		public static final String DATA_VALUE_RES_TYPE_ATTACH = "attach";
		/** 键值:视频或语音*/
		public static final String DATA_VALUE_RES_TYPE_VOICES = "voices";
		
		/** 键值:业务图片*/
		public static final String DATA_VALUE_RES_TYPE_BUSINESS = "business";
		/** 键值:菜单图片*/
		public static final String DATA_VALUE_RES_TYPE_MENU = "menu";
		/** 键值:修改名称, 用于群*/
		public static final String DATA_VALUE_MODIFY_NAME = "1";
		/** 键名:安全码,用于加密选项*/
		public static final String US_SAFE_CODE = "safe_code";
		/** 键值:安全码,代表不加密*/
		public static final int SAFE_CODE_UNENCRYPT = 0x0;
	}

	public static final class presence
	{

	}
	
	public static final class httpParm
	{
		/** 数据库连接异常 */
		public static final String HTTPSESSION = "10001";
		/** 数据库连接异常 */
		public static final String HTTPDATA = "data";
	}
	public static final class error_code
	{
		/** 数据库连接异常 */
		public static final String ERROR_CODE_DB_CONN = "10001";
		/** 数据库SQL执行异常 */
		public static final String ERROR_CODE_DB_SQL = "10002";
		/** 变量强制转换异常 */
		public static final String ERROR_CODE_ILLEGAL_CAST = "10003";
		/** 无法连接到服务器 */
		public static final String ERROR_CODE_SERVER_UNREACH = "10004";
		/** JSON解析异常 */
		public static final String ERROR_CODE_JSON_PARSE = "10005";
		/** 数据库操作异常 */
		public static final String ERROR_CODE_DB_OP = "10006";
		/** 请求参数错误 */
		public static final String ERROR_CODE_ILLEGAL_ARGUMENT = "20001";
		/** 用户名或密码错误 */
		public static final String ERROR_CODE_USERNAME_PASSWORD = "20101";
		/** 当前设备未授权 */
		public static final String ERROR_CODE_BAD_AUTH = "20102";
		/** SessionID无效 */
		public static final String ERROR_CODE_SESSION_INVALID = "20103";
		/** 重复登录错误 */
		public static final String ERROR_CODE_REPEAT_LOGIN = "20104";
		/** 没有操作权限 */
		public static final String ERROR_CODE_NO_PERMISSION = "20105";
		/** 注册码或验证码无效 */
		public static final String ERROR_CODE_REGIST_VERIFY = "20204";
		/** 联系人不存在*/
		public static final String ERROR_CODE_CONTACT_NOT_FOUND = "20106";
		/** 联系人已被添加*/
		public static final String ERROR_CODE_CONTACT_HAD_ADDED = "20107";
		/** 未曾有用户发过某操作请求给你 */
		public static final String ERROR_CODE_NO_REQUEST_FOR_YOU = "20108";
	}

	public static final class error_string
	{
		/** "error_code":"10001" */
		public static final String ERROR_STRING_DB_CONN = "数据库连接异常";
		/** "error_code":"10002" */
		public static final String ERROR_STRING_DB_SQL = "数据库SQL执行异常";
		/** "error_code":"10003" */
		public static final String ERROR_STRING_ILLEGAL_CAST = "变量强制转换异常";
		/** "error_code":"10004" */
		public static final String ERROR_STRING_SERVER_UNREACH = "无法连接到服务器";
		/** "error_code":"10005" */
		public static final String ERROR_STRING_JSON_PARSE = "JSON解析异常";
		/** "error_code":"10006" */
		public static final String ERROR_STRING_DB_OP = "数据库操作异常";
		/** "error_code":"20001" */
		public static final String ERROR_STRING_ILLEGAL_ARGUMENT = "请求参数错误";
		/** "error_code":"20101" */
		public static final String ERROR_STRING_USERNAME_PASSWORD = "用户名或密码错误";
		/** "error_code":"20102" */
		public static final String ERROR_STRING_BAD_AUTH = "当前设备未授权";
		/** "error_code":"20103" */
		public static final String ERROR_STRING_SESSION_INVALID = "密码已过期,请重新登录";
		/** "error_code":"20104" */
		public static final String ERROR_STRING_REPEAT_LOGIN = "重复登录错误";
		/** "error_code":"20105" */
		public static final String ERROR_STRING_NO_PERMISSION = "没有操作权限";
		/** "error_code":"20106" */
		public static final String ERROR_STRING_CONTACT_NOT_FOUND = "联系人不存在";
		/** "error_code":"20107" */
		public static final String ERROR_STRING_CONTACT_HAD_ADDED = "联系人已被添加";
		/** "error_code":"20108"  */
		public static final String ERROR_STRING_NO_REQUEST_FOR_YOU = "未曾有用户发过某操作请求给你";
		/** "error_code":"20204" */
		public static final String ERROR_STRING_REGIST_VERIFY = "注册码或验证码无效";
	}
	
	public static final class http
	{
		/** 获取头像的URL*/
		public static final String URLPATH_PULL_AVATAR = "/servlet/imageResourceServlet";
		/** 修改密码的URL*/
		public static final String URLPATH_CHANGE_PASSWD =  "/servlet/userSettingsServlet";
		/** HTTP主连接*/
		public static final String URLPATH_HJMOBILESERVLET =   "/servlet/hjMobileServlet";
		/** HTTP主连接*/
		public static final String URLPATH_HJMOBILEPUSHSERVLET =  "/servlet/hjMobilePushServlet";
	
	}
}

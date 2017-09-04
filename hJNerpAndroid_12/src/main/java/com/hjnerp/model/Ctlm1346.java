package com.hjnerp.model;

/*
 * 记录xml文件信息，一个xml文件对应一条数据
 */
public class Ctlm1346
{
	
	/** 节点ID */
	public static final String COL_ID_NODE = "id_node";
	/** 节点名称 */
	public static final String COL_NAME_NODE = "name_node";
	/** 组织代码 */
	public static final String COL_ID_COM = "id_com";
	/** 录入人ID */
	public static final String COL_ID_RECORDER = "id_recorder";
	/** 标记 */
	public static final String COL_VAR_REMARK = "var_remark";
	/** 路径(未使用) */
	public static final String COL_VAR_PATHS = "var_paths";
	/** 版本号 */
	public static final String COL_INT_VERSION = "int_version";
	
	public String id_node;
	public String name_node;
	public String id_com;
	public String id_recorder;
	public String var_remark;
	public String var_paths;
	public String int_version;
}

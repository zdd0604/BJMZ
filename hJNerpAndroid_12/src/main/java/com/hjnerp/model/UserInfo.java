package com.hjnerp.model;

import java.io.Serializable;

/**
 * 用户信息表Model
 * @author John Kenrinus Lee
 */
public class UserInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 用户代码 */
	public String userID;
	/** 用户图片 */
	public String userImage;
	/** 手机号 */
	public String phoneNumber;
	/** 用户邮箱 */
	public String email;
	/** 用户名称 */
	public String username;
	/** 用户密码 */
	public String password;
	/** 单位代码 */
	public String companyID;
	/** 单位名称 */
	public String companyName;
	/** 部门代码 */
	public String departmentID;
	/** 部门名称 */
	public String departmentName;
	/** 最后登录时间 */
	public String lastLoginTime;
	/** 会话代码 */
	public String sessionID;
	/** 是否自动登录 */
	public String isAutoLogin;
}

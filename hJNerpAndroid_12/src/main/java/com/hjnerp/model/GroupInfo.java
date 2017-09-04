package com.hjnerp.model;

import java.io.Serializable;

/**
 * 群组信息表Model
 * @author John Kenrinus Lee
 */
public class GroupInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** 群组ID*/
	public String groupID;
	/** 群组名称*/
	public String groupName;
	/** 群组头像*/
	public String groupImage;
	/** 群组类型*/
	public String groupType;
	/** 群主*/
	public String groupLord;
	
	@Override
	public String toString()
	{
		return "GroupInfo [groupID=" + groupID + ", groupName=" + groupName + ", groupImage="
				+ groupImage + ", groupType=" + groupType + ", groupLord=" + groupLord + "]";
	}
	
}

package com.hjnerp.model;

public class TempContactInfo extends FriendInfo
{
	private static final long serialVersionUID = 1L;
	private String comid;
	private String friendOS;
	
	public String getComid()
	{
		return comid;
	}
	public void setComid(String comid)
	{
		this.comid = comid;
	}
	public String getFriendOS()
	{
		return friendOS;
	}
	public void setFriendOS(String friendOS)
	{
		this.friendOS = friendOS;
	}
}

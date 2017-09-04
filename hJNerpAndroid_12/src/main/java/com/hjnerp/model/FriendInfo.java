package com.hjnerp.model;

import java.io.Serializable;

//import_location org.jivesoftware.smack.packet.RosterPacket;

public class FriendInfo implements Serializable {
	/**
	 * 将user保存在intent中时的key
	 */
	public static final String friendKey = "lovesong_user";
	protected String friendname;
	protected String friendid;
	protected String friendmtel;
	protected String friendmail;
	protected String deptid;
	protected String deptname;
	protected String friendimage;
	protected String frienddescribe;
	protected char sortLetter;

	public String getFriendname() {
		return friendname;
	}

	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}

	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public String getFriendmtel() {
		return friendmtel;
	}

	public void setFriendmtel(String friendmtel) {
		this.friendmtel = friendmtel;
	}

	public String getFriendmail() {
		return friendmail;
	}

	public void setFriendmail(String friendmail) {
		this.friendmail = friendmail;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getFriendimage() {
		return friendimage;
	}

	public void setFriendimage(String friendimage) {
		this.friendimage = friendimage;
	}

	public String getFrienddescribe() {
		return frienddescribe;
	}

	public void setFrienddescribe(String frienddescribe) {
		this.frienddescribe = frienddescribe;
	}
	
	public char getSortLetter() {
		return sortLetter;
	}

	public void setSortLetter(char sortLetter) {
		this.sortLetter = sortLetter;
	}
	
	@Override
	public String toString()
	{
		return "FriendInfo [friendid=" + friendid + ", friendname=" + friendname + ", friendmtel="
				+ friendmtel + ", friendmail=" + friendmail + ", deptid=" + deptid+ ", deptname="
				+ deptname + ", friendimage=" + friendimage + ", frienddescribe=" + frienddescribe + "]";
	}


}

package com.hjnerp.model;

import java.util.List;

public class DeptInfo {

	private String deptId;
	private String deptName;
	private int childCount;
	private String deptImage;
	private List<FriendInfo> deptFriendInfoList;// 对应大组的小组成员对象数组
	private int location;

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public String getDeptImage() {
		return deptImage;
	}

	public void setDeptImage(String deptImage) {
		this.deptImage = deptImage;
	}

	public List<FriendInfo> getDeptFriendInfoList() {
		return deptFriendInfoList;
	}

	public void setDeptChild(List<FriendInfo> deptChild) {
		this.deptFriendInfoList = deptChild;
	}

	public FriendInfo getFriendInfo(int index) {// 根据下标得到用户
		return deptFriendInfoList.get(index);
	}

}

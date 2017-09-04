package com.hjnerp.model;

import java.util.List;

public class BusinessInfo {
	private String businessGroupId;
	private String businessGroupName;
	private int childCount;
	private String businessGroupImage;
	private List<MenuContent> businessChild;// 对应大组的小组成员对象数组
	private int location;

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getbusinessGroupId() {
		return businessGroupId;
	}

	public void setbusinessGroupId(String businessGroupId) {
		this.businessGroupId = businessGroupId;
	}

	public String getbusinessGroupName() {
		return businessGroupName;
	}

	public void setbusinessGroupName(String businessGroupName) {
		this.businessGroupName = businessGroupName;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public String getbusinessGroupImage() {
		return businessGroupImage;
	}

	public void setbusinessGroupImage(String businessGroupImage) {
		this.businessGroupImage = businessGroupImage;
	}

	public List<MenuContent> getbusinessChild() {
		return businessChild;
	}

	public void setbusinessChild(List<MenuContent> businessChild) {
		this.businessChild = businessChild;
	}

	public MenuContent getChild(int index) {
		return businessChild.get(index);
	}
}

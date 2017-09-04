package com.hjnerp.business.activity;

import java.io.Serializable;

/**
 * 
 * @author 巫志英
 * 
 */
public class MapInfo implements Serializable {
	private static final long serialVersionUID = -758459502806858414L;
	/**
	 * 经度
	 */
	private double latitude;
	/**
	 * 纬度
	 */
	private double longitude;

	/**
	 * 商家名称
	 */
	private String name;
	/**
	 * 地址
	 */
	private String distance;
	/**
	 * 是否拜访
	 */
	private boolean checked;
	/**
	 * 拜访界面跳转需要传递的参数
	 */
	private String billno;
	private String values;
	private String nodeid;
	private String viewid2;
	private int row;
	

	
	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getViewid2() {
		return viewid2;
	}

	public void setViewid2(String viewid2) {
		this.viewid2 = viewid2;
	}

	public MapInfo() {
	}

	public MapInfo(double latitude, double longitude, String name,
			String distance, boolean checked, String viewid2, String billno,
			String nodeid, String values,int row) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;

		this.name = name;
		this.distance = distance;
		this.checked = checked;
		this.viewid2 = viewid2;
		this.billno = billno;
		this.nodeid = nodeid;
		this.values = values;
		this.row=row;
		

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}

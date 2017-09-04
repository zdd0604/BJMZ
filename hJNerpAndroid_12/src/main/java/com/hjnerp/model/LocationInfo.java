package com.hjnerp.model;

import java.io.Serializable;

import com.baidu.mapapi.search.core.PoiInfo;

public class LocationInfo implements Serializable {
	private PoiInfo info;
	private boolean selected;
	public PoiInfo getInfo() {
		return info;
	}
	public void setInfo(PoiInfo info) {
		this.info = info;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public LocationInfo(PoiInfo info, boolean selected) {
		super();
		this.info = info;
		this.selected = selected;
	}
	public LocationInfo() {
		super();
	}
	
}

package com.hjnerp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class NearResult implements Serializable {
	public Pint point;//经纬度坐标
	public String formatted_address;//标准地址
	public String business;//周围商业区
	public Address address;
	public ArrayList<NearBuild> pois;//周边建筑
}

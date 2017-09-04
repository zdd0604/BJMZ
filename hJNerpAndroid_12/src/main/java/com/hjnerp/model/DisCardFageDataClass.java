package com.hjnerp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zdd 地图信息序列化
 */
public class DisCardFageDataClass implements Parcelable {

	private String value;
	private String viewid2;
	private String billno;
	private String nodeid;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getViewid2() {
		return viewid2;
	}

	public void setViewid2(String viewid2) {
		this.viewid2 = viewid2;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.value);
		dest.writeString(this.viewid2);
		dest.writeString(this.billno);
		dest.writeString(this.nodeid);
	}

	public DisCardFageDataClass() {
	}

	protected DisCardFageDataClass(Parcel in) {
		this.value = in.readString();
		this.viewid2 = in.readString();
		this.billno = in.readString();
		this.nodeid = in.readString();
	}

	public static final Parcelable.Creator<DisCardFageDataClass> CREATOR = new Parcelable.Creator<DisCardFageDataClass>() {
		@Override
		public DisCardFageDataClass createFromParcel(Parcel source) {
			return new DisCardFageDataClass(source);
		}

		@Override
		public DisCardFageDataClass[] newArray(int size) {
			return new DisCardFageDataClass[size];
		}
	};

}

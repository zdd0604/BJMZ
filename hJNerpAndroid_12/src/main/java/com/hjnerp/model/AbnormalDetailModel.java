package com.hjnerp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zy on 2016/12/9.
 */

public class AbnormalDetailModel implements Parcelable,Comparable<AbnormalDetailModel>{
    private String id_schtype;
    private String var_on;
    private String var_date;
    private String var_off;

    protected AbnormalDetailModel(Parcel in) {
        id_schtype = in.readString();
        var_on = in.readString();
        var_date = in.readString();
        var_off = in.readString();
    }

    public static final Creator<AbnormalDetailModel> CREATOR = new Creator<AbnormalDetailModel>() {
        @Override
        public AbnormalDetailModel createFromParcel(Parcel in) {
            return new AbnormalDetailModel(in);
        }

        @Override
        public AbnormalDetailModel[] newArray(int size) {
            return new AbnormalDetailModel[size];
        }
    };

    public String getId_schtype() {
        return id_schtype;
    }

    public void setId_schtype(String id_schtype) {
        this.id_schtype = id_schtype;
    }

    public String getVar_on() {
        return var_on;
    }

    public void setVar_on(String var_on) {
        this.var_on = var_on;
    }

    public String getVar_date() {
        return var_date;
    }

    public void setVar_date(String var_date) {
        this.var_date = var_date;
    }

    public String getVar_off() {
        return var_off;
    }

    public void setVar_off(String var_off) {
        this.var_off = var_off;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_schtype);
        dest.writeString(var_on);
        dest.writeString(var_date);
        dest.writeString(var_off);
    }

    @Override
    public int compareTo(AbnormalDetailModel o) {

        return this.var_date.compareTo(o.var_date);
    }
}

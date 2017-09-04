package com.hjnerp.model;

import android.util.Log;

/**
 * Created by zy on 2016/12/6.
 */

public class LeaveType implements Comparable<LeaveType>{
    public String id_atttype;
    public String name_atttype;
    public String dec_no;

    public String getDec_no() {
        return dec_no;
    }

    public void setDec_no(String dec_no) {
        this.dec_no = dec_no;
    }



    public String getId_atttype() {
        return id_atttype;
    }

    public void setId_atttype(String id_atttype) {
        this.id_atttype = id_atttype;
    }

    public String getName_atttype() {
        return name_atttype;
    }

    public void setName_atttype(String name_atttype) {
        this.name_atttype = name_atttype;
    }

    @Override
    public int compareTo(LeaveType o) {
        try{
            Log.d("sort","ok");
            return Integer.parseInt(this.id_atttype)-Integer.parseInt(o.id_atttype);

        }catch (Exception e){
            Log.d("sort","wrong");
            return 0;
        }
//
    }
}

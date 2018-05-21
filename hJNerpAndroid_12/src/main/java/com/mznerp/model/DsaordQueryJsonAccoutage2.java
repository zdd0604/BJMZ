package com.mznerp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zy on 2017/6/13.
 */

public class DsaordQueryJsonAccoutage2 {

    /**
     * id_corr : 2000005
     * name_corr : 阿拉尔新农乳业有限责任公司
     * id_seller : 100043
     * name_seller : 王红艳
     * id_zone :
     * name_zone :
     * id_area : 21
     * name_area : 西北
     * 180-365天 : 0.00
     * 365天以上 : 0.00
     * 总欠款额 : 35500.00
     */

    private String id_corr;
    private String name_corr;
    private String id_seller;
    private String name_seller;
    private String id_zone;
    private String name_zone;
    private String id_area;
    private String name_area;
    @SerializedName("180-365天")
    private String _$180365天;
    @SerializedName("365天以上")
    private String _$365天以上;
    private String 总欠款额;

    public String getId_corr() {
        return id_corr;
    }

    public void setId_corr(String id_corr) {
        this.id_corr = id_corr;
    }

    public String getName_corr() {
        return name_corr;
    }

    public void setName_corr(String name_corr) {
        this.name_corr = name_corr;
    }

    public String getId_seller() {
        return id_seller;
    }

    public void setId_seller(String id_seller) {
        this.id_seller = id_seller;
    }

    public String getName_seller() {
        return name_seller;
    }

    public void setName_seller(String name_seller) {
        this.name_seller = name_seller;
    }

    public String getId_zone() {
        return id_zone;
    }

    public void setId_zone(String id_zone) {
        this.id_zone = id_zone;
    }

    public String getName_zone() {
        return name_zone;
    }

    public void setName_zone(String name_zone) {
        this.name_zone = name_zone;
    }

    public String getId_area() {
        return id_area;
    }

    public void setId_area(String id_area) {
        this.id_area = id_area;
    }

    public String getName_area() {
        return name_area;
    }

    public void setName_area(String name_area) {
        this.name_area = name_area;
    }

    public String get_$180365天() {
        return _$180365天;
    }

    public void set_$180365天(String _$180365天) {
        this._$180365天 = _$180365天;
    }

    public String get_$365天以上() {
        return _$365天以上;
    }

    public void set_$365天以上(String _$365天以上) {
        this._$365天以上 = _$365天以上;
    }

    public String get总欠款额() {
        return 总欠款额;
    }

    public void set总欠款额(String 总欠款额) {
        this.总欠款额 = 总欠款额;
    }
}

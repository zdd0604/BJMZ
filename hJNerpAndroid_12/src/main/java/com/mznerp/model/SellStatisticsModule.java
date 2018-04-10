package com.mznerp.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mznerp.util.Log;
import com.mznerp.util.StringUtil;

/**
 * Created by zhangdongdong on 2018/3/20.
 * 销售统计报表返回数据解析
 */

public class SellStatisticsModule implements Comparable<SellStatisticsModule> {

    /**
     * fiscal_year : 2018
     * fiscal_period : 2
     * dec_amt : 2280773.64
     * dec_lastamt : 0.00
     */

    private String fiscal_year;
    private String fiscal_period;
    private String dec_amt;
    private String dec_lastamt;

    public String getFiscal_year() {
        return fiscal_year;
    }

    public void setFiscal_year(String fiscal_year) {
        this.fiscal_year = fiscal_year;
    }

    public String getFiscal_period() {
        return fiscal_period;
    }

    public void setFiscal_period(String fiscal_period) {
        this.fiscal_period = fiscal_period;
    }

    public String getDec_amt() {
        return dec_amt;
    }

    public void setDec_amt(String dec_amt) {
        this.dec_amt = dec_amt;
    }

    public String getDec_lastamt() {
        return dec_lastamt;
    }

    public void setDec_lastamt(String dec_lastamt) {
        this.dec_lastamt = dec_lastamt;
    }


    @Override
    public int compareTo(SellStatisticsModule o) {
        if (StringUtil.isStrTrue(o.fiscal_period)) {
            int period1 = Integer.valueOf(o.fiscal_period);
            int period2 = Integer.valueOf(this.getFiscal_period());
            if (period1 < period2) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }
}

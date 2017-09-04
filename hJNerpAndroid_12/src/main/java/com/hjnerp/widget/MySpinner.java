package com.hjnerp.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by zy on 2017/6/12.
 */

public class MySpinner extends android.support.v7.widget.AppCompatSpinner {
    //定义一个选中位置默认选中-1
    private int currSelect=-1;

    //构造函数 一般选两个参数的就够了
    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //选中后currSelect设置为当前选中位置
    @Override
    public void setSelection(int position) {
        currSelect=position;
        super.setSelection(position);
    }
    @Override
    public void setSelection(int position, boolean animate) {
        currSelect=position;
        super.setSelection(position, animate);
    }
    //把默认的返回的position改为自定义的postion
    @Override
    public int getSelectedItemPosition() {
        return currSelect;
    }
}


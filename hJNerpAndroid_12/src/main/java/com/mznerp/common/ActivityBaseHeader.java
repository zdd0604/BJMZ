package com.mznerp.common;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.mznerp.R;
import com.mznerp.widget.MyToast;
import com.mznerp.widget.MyToast2;


/**
 * @author zhangdongdong
 *         返回上级菜单、中间标题、右边标题(默认隐藏)
 */
public class ActivityBaseHeader extends ActionBarWidgetActivity {
    protected Toolbar base_toolbar;
    protected TextView base_leftTv;
    protected TextView base_centerTv;
    protected TextView base_rightTv;
    protected TextView base_rightTv1;
    //传值
    protected Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.module_activity_base_header);

        init();
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View.inflate(this, layoutResID, (ViewGroup) findViewById(R.id.base_content));
    }


    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.base_toolbar));
        base_toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        base_leftTv = (TextView) findViewById(R.id.base_leftTv);
        base_centerTv = (TextView) findViewById(R.id.base_centerTv);
        base_rightTv = (TextView) findViewById(R.id.base_rightTv);
        base_rightTv1 = (TextView) findViewById(R.id.base_rightTv1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);


        //设置返回按钮图片
        setLeftTvImg(R.drawable.icon_action_back_img);

        base_leftTv.setOnClickListener(onClickListener);
        base_rightTv.setOnClickListener(onClickListener);
        base_rightTv1.setOnClickListener(onClickListener);


        if (mBundle == null )
        {
            mBundle = new Bundle();
        }

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           switch (v.getId()){
               case R.id.base_leftTv:
                   onLeftClick();
                   break;
               case R.id.base_rightTv:
                   onRightClick();
                   break;
               case R.id.base_rightTv1:
                   onRightClick();
                   break;
           }
        }
    };


    /**
     * 设置资源文件的图片
     * @param imgID
     */
    public void setTolbarResourcesDw(int imgID){
        base_toolbar.setBackground(BitmapUtils.getResourcesDw(context,imgID));
    }


    /**
     * 设置左边标题 图片适用于返回
     * @param imgID
     */
    protected void setLeftTvImg(int imgID) {
        Drawable drawable = getResources().getDrawable(imgID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        base_leftTv.setCompoundDrawables(drawable, null, null, null);//画在右边
        base_leftTv.setCompoundDrawablePadding(-5);
    }

    /**
     * 设置中间标题
     *
     * @param tvText
     */
    public void setBaseCenterTv(String tvText) {
        if (tvText != null) {
            if (base_centerTv != null) {
                base_centerTv.setText(tvText);
            }
        }
    }

    /**
     * 设置右边按钮是否显示
     * @param isShow
     */
    protected void setRightTvVISIBLE(boolean isShow){
        base_rightTv.setVisibility( isShow ? View.VISIBLE : View.GONE );
    }

    /**
     * 设置右边按钮1是否显示
     * @param isShow
     */
    protected void setRightTv1VISIBLE(boolean isShow){
        base_rightTv1.setVisibility( isShow ? View.VISIBLE : View.GONE );
    }

    /**
     * 左边标题的点击事件
     */
    protected void onLeftClick() {
        finish();
    }

    /**
     * 右标题点击事件
     */
    protected void onRightClick() {

    }
    /**
     * 右标题点击事件
     * 默认隐藏的
     */
    protected void onRightClick1() {

    }


    /**
     * 显示弹框
     * @param content
     */
    public void showMyToast(String content)
    {
        new MyToast2(context,content);
    }

}

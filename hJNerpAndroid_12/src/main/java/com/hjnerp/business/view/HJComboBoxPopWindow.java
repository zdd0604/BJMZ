package com.hjnerp.business.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;
import com.hjnerpandroid.R.color;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HJComboBoxPopWindow extends PopupWindow {
    String TAG = "WorkListBillTypeWindow";
    private View mMenuView;
    protected SharePreferenceUtil sputil;
    private List<Ctlm1345> ctlm1345List = new ArrayList<Ctlm1345>();
    private Context context;
    private RadioGroup radioGroup;
    private List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
    private String datasource;
    private String field;
    private String value;

    public static boolean ifAllTypeChecked = true;

    //	@SuppressLint("NewApi")
    public HJComboBoxPopWindow(final Context context,
                               OnCheckedChangeListener onCheckedChangeListener, String datasource, String field, String value) {
        super(context);
        this.context = context;
        this.datasource = datasource;
        this.field = field;
        this.value = value;
        sputil = SharePreferenceUtil.getInstance(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.worktype_window, null);

        radioGroup = (RadioGroup) mMenuView.findViewById(R.id.rbtn_worktype);

        int h = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        int w = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();

        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.mystyle);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(color.black);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 加载工作流信息
        // TODO 获取数据
        getData();

        if (ctlm1345List != null) {
            for (int i = 0; i < ctlm1345List.size(); i++) {
                RadioButton radio = new RadioButton(context);
                TextView textView = new TextView(context);
                textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
                textView.setBackgroundColor(Color.GRAY);
                textView.setPadding(80, 0, 0, 0);
                String json = ctlm1345List.get(i).getVar_value();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    String v = jsonObj.getString(field);

                    radio.setText(v);
                    radio.setTag(v);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                radio.setTextColor(context.getResources().getColor(
                        color.black));
                radio.setButtonDrawable(R.drawable.hjradiobutton); // 设置radiobutton
                // 图标

                radio.setPadding(40, 20, 0, 20);
                radioButtonList.add(radio);
                if (i != 0)
                    radioGroup.addView(textView);

                radioGroup.addView(radio);
            }
            setChecked();
            radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }


    public void setChecked() {
        if (!StringUtil.isNullOrEmpty(value)) {
            for (int i = 0; i < radioButtonList.size(); i++) {
                String tag = radioButtonList.get(i).getTag().toString();
                if (value.equalsIgnoreCase(tag)) {
                    radioButtonList.get(i).setChecked(true);
                } else {
                    radioButtonList.get(i).setChecked(false);
                }
            }
        }

    }


    //从sp中获取存储在本地的工作流类型,并手动加上“全部类型”这个选项
    private void getData() {

        ctlm1345List = BusinessBaseDao.getCTLM1345ByIdTable(datasource);
//		for(int i =0;i<ctlm1345List.size();i++){
//			Log.e(TAG,">>>>> " + ctlm1345List.get(i).toString());
//			
//		} 

    }

}


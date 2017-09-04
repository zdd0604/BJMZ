package com.hjnerp.activity.work;

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

import com.google.gson.Gson;
import com.hjnerpandroid.R;
import com.hjnerpandroid.R.color;
import com.hjnerp.model.WorkflowBillTypeData;
import com.hjnerp.model.WorkflowBillTypeInfo;
import com.hjnerp.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkListBillTypeWindow extends PopupWindow {
	String TAG = "WorkListBillTypeWindow";
	private View mMenuView;
	protected SharePreferenceUtil sputil;
	private List<WorkflowBillTypeInfo> billTypeList;
	private Context context;
	private RadioGroup radioGroup;
	private List<RadioButton> radioButtonList = new ArrayList<RadioButton>();

	public static boolean ifAllTypeChecked = true;

	// @SuppressLint("NewApi")
	public WorkListBillTypeWindow(final Activity context,
			OnCheckedChangeListener onCheckedChangeListener) {
		super(context);
		this.context = context;
		sputil = SharePreferenceUtil.getInstance(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.worktype_window, null);

        radioGroup = (RadioGroup) mMenuView.findViewById(R.id.rbtn_worktype);
//        radioGroup.setBackgroundResource(R.drawable.waitdialog_bkg);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();

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
		billTypeList = getData();

        if (billTypeList != null) {
            for (int i = 0; i < billTypeList.size(); i++) {
                RadioButton radio = new RadioButton(context);
                radio.setText(billTypeList.get(i).getName());
                radio.setTextColor(context.getResources().getColor(
                        color.black));
                radio.setButtonDrawable(R.drawable.hjradiobutton2); // 设置radiobutton
                // 图标
                radio.setTag(billTypeList.get(i).getId());
                radio.setPadding(40, 20, 0, 20);
                radio.setFocusable(true);
                radio.setId(100 * 9 + i);
                TextView textView = new TextView(context);
                textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
                textView.setBackgroundColor(Color.GRAY);
                textView.setPadding(80, 0, 0, 0);

                radioButtonList.add(radio);
                if (i != 0)
                    radioGroup.addView(textView);
                radioGroup.addView(radio);
            }
            radioGroup.setFocusable(false);
            setChecked();
            radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public void setChecked() {
        radioGroup.clearCheck();
        for (int i = 0; i < radioButtonList.size(); i++) {

            if (billTypeList.get(i).getIsChecked()) {
                radioButtonList.get(i).setChecked(true);
            } else {
                radioButtonList.get(i).setChecked(false);
            }
        }

	}

    // 从sp中获取存储在本地的工作流类型,并手动加上“全部类型”这个选项
    private List<WorkflowBillTypeInfo> getData() {
        List<WorkflowBillTypeInfo> items = new ArrayList<WorkflowBillTypeInfo>();
        WorkflowBillTypeInfo itemFirst = new WorkflowBillTypeInfo();
        itemFirst.setId(null);
        itemFirst.setName("全部类型");
        itemFirst.setId("allType");
        String msg = sputil.getWorkListBillType();

        if (!"".equalsIgnoreCase(msg)) {
            Gson gson = new Gson();
            WorkflowBillTypeData workflowData = gson.fromJson(msg, WorkflowBillTypeData.class);
            Collections.sort(workflowData.items);
            if (workflowData != null && workflowData.items != null) {
                int selecetdFalseCounts = 0;
                for (int i = 0; i < workflowData.items.size(); i++) {
                    if (workflowData.items.get(i).getIsChecked()) {
                        selecetdFalseCounts++;
                    }
                }
                if (selecetdFalseCounts == 0) {// 说明没有被选中的单据类型
                    itemFirst.setIsSelected(true);
                    itemFirst.setIsChecked(true);
                } else {
                    itemFirst.setIsSelected(false);
                    itemFirst.setIsChecked(false);
                }
                items.add(itemFirst);
                items.addAll(workflowData.items);

			} else {
				itemFirst.setIsSelected(true);
				itemFirst.setIsChecked(true);
				items.add(itemFirst);
			}
		}
		return items;
	}

}

package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerpandroid.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HJRadioButton extends LinearLayout implements HJViewInterface {
	private static final String TAG = "HJRadioButton";
	private TextView title;
	private RadioGroup content;
	private BusinessParam businessParam; // 当前界面上参数
	private WidgetClass items;// <HJRadioButton
	private ViewClass currentviewClass;// 当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;
	// private String[] billNoArray; //ToolBar有几个Button就生成几个bill_no
	private List<RadioButton> radioList = new ArrayList<RadioButton>();
	private Ctlm1347 datactlm1347 = new Ctlm1347();
	private String Data = "";

	public HJRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HJRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJRadioButton(Context context) {
		super(context);
		initView();
	}

	public HJRadioButton(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,
			BusinessParam param) {
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;
		initView();
	}

	public void saveDefaultCTLM1347() {
		if (currentviewClass.presave) {
			saveCTLM1347();
		}
	}

	public void saveCTLM1347() {
		datactlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		datactlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
 	 
		datactlm1347.setName_node(items.name);
		datactlm1347.setVar_billno(businessParam.getBillNo());
		datactlm1347.setId_model(startViewInfo.id);
		datactlm1347.setId_nodetype(WidgetName.HJ_RADIOBUTTON);
		//
		datactlm1347.setFlag_upload("N");
		datactlm1347.setVar_data1(Data);
		datactlm1347.setId_parentnode(businessParam.getIdParentNode());
		datactlm1347.setId_view(businessParam.getViewId());
		//
		if (StringUtil.isNullOrEmpty(datactlm1347.getId_node())) {
			datactlm1347.setId_node(StringUtil.getMyUUID());
		}

		datactlm1347.setInt_line(0);
		datactlm1347.setId_table(items.attribute.datasource);
		datactlm1347.setId_srcnode(items.id);
		datactlm1347.setDate_opr(businessParam.getDataOpr());
		BusinessBaseDao.replaceCTLM1347(datactlm1347);

	}

	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjradiobutton, null);
		title = (TextView) view.findViewById(R.id.hj_radiobutton_title);
		content = (RadioGroup) view.findViewById(R.id.hj_radiobutton_content);

		title.setText(items.name);
		if (items.attribute.singleline) {
			content.setOrientation(VERTICAL);
		} else {
			content.setOrientation(HORIZONTAL);
		}

		content.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) content
						.findViewById(radioButtonId);
				Data = rb.getTag().toString();
			}
		});

		for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {
			RadioButton radio = new RadioButton(getContext());
			radio.setText(items.HJRadioButtonOption.get(i).name);
			setRadionButtonAttribute(i, radio);
			radio.setTag(items.HJRadioButtonOption.get(i).defaultValue);
			content.addView(radio);
			radioList.add(radio);
		}

		addView(view);
		if (!items.attribute.visible) {
			this.setVisibility(View.GONE);
		}

	}

	private void setRadionButtonAttribute(int i, RadioButton button) {
		RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		button.setButtonDrawable(R.drawable.hjradiobutton); // 设置radiobutton 图标
		Paint paint = button.getPaint();
		paint.setFakeBoldText(false);

		// /加粗
		if ("medium"
				.equalsIgnoreCase(items.attribute.fontsize)) {
			title.setTextSize(18);
			button.setTextSize(18); 
		}

		// /加粗
		if ("large"
				.equalsIgnoreCase(items.attribute.fontsize)) {
			title.setTextSize(22);
			button.setTextSize(22); 
		}

		// /加粗
		if ("small"
				.equalsIgnoreCase(items.attribute.fontsize)) {
			title.setTextSize(14);
			button.setTextSize(14); 
		}
		title.getPaint().setFakeBoldText(
				items.attribute.bold);
		
		// /加粗
		if (items.HJRadioButtonOption.get(i).attribute.bold) {
			
			button.getPaint().setFakeBoldText(
					items.attribute.bold);
		}
		// /加粗
		if (!StringUtil
				.isNullOrEmpty(items.attribute.textcolor)) {
			button.getPaint()
					.setColor(
							Color.parseColor(items.attribute.textcolor));
		}

		button.setLayoutParams(lp);
	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		if (StringUtil.isNullOrEmpty(msg))
			return;
		for (int i = 0; i < radioList.size(); i++) {
			if (msg.equalsIgnoreCase(radioList.get(i).getTag().toString())) {
				radioList.get(i).setChecked(true);
			} else {
				radioList.get(i).setChecked(false);
			}
		}
	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
		if (StringUtil.isNullOrEmpty(items.defaultValue))
			return;
		setValue(items.defaultValue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setJesonValue(String jsonValue) {
		// TODO Auto-generated method stub

		Map<String, Object> map = null;
		Gson gson = new Gson();
		map = (Map<String, Object>) gson.fromJson(jsonValue, Object.class);
		if (map != null && !map.isEmpty()) {
			setValue(map.get(items.attribute.field).toString());// content.setText(
																// list.get(0).get(
																// items.attribute.field));
			datactlm1347.setvar_Json(jsonValue);
		}
	}

	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return items.attribute.datasource;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return items.id;
	}

	@Override
	public boolean getEditable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public void setDataBuild(Boolean flag, BusinessData ctlm1345List) {
		if (flag) {
			ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao.getCtlm1347List(
					startViewInfo.id, currentviewClass.id,
					businessParam.getIdParentNode(), businessParam.getBillNo(),
					items.id);

			if (ctlm1347List != null) {
				setValue(ctlm1347List);
			}
		} else if (ctlm1345List != null && !flag)

		{
			setValue(ctlm1345List);
		}
	}

	public void setValue(ArrayList<Ctlm1347> ctlm1347List) {
		for (int datasize = 0; datasize < ctlm1347List.size(); datasize++) {
			if (items.id.equalsIgnoreCase(ctlm1347List.get(datasize)
					.getId_srcnode())) {
				datactlm1347 = ctlm1347List.get(datasize);
				for (RadioButton rb : radioList) {
					if (rb.getTag().toString()
							.equalsIgnoreCase(datactlm1347.getVar_data1())) {
						rb.setChecked(true);
					}
				}
				break;
			}
		}
	}

	public void setValue(BusinessData ctlm1345List) {
		if (ctlm1345List.getCtlm1345().size() <= 0)
			return;
		datactlm1347.setvar_Json(ctlm1345List.getCtlm1345().get(0)
				.getVar_value());
		datactlm1347.setInt_line(0);
		datactlm1347.setId_node(StringUtil.getMyUUID());
		datactlm1347.setId_table(ctlm1345List.getId_table());
		// /设置显示值
		setJesonValue(ctlm1345List.getCtlm1345().get(0).getVar_value());
		// saveCTLM1347();
		// saveDefaultCTLM1347();
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		String val = "";
		 

		for (int i = 0; i < radioList.size(); i++) {
			if (radioList.get(i).isChecked()) {
				val = radioList.get(i).getTag().toString();
			}
		}

		return val;
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setLocation() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String setPhoto() {
		// TODO Auto-generated method stub

		return "";
	}

	@Override
	public int saveData(Boolean required) {
		// TODO Auto-generated method stub
		 
		saveDefaultCTLM1347(); 
		return 0;
	}

	@Override
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		String val = "";
 

		for (int i = 0; i < radioList.size(); i++) {
			if (radioList.get(i).isChecked()) {
				val = radioList.get(i).getTag().toString();
			}
		}

		if (items.attribute.required && StringUtil.isNullOrEmpty(val)) {

			ToastUtil.ShowShort(getContext(), items.name + "不能为空,请选择.");
			return false;

		}
		return true;
	}
}

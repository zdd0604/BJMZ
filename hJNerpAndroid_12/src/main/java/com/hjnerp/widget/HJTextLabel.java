package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

@SuppressLint("ResourceAsColor")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class HJTextLabel extends LinearLayout implements HJViewInterface {
	private static String TAG = "HJLabel";

	private Context context;
	private WidgetClass items;
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	private View view;
	private TextView content;
	private BusinessParam businessParam; // 当前界面上参数
	private Ctlm1347 datactlm1347 = new Ctlm1347();
	String data;

	public HJTextLabel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}

	public HJTextLabel(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public HJTextLabel(Context context) {
		super(context);
		this.context = context;

	}

	public HJTextLabel(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,
			BusinessParam param) {
		super(context);
		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;

		initView();
	}

	private void initView() {
		view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjlabel, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		addView(view);
		content = (TextView) view.findViewById(R.id.hjlabel);
		content.setText("");
		setAttribute(items);
		// saveDefaultCTLM1347();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setAttribute(WidgetClass item) {
		// 设置字体大小
		setHJLabelSize(item);
		// 设置居中
		setHJLabelGravity(item);
		// 设置加粗
		Paint paint = content.getPaint();
		paint.setFakeBoldText(item.attribute.bold);
		// 设置是否换行

		setHJLabelBackground(item);
		setHJLabelColor(item);
	}

	// 设置颜色
	public void setHJLabelColor(WidgetClass item) {

		if (item.attribute.textcolor != null
				&& !"".equalsIgnoreCase(item.attribute.textcolor)) {
			content.setTextColor(Color.parseColor(item.attribute.textcolor));
		}
	}

	// 设置位置
	public void setHJLabelGravity(WidgetClass item) {
		if ("left".equalsIgnoreCase(item.attribute.alignment)) {
			content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		}
		if ("center".equalsIgnoreCase(item.attribute.alignment)) {
			content.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		}
		if ("right".equalsIgnoreCase(item.attribute.alignment)) {
			content.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		}
	}

	// 设置HJLabel背景
	public void setHJLabelBackground(WidgetClass item) {

		if (item.attribute.backgroundcolor != null
				&& !"".equalsIgnoreCase(item.attribute.backgroundcolor)) {
			view.setBackgroundColor(Color
					.parseColor(item.attribute.backgroundcolor));
		}

	}

	// 设置字体大小
	public void setHJLabelSize(WidgetClass item) {

		if (getContext().getResources().getString(R.string.text_size_large)
				.equalsIgnoreCase(item.attribute.fontsize)) {
			content.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeLarge));
		} else if (getContext().getResources()
				.getString(R.string.text_size_medium)
				.equalsIgnoreCase(item.attribute.fontsize)) {
			content.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeMedium));
		} else {
			content.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeSmall));
		}
	}

	public void saveDefaultCTLM1347() {
		if (currentviewClass.presave) {
			saveCTLM1347();
		}
	}

	public void saveCTLM1347() {
		datactlm1347.setId_recorder(SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId());
		datactlm1347.setId_com(SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getComid());
		datactlm1347.setName_node(items.name);
		datactlm1347.setVar_billno(businessParam.getBillNo());
		datactlm1347.setId_model(startViewInfo.id);
		datactlm1347.setId_nodetype(WidgetName.HJ_TEXTVIEW);
		//
		datactlm1347.setFlag_upload("N");
		datactlm1347.setVar_data1(data);
		datactlm1347.setId_parentnode(businessParam.getIdParentNode());
		datactlm1347.setId_view(businessParam.getViewId());
		//
		if (StringUtil.isNullOrEmpty(datactlm1347.getId_node())) {
			datactlm1347.setId_node(StringUtil.getMyUUID());
		}
		// /保存JSOn值
		if (!StringUtil.isNullOrEmpty(items.attribute.datasource)) {
			String jsonvalues;
			jsonvalues = datactlm1347.getvar_Json();
			if (!StringUtil.isNullOrEmpty(jsonvalues)) {
				Map<String, Object> map = null;
				Gson gson = new Gson();
				map = (Map<String, Object>) gson.fromJson(jsonvalues,
						Object.class);
				map.put(items.attribute.field, data);
				datactlm1347.setvar_Json(gson.toJson(map));

			} else {
				datactlm1347.setvar_Json("{\"" + items.attribute.field
						+ "\":\"" + data + "\"}");
			}

		} else {
			datactlm1347.setvar_Json("");
		}
		datactlm1347.setInt_line(0);
		datactlm1347.setId_table(items.attribute.datasource);
		datactlm1347.setId_srcnode(items.id);
		datactlm1347.setDate_opr(businessParam.getDataOpr());
		BusinessBaseDao.replaceCTLM1347(datactlm1347);

	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		if (StringUtil.isNullOrEmpty(msg)) {
			msg = "";
		}

		msg = msg.replace("\\n", " \n");
		content.setText(msg);

		data = msg;
		content.setText(msg);
		if (currentviewClass.presave) {
			saveDefaultCTLM1347();
		}

	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
		Object defaultValue;
		defaultValue = items.defaultValue;
		// /如果默认值不为空，则直接加默认值
		if (defaultValue != null
				&& !StringUtil.isNullOrEmpty(defaultValue.toString())) {
			setValue(defaultValue.toString());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setJesonValue(String jsonValue) {
		// TODO Auto-generated method stub
		Map<String, Object> map = null;
		Gson gson = new Gson();
		map = (Map<String, Object>) gson.fromJson(jsonValue, Object.class);
		if (map != null && !map.isEmpty()) {
			datactlm1347.setvar_Json(jsonValue);
			setValue(map.get(items.attribute.field).toString());// content.setText(
																// list.get(0).get(
																// items.attribute.field));

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
		// TODO Auto-generated method stub
		if (flag) {

			ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao.getCtlm1347List(
					startViewInfo.id, currentviewClass.id,
					businessParam.getIdParentNode(), businessParam.getBillNo(),
					items.id);

			if (ctlm1347List != null) {
				setValue(ctlm1347List);
			}

		} else {
			// setctlm1345(ctlm1345List.getVar_values(),"N");
			setValue(ctlm1345List);
		}
		//
		// if(ctlm1347List != null && ctlm1347List.size() > 0){
		// setValue(ctlm1347List);
		// }else if(ctlm1345List != null &&(ctlm1347List == null ||
		// ctlm1347List.size() < 1)){
		// //setctlm1345(ctlm1345List.getVar_values(),"N");
		// setValue( ctlm1345List ) ;
		// }
	}

	public void setValue(BusinessData ctlm1345List) {
		// TODO Auto-generated method stub
		if (ctlm1345List.getCtlm1345().size() <= 0)
			return;
		datactlm1347.setvar_Json(ctlm1345List.getCtlm1345().get(0)
				.getVar_value());
		datactlm1347.setInt_line(0);
		if (StringUtil.isNullOrEmpty(datactlm1347.getId_node())) {
			datactlm1347.setId_node(StringUtil.getMyUUID());
		}
		datactlm1347.setId_table(items.attribute.datasource);
		// /设置显示 值
		setJesonValue(ctlm1345List.getCtlm1345().get(0).getVar_value());

	}

	public void setValue(ArrayList<Ctlm1347> ctlm1347List) {
		// TODO Auto-generated method stub
		if (ctlm1347List == null)
			return;
		datactlm1347 = ctlm1347List.get(0);
		setValue(datactlm1347.getVar_data1());

	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		items.attribute.datasource = Data;
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

		return 0;
	}

	@Override
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		if (items.attribute.required) {
			if (StringUtil.isNullOrEmpty(data)) {
				ToastUtil.ShowShort(getContext(), items.name + "不能为空,请输入.");
				return false;
			}
		}

		return true;
	}
}

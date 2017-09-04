package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.HJSender;
import com.hjnerp.util.LuaLoadScript;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

//@SuppressLint("NewApi")
public class HJToolBar extends LinearLayout implements HJViewInterface {
	private final static String TAG = "HJToolBar";
	private int width; // 每个按钮的宽度

	private WidgetClass items;
	private ViewClass currentviewClass;// 当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;
	public List<Ctlm1347> ctlm1347List = new ArrayList<Ctlm1347>();
	private Context context;
	private BusinessParam businessParam;
	private String currnodeid;

	public HJToolBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJToolBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJToolBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HJToolBar(Context context, WidgetClass items) {
		super(context);
		initView(items);
	}

	public HJToolBar(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,
			BusinessParam param) {
		super(context);
		this.items = items;
		this.context = context;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;
		initView(items);

	}

	public void saveDefaultCTLM1347() {
		if (currentviewClass.presave) {
			saveCTLM1347();
		}
	}

	public void saveCTLM1347() {

		for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {

			Ctlm1347 ctlm1347 = BusinessBaseDao.getSrcCtlm1347(
					businessParam.getIdParentNode(), businessParam.getBillNo(),
					businessParam.getModelId(),
					items.HJRadioButtonOption.get(i).id);
			if (ctlm1347 == null) {
				ctlm1347 = new Ctlm1347();
			}

			ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId());
			ctlm1347.setId_com(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getComid());

			if (StringUtil.isNullOrEmpty(ctlm1347.getId_node())) {
				ctlm1347.setId_node(StringUtil.getMyUUID());
			}
			ctlm1347.setName_node(items.HJRadioButtonOption.get(i).name);
			ctlm1347.setVar_billno(businessParam.getBillNo());
			ctlm1347.setId_model(businessParam.getModelId());
			ctlm1347.setId_view(businessParam.getViewId());
			ctlm1347.setId_nodetype(WidgetName.HJ_TOOLBAR);
			ctlm1347.setFlag_upload("N");
			ctlm1347.setId_parentnode(businessParam.getIdParentNode());
			ctlm1347.setInt_line(i);
			ctlm1347.setId_srcnode(items.HJRadioButtonOption.get(i).id);
			ctlm1347.setDate_opr(businessParam.getDataOpr());
			ctlm1347.setVar_data1("");
			BusinessBaseDao.replaceCTLM1347(ctlm1347);

		}

	}

	private void initView(final WidgetClass items) {

		int num = items.HJRadioButtonOption.size();
		Point outSize = new Point();
		((Activity) getContext()).getWindowManager().getDefaultDisplay()
				.getSize(outSize);
		width = outSize.x / num;
		if (items.HJRadioButtonOption.size() > 0) {

			for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {
				WidgetClass widget = items.HJRadioButtonOption.get(i);

				final Button button = new Button(getContext());
				button.setTag(widget.id);
				button.setLayoutParams(new LayoutParams(width,
						LayoutParams.MATCH_PARENT));
				// button.setBackground(getResources().getDrawable(R.drawable.hjtoolbar_button_selector)
				// );
				button.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.hjtoolbar_button_selector));
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						String buttonTag = (String) button.getTag();
						WidgetClass widgetClass = null;
						for (WidgetClass wClass : items.HJRadioButtonOption) {
							if (buttonTag.equalsIgnoreCase(wClass.id)) {
								widgetClass = wClass;
								break;
							}
						}
						if (widgetClass == null)
							return;
						// refresh ui 的操作代码
						// WidgetClass widgetClass =
						// items.HJRadioButtonOption.get(numberindex);
						Ctlm1347 ctlm1347 = BusinessBaseDao.getSrcCtlm1347(
								businessParam.getIdParentNode(),
								businessParam.getBillNo(),
								businessParam.getModelId(), widgetClass.id);

						if (ctlm1347 == null) {
							ctlm1347 = new Ctlm1347();
						}
						currnodeid = ctlm1347.getId_node();

						if (!StringUtil
								.isNullOrEmpty(widgetClass.attribute.onclick)) {
							try {
								// LuaLoadScript.LoadScript("",(com.hjnerp.business.activity.BusinessActivity)getContext());
								Map<String, String> mMap = new HashMap<String, String>();
								mMap.put(HJSender.COL, "");
								mMap.put(HJSender.COLID, "");
								mMap.put(HJSender.ROW, "1");
								mMap.put(HJSender.VALUES, "");
								mMap.put(HJSender.BILLNO,
										businessParam.getBillNo());
								mMap.put(HJSender.NODEID, currnodeid);

								LuaLoadScript.LoadScript();
								LuaLoadScript.runScript(
										widgetClass.attribute.onclick, mMap);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
				button.setText(widget.name);
				setButtonTextSize(button);
				addView(button);
			}

		}
	}

	private void setButtonTextSize(Button button) {
		if (items.attribute.fontsize != null) {
			if (getResources().getString(R.string.text_size_small)
					.equalsIgnoreCase(items.attribute.fontsize)) {
				button.setTextSize(getResources().getInteger(
						R.integer.TextSizeSmall));
			}
			if (getResources().getString(R.string.text_size_medium)
					.equalsIgnoreCase(items.attribute.fontsize)) {
				button.setTextSize(getResources().getInteger(
						R.integer.TextSizeMedium));
			}
			if (getResources().getString(R.string.text_size_large)
					.equalsIgnoreCase(items.attribute.fontsize)) {
				button.setTextSize(getResources().getInteger(
						R.integer.TextSizeLarge));
			}
		}

	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJesonValue(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setPhoto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return null;
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
	public String getRowCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataBuild(Boolean flag, BusinessData ctlm1345List) {
		// TODO Auto-generated method stub

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
		return true;
	}

}

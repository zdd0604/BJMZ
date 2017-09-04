package com.hjnerp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetAttribute;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("newapi")
public class HJListLayout extends LinearLayout implements HJViewInterface {
	private static String TAG = "HJListLayout";

	private ListView listView;

	private WidgetClass items;// <HJList
	private ViewClass currentviewClass;// 当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;
	private Context context;

	private String ID;
	private BusinessParam businessParam; // 当前界面上参数
	private MyListViewAdapter myAdapter;
	private LinearLayout titleLinearLayout;
	private TextView titleTextView;

	public ArrayList<Ctlm1347> ctlm1347List = new ArrayList<Ctlm1347>();
	public Boolean isbeing;
	private int listrow;

	public HJListLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJListLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	public HJListLayout(Context context, WidgetClass items,
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

	public WidgetClass getitems() {
		return this.items;
	}

	@Override
	public void setDataBuild(Boolean flag, BusinessData ctlm1345List) {
		if (flag) {
			ctlm1347List.clear();
			ctlm1347List = BusinessBaseDao.getCtlm1347List(startViewInfo.id,
					currentviewClass.id, businessParam.getIdParentNode(),
					businessParam.getBillNo(), items.id);
			if (ctlm1347List.size() > 0) {
				myAdapter.refreshList(ctlm1347List);
				setListViewHeightBasedOnChildren();
			}
		} else {
			if (ctlm1345List == null)
				return;
			setValue(ctlm1345List);
		}
	}

	private void setValue(BusinessData ctlm1345List) {
		for (int i = 0; i < ctlm1345List.getCtlm1345().size(); i++) {
			Ctlm1347 ctlm1347 = new Ctlm1347();

			ctlm1347.setvar_Json(ctlm1345List.getCtlm1345().get(i)
					.getVar_value());

			ctlm1347.setInt_line(ctlm1347List.size() + 1);

			ctlm1347.setId_node(StringUtil.getMyUUID());
			ctlm1347.setId_table(ctlm1345List.getId_table());
			ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId());
			ctlm1347.setId_com(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getComid());

			ctlm1347.setName_node(items.name);
			if (StringUtil.isNullOrEmpty(businessParam.getBillNo())) {
				ctlm1347.setVar_billno(StringUtil.getMyUUID());
			} else {
				ctlm1347.setVar_billno(businessParam.getBillNo());
			}
			ctlm1347.setId_model(businessParam.getModelId());
			ctlm1347.setId_nodetype(WidgetName.HJ_LIST);
			ctlm1347.setFlag_upload("N");
			ctlm1347.setId_view(businessParam.getViewId());
			ctlm1347.setId_parentnode(businessParam.getIdParentNode());
			ctlm1347.setId_srcnode(items.id);
			ctlm1347.setDate_opr(businessParam.getDataOpr());

			ctlm1347List.add(ctlm1347);
		}
		myAdapter.refreshList(ctlm1347List);
		setListViewHeightBasedOnChildren();

	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
		// Log.i(TAG,"setValueDefault()");
		String jsonstring = items.defaultValue;
		if (TextUtils.isEmpty(jsonstring))
			return;
		try {
			JSONArray jsonArray = new JSONArray(jsonstring);
			for (int i = 0; i < jsonArray.length(); i++) {
				// 处理每一行的数据
				Ctlm1347 ctlm1347 = new Ctlm1347();

				ctlm1347.setvar_Json(jsonArray.getJSONObject(i).toString());
				ctlm1347.setInt_line(ctlm1347List.size() + 1);
				ctlm1347.setId_node(StringUtil.getMyUUID());
				ctlm1347.setId_table(items.attribute.datasource);
				ctlm1347.setId_recorder(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
				ctlm1347.setId_com(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getComid());

				ctlm1347.setName_node(items.name);
				if (StringUtil.isNullOrEmpty(businessParam.getBillNo())) {
					ctlm1347.setVar_billno(StringUtil.getMyUUID());
				} else {
					ctlm1347.setVar_billno(businessParam.getBillNo());
				}
				ctlm1347.setId_model(businessParam.getModelId());
				ctlm1347.setId_nodetype(WidgetName.HJ_LIST);
				ctlm1347.setFlag_upload("N");
				ctlm1347.setId_view(businessParam.getViewId());
				ctlm1347.setId_parentnode(businessParam.getIdParentNode());
				ctlm1347.setId_srcnode(items.id);
				ctlm1347.setDate_opr(businessParam.getDataOpr());
				ctlm1347List.add(ctlm1347);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// saveDefaultCTLM1347();
		myAdapter.refreshList(ctlm1347List);
		setListViewHeightBasedOnChildren();
	}

	@Override
	public void setJesonValue(String jsonValue) {
		// /得到参数
		if (TextUtils.isEmpty(jsonValue))
			return;
		try {
			JSONArray jsonArray = new JSONArray(jsonValue);
			for (int i = 0; i < jsonArray.length(); i++) {
				// 处理每一行的数据
				Ctlm1347 ctlm1347 = new Ctlm1347();
				ctlm1347.setvar_Json(jsonArray.getJSONObject(i).toString());
				ctlm1347.setInt_line(ctlm1347List.size() + 1);
				ctlm1347.setId_node(StringUtil.getMyUUID());
				ctlm1347.setId_table(items.attribute.datasource);
				ctlm1347.setId_recorder(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getMyId());
				ctlm1347.setId_com(SharePreferenceUtil
						.getInstance(
								EapApplication.getApplication()
										.getApplicationContext()).getComid());

				ctlm1347.setName_node(items.name);

				if (StringUtil.isNullOrEmpty(businessParam.getBillNo())) {
					ctlm1347.setVar_billno(StringUtil.getMyUUID());
				} else {
					ctlm1347.setVar_billno(businessParam.getBillNo());
				}

				ctlm1347.setId_model(businessParam.getModelId());
				ctlm1347.setId_nodetype(WidgetName.HJ_LIST);
				ctlm1347.setFlag_upload("N");
				ctlm1347.setId_view(businessParam.getViewId());
				ctlm1347.setId_parentnode(businessParam.getIdParentNode());
				ctlm1347.setId_srcnode(items.id);
				ctlm1347.setDate_opr(businessParam.getDataOpr());
				ctlm1347List.add(ctlm1347);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		myAdapter.refreshList(ctlm1347List);
		setListViewHeightBasedOnChildren();

	}

	public void saveDefaultCTLM1347() {
		if (currentviewClass.presave) {
			saveCTLM1347();
		}
	}

	@SuppressWarnings("unchecked")
	public void saveCTLM1347() {

		for (Ctlm1347 ctlm1347 : ctlm1347List) {

			for (int j = 0; j < items.HJRadioButtonOption.size(); j++) {
				WidgetClass widget = items.HJRadioButtonOption.get(j);

				if ("HJTitle".equalsIgnoreCase(widget.widgetType))
					continue;

				Map<String, Object> map = null;
				String text = "";
				Gson gson = new Gson();
				map = (Map<String, Object>) gson.fromJson(
						ctlm1347.getvar_Json(), Object.class);
				if (map != null) {
					text = (String) map.get(widget.attribute.field);
					ctlm1347.setVar_data(widget.attribute.dbfield, text);
				}
			}
			BusinessBaseDao.replaceCTLM1347(ctlm1347);
		}
	}

	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjlist, null);
		listView = (ListView) view.findViewById(R.id.listView);
		addView(view);
		// 设置title
		myAdapter = new MyListViewAdapter(items, ctlm1347List);
		listView.setAdapter(myAdapter);

		for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {
			if ("HJTitle"
					.equalsIgnoreCase(items.HJRadioButtonOption.get(i).widgetType)) {
				titleLinearLayout = (LinearLayout) view
						.findViewById(R.id.hj_list_title_layout);
				titleTextView = (TextView) view
						.findViewById(R.id.hj_list_title);
				titleLinearLayout.setVisibility(View.VISIBLE);
				titleTextView.setVisibility(View.VISIBLE);
				titleTextView.setText(items.HJRadioButtonOption.get(i).name);
				settitleAttribute(items.HJRadioButtonOption.get(i));

			}
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listrow = arg2;

				if (items.attribute.onclick != null) {
					try {

						Map<String, String> mMap = new HashMap<String, String>();
						mMap.put(HJSender.COL, "");
						mMap.put(HJSender.COLID, "");
						mMap.put(HJSender.ROW, String.valueOf(listrow));
						mMap.put(HJSender.VALUES, ctlm1347List.get(listrow)
								.getvar_Json());
						mMap.put(HJSender.BILLNO, ctlm1347List.get(listrow)
								.getVar_billno());
						mMap.put(HJSender.NODEID, ctlm1347List.get(listrow)
								.getId_node());
						LuaLoadScript.LoadScript();
						LuaLoadScript.runScript(items.attribute.onclick, mMap);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});

	}

	private void setListViewHeightBasedOnChildren() {
		Adapter adapter = listView.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {

			View view = adapter.getView(i, null, listView);
			view.measure(0, 0);
			totalHeight += view.getMeasuredHeight();
		}
		android.view.ViewGroup.LayoutParams ll = listView.getLayoutParams();
		ll.height = totalHeight + listView.getDividerHeight()
				* (adapter.getCount() - 1);
		listView.setLayoutParams(ll);
	}

	public String getHJId() {
		return ID;
	}

	public class MyListViewAdapter extends BaseAdapter {
		WidgetClass adapterItems;
		ArrayList<Ctlm1347> adapterList;

		public MyListViewAdapter(WidgetClass items,
				ArrayList<Ctlm1347> adapterlist) {
			this.adapterItems = items;
			this.adapterList = adapterlist;
		}

		@Override
		public int getCount() {
			return adapterList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return adapterList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void refreshList(ArrayList<Ctlm1347> adapterList) {
			this.adapterList = adapterList;
			this.notifyDataSetChanged();

		}

		public void refreshList(WidgetClass items,
				ArrayList<Ctlm1347> adapterList) {
			// Log.d(TAG,"refreshList () >>>>>>>>>>>>>");
			this.adapterList = adapterList;
			this.adapterItems = items;
			this.notifyDataSetChanged();
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View view, ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (view == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.layout_hjlist_item, parent, false);
				viewHolder.imageLeft = (ImageView) view
						.findViewById(R.id.imageViewLeft);
				viewHolder.imageRight = (ImageView) view
						.findViewById(R.id.imageViewRight);
				viewHolder.linearMiddle = (LinearLayout) view
						.findViewById(R.id.linear_middle);
				viewHolder.linearLeft = (LinearLayout) view
						.findViewById(R.id.linear_left);
				viewHolder.linearRight = (LinearLayout) view
						.findViewById(R.id.linear_right);

				viewHolder.textViewRight = (TextView) view
						.findViewById(R.id.textViewRight);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			isbeing = false;
			// 中间linearLayout加载TextView
			viewHolder.linearMiddle.removeAllViews();
			Map<String, Object> map = null;
			Gson gson = new Gson();

			if (adapterItems.HJRadioButtonOption.size() > 0) {
				for (int i = 0; i < adapterItems.HJRadioButtonOption.size(); i++) {

					WidgetClass widget = adapterItems.HJRadioButtonOption
							.get(i);
					if ("HJTitle".equalsIgnoreCase(widget.widgetType))
						continue;

					WidgetAttribute attribute = widget.attribute;

					if (adapterItems.attribute.visibledisclosure) {
						viewHolder.imageRight.setVisibility(View.VISIBLE);
					} else {
						viewHolder.imageRight.setVisibility(View.GONE);
					}

					Ctlm1347 ctlm1347View = adapterList.get(position);
					String var_josn = ctlm1347View.getvar_Json();
					String text = "";
					String dbtext = "";
					String josntxt = "";

					if (adapterItems.attribute.upload) {
						viewHolder.textViewRight.setVisibility(View.VISIBLE);
						if ("Y".equalsIgnoreCase(ctlm1347View.getFlag_upload())) {
							viewHolder.textViewRight.setText("已");
						}

					} else {
						viewHolder.textViewRight.setVisibility(View.GONE);
					}

					dbtext = ctlm1347View.getVar_data(attribute.dbfield);
					if (!StringUtil.isNullOrEmpty(var_josn)) {
						map = null;
						map = (Map<String, Object>) gson.fromJson(var_josn,
								Object.class);
						if (map != null && !map.isEmpty()) {
							josntxt = (String) map.get(attribute.field);
						}
						if (StringUtil.isNullOrEmpty(dbtext)) {
							text = josntxt;

						} else if (josntxt != null
								&& !josntxt.equalsIgnoreCase(dbtext)
								&& !StringUtil.isNullOrEmpty(dbtext)) {
							text = dbtext;
							/**
							 * @author haijian 非空验证
							 */
							if (map != null && attribute != null) {

								map.put(attribute.field, dbtext);
								ctlm1347View.setvar_Json(gson.toJson(map));
							}
						} else if (josntxt != null
								&& josntxt.equalsIgnoreCase(dbtext)) {
							text = dbtext;
						}
					}

					if ("HJListCheck"
							.equalsIgnoreCase(adapterItems.HJRadioButtonOption
									.get(i).widgetType)) {
						viewHolder.imageLeft.setVisibility(View.VISIBLE);
						if ("Y".equalsIgnoreCase(text)) {
							viewHolder.imageLeft
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.round_selector_checked));
						} else {
							viewHolder.imageLeft
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.round_selector_normal));

						}
					} else {

						HJListLabel label = new HJListLabel(getContext());
						label.setAttribute(widget);

						if (!"".equalsIgnoreCase(widget.name)) {
							label.setDataResource(widget.name + " : " + text);
						} else {
							label.setDataResource(text);
						}
						viewHolder.linearMiddle.addView(label);
					}
				}
			}

			return view;
		}

	}

	static class ViewHolder {
		LinearLayout linearMiddle;
		LinearLayout linearLeft;
		LinearLayout linearRight;
		ImageView imageLeft;
		ImageView imageRight;
		TextView textViewRight;
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
		return items.attribute.editable;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		int irow;
		irow = Integer.parseInt(row);
		if (irow == 0) {
			irow = listrow;
		}
		// Map<String, String> mmap = dataList.get(irow).getMap();
		JSONObject json = null;
		try {
			json = new JSONObject(ctlm1347List.get(irow).getvar_Json());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int ii = 0; ii < items.HJRadioButtonOption.size(); ii++) {
			WidgetClass widget = items.HJRadioButtonOption.get(ii);
			if (column.equalsIgnoreCase(widget.id)) {
				column = widget.attribute.field;
				break;
			}
		}
		String text = "";
		try {
			text = (String) json.get(column);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return String.valueOf(ctlm1347List.size());
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return String.valueOf(listrow);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		items.attribute.datasource = Data;
	}

	public void settitleAttribute(WidgetClass item) {
		// 设置字体大小
		if (getContext().getResources().getString(R.string.text_size_large)
				.equalsIgnoreCase(item.attribute.fontsize)) {
			titleTextView.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeLarge));
		} else if (getContext().getResources()
				.getString(R.string.text_size_medium)
				.equalsIgnoreCase(item.attribute.fontsize)) {
			titleTextView.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeMedium));
		} else {
			titleTextView.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeSmall));
		}
		// 设置居中

		if ("left".equalsIgnoreCase(item.attribute.alignment)) {
			titleTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		}
		if ("center".equalsIgnoreCase(item.attribute.alignment)) {
			titleTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		}
		if ("right".equalsIgnoreCase(item.attribute.alignment)) {
			titleTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);

		}
		// 设置加粗
		Paint paint = titleTextView.getPaint();
		paint.setFakeBoldText(item.attribute.bold);
		// 设置是否换行
		titleTextView.setSingleLine(item.attribute.singleline);
		if (item.attribute.backgroundcolor != null
				&& !"".equalsIgnoreCase(item.attribute.backgroundcolor)) {
			titleLinearLayout.setBackgroundColor(Color
					.parseColor(item.attribute.backgroundcolor));
		}
		if (item.attribute.textcolor != null
				&& !"".equalsIgnoreCase(item.attribute.textcolor)) {
			titleTextView.setTextColor(Color
					.parseColor(item.attribute.textcolor));
		}
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
		saveCTLM1347();
		return 0;
	}

	@Override
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub

		Ctlm1347 ctlm1347 = new Ctlm1347();
		ctlm1347.setvar_Json(vlues);
		ctlm1347.setInt_line(ctlm1347List.size() + 1);
		ctlm1347.setId_node(nodeid);
		ctlm1347.setId_table(items.attribute.datasource);
		ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getMyId());
		ctlm1347.setId_com(SharePreferenceUtil.getInstance(
				EapApplication.getApplication().getApplicationContext())
				.getComid());

		ctlm1347.setName_node(items.name);
		ctlm1347.setVar_billno(billno);
		ctlm1347.setId_model(businessParam.getModelId());
		ctlm1347.setId_nodetype(WidgetName.HJ_LIST);
		ctlm1347.setFlag_upload("N");
		ctlm1347.setId_view(businessParam.getViewId());
		ctlm1347.setId_parentnode(businessParam.getIdParentNode());
		ctlm1347.setId_srcnode(items.id);
		ctlm1347.setDate_opr(businessParam.getDataOpr());
		ctlm1347List.add(ctlm1347);
		myAdapter.refreshList(ctlm1347List);
		setListViewHeightBasedOnChildren();
		this.saveDefaultCTLM1347();
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}

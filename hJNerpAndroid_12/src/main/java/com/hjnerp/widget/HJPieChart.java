package com.hjnerp.widget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.hjnerpandroid.R;

public class HJPieChart extends LinearLayout implements HJViewInterface,
		View.OnClickListener {

	public static final String TAG = "HJPieChart";

	public static final String ID = "id";
	public static final String QTY = "qty";

	private int width;
	private int height;
	private double total;

	private DefaultRenderer render = new DefaultRenderer();
	private CategorySeries dataset = new CategorySeries("");
	private WidgetClass items;
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	private Pattern p = Pattern.compile("\\s*|\t|\n|\n");
	private String[] coloritem;
	private RelativeLayout mrLayout;

	private LinearLayout layout;
	private TextView title;
	private TextView xLable;
	private TextView yLable;
	private GraphicalView graview;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	
	public ArrayList<Ctlm1347> ctlm1347List;
	public BusinessData ctlm1345List;
	private BusinessParam businessParam =  new BusinessParam(); //当前界面上参数
	private String id_node;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public HJPieChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HJPieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJPieChart(Context context) {
		super(context);
		initView();
	}

	public HJPieChart(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param) {
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;
		
		initView();
//		setAttribute(items);
	}

	private void setAttribute(WidgetClass items,String json) {
		String colors = items.attribute.colors;
		if (!TextUtils.isEmpty(colors)) {
			Matcher m = p.matcher(colors);
			colors = m.replaceAll("");
		}
		coloritem = colors.split(";");
		int[] color = new int[coloritem.length];
		for (int i = 0; i < color.length; i++) {
			color[i] = Color.parseColor(coloritem[i]);
		}
//		decompositionData(items.defaultValue);
		decompositionData(json);
		Collections.sort(data, new Sort());
		setRender(color);
		buildDataSet();
		graview = ChartFactory.getPieChartView(getContext(), dataset, render);
		if (graview != null) {
			graview.setOnClickListener(this);
		}
		layout.addView(graview);
		xLable.setVisibility(VISIBLE);
		title.setVisibility(VISIBLE);
		title.setText(items.name);
		//默认显示最大的块
		String id = (String) data.get(data.size()-1).get(ID);
		String qty = (String) data.get(data.size()-1).get(QTY);
		setLables(0, id, Double.parseDouble(qty));
		render.getSeriesRendererAt(render.getSeriesRendererCount()-1).setHighlighted(true);
		graview.repaint();
	}

	private void buildDataSet() {
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			dataset.add((String) map.get(ID),
					Double.parseDouble((String) map.get(QTY)));
		}
	}

	private void setRender(int[] color) {
		render.setApplyBackgroundColor(true);
		render.setBackgroundColor(getContext().getResources().getColor(
				R.color.white));
		render.setPanEnabled(false);
		render.setDisplayValues(false);
		render.setMargins(new int[] { 0, 60, 15, 0 });
		render.setClickEnabled(true);
		render.setZoomButtonsVisible(false);
		render.setZoomEnabled(false);
		render.setShowLegend(false);
		render.setShowLabels(false);
		render.setStartAngle(180);
		render.setClickEnabled(true);
		render.setLabelsTextSize(40);
		for (int i = 0; i < data.size(); i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color[i % color.length]);
			render.addSeriesRenderer(r);
		}
	}

	private void decompositionData(String defaultValue) {
		try {
			JSONArray jsonArray = new JSONArray(defaultValue);
			total = 0;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString(ID);
				String qty = jsonObject.getString(QTY);
				total += Double.parseDouble(qty);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(ID, id);
				map.put(QTY, qty);
				data.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {
		width = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getWidth();
		height = (int) (((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getHeight() / 3 * 1.5);
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hj_piechart, null);
		layout = (LinearLayout) view.findViewById(R.id.hj_pie_chart);
		mrLayout = (RelativeLayout) view.findViewById(R.id.hj_pie_relative);
		mrLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (graview != null) {
					render.setShowLabels(true);
					dataset.clear();
					for (int i = 0; i < data.size(); i++) {
						Map<String, Object> map = data.get(i);
						String id = (String) map.get(ID);
						String num = (String) map.get(QTY);
						double percent = Double.parseDouble(num) / total * 100;
						DecimalFormat df = new DecimalFormat("###.00");
						String result = id+" "+df.format(percent);
						dataset.add(result, Double.parseDouble(num));
					}
					graview.repaint();
				}
			}
		});
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		android.view.ViewGroup.LayoutParams ll = layout.getLayoutParams();
		ll.width = width;
		ll.height = height;
		title = (TextView) view.findViewById(R.id.hj_piechart_title);
		xLable = (TextView) view.findViewById(R.id.pieLableX);
		yLable = (TextView) view.findViewById(R.id.pieLableY);
		addView(view);
	}

	private void setLables(int index, String xValue, double yValue) {
		String xLableValue = "<font color="
				+ coloritem[index % coloritem.length] + ">" + xValue
				+ "</font>" + "<br>";
		xLableValue = xLableValue + total;
		double percent = yValue / total * 100;
		DecimalFormat df = new DecimalFormat("###.00");
		String percentString = df.format(percent) + "%";

		String yLableValue = "<font color="
				+ coloritem[index % coloritem.length] + ">" + yValue + "<br>";
		yLableValue += percentString;
		xLable.setText(Html.fromHtml(xLableValue));
		yLable.setText(Html.fromHtml(yLableValue));
	}

	@Override
	public void setValue(String msg) {

		// BI图不支持非json数据
		data.clear();
		dataset.clear();
		render.removeAllRenderers();
		decompositionData(msg);
		buildDataSet();
		int[] color = new int[coloritem.length];
		for(int i = 0;i<color.length;i++) {
			color[i] = Color.parseColor(coloritem[i]);
		}
		setRender(color);
		if(graview!= null){
			graview.repaint();
		}
	}

	@Override
	public void setValueDefault() {

	}

	@Override
	public void setJesonValue(String msg) {

	}

	@Override
	public String getDataSource() {
		return items.attribute.datasource ;
		}

	@Override
	public String getID() {
		return id_node;
	}

	@Override
	public boolean getEditable() {
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v instanceof GraphicalView) {
			SeriesSelection selection = graview.getCurrentSeriesAndPoint();
			if (selection != null) {
				render.setShowLabels(false);
				dataset.clear();
				buildDataSet();
				for (int i = 0; i < data.size(); i++) {
					render.getSeriesRendererAt(i).setHighlighted(
							i == selection.getPointIndex());
				}
				graview.repaint();
				String xValue = dataset.getCategory(selection.getPointIndex());
				double Yvalue = dataset.getValue(selection.getPointIndex());
				setLables(selection.getPointIndex(), xValue, Yvalue);
			}
		}
	}

	private class Sort implements Comparator<Map<String, Object>> {

		@Override
		public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
			double l = Double.parseDouble((String) lhs.get(QTY));
			double r = Double.parseDouble((String) rhs.get(QTY));
			if (l > r)
				return 1;
			return -1;
		}

	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		return null;
	}

	 
	 

	@Override
	public String getRowCount() {
		return null;
	}

	@Override
	public String getCurrentRow() {
		return null;
	}

	@Override
	public void setDataBuild(Boolean flag ,
			BusinessData ctlm1345List) {
		this.ctlm1345List = ctlm1345List;
//		this.ctlm1347List = ctlm1347List;
		
		if(flag){
		
			ctlm1347List= BusinessBaseDao. 
					 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id) ;
			 
			setValue(ctlm1347List);
			
		}else if(ctlm1345List != null &&(ctlm1347List == null || ctlm1347List.size() < 1)){ 
 
			if(ctlm1345List.getId_table().trim().equalsIgnoreCase(items.attribute.datasource.trim())){
				BusinessBaseDao.deletesrcctlm1347(businessParam.getBillNo(),businessParam.getViewId(),businessParam.getIdParentNode(), items.id);
				setctlm1345(ctlm1345List.getVar_values());
			} 
			
		}
	}
	
	public void setValue(ArrayList<Ctlm1347> ctlm1347Lista) {

//		for (int i = 0; i < ctlm1347Lista.size(); i++) {
//
//			if (items.id.trim().equalsIgnoreCase(
//					ctlm1347Lista.get(i).getId_srcnode().trim())) {
//				ctlm1347List.add(ctlm1347Lista.get(i));
//			
//			}
//		}
		
		for (int i = 0; i < ctlm1347List.size(); i++ )
		{
			setAttribute(items, ctlm1347List.get(i).getvar_Json());
		}
	}
	
	
	private void setctlm1345( String jsonValue)
	{
//		setJesonValue(jsonValue);
		
		id_node = StringUtil.getMyUUID();
		setAttribute(items,jsonValue);
//		saveCTLM1347();
		saveDefaultCTLM1347();
	}
	
	public void saveDefaultCTLM1347(){
		if(currentviewClass.presave){
			saveCTLM1347();
		}
	}
	
	public void saveCTLM1347() { 
		Ctlm1347 ctlm1347 = new Ctlm1347(); 
		ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		ctlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
 	    ctlm1347.setId_node( id_node ); // 自己生成id_node,当前时间+随机数
		ctlm1347.setName_node(items.name);
		ctlm1347.setVar_billno(businessParam.getBillNo());
		ctlm1347.setId_model(businessParam.getModelId() );
		ctlm1347.setId_nodetype(WidgetName.HJ_PIE_CHART); 
		ctlm1347.setFlag_upload("N");
		ctlm1347.setId_parentnode(businessParam.getIdParentNode()); 
		
		ctlm1347.setInt_line(0); 
		ctlm1347.setId_table(ctlm1345List.getId_table()); 
		ctlm1347.setvar_Json(ctlm1345List.getVar_values() ); 
		ctlm1347.setId_srcnode(items.id);
		ctlm1347.setDate_opr(businessParam.getDataOpr()); 
		BusinessBaseDao.replaceCTLM1347(ctlm1347);
}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataSource(String Data) {
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
	public void addItem(String billno,String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}

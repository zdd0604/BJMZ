package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

@SuppressLint("InflateParams") public class HJBarChart extends LinearLayout implements HJViewInterface {

	public static final String TAG = "HJBarChart";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String QTY = "qty";
	public static final String SORT = "sort";
	
	public static final int barWidth = 40;

	private WidgetClass items;
	private int height;
	private int width;
	private LinearLayout layout;
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	private List<Value> list = new ArrayList<Value>();
	private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	private XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	private float dpi;
	private int[] points; // 各个lable在屏幕上的位置
	private String []coloritem;

	private GraphicalView graview;
	private BarChart mBarChart;
	private TextView title;
	private TextView XLable;
	private TextView YLable;
	private CircleImageView ImageCircle;
	private RelativeLayout mLayout;

	public ArrayList<Ctlm1347> ctlm1347List;
	public BusinessData ctlm1345List;
	private BusinessParam businessParam =  new BusinessParam(); //当前界面上参数
	private String id_node;
	
//	@SuppressLint("NewApi")
	public HJBarChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HJBarChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJBarChart(Context context) {
		super(context);
		initView();
	}

	public HJBarChart(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param) {
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;
		
		initView();
//		setAttribute(items);
	}

	private void initView() {
		height = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getHeight() / 3;
		width = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getWidth();
		DisplayMetrics outMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		dpi = outMetrics.density;
		// LayoutInflater inflate =
		// ((Activity)getContext()).getLayoutInflater();
		LayoutInflater inflate = (LayoutInflater) ((Activity) getContext())
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.layout_hjbarchart, null);
		mLayout = (RelativeLayout) view.findViewById(R.id.hj_bar_relative);
		mLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(graview!= null) {
					for(int i = 0;i<renderer.getSeriesRendererCount();i++){
						boolean flag = renderer.getSeriesRendererAt(i).isDisplayChartValues();
						renderer.getSeriesRendererAt(i).setDisplayChartValues(!flag);
					}
					graview.repaint();
				}
			}
		});
		layout = (LinearLayout) view.findViewById(R.id.hj_bar_chart);
		title = (TextView) view.findViewById(R.id.hj_barchar_title);
		XLable = (TextView) view.findViewById(R.id.barLableX);
		YLable = (TextView) view.findViewById(R.id.BarLableY);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		ImageCircle = (CircleImageView) view
				.findViewById(R.id.hj_barline_circle);
		ImageCircle.setOnTouchListener(new OnTouchListener() {

			float lastX;
			float firstX;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = event.getRawX();
					firstX = lastX;
					break;
				case MotionEvent.ACTION_MOVE:
					float x = event.getRawX() - lastX;
					int left = (int) (ImageCircle.getLeft() + x);
					int right = (int) (ImageCircle.getRight() + x);
					int top = ImageCircle.getTop();
					int buttom = ImageCircle.getBottom();
					if (left < 0) {
						left = 0;
						right = left + ImageCircle.getWidth();
					}
					if (right > width) {
						right = width;
						left = right - getWidth();
					}
					ImageCircle.setX(left);
					ImageCircle.layout(left, top, right, buttom);
					ImageCircle.invalidate();
					lastX = event.getRawX();
					processPointShow(left);
					break;
				default:
					break;
				}
				return false;
			}
		});
		android.view.ViewGroup.LayoutParams lp = layout.getLayoutParams();
		lp.width = width;
		lp.height = height;
		addView(view);
	}

	protected void processPointShow(int x) {
		x = x+70;
		int num = list.size();// 每个坐标显示num条线
		for (int index = 0; index < points.length; index++) {
			int[] px = new int[num];
			px[0] = points[index] - barWidth/2 * num;
			for (int i = 1; i < num; i++) {
				px[i] = px[i-1] + barWidth;
//				Log.e(TAG,"px "+px[i]);
			}
		
			if (x >= px[0] && x <= px[num - 1]+barWidth) {
				// 表明落在区域内
				int i = 0;
				for (i = 0; i < num; i++) {
					if (x < px[i])
						break;
				}
				//i代表年份，index代表月份
				setXYLable(i-1,index);
				GradientDrawable drawble = (GradientDrawable) ImageCircle.getBackground();
				int col = Color.parseColor(coloritem[(i-1)%coloritem.length]);
				drawble.setStroke(1, col);
				break;
			}

		}
	}

	private void setXYLable(int i, int index) {
		Value value = list.get(i);//取出选中的年份
		String xLableValue = value.id + ":" + "<br>" + "<font color="
				+ coloritem[i % coloritem.length] + ">"
				+ (String) value.dataList.get(index).get(NAME) + "</font>";
		String yLabelValue = items.attribute.yaxisname + ":" + "<br>";
		yLabelValue += "<font color="
				+ coloritem[i % coloritem.length]
				+ ">"+ String.valueOf(list.get(i).dataList.get(index).get(
						QTY)) + "</font>" + "<br>";
		XLable.setVisibility(VISIBLE);
		XLable.setText(Html.fromHtml(xLableValue));
		YLable.setText(Html.fromHtml(yLabelValue));
	}

	public void setAttribute(WidgetClass items,String json) {
		String colors = items.attribute.colors;
		if (!TextUtils.isEmpty(colors)) {
			Matcher m = p.matcher(colors);
			colors = m.replaceAll("");
		}
	    coloritem = colors.split(";");
		int[] color = new int[coloritem.length];
		for (int i = 0; i < coloritem.length; i++) {
			color[i] = Color.parseColor(coloritem[i]);
		}
//		decompositionData(items.defaultValue);
		decompositionData(json);
		for (int i = 0; i < list.size(); i++) {
			Collections.sort(list.get(i).dataList, new Sort());
		}
		setRender(color);
		setChartSettings();
		buildDataSet();
		calculateXPoint();
		mBarChart = new BarChart(dataset, renderer, Type.DEFAULT);
		graview = new GraphicalView(getContext(), mBarChart);
		// graview = ChartFactory.getBarChartView(getContext(), dataset,
		// renderer,
		// Type.DEFAULT);
		title.setVisibility(VISIBLE);
		title.setText(items.name);
		ImageCircle.setX((int) (35 - 10 * dpi));
		ImageCircle.layout((int) (35 - 10 * dpi), 0, (int) (35 + 10 * dpi),
				(int) (20 * dpi));
		ImageCircle.invalidate();
		layout.addView(graview);
		setXYLable(0, 0);
		GradientDrawable drawable = (GradientDrawable) ImageCircle.getBackground();
		drawable.setStroke(1, color[0]);

	}

	private void calculateXPoint() {
		int max = getCount();
		// 原点的位置
		points = new int[max - 1];
		int original = 70;
		float space = (width - 70) / max;
		points[0] = (int) (original + space);
		for (int i = 1; i < points.length; i++) {
			points[i] = (int) (points[i - 1] + space);
		}

	}

	private void buildDataSet() {
		for (int i = 0; i < list.size(); i++) {
			Value value = list.get(i);
			CategorySeries series = new CategorySeries(value.id);
			List<Map<String, Object>> dataList = value.dataList;
			double[] XValue = new double[dataList.size()];
			double[] YValue = new double[dataList.size()];
			for (int j = 0; j < dataList.size(); j++) {
				XValue[j] = j;
				YValue[j] = (Double) dataList.get(j).get(QTY);
			}
			for (int j = 0; j < XValue.length; j++) {
				series.add(YValue[j]);
			}
			dataset.addSeries(series.toXYSeries());
		}
	}

	private void setRender(int[] coloritems) {
//		renderer.setOrientation(Orientation.VERTICAL);
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(40);
		renderer.setLegendTextSize(15);
		renderer.setBarWidth(barWidth);
		renderer.setBarSpacing(2);
		renderer.setShowLegend(false);
		renderer.setClickEnabled(false);
		renderer.setSelectableBuffer(30);
		renderer.setRange(new double[] { 0, getCount(), 0, 0});
		renderer.setMargins(new int[] { 40, 70, 50, 0});
		if(getCount()>7){
			renderer.setXLabelsAngle(-45);
		} else {
			renderer.setXLabelsAngle(0);
		}
		for (int i = 0; i < list.size(); i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setChartValuesTextSize(30);
			r.setColor(coloritems[i % coloritems.length]);
			renderer.addSeriesRenderer(r);
		}
	}

	private void setChartSettings() {
//		renderer.setXTitle(items.attribute.xaxisname);
//		renderer.setYTitle(items.attribute.yaxisname);
		renderer.setLabelsTextSize(30);
		renderer.setAxisTitleTextSize(15);
		renderer.setPanEnabled(false);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(false, false);
		renderer.setMarginsColor(getContext().getResources().getColor(
				R.color.white));
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(getContext().getResources().getColor(
				R.color.white));
		renderer.setYAxisMax(items.attribute.yaxismaxvalue);
		renderer.setYAxisMin(items.attribute.yaxisminvalue);
		int num = (items.attribute.yaxismaxvalue - items.attribute.yaxisminvalue)
				/ items.attribute.yaxistick;
		renderer.setYLabels(0);
		renderer.setYLabels(num);
		renderer.setXLabels(0);
		renderer.setLabelsColor(Color.LTGRAY);
		renderer.setXLabelsColor(Color.LTGRAY);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		// 设置x轴的名称
		Value v = getXCount();
		List<Map<String, Object>> dataList = v.dataList;
		for (int i = 0; i < dataList.size(); i++) {
			renderer.addXTextLabel(i + 1, (String) dataList.get(i).get(NAME));
		}
	}

	private int getCount() {
		int max = 0;
		for (int i = 0; i < list.size(); i++) {
			max = Math.max(max, list.get(i).dataList.size());
		}
		return max + 1;
	}

	private Value getXCount() {
		if(list.size() == 0){
			return null;
		}
		int index = 0;
		int max = 0;
		for (int i = 0; i < list.size(); i++) {
			List<Map<String, Object>> dataList = list.get(i).dataList;
			if (max < dataList.size()) {
				max = dataList.size();
				index = i;
			}
		}
		return list.get(index);
	}

	private void decompositionData(String defaultValue) {
		// 解析json数据
		try {
			JSONArray jsonArray = new JSONArray(defaultValue);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString(ID);
				String name = jsonObject.getString(NAME);
				double qty = jsonObject.getDouble(QTY);
				String sort = jsonObject.getString(SORT);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(NAME, name);
				map.put(QTY, qty);
				map.put(SORT, sort);
				Value value = getFromListValue(id);
				if (value == null) {
					value = new Value();
					value.id = id;
					value.dataList.add(map);
					list.add(value);
				} else {
					value.dataList.add(map);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Value getFromListValue(String id2) {
		for (Value v : list) {
			if (id2.equals(v.id))
				return v;
		}
		return null;
	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		// BI图不支持非json数据
		list.clear();
		dataset.clear();
		renderer.removeAllRenderers();
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
		// TODO Auto-generated method stub
	}

	 

	private class Sort implements Comparator<Map<String, Object>> {

		@Override
		public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
			int first = Integer.parseInt((String) lhs.get(SORT));
			int second = Integer.parseInt((String) rhs.get(SORT));
			if (first > second)
				return 1;
			return -1;
		}

	}

	private class Value {
		String id; // 表明数据的id，一个id可能对应多个数据
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(); // 具体的数据集合

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		return column;
		// TODO Auto-generated method stub
		
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
	public void setDataBuild( Boolean flag ,
			BusinessData ctlm1345List) {
		 if(  flag){ 
			   ctlm1347List.clear();
					 ctlm1347List = BusinessBaseDao. 
							 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id);
			  
			}else  if  (ctlm1345List != null  && !flag )
			{	//setctlm1345(ctlm1345List.getVar_values(),"N");
				setValue( ctlm1345List.getVar_values() ) ;
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
			ctlm1347.setId_com( SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
			ctlm1347.setId_node( id_node ); // 自己生成id_node,当前时间+随机数
			ctlm1347.setName_node(items.name);
			ctlm1347.setVar_billno(businessParam.getBillNo());
			ctlm1347.setId_model(businessParam.getModelId() );
			ctlm1347.setId_nodetype(WidgetName.HJ_BAR_CHART); 
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
	public int saveData( Boolean required) {
		// TODO Auto-generated method stub
		return 0; 
	}



 

	@Override
	public void setJesonValue(String values) {
		// TODO Auto-generated method stub
		
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

package com.mznerp.widget;

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
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.business.view.StartViewInfo;
import com.mznerp.business.view.ViewClass;
import com.mznerp.business.view.WidgetClass;
import com.mznerp.business.view.WidgetName;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.BusinessData;
import com.mznerp.model.BusinessParam;
import com.mznerp.model.Ctlm1347;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.util.StringUtil;
import com.mznerp.R;

public class HJLineChart extends LinearLayout implements HJViewInterface, View.OnClickListener {

	public static final String TAG = "HJLineChart";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String QTY = "qty";
	public static final String SORT = "sort";

	private WidgetClass items;
	private ViewClass currentviewClass;
	private LinearLayout layout;
	private int width;
	private int height;
	private String xLabelValue;
	private String yLabelValue;
	private int index = 0;// 当前选中的线，默认是第一条
	private int index1;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	private List<Value> list = new ArrayList<Value>();
	private StartViewInfo startViewInfo;
	private FrameLayout mFrameLayout;
	private RelativeLayout mRelativeLayout;
	private CircleImageView ImageCircle;

	private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	private XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	private GraphicalView graview;
	private LineView mLine;
	private TextView xLabel;
	private TextView yLabel;
	private String[] coloritem;
	private TextView title;
	private boolean move = false;
	private float dpi;

	public ArrayList<Ctlm1347> ctlm1347List = new ArrayList<Ctlm1347>() ;
	public BusinessData ctlm1345List;
	private BusinessParam businessParam =  new BusinessParam(); //当前界面上参数
	private String id_node;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public HJLineChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
//		setAttribute(items);
	}

	public HJLineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
//		setAttribute(items);
	}

	public HJLineChart(Context context) {
		super(context);
		initView();
//		setAttribute(items);
	}

	public HJLineChart(Context context, WidgetClass items,
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
		// 设置宽度和高度
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjlinechart, null);
		layout = (LinearLayout) view.findViewById(R.id.hj_line_chart);
		xLabel = (TextView) view.findViewById(R.id.labelX);
		yLabel = (TextView) view.findViewById(R.id.labelY);
		mLine = (LineView) view.findViewById(R.id.line);
		title = (TextView) view.findViewById(R.id.hj_linechar_title);
		ImageCircle = (CircleImageView) view.findViewById(R.id.hj_line_circle);
		ImageCircle.setOnClickListener(this);
		mFrameLayout = (FrameLayout) view.findViewById(R.id.frame_linechart);
		mRelativeLayout = (RelativeLayout) view.findViewById(R.id.hj_relative_linechart);
		mRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(graview != null){
					for(int i = 0;i<renderer.getSeriesRendererCount();i++){
						boolean flag = renderer.getSeriesRendererAt(i).isDisplayChartValues();
						renderer.getSeriesRendererAt(i).setDisplayChartValues(!flag);
					}
					graview.repaint();
				}
				
			}
		});
		width = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getWidth();
		height = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay().getHeight() / 3;
		layout.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		android.view.ViewGroup.LayoutParams lp = layout.getLayoutParams();
		lp.width = width;
		lp.height = height;
		DisplayMetrics outMetrics = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		dpi = outMetrics.density;
		ImageCircle.setOnTouchListener(new OnTouchListener() {
			float lastX;
			float lastY;
			float firstX;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = event.getRawX();
					lastY = event.getRawY();
					firstX = lastX;
					move = false;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) (event.getRawX() - lastX);
					int dy = (int) (event.getRawY() - lastY);
					int left = ImageCircle.getLeft() + dx;
					int right = ImageCircle.getRight() + dx;
					int top = ImageCircle.getTop();
					int buttom = ImageCircle.getBottom();
					if (left < 0) {
						left = 0;
						right = left + ImageCircle.getWidth();
					}
					if (left > width) {
						right = width;
						left = right - ImageCircle.getWidth();
					}
					ImageCircle.layout(left, top, right, buttom);
					ImageCircle.setX(left);
					ImageCircle.invalidate();
					processPointShow(left);
					mLine.setX((int)(left + ImageCircle.getWidth()/2+35));
					mLine.layout((int)(left + ImageCircle.getWidth()/2+35), 0, (int)(left + ImageCircle.getWidth()+35), height);
					mLine.invalidate();
					lastX = event.getRawX();
					lastY = event.getRawY();
					move = true;
					break;
				case MotionEvent.ACTION_UP:
					float finalX = event.getRawX();
					if(finalX == firstX &&move) {
						move = true;
					} 
					if(finalX != firstX){
						move = true;
					} else {
						move = false;
					}
//					Log.i(TAG,"onTouch up "+move);
					break;
				default:
					break;
				}
				return false;
			}
		});
		mFrameLayout.setOnTouchListener(new OnTouchListener() {
			private double x;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = event.getX();

					if (x < 70) {
						x = 70;
					}
					if (x > width - 20) {
						x = width - 20;
					}
					mLine.setX((int) x);
					mLine.layout((int) x, 0, (int) (x + 5), height);
					mLine.invalidate();
					ImageCircle.setX((int) (x - 35-ImageCircle.getWidth()/2));
					ImageCircle.layout((int) (x - 35-ImageCircle.getWidth()/2), ImageCircle.getTop(),
							(int) (x-35 + ImageCircle.getWidth()/2),
							ImageCircle.getBottom());
					ImageCircle.invalidate();
					processPointShow((int) x - 70);
					break;
				case MotionEvent.ACTION_MOVE:
					x = event.getX();
					if (x < 70) {
						x = 70;
					}
					if (x > width - 20) {
						x = width - 20;
					}
					processPointShow((int) x - 70);
					mLine.setX((int) x);
					mLine.layout((int) x, 0, (int) (x + 5), height);
					mLine.invalidate();
					ImageCircle.setX((int)(x - 35-ImageCircle.getWidth()/2));
					ImageCircle.layout((int)(x - 35-ImageCircle.getWidth()/2), ImageCircle.getTop(),
							(int)(x - 35+ImageCircle.getWidth()/2),
							ImageCircle.getBottom());
					ImageCircle.invalidate();
					break;
				default:
					break;
				}
				return true;
			}
		});
		addView(view);
	}

	protected void processPointShow(int x) {
		int lables = getXLabels();
		if (lables == 0) {
			return;
		}
		double[] points = new double[lables];
		points[0] = 0;
		// points对应的progress
		for (int i = 1; i < lables; i++) {
			points[i] = points[i - 1] + (width - 70) / (lables - 1);
		}
		int index;
		for (index = 0; index < lables; index++) {
			if (x <= points[index])
				break;
		}
		setXYLableValue(index);

	}
	//TODO
	public void setAttribute(WidgetClass items,String json) {
		// 获取颜色
		String colors = items.attribute.colors;
		if (colors != null) {
			Matcher m = p.matcher(colors);
			colors = m.replaceAll("");
		}
		coloritem = colors.split(";");
		int[] color = new int[coloritem.length];
		for (int i = 0; i < color.length; i++) {
			color[i] = Color.parseColor(coloritem[i]);
		}
		PointStyle[] pointStyles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND, PointStyle.SQUARE };
//		decompositionData(items.defaultValue);
		decompositionData(json);
		//对list中的数据进行排序
		for(int i = 0;i<list.size();i++){
			Value value = list.get(i);
			Collections.sort(value.dataList,new Sort());
		}
		setRender(color);
		setChartSettings();
		buildDateSet();
		if(dataset.getSeriesCount() == renderer.getSeriesRendererCount() && dataset.getSeriesCount()!=0) {
		graview = ChartFactory
				.getLineChartView(getContext(), dataset, renderer);
		}
		mLine.setVisibility(VISIBLE);
		mLine.setX((int)(70));
		mLine.setColors(color[0]);
		mLine.layout(70, 0, 75, height);
		mLine.invalidate();
		// 默认的图标显示第一个月份的
		setXYLableValue(0);
		// 获取x轴坐标的距离
		title.setVisibility(VISIBLE);
		title.setText(items.name);
		ImageCircle.setX((int)(35-10*dpi));
		ImageCircle.layout((int)(35-10*dpi), 0, (int)(35+10*dpi), (int) (20*dpi));
		ImageCircle.invalidate();
		GradientDrawable drawable = (GradientDrawable) ImageCircle.getBackground();
		drawable.setStroke(1, color[0]);
		layout.addView(graview);

		
		
	}
	
	private class Sort implements Comparator<Map<String, Object>> {

		@Override
		public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
			int first = Integer.parseInt((String) lhs.get(SORT));
			int second = Integer.parseInt((String) rhs.get(SORT));
			if(first > second) 
				return 1;
			return -1;
		}
		
	}

	private void setXYLableValue(int indexItem) {
		index1 = indexItem;
		Value v = list.get(index);
		xLabel.setVisibility(VISIBLE);
		xLabelValue = v.id + ":" + "<br>" + "<font color="
				+ coloritem[index % coloritem.length] + ">"
				+ (String) v.dataList.get(indexItem).get(NAME) + "</font>";
		yLabelValue = items.attribute.yaxisname + ":" + "<br>";

		yLabelValue += "<font color="
				+ coloritem[index % coloritem.length]
				+ ">"
				+ String.valueOf(list.get(index).dataList.get(indexItem).get(
						QTY)) + "</font>" + "<br>";
		yLabel.setVisibility(VISIBLE);
		xLabel.setText(Html.fromHtml(xLabelValue));
		yLabel.setText(Html.fromHtml(yLabelValue));
	}

	private void buildDateSet() {
		// 构造x和y的数据
		dataset.clear();
		String[] title = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			title[i] = list.get(i).id;
		}
		for (int i = 0; i < list.size(); i++) {
			XYSeries series = new XYSeries(title[i]);
			List<Map<String, Object>> dataMap = list.get(i).dataList;
			int length = dataMap.size();
			double[] xValue = new double[length];
			double[] yValue = new double[length];
			for (int j = 0; j < length; j++) {
				xValue[j] = j;
				yValue[j] = (Double) dataMap.get(j).get(QTY);
			}
			for (int j = 0; j < length; j++) {
				series.add(xValue[j], yValue[j]);
			}
			dataset.addSeries(series);
		}

	}

	private void decompositionData(String data) {
		list.clear();
		try {
			JSONArray jsonArray = new JSONArray(data);
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
		for(Value v:list){
//			Log.i(TAG,"the val is "+v);
		}
	}

	private Value getFromListValue(String id2) {
		for (Value v : list) {
			if (v.id.equals(id2))
				return v;
		}
		return null;
	}

	private void setChartSettings() {
		// renderer.setChartTitle(items.name);
		renderer.setChartTitleTextSize(30);
		// renderer.setXTitle(items.attribute.xaxisname);
		// renderer.setYTitle(items.attribute.yaxisname);
		renderer.setAxisTitleTextSize(35);
		renderer.setPanEnabled(false);
		renderer.setApplyBackgroundColor(true);
		renderer.setZoomButtonsVisible(false);
		renderer.setZoomEnabled(false, false);
		renderer.setDisplayValues(true);
		renderer.setMarginsColor(getContext().getResources().getColor(
				R.color.white));
		renderer.setBackgroundColor(getContext().getResources().getColor(
				R.color.white));
		// 设置y轴的份数,1000/200
		renderer.setYAxisMax(items.attribute.yaxismaxvalue);
		renderer.setYAxisMin(items.attribute.yaxisminvalue);
		int num = (items.attribute.yaxismaxvalue - items.attribute.yaxisminvalue)
				/ items.attribute.yaxistick;
		renderer.setYLabels(0);
		renderer.setYLabels(num);
		renderer.setXLabels(0);
		renderer.setLabelsColor(Color.LTGRAY);
		renderer.setAxesColor(Color.LTGRAY);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);

		// 设置x轴的名称
		Value v = getXCount();
		for (int i = 0; i < v.dataList.size(); i++) {
			renderer.addXTextLabel(i, (String) v.dataList.get(i).get(NAME));
		}
	}

	private int getXLabels() {
		int max = 0;
		for (Value v : list) {
			max = Math.max(max, v.dataList.size());
		}
		return max;
	}

	private Value getXCount() {
		int x = 0;
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			List<Map<String, Object>> dataList = list.get(i).dataList;
			if (x < dataList.size()) {
				x = dataList.size();
				index = i;
			}
		}
		return list.get(index);
	}

	private void setRender(int[] color) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setShowLegend(false);
		renderer.setMargins(new int[] { 40, 70, 15, 20 });
		for (int i = 0; i < list.size(); i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(color[i % color.length]);
			r.setPointStyle(PointStyle.CIRCLE);
			r.setLineWidth(5);
			r.setChartValuesTextSize(40);
			r.setDisplayBoundingPoints(true);
			renderer.addSeriesRenderer(r);
		}
	}

	@Override
	public void setValue(String msg) {

	}

	@Override
	public void setValueDefault() {

	}

	@Override
	public void setJesonValue(String msg) {
		// BI图不支持非json数据
		list.clear();
		dataset.clear();
		renderer.removeAllRenderers();
//		decompositionData(msg);
		buildDateSet();
		int[] color = new int[coloritem.length];
		for(int i = 0;i<color.length;i++) {
			color[i] = Color.parseColor(coloritem[i]);
		}
		setRender(color);
		if(graview!= null){
			graview.repaint();
		}
		
	}

	private class Value {
		String id; // 表明数据的id，一个id可能对应多个数据
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(); // 上述id对应的数据，大小代表有多少數據
		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer(id+" ");
			for(Map<String, Object> map:dataList){
				buf.append(map+"");
			}
			return buf.toString();
		}
	}

	
	@Override
	public void onClick(View v) {
		if (list.size() == 0) {
			// 表明没有数据，请直接返回
			return;
		}
		switch (v.getId()) {
		case R.id.hj_line_circle:
			if (!move) {
				index = (index + 1) % list.size();
				// 改变线条的颜色
				for (int i = 0; i < renderer.getSeriesRendererCount(); i++) {
					XYSeriesRenderer r = (XYSeriesRenderer) renderer
							.getSeriesRendererAt(i);
					if (i == index) {
						r.setLineWidth(8);
					} else {
						r.setLineWidth(5);
						r.setColor(Color.parseColor(coloritem[i
								% coloritem.length]));
					}
				}
				graview.repaint();
				int color = Color
						.parseColor(coloritem[index % coloritem.length]);
				mLine.setColors(color);
				mLine.setBackgroundColor(color);
				GradientDrawable drawble = (GradientDrawable) ImageCircle
						.getBackground();
				ImageCircle.setColors(color);
				drawble.setStroke(1, color);
				setXYLableValue(index1);
			}
			break;
		default:
			break;
		}
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
	public void setValue(String row, String column, String value) {
		
	}

	@Override
	public String getValue(String row, String column) {
		return "";
	}

 
 

	@Override
	public String getRowCount() {
		return null;
	}

	@Override
	public String getCurrentRow() {
		return null;
	}
	//TODO
	@Override
	public void setDataBuild(Boolean flag ,
			BusinessData ctlm1345List) {
	
//		this.ctlm1347List = ctlm1347List;
		if (flag) {
			 
		     ctlm1347List = BusinessBaseDao. 
					 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id) ;
			 
			  if (ctlm1347List != null)
			  {
				  setValue(ctlm1347List); 
			  }
		} else if (ctlm1345List != null && !flag ) {
			this.ctlm1345List = ctlm1345List;
			if (ctlm1345List.getId_table().equalsIgnoreCase(items.attribute.datasource)) {

				BusinessBaseDao.deletesrcctlm1347(businessParam.getBillNo(),businessParam.getViewId(),businessParam.getIdParentNode(), items.id);
				setctlm1345(ctlm1345List.getVar_values());
			}

		}
	}
	
	public void setValue(ArrayList<Ctlm1347> ctlm1347Lista) {

		for (int i = 0; i < ctlm1347Lista.size(); i++) {

			if (items.id.trim().equalsIgnoreCase(
					ctlm1347Lista.get(i).getId_srcnode().trim())) {
				ctlm1347List.add(ctlm1347Lista.get(i));
			
			}
		}
		
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
	 	    ctlm1347.setId_node( id_node ); //
			ctlm1347.setName_node(items.name);
			ctlm1347.setVar_billno(businessParam.getBillNo());
			ctlm1347.setId_model(businessParam.getModelId() );
			ctlm1347.setId_nodetype(WidgetName.HJ_LINE_CHART); 
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

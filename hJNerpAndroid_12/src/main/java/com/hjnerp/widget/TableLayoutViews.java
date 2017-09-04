package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hjnerp.common.EapApplication;
import com.hjnerp.util.TableCell;
import com.hjnerpandroid.R;

public class TableLayoutViews extends TableLayout {
	
	private List<List<TableCell>> list;
	private int rows; // 行数

	public TableLayoutViews(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param context
	 *            传入的上下文
	 * @param list
	 *            总的数据，其中每一项代表一行
	 */

	public TableLayoutViews(Context context, List<List<TableCell>> list) {
		super(context);
		this.list = list;
		rows = list.size();
		setStretchAllColumns(true); 
//		this.setColumnShrinkable(0, true);
//		 this.setShrinkAllColumns(true);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.width = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
		setLayoutParams(params);
		hideSoftInput(context);
		initView();

	}

	private void hideSoftInput(Context context) {
		if (context instanceof Activity) {
			((Activity) context)
					.getWindow()
					.setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
									| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		}

	}

	private void initView() {
		// 构造列的名称
		List<String> columnNames = getColumnsName();

		// 构造第一行的名称显示
		TableRow tableRow = new TableRow(getContext());
		for (int i = 0, p = columnNames.size(); i < p; i++) {
			TextView textView = new TextView(getContext());
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.drawable.text_back);
			textView.setText(columnNames.get(i));
		
			( textView).setTextColor(Color
					.parseColor("#FFFFFF"));
			tableRow.addView(textView);
			 
			
//				textView.setBackgroundColor(getResources().getColor(R.color.hjgridheader));
		}
		addView(tableRow);
		// 构造各行的数据填充到
		for (int rowindex = 0; rowindex < rows; rowindex++) {
			List<TableCell> cellList = list.get(rowindex);
			TableRow tableRow1 = addValue2Table(rowindex, cellList);

			if (null != tableRow1) {
				addView(tableRow1);
			}
		}

	}
    public static String ToSBC(String input) { 
        char c[] = input.toCharArray(); 
        for (int i = 0; i < c.length; i++) { 
            if (c[i] == ' ') { 
                c[i] = '\u3000'; 
            } else if (c[i] < '\177') { 
                c[i] = (char) (c[i] + 65248); 
            } 
        } 
        return new String(c); 
    }  
	private TableRow addValue2Table(int rowindex, List<TableCell> list) {
		View view = null;
		 
		int pos = 0 ; 
		TableRow tableRow = new TableRow(getContext());
		tableRow.setBackgroundResource(R.drawable.text_back2);
		for (TableCell cell : list) {
			
			if (EapApplication.TEXT_TYPE.equals(cell.getType())) {
				view = new TextView(getContext());
				  String cc=cell.getValue(); 
//				String cc =  "列的宽度由该列所有行中最宽的一个单元格决定.不过表格布局可以通过shrinkColumns和 stretchColumns两个属性来标记某些列可以收缩或可以拉伸. 如果标记为可以收缩";
				((TextView) view).setText(cc);
//				((TextView) view).setMinEms(20); 
//				((TextView) view).setLayoutParams(new LayoutParams( 
//						LayoutParams.MATCH_PARENT, 
//						LayoutParams.MATCH_PARENT));  
//				if ( cc.length() >40 )
//				{
//					((TextView) view).setWidth(800); 
//				}
				((TextView) view).setHeight(LayoutParams.MATCH_PARENT);
//				((TextView) view).setText(cell.getValue());
				((TextView) view).setGravity(Gravity.LEFT);
				((TextView) view).setSingleLine(false);
//				((TextView) view).setMaxLines(100);     
//				((TextView) view).setHorizontalScrollBarEnabled(false); 
				((TextView) view).setTextSize(getContext().getResources().getInteger(
						R.integer.TextSizeMedium));
			 
//				if (pos ==0 )
//			    { ((TextView) view).setBackgroundResource(R.drawable.text_back2);}
//				else
//				{
					((TextView) view).setBackgroundResource(R.drawable.text_back3);
//					}
			   //((TextView) view).setBackgroundColor( "#"); 
				 
			}
			if (EapApplication.EDIT_TYPE.equals(cell.getType())) {
				view = new EditText(getContext());
				((EditText) view).setGravity(Gravity.CENTER);
				((TextView) view).setTextSize(getContext().getResources().getInteger(
						R.integer.TextSizeMedium));
				if (pos ==0 )
			    { ((TextView) view).setBackgroundResource(R.drawable.text_back2);}
				else
				{ ((TextView) view).setBackgroundResource(R.drawable.text_back3);}
			}
		 
			pos ++;
			
			if (view != null) {
				tableRow.addView(view);
			}
		}
		return tableRow;

	}

	private List<String> getColumnsName() {
		// TODO Auto-generated method stub
		List<String> colummNames = new ArrayList<String>();
		List<TableCell> rowItem = list.get(0); // 取出第一行的所有数据
		for (TableCell cell : rowItem) {
			colummNames.add(cell.getName());
		}
		return colummNames;
	}

}

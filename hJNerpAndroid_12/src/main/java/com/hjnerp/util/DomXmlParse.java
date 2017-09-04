package com.hjnerp.util;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetAttribute;
import com.hjnerp.business.view.WidgetAttributeName;
import com.hjnerp.business.view.WidgetClass;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DomXmlParse {

	public static final String VIEW_TAG = "view";
	public static List<ViewClass> views;
	public static StartViewInfo startViewInfo;
	public static String DATA_ELEMENT = "data"; // 数据源标签

	/*
	 * 解析xml
	 */
	private static void parseXml(InputStream in) throws SAXException,
			IOException, ParserConfigurationException {
		views = new ArrayList<ViewClass>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(in);

		NodeList startlist = document.getElementsByTagName("HJModel");
		startViewInfo = new StartViewInfo();
		Node viewitem1 = startlist.item(0);
		startViewInfo.start = ((Element) viewitem1)
				.getAttribute(StartViewInfo.START);
		startViewInfo.type = ((Element) viewitem1)
				.getAttribute(StartViewInfo.TYPE);
		startViewInfo.id = ((Element) viewitem1).getAttribute(StartViewInfo.ID);
		startViewInfo.billnotype = ((Element) viewitem1)
				.getAttribute(StartViewInfo.BILLONTYPE);

		// 取出所有的界面显示
		NodeList nodelist = document.getElementsByTagName("HJView");
		// 针对每个界面进行处理
		for (int i = 0; i < nodelist.getLength(); i++) {
			// NodeList nodeList
			Node viewitem = nodelist.item(i); // HJView下面的一个HJView，<HJView id =
												// "005"
			ViewClass viewclass = new ViewClass();
			viewclass.name = ((Element) viewitem)
					.getAttribute(WidgetAttributeName.NAME_ATTRIBUTE);
			// Log.e("xml","viewclass.name: " + viewclass.name);
			viewclass.id = ((Element) viewitem)
					.getAttribute(WidgetAttributeName.ID_ATTRIBUTE);
			// Log.e("xml","viewclass.id: " + viewclass.id);
			// viewclass.returnenable = ((Element)
			// viewitem).getAttribute(WidgetAttributeName.RETURNENABLE_ATTRIBUTE);
			viewclass.returnenable = Boolean.parseBoolean(((Element) viewitem)
					.getAttribute(WidgetAttributeName.RETURNENABLE_ATTRIBUTE));

			viewclass.dataset = ((Element) viewitem)
					.getAttribute(WidgetAttributeName.DATASET_ATTRIBUTE);
			// Log.e("xml","viewclass.dataset: " + viewclass.dataset);
			viewclass.datasetmode = ((Element) viewitem)
					.getAttribute(WidgetAttributeName.DATASETMODE_ATTRIBUTE);
			// Log.e("xml","viewclass.datasetmode: " + viewclass.datasetmode);

			viewclass.condition = ((Element) viewitem)
					.getAttribute(WidgetAttributeName.CONDITION_ATTIRBUTE);

			viewclass.onload = ((Element) viewitem)
					.getAttribute(WidgetAttributeName.ONLOAD_ATTIRBUTE);
			viewclass.screendisply = ((Element) viewitem)
					.getAttribute(WidgetAttributeName.SCREENDISPLY_ATTIRBUTE);
			// presave字段表示控件是否默认保存数据，只有当定义为true的时候才保存，不定义或定义为false不默认保存
			if (((Element) viewitem)
					.getAttribute(WidgetAttributeName.PRESAVE_ATTIRBUTE) == null) {
				viewclass.presave = true;
			} else {
				viewclass.presave = Boolean.parseBoolean(((Element) viewitem)
						.getAttribute(WidgetAttributeName.PRESAVE_ATTIRBUTE));
			}

			if ("query".equalsIgnoreCase(startViewInfo.type)) {
				viewclass.presave = false;
			}

			if (((Element) viewitem)
					.getAttribute(WidgetAttributeName.BACKSAVE_ATTRIBUTE) == null) {
				viewclass.backsave = false;
			} else {
				viewclass.backsave = Boolean.parseBoolean(((Element) viewitem)
						.getAttribute(WidgetAttributeName.BACKSAVE_ATTRIBUTE));
			}

			if (((Element) viewitem)
					.getAttribute(WidgetAttributeName.BACKDEL_ATTRIBUTE) == null) {
				viewclass.backdel = false;
			} else {
				viewclass.backdel = Boolean.parseBoolean(((Element) viewitem)
						.getAttribute(WidgetAttributeName.BACKDEL_ATTRIBUTE));
			}

			list(viewitem, viewclass);
			views.add(viewclass);
		}
	}

	/*
	 * 从xml中获取全部view
	 */
	public static List<ViewClass> getViews(InputStream in) throws SAXException,
			IOException, ParserConfigurationException {
		if (views == null) {
			parseXml(in);
		}
		return views;
	}

	/*
	 * 从xml中获取启动view的信息
	 */
	public static StartViewInfo getStartViewInfo(InputStream in)
			throws SAXException, IOException, ParserConfigurationException {
		parseXml(in);
		return startViewInfo;
	}

	private static void list(Node item, ViewClass viewclass) {
		NodeList ll = item.getChildNodes();
		List<WidgetClass> list = new ArrayList<WidgetClass>(); // 子属性的列表
		for (int i = 0; i < ll.getLength(); i++) {
			Node temp = ll.item(i);// 某一个具体的控件，<HJRadioButton id = "005016"
			if (temp instanceof Element) {
				WidgetClass widgetitem = new WidgetClass();
				widgetitem.widgetType = temp.getNodeName();

				// 取文本
				// String content = temp.getTextContent();
				String content = null;
				if (temp.hasChildNodes()) {
					if (temp.getLastChild().getNodeType() == Node.TEXT_NODE)
						content = temp.getLastChild().getNodeValue();
				}
				// Log.e(TAG,">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> content defaultValue is "
				// + content);
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				if (content != null) {
					Matcher m = p.matcher(content);
					if (m != null) {
						content = m.replaceAll("");
					}
				}
				widgetitem.defaultValue = content;

				NamedNodeMap attrs = temp.getAttributes();
				widgetitem.id = ((Element) temp)
						.getAttribute(WidgetAttributeName.ID_ATTRIBUTE);
				widgetitem.name = ((Element) temp)
						.getAttribute(WidgetAttributeName.NAME_ATTRIBUTE);
				WidgetAttribute attribute = new WidgetAttribute();
				setWidgetAttribute(attrs, attribute);
				widgetitem.attribute = attribute;
				if (temp.hasChildNodes()) {
					setHJRadioButtonOptionAttribute(temp, widgetitem);
				}
				list.add(widgetitem);
			}
			// else if(temp.getNodeType()==Node.TEXT_NODE) {
			// Log.e(TAG,"____________________>");
			// // if(temp.getNodeValue()!="/0")
			// if(temp.hasChildNodes())
			// Log.e(TAG,"___________________________ content defaultValue is "
			// + temp.getLastChild().getNodeValue());
			// }
		}
		viewclass.list = list;
	}

	private static void setHJRadioButtonOptionAttribute(Node temp,
			WidgetClass widigetitem) {
		List<WidgetClass> HJRadioButtonOption = new ArrayList<WidgetClass>();
		NodeList childnodes = temp.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); i++) {
			Node item = childnodes.item(i);
			if (item instanceof Element) {
				WidgetClass HJRadioButtonWidget = new WidgetClass();

				HJRadioButtonWidget.widgetType = item.getNodeName();
				HJRadioButtonWidget.id = ((Element) item)
						.getAttribute(WidgetAttributeName.ID_ATTRIBUTE);
				HJRadioButtonWidget.name = ((Element) item)
						.getAttribute(WidgetAttributeName.NAME_ATTRIBUTE);

				// 取文本
				String content = null;
				// if(temp.hasChildNodes()){
				// if(temp.getLastChild().getNodeType() == Node.TEXT_NODE)
				// content = temp.getLastChild().getNodeValue();
				// }
				// Log.e(TAG,">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> content defaultValue is "
				// + content);

				content = item.getTextContent();
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				if (content != null) {
					Matcher m = p.matcher(content);
					if (m != null) {
						content = m.replaceAll("");
					}
				}

				HJRadioButtonWidget.defaultValue = content;

				NamedNodeMap attrs = item.getAttributes();
				WidgetAttribute attribute = new WidgetAttribute();
				setWidgetAttribute(attrs, attribute);
				HJRadioButtonWidget.attribute = attribute;
				HJRadioButtonOption.add(HJRadioButtonWidget);
			}
		}
		widigetitem.HJRadioButtonOption = HJRadioButtonOption;

	}

	private static void setWidgetAttribute(NamedNodeMap attrs,
			WidgetAttribute attribute) {
		for (int i = 0; i < attrs.getLength(); i++) {
			Attr attr = (Attr) attrs.item(i);
			setWidgetDetailAttribute(attr, attribute);
		}

	}

	private static void setWidgetDetailAttribute(Attr attr,
			WidgetAttribute attribute) {
		String name = attr.getName();
		String value = attr.getValue();
		if (WidgetAttributeName.EDITABLE_ATTRIBUTE.equals(name)) {
			attribute.editable = Boolean.parseBoolean(value);
			return;
		}
		if (WidgetAttributeName.FONT_SIZE_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.fontsize = value;
			return;
		}

		if (WidgetAttributeName.REQUIRED_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.required = Boolean.parseBoolean(value);
			return;
		}
		if (WidgetAttributeName.SINGLE_LINE_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.singleline = Boolean.parseBoolean(value);
			return;
		}
		if (WidgetAttributeName.NEXT_VIEW_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.nextview = value;
			return;
		}
		if (WidgetAttributeName.EDITABLE_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.editable = Boolean.parseBoolean(value);
		}
		if (WidgetAttributeName.VISIBLE_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.visible = Boolean.parseBoolean(value);
		}
		if (WidgetAttributeName.WIDTH_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.width = (Double.parseDouble(value)); // 百分比
		}
		if (WidgetAttributeName.BOLD_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.bold = Boolean.parseBoolean(value);
		}
		if (WidgetAttributeName.TEXTCOLOR_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.textcolor = value;
		}
		if (WidgetAttributeName.LOCKING_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.locking = Integer.parseInt(value); // 固定的列数
		}

		if (WidgetAttributeName.DATASOURCE_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.datasource = value; // 针对表格或者list添加的默认值
		}
		if (WidgetAttributeName.FIELD_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.field = value; // 针对表格或者list添加的默认值
		}
		if (WidgetAttributeName.ALIGNMENT_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.alignment = value; // 对齐方式
		}
		if (WidgetAttributeName.BACKVIEW_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.backview = value; // 是否可以返回
		}
		if (WidgetAttributeName.VISIBLENAME_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.visiblename = Boolean.parseBoolean(value);
		}
		if (WidgetAttributeName.DBFIELD_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.dbfield = value; // 是否可以返回
		}
		if (WidgetAttributeName.VIEWPARM_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.viewparm = value; // 是否可以返回
		}
		if (WidgetAttributeName.HEIGHT_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.height = Double.parseDouble(value);
		}

		if (WidgetAttributeName.VALUETYPE_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.valuetype = value; // 是否可以返回
		}
		if (WidgetAttributeName.FORMAT_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.format = value; // 输出格式
		}

		if (WidgetAttributeName.LENGTHLIMIT_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.lengthlimit = (Integer.parseInt(value)); // 是否多行
		}

		if (WidgetAttributeName.BACKGROUNDCOLOR_ATTIRBUTE
				.equalsIgnoreCase(name)) {
			attribute.backgroundcolor = value; // 背景颜色

		}
		if (WidgetAttributeName.VISIBLEDISCLOSURE_ATTRIBUTE
				.equalsIgnoreCase(name)) {
			attribute.visibledisclosure = Boolean.parseBoolean(value);
		}// HjListLayout是否显示右侧箭头
			// 是否显示 上传标起
		if (WidgetAttributeName.UPLOAD_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.upload = Boolean.parseBoolean(value);
		}

		// 折线的相关属性
		if (WidgetAttributeName.COLORS_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.colors = value; // 折线的颜色集合
		}
		if (WidgetAttributeName.CHARTFIELD_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.chartfield = value;
		}
		if (WidgetAttributeName.VALUEFIELD_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.valuefield = value;
		}
		if (WidgetAttributeName.XAXISNAME_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.xaxisname = value;
		}
		if (WidgetAttributeName.YAXISNAME_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.yaxisname = value;
		}
		if (WidgetAttributeName.XAXISFIELD_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.xaxisfield = value;
		}
		if (WidgetAttributeName.YAXISMAXVALUE_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.yaxismaxvalue = Integer.parseInt(value);
		}
		if (WidgetAttributeName.YAXISMINVALUE_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.yaxisminvalue = Integer.parseInt(value);
		}
		if (WidgetAttributeName.YAXISTICK_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.yaxistick = Integer.parseInt(value);
		}
		if (WidgetAttributeName.VISIBLELEGEND_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.visiblelegend = Boolean.parseBoolean(value);
		}
		if (WidgetAttributeName.SORTFIELD_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.sortfield = value;
		}

		if (WidgetAttributeName.ONCLICK_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.onclick = value;
		}

		if (WidgetAttributeName.VISIBLEDISCLOSURE_ATTRIBUTE
				.equalsIgnoreCase(name)) {
			attribute.visibledisclosure = Boolean.parseBoolean(value);
		}

		if (WidgetAttributeName.SUMENABLE_ATTIRBUTE.equalsIgnoreCase(name)) {
			attribute.sumenable = Boolean.parseBoolean(value);
		}

		if (WidgetAttributeName.STYLE_ATTIRBUTE.equals(name)) {
			attribute.style = value;
			return;
		}
		if (WidgetAttributeName.LAYOUTTYPE_ATTIRBUTE.equals(name)) {
			attribute.layouttype = value;
			return;
		}
		if (WidgetAttributeName.COLUMNTYPE_ATTIRBUTE.equals(name)) {
			attribute.columntype = value;
			return;
		}

		if (WidgetAttributeName.DELROW_ATTRIBUTE.equalsIgnoreCase(name)) {
			attribute.delrow = Boolean.parseBoolean(value);
			return;
		}

	}
}

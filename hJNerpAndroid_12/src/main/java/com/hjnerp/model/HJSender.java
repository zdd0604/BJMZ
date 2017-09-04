package com.hjnerp.model;

import java.io.Serializable;

public class HJSender implements Serializable{
	public String row;
	public String col;
	public String colid;
	public String values;
	public String billno;
	public String nodeid;
	public String oprdate;
	public final static String ROW = "row";//行
	public final static String COL = "col";//列
	public final static String COLID ="colid";
	public final static String VALUES = "values";
	public final static String BILLNO = "billno";
	public final static String NODEID = "nodeid";
	public final static String OPRDATE = "oprdate";
}

package com.hjnerp.model;

import java.io.Serializable;
import java.util.Map;

public class BusinessParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  ModelId;
	private String  IdParentNode;
	private String  IdSrcnode; 
	private String  BillNo;
	private Map<String ,String > mapparam; 
	private String  ViewId; 
	private String  DataOpr; 
	private String VarJosn;
	
	public String getVarJosn() {
		return VarJosn;
	}
	public void setVarJosn(String varJosn) {
		VarJosn = varJosn;
	}
	
	private String Table;
	
	public String getTable() {
		return Table;
	}
	public void setTable(String table) {
		Table = table;
	}
	
	public String getViewId() {
		return ViewId;
	}
	public void setViewId(String viewId) {
		ViewId = viewId;
	}
	public String getModelId() {
		return ModelId;
	}
	public void setModelId(String modelId) {
		ModelId = modelId;
	}
	public String getIdParentNode() {
		return IdParentNode;
	}
	public void setIdParentNode(String idParentNode) {
		IdParentNode = idParentNode;
	}
	 
	public Map<String, String> getMapparam() {
		return mapparam;
	}
	public void setMapparam(Map<String, String> mapparam) {
		this.mapparam = mapparam;
	}
	
	public void setMapparamput( String key , String values ) {
		this.mapparam.put( key, values);
	}
	public String getIdSrcnode() {
		return IdSrcnode;
	}
	public void setIdSrcnode(String idSrcnode) {
		IdSrcnode = idSrcnode;
	}
	public String getBillNo() {
		return BillNo;
	}
	public void setBillNo(String billNo) {
		BillNo = billNo;
	}
	
	public String getDataOpr() {
		return DataOpr;
	}
	public void setDataOpr(String dataopr) {
		DataOpr = dataopr;
	}
	
	@Override
	public String toString() {
		return ModelId+" "+BillNo+" "+IdParentNode +" "+IdSrcnode+" "+mapparam+" "+ViewId+" "+DataOpr;
	}
	
}

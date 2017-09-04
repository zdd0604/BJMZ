package com.hjnerp.model;

import java.util.List;
import java.util.Map;

public class WorkflowDetailData {
	public String error_code;
	public String msg;
//	public List<WorkflowDetailInfo> tables;
	public List<Map<String, List<WorkflowDetailInfo>>> tables;
}

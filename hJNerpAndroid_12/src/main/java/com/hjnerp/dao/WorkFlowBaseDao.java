package com.hjnerp.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.hjnerp.db.SQLiteWorker.AbstractSQLable;
import com.hjnerp.db.Tables;
import com.hjnerp.model.WorkFlowRecorderInfo;
import com.hjnerp.model.WorkflowListInfo;

/**
 * 
 * 
 * WorkFlow  manipulate
 * 
 * */
public class WorkFlowBaseDao extends BaseDao {
	
	//TODO 工作流列表
	/**插入或修改一批工作流列表信息*/
	public static final void replaceWorkFlowInfos(final List<WorkflowListInfo> workflowInfos){
		worker.postDML(new AbstractSQLable(){
			public Object doAysncSQL() {
				SQLiteDatabase database = beginDMLOnTransaction();
				try
				{
					ContentValues values = new ContentValues();
					for (WorkflowListInfo workflow : workflowInfos)
					{
						values.clear();
						values.put(Tables.WorkFlowList.WORK_TITLE, workflow.getTitle());
						values.put(Tables.WorkFlowList.WORK_CONTENT, workflow.getContent());
						values.put(Tables.WorkFlowList.WORK_ATTACH,workflow.getAttach());
						values.put(Tables.WorkFlowList.WORK_BILLTYPE,workflow.getBillType());
						values.put(Tables.WorkFlowList.WORK_BILLNO,workflow.getBillNo());
						values.put(Tables.WorkFlowList.WORK_DATE, workflow.getDate());
						values.put(Tables.WorkFlowList.WORK_FLAGDEAL, workflow.getFlagDeal());
						values.put(Tables.WorkFlowList.WORK_OPTTYPE,workflow.getOptType());
						values.put(Tables.WorkFlowList.WORK_USER,new Gson().toJson(workflow.getUser()));

						database.replace(Tables.WorkFlowList.NAME, null, values);
//						database.insert(Tables.WorkFlowList.NAME, null, values);
					}
				}
				finally
				{
					endDMLOffTransaction(database);
				}
				return null;
			}
			public void onCompleted(Object event) 
			{
//				if(!(event instanceof Throwable))
//				{
//					
//				}
			}});
	}
	/** 插入或修改某个工作流列表信息*/
	public static final void replaceWorkFlowInfo(WorkflowListInfo workflowinfo){
		replaceWorkFlowInfos(Arrays.asList(workflowinfo));
	}
	/** 查询工作流的信息*/
	public static final ArrayList<WorkflowListInfo> queryWorkFlowInfos(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.WorkFlowList.NAME);
		return queryWorkFlowList(sql.toString());
	}
	
	public static final void clearWorkList(){
		SQLiteDatabase database = beginDMLOnTransaction();
		database.delete(Tables.WorkFlowList.NAME, null, null);
		endDMLOffTransaction(database);
	}
	
	/** 根据是否处理查询工作流的信息*/
	public static final ArrayList<WorkflowListInfo> queryWorkFlowInfosBydealFlag(String dealFlag){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.WorkFlowList.NAME)
		.append(" where ").append(Tables.WorkFlowList.WORK_FLAGDEAL)
		.append(" = '").append(dealFlag).append("' order by ").append(Tables.WorkFlowList.WORK_DATE).append(" desc ");
		return queryWorkFlowList(sql.toString());
	}
	
	/** 根据单据类型查询工作流的信息*/
	public static final ArrayList<WorkflowListInfo> queryWorkFlowInfosBydealFlagAndBillType(String dealFlag,String billType){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.WorkFlowList.NAME)
		.append(" where ").append(Tables.WorkFlowList.WORK_FLAGDEAL)
		.append(" = '").append(dealFlag).append("' and ").append(Tables.WorkFlowList.WORK_BILLTYPE)
		.append(" ='").append(billType).append("' order by ").append(Tables.WorkFlowList.WORK_DATE).append(" desc ");
		return queryWorkFlowList(sql.toString());
	}
	
	/** 根据单号和类型查询工作流的信息*/
	public static final WorkflowListInfo queryWorkFlowInfosByBillnoAndTable(String billno,String table){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.WorkFlowList.NAME)
		.append(" where ").append(Tables.WorkFlowList.WORK_BILLNO)
		.append(" = '").append(billno).append("' and ").append(Tables.WorkFlowList.WORK_BILLTYPE)
		.append(" ='").append(table).append("';");
		List<WorkflowListInfo> workflowInfos = queryWorkFlowList(sql.toString());
		return workflowInfos.size()>0?workflowInfos.get(0):null;
	}
	/** 查询工作流信息*/
	private static final ArrayList<WorkflowListInfo> queryWorkFlowList(String sql)
	{
		ArrayList<WorkflowListInfo> result = new ArrayList<WorkflowListInfo>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try
		{
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				WorkflowListInfo wf = new WorkflowListInfo();
				wf.setTitle(getColumnString(cursor, Tables.WorkFlowList.WORK_TITLE));
				wf.setContent(getColumnString(cursor, Tables.WorkFlowList.WORK_CONTENT));
				wf.setAttach(getColumnString(cursor, Tables.WorkFlowList.WORK_ATTACH));
				wf.setBillType(getColumnString(cursor, Tables.WorkFlowList.WORK_BILLTYPE));
				wf.setBillNo(getColumnString(cursor, Tables.WorkFlowList.WORK_BILLNO));
				wf.setDate(getColumnString(cursor, Tables.WorkFlowList.WORK_DATE));
				wf.setFlagDeal(getColumnString(cursor, Tables.WorkFlowList.WORK_FLAGDEAL));
				wf.setOptType(getColumnString(cursor, Tables.WorkFlowList.WORK_OPTTYPE));
				wf.setUser(new Gson().fromJson(getColumnString(cursor, Tables.WorkFlowList.WORK_USER), WorkFlowRecorderInfo.class));			
				result.add(wf);
			}
		}
		finally
		{
			endDQL(database, cursor);
		}
		return result;
	}
}

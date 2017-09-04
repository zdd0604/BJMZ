package com.hjnerp.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.hjnerp.common.Constant;
import com.hjnerp.db.DBManager;
import com.hjnerp.db.SQLiteTemplate;
import com.hjnerp.db.SQLiteTemplate.RowMapper;
import com.hjnerp.model.WorkflowListInfo;

public class WorkManager {

	private static WorkManager workManager = null;
	private static DBManager manager = null;

	private WorkManager(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(
				Constant.LOGIN_SET, Context.MODE_PRIVATE);
		String databaseName = sharedPre.getString(Constant.USERNAME, null);
		manager = DBManager.getInstance(context, databaseName);
	}

	public static WorkManager getInstance(Context context) {

		if (workManager == null) {
			workManager = new WorkManager(context);
		}

		return workManager;
	}

	/**
	 * 查找所有已审或未审的单据类型和数量
	 */

	public ArrayList<WorkflowListInfo> getWorkListCount() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<WorkflowListInfo> list = st
				.queryForList(
						new RowMapper<WorkflowListInfo>() {
							@Override
							public WorkflowListInfo mapRow(Cursor cursor, int index) {
								WorkflowListInfo work = new WorkflowListInfo();

								work.setBillType(cursor.getString(cursor
										.getColumnIndex("work_billtype")));
								work.setTitle(cursor.getString(cursor
										.getColumnIndex("work_title")));

								work.setFlagDeal(cursor.getString(cursor
										.getColumnIndex("work_flagdeal")));

//								work.setContWork(cursor.getInt(cursor
//										.getColumnIndex("contWork")));

								return work;
							}
						},
						" select work_table ,   work_title,work_flagDeal  ,count(*)  contWork   from work_list group by work_table ,   work_title,work_flagDeal ",
						null);
		return (ArrayList<WorkflowListInfo>) list;
	}

	/**
	 * 
	 * 查找所有已审或未审的单据
	 * 
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            要查的记录条数
	 * @return
	 * @author 李庆义
	 * @update 2012-7-2 上午9:31:04
	 */
//	public List<WorkflowInfo> getWorkListAll(int workStatus, int pageNum,
//			int pageSize) {
//
//		int fromIndex = (pageNum - 1) * pageSize;
//		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
//		List<WorkflowInfo> list = st
//				.queryForList(
//						new RowMapper<WorkflowInfo>() {
//							@Override
//							public WorkflowInfo mapRow(Cursor cursor, int index) {
//								WorkflowInfo work = new WorkflowInfo();
//								work.setContext(cursor.getString(cursor
//										.getColumnIndex("work_context")));
//								work.setBillNo(cursor.getString(cursor
//										.getColumnIndex("work_billno")));
//								work.setTable(cursor.getString(cursor
//										.getColumnIndex("work_table")));
//								work.setName(cursor.getString(cursor
//										.getColumnIndex("work_name")));
//								work.setNotice(cursor.getInt(cursor
//										.getColumnIndex("work_notice")));
//								work.setStatus(cursor.getInt(cursor
//										.getColumnIndex("work_status")));
//								work.setSubhead(cursor.getString(cursor
//										.getColumnIndex("work_subhead")));
//								work.setTitle(cursor.getString(cursor
//										.getColumnIndex("work_title")));
//								work.setTo(cursor.getString(cursor
//										.getColumnIndex("work_to")));
//								String time = cursor.getString(cursor
//										.getColumnIndex("work_time"));
//								time = DateUtil.formatTimeString(time);
//
//								work.setTime(time);
//								work.setType(cursor.getString(cursor
//										.getColumnIndex("work_type")));
//								work.setAppProcess(cursor.getString(cursor
//										.getColumnIndex("work_process")));
//								return work;
//							}
//						},
//						"select work_type  ,   work_no  ,   work_subhead  ,  work_content  ,  work_status ,  work_from  ,   work_time  ,   work_process  ,   work_user  ,   work_notice ,   work_title  ,   work_name,work_to  from work_list where work_status = ? order by work_time ASC limit ? , ? ",
//						new String[] { "" + workStatus, "" + fromIndex,
//								"" + pageSize });
//		return list;
//
//	}

	/**
	 * 
	 * 查找所有已审或未审的单据
	 * 
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            要查的记录条数
	 * @return
	 * @author 李庆义
	 * @update 2012-7-2 上午9:31:04
	 */
//	public List<WorkflowInfo> getWorkListByFrom(String workType,
//			int workStatus, int pageNum, int pageSize) {
//
//		int fromIndex = (pageNum - 1) * pageSize;
//		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
//		List<WorkflowInfo> list = st
//				.queryForList(
//						new RowMapper<WorkflowInfo>() {
//							@Override
//							public WorkflowInfo mapRow(Cursor cursor, int index) {
//								WorkflowInfo work = new WorkflowInfo();
//								work.setContent(cursor.getString(cursor
//										.getColumnIndex("work_content")));
//								work.setFrom(cursor.getString(cursor
//										.getColumnIndex("work_from")));
//								work.setId(cursor.getString(cursor
//										.getColumnIndex("work_no")));
//								work.setName(cursor.getString(cursor
//										.getColumnIndex("work_name")));
//								work.setNotice(cursor.getInt(cursor
//										.getColumnIndex("work_notice")));
//								work.setStatus(cursor.getInt(cursor
//										.getColumnIndex("work_status")));
//								work.setSubhead(cursor.getString(cursor
//										.getColumnIndex("work_subhead")));
//								work.setTitle(cursor.getString(cursor
//										.getColumnIndex("work_title")));
//								work.setTo(cursor.getString(cursor
//										.getColumnIndex("work_to")));
//								String time = cursor.getString(cursor
//										.getColumnIndex("work_time"));
//								time = DateUtil.formatTimeString(time);
//								work.setTime(time);
//								work.setType(cursor.getString(cursor
//										.getColumnIndex("work_type")));
//								work.setAppProcess(cursor.getString(cursor
//										.getColumnIndex("work_process")));
//								return work;
//							}
//						},
//						"select work_type  ,   work_no  ,   work_subhead  ,  work_content  ,  work_status ,  work_from  ,   work_time  ,   work_process  ,   work_user  ,   work_notice ,   work_title  ,   work_name,work_to  from work_list where work_type = ? and work_status = ? order by work_time ASC limit ? , ? ",
//						new String[] { workType, "" + workStatus,
//								"" + fromIndex, "" + pageSize });
//		return list;
//
//	}

}

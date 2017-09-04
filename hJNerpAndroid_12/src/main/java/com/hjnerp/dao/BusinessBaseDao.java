package com.hjnerp.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.hjnerp.db.SQLiteWorker.AbstractSQLable;
import com.hjnerp.db.Tables;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.BusinessTableCreateModel;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Ctlm1346;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.Ctlm1347JsonPrototype;
import com.hjnerp.model.Ctlm4203;
import com.hjnerp.model.MenuContent;
import com.hjnerp.model.NBusinessTableCreateModel;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.myscom.ObjectUtils;
import com.hjnerp.util.myscom.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BusinessBaseDao extends BaseDao {
	// TODO 业务菜单表

	/* 清空业务菜单表 */
	public static final void deleteBusinessMenus() {
		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL("delete from " + Tables.BusinessMenu.NAME);
		endDMLOffTransaction(database);

	}

	/** 插入或修改一批业务菜单 */
	public static final void replaceBusinessMenus(final List<MenuContent> menus) {
		worker.postDML(new AbstractSQLable() {
			public Object doAysncSQL() {
				SQLiteDatabase database = beginDMLOnTransaction();
				try {
					ContentValues values = new ContentValues();
					for (MenuContent menuContent : menus) {
						values.clear();
						values.put(Tables.BusinessMenu.IDMENU,
								menuContent.getIdMenu());
						values.put(Tables.BusinessMenu.MODELWINDOW,
								menuContent.getModelWindow());
						values.put(Tables.BusinessMenu.NAMEMENU,
								menuContent.getNameMenu());
						values.put(Tables.BusinessMenu.PICPATH,
								menuContent.getPicpath());
						values.put(Tables.BusinessMenu.VARPARM,
								menuContent.getVarParm());
						values.put(Tables.BusinessMenu.VARPARM1,
								menuContent.getVarParm1());

						database.replace(Tables.BusinessMenu.NAME, null, values);
					}
				} finally {
					endDMLOffTransaction(database);
				}
				return null;
			}

			public void onCompleted(Object event) {
				// if (!(event instanceof Throwable)) {
				//
				// }
			}
		});
	}

	/** 插入或修改某个业务菜单 */
	public static final void replaceBusinessMenu(MenuContent menuContent) {
		replaceBusinessMenus(Arrays.asList(menuContent));
	}

	public static final List<Ctlm1347> getCTLM1347ByParentnodes(
			List<String> list) {
		if (list.isEmpty())
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("select * from ").append(Tables.BusinessCtlm1347.NAME)
				.append(" where id_parentnode in (");
		for (String idnode : list) {
			buf.append("'").append(idnode).append("',");
		}
		buf.deleteCharAt(buf.length() - 1).append(")");
		return queryCTLM1347s(buf.toString());
	}

	public static final List<Ctlm1347> getCTLM1347Bybillno(List<String> list) {
		if (list.isEmpty())
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("select * from ").append(Tables.BusinessCtlm1347.NAME)
				.append(" where var_billno in (");
		for (String idnode : list) {
			buf.append("'").append(idnode).append("',");
		}
		buf.deleteCharAt(buf.length() - 1).append(")");
		return queryCTLM1347s(buf.toString());
	}

	public static final List<Ctlm1347> getCTLM1347ByIdnodes(List<String> list) {
		if (list.isEmpty())
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("select * from ").append(Tables.BusinessCtlm1347.NAME)
				.append(" where id_node in (");
		for (String idnode : list) {
			buf.append("'").append(idnode).append("',");
		}
		buf.deleteCharAt(buf.length() - 1).append(")");
		return queryCTLM1347s(buf.toString());
	}

	public static final void getCTLM1347ParentNode(String idnode,
			String billNo, List<String> list) {
		String result = "";
		StringBuffer sb = new StringBuffer();
		sb.append("select  id_parentnode from ").append(
				Tables.BusinessCtlm1347.NAME);
		sb.append(" where  1=1 and id_node='" + idnode + "'");
		if (billNo != null) {
			sb.append(" and var_billno='" + billNo + "'");
		}
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = getColumnString(cursor, Ctlm1347.IDPNODE);
				list.add(result);
			}
		} finally {
			endDQL(database, cursor);
		}

	}

	public static final void getCTLM1347IdNode(String parentIdNo,
			String billNo, List<String> list) {
		String result = "";
		StringBuffer sb = new StringBuffer();
		sb.append("select  id_node from ").append(Tables.BusinessCtlm1347.NAME);
		sb.append(" where  1=1 and id_parentnode='" + parentIdNo + "'");
		if (billNo != null) {
			sb.append(" and var_billno='" + billNo + "'");
		}
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = getColumnString(cursor, Ctlm1347.IDNODE);
				list.add(result);
			}
		} finally {
			endDQL(database, cursor);
		}
	}

	/** 查询业务菜单 */
	public static final ArrayList<MenuContent> queryBusinessMenus() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.BusinessMenu.NAME)
				.append(" order by ").append(Tables.BusinessMenu.IDMENU);
		return queryBusinessMenus(sql.toString());
	}

	/** 查询业务菜单 */
	private static final ArrayList<MenuContent> queryBusinessMenus(String sql) {
		ArrayList<MenuContent> result = new ArrayList<MenuContent>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				MenuContent mc = new MenuContent();
				mc.setIdMenu(getColumnString(cursor, Tables.BusinessMenu.IDMENU));
				mc.setModelWindow(getColumnString(cursor,
						Tables.BusinessMenu.MODELWINDOW));
				mc.setNameMenu(getColumnString(cursor,
						Tables.BusinessMenu.NAMEMENU));
				mc.setPicpath(getColumnString(cursor,
						Tables.BusinessMenu.PICPATH));
				mc.setVarParm(getColumnString(cursor,
						Tables.BusinessMenu.VARPARM));
				mc.setVarParm1(getColumnString(cursor,
						Tables.BusinessMenu.VARPARM1));

				result.add(mc);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final void replaceBusinessTableCreateModels(
			final List<BusinessTableCreateModel> list) {
		worker.postDML(new AbstractSQLable() {
			public Object doAysncSQL() {
				SQLiteDatabase database = beginDMLOnTransaction();
				try {
					ContentValues values = new ContentValues();
					for (BusinessTableCreateModel btcm : list) {
						values.clear();
						if (Tables.BusinessCtlm1345.NAME.equals(btcm.table)) {
							values.put(Ctlm1345.FLAG_DOWNLOAD,
									btcm.data.get(Ctlm1345.FLAG_DOWNLOAD));
							values.put(Ctlm1345.IDCOLUMN,
									btcm.data.get(Ctlm1345.IDCOLUMN));
							values.put(Ctlm1345.IDCOM,
									btcm.data.get(Ctlm1345.IDCOM));
							values.put(Ctlm1345.IDRECORDER,
									btcm.data.get(Ctlm1345.IDRECORDER));
							values.put(Ctlm1345.IDTABLE,
									btcm.data.get(Ctlm1345.IDTABLE));
							values.put(Ctlm1345.LINENO,
									btcm.data.get(Ctlm1345.LINENO));
							values.put(Ctlm1345.NAMECOLUMN,
									btcm.data.get(Ctlm1345.NAMECOLUMN));
							values.put(Ctlm1345.VARCONDITION,
									btcm.data.get(Ctlm1345.VARCONDITION));
							values.put(Ctlm1345.VARLATI,
									btcm.data.get(Ctlm1345.VARLATI));
							values.put(Ctlm1345.VARLONGI,
									btcm.data.get(Ctlm1345.VARLONGI));
							values.put(Ctlm1345.VARVALNAME,
									btcm.data.get(Ctlm1345.VARVALNAME));
							values.put(Ctlm1345.VARVALUE,
									btcm.data.get(Ctlm1345.VARVALUE));
						}
					}
				} finally {
					endDMLOffTransaction(database);
				}
				return null;
			}
		});
	}

	public static final void opBusinessTableCreateModels(
			final List<BusinessTableCreateModel> list) {
		worker.postDML(new AbstractSQLable() {
			public Object doAysncSQL() {
				SQLiteDatabase database = beginDMLOnTransaction();
				try {
					for (BusinessTableCreateModel btcm : list) {
						database.execSQL(btcm.deleteSql);
						if (StringUtils.isNotBlank(btcm.insertSqls)) {
							String[] strs = btcm.insertSqls
									.split(BusinessTableCreateModel.DIVIDER_STRING);
							for (String str : strs)
								database.execSQL(str);
						}
					}
				} finally {
					endDMLOffTransaction(database);
				}
				return null;
			}

			public void onCompleted(Object event) {
				// if (!(event instanceof Throwable)) {
				//
				// }
			}
		});
	}

	public static final void opNBusinessTableCreateModels2(
			final NBusinessTableCreateModel model) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			if (StringUtils.isNotBlank(model.deleteSql)) {
				database.execSQL(model.deleteSql);
			}
			if (StringUtils.isNotBlank(model.insertSqls)) {
				database.execSQL(model.insertSqls);
			}
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final void opNBusinessTableCreateModels(
			final NBusinessTableCreateModel model) {
		worker.postDML(new AbstractSQLable() {
			public Object doAysncSQL() {
				SQLiteDatabase database = beginDMLOnTransaction();
				try {
					if (StringUtils.isNotBlank(model.deleteSql)) {
						database.execSQL(model.deleteSql);
					}
					if (StringUtils.isNotBlank(model.insertSqls)) {
						database.execSQL(model.insertSqls);
					}
				} finally {
					endDMLOffTransaction(database);
				}
				return null;
			}

			public void onCompleted(Object event) {
				// if (!(event instanceof Throwable)) {
				//
				// }
			}
		});
	}

	public static final List<Ctlm4203> queryCtlm4203sByFlag(String flag) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm4203.NAME)
				.append(" where ")
				.append(Tables.BusinessCtlm4203.COL_FLAG_DOWNLOAD_TYPE)
				.append(" = '").append(flag).append("' ");
		return queryCtlm4203s(sb.toString());
	}

	private static final List<Ctlm4203> queryCtlm4203s(String sql) {
		ArrayList<Ctlm4203> result = new ArrayList<Ctlm4203>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm4203 ctlm = new Ctlm4203();
				ctlm.flag_downtype = getColumnString(cursor,
						Tables.BusinessCtlm4203.COL_FLAG_DOWNLOAD_TYPE);
				ctlm.id_table = getColumnString(cursor,
						Tables.BusinessCtlm4203.COL_ID_TABLE);
				ctlm.var_where = getColumnString(cursor,
						Tables.BusinessCtlm4203.COL_VAR_WHERE);
				result.add(ctlm);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final void updateCtlm1345DownloadFlag(String[] where) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("update ").append(Tables.BusinessCtlm1345.NAME)
					.append(" set ").append(Ctlm1345.FLAG_DOWNLOAD)
					.append(" = 'Y' ").append(" where ")
					.append(Ctlm1345.IDTABLE).append(" in (")
					.append(StringUtils.join(where, ",")).append(")");
			database.execSQL(sb.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final void insertIQTABLE(String[] where) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("insert into ").append(Tables.IQTable.NAME).append(" ( ")
					.append(Tables.IQTable.ID_IQ).append(" , ")
					.append(Tables.IQTable.VAR_VALUE).append(" , ")
					.append(Tables.IQTable.VAR_FLAG).append(") values ('")
					.append(where[0]).append("','").append(where[1])
					.append("','").append(where[2]).append("')");
			database.execSQL(sb.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final void updateIQTABLE(String where) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("update ").append(Tables.IQTable.NAME).append(" set ")
					.append(Tables.IQTable.VAR_FLAG).append(" = 'Y' ")
					.append(" where ").append(Tables.IQTable.ID_IQ)
					.append(" = ").append("'").append(where).append("')");
			database.execSQL(sb.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final List<Ctlm1345> getCTLM1345ByIdTable(String idtable) {

		StringBuffer buf = new StringBuffer();
		buf.append("select * from ").append(Tables.BusinessCtlm1345.NAME)
				.append(" where ").append("id_table").append(" = ")
				.append(" '").append(idtable).append("' ");

		return queryCtlm1345s(buf.toString());
	}

	/**
	 *
	 * @param idtable
	 * @param id_column
	 * @param content
	 * @param id_recorder
     * @return
	 *    		查询工作日志的信息
     */
	public static final List<Ctlm1345> getCTLM1345NameColumn(String idtable,String id_column,String content,String id_recorder) {
		StringBuffer buf = new StringBuffer();
		buf.append("select * from ").append(Tables.BusinessCtlm1345.NAME)
				.append(" where ").append(" id_recorder ").append(" = ")
				.append(" '").append(id_recorder).append("' ")
				.append(" and ").append("id_table").append(" = ")
				.append(" '").append(idtable).append("' ")
				.append(" and ").append(" id_column ").append(" = ")
				.append(" '").append(id_column).append("' ")
				.append(" and ").append(" name_column ")
				.append(" like ").append(" '%").append(content).append("%' ");
		Log.v("show", buf.toString());
		return queryCtlm1345s(buf.toString());
	}

	public static final List<Ctlm1345> queryAllCtlm1345s(String where) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm1345.NAME)
				.append(where);
		return queryCtlm1345s(sb.toString());
	}

	private static final List<Ctlm1345> queryCtlm1345s(String sql) {
		ArrayList<Ctlm1345> result = new ArrayList<Ctlm1345>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1345 ctlm = new Ctlm1345();
				ctlm.setFlag_download(getColumnString(cursor,
						Ctlm1345.FLAG_DOWNLOAD));
				ctlm.setId_column(getColumnString(cursor, Ctlm1345.IDCOLUMN));
				ctlm.setId_com(getColumnString(cursor, Ctlm1345.IDCOM));
				ctlm.setId_recorder(getColumnString(cursor, Ctlm1345.IDRECORDER));
				ctlm.setId_table(getColumnString(cursor, Ctlm1345.IDTABLE));
				ctlm.setLine_no(getColumnString(cursor, Ctlm1345.LINENO));
				ctlm.setVar_condition(getColumnString(cursor,
						Ctlm1345.VARCONDITION));
				ctlm.setVar_image(getColumnString(cursor, Ctlm1345.VAR_IMAGE));
				// ctlm.setVar_location(getColumnString(cursor,
				// Ctlm1345.VAR_LOCATION));
				ctlm.setVar_valname(getColumnString(cursor, Ctlm1345.VARVALNAME));
				ctlm.setVar_value(getColumnString(cursor, Ctlm1345.VARVALUE));
				result.add(ctlm);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final void replaceCTLM1345s(final List<Ctlm1345> ctlm1345) {
		worker.postDML(new AbstractSQLable() {
			public Object doAysncSQL() {
				SQLiteDatabase database = beginDMLOnTransaction();
				try {
					ContentValues values = new ContentValues();
					for (Ctlm1345 ctlm1345Content : ctlm1345) {
						values.clear();
						values.put(Ctlm1345.IDTABLE,
								ctlm1345Content.getId_table());
						values.put(Ctlm1345.LINENO,
								ctlm1345Content.getLine_no());
						values.put(Ctlm1345.VARVALNAME,
								ctlm1345Content.getVar_valname());
						values.put(Ctlm1345.VARVALUE,
								ctlm1345Content.getVar_value());
						values.put(Ctlm1345.IDRECORDER,
								ctlm1345Content.getId_recorder());
						values.put(Ctlm1345.IDCOM, ctlm1345Content.getId_com());
						values.put(Ctlm1345.IDCOLUMN,
								ctlm1345Content.getId_column());
						values.put(Ctlm1345.VARCONDITION,
								ctlm1345Content.getVar_condition());
						values.put(Ctlm1345.NAMECOLUMN,
								ctlm1345Content.getName_column());
						values.put(Ctlm1345.VARLONGI,
								ctlm1345Content.getVar_longi());
						values.put(Ctlm1345.VARLATI,
								ctlm1345Content.getVar_lati());
						values.put(Ctlm1345.FLAG_DOWNLOAD,
								ctlm1345Content.getFlag_download());
						// values.put(Ctlm1345.VAR_LOCATION,
						// ctlm1345Content.getVar_location());
						values.put(Ctlm1345.VAR_IMAGE,
								ctlm1345Content.getVar_image());
						database.replace(Tables.BusinessCtlm1345.NAME, null,
								values);
					}
				} finally {
					endDMLOffTransaction(database);
				}
				return null;
			}

			public void onCompleted(Object event) {
				// if (!(event instanceof Throwable)) {
				//
				// }
			}
		});
	}

	public static void updateCtlm1347s(List<Ctlm1347> ctlm1347, String flag) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			for (Ctlm1347 ctlm1347Content : ctlm1347) {
				values.clear();
				values.put(Ctlm1347.IDRECORDER,
						ctlm1347Content.getId_recorder());
				values.put(Ctlm1347.IDCOM, ctlm1347Content.getId_com());
				values.put(Ctlm1347.IDNODE, ctlm1347Content.getId_node());
				values.put(Ctlm1347.NAMENODE, ctlm1347Content.getName_node());
				values.put(Ctlm1347.VARBILLNO, ctlm1347Content.getVar_billno());
				values.put(Ctlm1347.IDMODEL, ctlm1347Content.getId_model());
				values.put(Ctlm1347.IDSRCNODE, ctlm1347Content.getId_srcnode());
				values.put(Ctlm1347.FLAGUPLOAD, flag);
				values.put(Ctlm1347.IDPNODE, ctlm1347Content.getId_parentnode());
				values.put(Ctlm1347.IDNODETYPE,
						ctlm1347Content.getId_nodetype());
				values.put(Ctlm1347.VARDATA1, ctlm1347Content.getVar_data1());
				values.put(Ctlm1347.VARDATA2, ctlm1347Content.getVar_data2());
				values.put(Ctlm1347.VARDATA3, ctlm1347Content.getVar_data3());
				values.put(Ctlm1347.VARDATA4, ctlm1347Content.getVar_data4());
				values.put(Ctlm1347.VARDATA5, ctlm1347Content.getVar_data5());
				values.put(Ctlm1347.VARDATA6, ctlm1347Content.getVar_data6());
				values.put(Ctlm1347.VARDATA7, ctlm1347Content.getVar_data7());
				values.put(Ctlm1347.VARDATA8, ctlm1347Content.getVar_data8());
				values.put(Ctlm1347.VARDATA9, ctlm1347Content.getVar_data9());
				values.put(Ctlm1347.VARDATA10, ctlm1347Content.getVar_data10());
				values.put(Ctlm1347.VARDATA11, ctlm1347Content.getVar_data11());
				values.put(Ctlm1347.VARDATA12, ctlm1347Content.getVar_data12());
				values.put(Ctlm1347.VARDATA13, ctlm1347Content.getVar_data13());
				values.put(Ctlm1347.VARDATA14, ctlm1347Content.getVar_data14());
				values.put(Ctlm1347.VARDATA15, ctlm1347Content.getVar_data15());
				values.put(Ctlm1347.VARDATA16, ctlm1347Content.getVar_data16());
				values.put(Ctlm1347.VARDATA17, ctlm1347Content.getVar_data17());
				values.put(Ctlm1347.VARDATA18, ctlm1347Content.getVar_data18());
				values.put(Ctlm1347.VARDATA19, ctlm1347Content.getVar_data19());
				values.put(Ctlm1347.VARDATA20, ctlm1347Content.getVar_data20());
				values.put(Ctlm1347.DATEOPR, ctlm1347Content.getDate_opr());
				values.put(Ctlm1347.IDVIEW, ctlm1347Content.getId_view());
				values.put(Ctlm1347.VARJSON, ctlm1347Content.getvar_Json());
				values.put(Ctlm1347.VARVERSION,
						ctlm1347Content.getVar_version());
				values.put(Ctlm1347.IDTABLE, ctlm1347Content.getId_table());
				values.put(Ctlm1347.INTLINE, ctlm1347Content.getInt_line());
				database.replace(Tables.BusinessCtlm1347.NAME, null, values);
			}
		} finally {
			endDMLOffTransaction(database);
		}
	}

	// TODO Ctlm1347
	/** 插入或修改一批 */
	public static final void replaceCTLM1347s(final List<Ctlm1347> ctlm1347) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			for (Ctlm1347 ctlm1347Content : ctlm1347) {
				values.clear();
				values.put(Ctlm1347.IDRECORDER,
						ctlm1347Content.getId_recorder());
				values.put(Ctlm1347.IDCOM, ctlm1347Content.getId_com());
				values.put(Ctlm1347.IDNODE, ctlm1347Content.getId_node());
				values.put(Ctlm1347.NAMENODE, ctlm1347Content.getName_node());
				values.put(Ctlm1347.VARBILLNO, ctlm1347Content.getVar_billno());
				values.put(Ctlm1347.IDMODEL, ctlm1347Content.getId_model());
				values.put(Ctlm1347.IDSRCNODE, ctlm1347Content.getId_srcnode());
				values.put(Ctlm1347.FLAGUPLOAD,
						ctlm1347Content.getFlag_upload());
				values.put(Ctlm1347.IDPNODE, ctlm1347Content.getId_parentnode());
				values.put(Ctlm1347.IDNODETYPE,
						ctlm1347Content.getId_nodetype());
				values.put(Ctlm1347.VARDATA1, ctlm1347Content.getVar_data1());
				values.put(Ctlm1347.VARDATA2, ctlm1347Content.getVar_data2());
				values.put(Ctlm1347.VARDATA3, ctlm1347Content.getVar_data3());
				values.put(Ctlm1347.VARDATA4, ctlm1347Content.getVar_data4());
				values.put(Ctlm1347.VARDATA5, ctlm1347Content.getVar_data5());
				values.put(Ctlm1347.VARDATA6, ctlm1347Content.getVar_data6());
				values.put(Ctlm1347.VARDATA7, ctlm1347Content.getVar_data7());
				values.put(Ctlm1347.VARDATA8, ctlm1347Content.getVar_data8());
				values.put(Ctlm1347.VARDATA9, ctlm1347Content.getVar_data9());
				values.put(Ctlm1347.VARDATA10, ctlm1347Content.getVar_data10());
				values.put(Ctlm1347.VARDATA11, ctlm1347Content.getVar_data11());
				values.put(Ctlm1347.VARDATA12, ctlm1347Content.getVar_data12());
				values.put(Ctlm1347.VARDATA13, ctlm1347Content.getVar_data13());
				values.put(Ctlm1347.VARDATA14, ctlm1347Content.getVar_data14());
				values.put(Ctlm1347.VARDATA15, ctlm1347Content.getVar_data15());
				values.put(Ctlm1347.VARDATA16, ctlm1347Content.getVar_data16());
				values.put(Ctlm1347.VARDATA17, ctlm1347Content.getVar_data17());
				values.put(Ctlm1347.VARDATA18, ctlm1347Content.getVar_data18());
				values.put(Ctlm1347.VARDATA19, ctlm1347Content.getVar_data19());
				values.put(Ctlm1347.VARDATA20, ctlm1347Content.getVar_data20());
				values.put(Ctlm1347.DATEOPR, ctlm1347Content.getDate_opr());
				values.put(Ctlm1347.IDVIEW, ctlm1347Content.getId_view());
				values.put(Ctlm1347.VARJSON, ctlm1347Content.getvar_Json());
				values.put(Ctlm1347.VARVERSION,
						ctlm1347Content.getVar_version());
				values.put(Ctlm1347.IDTABLE, ctlm1347Content.getId_table());
				values.put(Ctlm1347.INTLINE, ctlm1347Content.getInt_line());
				database.replace(Tables.BusinessCtlm1347.NAME, null, values);
			}	
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 插入或修改某个 */
	public static final void replaceCTLM1347(Ctlm1347 ctlm1347) {
		replaceCTLM1347s(Arrays.asList(ctlm1347));
	}

	/** 查询 */
	public static final ArrayList<Ctlm1347> queryCTLM1347s() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.BusinessCtlm1347.NAME);
		return queryCTLM1347s(sql.toString());
	}

	/** 查询 */
	private static final ArrayList<Ctlm1347> queryCTLM1347s(String sql) {
		ArrayList<Ctlm1347> result = new ArrayList<Ctlm1347>();
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sql, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1347 ctlm1347Content = new Ctlm1347();

				fillCtlm1347(ctlm1347Content, cursor);

				result.add(ctlm1347Content);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	/* 根据DATEOPR查询最近的一条billno */
	public static final Ctlm1347 queryLastCtlm1347() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(Tables.BusinessCtlm1347.NAME)
				.append(" order by ").append(Ctlm1347.DATEOPR)
				.append(" desc limit 0,1");
		ArrayList<Ctlm1347> list = queryCTLM1347s(sql.toString());
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	/** 通过modelId删除 */
	public static final void deleteContactById(final String model_id) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(Tables.BusinessCtlm1347.NAME)
					.append(" where ").append(Ctlm1347.IDMODEL).append(" = '")
					.append(model_id).append("' ");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 通过modelId删除 */
	public static final void deleteContactById_node(final String Id_node) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(Tables.BusinessCtlm1347.NAME)
					.append(" where ").append(Ctlm1347.IDNODE).append(" = '")
					.append(Id_node).append("' ");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 删除BillNo为空的单据 */
	public static final void deleteContactByBill(final String model_id) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append(Tables.BusinessCtlm1347.NAME)
					.append(" where ").append(Ctlm1347.IDMODEL).append(" = '")
					.append(model_id).append("'  and ")
					.append(Ctlm1347.VARBILLNO).append(" = '' ");
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	/** 通过modelId删除 */
	// TODO
	public static final void updateCtlm1347BySonNo(String parent_id,
			String billno) {

		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(Tables.BusinessCtlm1347.NAME)
				.append(" set ");
		sql.append(Ctlm1347.VARBILLNO).append(" = '").append(billno)
				.append("' ");
		sql.append("where ").append(Ctlm1347.IDNODE).append(" = '")
				.append(parent_id).append("' ");
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final BusinessData getCtlm1345ListFromCtlm1347(String viewid,
			String parent, String billno, String idmodel, String id_table) {

		ArrayList<Ctlm1345> ctlm1345 = new ArrayList<Ctlm1345>();
		String varValues = "";
		BusinessData busdata = new BusinessData();
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct var_json ,int_line ,id_table  from ")
				.append(Tables.BusinessCtlm1347.NAME + " where 1=1 ");
		sb.append(" and " + Ctlm1347.IDPNODE).append(" = '").append(parent)
				.append("' ");
		sb.append(" and " + Ctlm1347.IDMODEL).append(" = '").append(idmodel)
				.append("' ");
		sb.append(" and " + Ctlm1347.VARBILLNO).append(" = '").append(billno)
				.append("' ");
		sb.append(" and " + Ctlm1347.IDTABLE).append(" = '").append(id_table)
				.append("' ");

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1345 ctlm1345Content = new Ctlm1345();
				ctlm1345Content.setVar_value(getColumnString(cursor,
						Ctlm1347.VARJSON));
				ctlm1345Content.setId_table(getColumnString(cursor,
						Ctlm1347.IDTABLE));
				ctlm1345.add(ctlm1345Content);
				varValues = varValues
						+ getColumnString(cursor, Ctlm1347.VARJSON) + ",";
			}
		} finally {
			endDQL(database, cursor);
		}
		varValues = "[" + varValues.substring(0, varValues.length() - 1) + "]";
		busdata.setId_table(id_table);
		busdata.setId_view(viewid);
		busdata.setCtlm1345(ctlm1345);
		busdata.setVar_values(varValues);

		return busdata;
	}

	public static final ArrayList<BusinessData> getCtlm1345ListFromCtlm1347(
			String viewid, String parent, String billno, String idmodel) {
		ArrayList<BusinessData> result = new ArrayList<BusinessData>();

		StringBuffer sb = new StringBuffer();
		String id_table;
		sb.append("select distinct  id_table  from ")
				.append(Tables.BusinessCtlm1347.NAME
						+ " where 1=1  and var_json is not null and var_json <>'' ");

		sb.append(" and " + Ctlm1347.IDPNODE).append(" = '").append(parent)
				.append("' ");

		if (StringUtils.isNotBlank(idmodel)) {
			sb.append(" and " + Ctlm1347.IDMODEL).append(" = '")
					.append(idmodel).append("' ");
		}

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and " + Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");
		}
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {

				id_table = getColumnString(cursor, Ctlm1347.IDTABLE);
				BusinessData bdata;
				bdata = getCtlm1345ListFromCtlm1347(viewid, parent, billno,
						idmodel, id_table);
				if (bdata != null) {
					result.add(bdata);
				}
			}
		} finally {
			endDQL(database, cursor);
		}

		return result;
	}

	public static final ArrayList<Ctlm1347> getCtlm1347List(String idmodel,
			String viewid, String parent, String billno, String id_srcnode) {
		ArrayList<Ctlm1347> result = new ArrayList<Ctlm1347>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm1347.NAME);

		sb.append(" where 1=1");
		if (StringUtils.isNotBlank(idmodel)) {
			sb.append(" and " + Ctlm1347.IDMODEL).append(" = '")
					.append(idmodel).append("' ");

		}
		if (StringUtils.isNotBlank(viewid)) {
			sb.append(" and " + Ctlm1347.IDVIEW).append(" = '").append(viewid)
					.append("' ");

		}

		if (StringUtils.isNotBlank(parent)) {
			sb.append(" and " + Ctlm1347.IDPNODE).append(" = '").append(parent)
					.append("' ");

		}

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and " + Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");
		}

		if (StringUtils.isNotBlank(id_srcnode)) {
			sb.append(" and " + Ctlm1347.IDSRCNODE).append(" = '")
					.append(id_srcnode).append("' ");
		}
		sb.append(" order by int_line ");
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1347 ctlm1347Content = new Ctlm1347();

				fillCtlm1347(ctlm1347Content, cursor);
				result.add(ctlm1347Content);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final ArrayList<Ctlm1347> getCtlm1347ListUpload(
			String parent, String billno, String idmodel, String id_srcnode) {
		ArrayList<Ctlm1347> result = new ArrayList<Ctlm1347>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm1347.NAME);

		sb.append(" where 1=1");

		if (StringUtils.isNotBlank(parent)) {
			sb.append(" and " + Ctlm1347.IDPNODE).append(" = '").append(parent)
					.append("' ");
		}

		if (StringUtils.isNotBlank(idmodel)) {
			sb.append(" and " + Ctlm1347.IDMODEL).append(" = '")
					.append(idmodel).append("' ");
		}

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and " + Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");

		}
		sb.append(" and " + Ctlm1347.FLAGUPLOAD).append(" <> '").append("Y")
				.append("' ");

		if (StringUtils.isNoneBlank(id_srcnode)) {
			sb.append(" and " + Ctlm1347.IDSRCNODE).append(" = '")
					.append(id_srcnode).append("' ");
		}
		sb.append(" and " + Ctlm1347.IDNODE).append(
				" in (select id_parentnode from ctlm1347 where  id_model = '"
						+ idmodel + "' ) ");

		sb.append(" group  by int_line");

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1347 ctlm1347Content = new Ctlm1347();

				fillCtlm1347(ctlm1347Content, cursor);
				result.add(ctlm1347Content);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final ArrayList<Ctlm1347> getCtlm1347List(String idnode,
			String billno) {
		ArrayList<Ctlm1347> result = new ArrayList<Ctlm1347>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm1347.NAME)
				.append(" where 1=1");

		if (StringUtils.isNotBlank(idnode)) {
			sb.append(" and " + Ctlm1347.IDNODE).append(" = '").append(idnode)
					.append("' ");

		}

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and " + Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");

		}

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1347 ctlm1347Content = new Ctlm1347();

				fillCtlm1347(ctlm1347Content, cursor);
				result.add(ctlm1347Content);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final ArrayList<Ctlm1347> getCtlm1347ListByBill(
			String parent, String billno, String idmodel, String viewid) {
		ArrayList<Ctlm1347> result = new ArrayList<Ctlm1347>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm1347.NAME)
				.append(" where 1=1");

		if (StringUtils.isNotBlank(parent))

		{
			sb.append(" and  id_parentnode = '").append(parent).append("' ");
		}

		if (StringUtils.isNotBlank(idmodel)) {
			sb.append(" and " + Ctlm1347.IDMODEL).append(" = '")
					.append(idmodel).append("' ");

		}

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and " + Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");

		}

		if (StringUtils.isNotBlank(viewid)) {
			sb.append(" and " + Ctlm1347.IDVIEW).append(" = '").append(viewid)
					.append("' ");

		}

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1347 ctlm1347Content = new Ctlm1347();
				fillCtlm1347(ctlm1347Content, cursor);
				result.add(ctlm1347Content);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final int getCtlm1347ListByBillCount(String parent,
			String billno, String idmodel, String viewid) {
		int result = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) rowcount from ")
				.append(Tables.BusinessCtlm1347.NAME).append(" where 1=1");

		if (StringUtils.isNotBlank(parent))

		{
			sb.append(" and  id_parentnode = '").append(parent).append("' ");
		}

		if (StringUtils.isNotBlank(idmodel)) {
			sb.append(" and " + Ctlm1347.IDMODEL).append(" = '")
					.append(idmodel).append("' ");

		}

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and " + Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");

		}

		if (StringUtils.isNotBlank(viewid)) {
			sb.append(" and " + Ctlm1347.IDVIEW).append(" = '").append(viewid)
					.append("' ");

		}

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = getColumnInt(cursor, "rowcount");
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final String getTemlateVersion(String id_node) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ").append(Ctlm1346.COL_INT_VERSION).append(" from ")
				.append(Tables.BusinessCtlm1346.NAME).append(" where ")
				.append(Ctlm1346.COL_ID_NODE).append(" = ? ");
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), new String[] { id_node });
			cursor.moveToFirst();
			if (cursor.isAfterLast())
				return "0";
			return cursor.getString(0);
		} catch (IllegalArgumentException ill){
			Log.v("show", ill.toString());
			return "0";
		}catch (Exception e) {
			Log.e(null, "", e);
			return "0";
		}finally {
			endDQL(database, cursor);
		}
	}

	public static final void updateCtlm1346TemplateVersion(String templateName,
			String version) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			ContentValues values = new ContentValues();
			values.put(Ctlm1346.COL_ID_NODE, templateName);
			values.put(Ctlm1346.COL_INT_VERSION, version);
			database.replace(Tables.BusinessCtlm1346.NAME, null, values);
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final void deleteBusinessData(String idTable, String condition) {

		condition = condition.trim();
		StringBuffer sb = new StringBuffer();
		sb.append("delete   FROM  ctlm1345 where id_table ='" + idTable + "'");
		if (condition != null && !condition.equals("")) {
			sb.append(" and " + condition);
		}
		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL(sb.toString());
		endDMLOffTransaction(database);
	}

	public static void deleteViewData(BusinessParam bus) {

		StringBuffer sb = new StringBuffer();
		sb.append("delete   FROM  ctlm1347 where id_model ='"
				+ bus.getModelId() + "' and id_parentnode = '"
				+ bus.getIdParentNode() + "'");

		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL(sb.toString());
		endDMLOffTransaction(database);
	}

	public static void deleteViewDataByPNode(String nodeid) {

		StringBuffer sb = new StringBuffer();
		sb.append("delete   FROM  ctlm1347 where id_parentnode = '" + nodeid
				+ "'");

		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL(sb.toString());
		endDMLOffTransaction(database);
	}

	public static void deleteViewDataByViewId(String viewid) {

		StringBuffer sb = new StringBuffer();
		sb.append("delete   FROM  ctlm1347 where id_view = '" + viewid
				+ "'");

		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL(sb.toString());
		endDMLOffTransaction(database);
	}

	public static final BusinessData getBusinessData(String idTable,
			String condition) {
		ArrayList<Ctlm1345> result = new ArrayList<Ctlm1345>();
		String varValues = "";
		BusinessData busdata;
		condition = condition.trim();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT *  FROM  ctlm1345 where id_table ='" + idTable + "'");
		// sb.append("SELECT id_table  ,id_recorder  ,id_com  ,line_no  ,id_column   ,name_column  ,var_value ,var_image  ,var_condition  ,flag_download ,var_rlati  ,var_rlongi  ,var_valname  FROM  ctlm1345 where id_table ='"
		// + idTable + "'");
		if (condition != null && !condition.equals("")) {
			sb.append(" and " + condition);
		}
		/**
		 * @author haijian 加上按行号排序
		 */
		sb.append(" order by line_no+0 ASC");
		Log.i("info", "查询语句==" + sb.toString());
		SQLiteDatabase database = beginDQL();
		Cursor cursors = null;
		try {
			cursors = database.rawQuery(sb.toString(), null);
			for (cursors.moveToFirst(); !cursors.isAfterLast(); cursors
					.moveToNext()) {
				Ctlm1345 ctlm1345content = new Ctlm1345();

				ctlm1345content.setVar_value(getColumnString(cursors,
						Ctlm1345.VARVALUE));
				ctlm1345content.setLine_no(getColumnString(cursors,
						Ctlm1345.LINENO));
				ctlm1345content.setId_column(getColumnString(cursors,
						Ctlm1345.IDCOLUMN));
				ctlm1345content.setId_table(getColumnString(cursors,
						Ctlm1345.IDTABLE));
				ctlm1345content.setVar_image(getColumnString(cursors,
						Ctlm1345.VAR_IMAGE));
				ctlm1345content.setVar_lati(getColumnString(cursors,
						Ctlm1345.VARLATI));
				ctlm1345content.setVar_longi(getColumnString(cursors,
						Ctlm1345.VARLONGI));
				ctlm1345content.setVar_valname(getColumnString(cursors,
						Ctlm1345.VARVALNAME));
				ctlm1345content.setName_column(getColumnString(cursors,
						Ctlm1345.NAMECOLUMN));
				varValues = varValues
						+ getColumnString(cursors, Ctlm1345.VARVALUE) + ",";
				result.add(ctlm1345content);
			}
		} catch (Exception e) {
			Log.i("info", e.toString());
		} finally {
			endDQL(database, cursors);
		}
		for (int i = 0; i < result.size(); i++) {
			com.hjnerp.util.Log.i("info", "第" + i + "个line_no=="
					+ result.get(i).getLine_no());
		}
		if (varValues == "") {
			return null;
		}
		busdata = new BusinessData();

		varValues = "[" + varValues.substring(0, varValues.length() - 1) + "]";
		busdata.setId_table(idTable);
		busdata.setVar_values(varValues);
		busdata.setCtlm1345(result);
		return busdata;
	}

	public static void opCtlm1347Prototype(Ctlm1347JsonPrototype proto) {
		SQLiteDatabase database = beginDMLOnTransaction();
		try {
			if (proto.idTableWithDFlag != null
					&& !proto.idTableWithDFlag.isEmpty()
					&& proto.idTableData != null
					&& proto.idTableData.size() > 0) {
				StringBuffer sql = new StringBuffer();
				sql.append("delete from ").append(Tables.BusinessCtlm1347.NAME)
						.append(" where ").append(Ctlm1347.IDMODEL)
						.append(" in (");
				for (String idTable : proto.idTableWithDFlag) {
					sql.append("'").append(idTable).append("'").append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(") ");
				database.execSQL(sql.toString());
			}
			ContentValues values = new ContentValues();
			for (Map<String, Object> map : proto.idTableData) {
				values.clear();
				values.put(Ctlm1347.DATEOPR,
						ObjectUtils.toString(map.get(Ctlm1347.DATEOPR)));
				values.put(Ctlm1347.FLAGUPLOAD, "N");
				values.put(Ctlm1347.IDCOM,
						ObjectUtils.toString(map.get(Ctlm1347.IDCOM)));
				values.put(Ctlm1347.IDNODE,
						ObjectUtils.toString(map.get(Ctlm1347.IDNODE)));
				values.put(Ctlm1347.IDPNODE,
						ObjectUtils.toString(map.get(Ctlm1347.IDPNODE)));
				values.put(Ctlm1347.IDRECORDER,
						ObjectUtils.toString(map.get(Ctlm1347.IDRECORDER)));
				values.put(Ctlm1347.IDSRCNODE,
						ObjectUtils.toString(map.get(Ctlm1347.IDSRCNODE)));
				values.put(Ctlm1347.IDTABLE,
						ObjectUtils.toString(map.get(Ctlm1347.IDTABLE)));
				values.put(Ctlm1347.NAMENODE,
						ObjectUtils.toString(map.get(Ctlm1347.NAMENODE)));
				values.put(Ctlm1347.VARBILLNO,
						ObjectUtils.toString(map.get(Ctlm1347.VARBILLNO)));
				values.put(Ctlm1347.VARDATA1,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA1)));
				values.put(Ctlm1347.VARDATA10,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA10)));
				values.put(Ctlm1347.VARDATA2,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA2)));
				values.put(Ctlm1347.VARDATA3,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA3)));
				values.put(Ctlm1347.VARDATA4,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA4)));
				values.put(Ctlm1347.VARDATA5,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA5)));
				values.put(Ctlm1347.VARDATA6,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA6)));
				values.put(Ctlm1347.VARDATA7,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA7)));
				values.put(Ctlm1347.VARDATA8,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA8)));
				values.put(Ctlm1347.VARDATA9,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA9)));
				values.put(Ctlm1347.VARDATA11,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA11)));
				values.put(Ctlm1347.VARDATA20,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA20)));
				values.put(Ctlm1347.VARDATA12,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA12)));
				values.put(Ctlm1347.VARDATA13,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA13)));
				values.put(Ctlm1347.VARDATA14,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA14)));
				values.put(Ctlm1347.VARDATA15,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA15)));
				values.put(Ctlm1347.VARDATA16,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA16)));
				values.put(Ctlm1347.VARDATA17,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA17)));
				values.put(Ctlm1347.VARDATA18,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA18)));
				values.put(Ctlm1347.VARDATA19,
						ObjectUtils.toString(map.get(Ctlm1347.VARDATA19)));
				values.put(Ctlm1347.IDVIEW,
						ObjectUtils.toString(map.get(Ctlm1347.IDVIEW)));
				values.put(Ctlm1347.VARJSON,
						ObjectUtils.toString(map.get(Ctlm1347.VARJSON)));
				values.put(Ctlm1347.VARVERSION,
						ObjectUtils.toString(map.get(Ctlm1347.VARVERSION)));
				values.put(Ctlm1347.IDMODEL,
						ObjectUtils.toString(map.get(Ctlm1347.IDMODEL)));
				values.put(Ctlm1347.IDNODETYPE,
						ObjectUtils.toString(map.get(Ctlm1347.IDNODETYPE)));
				values.put(Ctlm1347.IDNODETYPE,
						ObjectUtils.toString(map.get(Ctlm1347.IDNODETYPE)));
				values.put(Ctlm1347.INTLINE, Double.parseDouble(ObjectUtils
						.toString(map.get(Ctlm1347.INTLINE))));
				database.replace(Tables.BusinessCtlm1347.NAME, null, values);
			}
		} finally {
			endDMLOffTransaction(database);
		}
	}

	// //根据原结点返回当前结点的信息
	public static final Ctlm1347 getSrcCtlm1347(String parent, String billno,
			String idmodel, String idsrcnode) {
		Ctlm1347 result = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm1347.NAME);
		sb.append(" where  1=1");

		if (StringUtils.isNotBlank(parent)) {
			sb.append(" and ").append(Ctlm1347.IDPNODE).append(" = '")
					.append(parent).append("' ");
		}

		if (StringUtils.isNotBlank(idmodel)) {
			sb.append(" and ").append(Ctlm1347.IDMODEL).append(" = '")
					.append(idmodel).append("' ");
		}

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and ").append(Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");
		}

		if (StringUtils.isNotBlank(idsrcnode)) {
			sb.append(" and ").append(Ctlm1347.IDSRCNODE).append(" = '")
					.append(idsrcnode).append("' ");
		}

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = new Ctlm1347();

				fillCtlm1347(result, cursor);

			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	// 根据结点返回当前结点的信息
	public static final Ctlm1347 getNodeCtlm1347(String billno, String idnode) {

		Ctlm1347 result = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ").append(Tables.BusinessCtlm1347.NAME);
		sb.append(" where  1=1");

		if (StringUtils.isNotBlank(billno)) {
			sb.append(" and ").append(Ctlm1347.VARBILLNO).append(" = '")
					.append(billno).append("' ");
		}

		if (StringUtils.isNotBlank(idnode)) {
			sb.append(" and ").append(Ctlm1347.IDNODE).append(" = '")
					.append(idnode).append("' ");
		}

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = new Ctlm1347();

				fillCtlm1347(result, cursor);

			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	private static void fillCtlm1347(Ctlm1347 result, Cursor cursor) {
		result.setId_recorder(getColumnString(cursor, Ctlm1347.IDRECORDER));
		result.setId_com(getColumnString(cursor, Ctlm1347.IDCOM));
		result.setId_node(getColumnString(cursor, Ctlm1347.IDNODE));
		result.setName_node(getColumnString(cursor, Ctlm1347.NAMENODE));
		result.setVar_billno(getColumnString(cursor, Ctlm1347.VARBILLNO));
		result.setId_model(getColumnString(cursor, Ctlm1347.IDMODEL));
		result.setId_srcnode(getColumnString(cursor, Ctlm1347.IDSRCNODE));
		result.setFlag_upload(getColumnString(cursor, Ctlm1347.FLAGUPLOAD));
		result.setId_parentnode(getColumnString(cursor, Ctlm1347.IDPNODE));
		result.setId_nodetype(getColumnString(cursor, Ctlm1347.IDNODETYPE));
		result.setVar_data1(getColumnString(cursor, Ctlm1347.VARDATA1));
		result.setVar_data2(getColumnString(cursor, Ctlm1347.VARDATA2));
		result.setVar_data3(getColumnString(cursor, Ctlm1347.VARDATA3));
		result.setVar_data4(getColumnString(cursor, Ctlm1347.VARDATA4));
		result.setVar_data5(getColumnString(cursor, Ctlm1347.VARDATA5));
		result.setVar_data6(getColumnString(cursor, Ctlm1347.VARDATA6));
		result.setVar_data7(getColumnString(cursor, Ctlm1347.VARDATA7));
		result.setVar_data8(getColumnString(cursor, Ctlm1347.VARDATA8));
		result.setVar_data9(getColumnString(cursor, Ctlm1347.VARDATA9));
		result.setVar_data10(getColumnString(cursor, Ctlm1347.VARDATA10));
		result.setVar_data11(getColumnString(cursor, Ctlm1347.VARDATA11));
		result.setVar_data12(getColumnString(cursor, Ctlm1347.VARDATA12));
		result.setVar_data13(getColumnString(cursor, Ctlm1347.VARDATA13));
		result.setVar_data14(getColumnString(cursor, Ctlm1347.VARDATA14));
		result.setVar_data15(getColumnString(cursor, Ctlm1347.VARDATA15));
		result.setVar_data16(getColumnString(cursor, Ctlm1347.VARDATA16));
		result.setVar_data17(getColumnString(cursor, Ctlm1347.VARDATA17));
		result.setVar_data18(getColumnString(cursor, Ctlm1347.VARDATA18));
		result.setVar_data19(getColumnString(cursor, Ctlm1347.VARDATA19));
		result.setVar_data20(getColumnString(cursor, Ctlm1347.VARDATA20));
		result.setDate_opr(getColumnString(cursor, Ctlm1347.DATEOPR));
		result.setId_view(getColumnString(cursor, Ctlm1347.IDVIEW));
		result.setvar_Json(getColumnString(cursor, Ctlm1347.VARJSON));
		result.setVar_version(getColumnString(cursor, Ctlm1347.VARVERSION));
		result.setId_table(getColumnString(cursor, Ctlm1347.IDTABLE));
		result.setInt_line(getColumnInt(cursor, Ctlm1347.INTLINE));
	}

	public static final String getNodeValue(String billno, String node,
			String field) {
		String result = "";
		StringBuffer sb = new StringBuffer();
		sb.append("select " + field + " from ").append(
				Tables.BusinessCtlm1347.NAME);
		sb.append(" where  1=1 and var_billno='" + billno + "' ");

		if (!StringUtil.isNullOrEmpty(node)) {
			sb.append(" and id_node ='" + node + "'");
		}

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = getColumnString(cursor, field);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static void setNodeValue(String billno, String Nodeid, String Field,
			String Values) {
		Ctlm1347 ctlm1347 = getNodeCtlm1347(billno, Nodeid);

		if (ctlm1347 != null) {
			ctlm1347.setVar_data(Field, Values);
			List<Ctlm1347> ctlm1347List = new ArrayList<Ctlm1347>();
			ctlm1347List.add(ctlm1347);
			replaceCTLM1347s(ctlm1347List);
		}
	}

	public static String getCtlm1347Values(String where) {

		String result = "";
		List<Ctlm1347> listRe = new ArrayList<Ctlm1347>();

		StringBuffer sb = new StringBuffer();
		sb.append("select  *  from ").append(Tables.BusinessCtlm1347.NAME);
		sb.append(" where    " + where);

		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Ctlm1347 ctlm1347 = new Ctlm1347();
				fillCtlm1347(ctlm1347, cursor);
				listRe.add(ctlm1347);
			}
		} finally {
			endDQL(database, cursor);
		}
		Gson gson = new Gson();
		result = gson.toJson(listRe);
		return result;
	}

	public static final String getPnodeid(String billno, String Nodeid) {
		String result = "";
		StringBuffer sb = new StringBuffer();
		sb.append("select  id_parentnode from ").append(
				Tables.BusinessCtlm1347.NAME);
		sb.append(" where  1=1 and var_billno='" + billno + "' and id_node ='"
				+ Nodeid + "'");
		SQLiteDatabase database = beginDQL();
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(sb.toString(), null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				result = getColumnString(cursor, Ctlm1347.IDPNODE);
			}
		} finally {
			endDQL(database, cursor);
		}
		return result;
	}

	public static final void deletectlm1347(String where) {
		String sql = "delete from ctlm1347 where  " + where;
		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL(sql);
		endDMLOffTransaction(database);
	}

	public static final void deletesrcctlm1347(String billno, String viewid,
			String pnode, String srcnode) {
		String sql = "delete from ctlm1347 where var_billno = '" + billno
				+ "' and id_parentnode =   '" + billno + "' and  id_srcnode ='"
				+ srcnode + "'";
		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL(sql);
		endDMLOffTransaction(database);
	}

	public static final void updateCtlm1347(String where) {
		SQLiteDatabase database = null;
		try {
			StringBuffer sql = new StringBuffer("update ctlm1347 set " + where);
			database = beginDMLOnTransaction();
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}

	public static final void replaceCtlm1347ByKey(String column, String key) {
		SQLiteDatabase database = null;
		try {
			StringBuffer sql = new StringBuffer("update ctlm1347 set ")
					.append(column).append(" = replace(").append(column)
					.append(",'").append(key).append("','') where ")
					.append(column).append(" like '%").append(key)
					.append("%' ");
			database = beginDMLOnTransaction();
			database.execSQL(sql.toString());
		} finally {
			endDMLOffTransaction(database);
		}
	}
}

package com.hjnerp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * SQLite数据库的帮助类
 * 
 * 该类属于扩展类主要承担数据库初始化和版本升级使用其他核心全由核心父类完成
 * 
 * @author 李庆义
 * 
 */
public class DataBaseHelper extends SQLiteHelper {

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// 创建数据库
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys = ON"); //打开参照完整性约束
		
		/************************************************************/
		/************     如果更新这里, 请务必更新BaseDao    **********/
		/***********************************************************/
		
		//注册信息配置表
		db.execSQL(Tables.ConfigTable.getCreateSQLString());

		// 工作流表
		db.execSQL(Tables.WorkFlowList.getCreatedSQLString());
		
		// 业务菜单
		db.execSQL(Tables.BusinessMenu.getCreatedSQLString());
		db.execSQL(Tables.BusinessCtlm1345.getCreatedSQLString());
		db.execSQL(Tables.BusinessCtlm4203.getCreateSQLString());
		// 业务表
		db.execSQL(Tables.BusinessCtlm1345.getCreatedSQLString());
		db.execSQL(Tables.BusinessCtlm1346.getCreatedSQLString());
		db.execSQL(Tables.BusinessCtlm1347.getCreatedSQLString());
		
		//企信
		db.execSQL(Tables.UserTable.getCreateSQLString());
		db.execSQL(Tables.ContactTable.getCreateSQLString());
		db.execSQL(Tables.EnterpriseInfoTable.getCreateSQLString());
		db.execSQL(Tables.GroupInfoTable.getCreateSQLString());
		db.execSQL(Tables.GroupRelationTable.getCreateSQLString());
		db.execSQL(Tables.TempContactTable.getCreateSQLString());
		
//		/**
//		 * @author haijian
//		 * 创建iq记录表
//		 */
//		db.execSQL(Tables.IQTable.getCreateSQLString());
	}

	// 升级数据库
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

	void upgradeTablesBySwap(SQLiteDatabase db, String tableName,
			String tableCreate, String columns) {
		try {
			db.beginTransaction();

			// 1, Rename table.
			String tempTableName = tableName + "_temp";
			String sql = "ALTER TABLE " + tableName + " RENAME TO "
					+ tempTableName;
			db.execSQL(sql);
			// 2, Create table.
			db.execSQL(tableCreate);
			// 3, Load data
			sql = "INSERT INTO " + tableName + " (" + columns + ") "
					+ " SELECT " + columns + " FROM " + tempTableName;
			db.execSQL(sql);
			// 4, Drop the temporary table.
			sql = "DROP TABLE IF EXISTS " + tempTableName;
			db.execSQL(sql);

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	void upgradeTablesByAddColumn(SQLiteDatabase db, String tableName,
			String featureCol, String column) {
		String oldTableCreateSql = "";
		Cursor cursor = db.rawQuery(
				"select sql from sqlite_master where tbl_name='" + tableName
						+ "' and type='table';", null);
		if (cursor.moveToNext())
			oldTableCreateSql = cursor.getString(0);
		if (!oldTableCreateSql.contains(featureCol)) {
			StringBuffer sql = new StringBuffer();
			sql.append("ALTER TABLE ").append(tableName)
					.append(" ADD COLUMN " + column);
			db.execSQL(sql.toString());
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

}

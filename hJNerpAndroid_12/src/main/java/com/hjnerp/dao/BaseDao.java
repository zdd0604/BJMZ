package com.hjnerp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.db.DataBaseHelper;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.db.Tables;
import com.hjnerp.util.myscom.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseDao
{
	// TODO Utility

	protected static final String getColumnString(Cursor cursor, String colName)
	{
		return cursor.getString(cursor.getColumnIndex(colName));
	}
	protected static final Integer getColumnInt(Cursor cursor, String colName)
	{
		return cursor.getInt(cursor.getColumnIndex(colName));
	}

	protected static final SQLiteDatabase beginDMLOnTransaction()
	{
		SQLiteDatabase database = helper.getWritableDatabase();
		database.beginTransaction(); 
		return database;
	}

	protected static final void endDMLOffTransaction(SQLiteDatabase database)
	{
		database.setTransactionSuccessful();
		database.endTransaction();
//		database.close();
	}

	protected static final SQLiteDatabase beginDQL()
	{
		return helper.getReadableDatabase();
	}

	protected static final void endDQL(SQLiteDatabase database, Cursor cursor)
	{
		if(cursor != null && !cursor.isClosed())
			cursor.close();
		helper.releaseReadableDatabase(database);
//		database.close();
	}
	
	protected static final SQLiteWorker worker = SQLiteWorker.getSharedInstance().start();
	
	//singleton DataBaseHelper class (the child class of SQLiteHelper)
	protected static final DataBaseHelper helper = EapApplication.getApplication().getDataBaseHelper();
	
	// TODO others
	
	public static final void clearToBase()
	{
		SQLiteDatabase database = beginDMLOnTransaction();
		
		/////////////////////删除/////////////////////////////
//		database.execSQL("DROP TABLE " + Tables.ConfigTable.NAME);
//		database.execSQL("DROP TABLE " + Tables.UserTable.NAME);
		database.execSQL("DROP TABLE " + Tables.ContactTable.NAME);
//		database.execSQL("DROP TABLE " + Tables.EnterpriseInfoTable.NAME);//暂时不删除其他登录用户的聊天记录
		database.execSQL("DROP TABLE " + Tables.GroupInfoTable.NAME);
		database.execSQL("DROP TABLE " + Tables.GroupRelationTable.NAME);
		database.execSQL("DROP TABLE " + Tables.TempContactTable.NAME);
		database.execSQL("DROP TABLE " + Tables.WorkFlowList.NAME);
		database.execSQL("DROP TABLE " + Tables.BusinessMenu.NAME);
		database.execSQL("DROP TABLE " + Tables.BusinessCtlm1345.NAME);
		database.execSQL("DROP TABLE " + Tables.BusinessCtlm1346.NAME);
		database.execSQL("DROP TABLE " + Tables.BusinessCtlm1347.NAME);
		database.execSQL("DROP TABLE " + Tables.BusinessCtlm4203.NAME);
		
		/////////////////////创建//////////////////////////
//		database.execSQL(Tables.ConfigTable.getCreateSQLString());
//		database.execSQL(Tables.UserTable.getCreateSQLString());
		database.execSQL(Tables.ContactTable.getCreateSQLString());
//		database.execSQL(Tables.EnterpriseInfoTable.getCreateSQLString());
		database.execSQL(Tables.GroupInfoTable.getCreateSQLString());
		database.execSQL(Tables.GroupRelationTable.getCreateSQLString());
		database.execSQL(Tables.TempContactTable.getCreateSQLString());
		database.execSQL(Tables.WorkFlowList.getCreatedSQLString());
		database.execSQL(Tables.BusinessMenu.getCreatedSQLString());
		database.execSQL(Tables.BusinessCtlm1345.getCreatedSQLString());
		database.execSQL(Tables.BusinessCtlm1346.getCreatedSQLString());
		database.execSQL(Tables.BusinessCtlm1347.getCreatedSQLString());
		database.execSQL(Tables.BusinessCtlm4203.getCreateSQLString());
		
		endDMLOffTransaction(database);
	}
	
	//清除数据，清除聊天记录表1356和业务表1347
	public static final void wrapData()
	{
		SQLiteDatabase database = beginDMLOnTransaction();
		database.execSQL("DROP TABLE " + Tables.BusinessCtlm1347.NAME);
		database.execSQL("DROP TABLE " + Tables.EnterpriseInfoTable.NAME);
		

		database.execSQL(Tables.BusinessCtlm1347.getCreatedSQLString());
		database.execSQL(Tables.EnterpriseInfoTable.getCreateSQLString());
		
		endDMLOffTransaction(database);
	}
	
	// 清除缓存
	public static final void wrapcache() {
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
		
		FileUtils.deleteDirectory(Constant.HJPHOTO_CACHE_DIR);//清除业务图片缓存
		FileUtils.deleteDirectory(Constant.TEMP_DIR);//清除聊天图片缓存
//		FileUtils.deleteDirectory(Constant.CHAT_CACHE_DIR);//清除聊天图片缓存
		FileUtils.deleteDirectory(Constant.CHATAUDIO_DIR);//清除聊天图片缓存
	}
	
	
	
}

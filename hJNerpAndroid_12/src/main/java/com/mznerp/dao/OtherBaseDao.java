package com.mznerp.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mznerp.db.SQLiteWorker.AbstractSQLable;
import com.mznerp.db.Tables;
import com.mznerp.model.IDComConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OtherBaseDao extends BaseDao {
    /**
     * 插入或修改企业注册信息
     */
    public static final void replaceRegInfo(
            final List<Map<String, Object>> infos) {
        worker.postDML(new AbstractSQLable() {
            public Object doAysncSQL() {
                SQLiteDatabase database = beginDMLOnTransaction();
                try {
                    ContentValues values = new ContentValues();
                    for (Map<String, Object> map : infos) {
                        values.clear();
                        values.put(Tables.ConfigTable.COL_ID_COM,
                                map.get("comid").toString());
                        values.put(Tables.ConfigTable.COL_ID_USER, "");
                        values.put(Tables.ConfigTable.COL_NAME_COM,
                                String.valueOf(map.get("comName")));
                        // String[] urls =
                        // map.get("serverURL").toString().split(";");
                        values.put(Tables.ConfigTable.COL_URL_HTTP,
                                map.get("serverURL").toString());
                        values.put(Tables.ConfigTable.COL_URL_WEBSOCKET, "");
                        values.put(Tables.ConfigTable.COL_FLAG_MAP,
                                map.get("flagMap").toString());
                        values.put(Tables.ConfigTable.COL_VAR_WORKTABLE,
                                map.get("varWorktable") == null ? "" : map.get("varWorktable").toString());
                        database.replace(Tables.ConfigTable.NAME, null, values);
                    }
                } finally {
                    endDMLOffTransaction(database);
                }
                return null;
            }

            public void onCompleted(Object event) {
                // if (!(event instanceof Throwable))
                // {
                // }
            }
        });
    }

//	public static final List<Map<String, String>> queryAllRegInfoMaps() {
//		return queryRegInfos("select * from " + Tables.ConfigTable.NAME);
//	}

//	private static final List<Map<String, String>> queryRegInfos(String sql) {
//		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
//		SQLiteDatabase database = beginDQL();
//		Cursor cursor = null;
//		try {
//			cursor = database.rawQuery(sql, null);
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				HashMap<String, String> map = new HashMap<String, String>();
//				map.put(Tables.ConfigTable.COL_ID_COM,
//						getColumnString(cursor, Tables.ConfigTable.COL_ID_COM));
//				map.put(Tables.ConfigTable.COL_ID_USER,
//						getColumnString(cursor, Tables.ConfigTable.COL_ID_USER));
//				map.put(Tables.ConfigTable.COL_NAME_COM,
//						getColumnString(cursor, Tables.ConfigTable.COL_NAME_COM));
//				map.put(Tables.ConfigTable.COL_URL_HTTP,
//						getColumnString(cursor, Tables.ConfigTable.COL_URL_HTTP));
//				map.put(Tables.ConfigTable.COL_URL_WEBSOCKET,
//						getColumnString(cursor,
//								Tables.ConfigTable.COL_URL_WEBSOCKET));
//				result.add(map);
//			}
//		} finally {
//			endDQL(database, cursor);
//		}
//		return result;
//	}

    public static final List<IDComConfig> queryAllRegInfos() {
        ArrayList<IDComConfig> result = new ArrayList<IDComConfig>();
        SQLiteDatabase database = beginDQL();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from "
                    + Tables.ConfigTable.NAME, null);

//				result = new ArrayList<IDComConfig>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                IDComConfig reusltConifg = new IDComConfig();
                reusltConifg.setId_com(getColumnString(cursor,
                        Tables.ConfigTable.COL_ID_COM));
                reusltConifg.setName_com(getColumnString(cursor,
                        Tables.ConfigTable.COL_NAME_COM));
                reusltConifg.setId_user(getColumnString(cursor,
                        Tables.ConfigTable.COL_ID_USER));
                reusltConifg.setUrl_http(getColumnString(cursor,
                        Tables.ConfigTable.COL_URL_HTTP));
                reusltConifg.setUrl_websocket(getColumnString(cursor,
                        Tables.ConfigTable.COL_URL_WEBSOCKET));
                // HashMap<String, String> map = new HashMap<String,
                // String>();
                // map.put(Tables.ConfigTable.COL_ID_COM,
                // getColumnString(cursor,
                // Tables.ConfigTable.COL_ID_COM));
                // map.put(Tables.ConfigTable.COL_ID_USER,
                // getColumnString(cursor,
                // Tables.ConfigTable.COL_ID_USER));
                // map.put(Tables.ConfigTable.COL_NAME_COM,
                // getColumnString(cursor,
                // Tables.ConfigTable.COL_NAME_COM));
                // map.put(Tables.ConfigTable.COL_URL_HTTP,
                // getColumnString(cursor,
                // Tables.ConfigTable.COL_URL_HTTP));
                // map.put(Tables.ConfigTable.COL_URL_WEBSOCKET,
                // getColumnString(cursor,
                // Tables.ConfigTable.COL_URL_WEBSOCKET));
                result.add(reusltConifg);
            }
        } finally {
            endDQL(database, cursor);
        }
        return result;
    }

    public static final IDComConfig queryReginfo(String idCom) {
        String sql = "select * from    " + Tables.ConfigTable.NAME
                + " where id_com = '" + idCom + "'";
        IDComConfig reusltConifg = null;
        SQLiteDatabase database = beginDQL();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            if (cursor.getCount() != 0) {
                reusltConifg = new IDComConfig();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    reusltConifg.setId_com(getColumnString(cursor,
                            Tables.ConfigTable.COL_ID_COM));
                    reusltConifg.setName_com(getColumnString(cursor,
                            Tables.ConfigTable.COL_NAME_COM));
                    reusltConifg.setId_user(getColumnString(cursor,
                            Tables.ConfigTable.COL_ID_USER));
                    reusltConifg.setUrl_http(getColumnString(cursor,
                            Tables.ConfigTable.COL_URL_HTTP));
                    reusltConifg.setUrl_websocket(getColumnString(cursor,
                            Tables.ConfigTable.COL_URL_WEBSOCKET));
                    reusltConifg.setFlag_map(getColumnString(cursor,
                            Tables.ConfigTable.COL_FLAG_MAP));
                    if (!TextUtils.isEmpty(Tables.ConfigTable.COL_VAR_WORKTABLE)) {
                        reusltConifg.setVar_worktable(getColumnString(cursor,
                                Tables.ConfigTable.COL_VAR_WORKTABLE));
                    }
                }
            }
        } finally {
            endDQL(database, cursor);
        }
        return reusltConifg;

    }


}

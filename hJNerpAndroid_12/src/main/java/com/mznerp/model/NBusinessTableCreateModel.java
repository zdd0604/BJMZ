package com.mznerp.model;

import java.util.ArrayList;
import java.util.List;

import com.mznerp.util.myscom.StringUtils;

/**
 * 数据库插入
 * SQLite语句
 */
public class NBusinessTableCreateModel
{
	public String table;
	public String condition;
	public String cols;
	public List<String> values;
	public List<String> sqls = new ArrayList<>();
	
	public String insertSqls;
	public String deleteSql;
	
	public void create()
	{
		if(!values.isEmpty())
		{
			StringBuffer sb = new StringBuffer();
			sb.append("delete from ").append(table).append(" where ").append(condition);
			deleteSql = sb.toString();
			sb = new StringBuffer();
			sb.append("insert or replace into ").append(table).append(" (").append(cols).append(")")
			.append(" select ").append(StringUtils.join(values, " union select "));
			insertSqls = sb.toString();

			//
			sb = new StringBuffer();
			sb.append("insert or replace into ").append(table).append(" (").append(cols).append(")");
			String pixUrl = sb.toString();
			for (int i = 0; i < values.size(); i++) {
				sqls.add(pixUrl+" values("+values.get(i)+")");
			}
			values = null;
		}
	}
}

package com.hjnerp.manager;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.hjnerp.common.Constant;
import com.hjnerp.db.DBManager;
import com.hjnerp.db.SQLiteTemplate;
import com.hjnerp.db.SQLiteTemplate.RowMapper;
import com.hjnerp.model.DeptInfo;
import com.hjnerp.model.FriendInfo;


public class ContactManager {

	/**
	 * 保存着所有的联系人信息
	 */
	private static ContactManager contactManager = null;
	private static DBManager manager = null;
	
	private ContactManager(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(
				Constant.LOGIN_SET, Context.MODE_PRIVATE);
		String databaseName = sharedPre.getString(Constant.USERNAME, null);
		manager = DBManager.getInstance(context, databaseName);
	}
	
	public static ContactManager getInstance(Context context) {

		if (contactManager == null) {
			contactManager = new ContactManager(context);
		}

		return contactManager;
	}
	
	/**
	 * 获得所有的联系人列表
	 * 
	 * @return
	 */
	public static List<FriendInfo> getFriendList() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<FriendInfo> list = st.queryForList(
				new RowMapper<FriendInfo>() {
					@Override
					public FriendInfo mapRow(Cursor cursor, int index) {
						FriendInfo friend = new FriendInfo(); 
						friend.setFriendid(cursor.getString(cursor
								.getColumnIndex("friendid")));
						friend.setFriendname(cursor.getString(cursor
								.getColumnIndex("friendname")));
						friend.setFriendmtel(cursor.getString(cursor
								.getColumnIndex("friendmtel")));
						friend.setFriendmail(cursor.getString(cursor
								.getColumnIndex("friendmail")));
						friend.setDeptid(cursor.getString(cursor
								.getColumnIndex("deptid")));
						friend.setDeptname(cursor.getString(cursor
								.getColumnIndex("deptname")));
						friend.setFriendimage(cursor.getString(cursor
								.getColumnIndex("friendphoto")));
						friend.setFrienddescribe(cursor.getString(cursor
								.getColumnIndex("frienddescribe")));
						
						return friend;
					}
				},
				"SELECT friendid  ,friendname  ,friendmtel ,friendmail  ,deptid  ,deptname    ,friendimage  ,frienddescribe  FROM  friendInfo   ",
				 null);
		return list;	
	}

	public static List<FriendInfo> getFriendList(String deptid) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<FriendInfo> list = st.queryForList(
				new RowMapper<FriendInfo>() {
					@Override
					public FriendInfo mapRow(Cursor cursor, int index) {
						FriendInfo friend = new FriendInfo(); 
						friend.setFriendid(cursor.getString(cursor
								.getColumnIndex("friendid")));
						friend.setFriendname(cursor.getString(cursor
								.getColumnIndex("friendname")));
						friend.setFriendmtel(cursor.getString(cursor
								.getColumnIndex("friendmtel")));
						friend.setFriendmail(cursor.getString(cursor
								.getColumnIndex("friendmail")));
						friend.setDeptid(cursor.getString(cursor
								.getColumnIndex("deptid")));
						friend.setDeptname(cursor.getString(cursor
								.getColumnIndex("deptname")));
						friend.setFriendimage(cursor.getString(cursor
								.getColumnIndex("friendphoto")));
						friend.setFrienddescribe(cursor.getString(cursor
								.getColumnIndex("frienddescribe")));
						
						return friend;
					}
				},
				"SELECT friendid  ,friendname  ,friendmtel ,friendmail  ,deptid  ,deptname    ,friendphoto  ,frienddescribe  FROM  friendInfo  where deptid=?  ",
				new String []{ deptid} );
		return list;	
	}
	
	public static List<DeptInfo> getDeptAllList()
	{
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<DeptInfo> list = st.queryForList(
				new RowMapper<DeptInfo>() {
					@Override
					public DeptInfo mapRow(Cursor cursor, int index) {
						DeptInfo dept = new DeptInfo(); 
						dept.setDeptId(cursor.getString(cursor
								.getColumnIndex("id_dept")));
						dept.setDeptName (cursor.getString(cursor
								.getColumnIndex("name_dept")));
						dept.setChildCount( cursor.getInt(cursor
								.getColumnIndex("setChildCount")));
						return dept;
					}
				},
				"SELECT  deptid  ,deptname   ,count(*)  setChildCount  FROM  friendInfo  group by  deptid  ,deptname  ",
				 null);
		return list;	
		
	}
	
 
	 
	public static   FriendInfo  getFriendInfo( String friendid)
	{
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		 FriendInfo friendinfo  = st.queryForObject(  
				 
				new RowMapper<FriendInfo>() {
					@Override
					public FriendInfo mapRow(Cursor cursor, int index) {
						FriendInfo friend = new FriendInfo(); 
						friend.setFriendid(cursor.getString(cursor
								.getColumnIndex("friendid")));
						friend.setFriendname(cursor.getString(cursor
								.getColumnIndex("friendname")));
						friend.setFriendmtel(cursor.getString(cursor
								.getColumnIndex("friendmtel")));
						friend.setFriendmail(cursor.getString(cursor
								.getColumnIndex("friendmail")));
						friend.setDeptid(cursor.getString(cursor
								.getColumnIndex("deptid")));
						friend.setDeptname(cursor.getString(cursor
								.getColumnIndex("deptname")));
//						friend.setFriendimage(cursor.getString(cursor
//								.getColumnIndex("friendphoto")));
						friend.setFrienddescribe(cursor.getString(cursor
								.getColumnIndex("frienddescribe")));
						
						return friend;
					}
				},
				"SELECT friendid  ,friendname  ,friendmtel ,friendmail  ,deptid  ,deptname      ,frienddescribe  FROM  friendInfo  where  friendid=? ",
				new String []{ friendid} );
		return friendinfo;
		
	}
	 
}

package com.jiang.deliciousfood.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLDBHelper extends SQLiteOpenHelper{

	public SQLDBHelper(Context context) {
		/**
		 * 上下文参数
		 * 数据库名称
		 * 游标工厂（null）
		 * 版本号 （大于等于1的整数）
		 */
		super(context, "collections.db", null, 1);

	}

	/**
	 * 创建数据库，（只执行一次，一旦数据库存在，就不在执行）
	 * 方法被调用数据库就会被创建，只需要在该方法里面创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists collections(_id integer primary key,name varchar(30))");
		//不需要关闭数据库
		Log.i("jiang", "onCreate 被执行了");
	}

	/**
	 * 升级数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists collections");//删除已有的表
		onCreate(db);//再去创建表
		Log.i("jiang", "onUpgrade  被执行了");
	}

}

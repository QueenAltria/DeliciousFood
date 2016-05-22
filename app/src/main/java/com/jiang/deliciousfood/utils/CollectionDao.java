package com.jiang.deliciousfood.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiang.deliciousfood.bean.CollectionBean;

public class CollectionDao {
	SQLDBHelper dbHelper = null;

	public CollectionDao(Context context) {
		super();
		dbHelper = new SQLDBHelper(context);
	};

	/**
	 * 插入数据
	 */
	public boolean insert(CollectionBean collectionBean) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put("_id", collectionBean.getFoodId());
		values.put("name", collectionBean.getFoodName());
		long i = db.insert("collections", null, values);
		db.close();
		return i == -1 ? false : true;
	}

	/**
	 * 删除数据
	 */
	public void delete(int id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.delete("collections", "_id = ?", new String[]{id + ""});
		db.close();
	}

	/**
	 * 更新数据
	 */
	public void update(CollectionBean coll) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ContentValues values = new ContentValues();

		values.put("name", coll.getFoodName());

		db.update("collections", values, "_id = ?", new String[]{coll.getFoodId()+""});
		db.close();
	}

	/**
	 * 根据Id查询
	 */
	public CollectionBean findById(int id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		//db.rawQuery(sql, selectionArgs)
		Cursor cursor = db.query("collections", null, "_id = ?", new String[]{id+""}, null, null, null);
		CollectionBean collectionBean = null;
		if (cursor.moveToNext()) {
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));

			collectionBean = new CollectionBean(_id, name);
		}
		db.close();
		return collectionBean;
	}

	public List<CollectionBean> findAll() {
		List<CollectionBean> list = new ArrayList<>();

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("collections", null, null, null, null, null, null);
		CollectionBean collectionBean = null;
		while (cursor.moveToNext()) {
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			collectionBean = new CollectionBean(_id, name);
			list.add(collectionBean);
		}
		db.close();
		return list;
	}
}

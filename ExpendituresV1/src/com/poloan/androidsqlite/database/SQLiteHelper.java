package com.poloan.androidsqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "expenditures.db";
	public static final int DB_VERSION = 1;

	public static final String EXPEN_TABLES = "expenditures";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_EXPENDITURES = "amount";
	public static final String COLUMN_DATE = "date";

	private static final String DATABASE_CREATE = "create table "
			+ EXPEN_TABLES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_EXPENDITURES
			+ " text not null, " + COLUMN_DATE + " long not null);";

	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(SQLiteHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + EXPEN_TABLES);
		onCreate(db);
	}

}

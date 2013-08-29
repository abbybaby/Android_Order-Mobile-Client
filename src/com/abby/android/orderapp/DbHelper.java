package com.abby.android.orderapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public static String dbName="androidtest8.db";
	Context context;
	public DbHelper(Context context) {
		super(context, dbName, null, 1);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	public void deleteDb() {
		context.deleteDatabase(dbName);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists food (id integer primary key,code varchar(5),type_id integer,name varchar(20),price integer,description varchar(100))");
		db.execSQL("create table if not exists food_type (id integer primary key,name varchar(10))");
		db.execSQL("create table if not exists tables (id integer primary key,code varchar(5),seats integer,description varchar(50))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}

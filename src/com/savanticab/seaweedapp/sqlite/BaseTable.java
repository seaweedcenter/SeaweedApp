package com.savanticab.seaweedapp.sqlite;

import android.database.sqlite.SQLiteDatabase;

public abstract class BaseTable {
	public static String TABLE_NAME;
	protected static String DATABASE_CREATE;

	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(database);
	  }

}

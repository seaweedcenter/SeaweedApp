package com.savanticab.seaweedapp.sqlite;

import android.database.sqlite.SQLiteDatabase;

public class RawMaterialTable {
	
	// Database table
	  public static final String TABLE_NAME = "raw_materials";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_UNIT = "unit";
	  public static final String COLUMN_STOCK_QUANTITY = "stock_quantity";
	  public static final String COLUMN_ORDERED_QUANTITY = "ordered_quantity";
	  public static final String COLUMN_ALLOCATED_FOR_PRODUCTION_QUANTITY = "allocated_for_production_qty";
	  public static final String COLUMN_ICON = "icon";

	  // Database creation SQL statement
	  protected static final String DATABASE_CREATE = "create table " 
	      + TABLE_NAME
	      + "(" 
	      //+ COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_ID + " integer primary key, "
	      + COLUMN_NAME + " text not null unique, " 
	      + COLUMN_UNIT + " text, " 
	      + COLUMN_STOCK_QUANTITY + " real, " 
	      + COLUMN_ORDERED_QUANTITY + " real, "
	      + COLUMN_ALLOCATED_FOR_PRODUCTION_QUANTITY + " real, "
	      + COLUMN_ICON + " text"
	      + ");";
	  
	  public static void onCreate(SQLiteDatabase database) {
		  database.execSQL(DATABASE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
		  database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		  onCreate(database);
	  }

}

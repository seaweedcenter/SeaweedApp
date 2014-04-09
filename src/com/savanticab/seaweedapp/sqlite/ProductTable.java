package com.savanticab.seaweedapp.sqlite;

import android.database.sqlite.SQLiteDatabase;


public class ProductTable  {
	
	// Database table
	  public static final String TABLE_NAME = "products";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_CODE = "code";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_FRAGANCE = "fragance";
	  public static final String COLUMN_SIZE = "size";
	  public static final String COLUMN_PRICE = "price";
	  public static final String COLUMN_INSTOCKQTY = "inStockQty";
	  public static final String COLUMN_INPRODUCTIONQTY = "inProductionQty";
	  
	  // Database creation SQL statement
	  //TODO: make name+fragance+size combination unique?
	  protected static final String DATABASE_CREATE = "create table " 
	      + TABLE_NAME
	      + "(" 
	      //+ COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_ID + " integer primary key, " 
	      + COLUMN_CODE + " text not null unique, "
	      + COLUMN_NAME + " text not null, " 
	      + COLUMN_FRAGANCE + " text," 
	      + COLUMN_SIZE + " text, " 
	      + COLUMN_PRICE + " real, "
	      + COLUMN_INSTOCKQTY + " real, "
	      + COLUMN_INPRODUCTIONQTY + " real"
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

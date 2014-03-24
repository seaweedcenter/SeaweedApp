package com.savanticab.seaweedapp.sqlite;


public class ProductTable extends BaseTable {
	
	// Database table
	  public static final String TABLE_NAME = "products";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_CODE = "code";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_FRAGANCE = "fragance";
	  public static final String COLUMN_SIZE = "size";

	  // Database creation SQL statement
	  protected static final String DATABASE_CREATE = "create table " 
	      + TABLE_NAME
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_CODE + " text not null, "
	      + COLUMN_NAME + " text not null, " 
	      + COLUMN_FRAGANCE + " text," 
	      + COLUMN_SIZE + " text" 
	      + ");";

}

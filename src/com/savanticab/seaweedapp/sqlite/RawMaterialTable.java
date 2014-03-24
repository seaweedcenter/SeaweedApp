package com.savanticab.seaweedapp.sqlite;

public class RawMaterialTable extends BaseTable {
	
	// Database table
	  public static final String TABLE_NAME = "raw_materials";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_UNIT = "unit";
	  public static final String COLUMN_STOCK_QUANTITY = "stock_quantity";
	  public static final String COLUMN_ORDERED_QUANTITY = "ordered_quantity";
	  public static final String COLUMN_ICON = "icon";

	  // Database creation SQL statement
	  protected static final String DATABASE_CREATE = "create table " 
	      + TABLE_NAME
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_NAME + " text not null, " 
	      + COLUMN_UNIT + " text, " 
	      + COLUMN_STOCK_QUANTITY + " real, " 
	      + COLUMN_ORDERED_QUANTITY + " real, "
	      + COLUMN_ICON + " text"
	      + ");";

}

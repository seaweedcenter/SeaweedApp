package com.savanticab.seaweedapp.sqlite;

public class RecipeTable extends BaseTable {
	// Database table
		  public static final String TABLE_NAME = "recipes";
		  public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_PRODUCT_ID = "product";
		  public static final String COLUMN_RAW_MATERIAL_ID = "raw_material";
		  public static final String COLUMN_QUANTITY = "quantity";

		  // Database creation SQL statement
		  protected static final String DATABASE_CREATE = "create table " 
		      + TABLE_NAME
		      + "(" 
		      + COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_PRODUCT_ID + " integer not null, " 
		      + COLUMN_RAW_MATERIAL_ID + " integer not null, " 
		      + COLUMN_QUANTITY + " real" 
		      + ");";

}

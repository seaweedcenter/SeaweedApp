package com.savanticab.seaweedapp.sqlite;

public class RecipeTable extends BaseTable {
	// Database table
		  public static final String TABLE_NAME = "recipes";
		  //public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_PRODUCT_ID = "product_id";
		  public static final String COLUMN_RAW_MATERIAL_ID = "rawmaterial_id";
		  public static final String COLUMN_QUANTITY = "quantity";

		  // Database creation SQL statement
		  protected static final String DATABASE_CREATE = "create table " 
		      + TABLE_NAME
		      + "(" 
		      //+ COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_PRODUCT_ID + " integer not null references " + ProductTable.TABLE_NAME + "("+ProductTable.COLUMN_ID+"), " 
		      + COLUMN_RAW_MATERIAL_ID + " integer not null references " + RawMaterialTable.TABLE_NAME + "("+RawMaterialTable.COLUMN_ID+"), " 
		      + COLUMN_QUANTITY + " real, " 
			  + "primary key(" + COLUMN_PRODUCT_ID + ", " + COLUMN_RAW_MATERIAL_ID + ")"
		      + ");";

}

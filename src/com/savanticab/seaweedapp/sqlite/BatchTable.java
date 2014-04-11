package com.savanticab.seaweedapp.sqlite;

import java.util.Date;

import com.savanticab.seaweedapp.model.Recipe;

import android.database.sqlite.SQLiteDatabase;

public class BatchTable {
	// database table
	
	public static final String TABLE_NAME = "batches";
	//public static final String COLUMN_ID = "_id";
	public static final String COLUMN_BATCH_ID = "_id";
	public static final String COLUMN_RECIPE_ID = "recipe_id";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_STARTDATE = "startDate";
	public static final String COLUMN_FINISHDATE = "finishDate";
	
	// Database creation SQL statement
	protected static final String DATABASE_CREATE = "create table " 
	  + TABLE_NAME
	  + "("
	  + COLUMN_BATCH_ID + " integer primary key, "
	  + COLUMN_RECIPE_ID + " integer not null references "  + RecipeTable.TABLE_NAME + "("+RecipeTable.COLUMN_PRODUCT_ID+"), " 
	  + COLUMN_QUANTITY + " integer, "
	  + COLUMN_STARTDATE + " text, "
	  + COLUMN_FINISHDATE + " text"
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

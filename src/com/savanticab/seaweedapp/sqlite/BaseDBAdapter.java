package com.savanticab.seaweedapp.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDBAdapter<T> {

	protected MySQLiteHelper helper;
	protected Context mContext;
	public abstract String getTableName();
	protected abstract String getColumnIdName(); //first column of the database table
	
	public BaseDBAdapter(Context context){
		  helper = MySQLiteHelper.getInstance(context);
		  mContext = context;
	}

    public abstract ContentValues getContentValues(T item);
    public abstract T loadFromCursor(Cursor cursor);
    
    public void add(T item) {        
        SQLiteDatabase db = helper.getWritableDatabase();
        
        db.insert(getTableName(), null, getContentValues(item));
        db.close();
    }
    
    public List<T> getAll() {
    	
        String query = "SELECT  * FROM " + getTableName();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        List<T> items = new ArrayList<T>();
        T item = null;
        
        if (cursor.moveToFirst()) {
            do {
            	item = loadFromCursor(cursor);
        		
            	items.add(item);
            } while (cursor.moveToNext());
        }
        db.close();
        return items;
    }

    protected T find(String query){

    	SQLiteDatabase db = helper.getWritableDatabase();
    	Cursor cursor = db.rawQuery(query, null);
    	T item = null;
    	
    	if (cursor.moveToFirst()) {
    		item = loadFromCursor(cursor);
    		cursor.close();
    	}
        db.close();
    	return item;
    }
    
    protected int update(T item, String selections, String[] selectionArgs) {
		 
        //get reference to writable DB
        SQLiteDatabase db = helper.getWritableDatabase();
        
        //updating row
        int i = db.update(getTableName(), //table
                getContentValues(item), // column/value
                selections, // selections
                selectionArgs); //selection args
        //close
        db.close();
        return i; 
    }
    
    protected boolean delete(String query){
	    
    	boolean result = false;
    	SQLiteDatabase db = helper.getWritableDatabase();
    	Cursor cursor = db.rawQuery(query, null);
    	
    	if (cursor.moveToFirst()) {
    		db.delete(getTableName(), getColumnIdName() + " = ?",
    	            new String[] { cursor.getString(0) });
    		cursor.close();
    		result = true;
    	}
        db.close();
    	return result;
    }
}

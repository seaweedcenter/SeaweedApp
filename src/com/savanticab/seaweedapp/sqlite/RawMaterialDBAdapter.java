package com.savanticab.seaweedapp.sqlite;

import com.savanticab.seaweedapp.model.RawMaterial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class RawMaterialDBAdapter extends BaseDBAdapter<RawMaterial>{
	

	// Database table
	  public static final String TABLE_NAME = "raw_materials";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_UNIT = "unit";
	  public static final String COLUMN_ICON = "icon";

	  // Database creation SQL statement
	  protected static final String DATABASE_CREATE = "create table " 
	      + TABLE_NAME
	      + "(" 
	      //+ COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_ID + " integer primary key, "
	      + COLUMN_NAME + " text not null unique, " 
	      + COLUMN_UNIT + " text, "
	      + COLUMN_ICON + " text"
	      + ");";


		@Override public String getTableName() { return TABLE_NAME; }
		@Override protected String getColumnIdName() { return COLUMN_ID; }
		
		public RawMaterialDBAdapter(Context context) {
			super(context);
		}


		public ContentValues getContentValues(RawMaterial material) {
			ContentValues values = new ContentValues();
	    	values.put(RawMaterialDBAdapter.COLUMN_ID, material.getId());
	        values.put(RawMaterialDBAdapter.COLUMN_NAME, material.getName());
	        values.put(RawMaterialDBAdapter.COLUMN_UNIT, material.getUnit());
	        values.put(RawMaterialDBAdapter.COLUMN_ICON, material.getIcon());
			return values;
		}
		
		public RawMaterial loadFromCursor(Cursor cursor) {
			RawMaterial material = new RawMaterial();
			material.setId(Integer.parseInt(cursor.getString(0)));
			material.setName(cursor.getString(1));
			material.setUnit(cursor.getString(2));
			material.setIcon(cursor.getString(3));
			return material;
		}

	    public RawMaterial findRawMaterialById(int materialId){
	    	String query = "Select * FROM " + RawMaterialDBAdapter.TABLE_NAME + " WHERE " + RawMaterialDBAdapter.COLUMN_ID + " =  \"" + materialId + "\"";
	    	return find(query);
	    }
	    public RawMaterial findRawMaterialByName(String materialname){
	    	String query = "Select * FROM " + RawMaterialDBAdapter.TABLE_NAME + " WHERE " + RawMaterialDBAdapter.COLUMN_NAME + " =  \"" + materialname + "\"";
	    	return find(query);
	    }
	    
	    public int updateRawMaterial(RawMaterial material) {
	    	
	        return update(material, COLUMN_ID + " = ?", new String[] { String.valueOf(material.getId()) });
	    }
	    
	    public boolean deleteRawMaterial(String materialname){
	    	
	    	String query = "Select * FROM " + RawMaterialDBAdapter.TABLE_NAME + " WHERE " + RawMaterialDBAdapter.COLUMN_NAME + " =  \"" + materialname + "\"";
	    	return delete(query);
	    }

}

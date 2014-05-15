package com.savanticab.seaweedapp.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.savanticab.seaweedapp.model.RawMaterial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RawMaterialDBAdapter extends BaseDBAdapter{
	

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
		
	    public void addRawMaterial(RawMaterial material){
	 
	        SQLiteDatabase db = helper.getWritableDatabase();
	        db.insert(RawMaterialDBAdapter.TABLE_NAME, null, getContentValues(material));
	        db.close();
	    }

	    public RawMaterial findRawMaterialById(int materialId){
	    	String query = "Select * FROM " + RawMaterialDBAdapter.TABLE_NAME + " WHERE " + RawMaterialDBAdapter.COLUMN_ID + " =  \"" + materialId + "\"";
	    	return findRawMaterial(query);
	    }
//	    public HashMap<RawMaterial, MaterialInventory> findRawMaterialByName(String materialname){
//	    	String query = "Select * FROM " + RawMaterialTable.TABLE_NAME + " WHERE " + RawMaterialTable.COLUMN_NAME + " =  \"" + materialname + "\"";
//	    	return findRawMaterial(query);
//	    }
	    public RawMaterial findRawMaterialByName(String materialname){
	    	String query = "Select * FROM " + RawMaterialDBAdapter.TABLE_NAME + " WHERE " + RawMaterialDBAdapter.COLUMN_NAME + " =  \"" + materialname + "\"";
	    	return findRawMaterial(query);
	    }
	    
	    private RawMaterial findRawMaterial(String query){    	
	    
	    	SQLiteDatabase db = helper.getWritableDatabase();
	    	Cursor cursor = db.rawQuery(query, null);
	    	RawMaterial material = null;
	    	
	    	if (cursor.moveToFirst()) {
	    		material = loadFromCursor(cursor);
	    		cursor.close();
	    	}
	        db.close();
	    	return material;
	    }
	    
	    public List<RawMaterial> getAllRawMaterials() {
	    	
	        String query = "SELECT  * FROM " + RawMaterialDBAdapter.TABLE_NAME;
	        SQLiteDatabase db = helper.getWritableDatabase();
	        Cursor cursor = db.rawQuery(query, null);
	 
	        RawMaterial material = null;
	        List<RawMaterial> materials = new ArrayList<RawMaterial>();
	        
	        if (cursor.moveToFirst()) {
	            do {
	            	material = loadFromCursor(cursor);
	            	materials.add(material);
	            } while (cursor.moveToNext());
	        }
	        return materials;
	    }
	    
	    public int updateRawMaterial(RawMaterial material) {
	    	
	        SQLiteDatabase db = helper.getWritableDatabase();
	        
	        int i = db.update(RawMaterialDBAdapter.TABLE_NAME, //table
	                getContentValues(material), // column/value
	                RawMaterialDBAdapter.COLUMN_ID+" = ?", // selections
	                new String[] { String.valueOf(material.getId()) }); //selection args
	        
	        db.close();
	        return i;
	    }
	    
	    public boolean deleteRawMaterial(String materialname){
	    	
	    	boolean result = false;
	    	String query = "Select * FROM " + RawMaterialDBAdapter.TABLE_NAME + " WHERE " + RawMaterialDBAdapter.COLUMN_NAME + " =  \"" + materialname + "\"";
	    	SQLiteDatabase db = helper.getWritableDatabase();
	    	Cursor cursor = db.rawQuery(query, null);
	    	
	    	if (cursor.moveToFirst()) {
	    		db.delete(RawMaterialDBAdapter.TABLE_NAME, RawMaterialDBAdapter.COLUMN_ID + " = ?",
	    	            new String[] { cursor.getString(0) });
	    		cursor.close();
	    		result = true;
	    	}
	        db.close();
	    	return result;
	    }
}

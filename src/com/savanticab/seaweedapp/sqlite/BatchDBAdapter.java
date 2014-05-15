package com.savanticab.seaweedapp.sqlite;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.savanticab.seaweedapp.model.Batch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BatchDBAdapter extends BaseDBAdapter{
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
	  + COLUMN_RECIPE_ID + " integer not null references "  + RecipeDBAdapter.TABLE_NAME + "("+RecipeDBAdapter.COLUMN_PRODUCT_ID+"), " 
	  + COLUMN_QUANTITY + " integer, "
	  + COLUMN_STARTDATE + " text, "
	  + COLUMN_FINISHDATE + " text"
	  + ");";
	

	public BatchDBAdapter(Context context) {
		super(context);
	}

	public ContentValues getContentValues(Batch batch) {
		ContentValues values = new ContentValues();
        values.put(BatchDBAdapter.COLUMN_BATCH_ID, batch.getId());
        values.put(BatchDBAdapter.COLUMN_RECIPE_ID, batch.getRecipe().getProduct().getId());
        values.put(BatchDBAdapter.COLUMN_QUANTITY, batch.getQuantity());
        String startDate = (batch.getStartDate() != null) ? Long.toString(batch.getStartDate().getTime()) : "null";
        String finishDate = (batch.getFinishDate() != null) ? Long.toString(batch.getFinishDate().getTime()) : "null";
        values.put(BatchDBAdapter.COLUMN_STARTDATE, startDate);
        values.put(BatchDBAdapter.COLUMN_FINISHDATE, finishDate);
		return values;
	}
	
	public Batch loadFromCursor(Cursor cursor) {
		Batch batch = new Batch();
		batch.setId(Integer.parseInt(cursor.getString(0)));
		
		// recipe must be loaded separately from other table
		int recipeId = Integer.parseInt(cursor.getString(1));
		batch.setRecipe(new RecipeDBAdapter(mContext).findRecipeById(recipeId));
		
		batch.setQuantity(Integer.parseInt(cursor.getString(2)));
		String startDate = cursor.getString(3);
		batch.setStartDate(!startDate.equalsIgnoreCase("null") ? new Date(Long.parseLong(startDate)):null);
		String finishDate = cursor.getString(4);
		batch.setFinishDate(!finishDate.equalsIgnoreCase("null") ? new Date(Long.parseLong(finishDate)):null);
		return batch;
	}
    public void addBatch(Batch batch) {        
        SQLiteDatabase db = helper.getWritableDatabase();
        
        db.insert(BatchDBAdapter.TABLE_NAME, null, getContentValues(batch));
        db.close();
    }
    
    public boolean deleteBatch(int id) {
    	
    	boolean result = false;
    	String query = "Select * FROM " + BatchDBAdapter.TABLE_NAME + " WHERE " + BatchDBAdapter.COLUMN_BATCH_ID + " =  \"" + id + "\"";
    	SQLiteDatabase db = helper.getWritableDatabase();
    	Cursor cursor = db.rawQuery(query, null);
    	
    	if (cursor.moveToFirst()) {
    		db.delete(BatchDBAdapter.TABLE_NAME, BatchDBAdapter.COLUMN_BATCH_ID + " = ?",
    	            new String[] { String.valueOf(id) });
    		cursor.close();
    		result = true;
    	}
            db.close();
    	return result;
    }
    
    // "finders" and "getters" for Batch
    public Batch findBatchById(int batchid){
    	String query = "Select * FROM " + BatchDBAdapter.TABLE_NAME + " WHERE " + BatchDBAdapter.COLUMN_BATCH_ID + " =  \"" + batchid + "\"";
    	return findBatch(query);
    }

    private Batch findBatch(String query){

    	SQLiteDatabase db = helper.getWritableDatabase();
    	Cursor cursor = db.rawQuery(query, null);
    	Batch batch = null;
    	
    	if (cursor.moveToFirst()) {
    		batch = loadFromCursor(cursor);
    		cursor.close();
    	}
            db.close();
    	return batch;
    }
    
    public List<Batch> getAllBatches() {
    
    	List<Batch> batches = new LinkedList<Batch>();
        String query = "SELECT  * FROM " + BatchDBAdapter.TABLE_NAME;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        Batch batch = null;
        if (cursor.moveToFirst()) {
            do {
            	batch = loadFromCursor(cursor);
            	batches.add(batch);
    			batch = null;
            } while (cursor.moveToNext());
        }
        return batches;
    }
    
    public int getLastBatchId() {
    	
    	int lastId = 0;
    	List<Batch> allbatches = getAllBatches();
    	if (!allbatches.isEmpty()) {
    		Collections.sort(allbatches);
    		lastId = allbatches.get(allbatches.size()-1).getId();
    	}
		return lastId;
    }
    
    public int updateBatch(Batch batch) {
    	 
        SQLiteDatabase db = helper.getWritableDatabase();
 
        int i = db.update(BatchDBAdapter.TABLE_NAME, //table
	                getContentValues(batch), // column/value
	                BatchDBAdapter.COLUMN_BATCH_ID+" = ?", // selections
	                new String[] { String.valueOf(batch.getId()) }); //selection args
	    
	    db.close();
        return i;
    }
	
}

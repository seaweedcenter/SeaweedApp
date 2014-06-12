package com.savanticab.seaweedapp.sqlite;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.savanticab.seaweedapp.model.Batch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class BatchDBAdapter extends BaseDBAdapter<Batch>{
	// database table

	public static final String TABLE_NAME = "batches";
	//public static final String COLUMN_ID = "_id";
	public static final String COLUMN_BATCH_ID = "_id";
	public static final String COLUMN_RECIPE_ID = "recipe_id";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_STARTDATE = "startDate";
	public static final String COLUMN_FINISHDATE = "finishDate";
	public static final String COLUMN_EXTRACOMMENTS = "extraComments";
	
	// Database creation SQL statement
	protected static final String DATABASE_CREATE = "create table " 
	  + TABLE_NAME
	  + "("
	  + COLUMN_BATCH_ID + " integer primary key, "
	  + COLUMN_RECIPE_ID + " integer not null references "  + RecipeDBAdapter.TABLE_NAME + "("+RecipeDBAdapter.COLUMN_PRODUCT_ID+"), " 
	  + COLUMN_QUANTITY + " integer, "
	  + COLUMN_STARTDATE + " text, "
	  + COLUMN_FINISHDATE + " text,"
	  + COLUMN_EXTRACOMMENTS + " text"
	  + ");";
	
	@Override public String getTableName() { return TABLE_NAME;}
	@Override protected String getColumnIdName() { return COLUMN_BATCH_ID; }
	
	public BatchDBAdapter(Context context) {
		super(context);
	}

	@Override
	public ContentValues getContentValues(Batch batch) {
		ContentValues values = new ContentValues();
        values.put(COLUMN_BATCH_ID, batch.getId());
        values.put(COLUMN_RECIPE_ID, batch.getRecipe().getProduct().getId());
        values.put(COLUMN_QUANTITY, batch.getQuantity());
        String startDate = (batch.getStartDate() != null) ? Long.toString(batch.getStartDate().getTime()) : "null";
        String finishDate = (batch.getFinishDate() != null) ? Long.toString(batch.getFinishDate().getTime()) : "null";
        values.put(COLUMN_STARTDATE, startDate);
        values.put(COLUMN_FINISHDATE, finishDate);
        values.put(BatchDBAdapter.COLUMN_EXTRACOMMENTS, batch.getExtraComments());
		return values;
	}
	
	@Override
	public Batch loadFromCursor(Cursor cursor) {
		Batch batch = new Batch();
		batch.setId(cursor.getInt(0));
		
		// recipe must be loaded separately from other table
		int recipeId = cursor.getInt(1);
		batch.setRecipe(new RecipeDBAdapter(mContext).findRecipeById(recipeId));
		
		batch.setQuantity(cursor.getInt(2));
		String startDate = cursor.getString(3);
		batch.setStartDate(!startDate.equalsIgnoreCase("null") ? new Date(Long.parseLong(startDate)):null);
		String finishDate = cursor.getString(4);
		batch.setFinishDate(!finishDate.equalsIgnoreCase("null") ? new Date(Long.parseLong(finishDate)):null);
		batch.setExtraComments(cursor.getString(5));
		return batch;
	}
    
    public boolean deleteBatch(int id) {
    	
    	String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_BATCH_ID + " =  \"" + id + "\"";
    	return delete(query);
    }
    
    public Batch findBatchById(int batchid){
    	String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_BATCH_ID + " =  \"" + batchid + "\"";
    	return find(query);
    }
    
    public int getLastBatchId() {
    	
    	int lastId = 0;
    	List<Batch> allbatches = getAll();
    	if (!allbatches.isEmpty()) {
    		Collections.sort(allbatches);
    		lastId = allbatches.get(allbatches.size()-1).getId();
    	}
		return lastId;
    }
    
    public int updateBatch(Batch batch) {
        return update(batch, COLUMN_BATCH_ID + " = ?", new String[] { String.valueOf(batch.getId()) });
    }
	
}

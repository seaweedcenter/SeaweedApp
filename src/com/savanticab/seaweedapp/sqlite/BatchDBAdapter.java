package com.savanticab.seaweedapp.sqlite;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.savanticab.seaweedapp.model.Batch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class BatchDBAdapter extends BaseDBAdapter<Batch>{
	// database table

	public static final String TABLE_NAME = "batches";
	//public static final String COLUMN_ID = "_id";
	public static final String COLUMN_BATCH_NUMBER = "batch_number";
	public static final String COLUMN_RECIPE_ID = "recipe_id";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_STARTDATE = "startDate";
	public static final String COLUMN_FINISHDATE = "finishDate";
	public static final String COLUMN_EXTRACOMMENTS = "extraComments";
	
	// Database creation SQL statement
	protected static final String DATABASE_CREATE = "create table " 
	  + TABLE_NAME
	  + "("
	  + COLUMN_BATCH_NUMBER + " integer primary key, "
	  + COLUMN_RECIPE_ID + " integer not null references "  + RecipeDBAdapter.TABLE_NAME + "("+RecipeDBAdapter.COLUMN_PRODUCT_ID+"), " 
	  + COLUMN_QUANTITY + " integer, "
	  + COLUMN_STARTDATE + " text, "
	  + COLUMN_FINISHDATE + " text,"
	  + COLUMN_EXTRACOMMENTS + " text"
	  + ");";
	
	@Override public String getTableName() { return TABLE_NAME;}
	@Override protected String getColumnIdName() { return COLUMN_BATCH_NUMBER; }
	
	public BatchDBAdapter(Context context) {
		super(context);
	}

	@Override
	public ContentValues getContentValues(Batch batch) {
		ContentValues values = new ContentValues();
        values.put(COLUMN_BATCH_NUMBER, batch.getId());
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
	public DbxFields getFields(Batch item){
		DbxFields fields = new DbxFields();
		fields.set(COLUMN_BATCH_NUMBER, item.getBatchNumber());
		fields.set(COLUMN_RECIPE_ID, item.getRecipe().getId());
		fields.set(COLUMN_QUANTITY, item.getQuantity());
		Date startDate = item.getStartDate();
		if (null != startDate) { 
			fields.set(COLUMN_STARTDATE, startDate);
		} else{
			fields.deleteField(COLUMN_STARTDATE);
		}
			
		Date finishDate = item.getFinishDate();
		if (null != finishDate) {
			fields.set(COLUMN_FINISHDATE, finishDate);
		} else {
			fields.deleteField(COLUMN_FINISHDATE);
		}
		String extraComments = item.getExtraComments();
		if (null != extraComments && !extraComments.isEmpty()) {
			fields.set(COLUMN_EXTRACOMMENTS, extraComments);
		} else {
			fields.deleteField(COLUMN_EXTRACOMMENTS);
		}
		return fields;
	}
	
	@Override
	public Batch loadFromCursor(Cursor cursor) {
		Batch batch = new Batch();
		//batch.setId(cursor.getInt(0));
		
		// recipe must be loaded separately from other table
		int recipeId = cursor.getInt(1);
		//batch.setRecipe(new RecipeDBAdapter(mContext).findRecipeById(recipeId));
		
		batch.setQuantity(cursor.getInt(2));
		String startDate = cursor.getString(3);
		batch.setStartDate(!startDate.equalsIgnoreCase("null") ? new Date(Long.parseLong(startDate)):null);
		String finishDate = cursor.getString(4);
		batch.setFinishDate(!finishDate.equalsIgnoreCase("null") ? new Date(Long.parseLong(finishDate)):null);
		batch.setExtraComments(cursor.getString(5));
		return batch;
	}
	
	@Override
	public Batch loadFromRecord(DbxRecord record) {
		Batch batch = new Batch();
		batch.setBatchNumber((int) record.getLong(COLUMN_BATCH_NUMBER));
		
		// recipe must be loaded separately from other table
		String recipeId = record.getString(COLUMN_RECIPE_ID);
		batch.setRecipe(new RecipeDBAdapter(mContext).findItemById(recipeId));
		
		batch.setQuantity((int)record.getLong(COLUMN_QUANTITY));
		batch.setStartDate(record.getDate(COLUMN_STARTDATE));
		if (record.hasField(COLUMN_FINISHDATE)) batch.setFinishDate(record.getDate(COLUMN_FINISHDATE));
		if (record.hasField(COLUMN_EXTRACOMMENTS)) 
			batch.setExtraComments(record.getString(COLUMN_EXTRACOMMENTS));
		return batch;
	}
    
    public boolean deleteBatch(int batchnumber) {
    	
    	//String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_BATCH_ID + " =  \"" + id + "\"";
    	//return delete(query);
    	return delete(new DbxFields().set(COLUMN_BATCH_NUMBER, batchnumber));
    }
    
    public Batch findBatchByBatchNumber(int batchnumber){
    	//String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_BATCH_ID + " =  \"" + batchnumber + "\"";
    	//return find(query);
    	return find(new DbxFields().set(COLUMN_BATCH_NUMBER, batchnumber));
    }
    
    public int getLastBatchId() {
    	
    	int lastId = 0;
    	List<Batch> allbatches = getAll();
    	if (!allbatches.isEmpty()) {
    		Collections.sort(allbatches);
    		lastId = allbatches.get(allbatches.size()-1).getBatchNumber();
    	}
		return lastId;
    }
    
    public int updateBatch(Batch batch) {
        //return update(batch, COLUMN_BATCH_ID + " = ?", new String[] { String.valueOf(batch.getId()) });
        return update(batch, new DbxFields().set(COLUMN_BATCH_NUMBER, batch.getBatchNumber()));
    }
	
}

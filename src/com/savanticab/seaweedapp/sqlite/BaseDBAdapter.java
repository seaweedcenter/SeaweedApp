package com.savanticab.seaweedapp.sqlite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDBAdapter<T> {

	protected MySQLiteHelper helper;
	protected Context mContext;
	protected DbxAccount mAccount;

	public abstract String getTableName();

	protected abstract String getColumnIdName(); // first column of the database
													// table

	public BaseDBAdapter(Context context) {
		//helper = MySQLiteHelper.getInstance(context);
		mContext = context;
	}

	private static final String appKey = "r4za7fsvw23g6s3";
	private static final String appSecret = "ze5bnkmzy6famn7";
	
	public static DbxAccountManager getAccountManager(Context context){
		return DbxAccountManager.getInstance(context.getApplicationContext(), appKey, appSecret);
	}
	
	protected DbxAccount getDefaultAccount() {
		if (null == mAccount) {
			mAccount = DbxAccountManager.getInstance(
					mContext.getApplicationContext(), appKey, appSecret)
					.getLinkedAccount();
		}
		return mAccount;
	}

	public abstract ContentValues getContentValues(T item);
	public abstract DbxFields getFields(T item);

	public abstract T loadFromCursor(Cursor cursor);

	public abstract T loadFromRecord(DbxRecord record);

	public void add(T item) {
		
		DbxDatastore store = null;
		try {
			store = DbxDatastore.openDefault(getDefaultAccount());
			DbxTable table = store.getTable(getTableName());
			
			DbxRecord record = table.getOrInsert(String.valueOf(item.hashCode()), getFields(item));
			
			store.sync();
			store.close();
			
			
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != store && store.isOpen())
				store.close();
		}
		
		
		/*SQLiteDatabase db = helper.getWritableDatabase();

		db.insert(getTableName(), null, getContentValues(item));
		db.close();*/
	}

	public List<T> getAll() {

		List<T> items = new ArrayList<T>();
		T item = null;
		DbxDatastore store = null;
		try {
			store = DbxDatastore.openDefault(getDefaultAccount());
			DbxTable table = store.getTable(getTableName());
			DbxTable.QueryResult results = table.query();

			store.close();

			Iterator<DbxRecord> iterator = results.iterator();
			while (iterator.hasNext()) {
				item = loadFromRecord(iterator.next());
				items.add(item);
			}
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != store && store.isOpen())
				store.close();
		}

		/*
		 * String query = "SELECT  * FROM " + getTableName(); SQLiteDatabase db
		 * = helper.getWritableDatabase(); Cursor cursor = db.rawQuery(query,
		 * null);
		 * 
		 * List<T> items = new ArrayList<T>(); T item = null;
		 * 
		 * if (cursor.moveToFirst()) { do { item = loadFromCursor(cursor);
		 * 
		 * items.add(item); } while (cursor.moveToNext()); } db.close();
		 */

		return items;
	}
/*
	protected T find(String query) {

		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		T item = null;

		if (cursor.moveToFirst()) {
			item = loadFromCursor(cursor);
			cursor.close();
		}
		db.close();
		return item;
	}*/
	
	protected T find(DbxFields queryParams){
		T item = null;
		DbxDatastore store = null;
		try {
			store = DbxDatastore.openDefault(getDefaultAccount());
			DbxTable table = store.getTable(getTableName());
			DbxTable.QueryResult results = table.query(queryParams);
			DbxRecord record = results.iterator().next();
			store.close();
			item = loadFromRecord(record);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != store && store.isOpen())
				store.close();
		}
		return item;
	}
	
	protected T findItemById(String id){
		T item = null;
		DbxDatastore store = null;
		try {
			store = DbxDatastore.openDefault(getDefaultAccount());
			DbxTable table = store.getTable(getTableName());
			DbxRecord record = table.get(id);
			store.close();
			item = loadFromRecord(record);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != store && store.isOpen())
				store.close();
		}
		return item;
	}

/*	protected int update(T item, String selections, String[] selectionArgs) {

		// get reference to writable DB
		SQLiteDatabase db = helper.getWritableDatabase();

		// updating row
		int i = db.update(getTableName(), // table
				getContentValues(item), // column/value
				selections, // selections
				selectionArgs); // selection args
		// close
		db.close();
		return i;
	}*/
	
	protected int update(T item, DbxFields queryParams){
		DbxDatastore store = null;
		try {
			store = DbxDatastore.openDefault(getDefaultAccount());
			DbxTable table = store.getTable(getTableName());
			DbxTable.QueryResult results = table.query(queryParams);
			DbxRecord record = results.iterator().next();
			record.setAll(getFields(item));
			
			store.sync();
			store.close();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != store && store.isOpen())
				store.close();
		}
		return 1; //TODO: return item id instead?
	}
	
	protected int update(T item, String itemId){
		DbxDatastore store = null;
		try {
			store = DbxDatastore.openDefault(getDefaultAccount());
			DbxTable table = store.getTable(getTableName());
			DbxRecord record = table.get(itemId);
			record.setAll(getFields(item));
			
			store.sync();
			store.close();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != store && store.isOpen())
				store.close();
		}
		return 1; //TODO: return item id instead?
	}

/*	protected boolean delete(String query) {

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
	}*/
	
	protected boolean delete(DbxFields queryParams){
		DbxDatastore store = null;
		try {
			store = DbxDatastore.openDefault(getDefaultAccount());
			DbxTable table = store.getTable(getTableName());
			DbxTable.QueryResult results = table.query(queryParams);
			DbxRecord record = results.iterator().next();
			record.deleteRecord();
			
			store.sync();
			store.close();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != store && store.isOpen())
				store.close();
			return false;
		}
		return true;
	}
}

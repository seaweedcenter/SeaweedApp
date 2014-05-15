package com.savanticab.seaweedapp.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.savanticab.seaweedapp.model.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class ProductDBAdapter  extends BaseDBAdapter{
	
	// Database table
	  public static final String TABLE_NAME = "products";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_CODE = "code";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_FRAGANCE = "fragance";
	  public static final String COLUMN_SIZE = "size";
	  public static final String COLUMN_PRICE = "price";
	  
	  // Database creation SQL statement
	  //TODO: make name+fragance+size combination unique?
	  protected static final String DATABASE_CREATE = "create table " 
	      + TABLE_NAME
	      + "(" 
	      //+ COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_ID + " integer primary key, " 
	      + COLUMN_CODE + " text not null unique, "
	      + COLUMN_NAME + " text not null, " 
	      + COLUMN_FRAGANCE + " text, " 
	      + COLUMN_SIZE + " text, " 
	      + COLUMN_PRICE + " real"
	      + ");";
	  
		public ProductDBAdapter(Context context) {
			super(context);
		}
		
		public ContentValues getContentValues(Product product) {
			ContentValues values = new ContentValues();
			values.put(ProductDBAdapter.COLUMN_ID, product.getId());
			values.put(ProductDBAdapter.COLUMN_CODE, product.getCode());
			values.put(ProductDBAdapter.COLUMN_NAME, product.getName());
			values.put(ProductDBAdapter.COLUMN_FRAGANCE, product.getFragance());
			values.put(ProductDBAdapter.COLUMN_SIZE, product.getSize());
			values.put(ProductDBAdapter.COLUMN_PRICE, product.getPrice());
			return values;
		}
		
		public Product loadFromCursor(Cursor cursor) {
			Product product = new Product();
			product.setId(Integer.parseInt(cursor.getString(0)));
			product.setCode(cursor.getString(1));
			product.setName(cursor.getString(2));
			product.setFragance(cursor.getString(3));
			product.setSize(cursor.getString(4));
			product.setPrice(Double.parseDouble(cursor.getString(5)));
			return product;
		}
		
	    public void addProduct(Product product) {   
	        SQLiteDatabase db = helper.getWritableDatabase();
	        db.insert(ProductDBAdapter.TABLE_NAME, null, getContentValues(product));
	        db.close();
	    }
	    
	    public Product findProductById(int productId){
	    	String query = "Select * FROM " + ProductDBAdapter.TABLE_NAME + " WHERE " + ProductDBAdapter.COLUMN_ID + " =  \"" + productId + "\"";
	    	return findProduct(query);
	    }
	    
	    public Product findProductByCode(String productcode){
	    	String query = "Select * FROM " + ProductDBAdapter.TABLE_NAME + " WHERE " + ProductDBAdapter.COLUMN_CODE + " =  \"" + productcode + "\"";
	    	return findProduct(query);
	    }
	    
	    private Product findProduct(String query) {
	    	    	
	    	SQLiteDatabase db = helper.getWritableDatabase();
	    	Cursor cursor = db.rawQuery(query, null);
	    	
	    	Product product = null;
	    	
	    	if (cursor.moveToFirst()) {
	    		product = loadFromCursor(cursor);
	    		cursor.close();
	    	}
	    	
	        db.close();
	    	return product;
	    }
	    
	    public List<Product> getAllProducts() {
	    	
	        String query = "SELECT  * FROM " + ProductDBAdapter.TABLE_NAME;
	        SQLiteDatabase db = helper.getWritableDatabase();
	        Cursor cursor = db.rawQuery(query, null);
	        
	        List<Product> products = new ArrayList<Product>();
	        Product product = null;
	        
	        if (cursor.moveToFirst()) {
	            do {
	            	product = loadFromCursor(cursor);
	        		
	        		products.add(product);
	            } while (cursor.moveToNext());
	        }
	        db.close();
	        return products;
	    }
	    public int updateProduct(Product product) {
	 
	        //get reference to writable DB
	        SQLiteDatabase db = helper.getWritableDatabase();
	        
	        //updating row
	        int i = db.update(ProductDBAdapter.TABLE_NAME, //table
	                getContentValues(product), // column/value
	                ProductDBAdapter.COLUMN_ID+" = ?", // selections
	                new String[] { String.valueOf(product.getId()) }); //selection args
	        //close
	        db.close();
	        return i;
	 
	    }
	    public boolean deleteProduct(String productcode) {
	    	
	    	boolean result = false;
	    	String query = "Select * FROM " + ProductDBAdapter.TABLE_NAME + " WHERE " + ProductDBAdapter.COLUMN_CODE + " =  \"" + productcode + "\"";
	    	SQLiteDatabase db = helper.getWritableDatabase();
	    	Cursor cursor = db.rawQuery(query, null);
	    	
	    	if (cursor.moveToFirst()) {
	    		db.delete(ProductDBAdapter.TABLE_NAME, ProductDBAdapter.COLUMN_ID + " = ?",
	    	            new String[] { cursor.getString(0) });
	    		cursor.close();
	    		result = true;
	    	}
	        db.close();
	    	return result;
	    }
	  
}

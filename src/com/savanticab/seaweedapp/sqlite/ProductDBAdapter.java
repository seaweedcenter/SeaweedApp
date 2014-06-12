package com.savanticab.seaweedapp.sqlite;

import com.savanticab.seaweedapp.model.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class ProductDBAdapter  extends BaseDBAdapter<Product>{
	
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

		@Override public String getTableName() { return TABLE_NAME; }
		@Override protected String getColumnIdName() { return COLUMN_ID; }
	  
		public ProductDBAdapter(Context context) {
			super(context);
		}
		
		@Override
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
		
		@Override
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
	    
	    public Product findProductById(int productId){
	    	String query = "Select * FROM " + ProductDBAdapter.TABLE_NAME + " WHERE " + ProductDBAdapter.COLUMN_ID + " =  \"" + productId + "\"";
	    	return find(query);
	    }
	    
	    public Product findProductByCode(String productcode){
	    	String query = "Select * FROM " + ProductDBAdapter.TABLE_NAME + " WHERE " + ProductDBAdapter.COLUMN_CODE + " =  \"" + productcode + "\"";
	    	return find(query);
	    }
	    
	    public int updateProduct(Product product) {
	 
	        return update(product, COLUMN_ID + " = ?", new String[] { String.valueOf(product.getId()) });
	 
	    }
	    public boolean deleteProduct(String productcode) {
	    	
	    	String query = "Select * FROM " + ProductDBAdapter.TABLE_NAME + " WHERE " + ProductDBAdapter.COLUMN_CODE + " =  \"" + productcode + "\"";
	    	return delete(query);
	    }
	  
}

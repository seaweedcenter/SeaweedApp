package com.savanticab.seaweedapp.sqlite;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.ProductInventory;
import com.savanticab.seaweedapp.model.RawMaterial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProductInventoryDBAdaptor extends BaseDBAdapter{
	
	// Database table
		  public static final String TABLE_NAME = "product_inventories";
		  public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_PRODUCT_ID = "product_id";
		  public static final String COLUMN_INSTOCKQTY = "inStockQty";
		  public static final String COLUMN_INPRODUCTIONQTY = "inProductionQty";
		  
		  // Database creation SQL statement
		  protected static final String DATABASE_CREATE = "create table " 
		      + TABLE_NAME
		      + "(" 
		      //+ COLUMN_ID + " integer primary key autoincrement, " 
		      //+ COLUMN_ID + " integer primary key, " 
		      + COLUMN_PRODUCT_ID + " integer primary key references " + ProductDBAdapter.TABLE_NAME + "("+ ProductDBAdapter.COLUMN_ID + "), " 
		      + COLUMN_INSTOCKQTY + " real, "
		      + COLUMN_INPRODUCTIONQTY + " real"
		      + ");";

		  public ProductInventoryDBAdaptor(Context context) {
				super(context);
			}
		  
		  public ContentValues getContentValues(ProductInventory productInventory) {
				ContentValues values = new ContentValues();
				values.put(ProductInventoryDBAdaptor.COLUMN_PRODUCT_ID, productInventory.getProduct().getId());
				values.put(ProductInventoryDBAdaptor.COLUMN_INSTOCKQTY, productInventory.getStock());
				values.put(ProductInventoryDBAdaptor.COLUMN_INPRODUCTIONQTY, productInventory.getInproduction());
				return values;
			}
			
			public ProductInventory loadFromCursor(Cursor cursor) {
				ProductInventory productInventory = new ProductInventory();
				productInventory.setProduct(new ProductDBAdapter(mContext).findProductById(Integer.parseInt(cursor.getString(0))));
				productInventory.setStock(Integer.parseInt(cursor.getString(1)));
				productInventory.setInproduction(Integer.parseInt(cursor.getString(2)));
				return productInventory;
			}
			
		    public void addProductInventory(ProductInventory productInventory) {   
		        SQLiteDatabase db = helper.getWritableDatabase();
		        db.insert(TABLE_NAME, null, getContentValues(productInventory));
		        db.close();
		    }
		    
		    public ProductInventory findProductInventoryById(int productId){
		    	String query = "Select * FROM " + ProductInventoryDBAdaptor.TABLE_NAME + " WHERE " + ProductInventoryDBAdaptor.COLUMN_PRODUCT_ID + " =  \"" + productId + "\"";
		    	SQLiteDatabase db = helper.getWritableDatabase();
		    	Cursor cursor = db.rawQuery(query, null);
		    	
		    	ProductInventory productInventory = null;
		    	
		    	if (cursor.moveToFirst()) {
		    		productInventory = loadFromCursor(cursor);
		    		cursor.close();
		    	}
		    	
		        db.close();
		    	return productInventory;
		    }
		public List<ProductInventory> getAllProductInventories() {
		    	
		        String query = "SELECT  * FROM " + TABLE_NAME;
		        SQLiteDatabase db = helper.getWritableDatabase();
		        Cursor cursor = db.rawQuery(query, null);
		        
		        List<ProductInventory> products = new ArrayList<ProductInventory>();
		        ProductInventory product = null;
		        
		        if (cursor.moveToFirst()) {
		            do {
		            	product = loadFromCursor(cursor);
		        		
		        		products.add(product);
		            } while (cursor.moveToNext());
		        }
		        db.close();
		        return products;
		    }
		    public int updateProductInventory(ProductInventory product) {
		 
		        //get reference to writable DB
		        SQLiteDatabase db = helper.getWritableDatabase();
		        
		        //updating row
		        int i = db.update(TABLE_NAME, //table
		                getContentValues(product), // column/value
		                COLUMN_ID+" = ?", // selections
		                new String[] { String.valueOf(product.getProduct().getId()) }); //selection args
		        //close
		        db.close();
		        return i;
		 
		    }
		    public boolean deleteProductInventory(int productId) {
		    	
		    	boolean result = false;
		    	String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCT_ID + " =  \"" + productId + "\"";
		    	SQLiteDatabase db = helper.getWritableDatabase();
		    	Cursor cursor = db.rawQuery(query, null);
		    	
		    	if (cursor.moveToFirst()) {
		    		db.delete(TABLE_NAME, COLUMN_ID + " = ?",
		    	            new String[] { cursor.getString(0) });
		    		cursor.close();
		    		result = true;
		    	}
		            db.close();
		    	return result;
		    }
		    
		    //TODO: move somewhere else?
		    public void ProductProductionFinish(Product product, Batch batch) {
				int productQty = batch.getQuantity();
				
				ProductInventory inv = this.findProductInventoryById(product.getId());
				inv.setStock(inv.getStock() + productQty);
				inv.setInproduction(inv.getInproduction() - productQty);
				this.updateProductInventory(inv);
				
				// unreserve materials that were allocated for production
				LinkedHashMap<RawMaterial, Double> ingredients = batch.getRecipe().getIngredients();
				MaterialInventoryDBAdapter mIAdapter = new MaterialInventoryDBAdapter(mContext);
				for (Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
					RawMaterial mtrl =  entry.getKey();
					mIAdapter.MtrlReservedFinished(mtrl, entry.getValue()*productQty);
				}
			}

}

package com.savanticab.seaweedapp.sqlite;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.ProductInventory;
import com.savanticab.seaweedapp.model.RawMaterial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ProductInventoryDBAdaptor extends BaseDBAdapter<ProductInventory>{
	
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
		      + COLUMN_INSTOCKQTY + " integer, "
		      + COLUMN_INPRODUCTIONQTY + " integer"
		      + ");";

			@Override public String getTableName() { return TABLE_NAME; }
			@Override protected String getColumnIdName() { return COLUMN_ID; }
			
		  public ProductInventoryDBAdaptor(Context context) {
				super(context);
			}
		  
		  @Override
		public ContentValues getContentValues(ProductInventory productInventory) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_PRODUCT_ID, productInventory.getProduct().getId());
				values.put(COLUMN_INSTOCKQTY, productInventory.getStock());
				values.put(COLUMN_INPRODUCTIONQTY, productInventory.getInproduction());
				return values;
			}
			
			@Override
			public ProductInventory loadFromCursor(Cursor cursor) {
				ProductInventory productInventory = new ProductInventory();
				//productInventory.setProduct(new ProductDBAdapter(mContext).findProductById(cursor.getInt(0)));
				productInventory.setStock(cursor.getInt(1));
				productInventory.setInproduction(cursor.getInt(2));
				return productInventory;
			}
			

			@Override
			public DbxFields getFields(ProductInventory item){
				DbxFields fields = new DbxFields();
				fields.set(COLUMN_PRODUCT_ID, item.getProduct().getId());
				fields.set(COLUMN_INSTOCKQTY, item.getStock());
				fields.set(COLUMN_INPRODUCTIONQTY, item.getInproduction());
				return fields;
			}
			
			@Override
			public ProductInventory loadFromRecord(DbxRecord record) {
				ProductInventory item = new ProductInventory();
				item.setProduct(new ProductDBAdapter(mContext).findProductById(record.getString(COLUMN_PRODUCT_ID)));
				item.setStock((int) record.getLong(COLUMN_INSTOCKQTY));
				item.setInproduction((int) record.getLong(COLUMN_INPRODUCTIONQTY));
				return item;
			}
		    
		    public ProductInventory findProductInventoryById(String productId){
		    	//String query = "Select * FROM " + ProductInventoryDBAdaptor.TABLE_NAME + " WHERE " + ProductInventoryDBAdaptor.COLUMN_PRODUCT_ID + " =  \"" + productId + "\"";
		    	//return find(query);
		    	return findItemById(productId);
		    }
		    
		    public int updateProductInventory(ProductInventory product) {
		 
		        //return update(product, COLUMN_PRODUCT_ID + " = ?", new String[] { String.valueOf(product.getProduct().getId()) });
		        return update(product, product.getId());
		 
		    }
		    public boolean deleteProductInventory(String productId) {
		    	
		    	//String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCT_ID + " =  \"" + productId + "\"";
		    	//return delete(query);
		    	return delete(new DbxFields().set(COLUMN_PRODUCT_ID, productId));
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

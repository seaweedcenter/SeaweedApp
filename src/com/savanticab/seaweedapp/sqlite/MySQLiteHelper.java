package com.savanticab.seaweedapp.sqlite;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SeaweedDB";
 
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        ProductTable.onCreate(db);
        RawMaterialTable.onCreate(db);
        RecipeTable.onCreate(db);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ProductTable.onUpgrade(db, oldVersion, newVersion);
        RawMaterialTable.onUpgrade(db, oldVersion, newVersion);
        RecipeTable.onUpgrade(db, oldVersion, newVersion);
 
    }
    
    
    //Products
    public void addProduct(Product product) {

        ContentValues values = new ContentValues();
        values.put(ProductTable.COLUMN_CODE, product.getCode());
        values.put(ProductTable.COLUMN_NAME, product.getName());
        values.put(ProductTable.COLUMN_FRAGANCE, product.getFragance());
        values.put(ProductTable.COLUMN_SIZE, product.getSize());
 
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.insert(ProductTable.TABLE_NAME, null, values);
        db.close();
}
    public Product findProduct(String productcode) {
    	String query = "Select * FROM " + ProductTable.TABLE_NAME + " WHERE " + ProductTable.COLUMN_CODE + " =  \"" + productcode + "\"";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	Product product = new Product();
    	
    	if (cursor.moveToFirst()) {
    		product.setId(Integer.parseInt(cursor.getString(0)));
    		product.setCode(cursor.getString(1));
    		product.setName(cursor.getString(2));
    		product.setFragance(cursor.getString(3));
    		product.setSize(cursor.getString(4));
    		cursor.close();
    	} else {
    		product = null;
    	}
            db.close();
    	return product;
    }
    public List<Product> getAllProducts() {
        List<Product> products = new LinkedList<Product>();
 
        String query = "SELECT  * FROM " + ProductTable.TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        Product product = null;
        if (cursor.moveToFirst()) {
            do {
            	product = new Product();
            	product.setId(Integer.parseInt(cursor.getString(0)));
        		product.setCode(cursor.getString(1));
        		product.setName(cursor.getString(2));
        		product.setFragance(cursor.getString(3));
        		product.setSize(cursor.getString(4));
 
            	products.add(product);
            } while (cursor.moveToNext());
        }
        return products;
    }
    public int updateProduct(Product product) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ProductTable.COLUMN_CODE, product.getCode());
        values.put(ProductTable.COLUMN_NAME, product.getName());
        values.put(ProductTable.COLUMN_FRAGANCE, product.getFragance());
        values.put(ProductTable.COLUMN_SIZE, product.getSize());
 
        // 3. updating row
        int i = db.update(ProductTable.TABLE_NAME, //table
                values, // column/value
                ProductTable.COLUMN_ID+" = ?", // selections
                new String[] { String.valueOf(product.getId()) }); //selection args
 
        // 4. close
        db.close();
 
        return i;
 
    }
    public boolean deleteProduct(String productcode) {
    	
    	boolean result = false;
    	
    	String query = "Select * FROM " + ProductTable.TABLE_NAME + " WHERE " + ProductTable.COLUMN_CODE + " =  \"" + productcode + "\"";

    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	Product product = new Product();
    	
    	if (cursor.moveToFirst()) {
    		product.setId(Integer.parseInt(cursor.getString(0)));
    		db.delete(ProductTable.TABLE_NAME, ProductTable.COLUMN_ID + " = ?",
    	            new String[] { String.valueOf(product.getId()) });
    		cursor.close();
    		result = true;
    	}
            db.close();
    	return result;
    }

    //RawMaterials
    public void addRawMaterial(RawMaterial material){
    	ContentValues values = new ContentValues();
        values.put(RawMaterialTable.COLUMN_NAME, material.getName());
        values.put(RawMaterialTable.COLUMN_UNIT, material.getUnit());
        values.put(RawMaterialTable.COLUMN_STOCK_QUANTITY, material.getStockQuantity());
        values.put(RawMaterialTable.COLUMN_ORDERED_QUANTITY, material.getOrderedQuantity());
        values.put(RawMaterialTable.COLUMN_ICON, material.getIcon());
 
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.insert(RawMaterialTable.TABLE_NAME, null, values);
        db.close();
    }
    public RawMaterial findRawMaterial(String materialname){
    	String query = "Select * FROM " + RawMaterialTable.TABLE_NAME + " WHERE " + RawMaterialTable.COLUMN_NAME + " =  \"" + materialname + "\"";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	RawMaterial material = new RawMaterial();
    	
    	if (cursor.moveToFirst()) {
    		material.setId(Integer.parseInt(cursor.getString(0)));
    		material.setName(cursor.getString(1));
    		material.setUnit(cursor.getString(2));
    		material.setStockQuantity(Double.parseDouble(cursor.getString(3)));
    		material.setOrderedQuantity(Double.parseDouble(cursor.getString(4)));
    		material.setIcon(cursor.getString(5));
    		cursor.close();
    	} else {
    		material = null;
    	}
            db.close();
    	return material;
    }
    public boolean deleteRawMaterial(String materialname){
    	boolean result = false;
    	
    	String query = "Select * FROM " + RawMaterialTable.TABLE_NAME + " WHERE " + RawMaterialTable.COLUMN_NAME + " =  \"" + materialname + "\"";

    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	RawMaterial material = new RawMaterial();
    	
    	if (cursor.moveToFirst()) {
    		material.setId(Integer.parseInt(cursor.getString(0)));
    		db.delete(RawMaterialTable.TABLE_NAME, RawMaterialTable.COLUMN_ID + " = ?",
    	            new String[] { String.valueOf(material.getId()) });
    		cursor.close();
    		result = true;
    	}
        db.close();
    	return result;
    }
    
    //Recipes
    public void addRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values;
    	int productId = recipe.getProduct().getId();
    	for(Entry<RawMaterial, Double> entry : recipe.getIngredients().entrySet()) {
    		RawMaterial ingredient = entry.getKey();
    	    Double quantity = entry.getValue();
    	    
        	values = new ContentValues();
    	    values.put(RecipeTable.COLUMN_PRODUCT_ID, productId);
    	    values.put(RecipeTable.COLUMN_RAW_MATERIAL_ID, ingredient.getId());
    	    values.put(RecipeTable.COLUMN_QUANTITY, quantity);
    	    
    	    db.insert(RecipeTable.TABLE_NAME, null, values);    	    
    	}
        db.close();
    }
    public Recipe findRecipe(int productid){
    	String query = "Select * FROM " + RecipeTable.TABLE_NAME + " WHERE " + RecipeTable.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	Recipe recipe = new Recipe();
    	Map<RawMaterial, Double> ingredients = new HashMap<RawMaterial, Double>();
    	if (cursor.moveToFirst()) {
    		Integer productId = Integer.parseInt(cursor.getString(1));
    		recipe.setProduct(findProduct(productId.toString())); //TODO: cursor returns Id and findProduct needs productCode
    		do {
    			Integer raw_material_id = Integer.parseInt(cursor.getString(2));
    			RawMaterial material = findRawMaterial(raw_material_id.toString()); //TODO: cursor returns Id and findRawMaterial needs materialName
    			double quantity = Double.parseDouble(cursor.getString(3));
    			ingredients.put(material, quantity);
            } while (cursor.moveToNext());
    		
    		recipe.setIngredients(ingredients);
    		cursor.close();
    	} else {
    		recipe = null;
    	}
            db.close();
    	return recipe;
    }
    public boolean deleteRecipe(int productid){
    	boolean result = false;
    	
    	String query = "Select * FROM " + RecipeTable.TABLE_NAME + " WHERE " + RecipeTable.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";

    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	if (cursor.moveToFirst()) {
    		Integer product_id = Integer.parseInt(cursor.getString(1));
    		db.delete(RecipeTable.TABLE_NAME, RecipeTable.COLUMN_PRODUCT_ID + " = ?",
    	            new String[] { String.valueOf(product_id) });
    		cursor.close();
    		result = true;
    	}
        db.close();
    	return result;
    }

}

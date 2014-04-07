package com.savanticab.seaweedapp.sqlite;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
    private static final String DATABASE_NAME = "SeaweedDB.db";
 
    private static MySQLiteHelper sInstance;
    public static MySQLiteHelper getInstance(Context context) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
          sInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
      }
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
        addProduct(new Product(1, "SOAP1", "Soap", "Lime", "Big"));
        addProduct(new Product(2, "SOAP2", "Soap", "Clove", "Small"));
        addProduct(new Product(3, "SOAP3", "Soap", "Langi-langi", "Big"));
        addProduct(new Product(4, "SOAP4", "Soap", "Lemongrass", "Medium"));
        
        addRawMaterial(new RawMaterial(1, "Coconut oil", "L", 100, 0));
        addRawMaterial(new RawMaterial(2, "Seaweed", "Kg", 50, 0));
        addRawMaterial(new RawMaterial(3, "Bee wax", "Kg", 5, 2));
        
        Map m = new HashMap<RawMaterial, Double>();
        m.put(new RawMaterial(1, "Coconut oil", "L", 100, 0), 1.0);
        m.put(new RawMaterial(2, "Seaweed", "Kg", 50, 0), 0.5);
        Recipe r = new Recipe(new Product(1, "SOAP1", "Soap", "Lime", "Big"), m);
        addRecipe(r);
        
        m = new HashMap<RawMaterial, Double>();
        m.put(new RawMaterial(1, "Coconut oil", "L", 100, 0), 2.0);
        m.put(new RawMaterial(3, "Bee wax", "Kg", 50, 0), 0.25);
        r = new Recipe(new Product(2, "SOAP2", "Soap", "Lime", "Big"), m);
        addRecipe(r);
        
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
    public Product findProductById(int productId){
    	String query = "Select * FROM " + ProductTable.TABLE_NAME + " WHERE " + ProductTable.COLUMN_ID + " =  \"" + productId + "\"";
    	return findProduct(query);
    }
    public Product findProductByCode(String productcode){
    	String query = "Select * FROM " + ProductTable.TABLE_NAME + " WHERE " + ProductTable.COLUMN_CODE + " =  \"" + productcode + "\"";
    	return findProduct(query);
    }
    private Product findProduct(String query) {
    	
    	
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
        db.close();
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
    public RawMaterial findRawMaterialById(int materialId){
    	String query = "Select * FROM " + RawMaterialTable.TABLE_NAME + " WHERE " + RawMaterialTable.COLUMN_ID + " =  \"" + materialId + "\"";
    	return findRawMaterial(query);
    }
    public RawMaterial findRawMaterialByName(String materialname){
    	String query = "Select * FROM " + RawMaterialTable.TABLE_NAME + " WHERE " + RawMaterialTable.COLUMN_NAME + " =  \"" + materialname + "\"";
    	return findRawMaterial(query);
    }
    private RawMaterial findRawMaterial(String query){    	
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
    public List<RawMaterial> getAllRawMaterials() {
        List<RawMaterial> materials = new LinkedList<RawMaterial>();
 
        String query = "SELECT  * FROM " + RawMaterialTable.TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        RawMaterial material = null;
        if (cursor.moveToFirst()) {
            do {
            	material = new RawMaterial();
            	material.setId(Integer.parseInt(cursor.getString(0)));
        		material.setName(cursor.getString(1));
        		material.setUnit(cursor.getString(2));
        		material.setStockQuantity(Double.parseDouble(cursor.getString(3)));
        		material.setOrderedQuantity(Double.parseDouble(cursor.getString(4)));
        		material.setIcon(cursor.getString(5));
 
        		materials.add(material);
            } while (cursor.moveToNext());
        }
        return materials;
    }
    public int updateRawMaterial(RawMaterial material) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(RawMaterialTable.COLUMN_NAME, material.getName());
        values.put(RawMaterialTable.COLUMN_UNIT, material.getUnit());
        values.put(RawMaterialTable.COLUMN_STOCK_QUANTITY, material.getStockQuantity());
        values.put(RawMaterialTable.COLUMN_ORDERED_QUANTITY, material.getOrderedQuantity());
        values.put(RawMaterialTable.COLUMN_ICON, material.getIcon());
 
        // 3. updating row
        int i = db.update(RawMaterialTable.TABLE_NAME, //table
                values, // column/value
                RawMaterialTable.COLUMN_ID+" = ?", // selections
                new String[] { String.valueOf(material.getId()) }); //selection args
 
        // 4. close
        db.close();
 
        return i;
 
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
    	Set<Entry<RawMaterial, Double>> s = recipe.getIngredients().entrySet();
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
    public Recipe findRecipeByProductId(int productid){
    	String query = "Select * FROM " + RecipeTable.TABLE_NAME + " WHERE " + RecipeTable.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	Recipe recipe = new Recipe();
    	Map<RawMaterial, Double> ingredients = new HashMap<RawMaterial, Double>();
    	if (cursor.moveToFirst()) {
    		Integer productId = Integer.parseInt(cursor.getString(0));
    		recipe.setProduct(findProductById(productId));
    		do {
    			Integer raw_material_id = Integer.parseInt(cursor.getString(1));
    			RawMaterial material = findRawMaterialById(raw_material_id);
    			double quantity = Double.parseDouble(cursor.getString(2));
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
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new LinkedList<Recipe>();
        
        String query = "SELECT  * FROM " + RecipeTable.TABLE_NAME;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            do {
            	Integer productId = Integer.parseInt(cursor.getString(0));
            	
            	for (Recipe r : recipes){
            		if (r.getProduct().getId() == productId) {
            			recipe = r;
            		}
            	}
            	if (recipe == null){
            		recipe = new Recipe();
            		recipe.setProduct(findProductById(productId));
                	recipe.setId(productId);
                	//recipes.add(recipe);
            	}
    			Integer raw_material_id = Integer.parseInt(cursor.getString(1));
    			RawMaterial material = findRawMaterialById(raw_material_id);
    			double quantity = Double.parseDouble(cursor.getString(2));
    			
    			Map<RawMaterial, Double> ingredients = recipe.getIngredients();
    			ingredients.put(material, quantity);
    			recipe.setIngredients(ingredients);
    			recipes.remove(recipe); // remove (if already present, determined by product ID) and re-insert
    			recipes.add(recipe);
    			
    			recipe = null;
            } while (cursor.moveToNext());
        }
        return recipes;
    }
    public int updateRecipe(Recipe recipe) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        int i = 0;
        
    	int productId = recipe.getProduct().getId();
    	Map<RawMaterial, Double> ingredients = recipe.getIngredients();
    	Recipe recipeinDb = findRecipeByProductId(productId);
    	Map<RawMaterial, Double> ingredientsinDb = recipeinDb.getIngredients();
    	Boolean isindb = false;
    	for(Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
    		RawMaterial ingredient = entry.getKey();
    	    Double quantity = entry.getValue();
        	isindb = false;
    	    for(Entry<RawMaterial, Double> entryDb : ingredientsinDb.entrySet()) {
        		RawMaterial ingredientinDb = entryDb.getKey();
        		if(ingredient.getId() == ingredientinDb.getId()){
        			isindb = true;
        			break;
        		}
    	    }
    	    ContentValues values = new ContentValues();
    	    values.put(RecipeTable.COLUMN_QUANTITY, quantity);
    	    if(isindb){
    	    	//update
    	    	i = i + db.update(RecipeTable.TABLE_NAME, //table
    	                values, // column/value
    	                RecipeTable.COLUMN_PRODUCT_ID +" = ? AND " + RecipeTable.COLUMN_RAW_MATERIAL_ID + " = ?", // selections
    	                new String[] { String.valueOf(productId), String.valueOf(ingredient.getId()) }); //selection args
    	    }    	    	
    	    else{
    	    	//add
        	    values.put(RecipeTable.COLUMN_PRODUCT_ID, productId);
        	    values.put(RecipeTable.COLUMN_RAW_MATERIAL_ID, ingredient.getId());        	    
        	    db.insert(RecipeTable.TABLE_NAME, null, values);
    	    }
    	}
    	
    	//Remove ingredients that shouldn't be in db
    	for(Entry<RawMaterial, Double> entryDb : ingredientsinDb.entrySet()) {
    		RawMaterial ingredientinDb = entryDb.getKey();
    		isindb = false;
    		for(Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
    			RawMaterial ingredient = entry.getKey();
    			if(ingredient.getId() == ingredientinDb.getId()){
        			isindb = true;
        			break;
        		}
    		}
    		if(!isindb){
    			//remove from db
    			db.delete(RawMaterialTable.TABLE_NAME, RecipeTable.COLUMN_PRODUCT_ID +" = ? AND " + RecipeTable.COLUMN_RAW_MATERIAL_ID + " = ?",
        	            new String[] { String.valueOf(productId), String.valueOf(ingredientinDb.getId()) });
    		}
    	}
        // 4. close
        db.close();
 
        return i;
    }
    public boolean deleteRecipe(int productid){
    	boolean result = false;
    	
    	String query = "Select * FROM " + RecipeTable.TABLE_NAME + " WHERE " + RecipeTable.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";

    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	if (cursor.moveToFirst()) {
    		Integer product_id = Integer.parseInt(cursor.getString(0));
    		db.delete(RecipeTable.TABLE_NAME, RecipeTable.COLUMN_PRODUCT_ID + " = ?",
    	            new String[] { String.valueOf(product_id) });
    		cursor.close();
    		result = true;
    	}
        db.close();
    	return result;
    }

}

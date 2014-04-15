package com.savanticab.seaweedapp.sqlite;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.savanticab.seaweedapp.model.Batch;
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
    	// TODO: addProduct and the rest "adders" should check if item variation already exists
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
        addProduct(new Product("SOAP1", "Soap", "Lime", "Big", 10.0, 200, 0));
        addProduct(new Product("SOAP2", "Soap", "Clove", "Small", 6.0, 100, 0));
        addProduct(new Product("SOAP3", "Soap", "Langi-langi", "Big", 14.0, 300, 0));
        addProduct(new Product("SOAP4", "Soap", "Lemongrass", "Medium", 16.0, 150, 0));
        
        addRawMaterial(new RawMaterial("Coconut oil", "L", 100, 0, 0.0));
        addRawMaterial(new RawMaterial("Seaweed", "Kg", 50, 0, 0.0));
        addRawMaterial(new RawMaterial("Bee wax", "Kg", 5, 2, 0.0));
        
        HashMap m = new HashMap<RawMaterial, Double>();
        m.put(new RawMaterial("Coconut oil", "L", 100, 0, 0.0), 1.0);
        m.put(new RawMaterial("Seaweed", "Kg", 50, 0, 0.0), 0.5);
        Recipe r = new Recipe(new Product("SOAP1", "Soap", "Lime", "Big", 10.0, 200, 0), m);
        addRecipe(r);
        
        m = new HashMap<RawMaterial, Double>();
        m.put(new RawMaterial("Coconut oil", "L", 100, 0, 0.0), 2.0);
        m.put(new RawMaterial("Bee wax", "Kg", 50, 0, 0.0), 0.25);
        r = new Recipe(new Product("SOAP2", "Soap", "Clove", "Small", 6.0, 100, 0), m);
        addRecipe(r);
        
        //Batch b = new Batch(r, 1, 100);
        //addBatch(b);
        
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        ProductTable.onCreate(db);
        RawMaterialTable.onCreate(db);
        RecipeTable.onCreate(db);
        BatchTable.onCreate(db);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ProductTable.onUpgrade(db, oldVersion, newVersion);
        RawMaterialTable.onUpgrade(db, oldVersion, newVersion);
        RecipeTable.onUpgrade(db, oldVersion, newVersion);
        BatchTable.onUpgrade(db, oldVersion, newVersion);
    }
    
    
    //Products
    public void addProduct(Product product) {

        ContentValues values = new ContentValues();
        values.put(ProductTable.COLUMN_ID, product.getId());
        values.put(ProductTable.COLUMN_CODE, product.getCode());
        values.put(ProductTable.COLUMN_NAME, product.getName());
        values.put(ProductTable.COLUMN_FRAGANCE, product.getFragance());
        values.put(ProductTable.COLUMN_SIZE, product.getSize());
        values.put(ProductTable.COLUMN_PRICE, product.getPrice());
        values.put(ProductTable.COLUMN_INSTOCKQTY, product.getInStockQty());
        values.put(ProductTable.COLUMN_INPRODUCTIONQTY, product.getInProductionQty());
        
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
    		product.setPrice(Double.parseDouble(cursor.getString(5)));
    		product.setInStockQty(Integer.parseInt(cursor.getString(6)));
    		product.setInProductionQty(Integer.parseInt(cursor.getString(7)));
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
        		product.setPrice(Double.parseDouble(cursor.getString(5)));
        		product.setInStockQty(Integer.parseInt(cursor.getString(6)));
        		product.setInProductionQty(Integer.parseInt(cursor.getString(7)));
        		
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
        values.put(ProductTable.COLUMN_PRICE, product.getPrice());
        values.put(ProductTable.COLUMN_INSTOCKQTY, product.getInStockQty());
        values.put(ProductTable.COLUMN_INPRODUCTIONQTY, product.getInProductionQty());
        
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
    	values.put(RawMaterialTable.COLUMN_ID, material.getId());
        values.put(RawMaterialTable.COLUMN_NAME, material.getName());
        values.put(RawMaterialTable.COLUMN_UNIT, material.getUnit());
        values.put(RawMaterialTable.COLUMN_STOCK_QUANTITY, material.getStockQuantity());
        values.put(RawMaterialTable.COLUMN_ORDERED_QUANTITY, material.getOrderedQuantity());
        values.put(RawMaterialTable.COLUMN_ALLOCATED_FOR_PRODUCTION_QUANTITY, material.getAllocatedProdQty());
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
    		material.setAllocatedProdQty(Double.parseDouble(cursor.getString(5)));
    		material.setIcon(cursor.getString(6));
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
        		material.setAllocatedProdQty(Double.parseDouble(cursor.getString(5)));
        		material.setIcon(cursor.getString(6));
 
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
        values.put(RawMaterialTable.COLUMN_ALLOCATED_FOR_PRODUCTION_QUANTITY, material.getAllocatedProdQty());
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
    	HashMap<RawMaterial, Double> ingredients = new HashMap<RawMaterial, Double>();
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
    
    // recipe id should be identical to product id in current implementation
    public Recipe findRecipeById(int id){
    	return findRecipeByProductId(id);
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
    			
    			HashMap<RawMaterial, Double> ingredients = recipe.getIngredients();
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
    
    // Batch jobs
    public void addBatch(Batch batch) {
    	
    	ContentValues values = new ContentValues();
        values.put(BatchTable.COLUMN_BATCH_ID, batch.getId());
        values.put(BatchTable.COLUMN_RECIPE_ID, batch.getRecipe().getProduct().getId());
        values.put(BatchTable.COLUMN_QUANTITY, batch.getQuantity());
        Date startDate = batch.getStartDate();
        Date finishDate = batch.getFinishDate();
        if (startDate != null){
        	values.put(BatchTable.COLUMN_STARTDATE, Long.toString(batch.getStartDate().getTime()));
        }
        else {
        	values.put(BatchTable.COLUMN_STARTDATE, "null");
        }
        if (finishDate != null){
        	values.put(BatchTable.COLUMN_FINISHDATE, Long.toString(batch.getFinishDate().getTime()));
        }
        else {
        	values.put(BatchTable.COLUMN_FINISHDATE, "null");
        }
        
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.insert(BatchTable.TABLE_NAME, null, values);
        db.close();
    }
    
    public boolean deleteBatch(int id) {
    	
    	boolean result = false;
    	
    	String query = "Select * FROM " + BatchTable.TABLE_NAME + " WHERE " + BatchTable.COLUMN_BATCH_ID + " =  \"" + id + "\"";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	//Batch batch = new Batch();
    	
    	if (cursor.moveToFirst()) {
    		//batch.setId(Integer.parseInt(cursor.getString(0)));
    		db.delete(BatchTable.TABLE_NAME, BatchTable.COLUMN_BATCH_ID + " = ?",
    	            new String[] { String.valueOf(id) });
    		cursor.close();
    		result = true;
    	}
            db.close();
    	return result;
    }
    
    // "finders" and "getters" for Batch
    public Batch findBatchById(int batchid){
    	String query = "Select * FROM " + BatchTable.TABLE_NAME + " WHERE " + BatchTable.COLUMN_BATCH_ID + " =  \"" + batchid + "\"";
    	return findBatch(query);
    }
    private Batch findBatch(String query){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	Batch batch = new Batch();
    	
    	if (cursor.moveToFirst()) {
    		batch.setId(Integer.parseInt(cursor.getString(0)));
    		
    		// recipe must be loaded separately from other table
    		int recipeId = Integer.parseInt(cursor.getString(1));
    		List<Recipe> allrecipes = getAllRecipes(); // this could be done more elegantly by querying in the recipe table
    		Recipe recipe = null;
    		for (int i=0; i<allrecipes.size(); i++) {
    			if (allrecipes.get(i).getId()==recipeId) {
    				recipe = allrecipes.get(i);
    			}
    		}
    		batch.setRecipe(recipe);
    		
    		batch.setQuantity(Integer.parseInt(cursor.getString(2)));
    		batch.setStartDate(new Date(Integer.parseInt(cursor.getString(3))));
    		batch.setFinishDate(new Date(Integer.parseInt(cursor.getString(4))));
    		cursor.close();
    	} else {
    		batch = null;
    	}
            db.close();
    	return batch;
    }
    
    public List<Batch> getAllBatches() {
        List<Batch> batches = new LinkedList<Batch>();
        
        String query = "SELECT  * FROM " + BatchTable.TABLE_NAME;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        Batch batch = null;
        if (cursor.moveToFirst()) {
            do {
            	Integer id = Integer.parseInt(cursor.getString(0));
            	Integer recipeId = Integer.parseInt(cursor.getString(1));
            	Integer quantity = Integer.parseInt(cursor.getString(2));
            	
            	for (Batch b : batches){
            		if (b.getId() == id) {
            			batch = b;
            		}
            	}
            	if (batch == null){
            		batch = new Batch();
            		batch.setRecipe(findRecipeById(recipeId));
                	batch.setId(id);
                	batch.setQuantity(quantity);
                	//recipes.add(recipe);
            	}
            	
            	Date start, finish;
            	String s = cursor.getString(3);
            	if (s.equals("null")) {
            		start = null;
            	}
            	else {
            		start = new Date();
            		start.setTime(Long.parseLong(s));;
            	}
            	batch.setStartDate(start);
            	
            	String s2 = cursor.getString(4);
            	if (s2.equals("null")) {
            		finish = null;
            	}
            	else {
            		finish = new Date();
            		finish.setTime(Long.parseLong(s2));;
            	}
            	batch.setFinishDate(finish);
            	
            	batches.remove(batch); // remove (if already present, determined by product ID) and re-insert
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
    		//Collections.reverse(allbatches);
    		lastId = allbatches.get(allbatches.size()-1).getId();
    	}
		return lastId;
    }
    
    public int updateBatch(Batch batch) {
    	 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(BatchTable.COLUMN_BATCH_ID, batch.getId());
        values.put(BatchTable.COLUMN_RECIPE_ID, batch.getRecipe().getId());
        values.put(BatchTable.COLUMN_QUANTITY, batch.getQuantity());
        
        int id = batch.getId();
        int rid = batch.getRecipe().getId();
        int q = batch.getQuantity();
        
        String startDate = (batch.getStartDate() != null) ? Long.toString(batch.getStartDate().getTime()) : "null";
        String finishDate = (batch.getFinishDate() != null) ? Long.toString(batch.getFinishDate().getTime()) : "null";
        values.put(BatchTable.COLUMN_STARTDATE, startDate);
        values.put(BatchTable.COLUMN_FINISHDATE, finishDate);
        
        int i = 0;
        if (true) { //findRecipeByProductId(batch.getRecipe().getProduct().getId()) != null) {
	        // 3. updating row
	        i = db.update(BatchTable.TABLE_NAME, //table
	                values, // column/value
	                BatchTable.COLUMN_BATCH_ID+" = ?", // selections
	                new String[] { String.valueOf(batch.getId()) }); //selection args
	        
	        // 4. close
	        
        }
        db.close();
        return i;
 
    }
    
}

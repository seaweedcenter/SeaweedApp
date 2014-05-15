package com.savanticab.seaweedapp.sqlite;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RecipeDBAdapter extends BaseDBAdapter{
	

		// Database table
		  public static final String TABLE_NAME = "recipes";
		  //public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_PRODUCT_ID = "product_id";
		  //public static final String COLUMN_RAW_MATERIAL_ID = "rawmaterial_id";
		  //public static final String COLUMN_QUANTITY = "quantity";
		  public static final String COLUMN_INGREDIENTS = "ingredients";
		  
		  // Database creation SQL statement
		  protected static final String DATABASE_CREATE = "create table " 
		      + TABLE_NAME
		      + "(" 
		      //+ COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_PRODUCT_ID + " integer primary key references " + ProductDBAdapter.TABLE_NAME + "(" + ProductDBAdapter.COLUMN_ID + "), " 
		      //+ COLUMN_RAW_MATERIAL_ID + " integer not null references " + RawMaterialTable.TABLE_NAME + "(" + RawMaterialTable.COLUMN_ID + "), " 
		      //+ COLUMN_QUANTITY + " real, " 
			  //+ "primary key(" + COLUMN_PRODUCT_ID + ", " + COLUMN_RAW_MATERIAL_ID + ")"
		      + COLUMN_INGREDIENTS + " text"
		      + ");";
		  
		  
		  public RecipeDBAdapter(Context context) {
				super(context);
			}
		  Gson gson;
		  public ContentValues getContentValues(Recipe recipe) {
				ContentValues values = new ContentValues();
		    	values.put(COLUMN_PRODUCT_ID, recipe.getProduct().getId());
		    	gson = new Gson();
		    	//Store ingredients as JSON string
		        values.put(COLUMN_INGREDIENTS, gson.toJson(recipe.getIngredients()));
				return values;
			}
			
			public Recipe loadFromCursor(Cursor cursor) {
				Recipe recipe = new Recipe();
				int productId = Integer.parseInt(cursor.getString(0));
				recipe.setId(productId);
				recipe.setProduct(new ProductDBAdapter(mContext).findProductById(productId));
				
				Type type = new TypeToken<LinkedHashMap<String, Double>>(){}.getType();
				String strng = cursor.getString(1);
				gson = new Gson();
				LinkedHashMap<String, Double> ingredientsDB = gson.fromJson(strng, type);
				LinkedHashMap<RawMaterial, Double> ingredients = new LinkedHashMap<RawMaterial, Double>();
				RawMaterial material =  null;
				RawMaterialDBAdapter mAdapter = new RawMaterialDBAdapter(mContext);
				for(Entry<String, Double> ingredient: ingredientsDB.entrySet()){
					material = mAdapter.findRawMaterialByName(ingredient.getKey());
					ingredients.put(material, ingredient.getValue());
				}
				recipe.setIngredients(ingredients);
				return recipe;
			}
			
		    public void addRecipe(Recipe recipe){
		        SQLiteDatabase db = helper.getWritableDatabase();
		    	db.insert(TABLE_NAME, null, getContentValues(recipe));
		        db.close();
		    }
		    
		    public Recipe findRecipeByProductId(int productid){
		    
		    	String query = "Select * FROM " + RecipeDBAdapter.TABLE_NAME + " WHERE " + RecipeDBAdapter.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
		    	SQLiteDatabase db = helper.getWritableDatabase();
		    	Cursor cursor = db.rawQuery(query, null);
		    	Recipe recipe = null;
		    	
		    	if (cursor.moveToFirst()) {
		    		recipe = loadFromCursor(cursor);
		    		cursor.close();
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
		        String query = "SELECT  * FROM " + RecipeDBAdapter.TABLE_NAME;
		        SQLiteDatabase db = helper.getWritableDatabase();
		        Cursor cursor = db.rawQuery(query, null);
		        Recipe recipe = null;
		        
		        if (cursor.moveToFirst()) {
		            do {
		            	recipe = loadFromCursor(cursor);
		    			recipes.add(recipe);
		    			
		    			recipe = null;
		            } while (cursor.moveToNext());
		        }
		        return recipes;
		    }
		    
		    public int updateRecipe(Recipe recipe) {
		 
		        SQLiteDatabase db = helper.getWritableDatabase();
		        int i = 0;
		        
		    	int productId = recipe.getProduct().getId();
		    	
		    	i =  db.update(RecipeDBAdapter.TABLE_NAME, //table
    	                getContentValues(recipe), // column/value
    	                RecipeDBAdapter.COLUMN_PRODUCT_ID +" = ?", // selections
    	                new String[] { String.valueOf(productId)}); //selection args
		        
		        db.close();
		        return i;
		    }
		 
		    public boolean deleteRecipe(int productid){
		    
		    	boolean result = false;
		    	String query = "Select * FROM " + RecipeDBAdapter.TABLE_NAME + " WHERE " + RecipeDBAdapter.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
		    	SQLiteDatabase db = helper.getWritableDatabase();
		    	Cursor cursor = db.rawQuery(query, null);
		    	
		    	if (cursor.moveToFirst()) {
		    		db.delete(RecipeDBAdapter.TABLE_NAME, RecipeDBAdapter.COLUMN_PRODUCT_ID + " = ?",
		    	            new String[] { cursor.getString(0) });
		    		cursor.close();
		    		result = true;
		    	}
		        db.close();
		    	return result;
		    }
		  
}

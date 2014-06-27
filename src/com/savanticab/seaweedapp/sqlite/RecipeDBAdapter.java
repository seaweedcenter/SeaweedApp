package com.savanticab.seaweedapp.sqlite;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RecipeDBAdapter extends BaseDBAdapter<Recipe>{
	

		// Database table
		  public static final String TABLE_NAME = "recipes";
		  //public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_PRODUCT_ID = "product_id";
		  //public static final String COLUMN_RAW_MATERIAL_ID = "rawmaterial_id";
		  //public static final String COLUMN_QUANTITY = "quantity";
		  public static final String COLUMN_INGREDIENTS = "ingredients";
		  public static final String COLUMN_INSTRUCTIONS = "instructions";
		  
		  // Database creation SQL statement
		  protected static final String DATABASE_CREATE = "create table " 
		      + TABLE_NAME
		      + "(" 
		      //+ COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_PRODUCT_ID + " integer primary key references " + ProductDBAdapter.TABLE_NAME + "(" + ProductDBAdapter.COLUMN_ID + "), " 
		      //+ COLUMN_RAW_MATERIAL_ID + " integer not null references " + RawMaterialTable.TABLE_NAME + "(" + RawMaterialTable.COLUMN_ID + "), " 
		      //+ COLUMN_QUANTITY + " real, " 
			  //+ "primary key(" + COLUMN_PRODUCT_ID + ", " + COLUMN_RAW_MATERIAL_ID + ")"
		      + COLUMN_INGREDIENTS + " text,"
		      + COLUMN_INSTRUCTIONS + " text"
		      + ");";

			@Override public String getTableName() { return TABLE_NAME; }
			@Override protected String getColumnIdName() { return COLUMN_PRODUCT_ID; }
			
		  public RecipeDBAdapter(Context context) {
				super(context);
			}
		  Gson gson;
		  @Override
		public ContentValues getContentValues(Recipe recipe) {
				ContentValues values = new ContentValues();
		    	values.put(COLUMN_PRODUCT_ID, recipe.getProduct().getId());
		    	gson = new Gson();
		    	//Store ingredients as JSON string
		        values.put(COLUMN_INGREDIENTS, gson.toJson(recipe.getIngredients()));
		        values.put(COLUMN_INSTRUCTIONS, gson.toJson(recipe.getInstructions()));
		        //values.put(COLUMN_INSTRUCTIONS, recipe.getInstructions());
		        
				return values;
			}
			
			@Override
			public Recipe loadFromCursor(Cursor cursor) {
				Recipe recipe = new Recipe();
				int productId = cursor.getInt(0);
				//recipe.setId(productId);		
				//recipe.setProduct(new ProductDBAdapter(mContext).findProductById(productId));			
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
				
				Type type2 = new TypeToken<ArrayList<String>>(){}.getType();
				String strng2 = cursor.getString(2);
				gson = new Gson();
				ArrayList<String> instructionsDB = gson.fromJson(strng2, type2);

				recipe.setIngredients(ingredients);
				recipe.setInstructions(instructionsDB);
				//recipe.setInstructions(cursor.getString(2));
				return recipe;
			}	
			

			@Override
			public DbxFields getFields(Recipe item){
				DbxFields fields = new DbxFields();
				fields.set(COLUMN_PRODUCT_ID, item.getProduct().getId());
				fields.set(COLUMN_INGREDIENTS, new Gson().toJson(item.getIngredients()));
				fields.set(COLUMN_INSTRUCTIONS, new Gson().toJson(item.getInstructions()));
				return fields;
			}
			
			@Override
			public Recipe loadFromRecord(DbxRecord record) {
				Recipe item = new Recipe();
				item.setProduct(new ProductDBAdapter(mContext).findProductById(record.getString(COLUMN_PRODUCT_ID)));				
				String stringIng = record.getString(COLUMN_INGREDIENTS);			
				Type type = new TypeToken<LinkedHashMap<String, Double>>(){}.getType();
				LinkedHashMap<String, Double> ingredientsDB = new Gson().fromJson(stringIng, type);
				LinkedHashMap<RawMaterial, Double> ingredients = new LinkedHashMap<RawMaterial, Double>();
				RawMaterial material =  null;
				RawMaterialDBAdapter mAdapter = new RawMaterialDBAdapter(mContext);
				for(Entry<String, Double> ingredient: ingredientsDB.entrySet()){
					material = mAdapter.findRawMaterialByName(ingredient.getKey());
					ingredients.put(material, ingredient.getValue());
				}
				item.setIngredients(ingredients);	
				
				if (record.hasField(COLUMN_INSTRUCTIONS)){
					Type type1 = new TypeToken<ArrayList<String>>(){}.getType();
					String strng1 = record.getString(COLUMN_INSTRUCTIONS);
					gson = new Gson();
					ArrayList<String> instructionsDB = gson.fromJson(strng1, type1);
					item.setInstructions(instructionsDB);
				}
				return item;
			}
		    
		    public Recipe findRecipeByProductId(String productid){	    
		    	//String query = "Select * FROM " + RecipeDBAdapter.TABLE_NAME + " WHERE " + RecipeDBAdapter.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
		    	//return find(query);
		    	return find(new DbxFields().set(COLUMN_PRODUCT_ID, productid));
		    }
		    
		    // recipe id should be identical to product id in current implementation
		    public Recipe findRecipeById(String id){
		    	return findRecipeByProductId(id);
		    }
		    
		    public int updateRecipe(Recipe recipe) {
		 
		        //return update(recipe, COLUMN_PRODUCT_ID + " = ?", new String[] { String.valueOf(recipe.getProduct().getId())});
		        return update(recipe, recipe.getId());
		    }
		 
		    public boolean deleteRecipe(String productid){
		    
		    	//String query = "Select * FROM " + RecipeDBAdapter.TABLE_NAME + " WHERE " + RecipeDBAdapter.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
		    	//return delete(query);
		    	return delete(new DbxFields().set(COLUMN_PRODUCT_ID, productid));
		    }
		  
}

package com.savanticab.seaweedapp.sqlite;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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

			@Override public String getTableName() { return TABLE_NAME; }
			@Override protected String getColumnIdName() { return COLUMN_PRODUCT_ID; }
			
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
				int productId = cursor.getInt(0);
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
		    
		    public Recipe findRecipeByProductId(int productid){	    
		    	String query = "Select * FROM " + RecipeDBAdapter.TABLE_NAME + " WHERE " + RecipeDBAdapter.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
		    	return find(query);
		    }
		    
		    // recipe id should be identical to product id in current implementation
		    public Recipe findRecipeById(int id){
		    	return findRecipeByProductId(id);
		    }
		    
		    public int updateRecipe(Recipe recipe) {
		 
		        return update(recipe, COLUMN_PRODUCT_ID + " = ?", new String[] { String.valueOf(recipe.getProduct().getId())});
		    }
		 
		    public boolean deleteRecipe(int productid){
		    
		    	String query = "Select * FROM " + RecipeDBAdapter.TABLE_NAME + " WHERE " + RecipeDBAdapter.COLUMN_PRODUCT_ID + " =  \"" + productid + "\"";
		    	return delete(query);
		    }
		  
}

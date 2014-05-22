package com.savanticab.seaweedapp.sqlite;

import java.util.LinkedHashMap;

import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.MaterialInventory;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.ProductInventory;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// read/write to local SQLite DB
// TODO: think about homogenizing the different find* methods, not so consistent now...
// could even think about merging this class and Inventory class?

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SeaweedDB.db";
 
    private static MySQLiteHelper sInstance;
    public static MySQLiteHelper getInstance(Context context) {

        if (sInstance == null) {
            // Use the application context, which will ensure that you 
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
          sInstance = new MySQLiteHelper(context.getApplicationContext());
          
          //TODO: Check that items don't exist in DB before inserting... Next step is having items in remote DB
          Product product1 = new Product("SOAP1", "Soap", "Lime", "Big", 10.0);
          Product product2 = new Product("SOAP2", "Soap", "Clove", "Small", 6.0);
          Product product3 = new Product("SOAP3", "Soap", "Langi-langi", "Big", 14.0);
          Product product4 = new Product("SOAP4", "Soap", "Lemongrass", "Medium", 16.0);
          ProductDBAdapter productAdapter = new ProductDBAdapter(context);
          productAdapter.add(product1);
          productAdapter.add(product2);
          productAdapter.add(product3);
          productAdapter.add(product4);
          
          ProductInventoryDBAdaptor productInventoryAdapter = new ProductInventoryDBAdaptor(context);
          productInventoryAdapter.add(new ProductInventory(product1, 200, 0));
          productInventoryAdapter.add(new ProductInventory(product2, 100, 0));
          productInventoryAdapter.add(new ProductInventory(product3, 300, 0));
          productInventoryAdapter.add(new ProductInventory(product4, 150, 0));
          
          RawMaterialDBAdapter materialAdapter = new RawMaterialDBAdapter(context);
          RawMaterial material1 = new RawMaterial("Coconut oil", "L", "");
          RawMaterial material2 = new RawMaterial("Seaweed", "Kg", "");
          RawMaterial material3 = new RawMaterial("Bee wax", "Kg", "");
          materialAdapter.add(material1);
          materialAdapter.add(material2);
          materialAdapter.add(material3);
          
          MaterialInventoryDBAdapter matInvAdapter = new MaterialInventoryDBAdapter(context);
          matInvAdapter.add(new MaterialInventory(material1, 100, 0, 0.0));
          matInvAdapter.add(new MaterialInventory(material2, 50, 0, 0.0));
          matInvAdapter.add(new MaterialInventory(material3, 5, 2, 0.0));
          
          
          LinkedHashMap<RawMaterial, Double> m = new LinkedHashMap<RawMaterial, Double>();
          m.put(material1, 1.0);
          m.put(material2, 0.5);        
          Recipe r = new Recipe(product1, m);
          RecipeDBAdapter recipeAdapter = new RecipeDBAdapter(context);
          recipeAdapter.add(r);
          
          m = new LinkedHashMap<RawMaterial, Double>();
          m.put(material1, 2.0);
          m.put(material2, 0.5); 
          m.put(material3, 0.25);
          r = new Recipe(product2, m);
          recipeAdapter.add(r);
          
          Batch batch1 = new Batch(r, 1, 20);
          BatchDBAdapter bAdapter = new BatchDBAdapter(context);
          bAdapter.add(batch1);
        }
        return sInstance;
      }
    
    public MySQLiteHelper(Context context) {
    	
    	// set up a basic database to start from
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(ProductDBAdapter.DATABASE_CREATE);
    	db.execSQL(RawMaterialDBAdapter.DATABASE_CREATE);
    	db.execSQL(RecipeDBAdapter.DATABASE_CREATE);
    	db.execSQL(BatchDBAdapter.DATABASE_CREATE);
    	db.execSQL(ProductInventoryDBAdaptor.DATABASE_CREATE);
    	db.execSQL(MaterialInventoryDBAdapter.DATABASE_CREATE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    	db.execSQL("DROP TABLE IF EXISTS " + ProductDBAdapter.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS " + RawMaterialDBAdapter.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS " + RecipeDBAdapter.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS " + BatchDBAdapter.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS " + ProductInventoryDBAdaptor.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS " + MaterialInventoryDBAdapter.TABLE_NAME);
    	
    	onCreate(db);
    }
    
}

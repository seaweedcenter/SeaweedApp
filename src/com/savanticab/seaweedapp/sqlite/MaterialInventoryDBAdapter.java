package com.savanticab.seaweedapp.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.savanticab.seaweedapp.model.MaterialInventory;
import com.savanticab.seaweedapp.model.RawMaterial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MaterialInventoryDBAdapter extends BaseDBAdapter<MaterialInventory> {

	// Database table
		  public static final String TABLE_NAME = "material_inventories";
		  public static final String COLUMN_ID = "_id";
		  public static final String COLUMN_MATERIAL_ID = "material_id";
		  public static final String COLUMN_STOCK_QUANTITY = "stock_quantity";
		  public static final String COLUMN_ORDERED_QUANTITY = "ordered_quantity";
		  public static final String COLUMN_RESERVED_QUANTITY = "reserved_quantity";

		  // Database creation SQL statement
		  protected static final String DATABASE_CREATE = "create table " 
		      + TABLE_NAME
		      + "(" 
		      //+ COLUMN_ID + " integer primary key autoincrement, " 
		      //+ COLUMN_ID + " integer primary key, "
		      + COLUMN_MATERIAL_ID + " text primary key references " + RawMaterialDBAdapter.TABLE_NAME + "("+ RawMaterialDBAdapter.COLUMN_ID + "), " 
		      + COLUMN_STOCK_QUANTITY + " real, " 
		      + COLUMN_ORDERED_QUANTITY + " real, "
		      + COLUMN_RESERVED_QUANTITY + " real"
		      + ");";

		  @Override public String getTableName() { return TABLE_NAME; }	
		  @Override protected String getColumnIdName() { return COLUMN_ID; }
		  
		  public MaterialInventoryDBAdapter(Context context){
			  super(context);
		  }
			
			public ContentValues getContentValues(MaterialInventory materialInventory) {
				ContentValues values = new ContentValues();
				values.put(MaterialInventoryDBAdapter.COLUMN_MATERIAL_ID, materialInventory.getMaterial().getId());		
				values.put(MaterialInventoryDBAdapter.COLUMN_STOCK_QUANTITY, materialInventory.getStock());
				values.put(MaterialInventoryDBAdapter.COLUMN_ORDERED_QUANTITY, materialInventory.getOrdered());
				values.put(MaterialInventoryDBAdapter.COLUMN_RESERVED_QUANTITY, materialInventory.getReserved());
				return values;
			}
			
			public MaterialInventory loadFromCursor(Cursor cursor) {
				MaterialInventory materialInventory = new MaterialInventory();
				materialInventory.setMaterial(new RawMaterialDBAdapter(mContext).findRawMaterialById(Integer.parseInt(cursor.getString(0))));
				materialInventory.setStock(Integer.parseInt(cursor.getString(1)));
				materialInventory.setOrdered(Integer.parseInt(cursor.getString(2)));
				materialInventory.setReserved(Integer.parseInt(cursor.getString(3)));
				return materialInventory;
			}
		    
		    public MaterialInventory findMaterialInventoryByMaterialId(int materialId){
		    	String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_MATERIAL_ID + " =  \"" + materialId + "\"";
		    	return find(query);
		    }
		    
		    public List<RawMaterial> getAllMaterialsInInventory() {
		    	List<MaterialInventory> materialInventory = getAll();
		    	List<RawMaterial> materials = new ArrayList<RawMaterial>();
		    	for (MaterialInventory mi : materialInventory) {
		    		materials.add(mi.getMaterial());
		    	}
		    	return materials;
		    }
		    
		    public int updateMaterialInventory(MaterialInventory materialInventory) {
		    	
		        return update(materialInventory, COLUMN_MATERIAL_ID + " = ?", new String[] { String.valueOf(materialInventory.getMaterial().getId()) });
		        
		    }
		    
		    public boolean deleteMaterialInventory(int materialId) {
		    	
		    	String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_MATERIAL_ID + " =  \"" + materialId + "\"";
		    	return delete(query);
		    }
		    
		    //TODO: move somewhere else?
		    public void MtrlReserve(RawMaterial mtrl, double qty) { // when batch is created
				MaterialInventory inv = this.findMaterialInventoryByMaterialId(mtrl.getId());// materials.get(mtrl); 
				inv.setStock(inv.getStock() - qty);
				inv.setReserved(inv.getReserved() + qty);
				this.updateMaterialInventory(inv);
			}
			public void MtrlUnreserve(RawMaterial mtrl, double qty) {	// use if batch is cancelled
				MaterialInventory inv = this.findMaterialInventoryByMaterialId(mtrl.getId());// materials.get(mtrl); 
				inv.setStock(inv.getStock() + qty);
				inv.setReserved(inv.getReserved() - qty);
				this.updateMaterialInventory(inv);
			}
			public void MtrlReservedFinished(RawMaterial mtrl, double qty) { // when batch job is finished
				MaterialInventory inv = this.findMaterialInventoryByMaterialId(mtrl.getId());// materials.get(mtrl); 
				inv.setReserved(inv.getReserved() - qty);
				this.updateMaterialInventory(inv);
			}
			  
}

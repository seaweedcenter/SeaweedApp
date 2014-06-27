package com.savanticab.seaweedapp.sqlite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.savanticab.seaweedapp.model.Batch;
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
		      + COLUMN_MATERIAL_ID + " integer primary key references " + RawMaterialDBAdapter.TABLE_NAME + "("+ RawMaterialDBAdapter.COLUMN_ID + "), " 
		      + COLUMN_STOCK_QUANTITY + " real, " 
		      + COLUMN_ORDERED_QUANTITY + " real, "
		      + COLUMN_RESERVED_QUANTITY + " real"
		      + ");";

		  @Override public String getTableName() { return TABLE_NAME; }	
		  @Override protected String getColumnIdName() { return COLUMN_ID; }
		  
		  public MaterialInventoryDBAdapter(Context context){
			  super(context);
		  }
			
			@Override
			public ContentValues getContentValues(MaterialInventory materialInventory) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_MATERIAL_ID, materialInventory.getMaterial().getId());		
				values.put(COLUMN_STOCK_QUANTITY, materialInventory.getStock());
				values.put(COLUMN_ORDERED_QUANTITY, materialInventory.getOrdered());
				values.put(COLUMN_RESERVED_QUANTITY, materialInventory.getReserved());
				return values;
			}
			
			@Override
			public MaterialInventory loadFromCursor(Cursor cursor) {
				MaterialInventory materialInventory = new MaterialInventory();
				//materialInventory.setMaterial(new RawMaterialDBAdapter(mContext).findRawMaterialById(cursor.getInt(0)));
				materialInventory.setStock(cursor.getDouble(1));
				materialInventory.setOrdered(cursor.getDouble(2));
				materialInventory.setReserved(cursor.getDouble(3));
				return materialInventory;
			}

			
			@Override
			public DbxFields getFields(MaterialInventory item){
				DbxFields fields = new DbxFields();
				fields.set(COLUMN_MATERIAL_ID, item.getMaterial().getId());
				fields.set(COLUMN_STOCK_QUANTITY, item.getStock());
				fields.set(COLUMN_ORDERED_QUANTITY, item.getOrdered());
				fields.set(COLUMN_RESERVED_QUANTITY, item.getReserved());
				return fields;
			}
			
			@Override
			public MaterialInventory loadFromRecord(DbxRecord record) {
				MaterialInventory materialInv = new MaterialInventory();
				
				// material must be loaded separately from other table
				String materialId = record.getString(COLUMN_MATERIAL_ID);
				materialInv.setMaterial(new RawMaterialDBAdapter(mContext).findItemById(materialId));
				
				materialInv.setStock(record.getDouble(COLUMN_STOCK_QUANTITY));
				materialInv.setOrdered(record.getDouble(COLUMN_ORDERED_QUANTITY));
				materialInv.setReserved(record.getDouble(COLUMN_RESERVED_QUANTITY));
				return materialInv;
			}
			
		    public MaterialInventory findMaterialInventoryByMaterialId(String materialId){
		    	//String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_MATERIAL_ID + " =  \"" + materialId + "\"";
		    	//return find(query);
		    	return findItemById(materialId);
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
		    	
		        //return update(materialInventory, COLUMN_MATERIAL_ID + " = ?", new String[] { String.valueOf(materialInventory.getMaterial().getId()) });
		        return update(materialInventory, materialInventory.getId());
		    }
		    
		    public boolean deleteMaterialInventory(String materialId) {
		    	
		    	//String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_MATERIAL_ID + " =  \"" + materialId + "\"";
		    	//return delete(query);
		    	return delete(new DbxFields().set(COLUMN_MATERIAL_ID, materialId));
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

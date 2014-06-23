package com.savanticab.seaweedapp.model;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.savanticab.seaweedapp.SeaweedApplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable { // extends ParseObject

	public static final String TABLE_NAME = "recipes";
	public static final String ID = "id";
	public static final String PRODUCT_ID = "product_id";
	public static final String INGREDIENTS = "ingredients";

	private String id;
	private Product product; // unique
	private String productId; // Id of product, external key
	private LinkedHashMap<RawMaterial, Double> ingredients;
	private LinkedHashMap<String, Double> ingredientsId; // same as ingredients
															// but rawmaterial
															// id in db instead
															// of object

	private DbxRecord mRecord;

	// getters and setters
	public String getId() {
		return id;
	}

	/*
	 * public void setId(int id) { this.id = id; put("id", id); }
	 */
	public Product getProduct() {
		if (null == product) {
			DbxDatastore store = null;
			try {
				store = DbxDatastore.openDefault(SeaweedApplication.getDefaultAccount());
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DbxTable productTbl = store.getTable(Product.TABLE_NAME);
				DbxRecord firstResult;
				try {
					firstResult = productTbl.get(this.productId);
					product = new Product(firstResult);
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				store.close();
			}
		}
		return product;
	}

	/*
	 * public void setProduct(Product product) { this.product = product;
	 * this.productId = product.getId(); //put("product", product); //put("id",
	 * product.getId()); }
	 */
	public String getProductId() {
		return productId;
	}
	public LinkedHashMap<RawMaterial, Double> getIngredients() {
		if (null == ingredients) {
			ingredients = new LinkedHashMap<RawMaterial, Double>();
			DbxDatastore store = null;
			try {
				store = DbxDatastore.openDefault(SeaweedApplication.getDefaultAccount());
				
			} catch (DbxException e) {
				e.printStackTrace();
				ingredients = null;
			} finally {
				DbxTable materialTbl = store.getTable(RawMaterial.TABLE_NAME);
				
				//get material object information from db and material id
				for(Entry<String, Double> ing: this.ingredientsId.entrySet()){
					DbxRecord firstResult;
					try {
						firstResult = materialTbl.get(ing.getKey());
						ingredients.put(new RawMaterial(firstResult), ing.getValue());	
					} catch (DbxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				store.close();
			}
		}
		return ingredients;
	}

	public void setIngredients(LinkedHashMap<RawMaterial, Double> ingredients) {
		this.ingredients = ingredients;
		this.ingredientsId = new LinkedHashMap<String, Double>();
		for (Entry<RawMaterial, Double> ing : ingredients.entrySet()) {
			ingredientsId.put(ing.getKey().getId(), ing.getValue());
		}
	}

	public LinkedHashMap<String, Double> getIngredientsId() {

		return ingredientsId;
	}

	
	  public void setIngredientsId(LinkedHashMap<String, Double> ingredientsId)
	  { 
		  this.ingredientsId = ingredientsId;
		  this.ingredients = null;
	  
	  }
	 

	// constructors
	public Recipe() {
	}

	public Recipe(Product p, LinkedHashMap<String, Double> ingredientsId) {
		this.product = p;
		this.productId = p.getId();
		this.ingredientsId = ingredientsId; 
		this.ingredients = null;
	}

	public Recipe(DbxRecord record) {
		this.mRecord = record;
		this.productId = record.getString(PRODUCT_ID);
		this.product = null;

		String ingredients = record.getString(INGREDIENTS);
		Type type = new TypeToken<LinkedHashMap<String, Double>>() {
		}.getType();
		this.ingredientsId = new Gson().fromJson(ingredients, type);
		this.ingredients = null;

		this.id = record.getId();
	}

	public void save(DbxDatastore store) {
		if (null == mRecord) {
			mRecord = store.getTable(TABLE_NAME).insert();
		}
		//mRecord.set(PRODUCT_ID, this.product.getId());
		mRecord.set(INGREDIENTS, new Gson().toJson(this.ingredientsId));
		try {
			store.sync();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// equals and hashCode needed for LinkedHashMaps etc
	// TODO: think about a more proper way of implementing equals and hashCode ?
	public boolean equals(Recipe r) {
		return (r.getProductId() == this.getProductId());
	}

	public int hashCode() {
		return (this.id + " " + getProduct().getName() + getProduct().getCode())
				.hashCode();
	}

	// description
	public String toString() {
		/*
		 * product = (Product) getParseObject("product"); try {
		 * product.fetchIfNeeded(); } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		return (getProduct().getCode() + " " + getProduct().getName());
	}

	// Parcelable implementation
	// to pass object around in Bundles
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeParcelable(product, flags);
		out.writeSerializable(ingredients);
	}

	public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
		public Recipe createFromParcel(Parcel in) {
			return new Recipe(in);
		}

		public Recipe[] newArray(int size) {
			return new Recipe[size];
		}
	};

	private Recipe(Parcel in) {
		id = in.readString();
		product = (Product) in.readParcelable(Product.class.getClassLoader());
		ingredients = (LinkedHashMap<RawMaterial, Double>) in
				.readSerializable();
	}

}

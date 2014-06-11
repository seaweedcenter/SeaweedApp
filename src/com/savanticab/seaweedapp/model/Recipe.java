package com.savanticab.seaweedapp.model;

import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Parcel;
import android.os.Parcelable;

@ParseClassName("Recipe")
public class Recipe extends ParseObject implements Parcelable {
	
	private int id;
	private Product product;
	private LinkedHashMap<RawMaterial, Double> ingredients;
	
	// getters and setters
	public int getId() {
		//return id;
		return getInt("id");
	}
	public void setId(int id) {
		this.id = id;
		put("id", id);
	}
	public Product getProduct() {
		//return product;
		return (Product) getParseObject("product");
	}
	public void setProduct(Product product) {
		this.product = product;
		this.id = product.getId();
		put("product", product);
		//put("id", product.getId());
	}
	public LinkedHashMap<RawMaterial, Double> getIngredients() throws JSONException {
		ingredients = new LinkedHashMap<RawMaterial, Double>();
		JSONArray ingArray = getJSONArray("ingredients");
		for(int i = 0; i<ingArray.length(); i++){
			JSONObject ingObj = (JSONObject) ingArray.get(i);
			JSONObject ing = (JSONObject)ingObj.get("ingredient");
			String objId = ing.getString("objectId");
			ParseQuery<RawMaterial> query = ParseQuery.getQuery(RawMaterial.class);
			RawMaterial material = null;
			try {
				material = query.get(objId).fetchIfNeeded();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Double quantity = ingObj.getDouble("quantity");
			ingredients.put(material, quantity);
		}
		return ingredients;
	}
	public void setIngredients(LinkedHashMap<RawMaterial, Double> ingredients) throws JSONException {
		this.ingredients = ingredients;
		JSONArray ingArray = new JSONArray();
		for (java.util.Map.Entry<RawMaterial,Double> ing:ingredients.entrySet()){
			JSONObject ingObj = new JSONObject();
			ingObj.put("ingredient", ing.getKey());
			ingObj.put("quantity", ing.getValue());
			ingArray.put(ingObj);			
		}
		put("ingredients", ingArray);
		
	}

	// constructors
	public Recipe(){
		ingredients = new LinkedHashMap<RawMaterial, Double>();
	}
	public Recipe(Product p, LinkedHashMap<RawMaterial, Double> ingredients){
		this.product = p;
		//this.id = p.getId();	// is recipe ID supposed to be the same as prod. ID?
		this.ingredients = ingredients; //new HashMap<RawMaterial, Double>();
		put("product", p);
		try {
			setIngredients(ingredients);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// equals and hashCode needed for LinkedHashMaps etc
	// TODO: think about a more proper way of implementing equals and hashCode ?
	public boolean equals(Recipe r){
		return (r.getId() == this.getId());
	}
	public int hashCode() {
		return (this.id + " " + product.getName() + product.getCode()).hashCode();
	}
	
	// description
	public String toString() {
		product = (Product) getParseObject("product");
		try {
			product.fetchIfNeeded();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (product.getCode() + " " + product.getName());
	}
	
	// Parcelable implementation
	// to pass object around in Bundles
    public int describeContents() {
      return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(id);
    	out.writeParcelable(product, flags);
    	out.writeSerializable(ingredients);
    }
    public static final Parcelable.Creator<Recipe> CREATOR
        = new Parcelable.Creator<Recipe>() {
    	public Recipe createFromParcel(Parcel in) {
    		return new Recipe(in);
      }
      public Recipe[] newArray(int size) {
        return new Recipe[size];
      }
    };
    private Recipe(Parcel in) {
    	id = in.readInt();
    	product = (Product) in.readParcelable(Product.class.getClassLoader());
    	ingredients = (LinkedHashMap<RawMaterial, Double>) in.readSerializable();
    }
	
}

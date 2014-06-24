package com.savanticab.seaweedapp.model;

import java.util.LinkedHashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
	
	private int id;
	private Product product; // --> unique key in DB
	private LinkedHashMap<RawMaterial, Double> ingredients;
	private String instructions;
	
	// getters and setters
	public String getId() {
		return String.valueOf(this.hashCode());
	}
	/*public void setId(int id) {
		this.id = id;
	}*/
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
		//this.id = product.getId();
	}
	public LinkedHashMap<RawMaterial, Double> getIngredients() {
		return ingredients;
	}
	public void setIngredients(LinkedHashMap<RawMaterial, Double> ingredients) {
		this.ingredients = ingredients;
	}
	
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	// constructors
	public Recipe(){
		ingredients = new LinkedHashMap<RawMaterial, Double>();
	}
	public Recipe(Product p, LinkedHashMap<RawMaterial, Double> ingredients, String instructions){
		this.product = p;
		//this.id = p.getId();	// is recipe ID supposed to be the same as prod. ID?
		this.ingredients = ingredients; //new HashMap<RawMaterial, Double>();
		this.instructions = instructions;
	}
	
	
	// equals and hashCode needed for LinkedHashMaps etc
	// TODO: think about a more proper way of implementing equals and hashCode ?
	public boolean equals(Recipe r){
		return (r.getId() == this.getId());
	}
	@Override
	public int hashCode() {
		return (product).hashCode();
	}
	
	// description
	@Override
	public String toString() {
		return (product.getCode() + " " + product.getName());
	}
	
	// Parcelable implementation
	// to pass object around in Bundles
    @Override
	public int describeContents() {
      return 0;
    }
    @Override
	public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(id);
    	out.writeParcelable(product, flags);
    	out.writeSerializable(ingredients);
    }
    public static final Parcelable.Creator<Recipe> CREATOR
        = new Parcelable.Creator<Recipe>() {
    	@Override
		public Recipe createFromParcel(Parcel in) {
    		return new Recipe(in);
      }
      @Override
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

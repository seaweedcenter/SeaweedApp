package com.savanticab.seaweedapp.model;

import java.util.LinkedHashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
	
	private int id;
	private Product product;
	private LinkedHashMap<RawMaterial, Double> ingredients;
	
	// getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
		this.id = product.getId();
	}
	public LinkedHashMap<RawMaterial, Double> getIngredients() {
		return ingredients;
	}
	public void setIngredients(LinkedHashMap<RawMaterial, Double> ingredients) {
		this.ingredients = ingredients;
	}

	// constructors
	public Recipe(){
		ingredients = new LinkedHashMap<RawMaterial, Double>();
	}
	public Recipe(Product p, LinkedHashMap<RawMaterial, Double> ingredients){
		this.product = p;
		this.id = p.getId();	// is recipe ID supposed to be the same as prod. ID?
		this.ingredients = ingredients; //new HashMap<RawMaterial, Double>();
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

package com.savanticab.seaweedapp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
	
	private int id;
	private Product product;
	//private RawMaterial rawMaterial;
	//private double quantity;
	private HashMap<RawMaterial, Double> ingredients;
	
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
		//this.ingredients = null;
		//this.id = product.getId();
	}

	public HashMap<RawMaterial, Double> getIngredients() {
		return ingredients;
	}

	public void setIngredients(HashMap<RawMaterial, Double> ingredients) {
		this.ingredients = ingredients;
	}

	public Recipe(){
		ingredients = new HashMap<RawMaterial, Double>();
	}
	
	public Recipe(Product p, HashMap<RawMaterial, Double> ingredients){
		this.product = p;
		this.id = p.getId();	// is recipe ID supposed to be the same as prod. ID?
		this.ingredients = ingredients; //new HashMap<RawMaterial, Double>();
	}
	
	// TODO: should implement hashCode() too...for all model classes... SHA-1 based hashcode?
	public boolean equals(Recipe r){
		return (r.getId() == this.getId());
	}
		
	public String toString() {
		return (product.getCode() + " " + product.getName());
	}


//	public Recipe(List<RawMaterial> mtrl, List<double> quantity){
//		ingredients = new HashMap<RawMaterial, Double>();
//		
//		// should check that mtrl and quantity have the same length
//		for (int i=0; i<mtrl.size(); i++) {
//			
//		}
//	}

	// Parcelable implementation
	// This was done with minimal effort
	// just to make it possible to put objects in bundle
    public int describeContents() {
      return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(id);
    	out.writeParcelable(product, flags);
    	/*for (RawMaterial mtrl: ingredients.keySet()) {
    		out.writeParcelable(mtrl, flags);
    		out.writeDouble(ingredients.get(mtrl));
    	}*/
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
    	ingredients = (HashMap<RawMaterial, Double>) in.readSerializable();
    }
	
}

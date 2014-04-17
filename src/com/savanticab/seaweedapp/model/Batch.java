package com.savanticab.seaweedapp.model;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.savanticab.seaweedapp.model.Recipe;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;

public class Batch implements Parcelable, Comparable<Batch> {

	private int id;
	private Recipe recipe;
	private int quantity;
	private Date startDate;
	private Date finishDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isFinished() {
		return (finishDate != null);
	}
	public void setIsFinished(boolean finished) {
		if (finished) {
			this.finishDate = new Date();
			
		}
		else {
			this.finishDate = null;
		}
	}
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public String toString() {
		String returnstring = "ID: "+id;
		if (recipe == null) {
			return returnstring;
		}
		if (recipe.getProduct() != null) {
			returnstring += "Code: " + recipe.getProduct().getCode();
		}
		return returnstring;
	}
	
	public Batch(Recipe recipe, int id, int quantity) {
		startDate = new Date();
		finishDate = null; // not yet finished
		this.recipe = recipe;
		this.id = id;
		this.quantity = quantity;
	}
	public Batch() {
		startDate = null;
		finishDate = null;
		recipe = null;
		id = -1;
		quantity = 0;
	}
	
	public boolean equals(Object o){
		Batch other = (Batch) o;
		return (other.getId() == this.id);
	}
	public int hashCode() {
		return (id + " " + recipe.getProduct().getName() 
				+ recipe.getProduct().getCode() + " " + startDate).hashCode();
	}
	
	// Parcelable implementation
		// This was done with minimal effort
		// just to make it possible to put objects in bundle
	    public int describeContents() {
	      return 0;
	    }
	    public void writeToParcel(Parcel out, int flags) {
	    	out.writeInt(id);
	    	out.writeParcelable(recipe, flags);
	    	out.writeInt(quantity);
	    	out.writeSerializable(startDate);
	    	out.writeSerializable(finishDate);
	    }
	    public static final Parcelable.Creator<Batch> CREATOR
	        = new Parcelable.Creator<Batch>() {
	    	public Batch createFromParcel(Parcel in) {
	    		return new Batch(in);
	      }
	      public Batch[] newArray(int size) {
	        return new Batch[size];
	      }
	    };
	    private Batch(Parcel in) {
	    	id = in.readInt();
	    	recipe = in.readParcelable(Recipe.class.getClassLoader());
	    	quantity = in.readInt();
	    	startDate = (Date)in.readSerializable();
	    	finishDate = (Date)in.readSerializable();
	    }
	    
	    // Comparator implementation
	    public int compareTo(Batch other) {
	    	return (this.getId()<other.getId() ? 1 : this.getId() > other.getId() ? 1 : 0);
	    }
	
}

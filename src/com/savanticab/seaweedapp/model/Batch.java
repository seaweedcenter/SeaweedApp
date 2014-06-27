package com.savanticab.seaweedapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import android.os.Parcel;
import android.os.Parcelable;

import com.savanticab.seaweedapp.model.Recipe;

public class Batch implements Parcelable, Comparable<Batch> {

	private String id;		
	private int batchNumber; // incremented in app as batches are created -- unique key in DB
	private Recipe recipe;
	private int quantity;	// to produce
	private Date startDate;
	private Date finishDate;	// null if job is unfinished
	private ArrayList<String> extraComments;
	private ArrayList<String> Comments;
	private ArrayList<String> Date;
	
	// getters and setters
	public String getId() {
		return String.valueOf(this.hashCode());
	}
	/*public void setId(String id) {
		this.id = id;
	}*/
	public int getBatchNumber(){
		return batchNumber;
	}
	public void setBatchNumber(int batchNumber){
		this.batchNumber = batchNumber;
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
	public ArrayList<String> getExtraComments() {
		return extraComments;
	}
	public void setExtraComments(ArrayList<String> extraCommentsDB) {
		this.extraComments = extraCommentsDB;
	}
	
	public ArrayList<String> getComments() {
		return Comments;
	}
	public void setComments(ArrayList<String> commentsDB) {
		this.Comments = commentsDB;
	}
	public ArrayList<String> getDate() {
		return Date;
	}
	public void setDate(ArrayList<String> Date) {
		this.Date = Date;
	}
	
	// controls eg. how Batch objects are represented in Spinners, ListViews etc
	@Override
	public String toString() {
		String returnstring = "ID: " + getBatchNumber();
		if (recipe == null) {
			return returnstring;
		}
		if (recipe.getProduct() != null) {
			returnstring += " Code: " + recipe.getProduct().getCode();
		}
		return returnstring;
	}
	
	// constructors
	public Batch(Recipe recipe, int batchNumber, int quantity) {
		startDate = new Date();
		finishDate = null; // not yet finished
		this.recipe = recipe;
		this.batchNumber = batchNumber;
		this.quantity = quantity;
	}
	public Batch() {
		startDate = null;
		finishDate = null;
		recipe = null;
		batchNumber = -1;
		quantity = 0;
	}
	
	// equals and hashCode needed for LinkedHashMaps elsewhere
	@Override
	public boolean equals(Object o){
		Batch other = (Batch) o;
		return (other.getId() == this.getId());
	}
	@Override
	public int hashCode() {
		return this.getBatchNumber();
	}
	
	// Parcelable implementation
	// needed to pass objects around between activities etc
    @Override
	public int describeContents() {
      return 0;
    }
    @Override
	public void writeToParcel(Parcel out, int flags) {
    	out.writeString(id);
    	out.writeParcelable(recipe, flags);
    	out.writeInt(quantity);
    	out.writeSerializable(startDate);
    	out.writeSerializable(finishDate);
    	//out.writeStringArray(extraComments);
    }
    public static final Parcelable.Creator<Batch> CREATOR
        = new Parcelable.Creator<Batch>() {
    	@Override
		public Batch createFromParcel(Parcel in) {
    		return new Batch(in);
      }
      @Override
	public Batch[] newArray(int size) {
        return new Batch[size];
      }
    };
    private Batch(Parcel in) {
    	id = in.readString();
    	recipe = in.readParcelable(Recipe.class.getClassLoader());
    	quantity = in.readInt();
    	startDate = (Date)in.readSerializable();
    	finishDate = (Date)in.readSerializable();
    	//extraComments = in.readString();
    }
    
    // Comparator implementation
    @Override
	public int compareTo(Batch other) {
    	return (this.getBatchNumber()<other.getBatchNumber() ? -1 : this.getBatchNumber() > other.getBatchNumber() ? 1 : 0);
    }
	
}

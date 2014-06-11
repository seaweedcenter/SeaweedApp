package com.savanticab.seaweedapp.model;

import java.util.Date;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.savanticab.seaweedapp.model.Recipe;

@ParseClassName("Batch")
public class Batch extends ParseObject implements Parcelable, Comparable<Batch> {

	private int id;		// incremented in app as batches are created
	private Recipe recipe;
	private int quantity;	// to produce
	private Date startDate;
	private Date finishDate;	// null if job is unfinished
	
	// getters and setters
	public int getId() {
		id = getInt("batchId");
		return id;
		//return id;
	}
	public void setId(int id) {
		this.id = id;
		put("batchId", id);
	}
	public boolean isFinished() {
		//return (finishDate != null);
		return (getDate("finishDate") != JSONObject.NULL);
	}
	public void setIsFinished(boolean finished) {
		if (finished) {
			this.finishDate = new Date();
			put("finishDate", new Date());
		}
		else {
			this.finishDate = null;
			put("finishDate", null);
		}
	}
	public Recipe getRecipe() {
		//return recipe;
		recipe = (Recipe) getParseObject("recipe");
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
		put("recipe", recipe);
	}
	public int getQuantity(){
		//return quantity;
		quantity = getInt("quantity");
		return quantity;
	}
	public void setQuantity(int quantity){
		this.quantity = quantity;
		put("quantity", quantity);
	}
	public Date getStartDate() {
		//return startDate;
		startDate = getDate("startDate");
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		put("startDate", startDate);
	}
	public Date getFinishDate() {
		//return finishDate;
		this.finishDate = getDate("finishDate");
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
		put("finishDate", finishDate);
	}
	
	// controls eg. how Batch objects are represented in Spinners, ListViews etc
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
	
	// constructors
	public Batch(Recipe recipe, int id, int quantity) {
		startDate = new Date();
		finishDate = null; // not yet finished
		this.recipe = recipe;
		this.id = id;
		this.quantity = quantity;
		put("startDate", new Date());
		put("finishDate", JSONObject.NULL);
		put("recipe", recipe);
		put("batchId", id);
		put("quanitity", quantity);
	}
	public Batch() {
		/*startDate = null;
		finishDate = null;
		recipe = null;
		id = -1;
		quantity = 0;
		put("startDate", JSONObject.NULL);
		put("finishDate", JSONObject.NULL);
		put("recipe", JSONObject.NULL);
		put("batchId", -1);
		put("quanitity", 0);*/
	}
	
	// equals and hashCode needed for LinkedHashMaps elsewhere
	public boolean equals(Object o){
		Batch other = (Batch) o;
		return (other.getId() == this.id);
	}
	public int hashCode() {
		return (id + " " + recipe.getProduct().getName() 
				+ recipe.getProduct().getCode() + " " + startDate).hashCode();
	}
	
	// Parcelable implementation
	// needed to pass objects around between activities etc
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

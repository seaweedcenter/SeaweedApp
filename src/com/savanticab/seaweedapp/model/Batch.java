package com.savanticab.seaweedapp.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.savanticab.seaweedapp.SeaweedApplication;
import com.savanticab.seaweedapp.model.Recipe;

//@ParseClassName("Batch")
public class Batch implements Parcelable, Comparable<Batch> {//extends ParseObject 

	public static final String TABLE_NAME = "batches";
	public static final String ID = "id";
	public static final String BATCH_NUMBER = "batch_number";
	public static final String RECIPE_ID = "recipe_id";
	public static final String QUANTITY = "quantity";
	public static final String START_DATE = "start_date";
	public static final String FINISH_DATE = "finish_date";
	public static final String IS_FINISHED = "is_finished";
	
	private String id;		
	private int batchNumber; // incremented in app as batches are created
	private Recipe recipe;
	private String recipeId;
	private int quantity;	// to produce
	private Date startDate;
	private Date finishDate;	// null if job is unfinished
	private Boolean isFinished;
	
	private DbxRecord mRecord;
	
	// getters and setters
	public String getId() {
		return id;
	}
	/*public void setId(int id) {
		this.id = id;
		put("batchId", id);
	}*/
	
	public int getBatchNumber() {
		return batchNumber;
	}
	public Recipe getRecipe() {
		if(null == recipe) {
			DbxDatastore store = null;
			try {
				store = DbxDatastore.openDefault(SeaweedApplication.getDefaultAccount());				
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally  {
				DbxTable recipeTbl = store.getTable(Recipe.TABLE_NAME);
				DbxRecord firstResult;
				try {
					firstResult = recipeTbl.get(this.recipeId);
					recipe = new Recipe(firstResult);
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				store.close();
			}
		}
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
		this.recipeId = recipe.getId();
	}
	
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int quantity){
		this.quantity = quantity;
		//put("quantity", quantity);
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
	
	// controls eg. how Batch objects are represented in Spinners, ListViews etc
	public String toString() {
		String returnstring = "ID: " + batchNumber;
		/*if (recipe == null) {
			return returnstring;
		}
		if (recipe.getProduct() != null) {
			returnstring += "Code: " + recipe.getProduct().getCode();
		}*/
		return returnstring;
	}
	
	// constructors
	public Batch(Recipe recipe, int batchNumber, int quantity) {
		this.batchNumber = batchNumber;
		this.recipe = recipe;
		this.quantity = quantity;
		startDate = new Date();
		finishDate = null; // not yet finished
		isFinished = false;
	}
	public Batch(DbxRecord record) {
		this.mRecord = record;
		
		this.batchNumber = (int) record.getLong(BATCH_NUMBER);
		this.recipeId = record.getString(RECIPE_ID);
		this.recipe = null;
		this.quantity = (int) record.getLong(QUANTITY);
		this.startDate = record.getDate(START_DATE);
		this.isFinished = record.getBoolean(IS_FINISHED);
		this.finishDate = isFinished ? record.getDate(FINISH_DATE) : null;
		
		this.id = record.getId();
	}
	
	public void save(DbxDatastore store) {
		if (null == mRecord){
			mRecord = store.getTable(TABLE_NAME).insert();
			id = mRecord.getId();
		}
		mRecord.set(BATCH_NUMBER, this.batchNumber);
		mRecord.set(RECIPE_ID, this.recipe.getId());
		mRecord.set(QUANTITY, this.quantity);
		mRecord.set(START_DATE, this.startDate);
		mRecord.set(IS_FINISHED, this.isFinished);
		if (isFinished) {
			mRecord.set(FINISH_DATE, this.finishDate);
		}
		try {
			store.sync();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// equals and hashCode needed for LinkedHashMaps elsewhere
	public boolean equals(Object o){
		Batch other = (Batch) o;
		return (other.getBatchNumber() == this.getBatchNumber());
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
    	out.writeString(id);
    	out.writeInt(batchNumber);
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
    	id = in.readString();
    	batchNumber = in.readInt();
    	recipe = in.readParcelable(Recipe.class.getClassLoader());
    	quantity = in.readInt();
    	startDate = (Date)in.readSerializable();
    	finishDate = (Date)in.readSerializable();
    }
    
    // Comparator implementation
    public int compareTo(Batch other) {
    	return (this.getBatchNumber()<other.getBatchNumber() ? -1 : this.getBatchNumber() > other.getBatchNumber() ? 1 : 0);
    }
	
}

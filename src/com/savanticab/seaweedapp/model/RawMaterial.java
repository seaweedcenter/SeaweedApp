package com.savanticab.seaweedapp.model;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxRecord;

import android.os.Parcel;
import android.os.Parcelable;

public class RawMaterial implements Parcelable { //extends ParseObject  {
	
	public static final String TABLE_NAME = "materials";
	public static final String NAME = "name";
	public static final String UNIT = "unit";
	public static final String STOCK = "stock";
	public static final String ORDERED = "ordered";
	public static final String RESERVED = "reserved";
	
	
	private String id;	// id in dropbox
	private String name;
	private String unit;
	private String icon;
	private double stock;
	private double ordered;
	private double reserved;
	
	private DbxRecord mRecord;
	
	// getters and setters
	public String getId() {
		return id;
	}
	/*public void setId(int id) {
		this.id = id;
		//put("id", id);
	}*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public double getOrdered() {
		return ordered;
	}

	public void setOrdered(double ordered) {
		this.ordered = ordered;
	}

	public double getReserved() {
		return reserved;
	}

	public void setReserved(double reserved) {
		this.reserved = reserved;
	}
	// describes object in Spinners, ListViews etc
	public String toString() {
		return getName();
	}
	
	public RawMaterial(){}
	public RawMaterial(String name, String unit, String icon, Double stock, Double ordered, Double reserved){
		this.name = name;
		this.unit = unit;
		this.icon = icon;
		this.stock = stock;
		this.ordered = ordered;
		this.reserved = reserved;
	}
	public RawMaterial(DbxRecord record) {
		this.mRecord = record;
		
		this.name = record.getString(RawMaterial.NAME);
		this.unit = record.getString(RawMaterial.UNIT);
		this.ordered = record.getDouble(RawMaterial.ORDERED);
		this.stock = record.getDouble(RawMaterial.STOCK);
		this.reserved = record.getDouble(RawMaterial.RESERVED);
		this.id = record.getId();
	}
	
	public void save(DbxDatastore store) {
		if (null == mRecord){
			mRecord = store.getTable(TABLE_NAME).insert();
		}
		mRecord.set(UNIT, this.unit);
		mRecord.set(ORDERED, this.ordered);
		mRecord.set(STOCK, this.stock);
		mRecord.set(RESERVED, this.reserved);
		try {
			store.sync();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// equals needed for List.contains()
	// equals and hashCode needed for LinkedHashMap
	public boolean equals(Object other) {
		RawMaterial m = (RawMaterial)other;
		return (m.getName().equals(getName()));
	}
	public int hashCode() {
		return getName().hashCode();
	}
	
    // Parcelable implementation
	// for passing object in Bundle between activities etc
    public int describeContents() {
      return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
    	out.writeString(id);
    	out.writeString(name);
    	out.writeString(unit);
    	out.writeString(icon);
    	out.writeDouble(stock);
    	out.writeDouble(ordered);
    	out.writeDouble(reserved);
    }
    public static final Parcelable.Creator<RawMaterial> CREATOR
        = new Parcelable.Creator<RawMaterial>() {
    	public RawMaterial createFromParcel(Parcel in) {
    		return new RawMaterial(in);
      }
      public RawMaterial[] newArray(int size) {
        return new RawMaterial[size];
      }
    };
    private RawMaterial(Parcel in) {
    	id = in.readString();
    	name = in.readString();
    	unit = in.readString();
    	icon = in.readString();
    	stock = in.readDouble();
    	ordered = in.readDouble();
    	reserved = in.readDouble();
    }

}

package com.savanticab.seaweedapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RawMaterial implements Parcelable {
	
	private int id;	// id in SQL table and in app, hashed from name
	private String name;
	private String unit;
	private String icon;
	
	// getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.id = name.hashCode();
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
	
	// describes object in Spinners, ListViews etc
	public String toString() {
		return this.name;
	}
	
	public RawMaterial(){}
	public RawMaterial(String name, String unit, String icon){
		this.id = name.hashCode();
		this.name = name;
		this.unit = unit;
		this.icon = icon;
	}
	
	// equals needed for List.contains()
	// equals and hashCode needed for LinkedHashMap
	public boolean equals(Object other) {
		RawMaterial m = (RawMaterial)other;
		boolean result = (m.getName().equals(this.name));
		return (m.getName().equals(this.name));
	}
	public int hashCode() {
		return this.name.hashCode();
	}
	
    // Parcelable implementation
	// for passing object in Bundle between activities etc
    public int describeContents() {
      return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(id);
    	out.writeString(name);
    	out.writeString(unit);
    	out.writeString(icon);
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
    	id = in.readInt();
    	name = in.readString();
    	unit = in.readString();
    	icon = in.readString();
    }

}

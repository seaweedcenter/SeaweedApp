package com.savanticab.seaweedapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RawMaterial implements Parcelable {
	
	private int id;
	private String name;
	private String unit;
	private double stockQuantity;
	private double orderedQuantity;
	private String icon;
	
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
	public double getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(double stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public double getOrderedQuantity() {
		return orderedQuantity;
	}
	public void setOrderedQuantity(double orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String toString() {
		return this.name;
	}
	
	public RawMaterial(){}
	public RawMaterial(String name, String unit, double stockQuantity, double orderedQuantity){
		this.id = name.hashCode();
		this.name = name;
		this.unit = unit;
		this.stockQuantity = stockQuantity;
		this.orderedQuantity = orderedQuantity;
	}
	
	// needed for List.contains()
	public boolean equals(Object other) {
		RawMaterial m = (RawMaterial)other;
		boolean result = (m.getName().equals(this.name));
		return (m.getName().equals(this.name));
	}
	
    // Parcelable implementation
	// This was done with minimal effort
    public int describeContents() {
      return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
      out.writeString(name);
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
      name = in.readString();
    }

}

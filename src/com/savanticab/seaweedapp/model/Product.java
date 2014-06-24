package com.savanticab.seaweedapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
	
	private int id;	// id in SQL table and in app, hashed from code member
	private String code; // --> unique key in DB
	private String name;
	private String fragance;
	private String size;
	private Double price;
	
	// getters and setters...
	public String getId() {
		return String.valueOf(this.hashCode());
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
		this.id = code.hashCode();
	}
	public Double getPrice(){
		return price;
	}
	public void setPrice(Double p) {
		this.price = p;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFragance() {
		return fragance;
	}
	public void setFragance(String fragance) {
		this.fragance = fragance;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	/*public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}*/
	
	// equals and hashcode needed for proper LinkedHashMap compatibility
	@Override
	public boolean equals(Object other) {
		Product p = (Product)other;
		if (!p.getCode().equals(this.code)){ return false; }
		if (!p.getName().equals(this.name)){ return false; }
		if (!p.getFragance().equals(this.fragance)){ return false; }
		if (!p.getSize().equals(this.size)) { return false; }
		return true;
	}
	@Override
	public int hashCode() {
		return (this.code).hashCode();
	}
	
	public Product(){}
	public Product(String code, String name, String fragance, String size, 
			Double price){
		this.id = code.hashCode();//id;
		this.code = code;
		this.name = name;
		this.fragance = fragance;
		this.size = size;
		this.price = price;
	}
	
	
	// Parcelable implementation
	// needed when passing Products around in Bundles for example
    @Override
	public int describeContents() {
      return 0;
    }
    @Override
	public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(id);
    	out.writeString(code);
    	out.writeString(name);
    	out.writeString(fragance);
    	out.writeString(size);
    	out.writeDouble(price);
    }
    public static final Parcelable.Creator<Product> CREATOR
        = new Parcelable.Creator<Product>() {
    	@Override
		public Product createFromParcel(Parcel in) {
    		return new Product(in);
      }
      @Override
	public Product[] newArray(int size) {
        return new Product[size];
      }
    };
    private Product(Parcel in) {
    	id = in.readInt();
    	code = in.readString();
    	name = in.readString();
    	fragance = in.readString();
    	size = in.readString();
    	price = in.readDouble();
    }
	
}

package com.savanticab.seaweedapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
	
	private int id;
	private String code;
	private String name;
	private String fragance;
	private String size;
	private Double price;
	private int inStockQty;
	private int inProductionQty;
	
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
	public int getInStockQty(){
		return inStockQty;
	}
	public void setInStockQty(int inStockQty){
		this.inStockQty = inStockQty;
	}
	public int getInProductionQty() {
		return inProductionQty;
	}
	public void setInProductionQty(int inProductionQty){
		this.inProductionQty = inProductionQty;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Product(){}
	public Product(String code, String name, String fragance, String size, 
			Double price, int inStockQty, int inProductionQty){
		this.id = code.hashCode();//id;
		this.code = code;
		this.name = name;
		this.fragance = fragance;
		this.size = size;
		this.price = price;
		this.inStockQty = inStockQty;
		this.inProductionQty = inProductionQty;
	}

	
	// Parcelable implementation
	// This was done with minimal effort
	// just to make it possible to put objects in bundle
    public int describeContents() {
      return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(id);
    	out.writeString(code);
    	out.writeString(name);
    	out.writeString(fragance);
    	out.writeString(size);
    	out.writeDouble(price);
    	out.writeInt(inStockQty);
    	out.writeInt(inProductionQty);
    }
    public static final Parcelable.Creator<Product> CREATOR
        = new Parcelable.Creator<Product>() {
    	public Product createFromParcel(Parcel in) {
    		return new Product(in);
      }
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
    	inStockQty = in.readInt();
    	inProductionQty = in.readInt();
    }
	
}

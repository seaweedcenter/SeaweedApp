package com.savanticab.seaweedapp.model;

import com.dropbox.sync.android.DbxRecord;

import android.os.Parcel;
import android.os.Parcelable;

//@ParseClassName("Product")
public class Product implements Parcelable { //extends ParseObject 
	

	public static final String TABLE_NAME = "products";
	public static final String ID = "id";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String FRAGANCE = "fragance";
	public static final String SIZE = "size";
	public static final String PRICE = "price";
	public static final String STOCK = "stock";
	public static final String IN_PRODUCTION = "in_production";
	
	private String id;	// id in dropbox
	private String code;
	private String name;
	private String fragance;
	private String size;
	private Double price;
	private int stock;
	private int inProduction;
	
	private DbxRecord mRecord;
	
	// getters and setters...
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getId() {
		return id;
	}
	/*public void setId(int id) {
		this.id = id;
		put("id", id);
	}*/
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getInproduction() {
		return inProduction;
	}
	public void setInproduction(int inproduction) {
		this.inProduction = inproduction;
	}
	
	// equals and hashcode needed for proper LinkedHashMap compatibility
	public boolean equals(Object other) {
		Product p = (Product)other;
		if (!p.getCode().equals(this.code)){ return false; }
		if (!p.getName().equals(this.name)){ return false; }
		if (!p.getFragance().equals(this.fragance)){ return false; }
		if (!p.getSize().equals(this.size)) { return false; }
		return true;
	}
	public int hashCode() {
		return (this.name + this.code + this.fragance + this.size).hashCode();
	}
	
	public Product(){}
	public Product(String code, String name, String fragance, String size, 
			Double price){
		this.code = code;
		this.name = name;
		this.fragance = fragance;
		this.size = size;
		this.price = price;
	}

	public Product(DbxRecord record) {
		this.mRecord = record;
		
		this.code = record.getString(CODE);
		this.name = record.getString(NAME);
		this.fragance = record.getString(FRAGANCE);
		this.size = record.getString(SIZE);
		this.price = record.getDouble(PRICE);

		this.id = record.getId();
	}
	
	
	// Parcelable implementation
	// needed when passing Products around in Bundles for example
    public int describeContents() {
      return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
    	out.writeString(id);
    	out.writeString(code);
    	out.writeString(name);
    	out.writeString(fragance);
    	out.writeString(size);
    	out.writeDouble(price);
    	out.writeInt(stock);
    	out.writeInt(inProduction);
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
    	id = in.readString();
    	code = in.readString();
    	name = in.readString();
    	fragance = in.readString();
    	size = in.readString();
    	price = in.readDouble();
    	stock = in.readInt();
    	inProduction = in.readInt();
    }
	
}

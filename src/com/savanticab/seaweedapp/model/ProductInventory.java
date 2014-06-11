package com.savanticab.seaweedapp.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ProductInventory")
public class ProductInventory extends ParseObject {
	private Product product;
	private int stock;
	private int inproduction;
	
	public Product getProduct() {
		//return product;
		return (Product) getParseObject("product");
	}
	public void setProduct(Product product) {
		this.product = product;
		put("product", product);
	}
	public int getStock() {
		//return stock;
		return getInt("stock");
	}
	public void setStock(int stock) {
		this.stock = stock;
		put("stock", stock);
	}
	public int getInproduction() {
		//return inproduction;
		return getInt("inproduction");
	}
	public void setInproduction(int inproduction) {
		this.inproduction = inproduction;
		put("inproduction", inproduction);
	}
	
	public ProductInventory() {}
	public ProductInventory(Product product, int stock, int inproduction) {
		this.product = product;
		this.stock = stock;
		this.inproduction = inproduction;
		put("product", product);
		put("stock", stock);
		put("inproduction", inproduction);
	}
	
}
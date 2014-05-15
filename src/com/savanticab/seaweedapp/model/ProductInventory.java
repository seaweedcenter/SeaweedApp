package com.savanticab.seaweedapp.model;

public class ProductInventory {
	private Product product;
	private int stock;
	private int inproduction;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getInproduction() {
		return inproduction;
	}
	public void setInproduction(int inproduction) {
		this.inproduction = inproduction;
	}
	
	public ProductInventory() {}
	public ProductInventory(Product product, int stock, int inproduction) {
		this.product = product;
		this.stock = stock;
		this.inproduction = inproduction;
	}
	
}
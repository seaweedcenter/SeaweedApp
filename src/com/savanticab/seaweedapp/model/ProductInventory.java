package com.savanticab.seaweedapp.model;

public class ProductInventory {
	private Product product; // --> unique key in DB
	private int stock;
	private int inproduction;
	
	public String getId() {
		return String.valueOf(this.hashCode());
	}
	
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
	
	@Override
	public int hashCode() {
		return this.product.hashCode();
	}
	
}
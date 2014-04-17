package com.savanticab.seaweedapp.model;

//minimalist helper class for use in Inventory
public class ProductInventory {
	public int stock;
	public int inproduction;
	
	public ProductInventory() {
		stock = 0;
		inproduction = 0;
	}
	public ProductInventory(int stock, int inproduction) {
		this.stock = stock;
		this.inproduction = inproduction;
	}
}
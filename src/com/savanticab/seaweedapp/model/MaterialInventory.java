package com.savanticab.seaweedapp.model;

public class MaterialInventory {
	public double stock;
	public double ordered;
	public double reserved;
	
	public MaterialInventory() {
		stock = 0.0;
		ordered = 0.0;
		reserved = 0.0;
	}
	
	public MaterialInventory(double stock, double ordered, double reserved) {
		this.stock = stock;
		this.ordered = ordered;
		this.reserved = reserved;
	}
}
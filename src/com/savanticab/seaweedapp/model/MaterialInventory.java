package com.savanticab.seaweedapp.model;


public class MaterialInventory {//extends ParseObject{
	private RawMaterial material;
	private double stock;
	private double ordered;
	private double reserved;
	
	
	public RawMaterial getMaterial() {
		return material;
	}

	public void setMaterial(RawMaterial material) {
		this.material = material;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public double getOrdered() {
		return ordered;
	}

	public void setOrdered(double ordered) {
		this.ordered = ordered;
	}

	public double getReserved() {
		return reserved;
	}

	public void setReserved(double reserved) {
		this.reserved = reserved;
	}

	public MaterialInventory() {}
	
	public MaterialInventory(RawMaterial material, double stock, double ordered, double reserved) {
		this.material = material;
		this.stock = stock;
		this.ordered = ordered;
		this.reserved = reserved;
	}
}
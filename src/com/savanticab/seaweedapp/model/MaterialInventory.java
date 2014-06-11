package com.savanticab.seaweedapp.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("MaterialInventory")
public class MaterialInventory extends ParseObject{
	private RawMaterial material;
	private double stock;
	private double ordered;
	private double reserved;
	
	
	public RawMaterial getMaterial() {
		//return material;
		return (RawMaterial) getParseObject("material");
	}

	public void setMaterial(RawMaterial material) {
		this.material = material;
		put("material", material);
	}

	public double getStock() {
		//return stock;
		return getDouble("stock");
	}

	public void setStock(double stock) {
		this.stock = stock;
		put("stock", stock);
	}

	public double getOrdered() {
		//return ordered;
		return getDouble("ordered");
	}

	public void setOrdered(double ordered) {
		this.ordered = ordered;
		put("ordered", ordered);
	}

	public double getReserved() {
		//return reserved;
		return getDouble("reserved");
	}

	public void setReserved(double reserved) {
		this.reserved = reserved;
		put("reserved", reserved);
	}

	public MaterialInventory() {}
	
	public MaterialInventory(RawMaterial material, double stock, double ordered, double reserved) {
		this.material = material;
		this.stock = stock;
		this.ordered = ordered;
		this.reserved = reserved;
		put("material", material);
		put("stock", stock);
		put("ordered", ordered);
		put("reserved", reserved);
	}
}
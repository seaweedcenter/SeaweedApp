package com.savanticab.seaweedapp.model;

public class RawMaterial {
	
	private int id;
	private String name;
	private String unit;
	private double stockQuantity;
	private double orderedQuantity;
	private String icon;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(double stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public double getOrderedQuantity() {
		return orderedQuantity;
	}
	public void setOrderedQuantity(double orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public RawMaterial(){}
	public RawMaterial(String name, String unit, double stockQuantity, double orderedQuantity){
		this.name = name;
		this.unit = unit;
		this.stockQuantity = stockQuantity;
		this.orderedQuantity = orderedQuantity;
	}

}

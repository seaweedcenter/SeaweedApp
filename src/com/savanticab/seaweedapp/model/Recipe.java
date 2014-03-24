package com.savanticab.seaweedapp.model;

import java.util.HashMap;
import java.util.Map;

public class Recipe {
	
	private int id;
	private Product product;
	//private RawMaterial rawMaterial;
	//private double quantity;
	private Map<RawMaterial, Double> ingredients;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Map<RawMaterial, Double> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Map<RawMaterial, Double> ingredients) {
		this.ingredients = ingredients;
	}

	public Recipe(){
		ingredients = new HashMap<RawMaterial, Double>();
	}

}

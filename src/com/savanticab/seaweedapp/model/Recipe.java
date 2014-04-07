package com.savanticab.seaweedapp.model;

import java.util.HashMap;
import java.util.List;
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
	
	public Recipe(Product p, Map<RawMaterial, Double> ingredients){
		this.product = p;
		this.id = p.getId();	// is recipe ID supposed to be the same as prod. ID?
		this.ingredients = ingredients; //new HashMap<RawMaterial, Double>();
	}
	
	public boolean equals(Recipe r){
		return (r.getId() == this.getId());
	}
	
//	public Recipe(List<RawMaterial> mtrl, List<double> quantity){
//		ingredients = new HashMap<RawMaterial, Double>();
//		
//		// should check that mtrl and quantity have the same length
//		for (int i=0; i<mtrl.size(); i++) {
//			
//		}
//	}

}

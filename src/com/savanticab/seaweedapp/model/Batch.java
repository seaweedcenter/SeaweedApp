package com.savanticab.seaweedapp.model;

import java.util.Date;

import com.savanticab.seaweedapp.model.Recipe;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;

public class Batch {

	private int id;
	private boolean isFinished;
	private Recipe recipe;
	private int quantity;
	private Date startDate;
	private Date finishDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isFinished() {
		return isFinished;
	}
	public void setIsFinished(boolean finished) {
		this.isFinished = finished;
		if (!finished) {
			this.finishDate = null;
		}
		else {
			this.finishDate = new Date();
		}
	}
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	
	public void Batch(Recipe recipe, int id, int quantity) {
		startDate = new Date();
		finishDate = null; // not yet finished
		isFinished = false; // redundant?
		this.recipe = recipe;
		this.id = id;
		this.quantity = quantity;
	}
	
}

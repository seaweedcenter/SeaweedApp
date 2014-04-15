package com.savanticab.seaweedapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// TODO: think about handling cases such as
// TODO: - material/product not found in Map

public class Inventory {
	
	private Map<RawMaterial, MaterialInventory> materials;
	private Map<Product, ProductInventory> products;
	
	// constructors..
	public Inventory() {
		materials = new HashMap<RawMaterial, MaterialInventory>();
		products = new HashMap<Product, ProductInventory>();
	}
	public Inventory(Map<RawMaterial, MaterialInventory> materials, 
			Map<Product, ProductInventory> products) {
		this.materials = materials;
		this.products = products;
	}
	
	// getters 'n setters...
	public Map<RawMaterial, MaterialInventory> getMaterialInventory(){
		return materials;
	}
	public void setMaterialInventory(Map<RawMaterial, MaterialInventory> materials){
		this.materials = materials;
	}
	public Map<Product, ProductInventory> getProductInventory(){
		return products;
	}
	public void setProductInventory(Map<Product, ProductInventory> products)  {
		this.products = products;
	}
	
	// access inventory numbers
	public double getMtrlStock(RawMaterial mtrl) {
		return materials.get(mtrl).stock;
	}
	public double getMtrlOrdered(RawMaterial mtrl) {
		return materials.get(mtrl).ordered;
	}
	public double getMtrlReserved(RawMaterial mtrl) {
		return materials.get(mtrl).reserved;
	}
	public int getProductStock(Product prod) {
		return products.get(prod).stock;
	}
	public int getProductInproduction(Product prod) {
		return products.get(prod).inproduction;
	}
	
	// set inventory numbers
	public void setMtrlStock(RawMaterial mtrl, double stock){
		MaterialInventory inv = materials.get(mtrl);
		inv.stock = stock;
		materials.put(mtrl, inv);
	}
	public void setMtrlOrdered(RawMaterial mtrl, double ordered){
		MaterialInventory inv = materials.get(mtrl);
		inv.ordered = ordered;
		materials.put(mtrl, inv);
	}
	public void setMtrlReserved(RawMaterial mtrl, double reserved){
		MaterialInventory inv = materials.get(mtrl);
		inv.reserved = reserved;
		materials.put(mtrl, inv);
	}
	public void setProductStock(Product p, int stock){
		ProductInventory inv = products.get(p);
		inv.stock = stock;
		products.put(p, inv);
	}
	public void setProductInproduction(Product p, int inproduction){
		ProductInventory inv = products.get(p);
		inv.inproduction = inproduction;
		products.put(p, inv);
	}
	public void setMaterialQuantities(RawMaterial mtrl, double stock,
			double ordered, double reserved) {
		MaterialInventory inv = new MaterialInventory();
		inv.stock = stock;
		inv.ordered = ordered;
		inv.reserved = reserved;
		materials.put(mtrl, inv);
	}
	public void setProductQuantities(Product product, int stock,
			int inproduction) {
		ProductInventory inv = new ProductInventory();
		inv.stock = stock;
		inv.inproduction = inproduction;
		products.put(product, inv);
	}
	
	// add and delete materials
	public void addMaterial(RawMaterial mtrl, double stock, 
			double ordered, double reserved) {
		setMaterialQuantities(mtrl, stock, ordered, reserved); // should create new entry if does not exist
	}
	public void addProduct(Product product, int stock, int inproduction) {
		setProductQuantities(product, stock, inproduction); // should create new entry if does not exist
	}
	public void deleteMaterial(RawMaterial mtrl) {
		materials.remove(mtrl);
	}
	public void deleteProduct(Product product) {
		products.remove(product);
	}
	
	// material transactions
	public void MtrlReserve(RawMaterial mtrl, int qty) {
		MaterialInventory inv = materials.get(mtrl); // TODO: handle when object not found?
		inv.stock -= qty;
		inv.reserved += qty;
		materials.put(mtrl, inv);
	}
	public void MtrlUnreserve(RawMaterial mtrl, int qty) {	// use if batch is cancelled
		MaterialInventory inv = materials.get(mtrl);
		inv.stock += qty;
		inv.reserved -= qty;
		materials.put(mtrl, inv);
	}
	public void MtrlReservedUsed(RawMaterial mtrl, int qty) {
		MaterialInventory inv = materials.get(mtrl);
		inv.reserved -= qty;
		materials.put(mtrl, inv);
	}
	
	// product transactions
	public void ProductProductionFinish(Product product, int qty) {
		ProductInventory inv = products.get(product);
		inv.stock += qty;
		inv.inproduction -= qty;
		products.put(product, inv);
	}
}


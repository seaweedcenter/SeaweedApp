package com.savanticab.seaweedapp.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


// Inventory class holds quantities of RawMaterials and Products
// typically read from database and created by MySQLiteHelper "helper.getInventory()" when needed
// 

// TODO: think about handling cases such as
// TODO: - material/product not found in Map

public class Inventory {
	
	// LinkedHashMap: makes life easier as it iterates over objects in consistent way every time
	private LinkedHashMap<RawMaterial, MaterialInventory> materials;
	private LinkedHashMap<Product, ProductInventory> products;
	
	// constructors..
	public Inventory() {
		materials = new LinkedHashMap<RawMaterial, MaterialInventory>();
		products = new LinkedHashMap<Product, ProductInventory>();
	}
	public Inventory(LinkedHashMap<RawMaterial, MaterialInventory> materials, 
			LinkedHashMap<Product, ProductInventory> products) {
		this.materials = materials;
		this.products = products;
	}
	
	// getters 'n setters...
	public Map<RawMaterial, MaterialInventory> getMaterialInventory(){
		return materials;
	}
	public List<RawMaterial> getMaterialList() {
		List <RawMaterial> materialList = new LinkedList();
		for (Entry<RawMaterial, MaterialInventory> entry : materials.entrySet()) {
			materialList.add(entry.getKey());
		}
		return materialList;
	}
	public void setMaterialInventory(LinkedHashMap<RawMaterial, MaterialInventory> materials){
		this.materials = materials;
	}
	public Map<Product, ProductInventory> getProductInventory(){
		return products;
	}
	public LinkedList<Product> getProductList() {
		LinkedList <Product> productList = new LinkedList();
		for (Entry<Product, ProductInventory> entry : products.entrySet()) {
			productList.add(entry.getKey());
		}
		return productList;
	}
	public void setProductInventory(LinkedHashMap<Product, ProductInventory> products)  {
		this.products = products;
	}
	
	// access inventory numbers
	public double getMtrlStock(RawMaterial mtrl) {
		MaterialInventory t = materials.get(mtrl);
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
	
	// add and delete materials and products
	public void addMaterial(RawMaterial mtrl, double stock, 
			double ordered, double reserved) {
		setMaterialQuantities(mtrl, stock, ordered, reserved); // should create new entry if does not exist
	}
	public void addAllMaterials(LinkedHashMap<RawMaterial, MaterialInventory> in) {
		materials.putAll(in);
	}
	public void addProduct(Product product, int stock, int inproduction) {
		setProductQuantities(product, stock, inproduction); // should create new entry if does not exist
	}
	public void addAllProducts(LinkedHashMap<Product, ProductInventory> in) {
		products.putAll(in);
	}
	public void deleteMaterial(RawMaterial mtrl) {
		materials.remove(mtrl);
	}
	public void deleteProduct(Product product) {
		products.remove(product);
	}
	
	// material transactions
	// TODO: handle when object not found?
	public void MtrlReserve(RawMaterial mtrl, double qty) { // when batch is created
		MaterialInventory inv = materials.get(mtrl); 
		inv.stock -= qty;
		inv.reserved += qty;
		materials.put(mtrl, inv);
	}
	public void MtrlUnreserve(RawMaterial mtrl, double qty) {	// use if batch is cancelled
		MaterialInventory inv = materials.get(mtrl);
		inv.stock += qty;
		inv.reserved -= qty;
		materials.put(mtrl, inv);
	}
	public void MtrlReservedFinished(RawMaterial mtrl, double qty) { // when batch job is finished
		MaterialInventory inv = materials.get(mtrl);
		inv.reserved -= qty;
		materials.put(mtrl, inv);
	}
	
	// product transactions
	public void ProductProductionFinish(Product product, Batch batch) {
		int productQty = batch.getQuantity();
		ProductInventory inv = products.get(product);
		inv.stock += productQty;
		inv.inproduction -= productQty;
		products.put(product, inv);
		
		// unreserve materials that were allocated for production
		LinkedHashMap<RawMaterial, Double> ingredients = batch.getRecipe().getIngredients();
		for (Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
			RawMaterial mtrl =  entry.getKey();
			this.MtrlReservedFinished(mtrl, entry.getValue()*productQty);
		}
	}
	
	// check for "contains"
	public boolean contains(RawMaterial material) {
		return materials.containsKey(material);
	}
	public boolean contains(Product product) {
		return products.containsKey(product);
	}
}


package com.savanticab.seaweedapp;
import java.util.LinkedHashMap;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.MaterialInventory;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.ProductInventory;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

public class SeaweedApplication extends Application {
	public void onCreate() {
		//Parse.enableLocalDatastore(this);
		ParseObject.registerSubclass(Batch.class);
		ParseObject.registerSubclass(MaterialInventory.class);
		ParseObject.registerSubclass(Product.class);
		ParseObject.registerSubclass(ProductInventory.class);
		ParseObject.registerSubclass(RawMaterial.class);
		ParseObject.registerSubclass(Recipe.class);
		Parse.initialize(this, "wFMV1pPE7lhSoYIUvNyD05WrcvmTUqJ3BNWM6Zgv", "ux0Q6RgMFGSL5o8wzgTo6EnVM133xXvKjMAw8u2I");
		/*RawMaterial material1 = new RawMaterial("Coconut oil", "L", "path1");
        material1.saveEventually();
        RawMaterial material2 = new RawMaterial("Seaweed", "Kg", "path2");
        material2.saveEventually();
        RawMaterial material3 = new RawMaterial("Bee wax", "Kg", "path3");
        material3.saveEventually();
        
		Product product1 = new Product("SOAP1", "Soap", "Lime", "Big", 10.0);
		product1.saveEventually();
        Product product2 = new Product("SOAP2", "Soap", "Clove", "Small", 6.0);
        product2.saveEventually();
        Product product3 = new Product("SOAP3", "Soap", "Langi-langi", "Big", 14.0);
        product3.saveEventually();
        Product product4 = new Product("SOAP4", "Soap", "Lemongrass", "Medium", 16.0);
        product4.saveEventually();
        
        ProductInventory productInv1 = new ProductInventory(product1, 200, 0);
        productInv1.saveEventually();
        ProductInventory productInv2 = new ProductInventory(product2, 100, 0);
        productInv2.saveEventually();
        ProductInventory productInv3 = new ProductInventory(product3, 300, 0);
        productInv3.saveEventually();
        ProductInventory productInv4 = new ProductInventory(product4, 150, 0);
        productInv4.saveEventually();
        
        MaterialInventory matInv1 = new MaterialInventory(material1, 100, 0, 0.0);
        matInv1.saveEventually();
        MaterialInventory matInv2 = new MaterialInventory(material2, 50, 0, 0.0);
        matInv2.saveEventually();
        MaterialInventory matInv3 = new MaterialInventory(material3, 5, 2, 0.0);
        matInv3.saveEventually();
        
        
        LinkedHashMap<RawMaterial, Double> m = new LinkedHashMap<RawMaterial, Double>();
        m.put(material1, 1.0);
        m.put(material2, 0.5);        
        Recipe r = new Recipe(product1, m);
        r.saveEventually();
        
        m = new LinkedHashMap<RawMaterial, Double>();
        m.put(material1, 2.0);
        m.put(material2, 0.5); 
        m.put(material3, 0.25);
        r = new Recipe(product2, m);
        r.saveEventually();
        
        Batch batch1 = new Batch(r, 1, 20);
        batch1.saveEventually();*/
		  
	}

}

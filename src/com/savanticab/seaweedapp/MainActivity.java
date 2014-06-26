package com.savanticab.seaweedapp;

import java.util.LinkedHashMap;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.MaterialInventory;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.ProductInventory;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;
import com.savanticab.seaweedapp.sqlite.BaseDBAdapter;
import com.savanticab.seaweedapp.sqlite.BatchDBAdapter;
import com.savanticab.seaweedapp.sqlite.MaterialInventoryDBAdapter;
import com.savanticab.seaweedapp.sqlite.ProductDBAdapter;
import com.savanticab.seaweedapp.sqlite.ProductInventoryDBAdaptor;
import com.savanticab.seaweedapp.sqlite.RawMaterialDBAdapter;
import com.savanticab.seaweedapp.sqlite.RecipeDBAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

// Title self-explanatory
// starting point of App

public class MainActivity extends Activity {

	static final int REQUEST_LINK_TO_DBX = 0;
	private DbxAccountManager mAccountManager;
	private DbxAccount mAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		mAccountManager = BaseDBAdapter.getAccountManager(this);
		
		if (mAccountManager.hasLinkedAccount()) {
			mAccount = mAccountManager.getLinkedAccount();
		} else {
			mAccountManager.startLink(this, REQUEST_LINK_TO_DBX);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_LINK_TO_DBX) {
	        if (resultCode == Activity.RESULT_OK) {
	            mAccount = mAccountManager.getLinkedAccount();

	            Product product1 = new Product("SOAP1", "Soap", "Lime", "Big", 10.0);
	            Product product2 = new Product("SOAP2", "Soap", "Clove", "Small", 6.0);
	            Product product3 = new Product("SOAP3", "Soap", "Langi-langi", "Big", 14.0);
	            Product product4 = new Product("SOAP4", "Soap", "Lemongrass", "Medium", 16.0);
	            ProductDBAdapter productAdapter = new ProductDBAdapter(this);
	            productAdapter.add(product1);
	            productAdapter.add(product2);
	            productAdapter.add(product3);
	            productAdapter.add(product4);
	            
	            ProductInventoryDBAdaptor productInventoryAdapter = new ProductInventoryDBAdaptor(this);
	            productInventoryAdapter.add(new ProductInventory(product1, 200, 100));
	            productInventoryAdapter.add(new ProductInventory(product2, 100, 100));
	            productInventoryAdapter.add(new ProductInventory(product3, 300, 100));
	            productInventoryAdapter.add(new ProductInventory(product4, 150, 100));
	            
	            RawMaterialDBAdapter materialAdapter = new RawMaterialDBAdapter(this);
	            RawMaterial material1 = new RawMaterial("Coconut oil", "L", "");
	            RawMaterial material2 = new RawMaterial("Seaweed", "Kg", "");
	            RawMaterial material3 = new RawMaterial("Bee wax", "Kg", "");
	            RawMaterial material4 = new RawMaterial("TestMtrl", "Kg", "");
	            materialAdapter.add(material1);
	            materialAdapter.add(material2);
	            materialAdapter.add(material3);
	            materialAdapter.add(material4);
	            
	            MaterialInventoryDBAdapter matInvAdapter = new MaterialInventoryDBAdapter(this);
	            matInvAdapter.add(new MaterialInventory(material1, 100, 0, 0.0));
	            matInvAdapter.add(new MaterialInventory(material2, 50, 0, 0.0));
	            matInvAdapter.add(new MaterialInventory(material3, 5, 2, 0.0));
	            matInvAdapter.add(new MaterialInventory(material4, 5, 2, 0.0));
	            
	            
	            LinkedHashMap<RawMaterial, Double> m = new LinkedHashMap<RawMaterial, Double>();
	            String instr = new String( "Min sträng som kan innehålla massssssoooooor med teeeeeeeeeeext. Bla bla bla blla bla bla bla bla bla bla bla bla bla bla la bla bla bla bla bla bla bla bla bla bla");
	            m.put(material1, 1.0);
	            m.put(material2, 0.5);        
	            Recipe r = new Recipe(product1, m, instr);
	            RecipeDBAdapter recipeAdapter = new RecipeDBAdapter(this);
	            recipeAdapter.add(r);
	            
	            m = new LinkedHashMap<RawMaterial, Double>();
	            String instr2 = new String("Min andra sträng som kan innehålla massssssoooooor med teeeeeeeeeeext. Bla bla bla bla bla bla bla bla bla bla bla la bla bla bla bla bla bla bla bla bla bla");
	            m.put(material1, 2.0);
	            m.put(material2, 0.5); 
	            m.put(material3, 0.25);
	            m.put(material4, 0.25);
	            r = new Recipe(product2, m, instr2);
	            recipeAdapter.add(r);
	            
	            
	            Batch batch1 = new Batch(r, 1, 20);
	            batch1.setExtraComments("cladkd");
	            BatchDBAdapter bAdapter = new BatchDBAdapter(this);
	            bAdapter.add(batch1);
	            
	        } else {
	            // ... Link failed or was cancelled by the user.
	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			// Button "To the factory"
			Button button_factory_rawmtrl = (Button) rootView
					.findViewById(R.id.button_factory_rawmtrl);
			button_factory_rawmtrl.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(v.getContext(),
							RawMaterialListActivity.class);
					startActivity(i);
				}
			});
			
			Button button_factory_prod_plan = (Button) rootView
					.findViewById(R.id.button_factory_prod_plan);
			button_factory_prod_plan.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(v.getContext(),
							ProductionPlanActivity.class);
					startActivity(i);
				}
			});
			
			Button button_factory_prod_doc = (Button) rootView
					.findViewById(R.id.button_factory_prod_doc);
			button_factory_prod_doc.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(v.getContext(),
							ProductionDocumentListActivity.class);
					startActivity(i);
				}
			});
			

			// Button "To the shop"
			Button button_shop = (Button) rootView
					.findViewById(R.id.button_to_shop);
			button_shop.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});

			// Button "To the reports"
			Button button_reports = (Button) rootView
					.findViewById(R.id.button_report);
			button_reports.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
			return rootView;
		}
	}

}

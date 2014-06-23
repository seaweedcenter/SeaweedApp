package com.savanticab.seaweedapp;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.ActionBar;

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
		mAccountManager = SeaweedApplication
				.getAccountManager(MainActivity.this);
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

				DbxDatastore store = null;
				try {
					store = DbxDatastore.openDefault(mAccount);
				} catch (DbxException e) {
					// TODO Auto-generated catch block e.printStackTrace(); }
				} finally {
					DbxTable tasksTbl = store.getTable(RawMaterial.TABLE_NAME);
					tasksTbl.insert().set(RawMaterial.NAME, "Coconut oil")
							.set(RawMaterial.UNIT, "L")
							.set(RawMaterial.STOCK, 5)
							.set(RawMaterial.ORDERED, 0)
							.set(RawMaterial.RESERVED, 0);
					tasksTbl.insert().set(RawMaterial.NAME, "Seaweed")
							.set(RawMaterial.UNIT, "Kg")
							.set(RawMaterial.STOCK, 10)
							.set(RawMaterial.ORDERED, 2)
							.set(RawMaterial.RESERVED, 1);
					tasksTbl.insert().set(RawMaterial.NAME, "Bee wax")
							.set(RawMaterial.UNIT, "Kg")
							.set(RawMaterial.STOCK, 50)
							.set(RawMaterial.ORDERED, 5)
							.set(RawMaterial.RESERVED, 0);
					
					tasksTbl = store.getTable(Product.TABLE_NAME);
					DbxRecord product1 = tasksTbl.insert().set(Product.CODE, "SOAP1")
							.set(Product.NAME, "Soap").set(Product.FRAGANCE, "Lime")
							.set(Product.SIZE, "Big").set(Product.PRICE, 10.0)
							.set(Product.STOCK, 200).set(Product.IN_PRODUCTION, 0);
					DbxRecord product2 = tasksTbl.insert().set(Product.CODE, "SOAP2")
							.set(Product.NAME, "Soap").set(Product.FRAGANCE, "Clove")
							.set(Product.SIZE, "Small").set(Product.PRICE, 6.0)
							.set(Product.STOCK, 100).set(Product.IN_PRODUCTION, 0);
					tasksTbl.insert().set(Product.CODE, "SOAP3")
							.set(Product.NAME, "Soap")
							.set(Product.FRAGANCE, "Langi-langi")
							.set(Product.SIZE, "Big").set(Product.PRICE, 14.0)
							.set(Product.STOCK, 300).set(Product.IN_PRODUCTION, 0);
					tasksTbl.insert().set(Product.CODE, "SOAP4")
							.set(Product.NAME, "Soap")
							.set(Product.FRAGANCE, "Lemongrass")
							.set(Product.SIZE, "Medium").set(Product.PRICE, 16.0)
							.set(Product.STOCK, 150).set(Product.IN_PRODUCTION, 0);

					LinkedHashMap<String, Double> m = new LinkedHashMap<String, Double>();
					m.put("7yPdcNo8dTnPuAdhyjgLBA", 1.0);
					m.put("WZ8-OeGevhdl_H7iwWd32g", 0.5);
					Recipe r = new Recipe(new Product(product1), m);
					r.save(store);

					m = new LinkedHashMap<String, Double>();
					m.put("7yPdcNo8dTnPuAdhyjgLBA", 2.0);
					m.put("WZ8-OeGevhdl_H7iwWd32g", 0.5);
					m.put("d1gVXJko7eruD9XjWBpYqA", 0.25);
					r = new Recipe(new Product(product2), m);
					r.save(store);
					
					Batch batch1 = new Batch(r, 1, 20);
					batch1.save(store);
					try {
						store.sync();
					} catch (DbxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					store.close();
				}

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
			Button button_factory = (Button) rootView
					.findViewById(R.id.button_to_factory);
			button_factory.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(v.getContext(),
							FactoryTasksActivity.class);
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
					Intent i = new Intent(v.getContext(),
							FactoryTasksActivity.class);
					startActivity(i);
				}
			});

			// Button "To the reports"
			Button button_reports = (Button) rootView
					.findViewById(R.id.button_report);
			button_reports.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(v.getContext(),
							FactoryTasksActivity.class);
					startActivity(i);
				}
			});
			return rootView;
		}
	}

}

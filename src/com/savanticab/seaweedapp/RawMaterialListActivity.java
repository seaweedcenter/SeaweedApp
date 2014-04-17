package com.savanticab.seaweedapp;

import java.util.List;

import com.savanticab.seaweedapp.model.Inventory;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.sqlite.MySQLiteHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * An activity representing a list of RawMaterials. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link RawMaterialDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link RawMaterialListFragment} and the item details (if present) is a
 * {@link RawMaterialDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link RawMaterialListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class RawMaterialListActivity extends FragmentActivity implements
		RawMaterialListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private List<RawMaterial> rawMaterialList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rawmaterial_list);
		
		// enable Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// load raw materials to populate list
		MySQLiteHelper helper = MySQLiteHelper.getInstance(this);
		Inventory inventory = helper.getInventory();
		rawMaterialList = inventory.getMaterialList();
		//rawMaterialList = helper.getAllRawMaterials();
				
		if (findViewById(R.id.rawmaterial_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((RawMaterialListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.rawmaterial_list))
					.setActivateOnItemClick(true);
		}
		
		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link RawMaterialListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	// JN: send the clicked ID and the associated rawMaterial object
	// normally received by DetailFragment (or DetailActivity)
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(RawMaterialDetailFragment.ARG_ITEM_ID, id);
			arguments.putParcelable("rawMaterial", rawMaterialList.get(Integer.parseInt(id)-1));
			RawMaterialDetailFragment fragment = new RawMaterialDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.rawmaterial_detail_container, fragment)
					.commit();
		} else {
			// TODO: implement single pane activity
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					RawMaterialDetailActivity.class);
			detailIntent.putExtra(RawMaterialDetailFragment.ARG_ITEM_ID, id);
			detailIntent.putExtra("rawMaterial", rawMaterialList.get(Integer.parseInt(id)));
			startActivity(detailIntent);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}

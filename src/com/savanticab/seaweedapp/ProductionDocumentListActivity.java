package com.savanticab.seaweedapp;

import com.savanticab.seaweedapp.model.Batch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * 
 * "Main" activity for creating the list of Batches on side
 * and when selecting one batch launch user interaction to work with given Batch
 * 
 * Catch callbacks from list fragment (ProductionDocumentListFragment)
 * using onItemSelected(). onItemSelected activates the fragment for further
 * user interaction: ProductionDocumentDetailFragment
 * 
 * An activity representing a list of ProductionDocuments. This activity has
 * different presentations for handset and tablet-size devices. On handsets, the
 * activity presents a list of items, which when touched, lead to a
 * {@link ProductionDocumentDetailActivity} representing item details. On
 * tablets, the activity presents the list of items and item details
 * side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ProductionDocumentListFragment} and the item details (if present) is a
 * {@link ProductionDocumentDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ProductionDocumentListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ProductionDocumentListActivity extends FragmentActivity implements
		ProductionDocumentListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productiondocument_list);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (findViewById(R.id.productiondocument_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ProductionDocumentListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.productiondocument_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link ProductionDocumentListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	//public void onItemSelected(String id) {
	public void onItemSelected(Batch batch) {

		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			//arguments.putString(ProductionDocumentDetailFragment.ARG_ITEM_ID,
			//		id);
			
			arguments.putParcelable("batch", batch);
			ProductionDocumentDetailFragment fragment = new ProductionDocumentDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.productiondocument_detail_container, fragment)
					.commit();

		} else { // TODO: actually implement this activity for single-pane
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					ProductionDocumentDetailActivity.class);
			//detailIntent.putExtra(ProductionDocumentDetailFragment.ARG_ITEM_ID,
			//		id);
			detailIntent.putExtra("batch", batch);
			startActivity(detailIntent);
		}
	}
}

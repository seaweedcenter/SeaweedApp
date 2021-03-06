package com.savanticab.seaweedapp;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.sqlite.BatchDBAdapter;

/**
 * Responsible for listing Batches
 * Batch objects are loaded into an ArrayAdapter object used in the listing
 * when selecting an object, callback goes to ProductionDocumentListActivity
 * 
 * A list fragment representing a list of ProductionDocuments. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is currently
 * being viewed in a {@link ProductionDocumentDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ProductionDocumentListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		//public void onItemSelected(String id);

		public void onItemSelected(Batch batch);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		//public void onItemSelected(String id) {
		//}
		public void onItemSelected(Batch batch) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProductionDocumentListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		BatchDBAdapter bAdaptor = new BatchDBAdapter(this.getActivity().getApplicationContext());
		List<Batch> batchList = bAdaptor.getAll(); // helper.getAllBatches();
		List<Batch> batchListUnfinished = new LinkedList<Batch>();
		List<Batch> batchListFinished = new LinkedList<Batch>();
		
		for (Batch b : batchList) {
			if (b.isFinished()) {
				batchListFinished.add(b);
			}
			else {
				batchListUnfinished.add(b);
			}
		}
		batchList = batchListUnfinished;
		batchList.addAll(batchListFinished);
		
		
//		// TODO: replace with a real list adapter.
//		setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//				android.R.layout.simple_list_item_activated_1,
//				android.R.id.text1, DummyContent.ITEMS));
		setListAdapter(new ArrayAdapter<Batch>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, batchList));
		
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		ArrayAdapter<Batch> batchArray= (ArrayAdapter<Batch>)getListAdapter();
		Batch b = batchArray.getItem(position);
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		//mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
		mCallbacks.onItemSelected(b); // this goes to ProductionDocumentListActivity
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != AdapterView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
						: AbsListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == AdapterView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}

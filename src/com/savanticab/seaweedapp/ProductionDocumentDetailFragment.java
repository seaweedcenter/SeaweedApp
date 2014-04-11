package com.savanticab.seaweedapp;

import java.util.LinkedList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.savanticab.seaweedapp.dummy.DummyContent;
import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

/**
 * A fragment representing a single ProductionDocument detail screen. This
 * fragment is either contained in a {@link ProductionDocumentListActivity} in
 * two-pane mode (on tablets) or a {@link ProductionDocumentDetailActivity} on
 * handsets.
 */
public class ProductionDocumentDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProductionDocumentDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_productiondocument_detail, container, false);

		Batch batch = (Batch)getArguments().getParcelable("batch");
		
		if (batch != null) {
			
			Toast toast = Toast.makeText(getActivity(), batch.toString(), Toast.LENGTH_SHORT);
			toast.show();
			
			TextView textViewHeading = (TextView) rootView.findViewById(R.id.productiondocument_detail);
			textViewHeading.setText(batch.getRecipe().getProduct().getCode() + " " +
					batch.getRecipe().getProduct().getName());
		}
		
		// Show the dummy content as text in a TextView.
		//if (mItem != null) {
		//	((TextView) rootView.findViewById(R.id.productiondocument_detail))
		//			.setText(mItem.content);
		//}

		return rootView;
	}
}

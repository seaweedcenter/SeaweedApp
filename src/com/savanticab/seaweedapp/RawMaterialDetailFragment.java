package com.savanticab.seaweedapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.savanticab.seaweedapp.model.RawMaterial;

/**
 * User interactions (change material quantities basically) is handled here and
 * in RawMaterialDetailFragment
 * 
 * A fragment representing a single RawMaterial detail screen. This fragment is
 * either contained in a {@link RawMaterialListActivity} in two-pane mode (on
 * tablets) or a {@link RawMaterialDetailActivity} on handsets.
 */
public class RawMaterialDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_MATERIAL = "material";

	private RawMaterial mItem;

	// private MaterialInventoryDBAdapter mDBAdaptor;
	// private Inventory inventory;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RawMaterialDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		if (getArguments().containsKey(ARG_MATERIAL)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			// mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
			// ARG_ITEM_ID));
			// här måste den ta emot rätt RawMaterial
			mItem = getArguments().getParcelable(
					RawMaterialDetailFragment.ARG_MATERIAL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_rawmaterial_detail,
				container, false);

		// show objects and set up button listeners
		if (mItem != null) {

			updateObjects(rootView);

			// register OK-button listener, updates the
			ImageButton button = (ImageButton) rootView
					.findViewById(R.id.imageButtonOK);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// caught click on OK button
					EditText editText = (EditText) v.getRootView()
							.findViewById(R.id.editTextEnterQuantity);
					String msg = editText.getText().toString();
					CheckBox chkBoxStock = (CheckBox) v.getRootView()
							.findViewById(R.id.checkBoxStock);
					CheckBox chkBoxOrdered = (CheckBox) v.getRootView()
							.findViewById(R.id.checkBoxOrdered);
					boolean oneChecked = chkBoxStock.isChecked()
							^ chkBoxOrdered.isChecked(); // XOR

					if (!msg.isEmpty() & oneChecked) {
						if (chkBoxStock.isChecked()) {
							double newQuantity = mItem.getStock()
									+ Double.parseDouble(msg);
							mItem.setStock(newQuantity);
							mItem.setOrdered(mItem.getOrdered()
									- Double.parseDouble(msg));
						}
						if (chkBoxOrdered.isChecked()) {
							double newQuantity = mItem.getOrdered()
									+ Double.parseDouble(msg);
							mItem.setOrdered(newQuantity); 
						}
						DbxAccount acct = SeaweedApplication.getDefaultAccount();
						if (null == acct) {
							Log.e(RawMaterialListFragment.class.getName(),
									"No linked account.");
							return;
						}
						DbxDatastore store = null;

						try {
							store = DbxDatastore.openDefault(acct);
						} catch (DbxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mItem.save(store);
						store.close();
						editText.setText("");
						updateObjects(v.getRootView());
					} else {
						// TODO add textView that gives a little error message
						// if clicked OK but nothing entered AND (nothing
						// checked OR both checked)
					}
				}
			});
			// register Cancel-button listener, clears all choices
			ImageButton buttonCancel = (ImageButton) rootView
					.findViewById(R.id.imageButtonCancel);
			buttonCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					EditText editText = (EditText) v.getRootView()
							.findViewById(R.id.editTextEnterQuantity);
					editText.setText("");
					CheckBox chkBoxStock = (CheckBox) v.getRootView()
							.findViewById(R.id.checkBoxStock);
					CheckBox chkBoxOrdered = (CheckBox) v.getRootView()
							.findViewById(R.id.checkBoxOrdered);
					chkBoxStock.setChecked(false);
					chkBoxOrdered.setChecked(false);
				}
			});
			// register listeners for the check-boxes
			// allow only one to be ticked at a time
			CheckBox chkBoxStock = (CheckBox) rootView
					.findViewById(R.id.checkBoxStock);
			chkBoxStock.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox chkBoxOrdered = (CheckBox) v.getRootView()
							.findViewById(R.id.checkBoxOrdered);
					if (chkBoxOrdered.isChecked()) {
						chkBoxOrdered.setChecked(false);
					}
				}
			});
			CheckBox chkBoxOrdered = (CheckBox) rootView
					.findViewById(R.id.checkBoxOrdered);
			chkBoxOrdered.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox chkBoxStock = (CheckBox) v.getRootView()
							.findViewById(R.id.checkBoxStock);
					if (chkBoxStock.isChecked()) {
						chkBoxStock.setChecked(false);
					}
				}
			});
		}

		return rootView;
	}

	// just updates textViewes etc
	// private MaterialInventory mInventory;
	private void updateObjects(View rootView) {

		((TextView) rootView.findViewById(R.id.title_rawmaterial_detail))
				.setText(mItem.getName()); // .setText(mItem.content);

		((TextView) rootView.findViewById(R.id.rawmaterial_stock_quantity))
				.setText(Double.toString(mItem.getStock()) + " "
						+ mItem.getUnit());

		((TextView) rootView.findViewById(R.id.rawmaterial_ordered_quantity))
				.setText(Double.toString(mItem.getOrdered()) + " "
						+ mItem.getUnit());

		((TextView) rootView.findViewById(R.id.rawmaterial_reserved_quantity))
				.setText(Double.toString(mItem.getReserved()) + " "
						+ mItem.getUnit());

	}

	public void updateQuantity(View view) {

		// EditText editText = (EditText) getView().getRootView().
		// findViewById(R.id.editTextEnterQuantity);
		// String msg = editText.getText().toString();

		// RawMaterial mtrl = sqlhelper.findRawMaterialByName(mItem.getName());
		// double newQuantity = mtrl.getStockQuantity()+Double.parseDouble(msg);
		// mtrl.setStockQuantity(newQuantity);
		// sqlhelper.updateRawMaterial(mtrl);
		// view.getRootView()

	}
}

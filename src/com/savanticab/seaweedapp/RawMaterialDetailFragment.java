package com.savanticab.seaweedapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.savanticab.seaweedapp.model.MaterialInventory;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.sqlite.*;

/**
 *  User interactions (change material quantities basically) is handled here
 * and in RawMaterialDetailFragment
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
	public static final String ARG_ITEM_ID = "rawMaterial";//"item_id";

	private RawMaterial mItem;
	private MaterialInventoryDBAdapter mDBAdaptor;
	//private Inventory inventory;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RawMaterialDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDBAdaptor = new MaterialInventoryDBAdapter(getActivity().getApplicationContext());
		//inventory = sqlhelper.getInventory();
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			//mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
			//		ARG_ITEM_ID));
			// här måste den ta emot rätt RawMaterial
			//mItem = getArguments().getString(ARG_ITEM_ID);
			mItem = getArguments().getParcelable(ARG_ITEM_ID);
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
			ImageButton button = (ImageButton) rootView.findViewById(R.id.imageButtonOK);
			button.setOnClickListener(new OnClickListener()
				   {
				             @Override
				             public void onClick(View v)
				             {
				            	 // caught click on OK button
				            	 EditText editText = (EditText) v.getRootView().findViewById(R.id.editTextEnterQuantity);
				            	 String msg = editText.getText().toString();
				            	 CheckBox chkBoxStock = (CheckBox) v.getRootView().findViewById(R.id.checkBoxStock);
				            	 CheckBox chkBoxOrdered = (CheckBox) v.getRootView().findViewById(R.id.checkBoxOrdered);
				            	 boolean oneChecked = chkBoxStock.isChecked() ^ chkBoxOrdered.isChecked(); //XOR
				            	 
				            	 if (!msg.isEmpty() & oneChecked) {
				            		 
				            		 RawMaterial mtrl = mItem; //sqlhelper.findRawMaterialByName(mItem.getName()).entrySet().iterator().next().getKey();
				            		 MaterialInventory mInventory = mDBAdaptor.findMaterialInventoryByMaterialId(mtrl.getId());
				            		 if (chkBoxStock.isChecked())
				            		 {
				            			 double newQuantity = mInventory.getStock() + Double.parseDouble(msg);
				            			 mInventory.setStock(newQuantity);
				            			 mInventory.setOrdered(mInventory.getOrdered() - Double.parseDouble(msg));
				            			 //inventory.setMtrlStock(mtrl, newQuantity);
				            			 //inventory.setMtrlOrdered(mtrl, inventory.getMtrlOrdered(mtrl)-Double.parseDouble(msg));
				            		 }
				            		 if (chkBoxOrdered.isChecked())
				            		 {
				            			 double newQuantity = mInventory.getOrdered() + Double.parseDouble(msg); // inventory.getMtrlOrdered(mtrl) + Double.parseDouble(msg);
				            			 mInventory.setOrdered(newQuantity); //inventory.setMtrlOrdered(mtrl, newQuantity);
				            		 }				            		 
				            		 //sqlhelper.updateRawMaterial(mtrl);
				            		 //sqlhelper.updateInventory(inventory);
				            		 mDBAdaptor.updateMaterialInventory(mInventory);
				            		 editText.setText("");
				            		 updateObjects(v.getRootView());
				            	 }
				             	else {
				             		// TODO add textView that gives a little error message
				             		// if clicked OK but nothing entered AND (nothing checked OR both checked)
				             	}
				             }
				   });
			// register Cancel-button listener, clears all choices
			ImageButton buttonCancel = (ImageButton) rootView.findViewById(R.id.imageButtonCancel);
			buttonCancel.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							EditText editText = (EditText) v.getRootView().findViewById(R.id.editTextEnterQuantity);
							editText.setText("");
							CheckBox chkBoxStock = (CheckBox) v.getRootView().findViewById(R.id.checkBoxStock);
			            	CheckBox chkBoxOrdered = (CheckBox) v.getRootView().findViewById(R.id.checkBoxOrdered);
			            	chkBoxStock.setChecked(false);
			            	chkBoxOrdered.setChecked(false);
						}
					});
			// register listeners for the check-boxes
			// allow only one to be ticked at a time
			CheckBox chkBoxStock = (CheckBox) rootView.findViewById(R.id.checkBoxStock);
			chkBoxStock.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v)
						{
							CheckBox chkBoxOrdered = (CheckBox) v.getRootView().findViewById(R.id.checkBoxOrdered);
							if (chkBoxOrdered.isChecked()){
								chkBoxOrdered.setChecked(false);
							}
						}
					});
			CheckBox chkBoxOrdered = (CheckBox) rootView.findViewById(R.id.checkBoxOrdered);
			chkBoxOrdered.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v)
						{
							CheckBox chkBoxStock = (CheckBox) v.getRootView().findViewById(R.id.checkBoxStock);
							if (chkBoxStock.isChecked()){
								chkBoxStock.setChecked(false);
							}
						}
					});
		}

		return rootView;
	}
	
	// just updates textViewes etc
	private void updateObjects(View rootView) {

		// TODO: look into this, looks very cumbersome to extract objects...
		//HashMap<RawMaterial, MaterialInventory> mtrl = sqlhelper.findRawMaterialByName(mItem.getName());
		//RawMaterial material = mtrl.keySet().iterator().next();
		//MaterialInventory inv = inventory.get//mtrl.get(material);
		// anv�nd bara mItem?
		MaterialInventory mInventory = mDBAdaptor.findMaterialInventoryByMaterialId(mItem.getId());
		
		((TextView) rootView.findViewById(R.id.title_rawmaterial_detail))
		.setText(mItem.getName()); //.setText(mItem.content);
		
		((TextView) rootView.findViewById(R.id.rawmaterial_stock_quantity))
		.setText(Double.toString(mInventory.getStock()) + " " + mItem.getUnit());
		
		((TextView) rootView.findViewById(R.id.rawmaterial_ordered_quantity))
		.setText(Double.toString(mInventory.getOrdered()) + " " + mItem.getUnit());
		
		((TextView) rootView.findViewById(R.id.rawmaterial_reserved_quantity))
		.setText(Double.toString(mInventory.getReserved()) + " " + mItem.getUnit());
		
	}
		
	public void updateQuantity(View view) {
		
		//EditText editText = (EditText) getView().getRootView().
		//		findViewById(R.id.editTextEnterQuantity);
	    //String msg = editText.getText().toString();
	    		
		//RawMaterial mtrl = sqlhelper.findRawMaterialByName(mItem.getName());
		//double newQuantity = mtrl.getStockQuantity()+Double.parseDouble(msg);
		//mtrl.setStockQuantity(newQuantity);
		//sqlhelper.updateRawMaterial(mtrl);
		//view.getRootView()
		
	}
}

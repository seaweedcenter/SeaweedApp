package com.savanticab.seaweedapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.savanticab.seaweedapp.dummy.DummyContent;
import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;

/**
 * Show selection from list (ProductionDocumentListActivity + Fragment)
 * and handle user interaction
 * 
 * A fragment representing a single ProductionDocument detail screen. This
 * fragment is either contained in a {@link ProductionDocumentListActivity} in
 * two-pane mode (on tablets) or a {@link ProductionDocumentDetailActivity} on
 * handsets.
 */
public class ProductionDocumentDetailFragment extends Fragment implements OnClickListener {
	
	private ImageButton buttonOK;
	private Batch batch;
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

		batch = (Batch)getArguments().getParcelable("batch");
		try {
			batch.fetch();
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (batch != null) {
			
			Toast toast = Toast.makeText(getActivity(), batch.toString(), Toast.LENGTH_SHORT);
			toast.show();
			
			TextView textViewHeading = (TextView) rootView.findViewById(R.id.productiondocument_detail_batchid);
			textViewHeading.setText("Batch ID:" + batch.getId());
			
			Recipe r = null;
			Product p = null;
			try {
				r = batch.getRecipe().fetchIfNeeded();
				p = r.getProduct().fetchIfNeeded();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//DateFormat df = DateFormat.getDateTimeInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			TextView textViewDescription = (TextView) rootView.findViewById(R.id.productiondocument_detail_productname);
			textViewDescription.setText("Product: " + p.getName());
			TextView textViewStartDate = (TextView) rootView.findViewById(R.id.productiondocument_detail_startdate);
			textViewStartDate.setText("Started: " + df.format(batch.getStartDate()));
			TextView textViewFinishDate = (TextView) rootView.findViewById(R.id.productiondocument_detail_finishdate);
			if (batch.getFinishDate()==null) {
				textViewFinishDate.setText("Unfinished");
				textViewFinishDate.setTextColor(Color.RED);
			}
			else {
				textViewFinishDate.setText("Finished: " + df.format(batch.getFinishDate()));
				textViewFinishDate.setTextColor(Color.GREEN);
			}
			TextView textViewQuantity = (TextView) rootView.findViewById(R.id.productiondocument_detail_quantity);
			textViewQuantity.setText("Quantity to produce: " + batch.getQuantity());
			TextView textViewProductCode = (TextView) rootView.findViewById(R.id.productiondocument_detail_product_codeid);
			textViewProductCode.setText("Code: " + p.getCode() + ", ID: " + p.getId());
			TextView textViewProductFragrance = (TextView) rootView.findViewById(R.id.productiondocument_detail_product_fragrance);
			textViewProductFragrance.setText("Fragrance: " + p.getFragance());
			TextView textViewProductSize = (TextView) rootView.findViewById(R.id.productiondocument_detail_product_size);
			textViewProductSize.setText("Size: " + p.getSize());
						
//			TextView textViewDescription = (TextView) rootView.findViewById(R.id.productiondocument_detail_description);
//			textViewDescription.setText("Product: " + p.getName() + ", ID: " + p.getId()  + "Code: " + p.getCode() + "Fragrance: "   
//					+ p.getFragance() + "Size: " + p.getSize());
			
//			TextView textViewQuantity = (TextView) rootView.findViewById(R.id.productiondocument_detail_quantity);
//			textViewQuantity.setText("Quantity:" + batch.getQuantity());
			
			Map<RawMaterial, Double> ingredients = null;
			try {
				ingredients = r.getIngredients();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TableLayout table = (TableLayout) rootView.findViewById(R.id.table_ProdDoc);
			int productQty = batch.getQuantity();
			
			// TODO: generalise Recipe class and this to have also instructions ? not just list of material
			for(Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
				RawMaterial mtrl = entry.getKey();
				Double quantityNeeded = entry.getValue() * productQty;
				
				TableRow rowRecipe = new TableRow(this.getActivity());
				
				TextView textViewEmpty = new TextView(this.getActivity());
				textViewEmpty.setText(" ");
				rowRecipe.addView(textViewEmpty);
				TextView textViewItem = new TextView(this.getActivity());
				textViewItem.setText(mtrl.getName());
				rowRecipe.addView(textViewItem);
				TextView textViewQty = new TextView(this.getActivity());
				textViewQty.setText(Double.toString(quantityNeeded));
				rowRecipe.addView(textViewQty);
				
				EditText editTextComment = new EditText(this.getActivity());
				editTextComment.setHint("enter comments here");
				rowRecipe.addView(editTextComment);
				
				table.addView(rowRecipe);
			}
			
			TableRow rowRecipeButtons = new TableRow(this.getActivity());
			LinearLayout buttonsLayout = new LinearLayout(this.getActivity());
//			ImageButton buttonCancel = new ImageButton(this.getActivity());
//			buttonCancel.setImageResource(R.drawable.error_bullet_large);
//			buttonCancel.setId(View.generateViewId());
			
			buttonOK = new ImageButton(this.getActivity());
			buttonOK.setImageResource(R.drawable.ok_bullet_large);
			buttonOK.setId(View.generateViewId());
			buttonOK.setOnClickListener(this);
			//buttonsLayout.addView(buttonCancel);
			buttonsLayout.addView(buttonOK);
			
			rowRecipeButtons.addView(new TextView(this.getActivity()));
			rowRecipeButtons.addView(new TextView(this.getActivity()));
			rowRecipeButtons.addView(new TextView(this.getActivity()));
			rowRecipeButtons.addView(buttonsLayout);
			rowRecipeButtons.addView(new TextView(this.getActivity()));
			table.addView(rowRecipeButtons);
			
		}
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
	
		TableLayout table = (TableLayout)v.getRootView().findViewById(R.id.table_ProdDoc);
		
		// user click: batch finished
		if (v.getId()==buttonOK.getId() & !batch.isFinished()) {
			
			//MySQLiteHelper helper = MySQLiteHelper.getInstance(getActivity());
			//Inventory inventory = helper.getInventory();
			//ProductInventoryDBAdaptor pIAdaptor = new ProductInventoryDBAdaptor(this.getActivity().getApplicationContext());
			// update inventories and mark batch finished
			Product product = batch.getRecipe().getProduct();
			//TODO: product production finish needs to be readded
			//pIAdaptor.ProductProductionFinish(product, batch);
			batch.setIsFinished(true);
			
			// update database
			//new BatchDBAdapter(getActivity().getApplicationContext()).updateBatch(batch);//helper.updateBatch(batch);
			//helper.updateInventory(inventory);
			batch.saveEventually();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			TextView textViewFinishDate = (TextView) v.getRootView().findViewById(R.id.productiondocument_detail_finishdate);
			if (batch.getFinishDate()==null) {
				textViewFinishDate.setText("Unfinished");
				textViewFinishDate.setTextColor(Color.RED);
			}
			else {
				textViewFinishDate.setText("Finished: " + df.format(batch.getFinishDate()));
				textViewFinishDate.setTextColor(Color.GREEN);
			}
			
		}
		
		
	}
}

package com.savanticab.seaweedapp;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.savanticab.seaweedapp.dummy.DummyContent;
import com.savanticab.seaweedapp.model.Batch;
import com.savanticab.seaweedapp.model.MaterialInventory;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;
import com.savanticab.seaweedapp.sqlite.BatchDBAdapter;
import com.savanticab.seaweedapp.sqlite.MaterialInventoryDBAdapter;
import com.savanticab.seaweedapp.sqlite.ProductInventoryDBAdaptor;

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
	//KW
	private Button buttonAddRow;
	private BatchDBAdapter mDBAdaptor;
	//KW end
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
		
		if (batch != null) {
			
			Toast toast = Toast.makeText(getActivity(), batch.toString(), Toast.LENGTH_SHORT);
			toast.show();
			
			TextView textViewHeading = (TextView) rootView.findViewById(R.id.productiondocument_detail_batchid);
			textViewHeading.setText("Batch ID:" + batch.getId());
			
			Recipe r = batch.getRecipe();
			Product p = r.getProduct();
			//DateFormat df = DateFormat.getDateTimeInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			TextView textViewDescription = (TextView) rootView.findViewById(R.id.productiondocument_detail_productname);
			textViewDescription.setText("Product: " + p.getName());
			TextView textViewQuantity = (TextView) rootView.findViewById(R.id.productiondocument_detail_quantity);
			textViewQuantity.setText("Quantity to produce: " + batch.getQuantity());

			TextView textViewProductFragrance = (TextView) rootView.findViewById(R.id.productiondocument_detail_product_fragrance);
			textViewProductFragrance.setText("Fragrance: " + p.getFragance());
			TextView textViewProductSize = (TextView) rootView.findViewById(R.id.productiondocument_detail_product_size);
			textViewProductSize.setText("Size: " + p.getSize());
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
			TextView textViewProductCode = (TextView) rootView.findViewById(R.id.productiondocument_detail_product_codeid);
			textViewProductCode.setText("Code: " + p.getCode() + ", ID: " + p.getId());
			TextView textViewInstructions = (TextView) rootView.findViewById(R.id.text_recipeInstructions);
			textViewInstructions.setText("Instructions: " + r.getInstructions());
						
//			TextView textViewDescription = (TextView) rootView.findViewById(R.id.productiondocument_detail_description);
//			textViewDescription.setText("Product: " + p.getName() + ", ID: " + p.getId()  + "Code: " + p.getCode() + "Fragrance: "   
//					+ p.getFragance() + "Size: " + p.getSize());
			
//			TextView textViewQuantity = (TextView) rootView.findViewById(R.id.productiondocument_detail_quantity);
//			textViewQuantity.setText("Quantity:" + batch.getQuantity());
			
			Map<RawMaterial, Double> ingredients = r.getIngredients();
			TableLayout table = (TableLayout) rootView.findViewById(R.id.table_ProdDoc);
			int productQty = batch.getQuantity();
			
			// TODO: generalise Recipe class and this to have also instructions ? not just list of material
			for(Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
				RawMaterial mtrl = entry.getKey();
				Double quantityNeeded = entry.getValue() * productQty;
				
				TableRow rowRecipe = new TableRow(this.getActivity());
				
				//TextView textViewEmpty = new TextView(this.getActivity());
				//textViewEmpty.setText(" ");
			    //rowRecipe.addView(textViewEmpty);
				EditText editDate = new EditText(this.getActivity());
				editDate.setHint("enter date here");
				rowRecipe.addView(editDate);
				
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
			
			//KW
			
			TableLayout tableExtra = (TableLayout) rootView.findViewById(R.id.table_extra);
			TableRow rowRecipeExtra = new TableRow(this.getActivity());
			TextView textViewExtra = new TextView(this.getActivity());
			textViewExtra.setText(batch.getExtraComments());
			rowRecipeExtra.addView(textViewExtra);
			tableExtra.addView(rowRecipeExtra);
			
			//KW end

			//KW
			
			//Below the "old"(dynamically) way of adding buttons to this view. Those buttons are now added statically in the .xml file. 
			/*
			TableRow rowRecipe2 = new TableRow(this.getActivity());
			LinearLayout buttonContainer = new LinearLayout(this.getActivity());
			
			buttonAddRow = new Button(this.getActivity());
		    buttonAddRow.setText("Insert empty row");
		    
			buttonAddRow.setId(View.generateViewId());
			//buttonAddRow.setId(10);
			buttonAddRow.setOnClickListener(this);
			
			buttonContainer.addView(buttonAddRow);
			
			rowRecipe2.addView(new TextView(this.getActivity()));
			rowRecipe2.addView(new TextView(this.getActivity()));
			rowRecipe2.addView(new TextView(this.getActivity()));
			rowRecipe2.addView(new TextView(this.getActivity()));
			rowRecipe2.addView(buttonContainer);
			table.addView(rowRecipe2);	
			//KW end
					    
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
		    
			*/
			// register add row-button listener 			
			buttonAddRow = (Button) rootView.findViewById(R.id.buttonAddRow);
			buttonAddRow.setOnClickListener(this);
			
			// register OK-button listener
			buttonOK = (ImageButton) rootView.findViewById(R.id.imageButtonOK);
			buttonOK.setOnClickListener(this);
					
		}
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
	
		TableLayout tableExtra = (TableLayout)v.getRootView().findViewById(R.id.table_extra);
		TableLayout table = (TableLayout)v.getRootView().findViewById(R.id.table_ProdDoc);
		
		//KW
		//User click: Add extra row with comments in the batch in connection to the recipe
		int nrOfRows = table.getChildCount();

		if(v.getId()==buttonAddRow.getId()){
			
			TableRow rowRecipeExtra = new TableRow(this.getActivity());
			TableRow.LayoutParams params = new TableRow.LayoutParams();
			params.span = 4;
			
			rowRecipeExtra.setLayoutParams(params);

			tableExtra.addView(rowRecipeExtra, params);
			//table.addView(rowRecipeExtra);
			
			EditText editTextComment = new EditText(this.getActivity());
			editTextComment.setHint("enter additional comments here");
			
			//rowRecipeExtra.addView(editTextComment, nrOfRows -2, params);		
			rowRecipeExtra.addView(editTextComment);		
			
	}
		//KW end
			
		// user click: batch finished
		
		if (v.getId()==buttonOK.getId() & !batch.isFinished()) {
			
			//MySQLiteHelper helper = MySQLiteHelper.getInstance(getActivity());
			//Inventory inventory = helper.getInventory();
			ProductInventoryDBAdaptor pIAdaptor = new ProductInventoryDBAdaptor(this.getActivity().getApplicationContext());
			// update inventories and mark batch finished
			Product product = batch.getRecipe().getProduct();
					
			pIAdaptor.ProductProductionFinish(product, batch);
			batch.setIsFinished(true);
			
			//KW
			mDBAdaptor = new BatchDBAdapter(getActivity().getApplicationContext());
			
			// update database
			//new BatchDBAdapter(getActivity().getApplicationContext()).updateBatch(batch);//helper.updateBatch(batch);
			mDBAdaptor.updateBatch(batch);
			//helper.updateInventory(inventory);
			
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

			
			
			int rowPosition = 0;;
			int numOfExtraRows = tableExtra.getChildCount();
			
			//for(int i = 0; i <= numOfExtraRows; i++){
			   TableRow row = (TableRow)tableExtra.getChildAt(rowPosition);
			   EditText et = (EditText)row.getChildAt(0);
			   String text = et.getText().toString();
			   //rowPosition++;
			  	 
       	       if (!text.isEmpty()) {
       		 
       		     Batch mBatch = mDBAdaptor.findBatchById(batch.getId());
       		     mBatch.setExtraComments(text);
       		     mDBAdaptor.updateBatch(mBatch);
       	        }
       	     
			 //}
			  
		       	     	 
        	 //KW end
      	
			
		}
		
		
	}
	
}

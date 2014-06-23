package com.savanticab.seaweedapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.savanticab.seaweedapp.model.MaterialInventory;
import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;
import com.savanticab.seaweedapp.model.Batch;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

/*
 * Present user with a dropdown (or Spinner to be precise)
 * where Recipes can be selected
 * an ArrayAdapter is loaded with Recipe objects
 * 
 * onItemSelected() in PlaceHolderFragment below handles Spinner selections
 * the user can then create new Batch jobs
 * 
 */

public class ProductionPlanActivity extends Activity {

	private List<RawMaterial> rawMaterialList;

	// Recipe recipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_production_plan);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.production_plan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnItemSelectedListener, OnClickListener, TextWatcher {

		private ImageButton buttonCancel;
		private ImageButton buttonOK;
		private Recipe recipe;
		private Spinner productSpinner;
		private List<Recipe> mRecipeList;
		private DbxDatastore store;
		private DbxAccount acct;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_production_plan,
					container, false);

			
			acct = SeaweedApplication.getDefaultAccount();
			if (null == acct) {
				Log.e(RawMaterialListFragment.class.getName(),
						"No linked account.");
				return null;
			}
			mRecipeList = new ArrayList<Recipe>();

			try {
				store = DbxDatastore.openDefault(acct);
				DbxTable recipeTable = store.getTable(Recipe.TABLE_NAME);
				DbxTable.QueryResult results = recipeTable.query();
				Iterator<DbxRecord> iterator = results.iterator();
				while (iterator.hasNext()) {
					DbxRecord result = iterator.next();
					mRecipeList.add(new Recipe(result));
				}
				store.close();
			} catch (DbxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/*
			 * ParseQuery<Recipe> matInvQuery =
			 * ParseQuery.getQuery(Recipe.class);
			 * matInvQuery.setCachePolicy(ParseQuery
			 * .CachePolicy.NETWORK_ELSE_CACHE); mRecipeList = null; try {
			 * mRecipeList = matInvQuery.find(); } catch (ParseException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */
			// List<Recipe> recipes = new
			// RecipeDBAdapter(getActivity().getApplicationContext()).getAll();

			// the spinner from which the user can select a Recipe is stuffed
			// with an ArrayAdapter
			// which holds Recipe objects. The Recipe.toString() provides the
			// text description shown
			productSpinner = (Spinner) rootView
					.findViewById(R.id.spinner_product_name);

			TextView textViewFragrance = (TextView) rootView
					.findViewById(R.id.text_product_fragrance);
			TextView textViewSize = (TextView) rootView
					.findViewById(R.id.text_product_size);
			textViewFragrance.setText(" ");
			textViewSize.setText(" ");

			// add "empty" recipe to get a default description field in spinner
			// Recipe emptyRecipe = new Recipe();
			// emptyRecipe.setProduct(new
			// Product("Select what you want to produce", "", "", "", 0.0));
			// aa.add(emptyRecipe);
			ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item);
			aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			aa.add("Select what you want to produce");
			for (int i = 0; i < mRecipeList.size(); i++) {
				Recipe tmp = mRecipeList.get(i);
				aa.add(tmp.toString());
			}

			productSpinner.setAdapter(aa);
			// register a listener to deal with selections in spinner
			productSpinner.setOnItemSelectedListener(this);

			EditText editTextQuantity = (EditText) rootView.getRootView()
					.findViewById(R.id.production_planned_quantity);
			editTextQuantity.addTextChangedListener(this);

			return rootView;
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// ?
		}

		// spinner selection listener
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			TableLayout table = (TableLayout) view.getRootView().findViewById(
					R.id.table_recipe);
			EditText editTextQuantity = (EditText) table.getRootView()
					.findViewById(R.id.production_planned_quantity);
			// editTextQuantity.setHintTextColor(Color.RED);
			editTextQuantity.setVisibility(View.INVISIBLE);
			String msg = editTextQuantity.getText().toString();
			int quantity = msg.isEmpty() ? 0 : Integer.parseInt(msg);

			TextView textViewHeadingItem = (TextView) view.getRootView()
					.findViewById(R.id.TextViewHeadingItem);
			TextView textViewHeadingNeededQty = (TextView) view.getRootView()
					.findViewById(R.id.TextViewHeadingNeedQty);
			TextView textViewHeadingStock = (TextView) view.getRootView()
					.findViewById(R.id.TextViewHeadingStock);

			TextView textViewFragrance = (TextView) view.getRootView()
					.findViewById(R.id.text_product_fragrance);
			TextView textViewSize = (TextView) view.getRootView().findViewById(
					R.id.text_product_size);

			// no choice, clear
			if ((position == 0) | (quantity == 0)) {
				// TableLayout table =
				// (TableLayout)view.getRootView().findViewById(R.id.table_recipe);
				while (table.getChildCount() > 3) {
					table.removeViewAt(3);
				}
				textViewHeadingItem.setVisibility(View.INVISIBLE);
				textViewHeadingNeededQty.setVisibility(View.INVISIBLE);
				textViewHeadingStock.setVisibility(View.INVISIBLE);
				editTextQuantity.setVisibility(View.INVISIBLE);
				textViewFragrance.setVisibility(View.INVISIBLE);
				textViewSize.setVisibility(View.INVISIBLE);
			}

			// actual choice of product recipe
			if (position > 0) {
				textViewHeadingItem.setVisibility(View.VISIBLE);
				textViewHeadingNeededQty.setVisibility(View.VISIBLE);
				textViewHeadingStock.setVisibility(View.VISIBLE);
				editTextQuantity.setVisibility(View.VISIBLE);
				textViewFragrance.setVisibility(View.VISIBLE);
				textViewSize.setVisibility(View.VISIBLE);

				recipe = (Recipe) mRecipeList.get(position-1);

				/*
				 * try { recipe =
				 * (Recipe)mRecipeList.get(position).fetchIfNeeded(); } catch
				 * (ParseException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); }
				 */
				textViewFragrance.setText(recipe.getProduct().getFragance());
				textViewSize.setText(recipe.getProduct().getSize());

				if (quantity > 0) {
					// MySQLiteHelper helper =
					// MySQLiteHelper.getInstance(getActivity());
					// Inventory inventory = helper.getInventory();
					/*
					 * ParseQuery<MaterialInventory> matInvQuery =
					 * ParseQuery.getQuery(MaterialInventory.class);
					 * //matInvQuery
					 * .setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE
					 * ); List<MaterialInventory> mInventory = null; try {
					 * mInventory = matInvQuery.find(); } catch (ParseException
					 * e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); }
					 */
					// List<MaterialInventory> mInventory = new
					// MaterialInventoryDBAdapter(getActivity().getApplicationContext()).getAll();
					// pick up Recipe passed by adapter
					// Product product = recipe.getProduct();
					// String s = product.getCode()+" "+ product.getName();
					// Map<RawMaterial, Double> ingredients =
					// recipe.getIngredients();

					// clear table rows beyond heading rows and add data
					// TODO: make the layout of table rows defined in XML
					// would also be neater if the managing of rows worked on
					// ids instead of hard coded numbers
					// TableLayout table =
					// (TableLayout)view.getRootView().findViewById(R.id.table_recipe);
					while (table.getChildCount() > 3) {
						table.removeViewAt(3);
					}

					// (re-)populate table with recipe ingredients
					for (Entry<RawMaterial, Double> entry : recipe
							.getIngredients().entrySet()) {

						TableRow rowRecipe = new TableRow(this.getActivity());
						RawMaterial mtrl = entry.getKey();
						// try {
						// mtrl = entry.getKey();//.fetchIfNeeded();
						// } catch (ParseException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
						// }
						Double quantityNeeded = entry.getValue() * quantity;
						// HashMap<RawMaterial, MaterialInventory> materials =
						// helper.getAllRawMaterials();
						Double quantityStock = 0.0;
						quantityStock = mtrl.getStock();
						/*
						 * for (MaterialInventory mI : mInventory){ try {
						 * mI.fetchIfNeeded();
						 * mI.getMaterial();//.fetchIfNeeded(); } catch
						 * (ParseException e) { // TODO Auto-generated catch
						 * block e.printStackTrace(); } if
						 * (mI.getMaterial().equals(mtrl)){ quantityStock =
						 * mI.getStock(); } }
						 */

						TextView textEmpty = new TextView(this.getActivity());
						textEmpty.setText("");

						TextView textMaterial = new TextView(this.getActivity());
						textMaterial.setText(mtrl.getName());

						TextView textNeeded = new TextView(this.getActivity());
						textNeeded.setText(Double.toString(quantityNeeded)
								+ " " + mtrl.getUnit());

						TextView textInstock = new TextView(this.getActivity());
						textInstock.setText(Double.toString(quantityStock)
								+ " " + mtrl.getUnit());

						if (quantityStock >= quantityNeeded) {
							textNeeded.setTextColor(Color.GREEN);
							textInstock.setTextColor(Color.GREEN);
						} else {
							textNeeded.setTextColor(Color.RED);
							textInstock.setTextColor(Color.RED);
						}

						rowRecipe.addView(textEmpty);
						rowRecipe.addView(textMaterial);
						rowRecipe.addView(textNeeded);
						rowRecipe.addView(textInstock);
						table.addView(rowRecipe);
					}

					TableRow rowRecipe2 = new TableRow(this.getActivity());
					TextView emptyText = new TextView(this.getActivity());
					emptyText.setText(" ");
					rowRecipe2.addView(emptyText);
					rowRecipe2.addView(new TextView(this.getActivity()));
					rowRecipe2.addView(new TextView(this.getActivity()));
					rowRecipe2.addView(new TextView(this.getActivity()));
					rowRecipe2.addView(new TextView(this.getActivity()));
					table.addView(rowRecipe2);

					TableRow rowRecipe3 = new TableRow(this.getActivity());
					LinearLayout buttonsLayout = new LinearLayout(
							this.getActivity());
					buttonCancel = new ImageButton(this.getActivity());
					buttonCancel
							.setImageResource(R.drawable.error_bullet_large);
					buttonCancel.setId(View.generateViewId());

					buttonOK = new ImageButton(this.getActivity());
					buttonOK.setImageResource(R.drawable.ok_bullet_large);
					buttonOK.setId(View.generateViewId());
					buttonsLayout.addView(buttonCancel);
					buttonsLayout.addView(buttonOK);

					rowRecipe3.addView(new TextView(this.getActivity()));
					rowRecipe3.addView(new TextView(this.getActivity()));
					rowRecipe3.addView(new TextView(this.getActivity()));
					rowRecipe3.addView(buttonsLayout);
					rowRecipe3.addView(new TextView(this.getActivity()));
					table.addView(rowRecipe3);

					buttonCancel.setOnClickListener(this);
					buttonOK.setOnClickListener(this);
				}

			}
		}

		// handle buttonclicks
		public void onClick(View v) {

			TableLayout table = (TableLayout) v.getRootView().findViewById(
					R.id.table_recipe);
			// on cancel reset table and spinner
			if (v.getId() == buttonCancel.getId()) {

				while (table.getChildCount() > 3) {
					table.removeViewAt(3);
				}
				Spinner spinner = (Spinner) table.getRootView().findViewById(
						R.id.spinner_product_name);
				spinner.setSelection(0);

				// KW removed these lines, they caused the app to crasch. They
				// seems redundant!
				// Still seems to render some problem....need more tests
				// TODO: Test the cancel button more thoroughly!

				// TextView textViewFragrance =
				// (TextView)table.getRootView().findViewById(R.id.text_product_fragrance);
				// textViewFragrance.setText("");
				// TextView textViewSize =
				// (TextView)table.getRootView().findViewById(R.id.text_product_size);
				// textViewSize.setText("");
				// TextView textViewHeadingItem =
				// (TextView)v.getRootView().findViewById(R.id.TextViewHeadingItem);
				// textViewHeadingItem.setVisibility(View.INVISIBLE);
				// TextView textViewHeadingNeededQty =
				// (TextView)v.getRootView().findViewById(R.id.TextViewHeadingNeedQty);
				// textViewHeadingNeededQty.setVisibility(View.INVISIBLE);
				// TextView textViewHeadingStock =
				// (TextView)v.getRootView().findViewById(R.id.TextViewHeadingStock);
				// textViewHeadingStock.setVisibility(View.INVISIBLE);
				// TextView textViewHeadingStock =
				// (TextView)v.getRootView().findViewById(R.id.textViewProdPlanHeading);
				// textViewHeadingStock.setVisibility(View.INVISIBLE);

				// EditText editTextQuantity =
				// (EditText)table.getRootView().findViewById(R.id.production_planned_quantity);
				// editTextQuantity.setVisibility(View.INVISIBLE);
				// editTextQuantity.setText("");
				// editTextQuantity.setHintTextColor(Color.GRAY);

			}

			// on OK create new batch, add to database and pass data to new
			// Activity
			if (v.getId() == buttonOK.getId()) {

				EditText editTextQuantity = (EditText) table.getRootView()
						.findViewById(R.id.production_planned_quantity);
				String msg = editTextQuantity.getText().toString();
				int quantity = 0;
				if (!msg.isEmpty()) {
					quantity = Integer.parseInt(msg);
					Toast toast = Toast.makeText(v.getContext()
							.getApplicationContext(), "New batch created",
							Toast.LENGTH_SHORT);
					toast.show();

					//Get last batch number
					List<Batch> batchList = new ArrayList<Batch>();
					try {
						store = DbxDatastore.openDefault(acct);
						DbxTable batchTable = store.getTable(Batch.TABLE_NAME);
						DbxTable.QueryResult results = batchTable.query();
						Iterator<DbxRecord> iterator = results.iterator();
						while (iterator.hasNext()) {
							DbxRecord result = iterator.next();
							batchList.add(new Batch(result));
						}
					} catch (DbxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						store.close();
						return;
					}
					Collections.sort(batchList, Collections.reverseOrder());
					Batch lastIdBatch = batchList.get(0);
					Batch batch = new Batch(recipe,
							lastIdBatch.getBatchNumber() + 1, quantity);
					
					HashMap<RawMaterial, Double> ingredients = batch
							.getRecipe().getIngredients();
					;
					for (Entry<RawMaterial, Double> entry : ingredients
							.entrySet()) {
						double qty = entry.getValue() * quantity;
						RawMaterial material = entry.getKey();
						material.setStock(material.getStock() - qty);
						material.setReserved(material.getReserved() + qty);
						material.save(store);
					}
					batch.save(store);

					store.close();
					
					Intent i = new Intent(v.getContext(),
							ProductionDocumentListActivity.class);
					startActivity(i);
				} else {
					editTextQuantity.setHintTextColor(Color.RED);
				}

			}
		}

		// For TextWatcher for EditText field (quantity field)
		@Override
		// behaviour is that when text field is changed the spinner acts as if a
		// selection was made
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// after text changed
			long id = productSpinner.getSelectedItemId();
			int position = productSpinner.getSelectedItemPosition();
			View view = productSpinner.getSelectedView();
			onItemSelected((AdapterView<?>) productSpinner, view, position, id);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}

	}

}

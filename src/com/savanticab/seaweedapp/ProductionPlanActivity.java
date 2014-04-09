package com.savanticab.seaweedapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.savanticab.seaweedapp.model.Product;
import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;
import com.savanticab.seaweedapp.sqlite.MySQLiteHelper;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ProductionPlanActivity extends Activity{
	
	private List<RawMaterial> rawMaterialList;
	
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
		getMenuInflater().inflate(R.menu.production_plan, menu);
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
	public static class PlaceholderFragment extends Fragment implements OnItemSelectedListener {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_production_plan,
					container, false);
			
			MySQLiteHelper helper = MySQLiteHelper.getInstance(getActivity());
			List<RawMaterial> rawMaterialList = helper.getAllRawMaterials();
			List<Recipe> recipes = helper.getAllRecipes();
			
			Spinner productSpinner = (Spinner) rootView.findViewById(R.id.spinner_product_name);
			//ArrayAdapter<CharSequence> aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);	
			//ArrayAdapter<Map<String, Recipe>> aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
			ArrayAdapter<Recipe> aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
			
			aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			TextView textVariation = (TextView) rootView.findViewById(R.id.text_product_variation);
			TextView textSize = (TextView) rootView.findViewById(R.id.text_product_size);
			textVariation.setText(" ");
			textSize.setText(" ");
			
			// ugly solution: add "empty" recipe to get a default description field in spinner
			Recipe emptyRecipe = new Recipe();
			emptyRecipe.setProduct(new Product("Select what you want to produce", "", "", "", 
					0.0, 0, 0));
			//Map<String, Recipe> map = new HashMap<String, Recipe>();
			//map.put(emptyProduct);
			//aa.add(map);
			aa.add(emptyRecipe);
			
			for(int i=0; i<recipes.size(); i++){
				Recipe tmp = recipes.get(i);
				//aa.add(recipes.get(i).getProduct().getCode());
				//map.clear();
				//map = new HashMap<String, Recipe>();
				//map.put(recipes.get(i).getProduct().getCode(), recipes.get(i));
				//aa.add(map);
				aa.add(tmp);
			}
			
			productSpinner.setAdapter(aa);
			// register a listener to deal with selections in spinner
			productSpinner.setOnItemSelectedListener(this); 
			
			return rootView;
		}
		
		public void onNothingSelected(AdapterView<?> parent) {
			// ?
		}
		
		// spinner selection listener
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id)
		{
			// no choice, clear
			if (position==0) {
				TableLayout table = (TableLayout)view.getRootView().findViewById(R.id.table_recipe);
				while (table.getChildCount()>3) {
					table.removeViewAt(3);
				}
			}
			// actual choice of product recipe
			if (position > 0) { 
				//String s = (String)parent.getAdapter().getItem(position);
				//Map<String,Recipe> map = (HashMap<String,Recipe>)parent.getAdapter().getItem(position);
				//String s = (String)(map.keySet().toArray()[0]);
				//Recipe r = map.get(s);
				Recipe recipe = (Recipe)parent.getAdapter().getItem(position);	
				String s = recipe.getProduct().getCode()+" "+ recipe.getProduct().getName();
				
				Toast toast = Toast.makeText(view.getContext().getApplicationContext(), "Selected " + s, Toast.LENGTH_SHORT);
				toast.show();
				
				MySQLiteHelper helper = MySQLiteHelper.getInstance(getActivity());
				//Recipe recipe = helper.findRecipeByProductId(position);
				Map<RawMaterial, Double> ingredients = recipe.getIngredients();
				
				// for each raw material entry create a row in table
				// clear table rows beyond heading rows and add data
				// TODO: make the layout of table rows defined in XML
				// would also be neater if the managing of rows worked on ids instead of hard coded numbers
				TableLayout table = (TableLayout)view.getRootView().findViewById(R.id.table_recipe);
				while (table.getChildCount()>3) {
					table.removeViewAt(3);
				}
				
				// (re-)populate table with recipe ingredients
				for(Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
					
					TableRow rowRecipe = new TableRow(this.getActivity());
					RawMaterial mtrl = entry.getKey();
					Double quantityNeeded = entry.getValue();
					List<RawMaterial> materials = helper.getAllRawMaterials();
					Double quantityStock = 0.0;
					
					if (materials.contains(mtrl)) {
						quantityStock = materials.get(materials.indexOf(mtrl)).getStockQuantity();
					}
					//materials.
					TextView textEmpty = new TextView(this.getActivity());
					textEmpty.setText("");
					
					TextView textMaterial = new TextView(this.getActivity());
					textMaterial.setText(mtrl.getName());
					
					TextView textNeeded = new TextView(this.getActivity());
					textNeeded.setText(Double.toString(quantityNeeded) + " " + mtrl.getUnit());
					
					TextView textInstock = new TextView(this.getActivity());
					textInstock.setText(Double.toString(quantityStock) + " " + mtrl.getUnit());
					
					if (quantityStock >= quantityNeeded) {
						textNeeded.setTextColor(Color.GREEN);
						textInstock.setTextColor(Color.GREEN);
					}
					else {
						textNeeded.setTextColor(Color.RED);
						textInstock.setTextColor(Color.RED);
					}
					
					rowRecipe.addView(textEmpty);
					rowRecipe.addView(textMaterial);
					rowRecipe.addView(textNeeded);
					rowRecipe.addView(textInstock);
					table.addView(rowRecipe);
				}
				
			}
		}
		
	}
	
}



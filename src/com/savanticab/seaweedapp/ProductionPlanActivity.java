package com.savanticab.seaweedapp;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.savanticab.seaweedapp.model.RawMaterial;
import com.savanticab.seaweedapp.model.Recipe;
import com.savanticab.seaweedapp.sqlite.MySQLiteHelper;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
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
			ArrayAdapter<CharSequence> aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);	
			aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			TextView textVariation = (TextView) rootView.findViewById(R.id.text_product_variation);
			TextView textSize = (TextView) rootView.findViewById(R.id.text_product_size);
			textVariation.setText(" ");
			textSize.setText(" ");
			
			aa.add("Select product recipe");
			for(int i=0; i<recipes.size(); i++){
				Recipe tmp = recipes.get(i);
				aa.add(recipes.get(i).getProduct().getCode());
			}
			
			productSpinner.setAdapter(aa);
			
			// define a listener object to deal with selections in spinner
			productSpinner.setOnItemSelectedListener(this); 
			
			return rootView;
		}
		
		public void onNothingSelected(AdapterView<?> parent) {
			// ?
		}
		
		public void onItemSelected(AdapterView<?> parent, View view, int position,long id)
		{
			if (position > 0) {
				String s = (String)parent.getAdapter().getItem(position);
				Toast toast = Toast.makeText(view.getContext().getApplicationContext(), "Selected " + s, Toast.LENGTH_SHORT);
				toast.show();
				
				MySQLiteHelper helper = MySQLiteHelper.getInstance(getActivity());
				Recipe recipe = helper.findRecipeByProductId(position);
				Map<RawMaterial, Double> ingredients = recipe.getIngredients();
				for(Entry<RawMaterial, Double> entry : ingredients.entrySet()) {
					RawMaterial mtrl = entry.getKey();
					Double quantity = entry.getValue();
				}
			}
		}
		
	}
	
}



package spp.app.asehackathon;

import java.util.HashMap;

import spp.app.asehackathon.places.PlaceMapActivity;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DealTypesActivity extends Activity {

	Button proceed;
	String[] deals = new String[]{
	        "Restaurants",
	        "Entertainment",
	        "Beauty & Spa",
	        "Services",
	        "Shopping"
	        
	};
	String dealTypes ="";
	SparseBooleanArray sparseBooleanArray ;
	ListView listView;
	int count;
	HashMap<String, Integer> dealType  = new HashMap<String, Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_types);
		
		proceed = (Button)findViewById(R.id.next);
		dealType.put("Restaurants", 1);
		dealType.put("Entertainment", 2);
		dealType.put("Beauty & Spa", 3);
		dealType.put("Services", 4);
		dealType.put("Shopping store", 6);

		
		
		
		// The checkbox for the each item is specified by the layout android.R.layout.simple_list_item_multiple_choice
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, deals);
 
        // Getting the reference to the listview object of the layout
        listView = (ListView) findViewById(R.id.dealListView);
 
        // Setting adapter to the listview
        listView.setAdapter(adapter);
        
        
        count = listView.getCount();	
        proceed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
		        
				for(int i=0; i< count; i++){
					if(sparseBooleanArray.get(i)== true){
						int pType = dealType.get(listView.getItemAtPosition(i).toString());
						dealTypes += "," + pType;
					}
				};
				Log.d("deals String 1" , " > " + dealTypes);
				// TODO Auto-generated method stub
				String reqString = dealTypes.substring(1, dealTypes.length());
				Log.d("deals String", " > " + reqString);
				Intent i = new Intent();
				i.putExtra("dealTypes", reqString);
				setResult(RESULT_OK,i);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deal_types, menu);
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
}

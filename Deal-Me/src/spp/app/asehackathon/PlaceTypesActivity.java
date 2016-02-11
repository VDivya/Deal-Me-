package spp.app.asehackathon;

import java.util.HashMap;

import spp.app.asehackathon.places.PlaceMapActivity;
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

public class PlaceTypesActivity extends Activity {

	Button proceed;
	String[] places = new String[]{
	        "Cafe",
	        "Food",
	        "Restaurant",
	        "Bookstore",
	        "Clothing store",
	        "Florist",
	        "Jewelry",
	        "Movie Theater",
	        "Museum",
	        "Night Club",
	        "Shopping Mall",
	        "Spa"
	        
	};
	String placeTypes ="";
	SparseBooleanArray sparseBooleanArray ;
	ListView listView;
	int count;
	HashMap<String, String> placeType  = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_types);
		proceed = (Button)findViewById(R.id.proceed);
		placeType.put("Cafe", "cafe");
		placeType.put("Food", "food");
		placeType.put("Restaurant", "restaurant");
		placeType.put("Bookstore", "book_store");
		placeType.put("Clothing store", "clothing_store");
		placeType.put("Florist", "florist");
		placeType.put("Jewelry", "jewelry_store");
		placeType.put("Movie Theater", "movie_theater");
		placeType.put("Night Club", "night_club");
		placeType.put("Shopping Mall", "shopping_mall");
		placeType.put("Spa", "spa");
		
		
		
		// The checkbox for the each item is specified by the layout android.R.layout.simple_list_item_multiple_choice
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, places);
 
        // Getting the reference to the listview object of the layout
        listView = (ListView) findViewById(R.id.placeListview);
 
        // Setting adapter to the listview
        listView.setAdapter(adapter);
        
        
	count = listView.getCount();
		sparseBooleanArray= listView.getCheckedItemPositions();
        
		for(int i=0; i< count; i++){
			if(sparseBooleanArray.get(i)== true){
				String pType = placeType.get(listView.getItemAtPosition(i).toString());
				placeTypes += "|" + pType;
			}
		};
        proceed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
		        
				for(int i=0; i< count; i++){
					if(sparseBooleanArray.get(i)== true){
						String pType = placeType.get(listView.getItemAtPosition(i).toString());
						placeTypes += "|" + pType;
					}
				};
				// TODO Auto-generated method stub
				String reqString = placeTypes.substring(1, placeTypes.length());
				Log.d("places String", " > " + reqString);
				Intent i = new Intent(PlaceTypesActivity.this, PlaceMapActivity.class);
				i.putExtra("placeTypes", reqString);
				
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_types, menu);
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

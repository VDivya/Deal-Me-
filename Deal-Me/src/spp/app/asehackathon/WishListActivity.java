package spp.app.asehackathon;

import java.util.ArrayList;

import spp.app.asehackathon.Database.PlaceDbDetails;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import directions.DirectionsMapActivity;

public class WishListActivity extends Activity implements OnItemClickListener {

	Button example;
	Database db = new Database(this);
	private ArrayList<PlaceDbDetails> activityList = new ArrayList<PlaceDbDetails>();
	private ListView list;
	ActivityAdapter adapter = null;
	double latitude ;
	double longitude;
	String placeName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wish_list);
		
		activityList = db.getPlaceDetails();
		list = (ListView)findViewById(R.id.wishlistview);
		adapter = new ActivityAdapter(WishListActivity.this, activityList);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(this);
		
		
	
		
		
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		PlaceDbDetails pDetails  = adapter.getListElements().get(position);
		latitude = pDetails.getLatitude();
		longitude = pDetails.getLongitude();
		placeName = pDetails.getPname();
				
		
		AlertDialog.Builder builder = new Builder(WishListActivity.this);

		builder.setPositiveButton("Directions", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(WishListActivity.this, DirectionsMapActivity.class);
				i.putExtra("destLatitude", latitude);
				i.putExtra("destLongitude", longitude);
				startActivity(i);
			}
		});
		builder.setNegativeButton("Remove", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.setTitle("Wish List");
		builder.setMessage(placeName);

		AlertDialog dlg = builder.create();
		dlg.show();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wish_list, menu);
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

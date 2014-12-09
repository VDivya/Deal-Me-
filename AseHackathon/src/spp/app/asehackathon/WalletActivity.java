package spp.app.asehackathon;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import spp.app.asehackathon.Database.DealDbDetails;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import directions.DirectionsMapActivity;

public class WalletActivity extends Activity implements OnItemClickListener {

	Database db = new Database(this);
	private ArrayList<DealDbDetails> activityList = new ArrayList<DealDbDetails>();
	private ListView list;
	DealActivityAdapter adapter = null;
	double latitude ;
	double longitude;
	String placeName;
	String dealTitle;
	String url;
	String storeUrl;
	String expirationDate;
	Date expDate;
	Date todaysDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		
		activityList = db.getDealDetails();
		list = (ListView)findViewById(R.id.walletlistview);
		adapter = new DealActivityAdapter(WalletActivity.this, activityList);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(this);
		
		
	
		
		
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		DealDbDetails pDetails  = adapter.getListElements().get(position);
		latitude = pDetails.getStoreLat();
		longitude = pDetails.getStoreLon();
		placeName = pDetails.getStoreName();
		dealTitle = pDetails.getDealTitle();
		url = pDetails.getUrl();
		storeUrl = pDetails.getStoreUrl();
		expirationDate = pDetails.getDeal_expiration();
				
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   //get current date time with Date()
		   Date date = new Date();
		   System.out.println(dateFormat.format(date));
		   String dDate = dateFormat.format(date);
		   
		   try {
			expDate = dateFormat.parse(expirationDate);
			 todaysDate = dateFormat.parse(dDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  if(expDate.compareTo(todaysDate)>0){
			   AlertDialog.Builder builder = new Builder(WalletActivity.this);

				builder.setPositiveButton("Okay", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				builder.setNegativeButton("Remove", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();					}
				});
				builder.setTitle("Too Late");
				builder.setMessage("This Deal has Expired!");

				AlertDialog dlg = builder.create();
				dlg.show();
       	}else if((expDate.compareTo(todaysDate)<0) || (expDate.compareTo(todaysDate)==0)){
       		AlertDialog.Builder builder = new Builder(WalletActivity.this);

    		builder.setPositiveButton("Directions", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				Intent i = new Intent(WalletActivity.this, DirectionsMapActivity.class);
    				i.putExtra("destLatitude", latitude);
    				i.putExtra("destLongitude", longitude);
    				startActivity(i);
    			}
    		});
    		/*builder.setPositiveButton("URL", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				
    			}
    		});*/
    		builder.setNegativeButton("Store URL", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				
    			}
    		});
    		/*builder.setNegativeButton("Remove", new OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				
    			}
    		});*/
    		builder.setTitle(placeName);
    		builder.setMessage(dealTitle);

    		AlertDialog dlg = builder.create();
    		dlg.show();
       	}
		
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

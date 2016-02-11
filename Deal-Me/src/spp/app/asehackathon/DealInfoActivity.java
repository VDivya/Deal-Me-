package spp.app.asehackathon;

import directions.DirectionsMapActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DealInfoActivity extends Activity {

	String storeName;
	String dealTitle;
	String dealInfo;
	String surl;
	String storeUrl;
	String address;
	String totalDeals;
	String phone;
	String disclaimer;
	String latitude;
	String longitude;
	String Expiration;
	Database db = new Database(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_info);
		Intent i = getIntent();
		storeName = i.getStringExtra("name");
		dealTitle = i.getStringExtra("dealTitle");
		dealInfo = i.getStringExtra("dealinfo");
		surl = i.getStringExtra("URL");
		storeUrl = i.getStringExtra("storeURL");
		address = i.getStringExtra("address")+ ", "+i.getStringExtra("state") + ", "+i.getStringExtra("city")+ ", "+i.getStringExtra("ZIP");
		totalDeals = i.getStringExtra("totalDealsInThisStore");
		phone = i.getStringExtra("phone");
		disclaimer = i.getStringExtra("disclaimer");
		latitude = i.getStringExtra("lat");
		longitude = i.getStringExtra("lon");
		Expiration = i.getStringExtra("expirationDate");
		
		 TextView store_name = (TextView) findViewById(R.id.storeNameValue);
		 TextView deal_title = (TextView) findViewById(R.id.dealTitleValue);
		 TextView deal_info = (TextView) findViewById(R.id.dealInfoValue);
		 TextView store_address = (TextView) findViewById(R.id.storeAddressValue);
		 TextView url = (TextView) findViewById(R.id.UrlValue);
		 TextView store_url = (TextView) findViewById(R.id.StoreUrlValue);
		 TextView total_deals = (TextView) findViewById(R.id.totalDealsValue);
		 TextView store_phone = (TextView) findViewById(R.id.storePhoneValue);
		 TextView disclaimer_deal = (TextView) findViewById(R.id.disclaimerValue);
		 TextView expiration_deal = (TextView) findViewById(R.id.expirationValue);
		 Button showDir = (Button)findViewById(R.id.showStoreDir);
		 Button wallet = (Button)findViewById(R.id.saveToWallet);
		 
		 store_name.setText(storeName);
		 deal_title.setText(dealTitle);
		 deal_info.setText(dealInfo);
		 store_address.setText(address);
		 url.setText(surl);
		 store_url.setText(storeUrl);
		 total_deals.setText(totalDeals);
		 store_phone.setText(phone);
		 disclaimer_deal.setText(disclaimer);
		 expiration_deal.setText(Expiration);
		 
		 storeName = storeName == null ? "Not present" : storeName;
		 dealTitle = dealTitle == null ? "Not present" : dealTitle;
		 dealInfo = dealInfo == null ? "Not present" : dealInfo;
		 address = address == null ? "Not present" : address;
		 surl = surl == null ? "Not present" : surl;
		 storeUrl = storeUrl == null ? "Not present" : storeUrl;
		 totalDeals = totalDeals == null ? "Not present" : totalDeals;
		 phone = phone == null ? "Not present" : phone;
		 disclaimer = disclaimer == null ? "Not present" : disclaimer;
		 Expiration = Expiration == null ? "Not present" : Expiration;
		 
		 showDir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DealInfoActivity.this, DirectionsMapActivity.class);
				i.putExtra("destLatitude", Double.parseDouble(latitude));
				i.putExtra("destLongitude", Double.parseDouble(longitude));
				startActivity(i);
			}
		});
		 
		 wallet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.storeDealDetails(storeName, dealTitle, dealInfo, address, surl, storeUrl, latitude, longitude, Expiration);
				Toast.makeText(DealInfoActivity.this, "Saved to Wallet!", Toast.LENGTH_SHORT).show();
			}
		});
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deal_info, menu);
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

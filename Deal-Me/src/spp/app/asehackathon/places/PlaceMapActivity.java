package spp.app.asehackathon.places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import spp.app.asehackathon.DealInfoActivity;
import spp.app.asehackathon.DealTypesActivity;
import spp.app.asehackathon.R;
import spp.app.asehackathon.WalletActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import directions.DirectionsMapActivity;

public class PlaceMapActivity extends Activity implements
		OnCameraChangeListener, OnInfoWindowClickListener {

	GooglePlaces googlePlaces;
	PlaceDetails placeDetails;

	// Single Place Activity HashMap
	private HashMap<Marker, String> myHashMap = new HashMap<Marker, String>();

	private HashMap<Marker, Class> hashMap = new HashMap<Marker, Class>();

	Marker dMarker;
	PlacesList placesList;
	GoogleMap googleMap;
	Button search;
	AlertDialogManager alert = new AlertDialogManager();

	GPSLocation gpsLocation;

	double localLatitude;
	double localLongitude;

	ProgressDialog pDialog;

	AddItemizedOverlay itemizedOverlay;

	double latitude;
	double longitude;
	EditText searchText;
	String reference;

	// 8Coupons
	double dLatitude;
	double dLongitude;
	String dName;
	String dealInfo;

	// JSON data.. 8Coupons
	// 8Coupons REST API url
	public static final String COUPONS_KEY = "9508b0fc61fbf1955c4bb4c4addadfeba0cdb27de8549373067716eb535a0c6e406515c203fe2b2b6821dbe7039185f6";
	public static final String COUPON_URL = "http://api.8coupons.com/v1/getdeals?key=";
	public static final String RADIUS = "&mileradius=";
	public static final String LIMIT = "&limit=";
	public static final String CATEGORY_ID = "&categoryid=";
	public static final String LAT = "&lat=";
	public static final String LON = "&lon=";

	// 8Coupons objects and arrays
	public static final String TAG_NAME = "name";
	public static final String TAG_DEAL_TITLE = "dealTitle";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_STOREID = "storeID";
	public static final String TAG_TOTALDEALSINSTORE = "totalDealsInThisStore";
	public static final String TAG_PHONE = "phone";
	public static final String TAG_STATE = "state";
	public static final String TAG_CITY = "city";
	public static final String TAG_ZIP = "ZIP";
	public static final String TAG_URL = "URL";
	public static final String TAG_STORE_URL = "storeURL";
	public static final String TAG_DISCLAIMER = "disclaimer";
	public static final String TAG_DEALINFO = "dealinfo";
	public static final String TAG_LAT = "lat";
	public static final String TAG_LON = "lon";
	public static final String EXPIRATION = "expirationDate";

	// JSON Array
	JSONArray deals = null;

	// Deals List
	ArrayList<HashMap<String, String>> dealsList;

	ArrayList<HashMap<String, String>> placesArrayList = new ArrayList<HashMap<String, String>>();

	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	// Speech
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;

	// placeTypes selected
	String placeTypes = null;

	// dealTypes selected
	String dealTypes = null;

	Class cls;

	// Deals button
	Button dealsButton;
	String result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_map);
		dealsButton = (Button) findViewById(R.id.dealsButton);
		Intent intent = getIntent();
		placeTypes = intent.getStringExtra("placeTypes");
		dealTypes = intent.getStringExtra("dealTypes");

		Log.d("Intent Extras: Place Types :", " > " + placeTypes);
		// deals
		dealsList = new ArrayList<HashMap<String, String>>();

		gpsLocation = new GPSLocation(this);
		localLatitude = gpsLocation.getLatitude();
		localLongitude = gpsLocation.getLongitude();
		Log.d("Your Location", "latitude:" + localLatitude + ", longitude: "
				+ localLongitude);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		searchText = (EditText) findViewById(R.id.enterAddress);

		// If the activity is called from DealTypesActivity, placeTypes is null
		// and places will not be displayed
		if (placeTypes != null) {
			new LoadPlaces().execute();
		}
		if (dealTypes != null) {
			new GetDeals().execute();
		}

		// Deals Button
		dealsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PlaceMapActivity.this,
						DealTypesActivity.class);
				startActivityForResult(i, 1);
			}
		});
		getActionBar().hide();
		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				promptSpeechInput();
			}
		});
		// Test..
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new GetDeals().execute();
				/*
				 * Intent i = new Intent( getApplicationContext(),
				 * DealTypesActivity.class);
				 * 
				 * startActivity(i);
				 */
			}
		});

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		// maps v2
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			public void onCameraChange(CameraPosition arg0) {
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
				googleMap.setOnCameraChangeListener(PlaceMapActivity.this);
			}
		});

		// Overlay ApiV2
		LatLng youLocation = new LatLng(localLatitude, localLongitude);

		dMarker = googleMap.addMarker(new MarkerOptions()
				.position(youLocation)
				.title("Your Location")
				.snippet("That is you!")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		hashMap.put(dMarker, DealTypesActivity.class);
		googleMap.setOnInfoWindowClickListener(this);
		CameraUpdate updateCamera = CameraUpdateFactory.newLatLngZoom(
				youLocation, 15);
		googleMap.animateCamera(updateCamera);
		final LatLngBounds.Builder builder = new LatLngBounds.Builder();
		// check for null in case it is null

	}

	class LoadPlaces extends AsyncTask<String, String, String> implements
			OnInfoWindowClickListener {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PlaceMapActivity.this);
			pDialog.setMessage(Html
					.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();

			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// String types = "cafe|restaurant"; // Listing places only
				// cafes,
				// restaurants
				String types = placeTypes;
				// Radius in meters
				double radius = 5000; // 5000 meters

				// get nearest places
				placesList = googlePlaces.getPlaces(localLatitude,
						localLongitude, radius, types);

			} catch (Exception e) {
				Log.d("Exception", "Exception:" + e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = placesList.status;
					Log.d("Points", "four.10000");
					// Check for all possible status
					if (status.equals("OK")) {
						// Successfully got places details
						if (placesList.results != null) {
							// loop through each place
							for (PlaceObjects p : placesList.results) {
								HashMap<String, String> map = new HashMap<String, String>();

								// Place reference won't display in listview -
								// it will be hidden
								// Place reference is used to get
								// "place full details"
								map.put(KEY_REFERENCE, p.reference);

								// Place name
								map.put(KEY_NAME, p.name);

								// adding HashMap to ArrayList
								placesArrayList.add(map);
							}

						}
					} else if (status.equals("ZERO_RESULTS")) {
						// Zero results found
						alert.showAlertDialog(PlaceMapActivity.this,
								"Near Places",
								"Sorry no places found. Try to change the types of places");
					} else if (status.equals("UNKNOWN_ERROR")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error", "Sorry unknown error occured.");
					} else if (status.equals("OVER_QUERY_LIMIT")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error",
								"Sorry query limit to google places is reached");
					} else if (status.equals("REQUEST_DENIED")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error",
								"Sorry error occured. Request is denied");
					} else if (status.equals("INVALID_REQUEST")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error",
								"Sorry error occured. Invalid Request");
					} else {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error", "Sorry error occured.");
					}
				}
			});

			if (placesList.results != null) {
				// loop through all the places
				for (PlaceObjects place : placesList.results) {
					latitude = place.geometry.location.lat; // latitude
					longitude = place.geometry.location.lng; // longitude

					reference = place.reference;
					// API v2
					LatLng location = new LatLng(latitude, longitude);

					Marker myMarker = googleMap
							.addMarker(new MarkerOptions()
									.position(location)
									.title(place.name)
									.snippet(place.vicinity)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
					myHashMap.put(myMarker, reference);
					googleMap.setOnInfoWindowClickListener(this);

				}

			}
		}

		@Override
		public void onInfoWindowClick(Marker marker) {
			// TODO Auto-generated method stub
			Set<Marker> set = myHashMap.keySet();
			Toast.makeText(PlaceMapActivity.this, set.size() + "",
					Toast.LENGTH_SHORT).show();
			if (myHashMap.containsKey(marker)) {
				System.out.println("key exists");
			}
			String reference1 = myHashMap.get(marker);
			Intent i = new Intent(PlaceMapActivity.this,
					SinglePlaceActivity.class);

			i.putExtra(KEY_REFERENCE, reference1);
			startActivity(i);
		}

	}

	class AlertDialogManager {
		public void showAlertDialog(Context context, String title,
				String message) {
			AlertDialog.Builder builder = new Builder(context);

			builder.setPositiveButton("Okay", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			builder.setTitle(title);
			builder.setMessage(message);

			AlertDialog dlg = builder.create();
			dlg.show();
		}
	}

	// Speech
	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				// PP use the result
				searchText.setText(result.get(0));

			}
			break;
		}
		case 1: {
			if (resultCode == RESULT_OK) {
			result	 = data.getStringExtra("dealTypes");
			new GetDeals().execute();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}

		}

	}

	// deals Async Task
	private class GetDeals extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(PlaceMapActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			String url = COUPON_URL + COUPONS_KEY + LAT + localLatitude + LON
					+ localLongitude + RADIUS + "3" + LIMIT + "100"
					+ CATEGORY_ID + result;
			// Making a request to url and getting response
			Log.v("Url", "The url : " + url);
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			String jsonStr2 = jsonStr.substring(1, jsonStr.length() - 1);

			Log.d("Response: ", "> " + jsonStr2);

			if (jsonStr != null) {
				try {
					// JSONObject jsonObj = new JSONObject(jsonStr);

					JSONArray jsonArray = new JSONArray(jsonStr);
					int length = jsonArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						// Getting JSON Array node
						// deals = jsonObj.getJSONArray(TAG_CONTACTS);

						// looping through All Deals
						// for (int i = 0; i < jsonObj.length(); i++) {

						/*
						 * public static final String TAG_NAME = "name"; public
						 * static final String TAG_DEAL_TITLE ="dealTitle";
						 * public static final String TAG_ADDRESS ="address";
						 * public static final String TAG_STOREID = "storeID";
						 * public static final String TAG_TOTALDEALSINSTORE =
						 * "totalDealsInThisStore"; public static final String
						 * TAG_PHONE ="phone"; public static final String
						 * TAG_STATE ="state"; public static final String
						 * TAG_CITY ="city"; public static final String TAG_ZIP
						 * ="ZIP"; public static final String TAG_URL = "URL";
						 * public static final String TAG_STORE_URL =
						 * "storeURL"; public static final String TAG_DISCLAIMER
						 * = "disclaimer"; public static final String
						 * TAG_DEALINFO = "dealinfo"; public static final String
						 * TAG_LAT = "lat"; public static final String TAG_LON =
						 * "lon";
						 */

						String name = jsonObj.getString(TAG_NAME);
						String dealTitle = jsonObj.getString(TAG_DEAL_TITLE);
						String address = jsonObj.getString(TAG_ADDRESS);
						String storeID = jsonObj.getString(TAG_STOREID);
						String totDealsInStore = jsonObj
								.getString(TAG_TOTALDEALSINSTORE);
						String phone = jsonObj.getString(TAG_PHONE);
						String state = jsonObj.getString(TAG_STATE);
						String city = jsonObj.getString(TAG_CITY);
						String zip = jsonObj.getString(TAG_ZIP);
						String sUrl = jsonObj.getString(TAG_URL);
						String storeUrl = jsonObj.getString(TAG_STORE_URL);
						String disclaimer = jsonObj.getString(TAG_DISCLAIMER);
						String dealInfo = jsonObj.getString(TAG_DEALINFO);
						String lat = jsonObj.getString(TAG_LAT);
						String lon = jsonObj.getString(TAG_LON);
						String expiration = jsonObj.getString(EXPIRATION);

						// tmp hashmap for single contact
						HashMap<String, String> deal = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						deal.put(TAG_NAME, name);
						deal.put(TAG_DEAL_TITLE, dealTitle);
						deal.put(TAG_ADDRESS, address);
						deal.put(TAG_STOREID, storeID);
						deal.put(TAG_TOTALDEALSINSTORE, totDealsInStore);
						deal.put(TAG_PHONE, phone);
						deal.put(TAG_STATE, state);
						deal.put(TAG_CITY, city);
						deal.put(TAG_ZIP, zip);
						deal.put(TAG_URL, sUrl);
						deal.put(TAG_STORE_URL, storeUrl);
						deal.put(TAG_DISCLAIMER, disclaimer);
						deal.put(TAG_DEALINFO, dealInfo);
						deal.put(TAG_LAT, lat);
						deal.put(TAG_LON, lon);
						deal.put(EXPIRATION, expiration);

						// adding contact to contact list
						dealsList.add(deal);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			if (dealsList.size() != 0) {
				// loop through all the places
				for (int i = 0; i < dealsList.size(); i++) {

					final HashMap<String, String> deal = dealsList.get(i);

					dLatitude = Double.parseDouble(deal.get(TAG_LAT)); // latitude
					dLongitude = Double.parseDouble(deal.get(TAG_LON)); // longitude
					dName = deal.get(TAG_NAME);
					dealInfo = deal.get(TAG_DEAL_TITLE);

					// API v2
					LatLng location = new LatLng(dLatitude, dLongitude);
					final Marker myMarker;
					googleMap
							.addMarker(new MarkerOptions()
									.position(location)
									.title(dName)
									.snippet(dealInfo)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
					myMarker = googleMap
							.addMarker(new MarkerOptions()
									.position(location)
									.title(dName)
									.snippet(dealInfo)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
					googleMap
							.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

								@Override
								public void onInfoWindowClick(Marker marker) {
									marker = myMarker;
									Intent i = new Intent(
											getApplicationContext(),
											DealInfoActivity.class);
									// i.putExtra(KEY_REFERENCE, reference);
									i.putExtra(TAG_NAME, deal.get(TAG_NAME));
									i.putExtra(TAG_DEAL_TITLE,
											deal.get(TAG_DEAL_TITLE));
									i.putExtra(TAG_ADDRESS,
											deal.get(TAG_ADDRESS));
									i.putExtra(TAG_STOREID,
											deal.get(TAG_STOREID));
									i.putExtra(TAG_TOTALDEALSINSTORE,
											deal.get(TAG_TOTALDEALSINSTORE));
									i.putExtra(TAG_PHONE, deal.get(TAG_PHONE));
									i.putExtra(TAG_STATE, deal.get(TAG_STATE));
									i.putExtra(TAG_CITY, deal.get(TAG_CITY));
									i.putExtra(TAG_ZIP, deal.get(TAG_ZIP));
									i.putExtra(TAG_URL, deal.get(TAG_URL));
									i.putExtra(TAG_STORE_URL,
											deal.get(TAG_STORE_URL));
									i.putExtra(TAG_DISCLAIMER,
											deal.get(TAG_DISCLAIMER));
									i.putExtra(TAG_DEALINFO,
											deal.get(TAG_DEALINFO));
									i.putExtra(TAG_LAT, deal.get(TAG_LAT));
									i.putExtra(TAG_LON, deal.get(TAG_LON));
									i.putExtra(EXPIRATION, deal.get(EXPIRATION));
									startActivity(i);
								}
							});

				}
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		cls = hashMap.get(marker);
		AlertDialog.Builder builder = new Builder(PlaceMapActivity.this);

		builder.setPositiveButton("Deals", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(PlaceMapActivity.this, cls);
				startActivity(i);
			}
		});

		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		builder.setTitle("Deals");
		builder.setMessage("Want to check the deals nearby?");

		AlertDialog dlg = builder.create();
		dlg.show();

	}
}

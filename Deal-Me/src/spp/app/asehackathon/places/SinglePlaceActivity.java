package spp.app.asehackathon.places;

import directions.DirectionsMapActivity;
import spp.app.asehackathon.Database;
import spp.app.asehackathon.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePlaceActivity extends Activity { // flag for Internet connection status
    Boolean isInternetPresent = false;
 
     Database db = new Database(this);
 
    // Google Places
    GooglePlaces googlePlaces;
    AlertDialogManager alert = new AlertDialogManager();
     
    // Place Details
    PlaceDetails placeDetails;
     
    // Progress dialog
    ProgressDialog pDialog;
    String latitude;
    String longitude;
    String name;
    String address;
    
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    
    Button wishlist;
    String reference;
    
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_place);
        
       
        Intent i = getIntent();
         
        // Place referece id
        reference = i.getExtras().getString(KEY_REFERENCE);
//        reference = i.getStringExtra(KEY_REFERENCE);
        Log.d("reference", " > " + reference);
        // Calling a Async Background thread
        new LoadSinglePlaceDetails().execute(reference);
        
        wishlist = (Button)findViewById(R.id.saveToWl);
        Button directions = (Button)findViewById(R.id.showDir);
        directions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			double mlatitude = Double.parseDouble(latitude);
			double mlongitude = Double.parseDouble(longitude);
			Log.d("dest coordinates", "Lat : " + mlatitude + " Lon : " + mlongitude);
			Intent i = new Intent(SinglePlaceActivity.this,DirectionsMapActivity.class );
			i.putExtra("destLatitude", mlatitude);
			i.putExtra("destLongitude", mlongitude);
			startActivity(i);
					
			}

		
		});
        wishlist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.storePlaceDetails(name, address, latitude, longitude);
				Toast.makeText(SinglePlaceActivity.this, "Saved to WishList", Toast.LENGTH_LONG).show();
			}
		});
      
    }
     
     
    /**
     * Background Async Task to Load Google places
     * */
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SinglePlaceActivity.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];
             
            // creating Places class object
            googlePlaces = new GooglePlaces();
 
            // Check if used is connected to Internet
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
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
                    if(placeDetails != null){
                        String status = placeDetails.status;
                         
                        // check place deatils status
                        // Check for all possible status
                        if(status.equals("OK")){
                            if (placeDetails.result != null) {
                                name = placeDetails.result.name;
                                address = placeDetails.result.formatted_address;
                                String phone = placeDetails.result.formatted_phone_number;
                                latitude = Double.toString(placeDetails.result.geometry.location.lat);
                                longitude = Double.toString(placeDetails.result.geometry.location.lng);
                                 
                                Log.d("Place ", name + address + phone + latitude + longitude);
                                 
                                // Displaying all the details in the view
                                // single_place.xml
                                TextView lbl_name = (TextView) findViewById(R.id.vName);
                                TextView lbl_address = (TextView) findViewById(R.id.vaddress);
                                TextView lbl_phone = (TextView) findViewById(R.id.lphone);
                                TextView lbl_location = (TextView) findViewById(R.id.iLocation);
                                 
                                // Check for null data from google
                                // Sometimes place details might missing
                                name = name == null ? "Not present" : name; // if name is null display as "Not present"
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;
                                latitude = latitude == null ? "Not present" : latitude;
                                longitude = longitude == null ? "Not present" : longitude;
                                 
                                lbl_name.setText(name);
                                lbl_address.setText(address);
                                lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                                lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
                            }
                        }
                        else if(status.equals("ZERO_RESULTS")){
                            alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
                                    "Sorry no place found.");
                        }
                        else if(status.equals("UNKNOWN_ERROR"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry unknown error occured.");
                        }
                        else if(status.equals("OVER_QUERY_LIMIT"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry query limit to google places is reached");
                        }
                        else if(status.equals("REQUEST_DENIED"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Request is denied");
                        }
                        else if(status.equals("INVALID_REQUEST"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Invalid Request");
                        }
                        else
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured.");
                        }
                    }else{
                        alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                "Sorry error occured.");
                    }
                     
                     
                }
            });
 
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
}

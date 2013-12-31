package com.ntosto.seaturtlereport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class DataFormActivity extends Activity {
	
	private EditText reporterName; 	// the user reporting the turtle activity
	private EditText phone;			// the user's phone number
	private EditText sightComment;	// the user's description of the turtle activity
	private RadioButton adult;		// indication of the turtle's maturity (we assume hatchling if adult is not selected
	private double sightLat;		// GPS coordinates
	private double sightLong;		// GPS coordinates
	
	GPSTracker gps;					// class to get the user's location
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dataform);
		
		// initialize the gps class
		gps = new GPSTracker(DataFormActivity.this);
		
		// get the values from the form
		reporterName = (EditText) findViewById(R.id.nameText);
		phone = (EditText) findViewById(R.id.phoneText);
		sightComment = (EditText) findViewById(R.id.commentText);
		adult = (RadioButton)findViewById(R.id.adult);
		
		// Begin onClickListener
		findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
                // check if GPS enabled     
				if(gps.canGetLocation()){
		             
		             sightLat = gps.getLatitude();
		             sightLong = gps.getLongitude();
		             
		            // Stop using GPS after coordinates are taken
		             gps.stopUsingGPS();
		             
		             // instantiate and execute background task 
		             // to insert the turtle sighting report into the database
		             new CreateNewReport().execute();
		           
		         } else {
		             // can't get location
		             // GPS or Network is not enabled
		             // Ask user to enable GPS/network in settings
		             gps.showSettingsAlert();
		             // then go back to the form and let the user click Submit again
		         }	
				
            } // end of onclick
	
		} // end of onclick listener
		
		); // end of setonclicklistener;

	} // end of oncreate

	
	// class to insert the turtle report to the cloud-database
	class CreateNewReport extends AsyncTask<String, String, String> {
	
		
		protected void onPreExecute() {
			
			super.onPreExecute();	
			 Toast.makeText(DataFormActivity.this, "Sending...", Toast.LENGTH_SHORT).show();
			 
		}
		
	    
	    protected String doInBackground(String... args) {

	    	// prepare values for inserting into the database
	    	String nameParam = reporterName.getText().toString();
	        String phoneParam = phone.getText().toString();
	        String commentParam = sightComment.getText().toString();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String currentDateandTime = sdf.format(new Date());
	        String maturity;
	        
        	// Maturity Radio Button 
	        if (adult.isChecked()){
	        	maturity="A";
	        }
	        else{
	        	maturity="H";
	        } 
	       
	        // Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("ReporterName", nameParam));
	        params.add(new BasicNameValuePair("ReporterPhone", phoneParam));
	        params.add(new BasicNameValuePair("SightComment", commentParam));
	        params.add(new BasicNameValuePair("SightTime",currentDateandTime ));
	        params.add(new BasicNameValuePair("SightLong", String.valueOf(sightLat)));
	        params.add(new BasicNameValuePair("SightLat", String.valueOf(sightLong)));
	        params.add(new BasicNameValuePair("Maturity", maturity ));
	
	        // send the parameters to the database using HTTP POST to the PHP interface
	        try{
	        	//Http Post
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost("http://str.pthosted.com/insert_sighting.php");
	            httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
	            HttpResponse response = httpclient.execute(httppost);
	            String postResponse = response.getStatusLine().getReasonPhrase();
	            String mypost = httppost.toString();
	            
	            /* debug code
	            Log.i("log_tag","my params: " + params);
	            Log.i("log_tag","this is what I sent to the php: " + mypost);
	            Log.i("log_tag","this is what came back from the post" + postResponse);
	            */           
	        }
	        catch(Exception e){
	        	
	        	// if the post fails, write to the log
	            Log.e("log_tag", "Error:  "+e.toString());
	            // TO DO: alert the user and tell them to call the hotline
	            
	        } 
	
	        
	        return null;
	    } // end of doinbackground
	    
	    protected void onPostExecute(String file_url) {

	    	// all done, switch to the Thank You activity
	    	Intent intent = new Intent(DataFormActivity.this,ThanksActivity.class);
	    	DataFormActivity.this.startActivity(intent);

	    } // end of onpostexecute
	    
	} // end of createnewreport class
	
} // end of DataFormActivity class




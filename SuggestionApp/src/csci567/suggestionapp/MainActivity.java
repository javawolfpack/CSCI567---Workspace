package csci567.suggestionapp;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private EditText edittxt;
	private TextView txt;
	private SharedPreferences prefs;
	private boolean csrf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edittxt = (EditText) findViewById(R.id.edittext);
		txt = (TextView) findViewById(R.id.textView1);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		csrf = prefs.getBoolean("csrf", false);
		CheckBox csrfbox = (CheckBox) findViewById(R.id.csrftoken);
		if(csrf){
	        csrfbox.setChecked(true);
	    }
	    else{
	    	csrfbox.setChecked(false);	    	
	    }
		//Run getData class to populate TextView
		new getData().execute();
		
		csrfbox.setOnClickListener(this);
		Button buttonwrite = (Button) findViewById(R.id.button1);
		buttonwrite.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	    	case R.id.button1:
	    		EditText txt = (EditText) findViewById(R.id.edittext); 
	    		if(csrf){
	    	        Log.d("SuggestionAPP ", "CSRF TRUE");
	    	    }
	    	    else{
	    	    	//post w/ out CSRF Token
	    	    	//new postData().execute(txt.getText().toString());
	    	    	new postJSON().execute(txt.getText().toString());
	    	    	
	    	    }	    		
	        	break;
	    	case R.id.csrftoken:
	    		boolean checked = ((CheckBox) v).isChecked();
	    		if (checked){
	            	prefs.edit().putBoolean("csrf", true).commit();
	            }                
	            else{
	            	prefs.edit().putBoolean("csrf", false).commit();
	            }
	            break;
		}
		
	}
	
	private class getData extends AsyncTask<Void, Void, Void> {
		InputStream inputStream = null;
	    String result = ""; 
	    String url_select = "http://www.bryancdixon.com/androidjson";
	

		protected void onPreExecute() {
			Log.d("SuggestionAPP ", "Preparing to get Suggestions");		
		}

		protected Void doInBackground(Void... params) {
			try {
				URI uri = new URI(url_select);
				HttpClient httpclient = new DefaultHttpClient();
	            HttpResponse httpResponse = httpclient.execute(new HttpGet(uri));
	            HttpEntity httpEntity = httpResponse.getEntity();
	            inputStream = httpEntity.getContent(); 		        
			 } catch (UnsupportedEncodingException e1) {
		            Log.e("UnsupportedEncodingException", e1.toString());
		            e1.printStackTrace();
		        } catch (ClientProtocolException e2) {
		            Log.e("ClientProtocolException", e2.toString());
		            e2.printStackTrace();
		        } catch (IllegalStateException e3) {
		            Log.e("IllegalStateException", e3.toString());
		            e3.printStackTrace();
		        } catch (IOException e4) {
		            Log.e("IOException", e4.toString());
		            e4.printStackTrace();
		        } catch (URISyntaxException e) {
		        	Log.e("URISyntaxException ", e.toString());
					e.printStackTrace();
				}
		        // Convert response to string using String Builder
		        try {
		            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
		            StringBuilder sBuilder = new StringBuilder();
		            String line = null;
		            while ((line = bReader.readLine()) != null) {
		                sBuilder.append(line + "\n");
		            }
		            inputStream.close();
		            result = sBuilder.toString();

		        } catch (Exception e) {
		            Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
		        }
				return null;
		}
		
		protected void onPostExecute(Void donothing) {
			//parse JSON data
			String text = "";
	        try {
	        	JSONObject jO = new JSONObject(result);
	            JSONArray jArray = jO.getJSONArray("suggestions");
	            		
	            
	            for(int i=0; i < jArray.length(); i++) {
	                JSONObject jObject = jArray.getJSONObject(i);
	                text += jObject.getString("text")+"\n\n";
	                Log.d("SuggestionAPP ",text);

	            } // End Loop
	            if(jArray.length()<=0){
	            	text="No Suggestions";
	            }
	        } catch (JSONException e) {
	            Log.e("JSONException", "Error: " + e.toString());
	            text="No Suggestions";
	        }			
			txt.setText(text);
			
			//set TextView Contents to be JSON response
			//txt.setText(result);
		}		
	}
	/**
	 * 
	 * Class to submit submission to my website via unsafe post (no CSRF Token)
	 * @author bryandixon
	 *
	 */
	private class postData extends AsyncTask<String, Void, Void> {
		
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://10.100.100.18:8001/bob");

		protected void onPreExecute() {
			Log.d("SuggestionAPP ", "Preparing to submit suggestion");
			
		}

		protected Void doInBackground(String... params) {
			try {
				//Works if you don't have CSRF Tokens...  
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("suggestion", params[0]));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
 		   	    httpclient.execute(httppost);
 		   	    //Get EditText & restore it to empty form		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    	Log.d("SuggestionAPP:ERROR: ",e.toString());
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    	Log.d("SuggestionAPP:ERROR: ",e.toString());
		    } catch (Exception e){
		    	Log.d("SuggestionAPP:ERROR: ",e.toString());
		    }
			return null;
		}
		
		protected void onPostExecute(Void donothing) {
			edittxt.setText("");
			//Call get data to refresh suggestions list
			new getData().execute();
			//getBaseContext easier way to provide context then storing it
			Toast.makeText(getBaseContext(), "Submission Submitted", Toast.LENGTH_LONG).show();
		}		
	}
	
	private class postJSON extends AsyncTask<String, Void, Void> {
		
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://10.100.100.18:8001/bob");
	    boolean posted = false;

		protected void onPreExecute() {
			Log.d("SuggestionAPP ", "Preparing to submit suggestion");
			
		}

		protected Void doInBackground(String... params) {
			try {
				//Works if you don't have CSRF Tokens...  
		        //Package JSON Object
				JSONObject data = new JSONObject();
				JSONObject suggestion = new JSONObject();
				suggestion.put("topic", params[0]);
				data.put("suggestion", suggestion);
				
				//need to resolve JSON object... 
				StringEntity se = new StringEntity(data.toString());
				httppost.setEntity(se);
				//Log.d(TAG, "Past SE");
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httppost, responseHandler);
				if (responseBody.equalsIgnoreCase("OK")){
					posted = true;
				}
		        //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
 		   	    //httpclient.execute(httppost);
 		   	    //Get EditText & restore it to empty form		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    	Log.d("SuggestionAPP:ERROR: ",e.toString());
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    	Log.d("SuggestionAPP:ERROR: ",e.toString());
		    } catch (Exception e){
		    	Log.d("SuggestionAPP:ERROR: ",e.toString());
		    }
			return null;
		}
		
		protected void onPostExecute(Void donothing) {			
			//Only remove suggestion/update list if successfully submitted
			if(posted){
				//getBaseContext easier way to provide context then storing it
				Toast.makeText(getBaseContext(), "Submission Submitted", Toast.LENGTH_LONG).show();
				edittxt.setText("");
				//Call get data to refresh suggestions list
				new getData().execute();
			}
			else{
				Toast.makeText(getBaseContext(), "Submission Failed", Toast.LENGTH_LONG).show();
			}
		}		
	}
	

}

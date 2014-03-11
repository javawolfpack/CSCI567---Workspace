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
import java.util.Vector;

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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
//import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener{
	private EditText edittxt;
	//private TextView txt;
	private ListView listView1;
	private SharedPreferences prefs;
	private boolean csrf;
	ArrayAdapter<String> adapter;
	String [] items = {"No Suggestions"};
	String m_Text="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edittxt = (EditText) findViewById(R.id.edittext);
		//txt = (TextView) findViewById(R.id.textView1);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		csrf = prefs.getBoolean("csrf", false);
		//CheckBox csrfbox = (CheckBox) findViewById(R.id.csrftoken);
		
		
		listView1 = (ListView) findViewById(R.id.listView1);		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
		listView1.setAdapter(adapter);
		registerForContextMenu(listView1);
		
		//base.OnCreate(bundle);
		//items = new string[] { "Vegetables","Fruits","Flower Buds","Legumes","Bulbs","Tubers" };
		//ListAdapter = new ArrayAdapter<String>(this, Android.Resource.Layout.SimpleListItem1, items);
		/*if(csrf){
	        csrfbox.setChecked(true);
	    }
	    else{
	    	csrfbox.setChecked(false);	    	
	    }*/
		//Run getData class to populate TextView
		new getData().execute();
		
		//csrfbox.setOnClickListener(this);
		Button buttonwrite = (Button) findViewById(R.id.button1);
		buttonwrite.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 MenuInflater inflater = getMenuInflater();		 
		 inflater.inflate(R.menu.main, menu);
		 return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		if(item.getItemId()==R.id.action_search){
			m_Text="";
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Search");

			// Set up the input
			final EditText input = new EditText(this);
			// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("Search", new DialogInterface.OnClickListener() { 
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        m_Text = input.getText().toString();
			        Vector<String> v = new Vector<String>();
			        for(int i=0;i<adapter.getCount();i++){
			        	if(adapter.getItem(i).contains(m_Text)){
			        		v.add(adapter.getItem(i));
			        	}
			        }
			        String [] s = v.toArray(new String[v.size()]);
			        adapter = new ArrayAdapter<String>(getBaseContext(),
			                android.R.layout.simple_list_item_1, s);
					listView1.setAdapter(adapter);
			        Log.d("SuggestionApp: ",adapter.toString());
			        Toast.makeText(getBaseContext(), "Search for: "+m_Text, Toast.LENGTH_LONG).show();
			    }
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.cancel();
			    }
			});
			builder.show();
        	
            return true;
		}
	    if(item.getItemId()==R.id.action_refresh){
	        	//Refresh Data
	        	new getData().execute();
	        	Toast.makeText(getBaseContext(), "Refreshing", Toast.LENGTH_LONG).show();
	            return true;
	    }
	    if(item.getItemId()== R.id.action_settings){
	        	Toast.makeText(getBaseContext(), "Settings", Toast.LENGTH_LONG).show();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	    
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    int position = info.position;
	    if(item.getItemId()==R.id.edit){
	        	Toast.makeText(getBaseContext(), "Edit: " + adapter.getItem(position), Toast.LENGTH_LONG).show();
	            return true;
	    }
	    if(item.getItemId()==R.id.delete){
	        	Toast.makeText(getBaseContext(), "Delete: "+ adapter.getItem(position), Toast.LENGTH_LONG).show();
	            return true;
	    }
	    return super.onContextItemSelected(item);
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.button1){
	    		EditText txt = (EditText) findViewById(R.id.edittext); 
	    		if(csrf){
	    	        Log.d("SuggestionAPP ", "CSRF TRUE");
	    	    }
	    	    else{
	    	    	//post w/ out CSRF Token
	    	    	//new postData().execute(txt.getText().toString());
	    	    	new postJSON().execute(txt.getText().toString());
	    	    	
	    	    }	    		
		}
	    	/*case R.id.csrftoken:
	    		boolean checked = ((CheckBox) v).isChecked();
	    		if (checked){
	            	prefs.edit().putBoolean("csrf", true).commit();
	            }                
	            else{
	            	prefs.edit().putBoolean("csrf", false).commit();
	            }
	            break;*/
		
		
	}
	
	private class getData extends AsyncTask<Void, Void, Void> {
		InputStream inputStream = null;
	    String result = ""; 
	    String url_select = "http://www.bryancdixon.com/androidjson";
	    Vector<String> results = new Vector<String>();
	

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
	                results.add(jObject.getString("text"));
	                text += jObject.getString("text")+"\n\n";
	                Log.d("SuggestionAPP ",text);

	            } // End Loop
	            if(jArray.length()<=0){
	            	results.add("No Suggestions");
	            	text="No Suggestions";
	            }
	        } catch (JSONException e) {
	            Log.e("JSONException", "Error: " + e.toString());
	            results.add("No Suggestions");
	            text="No Suggestions";
	        }
	        //Generate String Array from Vector
	        String [] s = results.toArray(new String[results.size()]);
	        adapter = new ArrayAdapter<String>(getBaseContext(),
	                android.R.layout.simple_list_item_1, s);
			listView1.setAdapter(adapter);
	        //Method when using textview
			//txt.setText(text);
			
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

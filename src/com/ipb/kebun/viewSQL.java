package com.ipb.kebun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import library.JSONParser;

public class viewSQL extends Activity {
	SimpleAdapter Adapter;
	Button Btngetdata;
	Button viewmap;
	// URL to get JSON Array
	private static String url = "http://mati.16mb.com/kebun/proses.php";
	// JSON Node Names
	private static final String TAG_USER = "kebun";
	private static final String TAG_ALAMAT = "alamat";
	private static final String TAG_NAME = "nama";
	private static final String TAG_LNG = "lng";
	private static final String TAG_LAT = "lat";
	Double lat1,lng1,lat2,lng2;
	
	JSONArray user = null;
	ArrayList<HashMap<String, String>> mapList;
	HashMap<String, String> map;
	ListView lv;
	public GPSTracker gpsnya;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchsql);
		gpsnya = new GPSTracker(this);

		if (gpsnya.canGetLocation()) {
			lat1 = gpsnya.getLatitude();
			lng1 = gpsnya.getLongitude();
		} else {
			gpsnya.showSettingsAlert();
			return;
		}
		
		lv = (ListView) findViewById(R.id.list);
		Btngetdata = (Button) findViewById(R.id.refresh);
		Btngetdata.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new JSONParse().execute();
			}
		});
		
		viewmap= (Button) findViewById(R.id.viewmap);
		viewmap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						map.class);
				
				startActivityForResult(intent, 0);
				
			}
		});
		new JSONParse().execute();
		
		lv.getAdapter();
        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new OnItemClickListener() {
      
            public void onItemClick(AdapterView<?> adapter, View view,
                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), map.class);
                // getting
                String lati = ((TextView) view.findViewById(R.id.latitude)).getText().toString();
                String longi = ((TextView) view.findViewById(R.id.longitude)).getText().toString();
                in.putExtra("lat", lati);
                in.putExtra("long", longi);
                startActivity(in);
                finish();
            }
        });
	}
	
	public float getDistanceInMiles(Double lat1, Double lng1, Double lat2, Double lng2) {
	    float [] dist = new float[1];
	    Location.distanceBetween(lat1, lng1, lat2, lng2, dist);
	    return dist[0] * 0.000621371192f;
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(viewSQL.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			JSONObject json = jParser.getJSONFromUrl(url);
			return json;
		}

		
		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try {
				// Getting JSON Array
				mapList = new ArrayList<HashMap<String, String>>();
				user = json.getJSONArray(TAG_USER);
				for (int i = 0; i < user.length(); i++) {
					JSONObject c = user.getJSONObject(i);
					// Storing JSON item in a Variable
					map = new HashMap<String, String>();
					String alamat = c.getString(TAG_ALAMAT);
					String name = c.getString(TAG_NAME);
					String lat = c.getString(TAG_LAT);
					String lng = c.getString(TAG_LNG);
					map.put("nama", name);
					map.put("alamat", alamat);
					lat2 = Double.valueOf(lat);
					lng2 = Double.valueOf(lng);
					float jarak=getDistanceInMiles(lat1, lng1, lat2, lng2);
					map.put("jarak", String.valueOf(jarak));
					map.put("lat", lat);
					map.put("lng", lng);
					mapList.add(map);
					
				}
			    Collections.sort(mapList, new Comparator<HashMap< String,String >>() {

			        @Override
			        public int compare(HashMap<String, String> lhs,
			                HashMap<String, String> rhs) {
			            // Do your comparison logic here and retrn accordingly.
			        	String firstValue = lhs.get("jarak");
			            String secondValue = rhs.get("jarak");
			            return firstValue.compareTo(secondValue);
			           
			        }
			    });
			    
			    runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed items into listview
						 * */
						ListAdapter adapter = new SimpleAdapter(viewSQL.this, mapList,
						R.layout.detailisi, new String[] { "nama", "alamat",
								"jarak", "lat", "lng" }, new int[] { R.id.nama,
								R.id.alamat, R.id.jarak, R.id.latitude, R.id.longitude });
				lv.setAdapter(adapter);
					}
			    });
				// Set JSON Data in TextView
				// uid.setText(id);
				// name1.setText(name);
				// email1.setText(email);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
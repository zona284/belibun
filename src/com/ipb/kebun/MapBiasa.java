package com.ipb.kebun;


import java.util.ArrayList;
import java.util.List;

import library.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;

public class MapBiasa extends Activity{
	private GoogleMap map;
	private LatLng latLng;
	private Double longitude;
	private Double latitude;
	private GPSTracker gpsnya;
	public String judul;

	private static String url = "http://mati.16mb.com/kebun/proses.php";
	// JSON Node Names
	private static final String TAG_USER = "kebun";
	private static final String TAG_LNG = "lng";
	private static final String TAG_LAT = "lat";
	private static final String TAG_NAMA = "nama";
	private static final String TAG_ALAMAT = "alamat";
	JSONArray usersql = null;
	
	Marker user;
	Marker tujuanmarker;
	mapDirect md;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		judul = new String("Halo");
		gpsnya = new GPSTracker(this);
		md = new mapDirect();
		if (gpsnya.canGetLocation()) {
			latitude = gpsnya.getLatitude();
			longitude = gpsnya.getLongitude();

		} else {
			gpsnya.showSettingsAlert();
			return;
		}
		latLng = new LatLng(latitude, longitude);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		// map.getMapAsync(this);
		
		map.setMyLocationEnabled(true);
//		map.setOnMyLocationChangeListener(myLocationChangeListener);
		new JSONParse().execute();
		
	}
	
	public void onMapReady(Double lat, Double longi, String nama, String alamat){
		LatLng posisi = new LatLng(lat, longi);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, 15)); 
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

		map.addMarker(new MarkerOptions()
			.position(posisi)
			.title(nama)
			.snippet(alamat));

	}
	
	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
		@Override
		public void onMyLocationChange(Location location) {
			latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			user = map.addMarker(new MarkerOptions().position(latLng));
			if (map != null) {
				user.remove();
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
			}
		}
	};
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		
		@Override
		protected void onPreExecute() {
		
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
			try {
				// Getting JSON Array
				usersql = json.getJSONArray(TAG_USER);
//				List<Marker> markers = new ArrayList<Marker>();
				for (int i = 0; i < usersql.length(); i++) {
					JSONObject c = usersql.getJSONObject(i);
					// Storing JSON item in a Variable
					Double lati = c.getDouble(TAG_LAT);
					Double longi = c.getDouble(TAG_LNG);
					String nama = c.getString(TAG_NAMA);
					String alamat = c.getString(TAG_ALAMAT);
					/*Marker marker = map.addMarker(new MarkerOptions()
	                	.title(c.getString("nama"))
	                	.position(new LatLng(lati, longi))	                
					);
					
	            markers.add(marker);*/
				onMapReady(lati, longi, nama, alamat);	
				}

				
				// Set JSON Data in TextView
				// uid.setText(id);
				// name1.setText(name);
				// email1.setText(email);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
package com.ipb.kebun;

import java.util.ArrayList;

import org.w3c.dom.Document;

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
import com.google.android.gms.maps.model.PolylineOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

public class map extends Activity {
	private GoogleMap map;
	private LatLng latLng;
	private LatLng Tujuan;
	private Double longitude;
	private Double latitude;
	private GPSTracker gpsnya;
	public String judul;

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
		Intent intent = getIntent();
		if(intent != null){
			Double lati = Double.valueOf(intent.getStringExtra("lat"));
			Double longi = Double.valueOf(intent.getStringExtra("long"));
			Tujuan = new LatLng(lati, longi);
		}
		else {
			Tujuan= new LatLng(-6.560164, 106.713638);
		}
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		// map.getMapAsync(this);
		tujuanmarker=map.addMarker(new MarkerOptions().position(Tujuan));
		user = map.addMarker(new MarkerOptions().position(latLng));		

		map.setMyLocationEnabled(true);
		//map.setOnMyLocationChangeListener(myLocationChangeListener);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(Tujuan, 15)); 
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		getDirectionMap(latLng, Tujuan);

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

	private void getDirectionMap(LatLng from, LatLng to) {
		LatLng fromto[] = { from, to };
		new LongOperation().execute(fromto);
	}

	private class LongOperation extends AsyncTask<LatLng, Void, Document> {
		@Override
		protected Document doInBackground(LatLng... params) {
			Document doc = md.getDocument(params[0], params[1],
					mapDirect.MODE_WALKING);
			return doc;
		}

		@Override
		protected void onPostExecute(Document result) {
			setResult(result);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	public void setResult(Document doc) {
		int duration1 = md.getDurationValue(doc);
		String distance1 = md.getDistanceText(doc);
		String start_address1 = md.getStartAddress(doc);
		String copy_right1 = md.getCopyRights(doc);
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(3).color(
				Color.RED);
		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}
		map.addPolyline(rectLine);
		
	}

}

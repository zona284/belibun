package com.ipb.kebun;

import com.ipb.kebun.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.Intent;

public class isidata extends Activity {
	InputStream inputStream;
	String res = "";
	public GPSTracker gpsnya;
	TextView isilatgps, isilonggps;
	EditText namaed, alamated, noed;
	Button simpan;
	double src_lat, src_long;
	String lat, lng;
	String nama, alamat, nohp;
	Button lihat;
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.isidata);
		isilatgps = (TextView) findViewById(R.id.isilat);
		isilonggps = (TextView) findViewById(R.id.isilong);
		namaed = (EditText) findViewById(R.id.isinama);
		noed = (EditText) findViewById(R.id.nohp);
		alamated = (EditText) findViewById(R.id.isialamat);
		lihat = (Button) findViewById(R.id.lihat);

		gpsnya = new GPSTracker(this);

		lihat.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						viewSQL.class);
				startActivityForResult(intent, 0);
			}
		});

		if (gpsnya.canGetLocation()) {
			src_lat = gpsnya.getLatitude();
			src_long = gpsnya.getLongitude();

			lat = Double.toString(src_lat);
			lng = Double.toString(src_long);

			isilatgps.setText("Latitude   : " + lat);
			isilonggps.setText("Longitude   : " + lng);

		} else {
			gpsnya.showSettingsAlert();
			return;
		}
	}

	public void simpan(View view) {
		lat = Double.toString(src_lat).toString();
		lng = Double.toString(src_long).toString();
		new IsiData().execute();
	}

	public class IsiData extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(isidata.this);
			pDialog.setMessage(Html
					.fromHtml("<b>Belibun</b><br/>Saving Data..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = "http://mati.16mb.com/kebun/addmasjid.php";
			String result = "";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				String nama = URLEncoder.encode(namaed.getText().toString(),
						"utf-8");
				String alamat = URLEncoder.encode(
						alamated.getText().toString(), "utf-8");
				String nohp = URLEncoder.encode(noed.getText().toString(),
						"utf-8");
				String latnya = URLEncoder.encode(lat, "utf-8");
				String lngnya = URLEncoder.encode(lng, "utf-8");
				
				url += "?nama=" + nama + "&alamat=" + alamat + "&nohp=" + nohp
						+ "&lat=" + latnya + "&lng=" + lngnya;

				HttpPost httppost = new HttpPost(url);
				HttpResponse response = httpclient.execute(httppost);
				result = convertResponseToString(response);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();

			} catch (Exception e) {
				// Do something about exceptions
				result = e.getMessage();
			}
			return result;
		}

		protected void onPostExecute(String page) {
			pDialog.dismiss();
			Toast toast = Toast.makeText(getApplicationContext(), page,
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public String convertResponseToString(HttpResponse response)
			throws IllegalStateException, IOException {

		res = "";
		StringBuffer buffer = new StringBuffer();
		inputStream = response.getEntity().getContent();
		final int contentLength = (int) response.getEntity().getContentLength(); // getting
		// content
		// length…..
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				/*
				Toast.makeText(isidata.this,
						"contentLength : " + contentLength, Toast.LENGTH_LONG)
						.show();
						*/
			}
		});

		if (contentLength < 0) {
		} else {
			byte[] data = new byte[512];
			int len = 0;
			try {
				while (-1 != (len = inputStream.read(data))) {
					buffer.append(new String(data, 0, len)); // converting to
																// string and
																// appending to
																// stringbuffer…..
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inputStream.close(); // closing the stream…..
			} catch (IOException e) {
				e.printStackTrace();
			}
			res = buffer.toString(); // converting stringbuffer to string…..

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
				}
			});
			// System.out.println("Response => " +
			// EntityUtils.toString(response.getEntity()));
		}
		return res;
	}

}

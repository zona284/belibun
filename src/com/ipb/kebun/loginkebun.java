package com.ipb.kebun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import library.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

public class loginkebun extends Activity {
	EditText username;
	EditText pass;
	Button login;
	JSONArray user = null;
	private static String url = "http://10.0.2.2/kebun/proses.php";
	public static String URL_LOGIN = "http://10.0.2.2/kebun/";
    // Server user register url
    public static String URL_REGISTER = "http://10.0.2.2/kebun";
	// JSON Node Names
	private static final String status = "accepted";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cobamain);
		username = (EditText) findViewById(R.id.editText1);
		pass = (EditText) findViewById(R.id.editText2);
		login = (Button) findViewById(R.id.button1);

		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						isidata.class);
				startActivityForResult(intent, 0);
			}
		});
		
	}
	
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(loginkebun.this);
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
				user = json.getJSONArray(status);
				for (int i = 0; i < user.length(); i++) {
					JSONObject c = user.getJSONObject(i);
					
					// Storing JSON item in a Variable
										
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

	
}

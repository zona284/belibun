package com.ipb.kebun;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class UserPanel extends Activity {
	private Button iden;
	private Button maps;
	private Button mapbiasa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userpanel);
		iden = (Button) findViewById(R.id.button1);
		iden.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						isidata.class);
				startActivityForResult(intent, 0);
			}
		});
		maps = (Button) findViewById(R.id.button2);
		maps.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						RegisterActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		
		mapbiasa = (Button) findViewById(R.id.button3);
		mapbiasa.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						MapBiasa.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

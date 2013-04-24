package com.waffles.justinthyme;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity{
	private Button test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		test = (Button) this.findViewById(R.id.testButton);
		test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				ApiRequest request = new ApiRequest("test");
				request.sendThreaded(new JITResponse(){

					@Override
					public void onResponse(JSONObject response) {
						Log.i("MainActivity", response.toString());
					}

					@Override
					public void onException(String message, Exception e) {
						Log.e("MainActivity", e.getMessage());
					}
					
				});
			}
			
		});

		// Enables Up Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go up
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

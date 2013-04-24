package com.waffles.justinthyme;

import org.json.JSONObject;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText searchField;
	Button searchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.searchButton = (Button) this.findViewById(R.id.searchButton);
		this.searchField = (EditText) this.findViewById(R.id.searchField);

		this.searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				performSearch();
			}

		});

		searchField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					performSearch();
					return true;
				}
				return false;
			}

		});

		// Enables Up Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.invalidateOptionsMenu();
	}

	protected void performSearch() {
		Intent intent = new Intent(this, SearchResultActivity.class);
		intent.putExtra("search", searchField.getText().toString().trim());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!((LoginApplication) this.getApplication()).isLoggedIn()) {
			getMenuInflater().inflate(R.menu.main, menu);
		} else {
			getMenuInflater().inflate(R.menu.logged_in, menu);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_login:
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_logout:
			((LoginApplication) this.getApplication()).logOut();
			Toast.makeText(this, "You are now logged out", Toast.LENGTH_SHORT)
					.show();
			this.invalidateOptionsMenu();
		case R.id.action_favorites:
			intent = new Intent(this, FavoritesActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

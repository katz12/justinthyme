package com.waffles.justinthyme;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button loginButton = (Button) this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				attemptLogin();
			}

		});
	}

	protected void attemptLogin() {
		EditText userNameField = (EditText) this.findViewById(R.id.userName);
		EditText passwordField = (EditText) this.findViewById(R.id.password);
		String userName = userNameField.getText().toString();
		String password = passwordField.getText().toString();

		ApiRequest request;
		try {
			request = new ApiRequest("login", "?user_name="
					+ URLEncoder.encode(userName, "UTF-8") + "&password="
					+ URLEncoder.encode(password, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.e("LoginActivity", "Unsupported URL encoding");
			return;
		}

		request.sendThreaded(new JITResponseUI(this) {

			@Override
			public void onUiResponse(JSONObject response) {
				try {
					if (response.getBoolean("success")) {
						String userName = response.getString("user_name");
						JSONArray favorites = response.getJSONArray("favorites");
						int[] fav_ids = new int[favorites.length()];
						for (int i = 0; i < favorites.length(); i++){
							fav_ids[i] = favorites.getInt(i);
						}
						logIn(userName, fav_ids);
					} else {
						rejectLogin();
					}
				} catch (JSONException e) {
					Log.e("LoginActivity", e.getLocalizedMessage());
				}
			}

		});
	}

	protected void rejectLogin() {
		Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT)
				.show();
	}

	protected void logIn(String userName, int[] fav_ids) {
		((LoginApplication) this.getApplication()).logIn(userName, fav_ids);
		Toast.makeText(this, "You are logged in.", Toast.LENGTH_SHORT).show();
		finish();
	}

}

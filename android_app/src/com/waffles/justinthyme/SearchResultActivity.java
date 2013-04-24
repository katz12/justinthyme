package com.waffles.justinthyme;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SearchResultActivity extends Activity {

	private ArrayList<Recipe> recipes;

	private ListView resultList;
	private RecipeAdapter listAdapter;
	private String searchTerm;

	protected boolean loadingMore;

	private int page;

	private View footerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		if (savedInstanceState == null) {
			recipes = new ArrayList<Recipe>();

			Intent myIntent = getIntent();
			searchTerm = myIntent.getStringExtra("search");
			page = 1;
			runSearchQuery(searchTerm);
			initializeViews();
		} else {
			recipes = savedInstanceState.getParcelableArrayList("recipes");
			searchTerm = savedInstanceState.getString("search");
			initializeViews();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		this.invalidateOptionsMenu();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putParcelableArrayList("recipes", recipes);
		bundle.putString("serach", searchTerm);
	}

	private void runSearchQuery(String searchTerm) {
		ApiRequest request;
		try {
			request = new ApiRequest("search", "?search_text="
					+ URLEncoder.encode(searchTerm, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.e("LoginActivity", "Unsupported URL encoding");
			return;
		}
		request.sendThreaded(new JITResponseUI(this) {

			@Override
			public void onUiResponse(JSONObject response) {

				prepareListView();
				try {
					JSONArray recipesJson = response.getJSONArray("recipes");
					if (recipesJson.length() < 10) {
						resultList.removeFooterView(footerView);
					}

					for (int i = 0; i < recipesJson.length(); i++) {
						JSONObject recipeJson = recipesJson.getJSONObject(i);
						Recipe recipe = new Recipe(recipeJson);
						recipes.add(recipe);
						loadImages(recipes);
						listAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					Log.e("SearchResultActivity",
							"Error reading JSON: " + e.getMessage());
				}

			}

		});
	}

	protected void loadMoreRecipes() {
		loadingMore = true;
		page++;
		footerView = getLayoutInflater().inflate(R.layout.footer_loading, null);
		resultList.invalidate();

		ApiRequest request = new ApiRequest("search", "?search_text="
				+ searchTerm + "&page=" + page);
		request.sendThreaded(new JITResponseUI(this) {

			@Override
			public void onUiResponse(JSONObject response) {
				try {
					JSONArray recipesJson = response.getJSONArray("recipes");
					if (recipesJson.length() < 10) {
						resultList.removeFooterView(footerView);
					}

					for (int i = 0; i < recipesJson.length(); i++) {
						JSONObject recipeJson = recipesJson.getJSONObject(i);
						Recipe recipe = new Recipe(recipeJson);
						recipes.add(recipe);
						loadImages(recipes);
						listAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					Log.e("SearchResultActivity",
							"Error reading JSON: " + e.getMessage());
				}
				loadingMore = false;
			}

		});
	}

	protected void prepareListView() {
		resultList = (ListView) findViewById(R.id.resultList);
		footerView = getLayoutInflater().inflate(R.layout.footer_layout, null);
		resultList.addFooterView(footerView);

		listAdapter = new RecipeAdapter(this, recipes);
		resultList.setAdapter(listAdapter);
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aView, View view,
					int position, long id) {

				if (position == recipes.size()) {
					if (!loadingMore) {
						loadMoreRecipes();
					}
				} else {

					Recipe selectedRecipe = recipes.get(position);
					Intent intent = new Intent(getBaseContext(),
							RecipeActivity.class);
					intent.putExtra("recipe", selectedRecipe);
					startActivity(intent);
				}

			}

		});
	}

	protected void loadImages(ArrayList<Recipe> recipes) {
		for (Recipe recipe : recipes) {
			recipe.fetchImage(new JITResponseUI(this) {

				@Override
				public void onUiResponse(JSONObject response) {
					listAdapter.notifyDataSetChanged();
				}

			});
		}
	}

	private void initializeViews() {
		ActionBar actionBar = getActionBar();
		if ("".equals(searchTerm)) {
			actionBar.setTitle("All recipes");
		} else {
			actionBar.setTitle("Results for \"" + searchTerm + "\"");
		}
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
			return true;
		case R.id.action_favorites:
			intent = new Intent(this, FavoritesActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

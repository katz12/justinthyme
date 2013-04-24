package com.waffles.justinthyme;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FavoritesActivity extends Activity {

	private ArrayList<Recipe> recipes;

	private ListView resultList;
	private RecipeAdapter listAdapter;

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
			page = 1;
			getFavorites();
			initializeViews();
		} else {
			recipes = savedInstanceState.getParcelableArrayList("recipes");
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
	}

	private void getFavorites() {
		LoginApplication login = (LoginApplication) this.getApplication();
		ApiRequest request = new ApiRequest("favorites", "?user_name="
				+ login.getUserName());
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

		LoginApplication login = (LoginApplication) this.getApplication();
		ApiRequest request = new ApiRequest("favorites", "?user_name="
				+ login.getUserName() + "&page=" + page);
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
		actionBar.setTitle("Favorites");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
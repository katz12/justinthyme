package com.waffles.justinthyme;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class RecipeActivity extends Activity {

	private Recipe recipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe);

		if (savedInstanceState == null) {
			Intent myIntent = getIntent();
			recipe = myIntent.getParcelableExtra("recipe");
			initializeViews();
		} else {
			recipe = savedInstanceState.getParcelable("recipe");
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
		bundle.putParcelable("recipe", recipe);
	}

	private void initializeViews() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(recipe.getName());

		final ImageView recipeImage = (ImageView) this
				.findViewById(R.id.recipeImage);
		if (recipe.getImage() != null) {
			recipeImage.setImageBitmap(recipe.getImage());
		} else {
			recipe.fetchImage(new JITResponseUI(this) {

				@Override
				public void onUiResponse(JSONObject response) {
					recipeImage.setImageBitmap(recipe.getImage());
				}

			});
		}

		TextView recipeCookTime = (TextView) this
				.findViewById(R.id.recipeCookTime);
		recipeCookTime.setText("Cook Time: " + recipe.getCookTime());

		if (!"".equals(recipe.getPrepTime())) {
			TextView recipePrepTime = (TextView) this
					.findViewById(R.id.recipePrepTime);
			recipePrepTime.setVisibility(View.VISIBLE);
			recipePrepTime.setText("Prep Time: " + recipe.getPrepTime());
		}

		if (!"".equals(recipe.getWaitTime())) {
			TextView recipeWaitTime = (TextView) this
					.findViewById(R.id.recipeWaitTime);
			recipeWaitTime.setVisibility(View.VISIBLE);
			recipeWaitTime.setText("Wait Time: " + recipe.getWaitTime());
		}

		if (!"null".equals(recipe.getServings())) {
			TextView recipeServings = (TextView) this
					.findViewById(R.id.recipeServings);
			recipeServings.setVisibility(View.VISIBLE);
			recipeServings.setText("Servings: " + recipe.getServings());
		}

		if (!"null".equals(recipe.getDifficulty())) {
			TextView recipeDifficulty = (TextView) this
					.findViewById(R.id.recipeDifficulty);
			recipeDifficulty.setVisibility(View.VISIBLE);
			recipeDifficulty.setText("Difficulty: " + recipe.getDifficulty());
		}

		if (!"null".equals(recipe.getMethod())) {
			TextView recipeMethod = (TextView) this
					.findViewById(R.id.recipeMethod);
			recipeMethod.setVisibility(View.VISIBLE);
			recipeMethod.setText("Method: " + recipe.getMethod());
		}

		if (!"null".equals(recipe.getCourse())) {
			TextView recipeCourse = (TextView) this
					.findViewById(R.id.recipeCourse);
			recipeCourse.setVisibility(View.VISIBLE);
			recipeCourse.setText("Course: " + recipe.getCourse());
		}

		String ingredientString = "";
		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredientString += ingredient.getQuantity() + " "
					+ ingredient.getName() + "\n";
		}
		TextView recipeIngredients = (TextView) this
				.findViewById(R.id.recipeIngredients);
		recipeIngredients.setText(ingredientString);

		TextView recipeDirections = (TextView) this
				.findViewById(R.id.recipeDirections);
		recipeDirections.setText(recipe.getDescription());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (((LoginApplication) this.getApplication()).isLoggedIn()) {
			if (isFavorite(recipe.getId())) {
				getMenuInflater().inflate(R.menu.logged_in_fav, menu);
			} else {
				getMenuInflater().inflate(R.menu.logged_in_not_fav, menu);
			}
		} else {
			getMenuInflater().inflate(R.menu.main, menu);
		}

		return true;
	}

	private boolean isFavorite(int id) {
		int[] fav_ids = ((LoginApplication) this.getApplication()).getFavs();
		for (int i = 0; i < fav_ids.length; i++) {
			if (id == fav_ids[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ApiRequest request;
		final LoginApplication login = (LoginApplication) this.getApplication();

		switch (item.getItemId()) {
		case R.id.action_login:
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_logout:
			login.logOut();
			Toast.makeText(this, "You are now logged out", Toast.LENGTH_SHORT)
					.show();
			this.invalidateOptionsMenu();
			return true;
		case R.id.action_favorite:
			request = new ApiRequest("favorite", "?user_name="
					+ login.getUserName() + "&fav_id=" + recipe.getId());
			request.sendThreaded(new JITResponseUI(this) {

				@Override
				public void onUiResponse(JSONObject response) {
					try {
						if (response.getBoolean("success")) {
							login.addFavorite(recipe.getId());
							Toast.makeText(getApplicationContext(),
									recipe.getName() + " added to favorites",
									Toast.LENGTH_SHORT).show();
							invalidateOptionsMenu();
						} else {
							String error = response.getString("error");
							Log.e("RecipeActivity", "Favorite add error: "
									+ error);
						}
					} catch (JSONException e) {
						Log.e("RecipeActivity", e.getLocalizedMessage());
					}
				}

			});
			return true;
		case R.id.action_unfavorite:
			request = new ApiRequest("unfavorite", "?user_name="
					+ login.getUserName() + "&fav_id=" + recipe.getId());
			request.sendThreaded(new JITResponseUI(this) {

				@Override
				public void onUiResponse(JSONObject response) {
					try {
						if (response.getBoolean("success")) {
							login.removeFavorite(recipe.getId());
							Toast.makeText(
									getApplicationContext(),
									recipe.getName()
											+ " removed from favorites",
									Toast.LENGTH_SHORT).show();
							invalidateOptionsMenu();
						} else {
							String error = response.getString("error");
							Log.e("RecipeActivity", "Favorite remove error: "
									+ error);
						}
					} catch (JSONException e) {
						Log.e("RecipeActivity", e.getLocalizedMessage());
					}
				}

			});
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

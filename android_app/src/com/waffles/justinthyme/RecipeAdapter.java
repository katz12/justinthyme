package com.waffles.justinthyme;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeAdapter extends BaseAdapter {

	private ArrayList<Recipe> recipes;
	private LayoutInflater inflater;
	private Context context;

	/**
	 * Internalizes the list of reviews and sets up the adapter.
	 * 
	 * @param c
	 *            the application context
	 * @param reviewList
	 *            the list of reviews
	 */
	public RecipeAdapter(Context c, ArrayList<Recipe> recipes) {
		context = c;
		this.recipes = recipes;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 */
	@Override
	public int getCount() {
		return recipes.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 */
	@Override
	public Object getItem(int position) {
		return recipes.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 * 
	 * <p>
	 * Not implemented.
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * Get a View that displays the data at the specified position in the data
	 * set.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.recipe_item, null);
			holder = new ViewHolder();
			holder.recipeItemImage = (ImageView) convertView.findViewById(R.id.recipeItemImage);
			holder.recipeItemTitle = (TextView) convertView
					.findViewById(R.id.recipeItemTitle);
			holder.recipeItemIngredients = (TextView) convertView.findViewById(R.id.recipeItemIngredients);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Bitmap image = recipes.get(position).getImage();
		if(image != null){
			holder.recipeItemImage.setImageBitmap(image);
		}
		else{
			holder.recipeItemImage.setImageResource(R.drawable.default_image);
		}
		
		String recipe_name = recipes.get(position).getName();
		holder.recipeItemTitle.setText(recipe_name);
		
		String ingredientString = "";
		List<Ingredient> ingredients = recipes.get(position).getSortedIngredients();
		int max_len = 75;
		for (Ingredient i : ingredients){
			String name = i.getName();
			if (recipe_name.length() + ingredientString.length() + name.length() < max_len){
				ingredientString += (ingredientString.length() == 0) ? "Ingredients: " + name : ", " + name;
			}
			else if(ingredients.indexOf(i) != ingredients.size()-1){
				int pos = recipes.get(position).getIngredients().indexOf(i);
				int remaining = recipes.get(position).getIngredients().size() - pos - 1;
				ingredientString += " and " + remaining + " more.";
				break;
			}
		}
		holder.recipeItemIngredients.setText(ingredientString);

		return convertView;
	}

	/**
	 * ViewHolder
	 * 
	 * <p>
	 * Holds the Views that are contained in an item of the list.
	 * 
	 * <p>
	 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
	 * 
	 * @author Bazaarvoice Engineering
	 */
	class ViewHolder {
		ImageView recipeItemImage;
		TextView recipeItemTitle;
		TextView recipeItemIngredients;
	}

}

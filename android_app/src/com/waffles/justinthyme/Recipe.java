package com.waffles.justinthyme;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Recipe implements Parcelable {

	// REQUIRED
	private int id;
	private String url;
	private String imgUrl;
	private String name;
	private String description;
	private String cookTime;

	// NOT REQUIRED
	private String servings;
	private String course;
	private String method;
	private String difficulty;
	private String waitTime;
	private String prepTime;

	private Bitmap image;
	private List<Ingredient> ingredients;

	public Recipe(JSONObject json) {
		try {
			this.id = json.getInt("id");
			this.url = json.getString("url");
			this.imgUrl = json.getString("img_url");
			this.name = json.getString("name");
			this.description = json.getString("description");
			this.cookTime = makeTimeString(json.getInt("cook_time"));
			JSONArray ingredientList = json.getJSONArray("ingredients");

			this.servings = json.optString("servings");
			this.course = json.optString("course");
			this.method = json.optString("method");
			this.difficulty = json.optString("difficulty");
			this.waitTime = makeTimeString(json.optInt("wait_time"));
			this.prepTime = makeTimeString(json.optInt("prep_time"));

			ingredients = new ArrayList<Ingredient>();
			for (int i = 0; i < ingredientList.length(); i++) {
				JSONObject ingredient = ingredientList.getJSONObject(i);
				String quantity = ingredient.getString("quantity");
				String name = ingredient.getString("name");
				ingredients.add(new Ingredient(quantity, name));
			}

			image = null;

		} catch (JSONException e) {
			Log.e("Recipe", "JSON missing required field: " + json.toString());
		}
	}

	private String makeTimeString(int time) {
		if (time == 0){
			return "";
		}
		int minutes = 0;
		int hours = 0;
		
		while(time > 0){
			if (time >= 60){
				time -= 60;
				hours++;
			}
			else{
				minutes = time;
				time = 0;
			}
		}
		
		String retString = "";
		if(hours == 1){
			retString += hours + " hour";
		}
		else if(hours > 1){
			retString += hours + " hours";
		}
		 
		if(minutes == 1){
			retString += (retString.length() == 0) ? minutes + " minute" : ", " + minutes + " minute";
		}
		else if(minutes > 1){
			retString += (retString.length() == 0) ? minutes + " minutes" : ", " + minutes + " minutes";
		}
		return retString;
	}

	public Recipe(int id, String url, String imgUrl, String name, String desc,
			String cookTime) {
		this.id = id;
		this.url = url;
		this.imgUrl = imgUrl;
		this.name = name;
		this.description = desc;
		this.cookTime = cookTime;

		this.servings = "";
		this.course = "";
		this.method = "";
		this.difficulty = "";
		this.waitTime = "";
		this.prepTime = "";

		image = null;
	}

	public Recipe(int id, String url, String imgUrl, String name,
			String description, String cookTime, String servings, String course,
			String method, String difficulty, String waitTime, String prepTime) {
		this.id = id;
		this.url = url;
		this.imgUrl = imgUrl;
		this.name = name;
		this.description = description;
		this.cookTime = cookTime;
		this.servings = servings;
		this.course = course;
		this.method = method;
		this.difficulty = difficulty;
		this.waitTime = waitTime;
		this.prepTime = prepTime;
	}

	public void fetchImage(final JITResponseUI listener) {
		if ("".equals(imgUrl)){
			return;
		}
		new Thread() {
			public void run() {
				image = ImageDownloader.download(imgUrl, 100, 100);
				listener.onResponse(null);
			}
		}.start();
	}

	public List<Ingredient> getSortedIngredients() {
		List<Ingredient> ingredients = this.getIngredients();
		Collections.sort(ingredients, new MyComparator());
		return ingredients;
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCookTime() {
		return cookTime;
	}

	public String getServings() {
		return servings;
	}

	public String getCourse() {
		return course;
	}

	public String getMethod() {
		return method;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public String getPrepTime() {
		return prepTime;
	}

	public Bitmap getImage() {
		return image;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCookTime(String cookTime) {
		this.cookTime = cookTime;
	}

	public void setServings(String servings) {
		this.servings = servings;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public void setPrepTime(String prepTime) {
		this.prepTime = prepTime;
	}

	/*
	 * Parcel methods and constructors
	 */

	private Recipe(Parcel parcel) {
		readFromParcel(parcel);
	}

	/**
	 * A Creator object used by the android system when unpacking a parcelled
	 * instance of a Recipe object.
	 */
	public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

		@Override
		public Recipe createFromParcel(Parcel source) {
			return new Recipe(source);
		}

		@Override
		public Recipe[] newArray(int size) {
			return new Recipe[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Writes the object to a parcel. It writes the image as a byte array, or an
	 * empty one if it is null.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(this.id);
		dest.writeString(this.url);
		dest.writeString(this.imgUrl);
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeString(this.cookTime);
		dest.writeString(this.servings);
		dest.writeString(this.course);
		dest.writeString(this.method);
		dest.writeString(this.difficulty);
		dest.writeString(this.waitTime);
		dest.writeString(this.prepTime);
		dest.writeList(ingredients);

		if (image != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			dest.writeByteArray(byteArray);
		} else {
			dest.writeByteArray(new byte[0]);
		}
	}

	/**
	 * Reads the fields of the parcelled object into this instance.
	 * 
	 * <b>Note: </b>The fields must be read in the same order they were written.
	 * 
	 * @param parcel
	 */
	public void readFromParcel(Parcel parcel) {
		this.id = parcel.readInt();
		this.url = parcel.readString();
		this.imgUrl = parcel.readString();
		this.name = parcel.readString();
		this.description = parcel.readString();
		this.cookTime = parcel.readString();
		this.servings = parcel.readString();
		this.course = parcel.readString();
		this.method = parcel.readString();
		this.difficulty = parcel.readString();
		this.waitTime = parcel.readString();
		this.prepTime = parcel.readString();
		ingredients = new ArrayList<Ingredient>();
		parcel.readList(ingredients, Ingredient.class.getClassLoader());

		byte[] byteArray = parcel.createByteArray();
		if (byteArray.length == 0) {
			image = null;
		} else {
			image = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);
		}
	}

	public class MyComparator implements Comparator<Ingredient> {
		@Override
		public int compare(Ingredient first, Ingredient second) {
			String one = first.getName();
			String two = second.getName();
			if (one.length() > two.length()) {
				return 1;
			} else if (one.length() < two.length()) {
				return -1;
			}
			return one.compareTo(two);
		}
	}

}

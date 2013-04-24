package com.waffles.justinthyme;

import android.app.Application;

public class LoginApplication extends Application {
	private boolean loggedIn = false;
	private String userName = "";
	private int[] favoriteIds = null;

	public void logIn(String name, int[] fav_ids) {
		userName = name;
		loggedIn = true;
		favoriteIds = fav_ids;
	}

	public void logOut() {
		userName = "";
		loggedIn = false;
		favoriteIds = null;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public int[] getFavs() {
		return favoriteIds;
	}

	public void addFavorite(int id) {
		int[] newIds = new int[favoriteIds.length + 1];
		for (int i = 0; i < favoriteIds.length; i++) {
			newIds[i] = favoriteIds[i];
		}
		newIds[favoriteIds.length] = id;
		favoriteIds = newIds;
	}

	public void removeFavorite(int id) {
		int[] newIds = new int[favoriteIds.length - 1];
		int index = 0;
		for (int i = 0; i < favoriteIds.length; i++) {
			if (favoriteIds[i] != id) {
				newIds[index] = favoriteIds[i];
				index++;
			}
		}
		favoriteIds = newIds;
	}
}

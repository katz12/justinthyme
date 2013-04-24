package com.waffles.justinthyme;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
	
	private String quantity;
	private String name;

	public Ingredient(String quantity, String name) {
		this.quantity = quantity;
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getName() {
		return name;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Parcel methods and constructors
	 */

	private Ingredient(Parcel parcel) {
		readFromParcel(parcel);
	}

	/**
	 * A Creator object used by the android system when unpacking a parcelled
	 * instance of a BazaarProduct object.
	 */
	public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

		@Override
		public Ingredient createFromParcel(Parcel source) {
			return new Ingredient(source);
		}

		@Override
		public Ingredient[] newArray(int size) {
			return new Ingredient[size];
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
		dest.writeString(this.quantity);
		dest.writeString(this.name);
	}

	/**
	 * Reads the fields of the parcelled object into this instance.
	 * 
	 * <b>Note: </b>The fields must be read in the same order they were written.
	 * 
	 * @param parcel
	 */
	public void readFromParcel(Parcel parcel) {
		this.quantity = parcel.readString();
		this.name = parcel.readString();
	}

}

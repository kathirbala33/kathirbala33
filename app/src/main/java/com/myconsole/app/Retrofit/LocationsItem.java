package com.myconsole.app.Retrofit;

import com.google.gson.annotations.SerializedName;

public class LocationsItem{

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("isAdded")
	private boolean isAdded;

	@SerializedName("longitude")
	private String longitude;

	public String getLatitude(){
		return latitude;
	}

	public boolean isIsAdded(){
		return isAdded;
	}

	public String getLongitude(){
		return longitude;
	}
}
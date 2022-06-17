package com.myconsole.app.Retrofit;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("notes")
	private String notes;

	@SerializedName("mood")
	private Object mood;

	@SerializedName("isPrivate")
	private boolean isPrivate;

	@SerializedName("updatedDate")
	private String updatedDate;

	@SerializedName("userId")
	private String userId;

	@SerializedName("isDeleted")
	private boolean isDeleted;

	@SerializedName("smokes")
	private int smokes;

	@SerializedName("recipients")
	private Object recipients;

	@SerializedName("alertRecipients")
	private Object alertRecipients;

	@SerializedName("isAlertSocket")
	private boolean isAlertSocket;

	@SerializedName("locations")
	private List<LocationsItem> locations;

	@SerializedName("id")
	private String id;

	@SerializedName("startDate")
	private String startDate;

	public String getNotes(){
		return notes;
	}

	public Object getMood(){
		return mood;
	}

	public boolean isIsPrivate(){
		return isPrivate;
	}

	public String getUpdatedDate(){
		return updatedDate;
	}

	public String getUserId(){
		return userId;
	}

	public boolean isIsDeleted(){
		return isDeleted;
	}

	public int getSmokes(){
		return smokes;
	}

	public Object getRecipients(){
		return recipients;
	}

	public Object getAlertRecipients(){
		return alertRecipients;
	}

	public boolean isIsAlertSocket(){
		return isAlertSocket;
	}

	public List<LocationsItem> getLocations(){
		return locations;
	}

	public String getId(){
		return id;
	}

	public String getStartDate(){
		return startDate;
	}
}
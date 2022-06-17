package com.myconsole.app.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentsItem {

    @SerializedName("moodId")
    private String moodId;

    @SerializedName("notes")
    private String notes;

    @SerializedName("isDeleted")
    private boolean isDeleted;

    @SerializedName("smokes")
    private int smokes;

    @SerializedName("locations")
    private List<LocationsItem> locations;

    @SerializedName("id")
    private String id;

    @SerializedName("isPrivate")
    private boolean isPrivate;

    @SerializedName("userId")
    private String userId;

    @SerializedName("startDate")
    private String startDate;

    public DocumentsItem(String toString, boolean b, boolean b1, String s, String s1, String s2, int i, String date, String userID) {
        this.id = toString;
        this.isPrivate = b;
        this.isDeleted = b1;
        this.locations = null;
        this.moodId = s1;
        this.smokes = i;
        this.startDate = date;
        this.userId = userID;
    }

    public String getMoodId() {
        return moodId;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public int getSmokes() {
        return smokes;
    }

    public List<LocationsItem> getLocations() {
        return locations;
    }

    public String getId() {
        return id;
    }

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public String getUserId() {
        return userId;
    }

    public String getStartDate() {
        return startDate;
    }
}
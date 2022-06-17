package com.myconsole.app.Retrofit;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PostResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("errorMessage")
	private String errorMessage;

	@SerializedName("reasonCode")
	private int reasonCode;

	public List<DataItem> getData(){
		return data;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public int getReasonCode(){
		return reasonCode;
	}
}
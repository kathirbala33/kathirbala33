package com.myconsole.app.Retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("end-url")
    Call<PostResponse> postApi(@Body PostRequest postRequest);

    @GET
    Call<JsonObject> getApi(@Url String url);
}

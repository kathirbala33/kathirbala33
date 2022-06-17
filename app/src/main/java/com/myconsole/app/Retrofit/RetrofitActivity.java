package com.myconsole.app.Retrofit;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.myconsole.app.commonClass.DateUtils;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.ActivityRetrofitBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    private ActivityRetrofitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialView();
    }

    private void initialView() {
        binding.postApiButton.setOnClickListener(v -> callPostApi());
        binding.getApiButton.setOnClickListener(v -> callGetApi());
    }

    private void callPostOkhClientApi() {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        DocumentsItem postRequest = new DocumentsItem(UUID.randomUUID().toString(), false, true, "", "", "", 5, "date", "820e973f-8295-4c8f-921c-7d0c48bcb5ca");
        List<DocumentsItem> documentsItems = new ArrayList<>();
        documentsItems.add(postRequest);
        PostRequest postRequest1 = new PostRequest(documentsItems);
        String json = new Gson().toJson(postRequest1);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("url").header("", "")
                .post(body)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {

            }
        });

    }


    private void callGetApiUsingOkhClient() {
        String url = "get api url";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).header("Authorization", "token")
                .header("DOMAIN_KEY", "DOMAIN_KEY").
                header("tenant_key", "tenant_key").
                header("tenantId", String.valueOf(6)).
                header("SessionId", "SessionId").
                header("Source", "Mobile").
                header("ipaddress", "ipaddress")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Utils.printLog("##OkhGetApi", "onFailure");
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                Utils.printLog("##OkhGetApi", "onResponse");
            }
        });

    }

    private void callGetApi() {
        ApiInterface apiInterface = getRetrofit();
        Call<JsonObject> call = apiInterface.getApi("get api url");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void callPostApi() {
        String date = DateUtils.getCurrentDateUtcFormate();
        DocumentsItem postRequest = new DocumentsItem(UUID.randomUUID().toString(), false, true, "", "", "", 5, date, "820e973f-8295-4c8f-921c-7d0c48bcb5ca");
        List<DocumentsItem> documentsItems = new ArrayList<>();
        documentsItems.add(postRequest);
        ApiInterface apiInterface = getRetrofit();
        PostRequest postRequest1 = new PostRequest(documentsItems);
        Call<PostResponse> call = apiInterface.postApi(postRequest1);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
    }

    private ApiInterface getRetrofit() {
        ApiInterface className;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("base-url").
                addConverterFactory(GsonConverterFactory.create()).client(getSafeOkHttpClientNewUser()).build();
        className = retrofit.create(ApiInterface.class);
        return className;
    }

    public static OkHttpClient getSafeOkHttpClientNewUser() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.connectTimeout(2, TimeUnit.MINUTES);
                builder.readTimeout(2, TimeUnit.MINUTES);
                builder.writeTimeout(2, TimeUnit.MINUTES);
                builder.protocols(Util.immutableListOf(Protocol.HTTP_1_1));
                builder.hostnameVerifier((hostname, session) -> true);
                builder.addNetworkInterceptor(logging);
                builder.addInterceptor(chain -> {
                    Request requestMain = chain.request();
                    Request request = chain.request();
                    requestMain = chain.request().newBuilder().
                            header("Authorization", "token")
                            .header("DOMAIN_KEY", "DOMAIN_KEY").
                            header("tenant_key", "tenant_key").
                            header("tenantId", String.valueOf(6)).
                            header("SessionId", "SessionId").
                            header("Source", "Mobile").
                            header("ipaddress", "ipaddress")
                            .build();
                    okhttp3.Response response = chain.proceed(requestMain);
                    Utils.printLog("##APIBASE", "------------>" + response.code());
                    return response;
                });
                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .addNetworkInterceptor(logging)
                    .addInterceptor(chain -> {
                        Request requestMain = chain.request();
                        Request request = chain.request();
//                        String location = Utils.getLocationValues(isDeviceGPSEnabledAndLocationPermission(context), preferencesManager);
//                        requestMain = !isDeviceGPSEnabledAndLocationPermission(context) ? chain.request().newBuilder().header(AUTHORIZATION, Utils.getAuthorizationToken(preferencesManager)).header(DOMAIN_KEY, Utils.getDomainName()).header(TENANT_KEY, Utils.getTenantKey()).header(TENANT_ID_HEADER, String.valueOf(Utils.getTenantHeader())).header(SESSION_ID, Utils.getSessionId()).header(SOURCE, MOBILE).header(IPADDRESS, ApiUtils.getIPAddress(context)).build() : chain.request().newBuilder().header(AUTHORIZATION, Utils.getAuthorizationToken(preferencesManager)).header(DOMAIN_KEY, Utils.getDomainName()).header(TENANT_KEY, Utils.getTenantKey()).header(TENANT_ID_HEADER, String.valueOf(Utils.getTenantHeader())).header(SESSION_ID, Utils.getSessionId()).header(SOURCE, MOBILE).header(IPADDRESS, ApiUtils.getIPAddress(context)).header(LOCATION, location).build();
//                        okhttp3.Response response = chain.proceed(requestMain);
//                        Utils.printLogConsole("##APIBASE", "------------>" + response.code());

                        return null;
                    }).protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();
        }
    }
}
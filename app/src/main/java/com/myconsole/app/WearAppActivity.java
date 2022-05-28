package com.myconsole.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.myconsole.app.databinding.ActivityAppwearActivityBinding;
import com.myconsole.app.service.AppWearService;

import java.util.Calendar;

public class WearAppActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private ActivityAppwearActivityBinding binding;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppwearActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(v -> {
            connectWithWearOS();
        });
        connectClient();
    }

    private void connectClient() {
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API)
                .addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("##WearOs", "onConnected");
        connectService();
    }

    private void connectService() {
        Intent intent = new Intent(this, AppWearService.class);
        startService(intent);
    }

    private void connectWithWearOS() {
        boolean isConnected = googleApiClient.hasConnectedApi(Wearable.API);
        String values = binding.editTextView.getText().toString();

        if (isConnected) {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/Data_from_app_to_wear");
            DataMap dataMap = putDataMapRequest.getDataMap();
            dataMap.putString("SampleDATA", values);
            dataMap.putLong("DATE", Calendar.getInstance().getTimeInMillis());
            PutDataRequest request = putDataMapRequest.asPutDataRequest();
            request.setUrgent();
            Wearable.DataApi.putDataItem(googleApiClient, request)
                    .setResultCallback(dataItemResult -> {
                        if (dataItemResult.getStatus().isSuccess()) {
                            Toast.makeText(this, "isSuccess", Toast.LENGTH_SHORT).show();
                            Log.d("##WearOs", "isSuccess");
                        } else {
                            Log.d("##WearOs", "Error");
                        }
                    });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("##WearOs", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("##WearOs", "onConnectionFailed");
    }
}
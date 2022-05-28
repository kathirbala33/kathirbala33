package com.kt.wearconsole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.kt.wearconsole.databinding.ActivityOsWearMainBinding;

import java.util.Calendar;

public class WearOSMainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ActivityOsWearMainBinding binding;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOsWearMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addApi(Wearable.API)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
        binding.button.setOnClickListener(view -> connectWithApp());

    }

    private void connectWithApp() {
        boolean isConnected = googleApiClient.hasConnectedApi(Wearable.API);
        String values = binding.editTextView.getText().toString();
        binding.editTextView.setText("");
        if (isConnected) {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/Data_from_wear_to_app");
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
    protected void onStart() {

        super.onStart();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, WearOSDataService.class);
        startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }
}
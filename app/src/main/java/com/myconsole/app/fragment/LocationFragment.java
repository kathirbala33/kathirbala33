package com.myconsole.app.fragment;

import static com.myconsole.app.commonClass.Utils.printLog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.myconsole.app.R;

public class LocationFragment extends Fragment {
    private Context context;
    public static int LOCATION_PERMISSION_CODE = 1;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment, container);
        initializeUI();
        return view;

    }

        private void initializeUI() {
            context = getActivity();
            checkLocationPermission();
        }
    
        private void checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            } else {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    printLog("##LocationEnable", "--" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
    
                        }
    
                        @Override
                        public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                            super.onLocationAvailability(locationAvailability);
                        }
                    };
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                        if (location != null) {
                            double lan = location.getLatitude();
                            double lon = location.getLongitude();
                            printLog("##LocationLan", "--" + lan);
                            printLog("##Locationlong", "--" + lon);
                        }
    
                    }).addOnFailureListener(e -> {
                        printLog("##LocationOnFailure", "--" + e);
                    });
                } else {
                    printLog("##LocationEnable", "--" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
                }
            }
        }
}

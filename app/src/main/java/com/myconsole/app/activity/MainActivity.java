package com.myconsole.app.activity;

import static com.myconsole.app.fragment.LocationFragment.LOCATION_PERMISSION_CODE;
import static com.myconsole.app.commonClass.Utils.printLog;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.myconsole.app.fragment.LocationFragment;
import com.myconsole.app.fragment.MapsFragment;
import com.myconsole.app.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private FrameLayout mainFragmentLayout;
    private TextView locationTextView, googleFitTextView, linkTextView;
    private ImageView menuImageView, backArrowImageView,centerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initializeUI();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            printLog("##Location", "Granted");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initializeUI() {
        drawerLayout = findViewById(R.id.drawLayoutMain);
        menuImageView = findViewById(R.id.menuImageView);
        locationTextView = findViewById(R.id.locationTextView);
        googleFitTextView = findViewById(R.id.googleFitTextView);
        linkTextView = findViewById(R.id.linkTextView);
        mainFragmentLayout = findViewById(R.id.mainFragmentLayout);
        backArrowImageView = findViewById(R.id.backArrowImageView);
        centerImageView= findViewById(R.id.centerImageView);
        menuImageView.setOnClickListener(this);
        locationTextView.setOnClickListener(this);
        googleFitTextView.setOnClickListener(this);
        linkTextView.setOnClickListener(this);
        backArrowImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menuImageView) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.locationTextView) {
            commitFragments(0);
        } else if (v.getId() == R.id.googleFitTextView) {
            commitFragments(0);
        } else if (v.getId() == R.id.linkTextView) {
            commitFragments(0);
        } else if (v.getId() == R.id.backArrowImageView) {
            onBackPressed();
        }

    }

    private void commitFragments(int fragmentID) {
        Fragment fragment = null;
        centerImageView.setVisibility(View.GONE);
        mainFragmentLayout.setVisibility(View.VISIBLE);
        drawerLayout.closeDrawers();
        drawerLayout.closeDrawer(GravityCompat.START, true);
        if (fragmentID == 0) {
            fragment = new MapsFragment();
        } else if (fragmentID == 1) {
            fragment = new LocationFragment();
        } else {
            fragment = new LocationFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragmentLayout, fragment);
        fragmentTransaction.commit();
    }
}
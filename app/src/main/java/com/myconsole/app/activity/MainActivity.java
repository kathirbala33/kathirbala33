package com.myconsole.app.activity;

import static com.myconsole.app.commonClass.Utils.printLog;
import static com.myconsole.app.fragment.LocationFragment.LOCATION_PERMISSION_CODE;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.myconsole.app.R;
import com.myconsole.app.databinding.ActivityMainBinding;
import com.myconsole.app.fragment.LocationFragment;
import com.myconsole.app.fragment.MapsFragment;
import com.myconsole.app.fragment.link.LinkFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        binding.menuImageView.setOnClickListener(this);
        binding.locationTextView.setOnClickListener(this);
        binding.googleFitTextView.setOnClickListener(this);
        binding.linkTextView.setOnClickListener(this);
        binding.backArrowImageView.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragmentName = getSupportFragmentManager().findFragmentById(R.id.mainFragmentLayout);
        getClass().getSimpleName();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menuImageView) {
            binding.drawLayoutMain.openDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.locationTextView) {
            commitFragments(0);
        } else if (v.getId() == R.id.googleFitTextView) {
            commitFragments(1);
        } else if (v.getId() == R.id.linkTextView) {
            commitFragments(0);
        } else if (v.getId() == R.id.backArrowImageView) {
            onBackPressed();
        }

    }

    private void commitFragments(int fragmentID) {
        Fragment fragment = null;
        binding.centerImageView.setVisibility(View.GONE);
        binding.mainFragmentLayout.setVisibility(View.VISIBLE);
        binding.drawLayoutMain.closeDrawers();
        binding.drawLayoutMain.closeDrawer(GravityCompat.START, true);
        if (fragmentID == 0) {
            fragment = new MapsFragment();
        } else if (fragmentID == 1) {
            fragment = new LinkFragment();
        } else {
            fragment = new LocationFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragmentLayout, fragment);
        fragmentTransaction.commit();
    }
}
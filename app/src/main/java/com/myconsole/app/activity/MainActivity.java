package com.myconsole.app.activity;

import static com.myconsole.app.ListenerConstant.VITAL_GRAPH_VIEW_NAVIGATION;
import static com.myconsole.app.ListenerConstant.VITAL_NAVIGATION;
import static com.myconsole.app.commonClass.Utils.printLog;
import static com.myconsole.app.fragment.LocationFragment.LOCATION_PERMISSION_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.myconsole.app.Listener;
import com.myconsole.app.ListenerConstant;
import com.myconsole.app.R;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.ActivityMainBinding;
import com.myconsole.app.fragment.MapsFragment;
import com.myconsole.app.fragment.PickerFragment;
import com.myconsole.app.fragment.bluetooth.BluetoothFragment;
import com.myconsole.app.fragment.googleFit.GoogleFitFragment;
import com.myconsole.app.fragment.googleFit.VitalFragment;
import com.myconsole.app.fragment.googleFit.VitalGraphFragment;
import com.myconsole.app.fragment.link.LinkFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Listener {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapsFragment.REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(this, "GPS is tured on", Toast.LENGTH_SHORT).show();
                    commitFragments(0, "");
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS required to be tured on", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    private void initializeUI() {
        binding.menuImageView.setOnClickListener(this);
        binding.locationTextView.setOnClickListener(this);
        binding.googleFitTextView.setOnClickListener(this);
        binding.linkTextView.setOnClickListener(this);
        binding.backArrowImageView.setOnClickListener(this);
        binding.bluetoothTextView.setOnClickListener(this);
        binding.pickerTextView.setOnClickListener(this);
//        RunAnimation();
    }

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.animation);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.firstTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        String fragmentName = getSupportFragmentManager().findFragmentById(R.id.mainFragmentLayout).getClass().getSimpleName();
        if (!fragmentName.isEmpty()) {
            if (fragmentName.equals("VitalFragment") || fragmentName.equals("VitalGraphFragment")) {
                commitFragments(2, "");
            }else{
                this.recreate();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menuImageView) {
            binding.drawLayoutMain.openDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.locationTextView) {
            commitFragments(0, "");
        } else if (v.getId() == R.id.googleFitTextView) {
            commitFragments(2, "");
        } else if (v.getId() == R.id.linkTextView) {
            commitFragments(1, "");
        }else if (v.getId() == R.id.bluetoothTextView) {
            commitFragments(5, "");
        }else if (v.getId() == R.id.pickerTextView) {
            commitFragments(6, "");
        } else if (v.getId() == R.id.backArrowImageView) {
            onBackPressed();
        }
    }
    public static void visible(Intent intent, Context context) {
        Utils.printLog("CheckNavigate", "yes");
        if(intent.getComponent().getShortClassName().contains("MapsActivity")){
            Intent setIntent = new Intent(context, MapsActivity.class);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.printLog("CheckNavigatess", "yes");
            context.startActivity(setIntent);
        }
      /*  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
    }
    private void commitFragments(int fragmentID, String vitalName) {
        Fragment fragment = null;
        binding.centerImageView.setVisibility(View.GONE);
        binding.mainFragmentLayout.setVisibility(View.VISIBLE);
        binding.drawLayoutMain.closeDrawers();
        binding.drawLayoutMain.closeDrawer(GravityCompat.START, true);
        Bundle bundle = new Bundle();
        if (fragmentID == 0) {
            fragment = new MapsFragment();
        } else if (fragmentID == 1) {
            fragment = new LinkFragment();
        } else if (fragmentID == 2) {
            fragment = new GoogleFitFragment();
        } else if (fragmentID == 3) {
            bundle.putString("VitalName", vitalName);
            fragment = new VitalFragment();
            fragment.setArguments(bundle);
        } else if (fragmentID == 4) {
            bundle.putString("VitalName", vitalName);
            fragment = new VitalGraphFragment();
            fragment.setArguments(bundle);
        }else if (fragmentID == 5) {
            fragment = new BluetoothFragment();
        }else if (fragmentID ==6) {
            fragment = new PickerFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.replace(R.id.mainFragmentLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void listenerData(int action, Object data) {
        if (action == 1) {
            Toast.makeText(this, "Navigated", Toast.LENGTH_SHORT).show();
        } else if (action == VITAL_NAVIGATION) {
            String vitalName = (String) data;
            commitFragments(3, vitalName);
        } else if (action == VITAL_GRAPH_VIEW_NAVIGATION) {
            String name = (String) data;
            commitFragments(4, name);
        }
    }
}
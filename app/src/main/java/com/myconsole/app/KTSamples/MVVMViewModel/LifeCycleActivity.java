package com.myconsole.app.KTSamples.MVVMViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.myconsole.app.R;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.ActivityLifeCycleBinding;

public class LifeCycleActivity extends AppCompatActivity {
private ActivityLifeCycleBinding binding;
    private Model model = new Model();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.printLog("##LifeCycleActivity","---->onCreate");
        super.onCreate(savedInstanceState);
        binding = ActivityLifeCycleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.commit.setOnClickListener(v -> {
            setModelView();
            Fragment fragment = new LifeCycleFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (fragment != null) {
                fragmentTransaction.replace(R.id.frameLayout, fragment);
            }
            fragmentTransaction.commit();
        });
        binding.commitTwo.setOnClickListener(v -> {
            Fragment fragment = new MVVMFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (fragment != null) {
                fragmentTransaction.replace(R.id.frameLayout, fragment);
            }
            fragmentTransaction.commit();
        });

    }
    private void setModelView() {
//        model = new ViewModelProvider(this).get(Model.class);
        model = ViewModelProviders.of(this).get(Model.class);
        model.setTextValues("hai");
    }
    @Override
    protected void onStart() {
        Utils.printLog("##LifeCycleActivity","---->onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Utils.printLog("##LifeCycleActivity","---->onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Utils.printLog("##LifeCycleActivity","---->onRestart");
        super.onRestart();
    }


    @Override
    protected void onPause() {
        Utils.printLog("##LifeCycleActivity","---->onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Utils.printLog("##LifeCycleActivity","---->onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Utils.printLog("##LifeCycleActivity","---->onDestroy");
        super.onDestroy();
    }
}
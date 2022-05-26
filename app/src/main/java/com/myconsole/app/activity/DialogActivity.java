package com.myconsole.app.activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.myconsole.app.databinding.ActivityDialogBinding;
import com.myconsole.app.databinding.ActivityMainBinding;

public class DialogActivity extends AppCompatActivity {

    private ActivityDialogBinding binding;
    private String pathss = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }


    private void intit() {
       /* binding.dialogVideoView.setVideoPath(pathss);
        binding.dialogVideoView.start();*/
    }
}
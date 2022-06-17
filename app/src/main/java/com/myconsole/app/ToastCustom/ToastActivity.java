package com.myconsole.app.ToastCustom;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myconsole.app.databinding.ActivityToastBinding;

public class ToastActivity extends AppCompatActivity {
    private ActivityToastBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.normalToast.setOnClickListener(v -> Toast.makeText(this, "Normal", Toast.LENGTH_SHORT).show());
        binding.topToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getBaseContext());
                toast.setGravity(Gravity.TOP,0,0);
//                toast.("top");
                toast.show();

            }
        });

    }
}
package com.myconsole.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.myconsole.app.R;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);

        });
        Utils.printLog("CheckSplash", "yes");
    }

    public static void visible(Intent intent, Context context) {
        Utils.printLog("CheckNavigate", "yes");
        if (intent.getComponent().getShortClassName().contains("MapsActivity")) {
            Intent setIntent = new Intent(context, MapsActivity.class);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.printLog("CheckNavigatess", "yes");
            context.startActivity(setIntent);
        }
    }

}
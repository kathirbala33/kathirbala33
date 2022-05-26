package com.myconsole.app.KTSamples.sampleBroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.myconsole.app.R;


public class BroadCastReciverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast_reciver);
        registerReceiver(receiver, filters());
        setIntentBroadCast();
    }


    private IntentFilter filters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("BROADCAST");
        return filter;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("##BroadcastReceiver", "onReceive");
        }
    };

    private void setIntentBroadCast() {
        Intent intent = new Intent("BROADCAST");
        sendBroadcast(intent);
    }

}
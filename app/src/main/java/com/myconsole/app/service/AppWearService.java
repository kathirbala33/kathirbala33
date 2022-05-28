package com.myconsole.app.service;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class AppWearService extends WearableListenerService implements DataApi.DataListener, MessageApi.MessageListener {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent dataEvent : dataEventBuffer) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equals("/Data_from_wear_to_app")) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
                    String values = dataMapItem.getDataMap().getString("SampleDATA");
                    Toast.makeText(this, values, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onDataChanged(dataEventBuffer);
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        Toast.makeText(this, "onMessageReceived", Toast.LENGTH_SHORT).show();
        super.onMessageReceived(messageEvent);
    }
}

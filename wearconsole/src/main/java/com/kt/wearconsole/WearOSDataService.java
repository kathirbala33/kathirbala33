package com.kt.wearconsole;

import android.util.Log;
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

public class WearOSDataService extends WearableListenerService implements DataApi.DataListener, MessageApi.MessageListener {
    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        Log.d("##WearOs", "onMessageReceived");
        Toast.makeText(this, "onMessageReceived", Toast.LENGTH_SHORT).show();
        super.onMessageReceived(messageEvent);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        Log.d("##WearOs", "onDataChanged");
        for (DataEvent dataEvent : dataEventBuffer) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equals("/Data_from_app_to_wear")) {
                    DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                    String values = dataMap.getString("SampleDATA");
                    Toast.makeText(this, values, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onDataChanged(dataEventBuffer);
    }
}

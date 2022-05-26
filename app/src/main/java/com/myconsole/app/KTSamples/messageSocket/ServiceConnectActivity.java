package com.myconsole.app.KTSamples.messageSocket;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import com.myconsole.app.R;

import java.util.Random;

public class ServiceConnectActivity extends AppCompatActivity {
    private static TextView textView;
    private static String finaltext = "firstTime";
    private static boolean isShow;


    public static class MyService extends Service {
        @Override
        public void onCreate() {
            Log.d("##onService", "onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("##onService", "onStartCommand");
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            textView.setText("value1234");
            isShow = true;
            finaltext = "LatestValues";
            Log.d("##onService", "onBind");
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        Intent intent = new Intent(this, MyService.class);
        if (!isShow) {
            stopService(intent);
        }
        Log.d("##onDestroy", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_connect);
        initialView();
    }

    @SuppressLint("RestrictedApi")
    private void initialView() {
        textView = findViewById(R.id.textView);
        textView.setText(finaltext);
        Log.d("##initialView", "initialView");
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, connection, 0);
        if (!isShow) {
            setNotification();
        }
    }

    private void setNotification() {
        Intent intent = new Intent(this, ServiceConnectActivity.class);
        PendingIntent aPendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri defRingToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_Id", "channel_Name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.enableLights(true);
            channel.setSound(defRingToneUri,
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build());
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_Id");
        builder.setContentTitle("Timmer");
        builder.setContentText("");
        builder.setContentIntent(aPendingIntent);
        builder.setSound(defRingToneUri);
        @SuppressLint("RemoteViewLayout")
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_design);
        remoteViews.setTextViewText(R.id.noitficationTitle, "Timmer");
        remoteViews.setTextViewText(R.id.noitficationBody, "");
        remoteViews.setChronometer(R.id.chronometer, SystemClock.elapsedRealtime(), null, true);
        builder.setCustomContentView(remoteViews);
        builder.setSmallIcon(R.drawable.logo);
        builder.setSubText("Chronometer");
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
        Random random = new Random(555);
        notificationManager.notify(random.nextInt(), builder.build());
    }



    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("##onService", "onServiceConnected");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("##onService", "onServiceDisconnected");
        }
    };


}
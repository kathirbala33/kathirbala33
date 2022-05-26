package com.myconsole.app.KTSamples;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


import com.myconsole.app.KTSamples.messageSocket.ServiceConnectActivity;
import com.myconsole.app.R;

import java.util.Random;

public class LocalReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("##Time_LocalReciever", "onReceive");
        setNotification(context, intent);
    }

    private void setNotification(Context context, Intent alertIntent) {
        String title = "";
        String body = "";
        if (alertIntent != null) {
            title = String.valueOf(alertIntent.getExtras().get("Title"));
            body = String.valueOf(alertIntent.getExtras().get("Body"));
            Intent intent = new Intent(context, ServiceConnectActivity.class);
            PendingIntent aPendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Uri defRingToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // I would suggest that you use IMPORTANCE_DEFAULT instead of IMPORTANCE_HIGH
                NotificationChannel channel = new NotificationChannel("channel_Id_Local", "channel_Name_Local", NotificationManager.IMPORTANCE_HIGH);
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
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_Id");
            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setContentIntent(aPendingIntent);
            builder.setSound(defRingToneUri);
            @SuppressLint("RemoteViewLayout")
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_design);
            remoteViews.setTextViewText(R.id.noitficationTitle, title);
            remoteViews.setTextViewText(R.id.noitficationBody, body);
            builder.setCustomContentView(remoteViews);
            builder.setSmallIcon(R.drawable.logo);
            builder.setSubText("Local Message");
            builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
//        builder.setCustomBigContentView()
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
            builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(body));
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
            Random random = new Random(555);
            notificationManager.notify(random.nextInt(), builder.build());
        }
    }
}

package com.myconsole.app.KTSamples.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myconsole.app.KTSamples.messageSocket.ServiceConnectActivity;
import com.myconsole.app.R;

import java.util.Random;

public class MessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("##onMessageReceived", "get");
        if (message.getNotification() != null) {
            setNotification(message.getNotification());
        }
    }

    private void setNotification(RemoteMessage.Notification notification) {

        Intent intent = new Intent(this, ServiceConnectActivity.class);
        /*Bundle bundle = new Bundle();
        bundle.putString("Values",notification.getBody());
        intent.putExtras(bundle);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);*/
        PendingIntent aPendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri defRingToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // I would suggest that you use IMPORTANCE_DEFAULT instead of IMPORTANCE_HIGH
            NotificationChannel channel = new NotificationChannel("channel_Id", "channel_Name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.enableLights(true);
            channel.setSound(defRingToneUri,
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build());
            //channel.canShowBadge();
            // Did you mean to set the property to enable Show Badge?
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_Id");
        builder.setContentTitle(notification.getTitle());
        builder.setContentText(notification.getBody());
        builder.setContentIntent(aPendingIntent);
        builder.setSound(defRingToneUri);
        @SuppressLint("RemoteViewLayout")
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_design);
        remoteViews.setTextViewText(R.id.noitficationTitle, notification.getTitle());
        remoteViews.setTextViewText(R.id.noitficationBody, notification.getBody());
        builder.setCustomContentView(remoteViews);
        builder.setSmallIcon(R.drawable.logo);
        builder.setSubText("FireBase Message");
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);

//        builder.setCustomBigContentView()
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
        builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(notification.getBody()));
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
        Random random = new Random(555);
        notificationManager.notify(random.nextInt(), builder.build());

    }

}

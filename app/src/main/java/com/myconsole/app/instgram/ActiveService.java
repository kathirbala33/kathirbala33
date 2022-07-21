package com.myconsole.app.instgram;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.myconsole.app.R;
import com.myconsole.app.instgram.util.Events;


public class ActiveService extends Service {

    private RemoteViews rv;
    private int CHANNEL_ID = 104;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private String NOTIFICATION_CHANNEL_ID = "action_service";
    public static final String ACTION_SERVICE_START = "com.action.serviceStart";
    public static final String ACTION_SERVICE_STOP = "com.action.serviceStop";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("##SERActiveService", "Trigered");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setTicker(getResources().getString(R.string.downloading));
        builder.setWhen(System.currentTimeMillis());
        builder.setOnlyAlertOnce(true);

        rv = new RemoteViews(getPackageName(), R.layout.service_start_notification);

        Intent intentStop = new Intent(this, ActiveService.class);
        intentStop.setAction(ACTION_SERVICE_STOP);
        PendingIntent closeIntent = PendingIntent.getService(this, 0, intentStop, 0);
        rv.setOnClickPendingIntent(R.id.button_stop, closeIntent);

        builder.setCustomContentView(rv);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getResources().getString(R.string.app_name);// The user-visible name of the channel.
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
//        notificationManager.notify(CHANNEL_ID,builder.build());
        startForeground(CHANNEL_ID, builder.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("##SERActiveServiceonStartCommand", "Trigered");
        try {
            if (intent.getAction() != null && intent.getAction().equals(ACTION_SERVICE_START)) {
                Log.d("##SERActiveServiceonStartCommand", "ACTION_SERVICE_START");
                Intent intent_service_start = new Intent(getApplicationContext(), GetAppService.class);
                startService(intent_service_start);
            }
            if (intent.getAction() != null && intent.getAction().equals(ACTION_SERVICE_STOP)) {
                Log.d("##SERActiveServiceonStartCommand", "ACTION_SERVICE_STOP");
                stopForeground(false);
                stopSelf();
                Events.ServiceNotify serviceNotify = new Events.ServiceNotify("");
//                GlobalBus.getBus().post(serviceNotify);
            }
        } catch (Exception e) {
            Log.d("##SERActiveServiceonStartCommand", "Exception"+e.getMessage());
            stopForeground(false);
            stopSelf();
        }

        return START_STICKY;
    }

}

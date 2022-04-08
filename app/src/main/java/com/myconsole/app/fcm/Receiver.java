package com.myconsole.app.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.myconsole.app.activity.MainActivity;
import com.myconsole.app.activity.MapsActivity;
import com.myconsole.app.activity.SplashActivity;
import com.myconsole.app.commonClass.Utils;

public class Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "onReceive", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals("action")) {
            Utils.printLog("CheckonReceive", "yes");
            Intent setIntent = new Intent(context, MapsActivity.class);
            context.startService(setIntent);
//            SplashActivity.visible(setIntent,context);
           /* setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(setIntent);*/
        }
    }
    public static String checkName() {
        return "hai";
    }
}

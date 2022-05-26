package com.myconsole.app.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myconsole.app.commonClass.Utils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    public static Socket socket;

    @Override
    public void onCreate() {
        super.onCreate();
        socketInit();
    }

    private void socketInit() {
        try {
            socket = IO.socket("http://chat.socket.io");
            Log.d("##socket", "inital");
        } catch (URISyntaxException e) {
            Log.d("##socketEx", e.getMessage());
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Utils.printLog("CheckonCreate", "yes");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Utils.printLog("CheckonActivityStarted", "yes");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Utils.printLog("CheckonActivityResumed", "yes");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Utils.printLog("CheckonActivityPaused", "yes");
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Utils.printLog("CheckonActivityStopped", "yes");
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Utils.printLog("CheckonActivityDestroyed", "yes");
    }
}

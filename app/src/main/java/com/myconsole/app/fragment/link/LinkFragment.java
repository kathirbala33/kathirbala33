package com.myconsole.app.fragment.link;

import static android.view.View.GONE;
import static com.myconsole.app.ListenerConstant.FRAGMENT_NAME;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.myconsole.app.Listener;
import com.myconsole.app.R;
import com.myconsole.app.activity.SplashActivity;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.LinkPreviewFrgmentBinding;
import com.myconsole.app.fcm.Receiver;
import com.myconsole.app.fragment.link.linkPreview.LinkPreview;
import com.myconsole.app.fragment.link.linkPreview.MetaData;
import com.myconsole.app.fragment.link.linkPreview.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LinkFragment extends Fragment implements View.OnClickListener, Listener {
    String TAG = LinkFragment.class.getName();
    private LinkPreviewFrgmentBinding binding;
    private Context context;
    private Listener listener;
    private Socket socket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LinkPreviewFrgmentBinding.inflate(getLayoutInflater());
        context = getActivity();
        initializeUI();
        return binding.getRoot();
    }

    private void initializeUI() {
/*        try {
            socket = IO.socket("http://chat.socket.io");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        Utils.printLog("##socket", "--" + socket.isActive());
        socket.on(Socket.EVENT_CONNECT, emiter);
        Utils.printLog("##socket1", "--" + socket.isActive());*/

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FRAGMENT_NAME, TAG).apply();
        binding.linkDescriptionLayout.setVisibility(GONE);
        binding.linkPreviewTextView.setOnClickListener(this);
        binding.yourLinkEditText.setFocusable(true);
//        RecyclerView linkRecyclerView = view.findViewById(R.id.linkRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.linkRecyclerView.setLayoutManager(linearLayoutManager);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Utils.printLog("##FCM", "isNotSuccessful");
                        Utils.printLog("##FCM Fetching FCM registration token failed", "-" + task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    // Log and toast
                    Utils.printLog("##FCMtoken", token);
                });
    }

  /*  private Emitter.Listener emiter = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Utils.printLog("##socket", "socket");
        }
    };*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linkPreviewTextView) {
/*            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(binding.yourLinkEditText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("msg", jsonObj);
            Utils.printLog("##socket2", "--" + socket.isActive());*/

            binding.progressBar.setVisibility(View.VISIBLE);
           LinkPreview linkPreview = new LinkPreview(new ResponseListener() {
                @SuppressLint("CheckResulmetaData = {MetaData@8301} t")
                @Override
                public void onData(MetaData metaData) {
                    binding.linkDescriptionLayout.setVisibility(View.VISIBLE);
                    binding.linkLayout.setVisibility(GONE);
                    binding.progressBar.setVisibility(GONE);
                    binding.linkDescriptionTextView.setText(metaData.getDescription());
                    Glide.with(context).load(metaData.getImageurl()).into(binding.linkImageView);
                    binding.linkTitleTextView.setText(metaData.getTitle());
                    listener = (Listener) context;
                    listener.listenerData(1, true);
                }

                @Override
                public void onError(Exception e) {
                    binding.progressBar.setVisibility(GONE);
                }
            });
            linkPreview.getPreview(binding.yourLinkEditText.getText().toString());
        }

    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("Action");
        Receiver myReceiver = new Receiver();
        context.registerReceiver(myReceiver, filter);
        super.onResume();
    }

    private void showNotification(String title, String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(context, Receiver.class);
        Intent intentTest = new Intent(context, SplashActivity.class);
        intent.setAction("action");
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent android12 = TaskStackBuilder.create(context).addNextIntentWithParentStack(intentTest).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 117, intentTest, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Use like this:
        boolean foregroud = isAppOnForegroundCheck(context);
        Utils.printLog("CheckisAppForeground", "--" + foregroud);
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context,
                channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.logo, "Show Message", android12)
                .setContentIntent(pendingIntent);

        Utils.printLog("CheckVersion", "11");
        // Do something for phones running an SDK before Android 12

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }


    @Override
    public void listenerData(int action, Object data) {

    }


    private boolean isAppOnForegroundCheck(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}

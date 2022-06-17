package com.myconsole.app.KTSamples;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.myconsole.app.KTSamples.MVVMViewModel.LifeCycleActivity;
import com.myconsole.app.KTSamples.alarm.AlertActivity;
import com.myconsole.app.KTSamples.bluetooth.BluetoothActivity;
import com.myconsole.app.KTSamples.encryptDecrypt.EncryptDecryptActivity;
import com.myconsole.app.KTSamples.messageSocket.ServiceConnectActivity;
import com.myconsole.app.KTSamples.messageSocket.SocketActivity;
import com.myconsole.app.KTSamples.sampleBroadCast.BroadCastReciverActivity;
import com.myconsole.app.R;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class KTMainActivity extends AppCompatActivity {
    private static final String TAG = KTMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kt);
        initialView();
        locationPermission();
        clickAction();
        getAppSignatures();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            Log.d("##Permission", "grandeted");
        }
    }

    private void locationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("##MainService", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("##MainService", "onServiceDisconnected");
        }
    };

    private void initialView() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    Log.d("##Token", token);
                });
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
        bindService(intent, connection, 0);

    }


    public static class MainService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("##MainService", "onStartCommand");
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            Log.d("##MainService", "onBind");
            return null;
        }
    }

    private void clickAction() {
        findViewById(R.id.button).setOnClickListener(view -> {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(123);
        });
        findViewById(R.id.serviceNavigation).setOnClickListener(view -> {
            Intent intent = new Intent(this, ServiceConnectActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.buttonSocket).setOnClickListener(view -> {
            Intent intent = new Intent(this, SocketActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.alertNavigaion).setOnClickListener(view -> {
            Intent intent = new Intent(this, AlertActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.broadcastReceiver).setOnClickListener(view -> {
            Intent intent = new Intent(this, BroadCastReciverActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.encryptDecrypt).setOnClickListener(view -> {
            Intent intent = new Intent(this, EncryptDecryptActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bluetoothConnection).setOnClickListener(view -> {
            Intent intent = new Intent(this, BluetoothActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.lifeCycle).setOnClickListener(view -> {
            Intent intent = new Intent(this, LifeCycleActivity.class);
            startActivity(intent);
        });
    }

    public ArrayList<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();

        try {
            // Get all package signatures for the current package
            String packageName = getPackageName();
            PackageManager packageManager = getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures;

            // For each signature create a compatible hash
            for (Signature signature : signatures) {
                String hash = hash(packageName, signature.toCharsString());
                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Unable to find package to obtain hash.", e.getMessage());
        }
        return appCodes;
    }

    private static String hash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));
            byte[] hashSignature = messageDigest.digest();

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, 9);
            // encode into Base64
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, 11);

            Log.d(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash));
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "hash:NoSuchAlgorithm", e);
        }
        return null;
    }
}
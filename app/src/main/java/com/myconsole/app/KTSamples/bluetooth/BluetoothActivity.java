package com.myconsole.app.KTSamples.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.myconsole.app.KTSamples.service.BluetoothService;
import com.myconsole.app.Listener;
import com.myconsole.app.databinding.ActivityBluetoothBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("MissingPermission")
public class BluetoothActivity extends AppCompatActivity implements Listener {
    private ActivityBluetoothBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    public static String WEIGHT_SCALE_CHARACTERISTIC_UUID = "0000fff4-0000-1000-8000-00805f9b34fb";
    public final static UUID WEIGHT_SCALE_MEASUREMENT_UUID = UUID.fromString(WEIGHT_SCALE_CHARACTERISTIC_UUID);
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    private BluetoothService bluetoothService;
    private BluetoothManager mBluetoothManager;
    private String address = "";

    @Override
    protected void onResume() {
        registerReceiver(receiver, makeGattUpdateIntentFilter());
        super.onResume();
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("##BluetoothName", "device.getName()");
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    setAdapter(device);
                    return;

                }

            } else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(intent.getAction())) {
                Log.e("##BluetoothWeight", "ACTION_GATT_SERVICES_DISCOVERED");
                displayGattServices(bluetoothService.getSupportedGattServices());
            } else if (intent.getAction().equals("DISPLAY_VALUES")) {
                Log.e("##BluetoothWeight", "DISPLAY_VALUES");
                int values = Math.round(Float.parseFloat(intent.getExtras().getString("WEIGHT_VALUES")));
                binding.indicatorCircle.setProgress(values);
            }
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        List<BluetoothGattCharacteristic> gattCharacteristics = new ArrayList<>();
        BluetoothGattCharacteristic characteristic = null;
        BluetoothGattCharacteristic writeCharacteristic = null;
        String serviceUUID = "0000fff0-0000-1000-8000-00805f9b34fb";
        String characteristicUUID = "0000fff4-0000-1000-8000-00805f9b34fb";
        String writeCharacteristicUUID = "0000fff1-0000-1000-8000-00805f9b34fb";

        for (int i = 0; i < gattServices.size(); i++) {
            if (gattServices.get(i).getUuid().toString().equals(serviceUUID)) {
                gattCharacteristics = gattServices.get(i).getCharacteristics();
                break;
            }
        }
        if (!characteristicUUID.isEmpty()) {
            for (int n = 0; n < gattCharacteristics.size(); n++) {
                if (gattCharacteristics.get(n).getUuid().toString().equals(characteristicUUID)) {
                    characteristic = gattCharacteristics.get(n);
                    break;
                }
            }
        }

        if (!writeCharacteristicUUID.isEmpty()) {
            for (int j = 0; j < gattCharacteristics.size(); j++) {
                if (gattCharacteristics.get(j).getUuid().toString().equals(writeCharacteristicUUID)) {
                    writeCharacteristic = gattCharacteristics.get(j);
                    break;
                }
            }
        }
        if (writeCharacteristic != null) {
            final int writeProp = writeCharacteristic.getProperties();
            if ((writeProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                Log.e("##BluetoothWeight", "PROPERTY_WRITE");
                /*if (mDeviceName.equals(WEIGHT_SCALE_NAME)) {
                    byte[] writeValue = {(byte) 0xFE, 0x03, 0x01, 0x00, (byte) 0xAA, 0x19, 0x01, (byte) 0xB0};
                    mBleService.writeValueInternal(writeValue, writeCharacteristic);
                }*/
            }
        }

        if (characteristic != null) {
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                Log.e("##BluetoothWeight", "PROPERTY_READ");
                if (mNotifyCharacteristic != null) {
                    Log.e("##BluetoothWeight", "PROPERTY_READ123");
                    bluetoothService.setCharacteristicNotification(mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                bluetoothService.readCharacteristic(characteristic);
            }

            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                Log.e("##BluetoothWeight", "PROPERTY_NOTIFY");
                mNotifyCharacteristic = characteristic;
                bluetoothService.setCharacteristicNotification(
                        characteristic, true);
            }
        }

    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothService = ((BluetoothService.LocalBinder) service).getService();
            if (!bluetoothService.initialize()) {
                finish();
            }
            bluetoothService.connect(address);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetoothService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intialView();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothEnable();
        } else {
            bluetoothLeScanner.startScan(callBack);
        }
    }

    private void intialView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.bluetoothRecyclerView.setLayoutManager(linearLayoutManager);
        binding.bluetoothRecyclerView.setHasFixedSize(true);
        binding.indicatorCircle.setVisibility(View.VISIBLE);
        binding.indicatorCircle.setProgress(30);

    }

    @Override
    protected void onDestroy() {
        bluetoothLeScanner.stopScan(callBack);
        super.onDestroy();
    }

    private final ScanCallback callBack = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.e("##BluetoothDeviceAddress", result.getDevice().getAddress() != null ? result.getDevice().getAddress() : "");
            Log.e("##BluetoothDeviceName", result.getDevice().getName() != null ? result.getDevice().getName() : "");
            if (result.getDevice().getName() != null) {
                if (result.getDevice().getName().equals("Health Scale")) {
                    bluetoothLeScanner.stopScan(callBack);
                }
            }
            setAdapter(result.getDevice());
            Log.e("##BluetoothonScanResult", "onScanResult");
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e("##BluetoothonBatchScanResults", "onBatchScanResults");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("##BluetoothonScanFaileds", "onScanFailed");
        }
    };

    private void callBindServiceForBleDevices() {
        Intent intent = new Intent(this, BluetoothService.class);
        this.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @SuppressLint("MissingPermission")
    private void setAdapter(BluetoothDevice device) {
        bluetoothDeviceList.add(device);
        if (bluetoothDeviceAdapter == null) {
            Log.e("##BluetoothAdapter", "Initial");
            bluetoothDeviceAdapter = new BluetoothDeviceAdapter(bluetoothDeviceList, this, this);
            binding.bluetoothRecyclerView.setAdapter(bluetoothDeviceAdapter);
        } else {
            Log.e("##BluetoothAdapter", "Again");
            bluetoothDeviceAdapter.notifyDataSetChanged();
        }
    }


    private void bluetoothEnable() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                startActivity(intent);
                return;
            }

        } else {
            bluetoothAdapter.startDiscovery();
        }
    }


    @Override
    public void listenerData(int action, Object data) {
        if (data != null) {
            if (action == 123) {
                address = (String) data;
                binding.bluetoothRecyclerView.setVisibility(View.GONE);
                callBindServiceForBleDevices();
               /* Log.e("##BluetoothAddress", address);
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                bluetoothGatt = device.connectGatt(this, true, bluetoothGattCallback);*/
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction("DISPLAY_VALUES");
        return intentFilter;
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e("##BluetoothonConnectionStateChange", "Connected");
                super.onConnectionStateChange(gatt, status, newState);
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.e("##BluetoothonConnectionStateChange", "disconneted");
            }
        }

        @Override
        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
            Log.e("##BluetoothonServiceChanged", "onServiceChanged");
            super.onServiceChanged(gatt);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e("##BluetoothoonCharacteristicRead", "onCharacteristicRead");
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.e("##BluetoothonCharacteristicChanged", "onCharacteristicChanged");
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.e("##BluetoothonServicesDiscovered", "onServicesDiscovered");
//            displayGattServices1(gatt.getServices());
            super.onServicesDiscovered(gatt, status);
        }
    };

    private void displayIDS(List<BluetoothGattService> services) {
        if (services.size() > 0) {
            for (int i = 0; i < services.size(); i++) {
//                String id = services.get(i).getUuid();
                Log.e("##BluetoothUUID", "UUID---" + services.get(i).getUuid());
                if (services.get(i).getUuid().toString().equals("0000fff0-0000-1000-8000-00805f9b34fb")) {
                    Log.e("##BluetoothWeight", "UUID match");
                }
                if (services.get(i).getCharacteristics().size() > 0) {
                    for (int j = 0; j < services.get(i).getCharacteristics().size(); j++) {
                        String chara = String.valueOf(services.get(i).getCharacteristics().get(j).getUuid());
                        if (chara.equals("0000fff1-0000-1000-8000-00805f9b34fb")) {
                            Log.e("##BluetoothWeight", "chara match");
                            BluetoothGattCharacteristic characteristic = services.get(i).getCharacteristics().get(j);
                            if (characteristic != null) {
                                int pro = characteristic.getProperties();
                                if ((pro | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                                    Log.e("##BluetoothWeight", "chara match--" + pro);
                                    Log.e("##BluetoothWeight13", "chara match-er-" + BluetoothGattCharacteristic.PROPERTY_NOTIFY);
                                    if (mNotifyCharacteristic != null) {
                                        Log.e("##BluetoothWeight", "mNotifyCharacteristic not null");
                                        setNotify(characteristic);
                                    }
                                    bluetoothGatt.readCharacteristic(characteristic);
                                }
                                if ((pro | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                    Log.e("##BluetoothWeightNofir", "chara match--" + pro);
                                    mNotifyCharacteristic = characteristic;
                                    bluetoothGatt.setCharacteristicNotification(characteristic, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void setNotify(BluetoothGattCharacteristic characteristic) {
        if (WEIGHT_SCALE_MEASUREMENT_UUID.equals(characteristic.getUuid())) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                try {
                    Log.e("##BluetoothWeight", "asdf");
                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        bluetoothGatt.writeDescriptor(descriptor);
                        Log.e("##BluetoothWeight", "asdf123");
                    }
                } catch (Exception e) {
                    Log.e("##BluetoothWeight", "------>" + e.getMessage());
                }
            }, 500);
        }
    /*    final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (characteristic != null) {
                bluetoothGatt.setCharacteristicNotification(characteristic, false);
                UUID uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(uuid);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                bluetoothGatt.writeDescriptor(descriptor);
            }
        }, 500);*/
    }

}
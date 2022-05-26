package com.myconsole.app.KTSamples.service;


import static com.myconsole.app.KTSamples.bluetooth.BluetoothActivity.CLIENT_CHARACTERISTIC_CONFIG;
import static com.myconsole.app.KTSamples.encryptDecrypt.EncryptDecryptActivity.bytesToHex;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


import com.myconsole.app.BuildConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@SuppressLint("MissingPermission")

public class BluetoothService extends Service {

    public final static String ACTION_GATT_SERVICES_DISCOVERED = BuildConfig.APPLICATION_ID + "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = BuildConfig.APPLICATION_ID + "ACTION_DATA_AVAILABLE";
    public final static String ACTION_GATT_CONNECTED = BuildConfig.APPLICATION_ID + "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = BuildConfig.APPLICATION_ID + "ACTION_GATT_DISCONNECTED";
    public static String O2_SATURATION_CHARACTERISTIC_UUID = "cdeacb81-5235-4c07-8846-93a37ee6b86d";
    public static String WEIGHT_SCALE_CHARACTERISTIC_UUID = "0000fff4-0000-1000-8000-00805f9b34fb";
    public final static UUID WEIGHT_SCALE_MEASUREMENT_UUID = UUID.fromString(WEIGHT_SCALE_CHARACTERISTIC_UUID);
    public static String BLOOD_GLUCOSE_CHARACTERISTIC_UUID = "0000fff4-0000-1000-8000-00805f9b34fb";

    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void connect(String address) {
        if (mBluetoothAdapter == null || address == null) {
            return;
        }
        // Previously connected device.  Try to reconnect.

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            return;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, true, mGattCallback, BluetoothDevice.TRANSPORT_LE);
    }

    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
//        if (mBluetoothManager == null) {
//            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//            if (mBluetoothManager == null) {
//                return false;
//            }
//        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null;
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    public void setCharacteristicNotification(final BluetoothGattCharacteristic characteristic, boolean enabled) {
        try {
            if (mBluetoothAdapter == null || mBluetoothGatt == null) {
                return;
            }
            Log.e("##BluetoothWeight", "setCharacteristicNotification");
            mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
            if (WEIGHT_SCALE_MEASUREMENT_UUID.equals(characteristic.getUuid())) {
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    try {
                        Log.e("##BluetoothWeight", "asdf");
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            mBluetoothGatt.writeDescriptor(descriptor);
                            Log.e("##BluetoothWeight", "asdf123");
                        }
                    } catch (Exception e) {
//                        Utils.printLogConsole("##setCharacteristicNotification", "------>" + e.getMessage());
                    }
                }, 500);
            }
        } catch (Exception e) {
//            Utils.printLogConsole("##setCharacteristicNotification", "------>" + e.getMessage());
        }
    }


    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        Log.e("##BluetoothWeight", "readCharacteristic");
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e("##BluetoothWeight", "STATE_CONNECTED");
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.e("##BluetoothWeight", "STATE_DISCONNECTED");
            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("##BluetoothWeight", "onServicesDiscovered");
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("##BluetoothWeight", "onCharacteristicRead");
                valuesUpdate(characteristic, gatt.getDevice().getName());
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.e("##BluetoothWeight", "onCharacteristicChanged");
            valuesUpdate(characteristic, gatt.getDevice().getName());
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void valuesUpdate(final BluetoothGattCharacteristic characteristic, String deviceName) {

        Log.e("##BluetoothWeight", "SET Intent");
        final Intent intent = new Intent(BluetoothService.ACTION_DATA_AVAILABLE);
        if (WEIGHT_SCALE_MEASUREMENT_UUID.equals(characteristic.getUuid()) && (deviceName.equals("Health Scale"))) {
            byte[] data = characteristic.getValue();
            Log.d("##BluetoothValues", "--->" + Arrays.toString(data));
            Log.d("##BluetoothValuesHex", "--->" + bytesToHex(data));
            String byteResult = bytesToHex(data);
            String scaleResult = "";
            String dataPart1 = byteResult.substring(6, 8);
            String dataPart2 = byteResult.substring(8, 10);
            int intResultScale = Integer.parseInt(dataPart2.concat(dataPart1), 16);
            double floatResult = (float) intResultScale * 0.01; // in Kg
            if (byteResult.startsWith("01", 16)) {// Is device shows lbs
                scaleResult = String.format(Locale.ENGLISH, "%.1f", floatResult);
            } else { // Is device shows Kg
                double poundsValue = floatResult * 2.205; // in Pounds
                scaleResult = String.format(Locale.ENGLISH, "%.1f", poundsValue);
            }
            Log.d("##BluetoothValuesFInal", "--->" + scaleResult);
            Intent intent1 = new Intent("DISPLAY_VALUES");
            intent1.putExtra("WEIGHT_VALUES", scaleResult);
            sendBroadcast(intent1);
        }
    }

}

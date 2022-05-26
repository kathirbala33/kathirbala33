package com.myconsole.app.fragment.bluetooth;

import static com.myconsole.app.ListenerConstant.BLUETOOTH_NAVIGATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.myconsole.app.Listener;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.BluetoothFragmentBinding;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@SuppressLint("MissingPermission")
public class BluetoothFragment extends Fragment implements Listener {

    private BluetoothFragmentBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;
    private final List<String> bluetoothDevice = new ArrayList<>();
    private final List<BluetoothModel> bluetoothModelList = new ArrayList<>();
    private final List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    private boolean isRegister = false;
    private Context context;
    private Listener listener;
    private BluetoothDevice device;
    private BluetoothGatt bluetoothGatt;
    private final int REQUEST_ENABLE_BT = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BluetoothFragmentBinding.inflate(getLayoutInflater());
        context = getActivity();
        init();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        listener = (Listener) context;
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        if (isRegister) {
            context.unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            device.setPairingConfirmation(true);
        } else if (requestCode == REQUEST_ENABLE_BT) {
            searchBTDevice();
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.printLog("##BluetoothNameRecevive", "=--");
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Utils.printLog("##BluetoothName", "=--" + device.getName());
                    Utils.printLog("##BluetoothAddress", "=--" + device.getAddress());
                    setAdapterForBluetoothDevice(device);
                    return;
                }

            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(intent.getAction())) {
                //Device is now connected
                Utils.printLog("##Bluetooth", "=--connected");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(intent.getAction())) {
                //Device is about to disconnect
                Utils.printLog("##Bluetooth", "=--disconnect");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())) {
                //Device has disconnected
                Utils.printLog("##Bluetooth", "=--disconnected");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())) {
                //Device has disconnected
                Utils.printLog("##Bluetooth", "=--disconnected");
            }

        }
    };


    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.bluetoothRecyclerView.setLayoutManager(linearLayoutManager);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            enableBT();
         /*   bluetoothAdapter.enable();
            bluetoothAdapter.getBluetoothLeScanner();*/
        } else {
            searchBTDevice();
        }
    }


    private void searchBTDevice() {
        bluetoothAdapter.startDiscovery();
        isRegister = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        context.registerReceiver(receiver, filter);
    }

    private void enableBT() {
        isRegister = false;
        Toast.makeText(context, "Please Enable Bluetooth", Toast.LENGTH_SHORT).show();
        Intent bt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bt, REQUEST_ENABLE_BT);
    }

    private final BluetoothGattCallback callback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Utils.printLog("##Bluetooth", "=--onConnectionStateChange");
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                bluetoothGatt.discoverServices();
                Utils.printLog("##Bluetooth", "=--start");
            }else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                bluetoothGatt.discoverServices();
                Utils.printLog("##Bluetooth", "=--Disconct");

            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Utils.printLog("##Bluetooth", "=--onCharacteristicWrite");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Utils.printLog("##Bluetooth", "=--onCharacteristicChanged");
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            super.onServicesDiscovered(gatt, status);
            Utils.printLog("##Bluetooth", "=--onServicesDiscovered");
            List<BluetoothGattService> bluetoothGattServices = gatt.getServices();
            List<BluetoothGattCharacteristic> gattCharacteristics;
            if (bluetoothGattServices.size() > 0) {
                for (int i = 0; i < bluetoothGattServices.size(); i++) {
                    gattCharacteristics = bluetoothGattServices.get(i).getCharacteristics();
                    for (int j = 0; j < gattCharacteristics.size(); j++) {
                        bluetoothGatt.readCharacteristic(gattCharacteristics.get(i));
                        Utils.printLog("##BluetoothUUid", "=--" + gattCharacteristics.get(j).getUuid());
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Utils.printLog("##Bluetooth", "=-onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Utils.printLog("##BluetoothValues", "=-" + Arrays.toString(characteristic.getValue()));
                String str = new String(characteristic.getValue(), StandardCharsets.UTF_8); // for UTF-8 encoding
                Utils.printLog("##BluetoothValues1", "=-" + str);
            }
        }
    };

    @Override
    public void listenerData(int action, Object data) {
        if (action == BLUETOOTH_NAVIGATION) {
            BluetoothDevice id = (BluetoothDevice) data;
            Toast.makeText(context, "Got it " + id, Toast.LENGTH_SHORT).show();
            device = id;
            /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
*//*
                BluetoothGatt bluetoothGatt = device.connectGatt(context, true, new BluetoothGattCallback() {

                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                        super.onConnectionStateChange(gatt, status, newState);
                        Utils.printLog("##Bluetooth", "=--onConnectionStateChange");
                        if (newState == BluetoothGatt.STATE_CONNECTED) {
                            Utils.printLog("##Bluetooth", "=--start");
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                gatt.discoverServices();
                                return;
                            }

                        }
                    }

                    @Override
                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                        super.onServicesDiscovered(gatt, status);
                        Utils.printLog("##Bluetooth", "=--onServicesDiscovered");
                        List<BluetoothGattService> bluetoothGattServices = gatt.getServices();
                        List<BluetoothGattCharacteristic> gattCharacteristics;
                        if (bluetoothGattServices.size() > 0) {
                            for (int i = 0; i < bluetoothGattServices.size(); i++) {
                                gattCharacteristics = bluetoothGattServices.get(i).getCharacteristics();
                                for (int j = 0; j < gattCharacteristics.size(); j++) {
                                    Utils.printLog("##BluetoothUUid", "=--" + gattCharacteristics.get(j).getUuid());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicRead(gatt, characteristic, status);
                        Utils.printLog("##Bluetooth", "=-onCharacteristicRead");
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            Utils.printLog("##BluetoothValues", "=-" + Arrays.toString(characteristic.getValue()));
                        }
                    }
                });
*//*
                return;
            }*/

            bluetoothGatt = device.connectGatt(context, false, callback, BluetoothDevice.TRANSPORT_LE);
//            bluetoothGatt.connect();
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_PRIVILEGED) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED}, 3);
//            }

        }
    }
    private void setAdapterForBluetoothDevice(BluetoothDevice device) {
        bluetoothDeviceList.add(device);
        if (bluetoothDeviceAdapter == null) {
            bluetoothDeviceAdapter = new BluetoothDeviceAdapter(context, bluetoothDeviceList, this);
            binding.bluetoothRecyclerView.setAdapter(bluetoothDeviceAdapter);
        } else {
            bluetoothDeviceAdapter.notifyDataSetChanged();
        }
    }

}

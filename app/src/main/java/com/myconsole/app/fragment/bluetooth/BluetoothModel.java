package com.myconsole.app.fragment.bluetooth;

public class BluetoothModel {
    String bluetoothID;
    String BluetoothDeviceName;

    public BluetoothModel() {
    }

    public BluetoothModel(String address, String name) {
        this.bluetoothID = address;
        this.BluetoothDeviceName = name;
    }

    public String getBluetoothDeviceName() {
        return BluetoothDeviceName;
    }

    public String getBluetoothID() {
        return bluetoothID;
    }

    public void setBluetoothID(String bluetoothID) {
        this.bluetoothID = bluetoothID;
    }

    public void setBluetoothDeviceName(String bluetoothDeviceName) {
        BluetoothDeviceName = bluetoothDeviceName;
    }

}

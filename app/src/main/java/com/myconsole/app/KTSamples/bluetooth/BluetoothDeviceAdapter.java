package com.myconsole.app.KTSamples.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.myconsole.app.Listener;
import com.myconsole.app.R;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.MyviewHolder> {

    private List<BluetoothDevice> bluetoothDeviceLists = new ArrayList<>();
    private Context context;
    private Listener listener;


    public BluetoothDeviceAdapter(List<BluetoothDevice> bluetoothDeviceList, Listener listener, Context mContext) {
        this.context = mContext;
        this.bluetoothDeviceLists = bluetoothDeviceList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.bluetoothdeviceadapter, parent, false);
        return new MyviewHolder(view1);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        if (bluetoothDeviceLists.size() > 0) {
            BluetoothDevice device = bluetoothDeviceLists.get(position);
            if (device != null) {
                String deviceName = device.getName() != null ? device.getName() : "";
                String deviceAddress = device.getAddress() != null ? device.getAddress() : "";
                holder.deviceNames.setText(deviceName);
                holder.DeviceAddress.setText(deviceAddress);
                holder.deviceNames.setTag(position);
                holder.DeviceAddress.setTag(position);
            }
        }

    }


    @Override
    public int getItemCount() {
        return bluetoothDeviceLists.size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView deviceNames, DeviceAddress;
        private ConstraintLayout totalLayout;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNames = itemView.findViewById(R.id.deviceName);
            DeviceAddress = itemView.findViewById(R.id.deviceAddress);
            totalLayout = itemView.findViewById(R.id.totalLayout);
            totalLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.totalLayout) {
                int pos = (int) deviceNames.getTag();
                String address = bluetoothDeviceLists.get(pos).getAddress();
                listener.listenerData(123, address);
            }
        }
    }
}

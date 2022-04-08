package com.myconsole.app.fragment.bluetooth;

import static com.myconsole.app.ListenerConstant.BLUETOOTH_NAVIGATION;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.Listener;
import com.myconsole.app.R;
import com.myconsole.app.databinding.BluetoothAdapterBinding;

import java.util.List;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {
    private BluetoothAdapterBinding binding;
    private Context context;
    private Listener listener;
    private List<BluetoothDevice> bluetoothDeviceList;

    public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> bluetoothDevice, Listener listener) {
        this.context = context;
        this.bluetoothDeviceList = bluetoothDevice;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BluetoothDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_adapter, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceAdapter.ViewHolder holder, int position) {
        BluetoothDevice model = bluetoothDeviceList.get(position);
        if (model.getName() != null) {
            holder.bluetoothID.setText(model.getAddress());
            holder.bluetoothDeviceName.setText(model.getName());
            holder.bluetoothLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.listenerData(BLUETOOTH_NAVIGATION, model);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bluetoothDeviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bluetoothID, bluetoothDeviceName;
        private RelativeLayout bluetoothLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bluetoothID = itemView.findViewById(R.id.bluetoothID);
            bluetoothDeviceName = itemView.findViewById(R.id.bluetoothName);
            bluetoothLayout = itemView.findViewById(R.id.bluetoothLayout);
        }
    }
}

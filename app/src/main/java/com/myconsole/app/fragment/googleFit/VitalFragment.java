package com.myconsole.app.fragment.googleFit;

import static com.myconsole.app.ListenerConstant.FRAGMENT_NAME;
import static com.myconsole.app.ListenerConstant.VITAL_GRAPH_VIEW_NAVIGATION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.myconsole.app.Listener;
import com.myconsole.app.ListenerConstant;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.VitalFragmentBinding;
import com.myconsole.app.roomdb.AppDataBase;
import com.myconsole.app.roomdb.VitalEntity;

import java.util.List;

public class VitalFragment extends Fragment {
    String TAG = VitalFragment.class.getName();
    private VitalFragmentBinding binding;
    private Context context;
    private Listener listener;
    BroadcastReceiver getBroadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = VitalFragmentBinding.inflate(getLayoutInflater());
        context = getActivity();
        listener = (Listener) context;
        initinalView();
        return binding.getRoot();
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    public void onResume() {
//        context.registerReceiver(getBroadcastReceiver,"");
        super.onResume();
    }

    @Override
    public void onDestroy() {
//        context.unregisterReceiver(getBroadcastReceiver);
        super.onDestroy();
    }

    private void initinalView() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FRAGMENT_NAME, TAG).apply();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.vitalReRecyclerView.setLayoutManager(layoutManager);
        binding.vitalReRecyclerView.setHasFixedSize(true);
        Bundle bundle = this.getArguments();
        String vitalName = "";
        if (bundle != null) {
            vitalName = bundle.getString("VitalName");
            binding.vitalTitle.setText(vitalName);
        }
        Utils.printLog("##G_VitalName", vitalName);
        AppDataBase db = AppDataBase.getInstance(context);
        if (vitalName != null && !vitalName.isEmpty()) {
            List<VitalEntity> vitalEntityList = db.daoVital().getVitalValues(vitalName);
            int vitalID = Utils.getVitalIDUsingNames(vitalName, context);
            if (vitalEntityList.size() > 0) {
                VitalAdapter adapter = new VitalAdapter(vitalEntityList, vitalID, context, listener);
                binding.vitalReRecyclerView.setAdapter(adapter);
            }
            binding.graphImageView.setOnClickListener(v -> {
                String poisition = ListenerConstant.getVitalName[vitalID];
                listener.listenerData(VITAL_GRAPH_VIEW_NAVIGATION, poisition);
            });
        }
    }
}

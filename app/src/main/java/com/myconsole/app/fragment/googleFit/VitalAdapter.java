package com.myconsole.app.fragment.googleFit;

import static com.myconsole.app.ListenerConstant.VITAL_GRAPH_VIEW_NAVIGATION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.Listener;
import com.myconsole.app.ListenerConstant;
import com.myconsole.app.R;
import com.myconsole.app.databinding.VitalAdapterBinding;
import com.myconsole.app.roomdb.VitalEntity;

import java.util.List;

public class VitalAdapter extends RecyclerView.Adapter<VitalAdapter.MyViewHolder> {
    private VitalAdapterBinding binding;
    private final List<VitalEntity> vitalEntities;
    private final int vitalID;
    private final Context context;
    private final Listener listener;

    public VitalAdapter(List<VitalEntity> vitalEntityList, int id, Context context, Listener listener) {
        this.vitalEntities = vitalEntityList;
        this.vitalID = id;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = VitalAdapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VitalEntity model = vitalEntities.get(position);
        if (model != null) {
            String date = vitalEntities.get(position).getCreatedDate();
            binding.vitalValueDate.setText(date);
            binding.graphTextView.setTag(position);
            if (vitalID == 0) {
                String dia = String.valueOf(vitalEntities.get(position).getDiastolic());
                String sys = String.valueOf(vitalEntities.get(position).getSystolic());
                binding.systolicValueTextView.setText(sys);
                binding.diaValueTextView.setText(dia);
                binding.vitalImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.bp_icon));
            } else if (vitalID == 2) {
                String heartRate = String.valueOf(vitalEntities.get(position).getHeartRate());
                binding.systolicValueTextView.setVisibility(View.GONE);
                binding.systolicTextView.setVisibility(View.GONE);
                binding.diaValueTextView.setText(heartRate);
                binding.diaTextView.setText(context.getResources().getString(R.string.vital_heart_rate));
                binding.vitalImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.heart_rate_icon));
            } else if (vitalID == 1) {
                String spo2 = String.valueOf(vitalEntities.get(position).getSpo2());
                binding.systolicValueTextView.setVisibility(View.GONE);
                binding.systolicTextView.setVisibility(View.GONE);
                binding.diaValueTextView.setText(spo2);
                binding.diaTextView.setText(context.getResources().getString(R.string.vital_spo2));
                binding.vitalImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.spo2_icon));
            } else if (vitalID == 3) {
                String weight = String.valueOf(vitalEntities.get(position).getWeight());
                binding.systolicValueTextView.setVisibility(View.GONE);
                binding.diaValueTextView.setText(weight);
                binding.systolicTextView.setVisibility(View.GONE);
                binding.diaTextView.setText(context.getResources().getString(R.string.vital_weight));
                binding.vitalImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.weight_icon));
            } else if (vitalID == 4) {
                String temp = String.valueOf(vitalEntities.get(position).getTemperature());
                binding.systolicValueTextView.setVisibility(View.GONE);
                binding.systolicTextView.setVisibility(View.GONE);
                binding.diaTextView.setText(context.getResources().getString(R.string.vital_temp));
                binding.diaValueTextView.setText(temp);
                binding.vitalImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.thermometer_icon));
            } else if (vitalID == 5) {
                String glucose = String.valueOf(vitalEntities.get(position).getGlucose());
                binding.systolicValueTextView.setVisibility(View.GONE);
                binding.systolicTextView.setVisibility(View.GONE);
                binding.diaValueTextView.setText(glucose);
                binding.diaTextView.setText(context.getResources().getString(R.string.vital_glucose));
                binding.vitalImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.glucometer_icon));
            }
        }
    }


    @Override
    public int getItemCount() {
        return vitalEntities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Listener {
        private TextView graphTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            graphTextView = itemView.findViewById(R.id.graphTextView);
            graphTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.graphTextView) {

            }
        }

        @Override
        public void listenerData(int action, Object data) {

        }
    }
}

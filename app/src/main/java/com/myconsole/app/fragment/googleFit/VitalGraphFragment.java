package com.myconsole.app.fragment.googleFit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.myconsole.app.R;
import com.myconsole.app.databinding.VitalGraphFragmentBinding;
import com.myconsole.app.roomdb.AppDataBase;
import com.myconsole.app.roomdb.VitalEntity;

import java.util.ArrayList;
import java.util.List;

public class VitalGraphFragment extends Fragment {
    private VitalGraphFragmentBinding binding;
private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = VitalGraphFragmentBinding.inflate(getLayoutInflater());
        context = getActivity();
        initView();
        return binding.getRoot();
    }

    private void initView() {
        Bundle bundle = this.getArguments();
        String vitalName = "";
        if (bundle != null) {
            vitalName = bundle.getString("VitalName");
        }
        BarChart barChart = binding.barChart;
        AppDataBase db = AppDataBase.getInstance(getActivity());
        List<VitalEntity> vitalEntityList = db.daoVital().getVitalValues(vitalName);
        ArrayList<BarEntry> entriesSys = new ArrayList<>();
        ArrayList<BarEntry> entriesDai = new ArrayList<>();
        for (int i = 0; i < vitalEntityList.size(); i++) {
            if(vitalName.equals(context.getResources().getString(R.string.vital_title))){
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getSystolic());
                BarEntry barEntryDia = new BarEntry(i, vitalEntityList.get(i).getDiastolic() +vitalEntityList.get(i).getSystolic());
                entriesSys.add(i, barEntrySys);
                entriesDai.add(i, barEntryDia);
                BarDataSet barDataSetSys = new BarDataSet(entriesSys, "sys");
                BarDataSet barDataSetDia = new BarDataSet(entriesDai, "dia");
                barDataSetSys.setColor(context.getColor(R.color.bottom_navigation_text_background_green));
                barDataSetDia.setColor(context.getColor(R.color.activity_gradient_bottom));
                BarData dataSys = new BarData(barDataSetSys);
                barChart.setData(dataSys);
                barChart.invalidate();
                BarData dataSDia = new BarData(barDataSetDia);
                barChart.setData(dataSDia);
                barChart.invalidate();
            }else if(vitalName.equals(context.getResources().getString(R.string.vital_spo2))){
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getSpo2());
                entriesSys.add(i, barEntrySys);
                BarDataSet barDataSetSys = new BarDataSet(entriesSys, "spo2");
                BarData dataSys = new BarData(barDataSetSys);
                barChart.setData(dataSys);
                barChart.invalidate();
            }else if(vitalName.equals(context.getResources().getString(R.string.vital_weight))){
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getWeight());
                entriesSys.add(i, barEntrySys);
                BarDataSet barDataSetSys = new BarDataSet(entriesSys, "Weight");
                BarData dataSys = new BarData(barDataSetSys);
                barChart.setData(dataSys);
                barChart.invalidate();
            }else if(vitalName.equals(context.getResources().getString(R.string.vital_temp))){
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getTemperature());
                entriesSys.add(i, barEntrySys);
                BarDataSet barDataSetSys = new BarDataSet(entriesSys, "Temperature");
                BarData dataSys = new BarData(barDataSetSys);
                barChart.setData(dataSys);
                barChart.invalidate();
            }else if(vitalName.equals(context.getResources().getString(R.string.vital_glucose))){
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getGlucose());
                entriesSys.add(i, barEntrySys);
                BarDataSet barDataSetSys = new BarDataSet(entriesSys, "Glucose");
                BarData dataSys = new BarData(barDataSetSys);
                barChart.setData(dataSys);
                barChart.invalidate();
            }else if(vitalName.equals(context.getResources().getString(R.string.vital_heart_rate))){
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getHeartRate());
                entriesSys.add(i, barEntrySys);
                BarDataSet barDataSetSys = new BarDataSet(entriesSys, "heart Rate");
                BarData dataSys = new BarData(barDataSetSys);
                barChart.setData(dataSys);
                barChart.invalidate();
            }
        }

    }
}

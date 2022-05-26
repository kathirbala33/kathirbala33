package com.myconsole.app.fragment.googleFit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.myconsole.app.R;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.VitalGraphFragmentBinding;
import com.myconsole.app.roomdb.AppDataBase;
import com.myconsole.app.roomdb.VitalEntity;

import java.util.ArrayList;
import java.util.List;

public class VitalGraphFragment extends Fragment {
    private VitalGraphFragmentBinding binding;
    private Context context;
    private ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
    private List<ILineDataSet> lineDataSetsSysDia = new ArrayList<>();


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
        LineChart lineChart = binding.lineChart;
        AppDataBase db = AppDataBase.getInstance(getActivity());
        List<VitalEntity> vitalEntityList = db.daoVital().getVitalValues(vitalName);
        ArrayList<BarEntry> entriesSys = new ArrayList<>();
        ArrayList<Entry> entryArrayListSys = new ArrayList<>();
        ArrayList<Entry> entryArrayList = new ArrayList<>();
        ArrayList<Entry> entryArrayListDia = new ArrayList<>();
        ArrayList<BarEntry> glucoseEntryList = new ArrayList<>();
        ArrayList<String> xAxisLabel = new ArrayList<>();
        List<Entry> entryListSpo2 = new ArrayList<>();
        for (int i = 0; i < vitalEntityList.size(); i++) {
            lineDataSetsSysDia = new ArrayList<>();
            xAxisLabel.add(String.valueOf(i));
            entryArrayList = new ArrayList<>();
            if (vitalName.equals(context.getResources().getString(R.string.vital_title))) {
                binding.lineChart.setVisibility(View.VISIBLE);
                binding.barChart.setVisibility(View.GONE);
//Sys values add in list
                Entry entrySys = new Entry(i, vitalEntityList.get(i).getSystolic());
                entryArrayListSys.add(entrySys);
                LineDataSet lineDataSetSys = new LineDataSet(entryArrayListSys, "");
                lineDataSetSys.setCircleColor(vitalEntityList.get(i).getSystolic()>130?ContextCompat.getColor(context, R.color.red):ContextCompat.getColor(context, R.color.color_green));
                lineDataSetSys.setColor(ContextCompat.getColor(context, R.color.questionnaireTextColor));
                lineDataSetSys.setDrawCircleHole(false);
                lineDataSetsSysDia.add(lineDataSetSys);
//Dia values add in list
                Entry entry = new Entry(i, vitalEntityList.get(i).getDiastolic());
                entryArrayListDia.add(entry);
                LineDataSet lineDataSetDia = new LineDataSet(entryArrayListDia, "");
                lineDataSetDia.setDrawCircleHole(false);
                lineDataSetDia.setCircleColor(vitalEntityList.get(i).getDiastolic()>90?ContextCompat.getColor(context, R.color.red):ContextCompat.getColor(context, R.color.color_green));
                lineDataSetDia.setColor(ContextCompat.getColor(context, R.color.dia_circle_color));
                lineDataSetsSysDia.add(lineDataSetDia);
                LineData lineData = new LineData(lineDataSetsSysDia);
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelCount(5);
                lineChart.getAxisRight().setEnabled(false);
                lineChart.getAxisLeft().setAxisMinimum(0f);
                lineChart.getLegend().setEnabled(false);
                lineChart.setVisibleXRangeMaximum(5);
                lineChart.getDescription().setEnabled(false);
                lineChart.setData(lineData);
            } else if (vitalName.equals(context.getResources().getString(R.string.vital_spo2))) {
                binding.lineChart.setVisibility(View.VISIBLE);
                binding.barChart.setVisibility(View.GONE);
                Entry entry = new Entry(i, vitalEntityList.get(i).getSpo2());
                entryListSpo2.add(entry);
                LineDataSet lineDataSet = new LineDataSet(entryListSpo2, "");
                lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.red));
                lineDataSet.setLineWidth(2f);
                lineDataSet.setDrawCircleHole(false);
//                lineDataSet.setDrawValues(false);
//                lineDataSet.setDrawCircles(false);
                LineData lineData = new LineData(lineDataSet);
                lineData.setHighlightEnabled(false);
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setLabelCount(5);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                YAxis axisRight = lineChart.getAxisRight();
                lineChart.getAxisLeft().setAxisMinimum(0f);
                axisRight.setEnabled(false);
                lineChart.getLegend().setEnabled(false);
                lineChart.setVisibleXRangeMaximum(5);
                lineChart.getDescription().setEnabled(false);
                lineChart.setScaleEnabled(false);
                lineChart.setHighlightPerTapEnabled(false);
                lineChart.setData(lineData);
            } else if (vitalName.equals(context.getResources().getString(R.string.vital_weight))) {
                binding.lineChart.setVisibility(View.GONE);
                binding.barChart.setVisibility(View.VISIBLE);
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getWeight());
                entriesSys.add(i, barEntrySys);
                BarDataSet barDataSetSys = new BarDataSet(entriesSys, "Weight");
                BarData dataSys = new BarData(barDataSetSys);
                dataSys.setBarWidth(0.1f);
                barChart.getAxisRight().setEnabled(false);
                XAxis axis = barChart.getXAxis();
                axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                axis.setLabelCount(5);
                axis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        int x = (int) value;
                        if (xAxisLabel.size() > x) {
                            return xAxisLabel.get(x);
                        }
                        return null;
                    }
                });
                barChart.setVisibleXRangeMaximum(5);
                barChart.getDescription().setEnabled(false);
                barDataSetSys.setDrawValues(false);
                barChart.getLegend().setEnabled(false);
                barChart.setScaleEnabled(false);
                barChart.setData(dataSys);
                barChart.invalidate();
            } else if (vitalName.equals(context.getResources().getString(R.string.vital_temp))) {
                binding.lineChart.setVisibility(View.VISIBLE);
                binding.barChart.setVisibility(View.GONE);
                if (i == 0) {
                    entryArrayList.add(new Entry(i, 0));
                    setGraphValues(lineChart, 0, entryArrayList, xAxisLabel);
                    entryArrayList = new ArrayList<>();
                }
                entryArrayList.add(new Entry(i + 1, 0));
                setGraphValues(lineChart, 0, entryArrayList, xAxisLabel);
                entryArrayList.add(new Entry(i + 1, vitalEntityList.get(i).getTemperature()));
                setGraphValues(lineChart, ContextCompat.getColor(context, R.color.red), entryArrayList, xAxisLabel);
                lineChart.setVisibleXRangeMaximum(5);
                lineChart.getXAxis().setGranularityEnabled(false);
                lineChart.setScaleEnabled(false);
                LineData lineData = new LineData(lineDataSets);
                lineChart.setData(lineData);
            } else if (vitalName.equals(context.getResources().getString(R.string.vital_glucose))) {
                binding.lineChart.setVisibility(View.GONE);
                binding.barChart.setVisibility(View.VISIBLE);
                BarEntry barEntry = new BarEntry(i, vitalEntityList.get(i).getGlucose());
                glucoseEntryList.add(barEntry);
                BarDataSet barDataSet = new BarDataSet(glucoseEntryList, "glucose");
                barDataSet.setDrawValues(false);
                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.2f);
                barChart.setData(barData);
                XAxis axis = barChart.getXAxis();
                axis.setDrawGridLines(false);
//                axis.setAxisMaximum(vitalEntityList.size()-1);
                axis.setLabelCount(5);
                axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getLegend().setEnabled(false);
                barChart.getAxisLeft().setAxisMinimum(0f);
                barChart.getAxisRight().setEnabled(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.setScaleEnabled(false);
//                barChart.setMaxVisibleValueCount(5);
                barChart.setDragXEnabled(true);
                barChart.getDescription().setEnabled(false);
                barChart.setVisibleXRangeMaximum(5);
                axis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        int x = (int) value;
                        if (xAxisLabel.size() > x) {
//                            if (!xAxisLabel.get(x).isEmpty()) {
                            return xAxisLabel.get(x);
//                            }
                        }
                        return null;
                    }
                });
                barChart.invalidate();
            } else if (vitalName.equals(context.getResources().getString(R.string.vital_heart_rate))) {
                binding.lineChart.setVisibility(View.GONE);
                binding.barChart.setVisibility(View.VISIBLE);
                BarEntry barEntrySys = new BarEntry(i, vitalEntityList.get(i).getHeartRate());
                entriesSys.add(i, barEntrySys);
                BarDataSet barDataSet = new BarDataSet(entriesSys, "heart Rate");
                barDataSet.setDrawValues(false);
                barDataSet.setBarBorderWidth(1f);
                barDataSet.setBarBorderColor(ContextCompat.getColor(context, R.color.act_goal_activity));
                barDataSet.setColor(ContextCompat.getColor(context, R.color.dateColor));
                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.2f);
                barChart.setData(barData);
                barChart.getAxisRight().setEnabled(false);
                barChart.getAxisLeft().setAxisMinimum(0f);
                barChart.getAxisLeft().setDrawGridLines(false);
                XAxis axis = barChart.getXAxis();
                axis.setDrawGridLines(false);
                axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getLegend().setEnabled(false);
                barChart.getDescription().setEnabled(false);
                barChart.setScaleEnabled(false);
                barChart.setDrawGridBackground(false);
                axis.setLabelCount(5);
                barChart.setVisibleXRangeMaximum(5);
                barChart.invalidate();
            }
        }

    }

    private void graphView(BarChart barChart) {
        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.setScaleEnabled(false);
        barChart.setMaxVisibleValueCount(5);
        XAxis axis = barChart.getXAxis();
        axis.setDrawGridLines(false);
        axis.setAxisMaximum(5);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getDescription().setEnabled(false);
    }

    private void setGraphValues(LineChart lineChart, int color, ArrayList<Entry> entryArrayList, ArrayList<String> xAxisLabel) {
        LineDataSet lineDataSet = new LineDataSet(entryArrayList, "");
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setCubicIntensity(0.1f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSets.add(lineDataSet);
        lineChart.getAxisLeft().setAxisMinValue(0f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        XAxis axis = lineChart.getXAxis();
        axis.setLabelCount(5);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setAxisMaximum(xAxisLabel.size());
        axis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int x = (int) value;
                Utils.printLog("##xAxisInt", "--" + x);
                Utils.printLog("##xAxisIntAxis", "--" + axis.getGridDashPathEffect());
                if (xAxisLabel.size() > x) {
                    Utils.printLog("##xAxis", "--" + xAxisLabel.get(x));
                    return xAxisLabel.get(x);
                }
                return null;
            }
        });
    }

}

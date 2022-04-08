package com.myconsole.app.fragment.googleFit;


import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.fitness.data.DataType.TYPE_ACTIVITY_SEGMENT;
import static com.google.android.gms.fitness.data.DataType.TYPE_CALORIES_EXPENDED;
import static com.google.android.gms.fitness.data.DataType.TYPE_DISTANCE_DELTA;
import static com.google.android.gms.fitness.data.DataType.TYPE_HEART_RATE_BPM;
import static com.google.android.gms.fitness.data.DataType.TYPE_MOVE_MINUTES;
import static com.google.android.gms.fitness.data.DataType.TYPE_SLEEP_SEGMENT;
import static com.google.android.gms.fitness.data.DataType.TYPE_STEP_COUNT_DELTA;
import static com.google.android.gms.fitness.data.DataType.TYPE_WEIGHT;
import static com.google.android.gms.fitness.data.HealthDataTypes.TYPE_BLOOD_GLUCOSE;
import static com.google.android.gms.fitness.data.HealthDataTypes.TYPE_BLOOD_PRESSURE;
import static com.google.android.gms.fitness.data.HealthDataTypes.TYPE_BODY_TEMPERATURE;
import static com.google.android.gms.fitness.data.HealthDataTypes.TYPE_OXYGEN_SATURATION;
import static com.myconsole.app.ListenerConstant.IS_GOOGLE_FIT_SYNC;
import static com.myconsole.app.ListenerConstant.VITAL_BP;
import static com.myconsole.app.ListenerConstant.VITAL_GLUCOSE;
import static com.myconsole.app.ListenerConstant.VITAL_HEART_RATE;
import static com.myconsole.app.ListenerConstant.VITAL_NAVIGATION;
import static com.myconsole.app.ListenerConstant.VITAL_SPO2;
import static com.myconsole.app.ListenerConstant.VITAL_TEMPERATURE;
import static com.myconsole.app.ListenerConstant.VITAL_WEIGHT;

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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.HealthFields;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;
import com.myconsole.app.Listener;
import com.myconsole.app.R;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.GoogleFitFragmentBinding;
import com.myconsole.app.roomdb.AppDataBase;
import com.myconsole.app.roomdb.VitalEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GoogleFitFragment extends Fragment implements Listener {
    private GoogleFitFragmentBinding binding;
    private int GOOGLE_FIT_ACCOUNT_PERMISSION = 100;
    private int GOOGLE_FIT_FITNESS_PERMISSION = 101;
    private Context context;
    private Listener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GoogleFitFragmentBinding.inflate(getLayoutInflater());
        context = getActivity();
        initialView();
        listener = (Listener) context;
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        listener = (Listener) context;
        super.onResume();
    }

    public static FitnessOptions getFitnessSignInOptions() {
//        return FitnessOptions.builder().addDataType(TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ).build();
//         Request access to step count data from Fit history
        return FitnessOptions.builder()
                .addDataType(TYPE_BLOOD_GLUCOSE, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_BODY_TEMPERATURE, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_OXYGEN_SATURATION, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GOOGLE_FIT_ACCOUNT_PERMISSION) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                requestGoogleFitPermission(account);
                // Signed in successfully, show authenticated UI.
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.

            }
        } else if (requestCode == GOOGLE_FIT_FITNESS_PERMISSION) {
            getValuesFromGoogleFit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getValuesFromGoogleFit() {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
        DataReadRequest readRequest = getDatRequest();
        if (googleSignInAccount != null) {
            Fitness.getHistoryClient(context, googleSignInAccount).
                    readData(readRequest).addOnSuccessListener(this::insertMethod).addOnFailureListener(e -> Utils.printLog("##Listener", "FailureListener"));
        }
    }

    private void insertMethod(DataReadResponse dataReadResponse) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_GOOGLE_FIT_SYNC, true).apply();
        AppDataBase dataBase = AppDataBase.getInstance(getContext());
        if (dataReadResponse != null && dataReadResponse.getBuckets().size() > 0) {
            for (int i = 0; i < dataReadResponse.getBuckets().size(); i++) {
                List<DataSet> dataSets = dataReadResponse.getBuckets().get(i).getDataSets();
                if (dataSets.size() > 0) {
                    for (int k = 0; k < dataSets.size(); k++) {
                        if (dataSets.get(k).getDataPoints().size() > 0) {
                            VitalEntity vitalEntity = new VitalEntity();
                            String createdDate = "";
                            for (int j = 0; j < dataSets.get(k).getDataPoints().size(); j++) {
                                if (dataSets.get(k).getDataPoints().get(j).getDataType().getName().equals(VITAL_BP)) {
                                    Utils.printLog("##GoogleFit", "bp");
                                    float sys = dataSets.get(k).getDataPoints().get(j).getValue(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC).asFloat();
                                    float dia = dataSets.get(k).getDataPoints().get(j).getValue(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC).asFloat();
                                    long createdDateLong = dataSets.get(k).getDataPoints().get(j).getStartTime(TimeUnit.MILLISECONDS);
                                    createdDate = Utils.convertDateLongToString(createdDateLong);
                                    Utils.printLog("##GoogleFitsdis", "--" + dia);
                                    Utils.printLog("##GoogleFitsSys", "--" + sys);
                                    Utils.printLog("##GoogleFitsCreated", "--" + createdDate);
                                    vitalEntity.setId(UUID.randomUUID().toString());
                                    vitalEntity.setDiastolic(dia);
                                    vitalEntity.setVitalName(context.getResources().getString(R.string.vital_title));
                                    vitalEntity.setSystolic(sys);
                                    vitalEntity.setCreatedDate(createdDate);
                                } else if (dataSets.get(k).getDataPoints().get(j).getDataType().getName().equals(VITAL_HEART_RATE)) {
                                    Utils.printLog("##GoogleFit", "no");
                                    float heartRate = dataSets.get(k).getDataPoints().get(j).getValue(dataSets.get(k).getDataType().getFields().get(0)).asFloat();
                                    long createdDateLong = dataSets.get(k).getDataPoints().get(j).getStartTime(TimeUnit.MILLISECONDS);
                                    createdDate = Utils.convertDateLongToString(createdDateLong);
                                    Utils.printLog("##GoogleFitsheartRate", "--" + heartRate);
                                    Utils.printLog("##GoogleFitsCreated", "--" + createdDate);
                                    vitalEntity.setId(UUID.randomUUID().toString());
                                    vitalEntity.setHeartRate(heartRate);
                                    vitalEntity.setVitalName(context.getResources().getString(R.string.vital_heart_rate));
                                    vitalEntity.setCreatedDate(createdDate);
                                } else if (dataSets.get(k).getDataPoints().get(j).getDataType().getName().equals(VITAL_WEIGHT)) {
                                    Utils.printLog("##GoogleFit", "no");
                                    float weight = dataSets.get(k).getDataPoints().get(j).getValue(dataSets.get(k).getDataType().getFields().get(0)).asFloat();
                                    long createdDateLong = dataSets.get(k).getDataPoints().get(j).getStartTime(TimeUnit.MILLISECONDS);
                                    createdDate = Utils.convertDateLongToString(createdDateLong);
                                    Utils.printLog("##GoogleFitsheartRate", "--" + weight);
                                    Utils.printLog("##GoogleFitsCreated", "--" + createdDate);
                                    vitalEntity.setId(UUID.randomUUID().toString());
                                    vitalEntity.setWeight(weight);
                                    vitalEntity.setVitalName(context.getResources().getString(R.string.vital_weight));
                                    vitalEntity.setCreatedDate(createdDate);
                                } else if (dataSets.get(k).getDataPoints().get(j).getDataType().getName().equals(VITAL_TEMPERATURE)) {
                                    Utils.printLog("##GoogleFit", "no");
                                    Utils.printLog("##GoogleFit", "no");
                                    float weight = dataSets.get(k).getDataPoints().get(j).getValue(dataSets.get(k).getDataType().getFields().get(0)).asFloat();
                                    long createdDateLong = dataSets.get(k).getDataPoints().get(j).getStartTime(TimeUnit.MILLISECONDS);
                                    createdDate = Utils.convertDateLongToString(createdDateLong);
                                    Utils.printLog("##GoogleFitsTem", "--" + weight);
                                    Utils.printLog("##GoogleFitsCreated", "--" + createdDate);
                                    vitalEntity.setId(UUID.randomUUID().toString());
                                    vitalEntity.setTemperature(weight);
                                    vitalEntity.setVitalName(context.getResources().getString(R.string.vital_temp));
                                    vitalEntity.setCreatedDate(createdDate);
                                } else if (dataSets.get(k).getDataPoints().get(j).getDataType().getName().equals(VITAL_SPO2)) {
                                    float spo2 = dataSets.get(k).getDataPoints().get(j).getValue(dataSets.get(k).getDataType().getFields().get(0)).asFloat();
                                    long createdDateLong = dataSets.get(k).getDataPoints().get(j).getStartTime(TimeUnit.MILLISECONDS);
                                    createdDate = Utils.convertDateLongToString(createdDateLong);
                                    Utils.printLog("##GoogleFitsspo2", "--" + spo2);
                                    Utils.printLog("##GoogleFitsCreated", "--" + createdDate);
                                    vitalEntity.setId(UUID.randomUUID().toString());
                                    vitalEntity.setSpo2(spo2);
                                    vitalEntity.setVitalName(context.getResources().getString(R.string.vital_spo2));
                                    vitalEntity.setCreatedDate(createdDate);
                                } else if (dataSets.get(k).getDataPoints().get(j).getDataType().getName().equals(VITAL_GLUCOSE)) {
                                    float glucose = dataSets.get(k).getDataPoints().get(j).getValue(dataSets.get(k).getDataType().getFields().get(0)).asFloat();
                                    long createdDateLong = dataSets.get(k).getDataPoints().get(j).getStartTime(TimeUnit.MILLISECONDS);
                                    createdDate = Utils.convertDateLongToString(createdDateLong);
                                    Utils.printLog("##GoogleFitsGlucose", "--" + glucose);
                                    Utils.printLog("##GoogleFitsCreated", "--" + createdDate);
                                    vitalEntity.setId(UUID.randomUUID().toString());
                                    vitalEntity.setGlucose(glucose);
                                    vitalEntity.setVitalName(context.getResources().getString(R.string.vital_glucose));
                                    vitalEntity.setCreatedDate(createdDate);
                                }
                            }
                            if (!createdDate.isEmpty()) {
                                List<VitalEntity> check = dataBase.daoVital().checkAlreadyInserted(vitalEntity.getCreatedDate());
                                if (check.size() == 0) {
                                    Utils.printLog("##GoogleFitsInsert", "--");
                                    dataBase.daoVital().insertVitalValues(vitalEntity);
                                } else {
                                    Utils.printLog("##GoogleFitsnotInsert", "--");
                                }
                            }
                        }
                    }
                }
            }
        }

    }

//    private VitalEntity vitalValuesSetMethod(DataPoint dataPoint, boolean isbp) {
//        VitalEntity vitalEntity = new VitalEntity();
//        float sys = 0.0F;
//        float dia = 0.0F;
//        if (isbp) {
//            sys = dataPoint.getValue(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC).asFloat();
//            dia = dataPoint.getValue(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC).asFloat();
//        }
//        long createdDateLong = dataPoint.getStartTime(TimeUnit.MILLISECONDS);
//        String createdDate = Utils.convertDateLongToString(createdDateLong);
//        Utils.printLog("##GoogleFitsdis", "--" + dia);
//        Utils.printLog("##GoogleFitsSys", "--" + sys);
//        Utils.printLog("##GoogleFitsCreated", "--" + createdDate);
//        vitalEntity.setId(UUID.randomUUID().toString());
//        vitalEntity.setDiastolic(dia);
//        vitalEntity.setVitalName(context.getResources().getString(R.string.vital_title));
//        vitalEntity.setSystolic(sys);
//        vitalEntity.setCreatedDate(createdDate);
//        return vitalEntity;
//    }

    private DataReadRequest getDatRequest() {

        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();
        //end time means now current time
        long endTime = current.getTime();
        calendar.add(Calendar.DATE, -100);
        Date end = calendar.getTime();
        //Start time means start date time
        long startTime = end.getTime();
        return new DataReadRequest.Builder()
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .read(TYPE_BLOOD_PRESSURE)
                .read(TYPE_HEART_RATE_BPM)
                .read(TYPE_BLOOD_GLUCOSE)
                .read(TYPE_BODY_TEMPERATURE)
                .read(TYPE_WEIGHT)
                .read(TYPE_OXYGEN_SATURATION)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();
    }

    private void initialView() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        boolean isGoogleFitSyn = sharedPreferences.getBoolean(IS_GOOGLE_FIT_SYNC, false);
        if (isGoogleFitSyn) {
            binding.googleSignInImageView.setVisibility(View.GONE);
            binding.vitalNameLayout.setVisibility(View.VISIBLE);
        } else {
            binding.googleSignInImageView.setVisibility(View.VISIBLE);
            binding.vitalNameLayout.setVisibility(View.GONE);
        }

        binding.googleSignInImageView.setOnClickListener(v -> {
            GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient client = GoogleSignIn.getClient(context, options);
            Intent signInIntent = client.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_FIT_ACCOUNT_PERMISSION);
        });
        binding.bpLayout.setOnClickListener(v -> listener.listenerData(VITAL_NAVIGATION, context.getResources().getString(R.string.vital_title)));
        binding.heartRateLayout.setOnClickListener(v -> listener.listenerData(VITAL_NAVIGATION, context.getResources().getString(R.string.vital_heart_rate)));
        binding.weightLayout.setOnClickListener(v -> listener.listenerData(VITAL_NAVIGATION, context.getResources().getString(R.string.vital_weight)));
        binding.spo2Layout.setOnClickListener(v -> listener.listenerData(VITAL_NAVIGATION, context.getResources().getString(R.string.vital_spo2)));
        binding.temperatureLayout.setOnClickListener(v -> listener.listenerData(VITAL_NAVIGATION, context.getResources().getString(R.string.vital_temp)));
        binding.glucoseLayout.setOnClickListener(v -> listener.listenerData(VITAL_NAVIGATION, context.getResources().getString(R.string.vital_glucose)));
    }

    private void requestGoogleFitPermission(GoogleSignInAccount account) {
        FitnessOptions fitnessOptions = getFitnessSignInOptions();
        GoogleSignIn.requestPermissions(this, GOOGLE_FIT_FITNESS_PERMISSION, account, fitnessOptions);
    }

    @Override
    public void listenerData(int action, Object data) {

    }
}

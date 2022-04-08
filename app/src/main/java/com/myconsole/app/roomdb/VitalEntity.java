package com.myconsole.app.roomdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VitalEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name = "systolic")
    float systolic;

    @ColumnInfo(name = "diastolic")
    float diastolic;
    @ColumnInfo(name = "heartRate")
    float heartRate;
    @ColumnInfo(name = "weight")
    float weight;
    @ColumnInfo(name = "temperature")
    float temperature;
    @ColumnInfo(name = "spo2")
    float spo2;
    @ColumnInfo(name = "glucose")
    float glucose;
    @ColumnInfo(name = "createdDate")
    String createdDate;

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getSpo2() {
        return spo2;
    }

    public void setSpo2(float spo2) {
        this.spo2 = spo2;
    }

    public float getGlucose() {
        return glucose;
    }

    public void setGlucose(float glucose) {
        this.glucose = glucose;
    }

    @ColumnInfo(name = "vitalName")
    String vitalName;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public float getSystolic() {
        return systolic;
    }

    public void setSystolic(float systolic) {
        this.systolic = systolic;
    }

    public float getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(float diastolic) {
        this.diastolic = diastolic;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getVitalName() {
        return vitalName;
    }

    public void setVitalName(String vitalName) {
        this.vitalName = vitalName;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

    public VitalEntity() {
    }
}

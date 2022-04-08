package com.myconsole.app.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoVital {

    @Query("select * from VitalEntity where vitalName IN(:vitalNames)")
    List<VitalEntity> getVitalValues(String vitalNames);

    @Query("select * from VitalEntity where createdDate IN(:createdDates)")
    List<VitalEntity> checkAlreadyInserted(String createdDates);

    @Insert
    void insertVitalValues(VitalEntity vitalEntity);

    @Delete
    void deleteValues(VitalEntity vitalEntity);
}

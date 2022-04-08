package com.myconsole.app.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {VitalEntity.class}, version =5)
public abstract class AppDataBase extends RoomDatabase {
    public abstract DaoVital daoVital();

    public static AppDataBase getInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDataBase.class, "MyConsole").allowMainThreadQueries().build();
    }

}

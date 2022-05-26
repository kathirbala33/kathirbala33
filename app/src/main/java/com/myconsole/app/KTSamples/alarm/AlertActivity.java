package com.myconsole.app.KTSamples.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;


import com.myconsole.app.KTSamples.LocalReciever;
import com.myconsole.app.databinding.ActivityAlaramBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlertActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ActivityAlaramBinding binding;
    int day, month, year, hour, minute, sec;
    int myday, myMonth, myYear, myHour, myMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlaramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectDatePicker();
    }

    private void selectDatePicker() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    private void setAlertForTime(long alertTime) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LocalReciever.class);
        intent.putExtra("Title", "Notification");
        intent.putExtra("Body", "Aleram");
        @SuppressLint("WrongConstant") PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
    }


    public static String getCurrentDate() {
        SimpleDateFormat common_post_date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date c = Calendar.getInstance().getTime();
        return common_post_date_format.format(c);
    }

    public static String addTimeInMinutes(String cTime, int addedValue) {
        String resultTime;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf1.parse(cTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, addedValue);
        resultTime = sdf1.format(calendar.getTime());
        return resultTime;

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        sec = calendar.get(Calendar.SECOND);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(myYear, myMonth, myday, myHour, myMinute, 0);
        Date chosenDate = cal.getTime();
        long alertTime = chosenDate.getTime();
        setAlertForTime(alertTime);

    }
}
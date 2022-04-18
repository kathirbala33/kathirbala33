package com.myconsole.app.commonClass;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static SimpleDateFormat PATTERN_YEAR = new SimpleDateFormat("yyyy", Locale.getDefault());
    public static SimpleDateFormat PATTERN_MONTH = new SimpleDateFormat("MMM", Locale.getDefault());
    public static SimpleDateFormat PATTERN_MONTH_FULL = new SimpleDateFormat("MMMM", Locale.getDefault());
    public static SimpleDateFormat PATTERN_DAY_OF_MONTH = new SimpleDateFormat("dd", Locale.getDefault());
    public static SimpleDateFormat PATTERN_DAY_OF_WEEK = new SimpleDateFormat("EEEE", Locale.getDefault());
    public static SimpleDateFormat PATTERN_TIME = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static SimpleDateFormat PATTERN_TIME_24H = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static SimpleDateFormat PATTERN_SERVER_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat PATTERN_SERVER_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat PATTERN_START_WITH_MONTH = new SimpleDateFormat("MMM dd , yyyy", Locale.getDefault());
    public static SimpleDateFormat PATTERN_START_WITH_MONTH_NO_YEAR = new SimpleDateFormat("MMMM dd", Locale.getDefault());
    public static SimpleDateFormat PATTERN_START_WITH_DATE_NO_YEAR = new SimpleDateFormat("dd MMMM", Locale.getDefault());
    public static SimpleDateFormat PATTERN_START_WITH_MONTH_SHORT_NO_YEAR = new SimpleDateFormat("MMM dd", Locale.getDefault());
    public static SimpleDateFormat PATTERN_START_WITH_MONTH_WITH_TIME = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat PATTERN_START_WITH_MONTH_SMALL_NO_YEAR = new SimpleDateFormat("MMM dd", Locale.getDefault());
    public static SimpleDateFormat PATTERN_FULL_TIME = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());


    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        String dateToStr = "";
        Date today = new Date();
        SimpleDateFormat format = null;
        format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        if (format != null) {
            dateToStr = format.format(today);
        }
        return dateToStr;
    }

    public static String changeDateFormat(String date, SimpleDateFormat currentFormat, SimpleDateFormat needToChangeFormat) {
        String changedFormat = "";
        Date date1 = null;
        try {
            date1 = currentFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        changedFormat = needToChangeFormat.format(date1);
        return changedFormat;
    }
}

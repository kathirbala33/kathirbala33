package com.myconsole.app.commonClass;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.myconsole.app.R;

import java.util.Date;

public class Utils {
    public static void printLog(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static String convertDateLongToString(long createdDateLong) {
        return DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(createdDateLong)).toString();
    }

 /*   public static String[] getVitalIDUsingName(String vitalName) {
String[] name =new  String[]{"BloodPressure","Heart Rate","Spo2","Weight","Temperature","Glucose"};
        return new String[name];
    }*/

    public static int getVitalIDUsingNames(String vitalName, Context context) {
        int vitalID = 0;
        if(vitalName.equals(context.getResources().getString(R.string.vital_title))){
            vitalID=0;
        }else if(vitalName.equals(context.getResources().getString(R.string.vital_heart_rate))){
            vitalID=2;
        }else if(vitalName.equals(context.getResources().getString(R.string.vital_spo2))){
            vitalID=1;
        }else if(vitalName.equals(context.getResources().getString(R.string.vital_weight))){
            vitalID=3;
        }else if(vitalName.equals(context.getResources().getString(R.string.vital_temp))){
            vitalID=4;
        }else if(vitalName.equals(context.getResources().getString(R.string.vital_glucose))){
            vitalID=5;
        }
        return vitalID;
    }
}

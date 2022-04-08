package com.myconsole.app.prefrenceManger;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;

public class PreferencesManager extends Activity {

    private  SharedPreferences preferences = this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
        editor.putString(",","");
    }

    @SuppressLint("CommitPrefEdits")
    public void setPreferencesValue(String key, String value) {
        preferences.edit().putString(key,value).apply();
    }
}

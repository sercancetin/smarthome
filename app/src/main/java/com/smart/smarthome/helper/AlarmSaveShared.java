package com.smart.smarthome.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.smart.smarthome.model.MenuModel;

public class AlarmSaveShared {
    private SharedPreferences preferences;

    public AlarmSaveShared(Context context) {
        preferences = context.getSharedPreferences("alarm",Context.MODE_PRIVATE);
    }
    public void setAlarm(String b){
        preferences.edit().putString("alarm",b).apply();
    }
    public String getAlarm(){
        return preferences.getString("alarm",null);
    }
    public void reset(){
        preferences.edit().clear().apply();
    }

}

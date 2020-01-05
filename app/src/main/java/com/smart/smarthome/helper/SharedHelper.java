package com.smart.smarthome.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {
    private SharedPreferences preferences;
    private Context context;

    public SharedHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
    }
    public void setUserLogin(Boolean b){
        preferences.edit().putBoolean("login",b).apply();
    }
    public boolean getUserLogin(){
        return preferences.getBoolean("login",false);
    }

}

package com.smart.smarthome.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smart.smarthome.helper.AlarmTaskHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmService extends BroadcastReceiver {
    AlarmTaskHelper alarmTaskHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmTaskHelper = new AlarmTaskHelper(context);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        alarmTaskHelper.doTheTask(currentTime);
        alarmTaskHelper.deleteAlarm(currentTime);
    }
}
package com.smart.smarthome.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.smart.smarthome.R;
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
        Log.d("authorize","AlarmService: Sistem saat: "+currentTime);
        alarmTaskHelper.doTheTask(currentTime);
        alarmTaskHelper.deleteHourAlarm(currentTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("kanal_id","kanalad", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("kanal_desc");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"kanal_id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Smart Home")
                .setContentText("Ayarladığınız görevler gerçekleştirildi.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());
    }
}

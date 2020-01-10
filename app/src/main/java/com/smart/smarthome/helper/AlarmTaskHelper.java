package com.smart.smarthome.helper;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smart.smarthome.model.MenuModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlarmTaskHelper {
    public AlarmSaveShared alarmSaveShared;
    private Gson gson;

    public AlarmTaskHelper(Context context) {
        alarmSaveShared = new AlarmSaveShared(context);
        gson = new Gson();
    }
    public ArrayList<MenuModel> getAlarmList(){
        String lists= alarmSaveShared.getAlarm();
        Type type = new TypeToken<ArrayList<MenuModel>>(){}.getType();
        if(lists!=null&&lists.length()>0){
            return gson.fromJson(lists,type);
        }
        return null;
    }
    public void setAlarmList(ArrayList<MenuModel> menu_list){
        alarmSaveShared.setAlarm(gson.toJson(menu_list));
    }
    public void deleteHourAlarm(String hour){
        ArrayList<MenuModel> list = getAlarmList();
        for(int i =0;i<list.size();i++){
            if(hour.equals(list.get(i).getHour())){
                list.remove(i);
                setAlarmList(list);
            }
        }
    }
    public void deleteIdAlarm(String id,String hour){
        ArrayList<MenuModel> list = getAlarmList();
        for(int i =0;i<list.size();i++){
            if(id.equals(list.get(i).getMenuid())&&hour.equals(list.get(i).getHour())){
                list.remove(i);
                setAlarmList(list);
            }
        }
    }
    public String getIdAlarm(String hour){
        ArrayList<MenuModel> list = getAlarmList();
        for(int i = 0;i<list.size();i++){
            if(list.get(i).getHour().equals(hour)){
                return list.get(i).getMenuid();
            }
        }
        return null;
    }
    public void doTheTask(String hour){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("menuler");
        ArrayList<MenuModel> list = getAlarmList();
        for(int i = 0;i<list.size();i++){
            if(hour.equals(list.get(i).getHour())){
                HashMap<String,Object> map = new HashMap<>();
                map.put("seekbar",list.get(i).getSeekbar());
                map.put("onoff",list.get(i).getOnoff());
                reference.child(list.get(i).getMenuid()).updateChildren(map);
            }
        }
    }
}

package com.smart.smarthome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.smart.smarthome.R;
import com.smart.smarthome.adapter.TaskListAdapter;
import com.smart.smarthome.base.BaseActivity;
import com.smart.smarthome.helper.AlarmTaskHelper;
import com.smart.smarthome.model.MenuModel;

import java.util.ArrayList;
import java.util.Collections;

public class TaskListActivity extends BaseActivity {
    RecyclerView rcy_task_list;
    TaskListAdapter taskListAdapter;
    AlarmTaskHelper alarmTaskHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        rcy_task_list = findViewById(R.id.rcy_task_list);
        setRecyclerManagerVertical(rcy_task_list);
        alarmTaskHelper = new AlarmTaskHelper(getApplicationContext());
        ArrayList<MenuModel> list = alarmTaskHelper.getAlarmList();
        Collections.reverse(list);
        taskListAdapter = new TaskListAdapter(this,list,alarmTaskHelper);
        rcy_task_list.setAdapter(taskListAdapter);
    }
}

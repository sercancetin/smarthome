package com.smart.smarthome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smart.smarthome.R;
import com.smart.smarthome.activity.MenuShow;
import com.smart.smarthome.activity.TaskListActivity;
import com.smart.smarthome.helper.AlarmTaskHelper;
import com.smart.smarthome.model.MenuModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private ArrayList<MenuModel> list;
    private AlarmTaskHelper alarmTaskHelper;
    private Context context;
    public TaskListAdapter(Context context,ArrayList<MenuModel> list, AlarmTaskHelper alarmTaskHelper) {
        this.list = list;
        this.alarmTaskHelper = alarmTaskHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        MenuModel menuModel = list.get(position);

        h.txt_menu_name.setText(menuModel.getMenuad()+" id: "+menuModel.getMenuid());
        h.txt_hour.setText(menuModel.getHour());
        h.txt_seekbar.setText(String.valueOf(menuModel.getSeekbar()));
        h.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTaskHelper.deleteIdAlarm(menuModel.getMenuid(),menuModel.getHour());
                context.startActivity(new Intent(context, TaskListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_menu_name)
        TextView txt_menu_name;
        @BindView(R.id.txt_hour)
        TextView txt_hour;
        @BindView(R.id.txt_seekbar)
        TextView txt_seekbar;
        @BindView(R.id.btn_delete)
        Button btn_delete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

package com.smart.smarthome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.smart.smarthome.R;
import com.smart.smarthome.base.BaseActivity;
import com.smart.smarthome.helper.AlarmTaskHelper;
import com.smart.smarthome.model.MenuModel;
import com.smart.smarthome.service.AlarmService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuShow extends BaseActivity {
    @BindView(R.id.btn_onoff)
    Button btn_onoff;
    @BindView(R.id.txt_seekbar)
    TextView txt_seekbar;
    @BindView(R.id.seekbar)
    SeekBar seekbar;

    DatabaseReference db;
    ArrayList<MenuModel> list;
    AlarmTaskHelper alarmTaskHelper;
    int button_value, seeksingle = 0, seekbars;
    private String menuid,choosetime,menuname;
    int value_seekbar;
    Calendar takvim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_show);
        ButterKnife.bind(this);
        menuid = getIntent().getStringExtra("menuid");
        db = FirebaseDatabase.getInstance().getReference("menuler").child(menuid);
        getValueMenu();
        alarmTaskHelper = new AlarmTaskHelper(this);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_seekbar.setText("%" + progress);
                seekbars = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        list = new ArrayList<MenuModel>();
        //Log.d("authorize","MenuShow: getList"+alarmTaskHelper.alarmSaveShared.getAlarm());
    }

    @OnClick(R.id.btn_onoff)
    void btn_onoffClick() {
        if (button_value == 1) {
            db.child("onoff").setValue(0);
            buttonControlBorder(0);
        } else {
            db.child("onoff").setValue(1);
            buttonControlBorder(1);
        }
    }
    @OnClick(R.id.btn_alarm_add) void btn_alarm_addClick(){
        // Şimdiki zaman bilgilerini alıyoruz. güncel saat, güncel dakika.
        takvim = Calendar.getInstance();
        int saat = takvim.get(Calendar.HOUR_OF_DAY);
        int dakika = takvim.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(minute<10){
                    choosetime = hourOfDay+":0"+minute;
                } else {
                    choosetime = hourOfDay+":"+minute;
                }
                setAlarmDialog(choosetime);
                takvim.set(takvim.get(Calendar.YEAR),takvim.get(Calendar.MONTH),
                        takvim.get(Calendar.DAY_OF_MONTH),
                        hourOfDay,minute,0);
            }
        }, saat, dakika, true);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", timePickerDialog);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", timePickerDialog);
        timePickerDialog.show();
    }

    @OnClick(R.id.btn_save1)
    void btn_save1Click() {
        db.child("seekbar").setValue(seekbars);
        message("Kayıt Edildi");
        seekbarControlBorder(seekbars);
    }
    @OnClick(R.id.btn_menu_delete)
    void btn_menu_deleteClick(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Menüyü silmek mi istiyorsunuz?");
        builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.removeValue();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void getValueMenu() {
        db.addValueEventListener(new ValueEventListener() {
            MenuModel menuModel;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    menuModel = dataSnapshot.getValue(MenuModel.class);
                    getSupportActionBar().setTitle(menuModel.getMenuad()+" (Menu id="+menuid+")");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    menuname = menuModel.getMenuad();
                    if (seeksingle == 0) {
                        seeksingle++;
                        seekbar.setProgress(menuModel.getSeekbar());
                        txt_seekbar.setText("%" + menuModel.getSeekbar());
                    }

                    button_value = menuModel.getOnoff();
                    if (menuModel.getOnoff() == 1) {
                        btn_onoff.setText("Açık");
                        btn_onoff.setBackground(getResources().getDrawable(R.drawable.button_bg_on));
                    } else {
                        btn_onoff.setText("Kapalı");
                        btn_onoff.setBackground(getResources().getDrawable(R.drawable.button_bg_off));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setAlarmDialog(String hour){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_set_alarm);
        Button btn_onoff1 = dialog.findViewById(R.id.btn_onoff1);
        TextView txt_seekbar1 = dialog.findViewById(R.id.txt_seekbar1);
        Button btn_save1 = dialog.findViewById(R.id.btn_save1);
        SeekBar seekbar1 = dialog.findViewById(R.id.seekbar1);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value_seekbar = progress;
                txt_seekbar1.setText("%"+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_onoff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message("Açık olmak zorunda");
            }
        });
        btn_save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmTaskHelper.getAlarmList()!=null){
                    list = alarmTaskHelper.getAlarmList();
                }
                MenuModel menuModel = new MenuModel();
                menuModel.setHour(hour);
                menuModel.setMenuid(menuid);
                menuModel.setMenuad(menuname);
                menuModel.setOnoff(1);
                menuModel.setSeekbar(value_seekbar);
                list.add(menuModel);
                alarmTaskHelper.setAlarmList(list);
                message("Kayıt Edildi...");
                setAlarm(takvim.getTimeInMillis());
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    public void setAlarm(long timemillies){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        Objects.requireNonNull(alarmManager).set(AlarmManager.RTC_WAKEUP,timemillies,pendingIntent);
    }
    private void seekbarControlBorder(int seekbar_val){
        if(seekbar_val == 255){
            db.child("onoff").setValue(1);
        } else if(seekbar_val == 0){
            db.child("onoff").setValue(0);
        }
    }
    private void buttonControlBorder(int button_val){
        if(button_val==1){
            db.child("seekbar").setValue(255);
            seekbar.setProgress(255);
            txt_seekbar.setText("%" + 255);
        } else{
            db.child("seekbar").setValue(0);
            seekbar.setProgress(0);
            txt_seekbar.setText("%" + 0);
        }
    }
}

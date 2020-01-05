package com.smart.smarthome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smart.smarthome.R;
import com.smart.smarthome.model.MenuModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuShow extends AppCompatActivity {
    @BindView(R.id.btn_onoff)
    Button btn_onoff;
    @BindView(R.id.txt_seekbar)
    TextView txt_seekbar;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    DatabaseReference db;
    int button_value,seeksingle=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_show);
        ButterKnife.bind(this);
        String menuid = getIntent().getStringExtra("menuid");
        db = FirebaseDatabase.getInstance().getReference("menuler").child(menuid);
        getValueMenu();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                db.child("seekbar").setValue(progress);
                txt_seekbar.setText("%"+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @OnClick(R.id.btn_onoff) void btn_onoffClick(){
        if(button_value==1){
            db.child("onoff").setValue(0);
        } else {
            db.child("onoff").setValue(1);
        }
    }
    private void getValueMenu(){
        db.addValueEventListener(new ValueEventListener() {
            MenuModel menuModel;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    menuModel = dataSnapshot.getValue(MenuModel.class);
                    getSupportActionBar().setTitle(menuModel.getMenuad());
                    if(seeksingle==0){
                        seeksingle++;
                        seekbar.setProgress(menuModel.getSeekbar());
                        txt_seekbar.setText("%"+menuModel.getSeekbar());
                    }

                    button_value = menuModel.getOnoff();
                    if(menuModel.getOnoff()==1){
                        btn_onoff.setText("Açık");
                        btn_onoff.setBackgroundColor(getResources().getColor(R.color.onbuton));
                    } else {
                        btn_onoff.setText("Kapalı");
                        btn_onoff.setBackgroundColor(getResources().getColor(R.color.offbuton));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

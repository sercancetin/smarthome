package com.smart.smarthome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.smart.smarthome.R;
import com.smart.smarthome.helper.SharedHelper;

public class SplashActivity extends AppCompatActivity {
    SharedHelper sharedHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedHelper = new SharedHelper(getApplicationContext());
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(sharedHelper.getUserLogin()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), UserLoginControl.class));
                    finish();
                }
            }
        }.start();
    }
}

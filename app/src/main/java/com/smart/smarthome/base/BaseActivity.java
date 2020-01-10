package com.smart.smarthome.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smart.smarthome.helper.SharedHelper;

public abstract class BaseActivity extends AppCompatActivity {
    public SharedHelper sharedHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedHelper = new SharedHelper(this);
    }

    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    protected void setRecyclerGridLayoutManager(RecyclerView recyclerView, int count) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), count));
    }
    public void setRecyclerManagerVertical(RecyclerView recyclerView){
        RecyclerView.LayoutManager manager= new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
    }
}

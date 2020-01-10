package com.smart.smarthome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smart.smarthome.R;
import com.smart.smarthome.adapter.MenuAdapter;
import com.smart.smarthome.base.BaseActivity;
import com.smart.smarthome.model.MenuModel;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    RecyclerView rcy_menu;
    MenuAdapter menuAdapter;
    ArrayList<MenuModel> list;
    Button btn_add_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcy_menu = findViewById(R.id.rcy_menu);
        btn_add_menu = findViewById(R.id.btn_add_menu);
        setRecyclerGridLayoutManager(rcy_menu,2);
        list = new ArrayList<>();
        getMenu();
        btn_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MenuInsert.class));
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("Oturumu Kapat");
        MenuItem menuItem1 = menu.add("Görev Listesi");
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedHelper.setUserLogin(false);
                startActivity(new Intent(getApplicationContext(), UserLoginControl.class));
                finish();
                return false;
            }
        });
        menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void getMenu(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("menuler");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            MenuModel m;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.exists()){
                        m = snapshot.getValue(MenuModel.class);
                        assert m != null;
                        m.setMenuid(snapshot.getKey());
                        list.add(m);
                    }
                }
                menuAdapter = new MenuAdapter(list,getApplicationContext());
                rcy_menu.setAdapter(menuAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

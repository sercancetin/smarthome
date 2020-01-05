package com.smart.smarthome.activity;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smart.smarthome.base.BaseActivity;
import com.smart.smarthome.MainActivity;
import com.smart.smarthome.R;
import com.smart.smarthome.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserLoginControl extends BaseActivity {
    @BindView(R.id.edtmail)
    EditText edtmail;
    @BindView(R.id.edtsifre)
    EditText edtsifre;
    Query db;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);
        ButterKnife.bind(this);
        db = FirebaseDatabase.getInstance().getReference("user").orderByChild("mail");
    }

    @OnClick(R.id.btnadmn)
    void btnadmnClick() {
        if (edtmail.getText().length() > 0 && edtsifre.getText().length() > 0) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Kontrol Ediliyor");
            dialog.show();
            db.equalTo(edtmail.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                User user;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            user = snapshot.getValue(User.class);
                            if (edtsifre.getText().toString().equals(user.getSifre())) {
                                sharedHelper.setUserLogin(true);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                message("Şifre yanlış.");
                                dialog.dismiss();
                            }
                        }
                    } else {
                        dialog.dismiss();
                        message("Böyle bir kullanıcı yok.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            message("Her hangi biri boş olamaz.");
        }
    }
}

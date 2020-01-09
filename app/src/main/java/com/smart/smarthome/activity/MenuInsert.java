package com.smart.smarthome.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smart.smarthome.R;
import com.smart.smarthome.base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuInsert extends BaseActivity {
    @BindView(R.id.new_post_image)
    ImageView new_post_image;
    @BindView(R.id.edt_btn_name)
    EditText edt_btn_name;
    @BindView(R.id.txt_seekbar)
    TextView txt_seekbar;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.switch_btn_case)
    Switch switch_btn_case;

    ProgressDialog progressDialog;

    private StorageReference storageReference;
    private String image_url="";
    private int switch_case = 0, seekbar_case=0;
    private static final int PICK_IMAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_insert);
        ButterKnife.bind(this);
        storageReference = FirebaseStorage.getInstance().getReference("image_upload");
        switch_btn_case.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switch_case = 1;
                    switch_btn_case.setText("Açık");
                } else {
                    switch_case = 0;
                    switch_btn_case.setText("Kapalı");
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_case = progress;
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

    @OnClick(R.id.btn_save)
    void btn_saveClick() {
        insert();
    }

    @OnClick(R.id.new_post_image) void new_post_imageClick(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE);
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Biraz Bekleyin");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CODE) {
            showProgressDialog();
            UploadTask uploadTask = storageReference.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        message("Başarısız");
                        progressDialog.dismiss();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        image_url = task.getResult().toString();
                        Picasso.get().load(image_url).into(new_post_image);
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
    private void insert(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("menuler");
        Map<String,Object> map = new HashMap<>();
        map.put("icon",image_url);
        map.put("menuad",edt_btn_name.getText().toString());
        map.put("onoff",switch_case);
        map.put("seekbar",seekbar_case);
        db.push().setValue(map);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}

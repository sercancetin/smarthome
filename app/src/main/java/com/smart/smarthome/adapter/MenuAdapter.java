package com.smart.smarthome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smart.smarthome.R;
import com.smart.smarthome.activity.MenuShow;
import com.smart.smarthome.model.MenuModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<MenuModel> list;
    private Context context;

    public MenuAdapter(ArrayList<MenuModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        MenuModel menuModel = list.get(position);
        if(menuModel.getIcon()!=null&&menuModel.getIcon().length()>0){
            Picasso.get().load(menuModel.getIcon()).into(h.img_logo);
        }
        h.txt_name.setText(menuModel.getMenuad());
        h.lyt_wrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MenuShow.class).putExtra("menuid",
                        menuModel.getMenuid()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.img_logo)
        ImageView img_logo;
        @BindView(R.id.lyt_wrap)
        LinearLayout lyt_wrap;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}

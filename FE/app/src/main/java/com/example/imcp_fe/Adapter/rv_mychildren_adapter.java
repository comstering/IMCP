package com.example.imcp_fe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.imcp_fe.Child;
import com.example.imcp_fe.Data.rv_mychildren_data;
import com.example.imcp_fe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class rv_mychildren_adapter extends RecyclerView.Adapter<rv_mychildren_adapter.ViewHolder> {
    private ArrayList<rv_mychildren_data> mychildren;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mychild_photo;
        TextView mychild_name;

        public ViewHolder(View itemView) {
            super(itemView);
            mychild_photo = itemView.findViewById(R.id.mychild_photo);
            mychild_name = itemView.findViewById(R.id.mychild_name);
        }
    }


    @Override
    public int getItemCount() {
        return mychildren.size();
    }

    public rv_mychildren_adapter(Context context) {
        this.context = context;
    }

    public rv_mychildren_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_children, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    public void onBindViewHolder(rv_mychildren_adapter.ViewHolder holder, final int position) {
        final rv_mychildren_data item = mychildren.get(position);
        Picasso.with(context).load("http://tomcat.comstering.synology.me/IMCP_Server/upload/" + item.getRv_mychild_image()).into(holder.mychild_photo);

        holder.mychild_name.setText(item.getRv_mychild_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Child.class);
                intent.putExtra("key", item.getRv_mychild_Key());
                intent.putExtra("image", item.getRv_mychild_image());
                intent.putExtra("name", item.getRv_mychild_name());
                intent.putExtra("birth", item.getRv_mychild_Birth());

                context.startActivity(intent);
            }
        });
    }


    public rv_mychildren_adapter(Activity activity, ArrayList<rv_mychildren_data> list) {
        this.mychildren = list;//처리하려는 아이템 리스트
        this.context = activity;//보여지는 엑티비티
    }


}


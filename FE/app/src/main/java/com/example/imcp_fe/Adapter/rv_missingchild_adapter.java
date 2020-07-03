package com.example.imcp_fe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.imcp_fe.Data.rv_missingchild_data;
import com.example.imcp_fe.Missing_child_info;
import com.example.imcp_fe.R;

import java.util.ArrayList;

public class rv_missingchild_adapter extends RecyclerView.Adapter<rv_missingchild_adapter.ViewHolder>{
    private ArrayList<rv_missingchild_data> missingchild;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView missingchild_photo;
        TextView missingchild_name;

        public ViewHolder(View itemView){
            super(itemView);
            missingchild_photo = itemView.findViewById(R.id.missingchild_photo);
            missingchild_name = itemView.findViewById(R.id.missingchild_name);
        }
    }


    @Override
    public int getItemCount() {
        return missingchild.size();
    }

    public rv_missingchild_adapter(Context context){
        this.context = context;
    }

    public rv_missingchild_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_missingchild, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }



    public void onBindViewHolder(rv_missingchild_adapter.ViewHolder holder, final int postion){
        final rv_missingchild_data item = missingchild.get(postion);

        holder.missingchild_photo.setImageBitmap(item.getRv_missingchild_image());
        holder.missingchild_name.setText(item.getRv_missingchild_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Missing_child_info.class);
                context.startActivity(intent);
            }
        });
    }


    public rv_missingchild_adapter(Activity activity, ArrayList<rv_missingchild_data>list){
        this.missingchild =list;//처리하려는 아이템 리스트
        this.context =activity;//보여지는 엑티비티
    }


}
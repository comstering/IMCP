package com.example.imcp_fe.Parents.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imcp_fe.Parents.Data.rv_missingchild_data;
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
            missingchild_photo = itemView.findViewById(R.id.mychild_photo);
            missingchild_name = itemView.findViewById(R.id.mychild_name);
        }
    }


    @Override
    public int getItemCount() {
        return missingchild.size();
    }

    public rv_missingchild_adapter(Context context){
        this.context = context;
    }

    public rv_missingchild_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_missingchild, parent, false);
        rv_missingchild_adapter.ViewHolder vh = new rv_missingchild_adapter.ViewHolder(view);

        return vh;
    }



    public void onBindViewHolder(rv_missingchild_adapter.ViewHolder holder, final int postion){
        final rv_missingchild_data item = missingchild.get(postion);

        holder.missingchild_photo.setImageBitmap(item.getRv_missingchild_image());
        holder.missingchild_name.setText(item.getRv_missingchild_name());
    }


    public rv_missingchild_adapter(Activity activity, ArrayList<rv_missingchild_data>list){
        this.missingchild =list;//처리하려는 아이템 리스트
        this.context =activity;//보여지는 엑티비티
    }

    private Bitmap StringToBitmap(String encodedString){//String을 Bitmap으로 변환
        byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length) ;
        return bitmap;
    }
}
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

import androidx.recyclerview.widget.RecyclerView;
import com.example.imcp_fe.Parents.Data.rv_mychildren_data;
import com.example.imcp_fe.R;

import java.util.ArrayList;

public class rv_mychildren_adapter extends RecyclerView.Adapter<rv_mychildren_adapter.ViewHolder> {
    private ArrayList<rv_mychildren_data> mychildren;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mykids_photo;
        TextView mykids_name;

        public ViewHolder(View itemView){
            super(itemView);
            mykids_photo = itemView.findViewById(R.id.mykids_photo);
            mykids_name = itemView.findViewById(R.id.mykids_name);
        }
    }


    @Override
    public int getItemCount() {
        return mychildren.size();
    }

    public rv_mychildren_adapter(Context context){
        this.context = context;
    }

    public rv_mychildren_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_mykids, parent, false);
        rv_mychildren_adapter.ViewHolder vh = new rv_mychildren_adapter.ViewHolder(view);

        return vh;
    }

    public void onBindViewHolder(rv_mychildren_adapter.ViewHolder holder, final int postion){
        final rv_mychildren_data item = mychildren.get(postion);

        holder.mykids_photo.setImageBitmap(StringToBitmap(item.getRv_mykids_image()));
        holder.mykids_name.setText(item.getRv_mykids_name());
    }


    public rv_mychildren_adapter(Activity activity, ArrayList<rv_mychildren_data>list){
        this.mychildren =list;//처리하려는 아이템 리스트
        this.context =activity;//보여지는 엑티비티
    }

    private Bitmap StringToBitmap(String encodedString){//String을 Bitmap으로 변환
        byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length) ;
        return bitmap;
    }
}


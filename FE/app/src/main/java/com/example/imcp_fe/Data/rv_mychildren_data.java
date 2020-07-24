package com.example.imcp_fe.Data;

import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;
import android.widget.ImageView;

public class


rv_mychildren_data {
    private String rv_mychild_image;
    private String rv_mychild_name;
    private  String key;
    private  String birth;
//부모의 메인창인 내 아이들의 리스트 데이터들(사진, 이름) rv = RecyclerView
    public void setRv_mychild_image(String rv_mychild_image){ this.rv_mychild_image = rv_mychild_image; }

    public void setRv_mychild_name(String rv_mychild_name){
        this.rv_mychild_name =rv_mychild_name.trim();
    }
    public void setkey(String key){
        this.key =key;
    }
    public void setbirth(String birth){
        this.birth= birth ;
    }

    public String getRv_mychild_image(){
        return rv_mychild_image;
    }

    public String getRv_mychild_name(){
        return rv_mychild_name;
    }

    public String getRv_mychild_Key(){
        return key;
    }
    public String getRv_mychild_Birth(){
        return birth;
    }
}

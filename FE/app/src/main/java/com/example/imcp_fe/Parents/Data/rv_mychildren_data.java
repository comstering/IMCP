package com.example.imcp_fe.Parents.Data;

import android.graphics.Bitmap;

public class rv_mychildren_data {
    private Bitmap rv_mychild_image;
    private String rv_mychild_name;

//부모의 메인창인 내 아이들의 리스트 데이터들(사진, 이름) rv = RecyclerView
    public void setRv_mychild_image(Bitmap rv_mykids_image){ this.rv_mychild_image = rv_mychild_image; }

    public void setRv_mychild_name(String rv_mykids_name){
        this.rv_mychild_name =rv_mychild_name;
    }

    public Bitmap getRv_mychild_image(){
        return rv_mychild_image;
    }

    public String getRv_mychild_name(){
        return rv_mychild_name;
    }
}

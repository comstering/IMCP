package com.example.imcp_fe.Parents.Data;

import android.graphics.Bitmap;

public class rv_missingchild_data {
    private Bitmap rv_missingchild_image;
    private String rv_missingchild_name;

    //부모의 메인창인 내 아이들의 리스트 데이터들(사진, 이름) rv = RecyclerView
    public void setRv_missingchild_image(Bitmap rv_mykids_image){ this.rv_missingchild_image = rv_mykids_image; }

    public void setRv_missingchild_name(String rv_mykids_name){
        this.rv_missingchild_name =rv_mykids_name;
    }

    public Bitmap getRv_missingchild_image(){
        return rv_missingchild_image;
    }

    public String getRv_missingchild_name(){
        return rv_missingchild_name;
    }

}

package com.example.imcp_fe.Data;

import android.graphics.Bitmap;

public class rv_missingchild_data {
    private String rv_missingchild_image;
    private String rv_missingchild_name;
    private String key;
    private String birth;
    private String parentPhone;

    //부모의 메인창인 내 아이들의 리스트 데이터들(사진, 이름) rv = RecyclerView
    public void setRv_missingchild_image(String rv_missingchild_image){ this.rv_missingchild_image = rv_missingchild_image; }

    public void setRv_missingchild_name(String rv_missingchild_name){
        this.rv_missingchild_name =rv_missingchild_name;
    }
    public void setRv_missingchild_key(String key){
        this.key = key;
    }
    public void setRv_missingchild_birth(String birth){
        this.birth = birth;
    }

    public void setRv_missingchild_Phone(String parentPhone){
        this.parentPhone =parentPhone;
    }

    public String getRv_missingchild_image(){
        return rv_missingchild_image;
    }

    public String getRv_missingchild_name(){
        return rv_missingchild_name;
    }

    public  String getRv_missingchild_key(){
        return key;
    }

    public String getRv_missingchild_birth(){
        return birth;
    }

    public String getRv_missingchild_Phone() {
        return parentPhone;
    }
}



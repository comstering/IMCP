package com.example.imcp_fe.Parents.Data;

public class rv_mychildren_data {
    private String rv_mykids_image;
    private String rv_mykids_name;

//부모의 메인창인 내 아이들의 리스트 데이터들(사진, 이름) rv = RecyclerView
    public void setRv_mykids_image(String rv_mykids_image){ this.rv_mykids_image = rv_mykids_image; }

    public void setRv_mykids_name(String rv_mykids_name){
        this.rv_mykids_name =rv_mykids_name;
    }

    public String getRv_mykids_image(){
        return rv_mykids_image;
    }

    public String getRv_mykids_name(){
        return rv_mykids_name;
    }
}

package com.example.imcp_fe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
아이디 찾기
* */
public class Get_id extends AppCompatActivity {

    private String ID;
    private Intent intent;
    private TextView tv_getid_checkid;
    private Button btn_getid_login;
    private Button btn_getId_findpw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.get_id);
        intent = getIntent();
        ID = intent.getStringExtra("ID");

        //뷰 인스턴스 저장
        tv_getid_checkid = findViewById(R.id.tv_getid_checkid);
        btn_getid_login = findViewById(R.id.btn_getid_login);
        btn_getId_findpw = findViewById(R.id.btn_getid_findpw);
        tv_getid_checkid.setText(ID);

        //로그인 엑티비티로 전환
        btn_getid_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ParentsLoginActivity.class);
                startActivity(intent);
            }
        });
        //비밀번호 찾기 엑티비티로 전환
        btn_getId_findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ParentsFindPw.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);
    }
}

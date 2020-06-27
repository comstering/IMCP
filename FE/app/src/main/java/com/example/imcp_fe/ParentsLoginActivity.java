package com.example.imcp_fe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ParentsLoginActivity extends AppCompatActivity {

    private char[] id;
    private char[] pw;

    private Button btn_parents_login, btn_parents_signup, btn_parents_findid, btn_parents_findpw;

    private EditText sign_id, sign_pw;

    // 부모 로그인 창
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_login);

        btn_parents_login = (Button)findViewById(R.id.btn_parents_login);


        // 부모 회원가입
        btn_parents_signup = (Button)findViewById(R.id.btn_parents_signup);
        btn_parents_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),ParentsSignup.class);
                startActivity(intent);
//                id = sign_id.getText().toString().toCharArray();
//                pw = sign_pw.getText().toString().toCharArray();
            }
        });

        // 부모 아이디 찾기
        btn_parents_findid = (Button)findViewById(R.id.btn_parents_findid);
        btn_parents_findid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),ParentsFindId.class);
                startActivity(intent);
            }
        });

        // 부모 비밀번호 찾기
        btn_parents_findpw = (Button)findViewById(R.id.btn_parents_findpw);
        btn_parents_findpw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),ParentsFindPw.class);
                startActivity(intent);
            }
        });
    }
}

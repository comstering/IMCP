package com.example.imcp_fe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ParentsLoginActivity extends AppCompatActivity {

    private char[] email;
    private char[] name;
    private char[] id;
    private char[] pw;
    private char[] re_pw;

    private Button btn_signup_ok, btn_signup_cancle;

    private EditText sign_id, sign_pw, sign_re_pw, sign_name,
            sign_number, sign_email;

    // 부모 로그인 창
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_login);

        btn_signup_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                id = sign_id.getText().toString().toCharArray();
                pw = sign_pw.getText().toString().toCharArray();
                pw = sign_re_pw.getText().toString().toCharArray();
                name =sign_name.getText().toString().toCharArray();
                email = sign_email.getText().toString().toCharArray();
                email = sign_number.getText().toString().toCharArray();
            }
        });
    }
}

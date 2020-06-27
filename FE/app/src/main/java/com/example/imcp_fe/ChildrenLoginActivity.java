package com.example.imcp_fe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import javax.xml.datatype.Duration;

public class ChildrenLoginActivity extends AppCompatActivity {
    EditText et_children_new_pw, et_children_re_pw;
    String sNewPW, sRePW;
    Button btn_children_create_pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.children_first_password_page);

        // 비밀번호 입력 초기화
        et_children_new_pw = (EditText) findViewById(R.id.et_children_new_pw);
        et_children_re_pw = (EditText) findViewById(R.id.et_children_re_pw);
        btn_children_create_pw = (Button) findViewById(R.id.btn_children_create_pw);

        sNewPW = et_children_new_pw.getText().toString();
        sRePW = et_children_re_pw.getText().toString();
    }


    // 비밀번호 생성 버튼 명령
    public void btn_children_create_pw(View view) {
        sNewPW = et_children_new_pw.getText().toString();
        sRePW = et_children_re_pw.getText().toString();

        if (sNewPW.equals(sRePW)) {

            // 비밀번호 일치
            Toast.makeText(getApplicationContext(), "비밀번호 맞음", Toast.LENGTH_LONG).show();

            // 고유키 발급 창
            setContentView(R.layout.new_primary_key);
        } else {
            // 비밀번호 불일치
            Toast.makeText(getApplicationContext(), "비밀번호 틀림", Toast.LENGTH_LONG).show();
        }
    }
}
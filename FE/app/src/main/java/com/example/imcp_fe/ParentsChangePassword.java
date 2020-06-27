package com.example.imcp_fe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParentsChangePassword extends AppCompatActivity {
    EditText et_parents_change_pw, et_parents_change_re_pw;
    String sNewPW, sRePw;
    Button btn_parents_change_pw_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_change_password);

        // 비밀번호 입력 초기화
        et_parents_change_pw=(EditText)findViewById(R.id.et_parents_change_pw);
        et_parents_change_re_pw=(EditText)findViewById(R.id.et_parents_change_re_pw);
        btn_parents_change_pw_ok=(Button)findViewById(R.id.btn_parents_change_pw_ok);

        sNewPW = et_parents_change_pw.getText().toString();
        sRePw=et_parents_change_re_pw.getText().toString();

    }

    // 비밀번호 생성 버튼 명령
    public void btn_parents_change_pw_ok(View view){
        sNewPW=et_parents_change_pw.getText().toString();
        sRePw=et_parents_change_re_pw.getText().toString();

        if (sNewPW.equals(sRePw)) {

            // 비밀번호 일치
            Toast.makeText(getApplicationContext(), "비밀번호 맞음", Toast.LENGTH_LONG).show();

            setContentView(R.layout.new_primary_key);
        } else {
            // 비밀번호 불일치
            Toast.makeText(getApplicationContext(), "비밀번호 틀림", Toast.LENGTH_LONG).show();
        }

    }
}

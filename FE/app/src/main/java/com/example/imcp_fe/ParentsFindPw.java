package com.example.imcp_fe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class ParentsFindPw extends AppCompatActivity {

    private Button btn_parents_findpw;
    private EditText et_parents_findpw_name, et_parents_findpw_id, et_parents_findpw_email;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_find_password);

        btn_parents_findpw = (Button)findViewById(R.id.btn_parents_findpw);

        // 이름, 아이디, 이메일 서버에 저장된거랑 비교
        //et_parents_findpw_id

        // 재발급 신청 버튼 클릭 시
        // 이메일로 임시비밀번호 발급


    }
}

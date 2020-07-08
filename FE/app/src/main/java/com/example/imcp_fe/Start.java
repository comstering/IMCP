package com.example.imcp_fe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Start extends AppCompatActivity {

    private Button ib_children, ib_Parents;
    private SharedPreferences login_preference;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_preference = getSharedPreferences("Login",MODE_PRIVATE);
        Log.e("key", login_preference.getString("key",""));

        setContentView(R.layout.start);
        if(login_preference.getString("key","").equals(null)==false){
            intent = new Intent(getApplicationContext(), Child_main.class);
            startActivity(intent);
        }else if(login_preference.getString("id","").equals(null)==false){
            intent = new Intent(getApplicationContext(), ParentsLoginActivity.class);
            startActivity(intent);
        }else {

            // 아이 버튼 클릭 시 아이 비밀번호 입력창으로 이동.
            ImageButton ibChildren = (ImageButton)findViewById(R.id.ib_Children);
            ibChildren.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(getApplicationContext(), Keypassword.class);
                    //ChildrenLoginActivity.class
                    startActivity(intent);
                }
            });

            // 부모 버튼 클릭 시 부모 로그인 창으로 이동.
            ImageButton ibParents = (ImageButton)findViewById(R.id.ib_Parents);
            ibParents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),ParentsLoginActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
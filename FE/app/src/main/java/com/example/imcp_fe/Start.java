package com.example.imcp_fe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Start extends AppCompatActivity {

    private Button ib_children, ib_Parents;
    private SharedPreferences login_preference;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    &&ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

        login_preference = getSharedPreferences("Login",MODE_PRIVATE);
        Log.e("Login", login_preference.getString("key","null"));
        Log.e("Login", login_preference.getString("id","null"));
        setContentView(R.layout.start);





        출처: https://bottlecok.tistory.com/49 [잡캐의 IT 꿀팁]



        if(!login_preference.getString("key","null").equals("null")){
            intent = new Intent(getApplicationContext(), Child_main.class);
            startActivity(intent);
        }else if(!login_preference.getString("id","null").equals("null")){
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
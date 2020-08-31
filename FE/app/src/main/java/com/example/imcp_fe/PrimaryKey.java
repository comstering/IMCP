package com.example.imcp_fe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

/*
 * 고유키 확인
 * */
public class PrimaryKey extends AppCompatActivity {


    private TextView textView;
    private SharedPreferences login_preference;
    private long backKeyPressedTime = 0;
    private Toast toast;

    /*
     * 엑티비티 생성 시 호출
     * 사용자 인터페이스 설정
     *  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primary_key);

        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        textView = findViewById(R.id.tv_PrimaryKey_key);
        Log.e("key", "ss : " + login_preference.getString("key", "null"));
        textView.setText(login_preference.getString("key", "null"));

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrimaryKey.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }

    }

    //뒤로가기 2회 입력 시 종료
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            toast.cancel();

        }
    }
}




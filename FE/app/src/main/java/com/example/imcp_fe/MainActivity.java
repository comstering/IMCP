package com.example.imcp_fe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 아이 버튼 클릭 시 아이 비밀번호 입력창으로 이동.
        ImageButton ibChildren = (ImageButton)findViewById(R.id.ib_Children);
        ibChildren.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),ChildrenLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.imcp_fe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
<<<<<<< HEAD
import android.widget.ImageButton;

import com.example.imcp_fe.Parents.parents_main;
=======
import android.widget.Button;
import android.widget.ImageButton;
>>>>>>> origin/bjs

public class MainActivity extends AppCompatActivity {

    private Button ib_children, ib_Parents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        ImageButton ib_parents = (ImageButton)findViewById(R.id.ib_Parents);
        ImageButton ib_children = (ImageButton)findViewById(R.id.ib_Children);

        ib_parents.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), parents_main.class);

        // 아이 버튼 클릭 시 아이 비밀번호 입력창으로 이동.
        ImageButton ibChildren = (ImageButton)findViewById(R.id.ib_Children);
        ibChildren.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),ChildrenLoginActivity.class);
:
       
<<<<<<< HEAD
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

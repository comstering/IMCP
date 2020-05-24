package com.example.imcp_fe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.imcp_fe.Parents.parents_main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton ib_parents = (ImageButton)findViewById(R.id.ib_Parents);
        ImageButton ib_children = (ImageButton)findViewById(R.id.ib_Children);

        ib_parents.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), parents_main.class);
                startActivity(intent);
            }
        });

        ib_children.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}

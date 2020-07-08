package com.example.imcp_fe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PrimaryKey extends AppCompatActivity {

    private TextView textView;
    private SharedPreferences login_preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primary_key);

        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        textView = findViewById(R.id.tv_PrimaryKey_key);
        Log.e("key", "ss : "+login_preference.getString("key","null"));
        textView.setText(login_preference.getString("key","null"));

        }

    }




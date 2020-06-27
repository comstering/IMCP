package com.example.imcp_fe;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PrimaryKey extends AppCompatActivity {
    Random rnd;
    private Button editText;
    private TextView textView;
    String randStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_primary_key);
        editText = findViewById(R.id.btn_PrimaryKey);
        textView = findViewById(R.id.tv_PrimaryKey);
/*
        randStr =  new RandomStringBuilder().
                putLimitedChar(RandomStringBuilder.ALPHABET_UPPER_CASE).
                putLimitedChar(RandomStringBuilder.SPECIAL).
                putExcludedChar("?.,&$/\\\"'").
                setLength(32).build();
        System.out.println(randStr);
*/


      /*  editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(s.toString());
            }
        });*/
    }



}
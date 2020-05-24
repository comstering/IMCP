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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_primary_key);

        this.getViewObject();

        editText.addTextChangedListener(new TextWatcher() {
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
        });
    }

    private void getViewObject() {
        editText = findViewById(R.id.btn_PrimaryKey);
        textView = findViewById(R.id.tv_PrimaryKey);
    }
}

/*
        rnd = new Random();
        Button PrimaryKey = (Button) findViewById(R.id.btn_PrimaryKey);
        final TextView tvPrimaryKey = (TextView) findViewById(R.id.tv_PrimaryKey);
//        PrimaryKey.setOnClickListener(t);
        Button.setOnClickListener(new View.OnClickListener(){
//        Button.OnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Button PrimaryKey = (Button) findViewById(v.getId());
                int num = rnd.nextInt(100);
                tvPrimaryKey.setText(num);
            }
        });
    }
}
*/

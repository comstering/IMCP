package com.example.imcp_fe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.imcp_fe.Network.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParentsSignup extends AppCompatActivity {

    private EditText et_parentsignup_id;
    private EditText et_parentsignup_pw;
    private EditText et_parentsignup_repw;
    private EditText et_parentsignup_name;
    private EditText et_parentsignup_phone;
    private EditText et_parentsignup_email;
    private  Button btn_parentsignup_signok;
    private char[] id;
    private char[] pw;
    private char[] repw;
    private char[] name;
    private char[] phone;
    private  char[] email;
    private SharedPreferences login_preference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_signup);

        et_parentsignup_id = findViewById(R.id.et_sign_id);
        et_parentsignup_pw = findViewById(R.id.et_sign_pw);
        et_parentsignup_repw = findViewById(R.id.et_sign_re_pw);
        et_parentsignup_name = findViewById(R.id.et_sign_name);
        et_parentsignup_phone = findViewById(R.id.et_sign_phone_num);
        et_parentsignup_email = findViewById(R.id.et_sign_email);
        btn_parentsignup_signok = findViewById(R.id.btn_signup_ok);
        login_preference = getSharedPreferences("Login", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = login_preference.edit();

        btn_parentsignup_signok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = et_parentsignup_id.getText().toString().toCharArray();
                pw = et_parentsignup_pw.getText().toString().toCharArray();
                repw = et_parentsignup_repw.getText().toString().toCharArray();
                name = et_parentsignup_name.getText().toString().toCharArray();
                phone = et_parentsignup_phone.getText().toString().toCharArray();
                email = et_parentsignup_email.getText().toString().toCharArray();

                if(TextUtils.isEmpty(String.valueOf(id))){
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(String.valueOf(pw))){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(String.valueOf(name))){
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(String.valueOf(phone))){
                    Toast.makeText(getApplicationContext(), "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(String.valueOf(email))){
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(String.valueOf(pw).equals(String.valueOf(repw))==false){
                    Toast.makeText(getApplicationContext(), "비밀번호가 같은지 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if(String.valueOf(pw).equals(String.valueOf(repw))==true){
                    editor.putString("id", String.valueOf(id));
                    editor.putString("pw", String.valueOf(id));
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });



    }

    public void signRequest() {
        String url = "https://www.google.co.kr";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jarray = new JSONArray(response);
                            int size = jarray.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject row = jarray.getJSONObject(i);
                             /*   x= row.getDouble("x"); // x, y 좌표를 받아옴.
                                y = row.getDouble("y");
                           */ }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
         //       params.put("",sendlocation);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}

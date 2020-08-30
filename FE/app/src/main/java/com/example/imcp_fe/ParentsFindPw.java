package com.example.imcp_fe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.Network.AppHelper;

import java.util.HashMap;
import java.util.Map;

/*
 * 비밀번호 찾기
 * */
public class ParentsFindPw extends AppCompatActivity {

    private Button btn_parents_findpw;

    //edittexet 변수
    private EditText et_parents_findpw_name, et_parents_findpw_id, et_parents_findpw_email;

    //서버url
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/parentFindPW.jsp";

    private String name;
    private String id;
    private String email;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password);

        //인스턴스 설정
        btn_parents_findpw = (Button) findViewById(R.id.btn_parents_findpw);
        et_parents_findpw_name = findViewById(R.id.et_parents_findpw_name);
        et_parents_findpw_id = findViewById(R.id.et_parents_findpw_id);
        et_parents_findpw_email = findViewById(R.id.et_parents_findpw_email);

        //공란 확인 volley 호출
        btn_parents_findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                name = et_parents_findpw_name.getText().toString();
                id = et_parents_findpw_id.getText().toString();
                email = et_parents_findpw_email.getText().toString();

                if (TextUtils.isEmpty(String.valueOf(name))) {//이름 공란
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(String.valueOf(id))) {//id 공란
                    Toast.makeText(getApplicationContext(), "아이디을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(String.valueOf(email))) {//이미일 공란
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    findpwRequest(url);

                }

            }
        });


    }

    //비밀번호 변경 엑티비티로 전환
    public void changePW() {
        intent = new Intent(getApplicationContext(), Change_password.class);
        startActivity(intent);
    }


    public void findpwRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "FindSucess":
                                Toast.makeText(getApplicationContext(), "Sucess", Toast.LENGTH_SHORT).show();
                                changePW();
                                break;
                            case "FindPWFail":
                                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("volley", response);
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", id);
                params.put("Name", name);
                params.put("Email", email);
                return params;
            }
        };


        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }

}


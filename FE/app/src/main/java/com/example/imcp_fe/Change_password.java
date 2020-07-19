package com.example.imcp_fe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
 부모 계정 비밀번호 변경
* */
public class Change_password extends AppCompatActivity {

    private EditText et_changepassword_pw;
    private EditText et_changepassword_repw;
    private Button btn_changepassword_pwok;
    private String pw;
    private String repw;
    private String email;
    private String id;
    private Intent intent;
    private SharedPreferences login_preference;
    private final SharedPreferences.Editor editor = login_preference.edit();
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/parentNewPW.jsp";


    /*
    엑티비티 생성 시 호출
    사용자 인터페이스 설정
    버튼 이벤트 설정
    * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.change_password);
        login_preference = getSharedPreferences("Login", Activity.MODE_PRIVATE);

        et_changepassword_pw = findViewById(R.id.et_changepassword_pw);
        et_changepassword_repw = findViewById(R.id.et_changepassword_repw);
        btn_changepassword_pwok = findViewById(R.id.btn_changepassword_pwok);

        intent = getIntent();
        id = intent.getStringExtra("ID");
        email = intent.getStringExtra("eamil");


        btn_changepassword_pwok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pw = et_changepassword_pw.getText().toString();
                repw = et_changepassword_repw.getText().toString();

                if (pw.equals(repw) == false) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if (pw.equals(repw) == true) {
                    setpwRequest(url);
                }
            }
        });


        super.onCreate(savedInstanceState);
    }

/*
* Login 엑티비티로 전환
* 새로 설정한 pw를 sharedpreference로 저장
* */
    public void setPW() {
        editor.putString("pw", pw);
        editor.commit();
        intent = new Intent(getApplicationContext(), ParentsLoginActivity.class);
        startActivity(intent);
    }

/*
* volley 통신
* ID, 새로운 패스워드, 이메일 파라미터로 전송
* */
    public void setpwRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "NewPWSucess":
                                Toast.makeText(getApplicationContext(), "비밀번호가 새로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                                setPW();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", id);
                params.put("newPassword", pw);
                params.put("Email", email);
                return params;
            }
        };


        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }


}


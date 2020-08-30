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

    //비밀번호, 변경할 비밀번호 입력 변수
    private EditText et_changepassword_pw;
    private EditText et_changepassword_repw;

    //등록 버튼 변수
    private Button btn_changepassword_pwok;

    //값 저장 변수
    private String pw;
    private String repw;
    private String email;
    private String id;

    private Intent intent;

    //SharedPreferences 변수
    private SharedPreferences login_preference;
    private final SharedPreferences.Editor editor = login_preference.edit();

    //서버 url
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/parentNewPW.jsp";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.change_password);

        //SharedPreferences 인스턴스
        login_preference = getSharedPreferences("Login", Activity.MODE_PRIVATE);

        //각 변수 인스턴스를 받아옴
        et_changepassword_pw = findViewById(R.id.et_changepassword_pw);
        et_changepassword_repw = findViewById(R.id.et_changepassword_repw);
        btn_changepassword_pwok = findViewById(R.id.btn_changepassword_pwok);


        intent = getIntent();
        //받아온 ID, email 값 저장
        id = intent.getStringExtra("ID");
        email = intent.getStringExtra("eamil");


        //등록 버튼 이벤트
        btn_changepassword_pwok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //eidttext에서 변수 저장
                pw = et_changepassword_pw.getText().toString();
                repw = et_changepassword_repw.getText().toString();

                if (pw.equals(repw) == false) {//비밀번호가 일치하지 않을때
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if (pw.equals(repw) == true) {//일치 할 때
                    setpwRequest(url);
                }
            }
        });


        super.onCreate(savedInstanceState);
    }

    public void setPW() {

        //비밀번호를 sharedpreference에 저장
        editor.putString("pw", pw);
        editor.commit();

        //로그인 엑티비티로 전환
        intent = new Intent(getApplicationContext(), ParentsLoginActivity.class);
        startActivity(intent);
    }


    public void setpwRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {//리스폰값 수신
                        switch (response) {
                            case "NewPWSucess"://설정 성공
                                Toast.makeText(getApplicationContext(), "비밀번호가 새로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                                setPW();
                                break;
                            case "DBError"://Error
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {//파라미터로 id 새로운 비밀번호 이메일값
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


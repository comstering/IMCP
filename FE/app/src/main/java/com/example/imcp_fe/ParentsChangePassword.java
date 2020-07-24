package com.example.imcp_fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * 비밀번호 변경
 * */

public class ParentsChangePassword extends AppCompatActivity {
    private EditText et_parents_change_pw, et_parents_change_re_pw;
    private String sNewPW, sRePw;
    private Button btn_parents_change_pw_ok;
    private Intent intent;
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/parentNewPW.jsp";
    private SharedPreferences login_preference;

    /*
     * 엑티비티 생성 시 호출
     * 사용자 인터페이스 설정
     * 버튼 이벤트 설정
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        // 비밀번호 입력 초기화
        et_parents_change_pw = (EditText) findViewById(R.id.et_changepassword_pw);
        et_parents_change_re_pw = (EditText) findViewById(R.id.et_changepassword_repw);
        btn_parents_change_pw_ok = (Button) findViewById(R.id.btn_childinfo_save);
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);

        //비밀번호 일치 확인
        btn_parents_change_pw_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sNewPW = et_parents_change_pw.getText().toString();
                sRePw = et_parents_change_re_pw.getText().toString();
                if (sNewPW.equals(sRePw)) {

                    // 비밀번호 일치
                    Toast.makeText(getApplicationContext(), "비밀번호 맞음", Toast.LENGTH_LONG).show();
                    NewPWRequest(url);
                } else {
                    // 비밀번호 불일치
                    Toast.makeText(getApplicationContext(), "비밀번호 틀림", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    /*
     * volley 호출
     * 비밀번호 변경 적용
     * ID 이메일 새로운 패스워드 파라미터로 전송
     * */
    public void NewPWRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response.trim()) {
                            case "NewPWSuccess":
                                Toast.makeText(getApplicationContext(), "변경 성공", Toast.LENGTH_SHORT).show();

                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("pInfo", response);
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
                params.put("ID", login_preference.getString("id", ""));
                params.put("newPassword", sNewPW);
                params.put("email", login_preference.getString("email", ""));
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
}

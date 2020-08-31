package com.example.imcp_fe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.Network.AppHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

/*
 * 부모 로그인
 * */
public class ParentsLoginActivity extends AppCompatActivity {

    private String id;
    private String pw;
    private Intent intent;
    private SharedPreferences login_preference;

    //버튼
    private Button btn_parents_login, btn_parents_signup, btn_parents_findid, btn_parents_findpw;

    //서버url
    private String loginurl = "http://tomcat.comstering.synology.me/IMCP_Server/parentLogin.jsp";
    private String firebaseurl = "http://tomcat.comstering.synology.me/IMCP_Server/setFCMToken.jsp";

    //edittext 변수
    private EditText sign_id, sign_pw;
    private long backKeyPressedTime = 0;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        //sharedpreferencedml id  pw 가 null이 아닐경우 자동 로그인
        if (!login_preference.getString("id", "null").equals("null") && !login_preference.getString("pw", "null").equals("null")) {
            id = login_preference.getString("id", "");
            pw = login_preference.getString("pw", "");
            LoginRequest(loginurl);
        }

        setContentView(R.layout.parents_login);

        //인스턴스 설정
        sign_id = findViewById(R.id.et_parents_id);
        sign_pw = findViewById(R.id.et_parents_pw);
        btn_parents_login = (Button) findViewById(R.id.btn_parents_login);
        btn_parents_signup = (Button) findViewById(R.id.btn_parents_signup);
        btn_parents_findid = (Button) findViewById(R.id.btn_parents_findid);
        btn_parents_findpw = (Button) findViewById(R.id.btn_parents_findpw);


        //부모 로그인
        btn_parents_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = sign_id.getText().toString();
                pw = sign_pw.getText().toString();

                if (TextUtils.isEmpty(String.valueOf(id))) {//id 공란
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(String.valueOf(pw))) {//pw 공란
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(String.valueOf(pw)) && !TextUtils.isEmpty(String.valueOf(id))) {

                    //토큰 전달
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.d("[FCM Service]", "getInstanceId failed", task.getException());
                                        return;
                                    }
                                    String token = task.getResult().getToken();
                                    FirebaseRequest(firebaseurl, token);
                                    Log.d("[FCM Service]", "Token: " + token);
                                }
                            });


                    LoginRequest(loginurl);


                }


            }
        });

        // 부모 회원가입
        btn_parents_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), ParentsSignup.class);
                startActivity(intent);

            }
        });

        // 부모 아이디 찾기
        btn_parents_findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), ParentsFindId.class);
                startActivity(intent);
            }
        });

        // 부모 비밀번호 찾기
        btn_parents_findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), ParentsFindPw.class);
                startActivity(intent);
            }
        });
    }

    // 뒤로가기 버튼 2회 입력 시 종료
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            toast.cancel();

        }
    }

    /* 아이디 패스워드 sharedpreference로 저장
     * 부모 메인 화면으로 전환
     * */
    public void LoginPass() {
        final SharedPreferences.Editor editor = login_preference.edit();
        editor.putString("id", String.valueOf(id));
        editor.putString("pw", String.valueOf(pw));
        editor.commit();
        intent = new Intent(getApplicationContext(), Parents_main.class);
        startActivity(intent);
    }


    public void LoginRequest(String url) {//로그인

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        switch (response) {


                            case "LoginSuccess":
                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                LoginPass();
                                break;
                            case "LoginFail":
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                break;
                            case "NoID":
                                Toast.makeText(getApplicationContext(), "등록된 아이디가 아닙니다. ", Toast.LENGTH_SHORT).show();

                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("volley", response);
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
                params.put("ID", id.toString());
                params.put("password", pw.toString());
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }


    public void FirebaseRequest(String url, final String token) {//토큰전달

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {


                            case "SetFCMSuccess":
                                Log.e("firebase", response);
                                break;
                            case "DBError":
                                Log.e("firebase", response);
                                break;
                            default:
                                Log.e("volley", response);
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
                params.put("ID", id.toString());
                params.put("token", token);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }

}

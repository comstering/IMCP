package com.example.imcp_fe;

import android.app.Activity;
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
아이 비밀번호 초기 설정
* */
public class Keypassword extends AppCompatActivity {

    //인스턴스 저장 변수
    private EditText et_children_new_pw, et_children_re_pw;


    private String sNewPW, sRePW;
    private String key;

    //버튼 변수
    private Button btn_children_create_pw;

    private Intent intent;

    //서버 url
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/childRegister.jsp";
    private SharedPreferences login_preference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //키가 있을 경우 엑티비티 전환
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        if (!login_preference.getString("key", "null").equals("null")) {
            intent = new Intent(getApplicationContext(), Child_main.class);
            startActivity(intent);
        }

        setContentView(R.layout.key_password);

        //인스턴스 저장
        et_children_new_pw = (EditText) findViewById(R.id.et_children_new_pw);
        et_children_re_pw = (EditText) findViewById(R.id.et_children_re_pw);
        btn_children_create_pw = (Button) findViewById(R.id.btn_children_create_pw);





//비밀번호 일치 확인, 일치 시 랜덤키 생성
        btn_children_create_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sNewPW = et_children_new_pw.getText().toString();
                sRePW = et_children_re_pw.getText().toString();

                if (sNewPW.equals(sRePW) == true) {
                    // 비밀번호 일치
                    Toast.makeText(getApplicationContext(), "비밀번호 맞음", Toast.LENGTH_LONG).show();
                    getRamdomPassword();
                    Log.e("key", "key : " + key);
                    // 고유키 발급 창

                } else if (sNewPW.equals(sRePW) == false) {
                    // 비밀번호 불일치
                    Toast.makeText(getApplicationContext(), "비밀번호 틀림", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //랜덤 키 생성
    public void getRamdomPassword() {//랜덤키 생성

        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                +'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
                + '!', '@', '#', '$', '%', '&', '*'};
        int idx = 0;
        StringBuffer sb = new StringBuffer();
        System.out.println("charSet.length :::: " + charSet.length);
        for (int i = 0; i < 6; i++) {
            idx = (int) (charSet.length * Math.random()); // 36 * 생성된 난수를 Int로 추출 (소숫점제거)
            sb.append(charSet[idx]);
        }
        key = sb.toString();
        setkeyRequest(url);
    }

    // 고유키 확인 엑티비티로 전환
    public void Success() {
        intent = new Intent(getApplicationContext(), PrimaryKey.class);
        startActivity(intent);
    }

    public void setkeyRequest(String url) {

        final SharedPreferences.Editor editor = login_preference.edit();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        switch (response) {
                            case "ChildRegisterSuccess":
                                Toast.makeText(getApplicationContext(), "비밀번호가 새로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                                editor.putString("key", key);//비밀번호를 SHaredpreference에 저장
                                editor.commit();
                                Success();
                                break;
                            case "SamePrivateKey":
                                Toast.makeText(getApplicationContext(), "중복된 키입니다.", Toast.LENGTH_SHORT).show();
                                getRamdomPassword();
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
                params.put("childKey", key);
                params.put("password", sNewPW);
                return params;
            }
        };


        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }


}
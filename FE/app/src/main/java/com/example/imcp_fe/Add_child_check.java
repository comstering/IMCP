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
 *  아이 추가전 고유키와 비밀번호 확인
 * */
public class Add_child_check extends AppCompatActivity {

    //입력 받을 키값, 비밀번호
    private EditText et_addchildcheck_key;
    private EditText et_addchildcheck_password;

    //등록 버튼
    private Button btn_addchildcheck_add;

    //edittext에서 받은 값 저장
    private String key;
    private String password;

    //SharedPreferences 변수
    private SharedPreferences login_preference;

    //전송한 서버 주소
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/addChildCheck.jsp";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child_check);

        //SharedPreferences 인스턴스
        login_preference = getSharedPreferences("Login", Activity.MODE_PRIVATE);

        //각 변수의 인스턴스를 받아옴
        et_addchildcheck_key = findViewById(R.id.et_addchildcheck_key);
        et_addchildcheck_password = findViewById(R.id.et_addchildcheck_password);
        btn_addchildcheck_add = findViewById(R.id.btn_addchildcheck_add);

        //버튼 이벤트
        btn_addchildcheck_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edittext 내용을 key, password 변수에 저장
                key = et_addchildcheck_key.getText().toString();
                password = et_addchildcheck_password.getText().toString();


                if (TextUtils.isEmpty(String.valueOf(key))) { //edittext가 key가 공백일 경우
                    Toast.makeText(getApplicationContext(), "키값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(String.valueOf(password))) {  //edittext가 key가 공백일 경우
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(String.valueOf(key)) && !TextUtils.isEmpty(String.valueOf(password))) { //공백이 아닐경우
                    childcheckRequest(url);
                }
            }
        });
    }


    public void addchild() {

        //Add_child intent 설정 후 값 전달
        Intent intent = new Intent(getApplicationContext(), Add_child.class);
        intent.putExtra("key", key);
        intent.putExtra("password", password);

        //엑티비티 전환
        startActivity(intent);
    }

    public void childcheckRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {//리스폰 값 처리

                        switch (response) {

                            case "AlreadyInfo"://해당 키가 있을 경우
                                Toast.makeText(getApplicationContext(), "이미 있는 키입니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case "NoInfo"://키가 없을 경우
                                Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                addchild();//키, 비밀번호 전달 메소드
                                break;
                            case "DBError"://서버 에러
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "PtoCError"://
                                Toast.makeText(getApplicationContext(), "아이 연결실패", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("volley", "response12:" + response);
                                break;
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", "error : " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //서버로 보낼 파라미터 값
                params.put("ID", login_preference.getString("id", ""));
                params.put("childKey", key);
                params.put("password", password);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }

}

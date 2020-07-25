package com.example.imcp_fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.Network.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 * 부모 계정 마이페이지
 * */
public class Parent_info extends AppCompatActivity {
    private TextView tv_parentinfo_id;
    private TextView tv_parentinfo_name;
    private TextView tv_parentinfo_phone;
    private TextView tv_parentinfo_email;
    private Button btn_parentinfo_logout;
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/getParentInfo.jsp";
    private SharedPreferences login_preference;


    /*
     * 엑티비티 생성 시 호출
     * 사용자 인터페이스 설정
     * volley 호출
     * */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_info);
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        tv_parentinfo_id = findViewById(R.id.tv_parentinfo_id);
        tv_parentinfo_name = findViewById(R.id.tv_parentinfo_name);
        tv_parentinfo_phone = findViewById(R.id.tv_parentinfo_phone);
        tv_parentinfo_email = findViewById(R.id.tv_parentinfo_email);
        btn_parentinfo_logout = findViewById(R.id.btn_parentinfo_logout);

        btn_parentinfo_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences.Editor editor = login_preference.edit();
                editor.putString("id", "null");
                editor.putString("pw", "null");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), ParentsLoginActivity.class);
                startActivity(intent);
            }
        });


        mypageRequest(url);
    }

    /*
     * volley 호출
     * 마이페이지 정보 받기
     * ID를 파라미터로 전달
     * */
    public void mypageRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.equals(null)) {
                                JSONObject row = new JSONObject(response);

                                tv_parentinfo_id.setText(row.getString("id"));
                                tv_parentinfo_name.setText(row.getString("name"));
                                tv_parentinfo_phone.setText(row.getString("phone"));
                                tv_parentinfo_email.setText(row.getString("email"));


                            } else {
                                Log.e("pInfo", response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("pInfo", "Error ; " + error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", login_preference.getString("id", ""));
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);

    }
}

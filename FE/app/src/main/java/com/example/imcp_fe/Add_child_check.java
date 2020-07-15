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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Add_child_check extends AppCompatActivity {


    private EditText et_addchildcheck_key;
    private EditText et_addchildcheck_password;
    private Button btn_addchildcheck_add;
    private String key;
    private String password;
    private Intent intent;
    private SharedPreferences login_preference;
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/addChildCheck.jsp";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child_check);
        login_preference = getSharedPreferences("Login", Activity.MODE_PRIVATE);

        et_addchildcheck_key = findViewById(R.id.et_addchildcheck_key);
        et_addchildcheck_password = findViewById(R.id.et_addchildcheck_password);
        btn_addchildcheck_add = findViewById(R.id.btn_addchildcheck_add);


        btn_addchildcheck_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = et_addchildcheck_key.getText().toString();
                password = et_addchildcheck_password.getText().toString();
                if (TextUtils.isEmpty(String.valueOf(key))) {
                    Toast.makeText(getApplicationContext(), "키값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(String.valueOf(password))) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(String.valueOf(key)) && !TextUtils.isEmpty(String.valueOf(password))) {
                    childcheckRequest(url);
                }
            }
        });
    }

    public void addchild() {
        intent = new Intent(getApplicationContext(), Add_child.class);
        startActivity(intent);
    }


    public void childcheckRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("volley", "llll: " + response);
//                        if(response.equals("AlreadyInfo")){
//                            Log.e("volley", response);
//                        }else if(response.equals("NoInfo")){
//                            Log.e("volley", response);
//                        }else if(response.equals("DBError")) {
//                            Log.e("volley", response);
//                        }else{
//                            Log.e("volley","dasd : "+ response);
//                        }
                        switch (response) {

                            case "AlreadyInfo":
                                Toast.makeText(getApplicationContext(),"이미 있는 키입니다.",Toast.LENGTH_SHORT).show();
                                break;
                            case "NoInfo":
                                Toast.makeText(getApplicationContext(),"정보가 없습니다.",Toast.LENGTH_SHORT).show();
                                addchild();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                break;
                            case "PtoCError":
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

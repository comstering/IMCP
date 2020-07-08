package com.example.imcp_fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class Child_main extends AppCompatActivity {

    private EditText et_childmain_password;
    private ImageButton ib_childmain_sos;
    private SharedPreferences login_preference;
    private String password;
    private Intent intent;
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/childLogin.jsp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_main);
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);

        et_childmain_password = findViewById(R.id.et_childmain_password);
        ib_childmain_sos = findViewById(R.id.ib_childmain_sos);

        ib_childmain_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = et_childmain_password.getText().toString();
                childRequest(url);

            }
        });


    }

    public void childRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "ChildLoginSuccess":
                                Toast.makeText(getApplicationContext(), "로그인 성공",Toast.LENGTH_SHORT).show();
                                Success();
                                break;
                            case "ChildLoginFail":
                                Toast.makeText(getApplicationContext(), "로그인 실패",Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("child", response);
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
                params.put("childKey", login_preference.getString("key", ""));
                params.put("password",password);
                return params;
            }
        };


        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
    public void Success(){
        intent = new Intent(getApplicationContext(), PrimaryKey.class);
        startActivity(intent);
    }

}

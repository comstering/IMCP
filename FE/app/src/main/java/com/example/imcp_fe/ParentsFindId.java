package com.example.imcp_fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.imcp_fe.Network.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 * 아이디 찾기
 * */
public class ParentsFindId extends AppCompatActivity {

    //edittext 변수
    private EditText et_findid_name;
    private EditText et_findid_email;
    private Button btn_findid_ok;


    private String name;
    private String email;
    private Intent intent;

    //서버 url
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/parentFindID.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        //인스턴스 저장
        et_findid_name = findViewById(R.id.et_parents_findid_name);
        et_findid_email = findViewById(R.id.et_parents_findid_email);
        btn_findid_ok = findViewById(R.id.btn_parents_findid_ok);

        //edittext값 저장
        name = et_findid_name.getText().toString();
        email = et_findid_email.getText().toString();


        btn_findid_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findidRequest(url);
            }
        });
    }

    public void findID(String ID) {//아이디 찾기

        intent = new Intent(getApplicationContext(), Get_id.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }


    public void findidRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "NoID":
                                Toast.makeText(getApplicationContext(), "등록된 아이디가 아닙니다. ", Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                findID(response);
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
                params.put("Name", name);
                params.put("Email", email);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }



}

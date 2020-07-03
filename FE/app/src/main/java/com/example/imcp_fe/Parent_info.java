package com.example.imcp_fe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
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

public class Parent_info extends AppCompatActivity {
    private TextView tv_parentinfo_id;
    private TextView tv_parentinfo_name;
    private TextView tv_parentinfo_phone;
    private TextView tv_parentinfo_email;
    private Button btn_parentinfo_logout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_info);
        tv_parentinfo_id = findViewById(R.id.tv_parentinfo_id);
        tv_parentinfo_name = findViewById(R.id.tv_parentinfo_name);
        tv_parentinfo_phone = findViewById(R.id.tv_parentinfo_phone);
        tv_parentinfo_email = findViewById(R.id.tv_parentinfo_email);
        btn_parentinfo_logout = findViewById(R.id.btn_parentinfo_logout);


        //         mypageRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void mypageRequest() {
        String url = "https://www.google.co.kr";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jarray = new JSONArray(response);
                            int size = jarray.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject row = jarray.getJSONObject(i);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}

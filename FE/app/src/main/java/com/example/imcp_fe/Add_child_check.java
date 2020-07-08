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

public class Add_child_check extends AppCompatActivity {


    private EditText et_addchildcheck_key;
    private EditText et_addchildcheck_password;
    private Button btn_addchildcheck_add;
    private String key;
    private String password;
    private Intent intent;
    private SharedPreferences login_preference;
    private  String url = "http://tomcat.comstering.synology.me/IMCP_Server/addChildCheck.jsp";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child_check);
        login_preference = getSharedPreferences("Login", Activity.MODE_PRIVATE);

        et_addchildcheck_key = findViewById(R.id.et_addchildcheck_key);
        et_addchildcheck_password = findViewById(R.id.et_addchildcheck_password);
        btn_addchildcheck_add=findViewById(R.id.btn_addchildcheck_add);


        btn_addchildcheck_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key=et_addchildcheck_key.getText().toString();
                password = et_addchildcheck_password.getText().toString();

                if(key.equals(null)== true){
                    Toast.makeText(getApplicationContext(),"키값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(null)==true){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(key.equals(null)==false && password.equals(null)==false){
                    childcheckRequest(url);
                }


            }
        });
    }


    public void childcheckRequest(String url) {
        Log.d("Test", "4");
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        switch (response){
                            case "AlreadyInfo":
                                Log.e("volley", "response : "+response);
                                addchild();
                                break;
                            case "NoInfo":
                                Log.e("volley","response : "+ response);
                                break;
                            case "DBError":
                                Log.e("volley","response : "+ response);
                                break;
                            default:
                                Log.e("volley", "response : "+response);
                                break;
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", "error : "+error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", login_preference.getString("id",""));
                Log.e("volley", login_preference.getString("id",""));
                params.put("chidKey", key);
                params.put("paswwrod", password);

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
    public void addchild(){
        intent = new Intent(getApplicationContext(), Add_child.class);
        startActivity(intent);
    }

}

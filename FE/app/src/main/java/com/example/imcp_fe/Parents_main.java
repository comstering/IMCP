package com.example.imcp_fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.Network.AppHelper;
import com.example.imcp_fe.Adapter.rv_mychildren_adapter;
import com.example.imcp_fe.Data.rv_mychildren_data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.widget.Toast;

public class Parents_main extends AppCompatActivity {

    private RecyclerView rv_mychildren;
    private LinearLayoutManager layoutManager = null;
    private rv_mychildren_adapter rvMychildrenAdapter = null;
    private ArrayList<rv_mychildren_data> arrayList;
    private rv_mychildren_data rvMychildrenData;
    private Button btn_main_addchild;
    private Button btn_main_missingclist;
    private ImageButton iv_mypage;
    private Intent intent;
    public Bitmap test =null;
    private ArrayList<String> keyvalue;
    private ArrayList<String>birthvalue;
    private SharedPreferences login;
   // private  final SharedPreferences.Editor editor = login.edit();
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/getChildList.jsp";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn_main_missingclist = findViewById(R.id.btn_main_missinglist);
        btn_main_addchild = findViewById(R.id.btn_main_addchild);
        iv_mypage = findViewById(R.id.iv_mypage);
        rv_mychildren = findViewById(R.id.rv_mychildren);
        test = BitmapFactory.decodeResource(getResources(),R.drawable.children);

        btn_main_addchild.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), Add_child_check.class);
                startActivity(intent);
            }
        });

       iv_mypage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               intent = new Intent(view.getContext(), Parent_info.class);
               startActivity(intent);
           }
       });
       btn_main_missingclist.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               intent = new Intent(view.getContext(),Missing_children.class);
               startActivity(intent);
           }
       });
/*
        rvMychildrenData = new rv_mychildren_data(); //  리사이클러뷰 테스트용
        arrayList = new ArrayList<rv_mychildren_data>();
        rv_mychildren = (RecyclerView)findViewById(R.id.rv_mychildren);


        layoutManager = new LinearLayoutManager(this);
        rv_mychildren = findViewById(R.id.rv_mychildren);
        rv_mychildren.setHasFixedSize(true);//일정한 크기의 아이템뷰를 만들어줌
        rv_mychildren.setLayoutManager(layoutManager);//LinearLayout으로 리사이클러뷰 모양을 만듬.

        rvMychildrenData.setRv_mychild_image(test);
        rvMychildrenData.setRv_mychild_name("이민규");
        arrayList.add(rvMychildrenData);
        rvMychildrenAdapter = new rv_mychildren_adapter(this,arrayList);
        rv_mychildren.setAdapter(rvMychildrenAdapter);
*/
       childlistRequest(url);

    }



    //아이 리스트를 요청
    public void childlistRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           if(response.equals(null)==false ) {
                               arrayList = new ArrayList<rv_mychildren_data>();
                               JSONArray jarray = new JSONArray(response);
                               int size = jarray.length();
                               for (int i = 0; i < size; i++) {
                                   JSONObject row = jarray.getJSONObject(i);
                                   rvMychildrenData = new rv_mychildren_data();
                                   rvMychildrenData.setRv_mychild_image(row.getString("image"));
                                   rvMychildrenData.setRv_mychild_name(row.getString("name"));
                                   rvMychildrenData.setkey(row.getString("key"));
                                   rvMychildrenData.setbirth(row.getString("birth"));

                                   arrayList.add(rvMychildrenData);
                               }
                               rvMychildrenAdapter = new rv_mychildren_adapter(Parents_main.this, arrayList);
                               rv_mychildren.setAdapter(rvMychildrenAdapter);//리사이클러뷰에 어댑터 연결
                           }else if(response.equals(null)==true){
                               Toast.makeText(getApplicationContext(), "null..",Toast.LENGTH_SHORT).show();
                           } } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", "main error : "+error);
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
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);

    }


}

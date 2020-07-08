package com.example.imcp_fe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.imcp_fe.Adapter.rv_missingchild_adapter;
import com.example.imcp_fe.Data.rv_missingchild_data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Missing_children extends AppCompatActivity {
    private RecyclerView rv_missingchildren = null;
    private LinearLayoutManager layoutManager = null;
    private rv_missingchild_adapter rvMissingchildrenAdapter = null;
    private ArrayList<rv_missingchild_data> arrayList;
    private rv_missingchild_data rvMissingchildrenData;
    private ImageButton ib_missingchildren_back;
    public Bitmap test =null;
    private String url ="http://tomcat.comstering.synology.me/IMCP_Server/getMissingList.jsp";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_children);

        ib_missingchildren_back = (ImageButton) findViewById(R.id.btn_missingchilren_back);
        rv_missingchildren = (RecyclerView) findViewById(R.id.rv_missingchildren);
        test = BitmapFactory.decodeResource(getResources(),R.drawable.children);

        ib_missingchildren_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*rvMissingchildrenData = new rv_missingchild_data(); //  리사이클러뷰 테스트용
        arrayList = new ArrayList<rv_missingchild_data>();


        layoutManager = new LinearLayoutManager(this);
        rv_missingchildren = findViewById(R.id.rv_missingchildren);
        rv_missingchildren.setHasFixedSize(true);//일정한 크기의 아이템뷰를 만들어줌
        rv_missingchildren.setLayoutManager(layoutManager);//LinearLayout으로 리사이클러뷰 모양을 만듬.

        rvMissingchildrenData.setRv_missingchild_image(test);
        rvMissingchildrenData.setRv_missingchild_name("이민규");
        arrayList.add(rvMissingchildrenData);
        rvMissingchildrenAdapter = new rv_missingchild_adapter(this,arrayList);
        rv_missingchildren.setAdapter(rvMissingchildrenAdapter);
*/
        missingRequest(url);
    }



    public void missingRequest(String url) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                          if(response.equals(null)){
                            arrayList = new ArrayList<rv_missingchild_data>();
                            JSONArray jarray = new JSONArray(response);
                            int size = jarray.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject row = jarray.getJSONObject(i);
                                rvMissingchildrenData = new rv_missingchild_data();
                                rvMissingchildrenData.setRv_missingchild_key(row.getString("key"));// 이름 받아서 데이터로 저장
                                rvMissingchildrenData.setRv_missingchild_name(row.getString("name"));// 이름 받아서 데이터로 저장
                                rvMissingchildrenData.setRv_missingchild_birth(row.getString("birth"));// 이름 받아서 데이터로 저장
                                rvMissingchildrenData.setRv_missingchild_image(row.getString("image"));// 이름 받아서 데이터로 저장
                                rvMissingchildrenData.setRv_missingchild_Phone(row.getString("parentPhone"));
                                arrayList.add(rvMissingchildrenData);
                            }
                            rvMissingchildrenAdapter = new rv_missingchild_adapter(Missing_children.this, arrayList);
                            rv_missingchildren.setAdapter(rvMissingchildrenAdapter);//리사이클러뷰에 어댑터 연결
                             }else if(response.equals(null)==true){
                              Log.e("missing", response);
                          }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("missing", "error : "+ error);
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
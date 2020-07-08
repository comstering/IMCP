package com.example.imcp_fe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.Network.AppHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Child extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton ib_child_back = null;
    private ImageButton ib_child_info = null;
    private CircleImageView iv_child_photo;
    private TextView tv_child_name;
    private TextView tv_child_primarykey;
    private GoogleMap mMap;
    private Double x;
    private Double y;
    private  String url = "http://tomcat.comstering.synology.me/IMCP_Server/getChildGPS.jsp";
    private String key;
    private String name;
    private String image;
    private String birth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);

        Intent intent =getIntent();
        key = intent.getStringExtra("key");
        name = intent.getStringExtra("name");
        image =intent.getStringExtra("image");
        birth =intent.getStringExtra("birth");

        intent = new Intent(getApplicationContext(), Child_info.class);
        intent.putExtra("key",key);
        intent.putExtra("image", image);
        intent.putExtra("name", name);
        intent.putExtra("birth", birth);


        iv_child_photo =findViewById(R.id.iv_child_photo);
        tv_child_name =findViewById(R.id.tv_child_name);
        tv_child_primarykey = findViewById(R.id.tv_child_primarykey);

        ib_child_back = (ImageButton) findViewById(R.id.ib_child_back);
        ib_child_info = (ImageButton) findViewById(R.id.ib_child_info);
        ib_child_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Picasso.with(getApplicationContext()).load(image).into(iv_child_photo);
        tv_child_name.setText(name);
        tv_child_primarykey.setText(key);


        loactionRequest();
       }

    @Override
    protected void onStart() {
        super.onStart();

        ib_child_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Child_info.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mv_child);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {


        mMap = googleMap;
        LatLng loaction = new LatLng(x, y);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(loaction);
        markerOptions.title("현재 위치");



        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loaction));//카메라의 위도 경도를 설정, loaction으로 서버에서 위치를 받아온다.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));//카메라 확대 기능, 숫자가 높을수록 가까워짐 1단계일 경우 세계지도수준
    }






    public void loactionRequest() {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.equals(null)==false) {
                                JSONArray jarray = new JSONArray(response);
                                int size = jarray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject row = jarray.getJSONObject(i);
                                    x = row.getDouble("x"); // x, y 좌표를 받아옴.
                                    y = row.getDouble("y");
                                }
                            }else if(response.equals(null)==true){
                                Log.e("volley", response);
                                Toast.makeText(Child.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("childKey", key);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
}



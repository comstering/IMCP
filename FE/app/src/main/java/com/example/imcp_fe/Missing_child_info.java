package com.example.imcp_fe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.imcp_fe.R;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/*
실종아이 정보 확인
* */
public class Missing_child_info extends AppCompatActivity implements OnMapReadyCallback {


    public double x = 0.0;
    public double y = 0.0;
    private GoogleMap mMap;
    private ImageButton btn_missingchild_info_back;
    private CircleImageView iv_missingchild_info_photo;
    private TextView tv_missingchild_info_name;
    private TextView tv_missingchild_info_age;
    private TextView tv_missingchild_info_phone;

    private String key;
    private String image;
    private String name;
    private String birth;
    private String phone;
    private Intent intent;
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/getChildGPS.jsp";

    /*
    엑티비티 생성 시 호출
    사용자 인터페이스 설정
    volley 호출
     * */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_child_info);
        intent = getIntent();
        iv_missingchild_info_photo = findViewById(R.id.iv_missingchild_info_photo);
        tv_missingchild_info_name = findViewById(R.id.tv_missingchild_info_name);
        tv_missingchild_info_age = findViewById(R.id.tv_missingchild_info_age);
        tv_missingchild_info_phone = findViewById(R.id.tv_missingchild_info_phone);

        btn_missingchild_info_back = (ImageButton) findViewById(R.id.btn_missingchild_info_back);
        key = intent.getStringExtra("key");
        image = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        birth = intent.getStringExtra("birth");
        phone = intent.getStringExtra("phone");

        Picasso.with(getApplicationContext()).load("http://tomcat.comstering.synology.me/IMCP_Server/upload/" + image).into(iv_missingchild_info_photo);
        tv_missingchild_info_name.setText(name);
        tv_missingchild_info_phone.setText(phone);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formats;
        formats = new SimpleDateFormat("yyyy");

        Log.e("missinginfo", (formats.format(cal.getTime())));
        int time2 = Integer.parseInt(formats.format(cal.getTime()));
        Log.e("missinginfo", birth);
        int ageSum = Integer.parseInt(birth.substring(0, 4));

        tv_missingchild_info_age.setText(Integer.toString(time2 - ageSum + 1));


        loactionRequest(url);

    }

    /*
     * onCreate 종료 후 호출
     * 버튼 이벤트 설정
     * 구글 맵 설정
     * */
    @Override
    protected void onStart() {
        super.onStart();
        //뒤로가기 버튼으로 전 엑티비티로 전환
        btn_missingchild_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mv_missingchild);
        mapFragment.getMapAsync(this);

    }

    //구글맵 설정
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
    }

    //구글맵 초기화
    public void drawmap(double x, double y) {
        LatLng newlatlng = new LatLng(x, y);

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(newlatlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlatlng, 15));
    }


    public void loactionRequest(String url) {


        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.equals(null) == false) {

                                JSONObject row = new JSONObject(response);
                                x = row.getDouble("lati"); // x, y 좌표를 받아옴.
                                y = row.getDouble("longi");
                                drawmap(x, y);
                            } else if (response.equals(null) == true) {
                                Toast.makeText(Missing_child_info.this, "Error", Toast.LENGTH_SHORT).show();
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

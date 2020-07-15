package com.example.imcp_fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    private Button btn_child_sos;
    private CircleImageView iv_child_photo;
    private TextView tv_child_name;
    private TextView tv_child_primarykey;
    private GoogleMap mMap;
    private Double x = 0.0;
    private Double y = 0.0;
    private String locationurl = "http://tomcat.comstering.synology.me/IMCP_Server/getChildGPS.jsp";
    private String sosurl = "http://tomcat.comstering.synology.me/IMCP_Server/childMissing.jsp";
    private String key;
    private String name;
    private String image;
    private String birth;
    private String onoff = "off";
    private SharedPreferences login_preference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        birth = intent.getStringExtra("birth");



        iv_child_photo = findViewById(R.id.iv_child_photo);
        tv_child_name = findViewById(R.id.tv_child_name);
        tv_child_primarykey = findViewById(R.id.tv_child_primarykey);

        btn_child_sos = findViewById(R.id.btn_child_sos);
        ib_child_back = (ImageButton) findViewById(R.id.ib_child_back);
        ib_child_info = (ImageButton) findViewById(R.id.ib_child_info);

        Picasso.with(getApplicationContext()).load("http://tomcat.comstering.synology.me/IMCP_Server/upload/"+image).into(iv_child_photo);
        tv_child_name.setText(name);
        tv_child_primarykey.setText(key);


        locationRequest(locationurl);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationRequest(locationurl);
        ib_child_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Child_info.class);
                intent.putExtra("key", key);
                intent.putExtra("image", image);
                intent.putExtra("name", name);
                intent.putExtra("birth", birth);

                startActivity(intent);
            }
        });
        ib_child_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_child_sos.setOnClickListener(new View.OnClickListener() {
            final SharedPreferences.Editor editor = login_preference.edit();

            @Override
            public void onClick(View view) {
                if (onoff.equals("off")) {
                    onoff = "on";
                    editor.putString("onoff", String.valueOf(onoff));
                    editor.commit();
                    SOSRequest(sosurl);
                } else if(onoff.equals("on")) {
                    onoff ="off";
                    editor.putString("onoff", String.valueOf(onoff));
                    editor.commit();
                    SOSRequest(sosurl);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mv_child);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void drawmap(double x, double y) {
        LatLng newlatlng = new LatLng(x, y);

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(newlatlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlatlng, 15));
    }


    public void locationRequest(String url) {
        Log.e("map", "2");
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("map", "위도 경도 : " + response);
                        try {
                            if (response.equals(null) == false) {

                                JSONObject row = new JSONObject(response);
                                x = row.getDouble("lati"); // x, y 좌표를 받아옴.
                                y = row.getDouble("longi");
                                drawmap(x, y);
                            } else if (response.equals(null) == true) {
                                Log.e("volley", response);
                                Toast.makeText(Child.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("map", Double.toString(x));
                            Log.e("map", "3");
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


    public void SOSRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "MissingSetSuccess":
                                Toast.makeText(getApplicationContext(),"SOS 요청됨", Toast.LENGTH_SHORT).show();
                                break;
                            case "NoChildInfo":
                                Toast.makeText(getApplicationContext(),"정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
                                break;
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
                Log.e("missing", login_preference.getString("onoff",""));
                if (onoff.equals("on")) {
                    params.put("missing", "missed");
                }else if(onoff.equals("off")){
                    params.put("missing", "finded");
                }
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
}



package com.example.imcp_fe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Child extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton ib_child_back = null;
    private ImageButton ib_child_info = null;
    private GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);

        ib_child_back = (ImageButton) findViewById(R.id.ib_child_back);
        ib_child_info = (ImageButton) findViewById(R.id.ib_child_info);
        ib_child_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        loactionRequest();
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
        LatLng loaction = new LatLng(37.566, 126.978);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(loaction);
        markerOptions.title("현재 위치");



        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loaction));//카메라의 위도 경도를 설정, loaction으로 서버에서 위치를 받아온다.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));//카메라 확대 기능, 숫자가 높을수록 가까워짐 1단계일 경우 세계지도수준
    }
}





    /*public void loactionRequest() {
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
                                x= row.getDouble("x"); // x, y 좌표를 받아옴.
                                y = row.getDouble("y");
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
*/


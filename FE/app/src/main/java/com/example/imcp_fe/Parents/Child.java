package com.example.imcp_fe.Parents;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.imcp_fe.Network.AppHelper;
import com.example.imcp_fe.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Child extends AppCompatActivity implements OnMapReadyCallback {

    public double x=0.0;
    public double y=0.0;
    private  GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        LatLng loaction = new LatLng(x, y);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(loaction);
        markerOptions.title("현재 위치");
        loactionRequest();

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loaction));//카메라의 위도 경도를 설정, loaction으로 서버에서 위치를 받아온다.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));//카메라 확대 기능, 숫자가 높을수록 가까워짐 1단계일 경우 세계지도수준
    }


    public void loactionRequest() {
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

}
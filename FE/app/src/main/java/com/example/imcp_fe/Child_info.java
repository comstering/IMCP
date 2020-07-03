package com.example.imcp_fe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.imcp_fe.Network.AppHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Child_info extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

   private GoogleMap mMap;
   private ArrayList<MarkerOptions> markerlist = new ArrayList<>();
   private Button btn_childinfo_save;
   private String sendlocation=null;
   private CircleImageView iv_childinfo_photo;
   private EditText et_childinfo_name;
   private EditText et_childinfo_brithday;

   protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chlid_info);
        btn_childinfo_save = findViewById(R.id.btn_childinfo_save);
        iv_childinfo_photo = findViewById(R.id.iv_child_photo);
        et_childinfo_name = findViewById(R.id.et_childinfo_name);
        et_childinfo_brithday = findViewById(R.id.et_childinfo_brithday);

//        loactionRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final StringBuffer sendlocation=new StringBuffer("[");
        btn_childinfo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//지정한 마커들의 좌표를 파라미터로 전송?
                if(markerlist.isEmpty() ==false){
                    for(int i=0; i<markerlist.size();i++){
                        sendlocation.append("{"+"\""+"lati"+"\""+":"+"\""+Double.toString(markerlist.get(i).getPosition().latitude)+"\""+","+"\""+"longi"+"\""+":"+"\""+Double.toString(markerlist.get(i).getPosition().longitude)+"\""+"},");
                        Log.e("output" ,markerlist.get(i).getPosition().toString());
                    }
                    sendlocation.delete(sendlocation.length()-1, sendlocation.length());
                    sendlocation.append("]");
                    Log.e("output", sendlocation.toString());
                }else if(markerlist.isEmpty()==true){
                    Log.e("output", "리스트 빔");
                }else{
                    Log.e("output", "error");
                }
            }
        });

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mv_childinfo);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

    }

    @Override //구글맵을 띄울준비가 됬으면 자동호출된다.
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //지도타입 - 일반
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); mMap = googleMap;
        LatLng loaction = new LatLng(37.566, 126.978);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(loaction);
        markerOptions.title("현재 위치");


       // mMap.addMarker(markerOptions).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loaction));//카메라의 위도 경도를 설정, loaction으로 서버에서 위치를 받아온다.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));//카메라 확대 기능, 숫자가 높을수록 가까워짐 1단계일 경우 세계지도수준
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerlist.add(markerOptions);//마커들을 리스트로 저장
                // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.))
                //{"lati:"0.25", "longi":"1.24"}
//                latLng.latitude; //위도
//                latLng.longitude; //경도
                markerOptions.position(latLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);

            }
        });
        mMap.setOnMarkerClickListener(this);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(int i=0; i<markerlist.size();i++){
            if((markerlist.get(i).getPosition().toString()).equals(marker.getPosition().toString())){
                markerlist.remove(i);
                break;
            }else{
                Log.e("output", "else문");
            }
        }

        marker.remove();
        return false;
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
                             /*   x= row.getDouble("x"); // x, y 좌표를 받아옴.
                                y = row.getDouble("y");
                           */ }

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
                params.put("",sendlocation);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}





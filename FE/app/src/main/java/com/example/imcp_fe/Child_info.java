package com.example.imcp_fe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/*
아이정보 수정
* */
public class Child_info extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private final int REQ_CODE_SELECT_IMAGE = 100;
    private GoogleMap mMap;
    private ArrayList<MarkerOptions> markerlist = new ArrayList<>();
    private Button btn_childinfo_save;
    private StringBuffer sendlocation = null;
    private CircleImageView iv_childinfo_photo;
    private EditText et_childinfo_name;
    private EditText et_childinfo_brithday;
    private EditText et_childinfo_key;
    private EditText et_childinfo_pw;
    private Double x = 0.0;
    private Double y = 0.0;
    private String gpsurl = "http://tomcat.comstering.synology.me/IMCP_Server/setChildGPSInitial.jsp";
    private String infourl = "http://tomcat.comstering.synology.me/IMCP_Server/childModify.jsp";
    private String getlocationurl = "http://tomcat.comstering.synology.me/IMCP_Server/getChildInitial.jsp";
    private String img_path = null;
    private String imageName = null;
    private Bitmap image_bitmap = null;
    private Bitmap image_bitmap_copy = null;

    private String password;
    private String newkey;
    private String key;
    private String name;
    private String image;
    private String birth;
    private SharedPreferences login_preference;

    /*
    엑티비티 생성 시 호출
    사용자 인터페이스 설정
    버튼 이벤트 설정
    * */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chlid_info);
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        birth = intent.getStringExtra("birth");
        btn_childinfo_save = (Button) findViewById(R.id.btn_childinfo_save);


        iv_childinfo_photo = (CircleImageView) findViewById(R.id.iv_childinfo_photo);
        et_childinfo_name = findViewById(R.id.et_childinfo_name);
        et_childinfo_brithday = findViewById(R.id.et_childinfo_brithday);
        et_childinfo_key = findViewById(R.id.et_childinfo_key);
        et_childinfo_pw = findViewById(R.id.et_childinfo_password);

        et_childinfo_name.setText(name);
        et_childinfo_brithday.setText(birth);
        Log.e("asd", img_path + "");
        Log.e("key", key);
        et_childinfo_key.setText(key);


        Picasso.with(getApplicationContext()).load("http://tomcat.comstering.synology.me/IMCP_Server/upload/" + image).into(iv_childinfo_photo);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        //이미지 변경
        iv_childinfo_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);


            }
        });


    }

    /*
     * onCreate 종료 후 호출
     * 버튼 이벤트 설정
     * 구글맵 설정
     * */
    @Override
    protected void onStart() {
        super.onStart();
        sendlocation = new StringBuffer("[");
        //지정한 마커들의 좌표를 파라미터로 전송
        getlocationRequest(getlocationurl);

        btn_childinfo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = et_childinfo_name.getText().toString();
                birth = et_childinfo_brithday.getText().toString();
                newkey = et_childinfo_key.getText().toString();
                password = et_childinfo_pw.getText().toString();

                Log.e("TT", name + birth + key + password);


                if (!markerlist.isEmpty()) {

                    for (int i = 0; i < markerlist.size(); i++) {
                        sendlocation.append("{" + "\"" + "lati" + "\"" + ":" + "\"" + Double.toString(markerlist.get(i).getPosition().latitude) + "\"" + "," + "\"" + "longi" + "\"" + ":" + "\"" + Double.toString(markerlist.get(i).getPosition().longitude) + "\"" + "},");
                        Log.e("output", markerlist.get(i).getPosition().toString());
                    }
                    sendlocation.delete(sendlocation.length() - 1, sendlocation.length());
                    sendlocation.append("]");

                    DoFileUpload(infourl, img_path);
                    locationRequest(gpsurl);//주기적으로 업데이트가 가능해야함.

                } else if (markerlist.isEmpty()) {
                    if (sendlocation.length() >= 10) {
                        sendlocation.delete(sendlocation.length() - 1, sendlocation.length());
                        sendlocation.append("]");
                        DoFileUpload(infourl, img_path);
                        locationRequest(gpsurl);//주기적으로 업데이트가 가능해야함.
                    } else {
                        Toast.makeText(getApplicationContext(), "마커를 지정해주세요.", Toast.LENGTH_SHORT).show();
                        Log.e("output", "리스트 빔");

                    }
                } else {
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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerlist.add(markerOptions);//마커들을 리스트로 저장
                markerOptions.position(latLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);

            }
        });
        mMap.setOnMarkerClickListener(this);


    }

    public void drawmap(double x, double y) {
        LatLng newlatlng = new LatLng(x, y);


        mMap.addMarker(new MarkerOptions().position(newlatlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlatlng, 15));
    }

    //마커 클릭 이벤트 설정
    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < markerlist.size(); i++) {
            if ((markerlist.get(i).getPosition().toString()).equals(marker.getPosition().toString())) {
                markerlist.remove(i);
                break;
            } else {
                Log.e("output", "else문");
            }
        }

        marker.remove();
        return false;
    }

    /*
     * 설정한 이미지를 비트맵으로 변환, URI를 얻어 경로값을 반환
     * getImagePathToUri 메소드 이용
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //사용자 단말기의 width , height 값 반환
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                    ImageView image = (ImageView) findViewById(R.id.iv_childinfo_photo);  //이미지를 띄울 위젯 ID값
                    image.setImageBitmap(image_bitmap_copy);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }//end of onActivityResult()


    /*
     * 이미지 경로, 이름 값 설정
     * */
    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        Toast.makeText(Child_info.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()

    public void DoFileUpload(String apiUrl, String absolutePath) {
        HttpFileUpload(apiUrl, "", absolutePath);
    }

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    /*
    http 통신
    * */
    public void HttpFileUpload(String urlString, String params, String fileName) {
        try {

            Log.e("errorCheck", "1" + fileName);
            Log.e("errorCheck", "1");

            URL connectUrl = new URL(urlString);
            Log.e("errorCheck", "2");


            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            Log.e("errorCheck", "3");


            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);


            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeUTF(name);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"key\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(key + lineEnd);


            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"newkey\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(newkey + lineEnd);


            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"password\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(password + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"birth\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(birth + lineEnd);
            Log.e("errorCheck", "4");

//            dos.writeBytes(twoHyphens + boundary + lineEnd);
//            dos.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd);
//            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//            dos.writeBytes(lineEnd);
//            dos.writeBytes(login_preference.getString("id","") + lineEnd);

            if (fileName != null) {
                Log.e("errorCheck", "5");
                File file = new File(fileName);


                FileInputStream mFileInputStream = new FileInputStream(file);

                Log.e("file", file.getName());
//            if(file.getName().equals(null))
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())) + lineEnd);
                dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                dos.writeBytes(lineEnd);


                int bytesAvailable = mFileInputStream.available();
                int maxBufferSize = 1024;//?1024
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                byte[] buffer = new byte[bufferSize];
                int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);


                // read image
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = mFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                }

                // close streams
                Log.e("Test", "File is written");
                mFileInputStream.close();
            }
            Log.e("errorCheck", "6");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            Log.e("errorCheck", "7");
            dos.flush();
            // finish upload...
            Log.e("errorCheck", "8");

            // get response
            InputStream is = conn.getInputStream();

            StringBuilder b = new StringBuilder();
            for (int ch = 0; (ch = is.read()) != -1; ) {
                b.append((char) ch);
            }
            is.close();

            Log.e("Test", "responese : " + b.toString());

//            String C = "ModifySuccess";
//            if(b.toString().contains("ModifySuccess")){
//                Log.e("sfsdf", "ssss");
//                Success();
//            }
            switch (b.toString().trim()) {
                case "ModifySuccess":
                    Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                    Success();
                    break;
                case "NoPrivateKey":
                    Toast.makeText(getApplicationContext(), "등록된 키가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case "DBError":
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    break;
            }


        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }
    } // end of HttpFileUpload()


    public void locationRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        switch (response.trim()) {
                            case "InitialSucess":
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                break;
                            case "NoChildInfo":
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                break;
                            case "JSONError":
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                break;
                            case "DeleteError":
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", "error : " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("childKey", key);
                //  Log.e("volley", "sned"+sendlocation.toString());
                params.put("gps", sendlocation.toString());

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }

    public void getlocationRequest(String url) {

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

                                JSONArray jarray = new JSONArray(response);
                                int size = jarray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject row = jarray.getJSONObject(i);
                                    x = row.getDouble("lati"); // x, y 좌표를 받아옴.
                                    y = row.getDouble("longi");
                                    drawmap(x, y);
                                    sendlocation.append("{" + "\"" + "lati" + "\"" + ":" + "\"" + Double.toString(x) + "\"" + "," + "\"" + "longi" + "\"" + ":" + "\"" + Double.toString(y) + "\"" + "},");

                                }
                            } else if (response.equals(null) == true) {
                                Log.e("volley", response);
                                Toast.makeText(Child_info.this, "Error", Toast.LENGTH_SHORT).show();
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

    public void Success() {
        Intent intent = new Intent(getApplicationContext(), Parents_main.class);
        startActivity(intent);
    }
}





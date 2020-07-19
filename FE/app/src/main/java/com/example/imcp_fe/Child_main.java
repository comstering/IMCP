package com.example.imcp_fe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.GPS.GPStracker;
import com.example.imcp_fe.GPS.Restartservice;
import com.example.imcp_fe.Network.AppHelper;

import java.util.HashMap;
import java.util.Map;

/*아이 메인화면
 * */
public class Child_main extends AppCompatActivity {

    private EditText et_childmain_password;
    private ImageButton ib_childmain_sos;
    private Button btn_childmain_check;
    private SharedPreferences login_preference;
    private String password;
    private Intent intent;
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/childLogin.jsp";
    private String gpsurl = "http://tomcat.comstering.synology.me/IMCP_Server/setChildGPS.jsp";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Restartservice restartservice;
    private long backKeyPressedTime = 0;
    private Toast toast;

/*
* 엑티비티 생성 시 호출
* 사용자 인터페이스 설정
* 버튼 이벤트 설정
* */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_main);

        login_preference = getSharedPreferences("Login", MODE_PRIVATE);

        et_childmain_password = findViewById(R.id.et_childmain_password);
        ib_childmain_sos = findViewById(R.id.ib_childmain_sos);
        btn_childmain_check = findViewById(R.id.btn_childmain_check);

        //volley 호출
        btn_childmain_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = et_childmain_password.getText().toString();
                childRequest(url);


            }
        });
        //
        ib_childmain_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }
        initData();//실시간 위치전송
        Log.e("GPS", login_preference.getString("key", "error"));
    }
//뒤로 가기 버튼 2번 누를 시 종료
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finish();
            toast.cancel();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("restart", "브로드 캐스트 해제");
        unregisterReceiver(restartservice);
    }

    private void initData() {

        Log.e("restart", "설정?");

        restartservice = new Restartservice();
        intent = new Intent(Child_main.this, GPStracker.class);
        IntentFilter intentFilter = new IntentFilter("com.example.imcp_fe.GPS.GPStracker");
        registerReceiver(restartservice, intentFilter);
        startService(intent);
    }
//퍼미션 체크
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(Child_main.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(Child_main.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }
//퍼미션 체크
    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Child_main.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(Child_main.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Child_main.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Child_main.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Child_main.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Child_main.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


//    public String getCurrentAddress(double latitude, double longitude) {
//
//        //지오코더... GPS를 주소로 변환
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//
//        List<Address> addresses;
//
//        try {
//
//            addresses = geocoder.getFromLocation(
//                    latitude,
//                    longitude,
//                    7);
//        } catch (IOException ioException) {
//            //네트워크 문제
//            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
//            return "지오코더 서비스 사용불가";
//        } catch (IllegalArgumentException illegalArgumentException) {
//            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
//            return "잘못된 GPS 좌표";
//
//        }
//
//
//        if (addresses == null || addresses.size() == 0) {
//            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
//            return "주소 미발견";
//
//        }
//
//        Address address = addresses.get(0);
//        return address.getAddressLine(0).toString() + "\n";
//
//    }
//

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Child_main.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

// primarykey 엑티비티로 전환
    public void Success() {
        intent = new Intent(getApplicationContext(), PrimaryKey.class);
        startActivity(intent);
    }
/*
volley 호츌 로그인 성공 여부
고유키와 패스워드를 파라미터로 전송
*/
    public void childRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "ChildLoginSuccess":
                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                Success();
                                break;
                            case "ChildLoginFail":
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("child", response);
                                break;
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("childKey", login_preference.getString("key", ""));
                params.put("password", password);
                return params;
            }
        };


        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }


}

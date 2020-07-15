package com.example.imcp_fe.GPS;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.Child;
import com.example.imcp_fe.Child_main;
import com.example.imcp_fe.Network.AppHelper;
import com.example.imcp_fe.R;

import java.util.HashMap;
import java.util.Map;


public class GPStracker extends Service implements LocationListener {

    private Context mContext;
    Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;//10;
    private static final long MIN_TIME_BW_UPDATES = 1000;  //* 60 * 1;
    protected LocationManager locationManager;
    private String gpsurl = "http://tomcat.comstering.synology.me/IMCP_Server/setChildGPS.jsp";
    private String key;
    private CountDownTimer countDownTimer;
    private final int M = 1000 * 1000;
    private final int C = 1000;
    private SharedPreferences login_preference;
    private Restartservice restartservice;
//    public GPStracker(Context context, String key) {
//        this.key =key;
//        this.mContext = context;
//        getLocation();
////    }

//    public void setinfo(Intent intent,Context context, String key) {
//        this.key = key;
//        this.mContext = context;
//        getLocation();
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("test_ch", "TQ", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.GREEN);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
//            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        key = login_preference.getString("key","");

        mContext = getApplicationContext();
        getLocation();
        intent = new Intent(this, Child_main.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Child_main.class), PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("IMCP")
                        .setContentText("gps 동작중")
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
//                        .setOngoing(true);
        startForeground(1, notificationBuilder.build());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             String channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

      //  notificationManager.notify(0, notificationBuilder.build());

//
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, GPStracker.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification;
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "test_ch")
//                    .setContentIntent(pendingIntent)
//                    .setContentTitle("GPS 동작중")
//                    .setContentText("테스트")
//                    .setSmallIcon(R.drawable.children);
//            notification = builder.build();
//            nm.notify(startId, notification);
//        }


        return super.onStartCommand(intent, flags, startId);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(restartservice);
//    }
//
//    private void initData() {
//        restartservice = new Restartservice();
//        Intent intent = new Intent(GPStracker.this, GPStracker.class);
//        IntentFilter intentFilter = new IntentFilter(".GPS.GPStracker");
//        registerReceiver(restartservice,intentFilter);
//        startService(intent);
//    }

    public void countDownTimer() {
        countDownTimer = new CountDownTimer(M, C) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

            }
        };
    }

    //알림 매니저에 서비스 등록
    private void registerRestartAlarm() {

        Intent intent = new Intent(GPStracker.this, Restartservice.class);
        intent.setAction("ACTION.RESTART.GPStracker");
        PendingIntent sender = PendingIntent.getBroadcast(GPStracker.this, 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1 * 1000;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1 * 1000, sender);
    }

    //알림 매니저에 서비스 해제
    private void unregisterRestartAlarm() {
        Intent intent = new Intent(GPStracker.this, Restartservice.class);
        intent.setAction("ACTION.RESTART.GPStracker");
        PendingIntent sender = PendingIntent.getBroadcast(GPStracker.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(sender);
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                    ;
                } else
                    return null;


                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("@@@", "" + e.toString());
        }

        return location;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {

        GPSRequest(gpsurl, location.getLatitude(), location.getLongitude());
        Toast.makeText(mContext, "위치가 변경되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPStracker.this);
        }
    }

    public void GPSRequest(String url, final double latitude, final double longitude) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "SetGPSSuccess"://toast는 나중에 지워야함
                                Toast.makeText(mContext, "위치가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case "NoChildInfo":
                                Toast.makeText(mContext, "아이정보 없음", Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("childgps", response);
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

                params.put("childKey", key);
                params.put("lati", Double.toString(latitude));
                params.put("longi", Double.toString(longitude));

                return params;
            }
        };


        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(mContext);
        AppHelper.requestQueue.add(request);
    }


}
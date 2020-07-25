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
import com.example.imcp_fe.Parents_main;
import com.example.imcp_fe.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class GPStracker extends Service implements LocationListener {
    public static Intent serviceIntent = null;
    private Context mContext;
    private Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;//10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;//1분
    protected LocationManager locationManager =null;
    private String gpsurl;
    private String key;
    private String id;

    private final int M = 1000 * 1000;
    private final int C = 1000;
    private SharedPreferences login_preference;
    private Restartservice restartservice;
    private Thread mThread;
    private int mCount = 0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceIntent = intent;
        initializeNotification();

        getLocation();


        return super.onStartCommand(intent, flags, startId);
    }

    public void initializeNotification() {

        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        mContext = getApplicationContext();
        key = login_preference.getString("key", "null");
        id = login_preference.getString("id", "null");
        PendingIntent pendingIntent = null;
        if (id.equals("null")) {
            gpsurl = "http://tomcat.comstering.synology.me/IMCP_Server/setChildGPS.jsp";
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Child_main.class), PendingIntent.FLAG_ONE_SHOT);

        } else if (key.equals("null")) {
            gpsurl = "http://tomcat.comstering.synology.me/IMCP_Server/setParentGPS.jsp";
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Parents_main.class), PendingIntent.FLAG_ONE_SHOT);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요.");
        style.setBigContentTitle(null);
        style.setSummaryText("서비스 동작중");
        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);
        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification = builder.build();
        startForeground(1, notification);


//        Intent intent = new Intent(this, Child_main.class);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        String channelId = "Channel ID";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("IMCP")
//                        .setContentText("gps 동작중")
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
////                        .setOngoing(true);
//
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelName = "Channel Name";
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, Restartservice.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        locationManager.removeUpdates(this);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, Restartservice.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }


    public Location getLocation() {
        Log.e("thread", "1");
        try {
            Log.e("thread", "2");
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

    @Override
    public void onLocationChanged(Location location) {

        GPSRequest(gpsurl, location.getLatitude(), location.getLongitude());
        Toast.makeText(mContext, "위치가 변경되었습니다.", Toast.LENGTH_SHORT).show();
        Log.e("gps", location.toString());

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
                                Log.e("gpsgps", response);
                                break;
                            case "NoChildInfo":
                                Toast.makeText(mContext, "아이정보 없음", Toast.LENGTH_SHORT).show();
                                Log.e("gpsgps", response);
                                break;
                            case "DBError":
                                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                                Log.e("gpsgps", response);
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
                if (id.equals("null")) {
                    params.put("childKey", key);
                } else if (key.equals("null")) {
                    params.put("ID", id);
                }

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
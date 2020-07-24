package com.example.imcp_fe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.imcp_fe.Network.AppHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    protected static final String FCM_TAG = "[FCM Service]";
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/setFCMToken.jsp";
    private SharedPreferences login_preference;


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        Log.d(FCM_TAG, "New Token: " + s);
        //volley 추가
        FirebaseRequest(url, s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(FCM_TAG, "onMessageRecived is called");

        if (remoteMessage.getData().size() > 0) {    //  백그라운드
            String messageBody = remoteMessage.getData().get("body");
            String messageTitle = remoteMessage.getData().get("title");
            String messageData = remoteMessage.getData().get("SOS");

            showNotification(messageTitle, messageBody, messageData);
        }
    }

    private void showNotification(String messageTitle, String messageBody, String data) {
        Log.d(FCM_TAG, "Title: " + messageTitle);
        Log.d(FCM_TAG, "Body: " + messageBody);
        Log.d(FCM_TAG, "Data: " + data);
        if (data.equals("on")) {

            Intent intent = new Intent(this, Parents_main.class);//알림을 받고 바로 아이로 넘어가야함 지금은 부모 메인으로 설정
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Channel ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(messageTitle)
                            .setContentText(messageBody)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(FCM_TAG, "Version Check");
                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notificationBuilder.build());

        } else if (data.equals("off")) {
            Intent intent = new Intent(this, Parents_main.class);//알림을 받고 바로 아이로 넘어가야함 지금은 부모 메인으로 설정
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Channel ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(messageTitle)
                            .setContentText("아이를 찾았습니다.")
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(FCM_TAG, "Version Check");
                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notificationBuilder.build());

        }


    }

    public void FirebaseRequest(String url, final String token) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response.trim()) {


                            case "SetFCMSuccess":
                                Log.e("firebase", response);
                                break;
                            case "DBError":
                                Log.e("firebase", response);
                                break;
                            default:
                                Log.e("volley", response);
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
                params.put("ID", login_preference.getString("id",""));
                params.put("token", token);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }

}

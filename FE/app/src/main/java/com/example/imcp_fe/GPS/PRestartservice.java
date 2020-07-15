package com.example.imcp_fe.GPS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PRestartservice extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("service", "진입");

        //서비스 죽일 때 알람으로 다시 서비스 등록
        if(intent.getAction().equals("ACTION.RESTART.GPStracker")){
            Log.e("ser", "1");
            Intent i = new Intent(context, PGPStracker.class);
            context.startService(i);
        }//재부팅 시 서비스 등록
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.e("ser", "2");
            Intent i = new Intent(context, PGPStracker.class);
            context.startService(i);
        }
    }





}

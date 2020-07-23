package com.example.imcp_fe.GPS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Restartservice extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, GPStracker.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, GPStracker.class);
            context.startService(in);
        }
    }

}

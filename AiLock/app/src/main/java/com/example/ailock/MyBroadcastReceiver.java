package com.example.ailock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static String TAG = "MY BROADCAST RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.ACTION_SCREEN_ON.equals(intent.getAction())){
            Intent launchactivity = new Intent(context, CamLockActivity.class);
            launchactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchactivity);
            Log.i(TAG,"Screen On broadcast recieved");
        }
        else if (intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
            Log.i(TAG,"Screen Of broadcast recieved");
        }
    }
}

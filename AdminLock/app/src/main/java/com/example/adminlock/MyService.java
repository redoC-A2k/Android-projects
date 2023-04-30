package com.example.adminlock;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private static final String CHANNEL_ID = "AiLockNotificationChannel";
    private static final int SERVICE_ID = 121;
    MyBroadcastReceiver myBroadcastReceiver = null;
    private static final String TAG = "MY SERVICE";
    public static Handler handler = new Handler();
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Lock service status",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Intent notificationintent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationintent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(getApplicationInfo().name)
                .setContentText("Lock service is working")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(SERVICE_ID, notification);
        if(myBroadcastReceiver == null){
            myBroadcastReceiver = new MyBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myBroadcastReceiver,intentFilter);
        Log.i(TAG,"Foreground service working");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"Foreground Service is destroyed ");
        if (myBroadcastReceiver!=null)
            unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }
}
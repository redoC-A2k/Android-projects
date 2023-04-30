package com.example.jobandro;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {
    String TAG = "MY SERVICE";
    boolean isOn;
    int mRandomNumber;
    private static int MAX = 1000;
    Thread thread ;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"on Create called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"on start command is called");
        Log.i(TAG,"service Current thread : "+Thread.currentThread().getId());
        isOn = true;
        generateRandomNumber();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGeneration();
        Log.i(TAG,"on destroy command is called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void generateRandomNumber(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isOn){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isOn){
                        Log.i(TAG,"Random thread : "+Thread.currentThread().getId());
                        mRandomNumber = new Random().nextInt(MAX);
                        Log.i(TAG, "Random number "+mRandomNumber);
                    }
                    else {
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    private void stopRandomNumberGeneration(){
        isOn = false;
    }
}
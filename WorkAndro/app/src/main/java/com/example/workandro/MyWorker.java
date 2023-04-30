package com.example.workandro;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;

public class MyWorker extends Worker {
    Thread thread;
    boolean isOn = true;
    String TAG = "MY WORKER";
    int mRandomNumber ;
    int MAX=100;
    Context context;
    WorkerParameters workerParameters ;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParameters = workerParams;

        isOn = true;
    }

    @NonNull
    @Override
    public Result doWork() {
        generateRandomNumber();
        return Result.success();
    }

    private void generateRandomNumber(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
//                while (isOn){
                while (i<=10 && !isStopped()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isOn){
//                        Log.i(TAG,"Random thread : "+Thread.currentThread().getId());
                        mRandomNumber = new Random().nextInt(MAX);
                        Log.i(TAG, "Random number "+mRandomNumber);
                        i++;
                    }
                    else {
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG,"Worker has stopped");
    }
}

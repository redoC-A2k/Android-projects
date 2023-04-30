package com.example.jobandro;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private static boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG,"Job scheduler job started");
        doBackgroundWork(jobParameters);
        return true;
    }
    private void doBackgroundWork(JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<30; i++){
                    Log.d(TAG, "run: "+i);
                    if (jobCancelled){
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG,"Job Finished");
                jobFinished(params,false);

            }
        }).start();
    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG,"Job cancelled before completion");
        jobCancelled = true;
        return false;
    }
}

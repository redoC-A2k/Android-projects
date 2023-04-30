package com.example.myjobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG,"onStartJob: Job Started")
        doWorkBackground(jobParameters);
        return true;
    }

    private void doWorkBackground(JobParameters jobParameters) {
        new Thre
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}

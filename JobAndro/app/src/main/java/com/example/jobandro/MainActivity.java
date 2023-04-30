package com.example.jobandro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.jobandro.ExampleJobService;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button startservicebtn;
    Intent myServiceIntent;
    Button stopservicebtn;
    String TAG = "MAIN ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myServiceIntent = new Intent(getApplicationContext(), MyService.class);

        startservicebtn = findViewById(R.id.startservicebtn);
        startservicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "current Thread : "+Thread.currentThread().getId());
//                startService(myServiceIntent);
                scheduleJob(view);
            }
        });
        stopservicebtn = findViewById(R.id.stopservicebtn);
        stopservicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stopService(myServiceIntent);
                cancelJob(view);
            }
        });
    }

    public void scheduleJob(View v){
        ComponentName componentName = new ComponentName(this,ExampleJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(101, componentName)
//                .setRequiresCharging(true)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "Job scheduled");
        }
        else{
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(101);
        Log.d(TAG,"Job cancelled");
    }
}
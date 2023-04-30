package com.example.workandro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button startWorkBtn;
    Button stopWorkBtn;
    private WorkManager workManager;
    private WorkRequest workRequest;
    private boolean mStopLoop ;
    String TAG = "MAIN ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startWorkBtn = findViewById(R.id.startworkbtn);
        stopWorkBtn = findViewById(R.id.stopworkbtn);

        Log.i(TAG,"Main activity thread - "+Thread.currentThread().getId());

        startWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWork();
            }
        });
        stopWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWork();
            }
        });
        workManager = WorkManager.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            workRequest = new PeriodicWorkRequest
                    .Builder(
                            MyWorker.class,
                    15,
                            TimeUnit.MINUTES
                    ).build();
        }
    }

    private void startWork(){
        mStopLoop = true;
        workManager.enqueue(workRequest);
    }

    private void stopWork(){
        workManager.cancelWorkById(workRequest.getId());
        Log.i(TAG,"work manager is stopped");
    }

}
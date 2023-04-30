package com.example.adminlock;

import static com.example.adminlock.MainActivity.prefsDropKey;
import static com.example.adminlock.MainActivity.prefsNumQueKey;
import static com.example.adminlock.MainActivity.prefsTimeMinKey;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LockActivity extends AppCompatActivity {
    Intent foregroundServiceIntent ;
    String TAG = "LOCK ACTIVITY";
    boolean isOnMathsActivity = false;
    Button screenLockBtn,screenMathsBtn,screenUnlockBtn;
    DevicePolicyManager deviceManager ;
    Handler collapseNotificationHandler;
    // To keep track of activity's window focus
    boolean currentFocus;
    // To keep track of activity's foreground/background status
    boolean isPaused;

    SharedPreferences prefs ;
    int numQue, timeMin ;
    String difficultyLevel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock);

        deviceManager = (DevicePolicyManager)
                getSystemService(Context. DEVICE_POLICY_SERVICE ) ;
        prefs = getSharedPreferences("AdminLock",MODE_PRIVATE);
        numQue = prefs.getInt(prefsNumQueKey,4);
        timeMin = prefs.getInt(prefsTimeMinKey, 15);
        difficultyLevel = prefs.getString(prefsDropKey, "Easy");


        screenLockBtn = findViewById(R.id.screenLockBtn);
        screenMathsBtn = findViewById(R.id.screenMathsBtn);
        screenUnlockBtn = findViewById(R.id.screenUnlockBtn);

        screenLockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Time in mins is "+timeMin);
                MyService.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deviceManager.lockNow();
                    }
                },timeMin*1000);
                finish();
            }
        });

        screenMathsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LockActivity.this, QueActivity.class);
                startActivity(intent);
            }
        });

        screenUnlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Activity's been resumed
        Log.i(TAG,"Activity has been resumed");
        isPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Activity's been paused
        Log.i(TAG,"Activity has been paused");
        isPaused = true;
    }

    @Override
    protected void onUserLeaveHint() {
//        deviceManager.lockNow();
        Log.i(TAG,"User has leaved");
        super.onUserLeaveHint();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        currentFocus = hasFocus;
        if (!hasFocus) {
//            deviceManager.lockNow();
        }
        Log.i(TAG,"On window focus changed");

    }
}
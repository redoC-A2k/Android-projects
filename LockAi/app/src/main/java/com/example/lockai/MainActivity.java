package com.example.lockai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MyReceiver myReceiver = new MyReceiver();
    String TAG="MAIN_ACTIVITY";
    int ALARM_REQ_CODE=102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button unlockbtn = findViewById(R.id.unlockbtn);
        Button lockbtn = findViewById(R.id.lockbtn);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("AppData",MODE_PRIVATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);

        unlockbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 boolean settimer = sharedPreferences.getBoolean("settimeer",true);
                 if (settimer){
                     Log.i(TAG,"Alarm is set");
                     int time = sharedPreferences.getInt("mins",1);
                     Intent ibroadcast = new Intent(MainActivity.this,MyReceiver.class);
                 }
                 finish();
//                 minimizeApp();
             }
         });
        registerReceiver(myReceiver,filter);

        lockbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
             }
         });
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
//        lock.disableKeyguard();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent intent = new Intent(, MainActivity.class);
//        startActivity(intent);
        unregisterReceiver(myReceiver);
    }
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }


    @Override
    public void onBackPressed() {
        // We doing this too stop user from exiting app, normally.
//         super.onBackPressed();
    }

}